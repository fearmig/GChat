package org.mig.gchat.groups.compatability;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.entity.Player;
import ru.tehkode.permissions.PermissionGroup;
import ru.tehkode.permissions.PermissionUser;
import ru.tehkode.permissions.bukkit.PermissionsEx;

//Class made just to gather info from PEX.
//It gathers only the group info via superperms to reduce dependency but built for future
//capacity to upgrade.
public class PEXHandler {
	
	public String getGroup(Player p){
		PermissionUser pexUser = PermissionsEx.getUser(p);
		PermissionGroup temp = null;
		if(pexUser.getParents()==null){
			return "default";
		}
		
		if(pexUser.getParents().size()==1){
			return pexUser.getParents().get(0).getName();
		}
		
		for(PermissionGroup g: pexUser.getParents()){
			if(temp == null){
				temp = g;
			}
			else{
				if(g.getRank() < temp.getRank()){
					temp = g;
				}
			}
		}
		return temp.getName();
	}
	
	public List<String> getAllGroups(){
		List<String> groupList = new ArrayList<>();
		for(PermissionGroup g: PermissionsEx.getPermissionManager().getGroupList()){
			groupList.add(g.getName());
		}
		return groupList;
	}
}
