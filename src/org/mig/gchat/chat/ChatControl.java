package org.mig.gchat.chat;

import java.util.ArrayList;
import java.util.List;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.TextComponent;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.mig.gchat.chat.compatability.DefaultChat;
import org.mig.gchat.chat.compatability.TownyChat;
import org.mig.gchat.chat.filter.SpamBlocker;
import org.mig.gchat.chat.filter.BadWordHandler;
import org.mig.gchat.utils.GChat;
import org.mig.gchat.utils.MinechatCompatability;
import org.mig.gchat.utils.ThePlayer;
import org.mig.gchat.utils.compatability.EssenHandler;

//should control just about everything that comes to the chat.
public class ChatControl{
	
	private String message;
	private String name;
	private String group;
	private Player player;
	private ThePlayer tplayer;
	private boolean minechatToggle;
	private int chatMode;
	private ChatColor messageColor;
	private ChatColor nameColor;
	private List<Player> recipients = new ArrayList<Player>();
	
	private GChat main = GChat.getMain();
	
	//spam blockers
	private BadWordHandler bw = new BadWordHandler();
	private SpamBlocker sb = new SpamBlocker();
	
	// Constructors
	public ChatControl(){
	}
	public ChatControl(ThePlayer tp, String m, boolean mc){
		tplayer = tp;
		message = m;
		player = tp.getPlayer();
		name = tplayer.getName();
		group = tplayer.getGroup();
		minechatToggle = mc;
		nameColor = tplayer.getNameColor();
		messageColor = tplayer.getTextColor();
		chatMode = tplayer.getChatMode();
	}
	
	//set message
	public void setMessage(String m){
		message = m;
	}
	
	//return current message
	public String getMessage(){
		return message;
	}
	
	//set the bukkit player of the chat sender
	public void setPlayer(Player p){
		player = p;
	}
	
	//return the bukkit player of the chat sender
	public Player getPlayer(){
		return player;
	}
	
	//set name of chat sender
	public void setName(String n){
		name = n;
	}
	
	//return name of chat sender
	public String getName(){
		return name;
	}
	
	//set group of chat sender
	public void setGroup(String g){
		group = g;
	}
	
	//return group of chat sender
	public String getGroup(){
		return group;
	}
	
	//set boolean to indicate if minechat mode is on or off for chat sender
	public void setMinechat(boolean x){
		minechatToggle = x;
	}
	
	//return it chat sender is in minechat
	public boolean getMinechat(){
		return minechatToggle;
	}
	
	//send message into chat
	public void chat(){
		
		//test message for bad words
		String temp = bw.testMessage(message);
		boolean spam = sb.checkSpam(tplayer, message);
		
		if(temp != null){
			//if the message contained a bad word stop the message from being put into chat and
			//send those with "gchat.admin" a messages stating they tried to curse
			player.spigot().sendMessage(new TextComponent( new ComponentBuilder("Please do not use that language in here").color(ChatColor.RED).create()));
			adminGroupMessage(ChatColor.RED + player.getName() + " tried to curse by saying:", false);
			message = message.replace(temp,ChatColor.RED + temp + ChatColor.WHITE);
			adminGroupMessage(message, false);
			return;
		}
		//check if player sent the same message, anti-spam
		else if(spam && !player.hasPermission("gchat.capexempt")){
			player.spigot().sendMessage(new TextComponent( new ComponentBuilder("Please do not spam the same message").color(ChatColor.RED).create()));
		}
		//if the message passed the language chat send it to chat
		else{
			
			Object obj;
			
			//check for too many caps in message unless player has "gchat.capexempt"
			if(!player.hasPermission("gchat.capexempt")){
				message = sb.checkCaps(message);
			}
			
			//build the message
			TextComponent[] fullM;
			if(Bukkit.getServer().getPluginManager().isPluginEnabled("Towny")){
				messageColor = ChatColor.WHITE;
				obj = new TownyChat(tplayer, message, messageColor);
				fullM = ((TownyChat) obj).buildMessage();
			}
			else{
				obj = new DefaultChat(tplayer, message, messageColor);
				fullM = ((DefaultChat) obj).buildMessage();
			}
			
			//send message according to gamemode
			switch(chatMode){
				case 0: sendGlobalMessage(fullM);
						break;
				case 1: setupAdminMessage(fullM);
						sendSpecialMessage(fullM);
						break;
				case 2: setupTownMessage(fullM, (TownyChat) obj);
						sendSpecialMessage(fullM);
						break;
				case 3: setupNationMessage(fullM, (TownyChat) obj);
						sendSpecialMessage(fullM);
						break;
				default: sendGlobalMessage(fullM);
			}
		}
		//write message to console for logging, maybe future toggle option in config
		main.getLogger().info(name + ": " + message);
		//set previous message for anti-spam
		tplayer.setPrevMess(message);
	}
	
