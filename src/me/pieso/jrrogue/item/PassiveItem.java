package me.pieso.jrrogue.item;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import me.pieso.jrrogue.core.Game;
import me.pieso.jrrogue.core.ResourceManager;
import me.pieso.jrrogue.misc.Tier;

public class PassiveItem extends Item {

    private final BufferedImage og;
    private final BufferedImage ogo;

    public PassiveItem(String name, BufferedImage img, BufferedImage imgo, Class pickup) {
        super(name, img, 0);
        this.og = img;
        this.ogo = imgo;
        //showAmount = false;
        droppable = false;
        linkPickup(pickup);
        updatelook();
    }

    @Override
    public void add(int i) {
        super.add(Math.max(i, 1));
        setAmount(Math.min(amount(), Tier.tiers.length));
        updatelook();
    }

    @Override
    public void remove(int i) {
        //super.remove(i);
        //updatelook();
    }

    public double value() {
        return (tier() != null ? tier().val : 0);
    }

    public Tier tier() {
        if (amount() > 0 && amount() - 1 < Tier.tiers.length) {
            return Tier.tiers[amount() - 1];
        }
        return null;
    }

    final public void updatelook() {
        setImage(ResourceManager.tint(og, (tier() != null ? tier().col : new Color(0, 0, 0))));
    }

    @Override
    public void onUse(Game game) {

    }

    @Override
    public void draw(Graphics g, Rectangle rec) {
        super.draw(g, rec);
        g.drawImage(ogo, rec.x, rec.y, rec.width, rec.height, null);
    }

}
