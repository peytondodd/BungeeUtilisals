package com.dbsoftware.bungeeutilisals.bungee.commands;

import com.dbsoftware.bungeeutilisals.bungee.BungeeUser;
import com.dbsoftware.bungeeutilisals.bungee.BungeeUtilisals;
import com.dbsoftware.bungeeutilisals.bungee.utils.PluginMessageChannel;
import com.dbsoftware.bungeeutilisals.bungee.utils.Utils;

import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

public class HubCommand extends Command {
	
	private static BungeeUtilisals instance = (BungeeUtilisals)BungeeUtilisals.getInstance();

	
	public HubCommand(String cmd) {
		super(cmd);{
		}
	}
	
	public HubCommand(String cm, String[] cmd){
		super(cm, "", cmd);
	}
	
		
	public static void executeHubCommand(CommandSender sender, String[] args){
		if (sender instanceof ProxiedPlayer){
			ProxiedPlayer p = (ProxiedPlayer)sender;
			BungeeUser user = BungeeUtilisals.getInstance().getUser(p);
						
			String server = instance.getConfig().getString("Hub.Server");
			String playerserver = p.getServer().getInfo().getName();
			if(!playerserver.equalsIgnoreCase(server)){
				p.connect(ProxyServer.getInstance().getServerInfo(instance.getConfig().getString("Hub.Server")));
				user.sendMessage(instance.getConfig().getString("Hub.Message"));
				return;
			}
			user.sendMessage(instance.getConfig().getString("Hub.inHub"));
			return;
		}
	}
	
	@Override
	public void execute(CommandSender sender, String[] args) {
		if(!(sender instanceof ProxiedPlayer)){
			sender.sendMessage(Utils.format("The console cannot go to the hub!"));
			return;
		}
		if(BungeeUtilisals.getInstance().getConfig().getBoolean("Bukkit-Permissions")){
			PluginMessageChannel.sendPermissionCheckPluginMessage("hasPermission", "butilisals.hub", "hub", args, ((ProxiedPlayer)sender));
			return;
		}
		if(sender.hasPermission("butilisals.hub") || sender.hasPermission("butilisals.*")){
			executeHubCommand(sender, args);		
		} else {
			sender.sendMessage(Utils.format(BungeeUtilisals.getInstance().getConfig().getString("Prefix") + BungeeUtilisals.getInstance().getConfig().getString("Main-messages.no-permission")));
		}
	}
}
