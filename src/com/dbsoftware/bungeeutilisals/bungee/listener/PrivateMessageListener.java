package com.dbsoftware.bungeeutilisals.bungee.listener;

import com.dbsoftware.bungeeutilisals.bungee.BungeeUser;
import com.dbsoftware.bungeeutilisals.bungee.BungeeUtilisals;
import com.dbsoftware.bungeeutilisals.bungee.events.PrivateMessageEvent;

import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

public class PrivateMessageListener implements Listener {

	@EventHandler
	public void onPrivateMessage(PrivateMessageEvent event) {
		String message = BungeeUtilisals.getInstance().getConfig().getString("PrivateMessages.Spy.Messages.Format")
				.replace("%sender%", event.getSender().getPlayer().getName())
				.replace("%receiver%", event.getReceiver().getPlayer().getName())
				.replace("%message%", event.getMessage())
				.replace("%server%", event.getSender().getPlayer().getServer().getInfo().getName());
		for (BungeeUser user : BungeeUtilisals.getInstance().users) {
			if (user.isSocialSpy()) {
				user.sendMessage(message);
			}
		}
	}

}
