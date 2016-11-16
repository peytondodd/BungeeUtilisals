package com.dbsoftware.bungeeutilisals.bungee.commands;

import com.dbsoftware.bungeeutilisals.api.DBCommand;
import com.dbsoftware.bungeeutilisals.bungee.BungeeUser;
import com.dbsoftware.bungeeutilisals.bungee.BungeeUtilisals;
import com.dbsoftware.bungeeutilisals.bungee.utils.Utils;

import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;

public class ClearChatCommand extends DBCommand {
	
	public ClearChatCommand() {
		super("clearchat");
	}
	
	@Override
	public void onExecute(BungeeUser user, String[] args) {
		if(args.length != 1){
			user.sendMessage("&cUse /clearchat local or /clearchat global");
			return;
		}
		if(args[0].equalsIgnoreCase("local") || args[0].equalsIgnoreCase("l")){
			String server = user.getServer().getInfo().getName();
			for(ProxiedPlayer players : ProxyServer.getInstance().getServerInfo(server).getPlayers()){
				for(int i = 0; i < 100; i++){
					players.sendMessage(Utils.format("&e"));
				}
				players.sendMessage(Utils.format(BungeeUtilisals.getInstance().getConfig().getString("ClearChat.Local").replace("%player%", user.getName())));
			}
			return;
		} if(args[0].equalsIgnoreCase("global") || args[0].equalsIgnoreCase("g")){
			for(int i = 0; i < 100; i++){
				ProxyServer.getInstance().broadcast(Utils.format("&6"));
			}
			ProxyServer.getInstance().broadcast(Utils.format(BungeeUtilisals.getInstance().getConfig().getString("ClearChat.Global").replace("%player%", user.getName())));
			return;
		}
		user.sendMessage("&cUse /clearchat local or /clearchat global");
	}

	@Override
	public void onExecute(CommandSender sender, String[] args) {
		if(args.length != 1){
			sender.sendMessage(Utils.format("&cUse /clearchat global"));
			return;
		}
		if(args[0].equalsIgnoreCase("global") || args[0].equalsIgnoreCase("g")){
			for(int i = 0; i < 100; i++){
				ProxyServer.getInstance().broadcast(Utils.format("&6"));
			}
			ProxyServer.getInstance().broadcast(Utils.format(BungeeUtilisals.getInstance().getConfig().getString("ClearChat.Global").replace("%player%", sender.getName())));
			return;
		}
		sender.sendMessage(Utils.format("&cUse /clearchat global"));
	}
}
