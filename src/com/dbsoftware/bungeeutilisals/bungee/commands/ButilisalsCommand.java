package com.dbsoftware.bungeeutilisals.bungee.commands;

import com.dbsoftware.bungeeutilisals.api.DBCommand;
import com.dbsoftware.bungeeutilisals.bungee.BungeeUser;
import com.dbsoftware.bungeeutilisals.bungee.BungeeUtilisals;
import com.dbsoftware.bungeeutilisals.bungee.actionbarannouncer.ActionBarAnnouncements;
import com.dbsoftware.bungeeutilisals.bungee.announcer.Announcements;
import com.dbsoftware.bungeeutilisals.bungee.party.Party;
import com.dbsoftware.bungeeutilisals.bungee.punishment.Punishments;
import com.dbsoftware.bungeeutilisals.bungee.report.Reports;
import com.dbsoftware.bungeeutilisals.bungee.tabmanager.TabManager;
import com.dbsoftware.bungeeutilisals.bungee.titleannouncer.TitleAnnouncements;
import com.dbsoftware.bungeeutilisals.bungee.utils.Utils;

import net.md_5.bungee.api.CommandSender;

public class ButilisalsCommand extends DBCommand {
	
	public ButilisalsCommand() {
		super("butilisals", "butili");
		permissions.add("butilisals.admin");
	}
	
	@Override
	public void onExecute(BungeeUser user, String[] args) {
		onExecute(user.sender(), args);
	}
	
	@Override
	public void onExecute(CommandSender sender, String[] args) {
		if(args.length == 1){
			if(args[0].equalsIgnoreCase("reload")){
				BungeeUtilisals.getInstance().reloadConfig();
			    Announcements.reloadAnnouncements();
			    TitleAnnouncements.reloadAnnouncements();
			    ActionBarAnnouncements.reloadAnnouncements();
			    Reports.reloadReportsData();
			    Party.reloadPartyData();
			    Punishments.reloadPunishmentData();
			    TabManager.reloadTab();
				sender.sendMessage(Utils.format("&e&lBungeeUtilisals &8» &6Config reloaded!"));
				return;
			}
		}
		sender.sendMessage(Utils.format("&e&lBungeeUtilisals &8» &6Version &b" + BungeeUtilisals.getInstance().getDescription().getVersion() + " &6created by &bdidjee2&6!"));
		sender.sendMessage(Utils.format("&e&lBungeeUtilisals &8» &6Please use &b/butilisals reload &6to reload the plugin!"));
	}
}