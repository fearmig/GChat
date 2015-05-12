package org.mig.gchat.utils.compatability;

import com.palmergames.bukkit.towny.TownyFormatter;
import com.palmergames.bukkit.towny.exceptions.TownyException;
import com.palmergames.bukkit.towny.object.Resident;
import com.palmergames.bukkit.towny.object.TownyUniverse;

import java.util.List;

import org.bukkit.entity.Player;

//Class built to interact with the Towny API
public class TownyHandler {
	
	//Resident is how towny holds all their info about a player.
	private Resident r;
	
	//constructor
	public TownyHandler(String name){
		try {
			r = TownyUniverse.getDataSource().getResident(name);
		} catch (TownyException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	//returns the nation of the resident
	public String getNation(){
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
	
	//returns if a resident is apart of a nation
	public boolean inNation(){
		return r.hasNation();
	}
	
	//returns a List of players inside the resident's nation
	public List <Player> getNationPlayers(){
		try {
			return TownyUniverse.getOnlinePlayers(r.getTown().getNation());
		} catch (TownyException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	//return the resident's town
	public String getTown(){
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
	
	//return if the resident is apart of a town
	public boolean inTown(){
		return r.hasTown();
	}
	
	//return a List of players that are apart of the residents town
	public List<Player> getTownPlayers(){
		try {
			return TownyUniverse.getOnlinePlayers(r.getTown());
		} catch (TownyException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	//return a resident's surname
	public String getSur(){
		if(r.hasSurname())
			return (r.getSurname() + " ");
		else
			return "";
	}
	
	//return the prefix for a mayor or king if they have one.
	public String getMayor(){
		if(r.isMayor() || r.isKing())
			return TownyFormatter.getNamePrefix(r);
		else
			return "";
	}
}
