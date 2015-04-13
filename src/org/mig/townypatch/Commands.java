package org.mig.townypatch;

import java.sql.SQLException;



import net.md_5.bungee.api.ChatColor;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

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
		if(args[0].equalsIgnoreCase("off")){
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
		if(args[0].equalsIgnoreCase("blockword")){
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
		if(args[0].equalsIgnoreCase("unblockword")){
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
		if(args[0].equalsIgnoreCase("importwords")){
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
		if(args[0].equalsIgnoreCase("setTwitter")){
			if(args.length==2){
				if(sender instanceof Player){
					Player p = (Player) sender;
					if(p.hasPermission("gchat.medialink")){
						if(args[1]!=null){
							for(int i = 0; i<tPatch.onlinePlayers.size(); i++){
								if(tPatch.onlinePlayers.get(i).getName().equals(p.getName())){
									tPatch.onlinePlayers.get(i).setMediaLink("https://twitter.com/"+args[1]);
									p.sendMessage(ChatColor.AQUA + "Your Twitter username has been set as: " + ChatColor.DARK_AQUA + args[1]);
								}
							}
						}
					}
					else{
						p.sendMessage(ChatColor.GREEN + "If you would like a Social Media Link please visit our store on GorillaCraft.com");
					}
				}
			}
		}
		//Set Media link youtube
		if(args[0].equalsIgnoreCase("setYoutube")){
			if(args.length==2){
				if(sender instanceof Player){
					Player p = (Player) sender;
					if(p.hasPermission("gchat.medialink")){
						if(args[1]!=null){
							for(int i = 0; i<tPatch.onlinePlayers.size(); i++){
								if(tPatch.onlinePlayers.get(i).getName().equals(p.getName())){
									tPatch.onlinePlayers.get(i).setMediaLink("https://youtube.com/user/"+args[1]);
									p.sendMessage(ChatColor.AQUA + "Your Youtube username has been set as: " + ChatColor.DARK_AQUA + args[1]);
								}
							}
						}
					}
					else{
						p.sendMessage(ChatColor.GREEN + "If you would like a Social Media Link please visit our store on GorillaCraft.com");
					}
				}
			}
		}
		//Set Media link twitch
		if(args[0].equalsIgnoreCase("setTwitch")){
			if(args.length==2){
				if(sender instanceof Player){
					Player p = (Player) sender;
					if(p.hasPermission("gchat.medialink")){
						if(args[1]!=null){
							for(int i = 0; i<tPatch.onlinePlayers.size(); i++){
								if(tPatch.onlinePlayers.get(i).getName().equals(p.getName())){
									tPatch.onlinePlayers.get(i).setMediaLink("https://twitch.tv/"+args[1]);
									p.sendMessage(ChatColor.AQUA + "Your Twitch channel has been set as: " + ChatColor.DARK_AQUA + args[1]);
								}
							}
						}
					}
					else{
					p.sendMessage(ChatColor.GREEN + "If you would like a Social Media Link please visit our store on GorillaCraft.com");
					}
				}
			}
		}
		
		//Town Chat mode
		if(args[0].equalsIgnoreCase("tc")){
			if(args.length==1){
				if(sender instanceof Player){
					Player p = (Player) sender;
					for(int i = 0; i<tPatch.onlinePlayers.size(); i++){
						if(tPatch.onlinePlayers.get(i).getName().equals(p.getName())){
							thePatch tp = new thePatch();
							if(tp.getResident(tPatch.onlinePlayers.get(i).getName()).hasTown()){
								tPatch.onlinePlayers.get(i).setChatMode(2);
								tPatch.onlinePlayers.get(i).setTextColor(ChatColor.AQUA);
								p.sendMessage(ChatColor.AQUA + "Town Chat enabled.");
							}
						}
					}
				}
			}
		}
		//Nation Chat mode
		if(args[0].equalsIgnoreCase("nc")){
			if(args.length==1){
				if(sender instanceof Player){
					Player p = (Player) sender;
					for(int i = 0; i<tPatch.onlinePlayers.size(); i++){
						if(tPatch.onlinePlayers.get(i).getName().equals(p.getName())){
							thePatch tp = new thePatch();
							if(tp.getResident(tPatch.onlinePlayers.get(i).getName()).hasNation()){
								tPatch.onlinePlayers.get(i).setChatMode(1);
								tPatch.onlinePlayers.get(i).setTextColor(ChatColor.GOLD);
								p.sendMessage(ChatColor.GOLD + "Nation Chat enabled.");
							}
						}
					}
				}
			}
		}
		
		//Global Chat mode
		if(args[0].equalsIgnoreCase("gc")){
			if(args.length==1){
				if(sender instanceof Player){
					Player p = (Player) sender;
					for(int i = 0; i<tPatch.onlinePlayers.size(); i++){
						if(tPatch.onlinePlayers.get(i).getName().equals(p.getName())){
							tPatch.onlinePlayers.get(i).setChatMode(0);
							tPatch.onlinePlayers.get(i).setTextColor(ChatColor.WHITE);
							p.sendMessage(ChatColor.WHITE + "Global Chat enabled.");
						}
					}
				}
			}
		}
		
		//Spy Chat mode
		if(args[0].equalsIgnoreCase("spy")){
			if(args.length==1){
				if(sender instanceof Player){
					Player p = (Player) sender;
					if(p.hasPermission("gchat.admin"))
					for(int i = 0; i<tPatch.onlinePlayers.size(); i++){
						if(tPatch.onlinePlayers.get(i).getName().equals(p.getName())){
							if(tPatch.onlinePlayers.get(i).getSpyMode()){
								tPatch.onlinePlayers.get(i).setSpyMode(false);
								p.sendMessage(ChatColor.RED + "SpyMode disabled. Thats right, stop peaking.");
							}
							else{
								tPatch.onlinePlayers.get(i).setSpyMode(true);
								p.sendMessage(ChatColor.RED + "SpyMode enabled. You sneaky dog you.");
							}
						}
					}
				}
			}
		}
		return false;
	}

}
