package com.dbsoftware.bungeeutilisals.bungee.commands;

import com.dbsoftware.bungeeutilisals.api.DBCommand;
import com.dbsoftware.bungeeutilisals.bungee.BungeeUtilisals;
import com.dbsoftware.bungeeutilisals.bungee.user.BungeeUser;
import com.dbsoftware.bungeeutilisals.bungee.utils.Utils;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;

public class ChatCommand extends DBCommand {
	
	public ChatCommand(){
		super("chat");
	}
	
	@Override
	public void onExecute(BungeeUser user, String[] args) {
		onExecute(user.sender(), args);
	}
	
	@Override
	public void onExecute(CommandSender sender, String[] args) {
		if(args.length == 1 && args[0].equals("lock")){
		    if (BungeeUtilisals.getInstance().chatMuted){
		    	BungeeUtilisals.getInstance().chatMuted = false;
		    	sender.sendMessage(Utils.format(BungeeUtilisals.getInstance().getConfig().getString("ChatLock.Unlock")));
		    	if(BungeeUtilisals.getInstance().getConfig().getBoolean("ChatLock.Broadcast.Enabled")){
		        	for(String s : BungeeUtilisals.getInstance().getConfig().getStringList("ChatLock.Broadcast.UnLock")){
		            	ProxyServer.getInstance().broadcast(Utils.format(s));
		        	}
		        }
		    } else {
		    	BungeeUtilisals.getInstance().chatMuted = true;
		    	sender.sendMessage(Utils.format(BungeeUtilisals.getInstance().getConfig().getString("ChatLock.Lock")));
		    	if(BungeeUtilisals.getInstance().getConfig().getBoolean("ChatLock.Broadcast.Enabled")){
		    		for(String s : BungeeUtilisals.getInstance().getConfig().getStringList("ChatLock.Broadcast.Lock")){
		        		ProxyServer.getInstance().broadcast(Utils.format(s));
		    		}
		    	}
		    }
		    return;
		}
		sender.sendMessage(Utils.format(BungeeUtilisals.getInstance().getConfig().getString("ChatLock.ChatCommand.WrongArgs")));
		return;
	}
}