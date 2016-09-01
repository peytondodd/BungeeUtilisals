package com.dbsoftware.bungeeutilisals.bungee.commands;

import com.dbsoftware.bungeeutilisals.bungee.BungeeUser;
import com.dbsoftware.bungeeutilisals.bungee.BungeeUtilisals;
import com.dbsoftware.bungeeutilisals.bungee.utils.PluginMessageChannel;
import com.dbsoftware.bungeeutilisals.bungee.utils.Utils;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

public class LocalSpyCommand extends Command {
	
	public LocalSpyCommand() {
		super("localspy", "", BungeeUtilisals.getInstance().getConfig().getStringList("LocalSpy.Aliases").toArray(new String[]{}));
	}

	public static void executeSpyCommand(CommandSender sender, String[] args){
        if (!(sender instanceof ProxiedPlayer)) {
        	sender.sendMessage(Utils.format("&6Only players can use this command!"));
            return;
        }
        ProxiedPlayer p = (ProxiedPlayer)sender;
        BungeeUser user = BungeeUtilisals.getInstance().getUser(p);
        if(args.length > 0){
        	p.sendMessage(Utils.format(BungeeUtilisals.getInstance().getConfig().getString("LocalSpy.Messages.Usage")));
        	return;
        }
        
        if(user.isLocalSpy()){
        	user.setLocalSpy(false);
        	user.sendMessage(BungeeUtilisals.getInstance().getConfig().getString("LocalSpy.Messages.Disabled"));
        } else {
        	user.setLocalSpy(true);
        	user.sendMessage(BungeeUtilisals.getInstance().getConfig().getString("LocalSpy.Messages.Enabled"));
        }
	}

	@Override
	public void execute(CommandSender sender, String[] args) {
		if(BungeeUtilisals.getInstance().getConfig().getBoolean("Bukkit-Permissions")){
			PluginMessageChannel.sendPermissionCheckPluginMessage("hasPermission", "butilisals.localspy", "localspy", args, ((ProxiedPlayer)sender));
			return;
		}
		if(sender.hasPermission("butilisals.localspy") || sender.hasPermission("butilisals.*")){
			executeSpyCommand(sender, args);		
		} else {
			sender.sendMessage(Utils.format(BungeeUtilisals.getInstance().getConfig().getString("Prefix") + BungeeUtilisals.getInstance().getConfig().getString("Main-messages.no-permission")));
		}
	}
}
