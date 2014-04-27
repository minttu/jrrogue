package me.pieso.jrrogue.misc;

import java.util.Random;

public class Damage {

    private final double min;
    private final double max;

    public Damage(double min, double max) {
        this.min = min;
        this.max = max;
    }

    public double min() {
        return min;
    }

    public double max() {
        return max;
    }

    public int dmg() {
        if (max - min > 1) {
            return (int) Math.round(new Random().nextDouble() * (max - min) + min); //Random().nextInt(max - min) + min;
        }
        return (int) min;
    }
}
