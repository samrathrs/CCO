/**
 * 
 */
package com.transerainc.aim.ns;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.transerainc.aim.conf.ConfigurationManager;
import com.transerainc.aim.db.SqlStatementManager;
import com.transerainc.aim.notifier.ACGNotificatonDelegator;
import com.transerainc.aim.provisioning.ProvisioningConnection;
import com.transerainc.aim.provisioning.ProvisioningDAO;
import com.transerainc.aim.provisioning.ProvisioningManager;
import com.transerainc.aim.runtime.AgentLockoutManager;
import com.transerainc.aim.system.AgentInformationManagerSystem;
import com.transerainc.provisioning.notificationserver.AttributeUpdate;
import com.transerainc.provisioning.notificationserver.NodeUpdate;
import com.transerainc.provisioning.notificationserver.NotificationServerMessage;
import com.transerainc.tam.ns.NotificationCallback;

/**
 * @author <a href="mailto:ramesh.bobba@transerainc.com">Ramesh Bobba</a>
 * @version $Rivision:$
 */
public class AgentNotificationCallback implements NotificationCallback {
	private static Logger lgr = Logger
			.getLogger(AgentNotificationCallback.class.getName());

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
			lgr.info("Got a Agent changed/added/removed event.");
		}

		ProvisioningManager pmgr = (ProvisioningManager) AgentInformationManagerSystem
				.findBean("provManager");
		SqlStatementManager stmtMgr = (SqlStatementManager) AgentInformationManagerSystem
				.findBean("sqlStatementMgr");
		ConfigurationManager cmgr = (ConfigurationManager) AgentInformationManagerSystem
				.findBean("configManager");
		ProvisioningConnection provCon = new ProvisioningConnection(cmgr);
		ProvisioningDAO provDAO = new ProvisioningDAO(stmtMgr, provCon);
		pmgr.initializeAgentHandler(provDAO);

		// Notify TPGs with the change
		ACGNotificatonDelegator delegator = new ACGNotificatonDelegator(
				AgentNotificationCallback.class.getName());
		delegator.initNotificaitonToACG(message);

		//
		// ACGNotificationManager notifMgr =
		// (ACGNotificationManager) AgentInformationManagerSystem
		// .findBean("acgNotificationManager");
		//
		// try {
		// ActiveAgentManager activeAgentMgr =
		// (ActiveAgentManager) AgentInformationManagerSystem
		// .findBean("activeAgentManager");
		// List<String> urlList = activeAgentMgr.getAllAcgUrls();
		// ACGNotification notif =
		// new NSNotification(cmgr.getAgentInformationManager()
		// .getNotificationAcgContext());
		// notif.setAcgUrls(urlList);
		// String txtMsg =
		// JAXBHelper.getNotificationServerMessageAsString(message);
		//
		// notif.setMessage(txtMsg);
		// notif.setMethod(ACGNotification.METHOD_POST);
		//
		// notifMgr.sendNotification(notif);
		// } catch (Exception e) {
		// lgr.log(Level.SEVERE, "Error while sending notification to ACG", e);
		// }

		// Clear the agent lockout, if any
		handleAgentLockout(message);

	}

	@SuppressWarnings("unchecked")
	private void handleAgentLockout(NotificationServerMessage message) {
		AgentLockoutManager almMgr = (AgentLockoutManager) AgentInformationManagerSystem
				.findBean("agentLockoutManager");
		List<NodeUpdate> nodeList = message.getUpdateNotification()
				.getNodeUpdate();
		for (NodeUpdate nu : nodeList) {
			if ("agent".equals(nu.getNodeType())
					&& "update".equals(nu.getUpdateType())) {
				String agentId = nu.getNodeId();
				List<AttributeUpdate> attList = nu.getAttributeUpdate();
				for (AttributeUpdate au : attList) {
					if ("isLocked".equals(au.getName())) {
						// We dont need to track the local locking any more.
						// This info would be visible in the Prov Agent Data
						// because of the preceding refresh
						almMgr.clearAgentLockout(agentId);
					}
				}
			}
		}
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
