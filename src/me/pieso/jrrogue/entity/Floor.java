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

    public Floor(int x, int y) {
        super(ResourceManager.getImage("floor"));
        move(x, y);
        this.ent = null;
        this.lightLevel = 0;
        this.lastLight = 0;
        this.seen = false;
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

}
