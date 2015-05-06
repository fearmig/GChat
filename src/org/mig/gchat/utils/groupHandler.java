package org.mig.gchat.utils;


import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.configuration.MemorySection;
import org.bukkit.entity.Player;

public class groupHandler {
	private GChat main = (GChat) Bukkit.getServer().getPluginManager().getPlugin("GChatTowny");
	public Map<String, Object> getGroups(){
		return ((MemorySection) main.getConfig().get("Groups")).getValues(false);
	}
	
	public String getGroup(Player p){
		Map<String, Object> groups = getGroups();
		for(String s: groups.keySet()){
			if(!s.equals("default") && p.hasPermission("group."+s)){
				return s;
			}
		}
		return "default";
	}
}