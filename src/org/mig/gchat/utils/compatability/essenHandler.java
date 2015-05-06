package org.mig.gchat.utils.compatability;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import com.earth2me.essentials.Essentials;

public class essenHandler {
	Essentials ess = (Essentials) Bukkit.getServer().getPluginManager().getPlugin("Essentials");
	private List <String> ignoredPlayers;
	
	public boolean ignored(Player p, Player b){
		ignoredPlayers = ess.getUser(p)._getIgnoredPlayers();
		for(int i = 0; i < ignoredPlayers.size(); i++){
			if(ignoredPlayers.get(i).equalsIgnoreCase(b.getName())){
				return true;
			}
		}
		ignoredPlayers = ess.getUser(b)._getIgnoredPlayers();
		for(int i = 0; i < ignoredPlayers.size(); i++){
			if(ignoredPlayers.get(i).equalsIgnoreCase(p.getName())){
				return true;
			}
		}
		return false;
	}
}
