/**
 * 
 */
package com.transerainc.aim.ns;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.transerainc.aha.gen.agent.impl.MediaProfileImpl;
import com.transerainc.aim.conf.ConfigurationManager;
import com.transerainc.aim.notifier.ACGNotificationManager;
import com.transerainc.aim.pojo.ACGNotification;
import com.transerainc.aim.pojo.ActiveAgent;
import com.transerainc.aim.pojo.MediaProfile;
import com.transerainc.aim.pojo.NSNotification;
import com.transerainc.aim.runtime.ActiveAgentManager;
import com.transerainc.aim.system.AgentInformationManagerSystem;
import com.transerainc.aim.util.JAXBHelper;

/**
 * Date of Creation: Date: Jun 15, 2010<br>
 * 
 * @author <a href=mailto:suresh@transerainc.com>Suresh Kumar</a>
 * @version $Revision: 1.1 $
 */

public class MediaProfileNotificationHelper {

	private static Logger lgr = Logger
			.getLogger(MediaProfileNotificationHelper.class.getName());

	private MediaProfile profile;

	public MediaProfileNotificationHelper(MediaProfile profile) {
		this.profile = profile;
	}

	public void handleProfileModificationChanges() {
		ActiveAgentManager aaMgr = (ActiveAgentManager) AgentInformationManagerSystem
				.findBean("activeAgentManager");
		Set<String> agentIdSet = aaMgr.getActiveAgentsForMediaProfile(profile
				.getMediaProfileId());

		Set<ActiveAgent> agents = new HashSet<ActiveAgent>();
		if (lgr.isLoggable(Level.FINE)) {
			lgr.fine("The currently associated agents for the media profile "
					+ profile.getMediaProfileId() + " are : " + agentIdSet);
		}

		if (agentIdSet != null) {
			for (String agentId : agentIdSet) {
				ActiveAgent agent = aaMgr.getActiveAgentByAgentId(profile
						.getTenantId(), agentId);
				if (agent != null) {
					agents.add(agent);
				}
			}
		}

		for (ActiveAgent activeAgent : agents) {
			sendMediaProfileForAgent(activeAgent.getAgentId(), activeAgent
					.getTeamId(), activeAgent.getTenantId());
		}
	}

	private void sendMediaProfileForAgent(String agentId, String teamId,
			String tenantId) {

		com.transerainc.aha.gen.agent.MediaProfile mprofile = new MediaProfileImpl();
		mprofile.setTenantId(tenantId);
		mprofile.setAgentId(agentId);
		// mprofile.setTeamId(teamId);
		if (profile != null) {
			mprofile.setId(profile.getMediaProfileId());
			mprofile.setName(profile.getMediaProfileName());
			mprofile.setTelephony(profile.getNumberOfTelephonyChannels());
			mprofile.setEmail(profile.getNumberOfEmailChannels());
			mprofile.setFax(profile.getNumberOfFaxChannels());
			mprofile.setChat(profile.getNumberOfChatChannels());
			mprofile.setVideo(profile.getNumberOfVideoChannels());
			mprofile.setOther(profile.getNumberOfOtherChannels());
		} else {
			mprofile.setId("");
			mprofile.setName("");
		}
		notifyAcgs(mprofile);
	}

	private void notifyAcgs(com.transerainc.aha.gen.agent.MediaProfile mprofile) {

		// Notify TPGs with the change
		ACGNotificationManager notifMgr = (ACGNotificationManager) AgentInformationManagerSystem
				.findBean("acgNotificationManager");
		try {
			ActiveAgentManager activeAgentMgr = (ActiveAgentManager) AgentInformationManagerSystem
					.findBean("activeAgentManager");

			ConfigurationManager cmgr = (ConfigurationManager) AgentInformationManagerSystem
					.findBean("configManager");

			List<String> urlList = activeAgentMgr.getAllAcgUrls();
			ACGNotification notif = new NSNotification(cmgr
					.getAgentInformationManager()
					.getNotificationAcgUpdateMediaProfileContext());
			notif.setAcgUrls(urlList);
			String txtMsg = JAXBHelper.getMediaProfileAsString(mprofile);

			notif.setMessage(txtMsg);
			notif.setMethod(ACGNotification.METHOD_POST);

			notifMgr.sendNotification(notif);

			if (lgr.isLoggable(Level.FINEST)) {
				lgr.finest("Media profile notificaiton message " + txtMsg);
			}

		} catch (Exception e) {
			lgr.log(Level.SEVERE,
					"Error while sending media profile notification to ACG", e);
		}
	}

}
