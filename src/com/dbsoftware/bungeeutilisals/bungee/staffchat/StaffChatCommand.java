package com.dbsoftware.bungeeutilisals.bungee.staffchat;

import com.dbsoftware.bungeeutilisals.bungee.BungeeUser;
import com.dbsoftware.bungeeutilisals.bungee.BungeeUtilisals;
import com.dbsoftware.bungeeutilisals.bungee.utils.PluginMessageChannel;
import com.dbsoftware.bungeeutilisals.bungee.utils.Utils;

import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

public class StaffChatCommand extends Command {
	
	public StaffChatCommand() {
		super("staffchat", "", "sc");
	}
		
	public static void executeStaffChatCommand(CommandSender sender, String[] args){
		if (!StaffChat.inchat.contains(sender.getName())){
			StaffChat.inchat.add(sender.getName());
			sender.sendMessage(Utils.format(BungeeUtilisals.instance.getConfig().getString("StaffChat.ChatEnabled")));
			return;
		}
		StaffChat.inchat.remove(sender.getName());
		sender.sendMessage(Utils.format(BungeeUtilisals.instance.getConfig().getString("StaffChat.ChatDisabled")));
	}

	@Override
	public void execute(CommandSender sender, String[] args) {
		if(!(sender instanceof ProxiedPlayer)){
			sender.sendMessage(Utils.format("Only players can work with Bukkit permissions!"));
			return;
		}
		ProxiedPlayer p = (ProxiedPlayer)sender;
		BungeeUser user = BungeeUtilisals.getInstance().getUser(p);
		if(BungeeUtilisals.getInstance().getStaff().contains(user)){
			executeStaffChatCommand(sender, args);
			return;
		}
		if(BungeeUtilisals.getInstance().getConfig().getBoolean("Bukkit-Permissions")){
			PluginMessageChannel.sendPermissionCheckPluginMessage("hasPermission", "butilisals.staffchat", "staffchat", args, p);
			return;
		}
		if(sender.hasPermission("butilisals.staffchat") || sender.hasPermission("butilisals.*")){
			executeStaffChatCommand(sender, args);		
		} else {
			sender.sendMessage(Utils.format(BungeeUtilisals.getInstance().getConfig().getString("Prefix") + BungeeUtilisals.getInstance().getConfig().getString("Main-messages.no-permission")));
		}
	} 
}
