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
  * CICLO 2. Ademas permite:
  *                 - Intercambiar dos ruedas (swap)
  *                 - Fijar y soltar una rueda (lock / unlock)
  *                 - Rotar una rueda un numero de pasos, visualizando
  *                   el movimiento paso a paso si la maquina esta visible
  *                 - Dejar la maquina en una configuracion dada
  * CICLO 3. Tambien permite:
  *                 - Crear una maquina con igual numero de ruedas y simbolos
  *                 - Resuelve el problema de la maraton (SlotMachineContest)
  *                 - Simula la solucion del problema de la maraton (SlotMachineContest)
  *
  * @author Bryan Pinzón y Valentina Prieto
  * @version Ciclo 3
  */
public class SlotMachine{
    // Definimos el margen del canvas alrededor del cuerpo de la maquina
    private static final int MARGIN = 30;
    // Cantidad de ruedas que caben en el tamaño base, sin crecer el canvas
    private static final int BASE_CAPACITY = 10;
    // Numero maximo de ruedas que admite la maquina
    private static final int MAX_WHEELS = 50;

    // Definimos el tamaño del cuerpo de la maquina
    private static final int BODY_HEIGHT = 160;
    private static final int BODY_X = MARGIN;
    private static final int BODY_Y = MARGIN;
    private static final int CANVAS_HEIGHT = BODY_HEIGHT + 2 * MARGIN;

    // DEfinimos el margen de la rueda a la maquina
    private static final int WHEEL_MARGIN = 30;
    // definimos la separacion entre ruedas
    private static final int WHEEL_SEPARATION = 45;
    // Ancho del cuerpo con el que arranca la maquina (cabe BASE_CAPACITY ruedas)
    private static final int BASE_BODY_WIDTH = WHEEL_MARGIN * 2 + (BASE_CAPACITY - 1) * WHEEL_SEPARATION + 30;
    // Definimos la posicion de la fila de las ruedas
    private static final int WHEEL_BASE_X = BODY_X + WHEEL_MARGIN;
    private static final int WHEEL_BASE_Y = BODY_Y + 65;
    // Definimos la posicion vertical de la banderita de ganar (la horizontal
    // se recalcula cada vez que el cuerpo cambia de ancho)
    private static final int FLAG_Y = BODY_Y + 15;
    // Cuanto se demora, en milisegundos, cada paso al girar con spin(wheel,steps)
    private static final int STEP_DELAY_MS = 2000;

    // Donde guardamos los simbolos (colores)
    private ArrayList<String> symbols;
    // Donde guardamos las ruedas (circulos)
    private ArrayList<Wheel> wheels;
    // El cuerpo de la maquina (rectangulo)
    private Rectangle body;
    // La banderita de victoria (triangulo)
    private Triangle jackpotFlag;

    private Random azar;
    private boolean visible;
    private boolean ok;
    // Ancho actual del cuerpo (crece si hay mas de BASE_CAPACITY ruedas)
    private int currentBodyWidth;
    // Posicion horizontal actual de la banderita, para poder reubicarla
    private int flagX;

    /**
     * Constructor
     * Crea una SlotMachine vacia y por defecto inicia siendo ivisisble
     */
    public SlotMachine(){
        symbols = new ArrayList<String>();
        wheels = new ArrayList<Wheel>();
        currentBodyWidth = BASE_BODY_WIDTH;

        // Creamos el cuerpo de la maquina
        body = new Rectangle();
        // Le ponemos sus dimensiones
        body.changeSize(BODY_HEIGHT, currentBodyWidth);
        // Lo ubicamos centrado
        body.moveHorizontal(BODY_X - 70);
        body.moveVertical(BODY_Y - 15);
        // Le ponemos el color que queremos
        body.changeColor("steelblue");

        // Creamos la banderita
        jackpotFlag = new Triangle();
        // Le damos dimendsiones
        jackpotFlag.changeSize(20, 30);
        jackpotFlag.moveVertical(FLAG_Y - 15);
        // La ubicamos centrada respecto al ancho inicial del cuerpo
        flagX = 140;
        int targetFlagX = BODY_X + currentBodyWidth / 2 - 15;
        jackpotFlag.moveHorizontal(targetFlagX - flagX);
        flagX = targetFlagX;
        // Le ponemos el color que queremos
        jackpotFlag.changeColor("gold");

        azar = new Random();
        visible = false;
        ok = true;
    }

