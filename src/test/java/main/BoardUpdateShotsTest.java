package main;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import space_invaders.sprites.Alien;
import space_invaders.sprites.Shot;

import javax.swing.SwingUtilities;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;


class BoardUpdateShotsTest {

    private static final int ALIEN_X = 100;
    private static final int ALIEN_Y = 100;
    // Punto central del alien: dentro de su rectángulo (ALIEN_WIDTH x ALIEN_HEIGHT)
    private static final int CENTRO_X = ALIEN_X + Commons.ALIEN_WIDTH / 2;
    private static final int CENTRO_Y = ALIEN_Y + Commons.ALIEN_HEIGHT / 2;
    private static final int DEATHS_INICIAL = 5;

    private Board board;
    private Alien alien;

    @BeforeEach
    void setUp() throws Exception {
        SwingUtilities.invokeAndWait(() -> {
            board = new Board();
            board.getTimer().stop();
        });

        alien = new Alien(ALIEN_X, ALIEN_Y);
        List<Alien> aliens = new ArrayList<>();
        aliens.add(alien);
        board.setAliens(aliens);
        board.setDeaths(DEATHS_INICIAL);
    }

    private Shot colocarDisparo(int x, int y) {
        Shot shot = new Shot();
        shot.setX(x);
        shot.setY(y);
        board.setShot(shot);
        return shot;
    }

    /* update_shots() es privado --> se invoca por reflexión */
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
    @DisplayName("CP-US01 [R1]: disparo no visible -> ninguna acción")
    void cpUs01_disparoNoVisible() throws Exception {
        Shot shot = colocarDisparo(CENTRO_X, CENTRO_Y);
        shot.die();
        assertFalse(shot.isVisible(), "precondición: el disparo debe estar no visible");

        ejecutarUpdateShots();

        assertAll(
                () -> assertEquals(CENTRO_Y, shot.getY(), "el disparo no debe moverse"),
                () -> assertFalse(alien.isDying(), "el alien no debe destruirse"),
                () -> assertEquals(DEATHS_INICIAL, board.getDeaths(), "deaths no debe cambiar")
        );
    }

    @Test
    @DisplayName("CP-US02 [R2]: disparo visible impacta en alien visible -> alien destruido, deaths+1, disparo desaparece")
    void cpUs02_impactoEnAlien() throws Exception {
        Shot shot = colocarDisparo(CENTRO_X, CENTRO_Y);
        assertTrue(shot.isVisible(), "precondición: el disparo debe estar visible");
        assertTrue(alien.isVisible(), "precondición: el alien debe estar visible");

        ejecutarUpdateShots();

        assertAll(
                () -> assertTrue(alien.isDying(), "el alien debe quedar destruido"),
                () -> assertEquals(DEATHS_INICIAL + 1, board.getDeaths(), "deaths debe aumentar en 1"),
                () -> assertFalse(shot.isVisible(), "el disparo debe desaparecer tras el impacto")
        );
    }

    @Test
    @DisplayName("CP-US03 [R3]: disparo visible sin colisión -> el disparo sube")
    void cpUs03_sinColision() throws Exception {
        int yInicial = 200;
        Shot shot = colocarDisparo(50, yInicial);     // lejos del alien
        assertTrue(shot.isVisible(), "precondición: el disparo debe estar visible");

        ejecutarUpdateShots();

        assertAll(
                () -> assertEquals(yInicial - Commons.SHOT_SPEED, shot.getY(), "el disparo debe subir SHOT_SPEED"),
                () -> assertTrue(shot.isVisible(), "el disparo debe seguir visible"),
                () -> assertFalse(alien.isDying(), "el alien no debe destruirse"),
                () -> assertEquals(DEATHS_INICIAL, board.getDeaths(), "deaths no debe cambiar")
        );
    }

    @Test
    @DisplayName("CP-US04 [R4]: disparo sobre alien no visible -> sin colisión, el disparo sube")
    void cpUs04_alienNoVisible() throws Exception {
        alien.die();
        Shot shot = colocarDisparo(CENTRO_X, CENTRO_Y);
        assertFalse(alien.isVisible(), "precondición: el alien debe estar no visible");
        assertTrue(shot.isVisible(), "precondición: el disparo debe estar visible");

        ejecutarUpdateShots();

        assertAll(
                () -> assertEquals(CENTRO_Y - Commons.SHOT_SPEED, shot.getY(), "el disparo debe subir SHOT_SPEED"),
                () -> assertTrue(shot.isVisible(), "el disparo debe seguir visible"),
                () -> assertEquals(DEATHS_INICIAL, board.getDeaths(), "deaths no debe cambiar")
        );
    }

    @Test
    @DisplayName("CP-US05 [R5]: disparo visible sale por arriba -> el disparo desaparece")
    void cpUs05_saleporArriba() throws Exception {
        Shot shot = colocarDisparo(50, Commons.SHOT_SPEED - 1);   // tras moverse queda en y < 0
        assertTrue(shot.isVisible(), "precondición: el disparo debe estar visible");

        ejecutarUpdateShots();

        assertAll(
                () -> assertFalse(shot.isVisible(), "el disparo debe desaparecer"),
                () -> assertEquals(DEATHS_INICIAL, board.getDeaths(), "deaths no debe cambiar")
        );
    }
}
