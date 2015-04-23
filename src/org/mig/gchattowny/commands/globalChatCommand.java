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

public class globalChatCommand implements CommandExecutor{

	@Override
	public boolean onCommand(CommandSender sender, Command command,
			String label, String[] args) {
		if(sender instanceof Player){
			Player p = (Player) sender;
			if(args.length==0){
				tPatch.getThePlayer(p).setChatMode(0);
				tPatch.getThePlayer(p).setTextColor(ChatColor.WHITE);
				p.sendMessage(ChatColor.WHITE + "Global Chat enabled.");
			}
			else {
				String message = args[1];
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
