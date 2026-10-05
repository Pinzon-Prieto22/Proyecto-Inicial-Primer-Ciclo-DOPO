/**
 * Clase que representa al simbolo Ephemeral y hereda de Symbol
 * cumple con: 
 *              - cada vez que queda asignado a una rueda su tamaño se va encogiendo,
 *              hasta quedar como un punto Una vez llega al tamaño minimo, 
 *              se queda asi (no sigue encogiendo)
 *
 * @author Bryan Pinzón y Valentina Prieto
 * @version Ciclo 4
 */
public class EphemeralSymbol extends Symbol{

    private static final int SIZE_INICIAL = 30;
    private static final int SIZE_MINIMO = 4; // que quede ccomo un punto
    private static final int DECREMENTO = 4;

    private int sizeActual;

    /**
     * Constructor
     * Crea un simbolo ephemeral con el color dado, en su tamaño inicial
     *
     * @param color que es un String que es el color CSS de este simbolo
     */
    public EphemeralSymbol(String color){
        super(color);
        sizeActual = SIZE_INICIAL;
    }

    /**
     * Metodo que asigna el simbolo a la wheel
     */
    @Override
    public void alSerMostrado(Wheel rueda){
        if(sizeActual > SIZE_MINIMO){
            sizeActual = Math.max(SIZE_MINIMO, sizeActual - DECREMENTO);
        }
        rueda.cambiarSizeSimbolo(sizeActual);
    }
}