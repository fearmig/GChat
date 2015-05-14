package org.mig.gchat.utils;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.UUID;

import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.mig.gchat.chat.filter.BadWordHandler;
import org.mig.gchat.commands.Commands;
import org.mig.gchat.commands.AdminChatCommand;
import org.mig.gchat.commands.GlobalChatCommand;
import org.mig.gchat.commands.towny.NationChatCommand;
import org.mig.gchat.commands.towny.TownChatCommand;

/*This is GChat, a plugin built to improve chat in minecraft servers that run spigot as their base
 * build. It creates custome JSON messages that display information anywhere from youtube channels, which
 * also have a clickable link, to information that other plugins contain.
 * 
 * This class is the Main and extends JavaPlugin which is what taps us into the game
 */
public class GChat extends JavaPlugin{
	private static GChat main;
	
	//the boolean which shows if essentials is used by the server
	public static boolean essen = false;
	
	//class variables
	public final ListenerClass l = new ListenerClass();
	public MySqlMan mysql = new MySqlMan(this);
	public BadWordHandler bwh;
	
	//lists used to track online players and minechat users
	static ArrayList<UUID> mChatList = new ArrayList<UUID>();
	public static ArrayList<ThePlayer> onlinePlayers = new ArrayList<ThePlayer>();
	
	//these represent the players config file
	public static File players;
	public YamlConfiguration pConfig;
	
	public void onEnable() {
		//try to create a default config file but don't do anything if one already exists
		getConfig().options().copyDefaults(true);
		saveDefaultConfig();
		main = this;
		
		//test for essentials
		if(getServer().getPluginManager().isPluginEnabled("Essentials")){
			getLogger().info("Essentials detected");
			essen = true;
		}
		
		//set up MySql table and PlayersYML
		players = new File("plugins/GChat/players.yml");
		bwh = new BadWordHandler();
		//if MySql is being used initiate the class and gather information
		if(getConfig().getBoolean("MySql")){
			try {
				this.mysql.setupDB();
				this.mysql.retrieveBadWords();
			} catch (ClassNotFoundException | SQLException e) {
				//if unable to establish MySql connection print the error and reach out to default config
				//to gather bad words.
				this.getConfig().set("MySql", false);
				this.getLogger().info("Error: " + e);
				bwh.fillList();
			}
		}
		//if using config and not MySql create players file if it does exist and load configuration.
		//also fill badword list from default config.
		else if (!players.exists()) {
			try {
				bwh.fillList();
				players.createNewFile();
				pConfig = YamlConfiguration.loadConfiguration(players);
			} catch (IOException e) {
				bwh.fillList();
				this.getLogger().info("Error: " + e);
			}
		}
		//if players had tested to exist then load all defualt configurations.
		else{
			pConfig = YamlConfiguration.loadConfiguration(players);
			bwh.fillList();
		}
		//plugin = this;
		getServer().getPluginManager().registerEvents(this.l, this);
		
		//register commands
		getCommand("gchat").setExecutor(new Commands());
		getCommand("tc").setExecutor(new TownChatCommand());
		getCommand("nc").setExecutor(new NationChatCommand());
		getCommand("g").setExecutor(new GlobalChatCommand());
		getCommand("ac").setExecutor(new AdminChatCommand());
		
		//get all the online players and put them into the List
		for(Player p: getServer().getOnlinePlayers()){
			ThePlayer tp = new ThePlayer(p);
			if(!onlinePlayers.contains(tp)){
				onlinePlayers.add(tp);
			}
		}
		main = this;
	}
	
	//when plugin is disabled if using MySql close the connection
	public void onDisable(){
		if(getConfig().getBoolean("MySql")){
			try {
				this.mysql.closeDB();
			} catch (SQLException e) {
				this.getLogger().info("Error: " + e);
			}
		}
	}
	
	//return thePlayer object which is this plugins object to represent the player.
	public static ThePlayer getThePlayer(Player p){
		if(onlinePlayers!=null){
			for(ThePlayer tp: onlinePlayers){
				if(tp.getName().equalsIgnoreCase(p.getName()))
					return tp;
			}
		}
		return null;
	}
	
	//return the static instance of itself
	public static GChat getMain(){
		return main;
	}
}

