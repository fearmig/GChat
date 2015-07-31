package org.mig.gchat.chat.compatability;

import java.util.List;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;

import org.bukkit.entity.Player;
import org.mig.gchat.GChat;
import org.mig.gchat.objects.ThePlayer;
import org.mig.gchat.utils.compatability.TownyHandler;

//The sole purpose of this class is to build the Towny message that is to be sent out.

public class TownyChat{
	private String message;
	private String name;
	private String group;
	private String mediaLink;
	private ThePlayer tplayer;
	private boolean boldA;
	private boolean boldM;
	private boolean boldG;
	private ChatColor messageColor;
	private ChatColor groupColor;
	private ChatColor nameColor;
	private TownyHandler th;
	
	//constructors
	public TownyChat(ThePlayer tp, String m, ChatColor mc, GChat main){
		tplayer = tp;
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
	
	//Method that returns a TextComponent array that contains the formated message
	public TextComponent[] buildMessage(){
		
		//initialization of the Towny Resident object that provides info from Towny about
		//the player that is sent.
		th = new TownyHandler(tplayer.getPlayer().getName());
		
		//send json message
		TextComponent [] fullM;
		fullM = new TextComponent[2];
		
		//Build message including media link if the player has it
		if(mediaLink!= null && !mediaLink.equals("")){
			fullM[0] = new TextComponent( new ComponentBuilder(th.getMayor() + th.getTitle() + name + th.getSur() + ": ")
				.color(nameColor).bold(boldA)
				.event(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(group + "")
				.color(groupColor).bold(boldG)
				.append("\n" + mediaLink + "\n").color(ChatColor.DARK_PURPLE)
				.append("Nation: ").color(ChatColor.DARK_GREEN).bold(false)
				.append("" + th.getNation() + "\n").color(ChatColor.WHITE)
				.append("Town: ").color(ChatColor.DARK_GREEN).bold(false)
				.append("" + th.getTown()).color(ChatColor.WHITE).create()))
				.event(new ClickEvent(ClickEvent.Action.OPEN_URL, mediaLink)).create());
		}
		
		//Build message excluding media link when player doesn't have one
		else{
			fullM[0] = new TextComponent( new ComponentBuilder(th.getMayor() + th.getTitle() + name + th.getSur() + ": ")
				.color(nameColor).bold(boldA)
				.event(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(group + "")
				.color(groupColor).bold(boldG).append("\n").append("Nation: ").color(ChatColor.DARK_GREEN).bold(false)
				.append("" + th.getNation() + "\n").color(ChatColor.WHITE).append("Town: ")
				.color(ChatColor.DARK_GREEN).bold(false).append("" + th.getTown()).color(ChatColor.WHITE)
				.create())).create());
		}
					
		fullM[1] = new TextComponent( new ComponentBuilder(message).color(messageColor).bold(boldM).create());
		
		return fullM;
					
	}
	
	//Returns a list of all the players that are in the the TownyObject's nation
	public List <Player> nationMembers(){
		return th.getNationPlayers();
	}
	
	//Returns a list of all the players that are in the the TownyObject's town
	public List <Player> townMembers(){
		return th.getTownPlayers();
	}
	
}
