package com.dbsoftware.bungeeutilisals.bungee.listener;

import com.dbsoftware.bungeeutilisals.bungee.BungeeUser;
import com.dbsoftware.bungeeutilisals.bungee.BungeeUtilisals;

import net.md_5.bungee.BungeeCord;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.PostLoginEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

public class JoinListener implements Listener {

	@EventHandler
	public void onJoin(PostLoginEvent event) {
		final ProxiedPlayer p = event.getPlayer();
		if (BungeeUtilisals.getInstance().getUser(p) != null) {
			return;
		}
		Runnable r = new Runnable() {

			@Override
			public void run() {
				BungeeUtilisals.getInstance().users.add(new BungeeUser(p));
			}
		};
		BungeeCord.getInstance().getScheduler().runAsync(BungeeUtilisals.getInstance(), r);
	}

}
