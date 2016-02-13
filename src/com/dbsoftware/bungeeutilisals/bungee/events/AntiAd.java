
package com.dbsoftware.bungeeutilisals.bungee.events;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.dbsoftware.bungeeutilisals.bungee.BungeeUtilisals;

import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.ChatEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;
import net.md_5.bungee.event.EventPriority;

public class AntiAd implements Listener {
	
	public BungeeUtilisals plugin;
	
	public AntiAd(BungeeUtilisals plugin) {
		this.plugin = plugin;
	}
	
	Pattern ipPattern = Pattern.compile("((?<![0-9])(?:(?:25[0-5]|2[0-4][0-9]|[0-1]?[0-9]{1,2})[ ]?[.,-:; ][ ]?(?:25[0-5]|2[0-4][0-9]|[0-1]?[0-9]{1,2})[ ]?[., ][ ]?(?:25[0-5]|2[0-4][0-9]|[0-1]?[0-9]{1,2})[ ]?[., ][ ]?(?:25[0-5]|2[0-4][0-9]|[0-1]?[0-9]{1,2}))(?![0-9]))");
	Pattern webPattern = Pattern.compile("(http://)|(https://)?(www)?\\S{2,}((\\.com)|(\\.ru)|(\\.net)|(\\.au)|(\\.org)|(\\.me)|(\\.bz)|(\\.co\\.uk)|(\\.tk)|"
			+ "(\\.info)|(\\.es)|(\\.de)|(\\.arpa)|(\\.edu)|(\\.earth)|(\\.ly)|(\\.li)|(\\.firm)|(\\.int)|(\\.mil)|(\\.mobi)|(\\.nato)|(\\.to)|(\\.fr)|(\\.ms)|"
			+ "(\\.vu)|(\\.eu)|(\\.nl)|(\\.us)|(\\.dk)|(\\.biz)|(\\,com)|(\\,ru)|(\\,net)|(\\,org)|(\\,me)|(\\,bz)|(\\,co\\,uk)|(\\,tk)|(\\,au)|(\\,earth)|(\\,info)|"
			+ "(\\,es)|(\\,de)|(\\,arpa)|(\\,edu)|(\\,ly)|(\\,li)|(\\,firm)|(\\,int)|(\\,mil)|(\\,mobi)|(\\,nato)|(\\,to)|(\\,fr)|(\\,ms)|(\\,vu)|(\\,eu)|(\\,nl)|"
			+ "(\\,us)|(\\,dk)|(\\,biz))");
	
	@EventHandler(priority = EventPriority.LOWEST)
	public void AntiAD(ChatEvent event){
		boolean found = false;
		ProxiedPlayer p = (ProxiedPlayer)event.getSender();
		if(BungeeUtilisals.instance.getConfig().getBoolean("AntiAd.Enabled")){
			if(event.getMessage().startsWith("/ban") ||
					event.getMessage().startsWith("/banip") ||
					event.getMessage().startsWith("/checkban") || 
					event.getMessage().startsWith("/kick") ||
					event.getMessage().startsWith("/mute") ||
					event.getMessage().startsWith("/pinfo") ||
					event.getMessage().startsWith("/playerinfo") ||
					event.getMessage().startsWith("/tempban") ||
					event.getMessage().startsWith("/unban") ||
					event.getMessage().startsWith("/tempmute") ||
					event.getMessage().startsWith("/warn")){
				return;
			}
			for(String whitelist : BungeeUtilisals.instance.getConfig().getStringList("AntiAd.Whitelist")){
				if(event.getMessage().contains(whitelist)){
					return;
				}
			}
			if(p.hasPermission("butilisals.antiad.bypass") || p.hasPermission("butilisals.*")){
				return;
			} else {
				String message = event.getMessage();
				for(String s : plugin.getConfig().getStringList("AntiAd.Replace-with-dot")){
					message = message.replace(s, ".");
				}
				for (String s : message.split(" ")){
					if (isIPorURL(s)) {
						found = true;
					}
				}
				if(found){
					found = false;
					p.sendMessage(new TextComponent(plugin.getConfig().getString("AntiAd.Message").replace("&", "§")));
					event.setCancelled(true);
					return;
				}
			}
		}
	}
	
	public boolean isIPorURL(String word){
		Matcher searchforips = ipPattern.matcher(word.toLowerCase());
		Matcher searchforweb = webPattern.matcher(word.toLowerCase());
		return (searchforips.find()) || (searchforweb.find());
	}
}