/**
 * 
 */
package com.transerainc.aim.pojo;


/**
 * @author Rajpal Dangi
 *
 */
public class ActiveChannelInfo {
	private String channelType;
	private String channelStatus;
	private boolean matchedCriteria;

	/**
	 * @return the channelType
	 */
	public String getChannelType() {
		return channelType;
	}
	/**
	 * @param channelType the channelType to set
	 */
	public void setChannelType(String chType) {
		this.channelType = chType;
	}
	/**
	 * @return the channelStatus
	 */
	public String getChannelStatus() {
		return channelStatus;
	}
	/**
	 * @param channelStatus the channelStatus to set
	 */
	public void setChannelStatus(String chStatus) {
		this.channelStatus = chStatus;
	}
	/**
	 * @param matchedCriteria the matchedCriteria to set
	 */
	public void setMatchedCriteria(boolean matchriteria) {
		this.matchedCriteria = matchriteria;
	}
	/**
	 * @return the matchedCriteria
	 */
	public boolean getMatchedCriteria() {
		return matchedCriteria;
	}
	
}
