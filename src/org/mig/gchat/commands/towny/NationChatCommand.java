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
public class NationChatCommand implements CommandExecutor{

	@Override
	public boolean onCommand(CommandSender sender, Command command,
			String label, String[] args) {
		if(sender instanceof Player){
			Player p = (Player) sender;
			TownyHandler th = new TownyHandler(p.getName());
			//Test if the player is in a nation
			if(th.inNation()){
				//If only '/nc' was run then put the player into nation chat mode
				if(args.length == 0){
					//Set nation chat mode attributes
					GChat.getThePlayer(p).setChatMode(3);
					GChat.getThePlayer(p).setTextColor(ChatColor.GOLD);
					p.sendMessage(ChatColor.AQUA + "Nation Chat enabled!");
				}
				//If the player sends '/nc and some text' send a one time message in nation chat.
				else {
					String message = args[0];
					for(int i = 1; i < args.length; i++){
						message = message + " " + args[i];
					}
					ChatControl c = new ChatControl(GChat.getThePlayer(p), message,MinechatCompatability.mineChatStatus(p.getUniqueId()));
					
					c.startSingleNationMessage();
				}
			}
			//if the player is in a nation they are notified of such
			else{
				p.sendMessage(ChatColor.AQUA + "No nation found, please join a nation to enable Nation Chat!");
			}
		}
		return false;
	}

}
