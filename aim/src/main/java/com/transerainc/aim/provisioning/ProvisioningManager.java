/**
 * 
 */
package com.transerainc.aim.provisioning;

import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.transerainc.aim.conf.ConfigurationManager;
import com.transerainc.aim.db.SqlStatementManager;
import com.transerainc.aim.pojo.AdhocDialProperty;
import com.transerainc.aim.pojo.Agent;
import com.transerainc.aim.pojo.AgentProfile;
import com.transerainc.aim.pojo.ThirdPartyProfile;
import com.transerainc.aim.pojo.AgentTeamMapping;
import com.transerainc.aim.pojo.AuxilaryCode;
import com.transerainc.aim.pojo.MediaProfile;
import com.transerainc.aim.pojo.PasswordPolicy;
import com.transerainc.aim.pojo.Site;
import com.transerainc.aim.pojo.SkillProfile;
import com.transerainc.aim.pojo.SpeedDialList;
import com.transerainc.aim.pojo.Team;
import com.transerainc.aim.pojo.Tenant;
import com.transerainc.aim.pojo.ThirdPartyProfileAddInfo;
import com.transerainc.aim.pojo.ThirdPartyVendor;
import com.transerainc.aim.pojo.VirtualTeam;
import com.transerainc.aim.provisioning.handler.AdhocDialPropetiesHandler;
import com.transerainc.aim.provisioning.handler.AgentHandler;
import com.transerainc.aim.provisioning.handler.AgentMediaProfileHandler;
import com.transerainc.aim.provisioning.handler.AgentProfileHandler;
import com.transerainc.aim.provisioning.handler.ThirdPartyProfileHandler;
import com.transerainc.aim.provisioning.handler.AgentToTeamMappingHandler;
import com.transerainc.aim.provisioning.handler.IdleCodeHandler;
import com.transerainc.aim.provisioning.handler.PasswordPolicyHandler;
import com.transerainc.aim.provisioning.handler.SiteHandler;
import com.transerainc.aim.provisioning.handler.SkillProfileHandler;
import com.transerainc.aim.provisioning.handler.SpeedDialListHandler;
import com.transerainc.aim.provisioning.handler.TeamHandler;
import com.transerainc.aim.provisioning.handler.TenantHandler;
import com.transerainc.aim.provisioning.handler.ThirdPartyVendorHandler;
import com.transerainc.aim.provisioning.handler.VirtualTeamHandler;
import com.transerainc.aim.provisioning.handler.WrapupHandler;
import com.transerainc.aim.snmp.TrapManager;
import com.transerainc.aim.system.AgentInformationManagerSystem;

//import com.transerainc.aim.util.HashKey;

/**
 * @author <a href="mailto:ramesh.bobba@transerainc.com">Ramesh Bobba</a>
 * @version $Rivision:$
 */
public class ProvisioningManager {
	private static Logger lgr = Logger.getLogger(ProvisioningManager.class
			.getName());
	private ConfigurationManager configMgr;
	private SqlStatementManager sqlStmtMgr;
	private TenantHandler tenantHandler;
	private TeamHandler teamHandler;
	private AgentHandler agentHandler;
	private AgentProfileHandler agentProfileHandler;
	private PasswordPolicyHandler passwordPolicyHandler;
	private AgentToTeamMappingHandler agentToTeamMappingHandler;
	private AdhocDialPropetiesHandler adhocDialPropertiesHandler;
	private SpeedDialListHandler speedDialListHandler;
	private IdleCodeHandler idleCodeHandler;
	private WrapupHandler wrapupHandler;
	private SiteHandler siteHandler;
	private VirtualTeamHandler vteamHandler;
	private AgentMediaProfileHandler agentMediaProfileHandler;
	private SkillProfileHandler skillProfileHandler;
	private ThirdPartyProfileHandler tpProfileHandler;
	private ThirdPartyVendorHandler tpVendorHandler;

	public ProvisioningManager(ConfigurationManager cmgr,
			SqlStatementManager stmtMgr) throws ProvisioningException {
		this.configMgr = cmgr;
		this.sqlStmtMgr = stmtMgr;

		init();
		lgr.warning("Done Initialization of Prov Mgr");
	}

