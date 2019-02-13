/**
 * 
 */
package com.transerainc.aim.ns;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.transerainc.aim.conf.ConfigurationManager;
import com.transerainc.provisioning.notificationserver.NotificationServerMessage;
import com.transerainc.provisioning.notificationserver.SubscribeType;
import com.transerainc.provisioning.notificationserver.impl.NotificationServerMessageImpl;
import com.transerainc.provisioning.notificationserver.impl.SubscribeTypeImpl;
import com.transerainc.tam.config.ConfigAgentHelper;
import com.transerainc.tam.config.ServerMapping;
import com.transerainc.tam.ns.NotificationManager;

/**
 * @author <a href="mailto:ramesh.bobba@transerainc.com">Ramesh Bobba</a>
 * @version $Rivision:$
 */
public class NotificationSubscriber {
	private static Logger lgr =
			Logger.getLogger(NotificationSubscriber.class.getName());
	private ConfigurationManager configMgr;

	public NotificationSubscriber(ConfigurationManager cmgr) {
		this.configMgr = cmgr;
	}

	public void subscribe() throws Exception {
		String confAgentUrl = configMgr.getConfigAgentUrl();
		String nsXpath = configMgr.getXpath("notification.server.mappings");

		if (lgr.isLoggable(Level.CONFIG)) {
			lgr.config("Sending xpath " + nsXpath + " to config agent @ "
				+ confAgentUrl);
		}

		List<ServerMapping> smList =
				ConfigAgentHelper.getServerMappings(confAgentUrl, nsXpath);

		if (lgr.isLoggable(Level.FINEST)) {
			lgr.finest("Got the following server mapping "
				+ "list for notification server lookup: " + smList);
		}

		String nsUrl = null;
		if (smList != null) {
			ServerMapping sMapping = smList.get(0);

			nsUrl =
					"http://" + sMapping.getHost() + ":" + sMapping.getPort()
						+ "/notification-server";
		}

		if (lgr.isLoggable(Level.CONFIG)) {
			lgr.config("Registering with Notification Server: " + nsUrl);
		}

		NotificationManager.setNotificationServerUrl("all", nsUrl);

		NotificationServerMessage nsMsg = new NotificationServerMessageImpl();
		SubscribeType sType = new SubscribeTypeImpl();
		sType.setPath(configMgr.getXpath("adhoc.dia.properties.xpath"));
		sType.setNotifyUrl(configMgr.getMyUrl() + "/nsIntf");
		sType.setAttributeList("*");
		nsMsg.setSubscribe(sType);
		NotificationManager.subscribe("all", nsMsg,
			new AdhocDialPropertiesNotificationCallback());

		nsMsg = new NotificationServerMessageImpl();
		sType = new SubscribeTypeImpl();
		sType.setPath(configMgr.getXpath("agent.xpath"));
		sType.setNotifyUrl(configMgr.getMyUrl() + "/nsIntf");
		sType.setAttributeList("*");
		nsMsg.setSubscribe(sType);
		NotificationManager.subscribe("all", nsMsg,
			new AgentNotificationCallback());

		nsMsg = new NotificationServerMessageImpl();
		sType = new SubscribeTypeImpl();
		sType.setPath(configMgr.getXpath("agent.to.team.mapping.xpath"));
		sType.setNotifyUrl(configMgr.getMyUrl() + "/nsIntf");
		sType.setAttributeList("*");
		nsMsg.setSubscribe(sType);
		NotificationManager.subscribe("all", nsMsg,
			new AgentToTeamMappingNotificationCallback());

		nsMsg = new NotificationServerMessageImpl();
		sType = new SubscribeTypeImpl();
		sType.setPath(configMgr.getXpath("agent.profile.xpath"));
		sType.setNotifyUrl(configMgr.getMyUrl() + "/nsIntf");
		sType.setAttributeList("*");
		nsMsg.setSubscribe(sType);
		NotificationManager.subscribe("all", nsMsg,
			new AgentProfileNotificationCallback());

		nsMsg = new NotificationServerMessageImpl();
		sType = new SubscribeTypeImpl();
		sType.setPath(configMgr.getXpath("aux.code.xpath"));
		sType.setNotifyUrl(configMgr.getMyUrl() + "/nsIntf");
		sType.setAttributeList("*");
		nsMsg.setSubscribe(sType);
		NotificationManager.subscribe("all", nsMsg,
			new AuxilaryCodeNotificationCallback());

		nsMsg = new NotificationServerMessageImpl();
		sType = new SubscribeTypeImpl();
		sType.setPath(configMgr.getXpath("password.policy.xpath"));
		sType.setNotifyUrl(configMgr.getMyUrl() + "/nsIntf");
		sType.setAttributeList("*");
		nsMsg.setSubscribe(sType);
		NotificationManager.subscribe("all", nsMsg,
			new PasswordPolicyNotificationCallback());

		nsMsg = new NotificationServerMessageImpl();
		sType = new SubscribeTypeImpl();
		sType.setPath(configMgr.getXpath("speed.dial.list.xpath"));
		sType.setNotifyUrl(configMgr.getMyUrl() + "/nsIntf");
		sType.setAttributeList("*");
		nsMsg.setSubscribe(sType);
		NotificationManager.subscribe("all", nsMsg,
			new SpeedDialListNotificationCallback());

		nsMsg = new NotificationServerMessageImpl();
		sType = new SubscribeTypeImpl();
		sType.setPath(configMgr.getXpath("speed.dial.entry.xpath"));
		sType.setNotifyUrl(configMgr.getMyUrl() + "/nsIntf");
		sType.setAttributeList("*");
		nsMsg.setSubscribe(sType);
		NotificationManager.subscribe("all", nsMsg,
			new SpeedDialListNotificationCallback());

		nsMsg = new NotificationServerMessageImpl();
		sType = new SubscribeTypeImpl();
		sType.setPath(configMgr.getXpath("team.xpath"));
		sType.setNotifyUrl(configMgr.getMyUrl() + "/nsIntf");
		sType.setAttributeList("teamName, teamStatus");
		nsMsg.setSubscribe(sType);
		NotificationManager.subscribe("all", nsMsg,
			new TeamNotificationCallback());

		nsMsg = new NotificationServerMessageImpl();
		sType = new SubscribeTypeImpl();
		sType.setPath(configMgr.getXpath("virtualteam.xpath"));
		sType.setNotifyUrl(configMgr.getMyUrl() + "/nsIntf");
		sType.setAttributeList("virtualTeamName, seratelAcd");
		nsMsg.setSubscribe(sType);
		NotificationManager.subscribe("all", nsMsg,
			new VirtualTeamNotificationCallback());

		nsMsg = new NotificationServerMessageImpl();
		sType = new SubscribeTypeImpl();
		sType.setPath(configMgr.getXpath("site.xpath"));
		sType.setNotifyUrl(configMgr.getMyUrl() + "/nsIntf");
		sType.setAttributeList("siteName");
		nsMsg.setSubscribe(sType);
		NotificationManager.subscribe("all", nsMsg,
			new SiteNotificationCallback());

		nsMsg = new NotificationServerMessageImpl();
		sType = new SubscribeTypeImpl();
		sType.setPath(configMgr.getXpath("tenant.xpath"));
		sType.setNotifyUrl(configMgr.getMyUrl() + "/nsIntf");
		sType.setAttributeList("tenantId, companyName, "
			+ "loginDomain, cadHiddenFromAgent, "
			+ "autoWrapupInterval, tenantXmlUrl, addressBookId");
		nsMsg.setSubscribe(sType);
		NotificationManager.subscribe("all", nsMsg,
			new TenantNotificationCallback());
		
		nsMsg = new NotificationServerMessageImpl();
		sType = new SubscribeTypeImpl();
		sType.setPath(configMgr.getXpath("agent.media.profile.xpath"));
		sType.setNotifyUrl(configMgr.getMyUrl() + "/nsIntf");
		sType.setAttributeList("*");
		nsMsg.setSubscribe(sType);
		NotificationManager.subscribe("all", nsMsg,
			new MediaProfileNotificationCallback());

		nsMsg = new NotificationServerMessageImpl();
		sType = new SubscribeTypeImpl();
		sType.setPath(configMgr.getXpath("agent.media.profile.assignment.xpath"));
		sType.setNotifyUrl(configMgr.getMyUrl() + "/nsIntf");
		sType.setAttributeList("*");
		nsMsg.setSubscribe(sType);
		NotificationManager.subscribe("all", nsMsg,
			new MediaProfileAssignmentNotificationCallback());
		
		nsMsg = new NotificationServerMessageImpl();
		sType = new SubscribeTypeImpl();
		sType.setPath(configMgr.getXpath("agent.skill.profile.xpath"));
		sType.setNotifyUrl(configMgr.getMyUrl() + "/nsIntf");
		sType.setAttributeList("*");
		nsMsg.setSubscribe(sType);
		NotificationManager.subscribe("all", nsMsg,
			new SkillProfileNotificationCallback());

		nsMsg = new NotificationServerMessageImpl();
		sType = new SubscribeTypeImpl();
		sType.setPath(configMgr.getXpath("skill.profile.assignment.xpath"));
		sType.setNotifyUrl(configMgr.getMyUrl() + "/nsIntf");
		sType.setAttributeList("*");
		nsMsg.setSubscribe(sType);
		NotificationManager.subscribe("all", nsMsg,
			new SkillProfileAssignmentNotificationCallback());
		
		nsMsg = new NotificationServerMessageImpl();
		sType = new SubscribeTypeImpl();
		sType.setPath(configMgr.getXpath("pop.mapping.xpath"));
		sType.setNotifyUrl(configMgr.getMyUrl() + "/nsIntf");
		sType.setAttributeList("*");
		nsMsg.setSubscribe(sType);
		NotificationManager.subscribe("all", nsMsg,
			new PopMappingNotificationCallback());
			
		nsMsg = new NotificationServerMessageImpl();
		sType = new SubscribeTypeImpl();
		sType.setPath(configMgr.getXpath("seratelui.server.mappings"));
		sType.setNotifyUrl(configMgr.getMyUrl() + "/nsIntf");
		sType.setAttributeList("*");
		nsMsg.setSubscribe(sType);
		NotificationManager.subscribe("all", nsMsg,
			new SeratelUiNotificationCallback());
		
		nsMsg = new NotificationServerMessageImpl();
		sType = new SubscribeTypeImpl();
		sType.setPath(configMgr.getXpath("third.party.profile.xpath"));
		sType.setNotifyUrl(configMgr.getMyUrl() + "/nsIntf");
		sType.setAttributeList("*");
		nsMsg.setSubscribe(sType);
		NotificationManager.subscribe("all", nsMsg,
			new ThirdPartyProfileNotificationCallback());
		
		nsMsg = new NotificationServerMessageImpl();
		sType = new SubscribeTypeImpl();
		sType.setPath(configMgr.getXpath("third.party.profile.assignment.xpath"));
		sType.setNotifyUrl(configMgr.getMyUrl() + "/nsIntf");
		sType.setAttributeList("*");
		nsMsg.setSubscribe(sType);
		NotificationManager.subscribe("all", nsMsg,
			new ThirdPartyProfileAssignmentNotificationCallback());
		
		nsMsg = new NotificationServerMessageImpl();
		sType = new SubscribeTypeImpl();
		sType.setPath(configMgr.getXpath("third.party.vendor.xpath"));
		sType.setNotifyUrl(configMgr.getMyUrl() + "/nsIntf");
		sType.setAttributeList("*");
		nsMsg.setSubscribe(sType);
		NotificationManager.subscribe("all", nsMsg,
			new ThirdPartyVendorNotificationCallback());
		
	}
}
