package com.dbsoftware.bungeeutilisals.bungee.punishment;

import java.io.File;
import java.util.Arrays;
import java.util.List;

import com.dbsoftware.bungeeutilisals.bungee.BungeeUtilisals;
import com.dbsoftware.bungeeutilisals.bungee.managers.DatabaseManager;
import com.dbsoftware.bungeeutilisals.bungee.managers.FileManager;
import com.dbsoftware.bungeeutilisals.bungee.punishment.commands.BanCommand;
import com.dbsoftware.bungeeutilisals.bungee.punishment.commands.BanIPCommand;
import com.dbsoftware.bungeeutilisals.bungee.punishment.commands.KickCommand;
import com.dbsoftware.bungeeutilisals.bungee.punishment.commands.MuteCommand;
import com.dbsoftware.bungeeutilisals.bungee.punishment.commands.PlayerInfoCommand;
import com.dbsoftware.bungeeutilisals.bungee.punishment.commands.TempbanCommand;
import com.dbsoftware.bungeeutilisals.bungee.punishment.commands.TempmuteCommand;
import com.dbsoftware.bungeeutilisals.bungee.punishment.commands.UnbanCommand;
import com.dbsoftware.bungeeutilisals.bungee.punishment.commands.UnmuteCommand;
import com.dbsoftware.bungeeutilisals.bungee.punishment.commands.WarnCommand;
import com.dbsoftware.bungeeutilisals.bungee.punishment.listeners.ChatListener;
import com.dbsoftware.bungeeutilisals.bungee.punishment.listeners.LoginListener;

import net.md_5.bungee.api.ProxyServer;

public class Punishments {
	
    private static String path = File.separator + "plugins" + File.separator + "BungeeUtilisals" + File.separator + "bans.yml";
    public static FileManager punishments = new FileManager( path );
    public static DatabaseManager dbmanager = BungeeUtilisals.getDatabaseManager();

    public static void reloadPunishmentData() {
        punishments = null;
        punishments = new FileManager( path );
    }
    
    public static void registerPunishmentSystem(){
    	punishments = null;
    	punishments = new FileManager( path );
    	if(punishments.getBoolean("Punishments.Enabled", true)){
        	setDefaults();
        	ProxyServer.getInstance().getPluginManager().registerCommand(BungeeUtilisals.getInstance(), new BanCommand());
        	ProxyServer.getInstance().getPluginManager().registerCommand(BungeeUtilisals.getInstance(), new UnbanCommand());
        	ProxyServer.getInstance().getPluginManager().registerCommand(BungeeUtilisals.getInstance(), new KickCommand());
        	ProxyServer.getInstance().getPluginManager().registerCommand(BungeeUtilisals.getInstance(), new TempbanCommand());
        	ProxyServer.getInstance().getPluginManager().registerCommand(BungeeUtilisals.getInstance(), new BanIPCommand());
        	ProxyServer.getInstance().getPluginManager().registerCommand(BungeeUtilisals.getInstance(), new PlayerInfoCommand());
        	ProxyServer.getInstance().getPluginManager().registerCommand(BungeeUtilisals.getInstance(), new MuteCommand());
        	ProxyServer.getInstance().getPluginManager().registerCommand(BungeeUtilisals.getInstance(), new TempmuteCommand());
        	ProxyServer.getInstance().getPluginManager().registerCommand(BungeeUtilisals.getInstance(), new UnmuteCommand());
        	ProxyServer.getInstance().getPluginManager().registerCommand(BungeeUtilisals.getInstance(), new WarnCommand());
        	
        	ProxyServer.getInstance().getPluginManager().registerListener(BungeeUtilisals.getInstance(), new LoginListener());
        	ProxyServer.getInstance().getPluginManager().registerListener(BungeeUtilisals.getInstance(), new ChatListener());
    	}
    }
    
