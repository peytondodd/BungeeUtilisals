package com.dbsoftware.bungeeutilisals.bungee.punishment.commands;

import java.util.UUID;

import com.dbsoftware.bungeeutilisals.bungee.BungeeUtilisals;
import com.dbsoftware.bungeeutilisals.bungee.events.KickEvent;
import com.dbsoftware.bungeeutilisals.bungee.punishment.Punishments;
import com.dbsoftware.bungeeutilisals.bungee.utils.PlayerInfo;
import com.dbsoftware.bungeeutilisals.bungee.utils.PluginMessageChannel;
import com.dbsoftware.bungeeutilisals.bungee.utils.UUIDFetcher;
import com.dbsoftware.bungeeutilisals.bungee.utils.Utils;

import net.md_5.bungee.BungeeCord;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

public class KickCommand extends Command {

	public KickCommand() {
		super("kick");
	}

	public static void executeKickCommand(CommandSender sender, String[] args) {
		if(args.length < 2){
			for(String s : Punishments.punishments.getFile().getStringList("Punishments.Kick.Messages.WrongArgs")){
				sender.sendMessage(TextComponent.fromLegacyText(s.replace("&", "§")));
			}
			return;
		}
		ProxiedPlayer p = ProxyServer.getInstance().getPlayer(args[0]);
		if(p == null){
			for(String s : Punishments.punishments.getFile().getStringList("Punishments.Kick.Messages.NotOnline")){
				sender.sendMessage(Utils.format(s));
			}
			return;
		}
		String kreason = "";
		String reason = "";
		for(String s : args){
			reason = reason + s + " ";
		}
		reason = reason.replace(p.getName() + " ", "");
		for(String s : Punishments.punishments.getFile().getStringList("Punishments.Kick.Messages.KickMessage")){
			kreason = kreason + "\n" + s;
		}
        
		UUID uuid = UUIDFetcher.getUUIDOf(args[0]);
		if(uuid == null){
			return;
		}
		PlayerInfo info;
		if(BungeeUtilisals.getInstance().getConfigData().UUIDSTORAGE){
			info = new PlayerInfo(uuid.toString());
		} else {
			info = new PlayerInfo(args[0]);
		}
		info.addKick();
		KickEvent event = new KickEvent(sender.getName(), args[0], reason);
		BungeeCord.getInstance().getPluginManager().callEvent(event);
		p.disconnect(Utils.format(kreason.replace("%kicker%", sender.getName()).replace("%reason%", reason)));
		for(String s : Punishments.punishments.getFile().getStringList("Punishments.Kick.Messages.Kicked")){
			sender.sendMessage(Utils.format(s.replace("%player%", args[0])));
		}
	}
	

	@Override
	public void execute(CommandSender sender, String[] args) {
		if(!(sender instanceof ProxiedPlayer)){
			executeKickCommand(sender, args);
			return;
		}
		if(BungeeUtilisals.getInstance().getConfig().getBoolean("Bukkit-Permissions")){
			PluginMessageChannel.sendPermissionCheckPluginMessage("hasPermission", "butilisals.kick", "kick", args, ((ProxiedPlayer)sender));
			return;
		}
		if(sender.hasPermission("butilisals.kick") || sender.hasPermission("butilisals.*")){
			executeKickCommand(sender, args);
		} else {
			for(String s : Punishments.punishments.getFile().getStringList("Punishments.Messages.NoPermission")){
				sender.sendMessage(Utils.format(s));
			}
		}
	}
}
