package com.dbsoftware.bungeeutilisals.bungee.actionbarannouncer;

import java.io.File;

import com.dbsoftware.bungeeutilisals.bungee.managers.FileManager;

public class ActionBarAnnouncements {
    private static String path = File.separator + "plugins" + File.separator + "BungeeUtilisals" + File.separator + "barannouncer.yml";
    public static FileManager barannouncements = new FileManager( path );

    public static void reloadAnnouncements() {
        barannouncements = null;
        barannouncements = new FileManager( path );
        ActionBarAnnouncer.reloadAnnouncements();
    }
}