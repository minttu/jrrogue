package me.pieso.jrrogue.entity;

import java.awt.Graphics;
import me.pieso.jrrogue.core.ResourceManager;
import static me.pieso.jrrogue.entity.Floor.fog;

public class HealTrap extends Trap {
    
    private final int healpower;
    private int times;
    
    public HealTrap(int x, int y) {
        super(x, y, ResourceManager.getImage("healtrap"));
        this.healpower = 999;
        this.times = 1;
    }
    
    @Override
    public void steppedOnBy(Entity e) {
        if (times > 0) {
            if (e instanceof Player) {
                Player p = (Player) e;
                if (p.hp() < p.maxhp()) {
                    p.heal(healpower);
                    p.addStatus("You were fully by the HealTrap");
                    times--;
                }
            }
        }
    }
    
}
