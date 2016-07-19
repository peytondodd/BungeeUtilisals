package com.dbsoftware.bungeeutilisals.bungee.commands;

import java.lang.management.ManagementFactory;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.dbsoftware.bungeeutilisals.bungee.BungeeUtilisals;
import com.dbsoftware.bungeeutilisals.bungee.utils.PluginMessageChannel;
import com.dbsoftware.bungeeutilisals.bungee.utils.TPSRunnable;
import com.dbsoftware.bungeeutilisals.bungee.utils.Utils;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

public class BgcCommand extends Command {
	
	public BgcCommand() {
		super("bgc", "", new String[]{"glag", "gtps", "blag"});
	}

	public static void executeBgcCommand(CommandSender sender, String[] args){
		Runtime run = Runtime.getRuntime();
		long maxMemory = run.maxMemory() / 1024 / 1024;
		long totalMemory = run.totalMemory() / 1024 / 1024;
		long freeMemory = run.freeMemory() / 1024 / 1024;
		
		long uptime = ManagementFactory.getRuntimeMXBean().getStartTime();
		
		SimpleDateFormat df2 = new SimpleDateFormat("kk:mm dd/MM/yyyy");
		String date = df2.format(new Date(uptime));
		
		double tps = TPSRunnable.getTPS();
		
		sender.sendMessage(Utils.format("&e&lBungeeUtilisals &8» &6BungeeCord statistics:"));
		sender.sendMessage(Utils.format("&6TPS: &b" + getColor(tps) + tps));
		sender.sendMessage(Utils.format("&6Max: &b" + maxMemory + " MB"));
		sender.sendMessage(Utils.format("&6Free: &b" + freeMemory + " MB"));
		sender.sendMessage(Utils.format("&6Total: &b" + totalMemory + " MB"));
		sender.sendMessage(Utils.format("&6Online since: &b" + date));
	}
	
	@Override
	public void execute(CommandSender sender, String[] args) {
		if(!(sender instanceof ProxiedPlayer)){
			executeBgcCommand(sender, args);
			return;
		}
		if(BungeeUtilisals.getInstance().getConfig().getBoolean("Bukkit-Permissions")){
			PluginMessageChannel.sendPermissionCheckPluginMessage("hasPermission", "butilisals.glag", "glag", args, ((ProxiedPlayer)sender));
			return;
		}
		if(sender.hasPermission("butilisals.glag") || sender.hasPermission("butilisals.*")){
			executeBgcCommand(sender, args);
		} else {
			sender.sendMessage(Utils.format(BungeeUtilisals.getInstance().getConfig().getString("Prefix") + BungeeUtilisals.getInstance().getConfig().getString("Main-messages.no-permission")));
		}
	}
	
	static ChatColor getColor(double d){
		ChatColor color = ChatColor.GREEN;
		if (d < 15.0D) {
			color = ChatColor.RED;
		}
	    if (d >= 15.0D) {
	    	color = ChatColor.YELLOW;
	    }
	    if (d >= 18.0D) {
	    	color = ChatColor.GREEN;
	    }
	    return color;
	}
}
