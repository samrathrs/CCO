/**
 * 
 */
package com.transerainc.aim.pojo;

import java.io.Serializable;

import org.apache.commons.lang.math.NumberUtils;

/**
 * @author <a href="mailto:ramesh.bobba@transerainc.com">Ramesh Bobba</a>
 * @version $Rivision:$
 */
public class Team implements Serializable, Comparable<Team> {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String teamId;
	private String teamName;
	private String tenantId;

	/**
	 * @return the teamId
	 */
	public String getTeamId() {
		return teamId;
	}

	/**
	 * @param teamId
	 *            the teamId to set
	 */
	public void setTeamId(String teamId) {
		this.teamId = teamId;
	}

	/**
	 * @return the teamName
	 */
	public String getTeamName() {
		return teamName;
	}

	/**
	 * @param teamName
	 *            the teamName to set
	 */
	public void setTeamName(String teamName) {
		this.teamName = teamName;
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

	public String toString() {
		return "\nTeam Id: " + teamId + ", Team Name: " + teamName
			+ ", Tenant Id: " + tenantId;
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Comparable#compareTo(java.lang.Object)
	 */
	public int compareTo(Team o) {
		Integer id1 = NumberUtils.toInt(this.teamId, 0);
		Integer id2 = NumberUtils.toInt(o.teamId, 0);
		return id1.compareTo(id2);
	}
}
