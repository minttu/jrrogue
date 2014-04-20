package me.pieso.jrrogue.entity.pickup;

import me.pieso.jrrogue.entity.living.Living;
import java.awt.image.BufferedImage;

public abstract class Pickup extends Living {

    public Pickup(BufferedImage image) {
        super(image, 1);
        setMinDmg(0);
        setMaxDmg(0);
    }

}
