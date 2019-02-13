/**
 * 
 */
package com.transerainc.aim.lookup;

import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;

import org.apache.ws.jaxme.impl.JMMarshallerImpl;
import org.springframework.util.StringUtils;

import com.transerainc.aim.pojo.ActiveAgent;
import com.transerainc.aim.pojo.Agent;
import com.transerainc.aim.pojo.Team;
import com.transerainc.aim.provisioning.ProvisioningManager;
import com.transerainc.aim.provisioning.handler.AgentHandler;
import com.transerainc.aim.provisioning.handler.TeamHandler;
import com.transerainc.aim.runtime.ActiveAgentManager;
import com.transerainc.aim.system.AgentInformationManagerSystem;
import com.transerainc.aim.tpgintf.ActiveAgentListType;
import com.transerainc.aim.tpgintf.ActiveAgentType;
import com.transerainc.aim.tpgintf.impl.ActiveAgentImpl;
import com.transerainc.aim.tpgintf.impl.ActiveAgentListImpl;

/**
 * @author <a href="mailto:ramesh.bobba@transerainc.com">Ramesh Bobba</a>
 * @version $Rivision:$
 */

public class LookupHelper {
	private static final Logger lgr = Logger.getLogger(LookupHelper.class
			.getName());

	/**
	 * @param agentId
	 * @param tenantId
	 * @param teamId
	 * @param state
	 * @param acgUrl
	 * @return
	 */
	public static Lookup getLookup(String agentId, String tenantId,
			String teamId, String state, String acgUrl, String channelType) {
		Lookup lookup = new Lookup();
		lookup.setAgentId(agentId);
		lookup.setTenantId(tenantId);

		if (teamId != null && teamId.length() > 0) {
			String[] tokens = teamId.split(",");
			List<String> list = Arrays.asList(tokens);
			lookup.setTeamIdList(list);
		}

		if (state != null && state.length() > 0) {
			String[] tokens = state.split(",");
			List<String> list = Arrays.asList(tokens);
			lookup.setSubStatusList(list);
		}

		if (acgUrl != null && acgUrl.length() > 0) {
			String[] tokens = acgUrl.split(",");
			List<String> list = Arrays.asList(tokens);
			lookup.setAcgUrlList(list);
		}

		lookup.setChannelType(channelType);

		if (lgr.isLoggable(Level.INFO)) {
			lgr.info("Got a lookup for Agent " + agentId + ", Tenant "
					+ tenantId + ", Teams " + teamId + ", SubStatus' " + state
					+ ", and ACG URLs " + acgUrl + "Channel Type "
					+ channelType + ", Returning Lookup Object: " + lookup);
		}

		return lookup;
	}

	/**
	 * @param agentId
	 * @param tenantId
	 * @param teamId
	 */
	public static List<ActiveAgent> getAgentsMatchingLookup(Lookup lookup) {
		ActiveAgentManager aaMgr = (ActiveAgentManager) AgentInformationManagerSystem
				.findBean("activeAgentManager");

		if (lgr.isLoggable(Level.INFO)) {
			lgr.info("Looking up agents that match the criteria: " + lookup);
		}

		List<ActiveAgent> aaList = new ArrayList<ActiveAgent>();
		Set<String> buddyAgentSet = new HashSet<String>();
		String tenantId = lookup.getTenantId();
		if (StringUtils.hasText(lookup.getAgentId())) {
			lgr.info("Got request for agent list with agent id ...");
			String agentId = lookup.getAgentId();
			String channelType = lookup.getChannelType();
			ActiveAgent agent = null;
			if (tenantId != null) {
				if(channelType.equalsIgnoreCase("telephony")){
					agent = aaMgr.getActiveAgentByAgentIdAndChannel(tenantId, agentId,channelType);
				}else
					agent = aaMgr.getActiveAgentByAgentId(tenantId, agentId);
				if (agent != null) {
					aaList.add(agent);
				}
			} else {
				List<String> tenantIdList = aaMgr.getTenantIds();
				for (String tId : tenantIdList) {
					if(channelType.equalsIgnoreCase("telephony")){
						agent = aaMgr.getActiveAgentByAgentIdAndChannel(tId, agentId,channelType);
					}else
						agent = aaMgr.getActiveAgentByAgentId(tId, agentId);
					if (agent != null) {
						aaList.add(agent);
						break;
					}
				}
			}
		} else {
			if (StringUtils.hasText(tenantId)) {
				// Suresh : This request is for buddy list
				HashMap<String, ActiveAgent> agentMap = aaMgr
						.getActiveAgents(tenantId);

				for (ActiveAgent agent : agentMap.values()) {
					boolean urlMatches = doesAcgUrlMatch(agent, lookup);
					boolean teamMatches = doesTeamMatch(agent, lookup);
					boolean subStatusMatches = doesSubStatusMatch(agent, lookup);

					if (lgr.isLoggable(Level.INFO)) {
						lgr.info("Agent id: " + agent.getAgentId()
								+ "Criteria match result:" + " url:"
								+ urlMatches + ", team:" + teamMatches + ", "
								+ "subStatus: " + subStatusMatches);
					}
					// Suresh : Setting unique agents to aaList
					if (urlMatches && teamMatches && subStatusMatches
							&& !buddyAgentSet.contains(agent.getAgentId())) {
						aaList.add(agent);
						buddyAgentSet.add(agent.getAgentId());
					}
				}
			} else {
				// Suresh : This is for peer aim synch up
				List<String> tenantIdList = aaMgr.getTenantIds();
				lgr.fine("Tenant id list : " + tenantIdList);
				for (String tId : tenantIdList) {
					HashMap<String, ActiveAgent> agentMap = aaMgr
							.getActiveAgents(tId);
					for (ActiveAgent agent : agentMap.values()) {
						boolean urlMatches = doesAcgUrlMatch(agent, lookup);
						boolean teamMatches = doesTeamMatch(agent, lookup);
						boolean subStatusMatches = doesSubStatusMatch(agent,
								lookup);

						if (lgr.isLoggable(Level.FINER)) {
							lgr
									.finer("Agent "
											+ agent
											+ " has the following matching the criteria "
											+ lookup + ", URL: " + urlMatches
											+ ", Team: " + teamMatches
											+ ", Status: " + subStatusMatches);
						}

						if (urlMatches && teamMatches && subStatusMatches) {
							aaList.add(agent);
						}
					}
				}
			}
		}

		if (lgr.isLoggable(Level.FINE)) {
			lgr.fine("Returning matching agents for lookup " + lookup + ": "
					+ aaList);
		}

		return aaList;
	}

