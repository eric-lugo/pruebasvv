package BoardTests;

import main.Board;
import main.Commons;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import space_invaders.sprites.Alien;
import space_invaders.sprites.Player;

import javax.swing.*;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

public class UpdateBombWhiteBox {

    private static final int ALIEN_X = 100;
    private static final int ALIEN_Y = 100;
    private static final int PLAYER_X = 179;
    private static final int PLAYER_Y = 280;

    private Board board;

    @BeforeEach
    void setUp() throws Exception {
        SwingUtilities.invokeAndWait(() -> {
            board = new Board();
            board.getTimer().stop();
        });
    }

    private void ponerAliens(Alien... aliens) {
        board.setAliens(new ArrayList<>(Arrays.asList(aliens)));
    }

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
    @DisplayName("C1: [1,2,14] -> sin aliens en el tablero")
    void c1_sinAliens() throws Exception {
        ponerAliens();

        ejecutarUpdateBomb();

        assertTrue(board.getAliens().isEmpty(), "la lista de aliens debe seguir vacía");
    }

    @Test
    @DisplayName("C2: [1,2,3,4,6,7,10,2,F] -> alien no visible, bomba destruida (no dispara ni se mueve)")
    void c2_bombaDestruida() throws Exception {
        Alien alien = new Alien(ALIEN_X, ALIEN_Y);
        alien.die();
        Alien.Bomb bomb = alien.getBomb();
        bomb.setDestroyed(true);
        ponerAliens(alien);

        assertFalse(alien.isVisible(), "el alien debería no estar visible");
        assertTrue(bomb.isDestroyed(), "la bomba debería estar destruida");

        ejecutarUpdateBomb();

        assertTrue(bomb.isDestroyed(), "la bomba debe seguir destruida");
    }

    @Test
    @DisplayName("C3: [1,2,3,4,5,6,7,10,2,F] -> alien dispara bomba nueva (rand == CHANCE)")
    void c3_alienDisparaBomba() throws Exception {
        boolean disparada = false;
        Alien alien = null;
        Alien.Bomb bomb = null;

        for (int intento = 0; intento < 100 && !disparada; intento++) {
            alien = new Alien(ALIEN_X, ALIEN_Y);
            bomb = alien.getBomb();
            bomb.setDestroyed(true);
            board.getPlayer().die();
            ponerAliens(alien);

            ejecutarUpdateBomb();

            if (!bomb.isDestroyed()) {
                disparada = true;
            }
        }

        assertTrue(disparada, "el alien debe haber disparado la bomba cuando rand == Commons.CHANCE");
        final Alien.Bomb bombF = bomb;
        assertAll(
                () -> assertFalse(bombF.isDestroyed(), "la bomba debe quedar activa"),
                () -> assertEquals(ALIEN_X, bombF.getX(), "la bomba debe iniciar en las coordenadas del alien")
        );
    }

    @Test
    @DisplayName("C4: [1,2,3,4,6,7,8,10,2,F] -> bomba activa pero no hay colisión con el jugador")
    void c4_bombaActivaSinColision() throws Exception {
        Alien alien = new Alien(ALIEN_X, ALIEN_Y);
        Alien.Bomb bomb = alien.getBomb();
        bomb.setDestroyed(false);
        bomb.setX(100);
        bomb.setY(100);
        ponerAliens(alien);

        Player player = board.getPlayer();
        player.setX(PLAYER_X);
        player.setY(PLAYER_Y);

        ejecutarUpdateBomb();

        assertAll(
                () -> assertFalse(bomb.isDestroyed(), "la bomba debe seguir activa al no tocar al jugador ni el suelo"),
                () -> assertFalse(player.isDying(), "el jugador no debe morir")
        );
    }

}

