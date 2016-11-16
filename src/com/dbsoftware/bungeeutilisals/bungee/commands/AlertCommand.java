package com.dbsoftware.bungeeutilisals.bungee.commands;

import com.dbsoftware.bungeeutilisals.api.DBCommand;
import com.dbsoftware.bungeeutilisals.bungee.BungeeUtilisals;
import com.dbsoftware.bungeeutilisals.bungee.user.BungeeUser;
import com.dbsoftware.bungeeutilisals.bungee.utils.Utils;
import com.google.common.base.Joiner;

import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;

public class AlertCommand extends DBCommand {
	
	public AlertCommand() {
		super("alert");
	}
	
	@Override
	public void onExecute(BungeeUser user, String[] args) {
		onExecute(user.sender(), args);
	}
	
	@Override
	public void onExecute(CommandSender sender, String[] args) {
		String prefix = Utils.c(BungeeUtilisals.getInstance().getConfig().getString("Prefix"));
		if (args.length >= 1) {
			ProxyServer.getInstance().broadcast(Utils.format(prefix + Joiner.on(" ").join(args)));
		} else {
			sender.sendMessage(Utils.format("&cPlease enter a message!"));
		}
	}
}