package Alien;

import main.Commons;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import space_invaders.sprites.Alien;
import space_invaders.sprites.Sprite;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import static org.junit.jupiter.api.Assertions.*;

public class InitBombWhiteBox {
    private Field x;
    private Field y;
    private Method metodo;
    private Alien alien;
    private Alien.Bomb bomb;

    @BeforeEach
    public void setUp() throws Exception {
        alien= new Alien(50,50);
        bomb= alien.getBomb();

        x= Sprite.class.getDeclaredField("x");
        y= Sprite.class.getDeclaredField("y");
        metodo= Alien.Bomb.class.getDeclaredMethod("initBomb", int.class, int.class);
        x.setAccessible(true);
        y.setAccessible(true);
        metodo.setAccessible(true);


    }

    @Test
    @DisplayName("Camino 1")
    public void testCamino1() throws IllegalAccessException, InvocationTargetException {

        x.setInt(bomb, 10);
        y.setInt(bomb, 20);

        metodo.invoke(bomb, 150, 150);

        assertEquals(160, x.getInt(bomb));
        assertEquals(170, y.getInt(bomb));
        assertTrue(bomb.isDestroyed());
    }

    @Test
    @DisplayName("Camino 2")
    public void testCamino2() throws IllegalAccessException, InvocationTargetException {

        x.setInt(bomb, 10);
        y.setInt(bomb, 20);

        metodo.invoke(bomb, 400, 400);

        assertEquals(Commons.BOARD_WIDTH, x.getInt(bomb));
        assertEquals(Commons.BOARD_HEIGHT, y.getInt(bomb));
        assertTrue(bomb.isDestroyed());
    }




}


