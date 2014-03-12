package com.Sentue.dev.SentueMurder;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Fireball;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class KnifeManager {

	public static boolean isKnifeInHand(Player p){
		if(!p.getItemInHand().getType().equals(Material.STONE_SWORD)) return false;
		if(!p.getItemInHand().getItemMeta().getDisplayName().equalsIgnoreCase(ChatColor.RED + "Knife")) return false;
		else return true;
	}
	
	public void stabEvent(Player stabber, Player stabbed){
		if(!GameManager.getGameMurderer(PlayerManager.getPlayerGame(stabber)).getName().equalsIgnoreCase(stabber.getName())) return;
		if(PlayerManager.getPlayerGame(stabber) != PlayerManager.getPlayerGame(stabbed)) return;
		if(!isKnifeInHand(stabber)) return;
		stabbed.setHealth(1.0);
		stabbed.damage(1.0, stabber);
	}
	
	public void readyKnife(Player player){
		//Fill in code
	}
	
	public void throwKnife(Player player){
		if(!PlayerManager.getPlayerRole(player).equalsIgnoreCase("murderer")) return;
		if(!isKnifeInHand(player)) return;
		if(player.getExp() == 0) return;
		Projectile pro = player.launchProjectile(Fireball.class);
		pro.setVelocity(pro.getVelocity().multiply(10));
	}
	
	public static ItemStack getKnifeItem(){
		ItemStack knife = new ItemStack(Material.STONE_SWORD);
		ItemMeta im = knife.getItemMeta();
		im.setDisplayName(ChatColor.RED + "Knife");
		List<String> lore = new ArrayList<String>();
		lore.add(ChatColor.YELLOW + "Punch to murder, right click to throw");
		im.setLore(lore);
		knife.setItemMeta(im);
		return knife;
	}
	
}
