/**
 * 
 */
package com.transerainc.aim.pojo;

import java.io.Serializable;

import org.apache.commons.lang.math.NumberUtils;

/**
 * @author Rajpal Dangi
 * 
 */
public class MediaProfile implements Serializable, Comparable<MediaProfile> {

	private static final long serialVersionUID = 1L;
	private String mediaProfileId;
	private String mediaProfileName;
	private String tenantId;
	private int blendingMode;
	private int numberOfTelephonyChannels;
	private int numberOfEmailChannels;
	private int numberOfFaxChannels;
	private int numberOfChatChannels;
	private int numberOfVideoChannels;
	private int numberOfOtherChannels;

	/**
	 * @return the mediaProfileId
	 */
	public String getMediaProfileId() {
		return mediaProfileId;
	}

	/**
	 * @param mediaProfileId
	 *            the mediaProfileId to set
	 */
	public void setMediaProfileId(String mediaProfileId) {
		this.mediaProfileId = mediaProfileId;
	}

	/**
	 * @return the mediaProfileName
	 */
	public String getMediaProfileName() {
		return mediaProfileName;
	}

	/**
	 * @param mediaProfileName
	 *            the mediaProfileName to set
	 */
	public void setMediaProfileName(String mediaProfileName) {
		this.mediaProfileName = mediaProfileName;
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

	/**
	 * @return the numberOfPhoneChannels
	 */
	public int getNumberOfTelephonyChannels() {
		return numberOfTelephonyChannels;
	}

	/**
	 * @param numberOfPhoneChannels
	 *            the numberOfPhoneChannels to set
	 */
	public void setNumberOfTelephonyChannels(int numberOfPhoneChannels) {
		this.numberOfTelephonyChannels = numberOfPhoneChannels;
	}

	/**
	 * @return the numberOfEmailChannels
	 */
	public int getNumberOfEmailChannels() {
		return numberOfEmailChannels;
	}

	/**
	 * @param numberOfEmailChannels
	 *            the numberOfEmailChannels to set
	 */
	public void setNumberOfEmailChannels(int numberOfEmailChannels) {
		this.numberOfEmailChannels = numberOfEmailChannels;
	}

	/**
	 * @return the numberOfFaxChannels
	 */
	public int getNumberOfFaxChannels() {
		return numberOfFaxChannels;
	}

	/**
	 * @param numberOfFaxChannels
	 *            the numberOfFaxChannels to set
	 */
	public void setNumberOfFaxChannels(int numberOfFaxChannels) {
		this.numberOfFaxChannels = numberOfFaxChannels;
	}

	/**
	 * @return the numberOfChatChannels
	 */
	public int getNumberOfChatChannels() {
		return numberOfChatChannels;
	}

	/**
	 * @param numberOfChatChannels
	 *            the numberOfChatChannels to set
	 */
	public void setNumberOfChatChannels(int numberOfChatChannels) {
		this.numberOfChatChannels = numberOfChatChannels;
	}

	/**
	 * @return the numberOfVideoChannels
	 */
	public int getNumberOfVideoChannels() {
		return numberOfVideoChannels;
	}

	/**
	 * @param numberOfVideoChannels
	 *            the numberOfVideoChannels to set
	 */
	public void setNumberOfVideoChannels(int numberOfVideoChannels) {
		this.numberOfVideoChannels = numberOfVideoChannels;
	}

	/**
	 * @return the numberOfOtherChannels
	 */
	public int getNumberOfOtherChannels() {
		return numberOfOtherChannels;
	}

	/**
	 * @param numberOfOtherChannels
	 *            the numberOfOtherChannels to set
	 */
	public void setNumberOfOtherChannels(int numberOfOtherChannels) {
		this.numberOfOtherChannels = numberOfOtherChannels;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "AgentMediaProfile mediaProfileId=" + mediaProfileId
			+ ", mediaProfileName=" + mediaProfileName
			+ ", blendingMode=" + blendingMode
			+ ", numberOfChatChannels=" + numberOfChatChannels
			+ ", numberOfEmailChannels=" + numberOfEmailChannels
			+ ", numberOfFaxChannels=" + numberOfFaxChannels
			+ ", numberOfOtherChannels=" + numberOfOtherChannels
			+ ", numberOfPhoneChannels=" + numberOfTelephonyChannels
			+ ", numberOfVideoChannels=" + numberOfVideoChannels
			+ ", tenantId=" + tenantId + "]";
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Comparable#compareTo(java.lang.Object)
	 */
	public int compareTo(MediaProfile arg0) {
		Integer id1 = NumberUtils.toInt(this.mediaProfileId, 0);
		Integer id2 = NumberUtils.toInt(arg0.mediaProfileId, 0);

		return id1.compareTo(id2);
	}

	/**
	 * @return the blendingMode
	 */
	public int getBlendingMode() {
		return blendingMode;
	}

	/**
	 * @param blendingMode the blendingMode to set
	 */
	public void setBlendingMode(int blendingMode) {
		this.blendingMode = blendingMode;
	}
}
