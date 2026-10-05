import java.awt.*;

/**
 * Clase padre de las figuras que se pueden dibujar y manipular en un
 * Canvas (Circle, Rectangle, Triangle) 
 * Agrupa lo que todas lad figuras tienen en comun: 
 *                                                  - posicion
 *                                                  - color
 *                                                  - si estan visibles
 *                                                  - movimientos  
 * Cada figura hija solo debe encargarse de dibujarse y borrarse a si misma (draw/erase),
 * y de su propio tamaño, que varia segiun la forma
 *
 * @author Bryan Pinzón y Valentina Prieto
 * @version Ciclo 4
 */
public abstract class Figure{

    protected int xPosition;
    protected int yPosition;
    protected String color;
    protected boolean isVisible;

    /**
     * Constructor
     * Crea una figura en una posicion y con un color dados, inicialmente invisible
     *
     * @param x que es un entero que es la posicion horizontal inicial
     * @param y que es un entero que es la posicion vertical inicial
     * @param color que es un String que es el color inicial de la figura
     */
    public Figure(int x, int y, String color){
        xPosition = x;
        yPosition = y;
        this.color = color;
        isVisible = false;
    }

    /**
     * Metodo que hace la figura visimle
     */
    public void makeVisible(){
        isVisible = true;
        draw();
    }

    /**
     * Metodo que hace la figura invisible
     */
    public void makeInvisible(){
        erase();
        isVisible = false;
    }

    /**
     * Metodo que mueve la figura 20 px a la derecha
     */
    public void moveRight(){
        moveHorizontal(20);
    }

    /**
     * Metodo que mueve la figura 20 px a la izquierda
     */
    public void moveLeft(){
        moveHorizontal(-20);
    }

    /**
     * Metodo que mueve la figura 20 px hacia arriba
     */
    public void moveUp(){
        moveVertical(-20);
    }

    /**
     * Metodo que mueve la figura 20 px hacia abnajo
     */
    public void moveDown(){
        moveVertical(20);
    }

    /**
     * Metodo que mueve la figura horizontalmente
     * @param distance que es la distancia que se quiere mover en pixeles
     */
    public void moveHorizontal(int distance){
        erase();
        xPosition += distance;
        draw();
    }

    /**
     * Metodo que mueve la figura verticalmente
     * @param distance que es la distancia que se quiere mover en pixeles
     */
    public void moveVertical(int distance){
        erase();
        yPosition += distance;
        draw();
    }

    /**
     * Metodo que mueve la figura lentamente horizontalmente
     * @param distance que es la distancia que se quiere mover en pixeles
     */
    public void slowMoveHorizontal(int distance){
        int delta;

        if(distance < 0) {
            delta = -1;
            distance = -distance;
        } else {
            delta = 1;
        }

        for(int i = 0; i < distance; i++){
            xPosition += delta;
            draw();
        }
    }

    /**
     * Metodo que mueve la figura lentamente verticalmente
     * @param distance que es la distancia que se quiere mover en pixeles
     */
    public void slowMoveVertical(int distance){
        int delta;

        if(distance < 0) {
            delta = -1;
            distance = -distance;
        } else {
            delta = 1;
        }

        for(int i = 0; i < distance; i++){
            yPosition += delta;
            draw();
        }
    }

    /**
     * Metodo que cambia el color
     * @param color the new color. Valid colors are "red", "yellow", "blue", "green","magenta" and "black"
     */
    public void changeColor(String newColor){
        color = newColor;
        draw();
    }

    /**
     * Metodo que cada figura debe implementar para dibujarse a si misma en el canvas 
     */
    protected abstract void draw();

    /**
     * Metodo que cada figura debe implementar para borrarse del canvas
     */
    protected abstract void erase();
}