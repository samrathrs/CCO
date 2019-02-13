/**
 * 
 */
package com.transerainc.aim.runtime;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.springframework.util.StringUtils;

import com.transerainc.aim.conf.ConfigurationManager;
import com.transerainc.aim.notifier.ACGNotificationManager;
import com.transerainc.aim.pojo.ACGNotification;
import com.transerainc.aim.pojo.AIMNotification;
import com.transerainc.aim.pojo.ActiveAgent;
import com.transerainc.aim.pojo.LogoutNotification;
import com.transerainc.aim.provisioning.ProvisioningManager;
import com.transerainc.aim.provisioning.handler.AgentMediaProfileHandler;
import com.transerainc.aim.system.AgentInformationManagerSystem;

/**
 * @author <a href="mailto:ramesh.bobba@transerainc.com">Ramesh Bobba</a>
 * @version $Rivision:$
 */
public class ActiveAgentManager {
	private static Logger lgr = Logger.getLogger(ActiveAgentManager.class
			.getName());
	// Suresh : this need to revisit, maintain sing map for active agent and
	// channel id
	// <tenantId, <channelId, ActiveAgent>>
	private ConcurrentHashMap<String, ConcurrentHashMap<String, ActiveAgent>> loggedInAgentList;
	// <agentId, <channelIdset>
	private ConcurrentHashMap<String, HashSet<String>> activeAgentChannelList;
	// <mediaProfileId, <agentSet>>
	private ConcurrentHashMap<String, HashSet<String>> activeMediaProfileList;
	private ConcurrentHashMap<String, String> inprogressAgentSession;

	private Object mutex = new Object();
	private Object asMutex = new Object();

	public ActiveAgentManager() {
		loggedInAgentList = new ConcurrentHashMap<String, ConcurrentHashMap<String, ActiveAgent>>();
		activeAgentChannelList = new ConcurrentHashMap<String, HashSet<String>>();
		activeMediaProfileList = new ConcurrentHashMap<String, HashSet<String>>();
		inprogressAgentSession = new ConcurrentHashMap<String, String>();
	}

	public void addAgent(String tenantId, ActiveAgent agent, boolean notify) {
		if (lgr.isLoggable(Level.INFO)) {
			lgr.info("Adding agent " + agent + " to tenant " + tenantId
					+ "'s active list");
		}

		ConcurrentHashMap<String, ActiveAgent> agentList = null;
		String channelId = agent.getChannelId();
		synchronized (mutex) {
			if (loggedInAgentList.containsKey(tenantId)) {
				agentList = loggedInAgentList.get(tenantId);
			} else {
				agentList = new ConcurrentHashMap<String, ActiveAgent>();
				loggedInAgentList.put(tenantId, agentList);
			}
			agentList.put(channelId, agent);
			
			// Update agent to channel-id mapping
			HashSet<String> agentChannelList = null;
			if (activeAgentChannelList.containsKey(agent.getAgentId())) {
				agentChannelList = activeAgentChannelList.get(agent
						.getAgentId());
			} else {
				agentChannelList = new HashSet<String>();
				activeAgentChannelList
						.put(agent.getAgentId(), agentChannelList);
			}
			if (!agentChannelList.contains(agent.getChannelId())) {
//					&& agent.getChannelType().equalsIgnoreCase("telephony")) {  //MKOP - fix to iCRM issue.
				agentChannelList.add(channelId);
			}
			addAgentMediaProfile(agent.getAgentId(), agent.getTeamId(),
					agent.getSiteId());
			removeInprogressAgentSession(agent.getAgentId(),
					agent.getAgentSessionId());
		}

		if (notify) {
			// Notify the other AIMs of this agent.
			AIMNotification notif = new AIMNotification();
			notif.setActiveAgent(agent);
			notif.setTenantId(tenantId);
			notif.setCommand("LoggedIn");

			AIMNotificationManager notifMgr = (AIMNotificationManager) AgentInformationManagerSystem
					.findBean("aimNotifManager");
			notifMgr.sendNotification(notif);
		}
	}

