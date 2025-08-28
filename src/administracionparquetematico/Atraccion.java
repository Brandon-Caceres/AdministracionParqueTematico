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
        if(reserva.getAtraccion() != this){
            reserva.setAtraccion(this);
        }
    }
    
    public void eliminarReserva(int codigo){
        for(Iterator<Reserva> it = reservas.iterator(); it.hasNext();){
            Reserva reserva = it.next();
            if(reserva.getCodigoR() == codigo){
                it.remove();
                System.out.println("Reserva con código " + codigo + " eliminada.");
                return;
            }
        }
        System.out.println("No se encontró reserva con código " + codigo);
    }
}