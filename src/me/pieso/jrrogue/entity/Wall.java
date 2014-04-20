package me.pieso.jrrogue.entity;

import me.pieso.jrrogue.core.ResourceManager;
import me.pieso.jrrogue.entity.pickup.Torch;

public class Wall extends Floor {

    public Wall(int x, int y) {
        super(x, y);
        setImage(ResourceManager.getImage("wall"));
        setSolid(true);
    }

    @Override
    public Entity get() {
        return super.get();
    }

    @Override
    public boolean remove() {
        return super.remove();
    }

    @Override
    public boolean remove(Entity e) {
        return super.remove(e);
    }

    @Override
    public boolean set(Entity e) {
        if (e instanceof Torch) {
            return super.set(e);
        }
        return false;
    }
}
