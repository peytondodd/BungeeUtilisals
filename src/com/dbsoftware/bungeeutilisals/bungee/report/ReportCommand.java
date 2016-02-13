package com.dbsoftware.bungeeutilisals.bungee.report;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import com.dbsoftware.bungeeutilisals.bungee.BungeeUtilisals;
import com.dbsoftware.bungeeutilisals.bungee.managers.FileManager;
import com.dbsoftware.bungeeutilisals.bungee.utils.PluginMessageChannel;
import com.dbsoftware.bungeeutilisals.bungee.utils.Utils;

import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

public class ReportCommand extends Command {

	public ReportCommand() {
		super("report");{
		}
	}
	
	public static FileManager getReports(){
		return Reports.reports;
	}
	
	private static Map<String, Long> lastUsage = new HashMap<String, Long>();
	public static ArrayList<ProxiedPlayer> players = new ArrayList<ProxiedPlayer>();
	
	public static void executeReportToggleCommand(CommandSender sender, String[] args){
		ProxiedPlayer p = (ProxiedPlayer)sender;
		if(!players.contains(p)){
			players.add(p);
			p.sendMessage(TextComponent.fromLegacyText(getReports().getString("Reports.Messages.EnabledAlert", "&cNew reports will now be alerted!").replace("&", "§")));
		} else {
			players.remove(p);
			p.sendMessage(TextComponent.fromLegacyText(getReports().getString("Reports.Messages.DisabledAlert", "&cNew reports will not be alerted anymore!").replace("&", "§")));
		}
	}
	
	public static void executeReportDeleteCommand(CommandSender sender, String[] args){
		ProxiedPlayer p = (ProxiedPlayer)sender;
		int number = Integer.valueOf(args[1]);
		if(!ReportAPI.getReportNumbers().contains(number)){
			p.sendMessage(TextComponent.fromLegacyText(getReports().getString("Reports.Messages.NoReportNumberExist", "&cReport number %number% doesn't exist!").replace("&", "§").replace("%number%", number + "")));
			return;
		}
		ReportAPI.removeReport(number);
		p.sendMessage(TextComponent.fromLegacyText(getReports().getString("Reports.Messages.Removed", "&cYou have removed report number %number%!").replace("&", "§").replace("%number%", number + "")));
		return;
	}
	
	public static void executeReportCommand(CommandSender sender, String[] args) {
		if(!(sender instanceof ProxiedPlayer)){
			sender.sendMessage(new TextComponent("Only the Console can use this Command!"));
			return;
		}
		ProxiedPlayer p = (ProxiedPlayer)sender;
		if(args.length < 2){
			p.sendMessage(TextComponent.fromLegacyText(getReports().getString("Reports.Messages.WrongArgs", "&cPlease use /report (player) (reason)").replace("&", "§")));
			return;
		} else if(args.length == 1){
			if(args[0].contains("toggle")){
				if(BungeeUtilisals.getInstance().getConfig().getBoolean("Bukkit-Permissions")){
					PluginMessageChannel.sendPermissionCheckPluginMessage("hasPermission", "butilisals.report.toggle", "report", args, ((ProxiedPlayer)sender));
					return;
				}
				
				if(!p.hasPermission("butilisals.report.toggle") && !p.hasPermission("butilisals.*") && !Reports.reports.getStringList("Players", new ArrayList<String>()).contains(p.getName())){
					p.sendMessage(TextComponent.fromLegacyText(getReports().getString("Reports.Messages.NoPermission", "&cYou don't have the permission to use the Report Command!").replace("&", "§")));
					return;
				}
				executeReportToggleCommand(sender, args);
				return;
			}
		}
		if(args[0].contains("remove") || args[0].contains("delete")){
			if(BungeeUtilisals.getInstance().getConfig().getBoolean("Bukkit-Permissions")){
				PluginMessageChannel.sendPermissionCheckPluginMessage("hasPermission", "butilisals.report.remove", "report", args, ((ProxiedPlayer)sender));
				return;
			}
			if(!p.hasPermission("butilisals.report.remove") && !p.hasPermission("butilisals.*")){
				p.sendMessage(TextComponent.fromLegacyText(getReports().getString("Reports.Messages.NoPermission", "&cYou don't have the permission to use the Report Command!").replace("&", "§")));
				return;
			}
			executeReportDeleteCommand(sender, args);
			return;
		}
		long lastUsed = 0L;
		if (lastUsage.containsKey(p.getName())) {
			lastUsed = ((Long)lastUsage.get(p.getName())).longValue();
		}
		int cdmillis = Reports.reports.getInt("Cooldown", 60) * 1000;
		if (System.currentTimeMillis() - lastUsed < cdmillis){
			int timeLeft =  (int) (Reports.reports.getInt("Cooldown", 60) - (System.currentTimeMillis() - lastUsed) / 1000L);
			p.sendMessage(TextComponent.fromLegacyText(getReports().getString("Reports.Messages.CooldownMessage", "&cYou need to wait &6%time% &cseconds untill you can report again!").replace("&", "§").replace("%time%", timeLeft + "")));
			return;
		}
        lastUsage.put(p.getName(), Long.valueOf(System.currentTimeMillis()));
		
		String player = args[0];
		String reason = "";
		for(String s : args){
			reason = reason + s + " ";
		}
		reason = reason.replaceFirst("report", "").replaceFirst(player, "");
		ProxiedPlayer reported = ProxyServer.getInstance().getPlayer(player);
		if(reported == null){
			p.sendMessage(TextComponent.fromLegacyText(getReports().getString("Reports.Messages.NoPlayer", "&cYou can't report an offline player!").replace("&", "§")));
			lastUsage.remove(p.getName());
			return;
		}
		if(ReportAPI.getReportNumbers().isEmpty()){
			ReportAPI.addReport(1, p, reported, reason);
		} else {
			int highest = Collections.max(ReportAPI.getReportNumbers());
			ReportAPI.addReport(highest + 1, p, reported, reason);
		}
		p.sendMessage(TextComponent.fromLegacyText(getReports().getString("Reports.Messages.Reported", "&aYou have reported %player%!").replace("&", "§").replace("%player%", reported.getName())));
		for(ProxiedPlayer pl : players){
			if(pl != null){
				pl.sendMessage(TextComponent.fromLegacyText(getReports().getString("Reports.Messages.ReportBroadcast", "&a%player% &ehas been reported by &a%reporter% &efor &a%reason%&e!").replace("&", "§").replace("%player%", reported.getName()).replace("%reporter%", p.getName()).replace("%reason%", reason)));
			}
		}
	}

	@Override
	public void execute(CommandSender sender, String[] args) {
		if(!(sender instanceof ProxiedPlayer)){
			sender.sendMessage(Utils.format("Only players can work with Bukkit permissions!"));
			return;
		}
		if(BungeeUtilisals.getInstance().getConfig().getBoolean("Bukkit-Permissions")){
			PluginMessageChannel.sendPermissionCheckPluginMessage("hasPermission", "butilisals.report", "report", args, ((ProxiedPlayer)sender));
			return;
		}
		if(sender.hasPermission("butilisals.report") || sender.hasPermission("butilisals.*")){
			executeReportCommand(sender, args);
		} else {
			sender.sendMessage(TextComponent.fromLegacyText(getReports().getString("Reports.Messages.NoPermission", "&cYou don't have the permission to use the Report Command!").replace("&", "§")));
			return;
		}
	}
}