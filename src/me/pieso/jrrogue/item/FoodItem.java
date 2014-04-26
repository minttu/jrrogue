package me.pieso.jrrogue.item;

import me.pieso.jrrogue.core.ResourceManager;

public class FoodItem extends Item {

    public FoodItem(int amount) {
        super("food", ResourceManager.getImage("food"), amount);
    }
}
