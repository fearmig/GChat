package org.mig.gchat.commands;

import java.sql.SQLException;

import net.md_5.bungee.api.ChatColor;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.mig.gchat.GChat;
import org.mig.gchat.async.AsyncSetName;
import org.mig.gchat.chat.filter.BadWordHandler;
import org.mig.gchat.objects.ThePlayer;
import org.mig.gchat.utils.MinechatCompatability;

public class Commands implements CommandExecutor{
	//initialize class that handles bad language
	private BadWordHandler bw;
	private GChat main;
	
	public Commands(GChat main){
		this.main = main;
		bw = new BadWordHandler(main);
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command command,
			String label, String[] args) {
		
		if(sender instanceof Player){
			Player p = (Player) sender;
			
			if(args.length==0){
				p.sendMessage(ChatColor.RED + "Incorrect /gchat command. Please do " + ChatColor.GOLD +
						"/gchat help" + ChatColor.RED + "for help with gchat commands.");
			}
			else{
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
						if(p.hasPermission("gchat.admin"))
							blockWord(args, p);
						break;
					case "unblockword":
						//unblock word from being said in chat
						if(p.hasPermission("gchat.admin"))
							unblockWord(args, p);
						break;
					case "importwords":
						//import words into sql table
						if(p.hasPermission("gchat.admin"))
							importWords(args, p);
						break;
					case "set":
						//Home to medialinks (so far)
						if(p.hasPermission("gchat.medialinks"))
							try {
								set(args, p);
							} catch (ClassNotFoundException | SQLException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						else
							p.sendMessage(ChatColor.GREEN + "If you would like a Social Media Link please visit our store!");
						break;
					case "spy":
						//turn on spychat mode
						if(p.hasPermission("gchat.adminchat"))
							spyMode(args, p);
						break;
					case "help":
						//send help message
						help(p);
						break;
					case "rename":
						//rename the player
						if(p.hasPermission("gchat.admin"))
							renamePlayer(args, p);
						break;
					default:
						p.sendMessage(ChatColor.RED + "Incorrect /gchat command. Please do " + ChatColor.GOLD +
							"/gchat help" + ChatColor.RED + "for help with gchat commands.");
				}
			}
		}
		return false;
	}

	//turn minechat mode on
	private void minechatOn(String args[], Player p){	
		if(args.length==1){
			if(MinechatCompatability.mineChatStatus(p.getUniqueId())){
				p.sendMessage(ChatColor.AQUA + "Minechat has already been toggled on");				
			}
			else{
				p.sendMessage(ChatColor.AQUA + "Minechat has been toggled on");
				MinechatCompatability.mineChatOn(p.getUniqueId());
			}
		}
		else{
			p.sendMessage(ChatColor.AQUA + "Incorrect command format");
		}
	}
	