	public ActiveAgent removeAgent(String tenantId, String agentId,
			String channelId, boolean notify) {
		if (lgr.isLoggable(Level.INFO)) {
			lgr.info("Removing agent " + agentId + " channel-id " + channelId
					+ " from tenant " + tenantId + "'s active list");
		}

		ConcurrentHashMap<String, ActiveAgent> agentList = null;
		ActiveAgent activeAgent = null;
		synchronized (mutex) {
			agentList = loggedInAgentList.get(tenantId);
			if (agentList != null) {
				if (StringUtils.hasLength(channelId)
						&& agentList.containsKey(channelId))
					activeAgent = agentList.remove(channelId);
			}

			// Remove agent to channel-id mapping
			HashSet<String> agentChannelList = null;
			if (activeAgentChannelList.containsKey(agentId)
					&& activeAgentChannelList.get(agentId) != null) {
				agentChannelList = activeAgentChannelList.get(agentId);
				if (StringUtils.hasText(channelId)) {
					agentChannelList.remove(channelId);
				} else {
					// if (!agentChannelList.isEmpty()) {
					// channelId = agentChannelList
					// .toArray(new String[agentChannelList.size()])[0];
					// }
					if (lgr.isLoggable(Level.INFO)) {
						lgr.info("Removing all channels for agent " + agentId
								+ "from tenant " + tenantId + "'s active list");
					}
					if (agentList != null) {
						for (String chId : agentChannelList) {
							agentList.remove(chId);
						}
					}
					agentChannelList.clear();
					activeAgentChannelList.remove(agentId);
				}
			}

			// update the mediaProfile-agent relation
			removeAgentMediaProfile(agentId, "", "");

		}

		if (notify) {
			// Notify the other AIMs of this agent.
			AIMNotification notif = new AIMNotification();
			notif.setAgentId(agentId);
			notif.setTenantId(tenantId);
			notif.setChannelId(channelId);
			notif.setCommand("LoggedOut");

			AIMNotificationManager notifMgr = (AIMNotificationManager) AgentInformationManagerSystem
					.findBean("aimNotifManager");
			notifMgr.sendNotification(notif);
		}

		return activeAgent;
	}

	public List<String> getTenantIds() {
		List<String> lst = new ArrayList<String>();

		synchronized (mutex) {
			lst.addAll(loggedInAgentList.keySet());
		}

		return lst;
	}

	public HashMap<String, ActiveAgent> getActiveAgents(String tenantId) {
		HashMap<String, ActiveAgent> map = new HashMap<String, ActiveAgent>();

		if (loggedInAgentList.containsKey(tenantId)) {
			synchronized (mutex) {
				Map<String, ActiveAgent> activeList = loggedInAgentList
						.get(tenantId);
				if (activeList != null) {
					map.putAll(activeList);
				}
			}
		}

		return map;
	}

	public Set<String> getLoggedInAgents(String tenantId) {
		Set<String> set = new HashSet<String>();
		synchronized (mutex) {
			set.addAll(activeAgentChannelList.keySet());
		}
		return set;
	}

	public ActiveAgent getActiveAgentByChannelId(String tenantId,
			String channelId) {
		ActiveAgent agent = null;
		if (loggedInAgentList.containsKey(tenantId)) {
			synchronized (mutex) {
				Map<String, ActiveAgent> activeList = loggedInAgentList
						.get(tenantId);
				if (null != activeList) {
					agent = activeList.get(channelId);
				}
			}
		}

		if (lgr.isLoggable(Level.INFO)) {
			lgr.info("Returning Agent " + agent + " for channel Id "
					+ channelId + " and tenant Id " + tenantId);
		}

		return agent;
	}

	public ActiveAgent getActiveAgentByAgentId(String agentId) {
		ActiveAgent agent = null;
		if (activeAgentChannelList.containsKey(agentId)) {
			synchronized (mutex) {
				Set<String> channelSet = activeAgentChannelList.get(agentId);
				if (null != channelSet) {
					for (String channelId : channelSet) {
						for (Map.Entry<String, ConcurrentHashMap<String, ActiveAgent>> entry : loggedInAgentList
								.entrySet()) {
							if (entry.getValue() != null) {
								agent = entry.getValue().get(channelId);
								if (agent != null)
									break;
							}
						}
						if (agent != null)
							break;
					}
				}
			}
		}
		if (lgr.isLoggable(Level.INFO)) {
			lgr.info("Returning Agent " + agent + " for agent Id " + agentId);
		}
		return agent;
	}

