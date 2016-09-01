package com.dbsoftware.bungeeutilisals.bungee.listener;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import com.dbsoftware.bungeeutilisals.bungee.BungeeUtilisals;
import com.dbsoftware.bungeeutilisals.bungee.utils.Utils;

import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.ChatEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

public class MessageLimiter implements Listener {

	  public BungeeUtilisals plugin;
	  
	  public MessageLimiter(BungeeUtilisals plugin) {
	    this.plugin = plugin;
	  }
	  
	  public static Map<ProxiedPlayer, Integer> messagelimiter = new HashMap<ProxiedPlayer, Integer>();
	  
	  @EventHandler
	  public void Messagelimiter(ChatEvent event){
		  final ProxiedPlayer p = (ProxiedPlayer)event.getSender();
		  if(event.isCommand()){
			  return;
		  }
		  if(plugin.getConfig().getBoolean("MessageLimiter.Enabled")){
			  int max = plugin.getConfig().getInt("MessageLimiter.Max");
			  int time = plugin.getConfig().getInt("MessageLimiter.Time");
			  if(messagelimiter.containsKey(p)){
				  int got = messagelimiter.get(p);
				  if(got >= max){
					  event.setCancelled(true);
					  p.sendMessage(Utils.format(plugin.getConfig().getString("MessageLimiter.Message").replace("%player%", p.getName())));
				  } else {
					  messagelimiter.put(p, got + 1);
				  }
			  } else {
				 messagelimiter.put(p, 0);
				 
		    	    ProxyServer.getInstance().getScheduler().schedule(BungeeUtilisals.getInstance(), new Runnable(){
						public void run() {
			    		  	messagelimiter.remove(p);
						}
		    	    }, time, TimeUnit.SECONDS);
			  }
		  }
	  }

	  
	  
}
