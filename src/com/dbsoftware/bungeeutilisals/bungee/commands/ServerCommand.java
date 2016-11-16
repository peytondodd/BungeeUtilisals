package com.dbsoftware.bungeeutilisals.bungee.commands;

import com.dbsoftware.bungeeutilisals.api.DBCommand;
import com.dbsoftware.bungeeutilisals.bungee.BungeeUser;
import com.dbsoftware.bungeeutilisals.bungee.BungeeUtilisals;
import com.dbsoftware.bungeeutilisals.bungee.utils.Utils;

import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.config.ServerInfo;

public class ServerCommand extends DBCommand {

	public ServerCommand() {
		super("server");
	}

	@Override
	public void onExecute(BungeeUser user, String[] args) {
		if (args.length == 0) {
			String s = BungeeUtilisals.getInstance().getConfig().getString("Main-messages.server-message");
			user.sendMessage(s.replace("%player%", user.getName()).replace("%servername%",
					user.getServer().getInfo().getName()));
		}
		if (args.length == 1) {
			ServerInfo server = ProxyServer.getInstance().getServerInfo(args[0]);
			if (server != null) {
				if (user.getServer().getInfo().getName().toLowerCase().equals(server.getName().toLowerCase())) {
					user.sendMessage(
							BungeeUtilisals.getInstance().getConfig().getString("Main-messages.already-connected"));
					return;
				}
				user.connect(server);
				user.sendMessage(BungeeUtilisals.getInstance().getConfig().getString("Main-messages.sended-message")
						.replace("%player%", user.getName()).replace("%server%", server.getName()));
			} else {
				user.sendMessage(BungeeUtilisals.getInstance().getConfig().getString("Main-messages.no-server")
						.replace("%player%", user.getName()).replace("%server%", args[0]));
			}
		} else if (args.length != 1 && args.length != 0) {
			user.sendMessage(BungeeUtilisals.getInstance().getConfig().getString("Main-messages.use-server"));
		}
	}

	@Override
	public void onExecute(CommandSender sender, String[] args) {
		sender.sendMessage(Utils.format("&cThis command can only be used ingame!"));
	}
}
