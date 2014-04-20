package me.pieso.jrrogue.entity.living;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import me.pieso.jrrogue.core.Game;
import me.pieso.jrrogue.core.ResourceManager;
import me.pieso.jrrogue.entity.Entity;

public abstract class Living extends Entity {

    private int hp;
    private int maxhp;

    private int mindmg;
    private int maxdmg;
    private double hitrate;

    private boolean dead;
    private int washealed;
    private List<Integer> washurt;
    private int gold;

    public Living(BufferedImage image, int hp) {
        super(image);
        this.hp = hp;
        this.maxhp = hp;
        this.mindmg = 1;
        this.maxdmg = 2;
        this.hitrate = 0.7;
        this.dead = false;
        this.washealed = -1;
        this.washurt = new ArrayList<>();
        this.gold = 0;
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
        int org = hp;
        hp += amount;
        hp = Math.min(hp, maxhp);
        washealed = hp - org;
    }

    public boolean takeDamage(int amount, Living from) {
        if (amount > 0) {
            washurt.add(amount);
        }
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
        Graphics2D g2d = (Graphics2D) g;
        g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, .75f));
        g.drawImage(ResourceManager.getImage("shadow"), x, y, side, side, null);
        g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f));
        super.draw(g, x, y, side);
        if (washealed > 0) {
            g.drawImage(ResourceManager.getImage("heal"), x, y, side, side, null);
        }
        if (!washurt.isEmpty()) {
            for (Integer i : washurt) {
                int hx = x + (new Random().nextInt((side / 3) * 2));
                int hy = y + (new Random().nextInt((side / 7) * 4)) + side / 6;
                g.drawImage(ResourceManager.getImage("hurt"), hx, hy, side / 3, side / 3, null);
                g.setFont(new Font(Font.MONOSPACED, Font.PLAIN, (side / 3) - 3));
                g.setColor(Color.WHITE);
                g.drawString(i + "", hx + side / 12, hy + (side / 7) * 2);
            }
        }
        if (maxhp() != hp() || washealed > 0) {
            g.setColor(Color.BLACK);
            g.fillRect(x + 4, y, side - 8, 4);
            g.setColor(Color.RED);
            int bl = (int) (((double) (side - 8.0) / (double) maxhp()) * (double) hp());
            g.fillRect(x + 4, y + 1, bl, 2);
            if (!washurt.isEmpty()) {
                int tot = 0;
                for (Integer i : washurt) {
                    tot += i;
                }
                g.setColor(Color.WHITE);
                int bn = (int) (((double) (side - 8.0) / (double) maxhp()) * (double) tot);
                g.fillRect(x + 4 + bl, y + 1, bn, 2);

            }
            if (washealed > 0) {
                g.setColor(Color.GREEN);
                int bn = (int) (((double) (side - 8.0) / (double) maxhp()) * (double) washealed);
                g.fillRect(x + 4 + bl - bn, y + 1, bn, 2);
            }
        }
        washurt.clear();
        washealed = -1;
    }

    public abstract void tick(Game game);

    public void addGold(int value) {
        this.gold += value;
    }

    public int gold() {
        return gold;
    }

}
