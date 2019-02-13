/**
 * 
 */
package com.transerainc.aim.provisioning.handler;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.springframework.util.StringUtils;

import com.transerainc.aim.pojo.MediaProfile;
import com.transerainc.aim.store.AgentMediaProfileStore;

/**
 * @author Rajpal Dangi
 * 
 */
public class AgentMediaProfileHandler {
	private static Logger lgr = Logger.getLogger(AgentMediaProfileHandler.class
			.getName());

	private HashMap<String, MediaProfile> mediaProfileMap;
	private HashMap<String, HashMap<String, String>> entityProfileMap;


	private static final String ENTITY_SITE = "3";
	private static final String ENTITY_TEAM = "4";
	private static final String ENTITY_AGENT = "5";
	private static final String DEFAULT_TEL_PROFILE = "Default_Telephony_Profile";
	
	public AgentMediaProfileHandler(String dataDir) {
		AgentMediaProfileStore apStore = new AgentMediaProfileStore(dataDir);
		mediaProfileMap = apStore.retrieveAgentMediaProfiles();
		entityProfileMap = apStore.retrieveEntityMediaProfileMapping();
	}

	public AgentMediaProfileHandler(
			HashMap<String, MediaProfile> mediaProfileMap,
			HashMap<String, HashMap<String, String>> entityProfileMap,
			String dataDir) {
		this.mediaProfileMap = mediaProfileMap;
		this.entityProfileMap = entityProfileMap == null ? new HashMap<String, HashMap<String, String>>()
				: entityProfileMap;
		AgentMediaProfileStore apStore = new AgentMediaProfileStore(dataDir);
		apStore.storeAgentMediaProfiles(this.mediaProfileMap);
		apStore.storeEntityMediaProfileMapping(this.entityProfileMap);
	}

	// public void updateEntityProfileMap(
	// HashMap<String, HashMap<String, String>> entityProfileMap) {
	// this.entityProfileMap = entityProfileMap == null ? new HashMap<String,
	// HashMap<String, String>>()
	// : entityProfileMap;
	// AgentMediaProfileStore apStore = new AgentMediaProfileStore(dataDir);
	// apStore.storeEntityMediaProfileMapping(this.entityProfileMap);
	// }

	public MediaProfile getAgentMediaProfile(String mediaProfileId) {
		MediaProfile mediaProfile = mediaProfileMap.get(mediaProfileId);
		if (lgr.isLoggable(Level.INFO)) {
			lgr.info("Lookup for agent media profile " + mediaProfileId
					+ " returned - " + mediaProfile);
		}
		return mediaProfile;
	}

	public MediaProfile getAgentMediaProfileByAgent(String agentId,
			String teamId, String siteId) {
		MediaProfile matchingProfile = null;
		String profileId = "";
		
		if (lgr.isLoggable(Level.FINE)) {
			lgr.fine("Got request for media profile " + "with agentId : "
					+ agentId + " teamId : " + teamId + " and siteId : "
					+ siteId);
		}
		
		// check profile id in agentEntityMap
		HashMap<String, String> agentToProfileMap = this.entityProfileMap
				.get(ENTITY_AGENT);
		HashMap<String, String> teamToProfileMap = this.entityProfileMap
				.get(ENTITY_TEAM);
		HashMap<String, String> siteToProfileMap = this.entityProfileMap
				.get(ENTITY_SITE);

		if (null != agentToProfileMap && agentToProfileMap.containsKey(agentId)) {
			profileId = agentToProfileMap.get(agentId);
			if (lgr.isLoggable(Level.FINE)) {
				lgr.fine("Found a matching profile "
						+ "at the agent level for agent " + agentId + ":"
						+ profileId);
			}
		} else if (null != teamToProfileMap
				&& teamToProfileMap.containsKey(teamId)) {
			profileId = teamToProfileMap.get(teamId);
			if (lgr.isLoggable(Level.FINE)) {
				lgr.fine("Found a matching profile "
						+ "at the team level for agent " + agentId
						+ " and team " + teamId + ":" + profileId);
			}
		} else if (null != siteToProfileMap
				&& siteToProfileMap.containsKey(siteId)) {
			profileId = siteToProfileMap.get(siteId);
			if (lgr.isLoggable(Level.FINE)) {
				lgr.fine("Found a matching profile "
						+ "at the site level for agent " + agentId
						+ " and team " + teamId + " in site " + siteId + ":"
						+ profileId);
			}
		}
		if (StringUtils.hasText(profileId)) {
			matchingProfile = this.mediaProfileMap.get(profileId);
		}
		return matchingProfile;
	}

	public boolean isDefaultTelephony(String agentId){
		HashMap<String, String> agentToProfileMap = this.entityProfileMap
				.get(ENTITY_AGENT);
		String profileId = null;
		
		if (null != agentToProfileMap && agentToProfileMap.containsKey(agentId)) {
			profileId = agentToProfileMap.get(agentId);
		}
		MediaProfile mediaProfile = getAgentMediaProfile(profileId);
		
		if(mediaProfile!=null && mediaProfile.getMediaProfileName().equalsIgnoreCase(DEFAULT_TEL_PROFILE)){
			return true; //FIXME
		}
		return false;
	}
	public String getAgentMediaProfileIdByAgent(String agentId, String teamId,
			String siteId) {
		MediaProfile profile = getAgentMediaProfileByAgent(agentId, teamId,
				siteId);
		return null != profile ? profile.getMediaProfileId() : "";
	}

	public String getAgentMediaProfileIdNameByAgent(String agentId,
			String teamId, String siteId) {
		MediaProfile profile = getAgentMediaProfileByAgent(agentId, teamId,
				siteId);
		return null != profile ? "(" + profile.getMediaProfileId() + ")"
				+ profile.getMediaProfileName() : "";
	}

	public Set<MediaProfile> getAgentMediaProfilesForTenant(String tenantId) {
		Set<MediaProfile> ampSet = new HashSet<MediaProfile>();
		for (Map.Entry<String, MediaProfile> profileEntry : mediaProfileMap
				.entrySet()) {
			if (profileEntry.getValue() != null
					&& profileEntry.getValue().getTenantId().equals(tenantId)) {
				ampSet.add(profileEntry.getValue());
			}
		}
		return ampSet;
	}
	
	// This is used by admin gui 
	public HashMap<String, HashMap<String, String>> getProfileAssignmentMap(
			String tenantId) {
		HashMap<String, HashMap<String, String>> map = new HashMap<String, HashMap<String, String>>();

		if (this.entityProfileMap != null) {
			HashMap<String, String> agentToProfileMap = this.entityProfileMap
					.get(ENTITY_AGENT);
			HashMap<String, String> teamToProfileMap = this.entityProfileMap
					.get(ENTITY_TEAM);
			HashMap<String, String> siteToProfileMap = this.entityProfileMap
					.get(ENTITY_SITE);

			HashMap<String, String> entityMap = filterByTenant(tenantId,
					agentToProfileMap);
			map.put(ENTITY_AGENT, entityMap);
			entityMap = filterByTenant(tenantId, teamToProfileMap);
			map.put(ENTITY_TEAM, entityMap);
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
		if(entityToProfileMap != null){
			for(Map.Entry<String, String> entry : entityToProfileMap.entrySet()){
				MediaProfile profile = mediaProfileMap.get(entry.getValue());
				if(profile != null && profile.getTenantId().equals(tenantId)){
					entityMap.put(entry.getKey(), entry.getValue());
				}
			}
		}
		return entityMap;
	}
	
	
}
