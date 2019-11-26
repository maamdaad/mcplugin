package de.craftomania.plugin.MCPlugin;
import java.util.ArrayList;
import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

/*0. Pfeil 3sekunden
1. Pfeil 2,5 sekunden
2. 5% auf slowness 	
3. 5% auf weakness
4. 5% auf poison	
5. 5% auf harming
6. Pfeil 2 sekunden 	
7. 10% auf s
8. 10% auf w		
9. 10% auf p
10. 10% auf h 		
11. P auf 1,5
12. 15% s		
13. 15% w
14. 15% p		
15. 15% h
16. P auf 1		
17. 20% s
18. 20% w		
19. 20% p
20. 20% h		
21. P auf 0.5
22. 25% s		
23. 25% w
24. 25% p		
25. 25% h
 */

public class Klassen {
	
	private static Klassen klassen;
	
	private static long ticks = 14;
	
	public HashMap<String, Integer> MAXLEVEL;
	
	public static long[] food_cooldown = {ticks * 10, ticks * 10, ticks*10, ticks*7, ticks*7, ticks*6, ticks*6, ticks*5, ticks*5, ticks*5, ticks*4};
	
	public static long MAX_food = 5;
	
	public static long[] jaeger_bowcooldown = {ticks * 3, ticks * 2, ticks * 2, ticks * 2, ticks * 1, ticks * 1, ticks * 1, ticks * 1, ticks * 1, ticks * 1};
	public static double[] jaeger_slowchance = {.25,.25,.25,.25,.25,.25,.25,.25,.25,.25,.25,.25,.25,.25,.25,.25,.25,.25};
	public static double[] jaeger_weakchange = {.25,.25,.25,.25,.25,.25,.25,.25,.25,.25,.25,.25,.25,.25,.25,.25,.25,.25};
	public static double[] jaeger_poischance = {.25,.25,.25,.25,.25,.25,.25,.25,.25,.25,.25,.25,.25,.25,.25,.25,.25,.25};
	public static double[] jaeger_damachance = {.25,.25,.25,.25,.25,.25,.25,.25,.25,.25,.25,.25,.25,.25,.25,.25,.25,.25};
	
	public static int[] jager_score = {0, 5, 10, 15, 20, 30, 40, 50, 60, 70, 80, 90, 100};
	
	public Klassen() {
		MAXLEVEL = new HashMap<String, Integer>();
		
		MAXLEVEL.put("jaeger", 10);
		MAXLEVEL.put("tank", 10);
	}
	
	public static Klassen getInstance() {
		if (Klassen.klassen == null) {
			Klassen.klassen = new Klassen();
		}
		return Klassen.klassen;
	}
	
	public int[] jaegerMeta(Player target, String meta) {
		
		String[] atmp = meta.split(",");
		
		int rlevel = 0;
		int blevel = 0;
		int slevel = 0;
		
		for (String s : atmp) {
			
			if (s.contains("r:")) {
				
				rlevel = Integer.parseInt(s.split(":")[1]);
				
			} else if (s.contains("b:")) {
				
				blevel = Integer.parseInt(s.split(":")[1]);
				
			} else if (s.contains("s:")) {
				
				slevel = Integer.parseInt(s.split(":")[1]);
				
			}
			
		}
		
		int[] ret = {rlevel, blevel, slevel};
		
		return ret;
	}
	
