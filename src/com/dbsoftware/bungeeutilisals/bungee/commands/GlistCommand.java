package com.dbsoftware.bungeeutilisals.bungee.commands;

import java.util.ArrayList;
import java.util.Collections;
import com.dbsoftware.bungeeutilisals.bungee.BungeeUtilisals;
import com.dbsoftware.bungeeutilisals.bungee.utils.PluginMessageChannel;
import com.dbsoftware.bungeeutilisals.bungee.utils.Utils;
import net.md_5.bungee.Util;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.TextComponent;
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
			ProxiedPlayer localProxiedPlayer1 = (ProxiedPlayer)sender;
			for (ServerInfo localServerInfo : ProxyServer.getInstance().getServers().values()) {
				if (localServerInfo.canAccess(sender)){
					ArrayList<String> localArrayList = new ArrayList<String>();
					for (ProxiedPlayer localProxiedPlayer2 : localServerInfo.getPlayers()) {
						localArrayList.add(localProxiedPlayer2.getDisplayName());
					}
					Collections.sort(localArrayList, String.CASE_INSENSITIVE_ORDER);
					localProxiedPlayer1.sendMessage(new ComponentBuilder(instance.getConfig().getString("GList.Format")
							.replace("%server%", localServerInfo.getName())
							.replace("%players%", localServerInfo.getPlayers().size() + "")
							.replace("%playerlist%", Util.format(localArrayList, instance.getConfig().getString("GList.PlayerListColor") + ", " + instance.getConfig().getString("GList.PlayerListColor")))
							.replaceAll("&", "§")).create());
				}
			}
			sender.sendMessage(new ComponentBuilder(instance.getConfig().getString("GList.Total").replace("%totalnum%", ProxyServer.getInstance().getPlayers().size() + "").replaceAll("&", "§")).create());
		} else {
			Configuration cs = instance.getConfig().getSection("GList.Servers");
			for(String s : cs.getKeys()){
				int serverPlayers = 0;
				ArrayList<String> localArrayList = new ArrayList<String>();
				if(cs.getString(s).contains(",")){
					for(String calculate : cs.getString(s).split(",")){
						
						ServerInfo server = ProxyServer.getInstance().getServerInfo(calculate);
						if(server != null){
							serverPlayers = serverPlayers + server.getPlayers().size();
							for(ProxiedPlayer pl : server.getPlayers()){
								localArrayList.add(pl.getDisplayName());
							}
						}
					}
				} else {
					ServerInfo server = ProxyServer.getInstance().getServerInfo(cs.getString(s));
					if(server != null){
						serverPlayers = serverPlayers + server.getPlayers().size();
						for(ProxiedPlayer pl : server.getPlayers()){
							localArrayList.add(pl.getDisplayName());
						}
					}
				}
				Collections.sort(localArrayList, String.CASE_INSENSITIVE_ORDER);
				sender.sendMessage(new TextComponent(instance.getConfig().getString("GList.Format")
						.replace("%server%", s)
						.replace("%playerlist%", Util.format(localArrayList, instance.getConfig().getString("GList.PlayerListColor") + ", " + instance.getConfig().getString("GList.PlayerListColor")))
						.replace("%players%", serverPlayers + "")
						.replaceAll("&", "§")));
			}
			sender.sendMessage(new ComponentBuilder(instance.getConfig().getString("GList.Total").replace("%totalnum%", ProxyServer.getInstance().getPlayers().size() + "").replaceAll("&", "§")).create());
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
