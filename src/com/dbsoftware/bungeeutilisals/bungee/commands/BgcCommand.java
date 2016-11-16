package com.dbsoftware.bungeeutilisals.bungee.commands;

import java.lang.management.ManagementFactory;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.dbsoftware.bungeeutilisals.api.DBCommand;
import com.dbsoftware.bungeeutilisals.bungee.BungeeUser;
import com.dbsoftware.bungeeutilisals.bungee.utils.TPSRunnable;
import com.dbsoftware.bungeeutilisals.bungee.utils.Utils;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;

public class BgcCommand extends DBCommand {
	
	Integer L = 1024;
	
	public BgcCommand() {
		super("glag", "bgc", "gtps", "blag");
	}
	
	@Override
	public void onExecute(BungeeUser user, String[] args) {
		this.onExecute(user.sender(), args);
	}

	@Override
	public void onExecute(CommandSender sender, String[] args) {
		Long uptime = ManagementFactory.getRuntimeMXBean().getStartTime();
		SimpleDateFormat df2 = new SimpleDateFormat("kk:mm dd-MM-yyyy");
		String date = df2.format(new Date(uptime));
		
		Double TPS = TPSRunnable.getTPS();
		ChatColor color;
        if (TPS >= 18.0) {
            color = ChatColor.GREEN;
        } else if(TPS >= 14.0) {
            color = ChatColor.YELLOW;
        } else if(TPS >= 8.0){
            color = ChatColor.RED;
        } else {
        	color = ChatColor.DARK_RED;
        }
		sender.sendMessage(Utils.format("&e&lBungeeUtilisals &8� &6BungeeCord statistics:"));
		sender.sendMessage(Utils.format("&6TPS: &b" + color + TPS));
		sender.sendMessage(Utils.format("&6Max: &b" + (Runtime.getRuntime().maxMemory() / L / L) + " MB"));
		sender.sendMessage(Utils.format("&6Free: &b" + (Runtime.getRuntime().freeMemory() / L / L) + " MB"));
		sender.sendMessage(Utils.format("&6Total: &b" + (Runtime.getRuntime().totalMemory() / L / L) + " MB"));
		sender.sendMessage(Utils.format("&6Online since: &b" + date));
	}
}