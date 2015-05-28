package org.mig.gchat.chat;

import org.mig.gchat.utils.GChat;
import org.mig.gchat.utils.ThePlayer;

//this class will change the name in the tab list and also above the player's head

public class NameDisplayController {
	private GChat main;
	private ThePlayer tp;
	private String tempName;
	
	public NameDisplayController(ThePlayer theplayer){
		main = GChat.getMain();
		tp = theplayer;
	}
	
	public void setTabList(){
		//Set display for Player Tab List if in config the option is true
		if(main.getConfig().getBoolean("TabPlayerList")){
			tempName = tp.getName();
			if(tempName.length()>=15)
				tempName = tp.getName().substring(0,14);
			tp.getPlayer().setPlayerListName(tp.getNameColor()+tempName);
		}
	}
}
