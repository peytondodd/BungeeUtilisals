package com.dbsoftware.bungeeutilisals.bungee.managers;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import net.md_5.bungee.api.connection.ProxiedPlayer;

public class DatabaseManager {
	
	private static String host;
	private static int port;
	private static String database;
	private static String username;
	private static String password;
	private static Connection connection;
  
	public DatabaseManager(String host, int port, String database, String username, String password){
		DatabaseManager.host = host;
		DatabaseManager.port = port;
		DatabaseManager.database = database;
		DatabaseManager.username = username;
		DatabaseManager.password = password;
    
		System.out.println("[BungeeUtilisals] Connecting to database..");
	}
  
	public void openConnection(){
		if (!isConnected()) {
			try {
				Class.forName("com.mysql.jdbc.Driver");
				connection = DriverManager.getConnection("jdbc:mysql://" + host + ":" + port + "/" + database, username, password);
				this.prepareDatabase();
			} catch (SQLException e) {
				System.out.println("[BungeeUtilisals]: Can't connect to database: " + e.getMessage());
				e.printStackTrace();
			} catch (ClassNotFoundException e) {
				System.out.println("[BungeeUtilisals]: Can't connect to database: " + e.getMessage());
			}
		} else {
			System.out.println("[BungeeUtilisals]: Database already connected");
		}
	}
  
	public void prepareDatabase(){
		if (isConnected()) {
			try {
				Statement st = getConnection().createStatement();
				st.executeUpdate("CREATE TABLE IF NOT EXISTS Friends (Player VARCHAR(32) NOT NULL, Friends VARCHAR(32) NOT NULL)");
				st.executeUpdate("CREATE TABLE IF NOT EXISTS Requests (Player VARCHAR(32) NOT NULL, Requests VARCHAR(32) NOT NULL)");
				st.executeUpdate("CREATE TABLE IF NOT EXISTS Reports (Number INT NOT NULL, Reporter VARCHAR(32) NOT NULL, Player VARCHAR(32) NOT NULL, Reason TEXT NOT NULL)");
				st.executeUpdate("CREATE TABLE IF NOT EXISTS PlayerInfo (Player VARCHAR(32) NOT NULL, IP VARCHAR(120) NOT NULL, Bans INT NOT NULL, Warns INT NOT NULL, Mutes INT NOT NULL, Kicks INT NOT NULL)");
				st.executeUpdate("CREATE TABLE IF NOT EXISTS Bans (Server VARCHAR(32) NOT NULL, BannedBy VARCHAR(32) NOT NULL, Banned VARCHAR(32) NOT NULL, BanTime LONG NOT NULL, Reason TEXT NOT NULL)");
				st.executeUpdate("CREATE TABLE IF NOT EXISTS Mutes (Server VARCHAR(32) NOT NULL, MutedBy VARCHAR(32) NOT NULL, Muted VARCHAR(32) NOT NULL, MuteTime LONG NOT NULL, Reason TEXT NOT NULL)");
				st.executeUpdate("CREATE TABLE IF NOT EXISTS IPBans (Server VARCHAR(32) NOT NULL, BannedBy VARCHAR(32) NOT NULL, Banned VARCHAR(32) NOT NULL, Reason TEXT NOT NULL)");
			} catch (SQLException e) {
				System.out.println("[BungeeUtilisals]: Can't prepare database: " + e.getMessage());
			}
		} else {
			System.out.println("[BungeeUtilisals]: Database isn't connected ");
		}
	}
  
	public boolean isConnected(){
		boolean connected = false;
		try {
			connected = (connection != null) && (!connection.isClosed());
		} catch (SQLException e) {
	    	e.printStackTrace();
	    }
		return connected;
	}
	
	public Connection getConnection(){
		return connection;
	}
  
	public boolean isInTable(ProxiedPlayer player, String table) {
		if (isConnected()) {
			try {
				Statement statement = getConnection().createStatement();
				ResultSet result = statement.executeQuery("SELECT * FROM '" + table + "' WHERE NAME='" + player.getName() + "'");
        
				return result.next();
			} catch (SQLException e) {
				e.printStackTrace();
        
				return false;
			}
		}
		return false;
	}
  
	public void closeConnection() {
		try {
			connection.close();
		} catch (SQLException e) {
			System.out.println("[BungeeUtilisals]: Can't close the connection: " + e.getMessage());
		} finally {
			connection = null;
		}
	}
}