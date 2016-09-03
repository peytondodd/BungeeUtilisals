package com.dbsoftware.bungeeutilisals.bungee.punishment.commands;

import java.util.UUID;

import com.dbsoftware.bungeeutilisals.bungee.BungeeUtilisals;
import com.dbsoftware.bungeeutilisals.bungee.events.UnmuteEvent;
import com.dbsoftware.bungeeutilisals.bungee.punishment.MuteAPI;
import com.dbsoftware.bungeeutilisals.bungee.punishment.Punishments;
import com.dbsoftware.bungeeutilisals.bungee.utils.PluginMessageChannel;
import com.dbsoftware.bungeeutilisals.bungee.utils.UUIDFetcher;
import com.dbsoftware.bungeeutilisals.bungee.utils.Utils;

import net.md_5.bungee.BungeeCord;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

public class UnmuteCommand extends Command {

	public UnmuteCommand() {
		super("unmute");
	}

	public static void executeUnmuteCommand(CommandSender sender, String[] args) {
		if(args.length != 1){
			for(String s : Punishments.punishments.getFile().getStringList("Punishments.Unmute.Messages.WrongArgs")){
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
				MuteAPI.removeMute(uuid.toString());
				for(String s : Punishments.punishments.getFile().getStringList("Punishments.Unmute.Messages.Unmuted")){
					sender.sendMessage(Utils.format(s.replace("%player%", args[0])));
				}
				UnmuteEvent event = new UnmuteEvent(sender.getName(), args[0]);
				BungeeCord.getInstance().getPluginManager().callEvent(event);
				return;
			} else {
				for(String s : Punishments.punishments.getFile().getStringList("Punishments.Unmute.Messages.NotMuted")){
					sender.sendMessage(Utils.format(s));
				}
				UnmuteEvent event = new UnmuteEvent(sender.getName(), args[0]);
				BungeeCord.getInstance().getPluginManager().callEvent(event);
				return;
			}
		}
		if(MuteAPI.isMuted(args[0])){
			MuteAPI.removeMute(args[0]);
			for(String s : Punishments.punishments.getFile().getStringList("Punishments.Unmute.Messages.Unmuted")){
				sender.sendMessage(Utils.format(s.replace("%player%", args[0])));
			}
			UnmuteEvent event = new UnmuteEvent(sender.getName(), args[0]);
			BungeeCord.getInstance().getPluginManager().callEvent(event);
			return;
		} else {
			for(String s : Punishments.punishments.getFile().getStringList("Punishments.Unmute.Messages.NotMuted")){
				sender.sendMessage(Utils.format(s));
			}
			UnmuteEvent event = new UnmuteEvent(sender.getName(), args[0]);
			BungeeCord.getInstance().getPluginManager().callEvent(event);
			return;
		}
	}
	
	@Override
	public void execute(CommandSender sender, String[] args) {
		if(!(sender instanceof ProxiedPlayer)){
			executeUnmuteCommand(sender, args);
			return;
		}
		if(BungeeUtilisals.getInstance().getConfig().getBoolean("Bukkit-Permissions")){
			PluginMessageChannel.sendPermissionCheckPluginMessage("hasPermission", "butilisals.unmute", "unmute", args, ((ProxiedPlayer)sender));
			return;
		}
		if(sender.hasPermission("butilisals.unmute") || sender.hasPermission("butilisals.*")){
			executeUnmuteCommand(sender, args);
		} else {
			for(String s : Punishments.punishments.getFile().getStringList("Punishments.Messages.NoPermission")){
				sender.sendMessage(Utils.format(s));
			}
		}
	}
}
