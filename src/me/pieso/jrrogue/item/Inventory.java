package me.pieso.jrrogue.item;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.util.ArrayList;
import me.pieso.jrrogue.core.Game;
import me.pieso.jrrogue.util.GraphicsUtils;
import me.pieso.jrrogue.core.ResourceManager;

public class Inventory {

    private final ArrayList<Item> items;
    private final int maxitems;
    public static final char[] letters = {'q', 'w', 'e', 'a', 's', 'd', 'z', 'x', 'c'};

    public Inventory() {
        this.items = new ArrayList<>();
        this.maxitems = 9;
    }

    public Item findLinked(Class cls) {
        for (Item i : items) {
            if (i.linked() == cls) {
                return i;
            }
        }
        return null;
    }

    public Item find(String name) {
        for (Item i : items) {
            if (i.name().equals(name)) {
                return i;
            }
        }
        return null;
    }

    public Item findClass(Class cls) {
        for (Item i : items) {
            if (i.getClass() == cls) {
                return i;
            }
        }
        return null;
    }

    public boolean add(Item it) {
        if (it == null) {
            return false;
        }
        for (Item i : items) {
            if (i.getClass().isInstance(it)) {
                i.add(it.amount());
                return true;
            }
        }
        if (items.size() + 1 < maxitems) {
            items.add(it);
            return true;
        }
        return false;
    }

    public boolean remove(Item it) {
        for (Item i : items) {
            if (i.getClass().isInstance(it)) {
                i.remove(it.amount());
                return true;
            }
        }
        return false;
    }

    public boolean remove(int i, int amount) {
        if (i + 1 > items.size()) {
            return false;
        }
        Item it = items.get(i);

        if (it.amount() >= amount) {
            it.remove(amount);
            return true;
        }
        return false;
    }

    public boolean contains(Item it) {
        for (Item i : items) {
            if (i.getClass().isInstance(it)) {
                return true;
            }
        }
        return false;
    }

    public void checkUnloads(Game game) {
        for (Item it : items) {
            if (it.ready()) {
                it.unloadLinked(game);
            }
        }
    }

    public void draw(Graphics g, Rectangle rec) {
        int single = (rec.width - 6) / 3;
        g.setColor(ResourceManager.CGRAY);
        int height = (rec.height - 4) / 3 * (items.size() / 3 + 1) + 3;
        g.drawRect(rec.x, rec.y, rec.width, height);
        g.setColor(ResourceManager.DGRAY);
        g.fillRect(rec.x + 1, rec.y + 1, rec.width - 1, height - 1);
        for (int i = 0; i < items.size(); i++) {
            int row = i / 3;
            int col = i % 3;
            Rectangle nrec = new Rectangle(
                    rec.x + (single + 2) * col + 2,
                    rec.y + (single + 2) * row + 2,
                    single, single);

            items.get(i).draw(g, nrec);
            g.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 12));
            GraphicsUtils.drawTextOutlined(g,
                    new Rectangle(nrec.x + 1, nrec.y + 10, 0, 0),
                    "" + letters[i], Color.WHITE, Color.BLACK);
        }
    }
}