	/**
	 * @param agent
	 * @param lookup
	 * @return
	 */
	private static boolean doesSubStatusMatch(ActiveAgent agent, Lookup lookup) {
		boolean b = true;

		List<String> subStatusList = lookup.getSubStatusList();
		if (subStatusList != null) {
			b = false;
			if (subStatusList.contains(agent.getChannelStatus())
					&& lookup.getChannelType().equals(agent.getChannelType())) {
				b = true;
			}
		}
		return b;
	}

	/**
	 * @param agent
	 * @param lookup
	 * @return
	 */
	private static boolean doesTeamMatch(ActiveAgent agent, Lookup lookup) {
		boolean b = true;

		List<String> teamList = lookup.getTeamIdList();
		if (teamList != null) {
			if (!teamList.contains(agent.getTeamId())) {
				b = false;
			}
		}

		return b;
	}

	/**
	 * @param agent
	 * @param lookup
	 * @return
	 */
	private static boolean doesAcgUrlMatch(ActiveAgent agent, Lookup lookup) {
		boolean b = true;

		List<String> urlList = lookup.getAcgUrlList();
		if (urlList != null) {
			if (!urlList.contains(agent.getAcgUrl())) {
				b = false;
			}
		}

		return b;
	}

	/**
	 * @param agentList
	 * @return
	 * @throws JAXBException
	 */
	@SuppressWarnings("unchecked")
	public static String getActiveAgentsAsXML(List<ActiveAgent> agentList)
			throws JAXBException {
		long ts1 = System.currentTimeMillis();
		ActiveAgentListType aalType = new ActiveAgentListImpl();

		ProvisioningManager pMgr = (ProvisioningManager) AgentInformationManagerSystem
				.findBean("provManager");

		AgentHandler agentHandler = pMgr.getAgentHandler();
		TeamHandler teamHandler = pMgr.getTeamHandler();

		for (ActiveAgent agent : agentList) {
			Team provTeam = teamHandler.getTeam(agent.getTeamId());
			if (provTeam == null) {
				lgr.warning("Skipping agent that does not have valid team "
						+ agent);
				continue;
			}
			ActiveAgentType aaType = new ActiveAgentImpl();
			aaType.setAcgUrl(agent.getAcgUrl());
			aaType.setAgentId(agent.getAgentId());
			aaType.setChannelId(agent.getChannelId());
			aaType.setChannelStatus(agent.getChannelStatus());
			// backward compatibility
			aaType.setSubStatus(agent.getChannelStatus());
			aaType.setChannelType(agent.getChannelType());
			aaType.setTeamId(agent.getTeamId());
			aaType.setTimestamp(agent.getTimestamp());
			aaType.setTenantId(agent.getTenantId());
			aaType.setAgentSessionId(agent.getAgentSessionId());
			aaType.setDn(agent.getDn());
			aaType.setSiteId(agent.getSiteId());
			aaType.setExternalIpAddress(agent.getExternalIpAddress());
			aaType.setHostIpAddress(agent.getHostIpAddress());
			
			Agent provAgent = agentHandler.getAgent(agent.getAgentId());
			aaType.setAgentName(provAgent.getAgentName());
			aaType.setTeamName(provTeam.getTeamName());

			aalType.getActiveAgent().add(aaType);
		}

		aalType.setCount(aalType.getActiveAgent().size());

		long ts2 = System.currentTimeMillis();
		JAXBContext context = JAXBContext
				.newInstance("com.transerainc.aim.tpgintf");

		long ts3 = System.currentTimeMillis();
		StringWriter sw = new StringWriter();
		Marshaller marshaller = context.createMarshaller();
		marshaller.setProperty(Marshaller.JAXB_ENCODING, "UTF-8");
		marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.FALSE);
		marshaller.setProperty(JMMarshallerImpl.JAXME_XML_DECLARATION,
				Boolean.TRUE);
		marshaller.marshal(aalType, sw);
		long ts4 = System.currentTimeMillis();

		lgr.info("Time Taken::::::" + (ts2 - ts1) + ", " + (ts3 - ts2) + ", "
				+ (ts4 - ts3));

		return sw.toString();
	}
}
