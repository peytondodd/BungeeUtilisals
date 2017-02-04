package com.dbsoftware.bungeeutilisals.bungee.commands;

import com.dbsoftware.bungeeutilisals.api.commands.DBCommand;
import com.dbsoftware.bungeeutilisals.bungee.BungeeUtilisals;
import com.dbsoftware.bungeeutilisals.bungee.events.PrivateMessageEvent;
import com.dbsoftware.bungeeutilisals.bungee.user.BungeeUser;
import com.dbsoftware.bungeeutilisals.bungee.utils.Utils;
import com.google.common.base.Joiner;

import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;

public class ReplyCommand extends DBCommand {

	public ReplyCommand() {
		super("greply", BungeeUtilisals.getInstance().getConfig().getStringList("PrivateMessages.Reply.Aliases"));
		permissions.add("butilisals.reply");
	}

	@Override
	public void onExecute(BungeeUser user, String[] args) {
		ProxiedPlayer p = user.getPlayer();
		if (!BungeeUtilisals.getInstance().getPmcache().containsKey(p.getName().toLowerCase())) {
			p.sendMessage(Utils.format(BungeeUtilisals.getInstance().getConfig().getString("PrivateMessages.Reply.Messages.NoTarget")));
			return;
		}
		if (args.length < 1) {
			p.sendMessage(Utils.format(BungeeUtilisals.getInstance().getConfig().getString("PrivateMessages.Reply.Messages.WrongUsage")));
			return;
		}
		String target = BungeeUtilisals.getInstance().getPmcache().get(p.getName().toLowerCase());
		ProxiedPlayer pl = ProxyServer.getInstance().getPlayer(target);
		if (pl == null) {
			p.sendMessage(Utils.format(BungeeUtilisals.getInstance().getConfig().getString("PrivateMessages.Reply.Messages.OfflineTarget")));
			BungeeUtilisals.getInstance().getPmcache().remove(p.getName().toLowerCase());
			BungeeUtilisals.getInstance().getPmcache().remove(target.toLowerCase());
			return;
		}
		String message = Joiner.on(" ").join(args);

		PrivateMessageEvent event = new PrivateMessageEvent(BungeeUtilisals.getInstance().getUser(p),
				BungeeUtilisals.getInstance().getUser(pl), message);
		ProxyServer.getInstance().getPluginManager().callEvent(event);
		if (event.isCancelled()) {
			return;
		}

		if (BungeeUtilisals.getInstance().getPmcache().containsKey(p.getName().toLowerCase())) {
			BungeeUtilisals.getInstance().getPmcache().remove(p.getName().toLowerCase());
		}
		if (BungeeUtilisals.getInstance().getPmcache().containsKey(pl.getName().toLowerCase())) {
			BungeeUtilisals.getInstance().getPmcache().remove(pl.getName().toLowerCase());
		}
		BungeeUtilisals.getInstance().getPmcache().put(p.getName().toLowerCase(), pl.getName().toLowerCase());
		BungeeUtilisals.getInstance().getPmcache().put(pl.getName().toLowerCase(), p.getName().toLowerCase());

		p.sendMessage(Utils.format(BungeeUtilisals.getInstance().getConfig()
				.getString("PrivateMessages.Reply.Messages.Format.Sending").replace("%player%", pl.getName())
				.replace("%server%", pl.getServer().getInfo().getName()).replace("%message%", message)));
		pl.sendMessage(Utils.format(BungeeUtilisals.getInstance().getConfig()
				.getString("PrivateMessages.Reply.Messages.Format.Receiving").replace("%player%", p.getName())
				.replace("%server%", p.getServer().getInfo().getName()).replace("%message%", message)));
	}

	@Override
	public void onExecute(CommandSender sender, String[] args) {
		sender.sendMessage(Utils.format("&6Only players can use this command!"));
	}
}
