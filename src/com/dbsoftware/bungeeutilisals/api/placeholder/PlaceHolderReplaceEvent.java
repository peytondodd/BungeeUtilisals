package com.dbsoftware.bungeeutilisals.api.placeholder;

import com.dbsoftware.bungeeutilisals.bungee.BungeeUser;

public class PlaceHolderReplaceEvent {

	BungeeUser user;
	PlaceHolder placeholder;

	public PlaceHolderReplaceEvent(BungeeUser user, PlaceHolder placeholder) {
		this.user = user;
		this.placeholder = placeholder;
	}

	public BungeeUser getUser() {
		return user;
	}

	public PlaceHolder getPlaceHolder() {
		return placeholder;
	}
}