	//Methods to give temp attributes in the case of a command such as /tc Some text here
	//This way the user can send a single message in a chat channel instead of having to switch
	//between modes.
	public void startSingleGlobalMessage(){
		messageColor = ChatColor.WHITE;
		chatMode = 0;
		chat();
	}
	public void startSingleNationMessage(){
		messageColor = ChatColor.GOLD;
		chatMode = 3;
		chat();
	}
	public void startSingleTownMessage(){
		messageColor = ChatColor.AQUA;
		chatMode = 2;
		chat();
	}
	public void startSingleAdminMessage(){
		messageColor = ChatColor.GREEN;
		chatMode = 1;
		chat();
	}
	
	//send a global message
	private void sendGlobalMessage(TextComponent[] fullM){
		for(ThePlayer b: GChat.onlinePlayers){
			boolean ignored = false;
			
			//test if the player ignores the sender or the sender ignores the player
			if(GChat.essen){
				EssenHandler e = new EssenHandler();
				ignored = e.ignored(player, b.getPlayer());
			}
			
			//send chat from sender to player if not ignored
			if(!ignored){
			
				//check for mine chat mode and if on send regular non JSON message
				if(MinechatCompatability.mineChatStatus(b.getPlayer().getUniqueId())){
					b.getPlayer().sendMessage(nameColor + name +": " + messageColor + message);
				}
				else{
					b.getPlayer().spigot().sendMessage(fullM);
				}
			}
		}			
	}
	
	private void setupNationMessage(TextComponent[] fullM, TownyChat tc){
		messageColor = ChatColor.GOLD;
		recipients = tc.nationMembers();
	}
	private void setupTownMessage(TextComponent[] fullM, TownyChat tc){
		messageColor = ChatColor.AQUA;
		recipients = tc.townMembers();	
	}
	private void setupAdminMessage(TextComponent[] fullM){
		for(ThePlayer p: GChat.onlinePlayers){
			if(p.getPlayer().hasPermission("gchat.adminChat")){
				recipients.add(p.getPlayer());
			}
		}
	}
	
	private void sendSpecialMessage(TextComponent[] fullM){
		for(Player b: recipients){
			boolean ignored = false;
			
			//test if the player ignores the sender or the sender ignores the player
			if(GChat.essen){
				EssenHandler e = new EssenHandler();
				ignored = e.ignored(player, b);
			}
			
			//send chat from sender to player if not ignored
			if(!ignored){
			
				//check for mine chat mode and if on send regular non JSON message
				if(MinechatCompatability.mineChatStatus(b.getPlayer().getUniqueId())){
					b.sendMessage(nameColor + name +": " + messageColor + message);
				}
				else{
					b.spigot().sendMessage(fullM);
				}
			}
		}			
	}
	
	//send message to group that has perm gchat.admin
	public void adminGroupMessage(String s, boolean spy){
		for(ThePlayer p: GChat.onlinePlayers){
			if(p.getPlayer().hasPermission("gchat.adminChat")){
				if(p.getSpyMode() && spy){
					p.getPlayer().sendMessage(ChatColor.YELLOW + s + "");
				}
				else if(!spy){
					p.getPlayer().sendMessage(s);
				}
			}
		}
	}
}
