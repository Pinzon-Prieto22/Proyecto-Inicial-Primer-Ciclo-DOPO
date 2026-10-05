import java.util.ArrayList;
import java.util.Random;

/**
 * Clase de las ruedas de la SlotMachine 
 * esta es la rueda "normal" y de esta heredam: 
 *                                          - LeftyWheel
 *                                          - RebelWheel
 *                                          - CrazyWheel
 * Las ruedas son un circulo que muestra simbolos (colores)
 * Por defecto si no tiene un simbolo es de color gris claro y a nivel logico es null 
 * 
 * @author Bryan Pinzón y Valentina Prieto
 * @version Ciclo 4
 */
public class Wheel{
    // Definimos elColor predeterminado cuando no tiene simbolos
    private static final String EMPTY_LOOK = "lightgray";
    // Tamaño "de fabrica" del circulo que representa el simbolo
    private static final int SIZE_NORMAL = 30;

    private Circle symbolShape;
    private Symbol currentSymbol;
    private int xPosition;
    private int yPosition;
    private boolean locked;
    // true si un simbolo (ShySymbol) pidio que esta rueda
    // se mantenga oculta aunque la maquina este visible
    private boolean ocultoPorSimbolo;

    /**
     * Constructor
     * Crea una rueda vacia sin simbolo asignado en una posicion por defecto
     * que despues reposicionaremos
     */
    public Wheel(){
        symbolShape = new Circle();
        symbolShape.changeSize(SIZE_NORMAL);
        xPosition = 20;
        yPosition = 15;
        currentSymbol = null;
        locked = false;
        ocultoPorSimbolo = false;
    }

    /**
     * Metodo que ubica la rueda en una poscicion del canvas
     * 
     * @param x que es un entero que es la posicion horizontal en px
     * @param y que es un entero que es la posicion vertical en px
     */
    public void reposition(int x, int y){
        symbolShape.moveHorizontal(x - xPosition);
        symbolShape.moveVertical(y - yPosition);
        xPosition = x;
        yPosition = y;
    }

    /**
     * MEtodo que cambia el simbolo que muestra la rueda
     * se restituye a sus dimensiones de fabrica para que los simbolos especiales
     * no nos afecrten al siguiente simbolo que se asignara
     * 
     * @param symbol que es el Symbol a mostrat o null si la rueda debe quedar sin simbolo
     */
    public void setColor(Symbol symbol){
        currentSymbol = symbol;
        ocultoPorSimbolo = false;
        symbolShape.changeSize(SIZE_NORMAL);
        symbolShape.changeColor(symbol == null ? EMPTY_LOOK : symbol.getColor());
        if(symbol != null){
            symbol.alSerMostrado(this);
        }
    }

    /**
     * Metodo que nos dice el color del simbolo actual de la rueda
     * 
     * @return un String que es el nombre del color, o null si no tiene simbolo
     */
    public String getColor(){
        return currentSymbol == null ? null : currentSymbol.getColor();
    }

    /**
     * Metodo que nos da el Symbol que la rueda muestra actualmente
     *
     * @return el Symbol actual de la rueda, o null si no tiene ninguno
     */
    public Symbol getSymbol(){
        return currentSymbol;
    }

    /**
     * Metodo que hace visible la rueda en el canvas
     */
    public void show(){
        if(!ocultoPorSimbolo){
            symbolShape.makeVisible();
        }
    }

    /**
     * Metodo que oculta la rueda del canvas
     */
    public void hide(){
        symbolShape.makeInvisible();
    }

    /**
     * Metodo que un simbolo especial (ShySymbol) usa para esconder
     * esta rueda aunque la maquina siga vidible
     */
    public void ocultarPorSimbolo(){
        ocultoPorSimbolo = true;
        symbolShape.makeInvisible();
    }

    /**
     * Metodo que un simbolo especial (ShySymbol) usa para avisar
     * que esta rueda ya puede volver a mostrarse la restituye a su estado "normal"
     * solo cambiamos el estado
     */
    public void mostrarPorSimbolo(){
        ocultoPorSimbolo = false;
    }

