import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Pruebas de unidad de SlotMachine (ciclos 1 y 2) 
 * Todas las pruebas mantienen la maquina en modo invisible 
 *
 * @author Bryan Pinzón y Valentina Prieto
 * @version Ciclo 2
 */
public class SlotMachineC2Test{

    private SlotMachine maquina;
    @Before
    public void setUp(){
        maquina = new SlotMachine();
    }

    /**
     * Pruebas cosntructor
     */
    @Test
    public void constructorShouldCreateEmptyMachineInOkState(){
        assertTrue(maquina.ok());
        assertEquals(0, maquina.symbols().length);
        assertEquals(0, maquina.configuration().length);
        assertEquals(0, maquina.distinctSymbols());
        assertFalse(maquina.isJackpot());
    }

    /**
     * Pruebas add symbol
     * 
     */
    @Test
    public void addSymbolShouldAddAValidCssColor(){
        maquina.addSymbol(1, "red");
        assertTrue(maquina.ok());
        assertArrayEquals(new String[]{"red"}, maquina.symbols());
    }

    @Test
    public void addSymbolShouldRespectTheGivenOrder(){
        maquina.addSymbol(1, "red");
        maquina.addSymbol(1, "blue");
        assertArrayEquals(new String[]{"blue", "red"}, maquina.symbols());
    }

    @Test
    public void addSymbolShouldClampNegativePositionToFirst(){
        maquina.addSymbol(1, "red");
        maquina.addSymbol(-10, "blue");
        assertArrayEquals(new String[]{"blue", "red"}, maquina.symbols());
    }

    @Test
    public void addSymbolShouldClampTooLargePositionToLast(){
        maquina.addSymbol(1, "red");
        maquina.addSymbol(99, "blue");
        assertArrayEquals(new String[]{"red", "blue"}, maquina.symbols());
    }

    @Test
    public void addSymbolShouldNotAddAnInvalidCssColor(){
        maquina.addSymbol(1, "notacolor");
        assertFalse(maquina.ok());
        assertEquals(0, maquina.symbols().length);
    }

    @Test
    public void addSymbolShouldNotAddADuplicateColor(){
        maquina.addSymbol(1, "red");
        maquina.addSymbol(2, "RED");
        assertFalse(maquina.ok());
        assertEquals(1, maquina.symbols().length);
    }

    /**
     * Pruebas del symbol
     */
    @Test
    public void delSymbolShouldRemoveAnExistingColor(){
        maquina.addSymbol(1, "red");
        maquina.delSymbol("red");
        assertTrue(maquina.ok());
        assertEquals(0, maquina.symbols().length);
    }

    @Test
    public void delSymbolShouldNotRemoveAColorThatDoesNotExist(){
        maquina.addSymbol(1, "red");
        maquina.delSymbol("blue");
        assertFalse(maquina.ok());
        assertEquals(1, maquina.symbols().length);
    }

    @Test
    public void delSymbolShouldReassignWheelsThatShowedTheRemovedColor(){
        maquina.addSymbol(1, "red");
        maquina.addSymbol(2, "blue");
        maquina.addWheel(1);
        maquina.placeSymbol(1, "red");
        maquina.delSymbol("red");
        assertTrue(maquina.ok());
        assertEquals("blue", maquina.configuration()[0]);
    }

    @Test
    public void delSymbolShouldLeaveWheelsEmptyWhenNoSymbolsRemain(){
        maquina.addSymbol(1, "red");
        maquina.addWheel(1);
        maquina.placeSymbol(1, "red");
        maquina.delSymbol("red");
        assertEquals("none", maquina.configuration()[0]);
    }

    /**
     * Pruebas patra addwheel
     */
    @Test
    public void addWheelShouldCreateAWheelWithoutSymbol(){
        maquina.addWheel(1);
        assertTrue(maquina.ok());
        assertArrayEquals(new String[]{"none"}, maquina.configuration());
    }

    @Test
    public void addWheelShouldClampNegativePositionToFirst(){
        maquina.addSymbol(1, "red");
        maquina.addWheel(1);
        maquina.placeSymbol(1, "red");
        maquina.addWheel(-3);
        assertTrue(maquina.ok());
        assertEquals("none", maquina.configuration()[0]);
        assertEquals("red", maquina.configuration()[1]);
    }

    @Test
    public void addWheelShouldClampTooLargePositionToLastWithoutGaps(){
        maquina.addWheel(1);
        maquina.addWheel(60);
        assertTrue(maquina.ok());
        assertEquals(2, maquina.configuration().length);
    }

