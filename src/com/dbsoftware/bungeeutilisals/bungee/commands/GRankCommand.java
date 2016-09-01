package com.dbsoftware.bungeeutilisals.bungee.commands;

import com.dbsoftware.bungeeutilisals.bungee.BungeeRank;
import com.dbsoftware.bungeeutilisals.bungee.BungeeUser;
import com.dbsoftware.bungeeutilisals.bungee.BungeeUtilisals;
import com.dbsoftware.bungeeutilisals.bungee.utils.PluginMessageChannel;
import com.dbsoftware.bungeeutilisals.bungee.utils.Utils;

import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

public class GRankCommand extends Command {

	public GRankCommand() {
		super("grank");
	}

	public static void executeGRankCommand(CommandSender sender, String[] args){
		if(args.length != 2){
			sender.sendMessage(Utils.format("&6Please use /grank (player) (GUEST/STAFF)"));
			return;
		}
		String name = args[0];
		String rname = args[1];
		
		try {
			BungeeRank rank = BungeeRank.valueOf(rname);
			
			BungeeUser.setRank(name, rank);
			
			sender.sendMessage(Utils.format("&6You have set &b" + name + " &6his rank to &b" + rank.toString() + "&6!"));
		} catch(Exception e){
			sender.sendMessage(Utils.format("&6Incorrect rank! Please use GUEST or STAFF!"));
			return;
		}
	}
	
	@Override
	public void execute(CommandSender sender, String[] args) {
		if(!(sender instanceof ProxiedPlayer)){
			executeGRankCommand(sender, args);
			return;
		}
		if(BungeeUtilisals.getInstance().getConfig().getBoolean("Bukkit-Permissions")){
			PluginMessageChannel.sendPermissionCheckPluginMessage("hasPermission", "butilisals.grank", "grank", args, ((ProxiedPlayer)sender));
			return;
		}
		if(sender.hasPermission("butilisals.grank") || sender.hasPermission("butilisals.*")){
			executeGRankCommand(sender, args);		
		} else {
			sender.sendMessage(Utils.format(BungeeUtilisals.getInstance().getConfig().getString("Prefix") + BungeeUtilisals.getInstance().getConfig().getString("Main-messages.no-permission")));
		}
	}
}