package me.pieso.jrrogue.core;

import java.awt.Rectangle;
import java.awt.geom.Rectangle2D;
import java.util.Random;
import me.pieso.jrrogue.entity.Floor;
import me.pieso.jrrogue.entity.HealTrap;
import me.pieso.jrrogue.entity.Hole;
import me.pieso.jrrogue.entity.Monster;
import me.pieso.jrrogue.entity.Wall;

public class GenericMap extends MapGenerator {

    private Random rnd;
    private Rectangle[][] recs;

    public GenericMap(int width, int height) {
        super(width, height);
    }

    private void path(Rectangle rec) {

        int sx = rec.x;// + ri(-1, 1);
        int sy = rec.y;// + ri(-1, 1);
        int ex = rec.width;// + ri(-1, 1);
        int ey = rec.height;// + ri(-1, 1);
        for (int x = sx; x <= ex; x++) {
            if (sy > 0 && data[sy - 1][x] == null) {
                data[sy - 1][x] = new Wall(x, sy - 1);
            }
            if (sy < height - 1 && data[sy + 1][x] == null) {
                data[sy + 1][x] = new Wall(x, sy + 1);
            }
            data[sy][x] = new Floor(x, sy);
            if (x == (sx + ex) / 2) {
                for (int y = sy; y <= ey; y++) {
                    if (ex > 0 && data[y][ex - 1] == null) {
                        data[y][ex - 1] = new Wall(ex - 1, y);
                    }
                    if (ex < width - 1 && data[y][ex + 1] == null) {
                        data[y][ex + 1] = new Wall(ex + 1, y);
                    }
                    data[y][ex] = new Floor(ex, y);
                }
            }
        }

    }

    private void room(Rectangle rec) {
        int sx = rec.x;
        int sy = rec.y;
        int ex = rec.x + rec.width;
        int ey = rec.y + rec.height;
        for (int x = sx; x < ex; x++) {
            for (int y = sy; y < ey; y++) {
                if (y == sy || y == ey - 1 || x == sx || x == ex - 1) {
                    data[y][x] = new Wall(x, y);
                } else {
                    data[y][x] = new Floor(x, y);
                }
            }
        }
    }

    private int ri(int min, int max) {
        return rnd.nextInt(max - min) + min;
    }

    private Rectangle makeRect(int x, int y) {
        int mx = width / 3;
        int my = height / 3;
        int limx = width / 10;
        int limy = height / 10;
        int rx = x * mx + ri(0, limx);
        int rwidth = ri(mx / 2, mx - limx);
        int ry = y * my + ri(0, limy);
        int rheight = ri(my / 2, my - limy);
        Rectangle rec = new Rectangle(rx, ry, rwidth, rheight);
        return rec;
    }

    private void makePath(Rectangle r1, Rectangle r2) {
        Rectangle rec = new Rectangle(0, 0, 0, 0);

        int cx1 = r1.x + r1.width / 2;
        int cx2 = r2.x + r2.width / 2;
        int cy1 = r1.y + r1.height / 2;
        int cy2 = r2.y + r2.height / 2;

        if (cx1 == cx2) {
            rec.x = cx1;
            rec.width = 1;
        } else if (cx1 < cx2) {
            rec.x = cx1;
            rec.width = cx2;
        } else {
            rec.x = cx2;
            rec.width = cx1;
        }

        if (cy1 == cy2) {
            rec.y = cy1;
            rec.height = 1;
        } else if (cy1 < cy2) {
            rec.y = cy1;
            rec.height = cy2;
        } else {
            rec.y = cy2;
            rec.height = cy1;
        }

        path(rec);
    }

    private void placePlayer() {
        Rectangle rec = recs[rnd.nextInt(3)][rnd.nextInt(3)];
        while (true) {
            int x = rec.x + rnd.nextInt(rec.width - 2) + 1;
            int y = rec.y + rnd.nextInt(rec.height - 2) + 1;
            if (data[y][x].set(player)) {
                break;
            }
        }
    }

    private void placeMonsters(int num) {
        while (num > 0) {
            Rectangle rec = recs[rnd.nextInt(3)][rnd.nextInt(3)];
            Monster monst = new Monster();
            while (true) {
                int x = rec.x + rnd.nextInt(rec.width - 2) + 1;
                int y = rec.y + rnd.nextInt(rec.height - 2) + 1;
                if (data[y][x].set(monst)) {
                    break;
                }
            }
            live.add(monst);
            num--;
        }
    }

    private void placeHealTraps(int num) {
        while (num > 0) {
            Rectangle rec = recs[rnd.nextInt(3)][rnd.nextInt(3)];
            while (true) {
                int x = rec.x + rnd.nextInt(rec.width - 2) + 1;
                int y = rec.y + rnd.nextInt(rec.height - 2) + 1;
                if (data[y][x].get() == null) {
                    data[y][x] = new HealTrap(x, y);
                    break;
                }
            }
            num--;
        }
    }

    private void placeHole() {
        Rectangle rec = recs[rnd.nextInt(3)][rnd.nextInt(3)];
        while (true) {
            int x = rec.x + rnd.nextInt(rec.width - 2) + 1;
            int y = rec.y + rnd.nextInt(rec.height - 2) + 1;
            if (data[y][x].get() == null) {
                data[y][x] = new Hole(x, y);
                break;
            }
        }
    }

    @Override
    public void generate() {
        recs = new Rectangle[3][3];
        this.rnd = new Random();

        for (int y = 0; y < 3; y++) {
            for (int x = 0; x < 3; x++) {
                recs[y][x] = makeRect(x, y);
                room(recs[y][x]);
            }
        }

        makePath(recs[0][0], recs[0][1]);
        makePath(recs[0][1], recs[0][2]);

        makePath(recs[1][0], recs[1][1]);
        makePath(recs[1][1], recs[1][2]);

        makePath(recs[2][0], recs[2][1]);
        makePath(recs[2][1], recs[2][2]);

        makePath(recs[0][1], recs[1][1]);
        makePath(recs[1][0], recs[2][0]);
        makePath(recs[1][2], recs[2][1]);

        placeHole();
        placePlayer();
        placeMonsters(20);
        placeHealTraps(2);
    }

}