    @Test
    public void addWheelShouldShiftExistingWheelsWhenPositionIsAlreadyUsed(){
        maquina.addSymbol(1, "red");
        maquina.addWheel(1);
        maquina.placeSymbol(1, "red");
        maquina.addWheel(1);
        assertTrue(maquina.ok());
        assertEquals(2, maquina.configuration().length);
        assertEquals("none", maquina.configuration()[0]);
        assertEquals("red", maquina.configuration()[1]);
    }

    @Test
    public void addWheelShouldNotExceedTheMaximumCapacity(){
        for(int i = 1; i <= 50; i++){
            maquina.addWheel(i);
        }
        assertTrue(maquina.ok());
        assertEquals(50, maquina.configuration().length);

        maquina.addWheel(1);
        assertFalse(maquina.ok());
        assertEquals(50, maquina.configuration().length);
    }

    /**
     * Pruebas para delWheel
     */

    @Test
    public void delWheelShouldRemoveAnExistingWheel(){
        maquina.addWheel(1);
        maquina.delWheel(1);
        assertTrue(maquina.ok());
        assertEquals(0, maquina.configuration().length);
    }

    @Test
    public void delWheelShouldNotRemoveAnythingWhenThereAreNoWheels(){
        maquina.delWheel(1);
        assertFalse(maquina.ok());
    }

    @Test
    public void delWheelShouldCloseTheGapAfterRemovingAMiddleWheel(){
        maquina.addSymbol(1, "red");
        maquina.addSymbol(2, "blue");
        maquina.addSymbol(3, "green");
        maquina.addWheel(1);
        maquina.addWheel(2);
        maquina.addWheel(3);
        maquina.placeSymbol(1, "red");
        maquina.placeSymbol(2, "blue");
        maquina.placeSymbol(3, "green");
        maquina.delWheel(2);
        assertTrue(maquina.ok());
        assertArrayEquals(new String[]{"red", "green"}, maquina.configuration());
    }

    @Test
    public void delWheelShouldClampAnOutOfRangePosition(){
        maquina.addWheel(1);
        maquina.delWheel(500);
        assertTrue(maquina.ok());
        assertEquals(0, maquina.configuration().length);
    }

    /**
     * Pruebas para placesymbol
     */
    @Test
    public void placeSymbolShouldSetAnExistingColorOnAWheel(){
        maquina.addSymbol(1, "red");
        maquina.addWheel(1);
        maquina.placeSymbol(1, "red");
        assertTrue(maquina.ok());
        assertEquals("red", maquina.configuration()[0]);
    }

    @Test
    public void placeSymbolShouldNotSetAColorThatDoesNotExist(){
        maquina.addWheel(1);
        maquina.placeSymbol(1, "red");
        assertFalse(maquina.ok());
        assertEquals("none", maquina.configuration()[0]);
    }

    @Test
    public void placeSymbolShouldNotWorkWithoutAnyWheel(){
        maquina.addSymbol(1, "red");
        maquina.placeSymbol(1, "red");
        assertFalse(maquina.ok());
    }

    @Test
    public void placeSymbolShouldNotModifyALockedWheel(){
        maquina.addSymbol(1, "red");
        maquina.addSymbol(2, "blue");
        maquina.addWheel(1);
        maquina.placeSymbol(1, "red");
        maquina.lock(1);
        maquina.placeSymbol(1, "blue");
        assertFalse(maquina.ok());
        assertEquals("red", maquina.configuration()[0]);
    }

    /**
     * Pruebas para spin una wheel solito
     */
    @Test
    public void spinWheelShouldAdvanceToTheNextSymbolInOrder(){
        maquina.addSymbol(1, "red");
        maquina.addSymbol(2, "blue");
        maquina.addWheel(1);
        maquina.placeSymbol(1, "red");
        maquina.spin(1);
        assertTrue(maquina.ok());
        assertEquals("blue", maquina.configuration()[0]);
    }

    @Test
    public void spinWheelShouldWrapAroundToTheFirstSymbol(){
        maquina.addSymbol(1, "red");
        maquina.addSymbol(2, "blue");
        maquina.addWheel(1);
        maquina.placeSymbol(1, "blue");
        maquina.spin(1);
        assertEquals("red", maquina.configuration()[0]);
    }

    @Test
    public void spinWheelShouldNotWorkWithoutSymbols(){
        maquina.addWheel(1);
        maquina.spin(1);
        assertFalse(maquina.ok());
    }

    @Test
    public void spinWheelShouldNotWorkWithoutWheels(){
        maquina.addSymbol(1, "red");
        maquina.spin(1);
        assertFalse(maquina.ok());
    }

