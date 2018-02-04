package com.dbsoftware.bungeeutilisals.bungee.commands;

import com.dbsoftware.bungeeutilisals.api.commands.DBCommand;
import com.dbsoftware.bungeeutilisals.bungee.BungeeUtilisals;
import com.dbsoftware.bungeeutilisals.bungee.user.BungeeUser;
import com.dbsoftware.bungeeutilisals.bungee.utils.Utils;
import com.google.common.base.Joiner;

import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.config.Configuration;

public class BigalertCommand extends DBCommand {
	
	public BigalertCommand() {
		super("bigalert");
	}

	@Override
	public void onExecute(BungeeUser user, String[] args) {
		this.onExecute(user.sender(), args);
	}

	@Override
	public void onExecute(CommandSender sender, String[] args){
		if(args.length == 0){
			sender.sendMessage(Utils.format("&cPlease enter a message to alert."));
			return;
		}
		Configuration config = BungeeUtilisals.getInstance().getConfig();
		Boolean chat = config.getBoolean("BigAlert.Chat.Enabled"), title = config.getBoolean("BigAlert.Title.Enabled"), actionbar = config.getBoolean("BigAlert.ActionBar.Enabled");
		
		if(args.length == 2 && args[0].equalsIgnoreCase("config")){
			if(!BungeeUtilisals.getInstance().getConfig().contains("BigAlert.Messages." + args[1])){
				sender.sendMessage(Utils.format("&cThis message is does not exist in the config."));
				return;
			}
			String message = BungeeUtilisals.getInstance().getConfig().getString("BigAlert.Messages." + args[1]);
			if(chat){
				chat(message);
			}
			if(actionbar){
				bar(message);
			}
			if(title){
				String[] titles = message.split("%n");
				String tit = titles[0];
				String stit = (titles.length == 2 ? titles[1] : null);
				Integer in = config.getInt("BigAlert.Title.FadeIn"), stay = config.getInt("BigAlert.Title.Stay"), out = config.getInt("BigAlert.Title.FadeOut");
				
				title(in, stay, out, tit, stit);
			}
			return;
		}
		
		String message = Joiner.on(" ").join(args);
		if(chat){
			chat(message);
		}
		if(actionbar){
			bar(message);
		}
		if(title){
			String tit;
			String stit;
			
			if(message.contains("%n")){
				String[] titles = message.split("%n");
				tit = titles[0];
				stit = (titles.length == 2 ? titles[1] : null);
			} else {
				tit = message;
				stit = null;
			}
			Integer in = config.getInt("BigAlert.Title.FadeIn"), stay = config.getInt("BigAlert.Title.Stay"), out = config.getInt("BigAlert.Title.FadeOut");
			
			title(in, stay, out, tit, stit);
		}
	}
	
	void chat(String message){
		for(BungeeUser user : BungeeUtilisals.getInstance().getUsers()){
			user.sendMessage(BungeeUtilisals.getInstance().getConfig().getString("Prefix") + message.replace("%p%", user.getName()).replaceAll("%n", " "));
		}
	}
	
	void bar(String message){
		for(BungeeUser user : BungeeUtilisals.getInstance().getUsers()) user.sendBar(message.replaceAll("%n", " "));
	}
	
	void title(Integer in, Integer stay, Integer out, String title, String subtitle){
		for(BungeeUser user : BungeeUtilisals.getInstance().getUsers()) user.sendTitle(in, stay, out, title, subtitle);
	}
}