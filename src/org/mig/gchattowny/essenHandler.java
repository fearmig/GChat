package org.mig.gchattowny;

import java.util.List;

import org.bukkit.Bukkit;
import com.earth2me.essentials.Essentials;

public class essenHandler {
	Essentials ess = (Essentials) Bukkit.getServer().getPluginManager().getPlugin("Essentials");
	public List <String> getIgnored(thePlayer b){
		
		return ess.getUser(b.getPlayer())._getIgnoredPlayers();
		
	}
}
