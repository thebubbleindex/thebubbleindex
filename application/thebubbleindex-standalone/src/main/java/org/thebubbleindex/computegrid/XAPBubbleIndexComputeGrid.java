package org.thebubbleindex.computegrid;

import java.util.concurrent.atomic.AtomicInteger;

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
import javax.swing.SwingUtilities;

import org.apache.logging.log4j.Level;
import org.openspaces.core.GigaSpace;
import org.openspaces.core.GigaSpaceConfigurer;
import org.openspaces.core.space.UrlSpaceConfigurer;
import org.thebubbleindex.driver.BubbleIndexGridTask;
import org.thebubbleindex.logging.Logs;
import org.thebubbleindex.swing.GUI;

import com.j_spaces.core.client.SQLQuery;
import com.j_spaces.jms.utils.GSJMSAdmin;

import gnu.trove.map.hash.TIntObjectHashMap;

/**
 * The idea behind the XAP GigaSpace compute grid is that the application
 * functions as a feeder of tasks and a listener on a message topic. Thus, there
 * will be polling containers for task execution which can be remote, i.e. on
 * any machine. And there is another space and set of polling containers which
 * handle the completed tasks - these must be on the machine with the csv files.
 * 
 * @author thebubbleindex
 *
 */
public class XAPBubbleIndexComputeGrid implements BubbleIndexComputeGrid {
	public static final String PENDING_TASK_SPACE_NAME = "pendingTaskSpace";
	public static final String COMPLETED_TASK_SPACE_NAME = "completedTaskSpace";
	public static final String STOP_MESSAGE_TOPIC_NAME = "stopMessageTopic";
	public static final String TASK_MESSAGE_TOPIC_NAME = "taskMessageTopic";
	public static final String STOP_ALL_TASKS_MESSAGE = "STOP_ALL_TASKS";
	public static final String PENDING_TASKS_IN_PROCESS_COUNTER_NAME = "tasksInProcess";
	private final String lookupServiceName;

	// Stop message topics
	private final TopicConnection stopMessagePendingSpaceTopicConnection;
	private final TopicSession stopMessagePendingSpaceTopicSession;
	private final TopicPublisher stopMessagePendingSpaceTopicPublisher;

	// Info message topics
	private final TopicSession taskMessagePendingSpaceTopicSession;
	private final TopicSubscriber taskMessagePendingSpaceTopicSubscriber;
	private final TopicConnection taskMessagePendingSpaceTopicConnection;

	private final TopicSession taskMessageCompletedSpaceTopicSession;
	private final TopicSubscriber taskMessageCompletedSpaceTopicSubscriber;
	private final TopicConnection taskMessageCompletedSpaceTopicConnection;

	// spaces
	private final GigaSpace pendingTaskSpace;
	private final GigaSpace completedTaskSpace;

	private final AtomicInteger numberOfLines = new AtomicInteger();
	private final XAPCounter pendingTasksInProcessCounter;

	private boolean stopButtonPressed;

	public XAPBubbleIndexComputeGrid(final String lookupServiceName) throws JMSException {
		this.lookupServiceName = lookupServiceName;
		pendingTaskSpace = new GigaSpaceConfigurer(
				new UrlSpaceConfigurer("jini://" + this.lookupServiceName + "/*/" + PENDING_TASK_SPACE_NAME))
						.gigaSpace();
		completedTaskSpace = new GigaSpaceConfigurer(
				new UrlSpaceConfigurer("jini://" + this.lookupServiceName + "/*/" + COMPLETED_TASK_SPACE_NAME))
						.gigaSpace();

		final GSJMSAdmin admin = GSJMSAdmin.getInstance();
		final Topic stopMessageTopic = admin.getTopic(STOP_MESSAGE_TOPIC_NAME);
		final Topic taskMessageTopic = admin.getTopic(TASK_MESSAGE_TOPIC_NAME);

		// create stop message topic connection for the PENDING_TASK_SPACE_NAME
		stopMessagePendingSpaceTopicConnection = admin.getTopicConnectionFactory(pendingTaskSpace.getSpace())
				.createTopicConnection();
		stopMessagePendingSpaceTopicSession = stopMessagePendingSpaceTopicConnection.createTopicSession(false,
				Session.AUTO_ACKNOWLEDGE);
		stopMessagePendingSpaceTopicPublisher = stopMessagePendingSpaceTopicSession.createPublisher(stopMessageTopic);

		// create info message topic connection for the PENDING_TASK_SPACE_NAME
		taskMessagePendingSpaceTopicConnection = admin.getTopicConnectionFactory(pendingTaskSpace.getSpace())
				.createTopicConnection();
		taskMessagePendingSpaceTopicSession = taskMessagePendingSpaceTopicConnection.createTopicSession(false,
				Session.AUTO_ACKNOWLEDGE);
		taskMessagePendingSpaceTopicSubscriber = taskMessagePendingSpaceTopicSession.createSubscriber(taskMessageTopic);

		taskMessagePendingSpaceTopicSubscriber.setMessageListener(new MessageListener() {
			public void onMessage(final Message msg) {
				if (msg instanceof TextMessage) {
					final TextMessage txtMsg = (TextMessage) msg;
					try {
						displayGridMessageOutput(txtMsg.getText());
					} catch (final JMSException ex) {
						Logs.myLogger.log(Level.ERROR, ex);
					}
				}
			}
		});

		// create info message topic connection for the
		// COMPLETED_TASK_SPACE_NAME
		taskMessageCompletedSpaceTopicConnection = admin.getTopicConnectionFactory(completedTaskSpace.getSpace())
				.createTopicConnection();
		taskMessageCompletedSpaceTopicSession = taskMessageCompletedSpaceTopicConnection.createTopicSession(false,
				Session.AUTO_ACKNOWLEDGE);
		taskMessageCompletedSpaceTopicSubscriber = taskMessageCompletedSpaceTopicSession
				.createSubscriber(taskMessageTopic);

		taskMessageCompletedSpaceTopicSubscriber.setMessageListener(new MessageListener() {
			public void onMessage(final Message msg) {
				if (msg instanceof TextMessage) {
					final TextMessage txtMsg = (TextMessage) msg;
					try {
						displayGridMessageOutput(txtMsg.getText());
					} catch (final JMSException ex) {
						Logs.myLogger.log(Level.ERROR, ex);
					}
				}
			}
		});

		stopMessagePendingSpaceTopicConnection.start();
		taskMessagePendingSpaceTopicConnection.start();
		taskMessageCompletedSpaceTopicConnection.start();

		pendingTasksInProcessCounter = new XAPCounter(pendingTaskSpace, "id", PENDING_TASKS_IN_PROCESS_COUNTER_NAME, 0);
	}

