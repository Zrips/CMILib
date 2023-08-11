package net.Zrips.CMILib;

import net.Zrips.CMILib.Messages.CMIMessages;
import net.Zrips.CMILib.Version.Schedulers.CMIScheduler;

import org.bukkit.Bukkit;
import org.jetbrains.annotations.NotNull;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Utility class to download files from the internet.
 */
public class FileDownloader {

    /**
     * Stores all valid file extensions that can be downloaded.
     */
    @NotNull
    static final List<String> VALID_TYPES = new ArrayList<>(Arrays.asList("yml", "txt", "jar"));

    /**
     * Downloads a file from the internet using a {@link BufferedInputStream}.
     *
     * @param urlString the URL of the file to download.
     * @param fileName  the path to save the downloaded file to, as well as the name.
     * @param inform    should the console be informed when the file is being downloaded?
     */
    public void downloadUsingStream(@NotNull final String urlString, @NotNull final String fileName, final boolean inform) {
        try {
            if (!VALID_TYPES.contains(fileName.split("\\.")[fileName.split("\\.").length - 1].toLowerCase()))
                return;
        } catch (final Exception e) {
            return;
        }

        CMIScheduler.get().runTaskAsynchronously(() -> {
            final URL url;
            BufferedInputStream bufferedInputStream = null;
            FileOutputStream fileOutputStream = null;

            try {
                url = new URL(urlString);
                bufferedInputStream = new BufferedInputStream(url.openStream());
                fileOutputStream = new FileOutputStream(fileName);
                final byte[] buffer = new byte[1024];
                int count;

                while ((count = bufferedInputStream.read(buffer, 0, 1024)) != -1)
                    fileOutputStream.write(buffer, 0, count);

                fileOutputStream.close();
                bufferedInputStream.close();
                CMIScheduler.runTask(FileDownloader.this::afterDownload);
            } catch (final Throwable e) {
                final File file = new File(fileName);

                if (inform) {
                    CMIMessages.consoleMessage("Failed to download file '" + urlString + "' to folder '" + file.getParent() + File.separator + "'.");
                    CMIMessages.consoleMessage("You can do it manually, try again later or simply ignore it.");
                }

                FileDownloader.this.failedDownload();
            } finally {
                if (fileOutputStream != null)
                    try {
                        fileOutputStream.close();
                    } catch (final IOException e) {
                        e.printStackTrace();
                    }

                if (bufferedInputStream != null)
                    try {
                        bufferedInputStream.close();
                    } catch (final IOException e) {
                        e.printStackTrace();
                    }
            }
        });
    }

    /**
     * Called when a download is successful.
     */
    public void afterDownload() {
    }

    /**
     * Called when a download fails.
     */
    public void failedDownload() {
    }
}
