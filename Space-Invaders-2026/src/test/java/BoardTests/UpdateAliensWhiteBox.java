package BoardTests;

import main.Board;
import main.Commons;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import space_invaders.sprites.Alien;
import space_invaders.sprites.Sprite;


import javax.swing.*;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class UpdateAliensWhiteBox {
    private Board board;
    private List<Alien> aliens;
    private Method metodo;
    private Method visibleAlien;

    @BeforeEach
    public void setUp() throws Exception {
        SwingUtilities.invokeAndWait(() -> {
            board = new Board();
            board.getTimer().stop();
        });
        aliens= new ArrayList<>();
        board.setAliens(aliens);

        metodo= Board.class.getDeclaredMethod("update_aliens");
        visibleAlien= Sprite.class.getDeclaredMethod("setVisible",boolean.class);
        metodo.setAccessible(true);
        visibleAlien.setAccessible(true);
    }

    @AfterEach
    public void tearDown() throws Exception {

        SwingUtilities.invokeAndWait(() -> {
            if (board != null && board.getTimer() != null) {
                board.getTimer().stop();
            }
        });
    }

    @Test
    @DisplayName("Camino 1")
    public void testCamino1() throws IllegalAccessException, InvocationTargetException {
        board.setAliens(aliens);
        metodo.invoke(board);

        assertTrue(board.getAliens().isEmpty());
        assertEquals(-1, board.getDirection());
        assertTrue(board.isInGame());
    }

    @Test
    @DisplayName("Camino 2")
    public void testCamino2() throws IllegalAccessException, InvocationTargetException {
        int x = Commons.BORDER_LEFT;
        int y = 100;

        Alien alien = new Alien(x, y);
        alien.setX(x);
        alien.setY(y);
        visibleAlien.invoke(alien,false);

        aliens.add(alien);

        board.setDirection(-1);

        metodo.invoke(board);

        assertEquals(x, alien.getX());
        assertEquals(y, alien.getY());
        assertEquals(-1, board.getDirection());
        assertTrue(board.isInGame());

    }

    @Test
    @DisplayName("Camino 3")
    public void testCamino3() throws Exception {

        int x = Commons.BORDER_LEFT;
        int y = 0;

        Alien alien = new Alien(x, y);
        alien.setX(x);
        alien.setY(y);
        visibleAlien.invoke(alien,true);

        aliens.add(alien);

        board.setDirection(-1);

        metodo.invoke(board);

        assertEquals(x - 1, alien.getX());
        assertEquals(y, alien.getY());
        assertEquals(-1, board.getDirection());
        assertTrue(board.isInGame());
    }

    @Test
    @DisplayName("Camino 4")
    public void testCamino4() throws Exception {

        int x = Commons.BORDER_LEFT;
        int y = Commons.GROUND * Commons.ALIEN_HEIGHT + 1;

        Alien alien = new Alien(x, y);
        alien.setX(x);
        alien.setY(y);
        visibleAlien.invoke(alien,true);

        aliens.add(alien);

        board.setDirection(-1);

        metodo.invoke(board);

        assertEquals(x - 1, alien.getX());
        assertFalse(board.isInGame());
        assertEquals("Invasion!", board.getMessage());
    }

    @Test
    @DisplayName("Camino 5")
    public void testCamino5() throws Exception {

        int x = Commons.BORDER_LEFT + 3;
        int y = 100;

        Alien alien = new Alien(x, y);
        alien.setX(x);
        alien.setY(y);
        visibleAlien.invoke(alien,false);

        aliens.add(alien);

        board.setDirection(-1);

        metodo.invoke(board);

        assertEquals(1, board.getDirection());
        assertEquals(x, alien.getX());
        assertEquals(y + Commons.GO_DOWN, alien.getY());
    }

    @Test
    @DisplayName("Camino 6")
    public void testCamino6() throws Exception {

        int x = Commons.BOARD_WIDTH - Commons.BORDER_RIGHT;
        int y = 100;

        Alien alien = new Alien(x, y);
        alien.setX(x);
        alien.setY(y);
        visibleAlien.invoke(alien,false);

        aliens.add(alien);

        board.setDirection(1);

        metodo.invoke(board);

        assertEquals(1, board.getDirection());
        assertEquals(x, alien.getX());
        assertEquals(y + 2 * Commons.GO_DOWN, alien.getY());
    }

}
