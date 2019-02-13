/**
 * 
 */
package com.transerainc.aim.login;

import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.transerainc.aim.conf.ConfigurationManager;
import com.transerainc.aim.notifier.ACGNotificationManager;
import com.transerainc.aim.notifier.ACGNotifier;
import com.transerainc.aim.pojo.ACGNotification;
import com.transerainc.aim.pojo.ActiveAgent;
import com.transerainc.aim.pojo.AdhocDialProperty;
import com.transerainc.aim.pojo.Agent;
import com.transerainc.aim.pojo.AgentProfile;
import com.transerainc.aim.pojo.AgentTeamMapping;
import com.transerainc.aim.pojo.AuthenticationFailureResult;
import com.transerainc.aim.pojo.AuxilaryCode;
import com.transerainc.aim.pojo.LoginStatus;
import com.transerainc.aim.pojo.LogoutNotification;
import com.transerainc.aim.pojo.PasswordPolicy;
import com.transerainc.aim.pojo.PasswordPolicyResult;
import com.transerainc.aim.pojo.SpeedDialList;
import com.transerainc.aim.pojo.Team;
import com.transerainc.aim.pojo.Tenant;
import com.transerainc.aim.provisioning.ProvisioningManager;
import com.transerainc.aim.provisioning.handler.AdhocDialPropetiesHandler;
import com.transerainc.aim.provisioning.handler.AgentHandler;
import com.transerainc.aim.provisioning.handler.AgentProfileHandler;
import com.transerainc.aim.provisioning.handler.AgentToTeamMappingHandler;
import com.transerainc.aim.provisioning.handler.IdleCodeHandler;
import com.transerainc.aim.provisioning.handler.PasswordPolicyHandler;
import com.transerainc.aim.provisioning.handler.SiteHandler;
import com.transerainc.aim.provisioning.handler.SpeedDialListHandler;
import com.transerainc.aim.provisioning.handler.TeamHandler;
import com.transerainc.aim.provisioning.handler.TenantHandler;
import com.transerainc.aim.provisioning.handler.VirtualTeamHandler;
import com.transerainc.aim.provisioning.handler.WrapupHandler;
import com.transerainc.aim.runtime.ActiveAgentManager;
import com.transerainc.aim.runtime.AgentLockoutManager;
import com.transerainc.aim.snmp.TrapManager;
import com.transerainc.aim.system.AgentInformationManagerSystem;
import com.transerainc.security.PasswordHasher;
import com.transerainc.tam.config.AccessDetails.AnalyzerAccessDetails;
import com.transerainc.tam.util.HttpStatus;
import com.transerainc.tam.util.HttpUtil;

/**
 * @author <a href="mailto:ramesh.bobba@transerainc.com">Ramesh Bobba</a>
 * @version $Rivision:$
 */
public class LoginHandler {
	private static Logger lgr = Logger.getLogger(LoginHandler.class.getName());
	private String login;
	private String password;
	private String companyName;
	private TrapManager trapMgr;
	private Boolean casAuthenticated;
	private ConfigurationManager configMgr;
	
	public LoginHandler() {

	}

	public LoginHandler(String login, String password, String companyName) {
		this.login = login;
		this.password = password;
//		if(this.password.isEmpty() || this.password =="")
//			this.casAuthenticated=true;
//		else
//			this.casAuthenticated = false;
		this.companyName = companyName.toLowerCase();
		
		try {
			trapMgr = (TrapManager) AgentInformationManagerSystem
					.findBean("trapManager");
			configMgr = (ConfigurationManager) AgentInformationManagerSystem
					.findBean("configManager");
		} catch (Exception e) {
			lgr.log(Level.SEVERE, "Error while getting the trap manager", e);
		}
	}
	
