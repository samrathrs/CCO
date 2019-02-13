package com.transerainc.aim.pojo;

import java.io.Serializable;

import org.apache.commons.lang.math.NumberUtils;

public class ThirdPartyVendor implements Serializable, Comparable<ThirdPartyVendor> {
	private static final long serialVersionUID = 1L;
	private String vendorId;
	private String vendorName;
	private String tenantId;
	private String channelType;
	private String agentUrl;
	private String provUrl;
	
	
	public String getVendorId() {
		return vendorId;
	}
	public void setVendorId(String vendorId) {
		this.vendorId = vendorId;
	}
	public String getVendorName() {
		return vendorName;
	}
	public void setVendorName(String vendorName) {
		this.vendorName = vendorName;
	}
	public String getTenantId() {
		return tenantId;
	}
	public void setTenantId(String tenantId) {
		this.tenantId = tenantId;
	}
	public String getChannelType() {
		return channelType;
	}
	public void setChannelType(String channelType) {
		this.channelType = channelType;
	}
	public String getAgentUrl() {
		return agentUrl;
	}
	public void setAgentUrl(String agentUrl) {
		this.agentUrl = agentUrl;
	}
	public String getProvUrl() {
		return provUrl;
	}
	public void setProvUrl(String provUrl) {
		this.provUrl = provUrl;
	}
	
	@Override
	public String toString() {
		return "ThirdPartyVendors [vendorId=" + vendorId + ", vendorName="
				+ vendorName + ", tenantId=" + tenantId + ", channelType="
				+ channelType + ", agentUrl=" + agentUrl + ", provUrl="
				+ provUrl + "]";
	}
	@Override
	public int compareTo(ThirdPartyVendor o) {
		Integer id1 = NumberUtils.toInt(this.vendorId, 0);
		Integer id2 = NumberUtils.toInt(o.vendorId, 0);
		return id1.compareTo(id2);
	}
	
}
