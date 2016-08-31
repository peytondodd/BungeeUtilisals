package com.dbsoftware.bungeeutilisals.bungee.listener;

import com.dbsoftware.bungeeutilisals.bungee.BungeeUser;
import com.dbsoftware.bungeeutilisals.bungee.BungeeUtilisals;

import net.md_5.bungee.api.event.PostLoginEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

public class JoinListener implements Listener {
	
	@EventHandler
	public void onJoin(PostLoginEvent event){
		if(BungeeUtilisals.getInstance().getUser(event.getPlayer()) != null){
			return;
		}
		BungeeUtilisals.getInstance().users.add(new BungeeUser(event.getPlayer()));
	}
	
}
