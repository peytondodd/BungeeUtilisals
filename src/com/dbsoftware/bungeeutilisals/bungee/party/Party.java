package com.dbsoftware.bungeeutilisals.bungee.party;

import java.io.File;
import java.util.Arrays;
import java.util.List;
import com.dbsoftware.bungeeutilisals.bungee.BungeeUtilisals;
import com.dbsoftware.bungeeutilisals.bungee.managers.DatabaseManager;
import com.dbsoftware.bungeeutilisals.bungee.managers.FileManager;
import net.md_5.bungee.api.ProxyServer;

public class Party {
	
    private static String path = File.separator + "plugins" + File.separator + "BungeeUtilisals" + File.separator + "party.yml";
    public static FileManager party = new FileManager( path );
    public static DatabaseManager dbmanager = BungeeUtilisals.getDatabaseManager();

    public static void reloadPartyData() {
        party = null;
        party = new FileManager( path );
    }
    
    public static void registerPartySystem(){
    	party = null;
    	party = new FileManager( path );
    	if(party.getBoolean("Party.Enabled", true)){
        	setDefaults();
    		ProxyServer.getInstance().getPluginManager().registerCommand(BungeeUtilisals.getInstance(), new PartyCommand());
    		ProxyServer.getInstance().getPluginManager().registerListener(BungeeUtilisals.getInstance(), new PartyEvents());
    	}
    }
    
    private static void setDefaults(){
        List<String> check = party.getConfigurationSection( "Party" );
        if(!check.contains("Party-Limit")){
        	party.setInt("Party.Party-Limit", 0);
        }
        if(!check.contains("Server-Switch")){
        	party.setBoolean("Party.Server-Switch", false);
        }
        if(!check.contains("Messages")){
        	party.setStringList("Party.Messages.Help",
        			Arrays.asList(new String[]{
        					"&c&l&m----------&r &8[&a&lParty Help&8] &c&l&m----------&r",
        					"&c/party create &7- &c&oCreate a new party",
        					"&c/party list &7- &c&oList the current members of your party}",
        					"&c/party leave &7- &c&oLeave/Delete your current party",
        					"&c/party chat &7- &c&oChat with your party members",
        					"&c/party invite &7- &c&oInvite a player to your party",
        					"&c/party kick &7- &c&oKick someone from your party",
        					"&c/party admin (player) &7- &c&oPromote someone to admin in your party.",
        					"&c&l&m----------&r &8[&a&lParty Help&8] &c&l&m----------&r"
        					}));
        	
        	party.setString("Party.Messages.NoPermission", "&cYou don't have the permission to use this Command!");
        	party.setString("Party.Messages.WrongArgs", "&cPlease use /party help for more info.");
        	party.setString("Party.Messages.ConsoleError", "&cThe console cannot use this Command!");
        	party.setString("Party.Messages.AlreadyInParty", "&cYou are already in a party!");
        	party.setString("Party.Messages.PartyCreated", "&aYou have created a new party!");
        	party.setString("Party.Messages.OfflinePlayer", "&cThat player is not online!");
        	party.setString("Party.Messages.AdminRemove", "&c%player% is not a party admin anymore.");
        	party.setString("Party.Messages.AdminAdd", "&a%player% is now a party admin!");
        	party.setString("Party.Messages.NotInYourParty", "&cThat Player is not in your party!");
        	party.setString("Party.Messages.NotPartyOwner", "&cYou are not the party owner, so you cannot use this command!");
        	party.setString("Party.Messages.NotInParty", "&cYou are not in a party!");
        	party.setString("Party.Messages.LeftParty", "&cYou have left your party!");
        	party.setString("Party.Messages.LeftPartyMessage", "&c%player% has left the party!");
        	party.setString("Party.Messages.PartyRemoved", "&cYou have deleted your party!");
        	party.setString("Party.Messages.PartyRemovedMessage", "&c%player% has deleted the party!");
        	party.setString("Party.Messages.PartyChatFormat", "&f%player% &6:: &f%message%");
        	party.setString("Party.Messages.PartyFull", "&cYour party is full!");
        	party.setString("Party.Messages.PlayerAlreadyInParty", "&cThat player is already in your party!");
        	party.setString("Party.Messages.PlayerHasOwnParty", "&cThat player already has his own party!");
        	party.setString("Party.Messages.InvitedPlayer", "&aYou have invited &e%player% &ato your party!");
        	party.setStringList("Party.Messages.InviteReceive", Arrays.asList(new String[]{"&e%player% &ahas invited you to join his party!", "&aUse /party accept within 1 minute to join his Party!"}));
        	party.setString("Party.Messages.AcceptedToLate", "&cThis invitation is already expired!");
        	party.setString("Party.Messages.JoinedParty", "&aYou have joined the party!");
        	party.setString("Party.Messages.JoinedPartyMessage", "&e%player% &ahas joined the party!");
        	party.setString("Party.Messages.NoInvites", "&cYou do not have any invites.");
        	party.setString("Party.Messages.Kicked", "&cYou have been kicked out of the party!");
        	party.setString("Party.Messages.KickedPlayer", "&cYou have kicked %player% out of the party!");
        	party.setString("Party.Messages.KickedMessage", "&4%player% &cwas kicked out of the party!");
        	party.setString("Party.Messages.AdminError", "&cYou cannot make yourself admin!");
        	party.setString("Party.Messages.ServerWarp", "&aThe party owner has sent you to his server!");
        
        	party.setString("Party.Messages.PartyList.Header", "&a========== &eParty Members &a==========");
        	party.setString("Party.Messages.PartyList.Format", "&a%player% &eis %onORoff% &8[&a%partyrank%&8] [&e%server%&8]");
        	party.setString("Party.Messages.PartyList.Footer", "&a========== &eParty Members &a==========");
        }
    	party.save();
    } 
}
