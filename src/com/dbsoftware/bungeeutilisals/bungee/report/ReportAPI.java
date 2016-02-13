package com.dbsoftware.bungeeutilisals.bungee.report;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.dbsoftware.bungeeutilisals.bungee.BungeeUtilisals;
import com.dbsoftware.bungeeutilisals.bungee.managers.DatabaseManager;

import net.md_5.bungee.api.connection.ProxiedPlayer;

public class ReportAPI {
	
	private static DatabaseManager dbmanager = BungeeUtilisals.getDatabaseManager();
	
	public static void addReport(int number, ProxiedPlayer reporter, ProxiedPlayer reported, String reason){
		try {
			Statement st = dbmanager.getConnection().createStatement();
			st.executeUpdate("INSERT INTO Reports(Number, Reporter, Player, Reason) VALUES ('" + number + "', '" + reporter.getName() + "', '" + reported.getName() + "', '" + reason + "')");
		} catch (SQLException e) {
			System.out.println("[BungeeUtilisals]: Can't add Report from: " + reporter.getName() + ", " + e.getMessage());
		}
	}
	
	public static void removeReport(int number){
		try {
			Statement st = dbmanager.getConnection().createStatement();			
			st.executeUpdate("DELETE FROM Reports WHERE Number='" + number + "'");
		} catch (SQLException e) {
			System.out.println("[BungeeUtilisals]: Can't remove request number " + number + ", " + e.getMessage());
		}
	}
	
	public static List<Integer> getReportNumbers(){
	    List<Integer> reports = new ArrayList<Integer>();
	    try {
	      Statement st = dbmanager.getConnection().createStatement();
	      ResultSet rs = null;
	      rs = st.executeQuery("SELECT * FROM Reports");
	      while (rs.next()) {
	    	int reportnumber = rs.getInt("Number");
	    	reports.add(reportnumber);
	      }
	    } catch (SQLException e) {
	      System.out.println("[BungeeUtilisals]: An error occured while contacting the Database! " + e.getMessage());
	    }
	    return reports;
	}
	
	public static String getReporter(int number){
		String playername = "";
		try {
			Statement st = dbmanager.getConnection().createStatement();
			ResultSet rs = null;
			rs = st.executeQuery("SELECT * FROM Reports WHERE Number='" + number + "'");
			while(rs.next()){
				playername = rs.getString("Reporter");
			}
		} catch (SQLException e){
			e.printStackTrace();
		}
		return playername;
	}
	
	public static String getReportedPlayer(int number){
		String playername = "";
		try {
			Statement st = dbmanager.getConnection().createStatement();
			ResultSet rs = null;
			rs = st.executeQuery("SELECT * FROM Reports WHERE Number='" + number + "'");
			while(rs.next()){
				playername = rs.getString("Player");
			}
		} catch (SQLException e){
			e.printStackTrace();
		}
		return playername;
	}
	
	public static String getReason(int number){
		String reason = "";
		try {
			Statement st = dbmanager.getConnection().createStatement();
			ResultSet rs = null;
			rs = st.executeQuery("SELECT * FROM Reports WHERE Number='" + number + "'");
			while(rs.next()){
				reason = rs.getString("Reason");
			}
		} catch (SQLException e){
			e.printStackTrace();
		}
		return reason;
	}
	
}
