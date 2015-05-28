package org.mig.gchat.utils;

import java.sql.SQLException;
import java.util.ArrayList;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import org.mig.gchat.chat.ChatControl;
import org.mig.gchat.chat.NameDisplayController;

//This class handles all the listners
public class ListenerClass implements Listener{
	private static ArrayList<Player> newPlayerList = new ArrayList<Player>();
	private NameDisplayController nc;
	
	//when a player joins gather the info of the player and create a new ThePlayer object
	@EventHandler
	public void onJoin(PlayerJoinEvent event ){
		GChat main = GChat.getMain();
		
		if(main.getConfig().getBoolean("MySql")){
			try {
				main.mysql.updatePlayer(event.getPlayer());
			} catch (ClassNotFoundException | SQLException e) {
				main.getLogger().info("Error on join: " + e);
				main.getConfig().set("MySql", false);
			}
		}
		ThePlayer tp = new ThePlayer(event.getPlayer());
		GChat.onlinePlayers.add(tp);
		final Player p = event.getPlayer();
		
		//set up formatting of name in tab list and above players head.
		nc = new NameDisplayController(tp);
		nc.setTabList();
		
		//added to list used for mine chat check
		newPlayerList.add(p);
		main.getServer().getScheduler().scheduleSyncDelayedTask(main, new Runnable() { public void run() { newPlayerList.remove(p); } }, 20 * 4); // 20 (one second in ticks) * 5 (seconds to wait)
		event.setJoinMessage(null);
	}
	
	//when a player chats handle the chat with this plugin and cancel the event with the server.
	@EventHandler
	public void onChat(AsyncPlayerChatEvent event){
		if(GChat.getThePlayer(event.getPlayer()) != null){
			ChatControl c = new ChatControl(GChat.getThePlayer(event.getPlayer()), event.getMessage(),MinechatCompatability.mineChatStatus(event.getPlayer().getUniqueId()));
			c.chat();
			event.setCancelled(true);
		}
		
		
	}
	
	//when a player leaves remove them from the onlinePlayers list and stop the quit message
	@EventHandler
	public void onLeave(PlayerQuitEvent event){
		MinechatCompatability.mineChatOff(event.getPlayer().getUniqueId());
		GChat.onlinePlayers.remove(GChat.getThePlayer(event.getPlayer()));
		event.setQuitMessage(null);
	}
	
	/*when a player places a command this method recieves it before it is processed and checks to see how
	 *recently a player joined and if within 5 seconds it will automatically turn off json chat and just
	 *send regular messages due to that being an indication of a player using minechat which can not handle
	 *json chat. This is able to be toggled off by the player if they wish.
	 */
	
	@EventHandler
	public void onCommand(PlayerCommandPreprocessEvent event){
		ChatControl c = new ChatControl();
		c.adminGroupMessage(event.getPlayer().getName() + ": " + event.getMessage(), true);
		for(int i = 0; i < newPlayerList.size(); i++){
			if(newPlayerList.get(i).getUniqueId().equals(event.getPlayer().getUniqueId())&& event.getMessage().equals("/spawn")){
				event.getPlayer().sendMessage(ChatColor.BLUE + "MineChat has been detected and Minechat Mode turned on");
				event.getPlayer().sendMessage(ChatColor.BLUE + "If this has been a mistake please type" + ChatColor.RED + " /gchat off");
				MinechatCompatability.mineChatOn(event.getPlayer().getUniqueId());
			}
		}
	}
}
