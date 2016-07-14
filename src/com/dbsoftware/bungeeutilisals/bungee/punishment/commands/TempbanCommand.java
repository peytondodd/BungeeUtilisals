package com.dbsoftware.bungeeutilisals.bungee.punishment.commands;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

import com.dbsoftware.bungeeutilisals.bungee.BungeeUtilisals;
import com.dbsoftware.bungeeutilisals.bungee.punishment.BanAPI;
import com.dbsoftware.bungeeutilisals.bungee.punishment.Punishments;
import com.dbsoftware.bungeeutilisals.bungee.punishment.TimeUnit;
import com.dbsoftware.bungeeutilisals.bungee.utils.PlayerInfo;
import com.dbsoftware.bungeeutilisals.bungee.utils.PluginMessageChannel;
import com.dbsoftware.bungeeutilisals.bungee.utils.UUIDFetcher;
import com.dbsoftware.bungeeutilisals.bungee.utils.Utils;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

public class TempbanCommand extends Command {

	public TempbanCommand() {
		super("tempban");
	}

	public static void executeTempBanCommand(CommandSender sender, String[] args) {
		String banreason = "";
		if(args.length < 3){
			for(String s : Punishments.punishments.getFile().getStringList("Punishments.Tempban.Messages.WrongArgs")){
				sender.sendMessage(TextComponent.fromLegacyText(s.replace("&", "§")));
			}
			return;
		}
		UUID uuid = UUIDFetcher.getUUIDOf(args[0]);
		if(uuid == null){
			return;
		}
		if(BungeeUtilisals.getInstance().getConfigData().UUIDSTORAGE){
			if(BanAPI.isBanned(uuid.toString())){
				for(String s : Punishments.punishments.getFile().getStringList("Punishments.Tempban.Messages.AlreadyBanned")){
					sender.sendMessage(Utils.format(s));
				}
				return;
			}
		} else {
			if(BanAPI.isBanned(args[0])){
				for(String s : Punishments.punishments.getFile().getStringList("Punishments.Tempban.Messages.AlreadyBanned")){
					sender.sendMessage(Utils.format(s));
				}
				return;
			}
		}

		ProxiedPlayer p = ProxyServer.getInstance().getPlayer(args[0]);
		if(p != null){
			String kreason = "";
			String reason = "";
			for(String s : args){
				reason = reason + s + " ";
			}
			reason = reason.replace(args[0] + " ", "").replace(args[1] + " ", "");
			banreason = reason;
			for(String s : Punishments.punishments.getFile().getStringList("Punishments.Tempban.Messages.KickMessage")){
				kreason = kreason + "\n" + s;
			}
				
			Long time = -1L;
			try {
				time = TimeUnit.parseDateDiff(args[1], true);
			} catch(NumberFormatException e){
				for(String s : Punishments.punishments.getFile().getStringList("Punishments.Tempban.Messages.WrongTime")){
					sender.sendMessage(Utils.format(s));
				}
				return;
			} catch(Exception e){
				for(String s : Punishments.punishments.getFile().getStringList("Punishments.Tempban.Messages.WrongTime")){
					sender.sendMessage(Utils.format(s));
				}
				return;
			}
			if(time == -1L){
				for(String s : Punishments.punishments.getFile().getStringList("Punishments.Tempban.Messages.WrongTime")){
					sender.sendMessage(Utils.format(s));
				}
				return;
			}
			
			if(BungeeUtilisals.getInstance().getConfigData().UUIDSTORAGE){
				BanAPI.addBan(sender.getName(), uuid.toString(), time, reason);
			} else {
				BanAPI.addBan(sender.getName(), p.getName(), time, reason);
			}

			Long end = time;
			
			SimpleDateFormat df2 = new SimpleDateFormat("kk:mm dd/MM/yyyy");
			String date = df2.format(new Date(end));
              
			p.disconnect(Utils.format(kreason.replace("%banner%", sender.getName()).replace("%unbantime%", (end == -1L ? "Never" : date)).replace("%reason%", reason)));
		} else {
			String kreason = "";
			String reason = "";
			for(String s : args){
				reason = reason + s + " ";
			}
			reason = reason.replace(args[0] + " ", "").replace(args[1] + " ", "");
			banreason = reason;
			for(String s : Punishments.punishments.getFile().getStringList("Punishments.Tempban.Messages.KickMessage")){
				kreason = kreason + "\n" + s;
			}

			Long time = -1L;
			try {
				time = TimeUnit.parseDateDiff(args[1], true);
			} catch(NumberFormatException e){
				for(String s : Punishments.punishments.getFile().getStringList("Punishments.Tempban.Messages.WrongTime")){
					sender.sendMessage(Utils.format(s));
				}
				return;
			} catch(Exception e){
				for(String s : Punishments.punishments.getFile().getStringList("Punishments.Tempban.Messages.WrongTime")){
					sender.sendMessage(Utils.format(s));
				}
				return;
			}
			if(time == -1L){
				for(String s : Punishments.punishments.getFile().getStringList("Punishments.Tempban.Messages.WrongTime")){
					sender.sendMessage(Utils.format(s));
				}
				return;
			}
			if(BungeeUtilisals.getInstance().getConfigData().UUIDSTORAGE){
				BanAPI.addBan(sender.getName(), uuid.toString(), time, reason);
			} else {
				BanAPI.addBan(sender.getName(), args[0], time, reason);
			}
		}
		PlayerInfo info;
		if(BungeeUtilisals.getInstance().getConfigData().UUIDSTORAGE){
			info = new PlayerInfo(uuid.toString());
		} else {
			info = new PlayerInfo(args[0]);
		}
		info.addBan();
		for(String s : Punishments.punishments.getFile().getStringList("Punishments.Tempban.Messages.Banned")){
			sender.sendMessage(Utils.format(s.replace("%player%", args[0]).replace("%reason%", banreason)));
		}
	}
	
	@Override
	public void execute(CommandSender sender, String[] args) {
		if(!(sender instanceof ProxiedPlayer)){
			executeTempBanCommand(sender, args);
			return;
		}
		if(BungeeUtilisals.getInstance().getConfig().getBoolean("Bukkit-Permissions")){
			PluginMessageChannel.sendPermissionCheckPluginMessage("hasPermission", "butilisals.tempban", "tempban", args, ((ProxiedPlayer)sender));
			return;
		}
		if(sender.hasPermission("butilisals.tempban") || sender.hasPermission("butilisals.*")){
			executeTempBanCommand(sender, args);
		} else {
			for(String s : Punishments.punishments.getFile().getStringList("Punishments.Messages.NoPermission")){
				sender.sendMessage(Utils.format(s));
			}
		}
	}
}
