package com.dbsoftware.bungeeutilisals.bungee.report;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import net.md_5.bungee.api.ProxyServer;

import com.dbsoftware.bungeeutilisals.bungee.BungeeUtilisals;
import com.dbsoftware.bungeeutilisals.bungee.managers.DatabaseManager;
import com.dbsoftware.bungeeutilisals.bungee.managers.FileManager;

public class Reports {
	
    private static String path = File.separator + "plugins" + File.separator + "BungeeUtilisals" + File.separator + "reports.yml";
    public static FileManager reports = new FileManager( path );
    public static DatabaseManager dbmanager = BungeeUtilisals.getDatabaseManager();

    public static void reloadReportsData() {
        reports = null;
        reports = new FileManager( path );
    }
    
    public static void registerReportSystem(){
    	reports = null;
    	reports = new FileManager( path );
    	if(reports.getBoolean("Reports.Enabled", true)){
    		ProxyServer.getInstance().getPluginManager().registerCommand(BungeeUtilisals.instance, new ReportCommand());
    		ProxyServer.getInstance().getPluginManager().registerCommand(BungeeUtilisals.instance, new ReportListCommand());

        	setDefaults();
    	}
    }
    
    private static void setDefaults(){
        List<String> check = reports.getConfigurationSection( "Reports" );
        if(!check.contains("Broadcast")){
        	reports.setBoolean("Broadcast", true);
        }
        if(!check.contains("Players")){
        	ArrayList<String> players = new ArrayList<String>();
        	players.add("didjee2");
        	reports.setStringList("Players", players);
        }
        if(!check.contains("Cooldown")){
        	reports.setInt("Cooldown", 60);
        }
        if(!check.contains("Messages")){
        	reports.setString("Reports.Messages.WrongArgs", "&cPlease use /report (player) (reason).");
        	reports.setString("Reports.Messages.NoPermission", "&cYou don't have the permission to use the Report Command!");
        	reports.setString("Reports.Messages.NoPlayer", "&cYou can't report an offline player!");
        	reports.setString("Reports.Messages.Reported", "&aYou have reported %player%!");
        	reports.setString("Reports.Messages.Removed", "&cYou have removed report number %number%!");
        	reports.setString("Reports.Messages.ReportListFormat", "&6%number%&b) &6%player% &bhas reported &6%reported% &bfor &6%reason%&b!");
        	reports.setString("Reports.Messages.WrongListArgs", "&cPlease use /reports (page)!");
        	reports.setString("Reports.Messages.NoReportNumberExist", "&cReport number %number% doesn't exist!");
        	reports.setString("Reports.Messages.NoReportsOnThisPage", "&cThere are no reports on this page!");
        	reports.setString("Reports.Messages.CooldownMessage", "&cYou need to wait &6%time% &cseconds untill you can report again!");
			reports.setString("Reports.Messages.EnabledAlert", "&cNew reports will now be alerted!");
			reports.setString("Reports.Messages.DisabledAlert", "&cNew reports will not be alerted anymore!");
			reports.setString("Reports.Messages.WrongNumber", "&cPlease use use a valid number!");
    	}
    	reports.save();
    } 
}