	/**
	 * @throws ProvisioningException
	 * 
	 */
	public void init() throws ProvisioningException {
		ProvisioningConnection pcon = new ProvisioningConnection(configMgr);
		try {
			long ts1 = System.currentTimeMillis();
			ProvisioningDAO provDAO = new ProvisioningDAO(sqlStmtMgr, pcon);

			long ts2 = System.currentTimeMillis();
			initializeAgentHandler(provDAO);
			initializeAgentProfileHandler(provDAO);
			initializeSiteHandler(provDAO);
			initializeTeamHandler(provDAO);
			initializeTenantHandler(provDAO);
			initializePasswordPolicyHandler(provDAO);
			initializeAgentToTeamMappingHandler(provDAO);
			initializeAdhocDialPropertiesHandler(provDAO);
			initializeSpeedDialListHandler(provDAO);
			initializeAuxCodeHandlers(provDAO);
			initializeVirtualTeamHandler(provDAO);
			initializeAgentMediaProfileHandler(provDAO);
			initializeSkillProfileHandler(provDAO);
			initializeAgentTPProfileHandler(provDAO);
			initializeThirdPartyVendorsHandler(provDAO);

			long ts4 = System.currentTimeMillis();
			if (lgr.isLoggable(Level.INFO)) {
				lgr.info("Time taken to create object " + (ts2 - ts1)
						+ ", create handlers " + (ts4 - ts2));
			}
		} finally {
			pcon.closeConnection();
		}
	}

	/**
	 * @param trapManager
	 * @param provDAO
	 * @param auxCodeMap
	 * @return
	 */
	public void initializeAuxCodeHandlers(ProvisioningDAO provDAO) {
		TrapManager trapManager = (TrapManager) AgentInformationManagerSystem
				.findBean("trapManager");

		HashMap<String, AuxilaryCode> auxCodeMap = null;
		try {
			auxCodeMap = provDAO.getAuxilaryCodes("all.aux.code.query");
		} catch (Exception e) {
			lgr.log(Level.SEVERE, "Error while getting aux code "
					+ "list data from provisioning database", e);

			trapManager.sendProvisioningDataFetchFailed("aux-codes", configMgr
					.getAgentInformationManager().getProvisioningDatabaseUrl(),
					configMgr.getAgentInformationManager()
							.getProvisioningDatabaseUsername(), e.getMessage());

		}

		String dataDir = configMgr.getAgentInformationManager()
				.getDataDirectory();
		WrapupHandler wh = null;
		if (auxCodeMap != null) {
			wh = new WrapupHandler(auxCodeMap, dataDir);
		} else {
			wh = new WrapupHandler(dataDir);
		}

		wrapupHandler = wh;

		IdleCodeHandler ich = null;
		if (auxCodeMap != null) {
			ich = new IdleCodeHandler(auxCodeMap, dataDir);
		} else {
			ich = new IdleCodeHandler(dataDir);
		}

		idleCodeHandler = ich;

	}

	/**
	 * @param trapManager
	 * @param provDAO
	 * @param sdlMap
	 * @return
	 */
	public void initializeSpeedDialListHandler(ProvisioningDAO provDAO) {
		TrapManager trapManager = (TrapManager) AgentInformationManagerSystem
				.findBean("trapManager");

		HashMap<String, SpeedDialList> sdlMap = null;
		try {
			sdlMap = provDAO.getSpeedDialLists("all.speed.dial.list.query");
		} catch (Exception e) {
			lgr.log(Level.SEVERE, "Error while getting speed dial "
					+ "list data from provisioning database", e);

			trapManager.sendProvisioningDataFetchFailed("speed-dial-lists",
					configMgr.getAgentInformationManager()
							.getProvisioningDatabaseUrl(), configMgr
							.getAgentInformationManager()
							.getProvisioningDatabaseUsername(), e.getMessage());

		}

		String dataDir = configMgr.getAgentInformationManager()
				.getDataDirectory();
		SpeedDialListHandler sdlh = null;
		if (sdlMap != null) {
			sdlh = new SpeedDialListHandler(sdlMap, dataDir);
		} else {
			sdlh = new SpeedDialListHandler(dataDir);
		}

		speedDialListHandler = sdlh;
	}

