package org.mig.townypatch;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.UUID;

import org.bukkit.entity.Player;

import code.husky.mysql.MySQL;

public class mySqlMan {
	private final tPatch main;
	private MySQL db;
	
	public mySqlMan(tPatch g){
		this.main = g;
	}
	
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
	}
	
	public void closeDB() throws SQLException{
		this.db.closeConnection();
	}
	
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
	
	public String getName(Player p) throws ClassNotFoundException, SQLException{
		String uuid = "" + p.getUniqueId(); 
		if(!this.db.checkConnection())
			this.db.openConnection();
		Statement statement = this.db.getConnection().createStatement();
		ResultSet rs = statement.executeQuery("SELECT Name FROM `gchat` WHERE `UUID`='" + uuid +"';");
		if(!rs.first()){
			statement.close();
			return null;
		}
		statement.close();
		return rs.getString("Name");
	}
	
	public String getGroup(Player p) throws ClassNotFoundException, SQLException{
		String uuid = "" + p.getUniqueId(); 
		if(!this.db.checkConnection())
			this.db.openConnection();
		Statement statement = this.db.getConnection().createStatement();
		ResultSet rs = statement.executeQuery("SELECT Group FROM `gchat` WHERE `UUID`='" + uuid +"';");
		if(!rs.first()){
			statement.close();
			return null;
		}
		statement.close();
		return rs.getString("Group");
	}
	
	public void updatePlayer(Player p) throws ClassNotFoundException, SQLException{
		
		String group = "default";
		String mediaLink = "";
		String uuid = "" + p.getUniqueId(); 
		
		groupHandler gh = new groupHandler();
		group = gh.getGroup(p);
		
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
	
	public void updateMediaLink(UUID u, String l) throws ClassNotFoundException, SQLException{
		String uuid = "" + u;
		if(!this.db.checkConnection())
			this.db.openConnection();
		Statement statement = this.db.getConnection().createStatement();
		statement.executeUpdate("UPDATE `gchat` SET `MediaLink`='"+l+"' WHERE `UUID`='" + uuid +"';");
		statement.close();
	}
	
	public void retrieveBadWords() throws ClassNotFoundException, SQLException{
		if(!this.db.checkConnection())
			this.db.openConnection();
		Statement statement = this.db.getConnection().createStatement();
		ResultSet rs = statement.executeQuery("SELECT * FROM `badwords`;");
		if(rs!=null){
			if(badWordHandler.badWords!=null)
				badWordHandler.badWords.clear();
			if(badWordHandler.wordTier!=null)
				badWordHandler.wordTier.clear();
			while(rs.next()){
				badWordHandler.badWords.add(rs.getString("BlockedWord"));
				badWordHandler.wordTier.add(rs.getString("Tier"));
			}
		}
	}
	
	public void addWord(String w, String t) throws ClassNotFoundException, SQLException{
		if(!this.db.checkConnection())
			this.db.openConnection();
		Statement statementa = this.db.getConnection().createStatement();
		Statement statement = this.db.getConnection().createStatement();
		
		ResultSet rs = statementa.executeQuery("SELECT * FROM `badwords` WHERE `BlockedWord`='" + w +"';");
		if(!rs.first()){
			main.getLogger().info("Hit1");
			statement.executeUpdate("INSERT INTO `badwords` (`BlockedWord`,`Tier`) VALUES ('"
					+w+"','"+t+"');");
			statement.close();
		}
		else{
			statement.close();
		}
	}
	
	public void removeWord(String w) throws ClassNotFoundException, SQLException{
		if(!this.db.checkConnection())
			this.db.openConnection();
		Statement statement = this.db.getConnection().createStatement();
		if(badWordHandler.badWords.contains(w)){
			statement.executeUpdate("DELETE FROM `badwords` WHERE `BlockedWord` ='"+w+"' ");
			statement.close();
		}
	}
}
