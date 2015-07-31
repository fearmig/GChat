package org.mig.gchat;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.UUID;

import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.mig.gchat.async.AsyncRetrieveBadWords;
import org.mig.gchat.chat.filter.BadWordHandler;
import org.mig.gchat.commands.ClearChat;
import org.mig.gchat.commands.Commands;
import org.mig.gchat.commands.AdminChatCommand;
import org.mig.gchat.commands.GlobalChatCommand;
import org.mig.gchat.commands.GlobalMute;
import org.mig.gchat.commands.SendLink;
import org.mig.gchat.commands.towny.NationChatCommand;
import org.mig.gchat.commands.towny.TownChatCommand;
import org.mig.gchat.modules.GroupModule;
import org.mig.gchat.modules.ThePlayerModule;
import org.mig.gchat.utils.ListenerClass;
import org.mig.gchat.utils.MySqlMan;
import org.mig.gchat.utils.ScoreboardBuilder;

/*This is GChat, a plugin built to improve chat in minecraft servers that run spigot as their base
 * build. It creates custome JSON messages that display information anywhere from youtube channels, which
 * also have a clickable link, to information that other plugins contain.
 * 
 * This class is the Main and extends JavaPlugin which is what taps us into the game
 */
public class GChat extends JavaPlugin{
	//private static GChat main;
	
	//the boolean which shows if essentials is used by the server
	public static boolean essen = false;
	
	//class variables
	public MySqlMan mysql = new MySqlMan(this);
	private static ThePlayerModule thePlayerModule;
	private static GroupModule groupModule;
	private BadWordHandler bwh;
	private ScoreboardBuilder sb;
	
	//lists used to track online players and minechat users
	static ArrayList<UUID> mChatList = new ArrayList<UUID>();
	
	//these represent the players and names config file
	private static File players;
	private static File names;
	private YamlConfiguration pConfig;
	private YamlConfiguration nConfig;
	
	public void onEnable() {
		//try to create a default config file but don't do anything if one already exists
		getConfig().options().copyDefaults(true);
		saveDefaultConfig();
		
		//try to create a default config for names and players
		players = new File(getDataFolder(), "players.yml");
		names = new File(getDataFolder(), "names.yml");
		getConfigs();
		pConfig = YamlConfiguration.loadConfiguration(players);
		nConfig = YamlConfiguration.loadConfiguration(names);
		
		//test for essentials
		if(getServer().getPluginManager().isPluginEnabled("Essentials")){
			getLogger().info("Essentials detected");
			essen = true;
		}
		
		//Compile the Group objects list
		groupModule = new GroupModule();
		groupModule.compileGChatGroups(this);
		
		bwh = new BadWordHandler(this);
		//if MySql is being used initiate the class and gather information
		if(getConfig().getBoolean("MySql")){
			try {
				this.mysql.setupDB();
				AsyncRetrieveBadWords abw = new AsyncRetrieveBadWords(this);
				abw.runTaskAsynchronously(this);
			} catch (ClassNotFoundException | SQLException e) {
				//if unable to establish MySql connection print the error and reach out to default config
				//to gather bad words.
				this.getConfig().set("MySql", false);
				this.getLogger().info("Error: " + e);
				bwh.fillList();
			}
		}
		//if players had tested to exist then load all defualt configurations.
		else{
			bwh.fillList();
		}
		//main = this;
		
		/*
		 * Set up the scoreboard
		 */
		sb = new ScoreboardBuilder(this);
		sb.setGroupColors();
		
		//get all the online players and put them into the map held in ThePlayerModule
		thePlayerModule = new ThePlayerModule(this);
		for(Player p: getServer().getOnlinePlayers()){
			try {
				thePlayerModule.createPlayer(p);
			} catch (ClassNotFoundException | SQLException e) {
			// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
		
		//register commands
		getCommand("gchat").setExecutor(new Commands(this));
		getCommand("tc").setExecutor(new TownChatCommand(this));
		getCommand("nc").setExecutor(new NationChatCommand(this));
		getCommand("g").setExecutor(new GlobalChatCommand(this));
		getCommand("ac").setExecutor(new AdminChatCommand(this));
		getCommand("clearchat").setExecutor(new ClearChat(this));
		getCommand("link").setExecutor(new SendLink(this));
		getCommand("globalmute").setExecutor(new GlobalMute(this));
		
		getServer().getPluginManager().registerEvents(new ListenerClass(this), this);
		//main = this;
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
	
	// get the default values for the confiuration files names and players if they don't exist already
	private void getConfigs(){
		if(!names.exists()){
			names.getParentFile().mkdirs();
			copy(getResource("names.yml"), names);
		}
		if(!players.exists()){
			players.getParentFile().mkdirs();
			copy(getResource("players.yml"), players);
		}
	}

	// copy default values into newly created config files
	private void copy(InputStream i, File f){
		 try {
		        OutputStream out = new FileOutputStream(f);
		        byte[] buf = new byte[1024];
		        int l;
		        while((l=i.read(buf))>0){
		            out.write(buf,0,l);
		        }
		        out.close();
		        i.close();
		    } catch (Exception e) {
		        e.printStackTrace();
		    }
	}
	
	//return the static instance of itself
	//public static GChat getMain(){
		//return main;
	//}
	
	//return players config file
	public YamlConfiguration getPlayerConfig(){
		return pConfig;
	}
	
	//return players config file
	public YamlConfiguration getNamesConfig(){
		return nConfig;
	}
	
	//save the names.yml file
	public void saveNames(YamlConfiguration n){
		try {
			n.save(names);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	//save the names.yml file
	public void savePlayers(YamlConfiguration p){
		try {
			p.save(players);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/*
	 * return ThePlayerModule
	 */
	public ThePlayerModule getThePlayerModule(){
		return thePlayerModule;
	}
	
	/*
	 * return GroupModule
	 */
	public GroupModule getGroupModule(){
		return groupModule;
	}
}

