package net.Zrips.CMILib;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Scanner;
import java.util.function.Consumer;

import org.bukkit.plugin.java.JavaPlugin;

import net.Zrips.CMILib.Version.Schedulers.CMIScheduler;

public class updateChecker {

    private JavaPlugin plugin;
    private int resourceId;

    public updateChecker(JavaPlugin plugin, int resourceId) {
        this.plugin = plugin;
        this.resourceId = resourceId;
    }

    public void getVersion(final Consumer<String> consumer) {
	CMIScheduler.get().runTaskAsynchronously(() -> {
	    try (InputStream inputStream = new URL("https://api.spigotmc.org/legacy/update.php?resource=" + this.resourceId).openStream(); Scanner scanner = new Scanner(inputStream)) {
		if (scanner.hasNext()) {
		    consumer.accept(scanner.next());
		}
	    } catch (IOException exception) {
		this.plugin.getLogger().info("Cannot look for updates: " + exception.getMessage());
	    }
	});
    }
}
