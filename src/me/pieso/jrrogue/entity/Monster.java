package me.pieso.jrrogue.entity;

import java.awt.Color;
import java.awt.Graphics;
import java.util.Random;
import me.pieso.jrrogue.core.Game;
import me.pieso.jrrogue.core.ResourceManager;

class MonsterType {

    public final int hp;
    public final int mindmg;
    public final int maxdmg;
    public final String image;

    public MonsterType(String image, int hp, int dmg1, int dmg2) {
        this.image = image;
        this.hp = hp;
        this.mindmg = dmg1;
        this.maxdmg = dmg2;
    }
}

public class Monster extends Living {

    private static final MonsterType[] types = {new MonsterType("morso", 10, 1, 2)};
    private final String name;

    public Monster() {
        super(ResourceManager.getImage("undefined"), 0);
        MonsterType m = types[new Random().nextInt(types.length)];
        setImage(ResourceManager.getImage(m.image));
        limit(m.hp);
        setMinDmg(m.mindmg);
        setMaxDmg(m.maxdmg);
        this.name = m.image;
    }

    @Override
    public String name() {
        return name;
    }

    @Override
    public void tick(Game game) {
        game.moveRandomly(this);
    }

    @Override
    public void bumpedBy(Entity e) {

    }

    @Override
    public void bumped(Entity e) {
        if (e.getClass() == Player.class) {
            Player p = (Player) e;
            p.takeDamage(getDamage(), this);
        }
    }

}
