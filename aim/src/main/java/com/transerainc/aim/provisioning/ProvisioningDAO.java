/**
 * 
 */
package com.transerainc.aim.provisioning;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

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
import com.transerainc.aim.pojo.Skill;
import com.transerainc.aim.pojo.EntityAssignment;
import com.transerainc.aim.pojo.SkillProfile;
import com.transerainc.aim.pojo.SpeedDialList;
import com.transerainc.aim.pojo.SpeedDialListEntry;
import com.transerainc.aim.pojo.Team;
import com.transerainc.aim.pojo.Tenant;
import com.transerainc.aim.pojo.ThirdPartyProfileAddInfo;
import com.transerainc.aim.pojo.ThirdPartyVendor;
import com.transerainc.aim.pojo.VirtualTeam;

/**
 * @author <a href="mailto:ramesh.bobba@transerainc.com">Ramesh Bobba</a>
 * @version $Rivision:$
 * 
 *          Get the following tables: Agent, Team, Tenant, Agent Profile, Speed
 *          Dial List, Speed Dial List Entry, password policy, agent to team
 *          mapping, adhoc_dial_properties, agent_skills_view
 */
public class ProvisioningDAO {
	private static Logger lgr = Logger.getLogger(ProvisioningDAO.class
			.getName());
	private ProvisioningConnection provConnection;
	private SqlStatementManager sqlStmtMgr;

	public ProvisioningDAO(SqlStatementManager stmtMgr,
			ProvisioningConnection con) {
		this.sqlStmtMgr = stmtMgr;
		this.provConnection = con;
	}

	public HashMap<String, Tenant> getTenants(String queryName, String... args)
			throws ProvisioningException {
		HashMap<String, Tenant> tenantMap = new HashMap<String, Tenant>();

		try {
			String query = sqlStmtMgr.getQuery(queryName);

			if (lgr.isLoggable(Level.CONFIG)) {
				lgr.config("Issuing query " + query
						+ " to get the list of tenants");
			}

			Connection con = provConnection.getConnection();
			PreparedStatement stmt = con.prepareStatement(query);

			if (args != null && args.length > 0) {
				int count = 1;
				for (String arg : args) {
					stmt.setString(count, arg);
					count++;
				}
			}

			ResultSet rs = stmt.executeQuery();

			while (rs.next()) {
				Tenant tenant = getTenant(rs);
				tenantMap.put(tenant.getTenantId(), tenant);
			}
		} catch (Exception e) {
			throw new ProvisioningException(e);
		}

		return tenantMap;
	}

	public HashMap<String, Site> getSites(String queryName, String... args)
			throws ProvisioningException {
		HashMap<String, Site> siteMap = new HashMap<String, Site>();

		try {
			String query = sqlStmtMgr.getQuery(queryName);

			if (lgr.isLoggable(Level.CONFIG)) {
				lgr.config("Issuing query " + query
						+ " to get the list of sites");
			}

			Connection con = provConnection.getConnection();
			PreparedStatement stmt = con.prepareStatement(query);

			if (args != null && args.length > 0) {
				int count = 1;
				for (String arg : args) {
					stmt.setString(count, arg);
					count++;
				}
			}

			ResultSet rs = stmt.executeQuery();

			while (rs.next()) {
				Site site = getSite(rs);
				siteMap.put(site.getSiteId(), site);
			}
		} catch (Exception e) {
			throw new ProvisioningException(e);
		}

		return siteMap;
	}

	public HashMap<String, VirtualTeam> getVirtualTeams(String queryName,
			String... args) throws ProvisioningException {
		HashMap<String, VirtualTeam> vteamMap = new HashMap<String, VirtualTeam>();

		try {
			String query = sqlStmtMgr.getQuery(queryName);

			if (lgr.isLoggable(Level.CONFIG)) {
				lgr.config("Issuing query " + query
						+ " to get the list of VirtualTeams");
			}

			Connection con = provConnection.getConnection();
			PreparedStatement stmt = con.prepareStatement(query);

			if (args != null && args.length > 0) {
				int count = 1;
				for (String arg : args) {
					stmt.setString(count, arg);
					count++;
				}
			}

			ResultSet rs = stmt.executeQuery();

			while (rs.next()) {
				VirtualTeam vteam = getVirtualTeam(rs);
				vteamMap.put(vteam.getId(), vteam);
			}
		} catch (Exception e) {
			throw new ProvisioningException(e);
		}

		return vteamMap;
	}

