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
import org.openspaces.core.space.SpaceProxyConfigurer;
import org.thebubbleindex.driver.BubbleIndexGridTask;
import org.thebubbleindex.logging.Logs;
import org.thebubbleindex.swing.GUI;

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
	private final String PENDING_TASK_SPACE_NAME = "pendingTaskSpace";
	private final String COMPLETED_TASK_SPACE_NAME = "completedTaskSpace";
	private final String stopMessageTopicName = "stopMessageTopic";
	private final String taskMessageTopicName = "taskMessageTopic";

	// Stop message topics
	private final TopicConnection stopMessagePendingSpaceTopicConnection;
	private final TopicSession stopMessagePendingSpaceTopicSession;
	private final TopicPublisher stopMessagePendingSpaceTopicPublisher;

	private final TopicConnection stopMessageCompletedSpaceTopicConnection;
	private final TopicSession stopMessageCompletedSpaceTopicSession;
	private final TopicPublisher stopMessageCompletedSpaceTopicPublisher;

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

	public XAPBubbleIndexComputeGrid() throws JMSException {
		pendingTaskSpace = new GigaSpaceConfigurer(new SpaceProxyConfigurer(PENDING_TASK_SPACE_NAME)).gigaSpace();
		completedTaskSpace = new GigaSpaceConfigurer(new SpaceProxyConfigurer(COMPLETED_TASK_SPACE_NAME)).gigaSpace();

		final GSJMSAdmin admin = GSJMSAdmin.getInstance();
		final Topic stopMessageTopic = admin.getTopic(stopMessageTopicName);
		final Topic taskMessageTopic = admin.getTopic(taskMessageTopicName);

		// create stop message topic connection for the PENDING_TASK_SPACE_NAME
		stopMessagePendingSpaceTopicConnection = admin.getTopicConnectionFactory(pendingTaskSpace.getSpace())
				.createTopicConnection();
		stopMessagePendingSpaceTopicSession = stopMessagePendingSpaceTopicConnection.createTopicSession(false,
				Session.AUTO_ACKNOWLEDGE);
		stopMessagePendingSpaceTopicPublisher = stopMessagePendingSpaceTopicSession.createPublisher(stopMessageTopic);

		// create stop message topic connection for the
		// COMPLETED_TASK_SPACE_NAME
		stopMessageCompletedSpaceTopicConnection = admin.getTopicConnectionFactory(completedTaskSpace.getSpace())
				.createTopicConnection();
		stopMessageCompletedSpaceTopicSession = stopMessageCompletedSpaceTopicConnection.createTopicSession(false,
				Session.AUTO_ACKNOWLEDGE);
		stopMessageCompletedSpaceTopicPublisher = stopMessageCompletedSpaceTopicSession
				.createPublisher(stopMessageTopic);

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
		taskMessageCompletedSpaceTopicConnection = admin.getTopicConnectionFactory(pendingTaskSpace.getSpace())
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
		stopMessageCompletedSpaceTopicConnection.start();
		taskMessagePendingSpaceTopicConnection.start();
		taskMessageCompletedSpaceTopicConnection.start();
	}

	@Override
	public void executeBubbleIndexTasks() {
		// no direct execution in this version, the polling containers will look
		// and execute the tasks
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
			stopMessageCompletedSpaceTopicConnection.close();
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
		try {
			final TextMessage msg = stopMessageCompletedSpaceTopicSession.createTextMessage("STOP_ALL_TASKS");
			stopMessageCompletedSpaceTopicPublisher.send(msg);
		} catch (final JMSException ex) {
			Logs.myLogger.log(Level.ERROR, ex);
		}

		try {
			final TextMessage msg = stopMessagePendingSpaceTopicSession.createTextMessage("STOP_ALL_TASKS");
			stopMessagePendingSpaceTopicPublisher.send(msg);
		} catch (final JMSException ex) {
			Logs.myLogger.log(Level.ERROR, ex);
		}
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
