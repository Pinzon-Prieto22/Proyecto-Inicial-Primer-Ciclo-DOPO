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
  * CICLO 4. Ademas permite:
  *                 - Manejar diferentes tipos de rueda (normal, lefty, rebel, crazy)
  *                 - Manejar diferentes tipos de simbolo (normal, ephemeral, shy)
  *
  * @author Bryan Pinzón y Valentina Prieto
  * @version Ciclo 4
  */
public class SlotMachine{
    // Definimos el margen del canvas alrededor del cuerpo de la maquina
    private static final int MARGIN = 30;
    // Numero maximo de ruedas que admite la maquina
    private static final int MAX_WHEELS = 50;
    // Cuantas ruedas caben en una fila antes de empezar una nueva debajo
    private static final int WHEELS_POR_FILA = 10;
    // Separacion vertical entre una fila de ruedas y la siguiente
    private static final int WHEEL_ROW_HEIGHT = 55;

    // Definimos el tamaño del cuerpo de la maquina (el ancho es fijo,
    // para WHEELS_POR_FILA ruedas; el alto crece con las filas)
    private static final int BASE_BODY_HEIGHT = 150;
    private static final int BODY_X = MARGIN;
    private static final int BODY_Y = MARGIN;

    // DEfinimos el margen de la rueda a la maquina
    private static final int WHEEL_MARGIN = 30;
    // definimos la separacion entre ruedas
    private static final int WHEEL_SEPARATION = 45;
    // Ancho del cuerpo (fijo, cabe WHEELS_POR_FILA ruedas por fila)
    private static final int BASE_BODY_WIDTH = WHEEL_MARGIN * 2 + (WHEELS_POR_FILA - 1) * WHEEL_SEPARATION + 30;
    // Definimos la posicion de la primera fila de ruedas
    private static final int WHEEL_BASE_X = BODY_X + WHEEL_MARGIN;
    private static final int WHEEL_BASE_Y = BODY_Y + 65;
    // Definimos la posicion de la banderita de ganar (el ancho del
    // cuerpo ya no cambia, asi que esto queda fijo)
    private static final int FLAG_X = BODY_X + BASE_BODY_WIDTH / 2 - 15;
    private static final int FLAG_Y = BODY_Y + 15;
    // Cuanto se demora, en milisegundos, cada paso al girar con spin(wheel,steps)
    private static final int STEP_DELAY_MS = 1;

    // Donde guardamos el repertorio de simbolos
    private ArrayList<Symbol> symbols;
    // Donde guardamos las ruedas
    private ArrayList<Wheel> wheels;
    // El cuerpo de la maquina (rectangulo)
    private Rectangle body;
    // La banderita de victoria (triangulo)
    private Triangle jackpotFlag;

    private Random azar;
    private boolean visible;
    private boolean ok;
    // Alto actual del cuerpo (crece si hace falta mas de una fila de ruedas)
    private int currentBodyHeight;

