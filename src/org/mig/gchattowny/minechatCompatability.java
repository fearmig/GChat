package org.mig.gchattowny;

import java.util.ArrayList;
import java.util.UUID;

public class minechatCompatability {
	static ArrayList<UUID> mChatList = new ArrayList<UUID>();
	
	public static void mineChatOn(UUID p){
		mChatList.add(p);
	}
	
	public static void mineChatOff(UUID p){
		mChatList.remove(p);
	}
	
	public static boolean mineChatStatus(UUID p){
		if(mChatList.contains(p)){
			return true;
		}
		else{
			return false;
		}
	}
}
