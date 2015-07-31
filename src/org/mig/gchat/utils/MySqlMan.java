package org.mig.gchat.utils;

import java.sql.SQLException;
import java.sql.Statement;

import org.mig.gchat.GChat;

import code.husky.Database;
import code.husky.mysql.MySQL;

//Handles all interaction with the MySql database
public class MySqlMan {
	private final GChat main;
	private MySQL db;
	
	//constructor
	public MySqlMan(GChat g){
		this.main = g;
		g.getNamesConfig();
	}
	
	//create a table if the the table does not exist already
	public void setupDB() throws SQLException, ClassNotFoundException{
		this.db = new MySQL(this.main,main.getConfig().getString("MySqlHost"),main.getConfig().getString("MySqlPort")
				,main.getConfig().getString("MySqlDatabase"),main.getConfig().getString("MySqlUsername")
				,main.getConfig().getString("MySqlPassword"));
		this.db.openConnection();
		main.getLogger().info("Connected to MySQL database");
		Statement statement = this.db.getConnection().createStatement();
		statement.executeUpdate("CREATE TABLE IF NOT EXISTS `gchat` (`UUID` varchar(50)"
				+ ", `Name` varchar(32), `MediaLink` varchar(100))");
		statement.close();
		Statement statementb = this.db.getConnection().createStatement();
		statementb.executeUpdate("CREATE TABLE IF NOT EXISTS `badwords` (`BlockedWord` varchar(100)"
				+ ", `Tier` varchar(10))");
		statementb.close();
		
	}
	
	//close the connection to the database
	public void closeDB() throws SQLException{
		db.closeConnection();
	}
	
	//return the database
	public Database getDatabase() throws ClassNotFoundException, SQLException{
		if(!this.db.checkConnection())
			this.db.openConnection();
		
		return this.db;
	}
	
}
