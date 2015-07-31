package org.mig.gchat.objects;

import org.bukkit.entity.Player;

public class ThePlayer {
	private Player player;
	
	private String name;
	private String group;
	private String mediaLink;
	private String uuid;
	private String previousMessage = "";
	
	private boolean spyMode = true;
	private boolean reportMode = false;
	
	private int chatMode = 0;

	/**
	 * @return the player
	 */
	public Player getPlayer() {
		return player;
	}

	/**
	 * @param player the player to set
	 */
	public void setPlayer(Player player) {
		this.player = player;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the group
	 */
	public String getGroup() {
		return group;
	}

	/**
	 * @param group the group to set
	 */
	public void setGroup(String group) {
		this.group = group;
	}

	/**
	 * @return the mediaLink
	 */
	public String getMediaLink() {
		return mediaLink;
	}

	/**
	 * @param mediaLink the mediaLink to set
	 */
	public void setMediaLink(String mediaLink) {
		this.mediaLink = mediaLink;
	}

	/**
	 * @return the uuid
	 */
	public String getUuid() {
		return uuid;
	}

	/**
	 * @param uuid the uuid to set
	 */
	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

	/**
	 * @return the previousMessage
	 */
	public String getPreviousMessage() {
		return previousMessage;
	}

	/**
	 * @param previousMessage the previousMessage to set
	 */
	public void setPreviousMessage(String previousMessage) {
		this.previousMessage = previousMessage;
	}

	/**
	 * @return the spyMode
	 */
	public boolean isSpyMode() {
		return spyMode;
	}

	/**
	 * @param spyMode the spyMode to set
	 */
	public void setSpyMode(boolean spyMode) {
		this.spyMode = spyMode;
	}

	/**
	 * @return the chatMode
	 */
	public int getChatMode() {
		return chatMode;
	}

	/**
	 * @param chatMode the chatMode to set
	 */
	public void setChatMode(int chatMode) {
		this.chatMode = chatMode;
	}

	/**
	 * @return the reportMode
	 */
	public boolean inReportMode() {
		return reportMode;
	}

	/**
	 * @param reportMode the reportMode to set
	 */
	public void setReportMode(boolean reportMode) {
		this.reportMode = reportMode;
	}
	
}
