package me.pieso.jrrogue.entity.pickup;

import me.pieso.jrrogue.core.Game;
import me.pieso.jrrogue.core.ResourceManager;
import me.pieso.jrrogue.entity.Entity;

public class TorchPickup extends Pickup {

    private int ttl;

    public TorchPickup() {
        super(ResourceManager.getImage("torch"));
        this.ttl = 99999;
    }

    public int ttl() {
        return ttl;
    }

    @Override
    public String name() {
        return "a torch";
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
