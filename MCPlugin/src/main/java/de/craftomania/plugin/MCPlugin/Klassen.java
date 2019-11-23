package de.craftomania.plugin.MCPlugin;

import java.util.ArrayList;
import java.util.HashMap;

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
	
	public void jaeger(int level, Player target) {
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
	
	public void tank(int level, Player target) {
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
			ItemMeta cmeta = chest.getItemMeta();
			cmeta.setUnbreakable(true);
			cmeta.setDisplayName("Rüstung des Tanks");
			cmeta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
			lore = new ArrayList<String>();
			lore.add("+ 6 armor");
			cmeta.setLore(lore);
			chest.setItemMeta(cmeta);
			
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
    		
    	} else if (level == 1) {
    		
    		target.sendMessage("Du bist jetzt Tank Level 2");
    		
    	} 
	}
	
	
}
