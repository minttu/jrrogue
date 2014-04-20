package me.pieso.jrrogue.entity;

import me.pieso.jrrogue.core.ResourceManager;

public class Hole extends Trap {

    public Hole(int x, int y) {
        super(x, y, ResourceManager.getImage("hole"));
    }

    @Override
    public void steppedOnBy(Entity e) {
        if (e instanceof Player) {
            Player p = (Player) e;
            p.takeDamage(p.hp(), p);
        }
    }

}
