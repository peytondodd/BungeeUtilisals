package com.dbsoftware.bungeeutilisals.bungee;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.ResultSet;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;

import com.dbsoftware.bungeeutilisals.ConfigData;
import com.dbsoftware.bungeeutilisals.api.CommandAPI;
import com.dbsoftware.bungeeutilisals.bungee.actionbarannouncer.ActionBarAnnouncer;
import com.dbsoftware.bungeeutilisals.bungee.announcer.Announcer;
import com.dbsoftware.bungeeutilisals.bungee.commands.AlertCommand;
import com.dbsoftware.bungeeutilisals.bungee.commands.BgcCommand;
import com.dbsoftware.bungeeutilisals.bungee.commands.BigalertCommand;
import com.dbsoftware.bungeeutilisals.bungee.commands.ButilisalsCommand;
import com.dbsoftware.bungeeutilisals.bungee.commands.ChatCommand;
import com.dbsoftware.bungeeutilisals.bungee.commands.ClearChatCommand;
import com.dbsoftware.bungeeutilisals.bungee.commands.FindCommand;
import com.dbsoftware.bungeeutilisals.bungee.commands.GRankCommand;
import com.dbsoftware.bungeeutilisals.bungee.commands.GlistCommand;
import com.dbsoftware.bungeeutilisals.bungee.commands.HubCommand;
import com.dbsoftware.bungeeutilisals.bungee.commands.LocalSpyCommand;
import com.dbsoftware.bungeeutilisals.bungee.commands.MSGCommand;
import com.dbsoftware.bungeeutilisals.bungee.commands.ReplyCommand;
import com.dbsoftware.bungeeutilisals.bungee.commands.RulesCommand;
import com.dbsoftware.bungeeutilisals.bungee.commands.ServerCommand;
import com.dbsoftware.bungeeutilisals.bungee.commands.SpyCommand;
import com.dbsoftware.bungeeutilisals.bungee.commands.StoreCommand;
import com.dbsoftware.bungeeutilisals.bungee.commands.VoteCommand;
import com.dbsoftware.bungeeutilisals.bungee.listener.AntiAd;
import com.dbsoftware.bungeeutilisals.bungee.listener.AntiCaps;
import com.dbsoftware.bungeeutilisals.bungee.listener.AntiSpam;
import com.dbsoftware.bungeeutilisals.bungee.listener.AntiSwear;
import com.dbsoftware.bungeeutilisals.bungee.listener.ChatListener;
import com.dbsoftware.bungeeutilisals.bungee.listener.ChatLock;
import com.dbsoftware.bungeeutilisals.bungee.listener.ChatUtilities;
import com.dbsoftware.bungeeutilisals.bungee.listener.CommandListener;
import com.dbsoftware.bungeeutilisals.bungee.listener.DisconnectEvent;
import com.dbsoftware.bungeeutilisals.bungee.listener.JoinListener;
import com.dbsoftware.bungeeutilisals.bungee.listener.LoginEvent;
import com.dbsoftware.bungeeutilisals.bungee.listener.MessageLimiter;
import com.dbsoftware.bungeeutilisals.bungee.listener.PluginMessageReceive;
import com.dbsoftware.bungeeutilisals.bungee.listener.PrivateMessageListener;
import com.dbsoftware.bungeeutilisals.bungee.listener.ServerSwitch;
import com.dbsoftware.bungeeutilisals.bungee.managers.DatabaseManager;
import com.dbsoftware.bungeeutilisals.bungee.metrics.Metrics;
import com.dbsoftware.bungeeutilisals.bungee.party.Party;
import com.dbsoftware.bungeeutilisals.bungee.punishment.Punishments;
import com.dbsoftware.bungeeutilisals.bungee.report.Reports;
import com.dbsoftware.bungeeutilisals.bungee.staffchat.StaffChat;
import com.dbsoftware.bungeeutilisals.bungee.tabmanager.TabManager;
import com.dbsoftware.bungeeutilisals.bungee.titleannouncer.TitleAnnouncer;
import com.dbsoftware.bungeeutilisals.bungee.updater.UpdateChecker;
import com.dbsoftware.bungeeutilisals.bungee.utils.MySQL;
import com.dbsoftware.bungeeutilisals.bungee.utils.TPSRunnable;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.io.ByteStreams;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import net.md_5.bungee.BungeeCord;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.config.Configuration;
import net.md_5.bungee.config.ConfigurationProvider;
import net.md_5.bungee.config.YamlConfiguration;

