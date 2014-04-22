package me.pieso.jrrogue.entity.living;

import java.util.ArrayList;
import java.util.List;
import me.pieso.jrrogue.core.Game;
import me.pieso.jrrogue.core.ResourceManager;
import me.pieso.jrrogue.entity.Entity;
import me.pieso.jrrogue.entity.pickup.Pickup;
import me.pieso.jrrogue.item.FoodItem;
import me.pieso.jrrogue.item.GoldItem;
import me.pieso.jrrogue.item.Inventory;
import me.pieso.jrrogue.item.Item;
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
    private Inventory inventory;
    private double hunger;

    public Player() {
        super(ResourceManager.getImage("player"), 16);

        level = 1;
        xp = 0;
        maxxp = 20;
        status = new ArrayList<>();
        moves = 0;
        ascend = false;
        use = false;
        dungeon = 0;
        hunger = 0;

        inventory = new Inventory();
        inventory.add(new SwordItem());
        inventory.add(new TorchItem(6));
        inventory.add(new GoldItem(0));
        inventory.add(new FoodItem(2));

        setMinDmg(0);
        setMaxDmg(0);
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

    public void addXP(int amount) {
        xp += amount;
        while (xp >= maxxp) {
            levelUp();
        }
    }

    private void levelUp() {
        maxxp *= 2;
        limit(level * 6);
        level++;
        setMinDmg((int) Math.round(minDmg() * 1.5));
        setMaxDmg((int) Math.round(maxDmg() * 1.5));
        addStatus("You reached level", level + "");
    }

    public String toStatus() {
        StringBuilder sb = new StringBuilder();
        sb.append("HP ").append(hp()).append("/").append(maxhp());
        sb.append(" | ");
        sb.append("DPT ").append(minDmg()).append("-").append(maxDmg());
        sb.append(" | ");
        sb.append("LV ").append(level);
        sb.append(" | ");
        sb.append("XP ").append(xp).append("/").append(maxxp);
        sb.append(" | ");
        sb.append("Dungeon ").append(dungeon);
        sb.append("\n");
        for (String s : status) {
            sb.append(s).append(" ");
        }
        status.clear();
        return sb.toString();
    }

    public void addStatus(String... sss) {
        String ss = "";
        for (int i = 0; i < sss.length; i++) {
            ss += sss[i] + (i == sss.length - 1 ? "." : " ");
        }
        status.add(ss);
    }

    @Override
    public void tick(Game game) {
        inventory.checkUnloads(game);
        SwordItem sword = (SwordItem) inventory.findClass(SwordItem.class);
        if (sword != null) {
            setMaxDmg(sword.maxDmg());
            setMinDmg(sword.minDmg());
        }
        moves++;
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

    }

    @Override
    public void bumpedBy(Entity e) {

    }

    @Override
    public boolean takeDamage(int amount, Living from) {
        boolean b = super.takeDamage(amount, from);
        if (from != null) {
            if (amount > 0) {
                addStatus("Took damage from the", from.name());
            } else {
                addStatus("The", from.name(), "missed you");
            }
            if (hp() < 1) {
                addStatus("It was fatal");
            }
        }
        if (hp() < 1) {
            addStatus("You are dead");
            addStatus("Game over");
            addStatus("Goodbye");
        }
        return b;
    }

    @Override
    public void bumped(Entity e) {
        if (e instanceof Pickup) {
            ((Pickup) e).takeDamage(1, this);
            addStatus("You took the", ((Pickup) e).name());
            Item i = inventory.findLinked(((Pickup) e).getClass());
            if (i != null) {
                i.add(1);
            }
        }
        if (e instanceof Monster) {
            Living m = (Living) e;
            int dmg = getDamage();
            if (dmg == 0) {
                addStatus("You missed the", m.name());
                return;
            }
            addStatus("You hit the", m.name());
            if (m.takeDamage(dmg, this)) {
                addXP(m.toXP());
                addStatus("The", m.name(), "died");
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

    public void dungeon(int level) {
        this.dungeon = level;
        addStatus("Welcome to the", level + ".", "dungeon");
    }

}
