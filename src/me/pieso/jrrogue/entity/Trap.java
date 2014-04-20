package me.pieso.jrrogue.entity;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import me.pieso.jrrogue.core.ResourceManager;
import static me.pieso.jrrogue.entity.Floor.fog;

public abstract class Trap extends Floor {

    private final BufferedImage trapimg;

    public Trap(int x, int y) {
        super(x, y);
        this.trapimg = ResourceManager.getImage("trap");
    }

    public Trap(int x, int y, BufferedImage trapimg) {
        super(x, y);
        this.trapimg = trapimg;
    }

    @Override
    public boolean set(Entity e) {
        boolean b = super.set(e);
        steppedOnBy(e);
        return b;
    }

    @Override
    public void draw(Graphics g, int x, int y, int side) {
        if (!seen) {
            return;
        }
        super.draw(g, x, y, side);
        g.drawImage(trapimg, x, y, side, side, null);
        if (foggy) {
            g.setColor(fog);
            g.fillRect(x, y, side, side);
        } else if (ent != null) {
            ent.draw(g, x, y, side);
        }
    }

    public abstract void steppedOnBy(Entity e);

}
