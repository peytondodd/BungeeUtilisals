package com.dbsoftware.bungeeutilisals.bungee.listener;

import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.ChatEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;
import com.dbsoftware.bungeeutilisals.bungee.BungeeUtilisals;

public class AntiCaps implements Listener {

	public BungeeUtilisals plugin;
	  
	public AntiCaps(BungeeUtilisals plugin){
		this.plugin = plugin;
	}
	  
	@EventHandler
	public void Anticaps(ChatEvent event){
		ProxiedPlayer p = (ProxiedPlayer)event.getSender();
		int max = BungeeUtilisals.getInstance().getConfig().getInt("AntiCaps.Max_Percentage");
		int min = BungeeUtilisals.getInstance().getConfig().getInt("AntiCaps.Min_Length");
		
		if(event.getMessage().startsWith("/") || p.hasPermission("butilisals.caps.bypass") || p.hasPermission("butilisals.*")){
			return;
		} else { 
			if(plugin.getConfig().getBoolean("AntiCaps.Enabled")){
				if ((event.getMessage().length() >= min) 
						&& (this.getUppercasePercentage(event.getMessage()) > max)){	 
					event.setMessage(event.getMessage().toLowerCase());
					p.sendMessage(new TextComponent(plugin.getConfig().getString("AntiCaps.Message").replace("&", "§")));
				}
			} else {
				return;
			}
		}
	}
	
	private double getUppercasePercentage(String string){
		double upperCase = 0.0D;
		for (int i = 0; i < string.length(); i++) {
			if (isUppercase(string.substring(i, i + 1))) {
				upperCase += 1.0D;
			}
		}
		return upperCase / string.length() * 100.0D;
	}
  
	private boolean isUppercase(String string) {
		return "ABCDEFGHIJKLMNOPQRSTUVWXYZ".contains(string);
	}
}