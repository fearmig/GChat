package org.mig.gchat.commands.towny;

import net.md_5.bungee.api.ChatColor;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.mig.gchat.chat.ChatControl;
import org.mig.gchat.utils.GChat;
import org.mig.gchat.utils.MinechatCompatability;
import org.mig.gchat.utils.compatability.TownyHandler;

//Command to put a player into a semipermanent state of nation chat mode
public class TownChatCommand implements CommandExecutor{

	@Override
	public boolean onCommand(CommandSender sender, Command command,
			String label, String[] args) {
		if(sender instanceof Player){
			Player p = (Player) sender;
			TownyHandler th = new TownyHandler(p.getName());
			//Test if the player is in a town
			if(th.inTown()){
				//If only '/tc' was run then put the player into town chat mode
				if(args.length == 0){
					//set town chat mode attributes
					GChat.getThePlayer(p).setChatMode(2);
					GChat.getThePlayer(p).setTextColor(ChatColor.AQUA);
					p.sendMessage(ChatColor.AQUA + "Town Chat enabled!");
				}
				//If the player sends '/tc and some text' send a one time message in town chat.
				else {
					String message = args[0];
					for(int i = 2; i < args.length; i++){
						message = message + " " + args[i];
					}
					ChatControl c = new ChatControl(GChat.getThePlayer(p), message,MinechatCompatability.mineChatStatus(p.getUniqueId()));
					
					c.startSingleTownMessage();
				}
			}
			//if the player is in a town they are notified of such
			else{
				p.sendMessage(ChatColor.AQUA + "No town found, please join a town to enable Town Chat!");
			}
		}
		return false;
	}

}
