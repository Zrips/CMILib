package net.Zrips.CMILib.Images;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;

import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.Zrips.CMI.CMI;

import net.Zrips.CMILib.CMILib;
import net.Zrips.CMILib.Colors.CMIChatColor;
import net.Zrips.CMILib.Logs.CMIDebug;
import net.Zrips.CMILib.RawMessages.RawMessage;

public class CMIImage {

    public static String imageIndicator = "img:";
    public static String imageTextIndicator = "imgtext:";

    public static String imageFiller = "⬛";
    public static String imageEmptyFiller = CMIChatColor.GRAY + "_|";

    private static String[][] steveHeadRaw = {
        { "{#2f200d}", "{#2b1e0d}", "{#2f1f0f}", "{#281c0b}", "{#241808}", "{#261a0a}", "{#2b1e0d}", "{#2a1d0d}" },
        { "{#2b1e0d}", "{#2b1e0d}", "{#2b1e0d}", "{#332411}", "{#422a12}", "{#3f2a15}", "{#2c1e0e}", "{#281c0b}" },
        { "{#2b1e0d}", "{#b6896c}", "{#bd8e72}", "{#c69680}", "{#bd8b72}", "{#bd8e74}", "{#ac765a}", "{#342512}" },
        { "{#aa7d67}", "{#b4846d}", "{#aa7d66}", "{#ad806d}", "{#9c725c}", "{#bb8972}", "{#9c694c}", "{#9c694c}" },
        // herobrine type head
        { "{#b4846d}", "{#white}", "{#white}", "{#b57b67}", "{#bb8972}", "{#white}", "{#white}", "{#aa7d66}" },
//	{ "{#b4846d}", "{#white}", "{#523d89}", "{#b57b67}", "{#bb8972}", "{#523d89}", "{#white}", "{#aa7d66}" },
        { "{#9c6346}", "{#b37b62}", "{#b78272}", "{#6a4030}", "{#6a4030}", "{#be886c}", "{#a26a47}", "{#805334}" },
        { "{#905e43}", "{#965f40}", "{#40200a}", "{#874a3a}", "{#874a3a}", "{#40200a}", "{#8f5e3e}", "{#815339}" },
        { "{#6f452c}", "{#6d432b}", "{#40200a}", "{#40200a}", "{#40200a}", "{#40200a}", "{#83553b}", "{#7a4e33}" },
    };

    private static Color[][] steveHead = new Color[8][8];

    static {
        for (int i = 0; i < steveHeadRaw.length; i++) {
            for (int y = 0; y < steveHeadRaw[0].length; y++) {
                steveHead[i][y] = CMIChatColor.getColor(steveHeadRaw[i][y]).getJavaColor();
            }
        }
    }

    private static ConcurrentHashMap<String, CMIImage> cache = new ConcurrentHashMap<String, CMIImage>();

    public ArrayList<BufferedImage> getGifFrames(File gif) throws IOException {
        ArrayList<BufferedImage> frames = new ArrayList<BufferedImage>();
        ImageInputStream stream = null;
        try {
            ImageReader reader = ImageIO.getImageReadersByFormatName("gif").next();
            stream = ImageIO.createImageInputStream(gif);
            reader.setInput(stream);
            int count = reader.getNumImages(true);
            for (int index = 0; index < count; index++) {
                BufferedImage frame = reader.read(index);
                frames.add(frame);
            }
        } catch (IOException ex) {
        } finally {
            if (stream != null)
                stream.close();
        }
        return frames;
    }

    String name;
    private Color[][] array;
    private String[] stringArray;
    private long time = 0L;

    public CMIImage(String name, Color[][] array) {
        this.name = name;
        this.array = array;
        this.time = System.currentTimeMillis();
    }

    public static String[][] colorToHexPlain(Color[][] colors) {
        String[][] result = new String[colors.length][colors[0].length];
        for (int i = 0; i < colors.length; i++) {
            for (int y = 0; y < colors[0].length; y++) {
                result[i][y] = Integer.toHexString(colors[i][y].getRGB()).substring(2);
            }
        }
        return result;
    }

    public static String[][] colorToHexTranslated(Color[][] colors) {
        String[][] result = new String[colors.length][colors[0].length];
        for (int i = 0; i < colors.length; i++) {
            for (int y = 0; y < colors[0].length; y++) {
                String er = Integer.toHexString(colors[i][y].getRGB());
                if (er.length() > 7) {
                    er = er.substring(2);
//		    if (Version.isCurrentLower(Version.v1_16_R1)) {
//			String closest = CMIChatColor.getClosestVanilla(er);
//			result[i][y] = CMIChatColor.translate(closest);
//		    } else
                    result[i][y] = CMIChatColor.translate("{#" + er + "}");
                } else {
                    result[i][y] = "";
                }
            }
        }
        return result;
    }

