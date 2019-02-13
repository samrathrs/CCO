/**
 * 
 */
package com.transerainc.aim.pojo;

import java.io.Serializable;

import org.apache.commons.lang.builder.CompareToBuilder;

/**
 * @author <a href="mailto:ramesh.bobba@transerainc.com">Ramesh Bobba</a>
 * @version $Rivision:$
 */
public class Tenant implements Serializable, Comparable<Tenant> {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String tenantId;
	private String companyName;
	private String loginDomain;
	private String cadHiddenFromAgent;
	private int autoWrapupInterval;
	private String tenantXmlUrl;
	private String addressBookId;
	private boolean firstLoginPrecedence;

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
	 * @return the companyName
	 */
	public String getCompanyName() {
		return companyName;
	}

	/**
	 * @param companyName
	 *            the companyName to set
	 */
	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}

	/**
	 * @return the loginDomain
	 */
	public String getLoginDomain() {
		return loginDomain;
	}

	/**
	 * @param loginDomain
	 *            the loginDomain to set
	 */
	public void setLoginDomain(String loginDomain) {
		this.loginDomain = loginDomain;
	}

	/**
	 * @return the cadHiddenFromAgent
	 */
	public String getCadHiddenFromAgent() {
		return cadHiddenFromAgent;
	}

	/**
	 * @param cadHiddenFromAgent
	 *            the cadHiddenFromAgent to set
	 */
	public void setCadHiddenFromAgent(String cadHiddenFromAgent) {
		this.cadHiddenFromAgent = cadHiddenFromAgent;
	}

	/**
	 * @return the autoWrapupInterval
	 */
	public int getAutoWrapupInterval() {
		return autoWrapupInterval;
	}

	/**
	 * @param autoWrapupInterval
	 *            the autoWrapupInterval to set
	 */
	public void setAutoWrapupInterval(int autoWrapupInterval) {
		this.autoWrapupInterval = autoWrapupInterval;
	}

	/**
	 * @return the tenantXmlUrl
	 */
	public String getTenantXmlUrl() {
		return tenantXmlUrl;
	}

	/**
	 * @param tenantXmlUrl
	 *            the tenantXmlUrl to set
	 */
	public void setTenantXmlUrl(String tenantXmlUrl) {
		this.tenantXmlUrl = tenantXmlUrl;
	}

	/**
	 * @return the addressBookId
	 */
	public String getAddressBookId() {
		return addressBookId;
	}

	/**
	 * @param addressBookId
	 *            the addressBookId to set
	 */
	public void setAddressBookId(String addressBookId) {
		this.addressBookId = addressBookId;
	}

	/**
	 * @return the firstLoginPrecedence
	 */
	public boolean isFirstLoginPrecedence() {
		return firstLoginPrecedence;
	}

	/**
	 * @param firstLoginPrecedence
	 *            the firstLoginPrecedence to set
	 */
	public void setFirstLoginPrecedence(boolean firstLoginPrecedence) {
		this.firstLoginPrecedence = firstLoginPrecedence;
	}

	public String toString() {
		return "\nTenant Id: " + tenantId + ", Company Name: " + companyName
			+ ", Login Domain: " + loginDomain + ", CAD Hidden From Agent: "
			+ cadHiddenFromAgent + ", Auto Wrapup Interval: "
			+ autoWrapupInterval + ", Tenant XML URL: " + tenantXmlUrl
			+ ", Address Book Id: " + addressBookId
			+ ", First Login Precedence: " + firstLoginPrecedence;
	}

	/* (non-Javadoc)
	 * @see java.lang.Comparable#compareTo(java.lang.Object)
	 */
	public int compareTo(Tenant arg0) {
		CompareToBuilder builder = new CompareToBuilder();
		// reverse sort order
		if (null != this.companyName) {
			builder.append(this.getCompanyName().toLowerCase(), arg0.getCompanyName().toLowerCase());
		}
		return builder.toComparison();
	}

}
