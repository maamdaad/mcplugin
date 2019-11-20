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
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.block.*;
import org.bukkit.plugin.java.JavaPlugin;

public final class MCPlugin extends JavaPlugin implements Listener {

	HashMap<String, String> teams = new HashMap<String, String>();		
	HashMap<String, ItemStack[]> inv = new HashMap<String, ItemStack[]>();
	HashMap<String, ItemStack[]> arm = new HashMap<String, ItemStack[]>();
	
	@Override
    public void onEnable() {
        getLogger().info("MCPlugin an!");
        Bukkit.getServer().getPluginManager().registerEvents(this, this);
    }
    @Override
    public void onDisable() {
    	getLogger().info("MCPlugin aus!");
        
    }
    
    @EventHandler
    public void onPlayerClickSign(PlayerInteractEvent event){
        Player p = event.getPlayer();
        if(event.getClickedBlock().getType() == Material.OAK_SIGN || event.getClickedBlock().getType() == Material.OAK_WALL_SIGN){
            p.sendMessage("Sie haben ein Schild angeclickt!!!");
            Sign sign = (Sign) event.getClickedBlock().getState();
            
            p.sendMessage("Line 1: " + sign.getLine(0));
            p.sendMessage("Line 2: " + sign.getLine(1));
            p.sendMessage("Line 3: " + sign.getLine(2));
            p.sendMessage("Line 4: " + sign.getLine(3));
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
				
				if (teams.get(target.getUniqueId().toString()) != null) {
					target.getInventory().clear();
					target.sendMessage("Team verlassen!");
				
					target.getInventory().setContents(inv.get(target.getUniqueId().toString()));
					target.getInventory().setArmorContents(arm.get(target.getUniqueId().toString()));
				
					arm.remove(target.getUniqueId().toString());
					inv.remove(target.getUniqueId().toString());
					teams.remove(target.getUniqueId().toString());
				} else {
					target.sendMessage("Player nicht in Team");
				}
				
				
    		}
    		
    		if (args[0].equalsIgnoreCase("inteam")) {
    			Player target = Bukkit.getServer().getPlayer(args[1]);
    			
    			target.sendMessage("Du bist in Team: " + teams.get(target.getUniqueId().toString()));
    		}
    		
    	} else {
    		
    	}
    }
    
    private int joinClass(Player target, String classname) {
    	
    	target.setGameMode(GameMode.SURVIVAL);
    	
    	inv.put(target.getUniqueId().toString(), target.getInventory().getContents());
    	arm.put(target.getUniqueId().toString(), target.getInventory().getArmorContents());
    	
    	target.sendMessage("Beitritt Klasse: " + classname);
		
		target.getInventory().clear();
    	
    	switch (classname) {
		case "jaeger":
			
			teams.put(target.getUniqueId().toString(), classname);
			jaeger(0, target, classname);
			
			break;
		case "tank":
			
			
			
			break;
		
		default:
			break;
		}
    	
    	return 1;
    }
    
    private void jaeger(int level, Player target, String classname) {
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
    		
    		target.sendMessage("Inventar übertragen!");
    		target.sendMessage("Du bist jetzt in Klasse Jaeger!");
    	} else if (level == 1) {
    		
    	}
    }
    
}
