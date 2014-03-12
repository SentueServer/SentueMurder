package com.Sentue.dev.SentueMurder;

import java.util.HashMap;
import java.util.Iterator;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.entity.Snowball;

public class PistolManager {

	private HashMap<Player, Integer> ItemsCollected = new HashMap<Player, Integer>();
	private static HashMap<Player, Integer> ReloadTime = new HashMap<Player, Integer>();
	
	public static boolean isGunInHand(Player p){
		if(!p.getItemInHand().getType().equals(Material.CARROT)) return false;
		if(!p.getItemInHand().getItemMeta().getDisplayName().equalsIgnoreCase(ChatColor.RED + "Pistol")) return false;
		else return true;
	}
	
	public static void fireGun(Player p){
		if(!isGunInHand(p)) return;
		Projectile pro = p.launchProjectile(Snowball.class);
		pro.setVelocity(pro.getVelocity().multiply(10));
	}
	
	public static void bulletHit(Projectile pro){
		Player p1 = (Player) pro.getShooter();
		Iterator<Entity> it = pro.getNearbyEntities(1, 1, 1).iterator();
		while(it.hasNext()){
			Entity next = it.next();
			if(next.getType().equals(EntityType.PLAYER));
			((Player) next).setHealth(1.0);
			((Player) next).damage(1.0, p1);
		}
	}
	
	public static void registerReloadTime(final Player p){
		if(isReloading(p)) return;
		int taskId = Bukkit.getScheduler().scheduleSyncDelayedTask(MurderMain.getPlugin(), new Runnable() {
			
			@Override
			public void run() {
				removeReload(p);
			}
		}, 60L);
		ReloadTime.put(p, taskId);
	}
	
	public static void removeReload(Player p){
		if(ReloadTime.containsKey(p)) ReloadTime.remove(p);
	}
	
	public static boolean isReloading(Player p){
		if(ReloadTime.containsKey(p)) return true;
		else return false;
	}
	
	public static int getReloadId(Player p){
		if(!ReloadTime.containsKey(p)) return -1;
		return ReloadTime.get(p);
	}
	
	public int getItemsCollected(Player player){
		if(!ItemsCollected.containsKey(player)) return 0;
		return ItemsCollected.get(player);
	}
	
	public void collectItem(Player player){
		if(ItemsCollected.containsKey(player)){
			ItemsCollected.put(player, getItemsCollected(player) + 1);
			if(ItemsCollected.get(player) == 3) rewardPistol(player);
		}else{
			ItemsCollected.put(player, 1);
		}
	}
	
	public void resetCollects(Player player){
		if(ItemsCollected.containsKey(player)) return;
		ItemsCollected.remove(player);
	}
	
	public void removeItemCollected(Player player){
		if(ItemsCollected.containsKey(player)) ItemsCollected.put(player, getItemsCollected(player) -1);
	}
	
	public void rewardPistol(Player player){
		player.getInventory().setItem(0, MurderMain.getPistolItem());
	}
	
}
