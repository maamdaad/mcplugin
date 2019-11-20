package de.craftomania.plugin.MCPlugin;

import java.util.ArrayList;
import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
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
	
	long ticks = 14;
	
	long[] bowcooldown = {ticks * 3, ticks * 2, ticks * 2, ticks * 2, ticks * 1, ticks * 1, ticks * 1, ticks * 1};
	
	@Override
    public void onEnable() {
        getLogger().info("MCPlugin an!");
        Bukkit.getServer().getPluginManager().registerEvents(this, this);
    }
    @Override
    public void onDisable() {
    	getLogger().info("MCPlugin aus!");
        
    }
    
    private void start() {
    	
    	
    	
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
		} else {
			target.sendMessage("Player nicht in Team");
		}
    }
    
    @EventHandler
    public void onPlayerShoot(EntityShootBowEvent e) {
    	
    	if (e.getEntity() instanceof Player) {
    		final Player target = (Player) e.getEntity();
        	
        	if (teams.get( target.getUniqueId().toString() ).equalsIgnoreCase("jaeger")) {
        		target.sendMessage("Arrow geschossen! Cooldown: " + bowcooldown[ level.get(target.getUniqueId().toString()) ] + " ticks");
            	
        		getServer().getScheduler().scheduleSyncDelayedTask(this, new Runnable() {
        		    public void run() {
        		    	ItemStack a = new ItemStack(Material.ARROW,1);
                    	
                    	target.getInventory().addItem(a);
        		    }
        		}, bowcooldown[ level.get(target.getUniqueId().toString()) ]);

        	}
    	}
    }
    
    @EventHandler
    public void onPlayerClickSign(PlayerInteractEvent event){
        Player target = event.getPlayer();
        if(event.getClickedBlock().getType() == Material.OAK_SIGN || event.getClickedBlock().getType() == Material.OAK_WALL_SIGN){
           
            Sign sign = (Sign) event.getClickedBlock().getState();
            
            if (sign.getLine(0).equalsIgnoreCase("(join)")) {
            	
            	joinClass(target, sign.getLine(1));
            	
            } else if (sign.getLine(0).equalsIgnoreCase("(leave)")) {
            	
            	leaveteam(target);
            	
            } else if (sign.getLine(0).equalsIgnoreCase("(levelup)")) {
            	
            	levelclass(target);
            	
            } else if (sign.getLine(0).equalsIgnoreCase("(start)")) {
            	
            	start();
            	
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
    			
    			target.sendMessage("Du bist in Team: " + teams.get(target.getUniqueId().toString()));
    		}
    		
    		if (args[0].equalsIgnoreCase("inlevel")) {
    			Player target = Bukkit.getServer().getPlayer(args[1]);
    			
    			target.sendMessage("Du bist in Level: " + level.get(target.getUniqueId().toString()));
    		}
    		
    	} else {
    		
    	}
    }
    
    private int joinClass(Player target, String classname) {
    	
    	gamemodes.put(target.getUniqueId().toString(), target.getGameMode());
    	
    	target.setGameMode(GameMode.SURVIVAL);
    	
    	inv.put(target.getUniqueId().toString(), target.getInventory().getContents());
    	arm.put(target.getUniqueId().toString(), target.getInventory().getArmorContents());
    	
    	target.sendMessage("Beitritt Klasse: " + classname);
		
		target.getInventory().clear();
    	
    	switch (classname) {
		case "jaeger":
			
			teams.put(target.getUniqueId().toString(), classname);
			jaeger(0, target);
			
			break;
		case "tank":
			
			
			
			break;
		
		default:
			break;
		}
    	
    	return 1;
    }
    
    private void levelclass(Player target) {
    	
    	int newlevel = level.get(target.getUniqueId().toString()) + 1;
    	
    	if (teams.get(target.getUniqueId().toString()).equalsIgnoreCase("jaeger")){
    		
    		jaeger(newlevel, target);
    		
    	}
    	if (teams.get(target.getUniqueId().toString()).equalsIgnoreCase("tank")){
    		
    		tank(newlevel, target);
    		
    	}
    	
    	
    }
    
    private void jaeger(int level, Player target) {
    	
    	this.level.put(target.getUniqueId().toString(), level);
    	
    	if (level == 0) {
    		   		
    		ItemStack boots = new ItemStack(Material.LEATHER_BOOTS, 1);
    		
    		boots.addEnchantment(Enchantment.BINDING_CURSE, 1);
    		ItemMeta btmeta = boots.getItemMeta();
    		btmeta.setDisplayName("Schuhe des Jägers");
    		btmeta.setUnbreakable(true);
    		btmeta.addAttributeModifier(Attribute.GENERIC_MOVEMENT_SPEED, new AttributeModifier("generic.movementspeed", 0.015, AttributeModifier.Operation.ADD_NUMBER));
    		btmeta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
    		ArrayList<String> lore = new ArrayList<String>();
    		lore.add("+ 15% movement speed");
    		lore.add("+ 1 armor");
    		btmeta.setLore(lore);
    		boots.setItemMeta(btmeta);
    		target.getInventory().setBoots(boots);
    		
    		ItemStack chestplate = new ItemStack(Material.LEATHER_CHESTPLATE, 1);
    		chestplate.addEnchantment(Enchantment.BINDING_CURSE, 1);
    		ItemMeta chmeta = chestplate.getItemMeta();
    		chmeta.setDisplayName("Jacke des Jägers");
    		chmeta.setUnbreakable(true);
    		chmeta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
    		lore = new ArrayList<String>();
    		lore.add("+ 3 armor");
    		chmeta.setLore(lore);
    		chestplate.setItemMeta(chmeta);
    		target.getInventory().setChestplate(chestplate);
    		
    		ItemStack bow = new ItemStack(Material.BOW, 1);
    		ItemMeta bmeta = bow.getItemMeta();
    		bmeta.setDisplayName("Bogen des Jägers");
    		bmeta.setUnbreakable(true);
    		bow.setItemMeta(bmeta);
    		target.getInventory().setItem(0, bow);
    		
    		ItemStack sword = new ItemStack(Material.WOODEN_SWORD,1);
    		ItemMeta smeta = sword.getItemMeta();
    		smeta.setDisplayName("Schwert des Jägers");
    		smeta.setUnbreakable(true);
    		sword.setItemMeta(smeta);
    		target.getInventory().setItem(1, sword);
    		
    		ItemStack arrow = new ItemStack(Material.ARROW,1);
    		target.getInventory().addItem(arrow);
    		
    		target.sendMessage("Inventar übertragen!");
    		target.sendMessage("Du bist jetzt in Klasse Jaeger!");
    		
    	
    	} else if (level == 1) {
    		
    		target.sendMessage("Du bist jetzt Jäger Level 2");
    		
    	}
    }
    
    private void tank(int level, Player target) {
    	
    }
    
}
