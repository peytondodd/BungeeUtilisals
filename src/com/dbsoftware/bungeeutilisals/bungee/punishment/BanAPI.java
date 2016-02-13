package com.dbsoftware.bungeeutilisals.bungee.punishment;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import com.dbsoftware.bungeeutilisals.bungee.BungeeUtilisals;
import com.dbsoftware.bungeeutilisals.bungee.managers.DatabaseManager;

public class BanAPI {
	
	private static DatabaseManager dbmanager = BungeeUtilisals.getDatabaseManager();
		
	public static boolean isIPBanned(String IP){
		String reason = null;
		try {
			Statement st = dbmanager.getConnection().createStatement();
			ResultSet rs = st.executeQuery("SELECT * FROM IPBans WHERE Banned='" + IP + "'");
			while(rs.next()){
				reason = rs.getString("Reason");
			}
		} catch(SQLException e){
			e.printStackTrace();
		}
		
		return (reason != null && !reason.isEmpty());
	}
	
	public static Integer getIPBanNumber(String IP){
		int number = 0;
		try {
			Statement st = dbmanager.getConnection().createStatement();
			ResultSet rs = st.executeQuery("SELECT * FROM IPBans WHERE Banned='" + IP + "'");
			while(rs.next()){
				number = rs.getInt("Number");
			}
		} catch(SQLException e){
			e.printStackTrace();
		}
		return number;
	}
	
	public static void addIPBan(int number, String banned_by, String banned, String reason){
		try {
			Statement st = dbmanager.getConnection().createStatement();
			st.executeUpdate("INSERT INTO IPBans(Number, BannedBy, Banned, Reason) VALUES ('" + number + "', '" + banned_by + "', '" + banned + "', '" + reason + "')");
		} catch (SQLException e) {
			System.out.println("[BungeeUtilisals]: Can't add Ban of: " + banned + "(Banned by: " + banned_by + ", " + e.getMessage());
		}
	}
	
	public static void removeIPBan(int number){
		try {
			Statement st = dbmanager.getConnection().createStatement();			
			st.executeUpdate("DELETE FROM IPBans WHERE Number='" + number + "'");
		} catch (SQLException e) {
			System.out.println("[BungeeUtilisals]: Can't remove ipban number " + number + ", " + e.getMessage());
		}
	}
	
	public static List<Integer> getIPBanNumbers(){
	    List<Integer> reports = new ArrayList<Integer>();
	    try {
	      Statement st = dbmanager.getConnection().createStatement();
	      ResultSet rs = null;
	      rs = st.executeQuery("SELECT * FROM IPBans");
	      while (rs.next()) {
	    	int reportnumber = rs.getInt("Number");
	    	reports.add(reportnumber);
	      }
	    } catch (SQLException e) {
	    	System.out.println("[BungeeUtilisals]: An error occured while contacting the Database! " + e.getMessage());
	    }
	    return reports;
	}
	
	public static String getIPBannedBy(int number){
		String playername = "";
		try {
			Statement st = dbmanager.getConnection().createStatement();
			ResultSet rs = null;
			rs = st.executeQuery("SELECT * FROM IPBans WHERE Number='" + number + "'");
			while(rs.next()){
				playername = rs.getString("BannedBy");
			}
		} catch (SQLException e){
			e.printStackTrace();
		}
		return playername;
	}
	
	public static String getIPBanned(int number){
		String playername = "";
		try {
			Statement st = dbmanager.getConnection().createStatement();
			ResultSet rs = null;
			rs = st.executeQuery("SELECT * FROM IPBans WHERE Number='" + number + "'");
			while(rs.next()){
				playername = rs.getString("Banned");
			}
		} catch (SQLException e){
			e.printStackTrace();
		}
		return playername;
	}
	
	public static String getIPReason(int number){
		String reason = "";
		try {
			Statement st = dbmanager.getConnection().createStatement();
			ResultSet rs = null;
			rs = st.executeQuery("SELECT * FROM IPBans WHERE Number='" + number + "'");
			while(rs.next()){
				reason = rs.getString("Reason");
			}
		} catch (SQLException e){
			e.printStackTrace();
		}
		return reason;
	}
	
