package BoardTests;

import main.Board;
import main.Commons;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class gameInitWhiteBox {
    @Test
    void C3_conAliens() {
        Board board = new Board();

        int totalEsperado = Commons.ALIEN_ROWS * Commons.ALIEN_COLUMNS;
        assertEquals(totalEsperado, board.getAliens().size());
        assertEquals(Commons.ALIEN_INIT_X, board.getAliens().get(0).getX());
        assertEquals(Commons.ALIEN_INIT_Y, board.getAliens().get(0).getY());
        assertNotNull(board.getPlayer());
        assertNotNull(board.getShot());
    }
}

