package org.mig.gchat.commands;

import net.md_5.bungee.api.ChatColor;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.mig.gchat.GChat;
import org.mig.gchat.chat.ChatControl;

public class GlobalMute implements CommandExecutor{
	private GChat main;
	
	public GlobalMute(GChat main){
		this.main = main;
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command command, String label,
			String[] args) {
		if(args.length==0){
			if(sender.hasPermission("gchat.globalmute")){
				if(ChatControl.isGlobalMuteActive()){
					ChatControl.setGlobalMuteActive(false);
					main.getServer().broadcastMessage(ChatColor.AQUA + "GLOBAL MUTE HAS BEEN REMOVED");
				} else{
					ChatControl.setGlobalMuteActive(true);
					main.getServer().broadcastMessage(ChatColor.AQUA + "GLOBAL MUTE HAS BEEN ACTIVATED");
				}
				return true;
			}
		}
		return false;
	}
}

