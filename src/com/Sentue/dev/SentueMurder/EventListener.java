package com.Sentue.dev.SentueMurder;

import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
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
		if((event.getAction().equals(Action.RIGHT_CLICK_AIR)) || (event.getAction().equals(Action.RIGHT_CLICK_BLOCK))){
			if(PistolManager.isGunInHand(event.getPlayer())) PistolManager.fireGun(event.getPlayer());
		}
	}
	
	@EventHandler
	public void onProjectileHit(ProjectileHitEvent event){
		if(event.getEntityType().equals(EntityType.SNOWBALL)) PistolManager.bulletHit(event.getEntity());
	}
	
	@EventHandler
	public void onPlayerDeath(PlayerDeathEvent event){
		Player dead = event.getEntity();
		Player killer = dead.getKiller();
		if(PlayerManager.getPlayerRole(dead).equalsIgnoreCase("murderer")) GameManager.endGame(PlayerManager.getPlayerGame(dead), "bystander");
		if(PlayerManager.getPlayerRole(killer).equalsIgnoreCase("murderer")) event.setDeathMessage("");
		else event.setDeathMessage(killer.getName() + " has killed an innocent bystander");
	}
	
}
