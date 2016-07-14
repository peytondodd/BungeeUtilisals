package com.dbsoftware.bungeeutilisals.bungee.punishment.commands;

import java.util.UUID;

import com.dbsoftware.bungeeutilisals.bungee.BungeeUtilisals;
import com.dbsoftware.bungeeutilisals.bungee.punishment.MuteAPI;
import com.dbsoftware.bungeeutilisals.bungee.punishment.Punishments;
import com.dbsoftware.bungeeutilisals.bungee.utils.PlayerInfo;
import com.dbsoftware.bungeeutilisals.bungee.utils.PluginMessageChannel;
import com.dbsoftware.bungeeutilisals.bungee.utils.UUIDFetcher;
import com.dbsoftware.bungeeutilisals.bungee.utils.Utils;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

public class MuteCommand extends Command {

	public MuteCommand() {
		super("mute");
	}

	public static void executeMuteCommand(CommandSender sender, String[] args) {
		String mutereason = "";
		if(args.length < 2){
			for(String s : Punishments.punishments.getFile().getStringList("Punishments.Mute.Messages.WrongArgs")){
				sender.sendMessage(TextComponent.fromLegacyText(s.replace("&", "§")));
			}
			return;
		}
		UUID uuid = UUIDFetcher.getUUIDOf(args[0]);
		if(uuid == null){
			return;
		}
		if(BungeeUtilisals.getInstance().getConfigData().UUIDSTORAGE){
			if(MuteAPI.isMuted(uuid.toString())){
				for(String s : Punishments.punishments.getFile().getStringList("Punishments.Mute.Messages.AlreadyBanned")){
					sender.sendMessage(Utils.format(s));
				}
				return;
			}
		} else {
			if(MuteAPI.isMuted(args[0])){
				for(String s : Punishments.punishments.getFile().getStringList("Punishments.Mute.Messages.AlreadyBanned")){
					sender.sendMessage(Utils.format(s));
				}
				return;
			}
		}

		ProxiedPlayer p = ProxyServer.getInstance().getPlayer(args[0]);
		if(p != null){
			String reason = "";
			for(String s : args){
				reason = reason + s + " ";
			}
			reason = reason.replace(p.getName() + " ", "");
			mutereason = reason;
			for(String s : Punishments.punishments.getFile().getStringList("Punishments.Mute.Messages.MuteMessage")){
				p.sendMessage(Utils.format(s.replace("%player%", sender.getName()).replace("%reason%", reason)));
			}
			if(BungeeUtilisals.getInstance().getConfigData().UUIDSTORAGE){
				MuteAPI.addMute(sender.getName(), uuid.toString(), -1L, reason);
			} else {
				MuteAPI.addMute(sender.getName(), args[0], -1L, reason);
			}
		} else {
			String reason = "";
			for(String s : args){
				reason = reason + s + " ";
			}
			reason = reason.replace(args[0] + " ", "");
			mutereason = reason;
			if(BungeeUtilisals.getInstance().getConfigData().UUIDSTORAGE){
				MuteAPI.addMute(sender.getName(), uuid.toString(), -1L, reason);
			} else {
				MuteAPI.addMute(sender.getName(), args[0], -1L, reason);
			}
		}
		PlayerInfo pinfo;
		if(BungeeUtilisals.getInstance().getConfigData().UUIDSTORAGE){
			pinfo = new PlayerInfo(uuid.toString());
		} else {
			pinfo = new PlayerInfo(args[0]);
		}
		pinfo.addMute();
		for(String s : Punishments.punishments.getFile().getStringList("Punishments.Mute.Messages.Muted")){
			sender.sendMessage(Utils.format(s.replace("%player%", args[0]).replace("%reason%", mutereason)));
		}
	}
	

	@Override
	public void execute(CommandSender sender, String[] args) {
		if(!(sender instanceof ProxiedPlayer)){
			executeMuteCommand(sender, args);
			return;
		}
		if(BungeeUtilisals.getInstance().getConfig().getBoolean("Bukkit-Permissions")){
			PluginMessageChannel.sendPermissionCheckPluginMessage("hasPermission", "butilisals.mute", "mute", args, ((ProxiedPlayer)sender));
			return;
		}
		if(sender.hasPermission("butilisals.mute") || sender.hasPermission("butilisals.*")){
			executeMuteCommand(sender, args);
		} else {
			for(String s : Punishments.punishments.getFile().getStringList("Punishments.Messages.NoPermission")){
				sender.sendMessage(Utils.format(s));
			}
		}
	}
}
