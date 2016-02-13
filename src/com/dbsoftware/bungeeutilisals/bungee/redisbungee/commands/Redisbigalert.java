package com.dbsoftware.bungeeutilisals.bungee.redisbungee.commands;

import com.dbsoftware.bungeeutilisals.bungee.BungeeUtilisals;
import com.dbsoftware.bungeeutilisals.bungee.redisbungee.ChannelNames;
import com.dbsoftware.bungeeutilisals.bungee.utils.PluginMessageChannel;
import com.dbsoftware.bungeeutilisals.bungee.utils.Utils;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

public class Redisbigalert extends Command {
	
	public Redisbigalert() {
		super("bigalert");{
		}
	}
	
	public static void executeBigalertCommand(CommandSender sender, String[] args){
		String prefix = ChatColor.translateAlternateColorCodes('&', BungeeUtilisals.getInstance().getConfig().getString("Prefix"));
		if (args.length >= 1) {
			if((args.length == 2) && (args[0].equalsIgnoreCase("config"))){
				if(BungeeUtilisals.getInstance().getConfig().getString("BigAlert.Messages." + args[1]) != null){
					String message = BungeeUtilisals.getInstance().getConfig().getString("BigAlert.Messages." + args[1]);
					
					if(BungeeUtilisals.getInstance().getConfig().getBoolean("BigAlert.Chat.Enabled")){
						BungeeUtilisals.getInstance().getRedisManager().getRedis().sendChannelMessage(ChannelNames.BU_CHAT.toString(), prefix + message.replaceAll("%n", " "));
					}
					if(BungeeUtilisals.getInstance().getConfig().getBoolean("BigAlert.ActionBar.Enabled")){
						BungeeUtilisals.getInstance().getRedisManager().getRedis().sendChannelMessage(ChannelNames.BU_ACTIONBAR.toString(), message.replaceAll("%n", " "));
					}
					
					if(BungeeUtilisals.getInstance().getConfig().getBoolean("BigAlert.Title.Enabled")){
						BungeeUtilisals.getInstance().getRedisManager().getRedis().sendChannelMessage(ChannelNames.BU_TITLE.toString(), message);
					}
				} else {
					sender.sendMessage(Utils.format("&cThat String doesn't exist in the Config!"));
					return;
				}
			}
			if((args.length != 2) && (args[0].equalsIgnoreCase("config"))){
				return;
			}
			
			String msg = "";
			for(String s : args){
				msg = msg + s + " ";
			}
			
			if(BungeeUtilisals.getInstance().getConfig().getBoolean("BigAlert.Chat.Enabled")){
				BungeeUtilisals.getInstance().getRedisManager().getRedis().sendChannelMessage(ChannelNames.BU_CHAT.toString(), prefix + msg.replaceAll("%n", " "));
			}
			if(BungeeUtilisals.getInstance().getConfig().getBoolean("BigAlert.ActionBar.Enabled")){
				BungeeUtilisals.getInstance().getRedisManager().getRedis().sendChannelMessage(ChannelNames.BU_ACTIONBAR.toString(), msg.replaceAll("%n", " "));
			}
	        
			if(BungeeUtilisals.getInstance().getConfig().getBoolean("BigAlert.Title.Enabled")){
				BungeeUtilisals.getInstance().getRedisManager().getRedis().sendChannelMessage(ChannelNames.BU_TITLE.toString(), msg);
			}
		} else {
			sender.sendMessage(Utils.format("&cYou need to enter at least 1 word!"));
		}
	}
	
	@Override
	public void execute(CommandSender sender, String[] args) {
		if(!(sender instanceof ProxiedPlayer)){
			executeBigalertCommand(sender, args);
			return;
		}
		if(BungeeUtilisals.getInstance().getConfig().getBoolean("Bukkit-Permissions")){
			PluginMessageChannel.sendPermissionCheckPluginMessage("hasPermission", "butilisals.bigalert", "bigalert", args, ((ProxiedPlayer)sender));
			return;
		}
		if(sender.hasPermission("butilisals.bigalert") || sender.hasPermission("butilisals.*")){
			executeBigalertCommand(sender, args);		
		} else {
			sender.sendMessage(Utils.format(BungeeUtilisals.getInstance().getConfig().getString("Prefix") + BungeeUtilisals.getInstance().getConfig().getString("Main-messages.no-permission")));
		}
	}
}