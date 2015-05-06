package org.mig.gchat.commands;

import net.md_5.bungee.api.ChatColor;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.mig.gchat.chat.ChatControl;
import org.mig.gchat.utils.GChat;
import org.mig.gchat.utils.minechatCompatability;

public class globalChatCommand implements CommandExecutor{

	@Override
	public boolean onCommand(CommandSender sender, Command command,
			String label, String[] args) {
		if(sender instanceof Player){
			Player p = (Player) sender;
			if(args.length==0){
				GChat.getThePlayer(p).setChatMode(0);
				GChat.getThePlayer(p).setTextColor(ChatColor.WHITE);
				p.sendMessage(ChatColor.WHITE + "Global Chat enabled.");
			}
			else {
				String message = args[0];
				for(int i = 2; i < args.length; i++){
					message = message + " " + args[i];
				}
				ChatControl c = new ChatControl(GChat.getThePlayer(p), message,minechatCompatability.mineChatStatus(p.getUniqueId()));
				
				c.startSingleGlobalMessage();
			}
			
		}
		return false;
	}
}
