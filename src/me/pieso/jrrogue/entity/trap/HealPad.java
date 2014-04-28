package me.pieso.jrrogue.entity.trap;

import me.pieso.jrrogue.entity.living.Player;
import me.pieso.jrrogue.core.ResourceManager;
import me.pieso.jrrogue.entity.Entity;

public class HealPad extends Trap {

    private final int healpower;
    private int times;

    public HealPad(int x, int y) {
        super(x, y, ResourceManager.getImage("healpad"));
        this.healpower = 999;
        this.times = 1;
    }

    @Override
    public String name() {
        return "healpad";
    }

    @Override
    public void steppedOnBy(Entity e) {

    }

    @Override
    public boolean usedBy(Entity e) {
        if (times > 0) {
            if (e instanceof Player) {
                Player p = (Player) e;
                if (p.hp() < p.maxhp()) {
                    p.heal(healpower);
                    p.addStatus("You were fully by the healpad");
                    times--;
                    if (times == 0) {
                        setImage(ResourceManager.getImage("trap"));
                    }
                    return true;
                }
            }
        }
        return false;
    }

}
