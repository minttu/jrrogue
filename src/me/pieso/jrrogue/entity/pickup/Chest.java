package me.pieso.jrrogue.entity.pickup;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import me.pieso.jrrogue.core.Game;
import me.pieso.jrrogue.core.ResourceManager;
import me.pieso.jrrogue.entity.Entity;

public class Chest extends Pickup {

    private final List<Pickup> items;

    public Chest() {
        super(ResourceManager.getImage("chest"));
        this.items = new ArrayList<>();
        for (int i = 0; i < new Random().nextInt(5); i++) {
            items.add(new GoldPickup());
        }
        if (new Random().nextBoolean()) {
            int amo = (new Random().nextDouble() > 0.8 ? 2 : 1);
            for (int i = 0; i < amo; i++) {
                Pickup ite = null;
                switch (new Random().nextInt(6)) {
                    case 0:
                        ite = new SwordPickup();
                        break;
                    case 1:
                        ite = new ArmorPickup();
                        break;
                    case 2:
                        ite = new RingPickup();
                        break;
                    default:
                        ite = (new Random().nextBoolean() ? new FoodPickup() : new TorchPickup());
                }
                items.add(ite);
            }
        }
    }

    public List<Pickup> items() {
        return items;
    }

    @Override
    public String name() {
        return "chest";
    }

    @Override
    public void tick(Game game) {

    }

    @Override
    public void bumpedBy(Entity e) {

    }

    @Override
    public void bumped(Entity e) {

    }

}
