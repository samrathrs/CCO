/**
 * 
 */
package com.transerainc.aim.runtime;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.xml.bind.JAXBException;

import org.apache.commons.lang.builder.ToStringBuilder;

import com.transerainc.aha.gen.agent.Channel;
import com.transerainc.aha.gen.agent.ChannelSet;
import com.transerainc.aha.gen.agent.impl.ChannelImpl;
import com.transerainc.aha.gen.agent.impl.ChannelSetImpl;
import com.transerainc.aim.conf.ConfigurationManager;
import com.transerainc.aim.notifier.ACGNotificationManager;
import com.transerainc.aim.pojo.ACGNotification;
import com.transerainc.aim.pojo.ActiveAgent;
import com.transerainc.aim.pojo.MediaProfile;
import com.transerainc.aim.pojo.NSNotification;
import com.transerainc.aim.system.AgentInformationManagerSystem;
import com.transerainc.aim.util.JAXBHelper;

/**
 * Date of Creation: Date: Jun 9, 2010<br>
 * 
 * @author <a href=mailto:suresh@transerainc.com>Suresh Kumar</a>
 * @version $Revision: 1.2 $
 */
@Deprecated
public class ActiveChannelDeltaManager {

	private static Logger lgr = Logger
			.getLogger(ActiveChannelDeltaManager.class.getName());

	private MediaProfile oldProfile;
	private MediaProfile newProfile;

	public ActiveChannelDeltaManager(MediaProfile oldProfile,
			MediaProfile newProfile) {
		this.oldProfile = oldProfile;
		this.newProfile = newProfile;
	}

	public void handleChannelDelta() {

		if (lgr.isLoggable(Level.INFO)) {
			lgr.info("In the process of handling media profile delta...");
		}

		if (lgr.isLoggable(Level.FINE)) {
			lgr.fine("Old media profile : " + oldProfile.toString()
					+ "New media profile : " + newProfile.toString());
		}

		MediaProfileDelta delta = new MediaProfileDelta();

		delta.telephonyChannels = newProfile.getNumberOfTelephonyChannels()
				- oldProfile.getNumberOfTelephonyChannels();
		delta.emailChannels = newProfile.getNumberOfEmailChannels()
				- oldProfile.getNumberOfEmailChannels();
		delta.faxChannels = newProfile.getNumberOfFaxChannels()
				- oldProfile.getNumberOfFaxChannels();
		delta.chatChannels = newProfile.getNumberOfChatChannels()
				- oldProfile.getNumberOfChatChannels();
		delta.videoChannels = newProfile.getNumberOfVideoChannels()
				- oldProfile.getNumberOfVideoChannels();
		delta.otherChannels = newProfile.getNumberOfOtherChannels()
				- oldProfile.getNumberOfOtherChannels();

		// get the list of agents that using this media profile id
		ActiveAgentManager aaMgr = (ActiveAgentManager) AgentInformationManagerSystem
				.findBean("activeAgentManager");
		Set<String> agentSet = aaMgr.getActiveAgentsForMediaProfile(newProfile
				.getMediaProfileId());

		// create channel if necessary and then notify to tacg
		Set<ActiveAgent> peerActiveAgentSet = new HashSet<ActiveAgent>();
		if (null != agentSet && !agentSet.isEmpty()) {
			for (String agentId : agentSet) {
				ActiveAgent peerActiveAgent = aaMgr.getActiveAgentByAgentId(
						newProfile.getTenantId(), agentId);
				peerActiveAgentSet.add(peerActiveAgent);
			}
		}

		if (!peerActiveAgentSet.isEmpty()) {
			lgr.info("The number of activte agents found for media profile "
					+ newProfile.getMediaProfileId() + " are "
					+ peerActiveAgentSet.size() + " and profile delta is "
					+ delta.toString());
			ChannelSet addChannelSet = new ChannelSetImpl();
			ChannelSet removeChannelSet = new ChannelSetImpl();
			addChannelSet.setProfileId(newProfile.getMediaProfileId());
			addChannelSet.setProfileName(newProfile.getMediaProfileName());
			addChannelSet.setTenantId(newProfile.getTenantId());
			addChannelSet.setTenantId(newProfile.getTenantId());

			removeChannelSet.setProfileId(newProfile.getMediaProfileId());
			removeChannelSet.setProfileName(newProfile.getMediaProfileName());
			for (ActiveAgent activeAgent : peerActiveAgentSet) {
				addOrRemoveChannels(addChannelSet, removeChannelSet,
						activeAgent, delta);
			}
			notifyACGs(addChannelSet, 1);
			notifyACGs(removeChannelSet, 0);
		} else {
			lgr.info("There are no activte agents found for media profile "
					+ newProfile.getMediaProfileId());
		}
	}

