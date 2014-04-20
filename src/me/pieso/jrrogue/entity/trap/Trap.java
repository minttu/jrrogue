package me.pieso.jrrogue.entity.trap;

import me.pieso.jrrogue.entity.living.Player;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import me.pieso.jrrogue.core.ResourceManager;
import me.pieso.jrrogue.entity.Entity;
import me.pieso.jrrogue.entity.Floor;

public abstract class Trap extends Floor {

    private final BufferedImage trapimg;

    public Trap(int x, int y) {
        super(x, y);
        this.trapimg = ResourceManager.getImage("trap");
        setImage(trapimg);
    }

    public Trap(int x, int y, BufferedImage trapimg) {
        super(x, y);
        this.trapimg = trapimg;
        setImage(trapimg);
    }

    public abstract String name();

    @Override
    public boolean set(Entity e) {
        boolean b = super.set(e);
        if (b) {
            steppedOnBy(e);
            if (e instanceof Player) {
                ((Player) e).addStatus("You stepped on the", name());
            }
        }
        return b;
    }

    public abstract void steppedOnBy(Entity e);

    public abstract boolean usedBy(Entity e);

}
