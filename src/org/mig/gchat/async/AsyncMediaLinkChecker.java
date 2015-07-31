package org.mig.gchat.async;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.bukkit.scheduler.BukkitRunnable;
import org.mig.gchat.objects.ThePlayer;

import code.husky.Database;

public class AsyncMediaLinkChecker extends BukkitRunnable{

	private ThePlayer tPlayer;
	private Database db;
	
	public AsyncMediaLinkChecker(ThePlayer tPlayer, Database db) throws ClassNotFoundException, SQLException {
		this.tPlayer = tPlayer;
		this.db = db;
	}

	@Override
	public void run() {
		
		String uuid = tPlayer.getUuid(); 
		String ml;
		
		try {
			if (!this.db.checkConnection())
				this.db.openConnection();

			// check if the user has a forced rename
			Statement statementA = this.db.getConnection().createStatement();
			ResultSet rsA = statementA
					.executeQuery("SELECT MediaLink FROM `gchat` WHERE `UUID`='"
							+ uuid + "';");

			if (rsA.first()) {
				ml = rsA.getString("MediaLink");
				statementA.close();
				if (ml != null &&ml.length() > 1)
					tPlayer.setMediaLink(ml);

				rsA.close();
			}
		} catch (ClassNotFoundException | SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
