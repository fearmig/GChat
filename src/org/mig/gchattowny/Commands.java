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
		//turn minechat mode on
		if(args[0].equalsIgnoreCase("on")){
			if(args.length==1){
				if(sender instanceof Player){
					Player p = (Player) sender;
					if(minechatCompatability.mineChatStatus(p.getUniqueId())){
						p.sendMessage(ChatColor.AQUA + "Minechat has already been toggled on");
					}
					else{
						p.sendMessage(ChatColor.AQUA + "Minechat has been toggled on");
						minechatCompatability.mineChatOn(p.getUniqueId());
					}
				}
			}
			else{
				sender.sendMessage(ChatColor.AQUA + "Incorrect command format");
			}
		}
		//turn minechat mode off
		else if(args[0].equalsIgnoreCase("off")){
			if(args.length == 1){
				if(sender instanceof Player){
					Player p = (Player) sender;
					if(minechatCompatability.mineChatStatus(p.getUniqueId())){
						p.sendMessage(ChatColor.AQUA + "Minechat has been toggled off");
						minechatCompatability.mineChatOff(p.getUniqueId());
					}
					else{
						p.sendMessage(ChatColor.AQUA + "Minechat has already been toggled off");
					}
				}
			}
			else{
				sender.sendMessage(ChatColor.AQUA + "Incorrect command format");
			}
		}
		//add a word to the blocked words list
		else if(args[0].equalsIgnoreCase("blockword")){
			Player p = (Player) sender;
			if(p.hasPermission("gchat.admin")){
				p.sendMessage(ChatColor.WHITE + "The word '" + ChatColor.RED + args[1] + ChatColor.WHITE + "' has been blocked.");
				if(args.length==2){
					if(sender instanceof Player){
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
				}
				else{
					sender.sendMessage(ChatColor.AQUA + "Incorrect command format");
				}
			}
		}
		//remove a word from the blocked words list
		else if(args[0].equalsIgnoreCase("unblockword")){
			Player p = (Player) sender;
			if(p.hasPermission("gchat.admin")){
				p.sendMessage(ChatColor.WHITE + "The word '" + ChatColor.RED + args[1] + ChatColor.WHITE + "' has been unblocked.");
				String word = args[1].replace('_', ' ');
				if(args.length==2){
					if(sender instanceof Player){
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
					sender.sendMessage(ChatColor.AQUA + "Incorrect command format");
				}
			}
		}
		//Covert yml badwordslist to MySQL
		else if(args[0].equalsIgnoreCase("importwords")){
			Player p = (Player) sender;
			badWordHandler bwh = new badWordHandler();
			if(p.hasPermission("gchat.admin")){
				if(sender instanceof Player){
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
						sender.sendMessage(ChatColor.AQUA + "Words have been coverted from config.yml to MySQL.");
					}
				}
			}
		}
		
		
		//Set Media link twitter
		else if(args[0].equalsIgnoreCase("setTwitter")){
			if(args.length==2){
				if(sender instanceof Player){
					Player p = (Player) sender;
					if(p.hasPermission("gchat.medialink")){
						if(args[1]!=null){
							tPatch.getThePlayer(p).setMediaLink("https://twitter.com/"+args[1]);
							p.sendMessage(ChatColor.AQUA + "Your Twitter username has been set as: " + ChatColor.DARK_AQUA + args[1]);
						}
					}
					else{
						p.sendMessage(ChatColor.GREEN + "If you would like a Social Media Link please visit our store on GorillaCraft.com");
					}
				}
			}
		}
		//Set Media link youtube
		else if(args[0].equalsIgnoreCase("setYoutube")){
			if(args.length==2){
				if(sender instanceof Player){
					Player p = (Player) sender;
					if(p.hasPermission("gchat.medialink")){
						if(args[1]!=null){
							tPatch.getThePlayer(p).setMediaLink("https://youtube.com/user/"+args[1]);
							p.sendMessage(ChatColor.AQUA + "Your Youtube username has been set as: " + ChatColor.DARK_AQUA + args[1]);
						}
					}
					else{
						p.sendMessage(ChatColor.GREEN + "If you would like a Social Media Link please visit our store on GorillaCraft.com");
					}
				}
			}
		}
		//Set Media link twitch
		else if(args[0].equalsIgnoreCase("setTwitch")){
			if(args.length==2){
				if(sender instanceof Player){
					Player p = (Player) sender;
					if(p.hasPermission("gchat.medialink")){
						if(args[1]!=null){
							tPatch.getThePlayer(p).setMediaLink("https://twitch.tv/"+args[1]);
							p.sendMessage(ChatColor.AQUA + "Your Twitch channel has been set as: " + ChatColor.DARK_AQUA + args[1]);
						}
					}
					else{
					p.sendMessage(ChatColor.GREEN + "If you would like a Social Media Link please visit our store on GorillaCraft.com");
					}
				}
			}
		}
		
		//Town Chat mode
		else if(args[0].equalsIgnoreCase("tc")){
			if(sender instanceof Player){
				thePatch tp = new thePatch();
				Player p = (Player) sender;
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
		}
		
		//Nation Chat mode
		else if(args[0].equalsIgnoreCase("nc")){
			if(sender instanceof Player){
				thePatch tp = new thePatch();
				Player p = (Player) sender;
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
		}
		
		//Global Chat mode
		else if(args[0].equalsIgnoreCase("gc")){
			if(sender instanceof Player){
				Player p = (Player) sender;
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
		}
		
		
		//Spy Chat mode
		else if(args[0].equalsIgnoreCase("spy")){
			if(args.length==1){
				if(sender instanceof Player){
					Player p = (Player) sender;
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
		else{
			if(sender instanceof Player){
				Player p = (Player) sender;
				p.sendMessage(ChatColor.RED + "Incorrect /gchat command.");
			}
		}
		return false;
	}
}
