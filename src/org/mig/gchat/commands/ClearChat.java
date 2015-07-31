package org.mig.gchat.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.mig.gchat.GChat;

public class ClearChat implements CommandExecutor{

	private GChat main;
	
	public ClearChat(GChat main){
		this.main = main;
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command command, String label,
			String[] args) {
		if(args.length==0){
			if(sender.hasPermission("gchat.clearchat")){
				if(sender instanceof Player){
					Player p = (Player) sender;
					for(int i = 0; i < 100; i++){
						p.sendMessage("");
					}
					return true;
				}
			}
		}
		else if(args.length==1 && args[0].equals("g")){
			if(sender.hasPermission("gchat.clearchat.global")){
				for(int i = 0; i < 100; i++){
					this.main.getServer().broadcastMessage("");
				}
				return true;
			}
		}
		return false;
	}
	
}
