package com.dbsoftware.bungeeutilisals.bungee.commands;

import java.util.ArrayList;
import java.util.Collections;

import com.dbsoftware.bungeeutilisals.bungee.BungeeUtilisals;
import com.dbsoftware.bungeeutilisals.bungee.utils.PluginMessageChannel;
import com.dbsoftware.bungeeutilisals.bungee.utils.Utils;
import com.google.common.collect.Lists;

import net.md_5.bungee.Util;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;
import net.md_5.bungee.config.Configuration;

public class GlistCommand extends Command {
	
	public GlistCommand() {
		super("glist", "", instance.getConfig().getString("GList.Aliase"));{
		}
	}

	private static BungeeUtilisals instance = BungeeUtilisals.getInstance();
	
	public static void executeGlistCommand(CommandSender sender, String[] args){
		glist(sender);
	}
	
	private static void glist(CommandSender sender){
		if(!instance.getConfig().getBoolean("GList.Custom_GList")){
			ProxiedPlayer p = (ProxiedPlayer)sender;
			for (ServerInfo server : ProxyServer.getInstance().getServers().values()) {
				if (server.canAccess(sender)){
					ArrayList<String> list = new ArrayList<String>();
					for (ProxiedPlayer pl : server.getPlayers()) {
						list.add(pl.getDisplayName());
					}
					Collections.sort(list, String.CASE_INSENSITIVE_ORDER);
					p.sendMessage(Utils.format(instance.getConfig().getString("GList.Format")
							.replace("%server%", server.getName())
							.replace("%players%", server.getPlayers().size() + "")
							.replace("%playerlist%", Util.format(list, instance.getConfig().getString("GList.PlayerListColor") + ", " + instance.getConfig().getString("GList.PlayerListColor")))));
				}
			}
			sender.sendMessage(Utils.format(instance.getConfig().getString("GList.Total").replace("%totalnum%", String.valueOf(ProxyServer.getInstance().getPlayers().size()))));
		} else {
			Configuration cs = instance.getConfig().getSection("GList.Servers");
			for(String s : cs.getKeys()){
				int serverPlayers = 0;
				ArrayList<String> list = Lists.newArrayList();
				if(cs.getString(s).contains(",")){
					for(String calculate : cs.getString(s).split(",")){
						
						ServerInfo server = ProxyServer.getInstance().getServerInfo(calculate);
						if(server != null){
							serverPlayers = serverPlayers + server.getPlayers().size();
							for(ProxiedPlayer pl : server.getPlayers()){
								list.add(pl.getDisplayName());
							}
						}
					}
				} else {
					ServerInfo server = ProxyServer.getInstance().getServerInfo(cs.getString(s));
					if(server != null){
						serverPlayers = serverPlayers + server.getPlayers().size();
						for(ProxiedPlayer pl : server.getPlayers()){
							list.add(pl.getDisplayName());
						}
					}
				}
				Collections.sort(list, String.CASE_INSENSITIVE_ORDER);
				sender.sendMessage(Utils.format(instance.getConfig().getString("GList.Format")
						.replace("%server%", s)
						.replace("%playerlist%", Util.format(list, instance.getConfig().getString("GList.PlayerListColor") + ", " + instance.getConfig().getString("GList.PlayerListColor")))
						.replace("%players%", serverPlayers + "")));
			}
			sender.sendMessage(Utils.format(instance.getConfig().getString("GList.Total").replace("%totalnum%", String.valueOf(ProxyServer.getInstance().getPlayers().size()))));
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
