package com.dbsoftware.bungeeutilisals.bungee.utils;

import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.TextComponent;

public class Utils {
	
	public static BaseComponent[] format(String s){
		s = s.replace("&", "§");
		
		return TextComponent.fromLegacyText(s);
	}
}
