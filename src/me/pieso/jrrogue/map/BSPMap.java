package me.pieso.jrrogue.map;

import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import me.pieso.jrrogue.entity.Door;
import me.pieso.jrrogue.entity.Floor;
import me.pieso.jrrogue.entity.Wall;
import me.pieso.jrrogue.entity.living.Player;
import me.pieso.jrrogue.util.BSP;

public class BSPMap extends MapGenerator {
    
    private final List<Rectangle> rooms;
    
    public BSPMap(int width, int height, Player player) {
        super(width, height, player);
        rooms = new ArrayList<>();
    }
    
    private void rawroom(Rectangle rec) {
        for (int y = rec.y; y < rec.y + rec.height; y++) {
            for (int x = rec.x; x < rec.x + rec.width; x++) {
                if (data[y][x] == null) {
                    if (y == rec.y || x == rec.x || y == rec.y + rec.height - 1 || x == rec.x + rec.width - 1) {
                        data[y][x] = new Wall(x, y);
                    } else {
                        data[y][x] = new Floor(x, y);
                    }
                }
            }
        }
    }
    
    private void room(Rectangle rec) {
        Rectangle nw = new Rectangle(rec);
        
        nw.width = new Random().nextInt(rec.width - 6) + 6;
        nw.height = new Random().nextInt(rec.height - 6) + 6;
        
        nw.x = rec.x + (rec.width - nw.width) / 2;
        nw.y = rec.y + (rec.height - nw.height) / 2;
        rooms.add(nw);
        rawroom(nw);
    }
    
    private void rawpath(Rectangle rec) {
        boolean hori = rec.width < rec.height;
        int doors = 0;
        for (int y = rec.y; y < rec.y + rec.height; y++) {
            for (int x = rec.x; x < rec.x + rec.width; x++) {
                if (data[y][x] == null) {
                    data[y][x] = new Floor(x, y);
                } else {
                    if (data[y][x] instanceof Wall) {
                        data[y][x] = new Door(x, y, hori);
                        doors++;
                        if (doors >= 2) {
                            return;
                        }
                    }
                }
            }
        }
    }
    
    private void path(BSP bsp) {
        Rectangle rec = new Rectangle();
        if (bsp.whatSplit() == BSP.HSPLIT) {
            rec.width = 1;
            rec.height = bsp.height() / 2;
            rec.x = bsp.x() + bsp.width() / 2;
            rec.y = bsp.y() + bsp.height() / 4;
        } else {
            rec.height = 1;
            rec.width = bsp.width() / 2;
            rec.x = bsp.x() + bsp.width() / 4;
            rec.y = bsp.y() + bsp.height() / 2;
        }
        rawpath(rec);
    }
    
    @Override
    public void generate() {
        BSP root = new BSP(0, 0, width, height);
        List<BSP> bus = new ArrayList<>();
        List<BSP> fin = new ArrayList<>();
        List<BSP> con = new ArrayList<>();
        bus.add(root);
        while (!bus.isEmpty()) {
            BSP cur = bus.remove(0);
            if (cur.split()) {
                bus.add(cur.first());
                bus.add(cur.second());
            } else {
                fin.add(cur);
            }
            if (cur.whatSplit() != BSP.NOSPLIT) {
                con.add(cur);
            }
        }
        
        for (BSP b : fin) {
            room(b.rectangle());
        }
        
        while (!con.isEmpty()) {
            path(con.remove(con.size() - 1));
        }
        /*for (BSP b : con) {
         if (b.whatSplit() == BSP.HSPLIT) {
         int sx = b.x() + b.width() / 2;
         int sy = b.y() + 1;
         for (int i = sy; i < sy + b.height() - 1; i++) {
         if (data[i][sx] != null) {
         if (data[i][sx] instanceof Wall) {
         data[i][sx] = new Door(sx, i, true);
         break;
         }
         } else {
         data[i][sx] = new Floor(sx, i);
         }
         }
         } else {
         int sx = b.x() + 1;
         int sy = b.y() + b.height() / 2;
         for (int i = sx; i < sx + b.width() - 1; i++) {
         if (data[sy][i] != null) {
         if (data[sy][i] instanceof Wall) {
         data[sy][i] = new Door(i, sy, false);
         break;
         }
         } else {
         data[sy][i] = new Floor(i, sy);
         }
         }
         }
         }*/
        while (true) {
            int x = new Random().nextInt(width);
            int y = new Random().nextInt(height);
            if (data[y][x] != null) {
                if (data[y][x].set(player)) {
                    break;
                }
            }
        }
    }
    
}
