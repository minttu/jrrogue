package me.pieso.jrrogue.GUI;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import me.pieso.jrrogue.core.Game;

public class Mouse implements MouseListener {

    private final GameArea ga;
    private final Game game;

    Mouse(Game game, GameArea ga) {
        this.game = game;
        this.ga = ga;
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        if (!ga.hasFocus()) {
            ga.requestFocus();
        }
    }

    @Override
    public void mousePressed(MouseEvent e) {
        if (e.getButton() == 1) {
            int x = (e.getX() - ga.offsetX()) / game.getSide();
            int y = (e.getY() - ga.offsetY()) / game.getSide();
            game.trypath(x, y);
        }
    }

    @Override
    public void mouseReleased(MouseEvent e) {

    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }

}
