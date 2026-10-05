import java.util.Random;

/**
 * Clase que representa la rueda crazy diseñada por nosotros y hereda de wheel
 * cumple con: 
 *              - funciona exactamente como una rueda normal
 *              - no se alinea con las demas en la fila: se ubica en un lugar al azar dentro 
 *              del cuerpo de la maquina
 *              - Esa posicion se escoge una sola vez (al inicio) despues se queda quieta ahi
 *
 * @author Bryan Pinzón y Valentina Prieto
 * @version Ciclo 4
 */
public class CrazyWheel extends Wheel{

    private boolean yaUbicada;

    /**
     * Constructor
     * Crea una rueda crazy
     */
    public CrazyWheel(){
        super();
        yaUbicada = false;
    }

    /**
     * MEtodo que nos dice si va en la disposicion no no 
     * Esta rueda no respeta la disposicion
     */
    @Override
    public boolean respetaLayout(){
        return false;
    }

    /**
     * Metodo que hace la reposicion especial de esta rueda
     */
    @Override
    public void reposicionEspecial(int xMin, int xMax, int yMin, int yMax, Random azar){
        if(yaUbicada){
            return;
        }
        int anchoDisponible = Math.max(1, xMax - xMin);
        int altoDisponible = Math.max(1, yMax - yMin);
        int x = xMin + azar.nextInt(anchoDisponible);
        int y = yMin + azar.nextInt(altoDisponible);
        reposition(x, y);
        yaUbicada = true;
    }
}