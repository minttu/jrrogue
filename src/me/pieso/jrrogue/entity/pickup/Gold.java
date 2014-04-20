package me.pieso.jrrogue.entity.pickup;

import me.pieso.jrrogue.entity.living.Living;
import java.util.Random;
import me.pieso.jrrogue.core.Game;
import me.pieso.jrrogue.core.ResourceManager;
import me.pieso.jrrogue.entity.Entity;

public class Gold extends Pickup {

    public Gold() {
        super(ResourceManager.getImage("gold"));
        addGold(new Random().nextInt(30) + 10);
    }

    @Override
    public void bumpedBy(Entity e) {
        if (e instanceof Living) {
            ((Living) e).addGold(gold());
        }
    }

    @Override
    public void bumped(Entity e) {

    }

    @Override
    public String name() {
        return "gold";
    }

    @Override
    public void tick(Game game) {

    }

}
