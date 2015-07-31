package org.mig.gchat.async;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.bukkit.scheduler.BukkitRunnable;
import org.mig.gchat.GChat;
import org.mig.gchat.objects.ThePlayer;

import code.husky.Database;

public class AsyncNameChecker extends BukkitRunnable{

	private GChat main;
	private ThePlayer tPlayer;
	private Database db;
	
	public AsyncNameChecker(GChat main, ThePlayer tPlayer) throws ClassNotFoundException, SQLException {
		this.main = main;
		this.tPlayer = tPlayer;
		this.db = main.mysql.getDatabase();
	}

	@Override
	public void run() {
		
		String uuid = tPlayer.getUuid(); 
		String tempName;
		
		try {
			if (!this.db.checkConnection())
				this.db.openConnection();

			// check if the user has a forced rename
			Statement statementA = this.db.getConnection().createStatement();
			ResultSet rsA = statementA
					.executeQuery("SELECT `Name` FROM `gchat` WHERE `UUID`='"
							+ uuid + "';");

			if (rsA.first()) {
				tempName = rsA.getString("Name");
				statementA.close();
				if (tempName!= null && tempName.length() > 1)
					tPlayer.setName(tempName);

				rsA.close();
			}
			
			main.getServer().getScheduler().runTask(main, new Runnable() {
				public void run() {
					String tempName = tPlayer.getName();
					if(tempName.length()>=15)
						tempName = tempName.substring(0,14);
					tPlayer.getPlayer().setPlayerListName(main.getGroupModule().getGroup(main.getGroupModule()
						.getGroup(tPlayer.getPlayer(), main)).getNameColor() + tempName);
				}
			});
		} catch (ClassNotFoundException | SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
