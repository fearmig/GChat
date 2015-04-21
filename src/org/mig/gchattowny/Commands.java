package org.mig.gchattowny;

import java.sql.SQLException;






import net.md_5.bungee.api.ChatColor;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.palmergames.bukkit.towny.exceptions.TownyException;

public class Commands implements CommandExecutor{
	
	@Override
	public boolean onCommand(CommandSender sender, Command command,
			String label, String[] args) {
		if(sender instanceof Player){
			Player p = (Player) sender;
			
			switch(args[0]){
				case "on":
					//turn Mine chat mode on
					minechatOn(args, p);
					break;
				case "off":
					//turn Mine chat mode off
					minechatOff(args, p);
					break;
				case "blockword":
					//block word from being said in chat
					blockWord(args, p);
					break;
				case "unblockword":
					//unblock word from being said in chat
					unblockWord(args, p);
					break;
				case "importwords":
					//import words into sql table
					importWords(args, p);
					break;
				case "set":
					//Home to medialinks (so far)
					set(args, p);
					break;
				case "tc":
					//turn on townchat mode
					townChat(args, p);
					break;
				case "nc":
					//turn on nationchat mode
					nationChat(args, p);
					break;
				case "gc":
					//turn on globalchat mode
					globalChat(args, p);
					break;
				case "spy":
					//turn on spychat mode
					spyMode(args, p);
					break;
				default:
					
					break;
			}
		}
		return false;
	}
	
	//turn minechat mode on
	private void minechatOn(String args[], Player p){	
		if(args.length==1){
			if(minechatCompatability.mineChatStatus(p.getUniqueId())){
				p.sendMessage(ChatColor.AQUA + "Minechat has already been toggled on");				
			}
			else{
				p.sendMessage(ChatColor.AQUA + "Minechat has been toggled on");
				minechatCompatability.mineChatOn(p.getUniqueId());
			}
		}
		else{
			p.sendMessage(ChatColor.AQUA + "Incorrect command format");
		}
	}
	
	//turn minechat mode off
	private void minechatOff(String args[], Player p){
		if(args.length == 1){
			if(minechatCompatability.mineChatStatus(p.getUniqueId())){
				p.sendMessage(ChatColor.AQUA + "Minechat has been toggled off");
				minechatCompatability.mineChatOff(p.getUniqueId());
			}
			else{
				p.sendMessage(ChatColor.AQUA + "Minechat has already been toggled off");
			}
		}
		else{
			p.sendMessage(ChatColor.AQUA + "Incorrect command format");
		}
	}
	
