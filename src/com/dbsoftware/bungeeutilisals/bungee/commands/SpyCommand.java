package com.dbsoftware.bungeeutilisals.bungee.commands;

import com.dbsoftware.bungeeutilisals.api.DBCommand;
import com.dbsoftware.bungeeutilisals.bungee.BungeeUser;
import com.dbsoftware.bungeeutilisals.bungee.BungeeUtilisals;
import com.dbsoftware.bungeeutilisals.bungee.utils.Utils;

import net.md_5.bungee.api.CommandSender;

public class SpyCommand extends DBCommand {

	public SpyCommand() {
		super("gspy", BungeeUtilisals.getInstance().getConfig().getStringList("PrivateMessages.Spy.Aliases"));
	}

	@Override
	public void onExecute(BungeeUser user, String[] args) {
		if (args.length > 0) {
			user.sendMessage(
					BungeeUtilisals.getInstance().getConfig().getString("PrivateMessages.Spy.Messages.WrongUsage"));
			return;
		}
		if (user.isSocialSpy()) {
			user.setSocialSpy(false);
			user.sendMessage(
					BungeeUtilisals.getInstance().getConfig().getString("PrivateMessages.Spy.Messages.Disabled"));
		} else {
			user.setSocialSpy(true);
			user.sendMessage(
					BungeeUtilisals.getInstance().getConfig().getString("PrivateMessages.Spy.Messages.Enabled"));
		}
	}

	@Override
	public void onExecute(CommandSender sender, String[] args) {
		sender.sendMessage(Utils.format("&6Only players can use this command!"));
	}
}
