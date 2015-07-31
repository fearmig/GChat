package org.mig.gchat.async;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import net.md_5.bungee.api.ChatColor;

import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitRunnable;
import org.mig.gchat.GChat;

import code.husky.Database;

public class AsyncAddBadWord extends BukkitRunnable {

	private GChat main;
	private Database db;
	private final String word;
	private String tier;

	public AsyncAddBadWord(GChat main, String word, String tier)
			throws ClassNotFoundException, SQLException {
		this.main = main;
		this.db = main.mysql.getDatabase();
		this.word = word;
		this.tier = tier;
	}

	@Override
	public void run() {
		try {
			if (!this.db.checkConnection())
				this.db.openConnection();

			Statement statementa = this.db.getConnection().createStatement();
			Statement statement = this.db.getConnection().createStatement();

			ResultSet rs = statementa
					.executeQuery("SELECT * FROM `badwords` WHERE `BlockedWord`='"
							+ this.word + "';");
			if (!rs.first()) {
				statement
						.executeUpdate("INSERT INTO `badwords` (`BlockedWord`,`Tier`) VALUES ('"
								+ word + "','" + tier + "');");
				statementa.close();
				statement.close();
				main.getServer().getScheduler().runTask(main, new Runnable() {
					public void run() {
						Bukkit.broadcast(ChatColor.AQUA + word
								+ " has been added to the bad word list.",
								"gchat.mod");
					}
				});
			} else {
				statementa.close();
				statement.close();
			}
		} catch (ClassNotFoundException | SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
