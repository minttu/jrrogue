package me.pieso.jrrogue.item;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import me.pieso.jrrogue.core.Game;
import me.pieso.jrrogue.core.ResourceManager;
import me.pieso.jrrogue.entity.pickup.SwordPickup;
import me.pieso.jrrogue.misc.Damage;

public class SwordItem extends Item {

    private final BufferedImage og;

    public SwordItem() {
        super("your sword", ResourceManager.getImage("sword"), 1);
        og = ResourceManager.getImage("sword");
        showAmount = false;
        linkPickup(SwordPickup.class);
        updatelook();
    }

    @Override
    public void add(int i) {
        super.add(i);
        updatelook();
    }

    @Override
    public void remove(int i) {
        super.remove(i);
        updatelook();
    }

    public Tier tier() {
        if (amount() > 0 && amount() - 1 < Tier.tiers.length) {
            return Tier.tiers[amount() - 1];
        }
        return null;
    }

    public void updatelook() {
        setImage(ResourceManager.tint(og, (tier() != null ? tier().col : new Color(0, 0, 0))));
    }

    public Damage getDamage() {
        return new Damage((tier() != null ? tier().val * 2 : 0), (tier() != null ? tier().val * 3 : 0));
    }

    @Override
    public void onUse(Game game) {

    }

    @Override
    public void draw(Graphics g, Rectangle rec) {
        super.draw(g, rec);
        g.drawImage(ResourceManager.getImage("sword_overlay"), rec.x, rec.y, rec.width, rec.height, null);
    }
}
