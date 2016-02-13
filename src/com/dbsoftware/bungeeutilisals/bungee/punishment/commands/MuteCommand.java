package com.dbsoftware.bungeeutilisals.bungee.punishment.commands;

import java.util.Arrays;
import com.dbsoftware.bungeeutilisals.bungee.BungeeUtilisals;
import com.dbsoftware.bungeeutilisals.bungee.punishment.MuteAPI;
import com.dbsoftware.bungeeutilisals.bungee.punishment.Punishments;
import com.dbsoftware.bungeeutilisals.bungee.utils.PlayerInfo;
import com.dbsoftware.bungeeutilisals.bungee.utils.PluginMessageChannel;
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
			for(String s : Punishments.punishments.getStringList("Punishments.Mute.Messages.WrongArgs", Arrays.asList(new String[]{"&4&lBans &8» &ePlease use &b/mute (player) (reason) &e!"}))){
				sender.sendMessage(TextComponent.fromLegacyText(s.replace("&", "§")));
			}
			return;
		}
		int mutes = MuteAPI.getMuteNumbers().size();
		if(MuteAPI.isMuted(args[0])){
			for(String s : Punishments.punishments.getStringList("Punishments.Mute.Messages.AlreadyBanned", Arrays.asList(new String[]{"&4&lBans &8» &cThat player is already muted!"}))){
				sender.sendMessage(Utils.format(s));
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
			mutereason = reason;
			for(String s : Punishments.punishments.getStringList("Punishments.Mute.Messages.MuteMessage", Arrays.asList(new String[]{
					"&4&lBans &8» &b%player% &ehas muted you!",
					"&4&lBans &8» &eReason: &b%reason%"}))){
				p.sendMessage(Utils.format(s.replace("%player%", sender.getName()).replace("%reason%", reason)));
			}
			MuteAPI.addMute(mutes + 1, sender.getName(), args[0], -1L, reason);
		} else {
			String reason = "";
			for(String s : args){
				reason = reason + s + " ";
			}
			reason = reason.replace(args[0] + " ", "");
			mutereason = reason;
			MuteAPI.addMute(mutes + 1, sender.getName(), args[0], -1L, reason);
		}
		PlayerInfo pinfo = new PlayerInfo(args[0]);
		pinfo.addMute();
		for(String s : Punishments.punishments.getStringList("Punishments.Mute.Messages.Muted", Arrays.asList(new String[]{"&4&lBans &8» &b%player% &ehas been Muted! &bReason: &e%reason%"}))){
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
			for(String s : Punishments.punishments.getStringList("Punishments.Messages.NoPermission", Arrays.asList(new String[]{"&4&lPermissions &8» &cYou don't have the permission to use this command!"}))){
				sender.sendMessage(Utils.format(s));
			}
		}
	}
}
