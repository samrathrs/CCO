/**
 * 
 */
package com.transerainc.aim.pojo;

import java.util.List;

/**
 * @author <a href="mailto:ramesh.bobba@transerainc.com">Ramesh Bobba</a>
 * @version $Rivision:$
 */

public class TenantShutdownRequest {
	private String tenantId;
	private List<String> subStatusList;

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

	public boolean isSubStateAllowed(String subStatus) {
		boolean b = true;
		if (subStatusList != null && subStatusList.contains(subStatus)) {
			b = false;
		}

		return b;
	}

	public String toString() {
		return "Tenant Id: " + tenantId + ", States: " + subStatusList;
	}
}
