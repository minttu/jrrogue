package me.pieso.jrrogue.core;

import me.pieso.jrrogue.map.MapGenerator;
import me.pieso.jrrogue.map.GenericMap;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import me.pieso.jrrogue.entity.Entity;
import me.pieso.jrrogue.entity.Floor;
import me.pieso.jrrogue.entity.living.Living;
import me.pieso.jrrogue.entity.living.Monster;
import me.pieso.jrrogue.entity.living.Player;
import me.pieso.jrrogue.entity.pickup.Torch;
import me.pieso.jrrogue.entity.trap.Trap;

public final class Game implements ActionListener {

    private int width;
    private int height;
    private Floor[][] data;
    private Player player;
    private final List<GameHook> hooks;
    private List<Living> live;
    private boolean vision;
    private int side;
    private int level;

    public Game() {
        this.side = 32;
        this.vision = false;
        this.hooks = new ArrayList<>();
        this.player = null;
        this.level = 0;
        make(45, 45);
    }

    private void make(int width, int height) {
        level++;
        this.width = width;
        this.height = height;
        MapGenerator mg;
        if (player == null) {
            mg = new GenericMap(width, height);
        } else {
            mg = new GenericMap(width, height, player);
        }
        mg.generate();
        this.data = mg.getData();
        this.live = mg.getLive();
        this.player = mg.getPlayer();
        player.dungeon(level);
        calculateVision();
    }

    public void addHook(GameHook r) {
        this.hooks.add(r);
    }

    public void runHooks() {
        for (GameHook r : hooks) {
            r.hook(this);
        }
    }

    public Player getPlayer() {
        return player;
    }

    public boolean moveRandomly(Entity e) {
        int x = e.x();
        int y = e.y();
        switch (new Random().nextInt(4)) {
            case 0:
                x++;
                break;
            case 1:
                x--;
                break;
            case 2:
                y++;
                break;
            case 3:
                y--;
                break;
            default:
                System.out.println("crap");
        }
        return move(e, e.x(), e.y(), x, y);
    }

    public void moveTowards(Entity e1, Entity e2) {
        // ToDo: refactor
        int tries = 4;
        while (tries >= 0) {
            int x = e1.x();
            int y = e1.y();
            switch (new Random().nextInt(4)) {
                case 0:
                case 1:
                    if (e1.x() < e2.x()) {
                        x++;
                    } else if (e1.x() > e2.x()) {
                        x--;
                    }
                    if (e1.x() != e2.x()) {
                        break;
                    }
                case 2:
                case 3:
                    if (e1.y() < e2.y()) {
                        y++;
                    } else if (e1.y() > e2.y()) {
                        y--;
                    }
                    if (e1.y() != e2.y()) {
                        break;
                    }
                case 4:
                    switch (new Random().nextInt(4)) {
                        case 0:
                            x++;
                            break;
                        case 1:
                            x--;
                            break;
                        case 2:
                            y++;
                            break;
                        case 3:
                            y--;
                            break;
                    }
                    break;
            }
            if (canMove(e1, e1.x(), e1.y(), x, y)) {
                move(e1, e1.x(), e1.y(), x, y);
                return;
            } else {
                if (data[y][x].get() != null && data[y][x].get() == e2) {
                    move(e1, e1.x(), e1.y(), x, y);
                    return;
                }
            }
            tries--;
        }
    }

    public void recalculateVision() {
        recalculateVision(false);
    }

