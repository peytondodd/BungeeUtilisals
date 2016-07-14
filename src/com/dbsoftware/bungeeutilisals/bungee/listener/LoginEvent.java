package com.dbsoftware.bungeeutilisals.bungee.listener;

import com.dbsoftware.bungeeutilisals.bungee.BungeeUtilisals;
import com.dbsoftware.bungeeutilisals.bungee.updater.UpdateChecker;
import com.dbsoftware.bungeeutilisals.bungee.utils.Utils;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.ServerConnectEvent;
import net.md_5.bungee.api.plugin.Listener;

public class LoginEvent implements Listener {

	public BungeeUtilisals plugin;
	  
	public LoginEvent(BungeeUtilisals plugin){
		this.plugin = plugin;
	}
		
	public void onPlayerJoin(ServerConnectEvent event){
		ProxiedPlayer p = event.getPlayer();
		if((event.getPlayer().hasPermission("butilisals.notify") || event.getPlayer().hasPermission("butilisals.*")) && BungeeUtilisals.update){
			String version = UpdateChecker.getLatestVersion();
			
			p.sendMessage(Utils.format("&e&lBungeeUtilisals &8» &6Version &b" + version + " &6is available!"));
			p.sendMessage(Utils.format("&6Go download it on the &bSpigotMC &6page!"));	
		}
	}
}
