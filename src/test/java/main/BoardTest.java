package main;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import space_invaders.sprites.Alien;
import space_invaders.sprites.Player;

import javax.swing.SwingUtilities;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.function.Executable;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Caja negra - Constructor Board()
 *
 * Técnica: partición en clases de equivalencia.
 * El constructor no tiene parámetros explícitos ni el objeto existe antes de llamarlo,
 * así que solo hay UNA clase de equivalencia de entrada -> un único caso de prueba (CP-B01).
 * La salida es el estado del tablero recién creado, que se comprueba con los getters públicos.
 *
 * Los valores esperados salen de la documentación del juego y de las constantes de Commons.
 */
@DisplayName("CP-B01: Constructor Board()")
class BoardTest {

    // Valores esperados según la documentación (comprobadlos con la vuestra)
    private static final int DIRECCION_INICIAL = -1;
    private static final int PLAYER_INIT_X = 179;   // revisar en la documentación de Player
    private static final int PLAYER_INIT_Y = 280;   // revisar en la documentación de Player

    private Board board;

    @BeforeEach
    void setUp() throws Exception {
        // El constructor arranca un Timer que mueve el juego cada 17 ms.
        // Se crea el Board en el hilo de Swing y se para el Timer en la misma llamada,
        // así ningún ciclo del juego puede ejecutarse antes de las comprobaciones.
        SwingUtilities.invokeAndWait(() -> {
            board = new Board();
            assertTrue(board.getTimer().isRunning(), "El timer debería arrancar al crear el Board");
            board.getTimer().stop();
        });
    }

    @Test
    @DisplayName("El juego empieza en curso, sin muertes y con la dirección inicial")
    void estadoInicialDelJuego() {
        assertAll(
                () -> assertTrue(board.isInGame(), "inGame"),
                () -> assertEquals(0, board.getDeaths(), "deaths"),
                () -> assertEquals(DIRECCION_INICIAL, board.getDirection(), "direction"),
                () -> assertEquals("Game Over", board.getMessage(), "message"),
                () -> assertEquals(Commons.BOARD_WIDTH, board.getD().width, "ancho del tablero"),
                () -> assertEquals(Commons.BOARD_HEIGHT, board.getD().height, "alto del tablero")
        );
    }

    @Test
    @DisplayName("Se crean ALIEN_ROWS x ALIEN_COLUMNS aliens")
    void numeroDeAliens() {
        assertNotNull(board.getAliens());
        assertEquals(Commons.ALIEN_ROWS * Commons.ALIEN_COLUMNS, board.getAliens().size());
    }

    @Test
    @DisplayName("Cada alien está en su posición inicial de la cuadrícula")
    void posicionesDeAliens() {
        List<Alien> aliens = board.getAliens();
        List<Executable> comprobaciones = new ArrayList<>();

        // Los aliens se crean fila a fila: índice = fila * ALIEN_COLUMNS + columna
        for (int fila = 0; fila < Commons.ALIEN_ROWS; fila++) {
            for (int col = 0; col < Commons.ALIEN_COLUMNS; col++) {
                Alien alien = aliens.get(fila * Commons.ALIEN_COLUMNS + col);
                int xEsperada = Commons.ALIEN_INIT_X + Commons.ALIEN_SEPARATOR * col;
                int yEsperada = Commons.ALIEN_INIT_Y + Commons.ALIEN_SEPARATOR * fila;
                String id = "alien[fila " + fila + ", col " + col + "]";

                comprobaciones.add(() -> assertEquals(xEsperada, alien.getX(), id + " x"));
                comprobaciones.add(() -> assertEquals(yEsperada, alien.getY(), id + " y"));
                comprobaciones.add(() -> assertTrue(alien.isVisible(), id + " visible"));
            }
        }
        // assertAll ejecuta todas las comprobaciones y muestra todos los fallos juntos
        assertAll(comprobaciones);
    }

    @Test
    @DisplayName("El jugador y el disparo existen y el jugador está en su posición inicial")
    void jugadorYDisparo() {
        Player player = board.getPlayer();
        assertAll(
                () -> assertNotNull(player, "player"),
                () -> assertEquals(PLAYER_INIT_X, player.getX(), "player x"),
                () -> assertEquals(PLAYER_INIT_Y, player.getY(), "player y"),
                () -> assertTrue(player.isVisible(), "player visible"),
                () -> assertNotNull(board.getShot(), "shot")
        );
    }
}
