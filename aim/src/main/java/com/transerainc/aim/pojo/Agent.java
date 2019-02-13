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
public class Agent implements Serializable, Comparable<Agent> {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String agentId;
	private String agentName;
	private String externalId;
	private String login;
	private String password;
	private long passwordLastModifiedTimestamp;
	private String defaultDn;
	private String profileId;
	private String tenantId;
	private String userId;
	private String siteId;
	private boolean mustChangePassword = false;
	private boolean isLocked;
	private String nickName;

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
	 * @return the agentName
	 */
	public String getAgentName() {
		return agentName;
	}

	/**
	 * @param agentName
	 *            the agentName to set
	 */
	public void setAgentName(String agentName) {
		this.agentName = agentName;
	}

	/**
	 * @return the externalId
	 */
	public String getExternalId() {
		return externalId;
	}

	/**
	 * @param externalId
	 *            the externalId to set
	 */
	public void setExternalId(String externalId) {
		this.externalId = externalId;
	}

	/**
	 * @return the login
	 */
	public String getLogin() {
		return login;
	}

	/**
	 * @param login
	 *            the login to set
	 */
	public void setLogin(String login) {
		this.login = login;
	}

	/**
	 * @return the password
	 */
	public String getPassword() {
		return password;
	}

	/**
	 * @param password
	 *            the password to set
	 */
	public void setPassword(String password) {
		this.password = password;
	}

	/**
	 * @return the passwordLastModifiedTimestamp
	 */
	public long getPasswordLastModifiedTimestamp() {
		return passwordLastModifiedTimestamp;
	}

	/**
	 * @param passwordLastModifiedTimestamp
	 *            the passwordLastModifiedTimestamp to set
	 */
	public void setPasswordLastModifiedTimestamp(
			long passwordLastModifiedTimestamp) {
		this.passwordLastModifiedTimestamp = passwordLastModifiedTimestamp;
	}

	/**
	 * @return the defaultDn
	 */
	public String getDefaultDn() {
		return defaultDn;
	}

	/**
	 * @param defaultDn
	 *            the defaultDn to set
	 */
	public void setDefaultDn(String defaultDn) {
		this.defaultDn = defaultDn;
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
	 * @return the userId
	 */
	public String getUserId() {
		return userId;
	}

	/**
	 * @param userId
	 *            the userId to set
	 */
	public void setUserId(String userId) {
		this.userId = userId;
	}

	/**
	 * @param string
	 */
	public void setSiteId(String sid) {
		this.siteId = sid;
	}

	public String getSiteId() {
		return this.siteId;
	}

	/**
	 * @return the mustChangePassword
	 */
	public boolean isMustChangePassword() {
		return mustChangePassword;
	}

	/**
	 * @param mustChangePassword
	 *            the mustChangePassword to set
	 */
	public void setMustChangePassword(boolean mustChangePassword) {
		this.mustChangePassword = mustChangePassword;
	}
	
	/**
	 * @param string
	 */
	public void setNickName(String nName) {
		this.nickName = nName;
	}

	public String getNickName() {
		return this.nickName;
	}

	public String toString() {
		return "\nAgent Id: " + agentId + ", Agent Name: " + agentName
				+ ", ExternalId: " + externalId + ", Login: " + login
				+ ", DefaultDN: " + defaultDn + ", Password Last Modified On: "
				+ passwordLastModifiedTimestamp + ", Profile Id: " + profileId
				+ ", Tenant Id: " + tenantId + ", User Id: " + userId
				+ ", Site Id: " + siteId + ", Must Change Passwd? "
				+ mustChangePassword + ", Is Locked? " + isLocked + ", Nick Name: "
				+ nickName;
	}

	public void setIsLocked(boolean isLocked) {
		this.isLocked = isLocked;
	}

	public boolean getIsLocked() {
		return isLocked;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Comparable#compareTo(java.lang.Object)
	 */
	public int compareTo(Agent arg0) {
		Long id1 = NumberUtils.toLong(this.agentId, 0);
		Long id2 = NumberUtils.toLong(arg0.agentId, 0);

		return id1.compareTo(id2);
	}
}
