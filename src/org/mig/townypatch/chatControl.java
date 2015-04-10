package org.mig.townypatch;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import com.earth2me.essentials.Essentials;
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
	public ChatColor messageColor;
	public ChatColor groupColor;
	public ChatColor nameColor;
	
	Essentials ess = (Essentials) Bukkit.getServer().getPluginManager().getPlugin("Essentials");
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
		if(!tw.testMessage(message)){
			player.spigot().sendMessage(new TextComponent( new ComponentBuilder("Please do not use that language in here").color(ChatColor.RED).create()));
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
				for(int i = 0; i < ess.getUser(b.getPlayer())._getIgnoredPlayers().size(); i++){
					if(ess.getUser(b.getPlayer())._getIgnoredPlayers().get(i).equalsIgnoreCase(name)){
						ignored=true;
					}
				}
				if(!ignored){
					if(minechatCompatability.mineChatStatus(b.getPlayer().getUniqueId())){
						//send reg message
						b.getPlayer().sendMessage(nameColor + name +": " + messageColor + message);
					}
					else{
						thePatch tp = new thePatch();
						Resident r = tp.getResident(name);
						//send json message
						TextComponent [] fullM;
						if(!mediaLink.equals("")){
							fullM = new TextComponent[2];
							
							fullM[0] = new TextComponent( new ComponentBuilder(tp.getMayor(r) + tp.getSur(r) + name + ": ")
									.color(nameColor).bold(boldA)
									.event(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(group + "")
									.color(groupColor).bold(boldG).append("\n" + mediaLink).color(ChatColor.WHITE)
									.append("\n" + "Nation:").color(ChatColor.GOLD).bold(true).append("" + tp.getNation(r))
									.color(ChatColor.WHITE).append("\n" + "Town:").color(ChatColor.GOLD).bold(true)
									.append("" + r.getTown()).color(ChatColor.WHITE).create())).create());
							
							
							fullM[1] = new TextComponent( new ComponentBuilder(message).color(messageColor).bold(boldM).create());
							b.getPlayer().spigot().sendMessage(fullM);
						}
						else{
							fullM = new TextComponent[2];
							
								fullM[0] = new TextComponent( new ComponentBuilder(tp.getMayor(r) + tp.getSur(r) + name + ": ")
									.color(nameColor).bold(boldA)
									.event(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(group + "")
									.color(groupColor).bold(boldG).append("" + tp.getNation(r))
									.color(ChatColor.WHITE).append("\n" + "Town:").color(ChatColor.GOLD).bold(true)
									.append("" + r.getTown()).color(ChatColor.WHITE).create()))
									.event(new ClickEvent(ClickEvent.Action.OPEN_URL, mediaLink)).create());
							
							fullM[1] = new TextComponent( new ComponentBuilder(message).color(messageColor).bold(boldM).create());
							b.getPlayer().spigot().sendMessage(fullM);
						}
					}
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
