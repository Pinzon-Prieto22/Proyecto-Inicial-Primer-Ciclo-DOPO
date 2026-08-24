import java.util.ArrayList;
import javax.swing.JOptionPane;
import java.util.Random;
 /**
  * Clase que crea la maquina (simula)
  * CICLO 1. Permite:
  *                 - Crear una maquina vacia 
  *                 - Agregar o quitar ruedas
  *                 - Agregar o quitar simblolos (representados por colores)
  *                 - Girar una o todas las ruedas
  *                 - Consultar los simbolos de las ruedas
  *                 - Verificar si se gana
  *                 - Hacer visible o invisinble la maquina
  *                 - Salir del simulador
  *
  * @author Bryan Pinzón y Valentina Prieto
  * @version Ciclo 1
  */
public class SlotMachine{
    // Definimos el tamaño del canvas
    private static final int CANVAS_WIDTH = 460;
    private static final int CANVAS_HEIGHT = 220;
    // Definimos el tamaño de la maquina
    private static final int BODY_WIDTH = 400;
    private static final int BODY_HEIGHT = 160;
    // Definimos las posiciones centradas de la maquina
    private static final int BODY_X = (CANVAS_WIDTH - BODY_WIDTH) / 2;
    private static final int BODY_Y = (CANVAS_HEIGHT - BODY_HEIGHT) / 2;
 
    
    // DEfinimos el margen de la rueda a la maquina
    private static final int WHEEL_MARGIN = 30;
    // definimos la separacion entre ruedas
    private static final int WHEEL_SEPARATION = 45;
    // Definimos la posicion de la fila de las ruedas
    private static final int WHEEL_BASE_X = BODY_X + WHEEL_MARGIN;
    private static final int WHEEL_BASE_Y = BODY_Y + 65;
    // Definimos la posicion de la banderita de ganar
    private static final int FLAG_X = BODY_X + BODY_WIDTH / 2 - 15;
    private static final int FLAG_Y = BODY_Y + 15;
 
    // Calculamos el maximo de ruedas que caben en nuestra maquina
    private static final int MAX_WHEELS = (BODY_WIDTH - 2 * WHEEL_MARGIN) / WHEEL_SEPARATION + 1;
 
    // Donde guardamos los simbolos (colores)
    private ArrayList<String> symbols;
    // Donde guardamos las ruedas (circulos)
    private Wheel[] wheels;
    // El cuerpo de la maquina (rectangulo)
    private Rectangle body;
    // La banderita de victoria (triangulo)
    private Triangle jackpotFlag;
    
    private Random azar;
    private boolean visible;
    private boolean ok;
 
    /**
     * Constructor
     * Crea una SlotMachine vacia y por defecto inicia siendo ivisisble
     */
    public SlotMachine(){
        symbols = new ArrayList<String>();
        wheels = new Wheel[MAX_WHEELS];
        
        // Creamos el cuerpo de la maquina
        body = new Rectangle();
        // Le ponemos sus dimensiones
        body.changeSize(BODY_HEIGHT, BODY_WIDTH);
        // Lo ubicamos centrado
        body.moveHorizontal(BODY_X - 70);
        body.moveVertical(BODY_Y - 15);
        // Le ponemos el color que queremos
        body.changeColor("steelblue");
        // Creamos la banderita
        jackpotFlag = new Triangle();
        // Le damos dimendsiones
        jackpotFlag.changeSize(20, 30);
        // La ubicamos centrada
        jackpotFlag.moveHorizontal(FLAG_X - 140);
        jackpotFlag.moveVertical(FLAG_Y - 15);
        // Le ponemos el color que queremos
        jackpotFlag.changeColor("gold");
        
        azar = new Random();
        visible = false;
        ok = true;
    }
 
