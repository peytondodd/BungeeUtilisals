package com.dbsoftware.bungeeutilisals.bungee.punishment.commands;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import com.dbsoftware.bungeeutilisals.api.commands.DBCommand;
import com.dbsoftware.bungeeutilisals.bungee.BungeeUtilisals;
import com.dbsoftware.bungeeutilisals.bungee.punishment.BanAPI;
import com.dbsoftware.bungeeutilisals.bungee.punishment.MuteAPI;
import com.dbsoftware.bungeeutilisals.bungee.punishment.Punishments;
import com.dbsoftware.bungeeutilisals.bungee.user.BungeeUser;
import com.dbsoftware.bungeeutilisals.bungee.utils.PlayerInfo;
import com.dbsoftware.bungeeutilisals.bungee.utils.UUIDFetcher;
import com.dbsoftware.bungeeutilisals.bungee.utils.Utils;

import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.BaseComponent;

public class BanInfoCommand extends DBCommand {

	public BanInfoCommand() {
		super("baninfo", "binfo", "pinfo");
	}

	@Override
	public void onExecute(BungeeUser user, String[] args) {
		onExecute(user.sender(), args);
	}

	@Override
	public void onExecute(CommandSender sender, String[] args) {
		Boolean useUUIDs = BungeeUtilisals.getInstance().getConfigData().UUIDSTORAGE;
		if (args.length != 1) {
			for (String s : Punishments.punishments.getFile()
					.getStringList("Punishments.PlayerInfo.Messages.WrongArgs")) {
				sender.sendMessage(Utils.format(s));
			}
			return;
		}
		UUID uuid = UUIDFetcher.getUUIDOf(args[0]);
		if (uuid == null && useUUIDs) {
			return;
		}
		PlayerInfo info = new PlayerInfo((useUUIDs ? uuid.toString() : args[0]));

		if (!info.isInTable()) {
			for (String s : Punishments.punishments.getFile()
					.getStringList("Punishments.PlayerInfo.Messages.NeverJoined")) {
				sender.sendMessage(Utils.format(s));
			}
			return;
		}

		String IP = info.getIP();
		Long end = -1L;
		Long muteend = -1L;

		if (useUUIDs) {
			if (BanAPI.isBanned(uuid.toString())) {
				end = BanAPI.getBanTime(uuid.toString());
			}
		} else {
			if (BanAPI.isBanned(args[0])) {
				end = BanAPI.getBanTime(args[0]);
			}
		}
		if (useUUIDs) {
			if (MuteAPI.isMuted(uuid.toString())) {
				muteend = MuteAPI.getMuteTime(uuid.toString());
			}
		} else {
			if (MuteAPI.isMuted(args[0])) {
				muteend = MuteAPI.getMuteTime(args[0]);
			}
		}
		SimpleDateFormat df2 = new SimpleDateFormat("kk:mm dd/MM/yyyy");
		String date = df2.format(new Date(end));

		Boolean isbanned = (useUUIDs ? BanAPI.isBanned(uuid.toString()) : BanAPI.isBanned(args[0]));
		String bannedby = null;
		String banreason = null;
		if (isbanned) {
			if (useUUIDs) {
				bannedby = BanAPI.getBannedBy(uuid.toString());
				banreason = BanAPI.getReason(uuid.toString());
			} else {
				bannedby = BanAPI.getBannedBy(args[0]);
				banreason = BanAPI.getReason(args[0]);
			}
		}

		Boolean isIPbanned = BanAPI.isIPBanned(IP);
		String ipbannedby = null;
		String ipbanreason = null;
		if (isIPbanned) {
			ipbannedby = BanAPI.getIPBannedBy(IP);
			ipbanreason = BanAPI.getIPReason(IP);
		}

		Boolean ismuted;
		if (BungeeUtilisals.getInstance().getConfigData().UUIDSTORAGE) {
			ismuted = MuteAPI.isMuted(uuid.toString());
		} else {
			ismuted = MuteAPI.isMuted(args[0]);
		}

		String mutedby = null;
		String mutereason = null;
		if (ismuted) {
			if (BungeeUtilisals.getInstance().getConfigData().UUIDSTORAGE) {
				mutedby = MuteAPI.getMutedBy(uuid.toString());
				mutereason = MuteAPI.getReason(uuid.toString());
			} else {
				mutedby = MuteAPI.getMutedBy(args[0]);
				mutereason = MuteAPI.getReason(args[0]);
			}
		}

		List<BaseComponent[]> messages = new ArrayList<BaseComponent[]>();
		for (String s : Punishments.punishments.getFile().getStringList("Punishments.PlayerInfo.Messages.Info")) {
			messages.add(Utils.format(s.replace("&", "§").replace("%ip%", IP).replace("%player%", args[0])
					.replace("%bans%", String.valueOf(info.getBans()))
					.replace("%mutes%", String.valueOf(info.getMutes()))
					.replace("%kicks%", String.valueOf(info.getKicks()))
					.replace("%warns%", String.valueOf(info.getWarns()))
					/** IS BANNED */
					.replace("%isbanned%", (isbanned
							? Punishments.punishments.getFile()
									.getString("Punishments.PlayerInfo.Messages.isBanned",
											"&cBanned by %banner% for %reason%. Unban time: %time%")
									.replace("&", "§").replace("%banner%", bannedby).replace("%reason%", banreason)
									.replace("%time%", (end == -1L ? "Never" : date))

							/** IS NOT BANNED */
							: Punishments.punishments.getFile().getString("Punishments.PlayerInfo.Messages.isNotBanned",
									"&cNot Banned"))

					/** IS IPBANNED */
					).replace("%isIPbanned%", (isIPbanned ? Punishments.punishments.getFile().getString("Punishments.PlayerInfo.Messages.isIPBanned", "&cIPBanned by %banner% for %reason%.").replace("&", "§").replace("%banner%", ipbannedby).replace("%reason%", ipbanreason)

							/** IS NOT IPBANNED */
							: Punishments.punishments.getFile()
									.getString("Punishments.PlayerInfo.Messages.isNotIPBanned", "&cNot IPBanned"))

					/** IS MUTED */
					).replace("%ismuted%", (ismuted ? Punishments.punishments.getFile().getString("Punishments.PlayerInfo.Messages.isMuted", "&cMuted by %muter% for %reason%. Unmute time: %time%").replace("&", "§").replace("%muter%", mutedby).replace("%reason%", mutereason).replace("%time%", (muteend == -1L ? "Never" : date))

							/** IS NOT MUTED */
							: Punishments.punishments.getFile().getString("Punishments.PlayerInfo.Messages.isNotMuted",
									"&cNot Muted")))));
		}
		for (BaseComponent[] message : messages) {
			sender.sendMessage(message);
		}
	}
}