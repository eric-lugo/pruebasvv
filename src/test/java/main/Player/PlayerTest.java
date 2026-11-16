package main.Player;

import main.Commons;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import space_invaders.sprites.Player;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class PlayerTest {
    Player player;
    private Component component;

    @BeforeEach
    void setUp() {
        player = new Player();
        component = new Component() {};
    }

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
    public void testInitPlayer(){
        try{
            Method metodo = Player.class.getDeclaredMethod("initPlayer");
            metodo.setAccessible(true);
            metodo.invoke(player);

        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        } catch (InvocationTargetException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }

        int xEsperado = Commons.BOARD_WIDTH / 2;
        int yEsperado = Commons.GROUND - 10;

        assertEquals(xEsperado, player.getX());
        assertEquals(yEsperado, player.getY());
    }

    @Test
    public void testActSinMovimiento(){
        player.setX(179);
        setDxPrivate(player, 0);
        player.act();
        assertEquals(179, player.getX());
    }

    @Test
    public void testActMovimientoDerecha(){
        player.setX(179);
        setDxPrivate(player, 2);
        player.act();
        assertEquals(181, player.getX());
    }

    @Test
    public void testActMovimientoIzquierda(){
        player.setX(179);
        setDxPrivate(player, -2);
        player.act();
        assertEquals(177, player.getX());
    }

    @Test
    public void testActMovDerBorde(){
        player.setX(341);
        setDxPrivate(player, 2);
        player.act();
        assertEquals(343, player.getX());
    }

    @Test
    public void testActMovIzqBorde(){
        player.setX(2);
        setDxPrivate(player, -2);
        player.act();
        assertEquals(0, player.getX());
    }

    @Test
    public void testActDerNoMovimiento(){
        player.setX(343);
        setDxPrivate(player, 2);
        player.act();
        assertEquals(343, player.getX());
    }

    @Test
    public void testActIzqNoMovimiento(){
        player.setX(0);
        setDxPrivate(player, -2);
        player.act();
        assertEquals(0, player.getX());
    }

    @ParameterizedTest(name = "{0}: x={1}, dx={2} -> xEsperado={3}")
    @CsvSource({
            "CP-PL03 (Movimiento derecha normal),      179,  2, 181",
            "CP-PL04 (Mov. izq. hasta borde x=0),        2, -2,   0",
            "CP-PL05 (Límite inferior sobrepasado),      0, -2,   0",
            "CP-PL06 (Mov. der. hasta borde x=343),    341,  2, 343",
            "CP-PL07 (Límite superior sobrepasado),    343,  2, 343",
            "CP-PL08 (Sin movimiento dx=0),            179,  0, 179",
            "CP-PL09 (Movimiento izquierda normal),    179, -2, 177"
    })
    public void testAct_CajaNegra(String idCaso, int xInicial, int dx, int xEsperado) {
        player.setX(xInicial);
        setDxPrivate(player, dx);

        player.act();

        assertEquals(xEsperado, player.getX());
    }


    @Test
    public void testKeyPressedIzquierda(){
        setDxPrivate(player, 0);
        KeyEvent eventLeftPressed = new KeyEvent(
                component,
                KeyEvent.KEY_PRESSED,
                System.currentTimeMillis(),
                0,
                KeyEvent.VK_LEFT,
                KeyEvent.CHAR_UNDEFINED
        );

        player.keyPressed(eventLeftPressed);

        assertEquals(-2, getDxPrivate(player));
    }

    @Test
    public void testKeyPressedDerecha(){
        setDxPrivate(player, 0);
        KeyEvent eventRightPressed = new KeyEvent(
                component,
                KeyEvent.KEY_PRESSED,
                System.currentTimeMillis(),
                0,
                KeyEvent.VK_RIGHT,
                KeyEvent.CHAR_UNDEFINED
        );

        player.keyPressed(eventRightPressed);

        assertEquals(2, getDxPrivate(player));
    }

    @Test
    public void testKeyPressedNoValido(){
        setDxPrivate(player, 0);
        KeyEvent eventSpacePressed = new KeyEvent(
                component,
                KeyEvent.KEY_PRESSED,
                System.currentTimeMillis(),
                0,
                KeyEvent.VK_SPACE,
                KeyEvent.CHAR_UNDEFINED
        );

        player.keyPressed(eventSpacePressed);

        assertEquals(0, getDxPrivate(player));
    }

    @Test
    public void testKeyReleasedIzquierda(){
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
    public void testKeyReleasedDerecha(){
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
    public void testKeyReleasedNoValido(){
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
