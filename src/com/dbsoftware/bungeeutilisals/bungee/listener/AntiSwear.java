package com.dbsoftware.bungeeutilisals.bungee.listener;

import com.dbsoftware.bungeeutilisals.bungee.BungeeUser;
import com.dbsoftware.bungeeutilisals.bungee.BungeeUtilisals;
import com.dbsoftware.bungeeutilisals.bungee.utils.Utils;

import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.ChatEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;
import net.md_5.bungee.event.EventPriority;

public class AntiSwear implements Listener {

	@EventHandler(priority = EventPriority.HIGH)
	public void Anticurse(ChatEvent event) {
		ProxiedPlayer p = (ProxiedPlayer) event.getSender();
		Boolean foundSwear = false;
		String msg = event.getMessage().toLowerCase();
		BaseComponent[] antiswear = Utils.format(BungeeUtilisals.getInstance().getConfig().getString("AntiSwear.Message"));
		if (BungeeUtilisals.getInstance().getConfig().getBoolean("AntiSwear.Enabled")) {
			if (event.getMessage().startsWith("/ban") || event.getMessage().startsWith("/banip")
					|| event.getMessage().startsWith("/checkban") || event.getMessage().startsWith("/kick")
					|| event.getMessage().startsWith("/mute") || event.getMessage().startsWith("/pinfo")
					|| event.getMessage().startsWith("/playerinfo") || event.getMessage().startsWith("/tempban")
					|| event.getMessage().startsWith("/unban") || event.getMessage().startsWith("/tempmute")
					|| event.getMessage().startsWith("/warn")) {
				return;
			}
			for (String word : BungeeUtilisals.getInstance().getConfig().getStringList("AntiSwear.Blocked")) {
				if (BungeeUtilisals.getInstance().getConfig().getBoolean("AntiSwear.Advanced")) {
					if ((msg.contains(word.toLowerCase())) || (msg.replace(" ", "").replace("*", "").replace("?", "").replace(".", "").replace(",", "").replace(";", "").replace(":", "").replace("/", "").replace("-", "").replace("_", "").contains(word.toLowerCase()))) {
						String replacewith = BungeeUtilisals.getInstance().getConfig().getString("AntiSwear.Replace_With");
						if (!replacewith.isEmpty()) {
							event.setMessage(event.getMessage().replace(word, replacewith));
							p.sendMessage(antiswear);
							foundSwear = true;
						} else {
							event.setCancelled(true);
							p.sendMessage(antiswear);
							foundSwear = true;
						}
					}
					continue;
				} else {
					String replacewith = BungeeUtilisals.getInstance().getConfig().getString("AntiSwear.Replace_With");
					if (!event.getMessage().contains(" ")) {
						if (msg.toLowerCase().equalsIgnoreCase(word.toLowerCase())) {
							if (!replacewith.isEmpty()) {
								event.setMessage(event.getMessage().toLowerCase().replace(word.toLowerCase(), replacewith));
								p.sendMessage(antiswear);
								foundSwear = true;
							} else {
								event.setCancelled(true);
								p.sendMessage(antiswear);
								foundSwear = true;
							}
						}
						continue;
					}
					Boolean b = false;
					for (int i = 0; i < (msg.split(" ").length); i++) {
						String words = msg.split(" ")[i];
						if (words.toLowerCase().equalsIgnoreCase(word.toLowerCase())) {
							b = true;
							foundSwear = true;
							break;
						}
					}
					if (b) {
						if (!replacewith.isEmpty()) {
							event.setMessage(event.getMessage().toLowerCase().replace(word.toLowerCase(), replacewith));
						} else {
							event.setCancelled(true);
						}
						p.sendMessage(antiswear);
					}
				}
			}
		}
		if(foundSwear){
			for(BungeeUser user : BungeeUtilisals.getInstance().getStaff()){
				for(String s : BungeeUtilisals.getInstance().getConfig().getStringList("AntiSwear.StaffMessage")){
					user.sendMessage(s.replace("%name%", p.getName()).replace("%server%", p.getServer().getInfo().getName()).replace("%message%", event.getMessage()));
				}
			}	
		}
	}
}