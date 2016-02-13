package com.dbsoftware.bungeeutilisals.bungee.actionbarannouncer;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.scheduler.ScheduledTask;
import com.dbsoftware.bungeeutilisals.bungee.BungeeUtilisals;
import com.dbsoftware.bungeeutilisals.bungee.tasks.GlobalActionBarAnnouncements;
import com.dbsoftware.bungeeutilisals.bungee.tasks.ServerActionBarAnnouncements;

public class ActionBarAnnouncer {
    public static ArrayList<ScheduledTask> announcementTasks = new ArrayList<>();
    static ProxyServer proxy = ProxyServer.getInstance();
    private static BungeeUtilisals instance = BungeeUtilisals.getInstance();

    public static void loadAnnouncements() {
        setDefaults();
        if ( ActionBarAnnouncements.barannouncements.getBoolean("Announcements.Enabled", true) ) {
            List<String> global = ActionBarAnnouncements.barannouncements.getStringList( "Announcements.Global.Messages", new ArrayList<String>() );
            if ( !global.isEmpty() ) {
            	if(ActionBarAnnouncements.barannouncements.getBoolean( "Announcements.Global.Enabled", true)){
                int interval = ActionBarAnnouncements.barannouncements.getInt( "Announcements.Global.Interval", 0 );
                if ( interval > 0 ) {
                    GlobalActionBarAnnouncements g = new GlobalActionBarAnnouncements();
                    for ( String messages : global ) {
                        g.addAnnouncement( messages );
                    }
                    ScheduledTask t = proxy.getScheduler().schedule( instance, g, interval, interval, TimeUnit.SECONDS );
                    announcementTasks.add( t );
                	}
            	}
            }
            for ( String server : proxy.getServers().keySet() ) {
                List<String> servermessages = ActionBarAnnouncements.barannouncements.getStringList( "Announcements." + server + ".Messages", new ArrayList<String>() );
                if ( !servermessages.isEmpty() ) {
                	if(ActionBarAnnouncements.barannouncements.getBoolean( "Announcements." + server + ".Enabled", false)){
                    int interval = ActionBarAnnouncements.barannouncements.getInt( "Announcements." + server + ".Interval", 0 );
                    if ( interval > 0 ) {
                        
                        ServerActionBarAnnouncements s = new ServerActionBarAnnouncements( proxy.getServerInfo( server ));
                        for ( String messages : servermessages ) {
                            s.addAnnouncement( messages );
                        }
                        ScheduledTask t = proxy.getScheduler().schedule( instance, s, interval, interval, TimeUnit.SECONDS );
                        announcementTasks.add( t );
                    	}
                	}
                }
            }
        }
    }

    private static void setDefaults() {
        List<String> check = ActionBarAnnouncements.barannouncements.getConfigurationSection( "Announcements" );
        if ( !check.contains( "Enabled" ) ) {
            ActionBarAnnouncements.barannouncements.setBoolean("Announcements.Enabled", true);
        }
        if ( !check.contains( "Global" ) ) {
            ActionBarAnnouncements.barannouncements.setBoolean("Announcements.Global.Enabled", true);
            ActionBarAnnouncements.barannouncements.setInt( "Announcements.Global.Interval", 150 );
            
            List<String> l = new ArrayList<String>();
            l.add( "&a&lWelcome to our network!" );
            l.add( "&aThis server is using &e&lBungeeUtilisals." );
            ActionBarAnnouncements.barannouncements.setStringList( "Announcements.Global.Messages", l );
        }
        for ( String server : proxy.getServers().keySet() ) {
            if ( !check.contains( server ) ) {
                ActionBarAnnouncements.barannouncements.setBoolean("Announcements." + server + ".Enabled", false);
                ActionBarAnnouncements.barannouncements.setInt( "Announcements." + server + ".Interval", 75 );
                
                List<String> l = new ArrayList<String>();
                l.add( "&aHello %p%, &eWelcome to the &a" + server + " &eserver!" );
                l.add( "&aThis server is using BungeeUtilisals!" );
                ActionBarAnnouncements.barannouncements.setStringList( "Announcements." + server + ".Messages", l );
            }
        }
        ActionBarAnnouncements.barannouncements.save();
    }

    public static void reloadAnnouncements() {
        for ( ScheduledTask task : announcementTasks ) {
            task.cancel();
        }
        announcementTasks.clear();
        loadAnnouncements();
    }
}