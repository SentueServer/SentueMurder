package com.Sentue.dev.SentueMurder;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

import net.minecraft.server.v1_7_R1.PacketPlayOutWorldParticles;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.craftbukkit.v1_7_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class MurderMain extends JavaPlugin{
	
	public static Plugin getPlugin(){
		return Bukkit.getPluginManager().getPlugin("Murder");
	}
	
	public void onEnable(){
		Bukkit.getPluginManager().registerEvents(new EventListener(), getPlugin());
	}
	
	public void onDisable(){
		
	}
	
	public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args){
		if(commandLabel.equalsIgnoreCase("army")) startNewGame(Arrays.asList(Bukkit.getOnlinePlayers()));
		return false;
	}
	
	public void startNewGame(List<Player> players){
		int gameId = GameManager.getNextGameId();
		GameManager.registerGame(gameId, new Location(players.get(1).getWorld(), 0, 0, 0));
		
		PotionEffect Blind = new PotionEffect(PotionEffectType.BLINDNESS, 100, 1);
		PotionEffect Night = new PotionEffect(PotionEffectType.NIGHT_VISION, 100, 1);
		
		Iterator<Player> iterator = players.iterator();
		while(iterator.hasNext()){
			Player player = iterator.next();
			PlayerManager.resetPlayerInfo(player);
			PlayerManager.setPlayerInfo(player, "bystander", gameId);
			player.addPotionEffect(Blind);
			player.addPotionEffect(Night);
			sortInventory(player);
		}
		
		pickRandomRoles(players, gameId);
	}
	
	public void sortInventory(Player player){
		player.getInventory().clear();
		if(PlayerManager.getPlayerRole(player).equalsIgnoreCase("armed")) player.getInventory().setItem(0, PistolManager.getPistolItem());
		if(PlayerManager.getPlayerRole(player).equalsIgnoreCase("murderer")) player.getInventory().setItem(0, PistolManager.getPistolItem());
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
		PacketPlayOutWorldParticles wp = new PacketPlayOutWorldParticles("footstep", loc.getBlockX(),  loc.getBlockY() + 1, loc.getBlockZ(), 0, 0, 0, 0, 1);
		((CraftPlayer) player).getHandle().playerConnection.sendPacket(wp);
	}
}