    @Test
    public void spinWheelShouldNotModifyALockedWheel(){
        maquina.addSymbol(1, "red");
        maquina.addSymbol(2, "blue");
        maquina.addWheel(1);
        maquina.placeSymbol(1, "red");
        maquina.lock(1);
        maquina.spin(1);
        assertFalse(maquina.ok());
        assertEquals("red", maquina.configuration()[0]);
    }

    /**
     * Pruebas para spin todas las wheels
     */
    @Test
    public void spinAllShouldAdvanceEveryUnlockedWheel(){
        maquina.addSymbol(1, "red");
        maquina.addSymbol(2, "blue");
        maquina.addWheel(1);
        maquina.addWheel(2);
        maquina.placeSymbol(1, "red");
        maquina.placeSymbol(2, "red");
        maquina.spin();
        assertTrue(maquina.ok());
        assertArrayEquals(new String[]{"blue", "blue"}, maquina.configuration());
    }

    @Test
    public void spinAllShouldSkipLockedWheels(){
        maquina.addSymbol(1, "red");
        maquina.addSymbol(2, "blue");
        maquina.addWheel(1);
        maquina.addWheel(2);
        maquina.placeSymbol(1, "red");
        maquina.placeSymbol(2, "red");
        maquina.lock(1);
        maquina.spin();
        assertTrue(maquina.ok());
        assertEquals("red", maquina.configuration()[0]);
        assertEquals("blue", maquina.configuration()[1]);
    }

    @Test
    public void spinAllShouldNotWorkWhenEveryWheelIsLocked(){
        maquina.addSymbol(1, "red");
        maquina.addWheel(1);
        maquina.lock(1);
        maquina.spin();
        assertFalse(maquina.ok());
    }

    @Test
    public void spinAllShouldNotWorkWithoutWheelsOrSymbols(){
        maquina.spin();
        assertFalse(maquina.ok());
    }

    /**
     * Pruebas para spin una whel unos steps especificos
     */
    @Test
    public void spinStepsShouldAdvanceForwardWithPositiveSteps(){
        maquina.addSymbol(1, "red");
        maquina.addSymbol(2, "blue");
        maquina.addSymbol(3, "green");
        maquina.addWheel(1);
        maquina.placeSymbol(1, "red");
        maquina.spin(1, 2);
        assertTrue(maquina.ok());
        assertEquals("green", maquina.configuration()[0]);
    }

    @Test
    public void spinStepsShouldMoveBackwardWithNegativeSteps(){
        maquina.addSymbol(1, "red");
        maquina.addSymbol(2, "blue");
        maquina.addSymbol(3, "green");
        maquina.addWheel(1);
        maquina.placeSymbol(1, "green");
        maquina.spin(1, -1);
        assertTrue(maquina.ok());
        assertEquals("blue", maquina.configuration()[0]);
    }

    @Test
    public void spinStepsShouldNotModifyALockedWheel(){
        maquina.addSymbol(1, "red");
        maquina.addSymbol(2, "blue");
        maquina.addWheel(1);
        maquina.placeSymbol(1, "red");
        maquina.lock(1);
        maquina.spin(1, 3);
        assertFalse(maquina.ok());
        assertEquals("red", maquina.configuration()[0]);
    }

    @Test
    public void spinStepsShouldNotWorkWithoutWheels(){
        maquina.addSymbol(1, "red");
        maquina.spin(1, 2);
        assertFalse(maquina.ok());
    }

    @Test
    public void spinStepsShouldNotWorkWithoutSymbols(){
        maquina.addWheel(1);
        maquina.spin(1, 2);
        assertFalse(maquina.ok());
    }

    /**
     * Pruebas para spin que pone los simbolos
     */
    @Test
    public void spinSetSymbolsShouldSetTheExactGivenConfiguration(){
        maquina.addSymbol(1, "red");
        maquina.addSymbol(2, "blue");
        maquina.addWheel(1);
        maquina.addWheel(2);
        maquina.spin(new String[]{"blue", "red"});
        assertTrue(maquina.ok());
        assertArrayEquals(new String[]{"blue", "red"}, maquina.configuration());
    }

    @Test
    public void spinSetSymbolsShouldNotWorkWithTheWrongLength(){
        maquina.addSymbol(1, "red");
        maquina.addWheel(1);
        maquina.addWheel(2);
        maquina.spin(new String[]{"red"});
        assertFalse(maquina.ok());
    }

