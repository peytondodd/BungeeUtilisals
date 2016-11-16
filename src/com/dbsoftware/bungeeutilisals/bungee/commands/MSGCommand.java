package com.dbsoftware.bungeeutilisals.bungee.commands;

import java.util.ArrayList;
import java.util.List;

import com.dbsoftware.bungeeutilisals.api.DBCommand;
import com.dbsoftware.bungeeutilisals.bungee.BungeeUtilisals;
import com.dbsoftware.bungeeutilisals.bungee.events.PrivateMessageEvent;
import com.dbsoftware.bungeeutilisals.bungee.user.BungeeUser;
import com.dbsoftware.bungeeutilisals.bungee.utils.Utils;
import com.google.common.base.Joiner;

import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;

public class MSGCommand extends DBCommand {
	
	public MSGCommand() {
		super("gmsg", BungeeUtilisals.getInstance().getConfig().getStringList("PrivateMessages.MSG.Aliases"));
		permissions.add("butilisals.msg");
	}
	
	@Override
	public void onExecute(BungeeUser user, String[] args) {
        ProxiedPlayer p = user.getPlayer();
        if(args.length < 2){
        	p.sendMessage(Utils.format(BungeeUtilisals.getInstance().getConfig().getString("PrivateMessages.MSG.Messages.WrongUsage")));
        	return;
        }
        String target = args[0];
        ProxiedPlayer pl = ProxyServer.getInstance().getPlayer(target);
        if(pl == null){
        	p.sendMessage(Utils.format(BungeeUtilisals.getInstance().getConfig().getString("PrivateMessages.MSG.Messages.OfflineTarget")));
        	return;
        }
        List<String> newargs = new ArrayList<String>();
        for(String arg : args){
        	if(!arg.equals(target)){
            	newargs.add(arg);
        	}
        }
        String message = Joiner.on(" ").join(newargs);
        
        PrivateMessageEvent event = new PrivateMessageEvent(BungeeUtilisals.getInstance().getUser(p), BungeeUtilisals.getInstance().getUser(pl), message);
        ProxyServer.getInstance().getPluginManager().callEvent(event);
        
        if(event.isCancelled()){
        	return;
        }
        
        if (BungeeUtilisals.getInstance().getPmcache().containsKey(p.getName().toLowerCase())) {
        	BungeeUtilisals.getInstance().getPmcache().remove(p.getName().toLowerCase());
        }
        if (BungeeUtilisals.getInstance().getPmcache().containsKey(pl.getName().toLowerCase())) {
        	BungeeUtilisals.getInstance().getPmcache().remove(pl.getName().toLowerCase());
        }
        BungeeUtilisals.getInstance().getPmcache().put(p.getName().toLowerCase(), pl.getName().toLowerCase());
        BungeeUtilisals.getInstance().getPmcache().put(pl.getName().toLowerCase(), p.getName().toLowerCase());
        
        p.sendMessage(TextComponent.fromLegacyText(BungeeUtilisals.getInstance().getConfig().getString("PrivateMessages.MSG.Messages.Format.Sending").replace("%player%", pl.getName()).replace("%server%", pl.getServer().getInfo().getName()).replace("&", "§").replace("%message%", message)));
        pl.sendMessage(TextComponent.fromLegacyText(BungeeUtilisals.getInstance().getConfig().getString("PrivateMessages.MSG.Messages.Format.Receiving").replace("%player%", p.getName()).replace("%server%", p.getServer().getInfo().getName()).replace("&", "§").replace("%message%", message)));
	}

	@Override
	public void onExecute(CommandSender sender, String[] args) {
    	sender.sendMessage(Utils.format("&6Only players can use this command!"));
	}
}
