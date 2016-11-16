package com.dbsoftware.bungeeutilisals.bungee.commands;

import com.dbsoftware.bungeeutilisals.api.DBCommand;
import com.dbsoftware.bungeeutilisals.bungee.BungeeUser;
import com.dbsoftware.bungeeutilisals.bungee.BungeeUtilisals;
import com.dbsoftware.bungeeutilisals.bungee.utils.Utils;

import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;

public class FindCommand extends DBCommand {
	
	public FindCommand() {
		super("find");
	}
	
	@Override
	public void onExecute(BungeeUser user, String[] args) {
		this.onExecute(user.sender(), args);
	}

	@Override
	public void onExecute(CommandSender sender, String[] args) {
		if(args.length != 1){
			sender.sendMessage(Utils.format(BungeeUtilisals.getInstance().getConfig().getString("Main-messages.use-find")));
		}
		ProxiedPlayer target = ProxyServer.getInstance().getPlayer(args[0]);
		if(target == null){
			sender.sendMessage(Utils.format(BungeeUtilisals.getInstance().getConfig().getString("Main-messages.offline-player")));
		} else {
			String server = target.getServer().getInfo().getName();
			sender.sendMessage(Utils.format(BungeeUtilisals.getInstance().getConfig().getString("Main-messages.find-message").replace("%server%", server).replace("%player%", sender.getName()).replace("%target%", target.getName())));
		}
	}
}
