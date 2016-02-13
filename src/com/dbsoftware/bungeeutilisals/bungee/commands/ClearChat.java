package com.dbsoftware.bungeeutilisals.bungee.commands;

import com.dbsoftware.bungeeutilisals.bungee.BungeeUtilisals;
import com.dbsoftware.bungeeutilisals.bungee.utils.PluginMessageChannel;
import com.dbsoftware.bungeeutilisals.bungee.utils.Utils;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

public class ClearChat extends Command {
	
	public ClearChat() {
		super("clearchat");
	}

	private static BungeeUtilisals instance = BungeeUtilisals.getInstance();
		
	public static void executeClearChatCommand(CommandSender sender, String[] args){
		if(args.length != 1){
			sender.sendMessage(new TextComponent("§cUse /clearchat local or /clearchat global"));
		} else {
			if(args[0].equalsIgnoreCase("local") || args[0].equalsIgnoreCase("l")){
				if(!(sender instanceof ProxiedPlayer)){
					return;
				}
				ProxiedPlayer p = (ProxiedPlayer)sender;
				String server = p.getServer().getInfo().getName();
				for(ProxiedPlayer players : ProxyServer.getInstance().getServerInfo(server).getPlayers()){
					for(int i = 0; i < 100; i++){
						players.sendMessage(new TextComponent(" "));
					}
					players.sendMessage(new TextComponent(instance.getConfig().getString("ClearChat.Local").replace("&", "§").replace("%player%", p.getName())));
				}
				return;
			} if(args[0].equalsIgnoreCase("global") || args[0].equalsIgnoreCase("g")){
				for(int i = 0; i < 100; i++){
					ProxyServer.getInstance().broadcast(new TextComponent(" "));
				}
				ProxyServer.getInstance().broadcast(Utils.format(instance.getConfig().getString("ClearChat.Global").replace("%player%", sender.getName())));
				return;
			}
			sender.sendMessage(Utils.format("&cUse /clearchat local or /clearchat global"));
		}
	}
	
	@Override
	public void execute(CommandSender sender, String[] args) {
		if(!(sender instanceof ProxiedPlayer)){
			executeClearChatCommand(sender, args);
			return;
		}
		if(BungeeUtilisals.getInstance().getConfig().getBoolean("Bukkit-Permissions")){
			PluginMessageChannel.sendPermissionCheckPluginMessage("hasPermission", "butilisals.clearchat", "clearchat", args, ((ProxiedPlayer)sender));
			return;
		}
		if(sender.hasPermission("butilisals.clearchat") || sender.hasPermission("butilisals.*")){
			executeClearChatCommand(sender, args);		
		} else {
			sender.sendMessage(Utils.format(BungeeUtilisals.getInstance().getConfig().getString("Prefix") + BungeeUtilisals.getInstance().getConfig().getString("Main-messages.no-permission")));
		}
	}
}