	public ActiveAgent getActiveAgentByAgentId(String tenantId, String agentId) {
		ActiveAgent agent = null;
		if (activeAgentChannelList.containsKey(agentId)
				&& loggedInAgentList.containsKey(tenantId)) {
			synchronized (mutex) {
				// <channelId, ActiveAgent>
				Map<String, ActiveAgent> activeList = loggedInAgentList
						.get(tenantId);
				// <channelIdSet>
				Set<String> channelSet = activeAgentChannelList.get(agentId);
				if (null != channelSet && null != activeList) {
					for (String channelId : channelSet) {
						agent = activeList.get(channelId);
						if (agent != null)
							break;
					}
				}
			}
		}
		if (lgr.isLoggable(Level.INFO)) {
			lgr.info("Returning Agent " + agent + " for agent Id " + agentId
					+ " and tenant Id " + tenantId);
		}
		return agent;
	}

	public ActiveAgent getActiveAgentByAgentIdAndChannel(String tenantId, String agentId, String channelType) {
		ActiveAgent agent = null;
		if (activeAgentChannelList.containsKey(agentId)
				&& loggedInAgentList.containsKey(tenantId)) {
			synchronized (mutex) {
				// <channelId, ActiveAgent>
				Map<String, ActiveAgent> activeList = loggedInAgentList
						.get(tenantId);
				// <channelIdSet>
				Set<String> channelSet = activeAgentChannelList.get(agentId);
				if (null != channelSet && null != activeList) {
					for (String channelId : channelSet) {
						agent = activeList.get(channelId);
						if (agent != null && agent.getChannelType().equalsIgnoreCase(channelType))
							break;
					}
				}
			}
		}
		if (lgr.isLoggable(Level.INFO)) {
			lgr.info("Returning telephony Agent " + agent + " for agent Id " + agentId
					+ " and tenant Id " + tenantId);
		}
		return agent;
	}
	
	public Set<ActiveAgent> getActiveAgentsByAgentId(String tenantId,
			String agentId) {

		Set<ActiveAgent> set = new HashSet<ActiveAgent>();
		if (activeAgentChannelList.containsKey(agentId)
				&& loggedInAgentList.containsKey(tenantId)) {
			synchronized (mutex) {
				// <channelId, ActiveAgent>
				Map<String, ActiveAgent> activeList = loggedInAgentList
						.get(tenantId);
				// <channelIdSet>
				Set<String> channelSet = activeAgentChannelList.get(agentId);
				if (null != channelSet && null != activeList) {
					for (String channelId : channelSet) {
						ActiveAgent agent = activeList.get(channelId);
						set.add(agent);
					}
				}
			}
		}
		return set;
	}

	public Set<ActiveAgent> getActiveAgentsByTeam(String tenantId, String teamId) {
		Set<ActiveAgent> activeAgentSet = new HashSet<ActiveAgent>();
		Set<String> agentIdSet = new HashSet<String>();
		if (loggedInAgentList.containsKey(tenantId)) {
			synchronized (mutex) {
				Map<String, ActiveAgent> activeList = loggedInAgentList
						.get(tenantId);
				if (activeList != null) {
					for (Map.Entry<String, ActiveAgent> entry : activeList
							.entrySet()) {
						ActiveAgent aagent = entry.getValue();
						if (aagent != null && teamId.equals(aagent.getTeamId())
								&& !agentIdSet.contains(aagent.getAgentId())) {
							activeAgentSet.add(aagent);
							agentIdSet.add(aagent.getAgentId());
						}
					}
				}
			}
		}
		return activeAgentSet;
	}

