package com.Sentue.dev.SentueMurder;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

import net.minecraft.server.v1_7_R1.ChunkCoordinates;
import net.minecraft.server.v1_7_R1.EntityChicken;
import net.minecraft.server.v1_7_R1.EntityHorse;
import net.minecraft.server.v1_7_R1.EntityHuman;
import net.minecraft.server.v1_7_R1.EntityLiving;
import net.minecraft.server.v1_7_R1.EntityPlayer;
import net.minecraft.server.v1_7_R1.EntityWitherSkull;
import net.minecraft.server.v1_7_R1.IChatBaseComponent;
import net.minecraft.server.v1_7_R1.PacketPlayOutAnimation;
import net.minecraft.server.v1_7_R1.PacketPlayOutAttachEntity;
import net.minecraft.server.v1_7_R1.PacketPlayOutNamedEntitySpawn;
import net.minecraft.server.v1_7_R1.PacketPlayOutSpawnEntityLiving;
import net.minecraft.util.com.mojang.authlib.GameProfile;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.craftbukkit.v1_7_R1.entity.CraftEntity;
import org.bukkit.craftbukkit.v1_7_R1.entity.CraftHumanEntity;
import org.bukkit.craftbukkit.v1_7_R1.entity.CraftLivingEntity;
import org.bukkit.craftbukkit.v1_7_R1.entity.CraftPlayer;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Ageable;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.server.MapInitializeEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.map.MapCanvas;
import org.bukkit.map.MapRenderer;
import org.bukkit.map.MapView;
import org.bukkit.map.MinecraftFont;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.ScoreboardManager;
import org.bukkit.util.Vector;

public class MurderMain extends JavaPlugin implements Listener{
	
	private ItemStack gun = new ItemStack(Material.IRON_HOE);
	private int players = 0;
	private boolean canFire = true;
	private Player murdererPlayer;
	private Player armedPlayer;
	private HashMap<Player, Integer> pickedUp = new HashMap<Player, Integer>();
	private HashMap<Player, String> foot = new HashMap<Player, String>();
	
	/**
	 * When the plugin is enabled this will be called and it will register an event listener
	 * */
	@Override
	public void onEnable(){
		Bukkit.getPluginManager().registerEvents(this, this);
	}
	
	/**
	 * When called this method will give the specified player the gun item
	 * @param Player The player who will receive the item
	 * */
	public void giveItem(Player player){
		gun.setType(Material.IRON_HOE);
		ItemMeta im = gun.getItemMeta();
		im.setDisplayName("Gun");
		gun.setItemMeta(im);
		player.getInventory().addItem(gun);
	}
	
