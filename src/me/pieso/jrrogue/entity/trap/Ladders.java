package me.pieso.jrrogue.entity.trap;

import me.pieso.jrrogue.entity.living.Player;
import me.pieso.jrrogue.core.ResourceManager;
import me.pieso.jrrogue.entity.Entity;

public class Ladders extends Trap {

    public Ladders(int x, int y) {
        super(x, y, ResourceManager.getImage("hole"));
    }

    @Override
    public String name() {
        return "ladders";
    }

    @Override
    public void steppedOnBy(Entity e) {

    }

    @Override
    public boolean usedBy(Entity e) {
        if (e instanceof Player) {
            Player p = (Player) e;
            p.setAscend(true);
            return true;
        }
        return false;
    }

}
