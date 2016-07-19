package com.dbsoftware.bungeeutilisals.bungee;

import com.dbsoftware.bungeeutilisals.bungee.utils.Utils;

import net.md_5.bungee.api.connection.ProxiedPlayer;

public class BungeeUser {
	
	ProxiedPlayer p;
	Boolean socialspy,localspy;
	
	public BungeeUser(ProxiedPlayer p){
		this.p = p;
		this.socialspy = false;
		this.localspy = false;
	}
	
	public void sendMessage(String message){
		this.getPlayer().sendMessage(Utils.format(message));
	}
	
	public ProxiedPlayer getPlayer(){
		return p;
	}
	
	public void setPlayer(ProxiedPlayer p){
		this.p = p;
	}
	
	public Boolean isSocialSpy(){
		return socialspy;
	}
	
	public void setSocialSpy(Boolean b){
		this.socialspy = b;
	}
	
	public Boolean isLocalSpy(){
		return localspy;
	}
	
	public void setLocalSpy(Boolean b){
		this.localspy = b;
	}
	
}