    /**
     * Metodo para agregar una  nueva rueda en una posicion
     * Si ya se creo una rueda en esa posicion no la crea y avisa
     * Si ya no caben mas ruedas en la maquina no la cera y avisa
     * 
     * @param pos que es entero y es la posicion en la que se quiere agregar la rueda
     */
    public void addWheel(int pos){
        // Si ya hay un maximo de ruedas no la crea
        if(wheelCount() >= MAX_WHEELS && pos > MAX_WHEELS){
            fail("La maquina ya tiene el maximo de " + MAX_WHEELS + " ruedas: no hay espacio para mas.");
            return;
        }
        // Ajustamos la posicion si es negativa o mayor al mayor
        int p = ajustaPos(pos, 1, MAX_WHEELS);
        // Si ya hay una rueda en la posicon no la crea
        if(wheels[p - 1] != null){
            fail("Ya hay una rueda en la posicion " + p + ". Selecciona una posicion diferente");
            return;
        }
        // Creamos la rueda
        Wheel w = new Wheel();
        // La ubicamos
        w.reposition(WHEEL_BASE_X + (p - 1) * WHEEL_SEPARATION, WHEEL_BASE_Y);
        w.setColor(null);
        // lo ponemos en nuestra lista
        wheels[p - 1] = w;
        refreshVisuals();
        succeed();
    }
 
    /**
     * Metodo para eliminar la rueda
     * Si no hay una rueda en esa posicion avisa
     * 
     * @param pos que es entero y es la posicion de la rueda que se quiere quitar
     */
    public void delWheel(int pos){
        int p = ajustaPos(pos, 1, MAX_WHEELS);
        // Si no hay una rueda avbisa
        if(wheels[p - 1] == null){
            fail("No hay una rueda en la posicion " + p + ".");
            return;
        }
        // la quita
        wheels[p - 1].hide();
        wheels[p - 1] = null;
        succeed();
    }
 
    /**
     * Metodo que agrega un nuevo simbolo (color en formato css) en la posicion dada
     * 
     * black, white, red, green, blue, yellow, magenta, cyan, gray, grey, orange, 
     * purple, pink, brown, lime, navy, teal, olive, maroon, silver, gold, coral, salmon, 
     * turquoise, violet, indigo, khaki, plum, orchid, tan, beige, ivory, crimson, chocolate, 
     * chartreuse, azure, lavender, skyblue, steelblue, tomato, wheat, orangered, hotpink, 
     * darkgreen, darkblue, darkred, lightblue, lightgreen, lightgray, lightgrey
     * 
     * @param pos que es entero y es la posicion del simbolo
     * @param color que es un String y debe ser de los colores validos
     */
    public void addSymbol(int pos, String color){
        // Si no es valido no lo agrefa
        if(!CSSColor.esValido(color)){
            fail("'" + color + "' no es un color CSS valido.");
            return;
        }
        String key = color.toLowerCase();
        // Si ya existe avisa
        if(symbols.contains(key)){
            fail("El color '" + key + "' ya existe en la maquina.");
            return;
        }
        int p = ajustaPos(pos, 1, symbols.size() + 1);
        // Lo agregamos
        symbols.add(p - 1, key);
        succeed();
    }
 
    /**
     * Metodo que elimina un simbolo 
     * 
     * @param symbol que es un Strign que es ek nombre del color CSS que se quiere eliminar
     */
    public void delSymbol(String symbol){
        String key = symbol == null ? null : symbol.toLowerCase();
        if(key == null || !symbols.contains(key)){
            fail("El color '" + symbol + "' no existe en la maquina.");
            return;
        }
        symbols.remove(key);
        reassignWheelsShowing(key);
        refreshVisuals();
        succeed();
    }
 
    /**
     * Metodo que coloca un simbolo especifico (color) en una rueda especifica
     * 
     * @param wheel que es un entero que es la posicion de la rueda
     * @param symbol que es un Srting que es el nombre del color CSS del simbolo a colocar
     */
    public void placeSymbol(int wheel, String symbol){
        // revisamos que exista la rueda
        int p = ajustaPos(wheel, 1, MAX_WHEELS);
        if(wheels[p - 1] == null){
            fail("No hay una rueda en la posicion " + p + ".");
            return;
        }
        String key = symbol == null ? null : symbol.toLowerCase();
        // verificamos que el simbolo existe
        if(key == null || !symbols.contains(key)){
            fail("El color '" + symbol + "' no existe en la maquina.");
            return;
        }
        // Le ponemos el color a la rueda
        wheels[p - 1].setColor(key);
        refreshVisuals();
        succeed();
    }
 
