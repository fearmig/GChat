package org.mig.gchat.groups;

import org.bukkit.entity.Player;
import org.mig.gchat.groups.compatability.GroupManagerHandler;
import org.mig.gchat.groups.compatability.PEXHandler;
import org.mig.gchat.utils.GChat;

//This will be the class to interact with to get a players group.
public class Groups{
	
	private GChat main = GChat.getMain();
	
	//this method tests for a group manager of some sort and get the group from there but
	//if no group manager exists then returns 'default'
	public String getGroup(Player p){
		if(main.getServer().getPluginManager().isPluginEnabled("GroupManager")){
			GroupManagerHandler gm = new GroupManagerHandler(main);
			return gm.getGroup(p);
		}
		else if(main.getServer().getPluginManager().isPluginEnabled("PermissionsEX")){
			PEXHandler pex = new PEXHandler();
			return pex.getGroup(p);
		}
		else{
			return "default";
		}
	}
}