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

public class RulesCommand extends DBCommand {

	public RulesCommand() {
		super("rules");
	}

	@Override
	public void onExecute(BungeeUser user, String[] args) {
		onExecute(user.sender(), args);
	}

	@Override
	public void onExecute(CommandSender sender, String[] args) {
		TextComponent click = new TextComponent(ChatColor.translateAlternateColorCodes('&', BungeeUtilisals
				.getInstance().getConfig().getString("Rules.Text").replace("%player%", sender.getName())));
		click.setHoverEvent(
				new HoverEvent(HoverEvent.Action.SHOW_TEXT,
						new ComponentBuilder(ChatColor.translateAlternateColorCodes('&', BungeeUtilisals.getInstance()
								.getConfig().getString("Rules.Hover").replace("%player%", sender.getName())))
										.create()));
		click.setClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL,
				BungeeUtilisals.getInstance().getConfig().getString("Rules.Site")));

		sender.sendMessage(Utils.format(BungeeUtilisals.getInstance().getConfig().getString("Rules.Header")));
		for (String rules : BungeeUtilisals.getInstance().getConfig().getStringList("Rules.Rules")) {
			sender.sendMessage(Utils.format(rules.replace("%player%", sender.getName())));
		}
		sender.sendMessage(click);
		sender.sendMessage(Utils.format(BungeeUtilisals.getInstance().getConfig().getString("Rules.Footer")));
	}
}