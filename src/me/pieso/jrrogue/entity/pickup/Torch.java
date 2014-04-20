package me.pieso.jrrogue.entity.pickup;

import me.pieso.jrrogue.core.Game;
import me.pieso.jrrogue.core.ResourceManager;
import me.pieso.jrrogue.entity.Entity;

public class Torch extends Pickup {

    private int ttl;

    public Torch() {
        super(ResourceManager.getImage("torch"));
        this.ttl = 50;
    }

    public int ttl() {
        return ttl;
    }

    @Override
    public String name() {
        return "torch";
    }

    @Override
    public void tick(Game game) {
        ttl--;
        if (ttl == 0) {
            this.takeDamage(1, this);
        }
    }

    @Override
    public void bumpedBy(Entity e) {

    }

    @Override
    public void bumped(Entity e) {

    }

}
