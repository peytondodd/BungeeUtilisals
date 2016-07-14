package com.dbsoftware.bungeeutilisals.bungee.commands;

import java.util.ArrayList;
import java.util.List;

import com.dbsoftware.bungeeutilisals.bungee.BungeeUtilisals;
import com.dbsoftware.bungeeutilisals.bungee.events.PrivateMessageEvent;
import com.dbsoftware.bungeeutilisals.bungee.utils.PluginMessageChannel;
import com.dbsoftware.bungeeutilisals.bungee.utils.Utils;
import com.google.common.base.Joiner;

import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

public class MSGCommand extends Command {
	
	public MSGCommand() {
		super("gmsg", "", BungeeUtilisals.getInstance().getConfig().getStringList("PrivateMessages.MSGAliases").toArray(new String[]{}));
	}

	public static void executeMSGCommand(CommandSender sender, String[] args){
        if (!(sender instanceof ProxiedPlayer)) {
        	sender.sendMessage(Utils.format("&6Only players can use this command!"));
            return;
        }
        ProxiedPlayer p = (ProxiedPlayer)sender;
        if(args.length < 2){
        	p.sendMessage(Utils.format("&4Please use /gmsg (player) (message)!"));
        	return;
        }
        String target = args[0];
        ProxiedPlayer pl = ProxyServer.getInstance().getPlayer(target);
        if(pl == null){
        	p.sendMessage(Utils.format("&4That player is not online!"));
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
        
        if (BungeeUtilisals.getInstance().pmcache.containsKey(p.getName().toLowerCase())) {
        	BungeeUtilisals.getInstance().pmcache.remove(p.getName().toLowerCase());
        }
        if (BungeeUtilisals.getInstance().pmcache.containsKey(pl.getName().toLowerCase())) {
        	BungeeUtilisals.getInstance().pmcache.remove(pl.getName().toLowerCase());
        }
        BungeeUtilisals.getInstance().pmcache.put(p.getName().toLowerCase(), pl.getName().toLowerCase());
        BungeeUtilisals.getInstance().pmcache.put(pl.getName().toLowerCase(), p.getName().toLowerCase());
        p.sendMessage(Utils.format("&8[&6Me &c» &6" + pl.getName() + "&8] &r" + message));
        pl.sendMessage(Utils.format("&8[&6" + p.getName() + " &c» &6Me&8] &r" + message));
	}

	@Override
	public void execute(CommandSender sender, String[] args) {
		if(BungeeUtilisals.getInstance().getConfig().getBoolean("Bukkit-Permissions")){
			PluginMessageChannel.sendPermissionCheckPluginMessage("hasPermission", "butilisals.msg", "msg", args, ((ProxiedPlayer)sender));
			return;
		}
		if(sender.hasPermission("butilisals.msg") || sender.hasPermission("butilisals.*")){
			executeMSGCommand(sender, args);		
		} else {
			sender.sendMessage(Utils.format(BungeeUtilisals.getInstance().getConfig().getString("Prefix") + BungeeUtilisals.getInstance().getConfig().getString("Main-messages.no-permission")));
		}
	}
}