	public boolean checkIfActiveAgentForSessionId(String sessionId) {
		boolean result = false;
		HashMap<String, ActiveAgent> map = new HashMap<String, ActiveAgent>();
		for (String key : loggedInAgentList.keySet()) {
			synchronized (mutex) {
				Map<String, ActiveAgent> activeList = loggedInAgentList
						.get(key);
				map.putAll(activeList);
			}
			if (map != null) {
				for (String index : map.keySet()) {
					ActiveAgent aa = map.get(index);
					if (aa.getAgentSessionId().equals(sessionId)) {
						result = true;
						break;
					}
				}
			}
			if (result)
				break;
		}// for
		return result;
	}

	/**
	 * @return
	 */
	public List<String> getAllAcgUrls() {
		List<String> urlList = new ArrayList<String>();

		synchronized (mutex) {
			for (Map<String, ActiveAgent> activeAgentMap : loggedInAgentList
					.values()) {
				for (ActiveAgent agent : activeAgentMap.values()) {
					String url = agent.getAcgUrl();
					if (!urlList.contains(url)) {
						urlList.add(url);
					}
				}
			}
		}

		return urlList;
	}

	public boolean isAgentLoggedIn(String tenantId, String agentId) {
		boolean loggedIn = false;
		if (activeAgentChannelList.containsKey(agentId)) {
			loggedIn = true;
		}
		return loggedIn;
	}

	/**
	 * @param aaList
	 */
	public void recoverAgents(List<ActiveAgent> aaList) {
		// Note that its not synchronized because this method should only be
		// called at startup.
		if (aaList != null) {
			if (lgr.isLoggable(Level.INFO)) {
				lgr.info("Recovering Agents: " + aaList);
			}

			for (ActiveAgent agent : aaList) {
				String tenantId = agent.getTenantId();

				ConcurrentHashMap<String, ActiveAgent> agentMap = null;
				if (loggedInAgentList.containsKey(tenantId)) {
					agentMap = loggedInAgentList.get(tenantId);
				} else {
					agentMap = new ConcurrentHashMap<String, ActiveAgent>();
					loggedInAgentList.put(tenantId, agentMap);
				}
				agentMap.put(agent.getChannelId(), agent);

				// update second level cache that maintains active channelset
				// per agent
				HashSet<String> activeChannelSet = null;
				if (activeAgentChannelList.containsKey(agent.getAgentId())) {
					activeChannelSet = activeAgentChannelList.get(agent
							.getAgentId());
				} else {
					activeChannelSet = new HashSet<String>();
					activeAgentChannelList.put(agent.getAgentId(),
							activeChannelSet);
				}
				
				activeChannelSet.add(agent.getChannelId());

				// update the mediaProfile-agent relation
				addAgentMediaProfile(agent.getAgentId(), agent.getTeamId(),
						agent.getSiteId());
			}
		}
	}

	/**
	 * @param delay
	 * @return
	 */
	public List<ActiveAgent> getAgentsWithNoActivityFor(int delay) {
		List<ActiveAgent> aaList = new ArrayList<ActiveAgent>();

		long timestamp = System.currentTimeMillis();

		List<String> tenantIdList = getTenantIds();
		for (String tenantId : tenantIdList) {
			ConcurrentHashMap<String, ActiveAgent> agentMap = loggedInAgentList
					.get(tenantId);
			for (ActiveAgent agent : agentMap.values()) {
				if ((agent.getTimestamp() + delay) <= timestamp) {
					aaList.add(agent);
				}
			}
		}

		if (lgr.isLoggable(Level.INFO)) {
			lgr.info("Agents with no activity for " + delay + " millisecs: "
					+ aaList);
		}

		return aaList;
	}

