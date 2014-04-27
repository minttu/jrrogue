package me.pieso.jrrogue.entity;

import me.pieso.jrrogue.core.ResourceManager;

public class Leaves extends Wall {

    public Leaves(int x, int y) {
        super(x, y);
        setImage(ResourceManager.getImage("leaves"));
    }

}
