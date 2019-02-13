package com.transerainc.aim.pojo;

import java.io.Serializable;

import org.apache.commons.lang.math.NumberUtils;

public class ThirdPartyProfile implements Serializable, Comparable<ThirdPartyProfile> {
	
	private static final long serialVersionUID = 1L;
	private String tpProfileId;
	private String tenantId;
	private String tpProfileName;
	private String mmProfile;
	private boolean isCasEnabled;
	 		
	public String getTpProfileId() {
		return tpProfileId;
	}


	public void setTpProfileId(String tpProfileId) {
		this.tpProfileId = tpProfileId;
	}


	public String getTenantId() {
		return tenantId;
	}


	public void setTenantId(String tId) {
		this.tenantId = tId;
	}


	public String getTpProfileName() {
		return tpProfileName;
	}


	public void setTpProfileName(String tpProfileName) {
		this.tpProfileName = tpProfileName;
	}


	public String getMmProfile() {
		return mmProfile;
	}


	public void setMmProfile(String mmProfile) {
		this.mmProfile = mmProfile;
	}


	public boolean isCasEnabled() {
		return isCasEnabled;
	}


	public void setCasEnabled(boolean isCasEnabled) {
		this.isCasEnabled = isCasEnabled;
	}

	@Override
	public String toString() {
		return "AgentThirdPartyProfile [tpProfileId=" + tpProfileId
				+ ", tenantId=" + tenantId + ", tpProfileName=" + tpProfileName
				+ ", mmProfile=" + mmProfile + ", isCasEnabled="
				+ isCasEnabled + "]";
	}


	public int compareTo(ThirdPartyProfile o) {
		Integer id1 = NumberUtils.toInt(this.tpProfileId, 0);
		Integer id2 = NumberUtils.toInt(o.tpProfileId, 0);
		return id1.compareTo(id2);
	}

}
