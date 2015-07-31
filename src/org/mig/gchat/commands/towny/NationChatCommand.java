package org.mig.gchat.commands.towny;

import net.md_5.bungee.api.ChatColor;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.mig.gchat.GChat;
import org.mig.gchat.chat.ChatControl;
import org.mig.gchat.utils.MinechatCompatability;
import org.mig.gchat.utils.compatability.TownyHandler;

//Command to put a player into a semipermanent state of nation chat mode
public class NationChatCommand implements CommandExecutor{

	private GChat main;
	
	public NationChatCommand(GChat main){
		this.main = main;
	}
	
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
					main.getThePlayerModule().getThePlayer(p.getUniqueId().toString()).setChatMode(3);
					p.sendMessage(ChatColor.AQUA + "Nation Chat enabled!");
				}
				//If the player sends '/nc and some text' send a one time message in nation chat.
				else {
					if(!sender.hasPermission("gchat.nationmessage")){
						sender.sendMessage(ChatColor.RED + "You don't have permission to use this command.");
						return true;
					}
					String message = args[0];
					for(int i = 1; i < args.length; i++){
						message = message + " " + args[i];
					}
					ChatControl c = new ChatControl(main.getThePlayerModule().getThePlayer(p.getUniqueId().toString())
							, message,MinechatCompatability.mineChatStatus(p.getUniqueId()), main);
					
					c.startSingleNationMessage();
					c = null;
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
