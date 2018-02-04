package com.dbsoftware.bungeeutilisals.api.message;

import java.util.LinkedHashMap;
import java.util.List;

import com.dbsoftware.bungeeutilisals.bungee.user.BungeeUser;
import com.dbsoftware.bungeeutilisals.bungee.utils.Utils;
import com.google.common.base.Joiner;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import lombok.Getter;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;

/**
 * Created by Blancke on 4/12/2016.
 */
public class Message {
	
	@Getter List<HoverMessage> hover = Lists.newLinkedList();
	@Getter List<ClickMessage> click = Lists.newLinkedList();

    //@Getter private LinkedHashMap<String, List<String>> hover = Maps.newLinkedHashMap();
    //@Getter private LinkedHashMap<String, List<String>> click = Maps.newLinkedHashMap();
    @Getter private LinkedHashMap<String, ChatColor> colors = Maps.newLinkedHashMap();

    public void append(Boolean color, String message, List<String> hover, ChatColor... colors){
        String s = color ? Utils.c(message) : message;

        this.hover.add(new HoverMessage(s, hover));
        if(colors != null && colors.length > 0) this.colors.put(s, colors[0]);
    }
    
    public void append(String message, List<String> hover, String click, String clickaction){
    	String s = Utils.c(message);
    	this.hover.add(new HoverMessage(s, hover));
        this.click.add(new ClickMessage(s, click, clickaction));
    }
    
    public void append(String message, List<String> hover){
    	this.hover.add(new HoverMessage(Utils.c(message), hover));
    }
    
    public void newLine(){
    	hover.add(new HoverMessage("\n", null));
    }
    
    ClickMessage foundClick(String message){
    	for(ClickMessage c : click)
    		if(c.getMessage().equals(message))
    			return c;
    	return null;
    }
    
    public void sendMessage(BungeeUser user){
        TextComponent message = new TextComponent();
        
        hover.forEach(hover -> {
            TextComponent m = new TextComponent(hover.getMessage());
            if(hover.getHover() != null){
                m.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, getComponent(hover.getHover())));
            }
            ClickMessage cm = foundClick(hover.getMessage());
            if(cm != null){
            	m.setClickEvent(new ClickEvent(ClickEvent.Action.valueOf(cm.getAction()), cm.getClick()));
            }
            if(colors.containsKey(hover.getMessage())) m.setColor(colors.get(hover.getMessage()));
            message.addExtra(m);
        });

        user.sendMessage(message);
    }

    public BaseComponent[] getComponent(List<String> list){
        return new ComponentBuilder(Joiner.on("\n").join(list)).create();
    }

    public void sendMessage(Iterable<BungeeUser> users){
        users.forEach(this::sendMessage);
    }

    public void sendMessage(BungeeUser[] users){
        for(BungeeUser user : users) sendMessage(user);
    }
}