    @Test
    public void spinSetSymbolsShouldNotWorkWithAnUnknownColor(){
        maquina.addSymbol(1, "red");
        maquina.addWheel(1);
        maquina.spin(new String[]{"purple"});
        assertFalse(maquina.ok());
        assertEquals("none", maquina.configuration()[0]);
    }

    @Test
    public void spinSetSymbolsShouldNotModifyALockedWheel(){
        maquina.addSymbol(1, "red");
        maquina.addSymbol(2, "blue");
        maquina.addWheel(1);
        maquina.placeSymbol(1, "red");
        maquina.lock(1);
        maquina.spin(new String[]{"blue"});
        assertTrue(maquina.ok());
        assertEquals("red", maquina.configuration()[0]);
    }

    /**
     * Pruebas para spin rangdom general
     */
    @Test
    public void spinRandomWheelShouldPickAColorFromTheRepertoire(){
        maquina.addSymbol(1, "red");
        maquina.addSymbol(2, "blue");
        maquina.addWheel(1);
        maquina.spin(1);
        assertTrue(maquina.ok());
        String resultado = maquina.configuration()[0];
        assertTrue(resultado.equals("red") || resultado.equals("blue"));
    }

    @Test
    public void spinRandomWheelShouldNotWorkWithoutSymbols(){
        maquina.addWheel(1);
        maquina.spin(1);
        assertFalse(maquina.ok());
    }

    @Test
    public void spinRandomWheelShouldNotModifyALockedWheel(){
        maquina.addSymbol(1, "red");
        maquina.addSymbol(2, "blue");
        maquina.addWheel(1);
        maquina.placeSymbol(1, "red");
        maquina.lock(1);
        maquina.spin(1);
        assertFalse(maquina.ok());
        assertEquals("red", maquina.configuration()[0]);
    }

    @Test
    public void spinRandomAllShouldSkipLockedWheels(){
        maquina.addSymbol(1, "red");
        maquina.addWheel(1);
        maquina.addWheel(2);
        maquina.placeSymbol(1, "red");
        maquina.placeSymbol(2, "red");
        maquina.lock(1);
        maquina.spin();
        assertTrue(maquina.ok());
        assertEquals("red", maquina.configuration()[0]);
    }

    @Test
    public void spinRandomAllShouldNotWorkWithoutWheelsOrSymbols(){
        maquina.spin();
        assertFalse(maquina.ok());
    }

    /**
     * Pruebas para swap
     */

    @Test
    public void swapShouldExchangeColorsBetweenTwoWheels(){
        maquina.addSymbol(1, "red");
        maquina.addSymbol(2, "blue");
        maquina.addWheel(1);
        maquina.addWheel(2);
        maquina.placeSymbol(1, "red");
        maquina.placeSymbol(2, "blue");
        maquina.swap(1, 2);
        assertTrue(maquina.ok());
        assertArrayEquals(new String[]{"blue", "red"}, maquina.configuration());
    }

    @Test
    public void swapShouldExchangeTheLockedStateToo(){
        maquina.addSymbol(1, "red");
        maquina.addSymbol(2, "blue");
        maquina.addWheel(1);
        maquina.addWheel(2);
        maquina.placeSymbol(1, "red");
        maquina.placeSymbol(2, "blue");
        maquina.lock(1);
        maquina.swap(1, 2);

        maquina.spin(1);
        assertTrue("la posicion 1 ya no deberia estar fija", maquina.ok());

        maquina.spin(2);
        assertFalse("la posicion 2 ahora deberia estar fija", maquina.ok());
    }

    @Test
    public void swapShouldNotWorkWithLessThanTwoWheels(){
        maquina.addWheel(1);
        maquina.swap(1, 1);
        assertFalse(maquina.ok());
    }

    @Test
    public void swapShouldClampOutOfRangePositions(){
        maquina.addSymbol(1, "red");
        maquina.addSymbol(2, "blue");
        maquina.addWheel(1);
        maquina.addWheel(2);
        maquina.placeSymbol(1, "red");
        maquina.placeSymbol(2, "blue");
        maquina.swap(-5, 500);
        assertTrue(maquina.ok());
        assertArrayEquals(new String[]{"blue", "red"}, maquina.configuration());
    }

    /**
     * Pruebas para bloquear y desbloquear
     */
    @Test
    public void lockShouldPreventAWheelFromSpinning(){
        maquina.addSymbol(1, "red");
        maquina.addSymbol(2, "blue");
        maquina.addWheel(1);
        maquina.placeSymbol(1, "red");
        maquina.lock(1);
        assertTrue(maquina.ok());
        maquina.spin(1);
        assertFalse(maquina.ok());
        assertEquals("red", maquina.configuration()[0]);
    }