	public HashMap<String, Team> getTeams(String queryName, String... args)
			throws ProvisioningException {
		HashMap<String, Team> teamMap = new HashMap<String, Team>();

		try {
			String query = sqlStmtMgr.getQuery(queryName);

			if (lgr.isLoggable(Level.CONFIG)) {
				lgr.config("Issuing query " + query
						+ " to get the list of teams");
			}

			Connection con = provConnection.getConnection();
			PreparedStatement stmt = con.prepareStatement(query);

			if (args != null && args.length > 0) {
				int count = 1;
				for (String arg : args) {
					stmt.setString(count, arg);
					count++;
				}
			}

			ResultSet rs = stmt.executeQuery();

			while (rs.next()) {
				Team team = getTeam(rs);
				teamMap.put(team.getTeamId(), team);
			}
		} catch (Exception e) {
			throw new ProvisioningException(e);
		}

		return teamMap;
	}

	public HashMap<String, Agent> getAgents(String queryName, String... args)
			throws ProvisioningException {
		HashMap<String, Agent> agentMap = new HashMap<String, Agent>();

		try {
			String query = sqlStmtMgr.getQuery(queryName);

			if (lgr.isLoggable(Level.CONFIG)) {
				lgr.config("Issuing query " + query
						+ " to get the list of agents");
			}

			Connection con = provConnection.getConnection();
			PreparedStatement stmt = con.prepareStatement(query);

			if (args != null && args.length > 0) {
				int count = 1;
				for (String arg : args) {
					stmt.setString(count, arg);
					count++;
				}
			}

			ResultSet rs = stmt.executeQuery();

			while (rs.next()) {
				Agent agent = getAgent(rs);
				agentMap.put(agent.getAgentId(), agent);
			}
		} catch (Exception e) {
			throw new ProvisioningException(e);
		}

		return agentMap;
	}

	public HashMap<String, AgentProfile> getAgentProfiles(String queryName,
			String... args) throws ProvisioningException {
		HashMap<String, AgentProfile> profileMap = new HashMap<String, AgentProfile>();

		try {
			String query = sqlStmtMgr.getQuery(queryName);

			if (lgr.isLoggable(Level.CONFIG)) {
				lgr.config("Issuing query " + query
						+ " to get the list of agent profiles");
			}

			Connection con = provConnection.getConnection();
			PreparedStatement stmt = con.prepareStatement(query);

			if (args != null && args.length > 0) {
				int count = 1;
				for (String arg : args) {
					stmt.setString(count, arg);
					count++;
				}
			}

			ResultSet rs = stmt.executeQuery();

			while (rs.next()) {
				AgentProfile profile = getAgentProfile(rs);
				profileMap.put(profile.getProfileId(), profile);
			}
		} catch (Exception e) {
			throw new ProvisioningException(e);
		}

		return profileMap;
	}

	public HashMap<String, SpeedDialList> getSpeedDialLists(String queryName,
			String... args) throws ProvisioningException {
		HashMap<String, SpeedDialList> sdListMap = new HashMap<String, SpeedDialList>();

		try {
			String query = sqlStmtMgr.getQuery(queryName);

			if (lgr.isLoggable(Level.CONFIG)) {
				lgr.config("Issuing query " + query
						+ " to get all the speed dial lists");
			}

			Connection con = provConnection.getConnection();
			PreparedStatement stmt = con.prepareStatement(query);

			if (args != null && args.length > 0) {
				int count = 1;
				for (String arg : args) {
					stmt.setString(count, arg);
					count++;
				}
			}

			ResultSet rs = stmt.executeQuery();

			while (rs.next()) {
				getSpeedDialList(rs, sdListMap);
			}
		} catch (Exception e) {
			throw new ProvisioningException(e);
		}

		return sdListMap;
	}

	public HashMap<String, PasswordPolicy> getPasswordPolicys(String queryName,
			String... args) throws ProvisioningException {
		HashMap<String, PasswordPolicy> sdListMap = new HashMap<String, PasswordPolicy>();

		try {
			String query = sqlStmtMgr.getQuery(queryName);

			if (lgr.isLoggable(Level.CONFIG)) {
				lgr.config("Issuing query " + query
						+ " to get all the password policies");
			}

			Connection con = provConnection.getConnection();
			PreparedStatement stmt = con.prepareStatement(query);

			if (args != null && args.length > 0) {
				int count = 1;
				for (String arg : args) {
					stmt.setString(count, arg);
					count++;
				}
			}

			ResultSet rs = stmt.executeQuery();

			while (rs.next()) {
				PasswordPolicy pPolicy = getPasswordPolicy(rs);
				sdListMap.put(pPolicy.getId(), pPolicy);
			}
		} catch (Exception e) {
			throw new ProvisioningException(e);
		}

		return sdListMap;
	}

