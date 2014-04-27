package me.pieso.jrrogue.item;

import java.awt.Color;

public class Tier {

    public static final Tier[] tiers = new Tier[]{
        new Tier("wood", 0.5, new Color(200, 121, 0)),
        new Tier("bronze", 1, new Color(255, 135, 0)),
        new Tier("silver", 1.5, new Color(210, 210, 210)),
        new Tier("gold", 2, new Color(240, 240, 0)),
        new Tier("iron", 3, new Color(150, 150, 150)),
        new Tier("hellstone", 5, new Color(220, 75, 75)),
        new Tier("elf", 7, new Color(75, 75, 220)),
        new Tier("uranium", 9, new Color(75, 220, 75)),
        new Tier("crystal", 12, new Color(235, 235, 235))
    };

    public final String name;
    public final double val;
    public final Color col;

    public Tier(String name, double val, Color col) {
        this.name = name;
        this.val = val;
        this.col = col;
    }
}