    private static void setDefaults(){
        List<String> check = punishments.getConfigurationSection( "Punishments" );
        if(!check.contains("Settings")){
        	punishments.setStringList("Punishments.Settings.Blocked-Cmds-While-Muted", Arrays.asList(new String[]{"/msg", "/w", "/tell"}));
        }
        if(!check.contains("Messages")){
        	punishments.setStringList("Punishments.Messages.NoPermission", Arrays.asList(new String[]{"&4&lPermissions &8» &cYou don't have the permission to use this command!"}));
        }
        if(!check.contains("Ban")){
        	punishments.setStringList("Punishments.Ban.Messages.WrongArgs", Arrays.asList(new String[]{"&4&lBans &8» &ePlease use &b/ban (player) (reason) &e!"}));
        	punishments.setStringList("Punishments.Ban.Messages.KickMessage", Arrays.asList(new String[]{
							"&cBungeeUtilisals &8» &7Banned",
							" ",
							"&cReason &8» &7%reason%",
							"&cUnban at &8» &7%unbantime%",
						 	"&cBanned by &8» &7%kicker%"}));
        	punishments.setStringList("Punishments.Ban.Messages.AlreadyBanned", Arrays.asList(new String[]{"&4&lBans &8» &cThat player is already banned!"}));
        	punishments.getStringList("Punishments.Ban.Messages.Banned", Arrays.asList(new String[]{"&4&lBans &8» &b%player% &ehas been banned! &bReason: &e%reason%"}));
        }
        if(!check.contains("Unban")){
        	punishments.setStringList("Punishments.Unban.Messages.WrongArgs", Arrays.asList(new String[]{"&4&lBans &8» &ePlease use &b/unban (player)&e!"}));
        	punishments.setStringList("Punishments.Unban.Messages.NotBanned", Arrays.asList(new String[]{"&4&lBans &8» &cThat player is not banned!"}));
        	punishments.getStringList("Punishments.Unban.Messages.Unbanned", Arrays.asList(new String[]{"&4&lBans &8» &b%player% &ehas been unbanned!"}));
        	punishments.setStringList("Punishments.Unban.Messages.NeverJoined", Arrays.asList(new String[]{"&4&lBans &8» &cThat player never joined!"}));
        }
        if(!check.contains("Kick")){
        	punishments.setStringList("Punishments.Kick.Messages.WrongArgs", Arrays.asList(new String[]{"&4&lBans &8» &ePlease use &b/kick (player) (reason) &e!"}));
        	punishments.setStringList("Punishments.Kick.Messages.NotOnline", Arrays.asList(new String[]{"&4&lBans &8» &eThat player is not online!"}));
        	punishments.setStringList("Punishments.Kick.Messages.KickMessage", Arrays.asList(new String[]{
					"&cBungeeUtilisals &8» &7Kicked",
					" ",
					"&cReason &8» &7%reason%",
				 	"&cKicked by &8» &7%banner%"}));
        	punishments.setStringList("Punishments.Kick.Messages.Kicked", Arrays.asList(new String[]{"&4&lBans &8» &b%player% &ehas been kicked from the server!"}));
        }
        if(!check.contains("Tempban")){
        	punishments.setStringList("Punishments.Tempban.Messages.WrongArgs", Arrays.asList(new String[]{"&4&lBans &8» &ePlease use &b/tempban (player) (time) (reason) &e!"}));
        	punishments.setStringList("Punishments.Tempban.Messages.AlreadyBanned", Arrays.asList(new String[]{"&4&lBans &8» &cThat player is already banned!"}));
        	punishments.setStringList("Punishments.Tempban.Messages.KickMessage", Arrays.asList(new String[]{
					"&cBungeeUtilisals &8» &7Tempbanned",
					" ",
					"&cReason &8» &7%reason%",
					"&cUnban at &8» &7%unbantime%",
				 	"&cBanned by &8» &7%banner%"}));
        	punishments.setStringList("Punishments.Tempban.Messages.Banned", Arrays.asList(new String[]{"&4&lBans &8» &b%player% &ehas been tempbanned! &bReason: &e%reason%"}));
        	punishments.setStringList("Punishments.Tempban.Messages.WrongTime", Arrays.asList(new String[]{"&4&lBans &8» &ePlease use a good TimeUnit!"}));
        }
        if(!check.contains("BanIP")){
        	punishments.setStringList("Punishments.BanIP.Messages.WrongArgs", Arrays.asList(new String[]{"&4&lBans &8» &ePlease use &b/banip (player) (reason) &e!"}));
        	punishments.setStringList("Punishments.BanIP.Messages.AlreadyBanned", Arrays.asList(new String[]{"&4&lBans &8» &cThat IP is already banned!"}));
        	punishments.setStringList("Punishments.BanIP.Messages.KickMessage", Arrays.asList(new String[]{
					"&cBungeeUtilisals &8» &7IPBanned",
					" ",
					"&cReason &8» &7%reason%",
				 	"&cBanned by &8» &7%banner%"}));
        	punishments.setStringList("Punishments.BanIP.Messages.Banned", Arrays.asList(new String[]{"&4&lBans &8» &b%ip% &ehas been banned! &bReason: &e%reason%"}));
        	punishments.setStringList("Punishments.BanIP.Messages.NeverJoined", Arrays.asList(new String[]{"&4&lBans &8» &cThat player never joined!"}));
        } if(!check.contains("Mute")){
        	punishments.setStringList("Punishments.Mute.Messages.WrongArgs", Arrays.asList(new String[]{"&4&lBans &8» &ePlease use &b/mute (player) (reason) &e!"}));
        	punishments.setStringList("Punishments.Mute.Messages.AlreadyBanned", Arrays.asList(new String[]{"&4&lBans &8» &cThat player is already muted!"}));
        	punishments.setStringList("Punishments.Mute.Messages.MuteMessage", Arrays.asList(new String[]{
					"&4&lBans &8» &b%player% &ehas muted you!",
					"&4&lBans &8» &eReason: &b%reason%"}));
        	punishments.setStringList("Punishments.Mute.Messages.Muted", Arrays.asList(new String[]{"&4&lBans &8» &b%player% &ehas been Muted! &bReason: &e%reason%"}));
        } if(!check.contains("PlayerInfo")){
        	punishments.setStringList("Punishments.PlayerInfo.Messages.WrongArgs", Arrays.asList(new String[]{"&4&lBans &8» &ePlease use &b/pinfo (player)&e!"}));
        	punishments.setStringList("Punishments.PlayerInfo.Messages.NeverJoined", Arrays.asList(new String[]{"&4&lBans &8» &eThat player never joined before!"}));
        	punishments.setStringList("Punishments.PlayerInfo.Messages.Info",
					Arrays.asList(new String[]{"&4&lBans &8» &eInformation of &b%player%&e:",
					" &eIP: &b%ip%",
					" &eBans: &b%bans%",
					" &eMutes: &b%mutes%",
					" &eKicks: &b%kicks%",
					" &eWarns: &b%warns%",
					" &eCurrent Ban: &b%isbanned%",
					" &eCurrent IPBan: &b%isIPbanned%",
					" &eCurrent Mute: &b%ismuted%"}));
        	
        	punishments.setString("Punishments.PlayerInfo.Messages.isBanned", "&cBanned by %banner% for %reason%. Unban time: %time%");
        	punishments.setString("Punishments.PlayerInfo.Messages.isNotBanned", "&cNot Banned");
        	punishments.setString("Punishments.PlayerInfo.Messages.isIPBanned", "&cIPBanned by %banner% for %reason%.");
        	punishments.setString("Punishments.PlayerInfo.Messages.isNotIPBanned", "&cNot IPBanned");
        	punishments.setString("Punishments.PlayerInfo.Messages.isMuted", "&cMuted by %muter% for %reason%. Unmute time: %time%");
        	punishments.setString("Punishments.PlayerInfo.Messages.isNotMuted", "&cNot Muted");
        } if(!check.contains("Unmute")){
        	punishments.setStringList("Punishments.Unmute.Messages.WrongArgs", Arrays.asList(new String[]{"&4&lBans &8» &ePlease use &b/unmute (player)&e!"}));
        	punishments.setStringList("Punishments.Unmute.Messages.Unmuted", Arrays.asList(new String[]{"&4&lBans &8» &b%player% &ehas been unmuted!"}));
        	punishments.setStringList("Punishments.Unmute.Messages.NotMuted", Arrays.asList(new String[]{"&4&lBans &8» &cThat player is not muted!"}));
        } if(!check.contains("Warn")){
        	punishments.setStringList("Punishments.Warn.Messages.WrongArgs", Arrays.asList(new String[]{"&4&lBans &8» &ePlease use &b/warn (player) (reason) &e!"}));
        	punishments.setStringList("Punishments.Warn.Messages.WarnMessage", Arrays.asList(new String[]{
					"&4&lBans &8» &b%player% &ehas warned you!",
					"&4&lBans &8» &eReason: &b%reason%"}));
        	punishments.setStringList("Punishments.Warn.Messages.OfflinePlayer", Arrays.asList(new String[]{"&4&lBans &8» &eThat player is not online!"}));
        	punishments.setStringList("Punishments.Mute.Messages.Warned", Arrays.asList(new String[]{"&4&lBans &8» &b%player% &ehas been Warned! &bReason: &e%reason%"}));
        } if(!check.contains("Tempmute")){
        	punishments.setStringList("Punishments.Tempmute.Messages.WrongArgs", Arrays.asList(new String[]{"&4&lBans &8» &ePlease use &b/tempmute (player) (time) (reason) &e!"}));
        	punishments.setStringList("Punishments.Tempmute.Messages.AlreadyMuted", Arrays.asList(new String[]{"&4&lBans &8» &cThat player is already muted!"}));
        	punishments.setStringList("Punishments.Tempmute.Messages.WrongTime", Arrays.asList(new String[]{"&4&lBans &8» &ePlease use a good TimeUnit!"}));
        	punishments.setStringList("Punishments.Tempmute.Messages.MuteMessage", Arrays.asList(new String[]{
					"&4&lBans &8» &b%player% &ehas tempmuted you!",
					"&4&lBans &8» &eReason: &b%reason%",
					"&4&lBans &8» &eUnmute Time: &b%time%"}));
        	punishments.setStringList("Punishments.Tempmute.Messages.Muted", Arrays.asList(new String[]{"&4&lBans &8» &b%player% &ehas been tempmuted! &bReason: &e%reason%"}));
        }
        punishments.save();
    }
}
