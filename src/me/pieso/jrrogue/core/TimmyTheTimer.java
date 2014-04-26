package me.pieso.jrrogue.core;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import javax.swing.Timer;
import me.pieso.jrrogue.entity.Floor;

class AndyTheActionListener implements ActionListener {
    
    public final List<Floor> f;
    public final Game g;
    
    public AndyTheActionListener(Game g) {
        this.f = new ArrayList<>();
        this.g = g;
    }
    
    @Override
    public void actionPerformed(ActionEvent e) {
        if (!f.isEmpty()) {
            Floor ff = f.remove(0);
            g.movePlayer(ff.x() - g.getPlayer().x(), ff.y() - g.getPlayer().y());
        }
    }
    
}

public class TimmyTheTimer extends Timer {
    
    public TimmyTheTimer(int delay, Game g) {
        super(delay, new AndyTheActionListener(g));
        start();
    }
    
    public void add(Floor f) {
        ((AndyTheActionListener) getActionListeners()[0]).f.add(f);
    }
    
    public void empty() {
        List<Floor> ff = new ArrayList<>();
        ff.addAll(((AndyTheActionListener) getActionListeners()[0]).f);
        ((AndyTheActionListener) getActionListeners()[0]).f.clear();
        for (Floor f : ff) {
            f.setRouting(false);
        }
        //.clear();
    }
    
}
