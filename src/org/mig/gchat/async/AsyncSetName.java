package org.mig.gchat.async;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.bukkit.scheduler.BukkitRunnable;
import org.mig.gchat.objects.ThePlayer;

import code.husky.Database;

public class AsyncSetName extends BukkitRunnable{

	private Database db;
	private ThePlayer tPlayer;
	private String newName;
	
	public AsyncSetName (ThePlayer tPlayer, Database db, String newName){
		this.db = db;
		this.tPlayer = tPlayer;
		this.newName = newName;
	}
	
	@Override
	public void run() {
		try {
			if (!this.db.checkConnection())
				this.db.openConnection();

			String uuid = tPlayer.getUuid();

			Statement statement = this.db.getConnection().createStatement();

			// check to see if the player already has a changed name
			Statement statementA = this.db.getConnection().createStatement();
			ResultSet rsA = statementA
					.executeQuery("SELECT `Name` FROM `gchat` WHERE `UUID`='"
							+ uuid + "';");

			// if a player exists update their entry
			if (rsA.first()) {
				statement.executeUpdate("UPDATE `gchat` SET `Name`='" + newName
						+ "' WHERE `UUID`='" + uuid + "';");
			}
			// if a player didn't already exist in this table create their entry
			else {
				statement
						.executeUpdate("INSERT INTO `gchat` (`UUID`,`Name`) VALUES ('"
								+ uuid + "','" + newName + "');");
			}
			
			statementA.close();
			statement.close();
			rsA.close();
		} catch (ClassNotFoundException | SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
