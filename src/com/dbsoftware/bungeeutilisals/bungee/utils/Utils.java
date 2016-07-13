package com.dbsoftware.bungeeutilisals.bungee.utils;

import java.net.InetAddress;
import java.net.InetSocketAddress;

import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.TextComponent;

public class Utils {
	
	public static BaseComponent[] format(String s){
		s = s.replace("&", "§");
		
		return TextComponent.fromLegacyText(s);
	}
	
    public static String getAddress(InetSocketAddress address){
    	return getAddress(address.getAddress());
    }

    public static String getAddress(InetAddress address){
        String sfullip = address.toString();
        String[] fullip;
        String[] ipandport;
        fullip = sfullip.split("/");
        String sIpandPort = fullip[1];
        ipandport = sIpandPort.split(":");
        String IP = ipandport[0];
        return IP;
    }
}