	public HashMap<String, List<AgentTeamMapping>> getAgentToTeamMappings(
			String queryName, String... args) throws ProvisioningException {
		HashMap<String, List<AgentTeamMapping>> attMap = new HashMap<String, List<AgentTeamMapping>>();

		try {
			String query = sqlStmtMgr.getQuery(queryName);

			if (lgr.isLoggable(Level.CONFIG)) {
				lgr.config("Issuing query " + query
						+ " to get all the agent to team mappings");
			}

			Connection con = provConnection.getConnection();
			PreparedStatement stmt = con.prepareStatement(query);

			if (args != null && args.length > 0) {
				int count = 1;
				for (String arg : args) {
					stmt.setString(count, arg);
					count++;
				}
			}

			ResultSet rs = stmt.executeQuery();

			while (rs.next()) {
				String agentId = rs.getString("AGENT_ID");
				String teamId = rs.getString("TEAM_ID");
				String id = rs.getString("ID");
				String tenantId = rs.getString("TENANT_ID");

				List<AgentTeamMapping> teamList = null;
				if (attMap.containsKey(agentId)) {
					teamList = attMap.get(agentId);
				} else {
					teamList = new ArrayList<AgentTeamMapping>();
					attMap.put(agentId, teamList);
				}

				AgentTeamMapping atMapping = new AgentTeamMapping();
				atMapping.setAgentId(agentId);
				atMapping.setId(id);
				atMapping.setTeamId(teamId);
				atMapping.setTenantId(tenantId);
				teamList.add(atMapping);
			}
		} catch (Exception e) {
			throw new ProvisioningException(e);
		}

		return attMap;
	}

	public HashMap<String, AdhocDialProperty> getAdhocDialProperties(
			String queryName, String... args) throws ProvisioningException {
		HashMap<String, AdhocDialProperty> adpMap = new HashMap<String, AdhocDialProperty>();

		try {
			String query = sqlStmtMgr.getQuery(queryName);

			if (lgr.isLoggable(Level.CONFIG)) {
				lgr.config("Issuing query " + query
						+ " to get all the adhoc dial properties");
			}

			Connection con = provConnection.getConnection();
			PreparedStatement stmt = con.prepareStatement(query);

			if (args != null && args.length > 0) {
				int count = 1;
				for (String arg : args) {
					stmt.setString(count, arg);
					count++;
				}
			}

			ResultSet rs = stmt.executeQuery();

			while (rs.next()) {
				AdhocDialProperty adp = getAdhocDialProperty(rs);
				adpMap.put(adp.getId(), adp);
			}
		} catch (Exception e) {
			throw new ProvisioningException(e);
		}

		return adpMap;
	}

	public HashMap<String, AuxilaryCode> getAuxilaryCodes(String queryName,
			String... args) throws ProvisioningException {
		HashMap<String, AuxilaryCode> auxCodeMap = new HashMap<String, AuxilaryCode>();

		try {
			String query = sqlStmtMgr.getQuery(queryName);

			if (lgr.isLoggable(Level.CONFIG)) {
				lgr.config("Issuing query " + query
						+ " to get all the auxilary codes");
			}

			Connection con = provConnection.getConnection();
			PreparedStatement stmt = con.prepareStatement(query);

			if (args != null && args.length > 0) {
				int count = 1;
				for (String arg : args) {
					stmt.setString(count, arg);
					count++;
				}
			}

			ResultSet rs = stmt.executeQuery();

			while (rs.next()) {
				AuxilaryCode aCode = getAuxilaryCode(rs);
				auxCodeMap.put(aCode.getCodeId(), aCode);
			}
		} catch (Exception e) {
			throw new ProvisioningException(e);
		}

		return auxCodeMap;
	}

	public HashMap<String, MediaProfile> getAgentMediaProfiles(
			String queryName, String... args) throws ProvisioningException {
		
		HashMap<String, MediaProfile> mediaProfileMap = new HashMap<String, MediaProfile>();
		try {
			String query = sqlStmtMgr.getQuery(queryName);

			if (lgr.isLoggable(Level.CONFIG)) {
				lgr.config("Issuing query " + query
						+ " to get the list of agent media profiles");
			}

			Connection con = provConnection.getConnection();
			PreparedStatement stmt = con.prepareStatement(query);

			if (args != null && args.length > 0) {
				int count = 1;
				for (String arg : args) {
					stmt.setString(count, arg);
					count++;
				}
			}

			ResultSet rs = stmt.executeQuery();

			while (rs.next()) {
				MediaProfile mediaProfile = getAgentMediaProfile(rs);
				mediaProfileMap.put(mediaProfile.getMediaProfileId(),
						mediaProfile);
			}
		} catch (Exception e) {
			throw new ProvisioningException(e);
		}

		return mediaProfileMap;
	}
	
