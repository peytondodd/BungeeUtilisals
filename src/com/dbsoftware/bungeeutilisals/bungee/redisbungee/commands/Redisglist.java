package com.dbsoftware.bungeeutilisals.bungee.redisbungee.commands;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Set;
import java.util.UUID;

import com.dbsoftware.bungeeutilisals.bungee.BungeeUtilisals;
import com.dbsoftware.bungeeutilisals.bungee.utils.PluginMessageChannel;
import com.dbsoftware.bungeeutilisals.bungee.utils.Utils;

import net.craftminecraft.bungee.bungeeyaml.bukkitapi.ConfigurationSection;
import net.md_5.bungee.Util;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

public class Redisglist extends Command {
	
	public Redisglist() {
		super("glist", "", BungeeUtilisals.getInstance().getConfig().getString("GList.Aliase"));{
		}
	}
	
	public static void executeGlistCommand(CommandSender sender, String[] args){
		glist(sender);
	}
	
	private static void glist(CommandSender sender){
		if(sender instanceof ProxiedPlayer){
			if(!BungeeUtilisals.getInstance().getConfig().getBoolean("GList.Custom_GList")){
				ProxiedPlayer p = (ProxiedPlayer)sender;
				for (ServerInfo server : ProxyServer.getInstance().getServers().values()){
					ArrayList<String> pllist = new ArrayList<String>();
					for (UUID uuid : BungeeUtilisals.getInstance().getRedisManager().getRedis().getPlayersOnServer(server.getName())) {
						pllist.add(BungeeUtilisals.getInstance().getRedisManager().getRedis().getNameFromUuid(uuid));
					}
					Collections.sort(pllist, String.CASE_INSENSITIVE_ORDER);
					p.sendMessage(Utils.format(BungeeUtilisals.getInstance().getConfig().getString("GList.Format")
							.replace("%server%", server.getName())
							.replace("%players%", pllist.size() + "")
							.replace("%playerlist%", Util.format(pllist, BungeeUtilisals.getInstance().getConfig().getString("GList.PlayerListColor")
									+ ", " + BungeeUtilisals.getInstance().getConfig().getString("GList.PlayerListColor")))
							.replaceAll("&", "§")));
				}
				sender.sendMessage(Utils.format(BungeeUtilisals.getInstance().getConfig().getString("GList.Total")
						.replace("%totalnum%", BungeeUtilisals.getInstance().getRedisManager().getRedis().getPlayerCount() + "").replaceAll("&", "§")));
			} else {
				ConfigurationSection cs = BungeeUtilisals.getInstance().getConfig().getConfigurationSection("GList.Servers");
				for(String s : cs.getKeys(false)){
					int serverPlayers = 0;
					ArrayList<String> pllist = new ArrayList<String>();
					if(cs.getString(s).contains(",")){
						for(String calculate : cs.getString(s).split(",")){
							ServerInfo server = ProxyServer.getInstance().getServerInfo(calculate);
							if(server != null){
								Set<UUID> plOnServer = BungeeUtilisals.getInstance().getRedisManager().getRedis().getPlayersOnServer(server.getName());
								serverPlayers = serverPlayers + plOnServer.size();
								for (UUID uuid : plOnServer) {
									pllist.add(BungeeUtilisals.getInstance().getRedisManager().getRedis().getNameFromUuid(uuid));
								}
							}
						}
					} else {
						ServerInfo server = ProxyServer.getInstance().getServerInfo(cs.getString(s));
						Set<UUID> plOnServer = BungeeUtilisals.getInstance().getRedisManager().getRedis().getPlayersOnServer(server.getName());
						if(server != null){
							serverPlayers = serverPlayers + plOnServer.size();
							for (UUID uuid : plOnServer) {
								pllist.add(BungeeUtilisals.getInstance().getRedisManager().getRedis().getNameFromUuid(uuid));
							}
						}
					}
					Collections.sort(pllist, String.CASE_INSENSITIVE_ORDER);
					sender.sendMessage(new TextComponent(BungeeUtilisals.getInstance().getConfig().getString("GList.Format")
							.replace("%server%", s)
							.replace("%playerlist%", Util.format(pllist, BungeeUtilisals.getInstance().getConfig().getString("GList.PlayerListColor")
									+ ", " + BungeeUtilisals.getInstance().getConfig().getString("GList.PlayerListColor")))
							.replace("%players%", serverPlayers + "")
							.replaceAll("&", "§")));
				}
				sender.sendMessage(Utils.format(BungeeUtilisals.getInstance().getConfig().getString("GList.Total")
						.replace("%totalnum%", BungeeUtilisals.getInstance().getRedisManager().getRedis().getPlayerCount() + "").replaceAll("&", "§")));
			}
		} else {
			sender.sendMessage(new TextComponent("§cThat command can only be used ingame!"));
		}
	}
	
	@Override
	public void execute(CommandSender sender, String[] args) {
		if(!(sender instanceof ProxiedPlayer)){
			executeGlistCommand(sender, args);
			return;
		}
		if(BungeeUtilisals.getInstance().getConfig().getBoolean("Bukkit-Permissions")){
			PluginMessageChannel.sendPermissionCheckPluginMessage("hasPermission", "butilisals.glist", "glist", args, ((ProxiedPlayer)sender));
			return;
		}
		if(sender.hasPermission("butilisals.glist") || sender.hasPermission("butilisals.*")){
			executeGlistCommand(sender, args);		
		} else {
			sender.sendMessage(Utils.format(BungeeUtilisals.getInstance().getConfig().getString("Prefix") + BungeeUtilisals.getInstance().getConfig().getString("Main-messages.no-permission")));
		}
	}
}
