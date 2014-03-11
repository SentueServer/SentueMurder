package com.Sentue.dev.SentueMurder;

import java.util.HashMap;
import org.bukkit.Location;

public class GameManager {

	private static HashMap<Integer, Location> games = new HashMap<Integer, Location>();
	private static int currentId = 1000;
	
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
	
	
}
