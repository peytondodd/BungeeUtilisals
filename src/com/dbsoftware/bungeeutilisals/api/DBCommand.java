package com.dbsoftware.bungeeutilisals.api;

import java.util.List;

import com.dbsoftware.bungeeutilisals.bungee.BungeeUser;
import com.dbsoftware.bungeeutilisals.bungee.BungeeUtilisals;
import com.dbsoftware.bungeeutilisals.bungee.utils.PluginMessageChannel;
import com.dbsoftware.bungeeutilisals.bungee.utils.Utils;
import com.google.common.collect.Lists;

import net.md_5.bungee.BungeeCord;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

public abstract class DBCommand extends Command {
	
	private String name;
	private String[] aliases;
	protected List<String> permissions = Lists.newArrayList();
	
	public DBCommand(String name, String... aliases) {
		super(name, "", aliases);
		
		this.name = name;
		this.aliases = aliases;
		permissions.add("butilisals." + name);
		permissions.add("butilisals.*");
	}
	
	public DBCommand(String name, List<String> aliases) {
		this(name, aliases.toArray(new String[]{}));
	}

	public String getName(){
		return name;
	}
	
	public String[] getAliases(){
		return aliases;
	}
	
	public void register(){
		BungeeCord.getInstance().getPluginManager().registerCommand(BungeeUtilisals.getInstance(), this);
	}
	
	@Override
	public void execute(CommandSender sender, String[] args){
		if(sender instanceof ProxiedPlayer){
			BungeeUser user = BungeeUtilisals.getInstance().getUser((ProxiedPlayer)sender);
			if(user == null) return;
			for(String perm : permissions){
				if(user.hasPermission(perm)){
					this.onExecute(user, args);
					return;
				}
			}
			if(BungeeUtilisals.getInstance().getConfig().getBoolean("Bukkit-Permissions")){
				PluginMessageChannel.sendPermissionCheckPluginMessage("hasPermission", "butilisals." + name, name, args, ((ProxiedPlayer)sender));
				return;
			}
			sender.sendMessage(Utils.format(BungeeUtilisals.getInstance().getConfig().getString("Prefix") + BungeeUtilisals.getInstance().getConfig().getString("Main-messages.no-permission")));
		} else {
			this.onExecute(sender, args);
		}
	}
	
	public abstract void onExecute(BungeeUser user, String[] args);
	public abstract void onExecute(CommandSender sender, String[] args);
}