	/**
	 * @param trapManager
	 * @param provDAO
	 * @param adpMap
	 * @return
	 */
	public void initializeAdhocDialPropertiesHandler(ProvisioningDAO provDAO) {
		TrapManager trapManager = (TrapManager) AgentInformationManagerSystem
				.findBean("trapManager");

		HashMap<String, AdhocDialProperty> adpMap = null;
		try {
			adpMap = provDAO
					.getAdhocDialProperties("all.adhoc.dial.properties.query");
		} catch (Exception e) {
			lgr.log(Level.SEVERE, "Error while getting adhoc dial "
					+ "property data from provisioning database", e);

			trapManager.sendProvisioningDataFetchFailed(
					"adhoc-dial-properties", configMgr
							.getAgentInformationManager()
							.getProvisioningDatabaseUrl(), configMgr
							.getAgentInformationManager()
							.getProvisioningDatabaseUsername(), e.getMessage());

		}

		String dataDir = configMgr.getAgentInformationManager()
				.getDataDirectory();
		AdhocDialPropetiesHandler adph = null;
		if (adpMap != null) {
			adph = new AdhocDialPropetiesHandler(adpMap, dataDir);
		} else {
			adph = new AdhocDialPropetiesHandler(dataDir);
		}

		adhocDialPropertiesHandler = adph;
	}

	/**
	 * @param trapManager
	 * @param provDAO
	 * @param attMap
	 * @return
	 */
	public void initializeAgentToTeamMappingHandler(ProvisioningDAO provDAO) {
		TrapManager trapManager = (TrapManager) AgentInformationManagerSystem
				.findBean("trapManager");

		HashMap<String, List<AgentTeamMapping>> attMap = null;
		try {
			attMap = provDAO
					.getAgentToTeamMappings("all.agent.to.team.mapping.query");
		} catch (Exception e) {
			lgr.log(Level.SEVERE, "Error while getting agent to team "
					+ "mapping data from provisioning database", e);

			trapManager.sendProvisioningDataFetchFailed(
					"agent-to-team-mapping", configMgr
							.getAgentInformationManager()
							.getProvisioningDatabaseUrl(), configMgr
							.getAgentInformationManager()
							.getProvisioningDatabaseUsername(), e.getMessage());

		}

		String dataDir = configMgr.getAgentInformationManager()
				.getDataDirectory();
		AgentToTeamMappingHandler attmh = null;
		if (attMap != null) {
			attmh = new AgentToTeamMappingHandler(attMap, dataDir);
		} else {
			attmh = new AgentToTeamMappingHandler(dataDir);
		}

		agentToTeamMappingHandler = attmh;
	}

	/**
	 * @param trapManager
	 * @param provDAO
	 * @param passwdPolicyMap
	 * @return
	 */
	public void initializePasswordPolicyHandler(ProvisioningDAO provDAO) {
		TrapManager trapManager = (TrapManager) AgentInformationManagerSystem
				.findBean("trapManager");

		HashMap<String, PasswordPolicy> passwdPolicyMap = null;
		try {
			passwdPolicyMap = provDAO
					.getPasswordPolicys("all.password.policy.query");
		} catch (Exception e) {
			lgr.log(Level.SEVERE, "Error while getting password"
					+ " policy data from provisioning database", e);

			trapManager.sendProvisioningDataFetchFailed("password-policies",
					configMgr.getAgentInformationManager()
							.getProvisioningDatabaseUrl(), configMgr
							.getAgentInformationManager()
							.getProvisioningDatabaseUsername(), e.getMessage());

		}

		String dataDir = configMgr.getAgentInformationManager()
				.getDataDirectory();
		PasswordPolicyHandler pph = null;
		if (passwdPolicyMap != null) {
			pph = new PasswordPolicyHandler(passwdPolicyMap, dataDir);
		} else {
			pph = new PasswordPolicyHandler(dataDir);
		}

		passwordPolicyHandler = pph;
	}

	/**
	 * @param trapManager
	 * @param provDAO
	 * @param tenantMap
	 * @return
	 */
	public void initializeTenantHandler(ProvisioningDAO provDAO) {
		TrapManager trapManager = (TrapManager) AgentInformationManagerSystem
				.findBean("trapManager");

		HashMap<String, Tenant> tenantMap = null;
		try {
			tenantMap = provDAO.getTenants("tenant.list.query");
		} catch (Exception e) {
			lgr.log(Level.SEVERE, "Error while getting tenant data "
					+ "from provisioning database", e);

			trapManager.sendProvisioningDataFetchFailed("tenants", configMgr
					.getAgentInformationManager().getProvisioningDatabaseUrl(),
					configMgr.getAgentInformationManager()
							.getProvisioningDatabaseUsername(), e.getMessage());

		}

		String dataDir = configMgr.getAgentInformationManager()
				.getDataDirectory();
		TenantHandler th = null;
		if (tenantMap != null) {
			th = new TenantHandler(tenantMap, dataDir);
		} else {
			th = new TenantHandler(dataDir);
		}

		tenantHandler = th;
	}

