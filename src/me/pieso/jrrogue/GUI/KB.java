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
        int x = 0;
        int y = 0;
        if (!player.living()) {
            return;
        }
        switch (ke.getKeyCode()) {
            case KeyEvent.VK_K:
            case KeyEvent.VK_UP:
                y--;
                break;
            case KeyEvent.VK_J:
            case KeyEvent.VK_DOWN:
                y++;
                break;
            case KeyEvent.VK_H:
            case KeyEvent.VK_LEFT:
                x--;
                break;
            case KeyEvent.VK_L:
            case KeyEvent.VK_RIGHT:
                x++;
                break;
            case KeyEvent.VK_SPACE:
                break;
            case KeyEvent.VK_T:
                game.dropTorch(player.x(), player.y());
                break;
            case KeyEvent.VK_PLUS:
                game.zoomup();
                return;
            case KeyEvent.VK_MINUS:
                game.zoomdown();
                return;
            default:
                return;
        }
        game.movePlayer(x, y);
    }

    @Override
    public void keyReleased(KeyEvent ke) {

    }

}
