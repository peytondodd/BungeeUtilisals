package com.dbsoftware.bungeeutilisals.bungee.commands;

import com.dbsoftware.bungeeutilisals.bungee.BungeeUser;
import com.dbsoftware.bungeeutilisals.bungee.BungeeUtilisals;
import com.dbsoftware.bungeeutilisals.bungee.utils.PluginMessageChannel;
import com.dbsoftware.bungeeutilisals.bungee.utils.Utils;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

public class StoreCommand extends Command {
		
	public StoreCommand() {
			super("store");{
		}
	}
	
	private static BungeeUtilisals instance = BungeeUtilisals.getInstance();

	public static void executeStoreCommand(CommandSender sender, String[] args){
		if(!(sender instanceof ProxiedPlayer)){
			return;
		}
		BungeeUser user = BungeeUtilisals.getInstance().getUser((ProxiedPlayer)sender);
		TextComponent click = new TextComponent( ChatColor.translateAlternateColorCodes('&', instance.getConfig().getString("Store.Text").replace("%player%", sender.getName())) );
		click.setHoverEvent( new HoverEvent( HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(ChatColor.translateAlternateColorCodes('&', instance.getConfig().getString("Store.Hover").replace("%player%", sender.getName()))).create() ) );
		click.setClickEvent( new ClickEvent( ClickEvent.Action.OPEN_URL, instance.getConfig().getString("Store.Site") ) );
		user.sendMessage(instance.getConfig().getString("Store.Header"));
		
		for (String links : instance.getConfig().getStringList("Store.Message")) {
			user.sendMessage(links.replace("%player%", sender.getName()));
		}
		
		sender.sendMessage(click);
		user.sendMessage(instance.getConfig().getString("Store.Footer"));
	}
	
	@Override
	public void execute(CommandSender sender, String[] args) {
		if(!(sender instanceof ProxiedPlayer)){
			executeStoreCommand(sender, args);
			return;
		}
		if(BungeeUtilisals.getInstance().getConfig().getBoolean("Bukkit-Permissions")){
			PluginMessageChannel.sendPermissionCheckPluginMessage("hasPermission", "butilisals.store", "store", args, ((ProxiedPlayer)sender));
			return;
		}
		if(sender.hasPermission("butilisals.store") || sender.hasPermission("butilisals.*")){
			executeStoreCommand(sender, args);		
		} else {
			sender.sendMessage(Utils.format(BungeeUtilisals.getInstance().getConfig().getString("Prefix") + BungeeUtilisals.getInstance().getConfig().getString("Main-messages.no-permission")));
		}
	}
}