package com.dbsoftware.bungeeutilisals.bungee.commands;

import com.dbsoftware.bungeeutilisals.api.commands.DBCommand;
import com.dbsoftware.bungeeutilisals.bungee.BungeeUtilisals;
import com.dbsoftware.bungeeutilisals.bungee.user.BungeeUser;
import com.dbsoftware.bungeeutilisals.bungee.utils.Utils;
import com.google.common.base.Joiner;

import net.md_5.bungee.BungeeCord;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;

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
			String message = Joiner.on(" ").join(args);
			
			for(ProxiedPlayer p : BungeeCord.getInstance().getPlayers())
				p.sendMessage(Utils.format(prefix + message.replace("@p", p.getName())));
		} else {
			sender.sendMessage(Utils.format("&cPlease enter a message!"));
		}
	}
}