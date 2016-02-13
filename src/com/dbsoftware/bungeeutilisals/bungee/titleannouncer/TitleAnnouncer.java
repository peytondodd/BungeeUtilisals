package com.dbsoftware.bungeeutilisals.bungee.titleannouncer;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.scheduler.ScheduledTask;

import com.dbsoftware.bungeeutilisals.bungee.BungeeUtilisals;
import com.dbsoftware.bungeeutilisals.bungee.tasks.GlobalTitleAnnouncements;
import com.dbsoftware.bungeeutilisals.bungee.tasks.ServerTitleAnnouncements;

public class TitleAnnouncer {
    public static ArrayList<ScheduledTask> announcementTasks = new ArrayList<>();
    static ProxyServer proxy = ProxyServer.getInstance();
    private static BungeeUtilisals instance = BungeeUtilisals.getInstance();

    public static void loadAnnouncements() {
        setDefaults();
        if ( TitleAnnouncements.titleannouncements.getBoolean("Announcements.Enabled", true) ) {
            List<String> global = TitleAnnouncements.titleannouncements.getStringList( "Announcements.Global.Messages", new ArrayList<String>() );
            if ( !global.isEmpty() ) {
            	if(TitleAnnouncements.titleannouncements.getBoolean( "Announcements.Global.Enabled", true)){
                int interval = TitleAnnouncements.titleannouncements.getInt( "Announcements.Global.Interval", 0 );
                if ( interval > 0 ) {
                    GlobalTitleAnnouncements g = new GlobalTitleAnnouncements();
                    for ( String messages : global ) {
                        g.addAnnouncement( messages );
                    }
                    ScheduledTask t = proxy.getScheduler().schedule( instance, g, interval, interval, TimeUnit.SECONDS );
                    announcementTasks.add( t );
                	}
            	}
            }
            for ( String server : proxy.getServers().keySet() ) {
                List<String> servermessages = TitleAnnouncements.titleannouncements.getStringList( "Announcements." + server + ".Messages", new ArrayList<String>() );
                if ( !servermessages.isEmpty() ) {
                	if(TitleAnnouncements.titleannouncements.getBoolean( "Announcements." + server + ".Enabled", false)){
                    int interval = TitleAnnouncements.titleannouncements.getInt( "Announcements." + server + ".Interval", 0 );
                    if ( interval > 0 ) {
                        int fadeIn = TitleAnnouncements.titleannouncements.getInt( "Announcements." + server + ".FadeIn", 30 );
                        int stay = TitleAnnouncements.titleannouncements.getInt( "Announcements." + server + ".Stay", 60 );
                        int fadeOut = TitleAnnouncements.titleannouncements.getInt( "Announcements." + server + ".FadeOut", 30 );
                        
                        ServerTitleAnnouncements s = new ServerTitleAnnouncements( proxy.getServerInfo( server ), fadeIn, stay, fadeOut);
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
        List<String> check = TitleAnnouncements.titleannouncements.getConfigurationSection( "Announcements" );
        if ( !check.contains( "Enabled" ) ) {
            TitleAnnouncements.titleannouncements.setBoolean("Announcements.Enabled", true);
        }
        if ( !check.contains( "Global" ) ) {
            TitleAnnouncements.titleannouncements.setBoolean("Announcements.Global.Enabled", true);
            TitleAnnouncements.titleannouncements.setInt( "Announcements.Global.Interval", 150 );
            
            TitleAnnouncements.titleannouncements.setInt( "Announcements.Global.FadeIn", 30 );
            TitleAnnouncements.titleannouncements.setInt( "Announcements.Global.Stay", 60 );
            TitleAnnouncements.titleannouncements.setInt( "Announcements.Global.FadeOut", 30 );
            
            List<String> l = new ArrayList<String>();
            l.add( "&a&lWelcome to our network!" );
            l.add( "&aThis server is using%n&e&lBungeeUtilisals." );
            TitleAnnouncements.titleannouncements.setStringList( "Announcements.Global.Messages", l );
        }
        for ( String server : proxy.getServers().keySet() ) {
            if ( !check.contains( server ) ) {
                TitleAnnouncements.titleannouncements.setBoolean("Announcements." + server + ".Enabled", false);
                TitleAnnouncements.titleannouncements.setInt( "Announcements." + server + ".Interval", 75 );
                
                TitleAnnouncements.titleannouncements.setInt( "Announcements." + server + ".FadeIn", 30 );
                TitleAnnouncements.titleannouncements.setInt( "Announcements." + server + ".Stay", 60 );
                TitleAnnouncements.titleannouncements.setInt( "Announcements." + server + ".FadeOut", 30 );
                
                List<String> l = new ArrayList<String>();
                l.add( "&aHello %p%,%n&eWelcome to the &a" + server + " &eserver!" );
                l.add( "&aThis server is using BungeeUtilisals!" );
                TitleAnnouncements.titleannouncements.setStringList( "Announcements." + server + ".Messages", l );
            }
        }
        TitleAnnouncements.titleannouncements.save();
    }

    public static void reloadAnnouncements() {
        for ( ScheduledTask task : announcementTasks ) {
            task.cancel();
        }
        announcementTasks.clear();
        loadAnnouncements();
    }
}