	//add a word to the blocked words list
	private void blockWord(String args [], Player p){ 
		if(p.hasPermission("gchat.admin")){
			p.sendMessage(ChatColor.WHITE + "The word '" + ChatColor.RED + args[1] + ChatColor.WHITE + "' has been blocked.");
			if(args.length==2){
				badWordHandler tw = new badWordHandler();
				String word = args[1].replace('_', ' ');
				int i = tw.findBadWord(word);
				if(i==-1){
					badWordHandler.badWords.add(word);
					if(tPatch.plugin.getConfig().getBoolean("MySql")){
						try {
							tPatch.plugin.mysql.addWord(word, "1");
						} catch (ClassNotFoundException | SQLException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
					else{
						tPatch.plugin.getConfig().set("badWords", badWordHandler.badWords);
						tPatch.plugin.saveConfig();
					}
				}
			}
			else{
					p.sendMessage(ChatColor.AQUA + "Incorrect command format");
			}
		}
	}

	//remove a word from the blocked words list
	private void unblockWord(String args [], Player p){ 
		if(p.hasPermission("gchat.admin")){
			p.sendMessage(ChatColor.WHITE + "The word '" + ChatColor.RED + args[1] + ChatColor.WHITE + "' has been unblocked.");
			String word = args[1].replace('_', ' ');
			if(args.length==2){
				badWordHandler tw = new badWordHandler();
				int i = tw.findBadWord(word);
				if(i>=0){
					if(tPatch.plugin.getConfig().getBoolean("MySql")){
						try {
							tPatch.plugin.mysql.removeWord(word);
						} catch (ClassNotFoundException | SQLException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
					else{
						tPatch.plugin.getConfig().set("badWords", badWordHandler.badWords);
						tPatch.plugin.saveConfig();
					}
					badWordHandler.badWords.remove(i);	
				}
			}
		}
		else{
			p.sendMessage(ChatColor.AQUA + "Incorrect command format");
		}
	}
		
	//Covert yml badwordslist to MySQL
	private void importWords(String args[], Player p){
		badWordHandler bwh = new badWordHandler();
		if(p.hasPermission("gchat.admin")){
			if(args.length==1){
				bwh.fillList();
				for(int i = 0; i<badWordHandler.badWords.size(); i++){
					tPatch.plugin.getLogger().info(badWordHandler.badWords.get(i));
					try {
						tPatch.plugin.mysql.addWord(badWordHandler.badWords.get(i),badWordHandler.wordTier.get(i));
					} catch (ClassNotFoundException | SQLException e) {
						e.printStackTrace();
					}
				}
				p.sendMessage(ChatColor.AQUA + "Words have been coverted from config.yml to MySQL.");
			}
		}
	}
			
	//Set Media link twitter
	private void set(String args[], Player p){
		if(args.length==3){
			if(p.hasPermission("gchat.medialink")){
				if(args[1]!=null){
					if(args[1].equalsIgnoreCase("twitter")){
						tPatch.getThePlayer(p).setMediaLink("https://twitter.com/"+args[2]);
						p.sendMessage(ChatColor.AQUA + "Your Twitter username has been set as: " + ChatColor.DARK_AQUA + args[1]);
					}
					else if(args[1].equalsIgnoreCase("youtube")){
						tPatch.getThePlayer(p).setMediaLink("https://youtube.com/user/"+args[2]);
						p.sendMessage(ChatColor.AQUA + "Your Youtube username has been set as: " + ChatColor.DARK_AQUA + args[1]);
					}
					else if(args[1].equalsIgnoreCase("twitch")){
						tPatch.getThePlayer(p).setMediaLink("https://twitch.tv/"+args[2]);
						p.sendMessage(ChatColor.AQUA + "Your Twitch channel has been set as: " + ChatColor.DARK_AQUA + args[1]);
					}
				}
			}
			else{
				p.sendMessage(ChatColor.GREEN + "If you would like a Social Media Link please visit our store on GorillaCraft.com");
			}
		}
	}
		
	//Town Chat mode
	private void townChat(String args[], Player p){
		thePatch tp = new thePatch();
		//if the command is only "/gchat tc" put into Town Chat mode.
		if(tp.getResident(tPatch.getThePlayer(p).getName()).hasTown()){
			if(args.length == 1){
				tPatch.getThePlayer(p).setChatMode(2);
				tPatch.getThePlayer(p).setTextColor(ChatColor.AQUA);
				p.sendMessage(ChatColor.AQUA + "Town Chat enabled!");
			}
			else {
				String message = args[1];
				for(int i = 2; i < args.length; i++){
					message = message + " " + args[i];
				}
				chatControl c = new chatControl(tPatch.getThePlayer(p), message,minechatCompatability.mineChatStatus(p.getUniqueId()));
				try {
					c.sendTownMessage();
				} catch (TownyException e) {
					e.printStackTrace();
				}
			}
		}
		else{
			p.sendMessage(ChatColor.AQUA + "No town found, please join a town to enable Town Chat!");
		}
	}
		
	//Nation Chat mode
	private void nationChat(String args[], Player p){
		thePatch tp = new thePatch();
		//if the command is only "/gchat nc" put into Town Chat mode.
		if(tp.getResident(tPatch.getThePlayer(p).getName()).hasNation()){
			if(args.length == 1){
				tPatch.getThePlayer(p).setChatMode(1);
				tPatch.getThePlayer(p).setTextColor(ChatColor.GOLD);
				p.sendMessage(ChatColor.AQUA + "Nation Chat enabled!");
			}
			else {
				String message = args[1];
				for(int i = 2; i < args.length; i++){
					message = message + " " + args[i];
				}
				chatControl c = new chatControl(tPatch.getThePlayer(p), message,minechatCompatability.mineChatStatus(p.getUniqueId()));
				try {
					c.sendNationMessage();
				} catch (TownyException e) {
					e.printStackTrace();
				}
			}
		}
		else{
			p.sendMessage(ChatColor.AQUA + "No town found, please join a town to enable Town Chat!");
		}
	}
			
	//Global Chat mode
	private void globalChat(String args[], Player p){
		if(args.length==1){
			tPatch.getThePlayer(p).setChatMode(0);
			tPatch.getThePlayer(p).setTextColor(ChatColor.WHITE);
			p.sendMessage(ChatColor.WHITE + "Global Chat enabled.");
		}
		else {
			String message = args[1];
			for(int i = 2; i < args.length; i++){
				message = message + " " + args[i];
			}
			chatControl c = new chatControl(tPatch.getThePlayer(p), message,minechatCompatability.mineChatStatus(p.getUniqueId()));
			try {
				c.sendGlobalMessage();
			} catch (TownyException e) {
				e.printStackTrace();
			}
		}
	}
	
	//Spy Chat mode
	private void spyMode(String args[], Player p){
		if(args.length==1){
			if(p.hasPermission("gchat.admin")){
				if(tPatch.getThePlayer(p).getSpyMode()){
					tPatch.getThePlayer(p).setSpyMode(false);
					p.sendMessage(ChatColor.RED + "SpyMode disabled. Thats right, stop peaking.");
				}
				else{
					tPatch.getThePlayer(p).setSpyMode(true);
					p.sendMessage(ChatColor.RED + "SpyMode enabled. You sneaky dog you.");
				}
			}
		}
	}
}