    /**
     * Metodo que gira una rueda especifica una vez y muestra el siguiente simbolo
     * 
     * @param wheel que es un entero que es la posicion de la rueda que se quiere girar
     */
    public void spin(int wheel){
        if(symbols.isEmpty()){
            fail("No es posible girar: no hay simbolos definidos.");
            return;
        }
        int p = ajustaPos(wheel, 1, MAX_WHEELS);
        if(wheels[p - 1] == null){
            fail("No hay una rueda en la posicion " + p + ".");
            return;
        }
        spinOne(wheels[p - 1]);
        refreshVisuals();
        succeed();
    }
 
    /**
     * metodo que gira todas las ruedas existentes de la maquina al siguiente simbolo en la lista
     */
    public void spin(){
        if(wheelCount() == 0 || symbols.isEmpty()){
            fail("No es posible girar: faltan ruedas o simbolos.");
            return;
        }
        for(Wheel w : wheels){
            if(w != null){
                spinOne(w);
            }
        }
        refreshVisuals();
        succeed();
    }
    
    /**
     * Metodo que gira una sola rueda al azar (no el simbolo siguiente)
     * 
     * @param wheel que es un entero qu es la posicion de la rueda que se quiere cambiar
     */
    public void spinRandom(int wheel){
        // si no hay simbolos
        if(symbols.isEmpty()){
            fail("No es posible girar: no hay simbolos definidos.");
            return;
        }
        int p = ajustaPos(wheel, 1, MAX_WHEELS);
        // si no hay rueda
        if(wheels[p - 1] == null){
            fail("No hay una rueda en la posicion " + p + ".");
            return;
        }
        spinOneRandom(wheels[p - 1]);
        refreshVisuals();
        succeed();
    }
 
    /**
     * Metodo que gira todas las ruedas de la maquina, pero al azar cada una
     */
    public void spinRandom(){
        // si no hay simbolos
        if(wheelCount() == 0 || symbols.isEmpty()){
            fail("No es posible girar: faltan ruedas o simbolos.");
            return;
        }
        for(Wheel w : wheels){
            if(w != null){
                spinOneRandom(w);
            }
        }
        refreshVisuals();
        succeed();
    }
 
    /**
     * Metodo que nos dice los simbolos que tiene la maquina en el orden que 
     * se feuron definiendo
     * 
     * @return un arreglo con los simbolos (colores)
     */
    public String[] symbols(){
        return symbols.toArray(new String[0]);
    }
 
    /**
     * Metodo que cuenta cuantos colores hay en la maquina eb ek momento
     * 
     * @return un entero que es elnumero de simbolos distintos actualmente visibles
     */
    public int distinctSymbols(){
        ArrayList<String> vistos = new ArrayList<String>();
        for(Wheel w : wheels){
            if(w == null){
                continue;
            }
            String c = w.getColor();
            if(c != null && !vistos.contains(c)){
                vistos.add(c);
            }
        }
        return vistos.size();
    }
 
    /**
     * Metodo que nos da los colores de cada una de las ruedas existentes
     * de la maquina ordenados de izqiuerda a derecha
     * Si una rueda no tiene simbolo dice none
     * 
     * @return un arreglo con la configuracion visible de la maquina
     */
    public String[] configuration(){
        ArrayList<String> result = new ArrayList<String>();
        for(Wheel w : wheels){
            if(w == null){
                continue;
            }
            String c = w.getColor();
            result.add(c == null ? "none" : c);
        }
        return result.toArray(new String[0]);
    }
 
