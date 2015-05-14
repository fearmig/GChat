package org.mig.gchat.chat.filter;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.mig.gchat.utils.GChat;


//This class handles testing a message to see if it may contain a word on
//the prohibited list.
public class BadWordHandler {
	
	//To be implemented make badword an object with attributes of wordTier and wordString
	
	public static List <String> badWords = new ArrayList<String>();
	private static List <String> wordTier = new ArrayList<String>();
	
	//get an object of the main class
	private GChat main = GChat.getMain();
	
	//test to see if the word is on the forbidden list and if so return true
	public boolean isBadWord(String word){
		for(int i = 0 ; i < badWords.size(); i++){
			if(word.equals(badWords.get(i))){
				return true;
			}
		}
		return false;
	}
	
	//return a List of all the forbidden list.
	public List <String> getBadWordList(){
		return badWords;
	}
	
	//clear internal kept list of forbidden words and their tiers
	public void clearList(){
		badWords.clear();
		wordTier.clear();
	}
	
	//add a word to the forbidden list
	public void addBadWord(String w){
		badWords.add(w);
		if(main.getConfig().getBoolean("MySql")){
			try {
				main.mysql.addWord(w, "1");
			} catch (ClassNotFoundException | SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		else{
			main.getConfig().set("badWords", badWords);
			main.saveConfig();
		}
	}
	
	//remove a word from the forbidden list
	public void removeBadWord(String w){
		badWords.remove(w);
		if(main.getConfig().getBoolean("MySql")){
			try {
				main.mysql.removeWord(w);
			} catch (ClassNotFoundException | SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		else{
			main.getConfig().set("badWords",badWords);
			main.saveConfig();
		}
	}
	
	//build the forbidden word list
	public void fillList(){
		if(main.getConfig().getBoolean("MySql")){
			try {
				badWords = main.mysql.retrieveBadWords();
			} catch (ClassNotFoundException | SQLException e) {
				e.printStackTrace();
			}
		}
		else{
			List <String> temp;
			temp = main.getConfig().getStringList("badWordsTier1");
			for(String s: temp){
				badWords.add(s);
				wordTier.add("1");
			}
			temp = main.getConfig().getStringList("badWordsTier2");
			for(String s: temp){
				badWords.add(s);
				wordTier.add("2");
			}
		}
	}
	
	//take words listed in the YML and import them into MySql tables.
	public boolean convertToSql(){
		for(int i = 0; i < badWords.size(); i++){
			main.getLogger().info("Added " + badWords.get(i) + "to MySQL");
			try {
				main.mysql.addWord(badWords.get(i),BadWordHandler.wordTier.get(i));
			} catch (ClassNotFoundException | SQLException e) {
				e.printStackTrace();
				return false;
			}
		}
		return true;
	}
	
	//Test a message to see if it contains a forbidden word, if so it will 
	//return the word that is forbidden
	public String testMessage(String m){
		//make the message lowercase and remove special characters.
		m = m.toLowerCase();
		m = m.replaceAll("[^\\dA-Za-z ]", "");
		if(badWords!=null){
			
			for(int i = 0 ; i < badWords.size(); i++){
				//test _word
				String tWord1 = badWords.get(i);
				String tWord2 = (" " + badWords.get(i) + " ");
				String tWord3 = (" " + badWords.get(i));
				String tWord4 = (badWords.get(i) + " ");
				
				if(m.contains((CharSequence)tWord1)){
					int index;
					if(m.contains((CharSequence)tWord2)){
						
						return tWord1;
					}
					else if(m.contains((CharSequence)tWord3)){
						try{
							index = m.indexOf(tWord3) + tWord3.length();
							m.charAt(index);
						}catch(IndexOutOfBoundsException e){
							return tWord1;
						}
					}
					else if(m.contains((CharSequence)tWord4)){
						try{
							index = m.indexOf(tWord4)-1;
							m.charAt(index);
						}catch(IndexOutOfBoundsException e){
							return tWord1;
						}
					}
					else if(m.length()==tWord1.length()){
						return tWord1;
					}
				}
			}
		}
		return null;
			
	}
}
