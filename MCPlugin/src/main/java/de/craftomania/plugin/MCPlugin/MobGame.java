package de.craftomania.plugin.MCPlugin;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitTask;

public class MobGame {
	
	MCPlugin instance;
	int foodtask;
	
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
			
			foodtask = instance.getServer().getScheduler().scheduleSyncRepeatingTask(instance, new Runnable() {
			    public void run() {
			    	
			    	p.getInventory().addItem(new ItemStack(Material.COOKED_PORKCHOP,1));

			    }
			}, 5,Klassen.getInstance().food_cooldown[ instance.level.get(p.getUniqueId().toString()) ]);
		}
		
		for (Location loc : instance.spawnpoints) {
			
			loc.getWorld().spawnEntity(loc, EntityType.ZOMBIE);
			
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
		
		instance.getServer().getScheduler().cancelTask(foodtask);
		
	}
	
}
