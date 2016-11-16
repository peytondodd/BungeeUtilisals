package com.dbsoftware.bungeeutilisals.bungee.listener;

import com.dbsoftware.bungeeutilisals.bungee.BungeeUtilisals;
import com.dbsoftware.bungeeutilisals.bungee.utils.Utils;

import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.ChatEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

public class AntiCaps implements Listener {

	@EventHandler
	public void Anticaps(ChatEvent event) {
		ProxiedPlayer p = (ProxiedPlayer) event.getSender();
		int max = BungeeUtilisals.getInstance().getConfig().getInt("AntiCaps.Max_Percentage");
		int min = BungeeUtilisals.getInstance().getConfig().getInt("AntiCaps.Min_Length");

		if (event.getMessage().startsWith("/") || p.hasPermission("butilisals.caps.bypass")
				|| p.hasPermission("butilisals.*")) {
			return;
		} else {
			if (BungeeUtilisals.getInstance().getConfig().getBoolean("AntiCaps.Enabled")) {
				if ((event.getMessage().length() >= min) && (this.getUppercasePercentage(event.getMessage()) > max)) {
					event.setMessage(event.getMessage().toLowerCase());
					p.sendMessage(
							Utils.format(BungeeUtilisals.getInstance().getConfig().getString("AntiCaps.Message")));
				}
			} else {
				return;
			}
		}
	}

	private double getUppercasePercentage(String string) {
		double upperCase = 0.0D;
		for (int i = 0; i < string.length(); i++) {
			if (isUppercase(string.substring(i, i + 1))) {
				upperCase += 1.0D;
			}
		}
		return upperCase / string.length() * 100.0D;
	}

	private boolean isUppercase(String string) {
		return "ABCDEFGHIJKLMNOPQRSTUVWXYZ".contains(string);
	}
}