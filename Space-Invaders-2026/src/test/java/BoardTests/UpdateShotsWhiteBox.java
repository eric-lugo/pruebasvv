package BoardTests;

import main.Board;
import main.Commons;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import space_invaders.sprites.Alien;
import space_invaders.sprites.Shot;

import javax.swing.*;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

public class UpdateShotsWhiteBox {

    private static final int ALIEN_X = 100;
    private static final int ALIEN_Y = 100;
    private static final int CENTRO_X = ALIEN_X + Commons.ALIEN_WIDTH / 2;    // 106
    private static final int CENTRO_Y = ALIEN_Y + Commons.ALIEN_HEIGHT / 2;   // 106
    private static final int DEATHS_INICIAL = 5;

    private Board board;

    @BeforeEach
    void setUp() throws Exception {
        SwingUtilities.invokeAndWait(() -> {
            board = new Board();
            board.getTimer().stop();
        });
        board.setDeaths(DEATHS_INICIAL);
    }

    private void ponerAliens(Alien... aliens) {
        board.setAliens(new ArrayList<>(Arrays.asList(aliens)));
    }

    private Shot colocarDisparo(int x, int y) {
        Shot shot = new Shot();
        shot.setX(x);
        shot.setY(y);
        board.setShot(shot);
        return shot;
    }

    private void ejecutarUpdateShots() throws Exception {
        Method m = Board.class.getDeclaredMethod("update_shots");
        m.setAccessible(true);
        try {
            m.invoke(board);
        } catch (InvocationTargetException e) {
            fail("update_shots() lanzó una excepción: " + e.getCause());
        }
    }

    @Test
    @DisplayName("C1: [1,3,12] -> shot no visible")

    void c1_shotNoVisible() throws Exception{
        Alien alien = new Alien(ALIEN_X,ALIEN_Y);
        ponerAliens(alien);
        Shot shot = colocarDisparo(CENTRO_X,CENTRO_Y);
        shot.die();
        assertFalse(shot.isVisible(), "precondición: shot no visible");
        ejecutarUpdateShots();

        assertAll(
                () -> assertFalse(alien.isDying(), "el alien no debe morir"),
                () -> assertEquals(DEATHS_INICIAL,board.getDeaths(),"Deaths no debe cambiar")
        );
    }

    @Test
    @DisplayName("C2: [1,2,5,8,9,11,12]  shot visible, no aliens -> shot sube")

    void c2_sinAlienShotSube() throws Exception{
        ponerAliens(); //Aliens a 0
        Shot shot = colocarDisparo(50,200);
        assertTrue(shot.isVisible(), "precondición: shot visible");
        ejecutarUpdateShots();
        assertAll(
                () -> assertEquals(200 - Commons.SHOT_SPEED, shot.getY(), "el disparo debe subir SHOT_SPEED"),
                () -> assertTrue(shot.isVisible(), "el shot debe seguir visible"),
                () -> assertEquals(DEATHS_INICIAL, board.getDeaths(), "deaths no debe cambiar")
        );
    }

    @Test
    @DisplayName("C3: [1,2,5,8,10,11,12] shot visible, no aliens, sale por arriba -> shot desaparece")

    void c3_sinAlienShotSalePorArriba() throws Exception{
        ponerAliens();
        Shot shot = colocarDisparo(50,0);
        assertTrue(shot.isVisible(), "precondición: shot visible");
        ejecutarUpdateShots();
        assertAll(
                () -> assertFalse(shot.isVisible(), "el shot debe desaparecer al salir por arriba"),
                () -> assertEquals(DEATHS_INICIAL, board.getDeaths(), "deaths no debe cambiar")
        );
    }

    @Test
    @DisplayName("C4: [1,2,4,2,5,8,9,11,12] shot visible, hay aliens, alien no visible -> sin colisión, shot sube ")

    void c4_alienNoVisible() throws Exception{
        Alien alien = new Alien(ALIEN_X, ALIEN_Y);
        alien.die();
        ponerAliens(alien);
        Shot shot = colocarDisparo(CENTRO_X, CENTRO_Y);
        assertFalse(alien.isVisible(), "precondición: alien no visible");
        assertTrue(shot.isVisible(), "precondición: shot visible");
        ejecutarUpdateShots();
        assertAll(
                () -> assertEquals(CENTRO_Y - Commons.SHOT_SPEED, shot.getY(), "el disparo debe subir SHOT_SPEED"),
                () -> assertTrue(shot.isVisible(), "el shot debe seguir visible"),
                () -> assertEquals(DEATHS_INICIAL, board.getDeaths(), "deaths no debe cambiar")
        );
    }

    @Test
    @DisplayName("C5: [1,2,4,6,2,5,8,10,11,12] shot visible, hay aliens, alien visible, sin colisión, sale por arriba -> shot desaparece")

    void c5_sinColisionSalePorArriba() throws Exception{
        Alien alien = new Alien(ALIEN_X, ALIEN_Y);
        ponerAliens(alien);
        Shot shot = colocarDisparo(50, 0);
        assertTrue(alien.isVisible(), "precondición: alien visible");
        assertTrue(shot.isVisible(), "precondición: shot visible");

        ejecutarUpdateShots();

        assertAll(
                () -> assertFalse(shot.isVisible(), "el shot debe desaparecer al salir por arriba"),
                () -> assertFalse(alien.isDying(), "el alien no debe destruirse"),
                () -> assertEquals(DEATHS_INICIAL, board.getDeaths(), "deaths no debe cambiar")
        );
    }

    @Test
    @DisplayName("C6: [1,2,4,6,7,2,5,8,9,11,12] colisión -> alien destruido, deaths + 1")

    void c6_colision() throws Exception{
        Alien alien = new Alien(ALIEN_X, ALIEN_Y);
        ponerAliens(alien);
        Shot shot = colocarDisparo(CENTRO_X, CENTRO_Y);
        assertTrue(alien.isVisible(), "precondición: alien visible");
        assertTrue(shot.isVisible(), "precondición: disparo visible");
        ejecutarUpdateShots();
        assertAll(
                () -> assertTrue(alien.isDying(), "el alien debe quedar destruido"),
                () -> assertEquals(DEATHS_INICIAL + 1, board.getDeaths(), "deaths debe aumentar en 1"),
                () -> assertFalse(shot.isVisible(), "el disparo debe desaparecer tras el impacto")
        );
    }




}
