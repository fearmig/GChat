package org.mig.gchat.chat.compatability;

import java.util.List;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;

import org.bukkit.entity.Player;
import org.mig.gchat.utils.thePlayer;
import org.mig.gchat.utils.compatability.TownyHandler;

public class TownyChat{
	private String message;
	private String name;
	private String group;
	private String mediaLink;
	private thePlayer tplayer;
	private boolean boldA;
	private boolean boldM;
	private boolean boldG;
	private ChatColor messageColor;
	private ChatColor groupColor;
	private ChatColor nameColor;
	private TownyHandler th;
	
	public TownyChat(thePlayer tp, String m){
		tplayer = tp;
		name = tp.getName();
		group = tp.getGroup();
		mediaLink = tp.getMediaLink();
		nameColor = tp.getNameColor();
		boldA = tp.getNameBold();
		messageColor = tp.getTextColor();
		boldM = tp.getTextBold();
		groupColor = tp.getGroupColor();
		boldG = tp.getGroupBold();
		message = m;
	}
	
	public TextComponent[] buildMessage(){
	
		th = new TownyHandler(tplayer.getName());
		
		//send json message
		TextComponent [] fullM;
		fullM = new TextComponent[2];
		
		//Build message including media link if the player has it
		if(!mediaLink.equals("")){
			fullM[0] = new TextComponent( new ComponentBuilder(th.getMayor() + th.getSur() + name + ": ")
				.color(nameColor).bold(boldA)
				.event(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(group + "")
				.color(groupColor).bold(boldG).append("\n" + mediaLink + "\n").color(ChatColor.DARK_PURPLE)
				.append("Nation: ").color(ChatColor.DARK_GREEN).bold(false).append("" + th.getNation() + "\n")
				.color(ChatColor.WHITE).append("Town: ").color(ChatColor.DARK_GREEN).bold(false)
				.append("" + th.getTown()).color(ChatColor.WHITE).create())).event(new ClickEvent(ClickEvent.Action.OPEN_URL, mediaLink)).create());
		}
		
		//Build message excluding media link when player doesn't have one
		else{
			fullM[0] = new TextComponent( new ComponentBuilder(th.getMayor() + th.getSur() + name + ": ")
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
	
	public List <Player> nationMembers(){
		return th.getNationPlayers();
	}
	
	public List <Player> townMembers(){
		return th.getTownPlayers();
	}
	
}
