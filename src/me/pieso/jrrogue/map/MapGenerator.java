package me.pieso.jrrogue.map;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import me.pieso.jrrogue.entity.Floor;
import me.pieso.jrrogue.entity.Wall;
import me.pieso.jrrogue.entity.living.Living;
import me.pieso.jrrogue.entity.living.Player;
import me.pieso.jrrogue.entity.trap.Trap;

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

    public void put(Living l) {
        while (true) {
            int x = new Random().nextInt(width);
            int y = new Random().nextInt(height);
            if (data[y][x].set(l)) {
                break;
            }
        }
        live.add(l);
    }

    public void putTrap(Trap t) {
        while (true) {
            int x = new Random().nextInt(width);
            int y = new Random().nextInt(height);
            if (data[y][x].get() == null && !(data[y][x] instanceof Wall)) {
                data[y][x] = t;
                t.move(x, y);
                break;
            }
        }
    }

    public void putPlayer() {
        while (true) {
            int x = new Random().nextInt(width);
            int y = new Random().nextInt(height);
            if (data[y][x].set(player)) {
                break;
            }
        }
    }

    public void boxWithWalls() {
        for (int y = 0; y < height; y++) {
            data[y][0] = new Wall(0, y);
            data[y][width - 1] = new Wall(width - 1, y);
        }
        for (int x = 0; x < width; x++) {
            data[0][x] = new Wall(x, 0);
            data[height - 1][x] = new Wall(x, height - 1);
        }
    }

    public abstract void generate();
}
