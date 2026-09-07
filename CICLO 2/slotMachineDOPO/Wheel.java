/**
 * Clase de las ruedas de la SlotMAchine
 * Las ruedas son un circulo que muestra simbolos (colors)
 * Por defecto si no tiene un simbolo es de color gris claro y a nivel logico es null 
 * 
 * @author Bryan Pinzón y Valentina Prieto
 * @version Ciclo 1
 */
public class Wheel{
    // Definimos elColor predeterminado cuando no tiene simbolos
    private static final String EMPTY_LOOK = "lightgray";
    
    private Circle symbolShape;
    private String currentColor;
    private int xPosition;
    private int yPosition;
    private boolean locked;
 
    /**
     * Constructor
     * Crea una rueda vacia sin simbolo asignado en una posicion por defecto
     * que despues reposicionaremos
     */
    public Wheel(){
        symbolShape = new Circle();
        symbolShape.changeSize(30);
        xPosition = 20;
        yPosition = 15;
        currentColor = null;
        locked = false;
    }
 
    /**
     * Metodo que ubica la rueda en una poscicion del canvas
     * 
     * @param x que es un entero que es la posicion horizontal, en pixeles
     * @param y que es un entero que es la posicion vertical, en pixeles
     */
    public void reposition(int x, int y){
        symbolShape.moveHorizontal(x - xPosition);
        symbolShape.moveVertical(y - yPosition);
        xPosition = x;
        yPosition = y;
    }
 
    /**
     * Metodo que cambia el color (simbolo) de la rueda
     * 
     * @param colorName que es un String que es el nombre del color CSS a mostrar o puede 
     * ser null si la rueda debe quedar sin simbolo
     */
    public void setColor(String colorName){
        currentColor = colorName;
        symbolShape.changeColor(colorName == null ? EMPTY_LOOK : colorName);
    }
 
    /**
     * Metodo que nos dice el color (simbolo) actual de la rueda
     * 
     * @return un String que es el nombre del color, o null si no tiene simbolo
     */
    public String getColor(){
        return currentColor;
    }
 
    /**
     * Metodo que hace visible la rueda en el canvas
     */
    public void show(){
        symbolShape.makeVisible();
    }
 
    /**
     * Metodo que oculta la rueda del canvas
     */
    public void hide(){
        symbolShape.makeInvisible();
    }
    
    /**
     * Metodo que fija (lock = true) o suelta (unlock = false) esta rueda
     * Una rueda fija no debe ser modificada por los metodos de giro (spin) ni
     * por placeSymbol
     *
     * @param locked true para fijar la rueda, false para soltarla
     */
    public void setLocked(boolean locked){
        this.locked = locked;
    }
 
    /**
     * Metodo que nos dice si esta rueda esta fija (lock) actualmente
     *
     * @return true si la rueda esta fija
     */
    public boolean isLocked(){
        return locked;
    }
}