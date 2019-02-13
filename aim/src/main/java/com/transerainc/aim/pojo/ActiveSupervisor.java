/**
 * 
 */
package com.transerainc.aim.pojo;

import org.apache.commons.lang.math.NumberUtils;

/**
 * @author :rajpal.dangi@transerainc.com
 * 
 */
public class ActiveSupervisor implements Comparable<ActiveSupervisor>{
	private String supervisorId;
	private String sessionId;
	private String tenantId;
	private String callbackUrl;
	private long timestamp;
	
	public ActiveSupervisor(){
		
	}
	
	public ActiveSupervisor(ActiveSupervisor supervisor){
		this.supervisorId = supervisor.supervisorId;
		this.sessionId = supervisor.sessionId;
		this.tenantId  = supervisor.tenantId;
		this.callbackUrl = supervisor.callbackUrl;
		this.timestamp = supervisor.timestamp;
	}
	
	public String getSupervisorId() {
		return supervisorId;
	}

	public void setSupervisorId(String supervisorId) {
		this.supervisorId = supervisorId;
	}

	public String getSessionId() {
		return sessionId;
	}

	public void setSessionId(String sessionId) {
		this.sessionId = sessionId;
	}

	public String getTenantId() {
		return tenantId;
	}

	public void setTenantId(String tenantId) {
		this.tenantId = tenantId;
	}

	public String getCallbackUrl() {
		return callbackUrl;
	}

	public void setCallbackUrl(String callbackUrl) {
		this.callbackUrl = callbackUrl;
	}

	public long getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(long timestamp) {
		this.timestamp = timestamp;
	}
	

	/* (non-Javadoc)
	 * @see java.lang.Comparable#compareTo(java.lang.Object)
	 */
	public int compareTo(ActiveSupervisor arg0) {
		
		Long id1 = NumberUtils.toLong(this.supervisorId, 0);
		Long id2 = NumberUtils.toLong(arg0.supervisorId, 0);

		return id1.compareTo(id2);
	}

	@Override
	public String toString() {
		return "ActiveSupervisor [supervisorId=" + supervisorId
				+ ", sessionId=" + sessionId + ", tenantId=" + tenantId
				+ ", callbackUrl=" + callbackUrl + ", timestamp=" + timestamp
				+ "]";
	}

	

}
