package me.pieso.jrrogue.core;

import java.util.ArrayList;
import java.util.List;
import me.pieso.jrrogue.entity.Floor;

public class NeighbourIterator {

    public static List<Floor> filter(List<Floor> list, NeighbourFilter nf) {
        ArrayList<Floor> res = new ArrayList<>();
        for (Floor e : list) {
            if (nf.accept(e)) {
                res.add(e);
            }
        }
        return res;
    }
}
