package org.mig.gchattowny;

import java.util.List;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;

import org.bukkit.entity.Player;

import com.palmergames.bukkit.towny.exceptions.TownyException;
import com.palmergames.bukkit.towny.object.Resident;
//should control just about everything that comes to the chat.
public class chatControl{
	
	public String message;
	public String name;
	public String group;
	public String mediaLink;
	public Player player;
	public thePlayer tplayer;
	public boolean minechatToggle;
	public boolean boldA;
	public boolean boldM;
	public boolean boldG;
	public int chatMode;
	public ChatColor messageColor;
	public ChatColor groupColor;
	public ChatColor nameColor;
	
	badWordHandler tw = new badWordHandler();
	
	public chatControl(){
		
	}
	
	public chatControl(thePlayer tp, String m, boolean mc){
		tplayer = tp;
		message = m;
		player = tp.getPlayer();
		name = tplayer.getName();
		group = tplayer.getGroup();
		mediaLink = tplayer.getMediaLink();
		minechatToggle = mc;
		nameColor = tplayer.getNameColor();
		boldA = tplayer.getNameBold();
		messageColor = tplayer.getTextColor();
		boldM = tplayer.getTextBold();
		groupColor = tplayer.getGroupColor();
		boldG = tplayer.getGroupBold();
		chatMode = tplayer.getChatMode();
	}
	
	public void setMessage(String m){
		message = m;
	}
	public String getMessage(){
		return message;
	}
	
	public void setPlayer(Player p){
		player = p;
	}
	public Player getPlayer(){
		return player;
	}
	
	public void setName(String n){
		name = n;
	}
	public String getName(){
		return name;
	}
	
	public void setGroup(String g){
		group = g;
	}
	public String getGroup(){
		return group;
	}
	
	public void setMinechat(boolean x){
		minechatToggle = x;
	}
	public boolean getMinechat(){
		return minechatToggle;
	}
	
	public ChatColor getColor(String color){
		switch(color){
			case "black": return ChatColor.BLACK;
			case "dark blue": return ChatColor.DARK_BLUE;
			case "dark green": return ChatColor.DARK_GREEN;
			case "teal": return ChatColor.AQUA;
			case "dark red": return ChatColor.DARK_RED;
			case "purple": return ChatColor.DARK_PURPLE;
			case "gold": return ChatColor.GOLD;
			case "gray": return ChatColor.GRAY;
			case "dark gray": return ChatColor.DARK_GRAY;
			case "blue": return ChatColor.BLUE;
			case "lime green": return ChatColor.GREEN;
			case "aqua": return ChatColor.DARK_AQUA;
			case "red": return ChatColor.RED;
			case "pink": return ChatColor.LIGHT_PURPLE;
			case "yellow": return ChatColor.YELLOW;
			case "white": return ChatColor.WHITE;
			case "bold": return ChatColor.BOLD;
			default: return ChatColor.WHITE;
		}
	}
	
