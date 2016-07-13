package com.dbsoftware.bungeeutilisals.bungee.events;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import com.dbsoftware.bungeeutilisals.bungee.commands.Alert;
import com.dbsoftware.bungeeutilisals.bungee.commands.Bgc;
import com.dbsoftware.bungeeutilisals.bungee.commands.Bigalert;
import com.dbsoftware.bungeeutilisals.bungee.commands.Butilisals;
import com.dbsoftware.bungeeutilisals.bungee.commands.ClearChat;
import com.dbsoftware.bungeeutilisals.bungee.commands.Find;
import com.dbsoftware.bungeeutilisals.bungee.commands.Glist;
import com.dbsoftware.bungeeutilisals.bungee.commands.Hub;
import com.dbsoftware.bungeeutilisals.bungee.commands.Rules;
import com.dbsoftware.bungeeutilisals.bungee.commands.Server;
import com.dbsoftware.bungeeutilisals.bungee.commands.Store;
import com.dbsoftware.bungeeutilisals.bungee.commands.Vote;
import com.dbsoftware.bungeeutilisals.bungee.friends.FriendsCommand;
import com.dbsoftware.bungeeutilisals.bungee.party.PartyCommand;
import com.dbsoftware.bungeeutilisals.bungee.punishment.commands.BanCommand;
import com.dbsoftware.bungeeutilisals.bungee.punishment.commands.BanIPCommand;
import com.dbsoftware.bungeeutilisals.bungee.punishment.commands.BanInfoCommand;
import com.dbsoftware.bungeeutilisals.bungee.punishment.commands.KickCommand;
import com.dbsoftware.bungeeutilisals.bungee.punishment.commands.MuteCommand;
import com.dbsoftware.bungeeutilisals.bungee.punishment.commands.TempbanCommand;
import com.dbsoftware.bungeeutilisals.bungee.punishment.commands.TempmuteCommand;
import com.dbsoftware.bungeeutilisals.bungee.punishment.commands.UnbanCommand;
import com.dbsoftware.bungeeutilisals.bungee.punishment.commands.UnmuteCommand;
import com.dbsoftware.bungeeutilisals.bungee.punishment.commands.WarnCommand;
import com.dbsoftware.bungeeutilisals.bungee.report.ReportCommand;
import com.dbsoftware.bungeeutilisals.bungee.report.ReportListCommand;
import com.dbsoftware.bungeeutilisals.bungee.staffchat.StaffChatCommand;
import com.dbsoftware.bungeeutilisals.bungee.utils.Utils;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.PluginMessageEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

public class PluginMessageReceive implements Listener {

	@EventHandler
	public void onMessageReceive(PluginMessageEvent event){
		if (event.getTag().equalsIgnoreCase("BungeeCord")) {
			DataInputStream in = new DataInputStream(new ByteArrayInputStream(event.getData()));
			try {
				String channel = in.readUTF();
				if(channel.equals("hasPermission")){
					String pl = in.readUTF();
					String command = in.readUTF();
					String argline = in.readUTF();
					String[] args = new String[]{};
					if(argline.isEmpty()){
						args = new String[]{};
					} else {
						args = argline.split(" ");
					}
					boolean hasperm = in.readBoolean();
					
					ProxiedPlayer p = ProxyServer.getInstance().getPlayer(pl);
					if(p == null){
						return;
					}
					CommandSender sender = (CommandSender)p;
					if(!hasperm){
						p.sendMessage(Utils.format("&cYou don't have the permission to execute this Command!"));
						return;
					}
					if(command.equals("glag")){
						Bgc.executeBgcCommand(sender, args);
					} if(command.equals("alert")){
						Alert.executeAlertCommand(sender, args);
						return;
					} if(command.equals("bigalert")){
						Bigalert.executeBigalertCommand(sender, args);
						return;
					} if(command.equals("butilisals")){
						Butilisals.executeButilisalsCommand(sender, args);
						return;
					} if(command.equals("clearchat")){
						ClearChat.executeClearChatCommand(sender, args);
						return;
					} if(command.equals("find")){
						Find.executeFindCommand(sender, args);
						return;
					} if(command.equals("glist")){
						Glist.executeGlistCommand(sender, args);
						return;
					} if(command.equals("hub")){
						Hub.executeHubCommand(sender, args);
						return;
					} if(command.equals("rules")){
						Rules.executeRulesCommand(sender, args);
						return;
					} if(command.equals("server")){
						Server.executeServerCommand(sender, args);
						return;
					} if(command.equals("store")){
						Store.executeStoreCommand(sender, args);
						return;
					} if(command.equals("vote")){
						Vote.executeVoteCommand(sender, args);
						return;
					} if(command.equals("report")){
						if(args[0] != null && args[0].contains("toggle")){
							ReportCommand.executeReportToggleCommand(sender, args);
							return;
						} if(args[0] != null && args[0].contains("remove") || args[0].contains("delete")){
							ReportCommand.executeReportDeleteCommand(sender, args);
							return;
						}
						ReportCommand.executeReportCommand(sender, args);
						return;
					} if(command.equals("reportlist")){
						ReportListCommand.executeReportListCommand(sender, args);
						return;
					} if(command.equals("party")){
						PartyCommand.executePartyCommand(sender, args);
						return;
					} if(command.equals("friend")){
						FriendsCommand.executeFriendCommand(sender, args);
						return;
					} if(command.equals("staffchat")){
						StaffChatCommand.executeStaffChatCommand(sender, args);
						return;
					} if(command.equals("ban")){
						BanCommand.executeBanCommand(sender, args);
						return;
					} if(command.equals("unban")){
						UnbanCommand.executeUnbanCommand(sender, args);
						return;
					} if(command.equals("kick")){
						KickCommand.executeKickCommand(sender, args);
						return;
					} if(command.equals("tempban")){
						TempbanCommand.executeTempBanCommand(sender, args);
						return;
					} if(command.equals("banip")){
						BanIPCommand.executeBanIPCommand(sender, args);
						return;
					} if(command.equals("baninfo")){
						BanInfoCommand.executeCheckBanCommand(sender, args);
						return;
					} if(command.equals("mute")){
						MuteCommand.executeMuteCommand(sender, args);
						return;
					} if(command.equals("unmute")){
						UnmuteCommand.executeUnmuteCommand(sender, args);
						return;
					} if(command.equals("warn")){
						WarnCommand.executeWarnCommand(sender, args);
						return;
					} if(command.equals("tempmute")){
						TempmuteCommand.executeTempmuteCommand(sender, args);
						return;
					}
				}
				if(channel.equals("getLimit")){
					String name = in.readUTF();
					ProxiedPlayer p = ProxyServer.getInstance().getPlayer(name);
					if(p == null){
						return;
					}
					CommandSender sender = (CommandSender)p;
					String[] args = in.readUTF().split(" ");
					int limit = in.readInt();
					
					FriendsCommand.executeFriendAddCommand(sender, args, limit);
				}
			} catch (IOException e) {
				e.printStackTrace();
			}	
		}
	}
	
}
