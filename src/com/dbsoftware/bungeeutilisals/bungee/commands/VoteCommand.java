package com.dbsoftware.bungeeutilisals.bungee.commands;

import com.dbsoftware.bungeeutilisals.api.DBCommand;
import com.dbsoftware.bungeeutilisals.bukkit.BungeeUtilisals;
import com.dbsoftware.bungeeutilisals.bungee.BungeeUser;
import com.dbsoftware.bungeeutilisals.bungee.utils.Utils;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;

public class VoteCommand extends DBCommand {

	public VoteCommand() {
		super("vote");
	}

	@Override
	public void onExecute(BungeeUser user, String[] args) {
		onExecute(user.sender(), args);
	}

	@Override
	public void onExecute(CommandSender sender, String[] args) {
		TextComponent click = new TextComponent(ChatColor.translateAlternateColorCodes('&', BungeeUtilisals
				.getInstance().getConfig().getString("Vote.Text").replace("%player%", sender.getName())));
		click.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT,
				new ComponentBuilder(ChatColor.translateAlternateColorCodes('&', BungeeUtilisals.getInstance()
						.getConfig().getString("Vote.Hover").replace("%player%", sender.getName()))).create()));
		click.setClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL,
				BungeeUtilisals.getInstance().getConfig().getString("Vote.Site")));
		sender.sendMessage(Utils.format(BungeeUtilisals.getInstance().getConfig().getString("Vote.Header")));

		for (String links : BungeeUtilisals.getInstance().getConfig().getStringList("Vote.Links")) {
			sender.sendMessage(Utils.format(links.replace("%player%", sender.getName())));
		}

		sender.sendMessage(click);
		sender.sendMessage(Utils.format(BungeeUtilisals.getInstance().getConfig().getString("Vote.Footer")));
	}
}