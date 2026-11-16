package PlayerTests;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import space_invaders.sprites.Player;

import java.lang.reflect.InvocationTargetException;
import java.net.URL;
import java.net.URLClassLoader;

import static org.junit.jupiter.api.Assertions.*;

public class InitPlayerWhiteBox {

    private static final int START_X = 179;
    private static final int START_Y = 280;

    Player player;

    @BeforeEach
    void setUp() {
        player = null;
    }

    @Test
    @DisplayName("C2: [1,2,4,5] imageUrl != null -> jugador inicializado en (179, 280)")
    void c2_initPlayerNormal() {
        player = new Player();

        assertAll(
                () -> assertEquals(START_X, player.getX(), "la x inicial debe ser 179"),
                () -> assertEquals(START_Y, player.getY(), "la y inicial debe ser 280"),
                () -> assertNotNull(player.getImage(), "la imagen debe haberse cargado")
        );
    }
}
