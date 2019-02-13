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
public class AgentProfile implements Serializable, Comparable<AgentProfile> {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String parentId;
	private String parentType;
	private String profileId;
	private String profileName;
	private String profileText;
	private String tenantId;
	private String pwdPolicyId;

	/**
	 * @return the parentId
	 */
	public String getParentId() {
		return parentId;
	}

	/**
	 * @param parentId
	 *            the parentId to set
	 */
	public void setParentId(String parentId) {
		this.parentId = parentId;
	}

	/**
	 * @return the parentType
	 */
	public String getParentType() {
		return parentType;
	}

	/**
	 * @param parentType
	 *            the parentType to set
	 */
	public void setParentType(String parentType) {
		this.parentType = parentType;
	}

	/**
	 * @return the profileId
	 */
	public String getProfileId() {
		return profileId;
	}

	/**
	 * @param profileId
	 *            the profileId to set
	 */
	public void setProfileId(String profileId) {
		this.profileId = profileId;
	}

	/**
	 * @return the profileName
	 */
	public String getProfileName() {
		return profileName;
	}

	/**
	 * @param profileName
	 *            the profileName to set
	 */
	public void setProfileName(String profileName) {
		this.profileName = profileName;
	}

	/**
	 * @return the profileText
	 */
	public String getProfileText() {
		return profileText;
	}

	/**
	 * @param profileText
	 *            the profileText to set
	 */
	public void setProfileText(String profileText) {
		this.profileText = profileText;
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
		return "\nParent Id: " + parentId + ", Parent Type: " + parentType
			+ ", Profile Id: " + profileId + ", Profile Name: " + profileName
			+ ", Tenant Id: " + tenantId + ", Profile Text: " + profileText;
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Comparable#compareTo(java.lang.Object)
	 */
	public int compareTo(AgentProfile o) {
		Integer id1 = NumberUtils.toInt(this.profileId, 0);
		Integer id2 = NumberUtils.toInt(o.profileId, 0);
		return id1.compareTo(id2);
	}
}
