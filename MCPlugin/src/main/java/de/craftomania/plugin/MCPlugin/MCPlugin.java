package de.craftomania.plugin.MCPlugin;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public final class MCPlugin extends JavaPlugin {

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
    	if (args[0].equalsIgnoreCase("joinclass")) {
    		String classname = args[1];
    		String playername = args[2];
    		
    		Player target = Bukkit.getServer().getPlayer(playername);
    		
    		joinClass(target, classname);
    		
    		
    	}
    }
    
    private void joinClass(Player target, String classname) {
    	
    }
    
}
