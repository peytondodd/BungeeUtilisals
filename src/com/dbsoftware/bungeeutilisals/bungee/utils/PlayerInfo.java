package com.dbsoftware.bungeeutilisals.bungee.utils;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import com.dbsoftware.bungeeutilisals.bungee.BungeeUtilisals;
import com.dbsoftware.bungeeutilisals.bungee.managers.DatabaseManager;

import net.md_5.bungee.api.ProxyServer;

public class PlayerInfo {

	DatabaseManager dbmanager = BungeeUtilisals.getInstance().getDatabaseManager();
	String player;
	
	public PlayerInfo(String player){
		this.player = player;
	}
	
	public boolean isInTable(){
		try {
			Statement statement = dbmanager.getConnection().createStatement();
			ResultSet result = statement.executeQuery("SELECT * FROM PlayerInfo WHERE Player='" + this.player + "'");
			return result.next();
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}
		
	public void putPlayerInTable(String ip, int bans, int warns, int mutes, int kicks){
		if(this.isInTable()){
			this.removePlayer();
		}
		try {
			Statement st = dbmanager.getConnection().createStatement();
			st.executeUpdate("INSERT INTO PlayerInfo(Player, IP, Bans, Warns, Mutes, Kicks) VALUES ('" + this.player + "', '" + ip + "', '" + bans + "', '" + warns + "', '" + mutes + "', '" + kicks + "')");		
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
		
	public void removePlayer(){
		try {
			Statement st = dbmanager.getConnection().createStatement();			
			st.executeUpdate("DELETE FROM PlayerInfo WHERE Player='" + this.player + "'");
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public String getIP(){
		String ip = null;
		if(ProxyServer.getInstance().getPlayer(player) != null){
			return Utils.getAddress(ProxyServer.getInstance().getPlayer(player).getAddress());
		}
		if(this.player.contains(".")){
			return this.player;
		}
		try {
			Statement st = dbmanager.getConnection().createStatement();
			ResultSet rs = st.executeQuery("SELECT * FROM PlayerInfo WHERE Player='" + this.player + "'");
			while(rs.next()){
				ip = rs.getString("IP");
			}
		} catch(SQLException e){
			e.printStackTrace();
		}
		return ip;
	}
	
	public Integer getBans(){
		int bans = 0;
		try {
			Statement st = dbmanager.getConnection().createStatement();
			ResultSet rs = st.executeQuery("SELECT * FROM PlayerInfo WHERE Player='" + this.player + "'");
			while(rs.next()){
				bans = rs.getInt("Bans");
			}
		} catch(SQLException e){
			e.printStackTrace();
		}
		return bans;
	}
	
	public boolean setBans(int bans){
		try {
			Statement st = dbmanager.getConnection().createStatement();
			st.executeUpdate("INSERT INTO PlayerInfo(Player, IP, Bans, Warns, Mutes, Kicks) VALUES ('" + this.player + "', '" + this.getIP() + "', '" + bans + "', '" + this.getWarns() + "', '" + this.getMutes() + "', '" + this.getKicks() + "')");		
			return true;
		} catch(SQLException e){
			e.printStackTrace();
			return false;
		}
	}
	
	public boolean addBan(){
		return this.setBans(this.getBans() + 1);
	}
	
	public Integer getMutes(){
		int mutes = 0;
		try {
			Statement st = dbmanager.getConnection().createStatement();
			ResultSet rs = st.executeQuery("SELECT * FROM PlayerInfo WHERE Player='" + this.player + "'");
			while(rs.next()){
				mutes = rs.getInt("Mutes");
			}
		} catch(SQLException e){
			e.printStackTrace();
		}
		return mutes;
	}
	
	public boolean setMutes(int mutes){
		try {
			Statement st = dbmanager.getConnection().createStatement();
			st.executeUpdate("INSERT INTO PlayerInfo(Player, IP, Bans, Warns, Mutes, Kicks) VALUES ('" + this.player + "', '" + this.getIP() + "', '" + this.getBans() + "', '" + this.getWarns() + "', '" + mutes + "', '" + this.getKicks() + "')");		
			return true;
		} catch(SQLException e){
			e.printStackTrace();
			return false;
		}
	}
	
	public boolean addMute(){
		return this.setMutes(this.getMutes() + 1);
	}
	
	public Integer getWarns(){
		int warns = 0;
		try {
			Statement st = dbmanager.getConnection().createStatement();
			ResultSet rs = st.executeQuery("SELECT * FROM PlayerInfo WHERE Player='" + this.player + "'");
			while(rs.next()){
				warns = rs.getInt("Warns");
			}
		} catch(SQLException e){
			e.printStackTrace();
		}
		return warns;
	}
	
	public boolean setWarns(int warns){
		try {
			Statement st = dbmanager.getConnection().createStatement();
			st.executeUpdate("INSERT INTO PlayerInfo(Player, IP, Bans, Warns, Mutes, Kicks) VALUES ('" + this.player + "', '" + this.getIP() + "', '" + this.getBans() + "', '" + warns + "', '" + this.getMutes() + "', '" + this.getKicks() + "')");		
			return true;
		} catch(SQLException e){
			e.printStackTrace();
			return false;
		}
	}
	
	public boolean addWarn(){
		return this.setWarns(this.getWarns() + 1);
	}
	
	public Integer getKicks(){
		int kicks = 0;
		try {
			Statement st = dbmanager.getConnection().createStatement();
			ResultSet rs = st.executeQuery("SELECT * FROM PlayerInfo WHERE Player='" + this.player + "'");
			while(rs.next()){
				kicks = rs.getInt("Kicks");
			}
		} catch(SQLException e){
			e.printStackTrace();
		}
		return kicks;
	}
	
	public boolean setKicks(int kicks){
		try {
			Statement st = dbmanager.getConnection().createStatement();
			st.executeUpdate("INSERT INTO PlayerInfo(Player, IP, Bans, Warns, Mutes, Kicks) VALUES ('" + this.player + "', '" + this.getIP() + "', '" + this.getBans() + "', '" + this.getWarns() + "', '" + this.getMutes() + "', '" + kicks + "')");		
			return true;
		} catch(SQLException e){
			e.printStackTrace();
			return false;
		}
	}
	
	public boolean addKick(){
		return this.setKicks(this.getKicks() + 1);
	}
}