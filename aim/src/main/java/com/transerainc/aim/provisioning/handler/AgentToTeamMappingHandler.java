/**
 * 
 */
package com.transerainc.aim.provisioning.handler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.transerainc.aim.pojo.AgentTeamMapping;
import com.transerainc.aim.store.AgentToTeamMappingStore;

/**
 * @author <a href="mailto:ramesh.bobba@transerainc.com">Ramesh Bobba</a>
 * @version $Rivision:$
 */
public class AgentToTeamMappingHandler {
	private static Logger lgr =
			Logger.getLogger(AgentToTeamMappingHandler.class.getName());
	private HashMap<String, List<AgentTeamMapping>> agentToTeamMappingMap;
	private HashMap<String, String> idToAgentMap;

	public AgentToTeamMappingHandler(String dataDir) {
		AgentToTeamMappingStore attmStore =
				new AgentToTeamMappingStore(dataDir);

		agentToTeamMappingMap = attmStore.retrieveAgentToTeamMappings();

		mapIdToAgents(agentToTeamMappingMap);
	}

	public AgentToTeamMappingHandler(
			HashMap<String, List<AgentTeamMapping>> attMap, String dataDir) {
		this.agentToTeamMappingMap = attMap;

		AgentToTeamMappingStore attmStore =
				new AgentToTeamMappingStore(dataDir);
		attmStore.storeAgentToTeamMappings(agentToTeamMappingMap);
		mapIdToAgents(agentToTeamMappingMap);
	}

	public List<AgentTeamMapping> getMappingsForAgent(String agentId) {
		List<AgentTeamMapping> teamList = agentToTeamMappingMap.get(agentId);

		if (lgr.isLoggable(Level.INFO)) {
			lgr.info("Returning team list for agent (" + agentId + "): "
				+ teamList);
		}

		return teamList;
	}

	public String getAgentIdFor(String id) {
		return idToAgentMap.get(id);
	}

	public List<String> getAgentIdsFor(List<String> idList) {
		List<String> theList = new ArrayList<String>();

		for (String id : idList) {
			String agentId = getAgentIdFor(id);
			if (agentId != null) {
				theList.add(agentId);
			}
		}

		return theList;
	}

	public List<AgentTeamMapping> getMappingsForIds(List<String> idList) {
		List<AgentTeamMapping> atMappingList =
				new ArrayList<AgentTeamMapping>();

		for (String id : idList) {
			String agentId = idToAgentMap.get(id);
			if (agentId != null) {
				List<AgentTeamMapping> lst = getMappingsForAgent(agentId);
				if (lst != null) {
					atMappingList.addAll(lst);
				}
			}
		}

		if (lgr.isLoggable(Level.INFO)) {
			lgr.info("Returning agent team mappings " + atMappingList
				+ " for id list " + idList);
		}

		return atMappingList;
	}

	/**
	 * @param agentToTeamMappingMap2
	 */
	private void mapIdToAgents(HashMap<String, List<AgentTeamMapping>> rootMap) {
		HashMap<String, String> mappings = new HashMap<String, String>();
		for (String agentId : rootMap.keySet()) {
			List<AgentTeamMapping> atMappingList = rootMap.get(agentId);
			if (atMappingList != null) {
				for (AgentTeamMapping atMapping : atMappingList) {
					String id = atMapping.getId();
					mappings.put(id, agentId);
				}
			}
		}

		if (lgr.isLoggable(Level.CONFIG)) {
			lgr.config("ID to Agent Mappings: " + mappings);
		}

		idToAgentMap = mappings;
	}
}
