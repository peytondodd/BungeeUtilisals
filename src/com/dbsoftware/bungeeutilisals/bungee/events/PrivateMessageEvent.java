package com.dbsoftware.bungeeutilisals.bungee.events;

import org.bukkit.event.HandlerList;
import com.dbsoftware.bungeeutilisals.bungee.BungeeUser;
import net.md_5.bungee.api.plugin.Cancellable;
import net.md_5.bungee.api.plugin.Event;

public class PrivateMessageEvent extends Event implements Cancellable {

    private BungeeUser sender, receiver;
    private String message;
    private static final HandlerList handlers = new HandlerList();
    private Boolean cancelled = false;

    public PrivateMessageEvent(BungeeUser sender, BungeeUser receiver, String message){
        this.sender = sender;
        this.receiver = receiver;
        this.message = message;
    }

    public BungeeUser getSender(){
        return this.sender;
    }

    public BungeeUser getReceiver(){
        return this.receiver;
    }

    public String getMessage(){
        return this.message;
    }

    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

    public void setCancelled(boolean cancel){
        this.cancelled = cancel;
    }

    public boolean isCancelled() {
        return cancelled;
    }
}
