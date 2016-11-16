package com.dbsoftware.bungeeutilisals.bungee.commands;

import com.dbsoftware.bungeeutilisals.api.DBCommand;
import com.dbsoftware.bungeeutilisals.bungee.BungeeUtilisals;
import com.dbsoftware.bungeeutilisals.bungee.user.BungeeUser;
import com.dbsoftware.bungeeutilisals.bungee.utils.Utils;

import net.md_5.bungee.api.CommandSender;

public class LocalSpyCommand extends DBCommand {
	
	public LocalSpyCommand() {
		super("localspy", BungeeUtilisals.getInstance().getConfig().getStringList("LocalSpy.Aliases"));
	}
	
	@Override
	public void onExecute(BungeeUser user, String[] args) {
        if(args.length > 0){
        	user.sendMessage(BungeeUtilisals.getInstance().getConfig().getString("LocalSpy.Messages.Usage"));
        	return;
        }
        if(user.isLocalSpy()){
        	user.setLocalSpy(false);
        	user.sendMessage(BungeeUtilisals.getInstance().getConfig().getString("LocalSpy.Messages.Disabled"));
        } else {
        	user.setLocalSpy(true);
        	user.sendMessage(BungeeUtilisals.getInstance().getConfig().getString("LocalSpy.Messages.Enabled"));
        }
	}

	@Override
	public void onExecute(CommandSender sender, String[] args) {
    	sender.sendMessage(Utils.format("&6Only players can use this command!"));
    	return;
	}
}
