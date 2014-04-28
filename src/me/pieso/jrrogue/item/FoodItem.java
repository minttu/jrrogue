package me.pieso.jrrogue.item;

import me.pieso.jrrogue.core.Game;
import me.pieso.jrrogue.core.ResourceManager;
import me.pieso.jrrogue.entity.pickup.FoodPickup;

public class FoodItem extends Item {

    public FoodItem(int amount) {
        super("a piece of food", ResourceManager.getImage("food"), amount);
        linkPickup(FoodPickup.class);
        droppable = false;
    }

    @Override
    public void onUse(Game game) {
        game.getPlayer().eat(0.5);
        game.getPlayer().addStatus("You are", name());
    }
}
