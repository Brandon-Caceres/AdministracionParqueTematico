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
    private String atraccion;
    private String fecha;
    private List<Persona> grupo;
    
    //Constructor
    public Reserva(int codigoR, String atraccion ,String fecha){
        this.codigoR = codigoR;
        this.atraccion = atraccion;
        this.fecha = fecha;
        this.grupo = new ArrayList<>();
    }
    
    //Metodos
    public int getCodigoR(){return codigoR;}
    public void setCodigoR(int codigoR){this.codigoR = codigoR;}
    
    public String getAtraccion(){return atraccion;}
    public void setAtraccion(String atraccion) {this.atraccion = atraccion;}
    
    public String getFecha(){return fecha;}
    public void setFecha(String fecha) {this.fecha = fecha;}
    
    public List<Persona> getGrupo() {return grupo;}
    public void setGrupo(List<Persona> grupo) {this.grupo = grupo;}
    
    public void agregarPersona(Persona p) {
        grupo.add(p);
    }

    public void agregarPersona(List<Persona> personas) {
        grupo.addAll(personas);
    }
    
    public void agregarPersona(String nombre, int edad/*, int altura, int peso*/) { 
        grupo.add(new Persona(nombre, edad/*, altura, peso*/)); 
    }
    
    public void eliminarPersona(String name){
        for(Iterator<Persona> it = grupo.iterator(); it.hasNext();){
            Persona persona = it.next();
            if(persona.getNombre().equals(name)){
                it.remove();
                System.out.println("La persona " + name + " ha sido eliminada de la reserva");
                return;
            }
        }
        System.out.println("No se encontró persona con nombre " + name);
    }
    
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Reserva ").append(codigoR)
        .append(" | Atracción: ").append(atraccion)
        .append(" | Fecha: ").append(fecha)
        .append(" | Personas: ").append(grupo.size()).append("\n");

        for (Persona p : grupo) {
            sb.append("   - ").append(p.getNombre())
            .append(", Edad: ").append(p.getEdad()).append("\n");
        }

        return sb.toString();
    }
}