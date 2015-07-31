package org.mig.gchat.commands;

import net.md_5.bungee.api.ChatColor;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.mig.gchat.GChat;

public class Report implements CommandExecutor{
	private GChat main;
	
	public Report(GChat main){
		this.main = main;
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command command, String label,
			String[] args) {
		if(sender.hasPermission("gchat.mod")){
			Player p = (Player) sender;
			if(main.getThePlayerModule().getThePlayer(p.getUniqueId().toString()).inReportMode()){
				main.getServer().broadcastMessage(ChatColor.BLUE + "Report command ran");
			}
			return true;
		}
		return false;
	}

}
