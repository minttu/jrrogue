package me.pieso.jrrogue.entity.pickup;

import me.pieso.jrrogue.entity.living.Living;
import java.util.Random;
import me.pieso.jrrogue.core.Game;
import me.pieso.jrrogue.core.ResourceManager;
import me.pieso.jrrogue.entity.Entity;

public class GoldPickup extends Pickup {

    public GoldPickup() {
        super(ResourceManager.getImage("gold"));
    }

    @Override
    public void bumpedBy(Entity e) {

    }

    @Override
    public void bumped(Entity e) {

    }

    @Override
    public String name() {
        return "a gold coin";
    }

    @Override
    public void tick(Game game) {

    }

}
