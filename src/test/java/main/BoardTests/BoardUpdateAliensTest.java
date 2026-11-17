package main.BoardTests;

import main.Board;
import main.Commons;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import space_invaders.sprites.Alien;

import javax.swing.SwingUtilities;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

class BoardUpdateAliensTest {

    private static final int BORDE_IZQ = Commons.BORDER_LEFT;                          // 5
    private static final int BORDE_DER = Commons.BOARD_WIDTH - Commons.BORDER_RIGHT;   // 328
    private static final int LIMITE_SUELO = Commons.GROUND + Commons.ALIEN_HEIGHT;     // 302
    private static final int X_CENTRO = 150;
    private static final int Y_NORMAL = 100;          // lejos del suelo
    private static final String MENSAJE_INICIAL = "mensaje previo";

    private Board board;

    @BeforeEach
    void setUp() throws Exception {
        SwingUtilities.invokeAndWait(() -> {
            board = new Board();
            board.getTimer().stop();
        });
        board.setInGame(true);
        board.setMessage(MENSAJE_INICIAL);
    }

    private void ponerAliens(Alien... aliens) {
        board.setAliens(new ArrayList<>(Arrays.asList(aliens)));
    }

    /* update_aliens() es privado --> se invoca por reflexión. */
    private void ejecutarUpdateAliens() throws Exception {
        Method m = Board.class.getDeclaredMethod("update_aliens");
        m.setAccessible(true);
        try {
            m.invoke(board);
        } catch (InvocationTargetException e) {
            fail("update_aliens() lanzó una excepción: " + e.getCause());
        }
    }

    @Test
    @DisplayName("CP-UA01 [R1]: alien en el interior, dirección derecha -> se desplaza a la derecha")
    void cpUa01_interiorDerecha() throws Exception {
        Alien alien = new Alien(X_CENTRO, Y_NORMAL);
        ponerAliens(alien);
        board.setDirection(1);
        assertTrue(alien.isVisible(), "precondición: el alien debe estar visible");

        ejecutarUpdateAliens();

        assertAll(
                () -> assertTrue(alien.getX() > X_CENTRO, "A1: la x debe aumentar"),
                () -> assertEquals(1, board.getDirection(), "la dirección no debe cambiar"),
                () -> assertEquals(Y_NORMAL, alien.getY(), "el alien no debe bajar"),
                () -> assertTrue(board.isInGame(), "el juego debe seguir")
        );
    }

    @Test
    @DisplayName("CP-UA02 [R2]: alien en el interior, dirección izquierda -> se desplaza a la izquierda")
    void cpUa02_interiorIzquierda() throws Exception {
        Alien alien = new Alien(X_CENTRO, Y_NORMAL);
        ponerAliens(alien);
        board.setDirection(-1);
        assertTrue(alien.isVisible(), "precondición: el alien debe estar visible");

        ejecutarUpdateAliens();

        assertAll(
                () -> assertTrue(alien.getX() < X_CENTRO, "A1: la x debe disminuir"),
                () -> assertEquals(-1, board.getDirection(), "la dirección no debe cambiar"),
                () -> assertEquals(Y_NORMAL, alien.getY(), "el alien no debe bajar"),
                () -> assertTrue(board.isInGame(), "el juego debe seguir")
        );
    }

    @Test
    @DisplayName("CP-UA03 [R3]: alien alcanza el borde derecho yendo a la derecha -> rebota y todos bajan")
    void cpUa03_reboteDerecho() throws Exception {
        Alien enBorde = new Alien(BORDE_DER, Y_NORMAL);
        Alien enCentro = new Alien(X_CENTRO, Y_NORMAL);
        ponerAliens(enBorde, enCentro);
        board.setDirection(1);
        assertTrue(enBorde.isVisible(), "precondición: el alien del borde debe estar visible");
        assertTrue(enCentro.isVisible(), "precondición: el alien del centro debe estar visible");

        ejecutarUpdateAliens();

        assertAll(
                () -> assertEquals(-1, board.getDirection(), "A2: la dirección debe invertirse"),
                () -> assertEquals(Y_NORMAL + Commons.GO_DOWN, enBorde.getY(), "A3: el alien del borde debe bajar"),
                () -> assertEquals(Y_NORMAL + Commons.GO_DOWN, enCentro.getY(), "A3: el alien del centro también debe bajar")
        );
    }

