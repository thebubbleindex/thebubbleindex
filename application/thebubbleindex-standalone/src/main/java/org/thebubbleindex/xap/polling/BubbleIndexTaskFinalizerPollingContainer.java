package org.thebubbleindex.xap.polling;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.jms.JMSException;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.jms.Topic;
import javax.jms.TopicConnection;
import javax.jms.TopicPublisher;
import javax.jms.TopicSession;

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
import org.thebubbleindex.driver.BubbleIndexGridTask;

import com.j_spaces.core.client.SQLQuery;
import com.j_spaces.jms.utils.GSJMSAdmin;

@Polling(concurrentConsumers = 1, maxConcurrentConsumers = 4)
public class BubbleIndexTaskFinalizerPollingContainer {
	private final Logger logger = Logger.getLogger(BubbleIndexTaskFinalizerPollingContainer.class.getName());
	private final AtomicInteger numberOfTasks = new AtomicInteger();
	private final TopicSession messageTopicSession;
	private final TopicPublisher messageTopicPublisher;

	public BubbleIndexTaskFinalizerPollingContainer(final String lookupServiceName) throws JMSException {
		final GigaSpace completedTaskSpace = new GigaSpaceConfigurer(new UrlSpaceConfigurer(
				"jini://" + lookupServiceName + "/*/" + XAPBubbleIndexComputeGrid.COMPLETED_TASK_SPACE_NAME))
						.gigaSpace();
		final GSJMSAdmin admin = GSJMSAdmin.getInstance();
		final Topic taskMessageTopic = admin.getTopic(XAPBubbleIndexComputeGrid.TASK_MESSAGE_TOPIC_NAME);

		final TopicConnection messageTopicConnection = admin.getTopicConnectionFactory(completedTaskSpace.getSpace())
				.createTopicConnection();
		messageTopicSession = messageTopicConnection.createTopicSession(false, Session.AUTO_ACKNOWLEDGE);
		messageTopicPublisher = messageTopicSession.createPublisher(taskMessageTopic);

		messageTopicConnection.start();
	}

	@EventTemplate
	public SQLQuery<BubbleIndexGridTask> unprocessedData() {
		return new SQLQuery<BubbleIndexGridTask>(BubbleIndexGridTask.class, "");
	}

	@SpaceDataEvent
	public void processTask(final BubbleIndexGridTask task) {
		logger.log(Level.INFO, "BubbleIndexTaskFinalizerPollingContainer PROCESSING: {0}. Total processed: {1}",
				new Object[] { task.getId(), numberOfTasks.incrementAndGet() });

		task.getGUITextOutputsFromComputeGrid().clear();

		try {
			task.outputResults(null);
		} catch (final Exception ex) {
			logger.log(Level.SEVERE, "BubbleIndexGridTask Error: {0}", ex);
		}

		final List<String> textOutputs = task.getGUITextOutputsFromComputeGrid();
		for (final String textOutput : textOutputs) {
			try {
				final TextMessage msg = messageTopicSession
						.createTextMessage(task.getCategoryName() + ", " + task.getSelectionName() + ": " + textOutput);
				messageTopicPublisher.send(msg);
			} catch (final JMSException ex) {
				logger.log(Level.SEVERE, null, ex);
			}
		}
	}

	@ReceiveHandler
	public ReceiveOperationHandler receiveHandler() {
		final SingleTakeReceiveOperationHandler receiveHandler = new SingleTakeReceiveOperationHandler();
		receiveHandler.setNonBlocking(true);
		receiveHandler.setNonBlockingFactor(100);
		return receiveHandler;
	}
}
