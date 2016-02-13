package com.dbsoftware.bungeeutilisals.bungee.commands;

import com.dbsoftware.bungeeutilisals.bungee.BungeeUtilisals;
import com.dbsoftware.bungeeutilisals.bungee.utils.PluginMessageChannel;
import com.dbsoftware.bungeeutilisals.bungee.utils.Utils;

import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

public class Store extends Command {
		
	public Store() {
			super("store");{
		}
	}
	
	private static BungeeUtilisals instance = (BungeeUtilisals)BungeeUtilisals.getInstance();

	@SuppressWarnings("deprecation")
	public static void executeStoreCommand(CommandSender sender, String[] args){
		TextComponent click = new TextComponent( instance.getConfig().getString("Store.Text").replace("&", "§").replace("%player%", sender.getName()) );
		click.setHoverEvent( new HoverEvent( HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(instance.getConfig().getString("Store.Hover").replace("&", "§").replace("%player%", sender.getName())).create() ) );
		click.setClickEvent( new ClickEvent( ClickEvent.Action.OPEN_URL, instance.getConfig().getString("Store.Site") ) );
		sender.sendMessage(new TextComponent(instance.getConfig().getString("Store.Header").replace("&", "§")));
		
		for (String links : instance.getConfig().getStringList("Store.Message")) {
			sender.sendMessage(links.replace("&", "§").replace("%player%", sender.getName()));
		}
		
		sender.sendMessage(click);
		sender.sendMessage(new TextComponent(instance.getConfig().getString("Store.Footer").replace("&", "§")));
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