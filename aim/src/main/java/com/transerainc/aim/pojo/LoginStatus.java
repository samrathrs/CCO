/**
 * 
 */
package com.transerainc.aim.pojo;

import java.util.ArrayList;
import java.util.List;

/**
 * @author <a href="mailto:ramesh.bobba@transerainc.com">Ramesh Bobba</a>
 * @version $Rivision:$
 */
public class LoginStatus {
	private boolean loggedIn;
	private String errorMessage;
	private AgentProfile agentProfile;
	private Agent agent;
	private PasswordPolicy passwordPolicy;
	private List<Team> teamList;
	private List<AgentTeamMapping> agentTeamMapping;
	private List<AuxilaryCode> idleCodeList;
	private List<AuxilaryCode> wrapupList;
	private List<AdhocDialProperty> adhocDialPropertyList;
	private List<SpeedDialList> speedDialLists;
	private int code;
	private Tenant tenant;
	private Site site;
	private List<VirtualTeam> virtualTeamList;
	private PasswordPolicyResult pwdPolicyResult;
	private int maxInvalidAttempts;
	private int currentInvalidAttempts;

	/**
	 * @return the loggedIn
	 */
	public boolean isLoggedIn() {
		return loggedIn;
	}

	/**
	 * @param loggedIn
	 *            the loggedIn to set
	 */
	public void setLoggedIn(boolean loggedIn) {
		this.loggedIn = loggedIn;
	}

	/**
	 * @return the errorMessage
	 */
	public String getErrorMessage() {
		return errorMessage;
	}

	/**
	 * @param errorMessage
	 *            the errorMessage to set
	 */
	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}

	/**
	 * @return the agentProfile
	 */
	public AgentProfile getAgentProfile() {
		return agentProfile;
	}

	/**
	 * @param agentProfile
	 *            the agentProfile to set
	 */
	public void setAgentProfile(AgentProfile agentProfile) {
		this.agentProfile = agentProfile;
	}

	/**
	 * @return the agent
	 */
	public Agent getAgent() {
		return agent;
	}

	/**
	 * @param agent
	 *            the agent to set
	 */
	public void setAgent(Agent agent) {
		this.agent = agent;
	}

	/**
	 * @return the passwordPolicy
	 */
	public PasswordPolicy getPasswordPolicy() {
		return passwordPolicy;
	}

	/**
	 * @param passwordPolicy
	 *            the passwordPolicy to set
	 */
	public void setPasswordPolicy(PasswordPolicy passwordPolicy) {
		this.passwordPolicy = passwordPolicy;
	}

	/**
	 * @return the teamList
	 */
	public List<Team> getTeamList() {
		return teamList;
	}

	/**
	 * @param teamList
	 *            the teamList to set
	 */
	public void setTeamList(List<Team> teamList) {
		this.teamList = teamList;
	}

	/**
	 * @return the agentTeamMapping
	 */
	public List<AgentTeamMapping> getAgentTeamMappingList() {
		return agentTeamMapping;
	}

	/**
	 * @param agentTeamMapping
	 *            the agentTeamMapping to set
	 */
	public void setAgentTeamMappingList(List<AgentTeamMapping> agentTeamMapping) {
		this.agentTeamMapping = agentTeamMapping;
	}

	/**
	 * @param idleCodeList
	 */
	public void setIdleCodeList(List<AuxilaryCode> idleCodeList) {
		this.idleCodeList = idleCodeList;
	}

	public List<AuxilaryCode> getIdleCodeList() {
		return idleCodeList;
	}

	/**
	 * @param wrapupList
	 */
	public void setWrapupList(List<AuxilaryCode> wrapupList) {
		this.wrapupList = wrapupList;
	}

	public List<AuxilaryCode> getWrapupList() {
		return wrapupList;
	}

	/**
	 * @param i
	 */
	public void setCode(int code) {
		this.code = code;
	}

	public int getCode() {
		return this.code;
	}

	/**
	 * @return the adhocDialPropertyList
	 */
	public List<AdhocDialProperty> getAdhocDialPropertyList() {
		return adhocDialPropertyList;
	}

	/**
	 * @param adhocDialPropertyList
	 *            the adhocDialPropertyList to set
	 */
	public void setAdhocDialPropertyList(
			List<AdhocDialProperty> adhocDialPropertyList) {
		this.adhocDialPropertyList = adhocDialPropertyList;
	}

	public void addAdhocDialProperty(AdhocDialProperty adp) {
		if (adhocDialPropertyList == null) {
			adhocDialPropertyList = new ArrayList<AdhocDialProperty>();
		}

		adhocDialPropertyList.add(adp);
	}

	/**
	 * @return the speedDialLists
	 */
	public List<SpeedDialList> getSpeedDialLists() {
		return speedDialLists;
	}

	/**
	 * @param speedDialLists
	 *            the speedDialLists to set
	 */
	public void setSpeedDialLists(List<SpeedDialList> speedDialLists) {
		this.speedDialLists = speedDialLists;
	}

	public void addSpeedDialList(SpeedDialList sdl) {
		if (speedDialLists == null) {
			speedDialLists = new ArrayList<SpeedDialList>();
		}

		speedDialLists.add(sdl);
	}

	/**
	 * @param tenant
	 */
	public void setTenant(Tenant tenant) {
		this.tenant = tenant;
	}

	public Tenant getTenant() {
		return tenant;
	}

	/**
	 * @param site
	 */
	public void setSite(Site site) {
		this.site = site;
	}

	public Site getSite() {
		return this.site;
	}

	/**
	 * @param virtualTeamIdsForTenant
	 */
	public void setVirtualTeamList(List<VirtualTeam> vtList) {
		this.virtualTeamList = vtList;
	}

	/**
	 * @return the virtualTeamList
	 */
	public List<VirtualTeam> getVirtualTeamList() {
		return virtualTeamList;
	}

	public PasswordPolicyResult getPwdPolicyResult() {
		return pwdPolicyResult;
	}

	public void setPwdPolicyResult(PasswordPolicyResult pwdPolicyResult) {
		this.pwdPolicyResult = pwdPolicyResult;
	}

	public int getMaxInvalidAttempts() {
		return maxInvalidAttempts;
	}

	public void setMaxInvalidAttempts(int maxInvalidAttempts) {
		this.maxInvalidAttempts = maxInvalidAttempts;
	}

	public int getCurrentInvalidAttempts() {
		return currentInvalidAttempts;
	}

	public void setCurrentInvalidAttempts(int currentInvalidAttempts) {
		this.currentInvalidAttempts = currentInvalidAttempts;
	}

	public String toString() {
		return "Tenant " + tenant + ", Virtual Team List: " + virtualTeamList
			+ ", Site: " + site + ", Agent " + agent
			+ ", Is login successful? " + loggedIn + ", with profile "
			+ agentProfile + ", with password policy "
			+ passwordPolicy + ", can login into teams " + teamList
			+ ", with wrapup list " + wrapupList + " and idle code list "
			+ idleCodeList + ", Speed Dial Lists: " + speedDialLists
			+ ", Adhoc Dial Properties: " + adhocDialPropertyList
			+ ", Password policy result: " + pwdPolicyResult
			+ " Or has an error: " + errorMessage + ", Code: " + code 
			+ " Max Invalid: " + maxInvalidAttempts
			+ " Current Invalid: " + currentInvalidAttempts;
	}
}
