package net.Zrips.CMILib.Skins;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.UUID;

import javax.imageio.ImageIO;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import net.Zrips.CMILib.CMILib;
import net.Zrips.CMILib.Logs.CMIDebug;
import net.Zrips.CMILib.Version.Schedulers.CMIScheduler;

public class CMISkin {

    private String name;
    private UUID uuid;
    private String skin;
    private String signature;
    private Long lastUpdate = 0L;
    private BufferedImage skinBuffer = null;

    public CMISkin(String name, UUID uuid, String skin, String signature) {
        this.name = name;
        this.uuid = uuid;
        this.skin = skin;
        this.signature = signature;
    }

    public String getSkin() {
        return skin;
    }

    public void setSkin(String skin) {
        this.skin = skin;
    }

    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public UUID getUuid() {
        return uuid;
    }

    public void setUuid(UUID uuid) {
        this.uuid = uuid;
    }

    public Long getLastUpdate() {
        return lastUpdate;
    }

    public void setLastUpdate(Long lastUpdate) {
        this.lastUpdate = lastUpdate;
    }

    public BufferedImage getSkinBuffer() {

        if (skinBuffer != null && getLastUpdate() + (CMILib.getInstance().getSkinManager().getSkinUpdateTimer() * 60 * 1000L) > System.currentTimeMillis()) {
            return skinBuffer;
        }

        skinBuffer = getSkinTexture();

        return skinBuffer;
    }

    public void setSkinBuffer(BufferedImage skinBuffer) {
        this.skinBuffer = skinBuffer;
    }

    private boolean isEmpty(BufferedImage img) {
        int w = img.getWidth();
        int h = img.getHeight();

        for (int y = 0; y < h; y++) {
            for (int x = 0; x < w; x++) {
                int rgba = img.getRGB(x, y);
                if ((rgba >>> 24) != 0) {
                    return false;
                }
            }
        }
        return true;
    }

    public BufferedImage getHeadSkin() {

        BufferedImage skinImage = getSkinBuffer();

        if (skinImage == null)
            return null;

        try {
            // Base head layer
            BufferedImage head = skinImage.getSubimage(8, 8, 8, 8);

            // Final combined 8x8 head
            BufferedImage bimg = new BufferedImage(8, 8, BufferedImage.TYPE_INT_ARGB);

            Graphics2D g = bimg.createGraphics();

            g.drawImage(head, 0, 0, null);

            g.dispose();

            if (isEmpty(bimg))
                return null;

            return bimg;
        } catch (Throwable e) {
            e.printStackTrace();
        }

        return null;
    }

    public BufferedImage getHelmetSkin() {

        BufferedImage skinImage = getSkinBuffer();
        if (skinImage == null)
            return null;

        try {
            // Base head layer
            BufferedImage head = skinImage.getSubimage(8, 8, 8, 8);

            // Hat/overlay layer
            BufferedImage overlay = skinImage.getSubimage(40, 8, 8, 8);

            // Final combined 8x8 head
            BufferedImage bimg = new BufferedImage(8, 8, BufferedImage.TYPE_INT_ARGB);

            Graphics2D g = bimg.createGraphics();

            g.drawImage(head, 0, 0, null);
            g.drawImage(overlay, 0, 0, null);

            g.dispose();

            if (isEmpty(bimg))
                return null;

            return bimg;

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    private BufferedImage getSkinTexture() {

        if (skinBuffer != null && getLastUpdate() + (1000L * 60 * 60) > System.currentTimeMillis()) {
            return skinBuffer;
        }

        String base64 = getSkin();

        if (base64 == null || base64.isEmpty())
            return null;

        try {
            String decoded = new String(Base64.getDecoder().decode(base64), StandardCharsets.UTF_8);

            JsonObject json = JsonParser.parseString(decoded).getAsJsonObject();
            JsonObject textures = json.getAsJsonObject("textures");

            if (textures == null || !textures.has("SKIN"))
                return null;

            JsonObject skinObj = textures.getAsJsonObject("SKIN");

            if (!skinObj.has("url"))
                return null;

            String skinUrl = skinObj.get("url").getAsString();

            setSkinBuffer(getImage(new URL(skinUrl)));

        } catch (Exception e) {
            e.printStackTrace();
        }

        return skinBuffer;
    }

    private static BufferedImage getImage(URL url) {
        if (url == null) {
            return null;
        }
        InputStream stream = null;
        BufferedImage res = null;
        try {
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            stream = connection.getInputStream();
            res = ImageIO.read(stream);
        } catch (Throwable e) {

        } finally {
            if (stream != null)
                try {
                    stream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
        }
        return res;
    }
}