	public HashMap<String, HashMap<String, String>> getAgentMediaProfileAssignments(
			String queryName, String... args) throws ProvisioningException {

		HashMap<String, HashMap<String, String>> resultMap = new HashMap<String, HashMap<String, String>>();
		try {
			String query = sqlStmtMgr.getQuery(queryName);

			if (lgr.isLoggable(Level.CONFIG)) {
				lgr
						.config("Issuing query "
								+ query
								+ " to get the list of agent media profile assignment ");
			}

			Connection con = provConnection.getConnection();
			PreparedStatement stmt = con.prepareStatement(query);

			if (args != null && args.length > 0) {
				int count = 1;
				for (String arg : args) {
					stmt.setString(count, arg);
					count++;
				}
			}

			ResultSet rs = stmt.executeQuery();
			String entityType;
			String entityId;
			String profileId;
			while (rs.next()) {
				entityType = rs.getString("ENTITY_TYPE");
				entityId = rs.getString("ENTITY_ID");
				profileId = rs.getString("MEDIA_PROFILE_ID");
				HashMap<String, String> map = resultMap.get(entityType);
				if (null == map) {
					map = new HashMap<String, String>();
					resultMap.put(entityType, map);
				}
				map.put(entityId, profileId);
			}
		} catch (Exception e) {
			throw new ProvisioningException(e);
		}

		return resultMap;
	}

	public HashMap<String, SkillProfile> getSkillProfiles(String queryName,
			String... args) throws ProvisioningException {

		HashMap<String, SkillProfile> skillProfileMap = new HashMap<String, SkillProfile>();

		try {
			String query = sqlStmtMgr.getQuery(queryName);

			if (lgr.isLoggable(Level.CONFIG)) {
				lgr.config("Issuing query " + query
						+ " to get the agent skill profiles");
			}

			Connection con = provConnection.getConnection();
			PreparedStatement stmt = con.prepareStatement(query);

			if (args != null && args.length > 0) {
				int count = 1;
				for (String arg : args) {
					stmt.setString(count, arg);
					count++;
				}
			}

			ResultSet rs = stmt.executeQuery();

			while (rs.next()) {
				String profileId = rs.getString("SKILL_PROFILE_ID");
				String key = profileId;

				Skill skill = new Skill();
				skill.setId(rs.getString("SKILL_ID"));
				skill.setName(rs.getString("SKILL_NAME"));
				skill.setValue(rs.getString("SKILL_VALUE"));
				skill.setDescription(rs.getString("SKILL_DESC"));
				skill.setEnumSkillId(rs.getString("ENUM_SKILL_ID"));
				skill.setEnumSkillName(rs.getString("ENUM_SKILL_NAME"));
				skill.setType(rs.getInt("SKILL_TYPE"));
				skill.setTypeName(rs.getString("SKILL_TYPE_NAME"));

				SkillProfile profile = null;
				if (skillProfileMap.containsKey(key)) {
					profile = skillProfileMap.get(key);
				} else {
					profile = new SkillProfile();
					profile.setProfileId(rs.getString("SKILL_PROFILE_ID"));
					profile.setProfileName(rs.getString("PROFILE_NAME"));
					profile.setTenantId(rs.getString("TENANT_ID"));
					skillProfileMap.put(key, profile);
				}
				profile.addSkill(skill);

			}
		} catch (Exception e) {
			throw new ProvisioningException(e);
		}

		return skillProfileMap;
	}

