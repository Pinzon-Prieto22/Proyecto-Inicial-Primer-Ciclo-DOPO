import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Clase que resuelve el problema de la maraton "Slot Machine" 
 * usando de SlotMachine SOLO:
 *                          - SlotMachine(n)
 *                          - Spin(wheel,steps)
 *                          - DistinctSymbols()  
 * En el problema real, nunca vemos los colores, solo sabemos cuantos
 * distintos hay actualmente en la maquina
 * 
 * La idea de nuetsra solucion es usar la primera rueda como referencia para 
 * alinear las demas a esta. De modo que para cada rueda desde la segunda hasta n
 * encontramos el numero exacto de pasos que la alinearian con la primera rueda
 * Camos girando la primera rueda por ciclos completos y apoyandonos de distinctSymbols vamos
 * encontrando el valor minimo para encontrar la configuracion ganadora
 * Agregamos un corte: si mientras escaneamos la rueda 1
 * la maquina ya queda en jackpot (por ejemplo porque la ultima correccion
 * dejo todo alineado de una vez), se lanza una excepcion para no seguir
 * haciendo mas acciones de las necesarias
 *
 * @author Bryan Pinzón y Valentina Prieto
 * @version Ciclo 4
 */
public class SlotMachineContest{

    // Margen de seguridad para nunca pasarnos del limite real de 10000
    private static final int LIMITE_ACCIONES = 9955;
    // Tiempo de espera de configuracion inicial
    private static final int PAUSA_INICIAL_MS = 4000;

    /**
     * Clase privada para controlar nuestra excepcion donde se llega a jackpot anticipadamente
     */
    private static class JackpotEncontradoException extends RuntimeException{
    }

    /**
     * Metodo que resuelve el problema de la maraton para una maquina de
     * n ruedas y n simbolos, inicializada aleatoriamente
     * La maquina es invisible durante todo el proceso
     *
     * @param n que es un entero que es el numero de ruedas (y de simbolos) de la maquina
     * @return un arreglo de acciones {i,j}: la posicion de la rueda girada y los pasos girados
     */
    public static int[][] solve(int n){
        if(n >= 3){
            return resolverConSemilla(n, new Random().nextLong());
        }
        else{
            return new int[0][0];
        }
    }

    /**
     * Metodo que resuelve el problema de la maraton para una maquina de
     * n ruedas y n simbolos, y despues simula visualmente esas mismas acciones
     * sobre una maquina visible, para poder verla ganar en vivo 
     * La maquina visible nunca resuelve nada por su cuenta esta solo reproduce
     * los movimientos que ya se calcularon 
     *
     * @param n que es un entero que es el numero de ruedas (y de simbolos) de la maquina
     */
    public static void simulate(int n){
        if (n >= 3){
            long semilla = new Random().nextLong();
            int[][] acciones = resolverConSemilla(n, semilla);
    
            // Misma semilla => arranca exactamente en la misma configuracion
            // que la maquina (invisible) que ya resolvimos arriba
            SlotMachine maquina = new SlotMachine(n, semilla);
            maquina.makeVisible();
            maquina.esperar(PAUSA_INICIAL_MS);
            for(int[] accion : acciones){
                maquina.spin(accion[0], accion[1]);
            }
        }
    }

    /**
     * Metodo privado que crea una maquina invisible con la semilla dada,
     * la resuelve, y devuelve el arreglo de acciones que le tomo lograrlo
     */
    private static int[][] resolverConSemilla(int n, long semilla){
        SlotMachine maquina = new SlotMachine(n, semilla);
        List<int[]> acciones = new ArrayList<int[]>();
        resolver(maquina, n, acciones);
        return acciones.toArray(new int[0][]);
    }

