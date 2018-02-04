package com.dbsoftware.bungeeutilisals.bungee.utils;

import java.io.File;
import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.config.Configuration;
import net.md_5.bungee.config.ConfigurationProvider;
import net.md_5.bungee.config.YamlConfiguration;

public class Utils {
	
	public static BaseComponent[] format(String s){
		if(s == null) return null;
		return TextComponent.fromLegacyText(c(s));
	}
	
	public static String c(String s){
		if(s == null) return null;
		return ChatColor.translateAlternateColorCodes('&', s);
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
    
    public static void save(Configuration c, File f){
    	try {
			ConfigurationProvider.getProvider(YamlConfiguration.class).save(c, f);
		} catch (IOException e) {
			e.printStackTrace();
		}
    }
}
