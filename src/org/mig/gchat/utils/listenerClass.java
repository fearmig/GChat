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


public class listenerClass implements Listener{
	private static ArrayList<Player> newPlayerList = new ArrayList<Player>();
	
	@EventHandler
	public void onJoin(PlayerJoinEvent event ){
		GChat main = GChat.getMain();
		
		String tempName;
		if(main.getConfig().getBoolean("MySql")){
			try {
				main.mysql.updatePlayer(event.getPlayer());
			} catch (ClassNotFoundException | SQLException e) {
				main.getLogger().info("Error: " + e);
				main.getConfig().set("MySql", false);
			}
		}
		thePlayer tp = new thePlayer(event.getPlayer());
		GChat.onlinePlayers.add(tp);
		
		//Set display for Player Tab List
		final Player p = event.getPlayer();
		tempName = p.getName();
		if(tempName.length()>=15)
			tempName = p.getName().substring(0,14);
		p.setPlayerListName(tp.getNameColor()+tempName);
		
		//added to list used for mine chat check
		newPlayerList.add(p);
		main.getServer().getScheduler().scheduleSyncDelayedTask(main, new Runnable() { public void run() { newPlayerList.remove(p); } }, 20 * 4); // 20 (one second in ticks) * 5 (seconds to wait)
		event.setJoinMessage(null);
	}
	
	@EventHandler
	public void onChat(AsyncPlayerChatEvent event){
		if(GChat.getThePlayer(event.getPlayer()) != null){
			ChatControl c = new ChatControl(GChat.getThePlayer(event.getPlayer()), event.getMessage(),minechatCompatability.mineChatStatus(event.getPlayer().getUniqueId()));
			c.chat();
			event.setCancelled(true);
		}
		
		
	}
	
	@EventHandler
	public void onLeave(PlayerQuitEvent event){
		minechatCompatability.mineChatOff(event.getPlayer().getUniqueId());
		GChat.onlinePlayers.remove(GChat.getThePlayer(event.getPlayer()));
		event.setQuitMessage(null);
	}
	@EventHandler
	public void onCommand(PlayerCommandPreprocessEvent event){
		ChatControl c = new ChatControl();
		c.adminGroupMessage(event.getPlayer().getName() + ": " + event.getMessage());
		for(int i = 0; i < newPlayerList.size(); i++){
			if(newPlayerList.get(i).getUniqueId().equals(event.getPlayer().getUniqueId())&& event.getMessage().equals("/spawn")){
				event.getPlayer().sendMessage(ChatColor.BLUE + "MineChat has been detected and Minechat Mode turned on");
				event.getPlayer().sendMessage(ChatColor.BLUE + "If this has been a mistake please type" + ChatColor.RED + " /gchat off");
				minechatCompatability.mineChatOn(event.getPlayer().getUniqueId());
			}
		}
	}
}
