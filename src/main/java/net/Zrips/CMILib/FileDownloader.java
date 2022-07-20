package net.Zrips.CMILib;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.bukkit.Bukkit;

import net.Zrips.CMILib.Messages.CMIMessages;

public class FileDownloader {

    static List<String> validTypes = new ArrayList<String>(Arrays.asList("dat", "yml", "txt", "jar"));

    public void downloadUsingStream(final String urlStr, final String file, boolean inform) {
	try {
	    if (!validTypes.contains(file.split("\\.")[file.split("\\.").length - 1].toLowerCase()))
		return;
	} catch (Exception e) {
	    return;
	}
	Bukkit.getScheduler().runTaskAsynchronously(CMILib.getInstance(), new Runnable() {
	    @Override
	    public void run() {
		URL url;
		BufferedInputStream bis = null;
		FileOutputStream fis = null;
		try {
		    url = new URL(urlStr);
		    bis = new BufferedInputStream(url.openStream());
		    fis = new FileOutputStream(file);
		    byte[] buffer = new byte[1024];
		    int count = 0;
		    while ((count = bis.read(buffer, 0, 1024)) != -1) {
			fis.write(buffer, 0, count);
		    }
		    fis.close();
		    bis.close();
		    Bukkit.getScheduler().runTask(CMILib.getInstance(), () -> afterDownload());
		} catch (Throwable e) {
		    File f = new File(file);
		    if (inform) {
			CMIMessages.consoleMessage("Failed to download " + urlStr + " file into " + f.getParent() + File.separator + " folder ");
			CMIMessages.consoleMessage("You can do it manually or try again later or simply ignore it");
		    }
		    failedDownload();
		} finally {
		    if (fis != null) {
			try {
			    fis.close();
			} catch (IOException e) {
			    e.printStackTrace();
			}
		    }
		    if (bis != null) {
			try {
			    bis.close();
			} catch (IOException e) {
			    e.printStackTrace();
			}
		    }
		}

	    }
	});
    }

    public void afterDownload() {

    }

    public void failedDownload() {

    }

}
