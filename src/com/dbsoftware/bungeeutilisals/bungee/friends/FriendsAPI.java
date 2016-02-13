package com.dbsoftware.bungeeutilisals.bungee.friends;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.dbsoftware.bungeeutilisals.bungee.BungeeUtilisals;
import com.dbsoftware.bungeeutilisals.bungee.managers.DatabaseManager;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;

public class FriendsAPI{
 
	private static DatabaseManager dbmanager = BungeeUtilisals.getDatabaseManager();
	public static FriendsAPI friendsapi;
  
	public static boolean isInTable(String p) {
	  try {
		  Statement st = dbmanager.getConnection().createStatement();
		  ResultSet rs = null;
		  rs = st.executeQuery("SELECT * FROM Friends WHERE Player='" + p + "'");
		  while (rs.next()) {
			  if (rs != null) {
				  return true;
			  }
		  }
	  } catch (SQLException e) {
		  e.printStackTrace();
	  }
	  return false;
	}
  
	public static List<String> getFriends(String player){
		List<String> friends = new ArrayList<String>();
		try {
			Statement st = dbmanager.getConnection().createStatement();
			ResultSet rs = null;
			rs = st.executeQuery("SELECT * FROM Friends WHERE Player='" + player + "'");
			while (rs.next()) {
				String friend = rs.getString("Friends");
				friends.add(friend);
			}
		} catch (SQLException e){
			System.out.println("[BungeeUtilisals]: Can't get the Friends of " + player  + e.getMessage());
		}
		return friends;
	}
  
	public static List<String> getFriendRequests(String player){
		List<String> requests = new ArrayList<String>();
		try {
			Statement st = dbmanager.getConnection().createStatement();
			ResultSet rs = null;
			rs = st.executeQuery("SELECT * FROM Requests WHERE Player='" + player + "'");
			while (rs.next()) {
				String request = rs.getString("Requests");
				requests.add(request);
			}
		} catch (SQLException e){
			System.out.println("[BungeeUtilisals]: Can't get the Friend Requests of " + player  + e.getMessage());
		}
		return requests;
	}
  
	public static void addFriendRequest(ProxiedPlayer requestSender, String requested){
		if(requestSender.getName().equalsIgnoreCase(requested)){
			requestSender.sendMessage(new TextComponent(Friends.friends.getString("Friends.Messages.Error", "&cYou can't add yourself as a friend.").replace("&", "§")));
			return;
		} else {
			try {
				Statement st = dbmanager.getConnection().createStatement();
				st.executeUpdate("INSERT INTO Requests(Player, Requests) VALUES ('" + requested + "', '" + requestSender.getName() + "')");
			} catch (SQLException e) {
				System.out.println("[BungeeUtilisals]: Can't add Friend Request from: " + requestSender.getName() + " to: " + requested + ", " + e.getMessage());
			}
		}
	}
  
	public static void denyFriendRequest(ProxiedPlayer sender, String requester){
		try {
			Statement st = dbmanager.getConnection().createStatement();
			if(isInTable(sender.getName())) {
				st.executeUpdate("DELETE FROM Requests WHERE Player='" + sender.getName() + "' AND Requests='" + requester + "'");
			} else {
				sender.sendMessage(new TextComponent(Friends.friends.getString("Friends.Messages.RequestRemoveError", "&cYou can't remove a request from someone who didn't requested!").replace("&", "§")));
				return;
			}
		} catch (SQLException e) {
			System.out.println("[BungeeUtilisals]: Can't remove Friend Request from: " + requester + " to: " + sender.getName() + ", " + e.getMessage());
		}
	}
  
	public static void removeFriendRequest(ProxiedPlayer sender, String requester){
		try {
			Statement st = dbmanager.getConnection().createStatement();
			if (isInTable(sender.getName())) {
				st.executeUpdate("DELETE FROM Requests WHERE Player='" + sender.getName() + "' AND Requests='" + requester + "'");
			} else {
				return;
			}
		} catch (SQLException e) {
			System.out.println("[BungeeUtilisals]: Can't remove Friend Request from: " + requester + " to: " + sender.getName() + ", " + e.getMessage());
		}
	}
  
	public static void addFriend(ProxiedPlayer p, String friend){
		try {
			Statement st = dbmanager.getConnection().createStatement();
			st.executeUpdate("INSERT INTO Friends(Player, Friends) VALUES ('" + p.getName() + "', '" + friend + "')");
			st.executeUpdate("INSERT INTO Friends(Player, Friends) VALUES ('" + friend + "', '" + p.getName() + "')");
		} catch (SQLException e) {
			System.out.println("[BungeeUtilisals]: Can't add Friend: " + friend + " to: " + p.getName() + ", " + e.getMessage());
		}
	}
  
	public static void removeFriend(ProxiedPlayer p, String friend){
		try {
			Statement st = dbmanager.getConnection().createStatement();
			if (isInTable(p.getName())) {
				st.executeUpdate("DELETE FROM Friends WHERE Player='" + p.getName() + "' AND Friends='" + friend + "'");
				st.executeUpdate("DELETE FROM Friends WHERE Player='" + friend + "' AND Friends='" + p.getName() + "'");
			} else {
				p.sendMessage(new TextComponent(Friends.friends.getString("Friends.Messages.NoFriends", "&cYou have no friends yet!").replace("&", "§")));
			}
		} catch (SQLException e) {
			System.out.println("[BungeeUtilisals]: Can't remove Friend: " + friend + " from: " + p.getName() + ", " + e.getMessage());
		}
	}
}