	public void jaeger(int level, Player target, String meta) {
		
		int rlevel = jaegerMeta(target, meta)[0];
		int blevel = jaegerMeta(target, meta)[1];
		int slevel = jaegerMeta(target, meta)[2];
		
		if (blevel == 1) {
			target.sendMessage("Bogen Level 2");
			
			int bpos = 0;
			ItemStack bogen2 = new ItemStack(Material.BOW, 1);
			bogen2.addEnchantment(Enchantment.ARROW_DAMAGE, 1);
    		ItemMeta bmeta = bogen2.getItemMeta();
    		bmeta.setDisplayName("Bogen des Jägers II");
    		bmeta.setUnbreakable(true);
    		bogen2.setItemMeta(bmeta);
			for (ItemStack item : target.getInventory().getContents()) {
	    		if (item != null) {
	    			if (item.getItemMeta().getDisplayName().equalsIgnoreCase("Bogen des Jägers I")) {
		    			target.getInventory().setItem(bpos, bogen2);
		    		}
	    		}
	    		bpos++;
		    		
	    	}
		}
		
		if (rlevel == 1) {
			target.sendMessage("Rüstung Level 2");
			
			ItemStack helm = new ItemStack(Material.LEATHER_HELMET, 1);
    		helm.addEnchantment(Enchantment.BINDING_CURSE, 1);
    		ItemMeta hmeta = helm.getItemMeta();
    		hmeta.setDisplayName("Rüstung des Jägers II");
    		hmeta.setUnbreakable(true);
    		hmeta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
    		ArrayList<String> lore = new ArrayList<String>();
    		lore.add("+ 1 armor");
    		hmeta.setLore(lore);
    		helm.setItemMeta(hmeta);
    		target.getInventory().setHelmet(helm);
    		
    		ItemStack chestplate = new ItemStack(Material.LEATHER_CHESTPLATE, 1);
    		chestplate.addEnchantment(Enchantment.BINDING_CURSE, 1);
    		ItemMeta chmeta = chestplate.getItemMeta();
    		chmeta.setDisplayName("Rüstung des Jägers II");
    		chmeta.setUnbreakable(true);
    		chmeta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
    		lore = new ArrayList<String>();
    		lore.add("+ 3 armor");
    		chmeta.setLore(lore);
    		chestplate.setItemMeta(chmeta);
    		target.getInventory().setChestplate(chestplate);
    		
    		ItemStack leggins = new ItemStack(Material.LEATHER_LEGGINGS, 1);
    		leggins.addEnchantment(Enchantment.BINDING_CURSE, 1);
    		ItemMeta lmeta = leggins.getItemMeta();
    		lmeta.setDisplayName("Rüstung des Jägers II");
    		lmeta.setUnbreakable(true);
    		lmeta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
    		lore = new ArrayList<String>();
    		lore.add("+ 2 armor");
    		lmeta.setLore(lore);
    		leggins.setItemMeta(lmeta);
    		target.getInventory().setLeggings(leggins);
    		
    		ItemStack boots = new ItemStack(Material.LEATHER_BOOTS, 1);
    		boots.addEnchantment(Enchantment.BINDING_CURSE, 1);
    		ItemMeta btmeta = boots.getItemMeta();
    		btmeta.setDisplayName("Rüstung des Jägers II");
    		btmeta.setUnbreakable(true);
    		btmeta.addAttributeModifier(Attribute.GENERIC_MOVEMENT_SPEED, new AttributeModifier("generic.movementspeed", 0.015, AttributeModifier.Operation.ADD_NUMBER));
    		btmeta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
    		lore = new ArrayList<String>();
    		lore.add("+ 15% movement speed");
    		lore.add("+ 1 armor");
    		btmeta.setLore(lore);
    		boots.setItemMeta(btmeta);
    		target.getInventory().setBoots(boots);
		}
		
		if (slevel == 1) {
			
			target.sendMessage("Jagdmesser Level 2");
			
			int spos = 0;
			ItemStack sword2 = new ItemStack(Material.WOODEN_SWORD, 1);
			sword2.addEnchantment(Enchantment.DAMAGE_ALL, 1);
    		ItemMeta smeta = sword2.getItemMeta();
    		smeta.setDisplayName("Jagdmesser II");
    		smeta.setUnbreakable(true);
    		sword2.setItemMeta(smeta);
			for (ItemStack item : target.getInventory().getContents()) {
	    		if (item != null) {
	    			if (item.getItemMeta().getDisplayName().equalsIgnoreCase("Jagdmesser I")) {
		    			target.getInventory().setItem(spos, sword2);
		    		}
	    		}
	    		spos++;
		    		
	    	}
		}
		
		if (blevel == 2) {
			target.sendMessage("Bogen Level 3");
			
			int bpos = 0;
			ItemStack bogen3 = new ItemStack(Material.BOW, 1);
			bogen3.addEnchantment(Enchantment.ARROW_DAMAGE, 2);
    		ItemMeta bmeta = bogen3.getItemMeta();
    		bmeta.setDisplayName("Bogen des Jägers III");
    		bmeta.setUnbreakable(true);
    		bogen3.setItemMeta(bmeta);
			for (ItemStack item : target.getInventory().getContents()) {
	    		if (item != null) {
	    			if (item.getItemMeta().getDisplayName().equalsIgnoreCase("Bogen des Jägers II")) {
		    			target.getInventory().setItem(bpos, bogen3);
		    		}
	    		}
	    		bpos++;
		    		
	    	}
		}
		
		if (rlevel == 2) {
			target.sendMessage("Rüstung Level 3");
			
			ItemStack helm = new ItemStack(Material.LEATHER_HELMET, 1);
    		helm.addEnchantment(Enchantment.BINDING_CURSE, 1);
    		helm.addEnchantment(Enchantment.PROTECTION_PROJECTILE, 2);
    		ItemMeta hmeta = helm.getItemMeta();
    		hmeta.setDisplayName("Rüstung des Jägers III");
    		hmeta.setUnbreakable(true);
    		hmeta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
    		ArrayList<String> lore = new ArrayList<String>();
    		lore.add("+ 1 armor");
    		hmeta.setLore(lore);
    		helm.setItemMeta(hmeta);
    		target.getInventory().setHelmet(helm);
    		
    		ItemStack chestplate = new ItemStack(Material.CHAINMAIL_CHESTPLATE, 1);
    		chestplate.addEnchantment(Enchantment.BINDING_CURSE, 1);
    		ItemMeta chmeta = chestplate.getItemMeta();
    		chmeta.setDisplayName("Rüstung des Jägers III");
    		chmeta.setUnbreakable(true);
    		chmeta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
    		lore = new ArrayList<String>();
    		lore.add("+ 5 armor");
    		chmeta.setLore(lore);
    		chestplate.setItemMeta(chmeta);
    		target.getInventory().setChestplate(chestplate);
    		
    		ItemStack leggins = new ItemStack(Material.LEATHER_LEGGINGS, 1);
    		leggins.addEnchantment(Enchantment.BINDING_CURSE, 1);
    		ItemMeta lmeta = leggins.getItemMeta();
    		lmeta.setDisplayName("Rüstung des Jägers III");
    		lmeta.setUnbreakable(true);
    		lmeta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
    		lore = new ArrayList<String>();
    		lore.add("+ 2 armor");
    		lmeta.setLore(lore);
    		leggins.setItemMeta(lmeta);
    		target.getInventory().setLeggings(leggins);
    		
    		ItemStack boots = new ItemStack(Material.LEATHER_BOOTS, 1);
    		boots.addEnchantment(Enchantment.BINDING_CURSE, 1);
    		ItemMeta btmeta = boots.getItemMeta();
    		btmeta.setDisplayName("Rüstung des Jägers III");
    		btmeta.setUnbreakable(true);
    		btmeta.addAttributeModifier(Attribute.GENERIC_MOVEMENT_SPEED, new AttributeModifier("generic.movementspeed", 0.015, AttributeModifier.Operation.ADD_NUMBER));
    		btmeta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
    		lore = new ArrayList<String>();
    		lore.add("+ 15% movement speed");
    		lore.add("+ 1 armor");
    		btmeta.setLore(lore);
    		boots.setItemMeta(btmeta);
    		target.getInventory().setBoots(boots);
		}
		
		if (slevel == 2) {
			
			target.sendMessage("Jagdmesser Level 3");
			
			int spos = 0;
			ItemStack sword3 = new ItemStack(Material.WOODEN_SWORD, 1);
			sword3.addEnchantment(Enchantment.DAMAGE_ALL, 2);
    		ItemMeta smeta = sword3.getItemMeta();
    		smeta.setDisplayName("Jagdmesser III");
    		smeta.setUnbreakable(true);
    		sword3.setItemMeta(smeta);
			for (ItemStack item : target.getInventory().getContents()) {
	    		if (item != null) {
	    			if (item.getItemMeta().getDisplayName().equalsIgnoreCase("Jagdmesser II")) {
		    			target.getInventory().setItem(spos, sword3);
		    		}
	    		}
	    		spos++;
		    		
	    	}
		}
		
		if (level == 0) {
	   		
    		ItemStack boots = new ItemStack(Material.LEATHER_BOOTS, 1);
    		
    		boots.addEnchantment(Enchantment.BINDING_CURSE, 1);
    		ItemMeta btmeta = boots.getItemMeta();
    		btmeta.setDisplayName("Rüstung des Jägers I");
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
    		chmeta.setDisplayName("Rüstung des Jägers I");
    		chmeta.setUnbreakable(true);
    		chmeta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
    		lore = new ArrayList<String>();
    		lore.add("+ 3 armor");
    		chmeta.setLore(lore);
    		chestplate.setItemMeta(chmeta);
    		target.getInventory().setChestplate(chestplate);
    		
    		ItemStack bow = new ItemStack(Material.BOW, 1);
    		ItemMeta bmeta = bow.getItemMeta();
    		bmeta.setDisplayName("Bogen des Jägers I");
    		bmeta.setUnbreakable(true);
    		bow.setItemMeta(bmeta);
    		target.getInventory().setItem(0, bow);
    		
    		ItemStack sword = new ItemStack(Material.WOODEN_SWORD,1);
    		ItemMeta smeta = sword.getItemMeta();
    		smeta.setDisplayName("Jagdmesser I");
    		smeta.setUnbreakable(true);
    		sword.setItemMeta(smeta);
    		target.getInventory().setItem(1, sword);
    		
    		ItemStack arrow = new ItemStack(Material.ARROW,1);
    		target.getInventory().addItem(arrow);
    		
    		target.sendMessage("Inventar übertragen!");
    		target.sendMessage("Du bist jetzt in Klasse Jaeger!");
    		
    	
    	} else if (level == 1) {
    		
    		target.sendMessage("Du bist jetzt Jäger Level 2");
    		
    	} else if (level == 2) {
    		
    		target.sendMessage("Du bist jetzt Jäger Level 3");
    		
    	} else if (level == 3) {
    		
    		target.sendMessage("Du bist jetzt Jäger Level 4");
    		
    	} else if (level == 4) {
    		
    		target.sendMessage("Du bist jetzt Jäger Level 5");
    		
    	} else if (level == 5) {
    		
    		target.sendMessage("Du bist jetzt Jäger Level 6");
    		
    	} else if (level == 6) {
    		
    		target.sendMessage("Du bist jetzt Jäger Level 7");
    		
    	} else if (level == 7) {
    		
    		target.sendMessage("Du bist jetzt Jäger Level 8");
    		
    	} else if (level == 8) {
    		
    		target.sendMessage("Du bist jetzt Jäger Level 9");
    		
    	} else if (level == 9) {
    		
    		target.sendMessage("Du bist jetzt Jäger Level 10");
    		
    	} 
		
		
	}
	
