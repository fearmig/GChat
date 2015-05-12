package org.mig.gchat.utils;

import java.util.ArrayList;
import java.util.UUID;

//This class only holds the players that are in minechat mode and manages that mode
public class MinechatCompatability {
	static ArrayList<UUID> mChatList = new ArrayList<UUID>();
	
	//add a player to the minechat mode on list
	public static void mineChatOn(UUID p){
		mChatList.add(p);
	}
	
	//remove a player from the minechat mode list
	public static void mineChatOff(UUID p){
		mChatList.remove(p);
	}
	
	//return a players status meaning if they are in minechat mode
	public static boolean mineChatStatus(UUID p){
		if(mChatList.contains(p)){
			return true;
		}
		else{
			return false;
		}
	}
}
