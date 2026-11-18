package BoardTests;

import main.Board;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class UpdateWhiteBoxTest {

    // Helper para invocar el método privado update() mediante Reflexión
    private void invokeUpdatePrivate(Board board) {
        try {
            Method updateMethod = Board.class.getDeclaredMethod("update");
            updateMethod.setAccessible(true);
            updateMethod.invoke(board);
        } catch (Exception e) {
            throw new RuntimeException("Error al invocar el método privado update()", e);
        }
    }

    // Helper para modificar el atributo privado 'deaths'
    private void setDeathsPrivate(Board board, int deaths) {
        try {
            Field deathsField = Board.class.getDeclaredField("deaths");
            deathsField.setAccessible(true);
            deathsField.setInt(board, deaths);
        } catch (Exception e) {
            throw new RuntimeException("Error al modificar el atributo privado deaths", e);
        }
    }

    // Helper para leer el atributo privado 'inGame'
    private boolean getInGamePrivate(Board board) {
        try {
            Field inGameField = Board.class.getDeclaredField("inGame");
            inGameField.setAccessible(true);
            return inGameField.getBoolean(board);
        } catch (Exception e) {
            throw new RuntimeException("Error al leer el atributo privado inGame", e);
        }
    }

    // Helper para leer el atributo privado 'message'
    private String getMessagePrivate(Board board) {
        try {
            Field messageField = Board.class.getDeclaredField("message");
            messageField.setAccessible(true);
            return (String) messageField.get(board);
        } catch (Exception e) {
            throw new RuntimeException("Error al leer el atributo privado message", e);
        }
    }

    @Test
    @DisplayName("Camino 1: deaths = 0 (condicion verdadera)")
    void testCamino1_PartidaEnCurso() {
        // 1. Instanciación del tablero (ejecuta gameInit())
        Board board = new Board();

        board.getTimer().stop();
        board.setAliens(new ArrayList<>());
        board.getShot().die();
        board.setInGame(true);
        board.setMessage("Partida en curso");

        // 2. Forzado del valor de la variable de entrada implícita 'deaths' a 0 (CP-UP02)
        setDeathsPrivate(board, 0);

        // 3. Ejecución del método privado update()
        invokeUpdatePrivate(board);

        // 4. Verificaciones del Camino 1: la partida continúa activa y el mensaje no cambia a victoria
        assertTrue(getInGamePrivate(board), "El estado del juego (inGame) debe permanecer en true cuando deaths es 0.");
        assertFalse("Game won!".equals(getMessagePrivate(board)), "El mensaje de estado no debe ser 'Game won!' mientras la partida siga activa.");
    }

    @Test
    @DisplayName("Camino 2: deaths = 25 (condicion falsea)")
    void testCamino2_JuegoGanado() {
        // 1. Instanciación del tablero
        Board board = new Board();

        board.getTimer().stop();
        board.setAliens(new ArrayList<>());
        board.getShot().die();
        board.setInGame(true);
        board.setMessage("Partida en curso");

        // 2. Forzado del valor de 'deaths' a 25 (CP-UP07, superando el límite de 24 muertes)
        setDeathsPrivate(board, 25);

        // 3. Ejecución del método privado update()
        invokeUpdatePrivate(board);

        // 4. Verificaciones del Camino 2: el juego finaliza y asigna el mensaje de victoria
        assertFalse(getInGamePrivate(board), "El estado del juego (inGame) debe pasar a false al alcanzarse la victoria (deaths >= 24).");
        assertEquals("Game won!", getMessagePrivate(board), "El mensaje de finalización debe ser exactamente 'Game won!'.");
    }
}