    public static Color[][] imageToArray(String baseFolder, String specification) {
        return imageToArray(baseFolder, specification, 8, 0);
    }

    private static List<String> failed = new ArrayList<String>();

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

//	    if (failed.size() > 200)
//		failed.remove(0);
//
//	    if (!failed.contains(url.toString())) {
//		CMIMessages.consoleMessage("Failed to retrieve image from URL (" + url + ")");
//		failed.add(url.toString());
//	    }

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

    public static String[] imageToSingleArray(String baseFolder, String specification, int height, int width) {

        CMIImage old = cache.get(specification);
        if (old != null && old.getTime() + (1000L * 60 * 60) > System.currentTimeMillis() && old.getStringArray() != null) {
            return old.getStringArray();
        }

        Color[][] ar = imageToArray(baseFolder, specification, height, width);

        String[][] colorArray = CMIImage.colorToHexTranslated(ar);

        String[] array = new String[colorArray.length];

        for (int i = 0; i < colorArray.length; i++) {
            StringBuilder str = new StringBuilder();

            String prevColor = "";

            for (int y = 0; y < colorArray[0].length; y++) {
                if (colorArray[i][y].isEmpty()) {
                    str.append(imageEmptyFiller);
                    prevColor = "";
                } else {
                    if (prevColor.equals(colorArray[i][y]))
                        str.append(imageFiller);
                    else {
                        str.append(colorArray[i][y] + imageFiller);
                        prevColor = colorArray[i][y];
                    }
                }
            }
            array[i] = CMIChatColor.translate(str.toString());
        }

        old = cache.get(specification);

        if (old != null) {
            old.setStringArray(array);
        }

        return array;
    }

