package com.dbsoftware.bungeeutilisals.bungee.commands;

import com.dbsoftware.bungeeutilisals.bungee.BungeeUtilisals;
import com.dbsoftware.bungeeutilisals.bungee.utils.PluginMessageChannel;
import com.dbsoftware.bungeeutilisals.bungee.utils.Utils;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

public class ChatCommand extends Command {
	
	public ChatCommand(){
		super("chat");
	}

	@Override
	public void execute(CommandSender sender, String[] args) {
		if(!(sender instanceof ProxiedPlayer)){
			executeChatCommand(sender, args);
			return;
		}
		if(BungeeUtilisals.getInstance().getConfig().getBoolean("Bukkit-Permissions")){
			PluginMessageChannel.sendPermissionCheckPluginMessage("hasPermission", "butilisals.chat", "chat", args, (ProxiedPlayer)sender);
			return;
		}
		if(sender.hasPermission("butilisals.chat") || sender.hasPermission("butilisals.*")){
			executeChatCommand(sender, args);		
		} else {
			sender.sendMessage(Utils.format(BungeeUtilisals.getInstance().getConfig().getString("Prefix") + BungeeUtilisals.getInstance().getConfig().getString("Main-messages.no-permission")));
		}
	}
	
	public static void executeChatCommand(CommandSender sender, String[] args){
		if(args.length != 1){
			sender.sendMessage(Utils.format(BungeeUtilisals.getInstance().getConfig().getString("ChatLock.ChatCommand.WrongArgs")));
			return;
		}
		if(args[0].equals("lock")){
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
		} else {
			sender.sendMessage(Utils.format(BungeeUtilisals.getInstance().getConfig().getString("ChatLock.ChatCommand.WrongArgs")));
			return;
		}
	}
	
}
