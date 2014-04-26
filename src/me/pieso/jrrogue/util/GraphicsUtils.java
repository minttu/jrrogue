package me.pieso.jrrogue.util;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;

public class GraphicsUtils {

    public static void drawTextOutlined(Graphics g, Rectangle rec, String s, Color fg, Color bg) {
        g.setColor(bg);
        g.drawString(s, rec.x - 1, rec.y - 1);
        g.drawString(s, rec.x - 1, rec.y + 1);
        g.drawString(s, rec.x + 1, rec.y - 1);
        g.drawString(s, rec.x + 1, rec.y + 1);
        g.setColor(fg);
        g.drawString(s, rec.x, rec.y);
    }

}
