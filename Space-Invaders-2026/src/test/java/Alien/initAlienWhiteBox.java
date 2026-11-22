package Alien;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import space_invaders.sprites.Alien;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class initAlienWhiteBox {


    void initAlien_test(String idCaso, int xEntrada, int yEntrada, int xEsperada, int yEsperada) {
        Alien alien = new Alien(xEntrada, yEntrada);

        assertEquals(xEsperada, alien.getX(), "Falló la coordenada X en " + idCaso);
        assertEquals(yEsperada, alien.getY(), "Falló la coordenada Y en " + idCaso);
    }

    @Test
    @DisplayName("Camino 1 : No se cumple ningun if")
    public void testCamino1() {
        initAlien_test("C1", 179, 175, 179, 175);
    }

    @Test
    @DisplayName("Camino 2: X mayor que el ancho del board")
    void testCamino2() {
        initAlien_test("C2", 359, 175, 359, 175);
    }

    @Test
    @DisplayName("Camino 3: X negativa")
    void testCamino3() {
        initAlien_test("C3", -1, 175, -1, 175);
    }

    @Test
    @DisplayName("Camino 4: Y mayor que el alto del board")
    void testCamino4() {
        initAlien_test("C4", 179, 351, 179, 351);
    }


    @Test
    @DisplayName("Camino 5: Y negativo")
    void testCamino5() {

        initAlien_test("C5", 179, -1, 0, 0);
    }
}


