package com.Sentue.dev.SentueMurder;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;

public class EventListener implements Listener{

	@EventHandler
	public void onPlayerMove(PlayerMoveEvent event){
		if(!PlayerManager.hasPlayerGame(event.getPlayer())) return;
		if(PlayerManager.getPlayerRole(event.getPlayer()).equalsIgnoreCase("murderer")) return;
		MurderMain.sendFootprints(GameManager.getGameMurderer(PlayerManager.getPlayerGame(event.getPlayer())), event.getPlayer().getLocation());
	}
	
	@EventHandler
	public void onPlayerInteract(PlayerInteractEvent event){
		
	}
	
}
