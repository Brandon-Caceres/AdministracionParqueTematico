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
    public Map<Integer, Atraccion> getAtracciones() {return Collections.unmodifiableMap(atracciones);}
    public void setAtracciones(Map<Integer, Atraccion> atracciones) {this.atracciones = atracciones;}
    
    public void agregarAtraccion(Atraccion atraccion){
        atracciones.put(atraccion.getCodigo(), atraccion);
    }
    
    public void eliminarAtraccion(int codigo) {
    if(atracciones.remove(codigo) != null){
        System.out.println("Atracción con código " + codigo + " eliminada.");
    } else {
        System.out.println("No se encontró atracción con código " + codigo);
    }
}
    
    public Atraccion buscarAtraccion(int codigo) {
        return atracciones.get(codigo);
    }
}