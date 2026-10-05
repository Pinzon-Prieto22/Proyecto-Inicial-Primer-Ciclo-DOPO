import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import java.lang.reflect.Method;

/**
 * Pruebas de unidad de SlotMachine (ciclo 4)
 *
 * @author Bryan Pinzón y Valentina Prieto
 * @version Ciclo 4
 */
public class SlotMachineC4Test{

    private SlotMachine maquina;

    @Before
    public void setUp(){
        maquina = new SlotMachine();
    }

    /**
     * Pruebas para addwheel segun el type
     */
    
    @Test
    public void addWheelShouldCreateANormalWheelWithoutType(){
        maquina.addWheel(1);
        assertTrue(maquina.ok());
        assertEquals(1, maquina.configuration().length);
    }

    @Test
    public void addWheelWithTypeShouldAcceptLeftyRebelAndCrazy(){
        maquina.addWheel("lefty", 1);
        assertTrue(maquina.ok());
        maquina.addWheel("rebel", 2);
        assertTrue(maquina.ok());
        maquina.addWheel("crazy", 3);
        assertTrue(maquina.ok());
        assertEquals(3, maquina.configuration().length);
    }

    @Test
    public void addWheelWithTypeShouldBeCaseInsensitive(){
        maquina.addWheel("LEFTY", 1);
        assertTrue(maquina.ok());
    }

    @Test
    public void addWheelShouldNotAcceptAnInvalidType(){
        maquina.addWheel("inventado", 1);
        assertFalse(maquina.ok());
        assertEquals(0, maquina.configuration().length);
    }

    /**
     * Pruebas para addSymbol segun el type
     */
    @Test
    public void addSymbolShouldCreateANormalSymbolWithoutType(){
        maquina.addSymbol(1, "red");
        assertTrue(maquina.ok());
        assertArrayEquals(new String[]{"red"}, maquina.symbols());
    }

    @Test
    public void addSymbolWithTypeShouldAcceptEphemeralAndShy(){
        maquina.addSymbol("ephemeral", 1, "red");
        assertTrue(maquina.ok());
        maquina.addSymbol("shy", 2, "blue");
        assertTrue(maquina.ok());
        assertArrayEquals(new String[]{"red", "blue"}, maquina.symbols());
    }

    @Test
    public void addSymbolShouldNotAcceptAnInvalidType(){
        maquina.addSymbol("inventado", 1, "red");
        assertFalse(maquina.ok());
        assertEquals(0, maquina.symbols().length);
    }

    @Test
    public void addSymbolShouldStillRejectAnInvalidColorRegardlessOfType(){
        maquina.addSymbol("ephemeral", 1, "notacolor");
        assertFalse(maquina.ok());
    }

    /**
     * Pruebas para lefty wheel
     */

    @Test
    public void leftyWheelShouldCopyItsLeftNeighborWhenSpinningInOrder(){
        maquina.addSymbol(1, "red");
        maquina.addSymbol(2, "blue");
        maquina.addSymbol(3, "green");
        maquina.addWheel(1); // normal
        maquina.addWheel("lefty", 2);
        maquina.placeSymbol(1, "red");
        maquina.placeSymbol(2, "blue");

        maquina.spinOtro(2);

        assertTrue(maquina.ok());
        assertEquals("red", maquina.configuration()[1]);
    }

    @Test
    public void leftyWheelShouldCopyItsLeftNeighborWhenSpinningAtRandom(){
        maquina.addSymbol(1, "red");
        maquina.addSymbol(2, "blue");
        maquina.addWheel(1);
        maquina.addWheel("lefty", 2);
        maquina.placeSymbol(1, "red");
        maquina.placeSymbol(2, "blue");

        maquina.spin(2); // girar al azar tambien debe copiar, no escoger al azar

        assertTrue(maquina.ok());
        assertEquals("red", maquina.configuration()[1]);
    }

    @Test
    public void leftyWheelWithoutALeftNeighborShouldSpinLikeANormalWheel(){
        maquina.addSymbol(1, "red");
        maquina.addSymbol(2, "blue");
        maquina.addWheel("lefty", 1); // de primera, no tiene vecina izquierda
        maquina.placeSymbol(1, "red");

        maquina.spinOtro(1);

        assertTrue(maquina.ok());
        assertEquals("blue", maquina.configuration()[0]);
    }

