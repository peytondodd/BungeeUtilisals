package com.dbsoftware.bungeeutilisals.bungee.punishment.commands;

import java.text.SimpleDateFormat;
import java.util.Date;
import com.dbsoftware.bungeeutilisals.bungee.BungeeUtilisals;
import com.dbsoftware.bungeeutilisals.bungee.punishment.BanAPI;
import com.dbsoftware.bungeeutilisals.bungee.punishment.Punishments;
import com.dbsoftware.bungeeutilisals.bungee.utils.PlayerInfo;
import com.dbsoftware.bungeeutilisals.bungee.utils.PluginMessageChannel;
import com.dbsoftware.bungeeutilisals.bungee.utils.Utils;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

public class BanCommand extends Command {

	public BanCommand() {
		super("ban");
	}

	public static void executeBanCommand(CommandSender sender, String[] args) {
		String banreason = "";
		if(args.length < 2){
			for(String s : Punishments.punishments.getFile().getStringList("Punishments.Ban.Messages.WrongArgs")){
				sender.sendMessage(TextComponent.fromLegacyText(s.replace("&", "§")));
			}
			return;
		}
		if(BanAPI.isBanned(args[0])){
			for(String s : Punishments.punishments.getFile().getStringList("Punishments.Ban.Messages.AlreadyBanned")){
				sender.sendMessage(Utils.format(s));
			}
			return;
		}
		ProxiedPlayer p = ProxyServer.getInstance().getPlayer(args[0]);
		if(p != null){
			String kreason = "";
			String reason = "";
			for(String s : args){
				reason = reason + s + " ";
			}
			reason = reason.replaceFirst(p.getName() + " ", "");
			banreason = reason;
			for(String s : Punishments.punishments.getFile().getStringList("Punishments.Ban.Messages.KickMessage")){
				kreason = kreason + "\n" + s;
			}
			BanAPI.addBan(sender.getName(), p.getName(), -1L, reason);

			Long end = BanAPI.getBanTime(p.getName());
			
			SimpleDateFormat df2 = new SimpleDateFormat("kk:mm dd/MM/yyyy");
			String date = df2.format(new Date(end));
              
			p.disconnect(Utils.format(kreason.replace("%banner%", BanAPI.getBannedBy(p.getName())).replace("%unbantime%", (end == -1L ? "Never" : date)).replace("%reason%", BanAPI.getReason(p.getName()))));
		} else {
			String kreason = "";
			String reason = "";
			for(String s : args){
				reason = reason + s + " ";
			}
			reason = reason.replaceFirst(args[0] + " ", "");
			banreason = reason;
			for(String s : Punishments.punishments.getFile().getStringList("Punishments.Ban.Messages.KickMessage")){
				kreason = kreason + "\n" + s;
			}
			BanAPI.addBan(sender.getName(), args[0], -1L, reason);
		}
		PlayerInfo pinfo = new PlayerInfo(args[0]);
		pinfo.addBan();
		for(String s : Punishments.punishments.getFile().getStringList("Punishments.Ban.Messages.Banned")){
			sender.sendMessage(Utils.format(s.replace("%player%", args[0]).replace("%reason%", banreason)));
		}
	}
	
	@Override
	public void execute(CommandSender sender, String[] args) {
		if(!(sender instanceof ProxiedPlayer)){
			executeBanCommand(sender, args);
			return;
		}
		if(BungeeUtilisals.getInstance().getConfig().getBoolean("Bukkit-Permissions")){
			PluginMessageChannel.sendPermissionCheckPluginMessage("hasPermission", "butilisals.ban", "ban", args, ((ProxiedPlayer)sender));
			return;
		}
		if(sender.hasPermission("butilisals.ban") || sender.hasPermission("butilisals.*")){
			executeBanCommand(sender, args);		
		} else {
			for(String s : Punishments.punishments.getFile().getStringList("Punishments.Messages.NoPermission")){
				sender.sendMessage(Utils.format(s));
			}
		}
	}
}
