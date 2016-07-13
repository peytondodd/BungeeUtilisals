package com.dbsoftware.bungeeutilisals.bungee.staffchat;

import com.dbsoftware.bungeeutilisals.bungee.BungeeUtilisals;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.ChatEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

public class StaffChatEvent implements Listener {
	
	public BungeeUtilisals plugin;
	  
	public StaffChatEvent(BungeeUtilisals plugin){
		this.plugin = plugin;
	}
	  
	@EventHandler
	public void onStaffChat(ChatEvent event){
		if (event.getSender() instanceof ProxiedPlayer) {
			ProxiedPlayer p = (ProxiedPlayer)event.getSender();
			if(StaffChat.inchat.contains(p.getName()) && !event.isCommand()){
				event.setCancelled(true);
				for(ProxiedPlayer pl : BungeeUtilisals.instance.getProxy().getPlayers()){
					if(StaffChat.inchat.contains(pl.getName())){
						BaseComponent[] message = TextComponent.fromLegacyText(BungeeUtilisals.instance.getConfig().getString("StaffChat.Format")
								.replace("&", "§")
								.replace("%message%", event.getMessage())
								.replace("%player%", p.getName())
								.replace("%server%", p.getServer().getInfo().getName()));
						pl.sendMessage(message);
					}
				}
			}
		}
	}
}