package com.dbsoftware.bungeeutilisals.bungee.punishment.commands;

import java.util.Arrays;
import java.util.UUID;

import com.dbsoftware.bungeeutilisals.api.commands.DBCommand;
import com.dbsoftware.bungeeutilisals.bungee.BungeeUtilisals;
import com.dbsoftware.bungeeutilisals.bungee.events.WarnEvent;
import com.dbsoftware.bungeeutilisals.bungee.punishment.Punishments;
import com.dbsoftware.bungeeutilisals.bungee.user.BungeeUser;
import com.dbsoftware.bungeeutilisals.bungee.utils.PlayerInfo;
import com.dbsoftware.bungeeutilisals.bungee.utils.UUIDFetcher;
import com.dbsoftware.bungeeutilisals.bungee.utils.Utils;
import com.google.common.base.Joiner;

import net.md_5.bungee.BungeeCord;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;

public class WarnCommand extends DBCommand {

	public WarnCommand() {
		super("warn");
	}
	
	@Override
	public void onExecute(BungeeUser user, String[] args) {
		onExecute(user.sender(), args);
	}

	@Override
	public void onExecute(CommandSender sender, String[] args) {
		Boolean useUUIDs = BungeeUtilisals.getInstance().getConfigData().UUIDSTORAGE;
		if (args.length < 2) {
			for (String s : Punishments.punishments.getFile().getStringList("Punishments.Warn.Messages.WrongArgs")) {
				sender.sendMessage(Utils.format(s));
			}
			return;
		}
		UUID uuid = UUIDFetcher.getUUIDOf(args[0]);
		if (uuid == null && useUUIDs) {
			return;
		}
		ProxiedPlayer p = ProxyServer.getInstance().getPlayer(args[0]);
		String reason = Joiner.on(" ").join(Arrays.copyOfRange(args, 1, args.length));
		if (p == null) {
			for (String s : Punishments.punishments.getFile()
					.getStringList("Punishments.Warn.Messages.OfflinePlayer")) {
				sender.sendMessage(Utils.format(s));
			}
			return;

		}
		for (String s : Punishments.punishments.getFile().getStringList("Punishments.Warn.Messages.WarnMessage")) {
			p.sendMessage(Utils.format(s.replace("%player%", sender.getName()).replace("%reason%", reason)));
		}
		WarnEvent event = new WarnEvent(sender.getName(), p.getName(), reason);
		BungeeCord.getInstance().getPluginManager().callEvent(event);

		PlayerInfo pinfo = new PlayerInfo(useUUIDs ? uuid.toString() : args[0]);
		pinfo.addWarn();
		for (String s : Punishments.punishments.getFile().getStringList("Punishments.Mute.Messages.Warned")) {
			sender.sendMessage(Utils.format(s.replace("%player%", args[0]).replace("%reason%", reason)));
		}
	}
}
