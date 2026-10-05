import org.junit.Test;
import static org.junit.Assert.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Pruebas de unidad de SlotMachineContest (ciclo 3) 
 *
 * @author Bryan Pinzón y Valentina Prieto
 * @version Ciclo 3
 */
public class SlotMachineContestTestC3{

    // Tamaños representativos para nuestras pruebas
    private static final int[] TAMAÑOS = {3, 5, 8, 15, 30, 50};

    @Test
    public void solveShouldStayWithinTheActionBudget(){
        for(int n : TAMAÑOS){
            int[][] acciones = SlotMachineContest.solve(n);
            assertTrue("n=" + n + " se paso del presupuesto: " + acciones.length, acciones.length <= 10000);
        }
    }

    @Test
    public void solveShouldReturnActionsWithTwoElementsEach(){
        int[][] acciones = SlotMachineContest.solve(20);
        for(int[] accion : acciones){
            assertEquals("cada accion debe ser {i,j}", 2, accion.length);
        }
    }

    @Test
    public void solveShouldOnlyReferenceValidWheelPositions(){
        int n = 20;
        int[][] acciones = SlotMachineContest.solve(n);
        for(int[] accion : acciones){
            int wheel = accion[0];
            assertTrue("posicion de rueda fuera de rango: " + wheel, wheel >= 1 && wheel <= n);
        }
    }

    @Test
    public void solveShouldNotIncludeActionsThatDoNotRotateAnything(){
        int[][] acciones = SlotMachineContest.solve(15);
        for(int[] accion : acciones){
            assertNotEquals("no deberia haber una accion con j=0", 0, accion[1]);
        }
    }

    @Test
    public void solveShouldSolveTheSmallestMachine(){
        int[][] acciones = SlotMachineContest.solve(3);
        assertNotNull(acciones);
        assertTrue(acciones.length <= 10000);
    }

    @Test
    public void solveShouldSolveTheLargestMachine(){
        int[][] acciones = SlotMachineContest.solve(50);
        assertNotNull(acciones);
        assertTrue("n=50 se paso del presupuesto: " + acciones.length,
            acciones.length <= 10000);
    }
}