	public void tank(int level, Player target, String meta) {
		if (level == 0) {
			
			ItemStack boots = new ItemStack(Material.IRON_BOOTS,1);
			ItemStack chest = new ItemStack(Material.IRON_CHESTPLATE,1);
			ItemStack helm = new ItemStack(Material.IRON_HELMET,1);
			
			ItemStack sword = new ItemStack(Material.STONE_SWORD,1);
	   		
			
			boots.addEnchantment(Enchantment.BINDING_CURSE, 1);
			boots.addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 1);
			ItemMeta bmeta = boots.getItemMeta();
			bmeta.setUnbreakable(true);
			bmeta.setDisplayName("Schuhe des Tanks");
			bmeta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
			ArrayList<String> lore = new ArrayList<String>();
			lore.add("+ 2 armor");
			bmeta.setLore(lore);
			boots.setItemMeta(bmeta);
			
			chest.addEnchantment(Enchantment.BINDING_CURSE, 1);
			chest.addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 1);
			ItemMeta chmeta = chest.getItemMeta();
			chmeta.setUnbreakable(true);
			chmeta.setDisplayName("Rüstung des Tanks");
			chmeta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
			lore = new ArrayList<String>();
			lore.add("+ 6 armor");
			chmeta.setLore(lore);
			chest.setItemMeta(chmeta);
			
