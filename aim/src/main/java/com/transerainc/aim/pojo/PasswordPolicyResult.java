package com.transerainc.aim.pojo;


public class PasswordPolicyResult {
	 private String id;
     private boolean status;
     private int pwdRemainingDays;
     private String failureReason;
     
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public boolean getStatus() {
		return status;
	}
	public void setStatus(boolean status) {
		this.status = status;
	}
	public int getPwdRemainingDays() {
		return pwdRemainingDays;
	}
	public void setPwdRemainingDays(int pwdRemainingDays) {
		this.pwdRemainingDays = pwdRemainingDays;
	}	
	public String getFailureReason() {
		return failureReason;
	}
	public void setFailureReason(String failureReason) {
		this.failureReason = failureReason;
	}
	@Override
	public String toString() {
		return "PasswordPolicyResult Id=" + id + " isActive=" + status
				+ ", pwdRemainingDays=" + pwdRemainingDays;
	}

	
}
