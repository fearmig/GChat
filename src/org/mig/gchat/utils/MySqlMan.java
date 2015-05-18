package org.mig.gchat.utils;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.bukkit.entity.Player;
import org.mig.gchat.chat.filter.BadWordHandler;
import org.mig.gchat.groups.Groups;

import code.husky.mysql.MySQL;

//Handles all interaction with the MySql database
public class MySqlMan {
	private final GChat main;
	private MySQL db;
	private BadWordHandler bw = new BadWordHandler();
	
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
		statement.executeUpdate("CREATE TABLE IF NOT EXISTS `gchat` (`UUID` varchar(50), `Name` varchar(32), `Group` varchar(32), `MediaLink` varchar(100))");
		statement.close();
		Statement statementb = this.db.getConnection().createStatement();
		statementb.executeUpdate("CREATE TABLE IF NOT EXISTS `badwords` (`BlockedWord` varchar(100), `Tier` varchar(10))");
		statementb.close();
		Statement statementc = this.db.getConnection().createStatement();
		statementc.executeUpdate("CREATE TABLE IF NOT EXISTS `names` (`UUID` varchar(50), `Name` varchar(32))");
		statementc.close();
	}
	
	//close the connection to the database
	public void closeDB() throws SQLException{
		this.db.closeConnection();
	}
	
	//run a query to retrive the information of a player
	public ResultSet getPlayerAttr(Player p) throws SQLException, ClassNotFoundException{
		String uuid = "" + p.getUniqueId();
		
		if(!this.db.checkConnection())
			this.db.openConnection();
		Statement statement = this.db.getConnection().createStatement();
		ResultSet rs = statement.executeQuery("SELECT * FROM `gchat` WHERE `UUID`='" + uuid +"';");
		
		if(!rs.first()){
			statement.close();
			return null;
		}
		return rs;
		
	}
	
	//set a players name
	public void setName(String uuid, String name) throws ClassNotFoundException, SQLException{
		if(!this.db.checkConnection())
			this.db.openConnection();
		
		Statement statement = this.db.getConnection().createStatement();
		
		//check to see if the player already has a changed name
		Statement statementA = this.db.getConnection().createStatement();
		ResultSet rsA = statementA.executeQuery("SELECT Name FROM `names` WHERE `UUID`='" + uuid +"';");
		
		//if a player exists update their entry
		if(rsA.first()){
			statement.executeUpdate("UPDATE `names` SET `Name`='"+name+"' WHERE `UUID`='" + uuid +"';");
		}
		//if a player didn't already exist in this table create their entry
		else{
			statement.executeUpdate("INSERT INTO `names` (`UUID`,`Name`) VALUES ('"
					+uuid+"','"+name+"');");
		}
		statementA.close();
		statement.close();
	}
	
	//get the name of a player in the database by searching with their UUID
	public String getName(Player p) throws ClassNotFoundException, SQLException{
		String uuid = "" + p.getUniqueId(); 
		String tempName;
		
		if(!this.db.checkConnection())
			this.db.openConnection();
		
		//check if the user has a forced rename
		Statement statementA = this.db.getConnection().createStatement();
		ResultSet rsA = statementA.executeQuery("SELECT Name FROM `names` WHERE `UUID`='" + uuid +"';");
		
		if(rsA.first()){
			tempName = rsA.getString("Name");
			statementA.close();
			return tempName;
		}
		
		Statement statementB = this.db.getConnection().createStatement();
		ResultSet rsB = statementB.executeQuery("SELECT Name FROM `gchat` WHERE `UUID`='" + uuid +"';");
		
		if(!rsB.first()){
			statementB.close();
			return null;
		}
		tempName = rsB.getString("Name");
		statementB.close();
		return tempName;
	}
	
	//update the player's information by gathering info and sending it to the database
	public void updatePlayer(Player p) throws ClassNotFoundException, SQLException{
		
		String group = "default";
		String mediaLink = "";
		String uuid = "" + p.getUniqueId(); 
		
		Groups g = new Groups();
		group = g.getGroup(p);
		
		if(!this.db.checkConnection())
			this.db.openConnection();
		
		Statement statement = this.db.getConnection().createStatement();
		Statement statementa = this.db.getConnection().createStatement();
		
		if(getPlayerAttr(p)!=null){
			statement.executeUpdate("UPDATE `gchat` SET `Name`='"+p.getName()+"' WHERE `UUID`='" + uuid +"';");
			statementa.executeUpdate("UPDATE `gchat` SET `Group`='"+group+"' WHERE `UUID`='" + uuid +"';");			
		}
		else{
			statement.executeUpdate("INSERT INTO `gchat` (`UUID`,`Name`,`Group`,`MediaLink`) VALUES ('"
					+uuid+"','"+p.getName()+"','"+group+"','"+mediaLink+"');");
		}
		
		statement.close();
		statementa.close();
	}
	
	//update a players media link in the database
	public void updateMediaLink(UUID u, String l) throws ClassNotFoundException, SQLException{
		String uuid = "" + u;
		if(!this.db.checkConnection())
			this.db.openConnection();
		Statement statement = this.db.getConnection().createStatement();
		statement.executeUpdate("UPDATE `gchat` SET `MediaLink`='"+l+"' WHERE `UUID`='" + uuid +"';");
		statement.close();
	}
	
	//retrieve all the bad words in the table
	public List<String> retrieveBadWords() throws ClassNotFoundException, SQLException{
		List<String> badWords = new ArrayList<String>();
		if(!this.db.checkConnection())
			this.db.openConnection();
		Statement statement = this.db.getConnection().createStatement();
		ResultSet rs = statement.executeQuery("SELECT * FROM `badwords`;");
		if(rs!=null){
			if(bw.getBadWordList()!=null)
				bw.clearList();
			while(rs.next()){
				BadWordHandler.badWords.add(rs.getString("BlockedWord"));
				//badWordHandler.wordTier.add(rs.getString("Tier"));
			}
			statement.close();
			return badWords;
		}
		else{
			statement.close();
			return badWords;
		}
	}
	
	//add a word to the badword table
	public void addWord(String w, String t) throws ClassNotFoundException, SQLException{
		if(!this.db.checkConnection())
			this.db.openConnection();
		Statement statementa = this.db.getConnection().createStatement();
		Statement statement = this.db.getConnection().createStatement();
		
		ResultSet rs = statementa.executeQuery("SELECT * FROM `badwords` WHERE `BlockedWord`='" + w +"';");
		if(!rs.first()){
			statement.executeUpdate("INSERT INTO `badwords` (`BlockedWord`,`Tier`) VALUES ('"
					+w+"','"+t+"');");
			statement.close();
		}
		else{
			statement.close();
		}
	}
	
	//remove a word from the badword table
	public void removeWord(String w) throws ClassNotFoundException, SQLException{
		if(!this.db.checkConnection())
			this.db.openConnection();
		Statement statement = this.db.getConnection().createStatement();
		if(bw.isBadWord(w)){
			statement.executeUpdate("DELETE FROM `badwords` WHERE `BlockedWord` ='"+w+"' ");
			statement.close();
		}
	}
	
}