	public LoginHandler(String login, Boolean casAuthenticated, String companyName) {
		this.login = login;
		this.casAuthenticated = getCasAuthenticated();
		this.companyName = companyName.toLowerCase();
		this.password="";

		try {
			trapMgr = (TrapManager) AgentInformationManagerSystem
					.findBean("trapManager");
		} catch (Exception e) {
			lgr.log(Level.SEVERE, "Error while getting the trap manager", e);
		}
	}
	public LoginStatus login() {
		ActiveAgentManager aaMgr = (ActiveAgentManager) AgentInformationManagerSystem
				.findBean("activeAgentManager");
		ProvisioningManager pMgr = (ProvisioningManager) AgentInformationManagerSystem
				.findBean("provManager");
		this.casAuthenticated = getCasAuthenticated();
		AgentHandler agentHandler = pMgr.getAgentHandler();
		TenantHandler tenantHandler = pMgr.getTenantHandler();
		AgentToTeamMappingHandler attMapingHandler = pMgr
				.getAgentTeamMappingHandler();
		SiteHandler siteHandler = pMgr.getSiteHandler();
		TeamHandler teamHandler = pMgr.getTeamHandler();
		AgentProfileHandler apHandler = pMgr.getAgentProfileHandler();
		PasswordPolicyHandler ppHandler = pMgr.getPasswordPolicyHandler();
		IdleCodeHandler icHandler = pMgr.getIdleCodeHandler();
		WrapupHandler wrapupHandler = pMgr.getWrapupHandler();
		AdhocDialPropetiesHandler adpHandler = pMgr
				.getAdhocDialPropetiesHandler();
		SpeedDialListHandler sdlHandler = pMgr.getSpeedDialListHandler();
		VirtualTeamHandler vtHandler = pMgr.getVirtualTeamHandler();

		ConfigurationManager cmgr = (ConfigurationManager) AgentInformationManagerSystem
				.findBean("configManager");

		AgentLockoutManager alMgr = (AgentLockoutManager) AgentInformationManagerSystem
				.findBean("agentLockoutManager");

		String tenantId = tenantHandler.getTenantIdForCompany(companyName);
		Agent agent = agentHandler.getAgentForLogin(login, tenantId);

		if (lgr.isLoggable(Level.INFO)) {
			lgr.info("Checking to see if agent is valid: " + agent);
		}

		LoginStatus status = new LoginStatus();
		if (cmgr.isTenantShutdown(tenantId)) {
			status.setErrorMessage("Tenant is shutdown. Cannot login agents.");
			status.setCode(503);
		} else if (agent == null) {
			// Return an error code as well.
			handleNonExistantAgent(tenantId, status);
			// } else if (checkIfAgentAlreadyLoggedIn(aaMgr, agent, tenantId)) {
			// status.setErrorMessage("Agent " + login + " already logged in.");
			// status.setCode(409);
		} else if (agent.getTenantId().equals(tenantId)) {
			Tenant tenant = tenantHandler.getTenant(tenantId);
			boolean isLoggedIn = aaMgr.isAgentLoggedIn(tenantId,
					agent.getAgentId());

			boolean isLoggedInAcg = checkIfAgentAlreadyLoggedIn(aaMgr, agent,
					tenantId);
			if (lgr.isLoggable(Level.INFO)) {
				String acgStr = isLoggedInAcg ? " logged in as well."
						: " not logged in.";
				String aimStr = isLoggedIn ? " logged in" : " not logged in";
				lgr.info("Agent is valid and in the correct tenant. "
						+ "AIM thinks the agent is " + aimStr + " and TPG "
						+ "says that the agent is " + acgStr);
			}

			if (!isLoggedInAcg && isLoggedIn) {
				aaMgr.removeAgent(tenantId, agent.getAgentId(), "", true);
				isLoggedIn = false;
			}

			if (isLoggedIn && tenant.isFirstLoginPrecedence()) {
				// this means the the first agent is already logged in, so
				// reject this login request.
				handleAlreadyLoggedInAgent(tenantId, agent, aaMgr, status);
			} else if (alMgr.isAgentLocked(agent)) {
				status.setLoggedIn(false);
				status.setCode(410); // Error code for agent locked
				status.setErrorMessage("Agent account is locked");
			} else {
				// This means that the we allow this login and may drop the
				// first login.

				if (lgr.isLoggable(Level.INFO)) {
					lgr.info("Checking to see if a the agent is already "
							+ "logged in and a notification needs to be "
							+ "sent to the appropriate ACG to log him/her out.");
				}

				PasswordPolicyResult ppResult = new PasswordPolicyResult();
				if (!passwordMatch(agent)) { //Fiji CAS
					lgr.warning("Agent " + agent
							+ " provided password does not "
							+ "match provisioned password.");

					// Raise a trap
					trapMgr.sendIncorrectPassword(agent.getAgentId(),
							agent.getAgentName(), agent.getLogin(),
							agent.getTenantId());

					status.setLoggedIn(false);
					status.setCode(401);
					status.setErrorMessage("Provided password does "
							+ "not match the provisioned for company "
							+ companyName);
					AuthenticationFailureResult ar = alMgr.authenticationFailed(agent);									
					if (ar == null) {
						lgr.warning("Unable to determine max invalid attempts for: "
								+ agent.getAgentId()
								+ " tenant: "
								+ agent.getTenantId());
					} else {
                        status.setMaxInvalidAttempts(ar.getMaxInvalid());
                        status.setCurrentInvalidAttempts(ar.getCurrentInavlid());
					}					

				} else if (!this.casAuthenticated && !ppHandler.applyPasswordPolicy(agent, tenantId,
						ppResult)) { //Fiji CAS
					lgr.warning("Agent " + agent
							+ " password validation failed, reason: "
							+ ppResult.getFailureReason());
					status.setLoggedIn(false);
					status.setCode(401);
					status.setErrorMessage(ppResult.getFailureReason());
					if(!("ForcedPasswordChange".equalsIgnoreCase(ppResult.getFailureReason()))){
						alMgr.authenticationFailed(agent);
					}
					
				} else {
					if (isLoggedIn) {
						handleAgentAlreadyLoggedInNotifyACG(tenantId, agent,
								aaMgr);
					}
					if(!this.casAuthenticated) //Fiji CAS
						status.setPwdPolicyResult(ppResult);

					String agentProfileId = agent.getProfileId();
					AgentProfile profile = apHandler
							.getAgentProfile(agentProfileId);

					status.setAgentProfile(profile);

					status.setAgent(agent);

					status.setTenant(tenantHandler.getTenant(tenantId));

					status.setVirtualTeamList(vtHandler
							.getVirtualTeamsForTenant(tenantId));

				    status.setSite(siteHandler.getSite(agent.getSiteId()));
				    

					updateAccessableTeams(attMapingHandler, teamHandler, agent,
							status);
					if(!this.casAuthenticated) //Fiji CAS
						updatePasswordPolicies(ppHandler, tenantId, status);

					updateAuxilaryCodes(icHandler, wrapupHandler, tenantId,
							status);

					updateAdhocDialProperties(adpHandler, tenantId, status);

					updateSpeedDialList(sdlHandler, tenantId, status);

					status.setLoggedIn(true);
					alMgr.clearAgentLockout(agent.getAgentId(), true);
				}
			}
		} else {
			handleUnmatchedTenant(tenantId, agent, status);
		}

		if (lgr.isLoggable(Level.INFO)) {
			lgr.info("Returning Login Status: " + status);
		}

		return status;
	}

