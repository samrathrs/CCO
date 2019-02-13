/**
 * 
 */
package com.transerainc.aim.notifier;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.transerainc.aim.pojo.ACGNotification;
import com.transerainc.aim.snmp.TrapManager;
import com.transerainc.aim.system.AgentInformationManagerSystem;
import com.transerainc.tam.util.HttpStatus;
import com.transerainc.tam.util.HttpUtil;

/**
 * @author <a href="mailto:ramesh.bobba@transerainc.com">Ramesh Bobba</a>
 * @version $Rivision:$
 */
public class ACGNotifier implements Runnable {
	private static Logger lgr = Logger.getLogger(ACGNotifier.class.getName());
	private ACGNotification notification;
	private int maxRetryCount;
	private int errorSleepTime;

	public ACGNotifier(ACGNotification notif, int maxRetryCount,
			int errorSleepTime) {
		this.maxRetryCount = maxRetryCount;
		this.notification = notif;
		this.errorSleepTime = errorSleepTime;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Runnable#run()
	 */
	public void run() {
		notifyACGs();
	}
	
	
	public void notifyACGs(){
		String message = null;
		String url = null;
		try {
			// get the act notification context
			message = notification.getMessage();
			List<String> acgUrlList = notification.getAcgUrls();
			if (acgUrlList == null || acgUrlList.size() == 0) {
				if (lgr.isLoggable(Level.INFO)) {
					lgr.info("Null or Empty ACG URL List. Discarding message: "
							+ message);
				}
				return;
			}
			for (String acgUrl : acgUrlList) {
				url = acgUrl;

				if (lgr.isLoggable(Level.INFO)) {
					lgr.info("Sending message " + message + " to ACG at "
							+ acgUrl);
				}

				for (int retryCount = 0; retryCount < maxRetryCount; retryCount++) {
					try {
						HttpStatus status = null;
						if (notification.getMethod() == ACGNotification.METHOD_POST) {
							status = HttpUtil.doHttpXMLPostReturningStatus(
									acgUrl, message);
						} else {
							status = HttpUtil.doGet(acgUrl, 5000, acgUrl);
						}

						if (lgr.isLoggable(Level.INFO)) {
							lgr.info("ACG Returned: " + status);
						}

						break;
					} catch (Exception ex) {
						lgr.log(Level.SEVERE,
								"Error while trying to send notification to "
										+ acgUrl + ": " + message
										+ "(Retry Count: " + retryCount + ")",
								ex);

						// Raise a trap
						TrapManager trapMgr = (TrapManager) AgentInformationManagerSystem
								.findBean("trapManager");
						trapMgr.sendAcgNotificationFailed(acgUrl, message, ex
								.getMessage(), retryCount);

						// delay between re-notifying
						// Thread.sleep(errorSleepTime);
					}
				}
			}
		} catch (Exception e) {
			lgr.log(Level.SEVERE, "Error while sending notification " + message
					+ " to " + url, e);

			// Raise a trap
			TrapManager trapMgr = (TrapManager) AgentInformationManagerSystem
					.findBean("trapManager");
			trapMgr.sendAcgNotificationFailed(url, message, e.getMessage(), -1);
		}
	}
}
