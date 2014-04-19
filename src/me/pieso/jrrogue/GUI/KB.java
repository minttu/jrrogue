package me.pieso.jrrogue.GUI;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import me.pieso.jrrogue.core.Game;
import me.pieso.jrrogue.entity.Player;

public class KB implements KeyListener {

    private final Game game;

    public KB(Game game) {
        this.game = game;
    }

    @Override
    public void keyTyped(KeyEvent ke) {
    }

    @Override
    public void keyPressed(KeyEvent ke) {
        Player player = game.getPlayer();
        int x = player.x();
        int y = player.y();
        if(!player.living()) {
            return;
        }
        switch (ke.getKeyCode()) {
            case KeyEvent.VK_UP:
                game.move(player, x, y, x, y - 1);
                break;
            case KeyEvent.VK_DOWN:
                game.move(player, x, y, x, y + 1);
                break;
            case KeyEvent.VK_LEFT:
                game.move(player, x, y, x - 1, y);
                break;
            case KeyEvent.VK_RIGHT:
                game.move(player, x, y, x + 1, y);
                break;
            default:
                return;
        }
        game.tick();
    }

    @Override
    public void keyReleased(KeyEvent ke) {

    }

}