	public SkillProfile getSkillProfileById(String queryName, String... args)
			throws ProvisioningException {
		SkillProfile profile = new SkillProfile();
		try {
			String query = sqlStmtMgr.getQuery(queryName);
			String profileId = "";
			if (lgr.isLoggable(Level.CONFIG)) {
				lgr.config("Issuing query " + query
						+ " to get the skill profile with assigned skills ...");
			}
			Connection con = provConnection.getConnection();
			PreparedStatement stmt = con.prepareStatement(query);

			if (args != null && args.length > 0) {
				profileId = args[0];
				int count = 1;
				for (String arg : args) {
					stmt.setString(count, arg);
					count++;
				}
			}

			profile.setProfileId(profileId);

			ResultSet rs = stmt.executeQuery();
			while (rs.next()) {
				Skill skill = new Skill();
				skill.setId(rs.getString("SKILL_ID"));
				skill.setName(rs.getString("SKILL_NAME"));
				skill.setValue(rs.getString("SKILL_VALUE"));
				skill.setDescription(rs.getString("SKILL_DESC"));
				skill.setEnumSkillId(rs.getString("ENUM_SKILL_ID"));
				skill.setEnumSkillName(rs.getString("ENUM_SKILL_NAME"));
				skill.setType(rs.getInt("SKILL_TYPE"));
				skill.setTypeName(rs.getString("SKILL_TYPE_NAME"));
				
				// redundant
				profile.setTenantId(rs.getString("TENANT_ID"));
				profile.setProfileName(rs.getString("PROFILE_NAME"));
				
				profile.addSkill(skill);

				if (lgr.isLoggable(Level.FINEST)) {
					lgr.finest("Skill  : " + skill.toString()
							+ " added to profile " + profile.getProfileId());
				}
			}
		} catch (Exception e) {
			throw new ProvisioningException(e);
		}

		return profile;
	}

	
	public HashMap<String, HashMap<String, String>> getAgentTeamSkillProfileId(String queryName,
			String... args) throws ProvisioningException {

		HashMap<String, HashMap<String, String>> resultMap = new HashMap<String, HashMap<String, String>>();

		try {
			String query = sqlStmtMgr.getQuery(queryName);

			if (lgr.isLoggable(Level.CONFIG)) {
				lgr.config("Issuing query " + query
						+ " to get the agent team skill profile relation");
			}

			Connection con = provConnection.getConnection();
			PreparedStatement stmt = con.prepareStatement(query);

			if (args != null && args.length > 0) {
				int count = 1;
				for (String arg : args) {
					stmt.setString(count, arg);
					count++;
				}
			}

			ResultSet rs = stmt.executeQuery();

			while (rs.next()) {
				String agentId = rs.getString("AGENT_ID");
				String teamId = rs.getString("TEAM_ID");
				String profileId = rs.getString("SKILL_PROFILE_ID");
				HashMap<String, String> teamProfileIdMap = null;
				if (resultMap.containsKey(agentId)) {
					teamProfileIdMap = resultMap.get(agentId);
				} else {
					teamProfileIdMap = new HashMap<String, String>();
					resultMap.put(agentId, teamProfileIdMap);
				}
				teamProfileIdMap.put(teamId, profileId);
			}
		} catch (Exception e) {
			throw new ProvisioningException(e);
		}

		return resultMap;
	}
	
	public EntityAssignment getSkillAssignemnt(String queryName, String... args)
			throws ProvisioningException {
		EntityAssignment assignemnt = null;
		try {
			String query = sqlStmtMgr.getQuery(queryName);

			if (lgr.isLoggable(Level.CONFIG)) {
				lgr.config("Issuing query " + query
						+ " to get the skill assignemnt ");
			}

			Connection con = provConnection.getConnection();
			PreparedStatement stmt = con.prepareStatement(query);

			if (args != null && args.length > 0) {
				int count = 1;
				for (String arg : args) {
					stmt.setString(count, arg);
					count++;
				}
			}

			ResultSet rs = stmt.executeQuery();

			while (rs.next()) {
				String entityId = rs.getString("ENTITY_ID");
				String entityType = rs.getString("ENTITY_TYPE");
				String tenantId = rs.getString("TENANT_ID");
				assignemnt = new EntityAssignment(entityId, entityType, tenantId);
			}
		} catch (Exception e) {
			throw new ProvisioningException(e);
		}

		return assignemnt;
	}

	public HashMap<String, ThirdPartyProfile> getAgentTPProfiles(String queryName,
			String... args) throws ProvisioningException {
		HashMap<String, ThirdPartyProfile> profileMap = new HashMap<String, ThirdPartyProfile>();

		try {
			String query = sqlStmtMgr.getQuery(queryName);

			if (lgr.isLoggable(Level.CONFIG)) {
				lgr.config("Issuing query " + query
						+ " to get the list of agent tp profiles");
			}

			Connection con = provConnection.getConnection();
			PreparedStatement stmt = con.prepareStatement(query);

			if (args != null && args.length > 0) {
				int count = 1;
				for (String arg : args) {
					stmt.setString(count, arg);
					count++;
				}
			}

			ResultSet rs = stmt.executeQuery();

			while (rs.next()) {
				ThirdPartyProfile profile = getAgentTPProfile(rs);
				profileMap.put(profile.getTpProfileId(), profile);
			}
		} catch (Exception e) {
			throw new ProvisioningException(e);
		}

		return profileMap;
	}
	
	
	public HashMap<String, ThirdPartyProfileAddInfo> getAgentTPProfileAdditionalInfo(String queryName,
			String... args) throws ProvisioningException {
		HashMap<String, ThirdPartyProfileAddInfo> profileMap = new HashMap<String, ThirdPartyProfileAddInfo>();

		try {
			String query = sqlStmtMgr.getQuery(queryName);

			if (lgr.isLoggable(Level.CONFIG)) {
				lgr.config("Issuing query " + query
						+ " to get the list of agent tp profiles additioal info");
			}

			Connection con = provConnection.getConnection();
			PreparedStatement stmt = con.prepareStatement(query);

			if (args != null && args.length > 0) {
				int count = 1;
				for (String arg : args) {
					stmt.setString(count, arg);
					count++;
				}
			}

			ResultSet rs = stmt.executeQuery();

			while (rs.next()) {
				ThirdPartyProfileAddInfo profile = getAgentTPProfileAdditionalInfo(rs);
				profileMap.put(profile.getTpProfileId(), profile);
			}
		} catch (Exception e) {
			throw new ProvisioningException(e);
		}

		return profileMap;
	}
	public HashMap<String, HashMap<String, String>> getAgentTPProfileAssignments(
			String queryName, String... args) throws ProvisioningException {

		HashMap<String, HashMap<String, String>> resultMap = new HashMap<String, HashMap<String, String>>();
		try {
			String query = sqlStmtMgr.getQuery(queryName);

			if (lgr.isLoggable(Level.CONFIG)) {
				lgr
						.config("Issuing query "
								+ query
								+ " to get the list of agent third party profile assignment ");
			}

			Connection con = provConnection.getConnection();
			PreparedStatement stmt = con.prepareStatement(query);

			if (args != null && args.length > 0) {
				int count = 1;
				for (String arg : args) {
					stmt.setString(count, arg);
					count++;
				}
			}

			ResultSet rs = stmt.executeQuery();
			String entityType = null;
			String entityId = null;
			String profileId = null;
			while (rs.next()) {
				entityType = rs.getString("ENTITY_TYPE");
				entityId = rs.getString("ENTITY_ID");
				profileId = rs.getString("THIRD_PARTY_PROFILE_ID");
				HashMap<String, String> map = resultMap.get(entityType);
				if (null == map) {
					map = new HashMap<String, String>();
					resultMap.put(entityType, map);
				}
				map.put(entityId, profileId);
			}
		} catch (Exception e) {
			throw new ProvisioningException(e);
		}

		return resultMap;
	}
	
