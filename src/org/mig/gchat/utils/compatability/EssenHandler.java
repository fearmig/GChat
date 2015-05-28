package org.mig.gchat.utils.compatability;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import com.earth2me.essentials.Essentials;

//Class that handles essentials when it is installed on the server. Mainly used just to see if a player
//ignores another.
public class EssenHandler {
	Essentials ess = (Essentials) Bukkit.getServer().getPluginManager().getPlugin("Essentials");
	private List <String> ignoredPlayers;
	
	//test if either of the players ignore each other and return if true or not.
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
	
	// return if the player is vanished or not
	public boolean isVanished(Player p){
		if(ess.getUser(p).isVanished())
			return true;
		else
			return false;
	}
	
}
