package de.craftomania.plugin.MCPlugin;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;

public class MobGame {
	
	ArrayList<Location> spawnpoints;
	HashMap<String,String> teams;
	
	Location[] lobbydoor;
	
	Location spawn;
	Location lobby;
	
	public MobGame(ArrayList<Location> spawnpoints, HashMap<String, String> teams) {
		this.spawnpoints = spawnpoints;
		this.teams = teams;
	}
	
	public void setSpawns(Location lobby, Location spawn) {
		this.lobby = lobby;
		this.spawn = spawn;
	}
	
	public void setLobbyDoor(Location[] pos) {
		this.lobbydoor = pos;
	}
	
	private Player getByUUID(String str) {
		Player p = Bukkit.getServer().getPlayer( UUID.fromString(str) );
		return p;
	}
	
	public void startGame() {
		
		for (String key : teams.keySet()) {
			
			Player p = getByUUID(key);
			
			p.teleport(spawn);
			p.sendMessage("Willkommen in der Arena!");
		}
		
		for (Location loc : spawnpoints) {
			
			loc.getWorld().spawnEntity(loc, EntityType.ZOMBIE);
			
		}
		
	}
	
}