	public HashMap<String, ThirdPartyVendor> getThirdPartyVendors(String queryName,
			String... args) throws ProvisioningException {
		HashMap<String, ThirdPartyVendor> vendorMap = new HashMap<String, ThirdPartyVendor>();

		try {
			String query = sqlStmtMgr.getQuery(queryName);

			if (lgr.isLoggable(Level.CONFIG)) {
				lgr.config("Issuing query " + query
						+ " to get the list of third party vendors");
			}

			Connection con = provConnection.getConnection();
			PreparedStatement stmt = con.prepareStatement(query);

			if (args != null && args.length > 0) {
				int count = 1;
				for (String arg : args) {
					stmt.setString(count, arg);
					count++;
				}
			}

			ResultSet rs = stmt.executeQuery();

			while (rs.next()) {
				ThirdPartyVendor vendor = getThirdPartyVendors(rs);
				vendorMap.put(vendor.getVendorId(), vendor);
			}
		} catch (Exception e) {
			throw new ProvisioningException(e);
		}

		return vendorMap;
	}
	
	/**
	 * @param rs
	 * @return
	 * @throws SQLException
	 */
	private AuxilaryCode getAuxilaryCode(ResultSet rs) throws SQLException {
		AuxilaryCode code = new AuxilaryCode();
		code.setCodeId(rs.getString("CODE_ID"));
		code.setOmniChannelAuxId(rs.getString("OMNI_CHANNEL_AUX_ID"));
		code.setCodeName(rs.getString("CODE_NAME"));
		code.setCodeType(rs.getString("CODE_TYPE"));

		int bool = rs.getInt("IS_DEFAULT");
		code.setDefaultAuxCode((bool == 1) ? true : false);
		code.setTenantId(rs.getString("TENANT_ID"));
		int isSys = rs.getInt("IS_SYSTEM_AUX");
		code.setSystemAux((isSys == 1)? true : false);
		return code;
	}

	/**
	 * @param rs
	 * @throws SQLException
	 */
	private AdhocDialProperty getAdhocDialProperty(ResultSet rs)
			throws SQLException {
		AdhocDialProperty adp = new AdhocDialProperty();
		adp.setId(rs.getString("ID"));
		adp.setName(rs.getString("NAME"));
		adp.setPrefix(rs.getString("PREFIX"));
		adp.setRegularExpression(rs.getString("REG_EXP"));
		adp.setStrippedChars(rs.getString("STRIPPED_CHARS"));
		adp.setTenantId(rs.getString("ENTERPRISE_ID"));

		return adp;
	}

	/**
	 * @param rs
	 * @return
	 * @throws SQLException
	 */
	private PasswordPolicy getPasswordPolicy(ResultSet rs) throws SQLException {
		PasswordPolicy pPolicy = new PasswordPolicy();

		pPolicy.setId(Integer.toString(rs.getInt("POLICY_ID")));
		pPolicy.setName(rs.getString("POLICY_NAME"));
		pPolicy.setTenantId(Integer.toString(rs.getInt("TENANT_ID")));
		pPolicy.setMaxInvalidAttempts(rs.getInt("MAX_INVALID_ATTEMPTS"));
		pPolicy.setPwdExpirePeriod(rs.getInt("PWD_EXPIRY_PERIOD"));
		pPolicy.setIsDefault(rs.getInt("IS_DEFAULT"));

		return pPolicy;
	}

