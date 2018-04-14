package org.thebubbleindex.xap.polling;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.jms.Topic;
import javax.jms.TopicConnection;
import javax.jms.TopicPublisher;
import javax.jms.TopicSession;
import javax.jms.TopicSubscriber;

import org.openspaces.core.GigaSpace;
import org.openspaces.core.GigaSpaceConfigurer;
import org.openspaces.core.space.UrlSpaceConfigurer;
import org.openspaces.events.EventTemplate;
import org.openspaces.events.adapter.SpaceDataEvent;
import org.openspaces.events.polling.Polling;
import org.openspaces.events.polling.ReceiveHandler;
import org.openspaces.events.polling.receive.ReceiveOperationHandler;
import org.openspaces.events.polling.receive.SingleTakeReceiveOperationHandler;
import org.thebubbleindex.computegrid.XAPBubbleIndexComputeGrid;
import org.thebubbleindex.computegrid.XAPCounter;
import org.thebubbleindex.driver.BubbleIndexGridTask;
import org.thebubbleindex.runnable.RunContext;

import com.j_spaces.jms.utils.GSJMSAdmin;

/**
 * Only 1 concurrent consumer. Create multiple polling processes on each machine
 * if needed.
 * 
 * @author thebubbleindex
 *
 */
@Polling(concurrentConsumers = 1, maxConcurrentConsumers = 1)
public class BubbleIndexTaskWorkerPollingContainer {
	private final Logger logger = Logger.getLogger(BubbleIndexTaskWorkerPollingContainer.class.getName());
	private final AtomicInteger numberOfTasks = new AtomicInteger();
	private final GigaSpace completedTaskSpace;
	private final TopicSubscriber stopMessageTopicSubscriber;
	private final TopicPublisher messageTopicPublisher;
	private final TopicSession messageTopicSession;
	private final XAPCounter pendingTasksInProcessCounter;
	private RunContext runContext;

	public BubbleIndexTaskWorkerPollingContainer(final String lookupServiceName) throws JMSException {
		completedTaskSpace = new GigaSpaceConfigurer(new UrlSpaceConfigurer(
				"jini://" + lookupServiceName + "/*/" + XAPBubbleIndexComputeGrid.COMPLETED_TASK_SPACE_NAME))
						.gigaSpace();
		final GigaSpace pendingTaskSpace = new GigaSpaceConfigurer(new UrlSpaceConfigurer(
				"jini://" + lookupServiceName + "/*/" + XAPBubbleIndexComputeGrid.PENDING_TASK_SPACE_NAME)).gigaSpace();

		final GSJMSAdmin admin = GSJMSAdmin.getInstance();
		final Topic stopMessageTopic = admin.getTopic(XAPBubbleIndexComputeGrid.STOP_MESSAGE_TOPIC_NAME);
		final Topic taskMessageTopic = admin.getTopic(XAPBubbleIndexComputeGrid.TASK_MESSAGE_TOPIC_NAME);

		final TopicConnection stopMessageTopicConnection = admin.getTopicConnectionFactory(pendingTaskSpace.getSpace())
				.createTopicConnection();
		stopMessageTopicConnection.setClientID(String.valueOf(UUID.randomUUID().getMostSignificantBits()));
		final TopicSession stopMessageTopicSession = stopMessageTopicConnection.createTopicSession(false,
				Session.AUTO_ACKNOWLEDGE);
		stopMessageTopicSubscriber = stopMessageTopicSession.createSubscriber(stopMessageTopic);

		stopMessageTopicSubscriber.setMessageListener(new MessageListener() {
			public void onMessage(final Message msg) {
				if (msg instanceof TextMessage) {
					final TextMessage txtMsg = (TextMessage) msg;

					try {
						if (runContext != null && txtMsg.getText()
								.equalsIgnoreCase(XAPBubbleIndexComputeGrid.STOP_ALL_TASKS_MESSAGE)) {
							runContext.setStop(true);
						}
					} catch (final JMSException ex) {
						System.out.println(ex);
					}
				}
			}
		});

		final TopicConnection messageTopicConnection = admin.getTopicConnectionFactory(pendingTaskSpace.getSpace())
				.createTopicConnection();
		messageTopicConnection.setClientID(String.valueOf(UUID.randomUUID().getMostSignificantBits()));
		messageTopicSession = messageTopicConnection.createTopicSession(false, Session.AUTO_ACKNOWLEDGE);
		messageTopicPublisher = messageTopicSession.createPublisher(taskMessageTopic);

		messageTopicConnection.start();
		stopMessageTopicConnection.start();

		// initialize the counter if it is not already in the space
		pendingTasksInProcessCounter = new XAPCounter(pendingTaskSpace, "id",
				XAPBubbleIndexComputeGrid.PENDING_TASKS_IN_PROCESS_COUNTER_NAME, 0);

		logger.info("Started BubbleIndexTaskWorkerPollingContainer...");
	}

	@EventTemplate
	public BubbleIndexGridTask unprocessedData() {
		return new BubbleIndexGridTask();
	}

	@SpaceDataEvent
	public void processTask(final BubbleIndexGridTask task) {
		logger.log(Level.INFO, "BubbleIndexTaskWorkerPollingContainer PROCESSING: {0}. Total processed: {1}",
				new Object[] { task.getId(), numberOfTasks.incrementAndGet() });
		pendingTasksInProcessCounter.increment(XAPBubbleIndexComputeGrid.PENDING_TASKS_IN_PROCESS_COUNTER_NAME, 1);
		runContext = task.getRunContext();

		try {
			task.runBubbleIndex(null);

			if (!runContext.isStop())
				completedTaskSpace.write(task);
		} catch (final Exception ex) {
			logger.log(Level.SEVERE, "BubbleIndexGridTask Error: {0}", ex);
		}

		final List<String> textOutputs = task.getGUITextOutputsFromComputeGrid();
		for (final String textOutput : textOutputs) {
			try {
				final TextMessage msg = messageTopicSession
						.createTextMessage(task.getCategoryName() + ", " + task.getSelectionName() + ": " + textOutput);
				messageTopicPublisher.publish(msg);
			} catch (final JMSException ex) {
				logger.log(Level.SEVERE, null, ex);
			}
		}

		runContext = null;
		pendingTasksInProcessCounter.decrement(XAPBubbleIndexComputeGrid.PENDING_TASKS_IN_PROCESS_COUNTER_NAME, 1);
	}

	@ReceiveHandler
	public ReceiveOperationHandler receiveHandler() {
		final SingleTakeReceiveOperationHandler receiveHandler = new SingleTakeReceiveOperationHandler();
		receiveHandler.setNonBlocking(true);
		receiveHandler.setNonBlockingFactor(100);
		return receiveHandler;
	}
}
