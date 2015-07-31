package org.mig.gchat.chat.compatability;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;

import org.mig.gchat.GChat;
import org.mig.gchat.objects.ThePlayer;

//The sole purpose of this class is to build the default message that is to be sent out.

public class DefaultChat {
	
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
	
	//constructor
	public DefaultChat(ThePlayer tp, String m, ChatColor mc, GChat main){
		
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
		
		//Build message including media link if the player has it
		if(mediaLink!= null && !mediaLink.equals("")){
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