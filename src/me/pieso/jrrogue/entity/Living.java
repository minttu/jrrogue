package me.pieso.jrrogue.entity;

import java.awt.Color;
import java.awt.Graphics;
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
        return (maxhp * maxdmg) / 10;
    }

    public int getDamage() {
        if (new Random().nextDouble() < hitrate) {
            return new Random().nextInt(maxdmg - mindmg) + mindmg;
        }
        return 0;
    }

    @Override
    public void draw(Graphics g, int x, int y, int side) {
        super.draw(g, x, y, side);
        if (maxhp() != hp()) {
            g.setColor(Color.BLACK);
            g.fillRect(x + 4, y, side - 8, 4);
            g.setColor(Color.RED);
            int bl = (int)(((double)(side - 8.0) / (double)maxhp()) * (double)hp());
            g.fillRect(x + 4, y + 1, bl, 2);
        }
    }

    public abstract void tick(Game game);

}
