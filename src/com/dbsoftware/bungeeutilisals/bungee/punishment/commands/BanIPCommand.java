package com.dbsoftware.bungeeutilisals.bungee.punishment.commands;

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

public class BanIPCommand extends Command {

	public BanIPCommand() {
		super("banip");
	}

	public static void executeBanIPCommand(CommandSender sender, String[] args) {
		String banreason = "";
		if(args.length < 2){
			for(String s : Punishments.punishments.getFile().getStringList("Punishments.BanIP.Messages.WrongArgs")){
				sender.sendMessage(TextComponent.fromLegacyText(s.replace("&", "§")));
			}
			return;
		}
		if(BanAPI.isBanned(args[0])){
			for(String s : Punishments.punishments.getFile().getStringList("Punishments.BanIP.Messages.AlreadyBanned")){
				sender.sendMessage(Utils.format(s));
			}
			return;
		}
		ProxiedPlayer p = ProxyServer.getInstance().getPlayer(args[0]);
		PlayerInfo pinfo = new PlayerInfo(args[0]);
		if(p != null){
			String kreason = "";
			String reason = "";
			for(String s : args){
				reason = reason + s + " ";
			}
			reason = reason.replace(p.getName() + " ", "");
			banreason = reason;
			for(String s : Punishments.punishments.getFile().getStringList("Punishments.BanIP.Messages.KickMessage")){
				kreason = kreason + "\n" + s;
			}
			String IP = pinfo.getIP();
			if(IP == null){
				for(String s : Punishments.punishments.getFile().getStringList("Punishments.BanIP.Messages.NeverJoined")){
					sender.sendMessage(Utils.format(s));
				}
				return;
			}
			BanAPI.addIPBan(sender.getName(), IP.replace("localhost", "127.0.0.1"), reason);
			p.disconnect(Utils.format(kreason.replace("%banner%", sender.getName()).replace("%reason%", reason)));
		} else {
			String kreason = "";
			String reason = "";
			for(String s : args){
				reason = reason + s + " ";
			}
			reason = reason.replace(args[0] + " ", "");
			banreason = reason;
			for(String s : Punishments.punishments.getFile().getStringList("Punishments.BanIP.Messages.KickMessage")){
				kreason = kreason + "\n" + s;
			}
			String IP = pinfo.getIP();
			if(IP == null){
				for(String s : Punishments.punishments.getFile().getStringList("Punishments.BanIP.Messages.NeverJoined")){
					sender.sendMessage(Utils.format(s));
				}
				return;
			}
			BanAPI.addIPBan(sender.getName(), IP.replace("localhost", "127.0.0.1"), reason);
		}
		pinfo.addBan();
		for(String s : Punishments.punishments.getFile().getStringList("Punishments.BanIP.Messages.Banned")){
			sender.sendMessage(Utils.format(s.replace("%ip%", pinfo.getIP().replace("localhost", "127.0.0.1")).replace("%reason%", banreason)));
		}
	}
	
	@Override
	public void execute(CommandSender sender, String[] args) {
		if(!(sender instanceof ProxiedPlayer)){
			executeBanIPCommand(sender, args);
			return;
		}
		if(BungeeUtilisals.getInstance().getConfig().getBoolean("Bukkit-Permissions")){
			PluginMessageChannel.sendPermissionCheckPluginMessage("hasPermission", "butilisals.banip", "banip", args, ((ProxiedPlayer)sender));
			return;
		}
		if(sender.hasPermission("butilisals.banip") || sender.hasPermission("butilisals.*")){
			executeBanIPCommand(sender, args);
		} else {
			for(String s : Punishments.punishments.getFile().getStringList("Punishments.Messages.NoPermission")){
				sender.sendMessage(Utils.format(s));
			}
		}
	}
}
