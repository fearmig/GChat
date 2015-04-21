package org.mig.gchattowny;

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

import com.palmergames.bukkit.towny.exceptions.TownyException;


public class listenerClass implements Listener{
	private static ArrayList<Player> newPlayerList = new ArrayList<Player>();
	private final tPatch main;
	
	public listenerClass(tPatch g){
		this.main = g;
	}
	
	@EventHandler
	public void onJoin(PlayerJoinEvent event ){
		
		String tempName;
		if(main.getConfig().getBoolean("MySql")){
			try {
				main.mysql.updatePlayer(event.getPlayer());
			} catch (ClassNotFoundException | SQLException e) {
				main.getLogger().info("Error: " + e);
				main.getConfig().set("MySql", false);
			}
		}
		thePlayer tp = new thePlayer(event.getPlayer(),tPatch.plugin);
		tPatch.onlinePlayers.add(tp);
		
		//Set display for Player Tab List
		final Player p = event.getPlayer();
		tempName = p.getName();
		if(tempName.length()>=15)
			tempName = p.getName().substring(0,14);
		p.setPlayerListName(tp.getNameColor()+tempName);
		
		//added to list used for mine chat check
		newPlayerList.add(p);
		this.main.getServer().getScheduler().scheduleSyncDelayedTask(this.main, new Runnable() { public void run() { newPlayerList.remove(p); } }, 20 * 4); // 20 (one second in ticks) * 5 (seconds to wait)
		event.setJoinMessage(null);
	}
	
	@EventHandler
	public void onChat(AsyncPlayerChatEvent event) throws TownyException {
		thePatch.compileList();
		chatControl c = new chatControl(tPatch.getThePlayer(event.getPlayer()), event.getMessage(),minechatCompatability.mineChatStatus(event.getPlayer().getUniqueId()));
		c.chat();
				event.setCancelled(true);
	}
	
	@EventHandler
	public void onLeave(PlayerQuitEvent event){
		minechatCompatability.mineChatOff(event.getPlayer().getUniqueId());
		tPatch.onlinePlayers.remove(tPatch.getThePlayer(event.getPlayer()));
		event.setQuitMessage(null);
	}
	@EventHandler
	public void onCommand(PlayerCommandPreprocessEvent event){
		chatControl c = new chatControl();
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
