package me.pieso.jrrogue.entity;

import me.pieso.jrrogue.core.ResourceManager;

public class Grass extends Floor {

    public Grass(int x, int y) {
        super(x, y);
        setImage(ResourceManager.getImage("grass"));
    }

}
