package me.pieso.jrrogue.core;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import javax.imageio.ImageIO;

public class ResourceManager {

    private static final Map<String, BufferedImage> images = new HashMap<>();

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
