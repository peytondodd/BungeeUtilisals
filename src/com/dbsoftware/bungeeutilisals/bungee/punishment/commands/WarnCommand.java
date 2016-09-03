package com.dbsoftware.bungeeutilisals.bungee.punishment.commands;

import com.dbsoftware.bungeeutilisals.bungee.BungeeUtilisals;
import com.dbsoftware.bungeeutilisals.bungee.events.WarnEvent;
import com.dbsoftware.bungeeutilisals.bungee.punishment.Punishments;
import com.dbsoftware.bungeeutilisals.bungee.utils.PlayerInfo;
import com.dbsoftware.bungeeutilisals.bungee.utils.PluginMessageChannel;
import com.dbsoftware.bungeeutilisals.bungee.utils.Utils;

import net.md_5.bungee.BungeeCord;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

public class WarnCommand extends Command {

	public WarnCommand() {
		super("warn");
	}

	public static void executeWarnCommand(CommandSender sender, String[] args) {
		String warnreason = "";
		if(args.length < 2){
			for(String s : Punishments.punishments.getFile().getStringList("Punishments.Warn.Messages.WrongArgs")){
				sender.sendMessage(TextComponent.fromLegacyText(s.replace("&", "§")));
			}
			return;
		}
		ProxiedPlayer p = ProxyServer.getInstance().getPlayer(args[0]);
		if(p != null){
			String reason = "";
			for(String s : args){
				reason = reason + s + " ";
			}
			reason = reason.replace(p.getName() + " ", "");
			warnreason = reason;
			for(String s : Punishments.punishments.getFile().getStringList("Punishments.Warn.Messages.WarnMessage")){
				p.sendMessage(Utils.format(s.replace("%player%", sender.getName()).replace("%reason%", reason)));
			}
		} else {
			for(String s : Punishments.punishments.getFile().getStringList("Punishments.Warn.Messages.OfflinePlayer")){
				sender.sendMessage(TextComponent.fromLegacyText(s.replace("&", "§")));
			}
			return;
		}
		WarnEvent event = new WarnEvent(sender.getName(), p.getName(), warnreason);
		BungeeCord.getInstance().getPluginManager().callEvent(event);
		PlayerInfo pinfo = new PlayerInfo(args[0]);
		pinfo.addWarn();
		for(String s : Punishments.punishments.getFile().getStringList("Punishments.Mute.Messages.Warned")){
			sender.sendMessage(Utils.format(s.replace("%player%", args[0]).replace("%reason%", warnreason)));
		}
	}
	
	@Override
	public void execute(CommandSender sender, String[] args) {
		if(!(sender instanceof ProxiedPlayer)){
			executeWarnCommand(sender, args);
			return;
		}
		if(BungeeUtilisals.getInstance().getConfig().getBoolean("Bukkit-Permissions")){
			PluginMessageChannel.sendPermissionCheckPluginMessage("hasPermission", "butilisals.warn", "warn", args, ((ProxiedPlayer)sender));
			return;
		}
		if(sender.hasPermission("butilisals.warn") || sender.hasPermission("butilisals.*")){
			executeWarnCommand(sender, args);
		} else {
			for(String s : Punishments.punishments.getFile().getStringList("Punishments.Messages.NoPermission")){
				sender.sendMessage(Utils.format(s));
			}
		}
	}
}
