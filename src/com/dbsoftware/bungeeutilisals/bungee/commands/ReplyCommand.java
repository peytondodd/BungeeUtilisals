package com.dbsoftware.bungeeutilisals.bungee.commands;

import com.dbsoftware.bungeeutilisals.bungee.BungeeUtilisals;
import com.dbsoftware.bungeeutilisals.bungee.events.PrivateMessageEvent;
import com.dbsoftware.bungeeutilisals.bungee.utils.PluginMessageChannel;
import com.dbsoftware.bungeeutilisals.bungee.utils.Utils;
import com.google.common.base.Joiner;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

public class ReplyCommand extends Command {
	
	public ReplyCommand() {
		super("greply", "", BungeeUtilisals.getInstance().getConfig().getStringList("PrivateMessages.MSG.Aliases").toArray(new String[]{}));
	}

	public static void executeReplyCommand(CommandSender sender, String[] args){
        if (!(sender instanceof ProxiedPlayer)) {
        	sender.sendMessage(Utils.format("&6Only players can use this command!"));
            return;
        }
        ProxiedPlayer p = (ProxiedPlayer)sender;
		if(!BungeeUtilisals.getInstance().pmcache.containsKey(p.getName().toLowerCase())){
			p.sendMessage(Utils.format(BungeeUtilisals.getInstance().getConfig().getString("PrivateMessages.Reply.Messages.NoTarget")));
			return;
		}
        if(args.length < 1){
        	p.sendMessage(Utils.format(BungeeUtilisals.getInstance().getConfig().getString("PrivateMessages.Reply.Messages.WrongUsage")));
        	return;
        }
        String target = BungeeUtilisals.getInstance().pmcache.get(p.getName().toLowerCase());
        ProxiedPlayer pl = ProxyServer.getInstance().getPlayer(target);
        if(pl == null){
        	p.sendMessage(Utils.format(BungeeUtilisals.getInstance().getConfig().getString("PrivateMessages.Reply.Messages.OfflineTarget")));
			BungeeUtilisals.getInstance().pmcache.remove(p.getName().toLowerCase());
			BungeeUtilisals.getInstance().pmcache.remove(target.toLowerCase());
        	return;
        }
        String message = Joiner.on(" ").join(args);
        
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
        p.sendMessage(Utils.format(BungeeUtilisals.getInstance().getConfig().getString("PrivateMessages.Reply.Messages.Format.Sending")
        		.replace("%player%", pl.getName()).replace("%message%", message)));
        pl.sendMessage(Utils.format(BungeeUtilisals.getInstance().getConfig().getString("PrivateMessages.Reply.Messages.Format.Receiving")
        		.replace("%player%", p.getName()).replace("%message%", message)));
	}

	@Override
	public void execute(CommandSender sender, String[] args) {
		if(BungeeUtilisals.getInstance().getConfig().getBoolean("Bukkit-Permissions")){
			PluginMessageChannel.sendPermissionCheckPluginMessage("hasPermission", "butilisals.reply", "reply", args, ((ProxiedPlayer)sender));
			return;
		}
		if(sender.hasPermission("butilisals.reply") || sender.hasPermission("butilisals.*")){
			executeReplyCommand(sender, args);		
		} else {
			sender.sendMessage(Utils.format(BungeeUtilisals.getInstance().getConfig().getString("Prefix") + BungeeUtilisals.getInstance().getConfig().getString("Main-messages.no-permission")));
		}
	}
}
