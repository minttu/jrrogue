package me.pieso.jrrogue.entity;

import me.pieso.jrrogue.core.ResourceManager;

public class Wall extends Floor {

    public Wall(int x, int y) {
        super(x, y);
        setImage(ResourceManager.getImage("wall"));
    }

    @Override
    public Entity get() {
        return null;
    }

    @Override
    public boolean remove() {
        return false;
    }

    @Override
    public boolean remove(Entity e) {
        return false;
    }

    @Override
    public boolean set(Entity e) {
        return false;
    }
}
