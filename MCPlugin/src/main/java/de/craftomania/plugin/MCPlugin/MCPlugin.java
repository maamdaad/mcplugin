package de.craftomania.plugin.MCPlugin;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Properties;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.block.*;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

public final class MCPlugin extends JavaPlugin implements Listener {

	HashMap<String, String> teams = new HashMap<String, String>();		
	HashMap<String, ItemStack[]> inv = new HashMap<String, ItemStack[]>();
	HashMap<String, ItemStack[]> arm = new HashMap<String, ItemStack[]>();
	HashMap<String, Integer> level = new HashMap<String, Integer>();
	HashMap<String, GameMode> gamemodes = new HashMap<String, GameMode>(); 
	HashMap<String, Long> score = new HashMap<String, Long>();
	
	ArrayList<Location> spawnpoints = new ArrayList<Location>(); 
	
	Location spawn;
	Location lobby;
	Location[] lobbydoor = new Location[4];
	
	MobGame game;	
	
	@Override
    public void onEnable() {
        getLogger().info("MCPlugin an!");
        Bukkit.getServer().getPluginManager().registerEvents(this, this);
        
        
        BukkitRunnable run = new BukkitRunnable() {
			
			@Override
			public void run() {
				Properties prop = new Properties();
		        
		        InputStream input;
				try {
					input = new FileInputStream("arena.properties");
					prop.load(input);
					getLogger().info("Properties File reading...");
				} catch (FileNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		        
		        World w = Bukkit.getServer().getWorld(prop.getProperty("world"));
		        getLogger().info("Loaded Prop World " + w.getName());        
		        
		        String tmp = prop.getProperty("spawn");
		        String[] atmp = tmp.split(",");
		        spawn = new Location(w, Integer.parseInt(atmp[0]), Integer.parseInt(atmp[1]), Integer.parseInt(atmp[2]));
		        getLogger().info("Loaded Prop Spawn " + spawn.getBlockX() + "," + spawn.getBlockY() + "," + spawn.getBlockZ());
		        
		        tmp = prop.getProperty("lobby");
		        atmp = tmp.split(",");
		        lobby = new Location(w, Integer.parseInt(atmp[0]), Integer.parseInt(atmp[1]), Integer.parseInt(atmp[2]));
		        getLogger().info("Loaded Prop Lobby " + lobby.getBlockX() + "," + lobby.getBlockY() + "," + lobby.getBlockZ());
		        
		        for (int i = 0; i < lobbydoor.length; i++) {
		        	tmp = prop.getProperty("lobbydoor" + i);
		            atmp = tmp.split(",");
		        	
		            lobbydoor[i] = new Location(w, Integer.parseInt(atmp[0]), Integer.parseInt(atmp[1]), Integer.parseInt(atmp[2]));
		            getLogger().info("Loaded Prop Lobbydoor" + i + " " + lobbydoor[0].getBlockX() + ","+ lobbydoor[0].getBlockY() + ","+ lobbydoor[0].getBlockZ());
		        	
		        }
		               
		        int max = Integer.parseInt(prop.getProperty("spawnpoints"));
		        getLogger().info("Loaded Prop Spawnpoints " + max);
		        
		        for (int i = 0; i < max; i++) {
		        	tmp = prop.getProperty("spawnpoint" + i);
		            atmp = tmp.split(",");
		            
		            spawnpoints.add(new Location(w, Integer.parseInt(atmp[0]), Integer.parseInt(atmp[1]), Integer.parseInt(atmp[2])));
		            getLogger().info("Loaded Prop Spawnpoint" + i + " " + spawnpoints.get(i).getBlockX() + "," + spawnpoints.get(i).getBlockY() + "," + spawnpoints.get(i).getBlockZ());
		        }
		        
		        getLogger().info("Loaded all Props!");
			}
		};
		
		run.runTaskLater(this, 1L);
        
        
    }
    @Override
    public void onDisable() {
    	getLogger().info("MCPlugin aus!");
        
    	Properties prop = new Properties();
    	World world = spawn.getWorld();
        prop.setProperty("world", world.getName());
        getLogger().info("Set Prop World " + world.getName());
        prop.setProperty("spawn", spawn.getBlockX() + "," + spawn.getBlockY() + "," + spawn.getBlockZ());
        getLogger().info("Set Prop Spawn " + spawn.getBlockX() + "," + spawn.getBlockY() + "," + spawn.getBlockZ() );
        prop.setProperty("lobby", lobby.getBlockX() + "," + lobby.getBlockY() + "," + lobby.getBlockZ());
        getLogger().info("Set Prop Lobby " + lobby.getBlockX() + "," + lobby.getBlockY() + "," + lobby.getBlockZ());
        
        for (int i = 0; i < lobbydoor.length; i++) {
        	prop.setProperty("lobbydoor" + i, lobbydoor[i].getBlockX() + "," + lobbydoor[i].getBlockY() + "," + lobbydoor[i].getBlockZ());
        	getLogger().info("Set Prop Lobbydoor" + i + " " + lobbydoor[i].getBlockX() + "," + lobbydoor[i].getBlockY() + "," + lobbydoor[i].getBlockZ());
        }
        
        int i = 0;
        
        prop.setProperty("spawnpoints", spawnpoints.size() + "");
        getLogger().info("Set Prop Spawnpoints " + spawnpoints.size());
        
        for (Location loc : spawnpoints) {
        	prop.setProperty("spawnpoint" + i, loc.getBlockX() + "," + loc.getBlockY() + "," + loc.getBlockZ());
        	getLogger().info("Set Prop Spawnpoint" + i + " " + loc.getBlockX() + "," + loc.getBlockY() + "," + loc.getBlockZ());
        	i++;
        }
        
        try {
			prop.store(new FileOutputStream("arena.properties"), null);
			getLogger().info("Properties saved!");			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
    }
    
    private void start(Player target) {
    	
    	if (teams.size() < 1) {
    		target.sendMessage("Keine Spieler in Teams");
    	} else {
    		target.sendMessage("Starte Spiel mit " + teams.size() + " Spielern!");
    		
    		game = new MobGame(this);
    		
    		game.startGame();
    		
    	}
    	
    }
    
    @EventHandler
    private void onEntityDeath(EntityDeathEvent event) {
    	if (game != null) {
    		if (event.getEntity().getLocation().getWorld().equals(spawn.getWorld())) {
    			
    			if (teams.get( event.getEntity().getKiller().getUniqueId().toString() ) != null) {
    				
    				long currscore = score.get( event.getEntity().getKiller().getUniqueId().toString() );
    				
    				if (event.getEntityType().equals(EntityType.ZOMBIE)) {
    					
    					currscore += 1;
    					
    					game.skeletoncount--;
    					
    					event.getEntity().getKiller().sendMessage("Du hast +1 Score, aktueller Score " + score);
    					
    				}
    				
    				if (event.getEntityType().equals(EntityType.SKELETON)) {
    					
    					currscore += 1;
    					
    					game.zombiecount--;
    					
    					event.getEntity().getKiller().sendMessage("Du hast +1 Score, aktueller Score " + score);
    					
    				}
    				
    				score.put(event.getEntity().getKiller().getUniqueId().toString(), currscore);
    			}
    			
    		}
    		
    		if (event.getEntity() instanceof Player) {
    			if (teams.get( event.getEntity().getUniqueId().toString() ) != null) {
    				
    				event.getEntity().teleport(spawn);
    				
    			}
    		}
    	}
    }
    
    private void leaveteam(Player target) {
    	if (teams.get(target.getUniqueId().toString()) != null) {
			target.getInventory().clear();
			target.sendMessage("Team verlassen!");
		
			target.getInventory().setContents(inv.get(target.getUniqueId().toString()));
			target.getInventory().setArmorContents(arm.get(target.getUniqueId().toString()));
		
			target.setGameMode(gamemodes.get( target.getUniqueId().toString() ));
			
			arm.remove(target.getUniqueId().toString());
			inv.remove(target.getUniqueId().toString());
			teams.remove(target.getUniqueId().toString());
			level.remove(target.getUniqueId().toString());
			gamemodes.remove(target.getUniqueId().toString());
			
			if (teams.size() == 0) {
				game.stop();
				
				game = null;
			}
			
		} else {
			target.sendMessage("Player nicht in Team");
		}
    }
    
    @EventHandler
    public void onPlayerShoot(EntityShootBowEvent e) {
    	
    	if (e.getEntity() instanceof Player) {
    		final Player target = (Player) e.getEntity();
        	
        	if (teams.get( target.getUniqueId().toString() ).equalsIgnoreCase("jaeger")) {
        		target.sendMessage("Arrow geschossen! Cooldown: " + Klassen.getInstance().jaeger_bowcooldown[ level.get(target.getUniqueId().toString()) ] + " ticks");
            	
        		getServer().getScheduler().scheduleSyncDelayedTask(this, new Runnable() {
        		    public void run() {
        		    	ItemStack a = new ItemStack(Material.ARROW,1);
                    	
        		    	boolean hatnoch = false;
        		    	
          		    	for (ItemStack item : target.getInventory().getContents()) {
        		    		if (item != null) {
        		    			if (item.getType().equals(a.getType())) {
            		    			target.sendMessage("Aha! Du hast noch einen Pfeil du Spasti!");
            		    			hatnoch = true;
            		    		}
        		    		}
          		    		
        		    	}
          		    	
          		    	if (!hatnoch) {
          		    		
          		    		target.getInventory().addItem(a);
          		    	}

        		    }
        		}, Klassen.getInstance().jaeger_bowcooldown[ level.get(target.getUniqueId().toString()) ]);

        	}
    	}
    }
    
    @EventHandler
    public void onPlayerClickSign(PlayerInteractEvent event){
    	
    	if (event.getPlayer() instanceof Player) {
    		Player target = event.getPlayer();
            if(event.getClickedBlock().getType() == Material.OAK_SIGN || event.getClickedBlock().getType() == Material.OAK_WALL_SIGN){
               
                Sign sign = (Sign) event.getClickedBlock().getState();
                
                if (sign.getLine(0).equalsIgnoreCase("(join)")) {
                	
                	
                	if (teams.get(target.getUniqueId().toString()) == null) {
            			joinClass(target, sign.getLine(1));
            		} else {
            			target.sendMessage("Player schon in Team");
            		}
                	
                	
                } else if (sign.getLine(0).equalsIgnoreCase("(leave)")) {
                	
                	leaveteam(target);
                	
                } else if (sign.getLine(0).equalsIgnoreCase("(levelup)")) {
                	
                	if (teams.get(target.getUniqueId().toString()) != null) {
                		levelclass(target);
            		} else {
            			target.sendMessage("Player nicht in Team");
            		}
                	
                } else if (sign.getLine(0).equalsIgnoreCase("(start)")) {
                	
                	start(target);
                	
                }
                
            }
    	}

    }
	
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
    	if (cmd.getName().equalsIgnoreCase("arena")) { 
    		
    		checkCommand(sender, cmd, label, args);
    		
    		/*Player pl = (Player) sender;
    	    for(Entity nearby : pl.getNearbyEntities(10,10,10)) {
    	        if(nearby instanceof Player) {
    	        	getLogger().info("In der Nähe " + nearby.getName() + " : " + nearby.getLocation().toString());
    	        }
    	    }*/
    		
    		return true;
    	} 
    	return false; 
    }
    
    private void checkCommand(CommandSender sender, Command cmd, String label, String[] args) {
    	if (args.length > 0) {
    		if (args[0].equalsIgnoreCase("joinclass")) {
    			    			
    			String classname = args[1].toLowerCase();
        		String playername = args[2];
        		
        		Player target = Bukkit.getServer().getPlayer(playername);
        		
        		if (teams.get(target.getUniqueId().toString()) == null) {
        			joinClass(target, classname);
        		} else {
        			target.sendMessage("Player schon in Team");
        		}
        		
        		

        	} 
    		
    		if (args[0].equalsIgnoreCase("leaveclass")) {
    			
				Player target = Bukkit.getServer().getPlayer(args[1]);
				
				leaveteam(target);
				
    		}
    		
    		if (args[0].equalsIgnoreCase("inteam")) {
    			Player target = Bukkit.getServer().getPlayer(args[1]);
    			Player send = (Player) sender;
    			
    			send.sendMessage("Ist in Team: " + teams.get(target.getUniqueId().toString()));
    		}
    		
    		if (args[0].equalsIgnoreCase("inlevel")) {
    			Player target = Bukkit.getServer().getPlayer(args[1]);
    			Player send = (Player) sender;
    			
    			send.sendMessage("Ist in Level: " + level.get(target.getUniqueId().toString()));
    		}
    		
    		if (args[0].equalsIgnoreCase("start")) {
    			Player target = Bukkit.getServer().getPlayer(args[1]);
    			
    			start(target);
    		}
    		
    		if (args[0].equalsIgnoreCase("addsp")) {
    			Player target = (Player) sender;
    			
    			spawnpoints.add( target.getLocation() );
    			
    			target.sendMessage("Neuer Spawnpoint für Monster bei x=" + target.getLocation().getBlockX() + " y=" + target.getLocation().getBlockY() + " z=" + target.getLocation().getBlockZ());
    		}
    		
    		if (args[0].equalsIgnoreCase("clearsp")) {
    			Player target = (Player) sender;
    			
    			spawnpoints.clear();
    			
    			target.sendMessage("Spawnpunkte gelöscht.");
    		}
    		
    		if (args[0].equalsIgnoreCase("setspawn")) {
    			Player target = (Player) sender;
    			
    			spawn = target.getLocation();
    			
    			target.sendMessage("Player Spawnpoint bei x=" + target.getLocation().getBlockX() + " y=" + target.getLocation().getBlockY() + " z=" + target.getLocation().getBlockZ());
    		}
    		
    		if (args[0].equalsIgnoreCase("setlobby")) {
    			Player target = (Player) sender;
    			
    			lobby = target.getLocation();
    			
    			target.sendMessage("Lobby Spawnpoint bei x=" + target.getLocation().getBlockX() + " y=" + target.getLocation().getBlockY() + " z=" + target.getLocation().getBlockZ());
    			    			
    		}
    		
    		if (args[0].equalsIgnoreCase("setlobbydoor")){
    			Player target = (Player) sender;
    			
    			String pos1 = args[1];
    			String pos2 = args[2];
    			String pos3 = args[3];
    			String pos4 = args[4];
    			
    			String[] apos1 = pos1.split(",");
    			String[] apos2 = pos2.split(",");
    			String[] apos3 = pos3.split(",");
    			String[] apos4 = pos4.split(",");
    			
    			lobbydoor[0] = new Location(target.getWorld(), Integer.parseInt(apos1[0]), Integer.parseInt(apos1[1]), Integer.parseInt(apos1[2]));
    			lobbydoor[1] = new Location(target.getWorld(), Integer.parseInt(apos2[0]), Integer.parseInt(apos2[1]), Integer.parseInt(apos2[2]));
    			lobbydoor[2] = new Location(target.getWorld(), Integer.parseInt(apos3[0]), Integer.parseInt(apos3[1]), Integer.parseInt(apos3[2]));
    			lobbydoor[3] = new Location(target.getWorld(), Integer.parseInt(apos4[0]), Integer.parseInt(apos4[1]), Integer.parseInt(apos4[2]));
    			
    			target.sendMessage("Lobbydoor gesetzt.");
    		}
    		
    		if (args[0].equalsIgnoreCase("opendoor")) {
    			if (game != null) {
    			
    				game.openDoor();
    			
    			} else {
    				Player target = (Player) sender;
    				target.sendMessage("Kein Game initialisiert!");
    			}
    		}
    	
    		if (args[0].equalsIgnoreCase("closedoor")) {
    			if (game != null) {
    			
    				game.closeDoor();
    			
    			} else {
    				Player target = (Player) sender;
    				target.sendMessage("Kein Game initialisiert!");
    			}
    		}
    	
    	}
    	
    }
    
    private int joinClass(Player target, String classname) {
    	
    	gamemodes.put(target.getUniqueId().toString(), target.getGameMode());
    	
    	target.setGameMode(GameMode.SURVIVAL);
    	
    	inv.put(target.getUniqueId().toString(), target.getInventory().getContents());
    	arm.put(target.getUniqueId().toString(), target.getInventory().getArmorContents());
    	
    	target.sendMessage("Beitritt Klasse: " + classname);
		
		target.getInventory().clear();
    	
		teams.put(target.getUniqueId().toString(), classname);
		this.level.put(target.getUniqueId().toString(), 0);
		
    	switch (classname) {
		case "jaeger":
	    	
	    	Klassen.getInstance().jaeger(0, target);
			
			break;
		case "tank":
			
	    	Klassen.getInstance().tank(0, target);
			
			break;
		
		default:
			break;
		}
    	
    	return 1;
    }
    
    private void levelclass(Player target) {
    	
    	int newlevel = level.get(target.getUniqueId().toString()) + 1;
    	
    	if (Klassen.getInstance().MAXLEVEL.get( teams.get(target.getUniqueId().toString()) ) == newlevel) {
    		target.sendMessage("Du hast das maximale Level erreicht!");
    		
    		newlevel--;
    	}
    	
    	if (teams.get(target.getUniqueId().toString()).equalsIgnoreCase("jaeger")){
    		
    		Klassen.getInstance().jaeger(newlevel, target);
    		
    	}
    	if (teams.get(target.getUniqueId().toString()).equalsIgnoreCase("tank")){
    		
    		Klassen.getInstance().tank(newlevel, target);
    		
    	}
    	
    	level.put(target.getUniqueId().toString(), newlevel);
    	
    	
    }
}
