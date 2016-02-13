package com.dbsoftware.bungeeutilisals.bungee.punishment;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.dbsoftware.bungeeutilisals.bungee.BungeeUtilisals;
import com.dbsoftware.bungeeutilisals.bungee.managers.DatabaseManager;

public class MuteAPI {
	
	private static DatabaseManager dbmanager = BungeeUtilisals.getDatabaseManager();
	
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
	
	public static Integer getMuteNumber(String player){
		int number = 0;
		try {
			Statement st = dbmanager.getConnection().createStatement();
			ResultSet rs = null;
			rs = st.executeQuery("SELECT * FROM Mutes WHERE Muted='" + player + "'");
			while(rs.next()){
				number = rs.getInt("Number");
			}
		} catch (SQLException e){
			e.printStackTrace();
		}
		return number;
	}
	
	public static void addMute(int number, String muted_by, String muted, Long mute_time, String reason){
		try {
			Statement st = dbmanager.getConnection().createStatement();
			st.executeUpdate("INSERT INTO Mutes(Number, MutedBy, Muted, MuteTime, Reason) VALUES ('" + number + "', '" + muted_by + "', '" + muted + "', '" + mute_time + "', '" + reason + "')");
		} catch (SQLException e) {
			System.out.println("[BungeeUtilisals]: Can't add Mute for: " + muted + "(Muted by: " + muted_by + ", " + e.getMessage());
		}
	}
	
	public static void removeMute(int number){
		try {
			Statement st = dbmanager.getConnection().createStatement();			
			st.executeUpdate("DELETE FROM Mutes WHERE Number='" + number + "'");
		} catch (SQLException e) {
			System.out.println("[BungeeUtilisals]: Can't remove mute number " + number + ", " + e.getMessage());
		}
	}
	
	public static List<Integer> getMuteNumbers(){
	    List<Integer> reports = new ArrayList<Integer>();
	    try {
	      Statement st = dbmanager.getConnection().createStatement();
	      ResultSet rs = null;
	      rs = st.executeQuery("SELECT * FROM Mutes");
	      while (rs.next()) {
	    	int reportnumber = rs.getInt("Number");
	    	reports.add(reportnumber);
	      }
	    } catch (SQLException e) {
	    	System.out.println("[BungeeUtilisals]: An error occured while contacting the Database! " + e.getMessage());
	    }
	    return reports;
	}
	
	public static String getMutedBy(int number){
		String playername = "";
		try {
			Statement st = dbmanager.getConnection().createStatement();
			ResultSet rs = null;
			rs = st.executeQuery("SELECT * FROM Mutes WHERE Number='" + number + "'");
			while(rs.next()){
				playername = rs.getString("MutedBy");
			}
		} catch (SQLException e){
			e.printStackTrace();
		}
		return playername;
	}
	
	public static String getMuted(int number){
		String playername = "";
		try {
			Statement st = dbmanager.getConnection().createStatement();
			ResultSet rs = null;
			rs = st.executeQuery("SELECT * FROM Mutes WHERE Number='" + number + "'");
			while(rs.next()){
				playername = rs.getString("Muted");
			}
		} catch (SQLException e){
			e.printStackTrace();
		}
		return playername;
	}
	
	public static Long getMuteTime(int number){
		long bantime = -1;
		try {
			Statement st = dbmanager.getConnection().createStatement();
			ResultSet rs = null;
			rs = st.executeQuery("SELECT * FROM Mutes WHERE Number ='" + number + "'");
			while(rs.next()){
				bantime = rs.getLong("MuteTime");
			}
		} catch(SQLException e){
			e.printStackTrace();
		}
		return bantime;
	}
	
	public static String getReason(int number){
		String reason = "";
		try {
			Statement st = dbmanager.getConnection().createStatement();
			ResultSet rs = null;
			rs = st.executeQuery("SELECT * FROM Mutes WHERE Number='" + number + "'");
			while(rs.next()){
				reason = rs.getString("Reason");
			}
		} catch (SQLException e){
			e.printStackTrace();
		}
		return reason;
	}
	

}