    public static Color[][] imageToArray(String baseFolder, String specification, int height, int width) {
        try {
            String sp = specification;
            CMIImage old = cache.get(specification);
            if (old != null && old.getTime() + (1000L * 60 * 60) > System.currentTimeMillis()) {
                return old.getArray();
            }

            if (specification.contains(" ")) {
                String[] split = specification.split(" ");

                try {
                    height = Integer.parseInt(split[1]);
                } catch (Throwable e) {
                }

                if (split.length > 2)
                    try {
                        width = Integer.parseInt(split[2]);
                    } catch (Throwable e) {
                    }

                specification = specification.split(" ")[0];
            }

            BufferedImage bimg = null;

            if (specification.startsWith("img:head:")) {
                String headName = specification.substring("img:head:".length());
                headName = headName.split(" ")[0];
                bimg = getImage(new URL("https://minotar.net/avatar/" + headName + "/8.png"));
                if (bimg == null)
                    bimg = getImage(new URL("https://minepic.org/avatar/" + headName + "/8"));
            } else if (specification.startsWith("img:helmet:")) {
                String line = specification.substring("img:helmet:".length());
                String headName = line.split(" ")[0];
                bimg = getImage(new URL("https://minepic.org/avatar/8/" + headName));
                if (bimg == null)
                    bimg = getImage(new URL("https://minotar.net/helm/" + headName + "/8.png"));
            } else if (specification.startsWith("img:body:")) {
                String line = specification.substring("img:body:".length());
                String headName = line.split(" ")[0];
                bimg = getImage(new URL("https://mc-heads.net/player/" + headName + "/64"));
                if (bimg == null)
                    bimg = getImage(new URL("https://minepic.org/skin/" + headName + "/64"));
            } else if (specification.startsWith("img:iso:")) {
                String line = specification.substring("img:iso:".length());
                String headName = line.split(" ")[0];
                bimg = getImage(new URL("https://mc-heads.net/head/" + headName + "/64"));
                if (bimg == null)
                    bimg = getImage(new URL("https://minepic.org/head/" + headName + "/64"));
            } else if (specification.startsWith("img:bust:")) {
                String line = specification.substring("img:bust:".length());
                String headName = line.split(" ")[0];
                bimg = getImage(new URL("https://minotar.net/armor/bust/" + headName + "/64"));
            } else if (specification.startsWith("img:")) {
                String path = specification.substring("img:".length());
                path = path.split(" ")[0];
                if (baseFolder != null && !baseFolder.endsWith(File.separator))
                    baseFolder += File.separator;
                if (baseFolder == null)
                    baseFolder = CMILib.getInstance().getDataFolder().getPath() + File.separator;

                File file = new File(baseFolder + path);

                if (file.isFile()) {
                    bimg = ImageIO.read(new File(baseFolder + path));
                }
            }

            Color[][] arr = steveHead;
            if (bimg != null)
                arr = toArray(bimg, height, width);

            CMIImage image = new CMIImage(specification, arr);
            cache.put(sp, image);

            return arr;

//	    BufferedImage hugeImage = ImageIO.read(new File(urlS));
//	    Color[][] arr = toArray(hugeImage, height, width);
//	    CMIImage image = new CMIImage(urlS, arr);
//	    cache.put(urlS, image);
//	    return arr;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return steveHead;
    }

    public static Color[][] imageToArray(String url, double percentage) {
        try {
            BufferedImage hugeImage = ImageIO.read(new File(url));

            int height = (int) (hugeImage.getHeight() * percentage / 100D);
            int width = (int) (hugeImage.getWidth() * percentage / 100D);

            return toArray(hugeImage, height, width);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new Color[0][0];
    }

    public static Color[][] toArray(BufferedImage image) {
        return toArray(image, 0, 0);
    }

    public static Color[][] toArray(BufferedImage image, int height, int width) {

        if (height > 0 || width > 0) {
            image = resize(image, height, width);
        }

        byte[] pixels = ((DataBufferByte) image.getRaster().getDataBuffer()).getData();
        final int w = image.getWidth();
        final int h = image.getHeight();
        final boolean hasAlphaChannel = image.getAlphaRaster() != null;

        Color[][] result = new Color[h][w];
        if (hasAlphaChannel) {
            final int pixelLength = 4;
            for (int pixel = 0, row = 0, col = 0; pixel + 3 < pixels.length; pixel += pixelLength) {

                result[row][col] = new Color(pixels[pixel + 3] & 0xff, pixels[pixel + 2] & 0xff, pixels[pixel + 1] & 0xff, pixels[pixel] & 0xff);
                col++;
                if (col == w) {
                    col = 0;
                    row++;
                }
            }
        } else {
            final int pixelLength = 3;
            for (int pixel = 0, row = 0, col = 0; pixel + 2 < pixels.length; pixel += pixelLength) {
                result[row][col] = new Color(pixels[pixel + 3] & 0xff, pixels[pixel + 2] & 0xff, pixels[pixel + 1] & 0xff, 255);
                col++;
                if (col == w) {
                    col = 0;
                    row++;
                }
            }
        }

        return result;
    }

    public static BufferedImage resize(BufferedImage image, int height, int width) {
        int ww = image.getWidth();
        int hh = image.getHeight();

        if (height <= 0) {
            double percent = width * 100D / ww;
            height = (int) (percent * hh / 100);
        }
        if (width <= 0) {
            double percent = (height * 100D / hh);
            width = (int) (percent * ww / 100);
        }

        BufferedImage img = new BufferedImage(width, height, BufferedImage.TYPE_4BYTE_ABGR);
        int x, y;

        int[] ys = new int[height];
        for (y = 0; y < height; y++)
            ys[y] = y * hh / height;
        for (x = 0; x < width; x++) {
            int newX = x * ww / width;
            for (y = 0; y < height; y++) {
                int col = image.getRGB(newX, ys[y]);
                img.setRGB(x, y, col);
            }
        }
        return img;
    }

    private static RawMessage processImagePart(CommandSender sender, String baseFolder, String image, List<String> imageText) {

        RawMessage rm = new RawMessage();
        String[] sa = imageToSingleArray(baseFolder, image, 8, 0);

        for (int i = 0; i < sa.length; i++) {
            if (imageText.size() > i) {
                String textLine = imageText.get(i);
                if (textLine.contains("<T>")) {
                    String text = imageText.get(i);
                    if (text.startsWith(" ")) {
                        text = text.substring(1);
                        text = "<T> </T>" + text;
                    }

                    rm.addRM(RawMessage.translateTextOnlyRawMessage("<T>" + sa[i] + "</T>" + text));
                } else {
                    rm.addRM(RawMessage.translateTextOnlyRawMessage("<T>" + sa[i] + imageText.get(i)));
                }
            } else {
                if (i > 0)
                    rm.addText("\n");
                rm.addText(sa[i]);
            }
        }

        return rm;
    }

    private static List<String> processImagePartString(String baseFolder, String image, List<String> imageText) {
        List<String> rm = new ArrayList<String>();

        String[] sa = imageToSingleArray(baseFolder, image, 8, 0);

        boolean firstJson = true;
        for (int i = 0; i < sa.length; i++) {

            if (imageText.size() > i) {
                String textLine = imageText.get(i);
                if (textLine.contains("<T>")) {
                    if (firstJson) {
                        firstJson = false;
                    }
                    rm.add(sa[i] + imageText.get(i));
                } else {
                    rm.add(sa[i] + imageText.get(i));
                }
            } else {
                rm.add(sa[i]);
            }
        }

        return rm;
    }

    private static RawMessage processLines(CommandSender sender, String baseFolder, List<String> testLs) {
        RawMessage rm = new RawMessage();

        String image = null;

        List<String> imageText = new ArrayList<String>();
        for (int i = 0; i < testLs.size() && i >= 0; i++) {
            String one = testLs.get(i);
            if (image == null) {
                if (!one.startsWith(imageIndicator)) {

                    rm.addRM(RawMessage.translateTextOnlyRawMessage(!one.contains("<T>") ? "<T>" + one + "</T>" : one));
                    testLs.remove(i);
                    i--;
                    continue;
                }
                image = one;
                testLs.remove(i);
                i--;
                continue;
            }
            if (!one.startsWith(imageTextIndicator)) {
                break;
            }
            String text = one.substring(imageTextIndicator.length());
            testLs.remove(i);
            i--;
            imageText.add(text);

        }

        if (image != null) {
            rm.addRM(processImagePart(sender, baseFolder, image, imageText));
        }

        if (!testLs.isEmpty()) {
            rm.addRM(processLines(sender, baseFolder, testLs));
        }

        return rm;
    }

    private static List<String> processStringLines(CommandSender sender, String baseFolder, List<String> testLs) {
        List<String> rm = new ArrayList<String>();

        String image = null;

        List<String> imageText = new ArrayList<String>();
        for (int i = 0; i < testLs.size() && i >= 0; i++) {
            String one = testLs.get(i);
            if (image == null) {
                if (!one.startsWith(imageIndicator)) {
                    rm.add(one);
                    testLs.remove(i);
                    i--;
                    continue;
                }
                image = one;
                testLs.remove(i);
                i--;
                continue;
            }
            if (!one.startsWith(imageTextIndicator)) {
                break;
            }
            String text = one.substring(imageTextIndicator.length());
            testLs.remove(i);
            i--;
            imageText.add(text);
        }
        if (image != null)
            rm.addAll(processImagePartString(baseFolder, image, imageText));

        if (!testLs.isEmpty()) {
            rm.addAll(processStringLines(sender, baseFolder, testLs));
        }

        return rm;
    }

    public static CompletableFuture<RawMessage> convertIntoRawMessage(CommandSender sender, String baseFolder, List<String> testLs, boolean updatePlaceholders) {
        Player player = null;

        ArrayList<String> lines = new ArrayList<String>(testLs);

        if (updatePlaceholders) {
            if (sender instanceof Player)
                player = (Player) sender;
            for (int i = 0; i < lines.size(); i++) {
                if (player != null)
                    lines.set(i, CMI.getInstance().getPlaceholderAPIManager().updatePlaceHolders(player, lines.get(i)));
            }
        }

        return CompletableFuture.supplyAsync(() -> processLines(sender, baseFolder, lines));
    }

    public static CompletableFuture<List<String>> convertLines(CommandSender sender, String baseFolder, List<String> testLs, boolean updatePlaceholders) {
        Player player = null;

        ArrayList<String> lines = new ArrayList<String>(testLs);

        if (updatePlaceholders) {
            if (sender instanceof Player)
                player = (Player) sender;
            for (int i = 0; i < lines.size(); i++) {
                if (player != null)
                    lines.set(i, CMI.getInstance().getPlaceholderAPIManager().updatePlaceHolders(player, lines.get(i)));
            }
        }

        return CompletableFuture.supplyAsync(() -> processStringLines(sender, baseFolder, lines));
    }

    public Color[][] getArray() {
        return array;
    }

    public void setArray(Color[][] array) {
        this.array = array;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public String[] getStringArray() {
        return stringArray;
    }

    public void setStringArray(String[] stringArray) {
        this.stringArray = stringArray;
    }
}
