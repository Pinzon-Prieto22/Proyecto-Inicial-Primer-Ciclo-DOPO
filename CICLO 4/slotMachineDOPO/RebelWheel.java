/**
 * Clase que representa la rueda rebel y hereda de wheel
 * cumple con: 
 *              - funciona como una rueda normal, pero
 *              - No deja:
 *                      - fijar (lock)
 *                      - intercambiar (swap)
 *                      - eliminar (delWheel)
 *              - para identificarla tiene un rectangulo rojo detras del circulo como una placa
 *
 * @author Bryan Pinzón y Valentina Prieto
 * @version Ciclo 4
 */
public class RebelWheel extends Wheel{

    private Rectangle placa;
    private int placaX = 70; // posicion por defecto de Rectangle
    private int placaY = 15;

    /**
     * Constructor
     * Crea una rueda rebel, con su rectangulo rojo
     */
    public RebelWheel(){
        super();
        placa = new Rectangle();
        placa.changeSize(38, 38);
        placa.changeColor("darkred");
    }

    /**
     * Metodo que ubica la rueda y su distintivo
     */
    @Override
    public void reposition(int x, int y){
        super.reposition(x, y);
        int objetivoX = x - 4;
        int objetivoY = y - 4;
        placa.moveHorizontal(objetivoX - placaX);
        placa.moveVertical(objetivoY - placaY);
        placaX = objetivoX;
        placaY = objetivoY;
    }

    /**
     * Metodo que muestra la rueda
     */
    @Override
    public void show(){
        // La placa se muestra primero para que quede detras del circulo
        placa.makeVisible();
        super.show();
    }

    /**
     * etodo que esconde la rueda
     */
    @Override
    public void hide(){
        super.hide();
        placa.makeInvisible();
    }

    /**
     * Metodo que nos dice si se puede fijar (esta no se puede fijar)
     */
    @Override
    public boolean puedeFijarse(){
        return false;
    }

    /**
     * MEtodo que nos dice si se puede intercambuiar (esta no se puede intercambiar)
     */
    @Override
    public boolean puedeIntercambiarse(){
        return false;
    }

    /**
     * Metodo que nos dice si se puede elimiinar (esta no se puede eliminar)
     */
    @Override
    public boolean puedeEliminarse(){
        return false;
    }
}