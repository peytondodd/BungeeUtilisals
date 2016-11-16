package com.dbsoftware.bungeeutilisals.api.placeholder;

import com.dbsoftware.bungeeutilisals.bungee.user.BungeeUser;
import com.dbsoftware.bungeeutilisals.bungee.utils.Utils;

public class PlaceHolder {
	
	String placeholder;
	PlaceHolderReplaceEventHandler event;
	
	public PlaceHolder(String placeholder, PlaceHolderReplaceEventHandler event){
		this.placeholder = placeholder;
		this.event = event;
	}
	
	public String getPlaceHolder(){
		return placeholder;
	}
	
	public PlaceHolderReplaceEventHandler getEventHandler(){
		return event;
	}
	
	public String format(BungeeUser user, String message){
		String placeholder = this.getPlaceHolder();
		String replace = this.getEventHandler().getReplace(new PlaceHolderReplaceEvent(user, this));
		return message.replace(placeholder, Utils.c(replace));
	}
}