package com.dbsoftware.bungeeutilisals.bungee.redisbungee.listeners;

import com.dbsoftware.bungeeutilisals.bungee.BungeeUtilisals;
import com.dbsoftware.bungeeutilisals.bungee.redisbungee.ChannelNames;
import com.dbsoftware.bungeeutilisals.bungee.staffchat.StaffChat;
import com.dbsoftware.bungeeutilisals.bungee.utils.ActionBarUtil;
import com.dbsoftware.bungeeutilisals.bungee.utils.TitleUtil;
import com.dbsoftware.bungeeutilisals.bungee.utils.Utils;
import com.imaginarycode.minecraft.redisbungee.events.PubSubMessageEvent;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

public class PubSubMessageListener implements Listener {

	@EventHandler
	public void onPubSubMessage(PubSubMessageEvent event){
		String channel = event.getChannel();
		if(channel.equals(ChannelNames.BU_ACTIONBAR.name)){
			String message = event.getMessage().replace("&", "§");
			for(ProxiedPlayer p : ProxyServer.getInstance().getPlayers()){
				ActionBarUtil.sendActionBar(p, message.replace("%p%", p.getName()));
			}
		} else if(channel.equals(ChannelNames.BU_TITLE.name)){
			String message = event.getMessage();
			if(message.contains("%n")){
				String[] titles = message.split("%n");
				String title = titles[0];
				String subtitle = titles[1];
				int fadeIn = BungeeUtilisals.getInstance().getConfig().getInt("BigAlert.Title.FadeIn");
				int stay = BungeeUtilisals.getInstance().getConfig().getInt("BigAlert.Title.Stay");
				int fadeOut = BungeeUtilisals.getInstance().getConfig().getInt("BigAlert.Title.FadeOut");
				
				for (ProxiedPlayer p : ProxyServer.getInstance().getPlayers()) {
					BaseComponent[] btitle = new ComponentBuilder(title.replace("&", "§").replace("%p%", p.getName())).create();
					BaseComponent[] stitle = new ComponentBuilder(subtitle.replace("&", "§").replace("%p%", p.getName())).create();
					
					TitleUtil.sendFullTitle(p, fadeIn, stay, fadeOut, stitle, btitle);
				}
				return;
			} else {
				int fadeIn = BungeeUtilisals.getInstance().getConfig().getInt("BigAlert.Title.FadeIn");
				int stay = BungeeUtilisals.getInstance().getConfig().getInt("BigAlert.Title.Stay");
				int fadeOut = BungeeUtilisals.getInstance().getConfig().getInt("BigAlert.Title.FadeOut");
				for (ProxiedPlayer p : ProxyServer.getInstance().getPlayers()) {
					
					BaseComponent[] btitle = new ComponentBuilder(message.replace("&", "§").replace("%p%", p.getName())).create();
					BaseComponent[] stitle = new ComponentBuilder("").create();
					
					TitleUtil.sendFullTitle(p, fadeIn, stay, fadeOut, stitle, btitle);
				}
				return;
			}
		} else if(channel.equals(ChannelNames.BU_CHAT.name)){
			for(ProxiedPlayer p : ProxyServer.getInstance().getPlayers()){
				BaseComponent[] message = Utils.format(event.getMessage().replace("%p%", p.getName()));
				p.sendMessage(message);
			}
		} else if(channel.equals(ChannelNames.BU_STAFFCHAT.name)){
			String message = event.getMessage();
			
			for(ProxiedPlayer p : ProxyServer.getInstance().getPlayers()){
				if(StaffChat.inchat.contains(p.getName())){
					p.sendMessage(Utils.format(message));
				}
			}
		}
	}
}