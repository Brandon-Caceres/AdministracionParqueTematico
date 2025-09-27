/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com;

/**
 *
 * @author Brandon
 */

public class Persona{
    //Atributos
    private String nombre;
    private int edad;
    private int altura;
    
    //Constructor
    public Persona(String nombre, int edad , int altura){
        this.nombre = nombre;
        this.edad = edad;
        this.altura = altura;
    }
    
    //Metodos
    public String getNombre() { return nombre;}
    public void setNombre(String nombre) { this.nombre = nombre; }
    
    public int getEdad() { return edad; }
    public void setEdad(int edad) { this.edad = edad; }

    public int getAltura() { return altura; }
    public void setAltura(int altura) { this.altura = altura; }
    
    @Override
    public String toString() {
        return "Nombre: " + nombre + ", Edad: " + edad + ", Altura: " + altura;
    }
    
    public void informacionPersona(){
        System.out.println(this);
    }
}