/**
 * 
 */
package com.transerainc.aim.ns;

import java.util.logging.Level;
import java.util.logging.Logger;

import com.transerainc.aim.conf.ConfigurationManager;
import com.transerainc.aim.db.SqlStatementManager;
import com.transerainc.aim.provisioning.ProvisioningConnection;
import com.transerainc.aim.provisioning.ProvisioningDAO;
import com.transerainc.aim.provisioning.ProvisioningManager;
import com.transerainc.aim.system.AgentInformationManagerSystem;
import com.transerainc.provisioning.notificationserver.NotificationServerMessage;
import com.transerainc.tam.ns.NotificationCallback;

/**
 * Date of Creation: Date: Jun 10, 2010<br>
 * @author <a href=mailto:suresh@transerainc.com>Suresh Kumar</a>
 * @version $Revision: 1.1 $
 */

public class MediaProfileAssignmentNotificationCallback implements
		NotificationCallback {
	private static Logger lgr = Logger
			.getLogger(MediaProfileAssignmentNotificationCallback.class
					.getName());
	private String subscriptionId;

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.transerainc.tam.ns.NotificationCallback#entityChanged(com.transerainc
	 * .provisioning.notificationserver.NotificationServerMessage)
	 */
	public void entityChanged(NotificationServerMessage message) {
		if (lgr.isLoggable(Level.INFO)) {
			lgr.info("Got a Agent Media Profile assignment changed event.");
		}
		ProvisioningManager pmgr = (ProvisioningManager) AgentInformationManagerSystem
				.findBean("provManager");
		SqlStatementManager stmtMgr = (SqlStatementManager) AgentInformationManagerSystem
				.findBean("sqlStatementMgr");
		ConfigurationManager cmgr = (ConfigurationManager) AgentInformationManagerSystem
				.findBean("configManager");
		ProvisioningConnection provCon = new ProvisioningConnection(cmgr);
		ProvisioningDAO provDAO = new ProvisioningDAO(stmtMgr, provCon);
		pmgr.initializeAgentMediaProfileHandler(provDAO);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.transerainc.tam.ns.NotificationCallback#subscribed(java.lang.String)
	 */
	public void subscribed(String subscriptionId) {
		this.subscriptionId = subscriptionId;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.transerainc.tam.ns.NotificationCallback#unsubscribed(java.lang.String
	 * )
	 */
	public void unsubscribed(String subscriptionId) {
		if (this.subscriptionId.equals(subscriptionId)) {
			lgr.warning("Subscription " + subscriptionId + " unsubscribed.");
		}
	}

}