    /**
     * Constructor
     * Crea una SlotMachine vacia y por defecto inicia siendo ivisisble
     */
    public SlotMachine(){
        symbols = new ArrayList<Symbol>();
        wheels = new ArrayList<Wheel>();
        currentBodyHeight = BASE_BODY_HEIGHT;

        // Creamos el cuerpo de la maquina
        body = new Rectangle();
        // Le ponemos sus dimensiones
        body.changeSize(currentBodyHeight, BASE_BODY_WIDTH);
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
        jackpotFlag.moveHorizontal(FLAG_X - 140);
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
     * Constructor 3
     * Crea una SlotMachine de n ruedas y n simbolos, inicializada con una
     * semilla especifica, para poder repetir exactamente la misma
     * configuracion inicial mas adelante (lo usa SlotMachineContest para
     * que simulate() pueda mostrar en vivo la misma maquina que ya
     * resolvio solve(), sin tener que resolverla de nuevo visiblemente)
     *
     * @param n que es un entero que es el numero de ruedas y de simbolos de la maquina
     * @param semilla que es un long que es la semilla del generador aleatorio
     */
    public SlotMachine(int n, long semilla){
        this();
        azar = new Random(semilla);
        inicializaAleatoriamente(n);
    }

    /**
     * Metodo para agregar una nueva rueda normal en una posicion
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
        addWheel("normal", pos);
    }

    /**
     * Metodo para agregar una nueva rueda de un tipo dado en una posicion
     * Los tipos validos son: normal, lefty, rebel y crazy
     *
     * @param type que es un String con el tipo de rueda (normal, lefty, rebel, crazy)
     * @param pos que es entero y es la posicion en la que se quiere agregar la rueda
     */
    public void addWheel(String type, int pos){
        // Si ya hay un maximo de ruedas no la crea y avisa
        if(wheels.size() >= MAX_WHEELS){
            fail("La maquina ya tiene el maximo de " + MAX_WHEELS + " ruedas: no hay espacio para mas ruedas");
            return;
        }
        Wheel w = crearRueda(type);
        if(w == null){
            fail("'" + type + "' no es un tipo de rueda valido");
            return;
        }
        // Ajustamos la posicion si es negativa o mayor a la cantidad actual + 1
        int p = ajustaPos(pos, 1, wheels.size() + 1);
        // La insertamos, corriendo las demas si hace falta
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
     * Si la rueda es de tipo rebel, no se deja eliminar
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
        Wheel w = wheels.get(p - 1);
        if(!w.puedeEliminarse()){
            fail("La rueda en la posicion " + p + " es rebel: no se puede eliminar");
            return;
        }
        wheels.remove(p - 1);
        w.hide();
        repositionWheels();
        succeed();
    }

    /**
     * Metodo que agrega un nuevo simbolo normal (color en formato css) en la posicion dada
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
        addSymbol("normal", pos, color);
    }

    /**
     * Metodo que agrega un nuevo simbolo de un tipo dado en la posicion dada
     * Los tipos validos son: normal, ephemeral, shy
     * 
     * black, white, red, green, blue, yellow, magenta, cyan, gray, grey, orange, 
     * purple, pink, brown, lime, navy, teal, olive, maroon, silver, gold, coral, salmon, 
     * turquoise, violet, indigo, khaki, plum, orchid, tan, beige, ivory, crimson, chocolate, 
     * chartreuse, azure, lavender, skyblue, steelblue, tomato, wheat, orangered, hotpink, 
     * darkgreen, darkblue, darkred, lightblue, lightgreen, lightgray, lightgrey
     *
     * @param type que es un String con el tipo de simbolo (normal, ephemeral, shy)
     * @param pos que es entero y es la posicion del simbolo
     * @param color que es un String y debe ser de los colores validos
     */
    public void addSymbol(String type, int pos, String color){
        // Si no es valido no lo agrefa
        if(!CSSColor.esValido(color)){
            fail("'" + color + "' no es un color CSS valido");
            return;
        }
        // Si ya existe avisa
        if(buscarSimbolo(color) != null){
            fail("El color '" + color.toLowerCase() + "' ya existe en la maquina");
            return;
        }
        Symbol nuevo = crearSimbolo(type, color);
        if(nuevo == null){
            fail("'" + type + "' no es un tipo de simbolo valido");
            return;
        }
        int p = ajustaPos(pos, 1, symbols.size() + 1);
        // Lo agregamos
        symbols.add(p - 1, nuevo);
        succeed();
    }

    /**
     * Metodo que elimina un simbolo 
     * 
     * @param symbol que es un Strign que es ek nombre del color CSS que se quiere eliminar
     */
    public void delSymbol(String symbol){
        Symbol s = buscarSimbolo(symbol);
        if(s == null){
            fail("El color '" + symbol + "' no existe en la maquina");
            return;
        }
        symbols.remove(s);
        reassignWheelsShowing(s);
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
        // verificamos que el simbolo existe
        Symbol s = buscarSimbolo(symbol);
        if(s == null){
            fail("El color '" + symbol + "' no existe en la maquina");
            return;
        }
        // Le ponemos el color a la rueda
        w.setColor(s);
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
        Symbol[] resueltos = new Symbol[setSymbols.length];
        for(int i = 0; i < setSymbols.length; i++){
            Symbol s = buscarSimbolo(setSymbols[i]);
            if(s == null){
                fail("El color '" + setSymbols[i] + "' no existe en la maquina");
                return;
            }
            resueltos[i] = s;
        }
        for(int i = 0; i < wheels.size(); i++){
            Wheel w = wheels.get(i);
            if(!w.isLocked()){
                w.setColor(resueltos[i]);
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
     * Si alguna de las dos ruedas es rebel, no se deja intercambiar
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
        if(!a.puedeIntercambiarse() || !b.puedeIntercambiarse()){
            fail("Una de las ruedas es rebel: no se puede intercambiar");
            return;
        }
        Symbol symbolA = a.getSymbol();
        boolean lockedA = a.isLocked();
        a.setColor(b.getSymbol());
        a.setLocked(b.isLocked());
        b.setColor(symbolA);
        b.setLocked(lockedA);
        refreshVisuals();
        succeed();
    }

    /**
     * Metodo que fija (lock) una rueda: mientras este fija, los metodos
     * de giro y placeSymbol no la van a modificar
     * Si la rueda es rebel, no se deja fijar
     *
     * @param wheel posicion de la rueda a fijar
     */
    public void lock(int wheel){
        if(wheels.isEmpty()){
            fail("No hay ruedas en la maquina");
            return;
        }
        int p = ajustaPos(wheel, 1, wheels.size());
        Wheel w = wheels.get(p - 1);
        if(!w.puedeFijarse()){
            fail("La rueda en la posicion " + p + " es rebel: no se puede fijar");
            return;
        }
        w.setLocked(true);
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
        String[] result = new String[symbols.size()];
        for(int i = 0; i < symbols.size(); i++){
            result[i] = symbols.get(i).getColor();
        }
        return result;
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
        // Calculamos nuevamente el tamaño que necesita esta máquina
        currentBodyHeight = requiredBodyHeight();
        // Ajustamos el tamaño del cuerpo
        body.changeSize(currentBodyHeight, BASE_BODY_WIDTH);
        Canvas.getCanvas(BASE_BODY_WIDTH + 2 * MARGIN, currentBodyHeight + 2 * MARGIN);
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
     * Metodo privado que crea una rueda del tipo pedido (normal, lefty,
     * rebel, crazy)
     * Si el tipo no existe, retorna null
     */
    private Wheel crearRueda(String type){
        String t = (type == null) ? "normal" : type.toLowerCase();
        if(t.equals("normal")){
            return new Wheel();
        }
        if(t.equals("lefty")){
            return new LeftyWheel();
        }
        if(t.equals("rebel")){
            return new RebelWheel();
        }
        if(t.equals("crazy")){
            return new CrazyWheel();
        }
        return null;
    }

    /**
     * Metodo privado que crea un simbolo del tipo pedido (normal,
     * ephemeral, shy), con el color dado
     * Si el tipo no existe,retorna null
     */
    private Symbol crearSimbolo(String type, String color){
        String key = color.toLowerCase();
        String t = (type == null) ? "normal" : type.toLowerCase();
        if(t.equals("normal")){
            return new Symbol(key);
        }
        if(t.equals("ephemeral")){
            return new EphemeralSymbol(key);
        }
        if(t.equals("shy")){
            return new ShySymbol(key);
        }
        return null;
    }

    /**
     * Metodo privado que busca el Symbol del repertorio que tiene el
     * color dado (sin distinguir mayusculas/minusculas)
     *
     * @return el Symbol encontrado, o null si no existe en la maquina
     */
    private Symbol buscarSimbolo(String color){
        if(color == null){
            return null;
        }
        String key = color.toLowerCase();
        for(Symbol s : symbols){
            if(s.getColor().equals(key)){
                return s;
            }
        }
        return null;
    }

    /**
     * Metodo privado que muesta el siguiente simbolo que hay en la maquina
     * empieza por el que este de priemras
     * Le pregunta a la rueda misma cual deberia ser (las ruedas normales
     * avanzan en el repertorio; una lefty copia a su vecina izquierda)
     */
    private void spinOne(Wheel w){
        Symbol nuevo = w.siguienteSimbolo(symbols, 1, vecinaIzquierda(w));
        w.setColor(nuevo);
    }

    /**
     * Metodo privado que avanza una rueda un paso en una direccion dada
     * (1 = siguiente simbolo, -1 = simbolo anterior), en forma circular
     */
    private void avanzaUnPaso(Wheel w, int direccion){
        Symbol nuevo = w.siguienteSimbolo(symbols, direccion, vecinaIzquierda(w));
        w.setColor(nuevo);
    }

    /**
     * Metodo privado que le da un simbolo aleatorio a la rueda
     * Le pregunta a la rueda misma cual deberia ser (las ruedas normales
     * escogen al azar del repertorio; una lefty copia a su vecina izquierda)
     */
    private void spinOneRandom(Wheel w){
        Symbol nuevo = w.simboloAlAzar(symbols, azar, vecinaIzquierda(w));
        w.setColor(nuevo);
    }

    /**
     * Metodo privado que nos da la rueda inmediatamente a la izquierda
     * de la rueda dada, o null si no tiene (esta de primera o no esta
     * en la lista)
     */
    private Wheel vecinaIzquierda(Wheel w){
        int indice = wheels.indexOf(w);
        return (indice > 0) ? wheels.get(indice - 1) : null;
    }

    /**
     * Metodo privado que le reasigna el simbolo de la rueda cuando tenua
     * un simbolo que se acaba de eliminar
     */
    private void reassignWheelsShowing(Symbol eliminado){
        Symbol reemplazo = symbols.isEmpty() ? null : symbols.get(0);
        for(Wheel w : wheels){
            if(eliminado == w.getSymbol()){
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
     * Metodo privado que reubica todas las ruedas: 
     * las que respetan el layout (normal, lefty, rebel) se acomodan en filas de
     * WHEELS_POR_FILA, de izquierda a derecha y de arriba a abajo 
     * las que no (crazy) se ubican a su manera
     */
    private void repositionWheels(){
        int fila = 0;
        int columna = 0;
        for(Wheel w : wheels){
            if(w.respetaLayout()){
                int x = WHEEL_BASE_X + columna * WHEEL_SEPARATION;
                int y = WHEEL_BASE_Y + fila * WHEEL_ROW_HEIGHT;
                w.reposition(x, y);
                columna++;
                if(columna >= WHEELS_POR_FILA){
                    columna = 0;
                    fila++;
                }
            } else {
                int margen = 20;
                w.reposicionEspecial(BODY_X + margen, BODY_X + BASE_BODY_WIDTH - margen,
                    BODY_Y + margen, BODY_Y + currentBodyHeight - margen, azar);
            }
        }
    }

    /**
     * Metodo privado que calcula el alto que deberia tener el cuerpo
     * segun cuantas filas de ruedas hacen falta (el ancho ya es fijo,
     * siempre caben WHEELS_POR_FILA columnas). Las ruedas que no
     * respetan el layout (crazy) no cuentan para las filas
     */
    private int requiredBodyHeight(){
        int filas = contarFilas();
        if(filas <= 1){
            return BASE_BODY_HEIGHT;
        }
        return BASE_BODY_HEIGHT + (filas - 1) * WHEEL_ROW_HEIGHT;
    }

    /**
     * Metodo privado que cuenta cuantas filas hacen falta para las
     * ruedas que respetan el layout en fila
     */
    private int contarFilas(){
        int enLayout = 0;
        for(Wheel w : wheels){
            if(w.respetaLayout()){
                enLayout++;
            }
        }
        if(enLayout == 0){
            return 1;
        }
        return (int) Math.ceil(enLayout / (double) WHEELS_POR_FILA);
    }

    /**
     * Metodo privado que agranda el cuerpo (y el canvas, si ya esta
     * visible) cuando hacen falta mas filas de ruedas de las que caben ahora
     */
    private void growIfNeeded(){
        int needed = requiredBodyHeight();
        if(needed <= currentBodyHeight){
            return;
        }
        currentBodyHeight = needed;
        body.changeSize(currentBodyHeight, BASE_BODY_WIDTH);
        if(visible){
            Canvas.getCanvas().resize(BASE_BODY_WIDTH + 2 * MARGIN, currentBodyHeight + 2 * MARGIN);
        }
    }

    /**
     * Metodo privado que crea n simbolos distintos al azar y n ruedas,
     * cada una con un simbolo al azar del repertorio. Si por azar la
     * maquina queda ya ganadora, se vuelve a hacer
     */
    private void inicializaAleatoriamente(int n){
        if (n >= 3 && n <= 50 && n!=1){
            do{
                symbols = new ArrayList<Symbol>();
                wheels = new ArrayList<Wheel>();
                currentBodyHeight = BASE_BODY_HEIGHT;
            
                ArrayList<String> disponibles = new ArrayList<String>();
                for(String nombre : CSSColor.nombres()){
                    disponibles.add(nombre);
                }
                java.util.Collections.shuffle(disponibles, azar);
                for(int i = 0; i < n; i++){
                    symbols.add(new Symbol(disponibles.get(i)));
                }
                for(int i = 1; i <= n; i++){
                    addWheel(i);
                    Symbol elegido = symbols.get(azar.nextInt(symbols.size()));
                    placeSymbol(i, elegido.getColor());
                }
            } while(isJackpot());
        }
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