package org.mig.gchat.utils;

import java.util.ArrayList;
import java.util.Map;

import net.md_5.bungee.api.ChatColor;

import org.bukkit.Bukkit;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.ScoreboardManager;
import org.bukkit.scoreboard.Team;
import org.mig.gchat.GChat;
import org.mig.gchat.objects.ThePlayer;

public class ScoreboardBuilder {
	private GChat main;
	static private ScoreboardManager manager = Bukkit.getScoreboardManager();
	static private Scoreboard board = manager.getNewScoreboard();
	static private ArrayList<Team> groupList = new ArrayList<>();
	
	public ScoreboardBuilder(GChat main){
		this.main = main;
	}
	
	public void setGroupColors(){
		Map<String, Object> groupMap = main.getConfig().getConfigurationSection("Groups").getValues(false);
		Team temp;
		for (String s : groupMap.keySet()){
			temp = board.registerNewTeam(s);
			temp.setDisplayName(getColor(main.getConfig().getString("Groups."+s+".groupNameColor"))+s);
			temp.setPrefix(getColor(main.getConfig().getString("Groups."+s+".groupNameColor"))+"");
			groupList.add(temp);
		}
	}
	
	public void joinTeam(ThePlayer tp){
		for(Team t : groupList){
			if(t.getName().equalsIgnoreCase(tp.getGroup())){
				if(!t.hasPlayer(tp.getPlayer())){
					t.addPlayer(tp.getPlayer());
				}
				return;
			}
		}
		
	}
	
	public Scoreboard getBoard(){
		return board;
	}
	
	//return color associated with config entry
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
