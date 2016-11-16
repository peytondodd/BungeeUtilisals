package com.dbsoftware.bungeeutilisals.bungee.listener;

import com.dbsoftware.bungeeutilisals.bungee.BungeeUtilisals;
import com.dbsoftware.bungeeutilisals.bungee.utils.Utils;

import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.ChatEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

public class ChatLock implements Listener {

	@EventHandler
	public void Chatlock(ChatEvent event) {
		ProxiedPlayer p = (ProxiedPlayer) event.getSender();
		if (isChatMuted()) {
			if (p.hasPermission("butilisals.talkinlock") || p.hasPermission("butilisals.*")) {
				return;
			}
			if (!event.getMessage().startsWith("/")) {
				p.sendMessage(Utils.format(BungeeUtilisals.getInstance().getConfig().getString("ChatLock.Locked")));
				event.setCancelled(true);
			}
		}
	}

	private boolean isChatMuted() {
		return BungeeUtilisals.getInstance().chatMuted;
	}
}
