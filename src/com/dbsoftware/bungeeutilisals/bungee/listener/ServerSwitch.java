package com.dbsoftware.bungeeutilisals.bungee.listener;

import com.dbsoftware.bungeeutilisals.bungee.BungeeUtilisals;
import com.dbsoftware.bungeeutilisals.bungee.updater.UpdateChecker;
import com.dbsoftware.bungeeutilisals.bungee.utils.Utils;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.ServerSwitchEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

public class ServerSwitch implements Listener {
	  
	@EventHandler
	public void onJoin(ServerSwitchEvent event){
		ProxiedPlayer p = event.getPlayer();
		if((p.hasPermission("butilisals.notify") || p.hasPermission("butilisals.*")) && BungeeUtilisals.getInstance().update){
			String version = UpdateChecker.getLatestVersion();
			p.sendMessage(Utils.format("&e&lBungeeUtilisals &8» &6Version &b" + version + " &6is available!"));
			p.sendMessage(Utils.format("&6Go download it on the &bSpigotMC &6page!"));				
		}
	}
}