	private boolean passwordMatch(Agent agent) {
		if (this.casAuthenticated || agent.getPassword().equals(password)) {
			return true;
		}
		boolean matches = false;
		// Encode the supplied pwd and check
		String awsUrl = null;
		try {
			// If the pwd matches the encoded pwd in DB, return true
			try {
				if (PasswordHasher.check(this.password, agent.getPassword())) {
					return true;
				}
			} catch (Exception e) {
			}
			AnalyzerAccessDetails aad = configMgr.getAnalyzerAccessDetails();
			awsUrl = String.format(
					aad.getBaseUrl()
							+ "/auxiliary-data/user-data/user?login__s=%s",
					login);
			Map<String, String> headers = new HashMap<>();
			String authHeader = String.format("%s; user=%s", aad.getApiKey(),
					login);
			headers.put("From", aad.getFrom());
			headers.put("Authorization", authHeader);
			if (lgr.isLoggable(Level.FINE)) {
				lgr.fine("Fetching user data from analyzer " + awsUrl
						+ " with headers: " + headers);
			}
			HttpStatus status = HttpUtil.doGet(awsUrl, headers, 5000,
					HttpUtil.HTTP_SO_TIMEOUT, agent.getAgentId());
			String resp = status.getResponse();
			if (lgr.isLoggable(Level.INFO)) {
				lgr.info("Response for user object (" + awsUrl + "): " + resp);
			}
			JsonObject queryResult = new Gson().fromJson(resp,
					JsonObject.class);
			String legacyPwd = queryResult.get("auxiliaryDataList")
					.getAsJsonArray().get(0).getAsJsonObject().get("attributes")
					.getAsJsonObject().get("legacyPassword__s").getAsString();
			matches = PasswordHasher.check(this.password, legacyPwd);
		} catch (Exception e) {
			lgr.log(Level.WARNING, "Exception while checking  " + awsUrl,
					e);
			throw new RuntimeException(e);
		}
		return matches;
	}

