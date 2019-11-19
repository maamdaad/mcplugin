package de.craftomania.plugin.MCPlugin;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.enchantments.EnchantmentTarget;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.HashMap;

public final class MCPlugin extends JavaPlugin {

	HashMap<String, String> teams = new HashMap<String, String>();		
	HashMap<String, ItemStack[]> inv = new HashMap<String, ItemStack[]>();
	HashMap<String, ItemStack[]> arm = new HashMap<String, ItemStack[]>();
	
	@Override
    public void onEnable() {
        getLogger().info("MCPlugin an!");
        
    }
    @Override
    public void onDisable() {
    	getLogger().info("MCPlugin aus!");
        
    }
	
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
    	if (cmd.getName().equalsIgnoreCase("arena")) { 
    		
    		checkCommand(sender, cmd, label, args);
    		
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
        		
        		joinClass(target, classname);
        		
        		
        	}
    		if (args[0].equalsIgnoreCase("leaveclass")) {
    			teams.remove(args[1]);
    			
    			Player p = Bukkit.getServer().getPlayer(args[1]);
    			
    			p.removePotionEffect(PotionEffectType.SPEED);
    			p.getInventory().clear();
    			p.sendMessage("Team verlassen!");
    			
    			p.getInventory().setContents(inv.get(p.getName()));
    			p.getInventory().setContents(arm.get(p.getName()));
    			
    		}
    	} else {
    		
    	}
    }
    
    private void joinClass(Player target, String classname) {
    	
    	inv.put(target.getName(), target.getInventory().getContents());
    	arm.put(target.getName(), target.getInventory().getArmorContents());
    	
    	target.sendMessage("Beitritt Klasse: " + classname);
    	
    	switch (classname) {
		case "jaeger":
			
			teams.put(target.getName(), classname);
			
			target.getInventory().clear();
			
			ItemStack boots = new ItemStack(Material.LEATHER_BOOTS, 1);
			
			boots.addEnchantment(Enchantment.BINDING_CURSE, 1);
			target.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 65000, 1));
			ItemMeta btmeta = boots.getItemMeta();
			btmeta.setDisplayName("Schuhe des Jägers");
			btmeta.setUnbreakable(true);
			boots.setItemMeta(btmeta);
			target.getInventory().setBoots(boots);
			
			ItemStack chestplate = new ItemStack(Material.LEATHER_CHESTPLATE, 1);
			chestplate.addEnchantment(Enchantment.BINDING_CURSE, 1);
			ItemMeta chmeta = chestplate.getItemMeta();
			chmeta.setDisplayName("Jacke des Jägers");
			chmeta.setUnbreakable(true);
			chestplate.setItemMeta(chmeta);
			target.getInventory().setChestplate(chestplate);
			
			target.sendMessage("Inventar übertragen!");
			target.sendMessage("Du bist jetzt in Klasse Jaeger!");
			
			break;
		case "tank":
			
			
			
			break;
		
		default:
			break;
		}
    }
    
}
