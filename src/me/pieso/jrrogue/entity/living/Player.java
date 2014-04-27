package me.pieso.jrrogue.entity.living;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import me.pieso.jrrogue.core.Game;
import me.pieso.jrrogue.core.ResourceManager;
import me.pieso.jrrogue.entity.Entity;
import me.pieso.jrrogue.entity.pickup.Chest;
import me.pieso.jrrogue.entity.pickup.Pickup;
import me.pieso.jrrogue.item.ArmorItem;
import me.pieso.jrrogue.item.FoodItem;
import me.pieso.jrrogue.item.GoldItem;
import me.pieso.jrrogue.item.Inventory;
import me.pieso.jrrogue.item.Item;
import me.pieso.jrrogue.item.RingItem;
import me.pieso.jrrogue.item.SwordItem;
import me.pieso.jrrogue.item.TorchItem;

public class Player extends Living {

    private int xp;
    private int maxxp;
    private int level;
    private final List<String> status;
    private int moves;
    private boolean ascend;
    private boolean use;
    private int dungeon;
    private final Inventory inventory;
    private double hunger;
    private int lret;

    public Player() {
        super(ResourceManager.getImage("player"), 16);

        level = 1;
        xp = 0;
        maxxp = 10;
        status = new ArrayList<>();
        moves = 0;
        ascend = false;
        use = false;
        dungeon = 0;
        hunger = 0;
        lret = 0;

        inventory = new Inventory();
        inventory.add(new SwordItem());
        inventory.add(new SwordItem());
        inventory.add(new ArmorItem());
        inventory.add(new RingItem());
        inventory.add(new TorchItem(6));
        inventory.add(new GoldItem(0));
        inventory.add(new FoodItem(2));

        for (int i = 0; i < 10; i++) {
            status.add("\n");
        }
    }

    @Override
    public String name() {
        return "player";
    }

    public Inventory inventory() {
        return this.inventory;
    }

    public int XP() {
        return xp;
    }

    public int maxXP() {
        return maxxp;
    }

    public int level() {
        return level;
    }

    public double hunger() {
        return hunger;
    }

    public void addXP(int amount) {
        xp += amount;
        while (xp >= maxxp) {
            levelUp();
        }
    }

    private void levelUp() {
        xp -= maxxp;
        maxxp *= 1.5;
        level++;
        addStatus("You reached level", level + "");
    }

    public String toStatus() {
        StringBuilder sb = new StringBuilder();
        for (String s : status) {
            sb.append(s);
        }
        lret++;
        return sb.toString();
    }

    public void addStatus(boolean dot, String... sss) {
        String ss = lret + "@";
        for (int i = 0; i < sss.length; i++) {
            if (sss[i].length() > 0) {
                ss += sss[i] + (i == sss.length - 1 ? (dot ? "." : "") + "\n" : " ");
            }
        }
        status.add(ss);
        if (status.size() > 10) {
            status.remove(0);
        }
    }

    public void addStatus(String... sss) {
        addStatus(true, sss);
    }

    public int lret() {
        return lret;
    }

    @Override
    public void tick(Game game) {
        inventory.checkUnloads(game);
        SwordItem sword = (SwordItem) inventory.findClass(SwordItem.class);
        if (sword != null) {
            setDmg(sword.getDamage());
        }
        ArmorItem armor = (ArmorItem) inventory.findClass(ArmorItem.class);
        if (sword != null) {
            setMaxHP((int) (oghp() + ((maxxp / 10) * armor.value())));
        }
        if (moves % 40 == 0) {
            heal(maxhp() / 10);
        }
        hunger += 0.0005;
        if (hunger >= 1) {
            addStatus("You died from hunger");
            takeDamage(hp(), null);
        } else if (hunger > 0.95) {
            addStatus("You are extremely hungry");
            addStatus("You should consider eating");
            takeDamage(1, null);
        } else if (hunger > 0.8) {
            addStatus("You are really hungry");
        }
        moves++;
    }

    @Override
    public void bumpedBy(Entity e) {

    }

    @Override
    public boolean takeDamage(int amount, Living from) {
        if (from != null && !from.living()) {
            return false;
        }
        boolean b = super.takeDamage(amount, from);
        if (from != null) {
            if (amount > 0) {
                addStatus("The", from.name(), dhit[new Random().nextInt(dhit.length)], "you");
            } else {
                addStatus("The", from.name(), dmiss[new Random().nextInt(dmiss.length)], "missed you");
            }
            /*if (hp() < 1) {
             addStatus("It was fatal");
             }*/
        }
        if (b && hp() < 1) {
            addStatus("You are dead, game over");
        }
        return b;
    }

    @Override
    public void bumped(Entity e) {
        if (e instanceof Chest) {
            Chest c = (Chest) e;
            c.takeDamage(1, this);
            addStatus("You opened a chest");
            if (c.items().isEmpty()) {
                addStatus("It was empty");
            }
            for (Pickup p : c.items()) {
                addStatus("You picked up", p.name());
                Item i = inventory.findLinked((p).getClass());
                if (i != null) {
                    i.add(1);
                }
            }
        } else if (e instanceof Pickup) {
            ((Pickup) e).takeDamage(1, this);
            addStatus("You picked up", ((Pickup) e).name());
            Item i = inventory.findLinked(((Pickup) e).getClass());
            if (i != null) {
                i.add(1);
            }
        } else if (e instanceof Monster) {
            Living m = (Living) e;
            int dmg = getDamage();
            if (dmg == 0) {
                addStatus("You", dmiss[new Random().nextInt(dmiss.length)], "missed the", m.name());
                return;
            }
            addStatus("You", dhit[new Random().nextInt(dhit.length)], "the", m.name());
            if (m.takeDamage(dmg, this)) {
                addXP(m.toXP());
                addStatus("The", m.name(), dmesg[new Random().nextInt(dmesg.length)]);
            }
        }
    }

    public void setAscend(boolean bln) {
        ascend = bln;
    }

    public boolean shouldAscend() {
        return ascend;
    }

    public void setUse(boolean b) {
        use = b;
    }

    public boolean use() {
        return use;
    }

    public int dungeon() {
        return dungeon;
    }

    public void setDungeon(int level) {
        this.dungeon = level;
        addStatus("Welcome to the", level + ".", "dungeon");
    }

    public void eat(double d) {
        this.hunger -= d;
        this.hunger = Math.max(0, this.hunger);
    }

    public int moves() {
        return moves;
    }

}
