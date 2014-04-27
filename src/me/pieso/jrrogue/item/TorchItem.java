package me.pieso.jrrogue.item;

import me.pieso.jrrogue.core.Game;
import me.pieso.jrrogue.core.ResourceManager;
import me.pieso.jrrogue.entity.pickup.TorchPickup;

public class TorchItem extends Item {

    public TorchItem(int amount) {
        super("a torch", ResourceManager.getImage("torch"), amount);
        linkPickup(TorchPickup.class);
    }

    @Override
    public void onUse(Game game) {
        
    }
}