	public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args){
		if(commandLabel.equalsIgnoreCase("Packet")){
			Player p = (Player) sender;
			GameProfile gp = new GameProfile("Identifier", args[0]);
			EntityHuman cp = new EntityHuman(((CraftPlayer) p).getHandle().world, gp) {
			
				@Override
				public void sendMessage(IChatBaseComponent arg0) {
					// TODO Auto-generated method stub
				}
			
				@Override
				public ChunkCoordinates getChunkCoordinates() {
					// TODO Auto-generated method stub
					return null;
				}
			
				@Override
				public boolean a(int arg0, String arg1) {
					// TODO Auto-generated method stub
					return false;
				}
				};
			cp.setPosition(p.getLocation().getX(), p.getLocation().getY(), p.getLocation().getZ());
			if(args[1].equalsIgnoreCase("TRUE")) cp.setSneaking(true);
			else cp.setSneaking(false);
			CraftHumanEntity player = cp.getBukkitEntity();
			player.setItemInHand(p.getItemInHand());
			((CraftPlayer) p).getHandle().playerConnection.sendPacket(new PacketPlayOutNamedEntitySpawn(cp));
			((CraftPlayer) p).getHandle().playerConnection.sendPacket(new PacketPlayOutAnimation(cp, 1));
		}
		if(commandLabel.equalsIgnoreCase("hide")){
			Player p = (Player) sender;
			p.setDisplayName(args[0]);
			EntityHuman eh = ((CraftPlayer) p).getHandle();
			((CraftPlayer) p).getHandle().playerConnection.sendPacket(new PacketPlayOutNamedEntitySpawn(eh));
		}
		if(commandLabel.equalsIgnoreCase("army")){
			for(int x = 10; x > 0; x = x-1){
				for(int y = 10; y > 0; y = y-1){
					Player p = (Player) sender;
					GameProfile gp = new GameProfile("Identifier", args[0]);
					EntityHuman cp = new EntityHuman(((CraftPlayer) p).getHandle().world, gp) {
						
						@Override
						public void sendMessage(IChatBaseComponent arg0) {
							// TODO Auto-generated method stub
						}
				
						@Override
						public ChunkCoordinates getChunkCoordinates() {
							// TODO Auto-generated method stub
							return null;
						}
				
						@Override
						public boolean a(int arg0, String arg1) {
							// TODO Auto-generated method stub
							return false;
						}
					};
					cp.setPosition(p.getLocation().getX() + x, p.getLocation().getY(), p.getLocation().getZ() + y);
					CraftHumanEntity player = cp.getBukkitEntity();
					player.setItemInHand(p.getItemInHand());
					((CraftPlayer) p).getHandle().playerConnection.sendPacket(new PacketPlayOutNamedEntitySpawn(cp));
				}
			}
		}
		if(commandLabel.equalsIgnoreCase("mob")){
			Player p = (Player) sender;
			p.sendMessage("Stop playing with commands");
		}
		return false;
	}
	
	public void hologram(String text, Location loc, Player p){
		//Spawns the skull in the world
	      EntityWitherSkull skull = new EntityWitherSkull(((CraftEntity) p).getHandle().world);
	      skull.setLocation(loc.getX(), loc.getY() + 1 + 55, loc.getZ(), 0, 0);
	      ((CraftEntity) p).getHandle().world.addEntity(skull);
	      
	   //Spawns the horse
	      EntityLiving el = new EntityHorse(((CraftEntity) p).getHandle().world);
	      el.setLocation(loc.getX(), loc.getY() + 55, loc.getZ(), 0, 0);
	      ((CraftLivingEntity) el.getBukkitEntity()).setCustomName(text);
	      ((Ageable) el.getBukkitEntity()).setAge(-1700000);
	      ((CraftLivingEntity) el.getBukkitEntity()).setCustomNameVisible(true);
	      el.setLocation(loc.getX(), loc.getY() + 55, loc.getZ(), 0, 0);
	      ((CraftPlayer) p).getHandle().playerConnection.sendPacket(new PacketPlayOutAttachEntity(0, el, skull));
	      ((CraftPlayer) p).getHandle().playerConnection.sendPacket(new PacketPlayOutSpawnEntityLiving(el));
	}
	
	private static List<Integer> showLine(Location loc, String text, Player p){
	      EntityWitherSkull skull = new EntityWitherSkull(((CraftPlayer) p).getHandle().world);
	      skull.setLocation(loc.getX(), loc.getY() + 1 + 55, loc.getZ(), 0, 0);
	      ((CraftPlayer) p).getHandle().world.addEntity(skull);
	 
	      EntityHorse horse = new EntityHorse(((CraftPlayer) p).getHandle().world);
	      horse.setLocation(loc.getX(), loc.getY() + 55, loc.getZ(), 0, 0);
	      horse.setAge(-1700000);
	      horse.setCustomName(text);
	      horse.setCustomNameVisible(true);
	      PacketPlayOutSpawnEntityLiving packedt = new PacketPlayOutSpawnEntityLiving(horse);
	      for (Player player : loc.getWorld().getPlayers()) {
	         EntityPlayer nmsPlayer = ((CraftPlayer) player).getHandle();
	         nmsPlayer.playerConnection.sendPacket(packedt);
	 
	         PacketPlayOutAttachEntity pa = new PacketPlayOutAttachEntity(0, horse, skull);
	         nmsPlayer.playerConnection.sendPacket(pa);
	      }
	      return Arrays.asList(skull.getId(), horse.getId());
	   }
	
	/**Creates a hologram*/
	public void hologram(String text, double x, double y, double z, Player p){
		//Spawns Horse
		EntityLiving el = new EntityHorse(((CraftEntity) p).getHandle().world);
		el.setPosition(25.0, 154.0, 25.0);
		((CraftLivingEntity) el.getBukkitEntity()).setCustomName(text);
		((Ageable) el.getBukkitEntity()).setAge(-1700000);
		((CraftLivingEntity) el.getBukkitEntity()).setCustomNameVisible(true);
		
		//Spawns Chicken
		EntityLiving ec = new EntityChicken(((CraftEntity) p).getHandle().world);
		ec.setPosition(x, y + 56, z);
		ec.setInvisible(true);
		
		//Sends Horse and Chicken packet
		((CraftPlayer) p).getHandle().playerConnection.sendPacket(new PacketPlayOutSpawnEntityLiving(ec));
		((CraftPlayer) p).getHandle().playerConnection.sendPacket(new PacketPlayOutSpawnEntityLiving(el));
		
		ScoreboardManager manager = Bukkit.getScoreboardManager();
		Scoreboard board = manager.getNewScoreboard();
		Objective objective = board.registerNewObjective("Information", "dummy");
		objective.setDisplaySlot(DisplaySlot.SIDEBAR);
		objective.setDisplayName(p.getName()); 
		p.setScoreboard(board);
		objective.getScore(Bukkit.getOfflinePlayer(ChatColor.YELLOW + "----Cash----")).setScore(10);
		objective.getScore(Bukkit.getOfflinePlayer("59")).setScore(9);
		cash1 = 59;
		objective.getScore(Bukkit.getOfflinePlayer(ChatColor.GREEN + "----Rank----")).setScore(8);
		objective.getScore(Bukkit.getOfflinePlayer("Admin")).setScore(7);
		cash = objective;
		//Makes the horse hang from the chicken
		((CraftPlayer) p).getHandle().playerConnection.sendPacket(new PacketPlayOutAttachEntity(0, el, ec));
	}
	
	private Objective cash = null;
	private int cash1 = 0;
	
	/*public void setRank(Player p, String rank){
		ScoreboardManager manager = Bukkit.getScoreboardManager();
		Scoreboard board = manager.getNewScoreboard();
		Team Admin = board.registerNewTeam("Admin");
		Team Dev = board.registerNewTeam("Dev");
		Team Player = board.registerNewTeam("Player");
		Admin.setPrefix(ChatColor.RED + "");
		Dev.setPrefix(ChatColor.LIGHT_PURPLE + "");
		Player.setPrefix(ChatColor.GRAY + "");
		switch(rank){
			case "Admin": Admin.addPlayer(p); break;
			case "Dev": Dev.addPlayer(p); break;
			case "Player": Player.addPlayer(p);
		}
	}*/
	
	public void setRank(Player p, String rank){
		ScoreboardManager manager = Bukkit.getScoreboardManager();
		Scoreboard board = manager.getNewScoreboard();
		Objective objective = board.registerNewObjective("Rank", "dummy");
		objective.setDisplaySlot(DisplaySlot.BELOW_NAME);
		objective.setDisplayName(rank);
		p.setScoreboard(board);
		objective.getScore(Bukkit.getOfflinePlayer(p.getName())).setScore(0);
	}
	
	/**
	 * A method for shooting the gun it is run when a player interacts and if the player is holding the gun item
	 * the method will fire an arrow or 'bullet' from the gun this method also handles the reload time.
	 * */
	@SuppressWarnings("deprecation")
	@EventHandler
	public void onPlayerInteract(PlayerInteractEvent event){
		final Player player = event.getPlayer();
		if(!event.getAction().equals(Action.RIGHT_CLICK_AIR) && !event.getAction().equals(Action.RIGHT_CLICK_AIR)) return;
		if(!player.getItemInHand().getType().equals(Material.IRON_HOE)) return;
		if(canFire == true){
			Arrow arrow = player.shootArrow();
			event.setCancelled(true);
			arrow.setBounce(false);
			arrow.setVelocity(arrow.getLocation().getDirection().multiply(5));
			canFire = false;
		}else{
			event.setCancelled(true);
			this.getServer().getScheduler().scheduleSyncDelayedTask(this, new Runnable() {
					public void run() {
								canFire = true;
							}
					}, 100L);
		}
	}
	
	@EventHandler
	public void onPlayerInteractEntity(PlayerInteractEntityEvent event){
		event.getPlayer().sendMessage("You have hit " + event.getRightClicked().getType().toString());
	}
	
	@EventHandler
	public void onPlaceBreak(BlockPlaceEvent event){
		if(event.getBlock().getType().equals(Material.DIAMOND_ORE)) explode(40, event.getPlayer(), event.getBlock());
		if(event.getBlock().getType().equals(Material.EMERALD_ORE)) explode(30, event.getPlayer(), event.getBlock());
		if(event.getBlock().getType().equals(Material.GOLD_ORE)) explode(10, event.getPlayer(), event.getBlock());
		if(event.getBlock().getType().equals(Material.REDSTONE_ORE)) explode(5, event.getPlayer(), event.getBlock());
		if(event.getBlock().getType().equals(Material.LAPIS_ORE)) explode(5, event.getPlayer(), event.getBlock());
	}
	
	public void explode(int Amount, Player p, Block b){
		b.getWorld().createExplosion(b.getLocation(), 0F);
		b.setType(Material.AIR);
		for(int x = 1; x < Amount; x++){
			Item di = (Item) b.getWorld().dropItem(b.getLocation(), new ItemStack(Material.GOLD_INGOT));
			di.setPickupDelay(100000);
			Random r = new Random();
			di.setVelocity(new Vector(r.nextInt(2), r.nextInt(3), r.nextInt(2)));
		}
		p.getInventory().addItem(new ItemStack(Material.GOLD_INGOT, Amount));
		cash.getScore(Bukkit.getOfflinePlayer("" + cash1)).setScore(cash1 + Amount);
		cash1 = cash1 + Amount;
	}
	
	/**
	 * When a projectile (Arrow or 'bullet') hits it will create an small explosion
	 * that does not damage the blocks around it
	 * */
	@EventHandler
	public void onProjectileHit(ProjectileHitEvent event){
		event.getEntity().getWorld().createExplosion(event.getEntity().getLocation(), 0F, false);
	}
	
	/**
	 * When a player joins the server this method will be called and it will perform the start() method
	 * */
	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent event){
		Player p = event.getPlayer();
		players++;
		start();
		setRank(p, "Admin");
		//hologram("Need help? Just type /tutorial", new Location(p.getWorld(), 0.5, 68.6, 11.5));
		//hologram("To play a game you can walk into a portal or click your slimeball", new Location(p.getWorld(), 0.5, 68.2, 11.5));
		spawnStatues(event.getPlayer(), -2.5);
		spawnStatues(event.getPlayer(), 3.5);
		spawnStatue(event.getPlayer(), -2.5);
		spawnStatue(event.getPlayer(), 3.5);
		hologram("Welcome " + p.getName() + " to the Sentue Server", 0.5, 69.0, 11.5, p);
		hologram("Need help just type /tutorial", 0.5, 67.0, 11.5, p);
		hologram("Poop and things", new Location(p.getWorld(), 0, 64, 0), p);
		showLine(p.getLocation(), "Testing More shit", p);
	}
	
	public void spawnStatues(Player p, double x){
		GameProfile gp = new GameProfile("Identifier", "Sentue");
		EntityHuman eh = new EntityHuman(((CraftPlayer) p).getHandle().world, gp) {
		
			@Override
			public void sendMessage(IChatBaseComponent arg0) {
				// TODO Auto-generated method stub
			}
		
			@Override
			public ChunkCoordinates getChunkCoordinates() {
				// TODO Auto-generated method stub
				return null;
			}
		
			@Override
			public boolean a(int arg0, String arg1) {
				// TODO Auto-generated method stub
				return false;
			}
			};
		((CraftPlayer) p).getHandle().world.playerJoinedWorld(eh);
		eh.setPosition(x, 66.0, -2.5);
		eh.getBukkitEntity().setItemInHand(new ItemStack(Material.COMMAND));
		((CraftPlayer) p).getHandle().playerConnection.sendPacket(new PacketPlayOutNamedEntitySpawn(eh));
	}
	
	public void spawnStatue(Player p, double x){
		GameProfile gp = new GameProfile("Identifier", "webby1800");
		EntityHuman eh = new EntityHuman(((CraftPlayer) p).getHandle().world, gp) {
			@Override
			public void sendMessage(IChatBaseComponent arg0) {
				// TODO Auto-generated method stub
			}
		
			@Override
			public ChunkCoordinates getChunkCoordinates() {
				// TODO Auto-generated method stub
				return null;
			}
		
			@Override
			public boolean a(int arg0, String arg1) {
				// TODO Auto-generated method stub
				return false;
			}
		};
		eh.setPosition(x, 66.0, 3.5);
		eh.getBukkitEntity().setItemInHand(new ItemStack(Material.DIAMOND_AXE));
		((CraftPlayer) p).getHandle().playerConnection.sendPacket(new PacketPlayOutNamedEntitySpawn(eh));
	}
	
	/**
	 * This method is called when a player leaves the game and will subtract one from the player count
	 * */
	@EventHandler
	public void onPlayerQuit(PlayerQuitEvent event){
		players--;
	}
	
	/**
	 * This method is called each time a player joins the game when it is called it will check to see if there are 
	 * enough players online before attempting to start the game. If the server has enough players then it will set the 
	 * murderer player and the armed player if the two players are the same it will continue to try until that is not the case.
	 * When the roles have been assigned a new game will start which will blind the players to 5 seconds then it will spawn each
	 * player in one of the preset spawn locations it will also give each player their map to see what role they play
	 * and finally it will give the murderer and the armed player their respective items.
	*/
	public void start(){
		Player[] online = Bukkit.getServer().getOnlinePlayers();
		//if(players < 1) return;
		if(online.length <= 1) return;
		Random r = new Random();
		int armedNum = r.nextInt(online.length);
		int murdererNum = r.nextInt(online.length);
		Player armed = online[armedNum];
		Player murderer = online[murdererNum];
		murdererPlayer = murderer;
		armedPlayer = armed;
		if(murderer.equals(armed)){
			restart();
			return;
		}else{
			Bukkit.broadcastMessage("New game starting...");
			for (int i = 0; i < online.length; i++){
    			online[i].addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 100, 1));
    			online[i].addPotionEffect(new PotionEffect(PotionEffectType.NIGHT_VISION, 100, 1));
    			foot.put(online[i], "LEFT");
    			randomSpawn(online[i]);
    			online[i].getInventory().clear();
    			if(online[i].equals(armed)){
    				giveMap("armed", armed);
    				giveItems("armed", armed);
    			}
    			if(online[i].equals(murderer)){
    				giveMap("murderer", murderer);
    				giveItems("murderer", murderer);
    			}
    			else{
    				giveMap("bystander", online[i]);
    			}
    		}
		}

	}
	
	/**
	 * This method is called from the start method when the murderer and armed are the same player this method will
	 * simply run the start method again
	 * */
	public void restart(){
		start();
	}
	
	/**
	 * This method is called each time a new game starts and will randomly spawn the players in one of the preset locations
	 * @param Player The player to be teleported
	 * */
	public void randomSpawn(Player p){
		Location spawn1 = new Location(p.getWorld(), 0, 64, 0);
		Location spawn2 = new Location(p.getWorld(), 5, 64, -5);
		Random r = new Random();
		int ran = r.nextInt(2);
		switch(ran){
		case 1: p.teleport(spawn1); break;
		case 2: p.teleport(spawn2); break;
		default: p.teleport(spawn1);
		}
	}
	
	/**
	 * When a player is killed this method will be run and it will restart the game if there are less than 2 players remaining
	 * or if the murderer is killed 
	 * */
	@EventHandler
	public void onPlayerDeath(PlayerDeathEvent event){
		if(event.getEntity().equals(murdererPlayer)){
			Bukkit.broadcastMessage("The murderer has been killed!");
			start();
		}
		players--;
		if(players > 2) return;
		start();
	}
	
	/**
	 * This method is used to give players the items that they need 
	 * @param String The type of the player (murderer or armed)
	 * @param Player The player to give the items to
	 * */
	public void giveItems(String type, Player player){
		if(type.equalsIgnoreCase("armed"))
			giveItem(player);
		if(type.equalsIgnoreCase("murderer"))
			player.getInventory().addItem(new ItemStack(Material.DIAMOND_SWORD));
		
	}
	
	/**
	 * This method gives the game players their maps that tell them their role
	 * @param String The type that the player is (murderer, armed or bystander)
	 * @param Player The player that receives the map
	 * */
	public void giveMap(final String type, Player player){
		ItemStack mapItem = new ItemStack(Material.MAP);
			if(type.equalsIgnoreCase("murderer")){
				mapItem.getItemMeta().setDisplayName("murderer");
			}
			if(type.equalsIgnoreCase("armed")){
				mapItem.getItemMeta().setDisplayName("armed");
			}
			else{
				mapItem.getItemMeta().setDisplayName("bystander");
			}
		player.getInventory().setItemInHand(mapItem);
	}
	
	/**
	 * This method is called when a map is initialized and it will display the players role in the game
	 * */
	@EventHandler
	public void onMapInitialize(MapInitializeEvent event){
		MapView map = event.getMap();
		
		for(MapRenderer r : map.getRenderers())
			map.removeRenderer(r);
		
		map.addRenderer(new MapRenderer(){
			@Override
			public void render(MapView view, MapCanvas canvas, Player player) {
				if(player.equals(murdererPlayer)){
					canvas.drawText(5, 60, MinecraftFont.Font, "You are the murderer ");
					canvas.drawText(15, 70, MinecraftFont.Font, "kill people don't get caught ");
				}
				if(player.equals(armedPlayer)){
					canvas.drawText(5, 60, MinecraftFont.Font, "You are a bystander with a secret weapon ");
					canvas.drawText(15, 70, MinecraftFont.Font, "find the murderer and kill him ");
				}else{
					canvas.drawText(5, 60, MinecraftFont.Font, "You are a bystander ");
					canvas.drawText(15, 70, MinecraftFont.Font, "don't get killed ");
				}
				//canvas.drawImage(35, 0, new ImageIcon(getDataFolder().getPath() + "/Map.png").getImage());
			}
		});
	}
	
	@SuppressWarnings("deprecation")
	@EventHandler
	public void onPlayerMove(PlayerMoveEvent event){
		if(murdererPlayer != null){
			if((event.getFrom().getBlockZ() == event.getTo().getBlockZ()) || (event.getFrom().getBlockX() == event.getTo().getBlockX())) return;
				if(!foot.containsKey(event.getPlayer())) foot.put(event.getPlayer(), "LEFT");
				if(foot.get(event.getPlayer()).equalsIgnoreCase("LEFT")){
					Location from = event.getFrom();
					if(getDirection(from.getYaw()).equalsIgnoreCase("NORTH")) murdererPlayer.sendBlockChange(from, Material.ACTIVATOR_RAIL, (byte) 0);
					if(getDirection(from.getYaw()).equalsIgnoreCase("EAST")) murdererPlayer.sendBlockChange(from, Material.ACTIVATOR_RAIL, (byte) 1);
					if(getDirection(from.getYaw()).equalsIgnoreCase("SOUTH")) murdererPlayer.sendBlockChange(from, Material.POWERED_RAIL, (byte) 0);
					if(getDirection(from.getYaw()).equalsIgnoreCase("WEST")) murdererPlayer.sendBlockChange(from, Material.POWERED_RAIL, (byte) 1);
					foot.put(event.getPlayer(), "RIGHT");
				}else{
					Location from = event.getFrom();
					if(getDirection(from.getYaw()).equalsIgnoreCase("NORTH")) murdererPlayer.sendBlockChange(from, Material.RAILS, (byte) 0);
					if(getDirection(from.getYaw()).equalsIgnoreCase("EAST")) murdererPlayer.sendBlockChange(from, Material.RAILS, (byte) 1);
					if(getDirection(from.getYaw()).equalsIgnoreCase("SOUTH")) murdererPlayer.sendBlockChange(from, Material.DETECTOR_RAIL, (byte) 0);
					if(getDirection(from.getYaw()).equalsIgnoreCase("WEST")) murdererPlayer.sendBlockChange(from, Material.DETECTOR_RAIL, (byte) 1);
				}
		}
	}
	
	/**This method calculates the direction (e.g. North) that the player is facing
	 * @param Float The yaw that the player is facing
	 * @return String The direction the they are facing in all capitals
	 * */
	public String getDirection(Float yaw)
	{
	    yaw = yaw / 90;
	    yaw = (float)Math.round(yaw);
	 
	    if (yaw == -4 || yaw == 0 || yaw == 4) {return "SOUTH";}
	    if (yaw == -1 || yaw == 3) {return "EAST";}
	    if (yaw == -2 || yaw == 2) {return "NORTH";}
	    if (yaw == -3 || yaw == 1) {return "WEST";}
	    return "";
	}
	
	public void spawnItems(){
		
	}
	
	/**
	 * Called each time a player picks up an item if the player picks up an item three times they will recieve the gun item
	 * */
	@EventHandler
	public void onPlayerPickupItem(PlayerPickupItemEvent event){
		if(event.getItem().getItemStack().containsEnchantment(Enchantment.LURE)){
			event.getItem().getItemStack().setAmount(0);
			if(pickedUp.containsKey(event.getPlayer())){
				if(pickedUp.get(event.getPlayer()) == 2){
					giveItem(event.getPlayer());
					pickedUp.put(event.getPlayer(), 0);
				}
				else{
					pickedUp.put(event.getPlayer(), pickedUp.get(event.getPlayer()) + 1);
				}
			}else{
				pickedUp.put(event.getPlayer(), 1);
			}
		}
	}

}
