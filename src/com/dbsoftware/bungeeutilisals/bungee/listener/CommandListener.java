package com.dbsoftware.bungeeutilisals.bungee.listener;

import com.dbsoftware.bungeeutilisals.bungee.BungeeUtilisals;
import com.dbsoftware.bungeeutilisals.bungee.events.CommandEvent;
import com.dbsoftware.bungeeutilisals.bungee.user.BungeeUser;

import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

public class CommandListener implements Listener {

	@EventHandler
	public void onCommand(CommandEvent event) {
		BungeeUser user = event.getPlayer();
		if (user == null) {
			return;
		}
		ProxiedPlayer player = user.getPlayer();

		for (BungeeUser u : BungeeUtilisals.getInstance().users) {
			if (u.isLocalSpy()) {
				u.sendMessage(BungeeUtilisals.getInstance().getConfig().getString("LocalSpy.Messages.Format")
						.replace("%player%", player.getName()).replace("%command%", event.getCommand()));
			}
		}
	}

}
