package com.dbsoftware.bungeeutilisals.bungee.punishment.listeners;

import java.text.SimpleDateFormat;
import java.util.Date;
import com.dbsoftware.bungeeutilisals.bungee.punishment.MuteAPI;
import com.dbsoftware.bungeeutilisals.bungee.punishment.Punishments;
import com.dbsoftware.bungeeutilisals.bungee.utils.Utils;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.ChatEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

public class ChatListener implements Listener {
	
	@EventHandler
	public void onChat(final ChatEvent event){
		if(!(event.getSender() instanceof ProxiedPlayer)){
			return;
		}
		if(event.isCommand()){
			boolean blockedcmd = false;
			for(String s : Punishments.punishments.getFile().getStringList("Punishments.Settings.Blocked-Cmds-While-Muted")){
				if(event.getMessage().startsWith(s)){
					blockedcmd = true;
				}
			}
			
			if(!blockedcmd){
				return;
			}
		}
		
		ProxiedPlayer p = (ProxiedPlayer)event.getSender();
		
		if(MuteAPI.isMuted(p.getName())){
			Long time = MuteAPI.getMuteTime(p.getName());
			if(time < System.currentTimeMillis() && time != -1){
				MuteAPI.removeMute(p.getName());
				return;
			}
			if(time != -1L){
				String reason = MuteAPI.getReason(p.getName());
				SimpleDateFormat df2 = new SimpleDateFormat("kk:mm dd/MM/yyyy");
				String date = df2.format(new Date(time));
				
				for(String s : Punishments.punishments.getFile().getStringList("Punishments.Tempmute.Messages.MuteMessage")){
					p.sendMessage(Utils.format(s.replace("%player%", MuteAPI.getMutedBy(p.getName())).replace("%reason%", reason).replace("%time%", date)));
				}
				event.setCancelled(true);
				return;
			} else {
				String reason = MuteAPI.getReason(p.getName());
				for(String s : Punishments.punishments.getFile().getStringList("Punishments.Mute.Messages.MuteMessage")){
					p.sendMessage(Utils.format(s.replace("%player%", MuteAPI.getMutedBy(p.getName())).replace("%reason%", reason)));
				}
				event.setCancelled(true);
				return;
			}
		}
	}
}
