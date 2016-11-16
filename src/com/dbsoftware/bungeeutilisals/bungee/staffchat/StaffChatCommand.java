package com.dbsoftware.bungeeutilisals.bungee.staffchat;

import com.dbsoftware.bungeeutilisals.api.DBCommand;
import com.dbsoftware.bungeeutilisals.bungee.BungeeUser;
import com.dbsoftware.bungeeutilisals.bungee.BungeeUtilisals;
import com.dbsoftware.bungeeutilisals.bungee.utils.Utils;

import net.md_5.bungee.api.CommandSender;

public class StaffChatCommand extends DBCommand {

	public StaffChatCommand() {
		super("staffchat", "sc");
	}

	@Override
	public void onExecute(BungeeUser user, String[] args) {
		if (!StaffChat.inchat.contains(user.getName())) {
			StaffChat.inchat.add(user.getName());
			user.sendMessage(BungeeUtilisals.getInstance().getConfig().getString("StaffChat.ChatEnabled"));
			return;
		}
		StaffChat.inchat.remove(user.getName());
		user.sendMessage(BungeeUtilisals.getInstance().getConfig().getString("StaffChat.ChatDisabled"));
	}

	@Override
	public void onExecute(CommandSender sender, String[] args) {
		sender.sendMessage(Utils.format("This command is only for players!"));
	}
}
