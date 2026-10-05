/**
 * Clase que representa el simbolo (color) del repertorio de la maquina
 * 
 * Un simbolo normal que no tiene ningun comportamiento especial 
 * De esta clase heredan los tipos especiales: 
 *                                              - EphemeralSymbol que se va encogiendo
 *                                              - ShySymbol, que aparece y desaparece
 *
 * @author Bryan Pinzón y Valentina Prieto
 * @version Ciclo 4
 */
public class Symbol{

    private String color;

    /**
     * Constructor
     * Crea un simbolo normal con el color dado
     *
     * @param color que es un String que es el color del simbolo
     */
    public Symbol(String color){
        this.color = color;
    }

    /**
     * Metodo que nos dice el color del simbolo
     *
     * @return un String que es el color del simbolo
     */
    public String getColor(){
        return color;
    }

    /**
     * Metodo que asigna el simbolo a una rueda
     * Un simbolo normal no hace nada especial, entonces los simbolos especiales 
     * redefinen este metodo para reaccionar
     *
     * @param rueda que es la Wheel a la que se le acaba de asignar este simbolo
     */
    public void alSerMostrado(Wheel rueda){
        // un simbolo normal no hace nada especial
    }
}