package com.dbsoftware.bungeeutilisals.bungee.punishment.commands;

import java.util.UUID;

import com.dbsoftware.bungeeutilisals.api.DBCommand;
import com.dbsoftware.bungeeutilisals.bungee.BungeeUtilisals;
import com.dbsoftware.bungeeutilisals.bungee.events.UnbanEvent;
import com.dbsoftware.bungeeutilisals.bungee.punishment.BanAPI;
import com.dbsoftware.bungeeutilisals.bungee.punishment.Punishments;
import com.dbsoftware.bungeeutilisals.bungee.user.BungeeUser;
import com.dbsoftware.bungeeutilisals.bungee.utils.UUIDFetcher;
import com.dbsoftware.bungeeutilisals.bungee.utils.Utils;

import net.md_5.bungee.BungeeCord;
import net.md_5.bungee.api.CommandSender;

public class UnbanCommand extends DBCommand {

	public UnbanCommand() {
		super("unban");
	}

	@Override
	public void onExecute(BungeeUser user, String[] args) {
		onExecute(user.sender(), args);
	}

	@Override
	public void onExecute(CommandSender sender, String[] args) {
		Boolean useUUIDs = BungeeUtilisals.getInstance().getConfigData().UUIDSTORAGE;
		if (args.length != 1) {
			for (String s : Punishments.punishments.getFile().getStringList("Punishments.Unban.Messages.WrongArgs")) {
				sender.sendMessage(Utils.format(s));
			}
			return;
		}
		UUID uuid = UUIDFetcher.getUUIDOf(args[0]);
		if (uuid == null && useUUIDs) {
			return;
		}

		if ((useUUIDs ? BanAPI.isBanned(uuid.toString()) : BanAPI.isBanned(args[0]))) {
			BanAPI.removeBan((useUUIDs ? uuid.toString() : args[0]));
			for (String s : Punishments.punishments.getFile().getStringList("Punishments.Unban.Messages.Unbanned")) {
				sender.sendMessage(Utils.format(s.replace("%player%", args[0])));
			}
			UnbanEvent event = new UnbanEvent(sender.getName(), args[0]);
			BungeeCord.getInstance().getPluginManager().callEvent(event);
		}
	}
}