    @Test
    @DisplayName("CP-UA04 [R4]: alien alcanza el borde izquierdo yendo a la izquierda -> rebota y todos bajan")
    void cpUa04_reboteIzquierdo() throws Exception {
        Alien enBorde = new Alien(BORDE_IZQ, Y_NORMAL);
        Alien enCentro = new Alien(X_CENTRO, Y_NORMAL);
        ponerAliens(enBorde, enCentro);
        board.setDirection(-1);
        assertTrue(enBorde.isVisible(), "precondición: el alien del borde debe estar visible");
        assertTrue(enCentro.isVisible(), "precondición: el alien del centro debe estar visible");

        ejecutarUpdateAliens();

        // A1 (desplazamiento en X) no se evalúa: la especificación no define el orden
        assertAll(
                () -> assertEquals(1, board.getDirection(), "A2: la dirección debe invertirse"),
                () -> assertEquals(Y_NORMAL + Commons.GO_DOWN, enBorde.getY(), "A3: el alien del borde debe bajar"),
                () -> assertEquals(Y_NORMAL + Commons.GO_DOWN, enCentro.getY(), "A3: el alien del centro también debe bajar")
        );
    }

    @Test
    @DisplayName("CP-UA05 [R5]: alien en el borde derecho yendo a la izquierda -> se desplaza sin rebotar")
    void cpUa05_bordeDerechoAlejandose() throws Exception {
        Alien alien = new Alien(BORDE_DER, Y_NORMAL);
        ponerAliens(alien);
        board.setDirection(-1);
        assertTrue(alien.isVisible(), "precondición: el alien debe estar visible");

        ejecutarUpdateAliens();

        assertAll(
                () -> assertTrue(alien.getX() < BORDE_DER, "A1: la x debe disminuir"),
                () -> assertEquals(-1, board.getDirection(), "la dirección no debe invertirse"),
                () -> assertEquals(Y_NORMAL, alien.getY(), "el alien no debe bajar")
        );
    }

    @Test
    @DisplayName("CP-UA06 [R6]: alien en el borde izquierdo yendo a la derecha -> se desplaza sin rebotar")
    void cpUa06_bordeIzquierdoAlejandose() throws Exception {
        Alien alien = new Alien(BORDE_IZQ, Y_NORMAL);
        ponerAliens(alien);
        board.setDirection(1);
        assertTrue(alien.isVisible(), "precondición: el alien debe estar visible");

        ejecutarUpdateAliens();

        assertAll(
                () -> assertTrue(alien.getX() > BORDE_IZQ, "A1: la x debe aumentar"),
                () -> assertEquals(1, board.getDirection(), "la dirección no debe invertirse"),
                () -> assertEquals(Y_NORMAL, alien.getY(), "el alien no debe bajar")
        );
    }

    @Test
    @DisplayName("CP-UA07 [R7]: alien supera el límite inferior -> fin del juego con 'Invasion!'")
    void cpUa07_invasion() throws Exception {
        Alien alien = new Alien(X_CENTRO, LIMITE_SUELO + 1);
        ponerAliens(alien);
        board.setDirection(1);
        assertTrue(alien.isVisible(), "precondición: el alien debe estar visible");

        ejecutarUpdateAliens();

        assertAll(
                () -> assertFalse(board.isInGame(), "A4: el juego debe terminar"),
                () -> assertEquals("Invasion!", board.getMessage(), "A4: mensaje de invasión")
        );
    }

    @Test
    @DisplayName("CP-UA08 [R8]: alien no visible -> no se desplaza")
    void cpUa08_alienNoVisible() throws Exception {
        Alien alien = new Alien(X_CENTRO, Y_NORMAL);
        alien.die();
        ponerAliens(alien);
        board.setDirection(1);
        assertFalse(alien.isVisible(), "precondición: el alien debe estar no visible");

        ejecutarUpdateAliens();

        assertEquals(X_CENTRO, alien.getX(), "un alien no visible no debe desplazarse");
    }
}
