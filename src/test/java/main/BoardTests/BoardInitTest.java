package main.BoardTests;

import main.Board;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import space_invaders.sprites.Player;
import space_invaders.sprites.Shot;
import static org.junit.jupiter.api.Assertions.*;
import main.Commons;
import space_invaders.sprites.Alien;
import java.util.List;


public class BoardInitTest {

    @Test
    @DisplayName("CP-B02: Comprobar creación de 24 aliens")
    public void testCP_B02_CreacionAliens() throws Exception {
        Board board = new Board();

        assertEquals(24, board.getAliens().size());

    }


/*
    @Test
    @DisplayName("CP-B03: Posiciones iniciales de los aliens")
    public void testCP_B03_PosicionesInicialesAliens() {

        Board board = new Board();
        List<Alien> aliens = board.getAliens();

        assertNotNull(aliens);
        assertEquals(24, aliens.size());

        for (int i = 0; i < aliens.size(); i++) {
            Alien a = aliens.get(i);
            System.out.println("Alien " + i
                    + " -> X=" + a.getX()
                    + ", Y=" + a.getY());
        }

        int alienIndex = 0;

        for (int row = 0; row < Commons.ALIEN_ROWS; row++) {
            for (int col = 0; col < Commons.ALIEN_COLUMNS; col++) {

                Alien alien = aliens.get(alienIndex);

                int expectedX = Commons.ALIEN_INIT_X
                        + Commons.ALIEN_SEPARATOR * col;

                int expectedY = Commons.ALIEN_INIT_Y
                        + Commons.ALIEN_SEPARATOR * row;

                assertEquals(expectedX, alien.getX());
                assertEquals(expectedY, alien.getY());

                alienIndex++;
            }
        }
    }

    */


    @Test
    @DisplayName("CP-B04: Posición inicial del jugador")
    public void testCP_B04_PosicionInicialJugador() {

        Board board = new Board();
        Player player = board.getPlayer();

        assertNotNull(player, "El jugador no debe ser nulo");

        assertEquals(179, player.getX(),
                "La coordenada X del jugador debe ser 179");

        assertEquals(280, player.getY(),
                "La coordenada Y del jugador debe ser 280");
    }

    @Test
    @DisplayName("CP-B05: Comprobar que se crea el objeto Shot")
    public void testCP_B05_CreacionObjetoShot() {
        Board board = new Board();

        Shot shot = board.getShot();

        assertNotNull(shot, "El objeto Shot debe estar instanciado tras la inicialización del tablero.");
    }

    @Test
    @DisplayName("CP-B01: Comprobar que al crear un Board se inicializa el tablero correctamente")
    public void testCP_B01_InicializacionTablero() {
        Board board = new Board();

        assertNotNull(board, "La instancia de Board debe haberse creado correctamente.");

        assertNotNull(board.getPlayer(), "El jugador (Player) debe estar inicializado tras instanciar el tablero.");
        assertNotNull(board.getShot(), "El objeto disparo (Shot) debe estar inicializado tras la instanciación.");
        assertNotNull(board.getAliens(), "La lista de alienígenas no debe ser nula tras la inicialización.");

        assertFalse(board.getAliens().isEmpty(), "La lista de alienígenas no debe estar vacía al iniciar el juego.");
    }

}
