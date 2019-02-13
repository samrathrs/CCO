/**
 * 
 */
package com.transerainc.aim.notifier;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.transerainc.aim.conf.ConfigurationManager;
import com.transerainc.aim.pojo.ACGNotification;
import com.transerainc.aim.pojo.NSNotification;
import com.transerainc.aim.runtime.ActiveAgentManager;
import com.transerainc.aim.system.AgentInformationManagerSystem;
import com.transerainc.aim.util.JAXBHelper;
import com.transerainc.provisioning.notificationserver.NotificationServerMessage;

/**
 * Date of Creation: Date: Jun 11, 2010<br>
 * 
 * @author <a href=mailto:suresh@transerainc.com>Suresh Kumar</a>
 * @version $Revision: 1.1 $
 */

public class ACGNotificatonDelegator {

	private static Logger lgr = Logger.getLogger(ACGNotificatonDelegator.class
			.getName());
	public static final int PRIMARY_MODE = 0;
	public static final int BACKUP_MODE = 1;

	private String notificationType;

	public ACGNotificatonDelegator(String notificationType) {
		this.notificationType = notificationType;
	}

	/**
	 * @param message
	 * @param cmgr
	 */
	public void initNotificaitonToACG(NotificationServerMessage message) {

		// Notify TPGs with the change
		ACGNotificationManager notifMgr = (ACGNotificationManager) AgentInformationManagerSystem
				.findBean("acgNotificationManager");

		ConfigurationManager cmgr = (ConfigurationManager) AgentInformationManagerSystem
				.findBean("configManager");

		try {
			// if (cmgr.getOperationMode() != PRIMARY_MODE) {
			// lgr
			// .info("This instance of is not primary aim, so discarding the notification of type "
			// + notificationType
			// + ". Operation mode is :"
			// + cmgr.getOperationMode());
			// return;
			// }
			ActiveAgentManager activeAgentMgr = (ActiveAgentManager) AgentInformationManagerSystem
					.findBean("activeAgentManager");
			List<String> urlList = activeAgentMgr.getAllAcgUrls();
			ACGNotification notif = new NSNotification(cmgr
					.getAgentInformationManager().getNotificationAcgContext());
			notif.setAcgUrls(urlList);
			String txtMsg = JAXBHelper
					.getNotificationServerMessageAsString(message);

			notif.setMessage(txtMsg);
			notif.setMethod(ACGNotification.METHOD_POST);

			notifMgr.sendNotification(notif);

		} catch (Exception e) {
			lgr.log(Level.SEVERE,
					"Error while sending notification to ACG for notification type "
							+ notificationType, e);
		}
	}
}
