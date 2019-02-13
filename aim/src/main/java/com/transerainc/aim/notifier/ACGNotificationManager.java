/**
 * 
 */
package com.transerainc.aim.notifier;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.transerainc.aim.conf.ConfigurationManager;
import com.transerainc.aim.pojo.ACGNotification;
import com.transerainc.aim.runtime.ActiveAgentManager;
import com.transerainc.tam.pool.TAMThreadPool;

/**
 * @author <a href="mailto:ramesh.bobba@transerainc.com">Ramesh Bobba</a>
 * @version $Rivision:$
 */
public class ACGNotificationManager extends Thread {
	private static Logger lgr = Logger.getLogger(ACGNotificationManager.class
			.getName());
	private LinkedBlockingQueue<ACGNotification> nsmQueue;
	private TAMThreadPool notifPool;
	private int maxRetryCount;
	private int maxQueueSize;
	private int errorSleepTime = 500;
	private Object mutex = new Object();

	public ACGNotificationManager(ConfigurationManager cmgr,
			ActiveAgentManager aaMgr) {
		maxRetryCount = cmgr.getAgentInformationManager().getAcgMaxRetryCount();

		int poolSize = cmgr.getAgentInformationManager()
				.getAcgNotifierPoolSize();

		maxQueueSize = cmgr.getAgentInformationManager()
				.getAcgNotificationMaxQueueSize();

		errorSleepTime = cmgr.getAgentInformationManager()
				.getAcgNotificationFailureInterval();

		if (lgr.isLoggable(Level.CONFIG)) {
			lgr.config("Creating ACG Notification Thread Pool of size: "
					+ poolSize);
		}

		nsmQueue = new LinkedBlockingQueue<ACGNotification>();
		notifPool = new TAMThreadPool(poolSize);
	}
	
	public int getCurrentQueueSize() {
		return nsmQueue.size();
	}

	public void sendNotification(ACGNotification notif) {
		try {
			synchronized (mutex) {
				if (nsmQueue.size() >= maxQueueSize) {
					try {
						ACGNotification removeNotif = nsmQueue.remove();
						lgr.warning("Removed Node: " + removeNotif.toString());
					} catch (Exception ex) {
					}
				}
				nsmQueue.put(notif);
			}
		} catch (InterruptedException e) {
			lgr.log(Level.SEVERE, "Error while sending notification to ACGs: "
					+ notif, e);
		}
	}

	public void run() {
		lgr.info("Starting the Notification Manager thread.");

		while (true) {
			String strMsg = null;
			try {
				ACGNotification msg = nsmQueue.take();

				if (lgr.isLoggable(Level.FINE)) {
					lgr.fine("Picked up request: " + msg);
				}

				ACGNotifier notif = createACGNotifier(msg);

				notifPool.execute(notif);
			} catch (Exception e) {
				lgr.log(Level.SEVERE,
						"Error while notifying ACGs with message " + strMsg, e);
			}
		}
	}
	
	public ACGNotifier createACGNotifier(ACGNotification msg){
		ACGNotifier notif = new ACGNotifier(msg, maxRetryCount,
				errorSleepTime);
		return notif;
	}

	/**
	 * @return the maxQueueSize
	 */
	public int getMaxQueueSize() {
		return maxQueueSize;
	}	

}
