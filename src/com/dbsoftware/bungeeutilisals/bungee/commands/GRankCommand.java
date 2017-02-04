package com.dbsoftware.bungeeutilisals.bungee.commands;

import com.dbsoftware.bungeeutilisals.api.commands.DBCommand;
import com.dbsoftware.bungeeutilisals.bungee.user.BungeeRank;
import com.dbsoftware.bungeeutilisals.bungee.user.BungeeUser;
import com.dbsoftware.bungeeutilisals.bungee.utils.Utils;

import net.md_5.bungee.api.CommandSender;

public class GRankCommand extends DBCommand {
	
	public GRankCommand() {
		super("grank");
	}
	
	@Override
	public void onExecute(BungeeUser user, String[] args) {
		onExecute(user.sender(), args);
	}

	@Override
	public void onExecute(CommandSender sender, String[] args) {
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
}