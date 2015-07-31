package org.mig.gchat.chat;

import java.util.ArrayList;
import java.util.List;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.TextComponent;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.mig.gchat.GChat;
import org.mig.gchat.chat.compatability.DefaultChat;
import org.mig.gchat.chat.compatability.SurvivalGamesChat;
import org.mig.gchat.chat.compatability.TownyChat;
import org.mig.gchat.chat.filter.SpamBlocker;
import org.mig.gchat.chat.filter.BadWordHandler;
import org.mig.gchat.objects.ThePlayer;
import org.mig.gchat.utils.MinechatCompatability;
import org.mig.gchat.utils.compatability.EssenHandler;

//should control just about everything that comes to the chat.
public class ChatControl{
	
	private static boolean globalMuteActive = false;
	
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
	
	private GChat main;
	
	//spam blockers
	private BadWordHandler bw;
	private SpamBlocker sb;
	
	// Constructors
	public ChatControl(GChat main){
		this.main = main;
		bw  = new BadWordHandler(main);
	}
	
	public ChatControl(ThePlayer tp, String m, boolean mc, GChat main){
		this.main = main;
		tplayer = tp;
		message = m;
		player = tp.getPlayer();
		name = tplayer.getName();
		group = tplayer.getGroup();
		minechatToggle = mc;
		
		nameColor = main.getGroupModule().getGroup(tp.getGroup()).getNameColor();
		messageColor = main.getGroupModule().getGroup(tp.getGroup()).getTextColor();
		chatMode = tplayer.getChatMode();
		sb = new SpamBlocker(main);
		bw  = new BadWordHandler(main);
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
		
		//set previous message for anti-spam
		main.getThePlayerModule().getThePlayer(tplayer.getUuid()).setPreviousMessage(message);
		
		if(temp != null){
			//if the message contained a bad word stop the message from being put into chat and
			//send those with "gchat.admin" a messages stating they tried to curse
			player.spigot().sendMessage(new TextComponent( new ComponentBuilder("Please do not use that language in here")
					.color(ChatColor.RED).create()));
			main.getServer().broadcast(ChatColor.RED + player.getName() + " tried to curse by saying:", "gchat.mod");
			message = message.replace(temp,ChatColor.RED + temp + ChatColor.WHITE);
			main.getServer().broadcast(message, "gchat.mod");
			return;
		}
		//check if player sent the same message, anti-spam
		else if(spam && !player.hasPermission("gchat.spamexempt")){
			player.spigot().sendMessage(new TextComponent( new ComponentBuilder("Please do not spam the same message")
					.color(ChatColor.RED).create()));
		}
		//if the message passed the language chat send it to chat
		else{
			
			//check for too many caps in message unless player has "gchat.capexempt"
			if(!player.hasPermission("gchat.spamexempt")){
				message = sb.checkCaps(message);
			}
			
			//build the message
			TextComponent[] fullM;
			switch(chatMode){
				case 1: setupAdminMessage();
						break;
				case 2: setupTownMessage();
						break;
				case 3: setupNationMessage();
						break;
			}
			
			/*
			 * Check if towny is enabled and if so build recipeients depending on the chat mode.
			 */
			if (Bukkit.getServer().getPluginManager().isPluginEnabled("Towny")) {
				if(chatMode==0){
					messageColor = ChatColor.WHITE;
				}
				
				TownyChat tc = new TownyChat(tplayer, message, messageColor, main);
				fullM = tc.buildMessage();
				if (chatMode == 2)
					recipients = tc.townMembers();
				else if (chatMode == 3)
					recipients = tc.nationMembers();
				else
					
				
				for (String s : main.getThePlayerModule().getOnlineThePlayers().keySet()) {
					if (main.getThePlayerModule().getThePlayer(s)
							.getPlayer().hasPermission("gchat.adminChat")) {
						if (main.getThePlayerModule().getThePlayer(s).isSpyMode()) {
							if (!recipients.contains(main.getThePlayerModule()
									.getThePlayer(s).getPlayer())) {
								recipients.add(main.getThePlayerModule()
										.getThePlayer(s).getPlayer());
							}
						}
					}
				}
			}
			
			/*
			 * test for survival games and format if player dies
			 */
			else if (Bukkit.getServer().getPluginManager().isPluginEnabled("SurvivalGames")) {
				SurvivalGamesChat sc = new SurvivalGamesChat(tplayer, message, messageColor, main);
				fullM = sc.buildMessage();
				try{
					if(sc.getAllSpecs(tplayer.getName()).contains(tplayer.getPlayer())){
						chatMode = 1;
						recipients = sc.getAllSpecs(tplayer.getName());
					}
				} catch(NullPointerException e){
					
				}
			}
			else{
				DefaultChat dc = new DefaultChat(tplayer, message, messageColor, main);
				fullM = dc.buildMessage();
			}
			
			//send message according to gamemode
			if(chatMode == 0)
				sendGlobalMessage(fullM);
			else
				sendSpecialMessage(fullM);				
		}
		//write message to console for logging, maybe future toggle option in config
		main.getLogger().info(name + ": " + message);
		
	}
	
