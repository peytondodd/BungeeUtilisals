package com.dbsoftware.bungeeutilisals.bukkit;

import java.io.IOException;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import com.dbsoftware.bungeeutilisals.bukkit.Metrics;

public class BungeeUtilisals extends JavaPlugin implements Plugin {
	
	public static BungeeUtilisals instance;
	public PluginChannelListener pcl;
	
	public void onEnable(){
		instance = this;
		Bukkit.getMessenger().registerOutgoingPluginChannel(this, "BungeeCord");
		Bukkit.getMessenger().registerIncomingPluginChannel(this, "Return",  pcl = new PluginChannelListener());
		
	    try {
	        Metrics metrics = new Metrics(this);
	        metrics.start();
	    } catch (IOException e) {
	        Bukkit.getLogger().warning("[BungeeUtilisals] Metrics could not be enabled.");
	    }
	}
	
	public static BungeeUtilisals getInstance(){
		return instance;
	}

}
