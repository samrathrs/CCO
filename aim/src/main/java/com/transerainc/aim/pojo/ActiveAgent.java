/**
 * 
 */
package com.transerainc.aim.pojo;

import org.apache.commons.lang.math.NumberUtils;

/**
 * @author <a href="mailto:ramesh.bobba@transerainc.com">Ramesh Bobba</a>
 * @version $Rivision:$
 */
public class ActiveAgent implements Comparable<ActiveAgent>{
	private String agentId;
	private String acgUrl;
	private long timestamp;
	private String teamId;	
	private String tenantId;
	private String agentSessionId;
	private String dn;
	private String siteId;
	private String channelId;
	private String channelType;
	private String channelStatus;
	private String externalIpAddress;
	private String hostIpAddress;
	
	public ActiveAgent(){
		
	}
	
	public ActiveAgent(ActiveAgent agent){
		this.agentId = agent.agentId;
		this.acgUrl = agent.acgUrl;
		this.teamId = agent.teamId;
		this.tenantId  = agent.tenantId;
		this.agentSessionId = agent.agentSessionId;
		this.dn = agent.dn;
		this.siteId = agent.siteId;
	}
	
	/**
	 * @return the channelType
	 */
	public String getChannelType() {
		return channelType;
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
	 * @param channelType the channelType to set
	 */
	public void setChannelType(String channelType) {
		this.channelType = channelType;
	}

	/**
	 * @return the channelStatus
	 */
	public String getChannelStatus() {
		return channelStatus;
	}

	/**
	 * @param channelStatus the channelStatus to set
	 */
	public void setChannelStatus(String channelStatus) {
		this.channelStatus = channelStatus;
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
	 * @return the tpgUrl
	 */
	public String getAcgUrl() {
		return acgUrl;
	}

	/**
	 * @param tpgUrl
	 *            the tpgUrl to set
	 */
	public void setAcgUrl(String acgUrl) {
		this.acgUrl = acgUrl;
	}

	/**
	 * @return the timestamp
	 */
	public long getTimestamp() {
		return timestamp;
	}

	/**
	 * @param timestamp
	 *            the timestamp to set
	 */
	public void setTimestamp(long timestamp) {
		this.timestamp = timestamp;
	}

	/**
	 * @param teamId
	 */
	public void setTeamId(String teamId) {
		this.teamId = teamId;
	}

	public String getTeamId() {
		return teamId;
	}

	/**
	 * @param tenantId
	 */
	public void setTenantId(String tenantId) {
		this.tenantId = tenantId;
	}

	/**
	 * @return the tenantId
	 */
	public String getTenantId() {
		return tenantId;
	}

	/**
	 * @return the agentSessionId
	 */
	public String getAgentSessionId() {
		return agentSessionId;
	}

	/**
	 * @param agentSessionId
	 *            the agentSessionId to set
	 */
	public void setAgentSessionId(String agentSessionId) {
		this.agentSessionId = agentSessionId;
	}

	/**
	 * @param dn
	 */
	public void setDn(String dn) {
		this.dn = dn;
	}

	public String getDn() {
		return dn;
	}
	
	/**
	 * @param siteId
	 */
	public void setSiteId(String siteId) {
		this.siteId = siteId;
	}

	public String getSiteId() {
		return siteId;
	}

	/**
	 * @return the externalIpAddress
	 */
	public String getExternalIpAddress() {
		return externalIpAddress;
	}

	/**
	 * @param externalIpAddress the externalIpAddress to set
	 */
	public void setExternalIpAddress(String externalIpAddress) {
		this.externalIpAddress = externalIpAddress;
	}

	/**
	 * @return the hostIpAddress
	 */
	public String getHostIpAddress() {
		return hostIpAddress;
	}

	/**
	 * @param hostIpAddress the hostIpAddress to set
	 */
	public void setHostIpAddress(String hostIpAddress) {
		this.hostIpAddress = hostIpAddress;
	}
	

	public String toString() {
		return "Agent " + agentId + ", Team " + teamId + ", ACG URL: " + acgUrl
			+ ", Got the event @ " + timestamp			
			+ ", Tenant Id: " + tenantId + ", Agent Session Id: "
			+ agentSessionId + ", DN: " + dn + ", Site Id: " + siteId +
			", Channel-Id: "+ channelId + ", Channel Status: " + channelStatus +
			", Channel Type: "+ channelType;		
	}

	/* (non-Javadoc)
	 * @see java.lang.Comparable#compareTo(java.lang.Object)
	 */
	public int compareTo(ActiveAgent arg0) {
		
		Long id1 = NumberUtils.toLong(this.agentId, 0);
		Long id2 = NumberUtils.toLong(arg0.agentId, 0);

		return id1.compareTo(id2);
	}

	

}
