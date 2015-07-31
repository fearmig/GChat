package org.mig.gchat.async;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.bukkit.scheduler.BukkitRunnable;
import org.mig.gchat.objects.ThePlayer;

import code.husky.Database;

public class AsyncCreatePlayer extends BukkitRunnable{
	private ThePlayer tPlayer;
	private Database db;
	
	public AsyncCreatePlayer(ThePlayer tPlayer, Database db) throws ClassNotFoundException, SQLException {
		this.tPlayer = tPlayer;
		this.db = db;
	}

	@Override
	public void run() {
		
		String uuid = tPlayer.getUuid(); 
		
		try {
			if (!this.db.checkConnection())
				this.db.openConnection();

			
			if(!db.checkConnection())
				db.openConnection();
			Statement statement = this.db.getConnection().createStatement();
			ResultSet rs = statement.executeQuery("SELECT * FROM `gchat` WHERE `UUID`='" + uuid +"';");
			
			if(!rs.first()){
				statement.close();
				Statement statementa = this.db.getConnection().createStatement();
				statementa.executeUpdate("INSERT INTO `gchat` (`UUID`) VALUES ('"
						+uuid+"');");
			}
		} catch (ClassNotFoundException | SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
