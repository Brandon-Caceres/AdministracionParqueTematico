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
    //Atributos
    private int codigoR;
    private Atraccion atraccion;
    private List<Persona> grupo;
    
    //Constructor
    public Reserva(int codigoR, Atraccion atraccion){
        this.codigoR = codigoR;
        this.atraccion = atraccion;
        this.grupo = new ArrayList<>();
    }
    
    //Metodos
    public int getCodigoR(){return codigoR;}
    public void setCodigoR(int codigoR){this.codigoR = codigoR;}
    
    public Atraccion getAtraccion(){return atraccion;}
    public void setAtraccion(Atraccion atraccion) {this.atraccion = atraccion;}
    
    public List<Persona> getGrupo() {return grupo;}
    public void setGrupo(List<Persona> grupo) {this.grupo = grupo;}
    
    public void agregarPersona(Persona p) {
        grupo.add(p);
    }

    public void agregarPersonas(List<Persona> personas) {
        grupo.addAll(personas);
    }
}