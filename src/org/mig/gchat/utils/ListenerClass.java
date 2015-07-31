package org.mig.gchat.utils;

import java.sql.SQLException;
import java.util.ArrayList;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.mig.gchat.GChat;
import org.mig.gchat.chat.ChatControl;
import org.mig.gchat.objects.ThePlayer;
import org.mig.gchat.theplayer.PlayerFormatter;

//This class handles all the listners
public class ListenerClass implements Listener{
	private static ArrayList<Player> newPlayerList = new ArrayList<Player>();
	private PlayerFormatter pf;
	private GChat main;
	
	public ListenerClass(GChat main){
		this.main = main;
	}
	//when a player joins gather the info of the player and create a new ThePlayer object
	@EventHandler
	public void onJoin(PlayerJoinEvent event ) throws ClassNotFoundException, SQLException{
		
		this.main.getThePlayerModule().createPlayer(event.getPlayer());
		
		final Player p = event.getPlayer();
		
		//set up formatting of name in tab list and above players head.
		pf = new PlayerFormatter(this.main.getThePlayerModule().
				getThePlayer(event.getPlayer().getUniqueId().toString()), this.main);
		//pf.setTabList();
		pf.setOverHeadDisplay();
		
		//added to list used for mine chat check
		newPlayerList.add(p);
		this.main.getServer().getScheduler().scheduleSyncDelayedTask(this.main, new Runnable() { 
			public void run() {
				newPlayerList.remove(p); } }, 20 * 4); // 20 (one second in ticks) * 5 (seconds to wait)
		
		event.setJoinMessage(null);
	}
	
	//when a player chats handle the chat with this plugin and cancel the event with the server.
	@EventHandler(priority = EventPriority.HIGH)
	public void onChat(AsyncPlayerChatEvent event){
		if(ChatControl.isGlobalMuteActive()){
			if(!event.getPlayer().hasPermission("gchat.globalmute.exempt")){
				event.getPlayer().sendMessage(ChatColor.RED + "Global Mute is currently active.");
				event.setCancelled(true);
			}
		}
		if(!event.isCancelled()){
			if(this.main.getThePlayerModule()
					.getThePlayer(event.getPlayer().getUniqueId().toString()) != null){
				
				ChatControl c = new ChatControl(this.main.getThePlayerModule().getThePlayer(event.getPlayer()
						.getUniqueId().toString()), event.getMessage(),MinechatCompatability
						.mineChatStatus(event.getPlayer().getUniqueId()), main);
				
				c.chat();
				event.setCancelled(true);
			}
		}
	}
	
	//when a player leaves remove them from the onlinePlayers list and stop the quit message
	@EventHandler
	public void onLeave(PlayerQuitEvent event){
		MinechatCompatability.mineChatOff(event.getPlayer().getUniqueId());
		this.main.getThePlayerModule().removeFromMap(event.getPlayer().getUniqueId().toString());
		event.setQuitMessage(null);
	}
	
	/*when a player places a command this method recieves it before it is processed and checks to see how
	 *recently a player joined and if within 5 seconds it will automatically turn off json chat and just
	 *send regular messages due to that being an indication of a player using minechat which can not handle
	 *json chat. This is able to be toggled off by the player if they wish.
	 */
	
	@EventHandler
	public void onCommand(PlayerCommandPreprocessEvent event){
		ChatControl c = new ChatControl(main);
		c.adminSpyMessage(event.getPlayer().getName() + ": " + event.getMessage());
		for(int i = 0; i < newPlayerList.size(); i++){
			if(newPlayerList.get(i).getUniqueId().equals(event.getPlayer().getUniqueId())&& event.getMessage().equals("/spawn")){
				event.getPlayer().sendMessage(ChatColor.BLUE + "MineChat has been detected and Minechat Mode turned on");
				event.getPlayer().sendMessage(ChatColor.BLUE + "If this has been a mistake please type" + ChatColor.RED + " /gchat off");
				MinechatCompatability.mineChatOn(event.getPlayer().getUniqueId());
			}
		}
	}
	
	/*
	 * When a player dies change their name if it is changed by GChataw
	 */
	@EventHandler(priority = EventPriority.HIGHEST)
	public void onDeath(PlayerDeathEvent event){
		ThePlayer tp = main.getThePlayerModule().getThePlayer(event.getEntity().getUniqueId().toString());
		event.setDeathMessage(event.getDeathMessage().replaceAll(event.getEntity().getName(), tp.getName()));
	}
	
}
