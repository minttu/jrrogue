package me.pieso.jrrogue.core;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import javax.imageio.ImageIO;

public class ResourceManager {

    private static final Map<String, BufferedImage> images = new HashMap<>();

    public static final Color DGRAY = new Color(1f, 1f, 1f, 0.25f);
    public static final Color CGRAY = new Color(1f, 1f, 1f, .5f);

    public static void loadImage(String name) throws IOException {
        loadImage(name, "/resources/" + name + ".png");
    }

    public static void loadImage(String name, String filename) throws IOException {
        URL file = ResourceManager.class.getResource(filename);
        if (file == null) {
            throw new IOException();
        }
        BufferedImage image = ImageIO.read(new File(file.getFile()));
        images.put(name, image);
    }

    public static void setImage(String name, BufferedImage img) {
        images.put(name, img);
    }

    public static BufferedImage tint(BufferedImage img, Color clr) {
        BufferedImage ret = new BufferedImage(img.getWidth(), img.getHeight(), BufferedImage.TRANSLUCENT);
        ret.coerceData(true);
        Graphics2D g = ret.createGraphics();
        g.drawImage(img, 0, 0, null);
        g.dispose();
        for (int y = 0; y < ret.getHeight(); y++) {
            for (int x = 0; x < ret.getWidth(); x++) {
                if (ret.getRGB(x, y) == Color.WHITE.getRGB()) {
                    ret.setRGB(x, y, clr.getRGB());
                }
            }
        }
        return ret;
    }

    public static BufferedImage getImage(String name) {
        BufferedImage image = images.get(name);
        if (image == null) {
            try {
                loadImage(name);
            } catch (IOException ex) {
                try {
                    loadImage(name, "/resources/missing.png");
                } catch (IOException ex1) {
                    return null;
                }
            }
            image = images.get(name);
        }
        return image;
    }
}
