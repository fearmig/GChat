package org.mig.townypatch;

import com.palmergames.bukkit.towny.exceptions.TownyException;
import com.palmergames.bukkit.towny.object.Resident;
import com.palmergames.bukkit.towny.object.TownyUniverse;

import java.util.List;

public class thePatch {
	
	static List<Resident> playerList;
	TownyUniverse tu = new TownyUniverse(tPatch.plugin.getTowny());
	
	public void compileList(){
		playerList = tu.getActiveResidents();
	}
	
	public Resident getResident(String name){
		for(Resident r: playerList){
			if(name.equalsIgnoreCase(r.getName()))
				return r;
		}
		return null;
	}
	
	public String getNation(Resident r) throws TownyException{
		if(r.hasNation()){
			return r.getTown().getNation().getName();
		}
		else{
			return "none";
		}
	}
	public String getTown(Resident r) throws TownyException{
		if(r.hasTown()){
			return r.getTown().getName();
		}
		else{
			return "none";
		}
	}
	public String getSur(Resident r){
		if(r.hasSurname())
			return (r.getSurname() + " ");
		else
			return "";
	}
	public String getMayor(Resident r){
		if(r.isMayor() || r.isKing())
			return r.getTitle();
		else
			return "";
	}
	
	public String getChatChan(){
		return null;
	}
	
}
