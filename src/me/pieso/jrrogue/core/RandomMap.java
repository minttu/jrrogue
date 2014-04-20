package me.pieso.jrrogue.core;

import java.util.Random;
import me.pieso.jrrogue.entity.Floor;
import me.pieso.jrrogue.entity.Monster;
import me.pieso.jrrogue.entity.Wall;

public class RandomMap extends MapGenerator {

    public RandomMap(int width, int height) {
        super(width, height);
    }

    @Override
    public void generate() {
        boolean plradd = false;
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                if (x == 0 || y == 0 || x == width - 1 || y == height - 1) {
                    data[y][x] = new Wall(x, y);
                } else if (x >= 1 && x < width - 1
                        && y >= 1 && y < height - 1) {
                    if (new Random().nextDouble() > 0.75) {
                        data[y][x] = new Wall(x, y);
                    } else {
                        data[y][x] = new Floor(x, y);
                        if (new Random().nextDouble() > 0.95) {
                            if (!plradd) {
                                plradd = true;
                                data[y][x].set(player);
                            } else {
                                Monster m = new Monster();
                                live.add(m);
                                data[y][x].set(m);
                            }
                        }
                    }
                } else {
                    data[y][x] = new Floor(x, y);
                }
            }
        }
    }

}
