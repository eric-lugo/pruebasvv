package main.Shot;

import static org.junit.jupiter.api.Assertions.*;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.junit.jupiter.api.Test;
import space_invaders.sprites.Shot;
import space_invaders.sprites.Sprite;

/**
 * Pruebas de caja negra para la clase Shot
 * Rangos válidos: x en [5, 328], y en [0, 290]
 * Según el ejemplo del foro initShot(10, 10) deja x = 16 e y = 9
 * es decir H_SPACE = 6 (se suma a x) y V_SPACE = 1 (se resta a y)
 */
class ShotTest {





    // ------------------------------------------------------------------
    // Constructor Shot()
    // ------------------------------------------------------------------

    @Test
    void CP1_constructorSinParametros_creaObjeto() {
        Shot shot = new Shot();
        assertNotNull(shot);
    }

    @Test
    void CP2_constructorSinParametros_dosInstanciasDistintas() {
        Shot shot1 = new Shot();
        Shot shot2 = new Shot();
        assertNotNull(shot1);
        assertNotNull(shot2);
        assertNotSame(shot1, shot2);
    }


}
