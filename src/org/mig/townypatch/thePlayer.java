package org.mig.townypatch;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.UUID;

import net.md_5.bungee.api.ChatColor;

import org.bukkit.entity.Player;

public class thePlayer {
	private Player player;
	private String name;
	private String group;
	private String mediaLink;
	private ChatColor nameColor;
	private ChatColor textColor;
	private ChatColor groupColor;
	private boolean nameBold;
	private boolean textBold;
	private boolean groupBold;
	private final tPatch main;
	public List<UUID> onlinePlayers;
	
	public thePlayer(Player p, tPatch m){
		main = m;
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
					groupHandler gh = new groupHandler();
					group = gh.getGroup(player);
					mediaLink = "";
					savePlayersYML();
				}
				setAttributes();
			}	
		}
		else{
			groupHandler gh = new groupHandler();
			name = player.getName();
			if(main.pConfig.contains(""+player.getUniqueId())){
				group = gh.getGroup(player);
				mediaLink = main.pConfig.getString(""+player.getUniqueId()+".MediaLink");
				if(mediaLink == null){
					mediaLink = "";
				}
			}
			else{
				group = gh.getGroup(player);
				mediaLink = "";
				savePlayersYML();
			}
			setAttributes();
			
		}
	}
	public void setAttributes(){
		chatControl c = new chatControl();
		nameColor = c.getColor((String)tPatch.plugin.getConfig().get("Groups."+group+".nameColor"));
		groupColor = c.getColor((String)tPatch.plugin.getConfig().get("Groups."+group+".groupNameColor"));
		nameBold = (boolean)tPatch.plugin.getConfig().get("Groups."+group+".nameBold");
		textBold = (boolean)tPatch.plugin.getConfig().get("Groups."+group+".textBold");
		groupBold = (boolean) tPatch.plugin.getConfig().get("Groups."+group+".groupBold");
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
			  main.pConfig.save(tPatch.players);
			} catch(IOException e) {
				main.getLogger().info("Error: " + e);
			}
	}
}
