package me.pieso.jrrogue.entity.pickup;

import java.awt.image.BufferedImage;
import me.pieso.jrrogue.core.Game;
import me.pieso.jrrogue.core.ResourceManager;
import me.pieso.jrrogue.entity.Entity;

public class ArmorPickup extends Pickup {

    public ArmorPickup() {
        super(ResourceManager.getImage("armor"));
    }

    @Override
    public String name() {
        return "a armor upgrade";
    }

    @Override
    public void tick(Game game) {

    }

    @Override
    public void bumpedBy(Entity e) {

    }

    @Override
    public void bumped(Entity e) {

    }

}
