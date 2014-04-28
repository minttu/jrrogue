package me.pieso.jrrogue.item;

import me.pieso.jrrogue.core.ResourceManager;
import me.pieso.jrrogue.entity.pickup.SwordPickup;
import me.pieso.jrrogue.misc.Damage;

public class SwordItem extends PassiveItem {

    public SwordItem() {
        super(
                "your sword",
                ResourceManager.getImage("sword"),
                ResourceManager.getImage("sword_overlay"),
                SwordPickup.class
        );
    }

    public Damage getDamage() {
        return new Damage((tier() != null ? tier().val * 1.5 : 0), (tier() != null ? tier().val * 2.25 : 0));
    }
}
