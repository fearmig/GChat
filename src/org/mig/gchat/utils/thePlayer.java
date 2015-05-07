package org.mig.gchat.utils;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.UUID;

import net.md_5.bungee.api.ChatColor;

import org.bukkit.entity.Player;
import org.mig.gchat.groups.Groups;

public class thePlayer {
	private Player player;
	private String name;
	private String group;
	private String mediaLink;
	private String previousMessage = "";
	private ChatColor nameColor;
	private ChatColor textColor = ChatColor.WHITE;
	private ChatColor groupColor;
	private boolean spyMode = false;
	private boolean nameBold;
	private boolean textBold;
	private boolean groupBold;
	private int chatMode = 0;
	private final GChat main;
	public List<UUID> onlinePlayers;
	
	public thePlayer(Player p){
		main = GChat.getMain();
		player = p;
		
		if(main.getConfig().getBoolean("MySql")){
			try {
				ResultSet rs = main.mysql.getPlayerAttr(player);
				name = rs.getString("Name");
				mediaLink = rs.getString("MediaLink");
				group = rs.getString("Group");
				setAttributes();
			} catch (ClassNotFoundException | SQLException e) {
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
	
	private void setAttributes(){
		nameColor = getColor((String)main.getConfig().get("Groups."+group+".nameColor"));
		groupColor = getColor((String)main.getConfig().get("Groups."+group+".groupNameColor"));
		nameBold = (boolean)main.getConfig().get("Groups."+group+".nameBold");
		textBold = (boolean)main.getConfig().get("Groups."+group+".textBold");
		groupBold = (boolean)main.getConfig().get("Groups."+group+".groupBold");
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
	
	public String getGroup(){
		return group;
	}
	public void setGroup(String g){
		group = g;
		savePlayersYML();
	}
	
	public Player getPlayer(){
		return player;
	}
	
	public String getName(){
		return name;
	}
	public void setName(String n){
		name = n;
	}
	
	public ChatColor getNameColor(){
		return nameColor;
	}
	public void setNameColor(ChatColor n){
		nameColor = n;
	}
	
	public ChatColor getTextColor(){
		return textColor;
	}
	public void setTextColor(ChatColor n){
		textColor = n;
	}
	
	public ChatColor getGroupColor(){
		return groupColor;
	}
	public void setGroupColor(ChatColor n){
		groupColor = n;
	}
	
	public boolean getNameBold(){
		return nameBold;
	}
	public void getNameBold(boolean n){
		nameBold = n;
	}
	
	public boolean getTextBold(){
		return textBold;
	}
	public void setTextBold(boolean n){
		textBold = n;
	}
	
	public boolean getGroupBold(){
		return groupBold;
	}
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
	
	//Using ints for chatmode, 0 = Global, 1 = Nation, 2 = Town, 3 = Admin
	public void setChatMode(int i){
		chatMode = i;
	}
	public int getChatMode(){
		return chatMode;
	}
	
	//Spymode - a feature to allow commands and private messages to be displayed in chat to admins
	public void setSpyMode(boolean b){
		spyMode = b;
	}
	public boolean getSpyMode(){
		return spyMode;
	}
	
	public String getMediaLink(){
		return mediaLink;
	}
	public void setMediaLink(String l){
		mediaLink = l;
		if(main.getConfig().getBoolean("MySql")){
			try {
				main.mysql.updateMediaLink(player.getUniqueId(), mediaLink);
			} catch (ClassNotFoundException | SQLException e) {
				main.getLogger().info("Error: " + e);
			}
		}
		else{
			main.pConfig.set(""+player.getUniqueId()+".MediaLink", l);
			savePlayersYML();
		}
	}
	
	public void updatePlayer(){
		//to be implemented
	}
	public void savePlayersYML(){
		try {
			  main.pConfig.save(GChat.players);
			} catch(IOException e) {
				main.getLogger().info("Error: " + e);
			}
	}
}
