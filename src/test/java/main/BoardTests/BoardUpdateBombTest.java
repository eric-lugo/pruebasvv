package main.BoardTests;

import main.Board;
import main.Commons;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import space_invaders.sprites.Alien;
import space_invaders.sprites.Player;

import javax.swing.SwingUtilities;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;


class BoardUpdateBombTest {

    private static final int ALIEN_X = 150;
    private static final int ALIEN_Y = 50;
    private static final int PLAYER_X = 250;                       // lejos de la columna x = 50
    private static final int LIMITE_SUELO = Commons.GROUND - Commons.BOMB_HEIGHT;
    private static final int REPETICIONES = 500;

    private Board board;
    private Player player;

    @BeforeEach
    void setUp() throws Exception {
        SwingUtilities.invokeAndWait(() -> {
            board = new Board();
            board.getTimer().stop();
        });
        player = board.getPlayer();
        player.setX(PLAYER_X);
    }

    private void ponerAliens(Alien... aliens) {
        board.setAliens(new ArrayList<>(Arrays.asList(aliens)));
    }

    private Alien.Bomb activarBomba(Alien alien, int x, int y) {
        Alien.Bomb bomba = alien.getBomb();
        bomba.setDestroyed(false);
        bomba.setX(x);
        bomba.setY(y);
        return bomba;
    }


    private int centroJugadorX() { return player.getX() + Commons.PLAYER_WIDTH / 2; }
    private int centroJugadorY() { return player.getY() + Commons.PLAYER_HEIGHT / 2; }

    /* update_bomb() es privado --> se invoca por reflexión. */
    private void ejecutarUpdateBomb() throws Exception {
        Method m = Board.class.getDeclaredMethod("update_bomb");
        m.setAccessible(true);
        try {
            m.invoke(board);
        } catch (InvocationTargetException e) {
            fail("update_bomb() lanzó una excepción: " + e.getCause());
        }
    }

    @Test
    @DisplayName("CP-UB01 [R1+R2]: alien visible sin bomba activa -> a veces se crea y a veces no")
    void cpUb01_creacionAleatoria() throws Exception {
        Alien alien = new Alien(ALIEN_X, ALIEN_Y);
        ponerAliens(alien);
        Alien.Bomb bomba = alien.getBomb();
        assertTrue(alien.isVisible(), "precondición: el alien debe estar visible");

        int creadas = 0, noCreadas = 0;
        for (int i = 0; i < REPETICIONES; i++) {
            bomba.setDestroyed(true);                 // sin bomba activa antes de cada tirada
            ejecutarUpdateBomb();
            if (!alien.getBomb().isDestroyed()) creadas++; else noCreadas++;
        }

        int c = creadas, n = noCreadas;
        assertAll(
                () -> assertTrue(c > 0, "R1: debería haberse creado alguna bomba en " + REPETICIONES + " ciclos"),
                () -> assertTrue(n > 0, "R2: alguna vez no debería haberse creado la bomba")
        );
    }


    @Test
    @DisplayName("CP-UB02 [R3]: alien no visible sin bomba activa -> nunca se crea una bomba")
    void cpUb02_alienNoVisibleNoCrea() throws Exception {
        Alien alien = new Alien(ALIEN_X, ALIEN_Y);
        alien.die();
        ponerAliens(alien);
        Alien.Bomb bomba = alien.getBomb();
        bomba.setDestroyed(true);
        assertFalse(alien.isVisible(), "precondición: el alien debe estar no visible");

        for (int i = 1; i <= REPETICIONES; i++) {
            ejecutarUpdateBomb();
            int ciclo = i;
            assertAll("ciclo " + ciclo,
                    () -> assertSame(bomba, alien.getBomb(), "no debe sustituirse la bomba"),
                    () -> assertTrue(bomba.isDestroyed(), "un alien no visible no debe crear bombas")
            );
        }
    }

    @Test
    @DisplayName("CP-UB03 [R4]: bomba activa lejos del jugador y del suelo -> desciende BOMB_SPEED")
    void cpUb03_bombaDesciende() throws Exception {
        Alien alien = new Alien(ALIEN_X, ALIEN_Y);
        alien.die();                                  // elimina el azar
        ponerAliens(alien);
        Alien.Bomb bomba = activarBomba(alien, ALIEN_X, 100);
        assertFalse(bomba.isDestroyed(), "precondición: la bomba debe estar activa");

        ejecutarUpdateBomb();

        assertAll(
                () -> assertEquals(100 + Commons.BOMB_SPEED, bomba.getY(), "A2: debe descender BOMB_SPEED"),
                () -> assertFalse(bomba.isDestroyed(), "la bomba debe seguir activa"),
                () -> assertFalse(player.isDying(), "el jugador no debe destruirse")
        );
    }

