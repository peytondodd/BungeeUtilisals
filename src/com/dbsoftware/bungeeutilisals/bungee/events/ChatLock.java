package com.dbsoftware.bungeeutilisals.bungee.events;

import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.ChatEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;
import com.dbsoftware.bungeeutilisals.bungee.BungeeUtilisals;

public class ChatLock implements Listener {

	public BungeeUtilisals plugin;
	  
	public ChatLock(BungeeUtilisals plugin){
		this.plugin = plugin;
	}
	  
	private BungeeUtilisals instance = BungeeUtilisals.getInstance();
	  
	@EventHandler
	public void Chatlock(ChatEvent event){
		ProxiedPlayer p = (ProxiedPlayer)event.getSender();
		if (isChatMuted()){
			if(p.hasPermission("butilisals.talkinlock") || p.hasPermission("butilisals.*")){
				return;
			}
			if (!event.getMessage().startsWith("/")){
				p.sendMessage(new TextComponent(plugin.getConfig().getString("ChatLock.Locked").replace("&", "§")));
				event.setCancelled(true);
			}
		}
	}
	
	private boolean isChatMuted() {
		return instance.chatMuted;
	}
}

