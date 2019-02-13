package com.transerainc.aim.provisioning.handler;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.springframework.util.StringUtils;

import com.transerainc.aim.pojo.ThirdPartyProfile;
import com.transerainc.aim.pojo.ThirdPartyProfileAddInfo;
import com.transerainc.aim.provisioning.ProvisioningManager;
import com.transerainc.aim.store.AgentTPProfileStore;
import com.transerainc.aim.system.AgentInformationManagerSystem;

public class ThirdPartyProfileHandler {
	private static Logger lgr = Logger.getLogger(ThirdPartyProfileHandler.class
			.getName());
	private HashMap<String, ThirdPartyProfile> tpProfileMap;
	private HashMap<String, ThirdPartyProfileAddInfo> tpProfileAddInfoMap;
	private HashMap<String, HashMap<String, String>> entityProfileMap;
	
	private static final String ENTITY_SITE = "3";
	private static final String ENTITY_TEAM = "4";
	private static final String ENTITY_AGENT = "5";
	
	public ThirdPartyProfileHandler(String dataDir) {
		AgentTPProfileStore apStore = new AgentTPProfileStore(dataDir);
		
		tpProfileMap = apStore.retrieveAgentTPProfiles();
		tpProfileAddInfoMap = apStore.retrieveAgentTPProfileAddInfo();
		entityProfileMap = apStore.retrieveEntityTPProfileMapping();
	}

	public ThirdPartyProfileHandler(HashMap<String, ThirdPartyProfile> atpProfileMap,
			HashMap<String, ThirdPartyProfileAddInfo> atpProfileAddMap,
			HashMap<String, HashMap<String, String>> entityProfileMap,
			String dataDir) {
		this.tpProfileMap = atpProfileMap;
		this.tpProfileAddInfoMap = atpProfileAddMap;
		this.entityProfileMap = entityProfileMap == null ? new HashMap<String, HashMap<String, String>>()
				: entityProfileMap;
		AgentTPProfileStore apStore = new AgentTPProfileStore(dataDir);
		apStore.storeAgentTPProfiles(this.tpProfileMap);
		apStore.storeAgentTPProfilesAddInfo(this.tpProfileAddInfoMap);
		apStore.storeEntityTPProfileMapping(this.entityProfileMap);
	}

	public ThirdPartyProfile getAgentTPProfile(String tpProfileId) {
		ThirdPartyProfile tpProfile = tpProfileMap.get(tpProfileId);
		if (lgr.isLoggable(Level.INFO)) {
			lgr.info("Lookup for agent third party profile " + tpProfileId
					+ " returned - " + tpProfile);
		}
		return tpProfile;
	}

	
	public ThirdPartyProfileAddInfo getAgentTPAdditionalInfoById(String tpProfileId) {
		ThirdPartyProfileAddInfo tpProfile = tpProfileAddInfoMap.get(tpProfileId);
		if (lgr.isLoggable(Level.INFO)) {
			lgr.info("Lookup for agent third party profile additonal info " + tpProfileId
					+ " returned - " + tpProfile);
		}
		return tpProfile;
	}

	public ThirdPartyProfile getAgentTPProfileByAgent(String agentId,String siteId) {
		ThirdPartyProfile matchingProfile = null;
		String profileId = "";

		if (lgr.isLoggable(Level.FINE)) {
			lgr.fine("Got request for third party profile " + "with agentId : "
					+ agentId + " and siteId : " + siteId);
		}

		// check profile id in agentEntityMap
		HashMap<String, String> agentToProfileMap = this.entityProfileMap
				.get(ENTITY_AGENT);
		HashMap<String, String> siteToProfileMap = this.entityProfileMap
				.get(ENTITY_SITE);		
		if (null != agentToProfileMap && agentToProfileMap.containsKey(agentId)) {
			profileId = agentToProfileMap.get(agentId);
			if (lgr.isLoggable(Level.FINE)) {
				lgr.fine("Found a matching third party profile "
						+ "at the agent level for agentid: " + agentId + " ,profileid: "
						+ profileId);
			}
		} else if (null != siteToProfileMap
				&& siteToProfileMap.containsKey(siteId)) {
			ProvisioningManager pmgr = (ProvisioningManager) AgentInformationManagerSystem
					.findBean("provManager");
			if(pmgr.getAgentMediaProfileHandler().isDefaultTelephony(agentId))
			{
				lgr.info("This is a default tel profile - MKOP");
				return null;
			}
			profileId = siteToProfileMap.get(siteId);
			if (lgr.isLoggable(Level.FINE)) {
				lgr.fine("Found a matching third party profile "
						+ "at the site level for agent " + agentId
						+ " in site " + siteId + ":"
						+ profileId);
			}
		}else{
			lgr.warning("Found no matching third party profile "
					+ "for agent id" + agentId
					+ " in site " + siteId );
			return null;
		}
		if (StringUtils.hasText(profileId)) {
			matchingProfile = this.tpProfileMap.get(profileId);
		}
		lgr.finest(" Profile to be returned: " + matchingProfile.toString());
		return matchingProfile;
	}

	public String getAgentTPProfileIdByAgent(String agentId,String siteId) {
		ThirdPartyProfile profile = getAgentTPProfileByAgent(agentId,siteId);
		return null != profile ? profile.getTpProfileId() : "";
	}

	public String getAgentTPProfileNameByAgent(String agentId, String teamId,
			String siteId) {
		ThirdPartyProfile profile = getAgentTPProfileByAgent(agentId, siteId);
		return null != profile ? "(" + profile.getTpProfileId() + ")"
				+ profile.getTpProfileName() : "";
	}