public class BungeeUtilisals extends Plugin {

	@Getter
	@Setter(AccessLevel.PRIVATE)
	private static BungeeUtilisals instance;
	@Getter
	@Setter(AccessLevel.PRIVATE)
	private DatabaseManager databaseManager;
	public boolean chatMuted = false;
	public boolean update;
	private File configfile;
	@Getter
	private Configuration config;
	@Getter
	private ConfigData configData;
	@Getter
	private HashMap<String, String> pmcache = Maps.newHashMap();
	public List<BungeeUser> users = Lists.newArrayList();
	public List<String> staff = Lists.newArrayList();

	public void onEnable() {
		setInstance(this);

		try {
			Metrics metrics = new Metrics(this);
			metrics.start();
		} catch (IOException e) {
			ProxyServer.getInstance().getLogger().info("[BungeeUtilisals] Metrics could not be enabled.");
		}

		this.configfile = this.loadResource(this, "config.yml");
		try {
			this.config = ConfigurationProvider.getProvider(YamlConfiguration.class).load(this.configfile);
			this.saveConfig();
		} catch (IOException e) {
			e.printStackTrace();
		}
		this.configData = new ConfigData();

		loadCommands();
		registerEvents();
		Announcer.loadAnnouncements();
		TitleAnnouncer.loadAnnouncements();
		ActionBarAnnouncer.loadAnnouncements();
		StaffChat.registerStaffChat();
		Party.registerPartySystem();
		TabManager.loadTab();

		ProxyServer.getInstance().getScheduler().schedule(this, new TPSRunnable(), 50, TimeUnit.MILLISECONDS);

		if (configData.UPDATECHECKER) {
			UpdateChecker.checkUpdate(instance.getDescription().getVersion());

			ProxyServer.getInstance().getScheduler().schedule(instance, new Runnable() {
				public void run() {
					UpdateChecker.checkUpdate(getDescription().getVersion());
				}
			}, 15, 15, TimeUnit.MINUTES);
		}

		if (configData.MYSQL_ENABLED) {
			String host = config.getString("MySQL.Host", "localhost");
			int port = config.getInt("MySQL.Port", 3306);
			String database = config.getString("MySQL.Database", "database");
			String username = config.getString("MySQL.Username", "username");
			String password = config.getString("MySQL.Password", "password");
			databaseManager = new DatabaseManager(host, port, database, username, password);
			databaseManager.openConnection();

			ProxyServer.getInstance().getScheduler().schedule(BungeeUtilisals.getInstance(), new Runnable() {
				@Override
				public void run() {
					databaseManager.closeConnection();
					databaseManager.openConnection();
				}
			}, 5, 5, TimeUnit.MINUTES);

			Reports.registerReportSystem();
			Punishments.registerPunishmentSystem();
			Runnable r = new Runnable() {
				@Override
				public void run() {
					try {
						staff.clear();
						ResultSet rs = MySQL.getInstance().select().table("Staffs").column("*").select();

						while (rs.next()) {
							staff.add(rs.getString("Name").toLowerCase());
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			};
			BungeeCord.getInstance().getScheduler().schedule(this, r, 0, 1, TimeUnit.MINUTES);
		}
	}

	public List<BungeeUser> getStaff() {
		List<BungeeUser> list = Lists.newArrayList();

		for (BungeeUser user : Lists.newArrayList(users)) {
			if (user.isStaff()) {
				list.add(user);
			}
		}

		return list;
	}

	public BungeeUser getUser(ProxiedPlayer p) {
		List<BungeeUser> users = Lists.newArrayList(this.users);
		for (BungeeUser user : users) {
			if (user.getPlayer().equals(p)) {
				return user;
			}
		}
		return null;
	}

	public BungeeUser getUser(String name) {
		List<BungeeUser> users = Lists.newArrayList(this.users);
		for (BungeeUser user : users) {
			if (user.getPlayer().getName().toLowerCase().equals(name.toLowerCase())) {
				return user;
			}
		}
		return null;
	}

	public List<BungeeUser> getUsers() {
		return Lists.newArrayList(users);
	}

	public boolean saveConfig() {
		try {
			ConfigurationProvider.getProvider(YamlConfiguration.class).save(config, configfile);
			return true;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return false;
	}

	public boolean reloadConfig() {
		try {
			this.config = ConfigurationProvider.getProvider(YamlConfiguration.class).load(this.configfile);
			this.configData = new ConfigData();
			return true;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return false;
	}

	private void loadCommands() {
		CommandAPI.getInstance().registerCommand(new AlertCommand());
		CommandAPI.getInstance().registerCommand(new BgcCommand());
		CommandAPI.getInstance().registerCommand(new BigalertCommand());
		CommandAPI.getInstance().registerCommand(new ButilisalsCommand());
		if (configData.CHATLOCK_COMMAND_ENABLED) {
			CommandAPI.getInstance().registerCommand(new ChatCommand());
		}
		if (configData.CLEARCHAT_ENABLED) {
			CommandAPI.getInstance().registerCommand(new ClearChatCommand());
		}
		CommandAPI.getInstance().registerCommand(new FindCommand());
		if (configData.GLIST_ENABLED) {
			CommandAPI.getInstance().registerCommand(new GlistCommand());
		}
		if (configData.MYSQL_ENABLED) {
			CommandAPI.getInstance().registerCommand(new GRankCommand());
		}
		if (configData.HUB_ENABLED) {
			String cmd = getConfig().getString("Hub.Command");
			if (cmd.contains(";")) {
				String[] cmds = cmd.split(";");

				CommandAPI.getInstance()
						.registerCommand(new HubCommand(cmds[0], Arrays.copyOfRange(cmds, 1, cmds.length)));
			} else {
				CommandAPI.getInstance().registerCommand(new HubCommand(cmd));
			}
		}
		CommandAPI.getInstance().registerCommand(new LocalSpyCommand());
		CommandAPI.getInstance().registerCommand(new MSGCommand());
		CommandAPI.getInstance().registerCommand(new ReplyCommand());
		CommandAPI.getInstance().registerCommand(new ServerCommand());
		CommandAPI.getInstance().registerCommand(new SpyCommand());
		if (getConfig().getBoolean("Vote.Enabled")) {
			CommandAPI.getInstance().registerCommand(new VoteCommand());
		}
		if (getConfig().getBoolean("Store.Enabled")) {
			CommandAPI.getInstance().registerCommand(new StoreCommand());
		}
		if (getConfig().getBoolean("Rules.Enabled")) {
			CommandAPI.getInstance().registerCommand(new RulesCommand());
		}
	}

	private void registerEvents() {
		ProxyServer.getInstance().getPluginManager().registerListener(this, new MessageLimiter());
		ProxyServer.getInstance().getPluginManager().registerListener(this, new DisconnectEvent());
		ProxyServer.getInstance().getPluginManager().registerListener(this, new LoginEvent());
		ProxyServer.getInstance().getPluginManager().registerListener(this, new AntiSpam());
		ProxyServer.getInstance().getPluginManager().registerListener(this, new ChatLock());
		ProxyServer.getInstance().getPluginManager().registerListener(this, new ChatUtilities());
		ProxyServer.getInstance().getPluginManager().registerListener(this, new AntiSwear());
		ProxyServer.getInstance().getPluginManager().registerListener(this, new AntiCaps());
		ProxyServer.getInstance().getPluginManager().registerListener(this, new AntiAd());
		ProxyServer.getInstance().getPluginManager().registerListener(this, new ServerSwitch());
		ProxyServer.getInstance().getPluginManager().registerListener(this, new PluginMessageReceive());
		ProxyServer.getInstance().getPluginManager().registerListener(this, new JoinListener());
		ProxyServer.getInstance().getPluginManager().registerListener(this, new PrivateMessageListener());
		ProxyServer.getInstance().getPluginManager().registerListener(this, new CommandListener());
		ProxyServer.getInstance().getPluginManager().registerListener(this, new ChatListener());
	}

	public void onDisable() {
		databaseManager.closeConnection();
		ProxyServer.getInstance().getLogger().info("BungeeUtilisals is now Disabled!");
	}

	public File loadResource(Plugin plugin, String resource) {
		File folder = plugin.getDataFolder();
		if (!folder.exists()) {
			folder.mkdir();
		}
		File resourceFile = new File(folder, resource);
		try {
			if (!resourceFile.exists()) {
				resourceFile.createNewFile();
				try (InputStream in = plugin.getResourceAsStream(resource);
						OutputStream out = new FileOutputStream(resourceFile)) {
					ByteStreams.copy(in, out);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return resourceFile;
	}
}
