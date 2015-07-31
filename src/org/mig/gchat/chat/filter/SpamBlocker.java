package org.mig.gchat.chat.filter;

import org.mig.gchat.GChat;
import org.mig.gchat.objects.ThePlayer;

//The sole purpose of this class is to check for what is considered spam such
//as the use of too many capital letters or repeating a message over and over.
public class SpamBlocker {
	
	private GChat main;
	
	public SpamBlocker(GChat main){
		this.main = main;
	}
	
	//check for more than 4 caps in a message and if there are put entire message to lowercase
	//look to make that number configurable in the future
	public String checkCaps(String message){
		int i=0;
		int count=0;
		int capsAllowed = this.main.getConfig().getInt("CapsAllowed");
		do{
			if(Character.isUpperCase(message.charAt(i))){
				count++;
			}
			i++;
		}while(count < capsAllowed+1 && i < message.length());
		if(count > capsAllowed){
			return message.toLowerCase();
		}
		return message;
	}
	
	//check for spamming messages
	public boolean checkSpam(ThePlayer tplayer, String message){
		if(tplayer.getPreviousMessage().equalsIgnoreCase(message)){
			return true;
		}
		return false;
	}	
}
