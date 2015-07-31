package org.mig.gchat.objects;

import net.md_5.bungee.api.ChatColor;

public class Group {
	
	private String groupName;

	private ChatColor groupNameColor;
	private ChatColor nameColor;
	private ChatColor textColor;

	private boolean nameBold;
	private boolean textBold;
	private boolean groupBold;
	/**
	 * @return the groupName
	 */
	public String getGroupName() {
		return groupName;
	}
	/**
	 * @param groupName the groupName to set
	 */
	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}
	/**
	 * @return the groupNameColor
	 */
	public ChatColor getGroupNameColor() {
		return groupNameColor;
	}
	/**
	 * @param groupNameColor the groupNameColor to set
	 */
	public void setGroupNameColor(ChatColor groupNameColor) {
		this.groupNameColor = groupNameColor;
	}
	/**
	 * @return the nameColor
	 */
	public ChatColor getNameColor() {
		return nameColor;
	}
	/**
	 * @param nameColor the nameColor to set
	 */
	public void setNameColor(ChatColor nameColor) {
		this.nameColor = nameColor;
	}
	/**
	 * @return the textColor
	 */
	public ChatColor getTextColor() {
		return textColor;
	}
	/**
	 * @param textColor the textColor to set
	 */
	public void setTextColor(ChatColor textColor) {
		this.textColor = textColor;
	}
	/**
	 * @return the nameBold
	 */
	public boolean isNameBold() {
		return nameBold;
	}
	/**
	 * @param nameBold the nameBold to set
	 */
	public void setNameBold(boolean nameBold) {
		this.nameBold = nameBold;
	}
	/**
	 * @return the textBold
	 */
	public boolean isTextBold() {
		return textBold;
	}
	/**
	 * @param textBold the textBold to set
	 */
	public void setTextBold(boolean textBold) {
		this.textBold = textBold;
	}
	/**
	 * @return the groupBold
	 */
	public boolean isGroupBold() {
		return groupBold;
	}
	/**
	 * @param groupBold the groupBold to set
	 */
	public void setGroupBold(boolean groupBold) {
		this.groupBold = groupBold;
	}
}