			helm.addEnchantment(Enchantment.BINDING_CURSE, 1);
			helm.addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 1);
			ItemMeta hmeta = helm.getItemMeta();
			hmeta.setUnbreakable(true);
			hmeta.setDisplayName("Helm des Tanks");
			hmeta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
			lore = new ArrayList<String>();
			lore.add("+ 2 armor");
			hmeta.setLore(lore);
			helm.setItemMeta(hmeta);
			
			ItemMeta smeta = sword.getItemMeta();
			smeta.setUnbreakable(true);
			smeta.setDisplayName("Schwert des Tanks");
			smeta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
			lore = new ArrayList<String>();
			lore.add("5 attack damage");
			smeta.setLore(lore);
			sword.setItemMeta(smeta);
			
			target.getInventory().setChestplate(chest);
			target.getInventory().setBoots(boots);
			target.getInventory().setHelmet(helm);
			target.getInventory().setItem(0, sword);
			
    		target.sendMessage("Inventar übertragen!");
    		target.sendMessage("Du bist jetzt in Klasse Tank!");
    		
    	
    	} else if (level == 1) {
    		
    		target.sendMessage("Du bist jetzt Tank Level 2");
    		
    	} else if (level == 2) {
    		
    		target.sendMessage("Du bist jetzt Tank Level 3");
    		
    	} else if (level == 3) {
    		
    		target.sendMessage("Du bist jetzt Tank Level 4");
    		
    	} else if (level == 4) {
    		
    		target.sendMessage("Du bist jetzt Tank Level 5");
    		
    	} else if (level == 5) {
    		
    		target.sendMessage("Du bist jetzt Tank Level 6");
    		
    	} else if (level == 6) {
    		
    		target.sendMessage("Du bist jetzt Tank Level 7");
    		
    	} else if (level == 7) {
    		
    		target.sendMessage("Du bist jetzt Tank Level 8");
    		
    	} else if (level == 8) {
    		
    		target.sendMessage("Du bist jetzt Tank Level 9");
    		
    	} else if (level == 9) {
    		
    		target.sendMessage("Du bist jetzt Tank Level 10");
    		
    	}
		
	}
	
	public void healer(int level, Player target, String meta) {
		
		if (level == 0) {
			
			
			
			target.sendMessage("Inventar übertragen!");
			target.sendMessage("Du bist jetzt in der Klasse Healer");
			
		} else if (level == 1) {
			
			target.sendMessage("Du bist jetzt Healer Level 2");
			
		} else if (level == 2) {

			target.sendMessage("Du bist jetzt Healer Level 2");
			
		} else if (level == 3) {

			target.sendMessage("Du bist jetzt Healer Level 2");
			
		} else if (level == 4) {

			target.sendMessage("Du bist jetzt Healer Level 2");
			
		} else if (level == 5) {

			target.sendMessage("Du bist jetzt Healer Level 2");
			
		} else if (level == 6) {

			target.sendMessage("Du bist jetzt Healer Level 2");
			
		} else if (level == 7) {

			target.sendMessage("Du bist jetzt Healer Level 2");
			
		} else if (level == 8) {

			target.sendMessage("Du bist jetzt Healer Level 2");
			
		} else if (level == 9) {

			target.sendMessage("Du bist jetzt Healer Level 2");
			
		} 
		
	}
	
	public void mage(int level, Player target, String meta) {
		
	}
	
	public void alchemist(int level, Player target, String meta) {
		
	}
	
	public void assasin(int level, Player target, String meta) {
		
	}
	
	
}