    @Test
    public void leftyWheelShouldFollowItsNeighborEvenAfterTheNeighborChangesAgain(){
        maquina.addSymbol(1, "red");
        maquina.addSymbol(2, "blue");
        maquina.addWheel(1);
        maquina.addWheel("lefty", 2);
        maquina.placeSymbol(1, "red");
        maquina.placeSymbol(2, "red");

        maquina.placeSymbol(1, "blue"); // la vecina cambia
        maquina.spinOtro(2); // la lefty se vuelve a copiar

        assertEquals("blue", maquina.configuration()[1]);
    }

    /**
     * Pruebas para rebel eheel
     */
    @Test
    public void rebelWheelShouldNotAllowLock(){
        maquina.addWheel("rebel", 1);
        maquina.lock(1);
        assertFalse(maquina.ok());
    }

    @Test
    public void rebelWheelShouldNotAllowSwap(){
        maquina.addSymbol(1, "red");
        maquina.addSymbol(2, "blue");
        maquina.addWheel("rebel", 1);
        maquina.addWheel("normal", 2);
        maquina.placeSymbol(1, "red");
        maquina.placeSymbol(2, "blue");

        maquina.swap(1, 2);

        assertFalse(maquina.ok());
        assertEquals("red", maquina.configuration()[0]);
        assertEquals("blue", maquina.configuration()[1]);
    }

    @Test
    public void rebelWheelShouldNotAllowDelete(){
        maquina.addWheel("rebel", 1);
        maquina.delWheel(1);
        assertFalse(maquina.ok());
        assertEquals(1, maquina.configuration().length);
    }

    @Test
    public void rebelWheelShouldStillBeAbleToSpinAndReceivePlaceSymbol(){
        maquina.addSymbol(1, "red");
        maquina.addSymbol(2, "blue");
        maquina.addWheel("rebel", 1);

        maquina.placeSymbol(1, "red");
        assertTrue(maquina.ok());

        maquina.spinOtro(1);
        assertTrue(maquina.ok());
        assertEquals("blue", maquina.configuration()[0]);
    }

    @Test
    public void rebelWheelShouldNotPreventOperationsOnOtherWheels(){
        maquina.addWheel("rebel", 1);
        maquina.addWheel("normal", 2);

        maquina.lock(2);

        assertTrue("fijar la rueda normal si deberia funcionar", maquina.ok());
    }

    /**
     * Pruebas para crazy wheel
     */
    @Test
    public void crazyWheelShouldStillCountNormallyForConfigurationAndJackpot(){
        maquina.addSymbol(1, "red");
        maquina.addWheel("crazy", 1);
        maquina.addWheel("normal", 2);
        maquina.placeSymbol(1, "red");
        maquina.placeSymbol(2, "red");

        assertEquals(2, maquina.configuration().length);
        assertTrue(maquina.isJackpot());
    }

    @Test
    public void crazyWheelShouldStillBeLockableSwappableAndDeletable(){
        maquina.addSymbol(1, "red");
        maquina.addSymbol(2, "blue");
        maquina.addWheel("crazy", 1);
        maquina.addWheel("normal", 2);
        maquina.placeSymbol(1, "red");
        maquina.placeSymbol(2, "blue");

        maquina.swap(1, 2);
        assertTrue(maquina.ok());
        assertEquals("blue", maquina.configuration()[0]);

        maquina.lock(1);
        assertTrue(maquina.ok());

        maquina.delWheel(2);
        assertTrue(maquina.ok());
        assertEquals(1, maquina.configuration().length);
    }

    /**
     * pruebas para ephemeral y shy
     */
    @Test
    public void ephemeralSymbolShouldNotFailAfterManyReassignments(){
        maquina.addSymbol("ephemeral", 1, "red");
        maquina.addSymbol(2, "blue");
        maquina.addWheel(1);

        for(int i = 0; i < 12; i++){
            maquina.placeSymbol(1, "red");
            assertTrue(maquina.ok());
        }
        assertEquals("red", maquina.configuration()[0]);
    }

    @Test
    public void ephemeralSymbolShouldStillCountForDistinctSymbolsAndJackpot(){
        maquina.addSymbol("ephemeral", 1, "red");
        maquina.addWheel(1);
        maquina.addWheel(2);
        maquina.placeSymbol(1, "red");
        maquina.placeSymbol(2, "red");

        assertEquals(1, maquina.distinctSymbols());
        assertTrue(maquina.isJackpot());
    }