	/**
	 * @param trapManager
	 * @param provDAO
	 * @param teamMap
	 * @return
	 */
	public void initializeSiteHandler(ProvisioningDAO provDAO) {
		TrapManager trapManager = (TrapManager) AgentInformationManagerSystem
				.findBean("trapManager");

		HashMap<String, Site> siteMap = null;
		try {
			siteMap = provDAO.getSites("site.list.query");
		} catch (Exception e) {
			lgr.log(Level.SEVERE,
					"Error while getting site data from provisioning database",
					e);

			trapManager.sendProvisioningDataFetchFailed("sites", configMgr
					.getAgentInformationManager().getProvisioningDatabaseUrl(),
					configMgr.getAgentInformationManager()
							.getProvisioningDatabaseUsername(), e.getMessage());

		}

		String dataDir = configMgr.getAgentInformationManager()
				.getDataDirectory();
		SiteHandler siteh = null;
		if (siteMap != null) {
			siteh = new SiteHandler(siteMap, dataDir);
		} else {
			siteh = new SiteHandler(dataDir);
		}

		siteHandler = siteh;
	}

	/**
	 * @param trapManager
	 * @param provDAO
	 * @param teamMap
	 * @return
	 */
	public void initializeVirtualTeamHandler(ProvisioningDAO provDAO) {
		TrapManager trapManager = (TrapManager) AgentInformationManagerSystem
				.findBean("trapManager");

		HashMap<String, VirtualTeam> vteamMap = null;
		try {
			vteamMap = provDAO.getVirtualTeams("virtual.team.list.query");
		} catch (Exception e) {
			lgr.log(Level.SEVERE, "Error while getting virtual team "
					+ "data from provisioning database", e);

			trapManager.sendProvisioningDataFetchFailed("virtualteams",
					configMgr.getAgentInformationManager()
							.getProvisioningDatabaseUrl(), configMgr
							.getAgentInformationManager()
							.getProvisioningDatabaseUsername(), e.getMessage());

		}

		String dataDir = configMgr.getAgentInformationManager()
				.getDataDirectory();
		VirtualTeamHandler vteamh = null;
		if (vteamMap != null) {
			vteamh = new VirtualTeamHandler(vteamMap, dataDir);
		} else {
			vteamh = new VirtualTeamHandler(dataDir);
		}

		vteamHandler = vteamh;
	}

	/**
	 * @param trapManager
	 * @param provDAO
	 * @param teamMap
	 * @return
	 */
	public void initializeTeamHandler(ProvisioningDAO provDAO) {
		TrapManager trapManager = (TrapManager) AgentInformationManagerSystem
				.findBean("trapManager");

		HashMap<String, Team> teamMap = null;
		try {
			teamMap = provDAO.getTeams("team.list.query");
		} catch (Exception e) {
			lgr.log(Level.SEVERE,
					"Error while getting team data from provisioning database",
					e);

			trapManager.sendProvisioningDataFetchFailed("teams", configMgr
					.getAgentInformationManager().getProvisioningDatabaseUrl(),
					configMgr.getAgentInformationManager()
							.getProvisioningDatabaseUsername(), e.getMessage());

		}

		String dataDir = configMgr.getAgentInformationManager()
				.getDataDirectory();
		TeamHandler teamh = null;
		if (teamMap != null) {
			teamh = new TeamHandler(teamMap, dataDir);
		} else {
			teamh = new TeamHandler(dataDir);
		}

		teamHandler = teamh;
	}

