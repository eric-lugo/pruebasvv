
package main.ShotTests;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import space_invaders.sprites.Alien;
import space_invaders.sprites.Shot;
import space_invaders.sprites.Sprite;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import static org.junit.jupiter.api.Assertions.*;


public class ShotTest {

    private int H_SPACE;
    private int V_SPACE;
    private Method metodo;
    private Field valx;
    private Field valy;
    private Shot shot;

    @BeforeEach
    public void setUp() throws Exception {

        shot = new Shot();

        Field hField= Shot.class.getDeclaredField("H_SPACE");
        Field vField = Shot.class.getDeclaredField("V_SPACE");
        metodo=Shot.class.getDeclaredMethod("initShot", int.class, int.class);
        valx=Sprite.class.getDeclaredField("x");
        valy=Sprite.class.getDeclaredField("y");

        hField.setAccessible(true);
        vField.setAccessible(true);
        metodo.setAccessible(true);
        valx.setAccessible(true);
        valy.setAccessible(true);


        H_SPACE = hField.getInt(shot);
        V_SPACE = vField.getInt(shot);
    }



    // =========================================================================
    // 1. Pruebas para Constructor Shot()
    // =========================================================================

    @Test
    @DisplayName("CP1: Crear un objeto con Shot()")
    public void testCP1_CrearObjetoShotSinParametros() {
        Shot shot = new Shot();

        assertNotNull(shot, "El objeto SHOT creado no debe ser nulo");
    }

    @Test
    @DisplayName("CP2: Crear dos objetos con Shot()")
    public void testCP2_CrearDosInstanciasDistintasShot() {
        Shot shot1 = new Shot();
        Shot shot2 = new Shot();

        assertNotNull(shot1, "El primer objeto no debe ser nulo");
        assertNotNull(shot2, "El segundo objeto no debe ser nulo");
        assertNotSame(shot1, shot2, "Deben ser dos instancias distintas de Shot");
    }

    // =========================================================================
    // 2. Pruebas para Constructor Shot(int x, int y)
    // =========================================================================

    @Test
    @DisplayName("Shot(x, y): Comprobar que el constructor invoca e inicializa correctamente con initShot")
    public void testShotConParametros_InvocaInitShot() {
        int xInput = 150;
        int yInput = 150;

        Shot shot = new Shot(xInput, yInput);

        assertNotNull(shot, "La instancia creada no debe ser nula");

        // Se verifica que el constructor ejecuta initShot ajustando las coordenadas
        assertEquals(xInput + H_SPACE, shot.getX(),
                "Debe llamar a initShot y sumar H_SPACE a X");

        assertEquals(yInput - V_SPACE, shot.getY(),
                "Debe llamar a initShot y restar V_SPACE a Y");
    }

    // =========================================================================
    // 3. Pruebas para initshot(int x, int y)
    // =========================================================================

    @Test
    @DisplayName("SH-01: Valores nominales (x=150, y=150)")
    public void testSH01_ValoresNominales() throws InvocationTargetException, IllegalAccessException {
        metodo.invoke(shot,150, 150);
        assertEquals(150+ H_SPACE,shot.getX());
        assertEquals(150-V_SPACE,shot.getY());
    }

    @Test
    @DisplayName("SH-02: Límite inferior de x - 1 (x=4, y=150) - Inválido")
    public void testSH02_LimiteInferiorX_Menor() {
        assertThrows(InvocationTargetException.class, () -> {
            metodo.invoke(shot, 4, 150);
        });
    }

    @Test
    @DisplayName("SH-03: Límite inferior de x (x=5, y=150) - Válido")
    public void testSH03_LimiteInferiorX() throws InvocationTargetException, IllegalAccessException {
        metodo.invoke(shot,5, 150);
        assertEquals(5+H_SPACE,shot.getX());
        assertEquals(150-V_SPACE,shot.getY());
    }

    @Test
    @DisplayName("SH-04: Límite inferior de x + 1 (x=6, y=150) - Válido")
    public void testSH04_LimiteInferiorX_MasUno() throws InvocationTargetException, IllegalAccessException {
        metodo.invoke(shot,6, 150);
        assertEquals(6+H_SPACE,shot.getX());
        assertEquals(150-V_SPACE,shot.getY());
    }

    @Test
    @DisplayName("SH-05: Límite superior de x - 1 (x=327, y=150) - Válido")
    public void testSH05_LimiteSuperiorX_MenosUno() throws InvocationTargetException, IllegalAccessException {
        metodo.invoke(shot,327, 150);
        assertEquals(327+H_SPACE,shot.getX());
        assertEquals(150-V_SPACE,shot.getY());
    }

    @Test
    @DisplayName("SH-06: Límite superior de x (x=328, y=150) - Válido")
    public void testSH06_LimiteSuperiorX() throws InvocationTargetException, IllegalAccessException {
        metodo.invoke(shot,328, 150);
        assertEquals(328+H_SPACE,shot.getX());
        assertEquals(150-V_SPACE,shot.getY());
    }

    @Test
    @DisplayName("SH-07: Límite superior de x + 1 (x=329, y=150) - Inválido")
    public void testSH07_LimiteSuperiorX_MasUno() {
        assertThrows(InvocationTargetException.class, () -> {
            metodo.invoke(shot, 329, 150);
        });
    }

    @Test
    @DisplayName("SH-08: Límite inferior de y - 1 (x=150, y=-1) - Inválido")
    public void testSH08_LimiteInferiorY_Menor() {
        assertThrows(InvocationTargetException.class, () -> {
            metodo.invoke(shot, 150, -1);
        });

    }

    @Test
    @DisplayName("SH-09: Límite inferior de y (x=150, y=0) - Válido")
    public void testSH09_LimiteInferiorY() throws InvocationTargetException, IllegalAccessException {
        metodo.invoke(shot,150, 0);
        assertEquals(150+H_SPACE,shot.getX());
        assertEquals(0-V_SPACE,shot.getY());
    }

    @Test
    @DisplayName("SH-10: Límite inferior de y + 1 (x=150, y=1) - Válido")
    public void testSH10_LimiteInferiorY_MasUno() throws InvocationTargetException, IllegalAccessException {
        metodo.invoke(shot,150, 1);
        assertEquals(150+H_SPACE,shot.getX());
        assertEquals(1-V_SPACE,shot.getY());
    }


    @Test
    @DisplayName("SH-11: Límite superior de y - 1 (x=150, y=289) - Válido")
    public void testSH11_LimiteSuperiorY_MenosUno() throws InvocationTargetException, IllegalAccessException {
        metodo.invoke(shot,150, 289);
        assertEquals(150+H_SPACE,shot.getX());
        assertEquals(289-V_SPACE,shot.getY());
    }

    @Test
    @DisplayName("SH-12: Límite superior de y (x=150, y=290) - Válido")
    public void testSH12_LimiteSuperiorY() throws InvocationTargetException, IllegalAccessException {
        metodo.invoke(shot,150, 290);
        assertEquals(150+H_SPACE,shot.getX());
        assertEquals(290-V_SPACE,shot.getY());

    }


    @Test
    @DisplayName("SH-13: Límite superior de y + 1 (x=150, y=291) - Inválido")
    public void testSH13_LimiteSuperiorY_MasUno() {
        assertThrows(InvocationTargetException.class, () -> {
            metodo.invoke(shot, 150, 291);
        });
    }
}
