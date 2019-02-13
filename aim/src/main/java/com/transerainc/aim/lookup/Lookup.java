/**
 * 
 */
package com.transerainc.aim.lookup;

import java.util.List;

/**
 * @author <a href="mailto:ramesh.bobba@transerainc.com">Ramesh Bobba</a>
 * @version $Rivision:$
 */

public class Lookup {
	private String agentId;
	private String tenantId;
	private List<String> teamIdList;
	private List<String> subStatusList;
	private List<String> acgUrlList;
	private String channelType;


	/**
	 * @return the channelType
	 */
	public String getChannelType() {
		return channelType;
	}

	/**
	 * @param channelType the channelType to set
	 */
	public void setChannelType(String channelType) {
		this.channelType = channelType;
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
	 * @return the teamIdList
	 */
	public List<String> getTeamIdList() {
		return teamIdList;
	}

	/**
	 * @param teamIdList
	 *            the teamIdList to set
	 */
	public void setTeamIdList(List<String> teamIdList) {
		this.teamIdList = teamIdList;
	}

	/**
	 * @return the subStatusList
	 */
	public List<String> getSubStatusList() {
		return subStatusList;
	}

	/**
	 * @param subStatusList
	 *            the subStatusList to set
	 */
	public void setSubStatusList(List<String> subStatusList) {
		this.subStatusList = subStatusList;
	}

	/**
	 * @return the acgUrlList
	 */
	public List<String> getAcgUrlList() {
		return acgUrlList;
	}

	/**
	 * @param acgUrlList
	 *            the acgUrlList to set
	 */
	public void setAcgUrlList(List<String> acgUrlList) {
		this.acgUrlList = acgUrlList;
	}

	public String toString() {
		return "Looking for agent " + agentId + ", Tenant Id: " + tenantId
			+ ", team Ids: " + teamIdList + ", Sub Status List: "
			+ subStatusList + ", Channel Type:" + channelType + ", ACG URL List: " + acgUrlList;
	}
}