	public LoginStatus getLoginStatus(String agentId) {
		LoginStatus status = new LoginStatus();

		ProvisioningManager pMgr = (ProvisioningManager) AgentInformationManagerSystem
				.findBean("provManager");

		AgentHandler agentHandler = pMgr.getAgentHandler();
		TenantHandler tenantHandler = pMgr.getTenantHandler();
		AgentToTeamMappingHandler attMapingHandler = pMgr
				.getAgentTeamMappingHandler();
		SiteHandler siteHandler = pMgr.getSiteHandler();
		TeamHandler teamHandler = pMgr.getTeamHandler();
		AgentProfileHandler apHandler = pMgr.getAgentProfileHandler();
		PasswordPolicyHandler ppHandler = pMgr.getPasswordPolicyHandler();
		IdleCodeHandler icHandler = pMgr.getIdleCodeHandler();
		WrapupHandler wrapupHandler = pMgr.getWrapupHandler();
		AdhocDialPropetiesHandler adpHandler = pMgr
				.getAdhocDialPropetiesHandler();
		SpeedDialListHandler sdlHandler = pMgr.getSpeedDialListHandler();
		VirtualTeamHandler vtHandler = pMgr.getVirtualTeamHandler();

		Agent agent = agentHandler.getAgent(agentId);
		String tenantId = agent.getTenantId();

		status.setAgent(agent);
		status.setTenant(tenantHandler.getTenant(tenantId));
		status.setVirtualTeamList(vtHandler.getVirtualTeamsForTenant(tenantId));
		status.setSite(siteHandler.getSite(agent.getSiteId()));

		updateAccessableTeams(attMapingHandler, teamHandler, agent, status);

		String agentProfileId = agent.getProfileId();
		com.transerainc.aim.pojo.AgentProfile profile = apHandler
				.getAgentProfile(agentProfileId);
		status.setAgentProfile(profile);

		// FIXME....Rajpal
		// TACG no longer perform pwd policy validation. Ideally it should have
		// been removed
		// keeping it for backward compatibility

		updatePasswordPolicies(ppHandler, tenantId, status);

		updateAuxilaryCodes(icHandler, wrapupHandler, tenantId, status);

		updateAdhocDialProperties(adpHandler, tenantId, status);

		updateSpeedDialList(sdlHandler, tenantId, status);

		status.setLoggedIn(true);

		return status;
	}

	/**
	 * @param aaMgr
	 * @param agent
	 * @param tenantId
	 * 
	 */
	private boolean checkIfAgentAlreadyLoggedIn(ActiveAgentManager aaMgr,
			Agent agent, String tenantId) {
		boolean loggedIn = false;
		try {

			ActiveAgent activeAgent = aaMgr.getActiveAgentByAgentId(tenantId,
					agent.getAgentId());
			// Set<String> activeChannelList = aaMgr
			// .getActiveChannelForAgent(agent.getAgentId());
			// if (null != activeChannelList && !activeChannelList.isEmpty()) {
			// for (String activeChannelId : activeChannelList) {
			// activeAgent = aaMgr.getActiveAgentByChannelId(tenantId,
			// activeChannelId);
			// if (activeAgent != null)
			// break;
			// }
			// }
			if (activeAgent != null) {
				String acgUrl = activeAgent.getAcgUrl();
				URL url = new URL(acgUrl);

				ConfigurationManager cmgr = (ConfigurationManager) AgentInformationManagerSystem
						.findBean("configManager");
				String scContext = cmgr.getAgentInformationManager()
						.getStatusCheckAcgContext();

				acgUrl = "http://" + url.getHost() + ":" + url.getPort()
						+ scContext + "&agentId=" + agent.getAgentId()
						+ "&channelId=" + activeAgent.getChannelId()
						+ "&enterpriseId=" + tenantId + "&agentSessionId="
						+ activeAgent.getAgentSessionId();

				// Now go check with acg if the agent is still there.
				HttpStatus status = HttpUtil.doGet(acgUrl, 5000,
						agent.getAgentId());
				String resp = status.getResponse();
				if (resp != null && resp.contains("LoggedIn")) {
					lgr.warning("Duplicate Login Request: Agent " + agent
							+ " is already logged in.");
					loggedIn = true;
				}
			}
		} catch (Exception e) {
			lgr.log(Level.SEVERE,
					"Error while checking if the agent is already logged in: "
							+ agent, e);
		}

		return loggedIn;
	}