    @Test
    public void lockShouldNotWorkWithoutAnyWheel(){
        maquina.lock(1);
        assertFalse(maquina.ok());
    }

    @Test
    public void unlockShouldAllowTheWheelToSpinAgain(){
        maquina.addSymbol(1, "red");
        maquina.addSymbol(2, "blue");
        maquina.addWheel(1);
        maquina.placeSymbol(1, "red");
        maquina.lock(1);
        maquina.unlock(1);
        assertTrue(maquina.ok());
        maquina.spin(1);
        assertTrue(maquina.ok());
        assertEquals("blue", maquina.configuration()[0]);
    }

    @Test
    public void unlockShouldNotWorkWithoutAnyWheel(){
        maquina.unlock(1);
        assertFalse(maquina.ok());
    }

    /** 
     * Pruebas generales de
     * symbols / distin ctSymbols / configuration / isJackpot
     */

    @Test
    public void symbolsShouldReturnColorsInInsertionOrder(){
        maquina.addSymbol(1, "red");
        maquina.addSymbol(2, "blue");
        maquina.addSymbol(1, "green");
        assertArrayEquals(new String[]{"green", "red", "blue"}, maquina.symbols());
    }

    @Test
    public void symbolsShouldNotIncludeColorsThatWereNeverAdded(){
        maquina.addSymbol(1, "red");
        String[] result = maquina.symbols();
        for(String s : result){
            assertFalse("blue".equals(s));
        }
    }

    @Test
    public void distinctSymbolsShouldCountUniqueVisibleColors(){
        maquina.addSymbol(1, "red");
        maquina.addSymbol(2, "blue");
        maquina.addWheel(1);
        maquina.addWheel(2);
        maquina.placeSymbol(1, "red");
        maquina.placeSymbol(2, "red");
        assertEquals(1, maquina.distinctSymbols());
        maquina.placeSymbol(2, "blue");
        assertEquals(2, maquina.distinctSymbols());
    }

    @Test
    public void distinctSymbolsShouldNotCountWheelsWithoutSymbol(){
        maquina.addWheel(1);
        maquina.addWheel(2);
        assertEquals(0, maquina.distinctSymbols());
    }

    @Test
    public void configurationShouldReportNoneForWheelsWithoutSymbol(){
        maquina.addWheel(1);
        assertArrayEquals(new String[]{"none"}, maquina.configuration());
    }

    @Test
    public void configurationShouldNotIncludeMoreEntriesThanExistingWheels(){
        maquina.addWheel(1);
        maquina.addWheel(2);
        assertEquals(2, maquina.configuration().length);
    }

    @Test
    public void isJackpotShouldBeTrueWhenAllWheelsMatch(){
        maquina.addSymbol(1, "red");
        maquina.addWheel(1);
        maquina.addWheel(2);
        maquina.placeSymbol(1, "red");
        maquina.placeSymbol(2, "red");
        assertTrue(maquina.isJackpot());
    }

    @Test
    public void isJackpotShouldNotBeTrueWhenWheelsDiffer(){
        maquina.addSymbol(1, "red");
        maquina.addSymbol(2, "blue");
        maquina.addWheel(1);
        maquina.addWheel(2);
        maquina.placeSymbol(1, "red");
        maquina.placeSymbol(2, "blue");
        assertFalse(maquina.isJackpot());
    }

    @Test
    public void isJackpotShouldNotBeTrueWithoutAnyWheel(){
        assertFalse(maquina.isJackpot());
    }

    @Test
    public void isJackpotShouldNotBeTrueWhenSomeWheelHasNoSymbol(){
        maquina.addSymbol(1, "red");
        maquina.addWheel(1);
        maquina.addWheel(2);
        maquina.placeSymbol(1, "red");
        assertFalse(maquina.isJackpot());
    }

    /**
     * Pruebas de makeinvisible y okl
     */

    @Test
    public void makeInvisibleShouldSucceedEvenIfNeverShown(){
        maquina.addSymbol(1, "red");
        maquina.addWheel(1);
        maquina.makeInvisible();
        assertTrue(maquina.ok());
        assertEquals("none", maquina.configuration()[0]);
    }

    @Test
    public void okShouldReflectTheResultOfTheLastOperation(){
        maquina.addSymbol(1, "red");
        assertTrue(maquina.ok());
        maquina.addSymbol(1, "red");
        assertFalse(maquina.ok());
        maquina.addSymbol(2, "blue");
        assertTrue(maquina.ok());
    }
}