    /**
     * Constructor 2
     * Crea una SlotMachine de n ruedas y n simbolos, inicializada aleatoriamente 
     * Se asegura de que la configuracion inicial no quede ya ganadora
     *
     * @param n que es un entero que es el numero de ruedas y de simbolos de la maquina
     */
    public SlotMachine(int n){
        this();
        inicializaAleatoriamente(n);
    }

    /**
     * Metodo para agregar una nueva rueda en una posicion
     * Las ruedas nunca quedan con huecos entre ellas: si pos ya esta
     * ocupada, las ruedas desde esa posicion se corren un puesto
     * 
     * Si pos es menor a 1 se usa la posicion 1 
     * Si pos es mayor a la cantidad actual de ruedas, la nueva rueda queda de ultima
     * Si ya se llego al maximo de ruedas de la maquina, avisa y no la crea
     *
     * @param pos que es entero y es la posicion en la que se quiere agregar la rueda
     */
    public void addWheel(int pos){
        // Si ya hay un maximo de ruedas no la crea y avisa
        if(wheels.size() >= MAX_WHEELS){
            fail("La maquina ya tiene el maximo de " + MAX_WHEELS + " ruedas: no hay espacio para mas ruedas");
            return;
        }
        // Ajustamos la posicion si es negativa o mayor a la cantidad actual + 1
        int p = ajustaPos(pos, 1, wheels.size() + 1);
        // Creamos la rueda y la insertamos, corriendo las demas si hace falta
        Wheel w = new Wheel();
        wheels.add(p - 1, w);
        w.setColor(null);
        // Si hace falta, el cuerpo y el canvas crecen para que quepa
        growIfNeeded();
        // Reubicamos todas las ruedas segun su nuevo orden en la lista
        repositionWheels();
        refreshVisuals();
        succeed();
    }

