package org.mig.gchat.chat.filter;

import org.mig.gchat.utils.thePlayer;

public class SpamBlocker {
	
	//check for more than 4 caps in a message and if there are put entire message to lowercase
	//look to make that number configurable in the future
	public String checkCaps(String message){
		int i=0;
		int count=0;
		do{
			if(Character.isUpperCase(message.charAt(i))){
				count++;
			}
			i++;
		}while(count<5 && i < message.length());
		if(count>4){
			return message.toLowerCase();
		}
		return message;
	}
	
	//check for spamming messages
	public boolean checkSpam(thePlayer tplayer, String message){
		if(tplayer.getPrevMess().equals(message)){
			return true;
		}
		return false;
	}	
}
