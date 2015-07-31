package org.mig.gchat.modules;

import java.sql.SQLException;
import java.util.HashMap;

import net.md_5.bungee.api.ChatColor;

import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.mig.gchat.GChat;
import org.mig.gchat.async.AsyncCreatePlayer;
import org.mig.gchat.async.AsyncMediaLinkChecker;
import org.mig.gchat.async.AsyncNameChecker;
import org.mig.gchat.async.AsyncSetMediaLink;
import org.mig.gchat.objects.ThePlayer;

public class ThePlayerModule {
	private final GChat main;
	private YamlConfiguration pConfig;
	private YamlConfiguration nConfig;
	
	private static HashMap<String, ThePlayer> onlinePlayers = new HashMap<>();
	
	//Constructor
	public ThePlayerModule(GChat main){
		this.main = main;
		this.pConfig = main.getPlayerConfig();
		this.nConfig = main.getNamesConfig();
	}
	
	public void createPlayer(Player player)
			throws ClassNotFoundException, SQLException {

		ThePlayer tPlayer = new ThePlayer();

		tPlayer.setPlayer(player);

		tPlayer.setUuid(player.getUniqueId().toString());
	
		if (main.getConfig().getBoolean("MySql")){
			AsyncCreatePlayer cp = new AsyncCreatePlayer(tPlayer, main.mysql.getDatabase());
			cp.runTaskAsynchronously(main);
		}

		/* check if player name is on the forced rename list and if so give that
		 * name
		 */
		if (main.getConfig().getBoolean("MySql")) {
			tPlayer.setName(player.getName());
			AsyncNameChecker nm = new AsyncNameChecker(main, tPlayer);
			nm.runTaskAsynchronously(main);
		} else {
			if (nConfig.contains(tPlayer.getUuid()))
				tPlayer.setName(nConfig.getString(tPlayer.getUuid()));
			else
				tPlayer.setName(player.getName());
		}
		if(tPlayer.getName()==null){
			tPlayer.setName(player.getName());
		}
		// assign a group to the player
		GroupModule gm = new GroupModule();
		tPlayer.setGroup(gm.getGroup(player, main));
		
		tPlayer.getPlayer().setPlayerListName(main.getGroupModule().getGroup(tPlayer.getGroup())
				.getNameColor()+tPlayer.getName());

		if (main.getConfig().getBoolean("MySql")) {
			AsyncMediaLinkChecker mc = new AsyncMediaLinkChecker(tPlayer,
					main.mysql.getDatabase());
			mc.runTaskAsynchronously(main);
		} else {
			if (pConfig.contains(tPlayer.getUuid()+".MediaLink")) {
				tPlayer.setMediaLink(pConfig.getString(tPlayer.getUuid() + ".MediaLink"));
				if (tPlayer.getMediaLink() == null) {
					tPlayer.setMediaLink("");
				}
			} else {
				savePlayersYML();
			}
		}

		addToMap(tPlayer);
	}
	
	private void addToMap(ThePlayer tPlayer) {
		onlinePlayers.put(tPlayer.getUuid(), tPlayer);
	}
	
	/*
	 * Remove ThePlayer from onlineplayers
	 */
	public void removeFromMap(String uuid){
		onlinePlayers.remove(uuid);
	}
	
	/*
	 * Get ThePlayer object associated with a Bukkit Player
	 */
	public ThePlayer getThePlayer(String uuid){
		return onlinePlayers.get(uuid);
	}
	
	
	//set the medialink of a player
	public void setMediaLink(String mediaLink, String uuid) throws ClassNotFoundException, SQLException{
		ThePlayer tPlayer = getThePlayer(uuid);
		//if using MySql write to the database
		if(mediaLink.contains("http://") || mediaLink.contains("https://")){
			if(main.getConfig().getBoolean("MySql")){
				AsyncSetMediaLink setML = new AsyncSetMediaLink(main, tPlayer, main.mysql.getDatabase(), mediaLink);
				setML.runTaskAsynchronously(main);
			}
			//write to the default Player config
			else{
				pConfig.set(""+tPlayer.getPlayer().getUniqueId()+".MediaLink", mediaLink);
				savePlayersYML();
				tPlayer.getPlayer()
					.sendMessage(
						ChatColor.AQUA
							+ tPlayer.getName()+ "'s media link has been set to:");
				tPlayer.getPlayer().sendMessage(ChatColor.GOLD + mediaLink);
			}
			getThePlayer(uuid).setMediaLink(mediaLink);
			
		} else {
			tPlayer.getPlayer().sendMessage(ChatColor.RED + "Must be a valid url.");
		}
	}
	
	//update the players attributes
	public void updatePlayer(){
		//to be implemented
	}
	
	//save Player config file
	public void savePlayersYML(){
		main.savePlayers(pConfig);;
	}
	
	//get the map of ThePlayer's
	public HashMap<String, ThePlayer> getOnlineThePlayers(){
		return onlinePlayers;
	}
}
