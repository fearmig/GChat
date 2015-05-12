package org.mig.gchat.groups.compatability;

import java.util.Arrays;
import java.util.List;
import org.anjocaido.groupmanager.GroupManager;
import org.anjocaido.groupmanager.permissions.AnjoPermissionsHandler;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;
 
//Copied class made by the authors of group manager meant to be used as a hook into their plugin.
//When a server uses GroupManger the class Groups will reach to this class to gather the information
//about the players.
public class GroupManagerHandler{
	
	private GroupManager groupManager;
	private Plugin plugin;
 
	public GroupManagerHandler(final Plugin plugin)
	{
		this.plugin = plugin;
		enableGM();
	}
	
	public void enableGM(){
		
		final PluginManager pluginManager = plugin.getServer().getPluginManager();
		final Plugin GMplugin = pluginManager.getPlugin("GroupManager");
 
		if (GMplugin != null && GMplugin.isEnabled()){
			groupManager = (GroupManager)GMplugin;
		}
	}
 
	public String getGroup(final Player base){
		
		final AnjoPermissionsHandler handler = groupManager.getWorldsHolder().getWorldPermissions(base);
		if (handler == null)
		{
			return null;
		}
		return handler.getGroup(base.getName());
	}
 
	public List<String> getGroups(final Player base)
	{
		final AnjoPermissionsHandler handler = groupManager.getWorldsHolder().getWorldPermissions(base);
		if (handler == null)
		{
			return null;
		}
		return Arrays.asList(handler.getGroups(base.getName()));
	}
 
	public String getPrefix(final Player base)
	{
		final AnjoPermissionsHandler handler = groupManager.getWorldsHolder().getWorldPermissions(base);
		if (handler == null)
		{
			return null;
		}
		return handler.getUserPrefix(base.getName());
	}
 
	public String getSuffix(final Player base)
	{
		final AnjoPermissionsHandler handler = groupManager.getWorldsHolder().getWorldPermissions(base);
		if (handler == null)
		{
			return null;
		}
		return handler.getUserSuffix(base.getName());
	}
 
}
