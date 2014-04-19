package me.pieso.jrrogue.entity;

import java.awt.Graphics;
import java.util.ArrayList;
import java.util.List;
import me.pieso.jrrogue.core.ResourceManager;

public class Floor extends Entity {

    private Entity ent;

    public Floor(int x, int y) {
        super(ResourceManager.getImage("floor"));
        move(x, y);
        this.ent = null;
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
        super.draw(g, x, y, side);
        if (ent != null) {
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
