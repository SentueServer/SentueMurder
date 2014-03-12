package com.Sentue.dev.SentueMurder;

import java.util.HashMap;

import org.bukkit.Location;
import org.bukkit.entity.Player;

public class GameManager {

	private static HashMap<Integer, Location> games = new HashMap<Integer, Location>();
	private static int currentId = 1000;
	private static HashMap<Integer, Player> murderer = new HashMap<Integer, Player>();
	private static HashMap<Integer, Player> armed = new HashMap<Integer, Player>();
	
	public static void registerGame(int gameId, Location loc){
		currentId++;
		if(games.containsKey(gameId)) games.remove(gameId);
		games.put(gameId, loc);
	}
	
	public static void removeGame(int gameId){
		currentId--;
		if(games.containsKey(gameId)) games.remove(gameId);
		else return;
	}
	
	public static void removeAllGames(){
		currentId = 1000;
		games.clear();
	}
	
	public static Location getGameLocation(int gameId){
		if(games.containsKey(gameId)) return games.get(gameId);
		else return null;
	}
	
	public static int getNextGameId(){
		return currentId;
	}
	
	public static Player getGameMurderer(int gameId){
		if(!murderer.containsKey(gameId)) return null;
		return murderer.get(gameId);
	}
	
	public static void setGameMurderer(int gameId, Player player){
		if(murderer.containsKey(gameId)) murderer.remove(gameId);
		murderer.put(gameId, player);
	}
	
	public static Player getGameArmed(int gameId){
		if(!armed.containsKey(gameId)) return null;
		return armed.get(gameId);
	}
	
	public static void setGameArmed(int gameId, Player player){
		if(armed.containsKey(gameId)) armed.remove(gameId);
		armed.put(gameId, player);
	}
	
}
