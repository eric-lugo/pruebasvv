package PlayerTests;

import org.junit.jupiter.api.Test;
import space_invaders.sprites.Player;

import java.awt.Button;
import java.awt.event.KeyEvent;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class KeyPressedWhiteBox {

    @Test
    void C1_otraTecla() {
        Player player = new Player();
        KeyEvent e = new KeyEvent(new Button(), KeyEvent.KEY_PRESSED, 0, 0, KeyEvent.VK_SPACE, KeyEvent.CHAR_UNDEFINED);
        player.keyPressed(e);
        player.act();
        assertEquals(179, player.getX());
    }

    @Test
    void C2_moverIzquierda() {
        Player player = new Player();
        KeyEvent e = new KeyEvent(new Button(), KeyEvent.KEY_PRESSED, 0, 0, KeyEvent.VK_LEFT, KeyEvent.CHAR_UNDEFINED);
        player.keyPressed(e);
        player.act();
        assertEquals(177, player.getX());
    }

    @Test
    void C3_moverDerecha() {
        Player player = new Player();
        KeyEvent e = new KeyEvent(new Button(), KeyEvent.KEY_PRESSED, 0, 0, KeyEvent.VK_RIGHT, KeyEvent.CHAR_UNDEFINED);
        player.keyPressed(e);
        player.act();
        assertEquals(181, player.getX());
    }
}