    @Test
    @DisplayName("CP-UB04 [R5]: bomba activa dentro del jugador visible -> jugador destruido")
    void cpUb04_impactoJugador() throws Exception {
        Alien alien = new Alien(ALIEN_X, ALIEN_Y);
        alien.die();
        ponerAliens(alien);
        Alien.Bomb bomba = activarBomba(alien, centroJugadorX(), centroJugadorY());
        assertTrue(player.isVisible(), "precondición: el jugador debe estar visible");
        assertFalse(player.isDying(), "precondición: el jugador no debe estar destruido");

        ejecutarUpdateBomb();

        assertAll(
                () -> assertTrue(player.isDying(), "A3: el jugador debe quedar destruido"),
                () -> assertTrue(bomba.isDestroyed(), "(dominio) la bomba debe destruirse tras el impacto") //No se expresa formalmente en el documento que se destruye la bomba al impactar contra un jugador
        );
    }

    @Test
    @DisplayName("CP-UB05 [R6]: bomba activa sobre jugador no visible -> sin colisión, desciende")
    void cpUb05_jugadorNoVisible() throws Exception {
        Alien alien = new Alien(ALIEN_X, ALIEN_Y);
        alien.die();
        ponerAliens(alien);
        player.die();
        int yInicial = centroJugadorY();
        Alien.Bomb bomba = activarBomba(alien, centroJugadorX(), yInicial);
        assertFalse(player.isVisible(), "precondición: el jugador debe estar no visible");

        ejecutarUpdateBomb();

        assertAll(
                () -> assertFalse(player.isDying(), "un jugador no visible no debe destruirse"),
                () -> assertEquals(yInicial + Commons.BOMB_SPEED, bomba.getY(), "A2: la bomba debe descender"),
                () -> assertFalse(bomba.isDestroyed(), "la bomba debe seguir activa")
        );
    }

    @Test
    @DisplayName("CP-UB06 [R7]: bomba activa alcanza el suelo -> bomba destruida")
    void cpUb06_alcanzaSuelo() throws Exception {
        Alien alien = new Alien(ALIEN_X, ALIEN_Y);
        alien.die();
        ponerAliens(alien);
        Alien.Bomb bomba = activarBomba(alien, 50, LIMITE_SUELO);   // lejos del jugador
        assertFalse(bomba.isDestroyed(), "precondición: la bomba debe estar activa");

        ejecutarUpdateBomb();

        assertAll(
                () -> assertTrue(bomba.isDestroyed(), "A4: la bomba debe destruirse al alcanzar el suelo"),
                () -> assertFalse(player.isDying(), "el jugador no debe destruirse")
        );
    }

    // ---------- R8: con bomba activa no se crea otra ----------

    @Test
    @DisplayName("CP-UB07 [R8]: alien visible con bomba activa -> no se crea una bomba nueva")
    void cpUb07_noSeCreaConBombaActiva() throws Exception {
        Alien alien = new Alien(ALIEN_X, ALIEN_Y);
        ponerAliens(alien);
        Alien.Bomb bomba = activarBomba(alien, 50, 100);            // posición distinta a la del alien
        assertTrue(alien.isVisible(), "precondición: el alien debe estar visible");
        assertFalse(bomba.isDestroyed(), "precondición: la bomba debe estar activa");

        // Ciclos posibles sin que la bomba llegue al suelo
        int ciclos = Math.min(50, (LIMITE_SUELO - 100) / Commons.BOMB_SPEED - 1);
        assertTrue(ciclos > 0, "precondición: debe haber al menos un ciclo");

        int yEsperada = 100;
        for (int i = 1; i <= ciclos; i++) {
            ejecutarUpdateBomb();
            yEsperada += Commons.BOMB_SPEED;

            int ciclo = i;
            int yCiclo = yEsperada;
            assertAll("ciclo " + ciclo,
                    () -> assertSame(bomba, alien.getBomb(), "no debe sustituirse por una bomba nueva"),
                    () -> assertFalse(bomba.isDestroyed(), "la bomba debe seguir activa"),
                    () -> assertEquals(50, bomba.getX(), "no debe saltar a la x del alien"),
                    () -> assertEquals(yCiclo, bomba.getY(), "debe seguir cayendo, no reiniciarse")
            );
        }
    }
}