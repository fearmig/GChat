package org.mig.gchat.utils.compatability;

import com.palmergames.bukkit.towny.TownyFormatter;
import com.palmergames.bukkit.towny.exceptions.TownyException;
import com.palmergames.bukkit.towny.object.Resident;
import com.palmergames.bukkit.towny.object.TownyUniverse;

import java.util.List;

import org.bukkit.entity.Player;

public class TownyHandler {
	
	private Resident r;
	
	public TownyHandler(String name){
		try {
			r = TownyUniverse.getDataSource().getResident(name);
		} catch (TownyException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
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
	
	public boolean inNation(){
		return r.hasNation();
	}
	
	public List <Player> getNationPlayers(){
		try {
			return TownyUniverse.getOnlinePlayers(r.getTown().getNation());
		} catch (TownyException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
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
	
	public boolean inTown(){
		return r.hasTown();
	}
	
	public List<Player> getTownPlayers(){
		try {
			return TownyUniverse.getOnlinePlayers(r.getTown());
		} catch (TownyException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	public String getSur(){
		if(r.hasSurname())
			return (r.getSurname() + " ");
		else
			return "";
	}
	
	public String getMayor(){
		if(r.isMayor() || r.isKing())
			return TownyFormatter.getNamePrefix(r);
		else
			return "";
	}
}
