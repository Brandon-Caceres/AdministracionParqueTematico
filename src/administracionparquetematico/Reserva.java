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

public class Reserva{
    private int codigoR;
    private Atracciones atraccion;
    private List<Persona> grupo;
    
    public Reserva(int codigoR, Atracciones atraccion){
        this.codigoR = codigoR;
        this.atraccion = atraccion;
        this.grupo = new ArrayList<>();
    }
    
    public int gedCodigoR(){return codigoR;}
    public void setCodigoR(int codigoR){this.codigoR = codigoR;}
    
    public Atracciones getAtraccion(){return atraccion;}
    public void setAtraccion(Atracciones atraccion) {this.atraccion = atraccion;}
    
    public List<Persona> getGrupo() {return grupo;}
    public void setGrupo(Persona p) {grupo.add(p);}
    
    public void agregarPersona(Persona p) {
        grupo.add(p);
    }

    public void agregarPersonas(List<Persona> personas) {
        grupo.addAll(personas);
    }
}