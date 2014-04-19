package me.pieso.jrrogue.entity;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

public abstract class Entity {

    private BufferedImage image;
    private int x;
    private int y;
    private boolean solid;

    public Entity(BufferedImage image) {
        this.image = image;
        this.solid = false;
    }

    public void draw(Graphics g, int x, int y, int side) {
        g.drawImage(image, x, y, side, side, null);
    }

    public void setImage(BufferedImage bi) {
        this.image = bi;
    }

    public void move(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public void setSolid(boolean bln) {
        this.solid = bln;
    }

    public boolean solid() {
        return solid;
    }

    public int x() {
        return x;
    }

    public int y() {
        return y;
    }

    public abstract void bumpedBy(Entity e);

    public abstract void bumped(Entity e);
}
