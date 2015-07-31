package org.mig.gchat.utils.compatability;

import java.util.ArrayList;

import org.bukkit.entity.Player;

import me.wazup.survivalgames.PlayerData;
import me.wazup.survivalgames.main;

public class SurvivalGamesHandler {
	 
	 public ArrayList<Player> getSpectators(String name){
		 return ((PlayerData)main.plugin.playerData.get(name)).getArena().getSpectators();
	 }
	 
	 public int getKills(Player p){
		 return main.plugin.getKills(p);
	 }
	 
	 public int getDeaths(Player p){
		 return main.plugin.getDeaths(p);
	 }
	 
	 public int getWins(Player p){
		 return main.plugin.getWins(p);
	 }
	 
	 public int getCoins(Player p){
		 return main.plugin.getCoins(p);
	 }
	 
	 public double getKD(Player p){
		 if(main.plugin.getKills(p)!=0)
			 return main.plugin.getKills(p)/main.plugin.getDeaths(p);
		 else
			 return 0.00;
	 }
}
