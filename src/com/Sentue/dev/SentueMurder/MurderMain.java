package com.Sentue.dev.SentueMurder;

import java.util.Iterator;
import java.util.List;
import java.util.Random;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public class MurderMain extends JavaPlugin{
	
	public void onEnable(){
		
	}
	
	public void onDisable(){
		
	}
	
	public void startNewGame(List<Player> players){
		int gameId = GameManager.getNextGameId();
		GameManager.registerGame(gameId, new Location(players.get(1).getWorld(), 0, 0, 0));
		
		Iterator<Player> iterator = players.iterator();
		while(iterator.hasNext()){
			Player player = iterator.next();
			PlayerManager.resetPlayerInfo(player);
			PlayerManager.setPlayerInfo(player, "bystander", gameId);
		}
		
		pickRandomRoles(players, gameId);
	}
	
	public void displayScreen(Player player){
		
	}
	
	public void pickRandomRoles(List<Player> players, int gameId){
		Random r = new Random();
		int murdererId = r.nextInt(players.size());
		int armedId = r.nextInt(players.size());
		PlayerManager.setPlayerInfo(players.get(murdererId), "murderer", gameId);
		PlayerManager.setPlayerInfo(players.get(armedId), "armed", gameId);
	}
}