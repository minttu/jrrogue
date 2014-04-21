package me.pieso.jrrogue.item;

import me.pieso.jrrogue.core.ResourceManager;
import me.pieso.jrrogue.entity.pickup.TorchPickup;

public class TorchItem extends Item {

    public TorchItem(int amount) {
        super("Torch", ResourceManager.getImage("torch"), amount);
        linkPickup(TorchPickup.class);
    }
}
