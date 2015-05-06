package org.mig.gchat.chat.compatability;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;

import org.mig.gchat.utils.thePlayer;

public class DefaultChat {
	
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
	
	public DefaultChat(thePlayer tp){
		
		name = tplayer.getName();
		group = tplayer.getGroup();
		mediaLink = tplayer.getMediaLink();
		nameColor = tplayer.getNameColor();
		boldA = tplayer.getNameBold();
		messageColor = tplayer.getTextColor();
		boldM = tplayer.getTextBold();
		groupColor = tplayer.getGroupColor();
		boldG = tplayer.getGroupBold();
	}
	
	
	public TextComponent[] buildMessage(){
		//send json message
		TextComponent [] fullM;
		fullM = new TextComponent[2];
		
		//Build message including media link if the player has it
		if(!mediaLink.equals("")){
			fullM[0] = new TextComponent( new ComponentBuilder(name + ": ").color(nameColor).bold(boldA)
					.event(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(group +"\n"+mediaLink)
					.color(groupColor).bold(boldG).create())).event(new ClickEvent(ClickEvent.Action
					.OPEN_URL, mediaLink)).create());
		}
		else{
			fullM[0] = new TextComponent( new ComponentBuilder(name + ": ").color(nameColor).bold(boldA)
					.event(new HoverEvent(HoverEvent.Action.SHOW_TEXT,new ComponentBuilder(group)
					.color(groupColor).bold(boldG).create())).create());
		}
		
		fullM[1] = new TextComponent( new ComponentBuilder(message).color(messageColor).bold(boldM).create());
		
		return fullM;
					
	}
	
}