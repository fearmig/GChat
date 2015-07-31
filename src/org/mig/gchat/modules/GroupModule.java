package org.mig.gchat.modules;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.md_5.bungee.api.ChatColor;

import org.bukkit.entity.Player;
import org.mig.gchat.GChat;
import org.mig.gchat.groups.compatability.GroupManagerHandler;
import org.mig.gchat.groups.compatability.PEXHandler;
import org.mig.gchat.objects.Group;

//This will be the class to interact with to get a players group.
public class GroupModule{
	
	private Map<String, Object> groups = new HashMap<>();
	
	//this method tests for a group manager of some sort and get the group from there but
	//if no group manager exists then returns 'default'
	public String getGroup(Player p, GChat main){
		if(main.getServer().getPluginManager().isPluginEnabled("GroupManager")){
			GroupManagerHandler gm = new GroupManagerHandler(main);
			return gm.getGroup(p);
		}
		else if(main.getServer().getPluginManager().isPluginEnabled("PermissionsEx")){
			PEXHandler pex = new PEXHandler();
			return pex.getGroup(p);
		}
		else{
			return "default";
		}
	}
	
	/*
	 * Get a list of all the groups.
	 */
	public List<String> getGroups(Player p, GChat main){
		if(main.getServer().getPluginManager().isPluginEnabled("GroupManager")){
			GroupManagerHandler gm = new GroupManagerHandler(main);
			return gm.getGroups(p);
		}
		else if(main.getServer().getPluginManager().isPluginEnabled("PermissionsEx")){
			PEXHandler pex = new PEXHandler();
			return pex.getAllGroups();
		}
		else{
			return null;
		}
	}
	
	public void compileGChatGroups(GChat main){
		
		groups = main.getConfig().getConfigurationSection("Groups").getValues(false);
		
		for(String g :  groups.keySet()){
			
			Group group = new Group();
			group.setGroupName(g);
			
			if(main.getConfig().contains("Groups."+g+".groupNameColor"))
				group.setGroupNameColor(getColor(main.getConfig().getString("Groups."+g+".groupNameColor")));
			else
				group.setTextColor(ChatColor.WHITE);
			
			if(main.getConfig().contains("Groups."+g+".groupBold"))
				group.setGroupBold(main.getConfig().getBoolean("Groups."+g+".groupBold"));
			else
				group.setGroupBold(false);
			
			if(main.getConfig().contains("Groups."+g+".nameColor"))
				group.setNameColor(getColor(main.getConfig().getString("Groups."+g+".nameColor")));
			else
				group.setNameColor(ChatColor.WHITE);
			
			if(main.getConfig().contains("Groups."+g+".nameBold"))
				group.setNameBold(main.getConfig().getBoolean("Groups."+g+".nameBold"));
			else
				group.setNameBold(false);
			
			if(main.getConfig().contains("Groups."+g+".textColor"))
				group.setTextColor(getColor(main.getConfig().getString("Groups."+g+".textColor")));
			else
				group.setTextColor(ChatColor.WHITE);
			
			if(main.getConfig().contains("Groups."+g+".textBold"))
				group.setTextBold(main.getConfig().getBoolean("Groups."+g+".textBold"));
			else
				group.setTextBold(false);
			
			groups.put(g, group);
		}
	}
	
	/*
	 * Get a Group from the Key
	 */
	public Group getGroup(String key){
		return (Group) groups.get(key);
	}
	

	// return color associated with config entry
	private ChatColor getColor(String color) {
		switch (color) {
		case "black":
			return ChatColor.BLACK;
		case "dark blue":
			return ChatColor.DARK_BLUE;
		case "dark green":
			return ChatColor.DARK_GREEN;
		case "teal":
			return ChatColor.AQUA;
		case "dark red":
			return ChatColor.DARK_RED;
		case "purple":
			return ChatColor.DARK_PURPLE;
		case "gold":
			return ChatColor.GOLD;
		case "gray":
			return ChatColor.GRAY;
		case "dark gray":
			return ChatColor.DARK_GRAY;
		case "blue":
			return ChatColor.BLUE;
		case "lime green":
			return ChatColor.GREEN;
		case "aqua":
			return ChatColor.DARK_AQUA;
		case "red":
			return ChatColor.RED;
		case "pink":
			return ChatColor.LIGHT_PURPLE;
		case "yellow":
			return ChatColor.YELLOW;
		case "white":
			return ChatColor.WHITE;
		case "bold":
			return ChatColor.BOLD;
		default:
			return ChatColor.WHITE;
		}
	}
}