package org.mig.gchattowny;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.UUID;

import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.mig.gchattowny.commands.adminChatCommand;
import org.mig.gchattowny.commands.globalChatCommand;
import org.mig.gchattowny.commands.nationChatCommand;
import org.mig.gchattowny.commands.townChatCommand;


public class tPatch extends JavaPlugin{
	public static tPatch plugin;
	public static File players;
	public static boolean essen = false;
	
	public final listenerClass l = new listenerClass(this);
	public mySqlMan mysql = new mySqlMan(this);
	public badWordHandler bwh = new badWordHandler();
	
	static ArrayList<UUID> mChatList = new ArrayList<UUID>();
	static ArrayList<thePlayer> onlinePlayers = new ArrayList<thePlayer>();
	
	YamlConfiguration pConfig;
	
	public void onEnable() {
		plugin = this;
		getConfig().options().copyDefaults(true);
		saveConfig();
		
		//test for essentials
		if(getServer().getPluginManager().isPluginEnabled("Essentials")){
			getLogger().info("Essentials detected");
			essen = true;
		}
		
		//set up MySql table and PlayersYML
		players = new File("plugins/GChatTowny/players.yml");
		
		if(getConfig().getBoolean("MySql")){
			try {
				this.mysql.setupDB();
				this.mysql.retrieveBadWords();
			} catch (ClassNotFoundException | SQLException e) {
				this.getConfig().set("MySql", false);
				this.getLogger().info("Error: " + e);
				bwh.fillList();
			}
		}
		
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
		
		else{
			pConfig = YamlConfiguration.loadConfiguration(players);
			bwh.fillList();
		}
		plugin = this;
		getServer().getPluginManager().registerEvents(this.l, this);
		
		//register commands
		getCommand("gchat").setExecutor(new Commands());
		getCommand("tc").setExecutor(new townChatCommand());
		getCommand("nc").setExecutor(new nationChatCommand());
		getCommand("g").setExecutor(new globalChatCommand());
		getCommand("ac").setExecutor(new adminChatCommand());
		
		
		for(Player p: getServer().getOnlinePlayers()){
			thePlayer tp = new thePlayer(p, this);
			if(!onlinePlayers.contains(tp)){
				onlinePlayers.add(tp);
			}
		}
		
	}
	
	public void onDisable(){
		if(getConfig().getBoolean("MySql")){
			try {
				this.mysql.closeDB();
			} catch (SQLException e) {
				this.getLogger().info("Error: " + e);
			}
		}
	}
	
	public static thePlayer getThePlayer(Player p){
		if(onlinePlayers!=null){
			for(thePlayer tp: onlinePlayers){
				if(tp.getName().equalsIgnoreCase(p.getName()))
					return tp;
			}
		}
		return null;
	}
}

