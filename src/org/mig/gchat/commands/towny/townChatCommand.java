package org.mig.gchat.commands.towny;

import net.md_5.bungee.api.ChatColor;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.mig.gchat.chat.ChatControl;
import org.mig.gchat.utils.GChat;
import org.mig.gchat.utils.minechatCompatability;
import org.mig.gchat.utils.compatability.TownyHandler;


public class townChatCommand implements CommandExecutor{

	@Override
	public boolean onCommand(CommandSender sender, Command command,
			String label, String[] args) {
		if(sender instanceof Player){
			Player p = (Player) sender;
			TownyHandler th = new TownyHandler(p.getName());
			//if the command is only "/gchat tc" put into Town Chat mode.
			if(th.inTown()){
				if(args.length == 0){
					GChat.getThePlayer(p).setChatMode(2);
					GChat.getThePlayer(p).setTextColor(ChatColor.AQUA);
					p.sendMessage(ChatColor.AQUA + "Town Chat enabled!");
				}
				else {
					String message = args[0];
					for(int i = 2; i < args.length; i++){
						message = message + " " + args[i];
					}
					ChatControl c = new ChatControl(GChat.getThePlayer(p), message,minechatCompatability.mineChatStatus(p.getUniqueId()));
					
					c.startSingleTownMessage();
				}
			}
			else{
				p.sendMessage(ChatColor.AQUA + "No town found, please join a town to enable Town Chat!");
			}
		}
		return false;
	}

}