	/**
	 * @param rs
	 * @param sdList
	 * @throws SQLException
	 */
	private void getSpeedDialList(ResultSet rs,
			HashMap<String, SpeedDialList> sdListMap) throws SQLException {
		SpeedDialList sdList = null;

		String id = rs.getString("LIST_ID");
		if (sdListMap.containsKey(id)) {
			sdList = sdListMap.get(id);
		} else {
			sdList = new SpeedDialList();
			sdListMap.put(id, sdList);
		}

		sdList.setListId(id);
		sdList.setName(rs.getString("LIST_NAME"));
		sdList.setParentId(rs.getString("PARENT_ID"));
		sdList.setParentType(rs.getString("PARENT_TYPE"));
		sdList.setTenantId(rs.getString("TENANT_ID"));

		SpeedDialListEntry sdlEntry = new SpeedDialListEntry();
		sdlEntry.setEntryId(rs.getString("ENTRY_ID"));
		sdlEntry.setName(rs.getString("ENTRY_NAME"));
		sdlEntry.setNumber(rs.getString("ENTRY_NUMBER"));

		sdList.addEntry(sdlEntry);
	}

	/**
	 * @param rs
	 * @return
	 * @throws SQLException
	 */
	private AgentProfile getAgentProfile(ResultSet rs) throws SQLException {
		AgentProfile profile = new AgentProfile();

		profile.setParentId(rs.getString("PARENT_ID"));
		profile.setParentType(rs.getString("PARENT_TYPE"));
		profile.setProfileId(rs.getString("PROFILE_ID"));
		profile.setProfileName(rs.getString("PROFILE_NAME"));
		profile.setProfileText(rs.getString("PROFILE_TEXT"));
		profile.setTenantId(rs.getString("TENANT_ID"));

		return profile;
	}

	/**
	 * @param rs
	 * @return
	 * @throws SQLException
	 */
	private Agent getAgent(ResultSet rs) throws SQLException {
		Agent agent = new Agent();

		// AGENT_ID, AGENT_NAME, EXTERNAL_ID, LOGIN, PASSWD,
		// PASSWD_LAST_MODIFIED_TIMESTAMP, PROFILE_ID, TENANT_ID,
		// USER_ID, DEFAULT_POSITION
		agent.setAgentId(rs.getString("AGENT_ID"));
		agent.setAgentName(rs.getString("AGENT_NAME"));
		agent.setExternalId(rs.getString("EXTERNAL_ID"));
		agent.setLogin(rs.getString("LOGIN"));
		agent.setPassword(rs.getString("PASSWD"));
		agent.setPasswordLastModifiedTimestamp(rs
				.getLong("PASSWD_LAST_MODIFIED_TIMESTAMP"));
		agent.setProfileId(rs.getString("PROFILE_ID"));
		agent.setTenantId(rs.getString("TENANT_ID"));
		agent.setUserId(rs.getString("USER_ID"));
		agent.setSiteId(rs.getString("SITE_ID"));
		agent.setDefaultDn(rs.getString("DEFAULT_POSITION"));
		agent.setIsLocked(rs.getInt("IS_LOCKED") == 1);
		agent.setNickName(rs.getString("NICK_NAME"));

		int i = rs.getInt("IS_DEFAULT");
		if (i == 1) {
			agent.setMustChangePassword(true);
		} else {
			agent.setMustChangePassword(false);
		}

		return agent;
	}

	/**
	 * @param rs
	 * @return
	 * @throws SQLException
	 */
	private Site getSite(ResultSet rs) throws SQLException {
		Site site = new Site();

		site.setSiteId(rs.getString("SITE_ID"));
		site.setSiteName(rs.getString("SITE_NAME"));
		site.setTenantId(rs.getString("TENANT_ID"));

		return site;
	}

	/**
	 * @param rs
	 * @return
	 * @throws SQLException
	 */
	private VirtualTeam getVirtualTeam(ResultSet rs) throws SQLException {
		VirtualTeam vteam = new VirtualTeam();

		vteam.setId(rs.getString("VIR_TEAM_ID"));
		vteam.setName(rs.getString("VIR_TEAM_NAME"));

		int i = rs.getInt("VIR_TEAM_TYPE");
		if (i == 0) {
			vteam.setType("inboundentrypoint");
		} else if (i == 1){
			vteam.setType("inboundqueue");
		}
		else if (i == 2){
			vteam.setType("outdialentrypoint");
		}
		else {
			vteam.setType("outdialqueue");
		}

		vteam.setTenantId(rs.getString("TENANT_ID"));
		vteam.setSeratelAcd(rs.getInt("SERATEL_ACD"));

		return vteam;
	}

