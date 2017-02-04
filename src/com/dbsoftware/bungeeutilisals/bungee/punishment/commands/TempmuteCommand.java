package com.dbsoftware.bungeeutilisals.bungee.punishment.commands;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.UUID;

import com.dbsoftware.bungeeutilisals.api.commands.DBCommand;
import com.dbsoftware.bungeeutilisals.bungee.BungeeUtilisals;
import com.dbsoftware.bungeeutilisals.bungee.events.MuteEvent;
import com.dbsoftware.bungeeutilisals.bungee.punishment.DateUtil;
import com.dbsoftware.bungeeutilisals.bungee.punishment.MuteAPI;
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

public class TempmuteCommand extends DBCommand {

	public TempmuteCommand() {
		super("tempmute");
	}

	@Override
	public void onExecute(BungeeUser user, String[] args) {
		onExecute(user.sender(), args);
	}

	@Override
	public void onExecute(CommandSender sender, String[] args) {
		Boolean useUUIDs = BungeeUtilisals.getInstance().getConfigData().UUIDSTORAGE;
		if (args.length < 3) {
			for (String s : Punishments.punishments.getFile()
					.getStringList("Punishments.Tempmute.Messages.WrongArgs")) {
				sender.sendMessage(Utils.format(s));
			}
			return;
		}
		UUID uuid = UUIDFetcher.getUUIDOf(args[0]);
		if (uuid == null && useUUIDs) {
			return;
		}
		if ((useUUIDs ? MuteAPI.isMuted(uuid.toString()) : MuteAPI.isMuted(args[0]))) {
			for (String s : Punishments.punishments.getFile()
					.getStringList("Punishments.Tempmute.Messages.AlreadyMuted")) {
				sender.sendMessage(Utils.format(s));
			}
			return;
		}
		String reason = Joiner.on(" ").join(Arrays.copyOfRange(args, 2, args.length));
		Long time = -1L;
		try {
			time = DateUtil.parseDateDiff(args[1], true);
		} catch (Exception e) {
			for (String s : Punishments.punishments.getFile()
					.getStringList("Punishments.Tempmute.Messages.WrongTime")) {
				sender.sendMessage(Utils.format(s));
			}
			return;
		}
		MuteAPI.addMute(sender.getName(), (useUUIDs ? uuid.toString() : args[0]), time, reason);

		ProxiedPlayer p = ProxyServer.getInstance().getPlayer(args[0]);
		if (p != null) {
			SimpleDateFormat df2 = new SimpleDateFormat("kk:mm dd/MM/yyyy");
			String date = df2.format(new Date(time));

			for (String s : Punishments.punishments.getFile()
					.getStringList("Punishments.Tempmute.Messages.MuteMessage")) {
				p.sendMessage(Utils.format(
						s.replace("%player%", sender.getName()).replace("%reason%", reason).replace("%time%", date)));
			}
		}
		PlayerInfo info = new PlayerInfo((useUUIDs ? uuid.toString() : args[0]));
		MuteEvent event = new MuteEvent(sender.getName(), args[0], time, reason);
		BungeeCord.getInstance().getPluginManager().callEvent(event);
		info.addMute();
		for (String s : Punishments.punishments.getFile().getStringList("Punishments.Tempmute.Messages.Muted")) {
			sender.sendMessage(Utils.format(s.replace("%player%", args[0]).replace("%reason%", reason)));
		}
	}
}