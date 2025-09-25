/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package administracionparquetematico;

/**
 *
 * @author Brandon
 */
import java.util.*;

public class Parque{
    //Atributo
    private Map<Integer, Atraccion> atracciones;
    
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
    
    public void agregarAtraccion(Atraccion atraccion) {
        if (atracciones.containsKey(atraccion.getCodigo())) {
            throw new IllegalArgumentException("Ya existe una atracción con el código " + atraccion.getCodigo());
        }
        atracciones.put(atraccion.getCodigo(), atraccion);
    }
    
    public void agregarAtraccion(int codigo, String nombre, String tipo, int capacidad, String apertura, String cierre) {
        if (atracciones.containsKey(codigo)) {
            throw new IllegalArgumentException("Ya existe una atracción con el código " + codigo);
        }
        Atraccion nuevaAtraccion = new Atraccion(codigo, nombre, tipo, capacidad, apertura, cierre);
        atracciones.put(codigo, nuevaAtraccion);
    }
    
    public Atraccion buscarAtraccion(int codigo) {
        return atracciones.get(codigo);
    }

    public boolean eliminarAtraccion(int codigo) {
        return atracciones.remove(codigo) != null;
    }

    public void listarAtracciones() {
        for(Atraccion a : atracciones.values()) {
            System.out.println(a);
        }
    }
}