package com.dbsoftware.bungeeutilisals.bungee.listener;

import com.dbsoftware.bungeeutilisals.bungee.BungeeUtilisals;
import com.dbsoftware.bungeeutilisals.bungee.utils.Utils;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.ChatEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;
import net.md_5.bungee.event.EventPriority;

public class AntiSwear implements Listener {

	@EventHandler(priority = EventPriority.HIGH)
	public void Anticurse(ChatEvent event){
		ProxiedPlayer p = (ProxiedPlayer)event.getSender();
		String msg = event.getMessage().toLowerCase();
		if(BungeeUtilisals.getInstance().getConfig().getBoolean("AntiSwear.Enabled")){
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
			if(msg.startsWith("/ban") || msg.startsWith("/mute") || msg.startsWith("/tempmute") || msg.startsWith("/tempban") || msg.startsWith("/kick") || msg.startsWith("/tp")){
				return;
			} else {
				for (String word : BungeeUtilisals.getInstance().getConfig().getStringList("AntiSwear.Blocked")) {
					if(BungeeUtilisals.getInstance().getConfig().getBoolean("AntiSwear.Advanced")){
						if ((msg.contains(word.toLowerCase())) || (msg.replace(" ", "").replace("*", "").replace("?", "").replace(".", "").replace(",", "").replace(";", "").replace(":", "").replace("/", "").replace("-", "").replace("_", "").contains(word.toLowerCase()))){
							String replacewith = BungeeUtilisals.getInstance().getConfig().getString("AntiSwear.Replace_With");
							if(!replacewith.isEmpty()){
								event.setMessage(event.getMessage().replace(word, replacewith));
								p.sendMessage(new TextComponent(BungeeUtilisals.getInstance().getConfig().getString("AntiSwear.Message").replace("&", "§")));
							} else {
								event.setCancelled(true);
								p.sendMessage(new TextComponent(BungeeUtilisals.getInstance().getConfig().getString("AntiSwear.Message").replace("&", "§")));
							}
						}
						continue;
					} else {
						String replacewith = BungeeUtilisals.getInstance().getConfig().getString("AntiSwear.Replace_With");
						if(!event.getMessage().contains(" ")){
							if(msg.toLowerCase().equalsIgnoreCase(word.toLowerCase())){
								if(!replacewith.isEmpty()){
									event.setMessage(event.getMessage().toLowerCase().replace(word.toLowerCase(), replacewith));
									p.sendMessage(new TextComponent(BungeeUtilisals.getInstance().getConfig().getString("AntiSwear.Message").replace("&", "§")));
								} else {
									event.setCancelled(true);
									p.sendMessage(new TextComponent(BungeeUtilisals.getInstance().getConfig().getString("AntiSwear.Message").replace("&", "§")));
								}
							}
							continue;
						}
						Boolean b = false;
						for(int i = 0; i < (msg.split(" ").length); i++){
							String words = msg.split(" ")[i];
							if(words.toLowerCase().equalsIgnoreCase(word.toLowerCase())){
								b = true;
								if(!replacewith.isEmpty()){
									event.setMessage(event.getMessage().toLowerCase().replace(word.toLowerCase(), replacewith));
									p.sendMessage(new TextComponent(BungeeUtilisals.getInstance().getConfig().getString("AntiSwear.Message").replace("&", "§")));
								} else {
									event.setCancelled(true);
									p.sendMessage(new TextComponent(BungeeUtilisals.getInstance().getConfig().getString("AntiSwear.Message").replace("&", "§")));
								}
							}
						}
						if(b){
							p.sendMessage(Utils.format(BungeeUtilisals.getInstance().getConfig().getString("AntiSwear.Message")));
						}
					}
				}
			}
		}
	}
}