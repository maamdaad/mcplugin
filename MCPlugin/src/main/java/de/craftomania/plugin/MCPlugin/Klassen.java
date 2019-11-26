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

public class Klassen {
	
	private static Klassen klassen;
	
	private static long ticks = 14;
	
	public HashMap<String, Integer> MAXLEVEL;
	
	public static long[] food_cooldown = {ticks * 10, ticks * 10, ticks*10, ticks*7, ticks*7, ticks*6, ticks*6, ticks*5, ticks*5, ticks*5, ticks*4};
	
	public static long MAX_food = 5;
	
	public static long[] jaeger_bowcooldown = {ticks * 3, ticks * 2, ticks * 2, ticks * 2, ticks * 1, ticks * 1, ticks * 1, ticks * 1, ticks * 1, ticks * 1};
	
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
    		bmeta.setDisplayName("Bogen des J�gers II");
    		bmeta.setUnbreakable(true);
    		bogen2.setItemMeta(bmeta);
			for (ItemStack item : target.getInventory().getContents()) {
	    		if (item != null) {
	    			if (item.getItemMeta().getDisplayName().equalsIgnoreCase("Bogen des J�gers I")) {
		    			target.getInventory().setItem(bpos, bogen2);
		    		}
	    		}
	    		bpos++;
		    		
	    	}
		}
		
		if (rlevel == 1) {
			target.sendMessage("R�stung Level 2");
			
			ItemStack helm = new ItemStack(Material.LEATHER_HELMET, 1);
    		helm.addEnchantment(Enchantment.BINDING_CURSE, 1);
    		ItemMeta hmeta = helm.getItemMeta();
    		hmeta.setDisplayName("R�stung des J�gers II");
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
    		chmeta.setDisplayName("R�stung des J�gers II");
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
    		lmeta.setDisplayName("R�stung des J�gers II");
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
    		btmeta.setDisplayName("R�stung des J�gers II");
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
			target.sendMessage("Schwert Level 2");
		}
		
		if (blevel == 2) {
			target.sendMessage("Bogen Level 3");
		}
		
		if (rlevel == 2) {
			target.sendMessage("R�stung Level 3");
		}
		
		if (slevel == 2) {
			target.sendMessage("Schwert Level 3");
		}
		
		if (level == 0) {
	   		
    		ItemStack boots = new ItemStack(Material.LEATHER_BOOTS, 1);
    		
    		boots.addEnchantment(Enchantment.BINDING_CURSE, 1);
    		ItemMeta btmeta = boots.getItemMeta();
    		btmeta.setDisplayName("R�stung des J�gers I");
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
    		chmeta.setDisplayName("R�stung des J�gers I");
    		chmeta.setUnbreakable(true);
    		chmeta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
    		lore = new ArrayList<String>();
    		lore.add("+ 3 armor");
    		chmeta.setLore(lore);
    		chestplate.setItemMeta(chmeta);
    		target.getInventory().setChestplate(chestplate);
    		
    		ItemStack bow = new ItemStack(Material.BOW, 1);
    		ItemMeta bmeta = bow.getItemMeta();
    		bmeta.setDisplayName("Bogen des J�gers I");
    		bmeta.setUnbreakable(true);
    		bow.setItemMeta(bmeta);
    		target.getInventory().setItem(0, bow);
    		
    		ItemStack sword = new ItemStack(Material.WOODEN_SWORD,1);
    		ItemMeta smeta = sword.getItemMeta();
    		smeta.setDisplayName("Schwert des J�gers");
    		smeta.setUnbreakable(true);
    		sword.setItemMeta(smeta);
    		target.getInventory().setItem(1, sword);
    		
    		ItemStack arrow = new ItemStack(Material.ARROW,1);
    		target.getInventory().addItem(arrow);
    		
    		target.sendMessage("Inventar �bertragen!");
    		target.sendMessage("Du bist jetzt in Klasse Jaeger!");
    		
    	
    	} else if (level == 1) {
    		
    		target.sendMessage("Du bist jetzt J�ger Level 2");
    		
    	} else if (level == 2) {
    		
    		target.sendMessage("Du bist jetzt J�ger Level 3");
    		
    	} else if (level == 3) {
    		
    		target.sendMessage("Du bist jetzt J�ger Level 4");
    		
    	} else if (level == 4) {
    		
    		target.sendMessage("Du bist jetzt J�ger Level 5");
    		
    	} else if (level == 5) {
    		
    		target.sendMessage("Du bist jetzt J�ger Level 6");
    		
    	} else if (level == 6) {
    		
    		target.sendMessage("Du bist jetzt J�ger Level 7");
    		
    	} else if (level == 7) {
    		
    		target.sendMessage("Du bist jetzt J�ger Level 8");
    		
    	} else if (level == 8) {
    		
    		target.sendMessage("Du bist jetzt J�ger Level 9");
    		
    	} else if (level == 9) {
    		
    		target.sendMessage("Du bist jetzt J�ger Level 10");
    		
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
			chmeta.setDisplayName("R�stung des Tanks");
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
			
    		target.sendMessage("Inventar �bertragen!");
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
			
			
			
			target.sendMessage("Inventar �bertragen!");
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
