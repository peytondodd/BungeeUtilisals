package com.dbsoftware.bungeeutilisals.bungee.report;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;

import com.dbsoftware.bungeeutilisals.bungee.BungeeUtilisals;
import com.dbsoftware.bungeeutilisals.bungee.managers.FileManager;

import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.config.Configuration;

public class Reports {

	private static String path = File.separator + "plugins" + File.separator + "BungeeUtilisals" + File.separator
			+ "reports.yml";
	public static FileManager reports = new FileManager(path);

	public static void reloadReportsData() {
		reports = null;
		reports = new FileManager(path);
	}

	public static void registerReportSystem() {
		reports = null;
		reports = new FileManager(path);
		if (reports.getFile().getBoolean("Reports.Enabled", true)) {
			ProxyServer.getInstance().getPluginManager().registerCommand(BungeeUtilisals.getInstance(),
					new ReportCommand());
			ProxyServer.getInstance().getPluginManager().registerCommand(BungeeUtilisals.getInstance(),
					new ReportListCommand());

			setDefaults();
		}
	}

	private static void setDefaults() {
		Configuration cs = reports.getFile().getSection("Reports");
		Collection<String> check = cs.getKeys();
		if (!check.contains("Broadcast")) {
			reports.getFile().set("Broadcast", true);
		}
		if (!check.contains("Players")) {
			ArrayList<String> players = new ArrayList<String>();
			players.add("didjee2");
			reports.getFile().set("Players", players);
		}
		if (!check.contains("Cooldown")) {
			reports.getFile().set("Cooldown", 60);
		}
		if (!check.contains("Messages")) {
			reports.getFile().set("Reports.Messages.WrongArgs", "&cPlease use /report (player) (reason).");
			reports.getFile().set("Reports.Messages.NoPermission",
					"&cYou don't have the permission to use the Report Command!");
			reports.getFile().set("Reports.Messages.NoPlayer", "&cYou can't report an offline player!");
			reports.getFile().set("Reports.Messages.Reported", "&aYou have reported %player%!");
			reports.getFile().set("Reports.Messages.Removed", "&cYou have removed report number %number%!");
			reports.getFile().set("Reports.Messages.ReportListFormat",
					"&6%number%&b) &6%player% &bhas reported &6%reported% &bfor &6%reason%&b!");
			reports.getFile().set("Reports.Messages.WrongListArgs", "&cPlease use /reports (page)!");
			reports.getFile().set("Reports.Messages.NoReportNumberExist", "&cReport number %number% doesn't exist!");
			reports.getFile().set("Reports.Messages.NoReportsOnThisPage", "&cThere are no reports on this page!");
			reports.getFile().set("Reports.Messages.CooldownMessage",
					"&cYou need to wait &6%time% &cseconds untill you can report again!");
			reports.getFile().set("Reports.Messages.EnabledAlert", "&cNew reports will now be alerted!");
			reports.getFile().set("Reports.Messages.DisabledAlert", "&cNew reports will not be alerted anymore!");
			reports.getFile().set("Reports.Messages.WrongNumber", "&cPlease use use a valid number!");
		}
		reports.save();
	}
}
