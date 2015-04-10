package org.mig.townypatch;

import com.palmergames.bukkit.towny.TownyFormatter;
import com.palmergames.bukkit.towny.exceptions.TownyException;
import com.palmergames.bukkit.towny.object.Resident;
import com.palmergames.bukkit.towny.object.TownyUniverse;

import java.util.List;

public class thePatch {
	static List<Resident> playerList;
	
	public static void compileList(){
		playerList = TownyUniverse.getDataSource().getResidents();
	}
	
	public Resident getResident(String name){
		for(Resident r: playerList){
			if(name.equalsIgnoreCase(r.getName()))
				return r;
		}
		return null;
	}
	
	public String getNation(Resident r){
		if(r.hasNation()){
			try {
				return r.getTown().getNation().getName();
			} catch (TownyException e) {
				return "none";
			}
		}
		else{
			return "none";
		}
	}
	public String getTown(Resident r){
		if(r.hasTown()){
			try {
				return r.getTown().getName();
			} catch (TownyException e) {
				return "none";
			}
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
			return TownyFormatter.getNamePrefix(r);
		else
			return "";
	}
	
	public String getChatChan(){
		return null;
	}
	
}
