import java.awt.Color;
import java.util.HashMap;
 
/**
 * Clase de apoyo que valida nombres de colores CSS y los convierte a objetos 
 * java.awt.Color, para que Canvas pueda dibujar con cualquiera de esos colores
 *
 * @author Bryan Pinzón y Valentina Prieto
 * @version Ciclo 1
 */
public class CSSColor{
 
    private static HashMap<String, String> tabla = crearTabla();
 
    /**
     * Valida que el nombre corresponde a un color CSS
     * @param nombre que es el nombre del color
     * @return true si el nombre es un color CSS valido
     */
    public static boolean esValido(String nombre){
        return nombre != null && tabla.containsKey(nombre.toLowerCase());
    }
 
    /**
     * Convierte el color CSS a un objeto color de AWT
     * si el nombre no es valido, retorna Color.black por defecto
     * @param nombre nombre del color CSS
     * @return el color equivalente en AWT
     */
    public static Color toAwt(String nombre){
        if(!esValido(nombre)){
            return Color.black;
        }
        return Color.decode(tabla.get(nombre.toLowerCase()));
    }
    
    /**
     * Metodo que nos da todos los nombres de colores validos que soporta
     *
     * @return un arreglo con los nombres de todos los colores validos
     */
    public static String[] nombres(){
        return tabla.keySet().toArray(new String[0]);
    }
 
    /*
     * Metodo que crea la tabla con los nombres CSS y su codigo hexadecimal
     */
    private static HashMap<String, String> crearTabla(){
        HashMap<String, String> t = new HashMap<String, String>();
        t.put("black", "#000000");
        t.put("white", "#FFFFFF");
        t.put("red", "#FF0000");
        t.put("green", "#008000");
        t.put("blue", "#0000FF");
        t.put("yellow", "#FFFF00");
        t.put("magenta", "#FF00FF");
        t.put("cyan", "#00FFFF");
        t.put("gray", "#808080");
        t.put("grey", "#808080");
        t.put("orange", "#FFA500");
        t.put("purple", "#800080");
        t.put("pink", "#FFC0CB");
        t.put("brown", "#A52A2A");
        t.put("lime", "#00FF00");
        t.put("navy", "#000080");
        t.put("teal", "#008080");
        t.put("olive", "#808000");
        t.put("maroon", "#800000");
        t.put("silver", "#C0C0C0");
        t.put("gold", "#FFD700");
        t.put("coral", "#FF7F50");
        t.put("salmon", "#FA8072");
        t.put("turquoise", "#40E0D0");
        t.put("violet", "#EE82EE");
        t.put("indigo", "#4B0082");
        t.put("khaki", "#F0E68C");
        t.put("plum", "#DDA0DD");
        t.put("orchid", "#DA70D6");
        t.put("tan", "#D2B48C");
        t.put("beige", "#F5F5DC");
        t.put("ivory", "#FFFFF0");
        t.put("crimson", "#DC143C");
        t.put("chocolate", "#D2691E");
        t.put("chartreuse", "#7FFF00");
        t.put("azure", "#F0FFFF");
        t.put("lavender", "#E6E6FA");
        t.put("skyblue", "#87CEEB");
        t.put("steelblue", "#4682B4");
        t.put("tomato", "#FF6347");
        t.put("wheat", "#F5DEB3");
        t.put("orangered", "#FF4500");
        t.put("hotpink", "#FF69B4");
        t.put("darkgreen", "#006400");
        t.put("darkblue", "#00008B");
        t.put("darkred", "#8B0000");
        t.put("lightblue", "#ADD8E6");
        t.put("lightgreen", "#90EE90");
        t.put("lightgray", "#D3D3D3");
        t.put("lightgrey", "#D3D3D3");
        return t;
    }
}