package com.transerainc.aim.pojo;


public class AuthenticationFailure {
	
	private String agentId;
	
	private int failureCount;
	
	private boolean isLocked;
	
	public AuthenticationFailure() {
	}
	
	public AuthenticationFailure(String agentId) {
		this.agentId = agentId;
	}

	public String getAgentId() {
		return agentId;
	}

	public void setAgentId(String agentId) {
		this.agentId = agentId;
	}

	public int getFailureCount() {
		return failureCount;
	}

	public void setFailureCount(int failureCount) {
		this.failureCount = failureCount;
	}

	public boolean getIsLocked() {
		return isLocked;
	}

	public void setIsLocked(boolean isLocked) {
		this.isLocked = isLocked;
	}
	
}
