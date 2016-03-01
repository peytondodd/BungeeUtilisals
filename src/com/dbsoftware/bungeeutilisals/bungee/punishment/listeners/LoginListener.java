package com.dbsoftware.bungeeutilisals.bungee.punishment.listeners;

import java.text.SimpleDateFormat;
import java.util.Date;
import com.dbsoftware.bungeeutilisals.bungee.punishment.BanAPI;
import com.dbsoftware.bungeeutilisals.bungee.punishment.Punishments;
import com.dbsoftware.bungeeutilisals.bungee.utils.PlayerInfo;
import net.md_5.bungee.api.event.LoginEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

public class LoginListener implements Listener {
	
	@EventHandler
	public void onLogin(final LoginEvent event){
		PlayerInfo info = new PlayerInfo(event.getConnection().getName());
		if(!info.isInTable()){
			info.putPlayerInTable(event.getConnection().getAddress().getHostName().replace("localhost", "127.0.0.1"), 0, 0, 0, 0);
		}
		
		String IP = event.getConnection().getVirtualHost().getHostName();
		if(BanAPI.isIPBanned(IP)){
			int number = BanAPI.getIPBanNumber(IP);
			String reason = "";
			for(String s : Punishments.punishments.getFile().getStringList("Punishments.BanIP.Messages.KickMessage")){
				reason = reason + "\n" + s;
			}
			
			event.setCancelled(true);
			event.setCancelReason(reason.replace("%banner%", BanAPI.getIPBannedBy(number)).replace("%unbantime%", "Never").replace("%reason%", BanAPI.getIPReason(number)).replaceAll("&", "§"));
			return;
		}
		if(BanAPI.isBanned(event.getConnection().getName())){
			int number = BanAPI.getBanNumber(event.getConnection().getName());
			Long time = BanAPI.getBanTime(number);
			if(time < System.currentTimeMillis() && time != -1){
				BanAPI.removeBan(number);
				return;
			}
			if(time != -1){
				String reason = "";
				for(String s : Punishments.punishments.getFile().getStringList("Punishments.Tempban.Messages.KickMessage")){
					reason = reason + "\n" + s;
					SimpleDateFormat df2 = new SimpleDateFormat("kk:mm dd/MM/yyyy");
					String date = df2.format(new Date(time));
					
					event.setCancelled(true);
					event.setCancelReason(reason.replace("%banner%", BanAPI.getBannedBy(number)).replace("%unbantime%", (time == -1L ? "Never" : date)).replace("%reason%", BanAPI.getReason(number)).replaceAll("&", "§"));
				}
				return;
			}
			String reason = "";
			for(String s : Punishments.punishments.getFile().getStringList("Punishments.Ban.Messages.KickMessage")){
				reason = reason + "\n" + s;
			}
			SimpleDateFormat df2 = new SimpleDateFormat("kk:mm dd/MM/yyyy");
			String date = df2.format(new Date(time));
			
			event.setCancelled(true);
			event.setCancelReason(reason.replace("%banner%", BanAPI.getBannedBy(number)).replace("%unbantime%", (time == -1L ? "Never" : date)).replace("%reason%", BanAPI.getReason(number)).replaceAll("&", "§"));
		}
	}
}
