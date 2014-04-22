package me.pieso.jrrogue.util;

import me.pieso.jrrogue.entity.Floor;

public interface NeighbourFilter {

    public abstract boolean accept(Floor e);
}
