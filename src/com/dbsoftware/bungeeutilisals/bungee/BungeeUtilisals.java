package com.dbsoftware.bungeeutilisals.bungee;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.concurrent.TimeUnit;
import com.dbsoftware.bungeeutilisals.bungee.actionbarannouncer.ActionBarAnnouncer;
import com.dbsoftware.bungeeutilisals.bungee.announcer.Announcer;
import com.dbsoftware.bungeeutilisals.bungee.commands.Alert;
import com.dbsoftware.bungeeutilisals.bungee.commands.Bgc;
import com.dbsoftware.bungeeutilisals.bungee.commands.Bigalert;
import com.dbsoftware.bungeeutilisals.bungee.commands.Butilisals;
import com.dbsoftware.bungeeutilisals.bungee.commands.ClearChat;
import com.dbsoftware.bungeeutilisals.bungee.commands.Find;
import com.dbsoftware.bungeeutilisals.bungee.commands.Glist;
import com.dbsoftware.bungeeutilisals.bungee.commands.Hub;
import com.dbsoftware.bungeeutilisals.bungee.commands.Rules;
import com.dbsoftware.bungeeutilisals.bungee.commands.Server;
import com.dbsoftware.bungeeutilisals.bungee.commands.Store;
import com.dbsoftware.bungeeutilisals.bungee.commands.Vote;
import com.dbsoftware.bungeeutilisals.bungee.events.AntiAd;
import com.dbsoftware.bungeeutilisals.bungee.events.AntiCaps;
import com.dbsoftware.bungeeutilisals.bungee.events.AntiSpam;
import com.dbsoftware.bungeeutilisals.bungee.events.AntiSwear;
import com.dbsoftware.bungeeutilisals.bungee.events.ChatLock;
import com.dbsoftware.bungeeutilisals.bungee.events.ChatUtilities;
import com.dbsoftware.bungeeutilisals.bungee.events.DisconnectEvent;
import com.dbsoftware.bungeeutilisals.bungee.events.LoginEvent;
import com.dbsoftware.bungeeutilisals.bungee.events.MessageLimiter;
import com.dbsoftware.bungeeutilisals.bungee.events.PluginMessageReceive;
import com.dbsoftware.bungeeutilisals.bungee.events.ServerSwitch;
import com.dbsoftware.bungeeutilisals.bungee.friends.Friends;
import com.dbsoftware.bungeeutilisals.bungee.managers.DatabaseManager;
import com.dbsoftware.bungeeutilisals.bungee.metrics.Metrics;
import com.dbsoftware.bungeeutilisals.bungee.party.Party;
import com.dbsoftware.bungeeutilisals.bungee.punishment.Punishments;
import com.dbsoftware.bungeeutilisals.bungee.report.Reports;
import com.dbsoftware.bungeeutilisals.bungee.staffchat.StaffChat;
import com.dbsoftware.bungeeutilisals.bungee.tabmanager.TabManager;
import com.dbsoftware.bungeeutilisals.bungee.titleannouncer.TitleAnnouncer;
import com.dbsoftware.bungeeutilisals.bungee.updater.UpdateChecker;
import com.dbsoftware.bungeeutilisals.bungee.utils.TPSRunnable;
import com.google.common.io.ByteStreams;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.config.Configuration;
import net.md_5.bungee.config.ConfigurationProvider;
import net.md_5.bungee.config.YamlConfiguration;

/**
 * 
 * @author Dieter
 *
 */

public class BungeeUtilisals extends Plugin {

	public static BungeeUtilisals instance;
    public static DatabaseManager dbmanager;
    public boolean chatMuted = false;
    public static boolean update;
    private File configfile;
    private Configuration config;
    
	public void onEnable(){
		instance = this;

		this.configfile = this.loadResource(this, "config.yml");
	    try {
			this.config = ConfigurationProvider.getProvider(YamlConfiguration.class).load(this.configfile);
		} catch (IOException e) {
			e.printStackTrace();
		}
	    			    
	    loadCommands();
	    registerEvents();
	    Announcer.loadAnnouncements();
	    TitleAnnouncer.loadAnnouncements();
	    ActionBarAnnouncer.loadAnnouncements();
	    StaffChat.registerStaffChat();
		
	    ProxyServer.getInstance().getScheduler().schedule(BungeeUtilisals.getInstance(), new TPSRunnable(), 50, TimeUnit.MILLISECONDS);
	    	    
	    if(getConfig().getBoolean("UpdateChecker")){
	    	UpdateChecker.checkUpdate(instance.getDescription().getVersion());
	    	UpdateRunnable();
	    }
	    
	    try {
	        Metrics metrics = new Metrics(this);
	        metrics.start();
	    } catch (IOException e) {
	        ProxyServer.getInstance().getLogger().info("[BungeeUtilisals] Metrics could not be enabled.");
	    }
	    
	    Party.registerPartySystem();
	    
	    if(getConfig().getBoolean("MySQL.Enabled")){
	    	String host = getConfig().getString("MySQL.Host", "localhost");
			int port = getConfig().getInt("MySQL.Port", 3306);
			String database = getConfig().getString("MySQL.Database", "database");
			String username = getConfig().getString("MySQL.Username", "username");
			String password = getConfig().getString("MySQL.Password", "password");
			dbmanager = new DatabaseManager(host, port, database, username, password);
			dbmanager.openConnection();
			ProxyServer.getInstance().getScheduler().schedule(BungeeUtilisals.getInstance(), new Runnable(){

				@Override
				public void run(){
					dbmanager.closeConnection();
					dbmanager.openConnection();
				}
				
			}, 5, 5, TimeUnit.MINUTES);				
		    Friends.registerFriendsAddons();
		    Reports.registerReportSystem();
			Punishments.registerPunishmentSystem();
	    }
	    
	    TabManager.loadTab();
	    
	    
	    ProxyServer.getInstance().getLogger().info("BungeeUtilisals is now Enabled!");
	}
	