	/**
	 * @param tenantId
	 * @param agent
	 * @param aaMgr
	 */
	private void handleAgentAlreadyLoggedInNotifyACG(String tenantId,
			Agent agent, ActiveAgentManager aaMgr) {
		// Send a notification to the first ACG to
		// log the agent out.

		ActiveAgent activeAgent = aaMgr.getActiveAgentByAgentId(tenantId,
				agent.getAgentId());

		// Set<String> activeChannelList = aaMgr.getActiveChannelForAgent(agent
		// .getAgentId());
		// if (null != activeChannelList && !activeChannelList.isEmpty()) {
		// for (String activeChannelId : activeChannelList) {
		// activeAgent = aaMgr.getActiveAgentByChannelId(tenantId,
		// activeChannelId);
		// if (activeAgent != null)
		// break;
		// }

		if (activeAgent != null) {
			long ts1 = System.currentTimeMillis();

			lgr.warning("Agent " + agent + " is already logged in from "
					+ activeAgent.getAcgUrl()
					+ ", sending a notification to log him/her out");

			ConfigurationManager cmgr = (ConfigurationManager) AgentInformationManagerSystem
					.findBean("configManager");
			String logoutContext = cmgr.getAgentInformationManager()
					.getLogoutAcgContext();

			String url = null;
			try {
				url = activeAgent.getAcgUrl()
						+ logoutContext
						+ "?agentId="
						+ agent.getAgentId()
						+ "&enterpriseId="
						+ agent.getTenantId()
						+ "&sessionId="
						+ activeAgent.getAgentSessionId()
						+ "&reason="
						+ URLEncoder.encode(
								"Agent Loging In From a Different Location",
								"UTF-8");
			} catch (UnsupportedEncodingException e) {
				lgr.log(Level.SEVERE, "Error while encoding the URL", e);
			}

			if (url != null) {
				ACGNotification notif = new LogoutNotification();
				notif.addAcgUrl(url);
				notif.setMethod(ACGNotification.METHOD_GET);

				ACGNotificationManager notifMgr = (ACGNotificationManager) AgentInformationManagerSystem
						.findBean("acgNotificationManager");

				ACGNotifier notifier = notifMgr.createACGNotifier(notif);
				notifier.notifyACGs();
			}
			long duration = System.currentTimeMillis() - ts1;
			if (lgr.isLoggable(Level.FINE)) {
				lgr.fine("Time took to send the logout request to acg "
						+ activeAgent.getAcgUrl() + " for the agent "
						+ activeAgent.getAgentId() + " is " + duration + " ms");
			}
		} else {
			lgr.warning("No active agent info for Agent " + agent
					+ " with aim, unable to logout agent");
		}
	}

	/**
	 * @param tenantId
	 * @param agent
	 * @param status
	 */
	private void handleUnmatchedTenant(String tenantId, Agent agent,
			LoginStatus status) {
		lgr.warning("Agent  " + agent
				+ " does not have the same tenant as the one provided "
				+ companyName + "(" + tenantId + ")");

		// Raise a trap
		// wrongTenantName(agentId, agentName, login, companyName,
		// tenantId);
		trapMgr.sendWrongTenantName(agent.getAgentId(), agent.getAgentName(),
				login, companyName, tenantId);

		status.setLoggedIn(false);
		status.setCode(406);
		status.setErrorMessage("Tenant for agent does not "
				+ "match the provided tenant " + companyName);
	}

	/**
	 * @param sdlHandler
	 * @param tenantId
	 * @param status
	 */
	private void updateSpeedDialList(SpeedDialListHandler sdlHandler,
			String tenantId, LoginStatus status) {
		List<SpeedDialList> sdlList = sdlHandler
				.getSpeedDialListsForTenant(tenantId);
		if (sdlList != null) {
			for (SpeedDialList sdl : sdlList) {
				status.addSpeedDialList(sdl);
			}
		}
	}

	/**
	 * @param adpHandler
	 * @param tenantId
	 * @param status
	 */
	private void updateAdhocDialProperties(
			AdhocDialPropetiesHandler adpHandler, String tenantId,
			LoginStatus status) {
		List<AdhocDialProperty> adpList = adpHandler
				.getAdhocDialPropertiesForTenant("0");
		if (adpList != null) {
			for (AdhocDialProperty adp : adpList) {
				status.addAdhocDialProperty(adp);
			}
		}

		adpList = adpHandler.getAdhocDialPropertiesForTenant(tenantId);
		if (adpList != null) {
			for (AdhocDialProperty adp : adpList) {
				status.addAdhocDialProperty(adp);
			}
		}
	}

