
package com.dbsoftware.bungeeutilisals.bungee.friends;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;

import com.dbsoftware.bungeeutilisals.bungee.BungeeUtilisals;
import com.dbsoftware.bungeeutilisals.bungee.managers.DatabaseManager;
import com.dbsoftware.bungeeutilisals.bungee.managers.FileManager;

public class Friends {
	
    private static String path = File.separator + "plugins" + File.separator + "BungeeUtilisals" + File.separator + "friends.yml";
    public static FileManager friends = new FileManager( path );
    public static HashMap<ProxiedPlayer, ProxiedPlayer> msging = new HashMap<ProxiedPlayer, ProxiedPlayer>();
    public static DatabaseManager dbmanager = BungeeUtilisals.getDatabaseManager();

    public static void reloadFriendsData() {
        friends = null;
        friends = new FileManager( path );
    }
    
    public static void registerFriendsAddons(){
    	friends = null;
    	friends = new FileManager( path );
    	if(friends.getBoolean("Friends.Enabled", true)){
    		ProxyServer.getInstance().getPluginManager().registerCommand(BungeeUtilisals.instance, new FriendsCommand());
    		ProxyServer.getInstance().getPluginManager().registerListener(BungeeUtilisals.instance, new FriendJoin(BungeeUtilisals.instance));
    		ProxyServer.getInstance().getPluginManager().registerListener(BungeeUtilisals.instance, new FriendQuit(BungeeUtilisals.instance));
        	setDefaults();
    	}
    }
    
    private static void setDefaults(){
        List<String> check = friends.getConfigurationSection( "Friends" );
        if(!check.contains("Limit")){
			friends.setBoolean("Friends.Limit.Enabled", true);
			friends.setInt("Friends.Limit.Limit", 10);
        }
        if(!check.contains("Messages")){
       		List<String> help = new ArrayList<String>();
    		help.add("&a&m&l---------&r&c&l Friends &a&m&l---------");
    		help.add("&f- &7/friend add (player) &f=> Adds someone to your friends.");
    		help.add("&f- &7/friend remove (player) &f=> Removes a friend.");
    		help.add("&f- &7/friend list &f=> Lists all your friends.");
    		help.add("&f- &7/friend requests &f=> Lists all requests sended to you.");
    		help.add("&f- &7/friend accept (player) &f=> Accept a request.");
    		help.add("&f- &7/friend deny (player) &f=> Deny a request.");
    		help.add("&a&m&l---------&r&c&l Friends &a&m&l---------");
    		friends.setStringList("Friends.Messages.Help", help);
    		friends.setString("Friends.Messages.NoPlayer", "&cThat player is not online!");
    		friends.setString("Friends.Messages.Added", "&aYou have successfully added %player% to your friends!");
    		friends.setString("Friends.Messages.Add", "&a%player% has added you to his friend list!");
    		friends.setString("Friends.Messages.AlreadyFriend", "&a%player% is already a friend!");
    		friends.setString("Friends.Messages.Error", "&cYou can't add yourself as a friend.");
    		friends.setString("Friends.Messages.RemoveError", "&cYou can't remove someone who is not your friend.");
    		friends.setString("Friends.Messages.Removed", "&cYou have successfully removed %friend% from your friends.");
    		friends.setString("Friends.Messages.Remove", "&c%player% has removed you from his friend list.");
    		friends.setString("Friends.Messages.MsgError", "&cYou can't msg to yourself!");
			friends.setString("Friends.Messages.NoFriends", "&cYou have no friends yet!");
			friends.setString("Friends.Messages.FriendJoin", "&a%friend% has joined the server!");
			friends.setString("Friends.Messages.FriendQuit", "&a%friend% has left the server!");
    		friends.setString("Friends.Messages.FriendListFormat.Header", "&a&m&l-------&r&c&l Friends &a&m&l-------");
    		friends.setString("Friends.Messages.FriendListFormat.Footer", "&a&m&l-------&r&c&l Friends &a&m&l-------");
    		friends.setString("Friends.Messages.FriendListFormat.OnlineFormat", "&7- &a%friend% &7is online in &a%server%&7!");
    		friends.setString("Friends.Messages.FriendListFormat.OfflineFormat", "&7- &a%friend% &7is offline!");
    		friends.setString("Friends.Messages.ReachedLimit", "&cYou have reached our friend limit!");
    		
    		friends.setString("Friends.Messages.Hover.Teleport", "&aThis will teleport you to the &e%server% &aserver!");
    		friends.setString("Friends.Messages.Hover.Remove", "&aClick here to remove &e%player% &afrom your friend list.");
    		friends.setString("Friends.Messages.Hover.Text.Teleport", "&8&l[&a&lTeleport&8&l]");
    		friends.setString("Friends.Messages.Hover.Text.Remove", "&8&l[&c&lRemove&8&l]");
    		
    		
    		
	    	 friends.setString("Friends.Messages.RequestJoinMessage", "&aYou have &e%requests% &arequests atm! Do /friend requests to list them.");
	    	 friends.setString("Friends.Messages.NoRequests", "&cYou have no requests atm.");
	    	 friends.setString("Friends.Messages.RequestListFormat.Header", "&a&m&l-------&r&c&l Friend Requests &a&m&l-------");
	    	 friends.setString("Friends.Messages.RequestListFormat.Format", "&7- &f%player%");
	    	 friends.setString("Friends.Messages.RequestListFormat.Footer", "&a&m&l-------&r&c&l Friend Requests &a&m&l-------");
	    	 
	    	 friends.setString("Friends.Messages.Hover.Text.Accept", "&8&l[&a&lAccept&8&l]");
	    	 friends.setString("Friends.Messages.Hover.Text.Deny", "&8&l[&c&lDeny&8&l]");
	    	 friends.setString("Friends.Messages.Hover.Text.Msg", "&8&l[&e&lMsg&8&l]");
	    	 
	    	 friends.setString("Friends.Messages.Hover.Accept", "&aClick here to accept this request.");
	    	 friends.setString("Friends.Messages.Hover.Deny", "&cClick here to deny this request.");
	    	 friends.setString("Friends.Messages.Hover.Msg", "&eClick here to talk with &a%friend%&e!");
	    	 
	    	 friends.setString("Friends.Messages.AlreadyRequested", "&aThere is already a request for player.");
	    	 friends.setString("Friends.Messages.RequestSent", "&aThe request has successfully been send.");
	    	 friends.setString("Friends.Messages.NewRequest", "&aYou have a new request from &a%player%");
	    	 friends.setString("Friends.Messages.RequestRemoveError", "&cYou can't remove a request from someone who didn't requested!");
	    	 
	    	 friends.setString("Friends.Messages.Accepted", "&aYou are now friends with &e%player%&a!");
	    	 friends.setString("Friends.Messages.AcceptedFriend", "&e%player% &ahas accepted your friend request");
	    	 
	    	 friends.setString("Friends.Messages.Denied", "&cYou have denied %player% his friend request.");
	    	 friends.setString("Friends.Messages.DeniedFriend", "&4%player% &chas denied your friend request!");
	    	 
	    	 friends.setString("Friends.Messages.MsgSend", "&8[&aMe &2=> &e%player%&8] &a> &f%message%");
	    	 friends.setString("Friends.Messages.MsgReceive", "&8[&e%player% &2=> &aMe&8] &a> &f%message%");
	    	 friends.setString("Friends.Messages.NoReply", "&cThere is no player to reply to.");
	    	 friends.getString("Friends.Messages.NoFriendMsgError", "&cThat player is not a friend.");
    	}
        friends.save();
    } 
}
