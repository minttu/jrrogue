package me.pieso.jrrogue.util;

import java.awt.Rectangle;
import java.util.Random;

public class BSP {

    public static final int MINWIDTH = 8;
    public static final int MINHEIGHT = 8;
    public static final int NOSPLIT = -1;
    public static final int HSPLIT = 0;
    public static final int VSPLIT = 1;

    private BSP b1;
    private BSP b2;
    private final int width;
    private final int height;
    private final int x;
    private final int y;
    private int split;

    public BSP(int x, int y, int width, int height) {
        this.width = width;
        this.height = height;
        this.x = x;
        this.y = y;
        this.split = NOSPLIT;
        this.b1 = null;
        this.b2 = null;
        System.out.println("x: " + x + " y: " + y + " width: " + width + " height: " + height);
    }

    public BSP first() {
        return b1;
    }

    public BSP second() {
        return b2;
    }

    public int width() {
        return width;
    }

    public int height() {
        return height;
    }

    public int x() {
        return x;
    }

    public int y() {
        return y;
    }

    public int whatSplit() {
        return split;
    }

    public Rectangle rectangle() {
        return new Rectangle(x, y, width, height);
    }

    private boolean vsplit() {
        int slack = width - MINWIDTH * 2;
        if (slack < 0) {
            if (new Random().nextBoolean()) {
                return hsplit();
            } else {
                return false;
            }
        } else if (slack == 0) {
            b1 = new BSP(x, y, MINWIDTH, height);
            b2 = new BSP(x + MINWIDTH, y, MINWIDTH, height);
        } else {
            int splitp = MINWIDTH + new Random().nextInt(slack);
            b1 = new BSP(x, y, splitp, height);
            b2 = new BSP(x + splitp, y, width - splitp, height);
        }
        split = VSPLIT;
        return true;
    }

    private boolean hsplit() {
        int slack = height - MINHEIGHT * 2;
        if (slack < 0) {
            if (new Random().nextBoolean()) {
                return vsplit();
            } else {
                return false;
            }
        } else if (slack == 0) {
            b1 = new BSP(x, y, width, MINHEIGHT);
            b2 = new BSP(x, y + MINHEIGHT, width, MINHEIGHT);
        } else {
            int splitp = MINHEIGHT + new Random().nextInt(slack);
            b1 = new BSP(x, y, width, splitp);
            b2 = new BSP(x, y + splitp, width, height - splitp);
        }
        split = HSPLIT;
        return true;
    }

    public boolean split() {
        if (width < MINWIDTH || height < MINHEIGHT || b1 != null || b2 != null) {
            return false;
        }
        if (new Random().nextBoolean()) {
            return hsplit();
        } else {
            return vsplit();
        }
    }
}
