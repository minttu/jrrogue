package me.pieso.jrrogue.core;

import java.util.ArrayList;
import java.util.List;
import me.pieso.jrrogue.entity.Floor;
import me.pieso.jrrogue.entity.living.Living;
import me.pieso.jrrogue.entity.living.Player;

public abstract class MapGenerator {

    protected Floor[][] data;
    protected List<Living> live;
    protected final int width;
    protected final int height;
    protected final Player player;

    public MapGenerator(int width, int height, Player player) {
        this.width = width;
        this.height = height;
        this.data = new Floor[height][width];
        this.live = new ArrayList<>();
        this.player = player;
    }

    public MapGenerator(int width, int height) {
        this(width, height, new Player());
    }

    public Floor[][] getData() {
        return data;
    }

    public List<Living> getLive() {
        return live;
    }

    public Player getPlayer() {
        return player;
    }

    public abstract void generate();
}
