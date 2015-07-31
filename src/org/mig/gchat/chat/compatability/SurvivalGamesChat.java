package org.mig.gchat.chat.compatability;

import java.util.ArrayList;

import me.wazup.survivalgames.PlayerData;
import me.wazup.survivalgames.main;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;

import org.bukkit.entity.Player;
import org.mig.gchat.GChat;
import org.mig.gchat.objects.ThePlayer;
import org.mig.gchat.utils.compatability.SurvivalGamesHandler;

public class SurvivalGamesChat {

	private String message;
	private String name;
	private String group;
	private String mediaLink;
	private boolean boldA;
	private boolean boldM;
	private boolean boldG;
	private ChatColor messageColor;
	private ChatColor groupColor;
	private ChatColor nameColor;
	private Player player;
	private SurvivalGamesHandler sgh;
	
	//constructor
	public SurvivalGamesChat(ThePlayer tp, String m, ChatColor mc, GChat main){
		
		player = tp.getPlayer();
		name = tp.getName();
		group = tp.getGroup();
		mediaLink = tp.getMediaLink();
		nameColor = main.getGroupModule().getGroup(tp.getGroup()).getNameColor();
		boldA = main.getGroupModule().getGroup(tp.getGroup()).isNameBold();
		messageColor = mc;
		boldM = main.getGroupModule().getGroup(tp.getGroup()).isTextBold();
		groupColor = main.getGroupModule().getGroup(tp.getGroup()).getGroupNameColor();
		boldG = main.getGroupModule().getGroup(tp.getGroup()).isGroupBold();
		message = m;
	}
	
	//Method that returns a textComponent array that contains the formatted message
	public TextComponent[] buildMessage(){
		//send json message
		TextComponent [] fullM;
		fullM = new TextComponent[2];
		try{
			sgh = new SurvivalGamesHandler();
			if(sgh.getSpectators(this.name).contains(player)){
				this.name = "[SPEC]" + name;
			}
		}catch(NullPointerException e){
			
		}
		
		//Build message including media link if the player has it
		if(mediaLink!= null && !mediaLink.equals("")){
			fullM[0] = new TextComponent( new ComponentBuilder(name + ": ")
				.color(nameColor).bold(boldA)
				.event(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(group + "")
				.color(groupColor).bold(boldG)
				.append("\n" + mediaLink + "\n").color(ChatColor.DARK_PURPLE)
				.append("Wins:").color(ChatColor.DARK_GREEN).bold(false)
				.append(" " + sgh.getWins(player) + "\n").color(ChatColor.WHITE)
				.append("Coins:").color(ChatColor.DARK_GREEN).bold(false)
				.append(" " + sgh.getCoins(player) + "\n").color(ChatColor.WHITE)
				.append("KD:").color(ChatColor.DARK_GREEN).bold(false)
				.append(" " + sgh.getKD(player) + "\n").color(ChatColor.WHITE)
				.append("Kills:").color(ChatColor.DARK_GREEN).bold(false)
				.append(" " + sgh.getKills(player) + "\n").color(ChatColor.WHITE)
				.append("Deaths:").color(ChatColor.DARK_GREEN).bold(false)
				.append(" " + sgh.getDeaths(player) + "\n").color(ChatColor.WHITE)
				.create())).event(new ClickEvent(ClickEvent.Action.OPEN_URL, mediaLink)).create());
		}
		else{
			fullM[0] = new TextComponent( new ComponentBuilder(name + ": ").color(nameColor).bold(boldA)
				.event(new HoverEvent(HoverEvent.Action.SHOW_TEXT,new ComponentBuilder(group+ "\n")
				.color(groupColor).bold(boldG)
				.append("Wins:").color(ChatColor.DARK_GREEN).bold(false)
				.append(" " + sgh.getWins(player) + "\n").color(ChatColor.WHITE)
				.append("Coins:").color(ChatColor.DARK_GREEN).bold(false)
				.append(" " + sgh.getCoins(player) + "\n").color(ChatColor.WHITE)
				.append("KD:").color(ChatColor.DARK_GREEN).bold(false)
				.append(" " + sgh.getKD(player) + "\n").color(ChatColor.WHITE)
				.append("Kills:").color(ChatColor.DARK_GREEN).bold(false)
				.append(" " + sgh.getKills(player) + "\n").color(ChatColor.WHITE)
				.append("Deaths:").color(ChatColor.DARK_GREEN).bold(false)
				.append(" " + sgh.getDeaths(player) + "\n").color(ChatColor.WHITE)
				.create())).create());
		}
		
		fullM[1] = new TextComponent( new ComponentBuilder(message).color(messageColor).bold(boldM).create());
		
		return fullM;
					
	}
	
	public ArrayList<Player> getAllSpecs(String name){
		 return ((PlayerData)main.plugin.playerData.get(name)).getArena().getSpectators();
	 }
}
