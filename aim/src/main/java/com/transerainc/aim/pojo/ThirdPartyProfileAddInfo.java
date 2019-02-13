package com.transerainc.aim.pojo;

import java.io.Serializable;

import org.apache.commons.lang.math.NumberUtils;

public class ThirdPartyProfileAddInfo implements Serializable, Comparable<ThirdPartyProfileAddInfo>{
	private static final long serialVersionUID = 1L;
	private String tpProfileId;
	private String tenantId;
	private String chatVendorId;
	private String emailVendorId;
	public String getTpProfileId() {
		return tpProfileId;
	}
	public void setTpProfileId(String tpProfileId) {
		this.tpProfileId = tpProfileId;
	}
	public String getTenantId() {
		return tenantId;
	}
	public void setTenantId(String tenantId) {
		this.tenantId = tenantId;
	}
	public String getChatVendorId() {
		return chatVendorId;
	}
	public void setChatVendorId(String chatVendorId) {
		this.chatVendorId = chatVendorId;
	}
	public String getEmailVendorId() {
		return emailVendorId;
	}
	public void setEmailVendorId(String emailVendorId) {
		this.emailVendorId = emailVendorId;
	}
	@Override
	public String toString() {
		return "ThirdPartyProfileAddInfo [tpProfileId=" + tpProfileId
				+ ", tenantId=" + tenantId + ", chatVendorId=" + chatVendorId
				+ ", emailVendorId=" + emailVendorId + "]";
	}
	@Override
	public int compareTo(ThirdPartyProfileAddInfo o) {
		Integer id1 = NumberUtils.toInt(this.tpProfileId, 0);
		Integer id2 = NumberUtils.toInt(o.tpProfileId, 0);
		return id1.compareTo(id2);
	}
}
