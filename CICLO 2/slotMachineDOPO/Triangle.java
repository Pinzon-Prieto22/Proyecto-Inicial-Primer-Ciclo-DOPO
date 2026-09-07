import java.awt.*;
import java.lang.Math.*;
 
/**
 * A triangle that can be manipulated and that draws itself on a canvas.
 * 
 * @author  Michael Kolling and David J. Barnes
 * @version 1.0  (15 July 2000)
 */
 
public class Triangle{
    
    public static final int VERTICES=3;
    
    private int height;
    private int width;
    private int xPosition;
    private int yPosition;
    private String color;
    private boolean isVisible;
    private double area;
 
    /**
     * Create a new triangle at default position with default color.
     */
    public Triangle(){
        height = 30;
        width = 40;
        xPosition = 140;
        yPosition = 15;
        color = "green";
        isVisible = false;
    }
    
    /**
     * Constructor 2 del triangulo que pide sus medidas y color
     * @param entero alto
     * @param entero ancho
     * @param string color
     * @author Bryan Pinzón - Valentina Prieto
     * @version Lab1 DOPO
     */
    public Triangle(int alto, int ancho, String colorNuevo){
        height = alto;
        width = ancho;
        xPosition = 140;
        yPosition = 15;
        color = colorNuevo;
        isVisible = false;
    }
    
    /**
     * Calcula el area del triangulo
     * @author Bryan Pinzón - Valentina Prieto
     * @version Lab1 DOPO
     */
    public double area(){
        double operation = width * height;
        operation = operation / 2;
        area = operation;
        return operation; 
    }
    
    /**
     * Considerando el area del triangulo
     * Lo convertimos en un triangulo equilatero con un area
     * aproximadamente igual
     * Calculo del lado:
     * raiz cuadrada de (4*area / raiz cuadrada de (3))
     * Calculo de la altura:
     * (lado * raiz cuadrada de (3)) / 2
     * @author Bryan Pinzón - Valentina Prieto
     * @version Lab1 DOPO
     */
    public void equilateral(){
        double nuevoAncho;
        nuevoAncho = Math.sqrt((4*area)/(Math.sqrt(3)));
        double nuevaAltura;
        nuevaAltura = (nuevoAncho * Math.sqrt(3))/ 2 ;
        // Ahora hay que cambiar el tamaño del triangulo
        // vamos a usar changeSize(int newHeight, int newWidth)
        changeSize((int)nuevaAltura, (int)nuevoAncho);
    }
    
    /**
     * Mueve el triangulo hacia izquierda(negativo) o derecha(positivo) y va cayendo
     * @param entra un entero que da la direccion del movimiento
     * @author Bryan Pinzón - Valentina Prieto
     * @version Lab1 DOPO
     */
    public void walk(int times){
        int deltaX;
        int deltaY = 1; // esto es para que se mueva hacia abajo
        
        if (times < 0){
            deltaX = -1; // esto es para que se mueva a la izquierda
            times = times * (-1);
        }
        else{
            deltaX = 1; // esto es para que s e mueva a la derecha
        }
        
        for(int i = 0; i < times; i++){
            xPosition += deltaX;
            yPosition += deltaY;
            draw();
        }
    }
    
    /**
     * Mueve el triangulo por el perimetro de un cuadrado dad la 
     * longitud del lado del cuadrado
     * @param entero lado del cuadrado
     * @author Bryan Pinzón - Valentina Prieto
     * @version Lab1 DOPO
     */
    public void sigueCuadrado(int ladoCuadrado){
        if (ladoCuadrado <= 0){
            ladoCuadrado = ladoCuadrado * (-1);
        }
        int delta = 1;
        // Se mueve a la derecha
        for (int i = 0; i < ladoCuadrado; i++){
            xPosition += delta;
            draw();
        }
        // Se mueve hacia abajo
        for (int i = 0; i < ladoCuadrado; i++){
            yPosition += delta;
            draw();
        }
        // Se mueve hacia la izquierda
        for (int i = 0; i < ladoCuadrado; i++){
            xPosition -= delta;
            draw();
        }
        // Se mueve hacia arriba
        for (int i = 0; i < ladoCuadrado; i++){
            yPosition -= delta;
            draw();
        }
    }
    
    /**
     * Make this triangle visible. If it was already visible, do nothing.
     */
    public void makeVisible(){
        isVisible = true;
        draw();
    }
    
    /**
     * Make this triangle invisible. If it was already invisible, do nothing.
     */
    public void makeInvisible(){
        erase();
        isVisible = false;
    }
    
    /**
     * Move the triangle a few pixels to the right.
     */
    public void moveRight(){
        moveHorizontal(20);
    }
 
    /**
     * Move the triangle a few pixels to the left.
     */
    public void moveLeft(){
        moveHorizontal(-20);
    }
 
    /**
     * Move the triangle a few pixels up.
     */
    public void moveUp(){
        moveVertical(-20);
    }
 
    /**
     * Move the triangle a few pixels down.
     */
    public void moveDown(){
        moveVertical(20);
    }
 
    /**
     * Move the triangle horizontally.
     * @param distance the desired distance in pixels
     */
    public void moveHorizontal(int distance){
        erase();
        xPosition += distance;
        draw();
    }
 
    /**
     * Move the triangle vertically.
     * @param distance the desired distance in pixels
     */
    public void moveVertical(int distance){
        erase();
        yPosition += distance;
        draw();
    }
 
    /**
     * Slowly move the triangle horizontally.
     * @param distance the desired distance in pixels
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
     * Slowly move the triangle vertically.
     * @param distance the desired distance in pixels
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
     * Change the size to the new size
     * @param newHeight the new height in pixels. newHeight must be >=0.
     * @param newWidht the new width in pixels. newWidht must be >=0.
     */
    public void changeSize(int newHeight, int newWidth) {
        erase();
        //if(newHeight < 0){
            //height = newHeight * -1;
        //}
        //else{
            //height = newHeight;
        //}
        height = newHeight;
        width = newWidth;
        draw();
    }
    
    /**
     * Change the color. 
     * @param color the new color. Valid colors are "red", "yellow", "blue", "green",
     * "magenta" and "black".
     */
    public void changeColor(String newColor){
        color = newColor;
        draw();
    }
 
    /*
     * Draw the triangle with current specifications on screen.
     */
    private void draw(){
        if(isVisible) {
            Canvas canvas = Canvas.getCanvas();
            int[] xpoints = { xPosition, xPosition + (width/2), xPosition - (width/2) };
            int[] ypoints = { yPosition, yPosition + height, yPosition + height };
            canvas.draw(this, color, new Polygon(xpoints, ypoints, 3));
            canvas.wait(10);
        }
    }
 
    /*
     * Erase the triangle on screen.
     */
    private void erase(){
        if(isVisible) {
            Canvas canvas = Canvas.getCanvas();
            canvas.erase(this);
        }
    }
}