	/**
	 * @param icHandler
	 * @param wrapupHandler
	 * @param tenantId
	 * @param status
	 */
	private void updateAuxilaryCodes(IdleCodeHandler icHandler,
			WrapupHandler wrapupHandler, String tenantId, LoginStatus status) {
		List<AuxilaryCode> wrapupList = new ArrayList<AuxilaryCode>();
		List<AuxilaryCode> idleCodeList = new ArrayList<AuxilaryCode>();

		List<AuxilaryCode> acList = icHandler.getIdleCodesForTenant("0");
		if (acList != null) {
			idleCodeList.addAll(acList);
		}

		acList = icHandler.getIdleCodesForTenant(tenantId);
		if (acList != null) {
			idleCodeList.addAll(acList);
		}

		status.setIdleCodeList(idleCodeList);

		acList = wrapupHandler.getWrapupsForTenant("0");
		if (acList != null) {
			wrapupList.addAll(acList);
		}

		acList = wrapupHandler.getWrapupsForTenant(tenantId);
		if (acList != null) {
			wrapupList.addAll(acList);
		}

		status.setWrapupList(wrapupList);
	}

	/**
	 * @param ppHandler
	 * @param tenantId
	 * @param status
	 */
	private void updatePasswordPolicies(PasswordPolicyHandler ppHandler,
			String tenantId, LoginStatus status) {

		// FIXME::
		// This should be removed in next release of AIM, currently its being
		// sent to TACG to maintain the backward compatibility.
		// Sending only the default password policy

		// Check if password policy assigned to agent,otherwise send default

		// PasswordPolicy pwdPolicy = null;
		// if( status.getPwdPolicyResult() != null)
		// pwdPolicy =
		// ppHandler.getPasswordPolicy(status.getPwdPolicyResult().getId());
		// else {
		// pwdPolicy = ppHandler.getDefaultPasswordPolicyForTenant(tenantId);
		// lgr.warning(" Using default pwd policy, no password policy assigned to agent: "
		// + status.getAgent().getAgentId());
		// }

		PasswordPolicy pwdPolicy = ppHandler
				.getDefaultPasswordPolicyForTenant(tenantId);
		if (pwdPolicy != null) {
			status.setPasswordPolicy(pwdPolicy);
		} else {
			lgr.severe("No default password policy assigned to tenant: "
					+ tenantId);
		}
	}

	/**
	 * @param attMapingHandler
	 * @param teamHandler
	 * @param agent
	 * @param status
	 */
	private void updateAccessableTeams(
			AgentToTeamMappingHandler attMapingHandler,
			TeamHandler teamHandler, Agent agent, LoginStatus status) {
		List<AgentTeamMapping> teamIdList = attMapingHandler
				.getMappingsForAgent(agent.getAgentId());

		List<Team> teamList = new ArrayList<Team>();
		if (teamIdList != null) {
			for (AgentTeamMapping atMapping : teamIdList) {
				String teamId = atMapping.getTeamId();
				Team team = teamHandler.getTeam(teamId);
				if (!teamList.contains(team)) {
					teamList.add(team);
				}
			}
		}
		status.setAgentTeamMappingList(teamIdList);
		status.setTeamList(teamList);
	}

	/**
	 * @param tenantId
	 * @param agent
	 * @param aaMgr
	 * @param status
	 */
	private void handleAlreadyLoggedInAgent(String tenantId, Agent agent,
			ActiveAgentManager aaMgr, LoginStatus status) {

		ActiveAgent activeAgent = aaMgr.getActiveAgentByAgentId(tenantId,
				agent.getAgentId());

		if (null != activeAgent) {
			lgr.warning("Agent " + agent + " is already logged in from ACG at "
					+ activeAgent.getAcgUrl());
		}
		status.setLoggedIn(false);
		status.setCode(403);
		status.setErrorMessage("Agent " + agent.getAgentId()
				+ " is already logged.");
	}

	/**
	 * @param tenantId
	 * @param status
	 */
	private void handleNonExistantAgent(String tenantId, LoginStatus status) {
		lgr.warning("Agent  (" + login + ") does not exist in the system for "
				+ companyName + "(" + tenantId + ")");

		// Raise a trap
		// agentDoesNotExist(login, companyName, tenantId);
		trapMgr.sendAgentDoesNotExist(login, companyName, tenantId);

		status.setLoggedIn(false);
		status.setCode(404);
		status.setErrorMessage("Cannot find agent with login " + login
				+ ". Please check the login id and try again.");
	}
	
	public Boolean getCasAuthenticated() {
		return casAuthenticated;
	}

	public void setCasAuthenticated(Boolean casAuthenticated) {
		this.casAuthenticated = casAuthenticated;
	}
}
