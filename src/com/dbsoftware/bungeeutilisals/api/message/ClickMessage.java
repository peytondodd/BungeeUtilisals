package com.dbsoftware.bungeeutilisals.api.message;

import lombok.Data;

@Data
public class ClickMessage {
	
	String message;
	String click;
	String action;
	
	public ClickMessage(String m, String c, String a){
		message = m;
		click = c;
		action = a;
	}
}