    /**
     * Metodo para eliminar la rueda de una posicion
     * Las ruedas siguientes se corren un puesto para no dejar huecos
     * Si no hay ninguna rueda en la maquina, avisa
     *
     * @param pos que es entero y es la posicion de la rueda que se quiere quitar
     */
    public void delWheel(int pos){
        //revisamods si no tiene ruedas
        if(wheels.isEmpty()){
            fail("No hay ruedas para eliminar");
            return;
        }
        int p = ajustaPos(pos, 1, wheels.size());
        Wheel w = wheels.remove(p - 1);
        w.hide();
        repositionWheels();
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
            fail("'" + color + "' no es un color CSS valido");
            return;
        }
        String key = color.toLowerCase();
        // Si ya existe avisa
        if(symbols.contains(key)){
            fail("El color '" + key + "' ya existe en la maquina");
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
            fail("El color '" + symbol + "' no existe en la maquina");
            return;
        }
        symbols.remove(key);
        reassignWheelsShowing(key);
        refreshVisuals();
        succeed();
    }

    /**
     * Metodo que coloca un simbolo especifico (color) en una rueda especifica
     * No se puede usar sobre una rueda fija (lock)
     *
     * @param wheel que es un entero que es la posicion de la rueda
     * @param symbol que es un Srting que es el nombre del color CSS del simbolo a colocar
     */
    public void placeSymbol(int wheel, String symbol){
        if(wheels.isEmpty()){
            fail("No hay ruedas en la maquina");
            return;
        }
        int p = ajustaPos(wheel, 1, wheels.size());
        Wheel w = wheels.get(p - 1);
        // Si la rueda esta bloqueada no puede
        if(w.isLocked()){
            fail("La rueda en la posicion " + p + " esta fija (lock): no se puede modificar");
            return;
        }
        String key = symbol == null ? null : symbol.toLowerCase();
        // verificamos que el simbolo existe
        if(key == null || !symbols.contains(key)){
            fail("El color '" + symbol + "' no existe en la maquina");
            return;
        }
        // Le ponemos el color a la rueda
        w.setColor(key);
        refreshVisuals();
        succeed();
    }

    /**
     * Metodo que gira una rueda especifica una vez y muestra el siguiente simbolo
     * No se puede usar sobre una rueda fija (lock)
     *
     * @param wheel que es un entero que es la posicion de la rueda que se quiere girat
     */
    public void spinOtro(int wheel){
        if(symbols.isEmpty()){
            fail("No es posible girar: no hay simbolos definidos");
            return;
        }
        if(wheels.isEmpty()){
            fail("No hay ruedas en la maquina");
            return;
        }
        int p = ajustaPos(wheel, 1, wheels.size());
        Wheel w = wheels.get(p - 1);
        if(w.isLocked()){
            fail("La rueda en la posicion " + p + " esta fija (lock): no se puede girar");
            return;
        }
        spinOne(w);
        refreshVisuals();
        succeed();
    }

    /**
     * Metodo que gira todas las ruedas existentes de la maquina al siguiente
     * simbolo en la lista
     * Las ruedas fijas (lock) no se mueven
     */
    public void spinOtro(){
        if(wheels.isEmpty() || symbols.isEmpty()){
            fail("No es posible girar: faltan ruedas o simbolos");
            return;
        }
        boolean algunaGiro = false;
        for(Wheel w : wheels){
            if(!w.isLocked()){
                spinOne(w);
                algunaGiro = true;
            }
        }
        if(!algunaGiro){
            fail("Todas las ruedas estan fijas (lock): no hay ninguna para girar");
            return;
        }
        refreshVisuals();
        succeed();
    }

    /**
     * Metodo que gira una rueda un numero de pasos dado (steps), avanzando
     * en el repertorio de simbolos que tenemos
     * Si steps es negativo gira en sentido contrarrop
     * Si la maquina esta visible, el movimiento se muestra paso a pasp 
     * (con una pequeña pausa entre cada paso)
     * No se puede usar sobre una rueda fija (lock)
     *
     * @param wheel posicion de la rueda a girar
     * @param steps numero de pasos a girar (negativo gira al reves)
     */
    public void spin(int wheel, int steps){
        if(symbols.isEmpty()){
            fail("No es posible girar: no hay simbolos definidos");
            return;
        }
        if(wheels.isEmpty()){
            fail("No hay ruedas en la maquina");
            return;
        }
        int p = ajustaPos(wheel, 1, wheels.size());
        Wheel w = wheels.get(p - 1);
        if(w.isLocked()){
            fail("La rueda en la posicion " + p + " esta fija (lock): no se puede girar");
            return;
        }
        int direccion = (steps < 0) ? -1 : 1;
        int totalPasos = Math.abs(steps);
        for(int i = 0; i < totalPasos; i++){
            avanzaUnPaso(w, direccion);
            if(visible){
                refreshVisuals();
                Canvas.getCanvas().wait(STEP_DELAY_MS);
            }
        }
        refreshVisuals();
        succeed();
    }

    /**
     * Metodo que deja la maquina en la configuracion dada: un color por
     * cada rueda existente, en orden de izquierda a derecha 
     * El arreglo
     * debe tener exactamente una posicion por cada rueda de la maquina
     * y cada color debe existir en el repertorio de simbolos
     * Las ruedas fijas (lock) no se modifican, aunque el arreglo traiga un
     * color distinto para ellas
     *
     * @param setSymbols arreglo {"a","b,""c"} con el color deseado para cada rueda
     */
    public void spin(String[] setSymbols){
        if(setSymbols == null || setSymbols.length != wheels.size()){
            fail("El arreglo debe tener exactamente " + wheels.size() + " colores (uno por rueda)");
            return;
        }
        for(String s : setSymbols){
            String key = (s == null) ? null : s.toLowerCase();
            if(key == null || !symbols.contains(key)){
                fail("El color '" + s + "' no existe en la maquina");
                return;
            }
        }
        for(int i = 0; i < wheels.size(); i++){
            Wheel w = wheels.get(i);
            if(!w.isLocked()){
                w.setColor(setSymbols[i].toLowerCase());
            }
        }
        refreshVisuals();
        succeed();
    }

    /**
     * Metodo que gira una sola rueda al azat (no el simbolo siguiente)
     * No se puede usar sobre una rueda fija (lock)
     *
     * @param wheel que es un entero qu es la posicion de la rueda que se quiere cambiar
     */
    public void spin(int wheel){
        if(symbols.isEmpty()){
            fail("No es posible girar: no hay simbolos definidos");
            return;
        }
        if(wheels.isEmpty()){
            fail("No hay ruedas en la maquina");
            return;
        }
        int p = ajustaPos(wheel, 1, wheels.size());
        Wheel w = wheels.get(p - 1);
        if(w.isLocked()){
            fail("La rueda en la posicion " + p + " esta fija (lock): no se puede girar");
            return;
        }
        spinOneRandom(w);
        refreshVisuals();
        succeed();
    }

    /**
     * Metodo que gira todas las ruedas de la maquina, pero al azar cada una
     * Las ruedas fijas (lock) no se mueven
     */
    public void spin(){
        if(wheels.isEmpty() || symbols.isEmpty()){
            fail("No es posible girar: faltan ruedas o simbolos");
            return;
        }
        boolean algunaGiro = false;
        for(Wheel w : wheels){
            if(!w.isLocked()){
                spinOneRandom(w);
                algunaGiro = true;
            }
        }
        if(!algunaGiro){
            fail("Todas las ruedas estan fijas (lock): no hay ninguna para girar");
            return;
        }
        refreshVisuals();
        succeed();
    }

    /**
     * Metodo que intercambia el simboko y incluyendo su estado de fijado (lock) 
     * entre dos ruedas de la maquina
     * Funciona incluso si alguna de las dos esta fijsa
     *
     * @param wheel1 posicion de la primera rueda
     * @param wheel2 posicion de la segunda rueda
     */
    public void swap(int wheel1, int wheel2){
        if(wheels.size() < 2){
            fail("Se necesitan al menos 2 ruedas para intercambiar");
            return;
        }
        int p1 = ajustaPos(wheel1, 1, wheels.size());
        int p2 = ajustaPos(wheel2, 1, wheels.size());
        Wheel a = wheels.get(p1 - 1);
        Wheel b = wheels.get(p2 - 1);
        String colorA = a.getColor();
        boolean lockedA = a.isLocked();
        a.setColor(b.getColor());
        a.setLocked(b.isLocked());
        b.setColor(colorA);
        b.setLocked(lockedA);
        refreshVisuals();
        succeed();
    }

    /**
     * Metodo que fija (lock) una rueda: mientras este fija, los metodos
     * de giro y placeSymbol no la van a modificar
     *
     * @param wheel posicion de la rueda a fijar
     */
    public void lock(int wheel){
        if(wheels.isEmpty()){
            fail("No hay ruedas en la maquina");
            return;
        }
        int p = ajustaPos(wheel, 1, wheels.size());
        wheels.get(p - 1).setLocked(true);
        succeed();
    }

    /**
     * Metodo que suelta (unlock) una rueda previamente fijada, para que
     * vuelva a poder girar normalmente
     *
     * @param wheel posicion de la rueda a soltar
     */
    public void unlock(int wheel){
        if(wheels.isEmpty()){
            fail("No hay ruedas en la maquina.");
            return;
        }
        int p = ajustaPos(wheel, 1, wheels.size());
        wheels.get(p - 1).setLocked(false);
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
        Canvas.getCanvas(currentBodyWidth + 2 * MARGIN, CANVAS_HEIGHT);
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
            w.hide();
        }
        jackpotFlag.makeInvisible();
        body.makeInvisible();
        visible = false;
        succeed();
    }
    
    /**
     * Metodo que hace una pausa, sin cambiar nada de la maquina
     * Solo pausa si la maquina esta visible 
     *
     * @param milisegundos que es un entero que es cuanto tiempo esperar
     */
    public void esperar(int milisegundos){
        if(visible){
            Canvas.getCanvas().wait(milisegundos);
        }
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

    /**
     * Metodo privado que muesta el siguiente simbolo que hay en la maquina
     * empieza por el que este de priemras
     */
    private void spinOne(Wheel w){
        String actual = w.getColor();
        int indice = (actual == null) ? -1 : symbols.indexOf(actual);
        int siguiente = (indice + 1) % symbols.size();
        w.setColor(symbols.get(siguiente));
    }

    /**
     * Metodo privado que avanza una rueda un paso en una direccion dada
     * (1 = siguiente simbolo, -1 = simbolo anterior), en forma circular.
     */
    private void avanzaUnPaso(Wheel w, int direccion){
        String actual = w.getColor();
        int total = symbols.size();
        int indice = (actual == null) ? -1 : symbols.indexOf(actual);
        int siguiente = ((indice + direccion) % total + total) % total;
        w.setColor(symbols.get(siguiente));
    }

    /**
     * Metodo privado que le da un simbolo aleatorio a la rueda
     */
    private void spinOneRandom(Wheel w){
        int indice = azar.nextInt(symbols.size());
        w.setColor(symbols.get(indice));
    }

    /**
     * Metodo privado que le reasigna el simbolo de la rueda cuando tenua
     * un simbolo que se acaba de eliminar
     */
    private void reassignWheelsShowing(String colorEliminado){
        String reemplazo = symbols.isEmpty() ? null : symbols.get(0);
        for(Wheel w : wheels){
            if(colorEliminado.equals(w.getColor())){
                w.setColor(reemplazo);
            }
        }
    }

    /**
     * Metodo que vuelve a dibujar todos los elementos de la maquina una vez que hay modificaciones
     */
    private void refreshVisuals(){
        if(!visible){
            return;
        }
        for(Wheel w : wheels){
            w.show();
        }
        if(isJackpot()){
            jackpotFlag.makeVisible();
        } else {
            jackpotFlag.makeInvisible();
        }
    }

    /**
     * Metodo privado que reubica todas las ruedas segun su orden actual
     * en la lista (siempre consecutivas, sin huecos).
     */
    private void repositionWheels(){
        for(int i = 0; i < wheels.size(); i++){
            wheels.get(i).reposition(WHEEL_BASE_X + i * WHEEL_SEPARATION, WHEEL_BASE_Y);
        }
    }

    /**
     * Metodo privado que calcula el ancho que deberia tener el cuerpo
     * segun la cantidad actual de ruedas: se queda en el ancho base
     * mientras quepan BASE_CAPACITY ruedas, y crece a partir de ahi.
     */
    private int requiredBodyWidth(){
        int count = wheels.size();
        if(count <= BASE_CAPACITY){
            return BASE_BODY_WIDTH;
        }
        return WHEEL_MARGIN * 2 + (count - 1) * WHEEL_SEPARATION +30;
    }

    /**
     * Metodo privado que agranda el cuerpo (y el canvas, si ya esta
     * visible) cuando hacen falta mas ruedas de las que caben ahora.
     */
    private void growIfNeeded(){
        int needed = requiredBodyWidth();
        if(needed <= currentBodyWidth){
            return;
        }
        currentBodyWidth = needed;
        body.changeSize(BODY_HEIGHT, currentBodyWidth);
        reposicionaBandera();
        if(visible){
            Canvas.getCanvas().resize(currentBodyWidth + 2 * MARGIN, CANVAS_HEIGHT);
        }
    }

    /**
     * Metodo privado que reubica la banderita para que quede centrada
     * respecto al nuevo ancho del cuerpo.
     */
    private void reposicionaBandera(){
        int targetFlagX = BODY_X + currentBodyWidth / 2 - 15;
        jackpotFlag.moveHorizontal(targetFlagX - flagX);
        flagX = targetFlagX;
    }

    /**
     * Metodo privado que crea n simbolos distintos al azar y n ruedas,
     * cada una con un simbolo al azar del repertorio. Si por azar la
     * maquina queda ya ganadora, se vuelve a hacer
     */
    private void inicializaAleatoriamente(int n){
        do{
            symbols = new ArrayList<String>();
            wheels = new ArrayList<Wheel>();
            currentBodyWidth = BASE_BODY_WIDTH;
    
            ArrayList<String> disponibles = new ArrayList<String>();
            for(String nombre : CSSColor.nombres()){
                disponibles.add(nombre);
            }
            java.util.Collections.shuffle(disponibles, azar);
            for(int i = 1; i <= n; i++){
                symbols.add(disponibles.get(i - 1));
            }
            for(int i = 1; i <= n; i++){
                addWheel(i);
                placeSymbol(i, symbols.get(azar.nextInt(symbols.size())));
            }
        } while(isJackpot());
    }

    /**
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

    /**
     * metodo que marca la ultima operacion como fallida
     * Si la maquina esta visible muestra un mensajito de error
     */
    private void fail(String message){
        ok = false;
        if(visible){
            JOptionPane.showMessageDialog(null, message, "SlotMachine", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Metido que marca la ultima operacion como exitosa
     */
    private void succeed(){
        ok = true;
    }
}