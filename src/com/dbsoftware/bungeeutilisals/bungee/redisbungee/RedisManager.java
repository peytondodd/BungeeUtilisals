package com.dbsoftware.bungeeutilisals.bungee.redisbungee;

import com.dbsoftware.bungeeutilisals.bungee.BungeeUtilisals;
import com.dbsoftware.bungeeutilisals.bungee.redisbungee.commands.Redisalert;
import com.dbsoftware.bungeeutilisals.bungee.redisbungee.commands.Redisbigalert;
import com.dbsoftware.bungeeutilisals.bungee.redisbungee.commands.Redisfind;
import com.dbsoftware.bungeeutilisals.bungee.redisbungee.commands.Redisglist;
import com.dbsoftware.bungeeutilisals.bungee.redisbungee.listeners.PubSubMessageListener;
import com.imaginarycode.minecraft.redisbungee.RedisBungee;
import com.imaginarycode.minecraft.redisbungee.RedisBungeeAPI;

import net.md_5.bungee.api.ProxyServer;

public class RedisManager {
	
	private RedisBungeeAPI redis;
	
	public RedisManager(){
		this.redis = RedisBungee.getApi();
		
		this.getRedis().registerPubSubChannels(ChannelNames.BU_ACTIONBAR.name);
		this.getRedis().registerPubSubChannels(ChannelNames.BU_CHAT.name);
		this.getRedis().registerPubSubChannels(ChannelNames.BU_TITLE.name);
		this.getRedis().registerPubSubChannels(ChannelNames.BU_STAFFCHAT.name);

		ProxyServer.getInstance().getPluginManager().registerCommand(BungeeUtilisals.getInstance(), new Redisalert());
		ProxyServer.getInstance().getPluginManager().registerCommand(BungeeUtilisals.getInstance(), new Redisbigalert());
		ProxyServer.getInstance().getPluginManager().registerCommand(BungeeUtilisals.getInstance(), new Redisfind());
		if(BungeeUtilisals.getInstance().getConfig().getBoolean("GList.Enabled")){
			ProxyServer.getInstance().getPluginManager().registerCommand(BungeeUtilisals.getInstance(), new Redisglist());
		}

		ProxyServer.getInstance().getPluginManager().registerListener(BungeeUtilisals.getInstance(), new PubSubMessageListener());
	}
	
	public RedisBungeeAPI getRedis(){
		return this.redis;
	}
}
