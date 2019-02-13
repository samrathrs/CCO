/**
 * 
 */
package com.transerainc.aim.provisioning.handler;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.transerainc.aim.pojo.Agent;
import com.transerainc.aim.store.AgentStore;

/**
 * @author <a href="mailto:ramesh.bobba@transerainc.com">Ramesh Bobba</a>
 * @version $Rivision:$
 */
public class AgentHandler {
	private static Logger lgr = Logger.getLogger(AgentHandler.class.getName());
	private HashMap<String, Agent> agentMap;
	private HashMap<String, HashMap<String, String>> loginToIdMap;
	private HashMap<String, List<String>> tenantToAgentIdMap;

	public AgentHandler(String dataDir) {
		AgentStore aStore = new AgentStore(dataDir);

		agentMap = aStore.retrieveAgents();

		// Create a login to ID mapping
		createLoginToIdMapping();

		if (lgr.isLoggable(Level.CONFIG)) {
			lgr.config("Login To Id Mapping: " + loginToIdMap);
		}

		createTenantToAgentIdMapping();

		if (lgr.isLoggable(Level.CONFIG)) {
			lgr.config("Tenant To Agent Mapping: " + tenantToAgentIdMap);
		}
	}

	public AgentHandler(HashMap<String, Agent> agentMap, String dataDir) {
		this.agentMap = agentMap;

		// Create a login to ID mapping
		createLoginToIdMapping();

		if (lgr.isLoggable(Level.CONFIG)) {
			lgr.config("Login To Id Mapping: " + loginToIdMap);
		}

		createTenantToAgentIdMapping();

		if (lgr.isLoggable(Level.CONFIG)) {
			lgr.config("Tenant To Agent Mapping: " + tenantToAgentIdMap);
		}

		AgentStore aStore = new AgentStore(dataDir);
		aStore.storeAgents(agentMap);
	}

	public Agent getAgent(String id) {
		Agent agent = agentMap.get(id);

		if (lgr.isLoggable(Level.INFO)) {
			lgr.info("Lookup for agent " + id + " returned the following - "
				+ agent);
		}

		return agent;
	}

	public String getAgentIdForLogin(String login, String tenantId) {
		HashMap<String, String> tenantMap = loginToIdMap.get(login);

		String agentId = null;
		if (tenantMap != null) {
			agentId = tenantMap.get(tenantId);
		}

		if (lgr.isLoggable(Level.INFO)) {
			lgr.info("Lookup for login " + login
				+ " returned the following agent id " + agentId);
		}

		return agentId;
	}

	public Agent getAgentForLogin(String login, String tenantId) {
		String agentId = getAgentIdForLogin(login, tenantId);

		Agent agent = null;
		if (agentId != null) {
			agent = agentMap.get(agentId);
		} else {
			lgr.warning("No agent found for login " + login);
		}

		if (lgr.isLoggable(Level.INFO)) {
			lgr.info("Lookup for login " + login
				+ " returned the following agent " + agent);
		}

		return agent;
	}

	public Collection<String> getLogins() {
		Collection<String> keys = loginToIdMap.keySet();

		if (lgr.isLoggable(Level.INFO)) {
			lgr.info("Lookup for login list returned - " + keys);
		}

		return keys;
	}

	public Collection<String> getAgentIds() {
		Collection<String> keys = agentMap.keySet();

		if (lgr.isLoggable(Level.INFO)) {
			lgr.info("Lookup for agent ids returned - " + keys);
		}

		return keys;
	}

	public List<String> getAgentIdsForTenant(String tenantId) {
		List<String> idList = tenantToAgentIdMap.get(tenantId);

		if (lgr.isLoggable(Level.INFO)) {
			lgr.info("Lookup for tenant " + tenantId
				+ " returned the following agent ids - " + idList);
		}

		return idList;
	}

	public List<Agent> getAgentsForTenant(String tenantId) {
		List<String> idList = getAgentIdsForTenant(tenantId);

		List<Agent> agentList = new ArrayList<Agent>();
		if (idList != null) {
			for (String id : idList) {
				Agent agent = agentMap.get(id);

				agentList.add(agent);
			}
		} else {
			lgr.warning("Cannot find agents for tenant " + tenantId);
		}

		if (lgr.isLoggable(Level.INFO)) {
			lgr.info("Lookup for tenant " + tenantId
				+ " returned the following agents - " + agentList);
		}

		return agentList;
	}

	public HashMap<String, Agent> getAgentForTenantAndAgentId(String tenantId, Collection<String>agentIdList){
		HashMap<String, Agent> map = new HashMap<String, Agent>();
		List<String>agentList =  getAgentIdsForTenant(tenantId);
		for(String agentId: agentIdList){
			if(agentList.contains(agentId))
					map.put(agentId, agentMap.get(agentId));
		}
		return map;
	}
	
	public HashMap<String, Agent> getAgentForTenantAndSiteId(String tenantId, Collection<String>siteIdList){
        HashMap<String, Agent> agentMap = new HashMap<String, Agent>();
        List<Agent>agentList =  getAgentsForTenant(tenantId);
		for(Agent agent: agentList){
			if(siteIdList.contains(agent.getSiteId())){
				agentMap.put(agent.getAgentId(), agent);
			}
		}
		return agentMap;
	}
	/**
	 * 
	 */
	private void createTenantToAgentIdMapping() {
		this.tenantToAgentIdMap = new HashMap<String, List<String>>();
		for (String agentId : agentMap.keySet()) {
			Agent agent = agentMap.get(agentId);
			String tenantId = agent.getTenantId();
			if (lgr.isLoggable(Level.FINEST)) {
				lgr
						.finest("Adding agent " + agentId + " to tenant "
							+ tenantId);
			}

			List<String> agentIdList = null;
			if (tenantToAgentIdMap.containsKey(tenantId)) {
				agentIdList = tenantToAgentIdMap.get(tenantId);
			} else {
				agentIdList = new ArrayList<String>();
				tenantToAgentIdMap.put(tenantId, agentIdList);
			}

			agentIdList.add(agentId);
		}
	}

	/**
	 * 
	 */
	private void createLoginToIdMapping() {
		this.loginToIdMap = new HashMap<String, HashMap<String, String>>();
		for (String agentId : agentMap.keySet()) {
			Agent agent = agentMap.get(agentId);

			String login = agent.getLogin();
			String tenantId = agent.getTenantId();

			HashMap<String, String> tenantMap = null;
			if (loginToIdMap.containsKey(login)) {
				tenantMap = loginToIdMap.get(login);
			} else {
				tenantMap = new HashMap<String, String>();
				loginToIdMap.put(login, tenantMap);
			}

			tenantMap.put(tenantId, agentId);
		}
	}
}
