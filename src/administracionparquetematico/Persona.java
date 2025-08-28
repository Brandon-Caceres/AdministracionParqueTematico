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
    //Atributos
    private String nombre;
    private int edad;
    private int altura;
    private int peso;
    
    //Constructor
    public Persona(String nombre, int edad, int altura, int peso){
        this.nombre = nombre;
        this.edad = edad;
        this.altura = altura;
        this.peso = peso;
    }
    
    //Metodos
    public String getNombre() { return nombre;}
    public void setNombre(String nombre) { this.nombre = nombre; }
    
    public int getEdad() { return edad; }
    public void setEdad(int edad) { this.edad = edad; }

    public int getAltura() { return altura; }
    public void setAltura(int altura) { this.altura = altura; }

    public int getPeso() { return peso; }
    public void setPeso(int peso) { this.peso = peso; }
    
    @Override
    public String toString() {
        return "Nombre: " + nombre + ", Edad: " + edad + ", Altura: " + altura + ", Peso: " + peso;
    }
    
    public void informacionPersona(Persona persona){
        System.out.println(persona);
    }
}