	/**
	 * no direct execution in this version, the polling containers will look and
	 * execute the tasks, all this method does is wait for all tasks to complete
	 */
	@Override
	public void executeBubbleIndexTasks() {
		stopButtonPressed = false;

		int pendingTaskCount = pendingTaskSpace.count(new SQLQuery<BubbleIndexGridTask>(BubbleIndexGridTask.class, ""));
		int completedTaskCount;
		int tasksInProcess = 1;

		displayGridMessageOutput(
				"Executing tasks... Currently " + pendingTaskCount + " tasks in the pending task space.");

		do {
			for (int i = 0; i < 50; i++) {
				if (stopButtonPressed) break;
				
				try {
					Thread.sleep(100);
				} catch (final InterruptedException ex) {
					Logs.myLogger.log(Level.ERROR, ex);
				}
			}

			try {
				tasksInProcess = pendingTasksInProcessCounter.get(PENDING_TASKS_IN_PROCESS_COUNTER_NAME).intValue();
			} catch (final Exception ex) {
				Logs.myLogger.log(Level.ERROR, ex);
			}

			pendingTaskCount = pendingTaskSpace.count(new SQLQuery<BubbleIndexGridTask>(BubbleIndexGridTask.class, ""));
			completedTaskCount = completedTaskSpace
					.count(new SQLQuery<BubbleIndexGridTask>(BubbleIndexGridTask.class, ""));

			displayGridMessageOutput("Executing tasks... Currently " + pendingTaskCount
					+ " tasks in the pending task space. With " + tasksInProcess + " tasks in progress.");
		} while (!stopButtonPressed && (pendingTaskCount > 0 || completedTaskCount > 0 || tasksInProcess > 0));		
	}

	@Override
	public void addBubbleIndexTask(final Integer key, final BubbleIndexGridTask bubbleIndexTask) {
		pendingTaskSpace.write(bubbleIndexTask);
	}

	@Override
	public void addAllBubbleIndexTasks(final TIntObjectHashMap<BubbleIndexGridTask> bubbleIndexTasks) {
		pendingTaskSpace.writeMultiple(bubbleIndexTasks.values());
	}

	@Override
	public void shutdownGrid() {
		// nothing for the client proxy to do except disconnect
		try {
			stopMessagePendingSpaceTopicConnection.close();
		} catch (final JMSException ex) {
			Logs.myLogger.log(Level.ERROR, ex);
		}

		try {
			taskMessagePendingSpaceTopicConnection.close();
		} catch (final JMSException ex) {
			Logs.myLogger.log(Level.ERROR, ex);
		}

		try {
			taskMessageCompletedSpaceTopicConnection.close();
		} catch (final JMSException ex) {
			Logs.myLogger.log(Level.ERROR, ex);
		}

		pendingTaskSpace.clear(null);
	}

	@Override
	public String about() {
		final String pendingTaskSpaceName = pendingTaskSpace.getName();
		final String completedTaskSpaceName = completedTaskSpace.getName();

		return "Connected to pending task space: " + pendingTaskSpaceName + " and also to completed task space: "
				+ completedTaskSpaceName;
	}

	@Override
	public void triggerStopAllTasksMessage() {
		displayGridMessageOutput("Stopping all tasks, please wait...");
		stopButtonPressed = true;

		try {
			final TextMessage msg = stopMessagePendingSpaceTopicSession.createTextMessage(STOP_ALL_TASKS_MESSAGE);
			stopMessagePendingSpaceTopicPublisher.send(msg);
		} catch (final JMSException ex) {
			Logs.myLogger.log(Level.ERROR, ex);
		}

		pendingTaskSpace.clear(new SQLQuery<BubbleIndexGridTask>(BubbleIndexGridTask.class, ""));
	}

	/**
	 * 
	 * @param displayText
	 * @param resetTextArea
	 */
	private void displayGridMessageOutput(final String displayText) {
		Logs.myLogger.log(Level.INFO, displayText);

		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				if (numberOfLines.incrementAndGet() > 200) {
					GUI.OutputText.setText("");
					numberOfLines.set(0);
				}
				GUI.OutputText.append(displayText + "\n");
			}
		});
	}
}
