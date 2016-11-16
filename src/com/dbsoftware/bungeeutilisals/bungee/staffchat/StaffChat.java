package com.dbsoftware.bungeeutilisals.bungee.staffchat;

import java.util.ArrayList;
import java.util.List;

import com.dbsoftware.bungeeutilisals.api.CommandAPI;
import com.dbsoftware.bungeeutilisals.bungee.BungeeUtilisals;

import net.md_5.bungee.api.ProxyServer;

public class StaffChat {

	public static List<String> inchat = new ArrayList<String>();

	public static void registerStaffChat() {
		if (BungeeUtilisals.getInstance().getConfig().getBoolean("StaffChat.Enabled")) {
			CommandAPI.getInstance().registerCommand(new StaffChatCommand());
			ProxyServer.getInstance().getPluginManager().registerListener(BungeeUtilisals.getInstance(), new StaffChatEvent());
		}
	}
}