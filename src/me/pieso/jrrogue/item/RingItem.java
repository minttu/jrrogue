package me.pieso.jrrogue.item;

import me.pieso.jrrogue.core.ResourceManager;
import me.pieso.jrrogue.entity.pickup.RingPickup;

public class RingItem extends PassiveItem {

    public RingItem() {
        super(
                "your ring",
                ResourceManager.getImage("ring"),
                ResourceManager.getImage("ring_overlay"),
                RingPickup.class
        );
    }
}