	public static boolean isBanned(String player){
		String reason = null;
		try {
			Statement st = dbmanager.getConnection().createStatement();
			ResultSet rs = null;
			rs = st.executeQuery("SELECT * FROM Bans WHERE Banned='" + player + "'");
			while(rs.next()){
				reason = rs.getString("Reason");
			}
		} catch (SQLException e){
			e.printStackTrace();
		}
		return (reason != null && !reason.isEmpty());
	}
	
	public static Integer getBanNumber(String player){
		int number = 0;
		try {
			Statement st = dbmanager.getConnection().createStatement();
			ResultSet rs = null;
			rs = st.executeQuery("SELECT * FROM Bans WHERE Banned='" + player + "'");
			while(rs.next()){
				number = rs.getInt("Number");
			}
		} catch (SQLException e){
			e.printStackTrace();
		}
		return number;
	}
	
	public static void addBan(int number, String banned_by, String banned, Long ban_time, String reason){
		try {
			Statement st = dbmanager.getConnection().createStatement();
			st.executeUpdate("INSERT INTO Bans(Number, BannedBy, Banned, BanTime, Reason) VALUES ('" + number + "', '" + banned_by + "', '" + banned + "', '" + ban_time + "', '" + reason + "')");
		} catch (SQLException e) {
			System.out.println("[BungeeUtilisals]: Can't add Ban fom: " + banned + "(Banned by: " + banned_by + ", " + e.getMessage());
		}
	}
	
	public static void removeBan(int number){
		try {
			Statement st = dbmanager.getConnection().createStatement();			
			st.executeUpdate("DELETE FROM Bans WHERE Number='" + number + "'");
		} catch (SQLException e) {
			System.out.println("[BungeeUtilisals]: Can't remove ban number " + number + ", " + e.getMessage());
		}
	}
	
	public static List<Integer> getBanNumbers(){
	    List<Integer> reports = new ArrayList<Integer>();
	    try {
	      Statement st = dbmanager.getConnection().createStatement();
	      ResultSet rs = null;
	      rs = st.executeQuery("SELECT * FROM Bans");
	      while (rs.next()) {
	    	int reportnumber = rs.getInt("Number");
	    	reports.add(reportnumber);
	      }
	    } catch (SQLException e) {
	    	System.out.println("[BungeeUtilisals]: An error occured while contacting the Database! " + e.getMessage());
	    }
	    return reports;
	}
	
	public static String getBannedBy(int number){
		String playername = "";
		try {
			Statement st = dbmanager.getConnection().createStatement();
			ResultSet rs = null;
			rs = st.executeQuery("SELECT * FROM Bans WHERE Number='" + number + "'");
			while(rs.next()){
				playername = rs.getString("BannedBy");
			}
		} catch (SQLException e){
			e.printStackTrace();
		}
		return playername;
	}
	
	public static String getBanned(int number){
		String playername = "";
		try {
			Statement st = dbmanager.getConnection().createStatement();
			ResultSet rs = null;
			rs = st.executeQuery("SELECT * FROM Bans WHERE Number='" + number + "'");
			while(rs.next()){
				playername = rs.getString("Banned");
			}
		} catch (SQLException e){
			e.printStackTrace();
		}
		return playername;
	}
	
	public static Long getBanTime(int number){
		long bantime = -1;
		try {
			Statement st = dbmanager.getConnection().createStatement();
			ResultSet rs = null;
			rs = st.executeQuery("SELECT * FROM Bans WHERE Number ='" + number + "'");
			while(rs.next()){
				bantime = rs.getLong("BanTime");
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
			rs = st.executeQuery("SELECT * FROM Bans WHERE Number='" + number + "'");
			while(rs.next()){
				reason = rs.getString("Reason");
			}
		} catch (SQLException e){
			e.printStackTrace();
		}
		return reason;
	}
	
}