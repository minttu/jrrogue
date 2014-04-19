package me.pieso.jrrogue.entity;

import java.awt.image.BufferedImage;
import java.util.Random;
import me.pieso.jrrogue.core.Game;

public abstract class Living extends Entity {

    private int hp;
    private int maxhp;

    private int mindmg;
    private int maxdmg;
    private double hitrate;

    private boolean dead;

    public Living(BufferedImage image, int hp) {
        super(image);
        this.hp = hp;
        this.maxhp = hp;
        this.mindmg = 1;
        this.maxdmg = 2;
        this.hitrate = 0.7;
        this.dead = false;
    }

    public boolean living() {
        return !dead;
    }

    public int hp() {
        return hp;
    }

    public int maxhp() {
        return maxhp;
    }

    public void heal(int amount) {
        hp += amount;
        hp = Math.min(hp, maxhp);
    }

    public boolean takeDamage(int amount, Living from) {
        hp -= amount;
        if (hp < 1) {
            dead = true;
            return true;
        }
        return false;
    }
    
    public abstract String name();

    public void limit(int amount) {
        maxhp += amount;
        hp += amount;
    }

    public void setHitrate(double d) {
        this.hitrate = d;
    }

    public double hitrate() {
        return hitrate;
    }

    public void setMinDmg(int d) {
        this.mindmg = d;
    }

    public int minDmg() {
        return mindmg;
    }

    public void setMaxDmg(int d) {
        this.maxdmg = d;
    }

    public int maxDmg() {
        return maxdmg;
    }
    
    public int toXP() {
        return (maxhp*maxdmg)/10;
    }

    public int getDamage() {
        if (new Random().nextDouble() < hitrate) {
            return new Random().nextInt(maxdmg - mindmg) + mindmg;
        }
        return 0;
    }

    public abstract void tick(Game game);

}
