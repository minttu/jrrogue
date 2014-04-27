package me.pieso.jrrogue.item;

import me.pieso.jrrogue.core.ResourceManager;
import me.pieso.jrrogue.entity.pickup.ArmorPickup;

public class ArmorItem extends PassiveItem {

    public ArmorItem() {
        super(
                "your armor",
                ResourceManager.getImage("armor"),
                ResourceManager.getImage("armor_overlay"),
                ArmorPickup.class
        );

    }

    public int maxHP() {
        return (int) (tier() != null ? tier().val * 4 : 0);
    }
}
