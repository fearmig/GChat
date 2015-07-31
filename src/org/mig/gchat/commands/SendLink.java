package org.mig.gchat.commands;

import net.md_5.bungee.api.ChatColor;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.mig.gchat.GChat;

public class SendLink implements CommandExecutor{

	private GChat main;
	
	public SendLink(GChat main){
		this.main = main;
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command command, String label,
			String[] args) {
		if(args.length == 1){
			if(args[0].substring(0, 8).equals("https://") || args[0].substring(0, 7).equals("http://")){
				if(sender.hasPermission("gchat.link")){
						main.getServer().broadcastMessage(ChatColor.AQUA + sender.getName() + "'s link: " +
								ChatColor.GOLD + args[0]);
				}
			}
			else{
				sender.sendMessage(ChatColor.RED + "That is not a valid url.");
			}
			return true;
		}
		else{
			sender.sendMessage(ChatColor.RED + "Incorrect command format. For help do " + ChatColor.GOLD
					+ "/gperks help");
		}
		return true;
	}
}
