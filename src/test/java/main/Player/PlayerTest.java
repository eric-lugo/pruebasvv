package main.Player;

import main.Commons;
import org.junit.jupiter.api.Test;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import space_invaders.sprites.Player;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class PlayerTest {

    @Test
    public void testInitPlayer(){
        Player player = new Player();
        try{
            Method metodo = Player.class.getDeclaredMethod("initPlayer");
            metodo.setAccessible(true);
            metodo.invoke(player);

        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        } catch (InvocationTargetException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }

        int xEsperado = Commons.BOARD_WIDTH / 2;
        int yEsperado = Commons.GROUND - 10;

        assertEquals(xEsperado, player.getX());
        assertEquals(yEsperado, player.getY());


    }
}