	//turn minechat mode off
	private void minechatOff(String args[], Player p){
		if(args.length == 1){
			if(MinechatCompatability.mineChatStatus(p.getUniqueId())){
				p.sendMessage(ChatColor.AQUA + "Minechat has been toggled off");
				MinechatCompatability.mineChatOff(p.getUniqueId());
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
		p.sendMessage(ChatColor.WHITE + "The word '" + ChatColor.RED + args[1] + ChatColor.WHITE + "' has been blocked.");
		if(args.length==2){
			String word = args[1].replace('_', ' ');
			if(!bw.isBadWord(word.toLowerCase())){
				bw.addBadWord(word.toLowerCase());
			}
		}
		else{
			p.sendMessage(ChatColor.AQUA + "Incorrect command format");
		}
	}


	//remove a word from the blocked words list
	private void unblockWord(String args [], Player p){
		p.sendMessage(ChatColor.WHITE + "The word '" + ChatColor.RED + args[1] + ChatColor.WHITE + "' has been unblocked.");
		String word = (args[1].replace('_', ' ')).toLowerCase();
		if(args.length==2){
			if(bw.isBadWord(word))
				bw.removeBadWord(word);	
		}
		
		else{
			p.sendMessage(ChatColor.AQUA + "Incorrect command format");
		}
	}
		
	//Covert yml badwordslist to MySQL
	private void importWords(String args[], Player p){
		if(bw.convertToSql())
			p.sendMessage(ChatColor.AQUA + "Words have been coverted from config.yml to MySQL.");
		else
			p.sendMessage(ChatColor.RED + "There was a problem converting words from config.yml to MySQL.");
	}
			
	// Set Media link
	private void set(String args[], Player p) throws ClassNotFoundException,
			SQLException {
		if (args[1] != null) {
			// set the twitter link
			if (args[1].equalsIgnoreCase("twitter")) {
				main.getThePlayerModule()
						.getThePlayer(p.getUniqueId().toString())
						.setMediaLink("https://twitter.com/" + args[2]);
				p.sendMessage(ChatColor.AQUA
						+ "Your Twitter username has been set as: "
						+ ChatColor.DARK_AQUA + args[2]);
			}
			// set the youtube link
			else if (args[1].equalsIgnoreCase("youtube")) {
				main.getThePlayerModule()
						.getThePlayer(p.getUniqueId().toString())
						.setMediaLink("https://youtube.com/user/" + args[2]);
				p.sendMessage(ChatColor.AQUA
						+ "Your Youtube username has been set as: "
						+ ChatColor.DARK_AQUA + args[2]);
			}
			// set the twitch link
			else if (args[1].equalsIgnoreCase("twitch")) {
				main.getThePlayerModule()
						.getThePlayer(p.getUniqueId().toString())
						.setMediaLink("https://twitch.tv/" + args[2]);
				p.sendMessage(ChatColor.AQUA
						+ "Your Twitch channel has been set as: "
						+ ChatColor.DARK_AQUA + args[2]);
			}
			// set a custom link for the player
			else if (args[1].equalsIgnoreCase("link")) {
				if (p.hasPermission("gchat.customLink")) {
					if (args.length == 3) {
							main.getThePlayerModule().setMediaLink(
								args[2],p.getUniqueId().toString());
					}
				
					else if (args.length == 4 && p.hasPermission("gchat.admin")){
						if (Bukkit.getPlayer(args[3]) != null) {
							main.getThePlayerModule().setMediaLink(
								args[2],Bukkit.getPlayer(args[3]).getUniqueId()
									.toString());

						}
					}
					else {
						sendMediaLinkHelp(p);
					}
					
				} else {
					p.sendMessage(ChatColor.RED + "Mig says no.");
				}
			}

			// display all the commands and what the do to the player
			else if (args[1].equalsIgnoreCase("help")) {
				sendMediaLinkHelp(p);
			}
			
			else {
				sendMediaLinkHelp(p);
			}
		} else {
			sendMediaLinkHelp(p);
		}
	}	
	
	//Set media link help message
	private void sendMediaLinkHelp(Player p){
		p.sendMessage(ChatColor.AQUA
				+ "/gchat set twitter {twitterName}" + ChatColor.GOLD
				+ " ~ set Twitter media link. Do not include @");

		p.sendMessage(ChatColor.AQUA
				+ "/gchat set youtube {channelName}"
				+ ChatColor.GOLD
				+ " ~ set Youtube media link. Only include what is after youtube.com/user/");

		p.sendMessage(ChatColor.AQUA
				+ "/gchat set twitch {channelName}"
				+ ChatColor.GOLD
				+ " ~ set Twitch media link. Only include channel name.");

		if (p.hasPermission("gchat.customLink"))
			p.sendMessage(ChatColor.AQUA
					+ "/gchat set link [link]"
					+ ChatColor.GOLD
					+ " ~ set custom media link. Only include real links.");

		if (p.hasPermission("gchat.admin") && p.hasPermission("gchat.customLink"))
			p.sendMessage(ChatColor.AQUA
					+ "/gchat set link [link] [playername]"
					+ ChatColor.GOLD
					+ " ~ set custom media link. Only include real links.");
	}
	
	//Spy Chat mode
	private void spyMode(String args[], Player p){
		if(args.length==1){
			if(p.hasPermission("gchat.admin")){
				if(main.getThePlayerModule().getThePlayer(p.getUniqueId().toString()).isSpyMode()){
					main.getThePlayerModule().getThePlayer(p.getUniqueId().toString()).setSpyMode(false);
					p.sendMessage(ChatColor.RED + "SpyMode disabled. Thats right, stop peaking.");
				}
				else{
					main.getThePlayerModule().getThePlayer(p.getUniqueId().toString()).setSpyMode(true);
					p.sendMessage(ChatColor.RED + "SpyMode enabled. You sneaky dog you.");
				}
			}
		}
	}

	//Provide Help on commands
	private void help(Player p){
		
		p.sendMessage(ChatColor.AQUA + "      " + ChatColor.BOLD + "" + ChatColor.UNDERLINE + "GChat Help");
		p.sendMessage("");
		p.sendMessage(ChatColor.AQUA + "/gchat on" + ChatColor.GOLD + " ~ Turn on Minechat Mode (non JSON text)");
		p.sendMessage(ChatColor.AQUA + "/gchat off" + ChatColor.GOLD + " ~ Turn off Minechat Mode (non JSON text)");
		p.sendMessage(ChatColor.AQUA + "/gchat set {}" + ChatColor.GOLD + " ~ do '/gchat set help' to learn more");
		p.sendMessage(ChatColor.AQUA + "/link [link]" + ChatColor.GOLD + " ~ Send a link in chat. Must be a full link.");
		
		//if towny is enabled display towny only commands in help message
		if(Bukkit.getServer().getPluginManager().isPluginEnabled("Towny")){
			p.sendMessage(ChatColor.AQUA + "/tc" + ChatColor.GOLD + " ~ Put yourself into Town Chat Mode");
			p.sendMessage(ChatColor.AQUA + "/tc [message]" + ChatColor.GOLD + " ~ Sends a single message in Town Chat Mode");
			p.sendMessage(ChatColor.AQUA + "/nc" + ChatColor.GOLD + " ~ Put yourself into Nation Chat Mode");
			p.sendMessage(ChatColor.AQUA + "/nc [message]" + ChatColor.GOLD + " ~ Sends a single message in Nation Chat Mode");	
		}
		
		p.sendMessage(ChatColor.AQUA + "/g" + ChatColor.GOLD + " ~ Put yourself into Global Chat Mode");
		p.sendMessage(ChatColor.AQUA + "/g [message]" + ChatColor.GOLD + " ~ Sends a single message in Global Chat Mode");
		
		/*
		 * Display to the user the commands to clear chat if they have the permission to use that command
		 */
		if(p.hasPermission("gchat.clearchat"))
			p.sendMessage(ChatColor.AQUA + "/cchat" + ChatColor.GOLD + " ~ Clear your personal chat box.");
		if(p.hasPermission("gchat.clearchat.global"))
			p.sendMessage(ChatColor.AQUA + "/cchat g" + ChatColor.GOLD + " ~ Clear the Global chat box.");
		
		//display admin commands to user if they have admin permission
		if(p.hasPermission("gchat.admin")){
			p.sendMessage(ChatColor.AQUA + "/ac" + ChatColor.GOLD + " ~ Put yourself into Admin Chat Mode");
			p.sendMessage(ChatColor.AQUA + "/ac [message]" + ChatColor.GOLD + " ~ Sends a single message in Admin Chat Mode");
			p.sendMessage(ChatColor.AQUA + "/gchat spy" + ChatColor.GOLD + " ~ Toggles on and off Spy Mode");
			p.sendMessage(ChatColor.AQUA + "/gchat blockword {word}" + ChatColor.GOLD + " ~ Add word to blocked word list");
			p.sendMessage(ChatColor.AQUA + "/gchat unblockword {word}" + ChatColor.GOLD + " ~ remove word from blocked word list");
			p.sendMessage(ChatColor.AQUA + "/gchat importwords" + ChatColor.GOLD + " ~ import badword list to MySql");
			p.sendMessage(ChatColor.AQUA + "/gchat rename {playerName} {newName}" + ChatColor.GOLD + " ~ rename a player");
		}
		
	}
	
	//rename a player
	private void renamePlayer(String args[], Player p){
		YamlConfiguration nConfig = main.getNamesConfig();
		ThePlayer tp;
		
		//try to get the Player Object
		Player player = (Player) Bukkit.getPlayer(args[1]);
		
		//update the player to nConfig
		if(player!=null){
			
			//set the players name in ThePlayer class
			tp = main.getThePlayerModule().getThePlayer(player.getUniqueId().toString());
			
			tp.setName(args[2]);
			
			if(main.getConfig().getBoolean("MySql")){
				try {
					AsyncSetName sn = new AsyncSetName(tp, main.mysql.getDatabase(), args[2]);
					sn.runTaskAsynchronously(main);
				} catch (ClassNotFoundException | SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			else{
				nConfig.set(tp.getUuid(), args[2]);
				main.saveNames(nConfig);
			}
			
			//change name in player tab list if enabled
			if(main.getConfig().getBoolean("TabPlayerList")){
				if(args[2].length()>=15)
					args[2] = player.getName().substring(0,14);
				player.setPlayerListName(main.getGroupModule().getGroup(main.getGroupModule()
					.getGroup(player, main)).getNameColor() + args[2]);
			}
			
			//send confirmation to user
			p.sendMessage(ChatColor.AQUA + args[1] + ChatColor.WHITE + " name has been changed to " + ChatColor.AQUA + tp.getName());
			
		}	
	}
}
