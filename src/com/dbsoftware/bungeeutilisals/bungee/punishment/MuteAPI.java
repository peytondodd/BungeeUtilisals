package com.dbsoftware.bungeeutilisals.bungee.punishment;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.dbsoftware.bungeeutilisals.bungee.BungeeUtilisals;
import com.dbsoftware.bungeeutilisals.bungee.managers.DatabaseManager;

public class MuteAPI {
	
	private static DatabaseManager dbmanager = BungeeUtilisals.getInstance().getDatabaseManager();
	
	public static boolean isMuted(String player){
		String reason = null;
		try {
			Statement st = dbmanager.getConnection().createStatement();
			ResultSet rs = null;
			rs = st.executeQuery("SELECT * FROM Mutes WHERE Muted='" + player + "'");
			while(rs.next()){
				reason = rs.getString("Reason");
			}
		} catch (SQLException e){
			e.printStackTrace();
		}
		return (reason != null && !reason.isEmpty());
	}
	
	public static List<String> getMutes(){
		List<String> list = new ArrayList<String>();
		try {
		    PreparedStatement preparedStatement = dbmanager.getConnection().prepareStatement("SELECT `Muted` FROM `Mutes`;");
			
		    ResultSet rs = preparedStatement.executeQuery();
		    while(rs.next()){
		    	list.add(rs.getString("Muted"));
		    }
		} catch (SQLException e){
			BungeeUtilisals.getInstance().getLogger().info("An error occured while connecting to the database!" + e.getMessage());
		}
		return list;
	}
	
	public static void addMute(String muted_by, String muted, Long mute_time, String reason){
		try {
			Statement st = dbmanager.getConnection().createStatement();
			st.executeUpdate("INSERT INTO Mutes(MutedBy, Muted, MuteTime, Reason) VALUES ('" + muted_by + "', '" + muted + "', '" + mute_time + "', '" + reason + "')");
			
			Punishments.mutes.put(muted, new MuteInfo(muted, muted_by, mute_time, reason));
		} catch (SQLException e) {
			System.out.println("[BungeeUtilisals]: Can't add Mute for: " + muted + "(Muted by: " + muted_by + ", " + e.getMessage());
		}
	}
	
	public static void removeMute(String nplayer){
		try {
			Statement st = dbmanager.getConnection().createStatement();			
			st.executeUpdate("DELETE FROM Mutes WHERE Muted='" + nplayer + "'");
			
			Punishments.mutes.remove(nplayer);
		} catch (SQLException e) {
			System.out.println("[BungeeUtilisals]: Can't remove mute nplayer " + nplayer + ", " + e.getMessage());
		}
	}
	
	public static String getMutedBy(String nplayer){
		String playername = "";
		try {
			Statement st = dbmanager.getConnection().createStatement();
			ResultSet rs = null;
			rs = st.executeQuery("SELECT * FROM Mutes WHERE Muted='" + nplayer + "'");
			while(rs.next()){
				playername = rs.getString("MutedBy");
			}
		} catch (SQLException e){
			e.printStackTrace();
		}
		return playername;
	}
	
	public static String getMuted(String nplayer){
		String playername = "";
		try {
			Statement st = dbmanager.getConnection().createStatement();
			ResultSet rs = null;
			rs = st.executeQuery("SELECT * FROM Mutes WHERE Muted='" + nplayer + "'");
			while(rs.next()){
				playername = rs.getString("Muted");
			}
		} catch (SQLException e){
			e.printStackTrace();
		}
		return playername;
	}
	
	public static Long getMuteTime(String nplayer){
		long bantime = -1;
		try {
			Statement st = dbmanager.getConnection().createStatement();
			ResultSet rs = null;
			rs = st.executeQuery("SELECT * FROM Mutes WHERE Muted ='" + nplayer + "'");
			while(rs.next()){
				bantime = rs.getLong("MuteTime");
			}
		} catch(SQLException e){
			e.printStackTrace();
		}
		return bantime;
	}
	
	public static String getReason(String nplayer){
		String reason = "";
		try {
			Statement st = dbmanager.getConnection().createStatement();
			ResultSet rs = null;
			rs = st.executeQuery("SELECT * FROM Mutes WHERE Muted='" + nplayer + "'");
			while(rs.next()){
				reason = rs.getString("Reason");
			}
		} catch (SQLException e){
			e.printStackTrace();
		}
		return reason;
	}
	

}