	/**
	 * @param trapManager
	 * @param provDAO
	 * @param agentMap
	 * @return
	 */
	public void initializeAgentHandler(ProvisioningDAO provDAO) {
		TrapManager trapManager = (TrapManager) AgentInformationManagerSystem
				.findBean("trapManager");

		HashMap<String, Agent> agentMap = null;
		try {
			agentMap = provDAO.getAgents("agent.list.query");
		} catch (Exception e) {
			lgr.log(Level.SEVERE, "Error while getting agent data "
					+ "from provisioning database", e);

			// Raise a trap.
			// provisioningDataFetchFailed("agents",
			// configMgr.getAgentInformationManager().getProvisioningDatabaseUrl(),
			// configMgr.getAgentInformationManager().getProvisioningDatabaseUsername());

			trapManager.sendProvisioningDataFetchFailed("agents", configMgr
					.getAgentInformationManager().getProvisioningDatabaseUrl(),
					configMgr.getAgentInformationManager()
							.getProvisioningDatabaseUsername(), e.getMessage());

		}

		String dataDir = configMgr.getAgentInformationManager()
				.getDataDirectory();

		AgentHandler ah = null;
		if (agentMap != null) {
			ah = new AgentHandler(agentMap, dataDir);
		} else {
			ah = new AgentHandler(dataDir);
		}

		agentHandler = ah;
	}

	/**
	 * @param trapManager
	 * @param provDAO
	 * @param agentProfileMap
	 * @return
	 */
	public void initializeAgentProfileHandler(ProvisioningDAO provDAO) {
		TrapManager trapManager = (TrapManager) AgentInformationManagerSystem
				.findBean("trapManager");

		HashMap<String, AgentProfile> agentProfileMap = null;
		try {
			agentProfileMap = provDAO
					.getAgentProfiles("agent.profile.list.query");
		} catch (Exception e) {
			lgr.log(Level.SEVERE, "Error while getting agent profile "
					+ "data from provisionin database", e);

			trapManager.sendProvisioningDataFetchFailed("agent-profiles",
					configMgr.getAgentInformationManager()
							.getProvisioningDatabaseUrl(), configMgr
							.getAgentInformationManager()
							.getProvisioningDatabaseUsername(), e.getMessage());

		}

		String dataDir = configMgr.getAgentInformationManager()
				.getDataDirectory();

		AgentProfileHandler aph = null;
		if (agentProfileMap != null) {
			aph = new AgentProfileHandler(agentProfileMap, dataDir);
		} else {
			aph = new AgentProfileHandler(dataDir);
		}

		agentProfileHandler = aph;
	}

	/**
	 * @param trapManager
	 * @param provDAO
	 * @param agentMediaProfileMap
	 * @return
	 */
	public void initializeAgentMediaProfileHandler(ProvisioningDAO provDAO) {
		TrapManager trapManager = (TrapManager) AgentInformationManagerSystem
				.findBean("trapManager");
		HashMap<String, MediaProfile> mediaProfileMap = null;
		HashMap<String, HashMap<String, String>> assignmentMap = null;

		try {
			mediaProfileMap = provDAO
					.getAgentMediaProfiles("agent.media.profile.list.query");
		} catch (Exception e) {
			lgr.log(Level.SEVERE, "Error while getting agent media profile "
					+ "data from provisionin database", e);

			trapManager.sendProvisioningDataFetchFailed("agent-media-profiles",
					configMgr.getAgentInformationManager()
							.getProvisioningDatabaseUrl(), configMgr
							.getAgentInformationManager()
							.getProvisioningDatabaseUsername(), e.getMessage());
		}

		try {
			assignmentMap = provDAO
					.getAgentMediaProfileAssignments("agent.media.profile.assignment.list.query");
		} catch (Exception e) {
			lgr.log(Level.SEVERE, "Error while getting agent media profile "
					+ "data from provisionin database", e);

			trapManager.sendProvisioningDataFetchFailed(
					"agent-media-profile-assignment", configMgr
							.getAgentInformationManager()
							.getProvisioningDatabaseUrl(), configMgr
							.getAgentInformationManager()
							.getProvisioningDatabaseUsername(), e.getMessage());
		}

		String dataDir = configMgr.getAgentInformationManager()
				.getDataDirectory();

		AgentMediaProfileHandler amph = null;
		if (mediaProfileMap != null) {
			amph = new AgentMediaProfileHandler(mediaProfileMap, assignmentMap,
					dataDir);

		} else {
			amph = new AgentMediaProfileHandler(dataDir);
		}

		agentMediaProfileHandler = amph;
	}

