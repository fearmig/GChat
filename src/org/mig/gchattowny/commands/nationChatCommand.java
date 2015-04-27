package org.mig.gchattowny.commands;

import net.md_5.bungee.api.ChatColor;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.mig.gchattowny.chatControl;
import org.mig.gchattowny.minechatCompatability;
import org.mig.gchattowny.tPatch;
import org.mig.gchattowny.thePatch;

import com.palmergames.bukkit.towny.exceptions.TownyException;

public class nationChatCommand implements CommandExecutor{

	@Override
	public boolean onCommand(CommandSender sender, Command command,
			String label, String[] args) {
		if(sender instanceof Player){
			Player p = (Player) sender;
			thePatch tp = new thePatch();
			//if the command is only "/gchat nc" put into Town Chat mode.
			if(tp.getResident(tPatch.getThePlayer(p).getName()).hasNation()){
				if(args.length == 0){
					tPatch.getThePlayer(p).setChatMode(1);
					tPatch.getThePlayer(p).setTextColor(ChatColor.GOLD);
					p.sendMessage(ChatColor.AQUA + "Nation Chat enabled!");
				}
				else {
					String message = args[0];
					for(int i = 2; i < args.length; i++){
						message = message + " " + args[i];
					}
					chatControl c = new chatControl(tPatch.getThePlayer(p), message,minechatCompatability.mineChatStatus(p.getUniqueId()));
					try {
						c.sendNationMessage();
					} catch (TownyException e) {
						e.printStackTrace();
					}
				}
			}
			else{
				p.sendMessage(ChatColor.AQUA + "No town found, please join a town to enable Town Chat!");
			}
		}
		return false;
	}

}
