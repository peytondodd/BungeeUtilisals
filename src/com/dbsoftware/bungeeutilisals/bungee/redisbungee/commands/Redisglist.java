package com.dbsoftware.bungeeutilisals.bungee.redisbungee.commands;

import java.util.ArrayList;
import java.util.Map;
import java.util.TreeSet;
import java.util.UUID;
import com.dbsoftware.bungeeutilisals.bungee.BungeeUtilisals;
import com.dbsoftware.bungeeutilisals.bungee.utils.PluginMessageChannel;
import com.dbsoftware.bungeeutilisals.bungee.utils.Utils;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import com.imaginarycode.minecraft.redisbungee.RedisBungee;
import net.md_5.bungee.Util;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;
import net.md_5.bungee.config.Configuration;

public class Redisglist extends Command {
	
	public Redisglist() {
		super("glist", "", BungeeUtilisals.getInstance().getConfig().getString("GList.Aliase"));{
		}
	}
	
	public static void executeGlistCommand(CommandSender sender, String[] args){
		glist(sender);
	}
	
	private static void glist(CommandSender sender){
		if(!BungeeUtilisals.getInstance().getConfig().getBoolean("GList.Custom_GList")){
			ProxiedPlayer p = (ProxiedPlayer)sender;
			Multimap<String, UUID> serverToPlayers = RedisBungee.getApi().getServerToPlayers();
			Multimap<String, String> human = HashMultimap.create();
			for (Map.Entry<String, UUID> entry : serverToPlayers.entries()) {
				human.put(entry.getKey(), BungeeUtilisals.getInstance().getRedisManager().getRedis().getNameFromUuid(entry.getValue(), false));
			}
			for (String serv : new TreeSet<>(serverToPlayers.keySet())) {
				p.sendMessage(Utils.format(BungeeUtilisals.getInstance().getConfig().getString("GList.Format")
						.replace("%server%", serv)
						.replace("%players%", String.valueOf(serverToPlayers.get(serv).size()))
						.replace("%playerlist%", Util.format(human.get(serv), BungeeUtilisals.getInstance().getConfig().getString("GList.PlayerListColor")
								+ ", " + BungeeUtilisals.getInstance().getConfig().getString("GList.PlayerListColor")))
						.replaceAll("&", "§")));
			}
			sender.sendMessage(Utils.format(BungeeUtilisals.getInstance().getConfig().getString("GList.Total")
					.replace("%totalnum%", BungeeUtilisals.getInstance().getRedisManager().getRedis().getPlayerCount() + "").replaceAll("&", "§")));
		} else {
			Configuration cs = BungeeUtilisals.getInstance().getConfig().getSection("GList.Servers");
			Multimap<String, UUID> serverToPlayers = RedisBungee.getApi().getServerToPlayers();
			Multimap<String, String> human = HashMultimap.create();
			for (Map.Entry<String, UUID> entry : serverToPlayers.entries()) {
				human.put(entry.getKey(), BungeeUtilisals.getInstance().getRedisManager().getRedis().getNameFromUuid(entry.getValue(), false));
			}
			for(String s : cs.getKeys()){
				int serverPlayers = 0;
				ArrayList<String> pllist = new ArrayList<String>();
				if(cs.getString(s).contains(",")){
					for(String calculate : cs.getString(s).split(",")){
						ServerInfo server = ProxyServer.getInstance().getServerInfo(calculate);
						if(server != null){
							serverPlayers = serverPlayers + serverToPlayers.get(server.getName()).size();
							for(String ss : human.get(server.getName())){
								pllist.add(ss);
							}
						}
					}
				} else {
					ServerInfo server = ProxyServer.getInstance().getServerInfo(s);
					if(server != null){
						serverPlayers = serverPlayers + serverToPlayers.get(server.getName()).size();
						for(String ss : human.get(server.getName())){
							pllist.add(ss);
						}
					}
				}
				sender.sendMessage(new TextComponent(BungeeUtilisals.getInstance().getConfig().getString("GList.Format")
						.replace("%server%", s)
						.replace("%playerlist%", Util.format(pllist, BungeeUtilisals.getInstance().getConfig().getString("GList.PlayerListColor")
								+ ", " + BungeeUtilisals.getInstance().getConfig().getString("GList.PlayerListColor")))
						.replace("%players%", String.valueOf(serverPlayers))
						.replaceAll("&", "§")));
			}
			sender.sendMessage(Utils.format(BungeeUtilisals.getInstance().getConfig().getString("GList.Total")
					.replace("%totalnum%", BungeeUtilisals.getInstance().getRedisManager().getRedis().getPlayerCount() + "").replaceAll("&", "§")));
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
