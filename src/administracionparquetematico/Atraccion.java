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

public class Atraccion{
    //Atributos
    private int codigo;
    private String nombre;
    private String descripcion;
    private int cantidadMax;
    private List<Reserva> reservas;
    
    //Constructor
    public Atraccion(int codigo, String nombre, String descripcion, int cantidadMax) {
        this.codigo = codigo;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.cantidadMax = cantidadMax;
        this.reservas = new ArrayList<>();
    }
    
    //Metodos
    public int getCodigo() { return codigo; }
    public void setCodigo(int codigo) { this.codigo = codigo; }
    
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }

    public int getCantidadMax() { return cantidadMax; }
    public void setCantidadMax(int cantidadMax) { this.cantidadMax = cantidadMax; }
    
    public List<Reserva> getReservas() {return reservas;}
    public void setReservas(List<Reserva> reservas) {this.reservas = reservas;}
    
    public void agregarReserva(Reserva reserva){
        reservas.add(reserva);
    }
    
    public void eliminarReserva(int codigo) {
        reservas.removeIf(r -> r.getCodigoR() == codigo);
    }
    
    public void eliminarReserva(Reserva r) {
        reservas.remove(r);
    }
    
    public Reserva buscarReserva(int codigo) {
        for(Reserva r : reservas) if(r.getCodigoR() == codigo) return r;
        return null;
    }

    public void listarReservas() {
        for(Reserva r : reservas) System.out.println(r);
    }

    @Override
    public String toString() {
        return codigo + " - " + nombre + " (" + descripcion + "), cap: " + cantidadMax 
                + ", reservas: " + reservas.size();
    }
}