package com.dbsoftware.bungeeutilisals.bungee.tabmanager;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.concurrent.TimeUnit;

import com.dbsoftware.bungeeutilisals.bungee.BungeeUtilisals;
import com.dbsoftware.bungeeutilisals.bungee.managers.FileManager;

import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.scheduler.ScheduledTask;

public class TabManager {

	private static String path = File.separator + "plugins" + File.separator + "BungeeUtilisals" + File.separator
			+ "tab.yml";
	public static FileManager tab = new FileManager(path);
	public static ArrayList<ScheduledTask> tasks = new ArrayList<>();

	public static void loadTab() {
		tab = null;
		tab = new FileManager(path);

		if (!tab.getFile().getKeys().contains("Tab")) {
			tab.getFile().set("Tab.Enabled", false);
			tab.getFile().set("Tab.UpdateTime-in-Milliseconds", 150);
			tab.getFile().set("Tab.Header",
					Arrays.asList("&6This server uses", "&eThis server uses", "&aThis server uses",
							"&2This server uses", "&6&lBungeeUtilisals", "&6You are on %server%",
							"&eYou are on %server%", "&aYou are on %server%", "&2You are on %server%"));
			tab.getFile().set("Tab.Footer",
					Arrays.asList("&6This server uses", "&4This server uses", "&cThis server uses",
							"&bThis server uses", "&3&lBungeeUtilisals", "&6Online players: %globalcount%",
							"&4Online players: %globalcount%", "&cOnline players: %globalcount%",
							"&bOnline players: %globalcount%"));
			tab.save();
		}

		if (tab.getFile().getBoolean("Tab.Enabled", false)) {
			TabUpdateTask tabUpdateTask = new TabUpdateTask();
			for (String s : tab.getFile().getStringList("Tab.Header")) {
				tabUpdateTask.addHeader(s);
			}
			for (String s : tab.getFile().getStringList("Tab.Footer")) {
				tabUpdateTask.addFooter(s);
			}
			int interval = tab.getFile().getInt("Tab.UpdateTime-in-Milliseconds", 150);
			ScheduledTask t = ProxyServer.getInstance().getScheduler().schedule(BungeeUtilisals.getInstance(),
					tabUpdateTask, interval, interval, TimeUnit.MILLISECONDS);
			tasks.add(t);
			ProxyServer.getInstance().getPluginManager().registerListener(BungeeUtilisals.getInstance(),
					new JoinListener());
		}
	}

	public static void reloadTabSystem() {
		for (ScheduledTask task : tasks) {
			task.cancel();
		}
		tasks.clear();
		loadTab();
	}

	public static void reloadTab() {
		tab = null;
		tab = new FileManager(path);
		reloadTabSystem();
	}
}
