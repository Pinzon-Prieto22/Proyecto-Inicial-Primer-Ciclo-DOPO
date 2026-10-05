import java.util.ArrayList;
import java.util.Random;

/**
 * Clase que representa la rueda Lefty y hereda de wheel
 * cumple con:
 *              - en vez de avanzar en el repertorio o escoger un simbolo al azar al girar, 
 *              copia el simbolo que tiene actualmente la rueda inmediatamente a su izquierda
 *              Si no tiene ninguna rueda a su izquierda (esta en la posicion 1), 
 *              gira como una rueda normal(mejor esfuerzo)
 *              - Para identificarla tiene una marca triangular magenta
 *
 * @author Bryan Pinzón y Valentina Prieto
 * @version Ciclo 4
 */
public class LeftyWheel extends Wheel{

    private Triangle marca;
    private int marcaX = 140; // posicion por defecto de Triangle
    private int marcaY = 15;

    /**
     * Constructor
     * Crea una rueda lefty, con su marquita magenta
     */
    public LeftyWheel(){
        super();
        marca = new Triangle(10, 10, "magenta");
    }

    /**
     * Metodo que ubica la rueba y su distintivo
     */
    @Override
    public void reposition(int x, int y){
        super.reposition(x, y);
        int objetivoX = x - 14;
        int objetivoY = y + 10;
        marca.moveHorizontal(objetivoX - marcaX +10);
        marca.moveVertical(objetivoY - marcaY -10);
        marcaX = objetivoX;
        marcaY = objetivoY;
    }

    /**
     * Metodo que muestra la rueda
     */
    @Override
    public void show(){
        super.show();
        marca.makeVisible();
    }

    /**
     * Metodo que oculta la rueda
     */
    @Override
    public void hide(){
        super.hide();
        marca.makeInvisible();
    }

    /**
     * Metodo que calcula su siguiente simbolo
     */
    @Override
    public Symbol siguienteSimbolo(ArrayList<Symbol> repertorio, int direccion, Wheel izquierda){
        if(izquierda == null){
            return super.siguienteSimbolo(repertorio, direccion, izquierda);
        }
        return izquierda.getSymbol();
    }

    /**
     * Metodo que da un simbolo al azar
     */
    @Override
    public Symbol simboloAlAzar(ArrayList<Symbol> repertorio, Random azar, Wheel izquierda){
        if(izquierda == null){
            return super.simboloAlAzar(repertorio, azar, izquierda);
        }
        return izquierda.getSymbol();
    }
}