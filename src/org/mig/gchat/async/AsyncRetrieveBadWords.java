package org.mig.gchat.async;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.bukkit.scheduler.BukkitRunnable;
import org.mig.gchat.GChat;
import org.mig.gchat.chat.filter.BadWordHandler;

import code.husky.Database;

public class AsyncRetrieveBadWords extends BukkitRunnable{

	private Database db;
	private GChat main;
	
	public AsyncRetrieveBadWords(GChat main) throws ClassNotFoundException, SQLException{
		this.db = main.mysql.getDatabase();
		this.main = main;
	}
	
	@Override
	public void run() {

		BadWordHandler bw = new BadWordHandler(main);

		try {
			if (!this.db.checkConnection())
				this.db.openConnection();

			Statement statement = this.db.getConnection().createStatement();
			ResultSet rs = statement.executeQuery("SELECT * FROM `badwords`;");

			if (rs != null) {
				if (bw.getBadWordList() != null)
					bw.clearList();
				while (rs.next()) {
					BadWordHandler.badWords.add(rs.getString("BlockedWord"));
					// badWordHandler.wordTier.add(rs.getString("Tier"));
				}
				statement.close();
			} else {
				statement.close();
			}
		} catch (ClassNotFoundException | SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
