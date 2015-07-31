package org.mig.gchat.async;

import java.sql.SQLException;
import java.sql.Statement;

import net.md_5.bungee.api.ChatColor;

import org.bukkit.scheduler.BukkitRunnable;
import org.mig.gchat.GChat;
import org.mig.gchat.objects.ThePlayer;

import code.husky.Database;


public class AsyncSetMediaLink extends BukkitRunnable {

	private GChat main;
	private ThePlayer tPlayer;
	private Database db;
	private String mediaLink;

	public AsyncSetMediaLink(GChat main, ThePlayer tPlayer, Database db,
			String mediaLink) throws ClassNotFoundException, SQLException {
		this.main = main;
		this.tPlayer = tPlayer;
		this.db = db;
		this.mediaLink = mediaLink;
	}

	@Override
	public void run() {

		String uuid = tPlayer.getUuid();
		try {
			if (!this.db.checkConnection())
				this.db.openConnection();

			Statement statement = this.db.getConnection().createStatement();
			statement.executeUpdate("UPDATE `gchat` SET `MediaLink`='"
					+ mediaLink + "' WHERE `UUID`='" + uuid + "';");

			statement.close();

			main.getServer().getScheduler().runTask(main, new Runnable() {
				public void run() {
					tPlayer.getPlayer()
							.sendMessage(
									ChatColor.AQUA
											+ tPlayer.getName()+ "'s media link has been set to:");
					tPlayer.getPlayer().sendMessage(ChatColor.GOLD + mediaLink);
				}
			});

		} catch (ClassNotFoundException | SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
}