package main.AlienTest;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import space_invaders.sprites.Alien;

import static org.junit.jupiter.api.Assertions.*;

class BombTest {

    private Alien alien;

    @BeforeEach
    void setUp() {
        // Bomb es una clase interna de Alien: se crea a partir de un alien
        alien = new Alien(150, 50);
    }

    @Test
    @DisplayName("CP-BO01: new Bomb(179, 175) -> se crea un objeto Bomb no nulo")
    void cpBo01_creaBomba() {
        Alien.Bomb bomba = assertDoesNotThrow(() -> alien.new Bomb(179, 175),
                "el constructor no debe lanzar excepciones");

        assertNotNull(bomba, "debe crearse un objeto Bomb");
    }
}
