package net.Zrips.CMILib.Skins;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.net.ssl.HttpsURLConnection;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.YamlConfiguration;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import com.mojang.util.UUIDTypeAdapter;

import net.Zrips.CMILib.CMILib;
import net.Zrips.CMILib.FileHandler.ConfigReader;
import net.Zrips.CMILib.Logs.CMIDebug;
import net.Zrips.CMILib.Version.Schedulers.CMIScheduler;

public class SkinManager {
    public HashMap<UUID, CMISkin> skinCacheByUUID = new HashMap<UUID, CMISkin>();
    public HashMap<String, CMISkin> skinCacheByName = new HashMap<String, CMISkin>();

    protected HashMap<String, List<String>> preFetchNames = new HashMap<String, List<String>>();
    protected HashMap<String, UUID> preFetchUUIDs = new HashMap<String, UUID>();

    CMILib plugin;
    private long SkinUpdateTimer = 60;
    private long SkinRequestFrequency = 60;

    public SkinManager(CMILib plugin) {
	this.plugin = plugin;
    }

    public void loadConfig() {

	ConfigReader cfg = plugin.getConfigManager().getConfigFile();

	cfg.addComment("Skins.SkinUpdateTimer", "Defines time in minutes how often we want to update skin information from online Mojang servers",
	    "Keep in mind that your server can only send 1 request every minute, so keep it at a decent amount, hour or more",
	    "So if you have this set to 1 hour, then player skin information will be updated if player old skin information is older then 1 hour",
	    "This only triggers when player joins server or changes skin manually");
	SkinUpdateTimer = cfg.get("Skins.SkinUpdateTimer", 1320);
	SkinUpdateTimer = SkinUpdateTimer < 5 ? 5 : SkinUpdateTimer;

	cfg.addComment("Skins.SkinRequestFrequency", "Defines time in minutes how often we want to send requests to Mojang servers",
	    "This is to limit amount of requests in specific time to avoid clutter with possible requests");

	SkinRequestFrequency = cfg.get("Skins.SkinRequestFrequency", 10);
	SkinRequestFrequency = SkinRequestFrequency < 1 ? 1 : SkinRequestFrequency;
    }

    public void removeSkinFromCache(String playerName) {
	CMISkin skin = skinCacheByName.remove(playerName);
	if (skin != null)
	    skinCacheByUUID.remove(skin.getUuid());
    }

    public void removeSkinFromCache(UUID uuid) {
	CMISkin skin = skinCacheByUUID.remove(uuid);
	if (skin != null)
	    skinCacheByName.remove(skin.getName());
    }

    Long lastUpdateRequest = 0L;

