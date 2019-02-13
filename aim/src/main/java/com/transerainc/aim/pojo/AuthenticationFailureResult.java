/**
 * @author Rajpal Dangi 
 * 
 * @file   AuthenticationResult.java
 *
 * @email  rajpal.dangi@transerainc.com
 *
 *
 */
package com.transerainc.aim.pojo;

public class AuthenticationFailureResult {
	private int maxInvalid;
	private int currentInavlid;
	public int getMaxInvalid() {
		return maxInvalid;
	}
	public void setMaxInvalid(int maxInvalid) {
		this.maxInvalid = maxInvalid;
	}
	public int getCurrentInavlid() {
		return currentInavlid;
	}
	public void setCurrentInavlid(int currentInavlid) {
		this.currentInavlid = currentInavlid;
	}
	
}