    /**
     * Metodo privado que realmente resuelve la maquina, alineando las
     * ruedas 2..n respecto ala rueda 1, y guardando cada accion realizada
     * teniendo en cuenta el presupuesto de acciones 
     * Si en cualquier momento la maquina ya queda en jackpot (JackpotEncontradoException),
     * se corta todo el proceso de una vezporque ya no hay nada mas que hacer
     */
    private static void resolver(SlotMachine maquina, int n, List<int[]> acciones){
        try{
            for(int i = 2; i <= n && acciones.size() < LIMITE_ACCIONES; i++){
                alinear(maquina, n, i, acciones);
            }
            // por si algo quedo mal (Esto no deberia pasar, pero por si pasa)
            int intentos = 0;
            while(maquina.distinctSymbols() > 1 && acciones.size() < LIMITE_ACCIONES && intentos < 3){
                for(int i = 2; i <= n && acciones.size() < LIMITE_ACCIONES; i++){
                    alinear(maquina, n, i, acciones);
                }
                intentos++;
            }
        } catch(JackpotEncontradoException yaGanamos){
            // la maquina ya quedo en jackpot mientras escaneabamos entonces
            // no hay nada mas que hacer, las acciones ya quedaron guardadas
        }
    }

    /**
     * Metodo privado que alinea la rueda i con la rueda 1
     */
    private static void alinear(SlotMachine maquina, int n, int i, List<int[]> acciones){
        List<Integer> min1 = minimos(escanearRueda1(maquina, n, i, acciones));

        if(min1.size() == 1){
            hacerSpin(maquina, acciones, i, -min1.get(0));
            return;
        }
        //Probamos desplazando crecientemente la rueda i hasta obtener un nuevo resultado
        for(int prueba = 1; prueba < n && acciones.size() + n + 2 <= LIMITE_ACCIONES; prueba++){
            hacerSpin(maquina, acciones, i, prueba);
            List<Integer> minP = minimos(escanearRueda1(maquina, n, i, acciones));
            hacerSpin(maquina, acciones, i, -prueba);

            List<Integer> nuevos = new ArrayList<Integer>(minP);
            nuevos.removeAll(min1);
            if(nuevos.size() == 1){
                int s0 = ((nuevos.get(0) - prueba) % n + n) % n;
                hacerSpin(maquina, acciones, i, -s0);
                return;
            }
        }
        // si no se logra, recurrimos al primero de min1 como mejor esfuerzo
        hacerSpin(maquina, acciones, i, -min1.get(0));
    }

    /**
     * Metodo privado que gira la rueda 1 por su ciclo completo, dejando fija la rueda i 
     * y anota distinctSymbols() en cada paso. Deja la rueda 1 en su posicion original
     * Si en algun momento distinctSymbols() da 1, la maquina ya gano: se
     * lanza la excepcion para cortar todo el proceso de una vez
     */
    private static int[] escanearRueda1(SlotMachine maquina, int n, int i, List<int[]> acciones){
        int[] m = new int[n];
        m[0] = maquina.distinctSymbols();
        if(m[0] == 1){
            throw new JackpotEncontradoException();
        }
        for(int s = 1; s < n; s++){
            hacerSpin(maquina, acciones, 1, 1);
            m[s] = maquina.distinctSymbols();
            if(m[s] == 1){
                throw new JackpotEncontradoException();
            }
        }
        hacerSpin(maquina, acciones, 1, 1); // vuelve a dejar la rueda1 en su posicion original
        return m;
    }

    /**
     * Metodo privado que nos da las posiciones donde el arreglo alcanza
     * su valor minimo
     */
    private static List<Integer> minimos(int[] arreglo){
        int minimo = Integer.MAX_VALUE;
        for(int valor : arreglo){
            if(valor < minimo){
                minimo = valor;
            }
        }
        List<Integer> resultado = new ArrayList<Integer>();
        for(int t = 0; t < arreglo.length; t++){
            if(arreglo[t] == minimo){
                resultado.add(t);
            }
        }
        return resultado;
    }

    /**
     * Metodo privado que gira una rueda y ademas guarda la accion en la
     * lista de acciones realizadas 
     */
    private static void hacerSpin(SlotMachine maquina, List<int[]> acciones, int wheel, int steps){
        if(steps == 0){
            return;
        }
        maquina.spin(wheel, steps);
        acciones.add(new int[]{wheel, steps});
    }
}