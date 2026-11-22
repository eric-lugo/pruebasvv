package main.AlienTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import space_invaders.sprites.Alien;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import static org.junit.jupiter.api.Assertions.*;

class InitBombTest {

    private static final int X_NOMINAL = 179;
    private static final int Y_NOMINAL = 175;

    private Alien.Bomb bomba;

    @BeforeEach
    void setUp() {
        Alien alien = new Alien(150, 50);
        bomba = alien.new Bomb(0, 0);
    }

    /* initBomb() es privado --> se invoca por reflexión. */
    private void ejecutarInitBomb(int x, int y) throws Exception {
        Method initBomb = Alien.Bomb.class.getDeclaredMethod("initBomb", int.class, int.class);
        initBomb.setAccessible(true);
        try {
            initBomb.invoke(bomba, x, y);
        } catch (InvocationTargetException e) {
            fail("initBomb() lanzó una excepción: " + e.getCause());
        }
    }

    private void comprobarPosicion(int xEsperada, int yEsperada) {
        assertAll(
                () -> assertEquals(xEsperada, bomba.getX(), "coordenada x de la bomba"),
                () -> assertEquals(yEsperada, bomba.getY(), "coordenada y de la bomba")
        );
    }

    @Test
    @DisplayName("CP-IB01: (179, 175) nominal -> (179, 175)")
    void cpIb01_nominal() throws Exception {
        ejecutarInitBomb(X_NOMINAL, Y_NOMINAL);
        comprobarPosicion(179, 175);
    }

    @Test
    @DisplayName("CP-IB02: x = -1 (mín-1, no válido) -> x = 0")
    void cpIb02_xMinMenos1() throws Exception {
        ejecutarInitBomb(-1, Y_NOMINAL);
        comprobarPosicion(0, 175);
    }

    @Test
    @DisplayName("CP-IB03: x = 0 (mín) -> x = 0")
    void cpIb03_xMin() throws Exception {
        ejecutarInitBomb(0, Y_NOMINAL);
        comprobarPosicion(0, 175);
    }

    @Test
    @DisplayName("CP-IB04: x = 1 (mín+1) -> x = 1")
    void cpIb04_xMinMas1() throws Exception {
        ejecutarInitBomb(1, Y_NOMINAL);
        comprobarPosicion(1, 175);
    }

    @Test
    @DisplayName("CP-IB05: x = 357 (máx-1) -> x = 357")
    void cpIb05_xMaxMenos1() throws Exception {
        ejecutarInitBomb(357, Y_NOMINAL);
        comprobarPosicion(357, 175);
    }

    @Test
    @DisplayName("CP-IB06: x = 358 (máx) -> x = 358")
    void cpIb06_xMax() throws Exception {
        ejecutarInitBomb(358, Y_NOMINAL);
        comprobarPosicion(358, 175);
    }

    @Test
    @DisplayName("CP-IB07: x = 359 (máx+1, no válido) -> x = 358")
    void cpIb07_xMaxMas1() throws Exception {
        ejecutarInitBomb(359, Y_NOMINAL);
        comprobarPosicion(358, 175);
    }

    @Test
    @DisplayName("CP-IB08: y = -1 (mín-1, no válido) -> y = 0")
    void cpIb08_yMinMenos1() throws Exception {
        ejecutarInitBomb(X_NOMINAL, -1);
        comprobarPosicion(179, 0);
    }

    @Test
    @DisplayName("CP-IB09: y = 0 (mín) -> y = 0")
    void cpIb09_yMin() throws Exception {
        ejecutarInitBomb(X_NOMINAL, 0);
        comprobarPosicion(179, 0);
    }

    @Test
    @DisplayName("CP-IB10: y = 1 (mín+1) -> y = 1")
    void cpIb10_yMinMas1() throws Exception {
        ejecutarInitBomb(X_NOMINAL, 1);
        comprobarPosicion(179, 1);
    }

    @Test
    @DisplayName("CP-IB11: y = 349 (máx-1) -> y = 349")
    void cpIb11_yMaxMenos1() throws Exception {
        ejecutarInitBomb(X_NOMINAL, 349);
        comprobarPosicion(179, 349);
    }

    @Test
    @DisplayName("CP-IB12: y = 350 (máx) -> y = 350")
    void cpIb12_yMax() throws Exception {
        ejecutarInitBomb(X_NOMINAL, 350);
        comprobarPosicion(179, 350);
    }

    @Test
    @DisplayName("CP-IB13: y = 351 (máx+1, no válido) -> y = 350")
    void cpIb13_yMaxMas1() throws Exception {
        ejecutarInitBomb(X_NOMINAL, 351);
        comprobarPosicion(179, 350);
    }
}