    /**
     * Metodo que un simbolo especial (EphemeralSymbol) usa para
     * cambiar el tamaño del circulo que representa el simbolo
     *
     * @param nuevoSize que es un entero que es el nuevo diametro, en pxeles
     */
    public void cambiarSizeSimbolo(int nuevoSize){
        symbolShape.changeSize(nuevoSize);
    }

    /**
     * Metodo que fija (lock = true) o suelta (unlock = false) la rueda
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

    // aca empiezan mrtodos que nos ayudan con los comportamientos de cada rueda
    
    /**
     * Metodo que nos dice si esta rueda se ubica en la fila normal de
     * la maquina (true) o si se ubica de otra forma (false) -> CrazyWheel
     *
     * @return true si la rueda respeta el layout normal en fila
     */
    public boolean respetaLayout(){
        return true;
    }

    /**
     * Metodo que nos dice si esta rueda se puede fijar (lock)
     *
     * @return true si la rueda se puede fijar
     */
    public boolean puedeFijarse(){
        return true;
    }

    /**
     * Metodo que nos dice si esta rueda se puede intercambiar (swap)
     *
     * @return true si la rueda se puede intercambiar
     */
    public boolean puedeIntercambiarse(){
        return true;
    }

    /**
     * Metodo que nos dice si esta rueda se puede eliminar (delWheel)
     *
     * @return true si la rueda se puede eliminar
     */
    public boolean puedeEliminarse(){
        return true;
    }

    /**
     * Metodo que calcula cual deberia ser el siguiente simbolo de esta
     * rueda al girar secuencialmente, consiferando el repertorio completo, la
     * direccion (1 = siguiente, -1 = anterior) y la rueda a su
     * izquierda  (LEftyWheel) (o null si no tiene)
     * Una rueda normal simplemente avanza/retrocede en el repertorio, de forma circular
     *
     * @param repertorio que es el ArrayList con todos los simbolos de la maquina
     * @param direccion que es 1 para avanzar o -1 para retroceder
     * @param izquierda que es la rueda inmediatamente a la izquierda de esta, o null
     * @return el Symbol que le corresponde a esta rueda
     */
    public Symbol siguienteSimbolo(ArrayList<Symbol> repertorio, int direccion, Wheel izquierda){
        int total = repertorio.size();
        int indice = (currentSymbol == null) ? -1 : repertorio.indexOf(currentSymbol);
        int siguiente = ((indice + direccion) % total + total) % total;
        return repertorio.get(siguiente);
    }

    /**
     * Metodo que calcula un simbolo al azar para ls rueda, dado el
     * repertorio completo, el generador de numeros aleatorios y la
     * rueda a su izquierda (o null si no tiene)
     * Una rueda normal simplemente escoge un simbolo al azar del repertorio
     *
     * @param repertorio que es el ArrayList con todos los simbolos de la maquina
     * @param azar que es el generador de numeros aleatorios a usar
     * @param izquierda que es la rueda inmediatamente a la izquierda de esta, o null
     * @return el Symbol escogido para esta rueda
     */
    public Symbol simboloAlAzar(ArrayList<Symbol> repertorio, Random azar, Wheel izquierda){
        return repertorio.get(azar.nextInt(repertorio.size()));
    }

    /**
     * Metodo que ubica la rueda de forma especial (CrazyWheel), fuera del layout
     * normal en fila
     * Una rueda normal no hace nada aqui
     *
     * @param xMin que es un entero que es el limite izquierdo disponible
     * @param xMax que es un entero que es el limite derecho disponible
     * @param yMin que es un entero que es el limite superior disponible
     * @param yMax que es un entero que es el limite inferior disponible
     * @param azar que es el generador de numeros aleatorios a usar
     */
    public void reposicionEspecial(int xMin, int xMax, int yMin, int yMax, Random azar){
        // una rueda normal no hace nada aqui, se ubica con reposition()
    }
}