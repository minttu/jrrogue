package me.pieso.jrrogue.item;

import me.pieso.jrrogue.core.ResourceManager;
import me.pieso.jrrogue.entity.pickup.GoldPickup;

public class GoldItem extends Item {

    public GoldItem(int amount) {
        super("gold coin", ResourceManager.getImage("golditem"), amount);
        linkPickup(GoldPickup.class);
    }
}
