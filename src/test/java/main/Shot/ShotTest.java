
package main.Shot;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import space_invaders.sprites.Shot;
import space_invaders.sprites.Sprite;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import static org.junit.jupiter.api.Assertions.*;




/**
 * Pruebas de Caja Negra para la clase Shot.
 * Cubre la verificación del constructor por defecto,
 * el constructor parametrizado y las pruebas de valores límite
 * (SH-01 a SH-13) sobre el método privado initShot
 * mediante reflexión (java.lang.reflect).
 */
public class ShotTest {

    private int H_SPACE;
    private int V_SPACE;

    @BeforeEach
    public void setUp() throws Exception {
        Field hField= Shot.class.getDeclaredField("H_SPACE");
        Field vField = Shot.class.getDeclaredField("V_SPACE");

        hField.setAccessible(true);
        vField.setAccessible(true);

        Shot shot = new Shot();

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
    // 3. Método privado initShot(int x, int y) mediante Reflexión
    //    (SH-01 a SH-13)
    // =========================================================================

    /**
     * Método auxiliar para ejecutar initShot por reflexión y verificar
     * los atributos privados x e y heredados de Sprite.
     */
    private void probarInitShotReflexion(
            int xInput, int yInput, int xEsperado, int yEsperado) {

        Shot shot = new Shot();

        try {
            // Referencia y cambio de visibilidad del método privado initShot
            Method metodo = Shot.class.getDeclaredMethod(
                    "initShot", int.class, int.class);

            metodo.setAccessible(true);
            metodo.invoke(shot, xInput, yInput);

            // Referencia y acceso a los atributos privados x e y en Sprite
            Field valx = Sprite.class.getDeclaredField("x");
            valx.setAccessible(true);
            int valorx = (int) valx.get(shot);

            Field valy = Sprite.class.getDeclaredField("y");
            valy.setAccessible(true);
            int valory = (int) valy.get(shot);

            // Comprobación de los resultados esperados
            assertEquals(xEsperado, valorx,
                    "El valor final de X no coincide con el esperado");

            assertEquals(yEsperado, valory,
                    "El valor final de Y no coincide con el esperado");

        } catch (NoSuchMethodException | IllegalAccessException | NoSuchFieldException e) {
            fail("Error al acceder a miembros privados por reflexión: " + e.getMessage());

        } catch (InvocationTargetException e) {
            fail("initShot lanzó una excepción no esperada: " + e.getCause());
        }
    }

    /**
     * Método auxiliar para verificar que initShot rechaza entradas inválidas.
     */
    private void probarInitShotExcepcion(int xInput, int yInput) {
        Shot shot = new Shot();

        assertThrows(InvocationTargetException.class, () -> {
            Method metodo = Shot.class.getDeclaredMethod(
                    "initShot", int.class, int.class);

            metodo.setAccessible(true);
            metodo.invoke(shot, xInput, yInput);

        }, "Se esperaba que initShot lanzara una excepción para entradas fuera de límite");
    }

    // =========================================================================
    // 4. Casos de Prueba de Valores Límite
    // =========================================================================

    @Test
    @DisplayName("SH-01: Valores nominales (x=150, y=150)")
    public void testSH01_ValoresNominales() {
        probarInitShotReflexion(
                150, 150,
                150 + H_SPACE,
                150 - V_SPACE);
    }

    @Test
    @DisplayName("SH-02: Límite inferior de x - 1 (x=4, y=150) - Inválido")
    public void testSH02_LimiteInferiorX_Menor() {
        probarInitShotExcepcion(4, 150);
    }

    @Test
    @DisplayName("SH-03: Límite inferior de x (x=5, y=150) - Válido")
    public void testSH03_LimiteInferiorX() {
        probarInitShotReflexion(
                5, 150,
                5 + H_SPACE,
                150 - V_SPACE);
    }

    @Test
    @DisplayName("SH-04: Límite inferior de x + 1 (x=6, y=150) - Válido")
    public void testSH04_LimiteInferiorX_MasUno() {
        probarInitShotReflexion(
                6, 150,
                6 + H_SPACE,
                150 - V_SPACE);
    }

    @Test
    @DisplayName("SH-05: Límite superior de x - 1 (x=327, y=150) - Válido")
    public void testSH05_LimiteSuperiorX_MenosUno() {
        probarInitShotReflexion(
                327, 150,
                327 + H_SPACE,
                150 - V_SPACE);
    }

    @Test
    @DisplayName("SH-06: Límite superior de x (x=328, y=150) - Válido")
    public void testSH06_LimiteSuperiorX() {
        probarInitShotReflexion(
                328, 150,
                328 +H_SPACE,
                150 - V_SPACE);
    }

    @Test
    @DisplayName("SH-07: Límite superior de x + 1 (x=329, y=150) - Inválido")
    public void testSH07_LimiteSuperiorX_MasUno() {
        probarInitShotExcepcion(329, 150);
    }

    @Test
    @DisplayName("SH-08: Límite inferior de y - 1 (x=150, y=-1) - Inválido")
    public void testSH08_LimiteInferiorY_Menor() {
        probarInitShotExcepcion(150, -1);
    }

    @Test
    @DisplayName("SH-09: Límite inferior de y (x=150, y=0) - Válido")
    public void testSH09_LimiteInferiorY() {
        probarInitShotReflexion(
                150, 0,
                150 + H_SPACE,
                0 - V_SPACE);
    }

    @Test
    @DisplayName("SH-10: Límite inferior de y + 1 (x=150, y=1) - Válido")
    public void testSH10_LimiteInferiorY_MasUno() {
        probarInitShotReflexion(
                150, 1,
                150 + H_SPACE,
                1 - V_SPACE);
    }

    @Test
    @DisplayName("SH-11: Límite superior de y - 1 (x=150, y=289) - Válido")
    public void testSH11_LimiteSuperiorY_MenosUno() {
        probarInitShotReflexion(
                150, 289,
                150 + H_SPACE,
                289 - V_SPACE);
    }

    @Test
    @DisplayName("SH-12: Límite superior de y (x=150, y=290) - Válido")
    public void testSH12_LimiteSuperiorY() {

        try {


            probarInitShotReflexion(
                    150, 290,
                    150 + H_SPACE,
                    290 - V_SPACE);

        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }


    @Test
    @DisplayName("SH-13: Límite superior de y + 1 (x=150, y=291) - Inválido")
    public void testSH13_LimiteSuperiorY_MasUno() {
        probarInitShotExcepcion(150, 291);
    }
}
