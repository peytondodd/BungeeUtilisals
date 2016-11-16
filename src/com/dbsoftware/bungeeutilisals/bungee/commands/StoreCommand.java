package com.dbsoftware.bungeeutilisals.bungee.commands;

import com.dbsoftware.bungeeutilisals.api.DBCommand;
import com.dbsoftware.bungeeutilisals.bungee.BungeeUtilisals;
import com.dbsoftware.bungeeutilisals.bungee.user.BungeeUser;
import com.dbsoftware.bungeeutilisals.bungee.utils.Utils;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;

public class StoreCommand extends DBCommand {

	public StoreCommand() {
		super("store");
	}

	@Override
	public void onExecute(BungeeUser user, String[] args) {
		onExecute(user.sender(), args);
	}

	@Override
	public void onExecute(CommandSender sender, String[] args) {
		TextComponent click = new TextComponent(ChatColor.translateAlternateColorCodes('&', BungeeUtilisals
				.getInstance().getConfig().getString("Store.Text").replace("%player%", sender.getName())));
		click.setHoverEvent(
				new HoverEvent(HoverEvent.Action.SHOW_TEXT,
						new ComponentBuilder(ChatColor.translateAlternateColorCodes('&', BungeeUtilisals.getInstance()
								.getConfig().getString("Store.Hover").replace("%player%", sender.getName())))
										.create()));
		click.setClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL,
				BungeeUtilisals.getInstance().getConfig().getString("Store.Site")));
		sender.sendMessage(Utils.format(BungeeUtilisals.getInstance().getConfig().getString("Store.Header")));

		for (String links : BungeeUtilisals.getInstance().getConfig().getStringList("Store.Message")) {
			sender.sendMessage(Utils.format(links.replace("%player%", sender.getName())));
		}
		sender.sendMessage(click);
		sender.sendMessage(Utils.format(BungeeUtilisals.getInstance().getConfig().getString("Store.Footer")));
	}
}