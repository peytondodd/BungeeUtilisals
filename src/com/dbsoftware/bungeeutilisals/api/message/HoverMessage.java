package com.dbsoftware.bungeeutilisals.api.message;

import java.util.List;

import lombok.Data;

@Data
public class HoverMessage {
	
	String message;
	List<String> hover;
	
	
	public HoverMessage(String m, List<String> h){
		message = m;
		hover = h;
	}
}