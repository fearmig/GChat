package org.mig.gchat.utils;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.UUID;

import net.md_5.bungee.api.ChatColor;

import org.bukkit.entity.Player;
import org.mig.gchat.groups.Groups;

public class ThePlayer {
	private Player player;
	private String name;
	private String group;
	private String mediaLink;
	private String previousMessage = "";
	private ChatColor nameColor;
	private ChatColor textColor;
	private ChatColor groupColor;
	private boolean spyMode = false;
	private boolean nameBold;
	private boolean textBold;
	private boolean groupBold;
	private int chatMode = 0;
	private final GChat main;
	public List<UUID> onlinePlayers;
	
	//Constructor
	public ThePlayer(Player p){
		main = GChat.getMain();
		player = p;
		
		//if MySql is being used gather information from there
		if(main.getConfig().getBoolean("MySql")){
			try {
				ResultSet rs = main.mysql.getPlayerAttr(player);
				name = rs.getString("Name");
				mediaLink = rs.getString("MediaLink");
				group = rs.getString("Group");
				setAttributes();
			} catch (ClassNotFoundException | SQLException e) {
				
				//If connection to MySql database fails gather information from default configuration files.
				
				main.getLogger().info("Error: " + e);
				name = player.getName();
				
				if(main.pConfig.contains(""+player.getUniqueId())){
					mediaLink = main.pConfig.getString(""+player.getUniqueId()+".MediaLink");
					if(mediaLink == null){
						mediaLink = "";
					}
				}
				else{
					Groups g = new Groups();
					group = g.getGroup(player);
					mediaLink = "";
					savePlayersYML();
				}
				setAttributes();
			}	
		}
		//Gather information from default configuration files
		else{
			Groups g = new Groups();
			name = player.getName();
			if(main.pConfig.contains(""+player.getUniqueId())){
				group = g.getGroup(player);
				mediaLink = main.pConfig.getString(""+player.getUniqueId()+".MediaLink");
				if(mediaLink == null){
					mediaLink = "";
				}
			}
			else{
				group = g.getGroup(player);
				mediaLink = "";
				savePlayersYML();
			}
			setAttributes();
			
		}
	}
	
	//set a players chat attributes
	private void setAttributes(){
		nameColor = getColor((String)main.getConfig().get("Groups."+group+".nameColor"));
		groupColor = getColor((String)main.getConfig().get("Groups."+group+".groupNameColor"));
		textColor = getColor((String)main.getConfig().get("Groups."+group+".textColor"));
		nameBold = (boolean)main.getConfig().get("Groups."+group+".nameBold");
		textBold = (boolean)main.getConfig().get("Groups."+group+".textBold");
	}
	
	//return color associated with config entry
	private ChatColor getColor(String color){
		switch(color){
			case "black": return ChatColor.BLACK;
			case "dark blue": return ChatColor.DARK_BLUE;
			case "dark green": return ChatColor.DARK_GREEN;
			case "teal": return ChatColor.AQUA;
			case "dark red": return ChatColor.DARK_RED;
			case "purple": return ChatColor.DARK_PURPLE;
			case "gold": return ChatColor.GOLD;
			case "gray": return ChatColor.GRAY;
			case "dark gray": return ChatColor.DARK_GRAY;
			case "blue": return ChatColor.BLUE;
			case "lime green": return ChatColor.GREEN;
			case "aqua": return ChatColor.DARK_AQUA;
			case "red": return ChatColor.RED;
			case "pink": return ChatColor.LIGHT_PURPLE;
			case "yellow": return ChatColor.YELLOW;
			case "white": return ChatColor.WHITE;
			case "bold": return ChatColor.BOLD;
			default: return ChatColor.WHITE;
		}
	}
	
	//return a players group as a string
	public String getGroup(){
		return group;
	}
	//set a players group
	public void setGroup(String g){
		group = g;
		savePlayersYML();
	}
	
	//return the Bukkit Player object
	public Player getPlayer(){
		return player;
	}
	
	//return a players name as a string
	public String getName(){
		return name;
	}
	//set a players name *possible implentation of Nicknames
	public void setName(String n){
		name = n;
	}
	
	//return the color of the name in chat
	public ChatColor getNameColor(){
		return nameColor;
	}
	//set the color of the name in chat
	public void setNameColor(ChatColor n){
		nameColor = n;
	}
	
	//return the color of the text in chat
	public ChatColor getTextColor(){
		return textColor;
	}
	//set the color of the text in chat
	public void setTextColor(ChatColor n){
		textColor = n;
	}
	
	//return the color of the group in chat
	public ChatColor getGroupColor(){
		return groupColor;
	}
	//set the color of the group in chat
	public void setGroupColor(ChatColor n){
		groupColor = n;
	}
	
	//return if the name is bold
	public boolean getNameBold(){
		return nameBold;
	}
	//set if the name is bold
	public void setNameBold(boolean n){
		nameBold = n;
	}
	
	//return if the text is bold
	public boolean getTextBold(){
		return textBold;
	}
	//set if the text is bold
	public void setTextBold(boolean n){
		textBold = n;
	}
	
	//return if the group is bold
	public boolean getGroupBold(){
		return groupBold;
	}
	//set if the group is bold
	public void setGroupBold(boolean n){
		groupBold = n;
	}
	
	//set players previous message for anti-spam
	public void setPrevMess(String s){
		previousMessage = s;
	}
	
	//get players previous message for anti-spam
	public String getPrevMess(){
		return previousMessage;
	}
	
	//Using ints for chatmode, 0 = Global, 1 = Admin, 2 = Town, 3 = Nation
	public void setChatMode(int i){
		chatMode = i;
	}
	//returns a players chatmode
	public int getChatMode(){
		return chatMode;
	}
	
	//Spymode - a feature to allow commands and private messages to be displayed in chat to admins
	public void setSpyMode(boolean b){
		spyMode = b;
	}
	//returns if a player is in spymode
	public boolean getSpyMode(){
		return spyMode;
	}
	
	//returns the string of the medialink
	public String getMediaLink(){
		return mediaLink;
	}
	//set the medialink of a player
	public void setMediaLink(String l){
		mediaLink = l;
		//if using MySql write to the database
		if(main.getConfig().getBoolean("MySql")){
			try {
				main.mysql.updateMediaLink(player.getUniqueId(), mediaLink);
			} catch (ClassNotFoundException | SQLException e) {
				main.getLogger().info("Error: " + e);
			}
		}
		//write to the default Player config
		else{
			main.pConfig.set(""+player.getUniqueId()+".MediaLink", l);
			savePlayersYML();
		}
	}
	
	//update the players attributes
	public void updatePlayer(){
		//to be implemented
	}
	
	//save Player config file
	public void savePlayersYML(){
		try {
			  main.pConfig.save(GChat.players);
			} catch(IOException e) {
				main.getLogger().info("Error: " + e);
			}
	}
}
