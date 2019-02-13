/**
 * 
 */
package com.transerainc.aim.pojo;

/**
 * @author <a href="mailto:ramesh.bobba@transerainc.com">Ramesh Bobba</a>
 * @version $Rivision:$
 */

public class AIMNotification {
	private String command;
	private String tenantId;
	private String agentId;
	private String channelId;
	private ActiveAgent activeAgent;
	private ActiveSupervisor activeSupervisor;
	

	private AuthenticationFailure authenticationFailure;

	/**
	 * @return the command
	 */
	public String getCommand() {
		return command;
	}

	/**
	 * @param command
	 *            the command to set
	 */
	public void setCommand(String command) {
		this.command = command;
	}

	/**
	 * @return the channelId
	 */
	public String getChannelId() {
		return channelId;
	}

	/**
	 * @param channelId the channelId to set
	 */
	public void setChannelId(String channelId) {
		this.channelId = channelId;
	}

	/**
	 * @return the tenantId
	 */
	public String getTenantId() {
		return tenantId;
	}

	/**
	 * @param tenantId
	 *            the tenantId to set
	 */
	public void setTenantId(String tenantId) {
		this.tenantId = tenantId;
	}

	/**
	 * @return the agentId
	 */
	public String getAgentId() {
		return agentId;
	}

	/**
	 * @param agentId
	 *            the agentId to set
	 */
	public void setAgentId(String agentId) {
		this.agentId = agentId;
	}

	/**
	 * @return the activeAgent
	 */
	public ActiveAgent getActiveAgent() {
		return activeAgent;
	}

	/**
	 * @param activeAgent
	 *            the activeAgent to set
	 */
	public void setActiveAgent(ActiveAgent activeAgent) {
		this.activeAgent = activeAgent;
	}
	
	public ActiveSupervisor getActiveSupervisor() {
		return activeSupervisor;
	}

	public void setActiveSupervisor(ActiveSupervisor activeSupervisor) {
		this.activeSupervisor = activeSupervisor;
	}

	public String toString() {
		return "Command: " + command + ", Tenant Id: " + tenantId
				+ ", Active Agent: " + activeAgent + ", Agent Id: " + agentId
				+ ", Channel-id:" + channelId + ", Auth Failure: " + authenticationFailure;
	}

	public void setAuthenticationFailure(AuthenticationFailure af) {
		this.authenticationFailure = af;
	}

	public AuthenticationFailure getAuthenticationFailure() {
		return authenticationFailure;
	}
}
