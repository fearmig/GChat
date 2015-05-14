package org.mig.gchat.commands;

import net.md_5.bungee.api.ChatColor;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.mig.gchat.chat.ChatControl;
import org.mig.gchat.utils.GChat;
import org.mig.gchat.utils.MinechatCompatability;

//command to put a player into a semipermanent state of global chat mode
public class GlobalChatCommand implements CommandExecutor{

	@Override
	public boolean onCommand(CommandSender sender, Command command,
			String label, String[] args) {
		if(sender instanceof Player){
			Player p = (Player) sender;
			//If only '/g' was run then put the player into global chat mode
			if(args.length==0){
				GChat.getThePlayer(p).setChatMode(0);
				GChat.getThePlayer(p).setTextColor(ChatColor.WHITE);
				p.sendMessage(ChatColor.WHITE + "Global Chat enabled.");
			}
			//If the player sends '/g and some text' send a one time message in global chat.
			else {
				String message = args[0];
				for(int i = 1; i < args.length; i++){
					message = message + " " + args[i];
				}
				ChatControl c = new ChatControl(GChat.getThePlayer(p), message,MinechatCompatability.mineChatStatus(p.getUniqueId()));
				
				c.startSingleGlobalMessage();
			}
			
		}
		return false;
	}
}
