package com.dbsoftware.bungeeutilisals.bungee.listener;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;

import com.dbsoftware.bungeeutilisals.api.CommandAPI;
import com.dbsoftware.bungeeutilisals.api.DBCommand;
import com.dbsoftware.bungeeutilisals.bungee.BungeeUtilisals;
import com.dbsoftware.bungeeutilisals.bungee.user.BungeeUser;

import net.md_5.bungee.api.event.PluginMessageEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

public class PluginMessageReceive implements Listener {

	@EventHandler
	public void onMessageReceive(PluginMessageEvent event) {
		if (event.getTag().equalsIgnoreCase("BungeeCord")) {
			DataInputStream in = new DataInputStream(new ByteArrayInputStream(event.getData()));
			try {
				String channel = in.readUTF();
				if (channel.equals("hasPermission")) {
					String name = in.readUTF();
					String command = in.readUTF();
					String argline = in.readUTF();
					String[] args = new String[] {};
					if (argline.isEmpty()) {
						args = new String[] {};
					} else {
						args = argline.split(" ");
					}
					boolean hasperm = in.readBoolean();

					BungeeUser user = BungeeUtilisals.getInstance().getUser(name);
					if (user == null) {
						return;
					}
					if (!hasperm) {
						user.sendMessage(BungeeUtilisals.getInstance().getConfig().getString("Prefix")
								+ BungeeUtilisals.getInstance().getConfig().getString("Main-messages.no-permission"));
						return;
					}
					for (DBCommand cmd : CommandAPI.getInstance().commands) {
						if (cmd.getName().toLowerCase().contains(command.toLowerCase())) {
							cmd.onExecute(user, args);
							return;
						}
					}
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

}
