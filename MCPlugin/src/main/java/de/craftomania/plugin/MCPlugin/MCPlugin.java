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

import javax.accessibility.AccessibleAction;

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
import org.bukkit.event.block.Action;
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
	HashMap<String, String> meta = new HashMap<String, String>();
	
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
    			
    			Player target = null;
    			
    			event.getDrops().clear();
    			
    			try {
    				target = (Player) event.getEntity().getKiller();
    			} catch (Exception e) {
    				target = null;
    			}
    			
    			if (target != null) {
    				
    				if (teams.get( target.getUniqueId().toString() ) != null) {
        				
        				long currscore = score.get( target.getUniqueId().toString() );
        				
        				if (event.getEntityType().equals(EntityType.ZOMBIE)) {
        					
        					currscore += 1;
        					
        					game.skeletoncount--;
        					
        					target.sendTitle("","Du hast +1 Score, aktueller Score " + currscore);
        					
        				}
        				
        				if (event.getEntityType().equals(EntityType.SKELETON)) {
        					
        					currscore += 1;
        					
        					game.zombiecount--;
        					
        					target.sendTitle("","Du hast +1 Score, aktueller Score " + currscore);
        					
        				}
        				
        				score.put(target.getUniqueId().toString(), currscore);
        			}
    				
    			} else {
    				
    				if (event.getEntityType().equals(EntityType.ZOMBIE)) {
    					
    					game.zombiecount--;
    					
    				}
    				
    				if (event.getEntityType().equals(EntityType.SKELETON)) {
    					
    					game.skeletoncount--;
    					
    					
    				}
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
				if (game != null) {
					game.stop();
					
					game = null;
				}
			}
			
		} else {
			target.sendMessage("Player nicht in Team");
		}
    }
    
    @EventHandler
    public void onPlayerShoot(EntityShootBowEvent e) {
    	
    	if (e.getEntity() instanceof Player) {
    		final Player target = (Player) e.getEntity();
        	
    		if (teams.get(target.getUniqueId().toString()) != null) {
    			if (teams.get( target.getUniqueId().toString() ).equalsIgnoreCase("jaeger")) {
            		
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
              		    		
              		    		target.getInventory().setItem(8, a);
              		    	}

            		    }
            		}, Klassen.getInstance().jaeger_bowcooldown[ level.get(target.getUniqueId().toString()) ]);

            	}
    		}	
    	}
    }
    
    @EventHandler
    public void onPlayerUse(PlayerInteractEvent event) {
    	if (event != null ) {
    		if (event.getPlayer() != null && (event.getAction().equals(Action.RIGHT_CLICK_AIR) || event.getAction().equals(Action.RIGHT_CLICK_BLOCK))) {
    			Player target = event.getPlayer();
    			
    			if (target.getItemInHand().equals(new ItemStack(Material.STICK, 1))) {
    				
    				target.sendMessage("SOS STICKOLO!");
    				
    			} else if (target.getItemInHand().equals(new ItemStack(Material.BLAZE_ROD,1))) {
    				
    				target.sendMessage("SOS BLAZZE");
    				
    			} else if (target.getItemInHand().equals(new ItemStack(Material.CARROT_ON_A_STICK,1))) {
    				
    				target.sendMessage("SOS KAKAROTTI");
    				
    			}
    		}
    	}
    }
    
    @EventHandler
    public void onPlayerClickSign(PlayerInteractEvent event){
    	
    	if (event != null) {
    		if (event.getPlayer() != null && event.getClickedBlock() != null) { 
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
                    	
                    } else if (sign.getLine(0).equalsIgnoreCase("(start)")) {
                    	
                    	start(target);
                    	
                    } else if (sign.getLine(0).contentEquals("(chooselevel)")){
                    	
                    	getLogger().info("In chooselevel!");
                    	
                    	if (lobby.getWorld().getBlockAt( sign.getX()+1, sign.getY(), sign.getZ() ).getType().equals(Material.AIR) || lobby.getWorld().getBlockAt( sign.getX()-1, sign.getY(), sign.getZ() ).getType().equals(Material.AIR)) {
                    		
	                    	showlevelscreen(target, sign.getX(), sign.getX(), sign.getY()-1, sign.getY()+1, sign.getZ()-1, sign.getZ()+1, sign);
                    		
                    	}
                    	
                    	if (lobby.getWorld().getBlockAt(  sign.getX(), sign.getY(), sign.getZ()+1 ).getType().equals(Material.AIR) || lobby.getWorld().getBlockAt(  sign.getX(), sign.getY(), sign.getZ()-1 ).getType().equals(Material.AIR)) {
                    		
                    		showlevelscreen(target, sign.getX()-1, sign.getX()+1, sign.getY()-1, sign.getY()+1, sign.getZ(), sign.getZ(), sign);
                    		
                    	}
                    	
                    } else if (sign.getLine(0).contentEquals("(applylevel)")) {
                    	
                    	if ( teams.get( target.getUniqueId().toString()).equalsIgnoreCase("jaeger")) {
                    		                  		
                    		int rlevel = Klassen.getInstance().jaegerMeta(target, meta.get(target.getUniqueId().toString()))[0];
                    		int blevel = Klassen.getInstance().jaegerMeta(target, meta.get(target.getUniqueId().toString()))[1];
                    		int slevel = Klassen.getInstance().jaegerMeta(target, meta.get(target.getUniqueId().toString()))[2];
                    		
                    		String newmeta = "";
                    		
                    		if ( sign.getLine(1).equalsIgnoreCase("rüstung") ) {
                    			
                    			rlevel++;
                    			
                    			
                    		} else if (sign.getLine(1).equalsIgnoreCase("bogen")) {
                    			
                    			blevel++;
                    			
                    		
                    		} else if (sign.getLine(1).equalsIgnoreCase("schwert")) {
                    			
                    			slevel++;
                    		
                    		}
                    		
                    		newmeta = "r:" + rlevel + ",b:" + blevel + ",s:" + slevel;
                			
                			levelclass(target, newmeta);
                    		
                    		meta.put(target.getUniqueId().toString(), newmeta);
                    		
                    		findMidSign(sign);
                    		
                    	}
                    	
                    }
                    
                } 
    		}
    		
    	}

    }
    
    private void findMidSign(Sign sign) {
    	
    	for (int px = sign.getX()-1; px <= sign.getX()+1; px++) {
    		
    		for (int py = sign.getY()-1; py <= sign.getY()+1; py++) {
    			
    			for (int pz = sign.getZ()-1; pz <= sign.getZ()+1; pz++) {
    				
    				if (spawn.getWorld().getBlockAt(px,py,pz).getState() instanceof Sign) {
    					
    					Sign tmp = (Sign) spawn.getWorld().getBlockAt(px, py, pz).getState();
    					
    					if (tmp.getLine(0).equals("(chooselevel)")) {
    						
    						dellevelscreen(px - 1, px + 1, py - 1, py + 1, pz - 1, pz + 1, tmp);
    						
    					}
    				}
    				
    			}
    			
    		}
    		
    	}
    	
    }
    
    private void dellevelscreen(int minx, int maxx, int miny, int maxy, int minz, int maxz, Sign sign) {
    	
    	int signindex = 0;
    	
			for (int py = maxy; py >= miny; py--) {
	    		for (int pz = minz; pz <= maxz; pz++) {
	    			for (int px = minx; px <= maxx; px++) {
	    			
	    				if (signindex == 4) {
	    					
	    				} else {
	    					editSign(px, py, pz, sign, "", "", "", "");
	    				}
	    				
	    				signindex++;
	    			
	    			}
	    		}
	    	}
		}
    
    private int[] lowestsnd(int arr[]) { 
        int first, second, arr_size = arr.length;
        int firstindex = 0, secondindex = 0;
  
        first = second = Integer.MAX_VALUE; 
        for (int i = 0; i < arr_size ; i ++) 
        { 
            /* If current element is smaller than first 
              then update both first and second */
            if (arr[i] < first) 
            { 
                second = first; 
                first = arr[i]; 
                firstindex = i;
            } 
  
            /* If arr[i] is in between first and second 
               then update second  */
            else if (arr[i] < second && i != firstindex) {
                second = arr[i]; 
                secondindex = i;
            }
        } 
        int[] ret = {firstindex, secondindex};
        
        return ret;
    } 
    
    private void showlevelscreen(Player target, int minx, int maxx, int miny, int maxy, int minz, int maxz, Sign sign) {
    	
    	int signindex = 0;
    	
    	if (teams.get(target.getUniqueId().toString()) == null) {
 			target.sendMessage("Nicht in Team!");
     		dellevelscreen(minx, maxx, miny, maxy, minz, maxz, sign);
    	} else if (level.get( target.getUniqueId().toString() ) == (Klassen.getInstance().MAXLEVEL.get(teams.get(target.getUniqueId().toString())) - 1)) {
			target.sendTitle("Max Level!", "Du kannst nicht weiter aufsteigen!");
			dellevelscreen(minx, maxx, miny, maxy, minz, maxz, sign);
		} else if (score.get(target.getUniqueId().toString()) < Klassen.getInstance().jager_score[level.get(target.getUniqueId().toString()) + 1]) {
    		target.sendTitle("Nicht genug Score", "Score=" + score.get(target.getUniqueId().toString()) + ", du benötigst score=" + Klassen.getInstance().jager_score[level.get(target.getUniqueId().toString()) + 1]);
    		dellevelscreen(minx, maxx, miny, maxy, minz, maxz, sign);
    	} else {
    		
    		dellevelscreen(minx, maxx, miny, maxy, minz, maxz, sign);
			
			for (int py = maxy; py >= miny; py--) {
	    		for (int pz = minz; pz <= maxz; pz++) {
	    			for (int px = minx; px <= maxx; px++) {
	    				
	        			if (signindex == 0) {
	        				
	        				editSign(px, py, pz, sign, "Alte Eigenschaft", "|", "\\/", "");
	        				
	        			} else if (signindex == 1) {
	        				
	        				
	        				
	        				editSign(px, py, pz, sign, "Du bist auf", "Level " + (level.get(target.getUniqueId().toString()) + 1) + "", "aufgestiegen", "-------------");
	        				
	        			} else if (signindex == 2) {
	        				
	        				editSign(px, py, pz, sign, "Neue Eigenschaft", "|", "\\/", "");
	        				
	        			} else if (signindex == 3) {
	        				
	        				if (teams.get(target.getUniqueId().toString()).equalsIgnoreCase("jaeger")) {
	        					       					
	        					editSign(px, py, pz, sign, "Pfeilcooldown", "", Klassen.getInstance().jaeger_bowcooldown[level.get(target.getUniqueId().toString())] + " Ticks", "");
	        					
	        				}
	        			
	        			} else if (signindex == 4) {
	        				
	        				// Mittleres Schild...
	        				
	        			} else if (signindex == 5) {
	        				
	        				if (teams.get(target.getUniqueId().toString()).equalsIgnoreCase("jaeger")) {
	        					
	        					editSign(px, py, pz, sign, "Pfeilcooldown", "", (Klassen.getInstance().jaeger_bowcooldown[level.get(target.getUniqueId().toString())+1]) + " Ticks", "");
	        					
	        				}
	        				
	        			} else if (signindex == 6) {
	        				
	        				
	        				if (teams.get(target.getUniqueId().toString()).equalsIgnoreCase("jaeger")) {
	        					
	        					int[] metalevel = Klassen.getInstance().jaegerMeta(target, meta.get(target.getUniqueId().toString()));
	        					int min = lowestsnd(metalevel)[0];
	        					
	        					if (min == 0) {
	        						
	        						//Rüstung
	        						
	        						editSign(px, py, pz, sign, "(applylevel)", "Rüstung", "", "");
	        						
	        					} else if (min == 1 ) {
	        						
	        						//Bogen
	        						
	        						editSign(px, py, pz, sign, "(applylevel)", "Bogen", "", "");
	        						
	        					} else if (min == 2 ) {
	        						
	        						//Schwert
	        						
	        						editSign(px, py, pz, sign, "(applylevel)", "Schwert", "", "");
	        						
	        					}
	        				
	        				}
	        				
	        			} else if (signindex == 7) {
	        				
	        				if (teams.get(target.getUniqueId().toString()).equalsIgnoreCase("jaeger")) {
	        					
	        					editSign(px, py, pz, sign, "<<<<<<<<<<<<", "Wähle ein", "Upgrade", ">>>>>>>>>>>>");
	            				
	        					
	        				}
	        				
	        			} else if (signindex == 8) {
	        				
	        				if (teams.get(target.getUniqueId().toString()).equalsIgnoreCase("jaeger")) {
	        					
	        					int[] metalevel = Klassen.getInstance().jaegerMeta(target, meta.get(target.getUniqueId().toString()));
	        					int snd = lowestsnd(metalevel)[1];
	        					
	        					if (snd == 0) {
	        						
	        						//Rüstung
	        						
	        						editSign(px, py, pz, sign, "(applylevel)", "Rüstung", "", "");
	        						
	        					} else if (snd == 1 ) {
	        						
	        						//Bogen
	        						
	        						editSign(px, py, pz, sign, "(applylevel)", "Bogen", "", "");
	        						
	        					} else if (snd == 2 ) {
	        						
	        						//Schwert
	        						
	        						editSign(px, py, pz, sign, "(applylevel)", "Schwert", "", "");
	        						
	        					}
	        					
	        					
	        				}
	        				
	        			}
	        			
	        			signindex++;
	        			
	    				
	    			}
	            }
	        }
			
		}
    	
    	
    	
    }
    
    private boolean editSign(int px, int py, int pz, Sign sign, String msg0, String msg1, String msg2, String msg3) {
    	Block block = lobby.getWorld().getBlockAt(px, py, pz);
    	
		if (block.getState() instanceof Sign && !(px == sign.getX() && py == sign.getY() && py == sign.getZ())) {
			Sign tmp = (Sign) block.getState();
			
			tmp.setLine(0, msg0);
			tmp.setLine(1, msg1);
			tmp.setLine(2, msg2);
			tmp.setLine(3, msg3);
			tmp.update(true);
			
			return true;
		}
		
		return false;
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
    		
    		if (args[0].equalsIgnoreCase("addscore")) {
    			Player target = (Player) sender;
    			
    			long curr = score.get( target.getUniqueId().toString() );
    			
    			curr += Integer.parseInt( args[1] );
    			
    			score.put( target.getUniqueId().toString() , curr);
    	
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
		score.put(target.getUniqueId().toString(), 0L);
		meta.put(target.getUniqueId().toString(), "r:0,b:0,s:0");
		
    	switch (classname) {
		case "jaeger":
	    	
	    	Klassen.getInstance().jaeger(0, target, "r:0,b:0,s:0");
			
			break;
		case "tank":
			
	    	Klassen.getInstance().tank(0, target, "");
			
			break;
		
		default:
			break;
		}
    	
    	return 1;
    }
    
    private void levelclass(Player target, String meta) {
    	
    	int newlevel = level.get(target.getUniqueId().toString()) + 1;
    	
    	if (Klassen.getInstance().MAXLEVEL.get( teams.get(target.getUniqueId().toString()) ) == newlevel) {
    		target.sendMessage("Du hast das maximale Level erreicht!");
    		
    		newlevel--;
    	}
    	
    	if (teams.get(target.getUniqueId().toString()).equalsIgnoreCase("jaeger")){
    		
    		Klassen.getInstance().jaeger(newlevel, target, meta);
    		
    	}
    	if (teams.get(target.getUniqueId().toString()).equalsIgnoreCase("tank")){
    		
    		Klassen.getInstance().tank(newlevel, target, meta);
    		
    	}
    	
    	level.put(target.getUniqueId().toString(), newlevel);
    	
    	
    }
}