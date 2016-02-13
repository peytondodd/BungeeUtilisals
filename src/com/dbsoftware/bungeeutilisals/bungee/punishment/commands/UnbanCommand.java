package com.dbsoftware.bungeeutilisals.bungee.punishment.commands;

import java.util.Arrays;

import com.dbsoftware.bungeeutilisals.bungee.BungeeUtilisals;
import com.dbsoftware.bungeeutilisals.bungee.punishment.BanAPI;
import com.dbsoftware.bungeeutilisals.bungee.punishment.Punishments;
import com.dbsoftware.bungeeutilisals.bungee.utils.PlayerInfo;
import com.dbsoftware.bungeeutilisals.bungee.utils.PluginMessageChannel;
import com.dbsoftware.bungeeutilisals.bungee.utils.Utils;

import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

public class UnbanCommand extends Command {

	public UnbanCommand() {
		super("unban");
	}

	public static void executeUnbanCommand(CommandSender sender, String[] args) {
		if(args.length != 1){
			for(String s : Punishments.punishments.getStringList("Punishments.Unban.Messages.WrongArgs", Arrays.asList(new String[]{"&4&lBans &8» &ePlease use &b/unban (player)&e!"}))){
				sender.sendMessage(TextComponent.fromLegacyText(s.replace("&", "§")));
			}
			return;
		}
		PlayerInfo pinfo = new PlayerInfo(args[0]);
		String ip = pinfo.getIP();
		if(!BanAPI.isBanned(args[0]) && ip != null && !BanAPI.isIPBanned(ip)){
			for(String s : Punishments.punishments.getStringList("Punishments.Unban.Messages.NotBanned", Arrays.asList(new String[]{"&4&lBans &8» &cThat player is not banned!"}))){
				sender.sendMessage(Utils.format(s));
			}
			return;
		}
		if(BanAPI.isBanned(args[0])){
			BanAPI.removeBan(BanAPI.getBanNumber(args[0]));
			for(String s : Punishments.punishments.getStringList("Punishments.Unban.Messages.Unbanned", Arrays.asList(new String[]{"&4&lBans &8» &b%player% &ehas been unbanned!"}))){
				sender.sendMessage(Utils.format(s.replace("%player%", args[0])));
			}
		}
		if(ip == null){
			for(String s : Punishments.punishments.getStringList("Punishments.Unban.Messages.NeverJoined", Arrays.asList(new String[]{"&4&lBans &8» &cThat player never joined!"}))){
				sender.sendMessage(Utils.format(s));
			}
			return;
		}
		if(BanAPI.isIPBanned(ip)){
			BanAPI.removeIPBan(BanAPI.getIPBanNumber(ip));
			for(String s : Punishments.punishments.getStringList("Punishments.Unban.Messages.Unbanned", Arrays.asList(new String[]{"&4&lBans &8» &b%player% &ehas been unbanned!"}))){
				sender.sendMessage(Utils.format(s.replace("%player%", args[0])));
			}
		}
	}
	
	@Override
	public void execute(CommandSender sender, String[] args) {
		if(!(sender instanceof ProxiedPlayer)){
			executeUnbanCommand(sender, args);
			return;
		}
		if(BungeeUtilisals.getInstance().getConfig().getBoolean("Bukkit-Permissions")){
			PluginMessageChannel.sendPermissionCheckPluginMessage("hasPermission", "butilisals.unban", "unban", args, ((ProxiedPlayer)sender));
			return;
		}
		if(sender.hasPermission("butilisals.unban") || sender.hasPermission("butilisals.*")){
			executeUnbanCommand(sender, args);
		} else {
			for(String s : Punishments.punishments.getStringList("Punishments.Messages.NoPermission", Arrays.asList(new String[]{"&4&lPermissions &8» &cYou don't have the permission to use this command!"}))){
				sender.sendMessage(Utils.format(s));
			}
		}
	}
}
