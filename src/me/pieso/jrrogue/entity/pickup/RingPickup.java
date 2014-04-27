package me.pieso.jrrogue.entity.pickup;

import me.pieso.jrrogue.core.Game;
import me.pieso.jrrogue.core.ResourceManager;
import me.pieso.jrrogue.entity.Entity;

public class RingPickup extends Pickup {

    public RingPickup() {
        super(ResourceManager.getImage("ring"));
    }

    @Override
    public String name() {
        return "a ring upgrade";
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
