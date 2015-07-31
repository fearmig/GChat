package org.mig.gchat.theplayer;

import org.mig.gchat.GChat;
import org.mig.gchat.objects.ThePlayer;
import org.mig.gchat.utils.ScoreboardBuilder;

public class PlayerFormatter {
	private GChat main;
	private ThePlayer tp;
	
	public PlayerFormatter(ThePlayer theplayer, GChat main){
		this.main = main;
		this.tp = theplayer;
	}
	
	public void setOverHeadDisplay() {
		// Set display for Player Tab List if in config the option is true
		if (main.getConfig().getBoolean("OverHeadFormating")) {
			tp.getPlayer().setDisplayName(tp.getName());
			ScoreboardBuilder sb = new ScoreboardBuilder(main);
			sb.joinTeam(tp);
			tp.getPlayer().setScoreboard(sb.getBoard());
			
		}
	}
	/* Currently off to test if joining team changes name in tab
	public void setTabList(){
		//Set display for Player Tab List if in config the option is true
		if(main.getConfig().getBoolean("TabPlayerList")){
			tempName = tp.getName();
			if(tempName.length()>=15)
				tempName = tp.getName().substring(0,14);
			tp.getPlayer().setPlayerListName(tp.getNameColor()+tempName);
		}
	}*/
}