    /**
     * MEtodo que nos dice si la configuracion actual de la maquina es valida para GANAR
     * Para ganar todas las ruedas deben tener el mismo simbolo
     * 
     * @return un booleano que es true si la maquina esta en estado de jackpot (GANAR)
     */
    public boolean isJackpot(){
        String primero = null;
        for(Wheel w : wheels){
            if(w == null){
                continue;
            }
            String c = w.getColor();
            if(c == null){
                return false;
            }
            if(primero == null){
                primero = c;
            } else if(!primero.equals(c)){
                return false;
            }
        }
        return primero != null;
    }
 
    /**
     * Metodo que hace bisible el simulador
     */
    public void makeVisible(){
        Canvas.getCanvas(CANVAS_WIDTH, CANVAS_HEIGHT);
        visible = true;
        body.makeVisible();
        refreshVisuals();
        succeed();
    }
 
    /**
     * Metodo que hace invisible al simulador
     */
    public void makeInvisible(){
        for(Wheel w : wheels){
            if(w != null){
                w.hide();
            }
        }
        jackpotFlag.makeInvisible();
        body.makeInvisible();
        visible = false;
        succeed();
    }
 
    /**
     * Metodo que termina el simulador, oculta y limpia el canvas
     */
    public void exit(){
        makeInvisible();
        Canvas.getCanvas().clearAndClose();
        succeed();
    }
 
    /**
     * Metodo que dice si la ultima operacion de la maquina se pudo hacer
     * 
     * @return un booleano que es true si la ultima operacion se realizo correctamente
     */
    public boolean ok(){
        return ok;
    }
 
    /*
     * Metodo privado que cuenta cuantas ruedas existen actualmente en la maquina
     */
    private int wheelCount(){
        int count = 0;
        for(Wheel w : wheels){
            if(w != null){
                count++;
            }
        }
        return count;
    }
 
    /*
     * Metodo privado que muesta el siguiente simbolo que hay en la maquina
     * empieza por el que este de priemras
     */
    private void spinOne(Wheel w){
        String actual = w.getColor();
        int indice = (actual == null) ? -1 : symbols.indexOf(actual);
        int siguiente = (indice + 1) % symbols.size();
        w.setColor(symbols.get(siguiente));
    }
    
    /*
     * Metodo privado que le da un simbolo aleatorio a la rueda
     */
    private void spinOneRandom(Wheel w){
        int indice = azar.nextInt(symbols.size());
        w.setColor(symbols.get(indice));
    }
 
    /*
     * Metodo privado que le reasigna el simbolo de la rueda cuando tenua
     * un simbolo que se acaba de eliminar
     */
    private void reassignWheelsShowing(String colorEliminado){
        String reemplazo = symbols.isEmpty() ? null : symbols.get(0);
        for(Wheel w : wheels){
            if(w != null && colorEliminado.equals(w.getColor())){
                w.setColor(reemplazo);
            }
        }
    }
 
    /*
     * Metodo que vuelve a dibujar todos los elementos de la maquina una vez que hay modificaciones
     */
    private void refreshVisuals(){
        if(!visible){
            return;
        }
        for(Wheel w : wheels){
            if(w != null){
                w.show();
            }
        }
        if(isJackpot()){
            jackpotFlag.makeVisible();
        } else {
            jackpotFlag.makeInvisible();
        }
    }
 
    /*
     * MEtodo privado que ajusta la posicion
     * Si entra un negativo, devuelve el minimo que es 1
     * Si entra un valor mayor al maximo, devuelve el maximo posible
     */
    private int ajustaPos(int pos, int min, int max){
        if(pos < min){
            return min;
        }
        if(pos > max){
            return max;
        }
        return pos;
    }
 
    /*
     * metodo que marca la ultima operacion como fallida
     * Si la maquina esta visible muestra un mensajito de error
     */
    private void fail(String message){
        ok = false;
        if(visible){
            JOptionPane.showMessageDialog(null, message, "SlotMachine", JOptionPane.ERROR_MESSAGE);
        }
    }
 
    /*
     * Metido que marca la ultima operacion como exitosa
     */
    private void succeed(){
        ok = true;
    }
}