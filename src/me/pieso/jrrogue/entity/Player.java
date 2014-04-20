package me.pieso.jrrogue.entity;

import java.util.ArrayList;
import java.util.List;
import me.pieso.jrrogue.core.Game;
import me.pieso.jrrogue.core.ResourceManager;

public class Player extends Living {

    private int xp;
    private int maxxp;
    private int level;
    private List<String> status;
    private int moves;

    public Player() {
        super(ResourceManager.getImage("player"), 16);

        level = 1;
        xp = 0;
        maxxp = 20;
        status = new ArrayList<>();
        moves = 0;

        setMinDmg(3);
        setMaxDmg(6);
    }

    @Override
    public String name() {
        return "player";
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
        sb.append("[HP ").append(hp()).append("/").append(maxhp()).append("]");
        sb.append(" [DMG ").append(minDmg()).append("-").append(maxDmg()).append("]");
        sb.append(" [POS ").append(x()).append(",").append(y()).append("]");
        sb.append(" [XP ").append(xp).append("/").append(maxxp).append("]");
        sb.append(" [Level ").append(level).append("]");
        sb.append(" [Turn ").append(moves).append("]");
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
        moves++;
        if (moves % 25 == 0) {
            heal(maxhp() / 10);
        }
    }

    @Override
    public void bumpedBy(Entity e) {

    }

    @Override
    public boolean takeDamage(int amount, Living from) {
        boolean b = super.takeDamage(amount, from);
        if (amount > 0) {
            addStatus("Took damage from the", from.name());
        } else {
            addStatus("The", from.name(), "missed you");
        }
        return b;
    }

    @Override
    public void bumped(Entity e) {
        if (e instanceof Torch) {
            ((Torch) e).takeDamage(1, this);
            addStatus("You broke the torch");
            return;
        }
        if (e instanceof Living) {
            if (e.equals(this)) {
                return;
            }
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

}