	/*
	 * Methods to give temp attributes in the case of a command such as /tc Some text here
	 * This way the user can send a single message in a chat channel instead of having to switch
	 * between modes.
	 */
	public void startSingleGlobalMessage(){
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
		for(String b: main.getThePlayerModule().getOnlineThePlayers().keySet()){
			boolean ignored = false;
			
			//test if the player ignores the sender or the sender ignores the player
			if(GChat.essen){
				EssenHandler e = new EssenHandler();
				ignored = e.ignored(player, main.getThePlayerModule().getThePlayer(b).getPlayer());
			}
			
			//send chat from sender to player if not ignored
			if(!ignored){
			
				//check for mine chat mode and if on send regular non JSON message
				if(MinechatCompatability.mineChatStatus(main.getThePlayerModule().getThePlayer(b)
						.getPlayer().getUniqueId())){
					main.getThePlayerModule().getThePlayer(b).getPlayer()
							.sendMessage(nameColor + name +": " + messageColor + message);
				}
				else{
					main.getThePlayerModule().getThePlayer(b).getPlayer().spigot().sendMessage(fullM);
				}
			}
		}			
	}
	
	//set up message attributes for chatmodes
	private void setupNationMessage(){
		messageColor = ChatColor.GOLD;
	}
	private void setupTownMessage(){
		messageColor = ChatColor.AQUA;
	}
	private void setupAdminMessage(){
		for(String p: main.getThePlayerModule().getOnlineThePlayers().keySet()){
			if(main.getThePlayerModule().getThePlayer(p).getPlayer().hasPermission("gchat.mod")){
				recipients.add(main.getThePlayerModule().getThePlayer(p).getPlayer());
			}
		}
		messageColor = ChatColor.GREEN;
	}
	
	/*
	 * Send the message to the determined recipients.
	 */
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
	public void adminSpyMessage(String s){
		for(String p: main.getThePlayerModule().getOnlineThePlayers().keySet()){
			if(main.getThePlayerModule().getThePlayer(p).getPlayer().hasPermission("gchat.adminChat")){
				if(main.getThePlayerModule().getThePlayer(p).isSpyMode()){
					main.getThePlayerModule().getThePlayer(p).getPlayer().sendMessage(ChatColor.YELLOW + s + "");
				}
			}
		}
	}

	/**
	 * @return the globalMuteActive
	 */
	public static boolean isGlobalMuteActive() {
		return globalMuteActive;
	}

	/**
	 * @param globalMuteActive the globalMuteActive to set
	 */
	public static void setGlobalMuteActive(boolean globalMuteActive) {
		ChatControl.globalMuteActive = globalMuteActive;
	}
}