	/**
	 * @param trapManager
	 * @param provDAO
	 * @param agentMediaProfileMap
	 * @return
	 */
	public void initializeSkillProfileHandler(ProvisioningDAO provDAO) {
		TrapManager trapManager = (TrapManager) AgentInformationManagerSystem
				.findBean("trapManager");

		HashMap<String, SkillProfile> skillProfileMap = null;
		HashMap<String, HashMap<String, String>> agentTeamProfileIdMap = null;

		try {
			skillProfileMap = provDAO
					.getSkillProfiles("agent.skill.profile.list.query");
		} catch (Exception e) {
			lgr.log(Level.SEVERE, "Error while getting agent skill profile "
					+ "data from provisionin database", e);

			trapManager.sendProvisioningDataFetchFailed("agent-skill-profiles",
					configMgr.getAgentInformationManager()
							.getProvisioningDatabaseUrl(), configMgr
							.getAgentInformationManager()
							.getProvisioningDatabaseUsername(), e.getMessage());

		}

		try {
			agentTeamProfileIdMap = provDAO
					.getAgentTeamSkillProfileId("agent.team.skill.profile.query");
		} catch (Exception e) {
			lgr.log(Level.SEVERE,
					"Error while getting agent team skill profileId "
							+ "data from provisionin database", e);

			trapManager.sendProvisioningDataFetchFailed(
					"agent-team-skill-profile-id", configMgr
							.getAgentInformationManager()
							.getProvisioningDatabaseUrl(), configMgr
							.getAgentInformationManager()
							.getProvisioningDatabaseUsername(), e.getMessage());
		}

		String dataDir = configMgr.getAgentInformationManager()
				.getDataDirectory();

		SkillProfileHandler sph = null;

		if (skillProfileMap != null) {
			sph = new SkillProfileHandler(skillProfileMap,
					agentTeamProfileIdMap, dataDir);
		} else {
			sph = new SkillProfileHandler(dataDir);
		}

		skillProfileHandler = sph;

	}

	/**
	 * @param trapManager
	 * @param provDAO
	 * @param agentTPProfileMap
	 * @return
	 */
	public void initializeAgentTPProfileHandler(ProvisioningDAO provDAO) {
		TrapManager trapManager = (TrapManager) AgentInformationManagerSystem
				.findBean("trapManager");
		HashMap<String, ThirdPartyProfile> tpProfileMap = null;
		HashMap<String, ThirdPartyProfileAddInfo> tpProfileAddInfoMap = null;
		HashMap<String, HashMap<String, String>> assignmentMap = null;

		try {
			tpProfileMap = provDAO
					.getAgentTPProfiles("third.party.profile.list.query");
		} catch (Exception e) {
			lgr.log(Level.SEVERE,
					"Error while getting agent third party profile "
							+ "data from provisionin database", e);

			trapManager.sendProvisioningDataFetchFailed(
					"agent-third-party-profiles", configMgr
							.getAgentInformationManager()
							.getProvisioningDatabaseUrl(), configMgr
							.getAgentInformationManager()
							.getProvisioningDatabaseUsername(), e.getMessage());
		}
		
		try {
			tpProfileAddInfoMap = provDAO
					.getAgentTPProfileAdditionalInfo("third.party.profile.additional.info.list.query");
		} catch (Exception e) {
			lgr.log(Level.SEVERE,
					"Error while getting agent third party profile additional info"
							+ "data from provisionin database", e);

			trapManager.sendProvisioningDataFetchFailed(
					"agent-third-party-profiles-additional-info", configMgr
							.getAgentInformationManager()
							.getProvisioningDatabaseUrl(), configMgr
							.getAgentInformationManager()
							.getProvisioningDatabaseUsername(), e.getMessage());
		}

		try {
			assignmentMap = provDAO
					.getAgentTPProfileAssignments("third.party.profile.assignment.list.query");
		} catch (Exception e) {
			lgr.log(Level.SEVERE,
					"Error while getting agent third party profile "
							+ "data from provisionin database", e);

			trapManager.sendProvisioningDataFetchFailed(
					"agent-tp-profile-assignment", configMgr
							.getAgentInformationManager()
							.getProvisioningDatabaseUrl(), configMgr
							.getAgentInformationManager()
							.getProvisioningDatabaseUsername(), e.getMessage());
		}

		String dataDir = configMgr.getAgentInformationManager()
				.getDataDirectory();

		ThirdPartyProfileHandler amph = null;
		if (tpProfileMap != null) {
			amph = new ThirdPartyProfileHandler(tpProfileMap, tpProfileAddInfoMap, assignmentMap,
					dataDir);

		} else {
			amph = new ThirdPartyProfileHandler(dataDir);
		}

		tpProfileHandler = amph;
	}

