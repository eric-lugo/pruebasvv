package main.Player;

import main.Commons;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import space_invaders.sprites.Player;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class PlayerTest {
    Player player;

    @BeforeEach
    void setUp() {
        player = new Player();
    }

    private void setDxPrivate(Player player, int dx) {
        try {
            Field dxPublic;
            try {
                dxPublic = Player.class.getDeclaredField("dx");
            } catch (NoSuchFieldException e) {
                dxPublic = Player.class.getSuperclass().getDeclaredField("dx");
            }
            dxPublic.setAccessible(true);
            dxPublic.setInt(player, dx);
        } catch (Exception e) {
            throw new RuntimeException("Error al modificar dx", e);
        }
    }



    @Test
    public void testInitPlayer(){
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

    //todo: separados los tests de act
    @Test
    public void testActSinMovimiento(){
        player.setX(179);
        setDxPrivate(player, 0);
        player.act();
        assertEquals(179, player.getX());
    }

    @Test
    public void testActMovimientoDerecha(){
        player.setX(179);
        setDxPrivate(player, 2);
        player.act();
        assertEquals(181, player.getX());
    }

    @Test
    public void testActMovimientoIzquierda(){
        player.setX(179);
        setDxPrivate(player, -2);
        player.act();
        assertEquals(177, player.getX());
    }

    @Test
    public void testActMovDerBorde(){
        player.setX(341);
        setDxPrivate(player, 2);
        player.act();
        assertEquals(343, player.getX());
    }

    @Test
    public void testActMovIzqBorde(){
        player.setX(2);
        setDxPrivate(player, -2);
        player.act();
        assertEquals(0, player.getX());
    }

    //todo: estos dos deberíamos probarlos ??
    @Test
    public void testActDerNoMovimiento(){
        player.setX(343);
        setDxPrivate(player, 2);
        player.act();
        assertEquals(343, player.getX());
    }

    @Test
    public void testActIzqNoMovimiento(){
        player.setX(0);
        setDxPrivate(player, -2);
        player.act();
        assertEquals(0, player.getX());
    }

    //todo: ??
}