    public void recalculateVision(boolean seen) {
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                if (data[y][x] != null) {
                    data[y][x].setFoggy(true);
                    if (seen) {
                        data[y][x].setSeen(false);
                    }
                }
            }
        }
        calculateVision();
    }

    public void calculateVision() {
        if (!vision) {
            calculateVisionEntity(player);
            for (Living l : live) {
                if (l instanceof Torch) {
                    calculateVisionEntity(l);
                }
            }
        } else {
            for (int y = 0; y < height; y++) {
                for (int x = 0; x < width; x++) {
                    if (data[y][x] != null) {
                        data[y][x].setFoggy(false);
                        data[y][x].setSeen(true);
                    }
                }
            }
        }
    }

    public void calculateVisionEntity(Entity e) {
        int mm = 4;
        boolean over = false;
        boolean torch = false;
        if (e instanceof Torch) {
            torch = true;
            if (((Torch) e).ttl() == 1) {
                over = true;
            }
        }
        for (int y = e.y() - mm; y < e.y() + mm; y++) {
            for (int x = e.x() - mm; x < e.x() + mm; x++) {
                if (x >= 0 && y >= 0 && x < width && y < height) {
                    if (data[y][x] != null) {
                        int dis = Math.abs(x - e.x()) + Math.abs(y - e.y());
                        if (dis < mm - 1 && !over) {
                            data[y][x].setFoggy(false);
                            data[y][x].setSeen(true);
                        } else {
                            if (torch && over) {
                                data[y][x].setFoggy(true);
                            } else if (!torch) {
                                data[y][x].setFoggy(true);
                            }
                        }
                    }
                }
            }
        }
    }

    public List<Floor> getNeighbours(Entity e) {
        return getNeighbours(e.x(), e.y());
    }

    public List<Floor> getNeighbours(int rx, int ry) {
        return getNeighbours(rx, ry, 1);
    }

    public List<Floor> getNeighbours(int rx, int ry, int range) {
        List<Floor> res = new ArrayList<>();
        for (int y = (ry - range); y < (ry + range + 1); y++) {
            for (int x = (rx - range); x < (rx + range + 1); x++) {
                if (x >= 0 && y >= 0 && x < width && y < height) {
                    if (data[y][x] != null) {
                        if (rx == x && ry == y) {

                        } else {
                            res.add(data[y][x]);
                        }
                    }
                }
            }
        }
        return res;
    }

    public boolean movePlayer(int dx, int dy) {
        boolean b = move(player, player.x(), player.y(), player.x() + dx, player.y() + dy);
        calculateVision();
        tick();
        return b;
    }

    public void dropTorch(int x, int y) {
        List<Floor> possible = getNeighbours(x, y);
        Torch torch = new Torch();
        while (true) {
            int len = possible.size();
            if (len == 0) {
                return;
            }
            int pos = new Random().nextInt(len);
            if (!possible.get(pos).set(torch)) {
                possible.remove(pos);
            } else {
                break;
            }
        }
        live.add(torch);
    }

    public boolean canMove(Entity e, int x1, int y1, int x2, int y2) {
        if (Math.abs(x1 - x2) > 0 && Math.abs(y1 - y2) > 0) {
            return false;
        }
        if (x1 >= 0 && x1 < width && y1 >= 0 && y1 < height
                && x2 >= 0 && x2 < width && y2 >= 0 && y2 < height) {
            if (data[y2][x2] == null) {
                return false;
            }
            Entity en = data[y2][x2].get();
            if (en == null) {
                return true;
            }
        }
        return false;
    }

    public boolean move(Entity e, int x1, int y1, int x2, int y2) {
        if (Math.abs(x1 - x2) > 0 && Math.abs(y1 - y2) > 0) {
            return false;
        }
        if (x1 >= 0 && x1 < width && y1 >= 0 && y1 < height
                && x2 >= 0 && x2 < width && y2 >= 0 && y2 < height) {
            if (data[y2][x2] == null) {
                return false;
            }
            Entity en = data[y2][x2].get();
            if (en == null) {
                if (data[y1][x1].remove(e)) {
                    if (data[y2][x2].set(e)) {
                        return true;
                    } else {
                        data[y1][x1].set(e);
                    }
                }
            } else {
                e.bumped(en);
                en.bumpedBy(e);
            }
        }
        return false;
    }

    public void tick() {
        ArrayList<Living> deletion = new ArrayList<>();
        player.tick(this);
        if (player.use()) {
            Floor f = data[player.y()][player.x()];
            if (f instanceof Trap) {
                player.addStatus("You tried using the", ((Trap) f).name());
                if (!((Trap) f).usedBy(player)) {
                    player.addStatus("It did not work");
                }
            }
            player.setUse(false);
        }
        if (player.shouldAscend()) {
            make(width, height);
            player.setAscend(false);
            runHooks();
            return;
        }
        if (!player.living()) {
            player.addStatus("You are dead");
        }
        for (Living l : live) {
            l.tick(this);
            if (!l.living()) {
                deletion.add(l);
            }
        }
        for (Living l : deletion) {
            data[l.y()][l.x()].remove(l);
            live.remove(l);
        }
        runHooks();
    }

    public Entity[][] getData() {
        return data;
    }

    @Override
    public void actionPerformed(ActionEvent ae) {

    }

    public void setVision(boolean state) {
        boolean b = false;
        if (vision != state) {
            b = true;
        }
        this.vision = state;
        recalculateVision();
        if (b) {
            runHooks();
        }
    }

    public void zoomup() {
        side += 8;
        zoom();
    }

    public void zoomdown() {
        side -= 8;
        zoom();
    }

    public void zoom() {
        if (side < 16) {
            side = 16;
        }
        if (side > 128) {
            side = 128;
        }
        runHooks();
    }

    public int getSide() {
        return side;
    }
}
