package com.dbsoftware.bungeeutilisals.bungee.punishment.commands;

import com.dbsoftware.bungeeutilisals.bungee.BungeeUtilisals;
import com.dbsoftware.bungeeutilisals.bungee.punishment.MuteAPI;
import com.dbsoftware.bungeeutilisals.bungee.punishment.Punishments;
import com.dbsoftware.bungeeutilisals.bungee.utils.PluginMessageChannel;
import com.dbsoftware.bungeeutilisals.bungee.utils.Utils;
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
		if(MuteAPI.isMuted(args[0])){
			MuteAPI.removeMute(args[0]);
			for(String s : Punishments.punishments.getFile().getStringList("Punishments.Unmute.Messages.Unmuted")){
				sender.sendMessage(Utils.format(s.replace("%player%", args[0])));
			}
		} else {
			for(String s : Punishments.punishments.getFile().getStringList("Punishments.Unmute.Messages.NotMuted")){
				sender.sendMessage(Utils.format(s));
			}
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
