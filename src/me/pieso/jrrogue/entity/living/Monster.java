package me.pieso.jrrogue.entity.living;

import java.util.List;
import java.util.Random;
import me.pieso.jrrogue.core.Game;
import me.pieso.jrrogue.core.ResourceManager;
import me.pieso.jrrogue.entity.Entity;
import me.pieso.jrrogue.entity.Floor;
import me.pieso.jrrogue.misc.Damage;
import me.pieso.jrrogue.util.NeighbourFilter;
import me.pieso.jrrogue.util.NeighbourIterator;

class MonsterType {

    public final int hp;
    public final int mindmg;
    public final int maxdmg;
    public final String name;
    public final String image;

    public MonsterType(String name, String image, int hp, int dmg1, int dmg2) {
        this.image = image;
        this.name = name;
        this.hp = hp;
        this.mindmg = dmg1;
        this.maxdmg = dmg2;
    }
}

public class Monster extends Living {

    private static final MonsterType[] types = {
        new MonsterType("bat", "b", 4, 1, 2),
        new MonsterType("goblin", "g", 7, 2, 4),
        new MonsterType("mursu", "m", 12, 1, 1)};
    private final String name;

    public Monster() {
        super(ResourceManager.getImage("undefined"), 0);
        MonsterType m = types[new Random().nextInt(types.length)];
        setImage(ResourceManager.getImage("monster/" + m.image));
        limit(m.hp);
        setDmg(new Damage(m.mindmg, m.maxdmg));
        this.name = m.name;
        setSolid(true);
    }

    @Override
    public String name() {
        return name;
    }

    @Override
    public void tick(Game game) {
        List<Floor> plr = NeighbourIterator.filter(game.getNeighbours(x(), y(), 4), new NeighbourFilter() {

            @Override
            public boolean accept(Floor e) {
                Entity ent = e.get();
                if (ent != null) {
                    if (ent instanceof Player) {
                        return true;
                    }
                }
                return false;
            }
        });
        if (plr.isEmpty()) {
            game.moveRandomly(this);
        } else {
            game.moveTowards(this, plr.get(0).get());
        }
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