	/**
	 * @param trapManager
	 * @param provDAO
	 * @param vendorsMap
	 * @return
	 */
	public void initializeThirdPartyVendorsHandler(ProvisioningDAO provDAO) {
		TrapManager trapManager = (TrapManager) AgentInformationManagerSystem
				.findBean("trapManager");
		HashMap<String, ThirdPartyVendor> tpvMap = null;
		try {
			tpvMap = provDAO
					.getThirdPartyVendors("third.party.vendor.list.query");
		} catch (Exception e) {
			lgr.log(Level.SEVERE,
					"Error while getting agent third party profile "
							+ "data from provisionin database", e);

			trapManager.sendProvisioningDataFetchFailed("third-party-vendor",
					configMgr.getAgentInformationManager()
							.getProvisioningDatabaseUrl(), configMgr
							.getAgentInformationManager()
							.getProvisioningDatabaseUsername(), e.getMessage());
		}

		String dataDir = configMgr.getAgentInformationManager()
				.getDataDirectory();

		ThirdPartyVendorHandler amph = null;
		if (tpvMap != null) {
			amph = new ThirdPartyVendorHandler(tpvMap, dataDir);

		} else {
			amph = new ThirdPartyVendorHandler(dataDir);
		}

		tpVendorHandler = amph;
	}

	public boolean updateAgentTeamSkillProfileIdMap(ProvisioningDAO provDAO) {
		boolean result = true;
		TrapManager trapManager = (TrapManager) AgentInformationManagerSystem
				.findBean("trapManager");
		if (skillProfileHandler == null) {
			initializeSkillProfileHandler(provDAO);
		} else {
			HashMap<String, HashMap<String, String>> agentTeamProfileIdMap = null;
			try {
				agentTeamProfileIdMap = provDAO
						.getAgentTeamSkillProfileId("agent.team.skill.profile.query");
				if (agentTeamProfileIdMap != null) {
					skillProfileHandler
							.updateAgentTeamSkillProfileIdMap(agentTeamProfileIdMap);
				}
			} catch (Exception e) {
				result = false;
				lgr.log(Level.SEVERE,
						"Error while getting agent team skill profileId "
								+ "data from provisionin database", e);

				trapManager.sendProvisioningDataFetchFailed(
						"agent-team-skill-profile-id", configMgr
								.getAgentInformationManager()
								.getProvisioningDatabaseUrl(), configMgr
								.getAgentInformationManager()
								.getProvisioningDatabaseUsername(), e
								.getMessage());
			}
		}
		return result;
	}

	public TenantHandler getTenantHandler() {
		return tenantHandler;
	}

	public SiteHandler getSiteHandler() {
		return siteHandler;
	}

	public VirtualTeamHandler getVirtualTeamHandler() {
		return vteamHandler;
	}

	public TeamHandler getTeamHandler() {
		return teamHandler;
	}

	public AgentHandler getAgentHandler() {
		return agentHandler;
	}

	public AgentProfileHandler getAgentProfileHandler() {
		return agentProfileHandler;
	}

	public PasswordPolicyHandler getPasswordPolicyHandler() {
		return passwordPolicyHandler;
	}

	public AgentToTeamMappingHandler getAgentTeamMappingHandler() {
		return agentToTeamMappingHandler;
	}

	public AdhocDialPropetiesHandler getAdhocDialPropetiesHandler() {
		return adhocDialPropertiesHandler;
	}

	public SpeedDialListHandler getSpeedDialListHandler() {
		return speedDialListHandler;
	}

	public IdleCodeHandler getIdleCodeHandler() {
		return idleCodeHandler;
	}

	public WrapupHandler getWrapupHandler() {
		return wrapupHandler;
	}

	public AgentMediaProfileHandler getAgentMediaProfileHandler() {
		return agentMediaProfileHandler;
	}

	public SkillProfileHandler getSkillProfileHandler() {
		return skillProfileHandler;
	}

	public ThirdPartyProfileHandler getAgentTPProfileHandler() {
		return tpProfileHandler;
	}
	
	public ThirdPartyVendorHandler getThirdPartyVendorHandler() {
		return tpVendorHandler;
	}
}
