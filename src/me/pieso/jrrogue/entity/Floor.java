package me.pieso.jrrogue.entity;

import java.awt.Color;
import java.awt.Graphics;
import me.pieso.jrrogue.core.ResourceManager;

public class Floor extends Entity {

    protected Entity ent;
    protected boolean foggy;
    protected boolean seen;
    public static final Color fog = new Color(.0f, .0f, .0f, .5f);

    public Floor(int x, int y) {
        super(ResourceManager.getImage("floor"));
        move(x, y);
        this.ent = null;
        this.foggy = true;
        this.seen = false;
    }

    public boolean foggy() {
        return foggy;
    }

    public void setFoggy(boolean bln) {
        this.foggy = bln;
    }

    public boolean seen() {
        return seen;
    }

    public void setSeen(boolean bln) {
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
        if (foggy) {
            g.setColor(fog);
            g.fillRect(x, y, side, side);
        } else if (ent != null) {
            ent.draw(g, x, y, side);
        }
    }

    @Override
    public void bumpedBy(Entity e) {

    }

    @Override
    public void bumped(Entity e) {

    }

}
