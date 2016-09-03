package com.dbsoftware.bungeeutilisals.bungee.punishment.commands;

import java.util.UUID;

import com.dbsoftware.bungeeutilisals.bungee.BungeeUtilisals;
import com.dbsoftware.bungeeutilisals.bungee.events.UnbanEvent;
import com.dbsoftware.bungeeutilisals.bungee.punishment.BanAPI;
import com.dbsoftware.bungeeutilisals.bungee.punishment.Punishments;
import com.dbsoftware.bungeeutilisals.bungee.utils.PlayerInfo;
import com.dbsoftware.bungeeutilisals.bungee.utils.PluginMessageChannel;
import com.dbsoftware.bungeeutilisals.bungee.utils.UUIDFetcher;
import com.dbsoftware.bungeeutilisals.bungee.utils.Utils;

import net.md_5.bungee.BungeeCord;
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
			for(String s : Punishments.punishments.getFile().getStringList("Punishments.Unban.Messages.WrongArgs")){
				sender.sendMessage(TextComponent.fromLegacyText(s.replace("&", "§")));
			}
			return;
		}
		UUID uuid = UUIDFetcher.getUUIDOf(args[0]);
		if(uuid == null){
			return;
		}
		Boolean b = false;
		PlayerInfo pinfo;
		pinfo = new PlayerInfo(args[0]);
		if(BungeeUtilisals.getInstance().getConfigData().UUIDSTORAGE){
			pinfo = new PlayerInfo(uuid.toString());
			if(BanAPI.isBanned(uuid.toString())){
				BanAPI.removeBan(uuid.toString());
				for(String s : Punishments.punishments.getFile().getStringList("Punishments.Unban.Messages.Unbanned")){
					sender.sendMessage(Utils.format(s.replace("%player%", args[0])));
				}
			}
			b = true;
		}
		String ip = pinfo.getIP();
		if(ip == null){
			for(String s : Punishments.punishments.getFile().getStringList("Punishments.Unban.Messages.NeverJoined")){
				sender.sendMessage(Utils.format(s));
			}
			return;
		}
		if(!BanAPI.isBanned(args[0]) && ip != null && !BanAPI.isIPBanned(ip)){
			for(String s : Punishments.punishments.getFile().getStringList("Punishments.Unban.Messages.NotBanned")){
				sender.sendMessage(Utils.format(s));
			}
			return;
		}
		
		if(BanAPI.isBanned(args[0])){
			BanAPI.removeBan(args[0]);
			b = true;
		}
		if(BanAPI.isIPBanned(ip)){
			BanAPI.removeIPBan(ip);
			b = true;
		}
		
		if(b){
			for(String s : Punishments.punishments.getFile().getStringList("Punishments.Unban.Messages.Unbanned")){
				sender.sendMessage(Utils.format(s.replace("%player%", args[0])));
			}
			UnbanEvent event = new UnbanEvent(sender.getName(), args[0]);
			BungeeCord.getInstance().getPluginManager().callEvent(event);
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
			for(String s : Punishments.punishments.getFile().getStringList("Punishments.Messages.NoPermission")){
				sender.sendMessage(Utils.format(s));
			}
		}
	}
}