	public Set<ThirdPartyProfile> getAgentTPProfilesForTenant(String tenantId) {
		Set<ThirdPartyProfile> ampSet = new HashSet<ThirdPartyProfile>();
		for (Map.Entry<String, ThirdPartyProfile> profileEntry : tpProfileMap
				.entrySet()) {
			if (profileEntry.getValue() != null
					&& profileEntry.getValue().getTenantId().equals(tenantId)) {
				ampSet.add(profileEntry.getValue());
			}
		}
		return ampSet;
	}
	
	public HashMap<String, ThirdPartyProfileAddInfo> getAgentTPProfilesAddInfoForTenant(String tenantId) {
		HashMap<String, ThirdPartyProfileAddInfo> tpInfoMap = new HashMap<String, ThirdPartyProfileAddInfo>();
		for (String profileId : tpProfileAddInfoMap.keySet()) {
			ThirdPartyProfileAddInfo tppInfo = tpProfileAddInfoMap.get(profileId);
			if (tppInfo.getTenantId().equals(tenantId)) {
				tpInfoMap.put(tppInfo.getTpProfileId(), tppInfo);
			}
		}
		return tpInfoMap;
	}

	// This is used by admin gui
	public HashMap<String, HashMap<String, String>> getTPProfileAssignmentMap(
			String tenantId) {
		HashMap<String, HashMap<String, String>> map = new HashMap<String, HashMap<String, String>>();

		if (this.entityProfileMap != null) {
			HashMap<String, String> agentToProfileMap = this.entityProfileMap
					.get(ENTITY_AGENT);
			HashMap<String, String> siteToProfileMap = this.entityProfileMap
					.get(ENTITY_SITE);

			HashMap<String, String> entityMap = filterByTenant(tenantId,
					agentToProfileMap);
			map.put(ENTITY_AGENT, entityMap);
			entityMap = filterByTenant(tenantId, siteToProfileMap);
			map.put(ENTITY_SITE, entityMap);
		}
		return map;
	}

	/**
	 * @param tenantId
	 * @param agentToProfileMap
	 */
	private HashMap<String, String> filterByTenant(String tenantId,
			HashMap<String, String> entityToProfileMap) {
		HashMap<String, String> entityMap = new HashMap<String, String>();
		if (entityToProfileMap != null) {
			for (Map.Entry<String, String> entry : entityToProfileMap
					.entrySet()) {
				ThirdPartyProfile profile = tpProfileMap.get(entry.getValue());
				if (profile != null && profile.getTenantId().equals(tenantId)) {
					entityMap.put(entry.getKey(), entry.getValue());
				}
			}
		}
		return entityMap;
	}

	public void updateAgentTPProfile(Map<String,ThirdPartyProfile> atp, String dataDir) {
		this.tpProfileMap.putAll(atp);
		AgentTPProfileStore apStore = new AgentTPProfileStore(dataDir);
		apStore.storeAgentTPProfiles(this.tpProfileMap);

	}

	public void updateAgentTPProfileAdditionalInfo(Map<String,ThirdPartyProfileAddInfo> atp, String dataDir) {
		this.tpProfileAddInfoMap.putAll(atp);
		AgentTPProfileStore apStore = new AgentTPProfileStore(dataDir);
		apStore.storeAgentTPProfilesAddInfo(this.tpProfileAddInfoMap);
	}
	
	public void removeAgentTPProfile(String profileId, String dataDir) {
		this.tpProfileMap.remove(profileId);
		AgentTPProfileStore apStore = new AgentTPProfileStore(dataDir);
		apStore.storeAgentTPProfiles(this.tpProfileMap);
	}

	public HashMap<String, String> getAgentTPProfileAssignmentMap(
			String tenantId) {
		HashMap<String, String> map = new HashMap<String, String>();

		if (this.entityProfileMap != null) {
			HashMap<String, String> agentToProfileMap = this.entityProfileMap
					.get(ENTITY_AGENT);
			map = filterByTenant(tenantId, agentToProfileMap);
		}
		return map;
	}

	public Collection<String> getSiteTPProfileAssignmentMap(String profileId) {
		HashMap<String, String> map = new HashMap<String, String>();

		if (this.entityProfileMap != null) {
			HashMap<String, String> siteToProfileMap = this.entityProfileMap
					.get(ENTITY_SITE);
			map = filterByProfileId(profileId, siteToProfileMap);
		}
		return map.keySet(); // FIXME...verify
	}

	private HashMap<String, String> filterByProfileId(String profileId,
			HashMap<String, String> entityToProfileMap) {
		HashMap<String, String> entityMap = new HashMap<String, String>();
		if (entityToProfileMap != null) {
			for (Map.Entry<String, String> entry : entityToProfileMap
					.entrySet()) {
				ThirdPartyProfile profile = tpProfileMap.get(entry.getValue());
				if (profile != null
						&& profile.getTpProfileId().equals(profileId)) {
					entityMap.put(entry.getKey(), entry.getValue());
				}
			}
		}
		return entityMap;
	}

	public void updateTPProfileAssignment(String entityType,
			HashMap<String, String> entryMap, String dataDir) {
		HashMap<String, String> currMap = this.entityProfileMap.get(entityType);
		currMap.putAll(entryMap);
		this.entityProfileMap.put(entityType, currMap);
		AgentTPProfileStore apStore = new AgentTPProfileStore(dataDir);
		apStore.storeEntityTPProfileMapping(entityProfileMap);
	}

	public void removeTPProfileAssignment(String entityType, String entityId,
			String dataDir) {
		HashMap<String, String> currMap = this.entityProfileMap.get(entityType);
		currMap.remove(entityId);
		this.entityProfileMap.put(entityType, currMap);
		AgentTPProfileStore apStore = new AgentTPProfileStore(dataDir);
		apStore.storeEntityTPProfileMapping(entityProfileMap);

	}
}