	/**
	 * @param removeChannelSet
	 * @param notifMgr
	 * @param cmgr
	 * @param urlList
	 * @throws JAXBException
	 */
	private void notifyACGs(ChannelSet channelSet, int type) {
		try {
			ACGNotificationManager notifMgr = (ACGNotificationManager) AgentInformationManagerSystem
					.findBean("acgNotificationManager");
			ActiveAgentManager activeAgentMgr = (ActiveAgentManager) AgentInformationManagerSystem
					.findBean("activeAgentManager");
			ConfigurationManager cmgr = (ConfigurationManager) AgentInformationManagerSystem
					.findBean("configManager");
			List<String> urlList = activeAgentMgr.getAllAcgUrls();
			// String context = type == 1 ? cmgr.getAgentInformationManager()
			// .getNotificationAcgAddChannelsContext() : cmgr
			// .getAgentInformationManager()
			// .getNotificationAcgRemoveChannelsContext();
			String context = cmgr.getAgentInformationManager()
					.getNotificationAcgUpdateMediaProfileContext();
			String optype = type == 1 ? " add " : " remove ";
			if (!channelSet.getChannel().isEmpty()) {
				ACGNotification notif = new NSNotification(context);
				notif.setAcgUrls(urlList);
				String txtMsg = JAXBHelper.getChannelSetAsString(channelSet);
				notif.setMessage(txtMsg);
				notif.setMethod(ACGNotification.METHOD_POST);
				notifMgr.sendNotification(notif);
				if (lgr.isLoggable(Level.FINEST)) {
					lgr.finest("Notifying ACGs " + urlList.toString()
							+ " with " + optype + " channelset " + txtMsg);
				}
			}
		} catch (Exception e) {
			lgr.log(Level.SEVERE, "Error while sending notification to ACG", e);
		}
	}

	private void addOrRemoveChannels(ChannelSet addChannelSet,
			ChannelSet deleteChannelSet, ActiveAgent activeAgent,
			MediaProfileDelta profileDelta) {

		if (profileDelta.chatChannels > 0) {
			createChannel(addChannelSet, activeAgent,
					profileDelta.chatChannels, "chat");
		} else {
			// remove channel by finding the right channel with type
		}
		if (profileDelta.emailChannels > 0) {
			createChannel(addChannelSet, activeAgent,
					profileDelta.emailChannels, "email");
		}
		if (profileDelta.faxChannels > 0) {
			createChannel(addChannelSet, activeAgent, profileDelta.faxChannels,
					"fax");
		}
		if (profileDelta.otherChannels > 0) {
			createChannel(addChannelSet, activeAgent,
					profileDelta.otherChannels, "other");
		}
		if (profileDelta.telephonyChannels > 0) {
			createChannel(addChannelSet, activeAgent,
					profileDelta.telephonyChannels, "telephony");
		}
		if (profileDelta.videoChannels > 0) {
			createChannel(addChannelSet, activeAgent,
					profileDelta.videoChannels, "video");
		}
	}

	private void createChannel(ChannelSet channelSet, ActiveAgent activeAgent,
			int count, String channelName) {
		for (int i = 0; i < count; i++) {
			Channel channel = new ChannelImpl();
			channel.setId(UUID.randomUUID().toString());
			channel.setType(channelName);
			channel.setAgentId(activeAgent.getAgentId());
			channel.setTeamId(activeAgent.getTeamId());
			channelSet.getChannel().add(channel);
		}
	}

	class MediaProfileDelta {
		int telephonyChannels;
		int emailChannels;
		int faxChannels;
		int chatChannels;
		int videoChannels;
		int otherChannels;

		/*
		 * (non-Javadoc)
		 * 
		 * @see java.lang.Object#toString()
		 */
		@Override
		public String toString() {
			// TODO Auto-generated method stub
			return ToStringBuilder.reflectionToString(this);
		}
	}
}