    public boolean setSkin(GameProfile profile, UUID uuid) {

	if (checkCache(profile, uuid))
	    return true;
	try {

	    HttpsURLConnection connection = (HttpsURLConnection) new URL(String.format("https://sessionserver.mojang.com/session/minecraft/profile/%s?unsigned=false", uuid.toString().replace("-", "")))
		.openConnection();
	    if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
		InputStream stream = connection.getInputStream();
		BufferedReader buffer = new BufferedReader(new InputStreamReader(stream));

		try {
		    String reply = buffer.readLine();

		    String lines = "";
		    Iterator<String> iter = buffer.lines().iterator();
		    while (iter.hasNext()) {
			lines += iter.next();
		    }
		    reply = lines.replace(" ", "");

		    if (!reply.contains("name"))
			return checkCache(profile, uuid);

		    String playerName = reply.split("\"name\":\"")[1].split("\"")[0];
		    if (!reply.contains("value"))
			return checkCache(profile, uuid);

		    String skin = reply.split("\"value\":\"")[1].split("\"")[0];
		    if (!reply.contains("signature"))
			return checkCache(profile, uuid);

		    String signature = reply.split("\"signature\":\"")[1].split("\"")[0];

		    final CMISkin cmiSkin = new CMISkin(playerName, uuid, skin, signature);
		    cmiSkin.setLastUpdate(System.currentTimeMillis());
		    lastUpdateRequest = System.currentTimeMillis();
		    skinCacheByUUID.put(uuid, cmiSkin);
		    skinCacheByName.put(playerName, cmiSkin);
		    CMIScheduler.get().runTaskAsynchronously(new Runnable() {
			@Override
			public void run() {
			    if (!saving)
				save(cmiSkin);
			    return;
			}
		    });

		    if (profile != null) {
			profile.getProperties().removeAll("textures");
			profile.getProperties().put("textures", new Property("textures", skin, signature));
		    }
		} catch (Throwable ex) {
		    ex.printStackTrace();
		} finally {
		    buffer.close();
		    stream.close();
		    connection.disconnect();
		}
		return true;
	    }

	    if (checkCache(profile, uuid))
		return true;

	    System.out.println("Connection could not be opened (Response code " + connection.getResponseCode() + ", " + connection.getResponseMessage() + ")");
	    return false;
	} catch (IOException e) {
 	    e.printStackTrace();
	    return false;
	}

    }

    private boolean checkCache(GameProfile profile, UUID uuid) {
	if (profile == null)
	    return false;
	CMISkin cache = skinCacheByUUID.get(uuid);
	if (cache != null && cache.getSkin() != null && cache.getSignature() != null && cache.getLastUpdate() + (SkinUpdateTimer * 60 * 1000L) > System.currentTimeMillis()) {
	    profile.getProperties().removeAll("textures");
	    profile.getProperties().put("textures", new Property("textures", cache.getSkin(), cache.getSignature()));
	    return true;
	}
	return false;
    }

    public CMISkin getSkin(String name) {
	if (name == null)
	    return null;
	UUID onlineUUID = null;
	if (name.length() == 36) {
	    onlineUUID = UUID.fromString(name);
	} else {
	    CMISkin cache = skinCacheByName.get(name);
	    if (cache != null) {
		return cache;
	    }
	    if (!preFetchUUIDS().containsKey(name)) {
		onlineUUID = getUUID(name);
		preFetchUUIDS().put(name, onlineUUID);
	    } else {
		onlineUUID = preFetchUUIDS().get(name);
	    }
	}

	if (onlineUUID == null)
	    return null;

	CMISkin cache = skinCacheByUUID.get(onlineUUID);

	if (cache != null) {
	    return cache;
	}

	setSkin(null, onlineUUID);

	cache = skinCacheByUUID.get(onlineUUID);

	if (cache != null) {
	    return cache;
	}

	return null;
    }

    public UUID getUUID(String name) {
	UUID cached = preFetchUUIDS().get(name);
	if (cached != null)
	    return cached;
	String target = "https://api.mojang.com/users/profiles/minecraft/" + name;
//	String target = "https://api.mojang.com/user/profiles/a0d1fc8a35414a33bef8bb1c66cf2b9e/names";

	StringBuilder response = new StringBuilder();

	HttpURLConnection connection = null;
	try {
	    URL url = new URL(target);
	    connection = (HttpURLConnection) url.openConnection();
	    connection.setRequestMethod("GET");
	    connection.setUseCaches(false);
	    connection.setDoOutput(true);
	    connection.connect();

	    if (connection.getResponseCode() != 200) {
		return null;
	    }

	    InputStream is = connection.getInputStream();
	    BufferedReader rd = new BufferedReader(new InputStreamReader(is));
	    String line;
	    while ((line = rd.readLine()) != null) {
		response.append(line);
		response.append('\r').append('\n');
	    }
	    rd.close();

	} catch (Exception e) {
	    e.printStackTrace();
	} finally {
	    if (connection != null) {
		connection.disconnect();
	    }
	}

	UUID uuid = null;
	try {
	    Pattern pattern = Pattern.compile("(\")([a-zA-Z0-9]{32})(\")");
	    Matcher matcher = pattern.matcher(response.toString());
	    if (matcher.find()) {
		uuid = UUID.fromString(matcher.group(2).replaceFirst(
		    "([0-9a-fA-F]{8})([0-9a-fA-F]{4})([0-9a-fA-F]{4})([0-9a-fA-F]{4})([0-9a-fA-F]+)", "$1-$2-$3-$4-$5"));
	    }
	} catch (Exception e) {
	    try {
		String id = response.toString().split("\",\"")[0].split(":")[1].replace("\"", "").replaceFirst(
		    "([0-9a-fA-F]{8})([0-9a-fA-F]{4})([0-9a-fA-F]{4})([0-9a-fA-F]{4})([0-9a-fA-F]+)", "$1-$2-$3-$4-$5");
		uuid = UUID.fromString(id);
	    } catch (Exception ex) {
	    }
	}
	return uuid;
    }

    boolean saving = false;

    public void save(CMISkin skin) {
	saving = true;
	File file = new File(plugin.getDataFolder(), "skinsCache.yml");

	if (!file.exists()) {
	    try {
		file.createNewFile();
	    } catch (IOException e) {
		e.printStackTrace();
	    }
	}

	YamlConfiguration k = YamlConfiguration.loadConfiguration(file);

	String uuid = skin.getUuid().toString();
	k.set(uuid + ".name", skin.getName());
	k.set(uuid + ".skin", skin.getSkin());
	k.set(uuid + ".signature", skin.getSignature());
	k.set(uuid + ".lastUpdate", skin.getLastUpdate() == 0L ? null : skin.getLastUpdate());

	try {
	    k.save(file);
	} catch (IOException e) {
	    e.printStackTrace();
	}
	saving = false;
    }

    public void load() {
	try {
	    Long time = System.currentTimeMillis();
	    File file = new File(plugin.getDataFolder(), "skinsCache.yml");

	    if (!file.exists()) {
		try {
		    file.createNewFile();
		} catch (IOException e) {
		    e.printStackTrace();
		}
		return;
	    }

	    YamlConfiguration k = YamlConfiguration.loadConfiguration(file);

	    for (String key : k.getKeys(false)) {
		UUID uuid = UUID.fromString(key);
		if (uuid == null)
		    continue;
		String name = k.getString(key + ".name");
		String skin = k.getString(key + ".skin");
		String signature = k.getString(key + ".signature");
		Long lastUpdate = 0L;
		if (k.isLong(key + ".lastUpdate"))
		    lastUpdate = k.getLong(key + ".lastUpdate");
		CMISkin cmiSkin = new CMISkin(name, uuid, skin, signature);
		cmiSkin.setLastUpdate(lastUpdate);
		skinCacheByUUID.put(uuid, cmiSkin);
		skinCacheByName.put(name, cmiSkin);
	    }
	    plugin.loadMessage(skinCacheByUUID.size(), "skin cache entries", System.currentTimeMillis() - time);
	} catch (Exception e) {

	}
    }

    public HashMap<String, UUID> preFetchUUIDS() {
	return preFetchUUIDs;
    }
}
