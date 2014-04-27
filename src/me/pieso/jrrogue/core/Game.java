package me.pieso.jrrogue.core;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import me.pieso.jrrogue.entity.Entity;
import me.pieso.jrrogue.entity.Floor;
import me.pieso.jrrogue.entity.living.Living;
import me.pieso.jrrogue.entity.living.Player;
import me.pieso.jrrogue.entity.pickup.TorchPickup;
import me.pieso.jrrogue.entity.trap.Trap;
import me.pieso.jrrogue.item.GoldItem;
import me.pieso.jrrogue.map.CellularMap;
import me.pieso.jrrogue.map.MapGenerator;
import me.pieso.jrrogue.util.GameHook;

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
    private int frame;
    private final TimmyTheTimer ttt;
    public boolean typing;
    public String type;
    public int typing_caret;
    public List<String> type_history;
    public int type_history_place;

    public Game() {
        this.side = 32;
        this.vision = false;
        this.hooks = new ArrayList<>();
        this.player = null;
        this.level = 0;
        make(100, 100);
        this.frame = 0;
        this.typing = false;
        this.type = "";
        this.typing_caret = 0;
        this.type_history = new ArrayList<>();
        this.type_history_place = 0;
        this.ttt = new TimmyTheTimer(100, this);
    }

    private void make(int width, int height) {
        level++;
        this.width = width;
        this.height = height;
        MapGenerator mg;
        if (player == null) {
            mg = new CellularMap(width, height, new Player());
        } else {
            mg = new CellularMap(width, height, player);
        }
        mg.generate();
        this.data = mg.getData();
        this.live = mg.getLive();
        this.player = mg.getPlayer();
        player.setDungeon(level);
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

    public void addLiving(Living l) {
        this.live.add(l);
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

    public void resetVision() {
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                if (data[y][x] != null) {
                    if (vision == false) {
                        data[y][x].setSeen(false);
                        data[y][x].setLight(frame, 0);
                    } else {
                        data[y][x].setSeen(true);
                        data[y][x].setLight(frame, 8);
                    }
                }
            }
        }
    }

    public void calculateVision() {
        if (!vision) {
            resetVision();
            calculateVisionEntity(player);
            for (Living l : live) {
                if (l instanceof TorchPickup) {
                    calculateVisionEntity(l);
                }
            }
        } else {
            for (int y = 0; y < height; y++) {
                for (int x = 0; x < width; x++) {
                    if (data[y][x] != null) {
                        data[y][x].setLight(frame, 4);
                        data[y][x].setSeen(true);
                    }
                }
            }
        }
    }

    public void calculateVisionEntity(Entity e) {
        int mm = 5;
        for (int y = e.y() - mm; y < e.y() + mm; y++) {
            for (int x = e.x() - mm; x < e.x() + mm; x++) {
                if (x >= 0 && y >= 0 && x < width && y < height) {
                    if (data[y][x] != null) {
                        int dis = Math.abs(x - e.x()) + Math.abs(y - e.y());
                        if (dis < mm && los(e.x(), e.y(), x, y)) {
                            data[y][x].setSeen(true);
                            data[y][x].setLight(frame, mm - dis);
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

    /**
     * A star
     *
     * @param x target x
     * @param y target y
     */
    public void trypath(int x, int y) {

        if (x == player.x() && y == player.y()) {
            player.setUse(true);
            tick();
            return;
        }

        List<Floor> open = new ArrayList<>();
        List<Floor> closed = new ArrayList<>();
        if (x < 0 || y < 0 || x >= width || y >= height) {
            return;
        }
        Floor exit = data[y][x];
        if (exit.solid()) {
            return;
        }
        data[player.y()][player.x()].setG(0);
        open.add(data[player.y()][player.x()]);
        while (!open.isEmpty()) {
            Floor lowest = open.get(0);
            lowest.getF(x, y);
            for (Floor f : open) {
                if (f.getF(x, y) < lowest.getF(x, y)) {
                    lowest = f;
                }
            }
            if (lowest == exit) {
                Floor current = lowest;
                List<Floor> out = new ArrayList<>();
                while (current.getParent() != null) {
                    out.add(current);
                    current = current.getParent();
                }
                Collections.reverse(out);
                ttt.empty();
                for (Floor f : out) {
                    f.setRouting(true);
                    ttt.add(f);
                }
                if (!out.isEmpty()) {
                    out.get(0).setRouting(false);
                }
                break;
            }
            open.remove(lowest);
            closed.add(lowest);

            List<Floor> neigh = new ArrayList<>();
            if (lowest.x() - 1 > 0) {
                neigh.add(data[lowest.y()][lowest.x() - 1]);
            }
            if (lowest.x() + 1 < width - 1) {
                neigh.add(data[lowest.y()][lowest.x() + 1]);
            }
            if (lowest.y() - 1 > 0) {
                neigh.add(data[lowest.y() - 1][lowest.x()]);
            }
            if (lowest.y() + 1 < height - 1) {
                neigh.add(data[lowest.y() + 1][lowest.x()]);
            }
            for (Floor floor : neigh) {
                if (floor.solid() || closed.contains(floor)) {
                    continue;
                }
                int nextG = lowest.getG() + 10;

                boolean asd = false;
                if (!open.contains(floor)) {
                    open.add(floor);
                    asd = true;
                } else if (nextG < floor.getG()) {
                    asd = true;
                }
                if (asd) {
                    floor.setParent(lowest);
                    floor.setG(nextG);
                    floor.getH(x, y);
                }
            }
        }
        for (Floor f : closed) {
            f.cleanHeur();
        }
        for (Floor f : open) {
            f.cleanHeur();
        }
    }

    public boolean movePlayer(int dx, int dy) {
        if (!canMove(player, player.x(), player.y(), player.x() + dx, player.y() + dy)) {
            ttt.empty();
        }
        Floor f = data[player.y() + dy][ player.x() + dx];
        f.setRouting(false);
        boolean b = move(player, player.x(), player.y(), player.x() + dx, player.y() + dy);

        tick();
        if (!player.living()) {
            return false;
        }
        return b;
    }

    public void dropTorch(int x, int y) {
        List<Floor> possible = getNeighbours(x, y);
        TorchPickup torch = new TorchPickup();
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

    public boolean move(Entity e, int x1, int y1, int x2, int y2, boolean check) {
        if (check) {
            if (Math.abs(x1 - x2) > 0 && Math.abs(y1 - y2) > 0) {
                return false;
            }
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

    public boolean move(Entity e, int x1, int y1, int x2, int y2) {
        return move(e, x1, y1, x2, y2, true);
    }

    public void tick() {
        if (!player.living()) {
            return;
        }
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
        calculateVision();
        runHooks();
        frame++;
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
        resetVision();
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

    public int frames() {
        return frame;
    }

    /* 
     * Taken from http://www.roguebasin.com/index.php?title=Simple_Line_of_Sight
     */
    public boolean los(int x1, int y1, int x2, int y2) {
        if (x1 == x2 && y1 == y2) {
            return true;
        }
        int t, x, y, ax, ay, sx, sy, dx, dy;
        dx = x1 - x2;
        dy = y1 - y2;
        ax = Math.abs(dx) * 2;
        ay = Math.abs(dy) * 2;
        sx = (dx < 0 ? -1 : 1);
        sy = (dy < 0 ? -1 : 1);
        x = x2;
        y = y2;
        if (ax > ay) {
            t = ay - (ax / 2);
            do {
                if (t >= 0) {
                    y += sy;
                    t -= ax;
                }
                x += sx;
                t += ay;
                if (x == x1 && y == y1) {
                    return true;
                }
            } while (data[y][x] != null && !data[y][x].solid());
            return false;
        } else {
            t = ax - (ay / 2);
            do {
                if (t >= 0) {
                    x += sx;
                    t -= ay;
                }
                y += sy;
                t += ax;
                if (x == x1 && y == y1) {
                    return true;
                }
            } while (data[y][x] != null && !data[y][x].solid());
        }

        return false;
    }

    public void command() {
        if (!typing || type.length() < 0) {
            typing = false;
            type = "";
            typing_caret = 0;
            return;
        }
        player.addStatus(false, ":" + type);
        //if (type.contains(" ")) {
        Boolean tt = false;
        String[] cmds = type.split(" ");
        int len = cmds.length;
        switch (cmds[0]) {
            case "v":
            case "vision":
                setVision(!vision);
                tt = true;
                break;
            case "rv":
            case "resetvision":
                resetVision();
                calculateVision();
                tt = true;
                break;
            case "gold":
                if (len == 1) {
                    player.inventory().add(new GoldItem(1));
                } else {
                    try {
                        player.inventory().add(new GoldItem(Integer.parseInt(cmds[1])));
                    } catch (NumberFormatException ex) {

                    }
                }
                tt = true;
                break;
            case "xp":
                if (len == 2) {
                    try {
                        player.addXP(Integer.parseInt(cmds[1]));
                        tt = true;
                    } catch (NumberFormatException ex) {

                    }
                }
                break;
            case "heal":
            case "hp":
                if (len == 1) {
                    player.heal(999999999);
                } else if (len == 2) {
                    try {
                        player.heal(Integer.parseInt(cmds[1]));
                        tt = true;
                    } catch (NumberFormatException ex) {

                    }
                }
                break;
            case "tp":
                if (len == 3) {
                    try {
                        int px = player.x();
                        int py = player.y();
                        int dx = px + Integer.parseInt(cmds[1]);
                        int dy = py + Integer.parseInt(cmds[2]);
                        move(player, px, py, dx, dy, false);
                        tt = true;
                    } catch (NumberFormatException ex) {

                    }
                }
                break;
            default:
                player.addStatus("Unknown command");
        }
        if (tt) {
            tick();
        }
        //}
        if (type.length() > 0) {
            type_history.add(type);
        }
        typing = false;
        type = "";
        typing_caret = 0;
        type_history_place = 0;
    }
}
