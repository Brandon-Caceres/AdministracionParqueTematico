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
    private List<Atracciones> atracciones;
    private List<Reserva> reservas;
    
    public Parque() {
        atracciones = new ArrayList<>();
        reservas = new ArrayList<>();
    }

    public void agregarAtraccion(Atracciones a) {atracciones.add(a);}
    public void agregarReserva(Reserva r) {reservas.add(r);}

    public List<Atracciones> getAtracciones() {return atracciones;}
    public List<Reserva> getReservas() {return reservas;}
}