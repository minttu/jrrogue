package me.pieso.jrrogue.entity;

import java.awt.Color;
import java.awt.Graphics;
import me.pieso.jrrogue.core.ResourceManager;

public class Floor extends Entity {

    protected Entity ent;
    protected boolean seen;
    protected int lastLight;
    protected int lightLevel;
    protected static final int maxLight = 4;
    private boolean hasbeenseen;

    private int g;
    private int f;
    private Floor parent;
    private boolean routing;

    public Floor(int x, int y) {
        super(ResourceManager.getImage("floor"));
        move(x, y);
        this.ent = null;
        this.lightLevel = 0;
        this.lastLight = 0;
        this.seen = false;

        this.g = 0;
        this.f = 0;
        this.routing = false;
        this.parent = null;
    }

    public Floor getParent() {
        return parent;
    }

    public void setParent(Floor f) {
        this.parent = f;
    }

    public void setRouting(boolean bln) {
        this.routing = bln;
    }

    public boolean getPathing() {
        return this.routing;
    }

    public void cleanHeur() {
        this.g = 0;
        this.f = 0;
        this.parent = null;
    }

    public void setG(int g) {
        this.g = g;
    }

    public int getG() {
        return g;
    }

    public int getF(int ax, int ay) {
        f = this.g + getH(ax, ay);
        return f;
    }

    public int getH(int ax, int ay) {
        return Math.abs(x() - ax) + Math.abs(y() - ay);
    }

    public int lightLevel() {
        return lightLevel;
    }

    public void setLight(int frame, int i) {
        if (frame != lastLight) {
            lastLight = frame;
            lightLevel = 0;
        }
        if (i > lightLevel) {
            lightLevel = Math.min(maxLight, i);
        }
    }

    public boolean seen() {
        return seen;
    }

    public void setSeen(boolean bln) {
        if (bln) {
            this.hasbeenseen = true;
        }
        this.seen = bln;
    }

    public boolean set(Entity e) {
        if (ent == null) {
            ent = e;
            e.move(x(), y());
            return true;
        } else {
            return false;
        }
    }

    public Entity get() {
        return ent;
    }

    public boolean remove() {
        if (ent == null) {
            return false;
        }
        ent = null;
        return true;
    }

    public boolean remove(Entity e) {
        if (ent == null || !e.equals(ent)) {
            return false;
        }
        ent = null;
        return true;
    }

    @Override
    public void draw(Graphics g, int x, int y, int side) {
        if (!seen) {
            return;
        }
        super.draw(g, x, y, side);
        if (ent != null) {
            ent.draw(g, x, y, side);
        }
        if (routing) {
            g.setColor(new Color(0f, 1f, 0f, 0.5f));
            g.drawRect(x, y, side - 1, side - 1);
            g.drawRect(x + 2, y + 2, side - 5, side - 5);
        }
        lightLevel = Math.min(maxLight, lightLevel);
        g.setColor(new Color(0f, 0f, 0f, 1f - (float) ((float) lightLevel / (float) maxLight)));
        g.fillRect(x, y, side, side);
    }

    @Override
    public void bumpedBy(Entity e) {

    }

    @Override
    public void bumped(Entity e) {

    }

    public boolean hasBeenSeen() {
        return hasbeenseen;
    }

    @Override
    public String toString() {
        return "Floor (" + x() + ", " + y() + ")";
    }

}
