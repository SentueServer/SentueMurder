package com.Sentue.dev.SentueMurder;

import java.util.HashMap;
import java.util.List;

import org.bukkit.entity.Player;

public class RoleManager {

	private static HashMap<Integer, Player> murderer = new HashMap<>();
	private static HashMap<Integer, Player> armed = new HashMap<>();
	
	public static void setRole(int gameId, String role, Player player){
		switch(role){
			case "murderer": {
				if(murderer.containsValue(player)) return;
				murderer.remove(gameId);
				murderer.put(gameId, player);
			}
			case "armed": {
				if(armed.containsValue(player)) return;
				armed.remove(gameId);
				armed.put(gameId, player);
			}
		}
	}
	
	public static void resetRole(int gameId, Player player){
		if(murderer.containsValue(player)) murderer.remove(gameId);
		if(armed.containsKey(player)) armed.remove(gameId);
	}
	
	public static String getRole(Player player){
		if(murderer.containsValue(player)) return "murderer";
		if(armed.containsKey(player)) return "armed";
		else return null;
	}
	
	public static void resetRoleAll(){
		murderer.clear();
		armed.clear();
	}
	
	public static void resetGameRoleAll(int gameId){
		murderer.remove(gameId);
		armed.remove(gameId);
	}
	
	@SuppressWarnings("null")
	public static List<Player> getRoles(int gameId){
		List<Player> roles = null;
		roles.add(murderer.get(gameId));
		roles.add(armed.get(gameId));
		return roles;
	}
	
}
