/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com;

/**
 *
 * @author Brandon
 */
import java.util.*;

public class Parque{
    //Atributo
    private Map<Integer, Atraccion> atracciones;
    private int contadorCodigosAtraccion = 1;
    
    //Constructor
    public Parque() {
        atracciones = new HashMap<>();
    }
    
    //Metodos
    public Map<Integer, Atraccion> getAtracciones() {
        return new HashMap<>(atracciones);
    }
    
    public Collection<Atraccion> getAtraccionesCollection() {
        return Collections.unmodifiableCollection(atracciones.values());
    }
    
    public void setAtracciones(Map<Integer, Atraccion> atracciones) {
        if (atracciones == null) {
            throw new IllegalArgumentException("El Map de atracciones no puede ser nulo.");
        }
        this.atracciones = new HashMap<>(atracciones);
    }
    
    public void agregarAtraccion(String nombre, String descripcion, int capacidad, String apertura, String cierre, int edadMin, int alturaMin, int duracion) {
        int nuevoCodigo = contadorCodigosAtraccion++;
        
        Atraccion nuevaAtraccion = new Atraccion(nuevoCodigo, nombre, descripcion, capacidad, apertura, cierre, edadMin, alturaMin, duracion);
        atracciones.put(nuevoCodigo, nuevaAtraccion);
    }
    
    public void agregarAtraccion(Atraccion nueva){
        atracciones.put(nueva.getCodigo() , nueva);
    }
    
    public Atraccion buscarAtraccion(int codigo) {
        return atracciones.get(codigo);
    }

    public boolean eliminarAtraccion(int codigo) {
        return atracciones.remove(codigo) != null;
    }
    
    public void actualizarContadorAtracciones() {
        if (this.atracciones.isEmpty()) {
            this.contadorCodigosAtraccion = 1; // O el valor inicial que uses
            return;
        }

        // Busca el código más alto entre todas las atracciones existentes
        int maxCodigo = 0;
        for (Atraccion a : this.atracciones.values()) {
            if (a.getCodigo() > maxCodigo) {
                maxCodigo = a.getCodigo();
            }
        }
    
        // Establece el próximo código a ser uno más que el máximo encontrado
        this.contadorCodigosAtraccion = maxCodigo + 1;
        System.out.println("Contador de atracciones actualizado. Próximo código: " + this.contadorCodigosAtraccion);
    }

    public void listarAtracciones() {
        for(Atraccion a : atracciones.values()) {
            System.out.println(a);
        }
    }
}