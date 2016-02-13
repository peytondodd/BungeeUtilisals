package com.dbsoftware.bungeeutilisals.bungee.punishment.commands;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import com.dbsoftware.bungeeutilisals.bungee.BungeeUtilisals;
import com.dbsoftware.bungeeutilisals.bungee.punishment.BanAPI;
import com.dbsoftware.bungeeutilisals.bungee.punishment.MuteAPI;
import com.dbsoftware.bungeeutilisals.bungee.punishment.Punishments;
import com.dbsoftware.bungeeutilisals.bungee.utils.PlayerInfo;
import com.dbsoftware.bungeeutilisals.bungee.utils.PluginMessageChannel;
import com.dbsoftware.bungeeutilisals.bungee.utils.Utils;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

public class PlayerInfoCommand extends Command {

	public PlayerInfoCommand() {
		super("playerinfo", "", "pinfo");
	}

	public static void executeCheckBanCommand(final CommandSender sender, final String[] args) {
		if(args.length != 1){
			for(String s : Punishments.punishments.getStringList("Punishments.PlayerInfo.Messages.WrongArgs", Arrays.asList(new String[]{"&4&lBans &8» &ePlease use &b/pinfo (player)&e!"}))){
				sender.sendMessage(TextComponent.fromLegacyText(s.replace("&", "§")));
			}
			return;
		}
		PlayerInfo pinfo = new PlayerInfo(args[0]);
		if(!pinfo.isInTable()){
			for(String s : Punishments.punishments.getStringList("Punishments.PlayerInfo.Messages.NeverJoined", Arrays.asList(new String[]{"&4&lBans &8» &eThat player never joined before!"}))){
				sender.sendMessage(TextComponent.fromLegacyText(s.replace("&", "§")));
			}
			return;
		}
		
		String IP = pinfo.getIP();
		
		int bannumber = -1;
		long end = -1L;
		int mutenumber = -1;
		long muteend = -1L;
		int ipbannumber = -1;
		if(BanAPI.isBanned(args[0])){
			bannumber = BanAPI.getBanNumber(args[0]);
			end = BanAPI.getBanTime(bannumber);
		}
		
		if(MuteAPI.isMuted(args[0])){
			mutenumber = MuteAPI.getMuteNumber(args[0]);
			end = MuteAPI.getMuteTime(mutenumber);
		}
		
		if(BanAPI.isIPBanned(IP)){
			ipbannumber = BanAPI.getIPBanNumber(IP);
		}

		SimpleDateFormat df2 = new SimpleDateFormat("kk:mm dd/MM/yyyy");
		String date = df2.format(new Date(end));
		
		boolean isbanned = BanAPI.isBanned(args[0]);
		String bannedby = BanAPI.getBannedBy(bannumber);
		String banreason = BanAPI.getReason(bannumber);
		
		boolean isIPbanned = BanAPI.isIPBanned(IP);
		String ipbannedby = BanAPI.getIPBannedBy(ipbannumber);
		String ipbanreason = BanAPI.getIPReason(ipbannumber);
		
		boolean ismuted = MuteAPI.isMuted(args[0]);
		String mutedby = MuteAPI.getMutedBy(mutenumber);
		String mutereason = MuteAPI.getReason(mutenumber);
		
		List<BaseComponent[]> messages = new ArrayList<BaseComponent[]>();
		for(String s : Punishments.punishments.getStringList("Punishments.PlayerInfo.Messages.Info",
					Arrays.asList(new String[]{"&4&lBans &8» &eInformation of &b%player%&e:",
					" &eIP: &b%ip%",
					" &eBans: &b%bans%",
					" &eMutes: &b%mutes%",
					" &eKicks: &b%kicks%",
					" &eWarns: &b%warns%",
					" &eCurrent Ban: &b%isbanned%",
					" &eCurrent IPBan: &b%isIPbanned%",
					" &eCurrent Mute: &b%ismuted%"}))){
			messages.add(Utils.format(s.replace("&", "§").replace("%ip%", IP.replace("localhost", "127.0.0.1"))
						.replace("%player%", args[0])
							.replace("%bans%", String.valueOf(pinfo.getBans()))
							.replace("%mutes%", String.valueOf(pinfo.getMutes()))
							.replace("%kicks%", String.valueOf(pinfo.getKicks()))
							.replace("%warns%", String.valueOf(pinfo.getWarns()))
							/**IS BANNED*/	.replace("%isbanned%", (isbanned ? Punishments.punishments.getString("Punishments.PlayerInfo.Messages.isBanned", "&cBanned by %banner% for %reason%. Unban time: %time%")
														.replace("&", "§")
														.replace("%banner%", bannedby).replace("%reason%", banreason)
														.replace("%time%", (end == -1L ? "Never" : date))
												
							/**IS NOT BANNED*/			: Punishments.punishments.getString("Punishments.PlayerInfo.Messages.isNotBanned", "&cNot Banned"))
												
							/**IS IPBANNED*/).replace("%isIPbanned%", (isIPbanned ? Punishments.punishments.getString("Punishments.PlayerInfo.Messages.isIPBanned", "&cIPBanned by %banner% for %reason%.")
												.replace("&", "§")
												.replace("%banner%", ipbannedby).replace("%reason%", ipbanreason)
										
					/**IS NOT IPBANNED*/		: Punishments.punishments.getString("Punishments.PlayerInfo.Messages.isNotIPBanned", "&cNot IPBanned"))
										
					/**IS MUTED*/			).replace("%ismuted%", (ismuted ? Punishments.punishments.getString("Punishments.PlayerInfo.Messages.isMuted", "&cMuted by %muter% for %reason%. Unmute time: %time%")
												.replace("&", "§")
												.replace("%muter%", mutedby).replace("%reason%", mutereason)
												.replace("%time%", (muteend == -1L ? "Never" : date))
										
					/**IS NOT MUTED*/			: Punishments.punishments.getString("Punishments.PlayerInfo.Messages.isNotMuted", "&cNot Muted")))));
		}
		for(BaseComponent[] message : messages){
			sender.sendMessage(message);
		}
		return;
	}
	
	@Override
	public void execute(CommandSender sender, String[] args) {
		if(!(sender instanceof ProxiedPlayer)){
			executeCheckBanCommand(sender, args);
			return;
		}
		if(BungeeUtilisals.getInstance().getConfig().getBoolean("Bukkit-Permissions")){
			PluginMessageChannel.sendPermissionCheckPluginMessage("hasPermission", "butilisals.playerinfo", "playerinfo", args, ((ProxiedPlayer)sender));
			return;
		}
		if(sender.hasPermission("butilisals.playerinfo") || sender.hasPermission("butilisals.*")){
			executeCheckBanCommand(sender, args);
		} else {
			for(String s : Punishments.punishments.getStringList("Punishments.Messages.NoPermission", Arrays.asList(new String[]{"&4&lPermissions &8» &cYou don't have the permission to use this command!"}))){
				sender.sendMessage(Utils.format(s));
			}
		}
	}
}