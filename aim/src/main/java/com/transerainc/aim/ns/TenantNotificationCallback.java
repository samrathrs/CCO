/**
 * 
 */
package com.transerainc.aim.ns;

import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.xml.bind.JAXBException;

import com.transerainc.aim.conf.ConfigurationManager;
import com.transerainc.aim.db.SqlStatementManager;
import com.transerainc.aim.notifier.ACGNotificationManager;
import com.transerainc.aim.pojo.ACGNotification;
import com.transerainc.aim.pojo.NSNotification;
import com.transerainc.aim.pojo.Tenant;
import com.transerainc.aim.provisioning.ProvisioningConnection;
import com.transerainc.aim.provisioning.ProvisioningDAO;
import com.transerainc.aim.provisioning.ProvisioningManager;
import com.transerainc.aim.provisioning.handler.TenantHandler;
import com.transerainc.aim.runtime.ActiveAgentManager;
import com.transerainc.aim.system.AgentInformationManagerSystem;
import com.transerainc.aim.tpgintf.NotificationWrapperType;
import com.transerainc.aim.tpgintf.impl.NotificationWrapperImpl;
import com.transerainc.aim.util.JAXBHelper;
import com.transerainc.provisioning.notificationserver.NodeUpdateType;
import com.transerainc.provisioning.notificationserver.NotificationServerMessage;
import com.transerainc.tam.ns.NotificationCallback;

/**
 * @author <a href="mailto:ramesh.bobba@transerainc.com">Ramesh Bobba</a>
 * @version $Rivision:$
 */
public class TenantNotificationCallback implements NotificationCallback {
	private static Logger lgr = Logger
			.getLogger(TenantNotificationCallback.class.getName());
	private String subscriptionId;

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.transerainc.tam.ns.NotificationCallback#entityChanged(com.transerainc
	 * .provisioning.notificationserver.NotificationServerMessage)
	 */
	@SuppressWarnings("unchecked")
	public void entityChanged(NotificationServerMessage message) {
		List<NodeUpdateType> nodeList = message.getUpdateNotification()
				.getNodeUpdate();

		ConfigurationManager cmgr = null;
		try {
			if (lgr.isLoggable(Level.INFO)) {
				lgr.info("Got a tenant changed/added/removed event.");
			}

			ProvisioningManager pmgr = (ProvisioningManager) AgentInformationManagerSystem
					.findBean("provManager");
			SqlStatementManager stmtMgr = (SqlStatementManager) AgentInformationManagerSystem
					.findBean("sqlStatementMgr");
			cmgr = (ConfigurationManager) AgentInformationManagerSystem
					.findBean("configManager");
			ProvisioningConnection provCon = new ProvisioningConnection(cmgr);
			ProvisioningDAO provDAO = new ProvisioningDAO(stmtMgr, provCon);
			TenantHandler tenantHandler = pmgr.getTenantHandler();

			HashMap<String, Tenant> tenantMap = new HashMap<String, Tenant>();
			for (NodeUpdateType nuType : nodeList) {
				String tenantId = nuType.getNodeId();
				HashMap<String, Tenant> tMap = provDAO.getTenants(
						"tenant.query", tenantId);

				if (tMap != null) {
					tenantMap.putAll(tMap);
				}
			}

			tenantHandler.updateTenants(tenantMap);

		} catch (Exception e) {
			String xml = null;
			try {
				xml = JAXBHelper.getNotificationServerMessageAsString(message);
			} catch (JAXBException e1) {
				lgr.log(Level.SEVERE, "Error while converting Notification "
						+ "Server Message to string", e1);
			}

			lgr.log(Level.SEVERE, "Error while updating tenants changed: "
					+ xml, e);
		}

		// if (cmgr.getOperationMode() != ACGNotificatonDelegator.PRIMARY_MODE)
		// {
		// lgr
		// .info("This instance is not primary aim, so discarding the notification of type "
		// + " TenantNotificationCallback"
		// + ". Operation mode is :" + cmgr.getOperationMode());
		// return;
		// }

		// Notify TPGs with the change
		ACGNotificationManager notifMgr = (ACGNotificationManager) AgentInformationManagerSystem
				.findBean("acgNotificationManager");

		try {
			ActiveAgentManager activeAgentMgr = (ActiveAgentManager) AgentInformationManagerSystem
					.findBean("activeAgentManager");
			List<String> urlList = activeAgentMgr.getAllAcgUrls();
			ACGNotification notif = new NSNotification(cmgr
					.getAgentInformationManager().getNotificationAcgContext());
			notif.setAcgUrls(urlList);
			String txtMsg = JAXBHelper
					.getNotificationServerMessageAsString(message);

			for (NodeUpdateType nuType : nodeList) {
				String tenantId = nuType.getNodeId();
				NotificationWrapperType notifWrapper = new NotificationWrapperImpl();
				notifWrapper.setTenantId(tenantId);
				notifWrapper.setNotificationServerMessage(txtMsg);

				String wrapperMsg = JAXBHelper.getNotificationWrapperAsString(
						notifWrapper, false);

				notif.setMessage(wrapperMsg);
				notif.setMethod(ACGNotification.METHOD_POST);

				notifMgr.sendNotification(notif);
			}
		} catch (Exception e) {
			lgr.log(Level.SEVERE, "Error while sending notification to ACG", e);
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
