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
public class Site implements Serializable,Comparable<Site> {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String siteId;
	private String siteName;
	private String tenantId;

	/**
	 * @return the siteId
	 */
	public String getSiteId() {
		return siteId;
	}

	/**
	 * @param siteId
	 *            the siteId to set
	 */
	public void setSiteId(String siteId) {
		this.siteId = siteId;
	}

	/**
	 * @return the siteName
	 */
	public String getSiteName() {
		return siteName;
	}

	/**
	 * @param siteName
	 *            the siteName to set
	 */
	public void setSiteName(String siteName) {
		this.siteName = siteName;
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
		return "\nSite Id: " + siteId + ", Site Name: " + siteName
			+ ", Tenant Id: " + tenantId;
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Comparable#compareTo(java.lang.Object)
	 */
	public int compareTo(Site o) {
		Integer id1 = NumberUtils.toInt(this.siteId, 0);
		Integer id2 = NumberUtils.toInt(o.siteId, 0);
		return id1.compareTo(id2);
	}
}
