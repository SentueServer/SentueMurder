package com.Sentue.dev.SentueMurder;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

import net.minecraft.server.v1_7_R1.PacketPlayOutWorldParticles;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_7_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

public class MurderMain extends JavaPlugin{
	
	public static Plugin getPlugin(){
		return Bukkit.getPluginManager().getPlugin("Murder");
	}
	
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
	
	public static void sendFootprints(Player player, Location loc){
		PacketPlayOutWorldParticles wp = new PacketPlayOutWorldParticles("footstep", loc.getBlockX(),  loc.getBlockY(), loc.getBlockZ(), 0, 0, 0, 0, 1);
		((CraftPlayer) player).getHandle().playerConnection.sendPacket(wp);
	}
	
	public static ItemStack getPistolItem(){
		ItemStack pistol = new ItemStack(Material.CARROT_ITEM);
		ItemMeta im = pistol.getItemMeta();
		im.setDisplayName(ChatColor.RED + "Pistol");
		List<String> lore = new ArrayList<String>();
		lore.add(ChatColor.YELLOW + "Right click to fire, only shoot the murderer");
		im.setLore(lore);
		pistol.setItemMeta(im);
		return pistol;
	}
}