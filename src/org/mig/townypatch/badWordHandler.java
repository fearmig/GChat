package org.mig.townypatch;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class badWordHandler {
	public static List <String> badWords = new ArrayList<String>();
	public static List <String> wordTier = new ArrayList<String>();
	
	public int findBadWord(String word){
		for(int i = 0 ; i < badWords.size(); i++){
			if(word.equals(badWords.get(i))){
				return i;
			}
		}
		return -1;
	}
	
	public void fillList(){
		if(tPatch.plugin.getConfig().getBoolean("MySql")){
			try {
				tPatch.plugin.mysql.retrieveBadWords();
			} catch (ClassNotFoundException | SQLException e) {
				e.printStackTrace();
			}
		}
		List <String> temp;
		temp = tPatch.plugin.getConfig().getStringList("badWordsTier1");
		for(String s: temp){
			badWords.add(s);
			wordTier.add("1");
		}
		temp = tPatch.plugin.getConfig().getStringList("badWordsTier2");
		for(String s: temp){
			badWords.add(s);
			wordTier.add("2");
		}
	}
	
	public String testMessage(String m){
		//gChat.plugin.reloadConfig();
		m = m.toLowerCase();
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
