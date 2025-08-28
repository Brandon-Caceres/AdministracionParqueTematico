/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package administracionparquetematico;

/**
 *
 * @author Brandon
 */

public class Persona{
    private int edad;
    private int altura;
    private int peso;
    
    public Persona(int edad, int altura, int peso){
        this.edad = edad;
        this.altura = altura;
        this.peso = peso;
    }

    public int getEdad() { return edad; }
    public void setEdad(int edad) { this.edad = edad; }

    public int getAltura() { return altura; }
    public void setAltura(int altura) { this.altura = altura; }

    public int getPeso() { return peso; }
    public void setPeso(int peso) { this.peso = peso; }
}
