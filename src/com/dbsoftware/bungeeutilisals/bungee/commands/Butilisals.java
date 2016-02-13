package com.dbsoftware.bungeeutilisals.bungee.commands;

import com.dbsoftware.bungeeutilisals.bungee.BungeeUtilisals;
import com.dbsoftware.bungeeutilisals.bungee.actionbarannouncer.ActionBarAnnouncements;
import com.dbsoftware.bungeeutilisals.bungee.announcer.Announcements;
import com.dbsoftware.bungeeutilisals.bungee.friends.Friends;
import com.dbsoftware.bungeeutilisals.bungee.party.Party;
import com.dbsoftware.bungeeutilisals.bungee.punishment.Punishments;
import com.dbsoftware.bungeeutilisals.bungee.report.Reports;
import com.dbsoftware.bungeeutilisals.bungee.titleannouncer.TitleAnnouncements;
import com.dbsoftware.bungeeutilisals.bungee.utils.PluginMessageChannel;
import com.dbsoftware.bungeeutilisals.bungee.utils.Utils;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

public class Butilisals extends Command {
	
	public Butilisals() {
		super("butilisals", "", "butili");{
		}
	}
		
	public static void executeButilisalsCommand(CommandSender sender, String[] args){		
		if(args.length == 1){
			if(args[0].equalsIgnoreCase("reload")){
				ReloadConfig(sender);
				return;
			} else if(args[0].equalsIgnoreCase("lock")){
				toggleChat(sender);
				return;
			}
		}
		sender.sendMessage(Utils.format("&e&lBungeeUtilisals &8» &6Version &b" + BungeeUtilisals.getInstance().getDescription().getVersion() + " &6created by &bdidjee2&6!"));
		sender.sendMessage(Utils.format("&e&lBungeeUtilisals &8» &6Please use &b/butilisals reload &6to reload the plugin!"));
	}
	
	private static void toggleChat(CommandSender sender) {
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
	  }
	
	private static void ReloadConfig(CommandSender sender){
		BungeeUtilisals.getInstance().reloadConfig();
	    Announcements.reloadAnnouncements();
	    TitleAnnouncements.reloadAnnouncements();
	    ActionBarAnnouncements.reloadAnnouncements();
	    Friends.reloadFriendsData();
	    Reports.reloadReportsData();
	    Party.reloadPartyData();
	    Punishments.reloadPunishmentData();
		sender.sendMessage(Utils.format("&e&lBungeeUtilisals &8» &6Config reloaded!"));
	}
	
	@Override
	public void execute(CommandSender sender, String[] args) {
		if(!(sender instanceof ProxiedPlayer)){
			executeButilisalsCommand(sender, args);
			return;
		}
		if(BungeeUtilisals.getInstance().getConfig().getBoolean("Bukkit-Permissions")){
			PluginMessageChannel.sendPermissionCheckPluginMessage("hasPermission", "butilisals.admin", "butilisals", args, ((ProxiedPlayer)sender));
			return;
		}
		if(sender.hasPermission("butilisals.admin") || sender.hasPermission("butilisals.*")){
			executeButilisalsCommand(sender, args);		
		} else {
			sender.sendMessage(Utils.format(BungeeUtilisals.getInstance().getConfig().getString("Prefix") + BungeeUtilisals.getInstance().getConfig().getString("Main-messages.no-permission")));
		}
	}
}