	public void chat() throws TownyException {
		//test message for bad words
		String temp = tw.testMessage(message);
		if(temp != null){
			player.spigot().sendMessage(new TextComponent( new ComponentBuilder("Please do not use that language in here").color(ChatColor.RED).create()));
			adminGroupMessage(ChatColor.RED + player.getName() + " tried to curse by saying:");
			adminGroupMessage(message);
			return;
		}
		else{
			if(!player.hasPermission("gchat.capexempt")){
				checkCaps();
			}
			//test for rank and send message
			for(thePlayer b: tPatch.onlinePlayers){
				boolean ignored = false;
				
				//test for ignored player
				if(tPatch.essen){
					essenHandler e = new essenHandler();
					List <String> ignoredPlayersList = e.getIgnored(b);
					
					for(int i = 0; i < ignoredPlayersList.size(); i++){
						if(ignoredPlayersList.get(i).equalsIgnoreCase(name)){
							ignored=true;
						}
					}
				}
				
				if(!ignored){
					
					thePatch tp = new thePatch();
					Resident r = tp.getResident(name);
					
					if(minechatCompatability.mineChatStatus(b.getPlayer().getUniqueId())){
						//send reg message
						if(chatMode==0){
							b.getPlayer().sendMessage(nameColor + name +": " + messageColor + message);
						}
						else if(chatMode==1){
							if(r.hasNation() || b.getSpyMode()){
								if(r.getTown().getNation().equals(tp.getResident(b.getName()).getTown().getNation())
										 || b.getSpyMode()){
									b.getPlayer().sendMessage(nameColor + name +": " + messageColor + message);
								}
							}
						}
						else if(chatMode==2){
							if(tp.getResident(b.getName()).hasTown() || b.getSpyMode()){
								if(r.getTown().equals(tp.getResident(b.getName()).getTown())
										 || b.getSpyMode()){
									b.getPlayer().sendMessage(nameColor + name +": " + messageColor + message);
								}
							}
						}
					}
					else{
						//send json message
						TextComponent [] fullM;
						fullM = new TextComponent[2];
						if(!mediaLink.equals("")){
							fullM[0] = new TextComponent( new ComponentBuilder(tp.getMayor(r) + tp.getSur(r) + name + ": ")
								.color(nameColor).bold(boldA)
								.event(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(group + "")
								.color(groupColor).bold(boldG).append("\n" + mediaLink + "\n").color(ChatColor.DARK_PURPLE)
								.append("Nation: ").color(ChatColor.DARK_GREEN).bold(false).append("" + tp.getNation(r) + "\n")
								.color(ChatColor.WHITE).append("Town: ").color(ChatColor.DARK_GREEN).bold(false)
								.append("" + tp.getTown(r)).color(ChatColor.WHITE).create())).event(new ClickEvent(ClickEvent.Action.OPEN_URL, mediaLink)).create());
						}
						else{
							fullM[0] = new TextComponent( new ComponentBuilder(tp.getMayor(r) + tp.getSur(r) + name + ": ")
								.color(nameColor).bold(boldA)
								.event(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(group + "")
								.color(groupColor).bold(boldG).append("\n").append("Nation: ").color(ChatColor.DARK_GREEN).bold(false)
								.append("" + tp.getNation(r) + "\n").color(ChatColor.WHITE).append("Town: ")
								.color(ChatColor.DARK_GREEN).bold(false).append("" + tp.getTown(r)).color(ChatColor.WHITE)
								.create())).create());
						}
						
						fullM[1] = new TextComponent( new ComponentBuilder(message).color(messageColor).bold(boldM).create());
						
						//Send to players depending on chatmode.
						if(chatMode==0){
							b.getPlayer().spigot().sendMessage(fullM);
						}
						else if(chatMode==1){
							if(tp.getResident(b.getName()).hasNation() || b.getSpyMode()){
								if(r.getTown().getNation().equals(tp.getResident(b.getName()).getTown().getNation())
										 || b.getSpyMode()){
									b.getPlayer().spigot().sendMessage(fullM);
								}
							}
						}
						else if(chatMode==2){
							if(tp.getResident(b.getName()).hasTown() || b.getSpyMode()){
								if(r.getTown().equals(tp.getResident(b.getName()).getTown())
										 || b.getSpyMode()){
									b.getPlayer().spigot().sendMessage(fullM);
								}
							}
						}
					}
				}
			}
		}
		tPatch.plugin.getLogger().info(name + ": " + message);
	}
	
	//Methods to give temp attributes in the case of a command such as /tc Some text here
	//This way the user can send a single message in a chat channel instead of having to switch
	//between modes.
	public void sendGlobalMessage() throws TownyException{
		tPatch.plugin.getLogger().info("Hit1");
		messageColor = ChatColor.WHITE;
		chatMode = 0;
		chat();
	}
	public void sendNationMessage() throws TownyException{
		tPatch.plugin.getLogger().info("Hit2");
		messageColor = ChatColor.GOLD;
		chatMode = 1;
		chat();
	}
	public void sendTownMessage() throws TownyException{
		tPatch.plugin.getLogger().info("Hit3");
		messageColor = ChatColor.AQUA;
		chatMode = 2;
		chat();
	}
	
	//send message to group that has perm gchat.admin
	public void adminGroupMessage(String s){
		for(thePlayer p: tPatch.onlinePlayers){
			if(p.getPlayer().hasPermission("gchat.admin")){
				if(p.getSpyMode()){
					p.getPlayer().sendMessage(ChatColor.YELLOW + s + "");
				}
			}
		}
	}
	
	public void checkCaps(){
		int i=0;
		int count=0;
		do{
			if(Character.isUpperCase(message.charAt(i))){
				count++;
			}
			i++;
		}while(count<5 && i < message.length());
		if(count>4){
			message = message.toLowerCase();
		}
	}
}