	/**
	 * @param cmgr
	 */
	public void logoutShutdownTenantAgents(ConfigurationManager cmgr) {
		ACGNotificationManager notifMgr = (ACGNotificationManager) AgentInformationManagerSystem
				.findBean("acgNotificationManager");
		ACGNotification notif = new LogoutNotification();
		String logoutContext = cmgr.getAgentInformationManager()
				.getLogoutAcgContext();
		List<String> idList = cmgr.getShutdownTenantIdList();

		if (lgr.isLoggable(Level.INFO)) {
			lgr.info("Tenants in shutdown state: " + idList);
		}

		for (String tenantId : idList) {
			HashMap<String, ActiveAgent> aaMap = getActiveAgents(tenantId);

			if (lgr.isLoggable(Level.FINE)) {
				lgr.fine("Agents for tenant " + tenantId + " that are active: "
						+ aaMap);
			}

			for (ActiveAgent agent : aaMap.values()) {
				if (!cmgr
						.isSubStatusAllowed(tenantId, agent.getChannelStatus())) {
					// Send the logout notification
					lgr.warning("Logging out agent cos the "
							+ "tenant is in shutdown mode: " + agent);
					String url = agent.getAcgUrl() + logoutContext
							+ "?agentId=" + agent.getAgentId() + "&channelId="
							+ agent.getChannelId() + "&enterpriseId="
							+ agent.getTenantId() + "&sessionId="
							+ agent.getAgentSessionId() + "&reason="
							+ "Tenant+is+in+shutdown+mode";
					notif.addAcgUrl(url);
					notif.setMethod(ACGNotification.METHOD_GET);
					removeAgent(tenantId, agent.getAgentId(),
							agent.getChannelId(), true);
				}// if isSubStatusAllowed
			}// for
		}
		if (notif.getAcgUrls() != null) {
			notifMgr.sendNotification(notif);
		}
	}

	public Set<String> getActiveChannelForAgent(String agentId) {

		Set<String> retChannelList = null;

		if (activeAgentChannelList.containsKey(agentId)) {
			retChannelList = activeAgentChannelList.get(agentId);
		}

		return retChannelList;

	}

	public Set<String> getActiveAgentsForMediaProfile(String profileId) {
		return activeMediaProfileList.get(profileId);
	}

	private void addAgentMediaProfile(String agentId, String teamId,
			String siteId) {
		ProvisioningManager pmgr = (ProvisioningManager) AgentInformationManagerSystem
				.findBean("provManager");
		AgentMediaProfileHandler amph = pmgr.getAgentMediaProfileHandler();
		String profileId = amph.getAgentMediaProfileIdByAgent(agentId, teamId,
				siteId);
		HashSet<String> agentSet = activeMediaProfileList.get(profileId);
		if (StringUtils.hasText(profileId)) {
			if (null == agentSet) {
				agentSet = new HashSet<String>();
				activeMediaProfileList.put(profileId, agentSet);
			}
			agentSet.add(agentId);
		} else {
			lgr.fine("add case ... couldn't find media profile id for agent "
					+ agentId);
		}
	}

	private void removeAgentMediaProfile(String agentId, String teamId,
			String siteId) {
		ProvisioningManager pmgr = (ProvisioningManager) AgentInformationManagerSystem
				.findBean("provManager");
		AgentMediaProfileHandler amph = pmgr.getAgentMediaProfileHandler();
		String profileId = amph.getAgentMediaProfileIdByAgent(agentId, teamId,
				siteId);
		HashSet<String> agentSet = activeMediaProfileList.get(profileId);
		if (StringUtils.hasText(profileId)) {
			if (null != agentSet) {
				agentSet.remove(agentId);
			}
		} else {
			lgr.fine("remove case ... couldn't find media profile id for agent "
					+ agentId);
			for (Map.Entry<String, HashSet<String>> entry : activeMediaProfileList
					.entrySet()) {
				if (entry.getValue().contains(agentId)) {
					entry.getValue().remove(agentId);
				}
			}
		}
	}

	public void addInprogressAgentSession(String agentId, String sessionId) {
		if (lgr.isLoggable(Level.FINER)) {
			lgr.info("Adding agent " + agentId + " sessionid " + sessionId
					+ "' to in-progress session list");
		}

		inprogressAgentSession.put(agentId, sessionId);
	}

	public String getInprogressAgentSession(String agentId) {
		if (lgr.isLoggable(Level.FINER)) {
			lgr.info("Getting agent " + agentId
					+ " from in-progress session list");
		}
		return inprogressAgentSession.get(agentId);
	}

	public void removeInprogressAgentSession(String agentId, String sessionId) {
		if (lgr.isLoggable(Level.FINER)) {
			lgr.finer("Removing agent " + agentId + " sessionid " + sessionId
					+ "' from in-progress session list");
		}

		inprogressAgentSession.remove(agentId);
	}
}
