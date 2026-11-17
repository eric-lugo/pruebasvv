package main.BoardTests;

import main.Board;
import main.Commons;
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

class BoardUpdateTest {

    private static final String MENSAJE_INICIAL = "Match ON";
    private static final String MENSAJE_VICTORIA = "Game won!";

    private Board board;

    @BeforeEach
    void setUp() throws Exception {
        SwingUtilities.invokeAndWait(() -> {
            board = new Board();
            board.getTimer().stop();
        });
    }

    private void prepararEstado(int deaths) {
        List<Alien> aliens = new ArrayList<>();
        aliens.add(new Alien(150, 50));
        board.setAliens(aliens);

        Shot shot = new Shot();
        shot.die();
        board.setShot(shot);

        board.setInGame(true);
        board.setMessage(MENSAJE_INICIAL);
        board.setDeaths(deaths);
    }

    /* update_aliens() es privado --> se invoca por reflexión. */
    private void ejecutarUpdate() throws Exception {
        Method update = Board.class.getDeclaredMethod("update");
        update.setAccessible(true);
        try {
            update.invoke(board);
        } catch (InvocationTargetException e) {
            fail("update() lanzó una excepción: " + e.getCause());
        }
    }

    private void comprobarJuegoSigue() {
        assertAll(
                () -> assertTrue(board.isInGame(), "inGame debe seguir a true"),
                () -> assertEquals(MENSAJE_INICIAL, board.getMessage(), "el mensaje no debe cambiar")
        );
    }

    private void comprobarVictoria() {
        assertAll(
                () -> assertFalse(board.isInGame(), "inGame debe pasar a false"),
                () -> assertEquals(MENSAJE_VICTORIA, board.getMessage(), "mensaje de victoria")
        );
    }

    @Test
    @DisplayName("CP-UP01: deaths = -1 (mín-1, no válido) -> el juego sigue")
    void cpUp01_deathsMinMenos1() throws Exception {
        prepararEstado(-1);
        ejecutarUpdate();
        comprobarJuegoSigue();
    }

    @Test
    @DisplayName("CP-UP02: deaths = 0 (mín) -> el juego sigue")
    void cpUp02_deathsMin() throws Exception {
        prepararEstado(0);
        ejecutarUpdate();
        comprobarJuegoSigue();
    }

    @Test
    @DisplayName("CP-UP03: deaths = 1 (mín+1) -> el juego sigue")
    void cpUp03_deathsMinMas1() throws Exception {
        prepararEstado(1);
        ejecutarUpdate();
        comprobarJuegoSigue();
    }

    @Test
    @DisplayName("CP-UP04: deaths = 12 (nominal) -> el juego sigue")
    void cpUp04_deathsNominal() throws Exception {
        prepararEstado(12);
        ejecutarUpdate();
        comprobarJuegoSigue();
    }

    @Test
    @DisplayName("CP-UP05: deaths = 23 (máx-1) -> el juego sigue")
    void cpUp05_deathsMaxMenos1() throws Exception {
        prepararEstado(Commons.NUMBER_OF_ALIENS_TO_DESTROY - 1);
        ejecutarUpdate();
        comprobarJuegoSigue();
    }

    @Test
    @DisplayName("CP-UP06: deaths = 24 (máx) -> victoria, 'Game won!'")
    void cpUp06_deathsMax() throws Exception {
        prepararEstado(Commons.NUMBER_OF_ALIENS_TO_DESTROY);
        ejecutarUpdate();
        comprobarVictoria();
    }

    @Test
    @DisplayName("CP-UP07: deaths = 25 (máx+1, no válido) -> victoria, 'Game won!'")
    void cpUp07_deathsMaxMas1() throws Exception {
        prepararEstado(Commons.NUMBER_OF_ALIENS_TO_DESTROY + 1);
        ejecutarUpdate();
        comprobarVictoria();
    }

    /* ---------- Postcondición: coordinación de los métodos de actualización ---------- No si hara falta ahora, son pruebas baratas

    @Test
    @DisplayName("CP-UP08: update() ejecuta update_shots() -> el disparo sube")
    void cpUp08_ejecutaUpdateShots() throws Exception {
        prepararEstado(12);
        Shot shot = new Shot(50, 200);
        board.setShot(shot);
        int yAntes = shot.getY();

        ejecutarUpdate();

        assertTrue(shot.getY() < yAntes, "el disparo debería haber subido");
    }

    @Test
    @DisplayName("CP-UP09: update() ejecuta update_bomb() -> la bomba baja")
    void cpUp09_ejecutaUpdateBomb() throws Exception {
        prepararEstado(12);
        Alien alien = board.getAliens().get(0);
        Alien.Bomb bomba = alien.getBomb();
        bomba.setDestroyed(false);           // bomba activa
        bomba.setX(150);
        bomba.setY(100);                     // lejos del jugador y del suelo
        int yAntes = bomba.getY();

        ejecutarUpdate();

        assertTrue(bomba.getY() > yAntes, "la bomba debería haber bajado");
    }

    @Test
    @DisplayName("CP-UP10: update() ejecuta update_aliens() -> el alien se desplaza")
    void cpUp10_ejecutaUpdateAliens() throws Exception {
        prepararEstado(12);
        board.setDirection(1);               // hacia la derecha
        Alien alien = board.getAliens().get(0);
        int xAntes = alien.getX();

        ejecutarUpdate();

        assertTrue(alien.getX() > xAntes, "el alien debería haberse movido a la derecha");
    }
     */
}
