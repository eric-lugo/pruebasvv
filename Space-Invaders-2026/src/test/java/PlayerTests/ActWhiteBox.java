package PlayerTests;

import main.Commons;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import space_invaders.sprites.Player;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class ActWhiteBox {

    private static final int X_MINIMA = 2;
    private static final int X_MAXIMA = Commons.BOARD_WIDTH - 2 * Commons.PLAYER_WIDTH;

    private Player player;
    private int yInicial;

    @BeforeEach
    void setUp() {
        player = new Player();
        yInicial = player.getY();
    }

    @Test
    @DisplayName("C1: [1,2,5] x en el interior de los limites -> x no cambia")

    void c1_interior(){
        player.setX(100);
        player.act();
        assertAll(
                () -> assertEquals(100, player.getX(), "la x no debe cambiar"),
                () -> assertEquals(yInicial, player.getY(), "la y no debe cambiar")
        );
    }

    @Test
    @DisplayName("C2: [1,2,3,5] x<= 2 -> x = 2")

    void c2_resetLimiteIzquierdo(){
        player.setX(1);
        player.act();
        assertAll(
                () -> assertEquals(X_MINIMA, player.getX(), "el jugador debe quedar en el borde izquierdo"),
                () -> assertEquals(yInicial, player.getY(), "la y no debe cambiar")
        );
    }

    @Test
    @DisplayName("C3: [1,3,4,5] x>= 10740 -> 328")

    void c3_resetLimiteDerecho(){
        int xForzada = Commons.BOARD_WIDTH * 2 * Commons.PLAYER_WIDTH;   // 10740
        player.setX(xForzada);
        player.act();
        assertAll(
                () -> assertEquals(X_MAXIMA, player.getX(), "el jugador debe quedar en el borde derecho"),
                () -> assertEquals(yInicial, player.getY(), "la y no debe cambiar")
        );
    }
}
