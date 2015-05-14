package org.mig.gchat.commands;

import net.md_5.bungee.api.ChatColor;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.mig.gchat.chat.ChatControl;
import org.mig.gchat.utils.GChat;
import org.mig.gchat.utils.MinechatCompatability;

//Command to put a player into a semipermanent state of admin chat mode
public class AdminChatCommand implements CommandExecutor{
	
	@Override
	public boolean onCommand(CommandSender sender, Command command,
			String label, String[] args) {
		if(sender instanceof Player){
			Player p = (Player) sender;
			//test if a player is allowed to run this command
			if(p.hasPermission("gchat.adminchat")){
				//If only '/ac' was run then put the player into admin chat mode
				if(args.length==0){
					//set the attributes of that chat mode
					GChat.getThePlayer(p).setChatMode(1);
					GChat.getThePlayer(p).setTextColor(ChatColor.GREEN);
					p.sendMessage(ChatColor.GREEN + "Admin Chat enabled.");
				}
				//If the player sends '/ac and some text' send a one time message in admin chat.
				else {
					String message = args[0];
					for(int i = 1; i < args.length; i++){
						message = message + " " + args[i];
					}
					ChatControl c = new ChatControl(GChat.getThePlayer(p), message,MinechatCompatability.mineChatStatus(p.getUniqueId()));
					c.startSingleAdminMessage();
				}
			}
			
		}
		return false;
	}
}
