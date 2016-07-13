package com.dbsoftware.bungeeutilisals.bungee.commands;

import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;
import com.dbsoftware.bungeeutilisals.bungee.BungeeUtilisals;
import com.dbsoftware.bungeeutilisals.bungee.utils.PluginMessageChannel;
import com.dbsoftware.bungeeutilisals.bungee.utils.Utils;

public class Server
	extends Command {
	
	public Server() {
		super("server");{
		}
	}
	
	private static BungeeUtilisals instance = BungeeUtilisals.getInstance();
	
	public static void executeServerCommand(CommandSender sender, String[] args){
		server(sender, args);
	}
	
	private static void server(CommandSender sender, String[] args){
		if(sender instanceof ProxiedPlayer){
			if(args.length == 0){
				String s = instance.getConfig().getString("Main-messages.server-message");
				ProxiedPlayer p = (ProxiedPlayer)sender;
				sender.sendMessage(new TextComponent(s.replace("%player%", sender.getName()).replace("%servername%", p.getServer().getInfo().getName()).replaceAll("&", "§")));
			} if(args.length == 1){
				ServerInfo server = ProxyServer.getInstance().getServerInfo(args[0]);
				ProxiedPlayer p = (ProxiedPlayer)sender;
				if(server != null) {
					if(p.getServer().getInfo().getName() == server.getName()){
						p.sendMessage(new TextComponent(instance.getConfig().getString("Main-messages.already-connected").replace("&", "§")));
						return;
					}
					p.connect(server);
					p.sendMessage(new TextComponent(instance.getConfig().getString("Main-messages.sended-message").replace("&", "§").replace("%player%", sender.getName()).replace("%server%", server.getName())));
				} else {
					p.sendMessage(new TextComponent(instance.getConfig().getString("Main-messages.no-server").replace("&", "§").replace("%player%", sender.getName()).replace("%server%", args[0])));  
				}
			} else if(args.length != 1 && args.length != 0){
				sender.sendMessage(new TextComponent(instance.getConfig().getString("Main-messages.use-server").replace("&", "§")));
			}
		} else {
			sender.sendMessage(new TextComponent("§cThat command can only be used ingame!"));
		}
	}
	
	@Override
	public void execute(CommandSender sender, String[] args){
		if(!(sender instanceof ProxiedPlayer)){
			sender.sendMessage(Utils.format("The console cannot use this command!"));
			return;
		}
		if(BungeeUtilisals.getInstance().getConfig().getBoolean("Bukkit-Permissions")){
			PluginMessageChannel.sendPermissionCheckPluginMessage("hasPermission", "butilisals.server", "server", args, ((ProxiedPlayer)sender));
			return;
		}
		if(sender.hasPermission("butilisals.server") || sender.hasPermission("butilisals.*")){
			executeServerCommand(sender, args);
		} else {
			sender.sendMessage(Utils.format(BungeeUtilisals.getInstance().getConfig().getString("Prefix") + BungeeUtilisals.getInstance().getConfig().getString("Main-messages.no-permission")));
		}
	}
}
