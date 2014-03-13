package com.Sentue.dev.SentueMurder;

import java.util.HashMap;

import org.bukkit.entity.Player;

public class PlayerManager {

	private static HashMap<Player, String> Role = new HashMap<Player, String>();
	private static HashMap<Player, Integer> Game = new HashMap<Player, Integer>();
	
	public static void setPlayerInfo(Player player, String role, int game){
		resetPlayerInfo(player);
		Role.put(player, role);
		Game.put(player, game);
		gameHandler(player, role, game);
	}
	
	public static void resetPlayerInfo(Player player){
		if(Role.containsKey(player)) Role.remove(player);
		if(Game.containsKey(player)) Game.remove(player);
		gameHandler(player, "bystander", 0);
	}
	
	public static String getPlayerRole(Player player){
		if(Role.containsKey(player)) return Role.get(player);
		else return null;
	}
	
	public static int getPlayerGame(Player player){
		if(Game.containsKey(player)) return Game.get(player);
		else return 0;
	}
	
	public static void setPlayerRole(Player player, String role){
		if(Role.containsKey(player)) resetPlayerRole(player);
		Role.put(player, role);
		gameHandler(player, getPlayerRole(player), getPlayerGame(player));
	}
	
	public static void setPlayerGame(Player player, int game){
		if(Game.containsKey(player)) resetPlayerGame(player);
		Game.put(player, game);
		gameHandler(player, getPlayerRole(player), getPlayerGame(player));
	}
	
	public static void resetPlayerRole(Player player){
		if(Role.containsKey(player)) Role.remove(player);
		else return;
		gameHandler(player, getPlayerRole(player), getPlayerGame(player));
	}
	
	public static void resetPlayerGame(Player player){
		if(Game.containsKey(player)) Game.remove(player);
		else return;
		gameHandler(player, getPlayerRole(player), getPlayerGame(player));
	}
	
	public static boolean hasPlayerRole(Player player){
		if(Role.containsKey(player)) return true;
		else return false;
	}
	
	public static boolean hasPlayerGame(Player player){
		if(Game.containsKey(player)) return true;
		else return false;
	}
	
	public static void gameHandler(Player player, String role, int gameId){
		switch(role){
		case "murderer": GameManager.setGameMurderer(gameId, player);
		case "armed": GameManager.setGameArmed(gameId, player);
		case "bystander": return;
		}
	}
	
}
