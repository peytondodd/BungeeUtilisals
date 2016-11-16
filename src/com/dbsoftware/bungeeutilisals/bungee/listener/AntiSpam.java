package com.dbsoftware.bungeeutilisals.bungee.listener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import com.dbsoftware.bungeeutilisals.bungee.BungeeUtilisals;
import com.dbsoftware.bungeeutilisals.bungee.utils.Utils;

import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.ChatEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

public class AntiSpam implements Listener {

	public static ArrayList<String> chatspam = new ArrayList<String>();
	public static Map<ProxiedPlayer, String> norepeat = new HashMap<ProxiedPlayer, String>();

	@EventHandler
	public void Antispam(ChatEvent event) {
		final ProxiedPlayer p = (ProxiedPlayer) event.getSender();
		if (p.hasPermission("butilisals.antispam.bypass")) {
			return;
		}
		if (BungeeUtilisals.getInstance().getConfig().getBoolean("AntiSpam.Enabled")
				&& !(norepeat.containsKey(p) && norepeat.containsValue(event.getMessage()))) {
			if (chatspam.contains(p.getName())) {
				if (!event.isCommand()) {
					p.sendMessage(Utils.format(BungeeUtilisals.getInstance().getConfig().getString("AntiSpam.Message")
							.replace("%time%", String
									.valueOf(BungeeUtilisals.getInstance().getConfig().getInt("AntiSpam.Seconds")))));
					event.setCancelled(true);
				} else if (event.getMessage().startsWith("/r ") || event.getMessage().startsWith("/msg ")
						|| event.getMessage().startsWith("/m ") || event.getMessage().startsWith("/t ")
						|| event.getMessage().startsWith("/w ")) {
					p.sendMessage(Utils.format(BungeeUtilisals.getInstance().getConfig().getString("AntiSpam.Message")
							.replace("%time%", String
									.valueOf(BungeeUtilisals.getInstance().getConfig().getInt("AntiSpam.Seconds")))));
					event.setCancelled(true);
				}
				return;
			} else {
				chatspam.add(p.getName());
				ProxyServer.getInstance().getScheduler().schedule(BungeeUtilisals.getInstance(), new Runnable() {
					public void run() {
						chatspam.remove(p.getName());
					}
				}, BungeeUtilisals.getInstance().getConfig().getInt("AntiSpam.Seconds"), TimeUnit.SECONDS);
			}
		}
		if (BungeeUtilisals.getInstance().getConfig().getBoolean("Norepeat.Enabled")) {
			if (!event.isCommand()) {
				if (norepeat.containsKey(p)
						&& norepeat.get(p).toLowerCase().equalsIgnoreCase(event.getMessage().toLowerCase())) {
					event.setCancelled(true);
					p.sendMessage(
							Utils.format(BungeeUtilisals.getInstance().getConfig().getString("Norepeat.Message")));
					return;
				} else {
					norepeat.remove(p);
					norepeat.put(p, event.getMessage());
				}
			}
		}
	}
}
