/**
 * Clase que representa al simbolo Shy y hereda de Symbol
 * cumple con: 
 *              - cada vez que queda asignado a una rueda, alterna su estado 
 *              entre visible e invisible. Logicamente la rueda sigue mostrando este simbolo 
 *              lo unico que cambia es si su circulo se dibuja o no en el canvas
 *
 * @author Bryan Pinzón y Valentina Prieto
 * @version Ciclo 4
 */
public class ShySymbol extends Symbol{

    private boolean visible;

    /**
     * Constructor
     * Crea un simbolo shy con el color dado, empezando visible
     *
     * @param color que es un String que es el color CSS de este simbolo
     */
    public ShySymbol(String color){
        super(color);
        visible = true;
    }

    /**
     * Metodo que asigna el simbolo a la wheel
     */
    @Override
    public void alSerMostrado(Wheel rueda){
        visible = !visible;
        if(visible){
            rueda.mostrarPorSimbolo();
        } else {
            rueda.ocultarPorSimbolo();
        }
    }
}