package me.pieso.jrrogue.entity.pickup;

import me.pieso.jrrogue.core.Game;
import me.pieso.jrrogue.core.ResourceManager;
import me.pieso.jrrogue.entity.Entity;

public class FoodPickup extends Pickup {

    public FoodPickup() {
        super(ResourceManager.getImage("food"));
    }

    @Override
    public void bumpedBy(Entity e) {

    }

    @Override
    public void bumped(Entity e) {

    }

    @Override
    public String name() {
        return "an apple";
    }

    @Override
    public void tick(Game game) {

    }

}
