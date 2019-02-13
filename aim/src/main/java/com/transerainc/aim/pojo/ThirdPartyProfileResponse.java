package com.transerainc.aim.pojo;

public class ThirdPartyProfileResponse {
	private String id;
	private String name;		
	private int blendingMode;
	private int chatChannels;
	private int emailChannels;
	private int teleChannels;	
	private String chatUrl;
	private String emailUrl;
	private boolean casEnabled;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getChatChannels() {
		return chatChannels;
	}
	public void setChatChannels(int chatChannels) {
		this.chatChannels = chatChannels;
	}
	public int getEmailChannels() {
		return emailChannels;
	}
	public void setEmailChannels(int emailChannels) {
		this.emailChannels = emailChannels;
	}
	public int getTeleChannels() {
		return teleChannels;
	}
	public void setTeleChannels(int teleChannels) {
		this.teleChannels = teleChannels;
	}
	public String getChatUrl() {
		return chatUrl;
	}
	public void setChatUrl(String chatUrl) {
		this.chatUrl = chatUrl;
	}
	public String getEmailUrl() {
		return emailUrl;
	}
	public void setEmailUrl(String emailUrl) {
		this.emailUrl = emailUrl;
	}
	public boolean isCasEnabled() {
		return casEnabled;
	}
	public void setCasEnabled(boolean casEnabled) {
		this.casEnabled = casEnabled;
	}
	@Override
	public String toString() {
		return "ThirdPartyProfileResponse [id=" + id + ", name=" + name
				+ ", blendingMode=" + blendingMode + ", chatChannels=" + chatChannels
				+ ", emailChannels=" + emailChannels + ", teleChannels="
				+ teleChannels + ", chatUrl=" + chatUrl + ", emailUrl="
				+ emailUrl + ", casEnabled=" + casEnabled + "]";
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