	public boolean reloadConfig(){
		try {
			this.config = ConfigurationProvider.getProvider(YamlConfiguration.class).load(this.configfile);
			return true;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return false;
	}
	
	public Configuration getConfig(){
		return this.config;
	}
	
    public DatabaseManager getDatabaseManager(){
    	return dbmanager;
    }
	
	private void loadCommands(){
		ProxyServer.getInstance().getPluginManager().registerCommand(this, new Alert());
		ProxyServer.getInstance().getPluginManager().registerCommand(this, new Bigalert());
		if(getConfig().getBoolean("GList.Enabled")){
			ProxyServer.getInstance().getPluginManager().registerCommand(this, new Glist());
		}
		ProxyServer.getInstance().getPluginManager().registerCommand(this, new Find());
		ProxyServer.getInstance().getPluginManager().registerCommand(this, new Bgc());
	    ProxyServer.getInstance().getPluginManager().registerCommand(this, new Butilisals());
	    ProxyServer.getInstance().getPluginManager().registerCommand(this, new Server());
	    
	    if(getConfig().getBoolean("ClearChat.Enabled")){
	    	ProxyServer.getInstance().getPluginManager().registerCommand(this, new ClearChat());
	    }
	    if(getConfig().getBoolean("Hub.Enabled")){
	    	String cmd = getConfig().getString("Hub.Command");
	    	if(cmd.contains(";")){
	    		String c = cmd.split(";")[0];
	    		String ncmd = cmd.replace(c + ";", "");
	    		String[] cmds;
	    		if(ncmd.contains(";")){
	    			cmds = ncmd.split(";");
	    		} else {
	    			cmds = new String[]{ncmd};
	    		}
	    		
	    		ProxyServer.getInstance().getPluginManager().registerCommand(this, new Hub(c, cmds));
	    	} else {
		    	ProxyServer.getInstance().getPluginManager().registerCommand(this, new Hub(cmd));
	    	}
	    }
	    if(getConfig().getBoolean("Vote.Enabled")){
	    	ProxyServer.getInstance().getPluginManager().registerCommand(this, new Vote());
	    }
	    if(getConfig().getBoolean("Store.Enabled")){
	    	ProxyServer.getInstance().getPluginManager().registerCommand(this, new Store());
	    }
	    if(getConfig().getBoolean("Rules.Enabled")){
	    	ProxyServer.getInstance().getPluginManager().registerCommand(this, new Rules());
	    }
	}
	
	private void UpdateRunnable(){
	    ProxyServer.getInstance().getScheduler().schedule(instance, new Runnable(){
			public void run() {
				UpdateChecker.checkUpdate(instance.getDescription().getVersion());
			}
	    }, 15, 15, TimeUnit.MINUTES);
	}
	
	private void registerEvents(){
	    ProxyServer.getInstance().getPluginManager().registerListener(this, new MessageLimiter(this));
	    ProxyServer.getInstance().getPluginManager().registerListener(this, new DisconnectEvent(this));
	    ProxyServer.getInstance().getPluginManager().registerListener(this, new LoginEvent(this));
	    ProxyServer.getInstance().getPluginManager().registerListener(this, new AntiSpam(this));
	    ProxyServer.getInstance().getPluginManager().registerListener(this, new ChatLock(this));
	    ProxyServer.getInstance().getPluginManager().registerListener(this, new ChatUtilities(this));
	    ProxyServer.getInstance().getPluginManager().registerListener(this, new AntiSwear(this));
	    ProxyServer.getInstance().getPluginManager().registerListener(this, new AntiCaps(this));
	    ProxyServer.getInstance().getPluginManager().registerListener(this, new AntiAd(this));
	    ProxyServer.getInstance().getPluginManager().registerListener(this, new ServerSwitch(this));
	    ProxyServer.getInstance().getPluginManager().registerListener(this, new PluginMessageReceive());
	}
	
	public void onDisable(){
	    dbmanager.closeConnection();
	    	    
	    ProxyServer.getInstance().getLogger().info("BungeeUtilisals is now Disabled!");
	}
	
	public static BungeeUtilisals getInstance(){
		return instance;
	}
	
    public File loadResource(Plugin plugin, String resource) {
        File folder = plugin.getDataFolder();
        if (!folder.exists()){
            folder.mkdir();
        }
        File resourceFile = new File(folder, resource);
        try {
            if (!resourceFile.exists()) {
                resourceFile.createNewFile();
                try (InputStream in = plugin.getResourceAsStream(resource); OutputStream out = new FileOutputStream(resourceFile)){
                	ByteStreams.copy(in, out);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return resourceFile;
    }
}
