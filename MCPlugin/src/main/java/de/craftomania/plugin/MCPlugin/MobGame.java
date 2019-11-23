package de.craftomania.plugin.MCPlugin;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Skeleton;
import org.bukkit.entity.Zombie;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

public class MobGame {
		
	MCPlugin instance;
	BukkitRunnable rfood;
	BukkitRunnable rmobs;
	static long ticks = 14;
	static long[] INTERVALS = {5*ticks, 5*ticks, 4*ticks, 4*ticks, 3*ticks, 3*ticks};
	static int[] COUNTS = {10, 10, 20, 20, 30, 30};
	int zombiecount = 0;
	int skeletoncount = 0;
	int currlevel = 0;
	boolean roundrunning = false;
	
	BukkitRunnable spawnz = new BukkitRunnable() {
		
		@Override
		public void run() {
			if (zombiecount < COUNTS[currlevel]) {
				for (Location loc : instance.spawnpoints) {
					
					Zombie zom = (Zombie) loc.getWorld().spawnEntity(loc, EntityType.ZOMBIE);
					zom.setCustomName("TELEKOM!!");
					zom.getEquipment().setHelmet(new ItemStack(Material.LEATHER_HELMET,1));
					
					zombiecount += instance.spawnpoints.size();
					
				}
			} else {
				
				this.cancel();
			}
			
			
		}
	};
	
	BukkitRunnable spawns = new BukkitRunnable() {
		
		@Override
		public void run() {
			if (skeletoncount < COUNTS[currlevel]) {
				for (Location loc : instance.spawnpoints) {
					
					Skeleton ent = (Skeleton) loc.getWorld().spawnEntity(loc, EntityType.SKELETON);
					ent.setCustomName("TELEKOM");
					ent.getEquipment().setHelmet(new ItemStack(Material.LEATHER_HELMET,1));				
					
					skeletoncount++;
					
				}
			} else {
				
				this.cancel();
				
			}
			
			
		}
	};
	
	BukkitRunnable od = new BukkitRunnable() {
		
		@Override
		public void run() {
			if (roundrunning) {
				
				broadcast("Noch " + zombiecount + " Zombies und " + skeletoncount + " Skeletons!");
				
				if (zombiecount == 0 || skeletoncount == 0) {
					roundrunning = false;
					broadcast("Runde vorbei!");
				}
				
			} else {
				openDoor();
				
				broadcast("Runde ist vorbei! Alle Viecher tot!");
				
				this.cancel();
			}
			
		}
	};
	
	public MobGame(MCPlugin instance) {
		this.instance = instance;
	}
	
	
	private Player getByUUID(String str) {
		Player p = Bukkit.getServer().getPlayer( UUID.fromString(str) );
		return p;
	}
	
	public void startGame() {
		
		for (String key : instance.teams.keySet()) {
			
			final Player p = getByUUID(key);
			
			p.teleport(instance.spawn);
			p.sendMessage("Willkommen in der Arena!");
			
			rfood = new BukkitRunnable() {
			    public void run() {
			    	
			    	p.getInventory().addItem(new ItemStack(Material.COOKED_PORKCHOP,1));
			    	
			    }
			};
			
			rfood.runTaskTimer(instance, 5,Klassen.getInstance().food_cooldown[ instance.level.get(p.getUniqueId().toString()) ]);
		}
		
		rmobs = new BukkitRunnable() {
			
			@Override
			public void run() {
				spawnMobs(0);
				
			}
		};
		
		
		openDoor();
		broadcast("Das Tor geht gleich zu!");
		
		rmobs.runTaskLater(instance, 50);
		
	}
	
	private void spawnMobs(int level) {
		
		broadcast("Das Tor schließt!");
		
		closeDoor();
		
		broadcast( (level + 1) +  ". Runde startet!");
		
		roundrunning = true;
		   
		currlevel = level;
			
		spawnz.runTaskTimer(instance, 0, (long) INTERVALS[level]);
			
		spawns.runTaskTimer(instance, 0, (long) INTERVALS[level]);
		
		od.runTaskTimer(instance, INTERVALS[level] * COUNTS.length, 30);
		
	}
	
	private void broadcast(String msg) {
		for (String key : instance.teams.keySet()) {
			
			final Player p = getByUUID(key);
			
			p.sendMessage(msg);
		}	
	}
	
	private int min(int[] arr) {
		int r = arr[0];
		
		for (int i = 0; i < arr.length; i++) {
			if (r > arr[i]) {
				r = arr[i];
			}
		}
		
		return r;
	}
	
	private int max(int[] arr) {
		int r = arr[0];
		
		for (int i = 0; i < arr.length; i++) {
			if (r < arr[i]) {
				r = arr[i];
			}
		}
		
		return r;
	}
	
	public void openDoor() {
		
		int i = 0;
		
		int[] x = new int[4];
		int[] y = new int[4];
		int[] z = new int[4];
		
		for (Location loc : instance.lobbydoor) {
			x[i] = loc.getBlockX();
			y[i] = loc.getBlockY();
			z[i] = loc.getBlockZ();
			
			i++;
		}
		
		for (int px = min(x); px <= max(x); px++) {
			for (int py = min(y); py <= max(y); py++) {
				for (int pz = min(z); pz <= max(z); pz++) {
					
					Location loc = new Location(instance.spawn.getWorld(), px, py, pz);
					
					instance.spawn.getWorld().getBlockAt(loc).setType(Material.AIR);
					
				}
			}
		}
		
	}
	
	public void closeDoor() {
		
		int i = 0;
		
		int[] x = new int[4];
		int[] y = new int[4];
		int[] z = new int[4];
		
		for (Location loc : instance.lobbydoor) {
			x[i] = loc.getBlockX();
			y[i] = loc.getBlockY();
			z[i] = loc.getBlockZ();
			
			i++;
		}
		
		for (int px = min(x); px <= max(x); px++) {
			for (int py = min(y); py <= max(y); py++) {
				for (int pz = min(z); pz <= max(z); pz++) {
					
					Location loc = new Location(instance.spawn.getWorld(), px, py, pz);
					
					instance.spawn.getWorld().getBlockAt(loc).setType(Material.SPRUCE_FENCE);
					
				}
			}
		}
		
	}
	
	public void stop() {
		
		broadcast("Game wird gestoppt...");
		
		rfood.cancel();
		rmobs.cancel();
		spawns.cancel();
		spawnz.cancel();
		od.cancel();
		
		rfood = null;
		rmobs = null;
		spawns = null;
		spawnz = null;
		od = null;
	}
	
}
