package org.mig.gchattowny;


import java.util.Map;

import org.bukkit.configuration.MemorySection;
import org.bukkit.entity.Player;

public class groupHandler {
	
	public Map<String, Object> getGroups(){
		return ((MemorySection) tPatch.plugin.getConfig().get("Groups")).getValues(false);
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