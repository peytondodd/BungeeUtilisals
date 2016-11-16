package com.dbsoftware.bungeeutilisals.bungee.listener;

import com.dbsoftware.bungeeutilisals.bungee.BungeeUtilisals;
import com.dbsoftware.bungeeutilisals.bungee.events.CommandEvent;

import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.ChatEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

public class ChatListener implements Listener {

	@EventHandler
	public void onChat(ChatEvent event) {
		if (event.isCommand()) {
			CommandEvent commandEvent = new CommandEvent(
					BungeeUtilisals.getInstance().getUser((ProxiedPlayer) event.getSender()), event.getMessage());
			ProxyServer.getInstance().getPluginManager().callEvent(commandEvent);

			if (commandEvent.isCancelled()) {
				event.setCancelled(true);
			}
		}
	}
}