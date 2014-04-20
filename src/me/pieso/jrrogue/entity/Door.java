package me.pieso.jrrogue.entity;

import me.pieso.jrrogue.core.ResourceManager;
import me.pieso.jrrogue.entity.living.Player;

public class Door extends Floor {

    private boolean closed;

    public Door(int x, int y, boolean horizontal) {
        super(x, y);
        if (horizontal) {
            setImage(ResourceManager.getImage("door_horizontal"));
        } else {
            setImage(ResourceManager.getImage("door_vertical"));
        }
        this.closed = true;
        setSolid(true);
    }

    @Override
    public void bumpedBy(Entity e) {
        if (e instanceof Player && closed) {
            setImage(ResourceManager.getImage("floor"));
            closed = false;
            setSolid(false);
            Player p = (Player) e;
            p.addStatus("You opened a door");
        }
    }

    @Override
    public boolean remove(Entity e) {
        if (!closed) {
            return super.remove(e);
        }
        return false;
    }

    @Override
    public boolean remove() {
        if (!closed) {
            return super.remove();
        }
        return false;
    }

    @Override
    public Entity get() {
        if (!closed) {
            return super.get();
        }
        return null;
    }

    @Override
    public boolean set(Entity e) {
        if (!closed) {
            return super.set(e);
        } else {
            bumpedBy(e);
        }
        return false;
    }

}