    @Test
    public void shySymbolShouldNotFailAfterManyReassignments(){
        maquina.addSymbol("shy", 1, "blue");
        maquina.addSymbol(2, "red");
        maquina.addWheel(1);

        for(int i = 0; i < 9; i++){
            maquina.placeSymbol(1, "blue");
            assertTrue(maquina.ok());
        }
        assertEquals("blue", maquina.configuration()[0]);
    }

    @Test
    public void shySymbolShouldStillCountForDistinctSymbolsEvenWhileHidden(){
        maquina.addSymbol("shy", 1, "blue");
        maquina.addWheel(1);
        maquina.addWheel(2);

        maquina.placeSymbol(1, "blue"); // primera vez: queda visible
        maquina.placeSymbol(1, "blue"); // segunda vez: alterna a invisible
        maquina.placeSymbol(2, "blue");

        // aunque visualmente este oculto, logicamente sigue siendo "blue"
        assertEquals("blue", maquina.configuration()[0]);
        assertEquals(1, maquina.distinctSymbols());
        assertTrue(maquina.isJackpot());
    }

    /**
     * Pruebas de que agrega bien las ruedas con nuestra nueva disposicion
     */
    @Test
    public void machineShouldSupportMoreThanTenWheelsAcrossSeveralRows(){
        for(int i = 1; i <= 23; i++){
            maquina.addWheel(i);
            assertTrue("fallo agregando la rueda " + i, maquina.ok());
        }
        assertEquals(23, maquina.configuration().length);
    }

    @Test
    public void machineShouldStillReachTheMaximumOfFiftyWheelsWithRows(){
        for(int i = 1; i <= 50; i++){
            maquina.addWheel(i);
        }
        assertTrue(maquina.ok());
        assertEquals(50, maquina.configuration().length);
        maquina.addWheel(1);
        assertFalse(maquina.ok());
    }

    /**
     * Pruebas para el metodo que usamos de "seed" para mantener la misma 
     * solucion a la misma maquina con la misma configuracion
     */
    @Test
    public void sameSeedShouldProduceTheExactSameInitialConfiguration(){
        SlotMachine m1 = new SlotMachine(10, 42L);
        SlotMachine m2 = new SlotMachine(10, 42L);
        assertArrayEquals(m1.symbols(), m2.symbols());
        assertArrayEquals(m1.configuration(), m2.configuration());
    }

    @Test
    public void differentSeedsShouldUsuallyProduceDifferentConfigurations(){
        SlotMachine m1 = new SlotMachine(10, 1L);
        SlotMachine m2 = new SlotMachine(10, 2L);
        assertFalse(java.util.Arrays.equals(m1.configuration(), m2.configuration()));
    }

    @Test
    public void seededConstructorShouldNeverStartAlreadyAtJackpot(){
        for(long semilla = 0; semilla < 20; semilla++){
            SlotMachine m = new SlotMachine(5, semilla);
            assertFalse("semilla=" + semilla, m.isJackpot());
        }
    }

    /**
     * Pruebas para nuestra nueva excepcion
     */

    @Test
    public void contestSolveShouldStillStayWithinBudgetAfterTheEarlyExitChange(){
        for(int n : new int[]{3, 10, 30, 50}){
            int[][] acciones = SlotMachineContest.solve(n);
            assertTrue("n=" + n, acciones.length <= 10000);
        }
    }

    @Test
    public void contestResolverShouldActuallyReachJackpotWithTheEarlyExit() throws Exception{
        Method resolverConSemilla = SlotMachineContest.class.getDeclaredMethod(
            "resolverConSemilla", int.class, long.class);
        resolverConSemilla.setAccessible(true);

        for(int n : new int[]{3, 8, 20, 50}){
            long semilla = 777L + n;
            int[][] acciones = (int[][]) resolverConSemilla.invoke(null, n, semilla);

            SlotMachine maquinaVerificacion = new SlotMachine(n, semilla);
            for(int[] accion : acciones){
                maquinaVerificacion.spin(accion[0], accion[1]);
            }
            assertTrue("n=" + n + " no quedo en jackpot", maquinaVerificacion.isJackpot());
        }
    }
}