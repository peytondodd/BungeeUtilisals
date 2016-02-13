package com.dbsoftware.bungeeutilisals.bungee.punishment.commands;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;

import com.dbsoftware.bungeeutilisals.bungee.BungeeUtilisals;
import com.dbsoftware.bungeeutilisals.bungee.punishment.BanAPI;
import com.dbsoftware.bungeeutilisals.bungee.punishment.Punishments;
import com.dbsoftware.bungeeutilisals.bungee.punishment.TimeUnit;
import com.dbsoftware.bungeeutilisals.bungee.utils.PlayerInfo;
import com.dbsoftware.bungeeutilisals.bungee.utils.PluginMessageChannel;
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
			for(String s : Punishments.punishments.getStringList("Punishments.Tempban.Messages.WrongArgs", Arrays.asList(new String[]{"&4&lBans &8» &ePlease use &b/tempban (player) (time) (reason) &e!"}))){
				sender.sendMessage(TextComponent.fromLegacyText(s.replace("&", "§")));
			}
			return;
		}
		int bans = BanAPI.getBanNumbers().size();
		if(BanAPI.isBanned(args[0])){
			for(String s : Punishments.punishments.getStringList("Punishments.Tempban.Messages.AlreadyBanned", Arrays.asList(new String[]{"&4&lBans &8» &cThat player is already banned!"}))){
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
			reason = reason.replace(args[0] + " ", "").replace(args[1] + " ", "");
			banreason = reason;
			for(String s : Punishments.punishments.getStringList("Punishments.Tempban.Messages.KickMessage", Arrays.asList(new String[]{
					"&cBungeeUtilisals &8» &7Tempbanned",
					" ",
					"&cReason &8» &7%reason%",
					"&cUnban at &8» &7%unbantime%",
				 	"&cBanned by &8» &7%banner%"}))){
				kreason = kreason + "\n" + s;
			}
				
			Long time = -1L;
			try {
				time = TimeUnit.parseDateDiff(args[1], true);
			} catch(NumberFormatException e){
				for(String s : Punishments.punishments.getStringList("Punishments.Tempban.Messages.WrongTime", Arrays.asList(new String[]{"&4&lBans &8» &ePlease use a good TimeUnit!"}))){
					sender.sendMessage(Utils.format(s));
				}
				return;
			} catch(Exception e){
				for(String s : Punishments.punishments.getStringList("Punishments.Tempban.Messages.WrongTime", Arrays.asList(new String[]{"&4&lBans &8» &ePlease use a good TimeUnit!"}))){
					sender.sendMessage(Utils.format(s));
				}
				return;
			}
			if(time == -1L){
				for(String s : Punishments.punishments.getStringList("Punishments.Tempban.Messages.WrongTime", Arrays.asList(new String[]{"&4&lBans &8» &ePlease use a good TimeUnit!"}))){
					sender.sendMessage(Utils.format(s));
				}
				return;
			}
			
			BanAPI.addBan(bans + 1, sender.getName(), p.getName(), time, reason);

			int number = BanAPI.getBanNumber(p.getName());
			Long end = BanAPI.getBanTime(number);
			
			SimpleDateFormat df2 = new SimpleDateFormat("kk:mm dd/MM/yyyy");
			String date = df2.format(new Date(end));
              
			p.disconnect(Utils.format(kreason.replace("%banner%", BanAPI.getBannedBy(number)).replace("%unbantime%", (end == -1L ? "Never" : date)).replace("%reason%", BanAPI.getReason(number))));
		} else {
			String kreason = "";
			String reason = "";
			for(String s : args){
				reason = reason + s + " ";
			}
			reason = reason.replace(args[0] + " ", "").replace(args[1] + " ", "");
			banreason = reason;
			for(String s : Punishments.punishments.getStringList("Punishments.Tempban.Messages.KickMessage", Arrays.asList(new String[]{
					"&cBungeeUtilisals &8» &7Tempbanned",
					" ",
					"&cReason &8» &7%reason%",
					"&cUnban at &8» &7%unbantime%",
				 	"&cBanned by &8» &7%banner%"}))){
				kreason = kreason + "\n" + s;
			}

			Long time = -1L;
			try {
				time = TimeUnit.parseDateDiff(args[1], true);
			} catch(NumberFormatException e){
				for(String s : Punishments.punishments.getStringList("Punishments.Tempban.Messages.WrongTime", Arrays.asList(new String[]{"&4&lBans &8» &ePlease use a good TimeUnit!"}))){
					sender.sendMessage(Utils.format(s));
				}
				return;
			} catch(Exception e){
				for(String s : Punishments.punishments.getStringList("Punishments.Tempban.Messages.WrongTime", Arrays.asList(new String[]{"&4&lBans &8» &ePlease use a good TimeUnit!"}))){
					sender.sendMessage(Utils.format(s));
				}
				return;
			}
			if(time == -1L){
				for(String s : Punishments.punishments.getStringList("Punishments.Tempban.Messages.WrongTime", Arrays.asList(new String[]{"&4&lBans &8» &ePlease use a good TimeUnit!"}))){
					sender.sendMessage(Utils.format(s));
				}
				return;
			}
			BanAPI.addBan(bans + 1, sender.getName(), args[0], time, reason);
		}
		PlayerInfo pinfo = new PlayerInfo(args[0]);
		pinfo.addBan();
		for(String s : Punishments.punishments.getStringList("Punishments.Tempban.Messages.Banned", Arrays.asList(new String[]{"&4&lBans &8» &b%player% &ehas been tempbanned! &bReason: &e%reason%"}))){
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
			for(String s : Punishments.punishments.getStringList("Punishments.Messages.NoPermission", Arrays.asList(new String[]{"&4&lPermissions &8» &cYou don't have the permission to use this command!"}))){
				sender.sendMessage(Utils.format(s));
			}
		}
	}
}