	/**
	 * @param rs
	 * @return
	 * @throws SQLException
	 */
	private Team getTeam(ResultSet rs) throws SQLException {
		Team team = new Team();

		team.setTeamId(rs.getString("TEAM_ID"));
		team.setTeamName(rs.getString("TEAM_NAME"));
		team.setTenantId(rs.getString("TENANT_ID"));

		return team;
	}

	/**
	 * @param rs
	 * @return
	 * @throws SQLException
	 */
	private Tenant getTenant(ResultSet rs) throws SQLException {
		Tenant tenant = new Tenant();

		tenant.setAddressBookId(rs.getString("ADDRESS_BOOK_VALUE"));
		tenant.setAutoWrapupInterval(rs.getInt("AUTO_WRAPUP_INTERVAL"));
		tenant.setCadHiddenFromAgent(rs.getString("CAD_HIDDEN_FROM_AGENT"));
		tenant.setCompanyName(rs.getString("COMPANY_NAME"));
		tenant.setLoginDomain(rs.getString("LOGIN_DOMAIN"));
		tenant.setTenantId(rs.getString("TENANT_ID"));
		tenant.setTenantXmlUrl(rs.getString("TENANT_XML_URL"));

		int i = rs.getInt("MULTI_LOGIN_PRECEDENCE");
		boolean b = false;
		if (i == 1) {
			b = true;
		}

		tenant.setFirstLoginPrecedence(b);

		return tenant;
	}

	/**
	 * @param rs
	 * @return
	 * @throws SQLException
	 */
	private MediaProfile getAgentMediaProfile(ResultSet rs) throws SQLException {
		MediaProfile mediaProfile = new MediaProfile();

		mediaProfile.setTenantId(rs.getString("TENANT_ID"));
		mediaProfile.setMediaProfileId(rs.getString("MEDIA_PROFILE_ID"));
		mediaProfile.setMediaProfileName(rs.getString("MEDIA_PROFILE_NAME"));
		mediaProfile.setNumberOfTelephonyChannels(rs
				.getInt("NUMBER_OF_PHONE_CALLS"));
		mediaProfile.setNumberOfEmailChannels(rs.getInt("NUMBER_OF_EMAILS"));
		mediaProfile.setNumberOfFaxChannels(rs.getInt("NUMBER_OF_FAXES"));
		mediaProfile.setNumberOfChatChannels(rs.getInt("NUMBER_OF_CHATS"));
		mediaProfile.setNumberOfVideoChannels(rs.getInt("NUMBER_OF_VIDEOS"));
		mediaProfile.setNumberOfOtherChannels(rs.getInt("NUMBER_OF_OTHER"));
		mediaProfile.setBlendingMode(rs.getInt("BLENDING_MODE"));
		return mediaProfile;
	}
	
	/**
	 * @param rs
	 * @return
	 * @throws SQLException
	 */
	private ThirdPartyProfileAddInfo getAgentTPProfileAdditionalInfo(ResultSet rs) throws SQLException {
		ThirdPartyProfileAddInfo profile = new ThirdPartyProfileAddInfo();

		profile.setTpProfileId(rs.getString("ID"));
		profile.setTenantId(rs.getString("TENANT_ID"));
		profile.setChatVendorId(rs.getString("CHAT_VENDOR_ID"));
		profile.setEmailVendorId(rs.getString("EMAIL_VENDOR_ID"));

		return profile;
	}
	
	/**
	 * @param rs
	 * @return
	 * @throws SQLException
	 */
	private ThirdPartyProfile getAgentTPProfile(ResultSet rs) throws SQLException {
		ThirdPartyProfile profile = new ThirdPartyProfile();

		profile.setTpProfileId(rs.getString("ID"));
		profile.setTpProfileName(rs.getString("NAME"));
		profile.setTenantId(rs.getString("TENANT_ID"));
		profile.setMmProfile(rs.getString("MM_PROFILE_ID"));
		profile.setCasEnabled(rs.getString("CAS_ENABLED") == "1");

		return profile;
	}
	
	/**
	 * @param rs
	 * @return
	 * @throws SQLException
	 */
	private ThirdPartyVendor getThirdPartyVendors(ResultSet rs) throws SQLException {
		ThirdPartyVendor profile = new ThirdPartyVendor();
		profile.setVendorId(rs.getString("ID"));
		profile.setVendorName(rs.getString("NAME"));
		profile.setTenantId(rs.getString("TENANT_ID"));
		profile.setChannelType(rs.getString("CHANNEL_TYPE"));
		profile.setAgentUrl(rs.getString("AGENT_URL"));
		profile.setProvUrl(rs.getString("PROVISIONING_URL"));
		
		return profile;
	}
}
