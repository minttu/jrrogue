package me.pieso.jrrogue.entity;

import java.awt.image.BufferedImage;
import javax.annotation.Resource;
import me.pieso.jrrogue.core.Game;
import me.pieso.jrrogue.core.ResourceManager;

public class Torch extends Living {
    private int ttl;

    public Torch() {
        super(ResourceManager.getImage("torch"), 1);
        this.ttl = 50;
        setMinDmg(0);
        setMaxDmg(0);
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
        if(ttl == 0) {
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
