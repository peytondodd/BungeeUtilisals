package com.dbsoftware.bungeeutilisals.bungee;

import com.dbsoftware.bungeeutilisals.bungee.utils.MySQL;
import com.dbsoftware.bungeeutilisals.bungee.utils.MySQL.WhereType;
import com.dbsoftware.bungeeutilisals.bungee.utils.Utils;

import net.md_5.bungee.BungeeCord;
import net.md_5.bungee.api.connection.ProxiedPlayer;

public class BungeeUser {
	
	ProxiedPlayer p;
	BungeeRank rank;
	Boolean socialspy,localspy;
	
	public BungeeUser(ProxiedPlayer p){
		this.p = p;
		if(BungeeUtilisals.getInstance().staff.contains(p.getName().toLowerCase())){
			this.rank = BungeeRank.STAFF;
		} else {
			this.rank = BungeeRank.GUEST;
		}
		if(this.getRank().equals(BungeeRank.STAFF)){
			this.socialspy = true;
		} else {
			this.socialspy = false;
		}
		this.localspy = false;
	}
	
	public void sendMessage(String message){
		this.getPlayer().sendMessage(Utils.format(message));
	}
	
	public ProxiedPlayer getPlayer(){
		return p;
	}
	
	public void setPlayer(ProxiedPlayer p){
		this.p = p;
	}
	
	public Boolean isSocialSpy(){
		return socialspy;
	}
	
	public void setSocialSpy(Boolean b){
		this.socialspy = b;
	}
	
	public Boolean isLocalSpy(){
		return localspy;
	}
	
	public void setLocalSpy(Boolean b){
		this.localspy = b;
	}
	
	public Boolean isStaff(){
		return rank.equals(BungeeRank.STAFF);
	}
	
	public static void setRank(final String name, final BungeeRank rank){
		Runnable r = new Runnable(){

			@Override
			public void run() {
				try {
					if(rank.equals(BungeeRank.STAFF)){
						if(!MySQL.getInstance().select().table("Staffs").column("Name").wheretype(WhereType.EQUALS).where("Name").wherereq(name.toLowerCase()).select().next()){
							MySQL.getInstance().insert().single().table("Staffs").column("Name").value(name.toLowerCase()).insert();
						}
					} else {
						if(MySQL.getInstance().select().table("Staffs").column("Name").wheretype(WhereType.EQUALS).where("Name").wherereq(name.toLowerCase()).select().next()){
							MySQL.getInstance().delete().table("Staffs").where("Name").equals(name.toLowerCase()).delete();
						}
					}
				} catch(Exception e){
					e.printStackTrace();
				}
			}
			
		};
		BungeeCord.getInstance().getScheduler().runAsync(BungeeUtilisals.getInstance(), r);
	}
	
	public BungeeRank getRank(){
		return rank;
	}
}
