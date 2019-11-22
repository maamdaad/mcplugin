package de.craftomania.plugin.MCPlugin;

import java.util.ArrayList;
import java.util.HashMap;

import org.bukkit.Location;
import org.bukkit.entity.Player;

public class MobGame {
	
	ArrayList<Player> player;
	ArrayList<Location> spawnpoints;
	HashMap<String,String> teams;
	
	Location[] lobbydoor;
	
	Location spawn;
	Location lobby;
	
	public MobGame(ArrayList<Player> player, ArrayList<Location> spawnpoints, HashMap<String, String> teams) {
		this.player = player;
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
	
	public void startGame() {
		
		for (Player p : player) {
			p.teleport(spawn);
			p.sendMessage("Willkommen in der Arena!");
		}
		
	}
	
}
