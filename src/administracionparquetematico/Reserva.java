/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package administracionparquetematico;

/**
 *
 * @author Brandon
 */
import java.time.*;
import java.util.*;

public class Reserva{
    //Atributos
    private int codigoR;
    private String atraccion;
    private String fecha;
    private LocalTime hora;
    private List<Persona> grupo;
    private int numeroPersonas;
    
    //Constructor
    public Reserva(int codigoR, String atraccion ,String fecha, LocalTime horaActual, int numeroPersonas){
        this.codigoR = codigoR;
        this.atraccion = atraccion;
        this.fecha = fecha;
        this.hora = horaActual;
        this.numeroPersonas = numeroPersonas;
        this.grupo = new ArrayList<>();
    }
    
    //Metodos
    public int getCodigoR(){return codigoR;}
    public void setCodigoR(int codigoR){this.codigoR = codigoR;}
    
    public String getAtraccion(){return atraccion;}
    public void setAtraccion(String atraccion) {this.atraccion = atraccion;}
    
    public String getFecha(){return fecha;}
    public void setFecha(String fecha) {this.fecha = fecha;}
    
    public LocalTime getHora(){return hora;}
    public void setHora(String nueva){this.hora = LocalTime.parse(nueva);}
    
    public int getNumeroPersonas() {return numeroPersonas;}
    public void setNumeroPersonas(int numeroPersonas){this.numeroPersonas = numeroPersonas;}
    
    public List<Persona> getGrupo() {
        return new ArrayList<>(grupo);
    }
    
    public void setGrupo(List<Persona> grupo) {
        if (grupo == null) {
            throw new IllegalArgumentException("La lista de personas no puede ser nula.");
        }
        this.grupo = new ArrayList<>(grupo);
    }
    
    public void limpiarGrupo() {
        this.grupo.clear();
    }
    
    /**
    * Agrega una persona, verificando que no se exceda el tamaño de la reserva.
    * @param p La persona a agregar.
    * @throws ReservaCompletaException si la reserva ya está llena.
    */
    public void agregarPersona(Persona p) throws ReservaCompletaException {
        if (grupo.size() >= numeroPersonas) {
            throw new ReservaCompletaException("La reserva ya está completa. No se pueden añadir más personas.");
        }
        if (p == null) {
            throw new IllegalArgumentException("No se puede agregar una persona nula.");
        }
        grupo.add(p);
    }
    
    public void agregarPersona(List<Persona> personas) {
        if (personas == null || personas.contains(null)) {
            throw new IllegalArgumentException("La lista de personas no puede contener elementos nulos.");
        }
        grupo.addAll(personas);
    }
    
    public void agregarPersona(String nombre, int edad, int altura) {
        if (nombre == null || nombre.trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre no puede ser nulo o vacío.");
        }
        grupo.add(new Persona(nombre, edad, altura)); 
    }
    
    public void eliminarPersona(String nombre) {
        boolean eliminada = grupo.removeIf(p -> p.getNombre().equals(nombre));
        
        if (eliminada) {
            System.out.println("La persona " + nombre + " ha sido eliminada de la reserva");
        } else {
            System.out.println("No se encontró persona con nombre " + nombre);
        }
    }
    
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Reserva ").append(codigoR)
        .append(" | Atracción: ").append(atraccion)
        .append(" | Fecha: ").append(fecha)
        .append(" | Hora: ").append(hora)
        .append(" | Personas: ").append(grupo.size()).append("\n");

        for (Persona p : grupo) {
            sb.append("   - ").append(p.getNombre())
            .append(", Edad: ").append(p.getEdad()).append("\n");
        }

        return sb.toString();
    }
}