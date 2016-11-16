package com.dbsoftware.bungeeutilisals.bungee.punishment.commands;

import java.util.UUID;

import com.dbsoftware.bungeeutilisals.api.DBCommand;
import com.dbsoftware.bungeeutilisals.bungee.BungeeUser;
import com.dbsoftware.bungeeutilisals.bungee.BungeeUtilisals;
import com.dbsoftware.bungeeutilisals.bungee.events.UnbanEvent;
import com.dbsoftware.bungeeutilisals.bungee.punishment.BanAPI;
import com.dbsoftware.bungeeutilisals.bungee.punishment.Punishments;
import com.dbsoftware.bungeeutilisals.bungee.utils.PlayerInfo;
import com.dbsoftware.bungeeutilisals.bungee.utils.UUIDFetcher;
import com.dbsoftware.bungeeutilisals.bungee.utils.Utils;

import net.md_5.bungee.BungeeCord;
import net.md_5.bungee.api.CommandSender;

public class UnbanIPCommand extends DBCommand {

	public UnbanIPCommand() {
		super("unbanip");
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

		PlayerInfo info = new PlayerInfo((useUUIDs ? uuid.toString() : args[0]));
		String IP = info.getIP();

		if(IP == null){
			for(String s : Punishments.punishments.getFile().getStringList("Punishments.Unban.Messages.NeverJoined")){
				sender.sendMessage(Utils.format(s));
			}
			return;
		}
		if(!BanAPI.isIPBanned(IP)){
			for(String s : Punishments.punishments.getFile().getStringList("Punishments.Unban.Messages.NotBanned")){
				sender.sendMessage(Utils.format(s));
			}
			return;
		}
		BanAPI.removeIPBan(IP);
		for(String s : Punishments.punishments.getFile().getStringList("Punishments.Unban.Messages.Unbanned")){
			sender.sendMessage(Utils.format(s.replace("%player%", args[0])));
		}
		UnbanEvent event = new UnbanEvent(sender.getName(), args[0]);
		BungeeCord.getInstance().getPluginManager().callEvent(event);
	}
}