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
    public Map<Integer, Atraccion> getAtracciones() {return atracciones;}
    public void setAtracciones(Map<Integer, Atraccion> atracciones) {this.atracciones = atracciones;}
    
}