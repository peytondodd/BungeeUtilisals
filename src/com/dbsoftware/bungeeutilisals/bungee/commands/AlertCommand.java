package com.dbsoftware.bungeeutilisals.bungee.commands;

import com.dbsoftware.bungeeutilisals.bungee.BungeeUtilisals;
import com.dbsoftware.bungeeutilisals.bungee.utils.PluginMessageChannel;
import com.dbsoftware.bungeeutilisals.bungee.utils.Utils;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

public class AlertCommand extends Command {
	
	public AlertCommand() {
		super("alert");
	}

	public static void executeAlertCommand(CommandSender sender, String[] args){
		String prefix = ChatColor.translateAlternateColorCodes('&', BungeeUtilisals.getInstance().getConfig().getString("Prefix"));
		if (args.length >= 1) {
			String msg = "";
			for (String messages : args) {
				msg = msg + messages + " ";
			}
			ProxyServer.getInstance().broadcast(Utils.format(prefix + msg));
		} else {
			sender.sendMessage(Utils.format("§cYou need to enter at least 1 word!"));
		}
	}

	@Override
	public void execute(CommandSender sender, String[] args) {
		if(!(sender instanceof ProxiedPlayer)){
			executeAlertCommand(sender, args);
			return;
		}
		if(BungeeUtilisals.getInstance().getConfig().getBoolean("Bukkit-Permissions")){
			PluginMessageChannel.sendPermissionCheckPluginMessage("hasPermission", "butilisals.alert", "alert", args, ((ProxiedPlayer)sender));
			return;
		}
		if(sender.hasPermission("butilisals.alert") || sender.hasPermission("butilisals.*")){
			executeAlertCommand(sender, args);		
		} else {
			sender.sendMessage(Utils.format(BungeeUtilisals.getInstance().getConfig().getString("Prefix") + BungeeUtilisals.getInstance().getConfig().getString("Main-messages.no-permission")));
		}
	}
}
