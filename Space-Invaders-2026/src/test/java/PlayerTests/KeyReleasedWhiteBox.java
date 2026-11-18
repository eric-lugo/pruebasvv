package PlayerTests;


import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.awt.event.KeyEvent;

import static org.junit.jupiter.api.Assertions.assertEquals;


import space_invaders.sprites.Player;

import java.awt.Component;
import java.lang.reflect.Field;


public class KeyReleasedWhiteBox {


    private Player player = new Player();
    private Component component = new Component() {};



    private void setDxPrivate(Player player, int dx) {
        try {
            Field dxPublic;
            dxPublic = Player.class.getSuperclass().getDeclaredField("dx");
            dxPublic.setAccessible(true);
            dxPublic.setInt(player, dx);
        } catch (Exception e) {
            throw new RuntimeException("Error al modificar dx", e);
        }
    }


    private int getDxPrivate(Player player) {
        try {
            Field dx;
            dx = Player.class.getSuperclass().getDeclaredField("dx");
            dx.setAccessible(true);
            return dx.getInt(player);
        } catch (Exception e) {
            throw new RuntimeException("Error al leer dx", e);
        }
    }


    @Test
    @DisplayName("Camino 1: soltar izquierda")
    void testCamino1() {
        setDxPrivate(player, -2);
        KeyEvent eventLeftReleased = new KeyEvent(
                component,
                KeyEvent.KEY_RELEASED,
                System.currentTimeMillis(),
                0,
                KeyEvent.VK_LEFT,
                KeyEvent.CHAR_UNDEFINED
        );

        player.keyReleased(eventLeftReleased);

        assertEquals(0, getDxPrivate(player));

    }

    @Test
    @DisplayName("Camino 2: soltar derecha")
    void testCamino2() {
        setDxPrivate(player, 2);
        KeyEvent eventRightReleased = new KeyEvent(
                component,
                KeyEvent.KEY_RELEASED,
                System.currentTimeMillis(),
                0,
                KeyEvent.VK_RIGHT,
                KeyEvent.CHAR_UNDEFINED
        );

        player.keyReleased(eventRightReleased);

        assertEquals(0, getDxPrivate(player));
    }

    @Test
    @DisplayName("Camino 3: soltar otra tecla")
    void testCamino3() {

        setDxPrivate(player, 2);
        KeyEvent eventSpacePressed = new KeyEvent(
                component,
                KeyEvent.KEY_RELEASED,
                System.currentTimeMillis(),
                0,
                KeyEvent.VK_SPACE,
                KeyEvent.CHAR_UNDEFINED
        );

        player.keyReleased(eventSpacePressed);

        assertEquals(2, getDxPrivate(player));

    }
}