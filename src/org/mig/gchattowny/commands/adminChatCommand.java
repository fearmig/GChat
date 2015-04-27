package org.mig.gchattowny.commands;

import net.md_5.bungee.api.ChatColor;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.mig.gchattowny.chatControl;
import org.mig.gchattowny.minechatCompatability;
import org.mig.gchattowny.tPatch;

import com.palmergames.bukkit.towny.exceptions.TownyException;

public class adminChatCommand implements CommandExecutor{

	@Override
	public boolean onCommand(CommandSender sender, Command command,
			String label, String[] args) {
		if(sender instanceof Player){
			Player p = (Player) sender;
			if(args.length==0){
				tPatch.getThePlayer(p).setChatMode(3);
				tPatch.getThePlayer(p).setTextColor(ChatColor.GREEN);
				p.sendMessage(ChatColor.GREEN + "Admin Chat enabled.");
			}
			else {
				String message = args[0];
				for(int i = 2; i < args.length; i++){
					message = message + " " + args[i];
				}
				chatControl c = new chatControl(tPatch.getThePlayer(p), message,minechatCompatability.mineChatStatus(p.getUniqueId()));
				try {
					c.sendGlobalMessage();
				} catch (TownyException e) {
					e.printStackTrace();
				}
			}
			
		}
		return false;
	}
}
