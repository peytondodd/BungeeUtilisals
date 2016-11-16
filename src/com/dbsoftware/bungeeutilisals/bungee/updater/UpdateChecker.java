package com.dbsoftware.bungeeutilisals.bungee.updater;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.regex.Pattern;

import com.dbsoftware.bungeeutilisals.bungee.BungeeUtilisals;

public class UpdateChecker {

	private static String latestVersion;

	private static boolean checkHigher(String currentVersion, String newVersion) {
		String current = toReadable(currentVersion);
		String update = toReadable(newVersion);
		return current.compareTo(update) < 0;
	}

	public static void checkUpdate(String currentVersion) {
		String version = getSpigotVersion();
		if (version != null) {
			if (checkHigher(currentVersion, version)) {
				BungeeUtilisals.getInstance().update = true;
				latestVersion = version;
			}
		}
	}

	public static String getLatestVersion() {
		return latestVersion;
	}

	private static String getSpigotVersion() {
		try {
			HttpURLConnection con = (HttpURLConnection) new URL("http://www.spigotmc.org/api/general.php")
					.openConnection();
			con.setDoOutput(true);
			con.setRequestMethod("POST");
			con.getOutputStream()
					.write(("key=98BE0FE67F88AB82B4C197FAF1DC3B69206EFDCC4D3B80FC83A00037510B99B4&resource=7865")
							.getBytes("UTF-8"));
			String version = new BufferedReader(new InputStreamReader(con.getInputStream())).readLine();
			if (version.length() <= 7) {
				return version;
			}
		} catch (Exception ex) {
			System.out.print("[BungeeUtilisals] Failed to check for an update on Spigotmc.org.");
		}
		return null;
	}

	private static String toReadable(String version) {
		String[] split = Pattern.compile(".", Pattern.LITERAL).split(version.replace("v", ""));
		version = "";
		for (String s : split) {
			version += String.format("%4s", s);
		}
		return version;
	}
}