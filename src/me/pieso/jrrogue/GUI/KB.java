package me.pieso.jrrogue.GUI;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import me.pieso.jrrogue.core.Game;
import me.pieso.jrrogue.entity.living.Player;
import me.pieso.jrrogue.item.Inventory;

public class KB implements KeyListener {

    private final Game game;

    public KB(Game game) {
        this.game = game;
    }

    @Override
    public void keyTyped(KeyEvent ke) {
    }

    public boolean isPrintableChar(char c) {
        Character.UnicodeBlock block = Character.UnicodeBlock.of(c);
        return (!Character.isISOControl(c))
                && c != KeyEvent.CHAR_UNDEFINED
                && block != null
                && block != Character.UnicodeBlock.SPECIALS;
    }

    @Override
    public void keyPressed(KeyEvent ke) {
        if (game.typing) {
            if (ke.getKeyCode() == KeyEvent.VK_ESCAPE) {
                game.type = "";
                game.typing = false;
                game.typing_caret = 0;
            } else if (ke.getKeyCode() == KeyEvent.VK_ENTER) {
                game.command();
            } else if (ke.getKeyCode() == KeyEvent.VK_UP) {
                if (!game.type_history.isEmpty()) {
                    game.type_history_place = Math.min(game.type_history.size(), game.type_history_place + 1);
                    game.type = game.type_history.get(game.type_history.size() - game.type_history_place);
                    game.typing_caret = game.type.length();
                }
            } else if (ke.getKeyCode() == KeyEvent.VK_DOWN) {
                if (game.type_history_place > 1) {
                    game.type_history_place = Math.max(0, game.type_history_place - 1);
                    game.type = game.type_history.get(game.type_history.size() - game.type_history_place);
                    game.typing_caret = game.type.length();
                } else {
                    game.type_history_place = 0;
                    game.type = "";
                    game.typing_caret = 0;
                }
            } else if (ke.getKeyCode() == KeyEvent.VK_RIGHT) {
                game.typing_caret = Math.min(game.type.length(), game.typing_caret + 1);
            } else if (ke.getKeyCode() == KeyEvent.VK_LEFT) {
                game.typing_caret = Math.max(0, game.typing_caret - 1);
            } else if (ke.getKeyCode() == KeyEvent.VK_BACK_SPACE) {
                if (game.typing_caret > 0) {
                    game.type = game.type.substring(0, game.typing_caret - 1) + game.type.substring(game.typing_caret);
                    game.typing_caret = Math.max(0, game.typing_caret - 1);
                }
            } else if (isPrintableChar(ke.getKeyChar())) {
                game.type = game.type.substring(0, game.typing_caret) + ke.getKeyChar() + game.type.substring(game.typing_caret);
                game.typing_caret++;
            }
            game.runHooks();
            return;
        }
        Player player = game.getPlayer();
        int x = 0;
        int y = 0;
        if (!player.living()) {
            return;
        }
        out:
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
                player.setUse(true);
                break;
            case KeyEvent.VK_PERIOD:
                if (ke.isShiftDown()) {
                    game.typing = true;
                    game.runHooks();
                }
                return;
            case KeyEvent.VK_PLUS:
                game.zoomup();
                return;
            case KeyEvent.VK_MINUS:
                game.zoomdown();
                return;
            default:
                for (int c = 0; c < Inventory.letters.length; c++) {
                    char cc = Inventory.letters[c];
                    if (ke.getKeyChar() == cc) {
                        player.inventory().remove(c, 1);
                        break out;
                    }
                }
                return;
        }
        game.movePlayer(x, y);
    }

    @Override
    public void keyReleased(KeyEvent ke) {

    }

}
