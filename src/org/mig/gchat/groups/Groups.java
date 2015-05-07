package org.mig.gchat.groups;


import java.util.Map;

import org.bukkit.configuration.MemorySection;
import org.bukkit.entity.Player;
import org.mig.gchat.utils.GChat;

public class Groups{
	
	private GChat main = GChat.getMain();
	
	private Map<String, Object> getGroups(){
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