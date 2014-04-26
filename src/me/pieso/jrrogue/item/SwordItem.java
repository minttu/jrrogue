package me.pieso.jrrogue.item;

import java.awt.Color;
import java.awt.image.BufferedImage;
import me.pieso.jrrogue.core.ResourceManager;
import me.pieso.jrrogue.entity.pickup.SwordPickup;

public class SwordItem extends Item {

    private final BufferedImage og;

    public SwordItem() {
        super("sword", ResourceManager.getImage("sword"), 1);
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

    public void updatelook() {
        Color tint = null;
        switch (amount()) {
            case 1:
                tint = new Color(0.7f, 0.7f, 0.7f);
                break;
            case 2:
                tint = new Color(0.7f, 0f, 0f);
                break;
            default:
                tint = new Color(1f, 1f, 1f);
                break;
        }
        setImage(ResourceManager.tint(og, tint));
    }

    public int minDmg() {
        return (int) (amount() * 2);
    }

    public int maxDmg() {
        return (int) (amount() * 3);
    }

}
