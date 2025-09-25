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

public class Atraccion{
    //Atributos
    private int codigo;
    private String nombre;
    private String descripcion;
    private int cantidadMax;
    LocalTime horaApertura;
    LocalTime horaCierre;
    private List<Reserva> reservas;
    private int contadorCodigosReserva = 1;
    
    //Constructor
    public Atraccion(int codigo, String nombre, String descripcion, int cantidadMax, String apertura, String cierre) {
        this.codigo = codigo;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.cantidadMax = cantidadMax;
        this.horaApertura = LocalTime.parse(apertura);
        this.horaCierre = LocalTime.parse(cierre);
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
    
    public LocalTime getApertura(){return horaApertura;}
    public void setApertura(String nueva){this.horaApertura = LocalTime.parse(nueva);}
    
    public LocalTime getCierre(){return horaCierre;}
    public void setCierre(String nueva){this.horaCierre = LocalTime.parse(nueva);}
    
    public List<Reserva> getReservas() {
        return Collections.unmodifiableList(reservas);
    }
    public void setReservas(List<Reserva> reservas) {
        this.reservas = new ArrayList<>(reservas);
    }
    
    public Reserva crearNuevaReserva(String fecha, String hora, List<Persona> grupoInicial) {
        LocalTime horaReserva = LocalTime.parse(hora);
        int numeroDePersonas = grupoInicial.size();

        // 1. VERIFICAR CAPACIDAD DISPONIBLE
        int capacidadOcupada = getCapacidadOcupadaEn(horaReserva);
        if ((capacidadOcupada + numeroDePersonas) > this.cantidadMax) {
            throw new IllegalArgumentException("Capacidad excedida. Solo quedan " + (this.cantidadMax - capacidadOcupada) + " cupos para esa hora.");
        }
        // 2. Si hay capacidad, se crea la reserva
        int nuevoCodigoReserva = contadorCodigosReserva++;
        Reserva nuevaReserva = new Reserva(nuevoCodigoReserva, this.nombre, fecha, hora, numeroDePersonas);
        // Se agrega el grupo completo a la reserva
        for(Persona p : grupoInicial){
            nuevaReserva.agregarPersona(p);
        }
        
        this.reservas.add(nuevaReserva);
        return nuevaReserva;
    }
    
    public int getCapacidadOcupadaEn(LocalTime hora) {
        int totalPersonas = 0;
        for (Reserva r : this.reservas) {
            if (r.getHora().equals(hora)) {
                totalPersonas += r.getNumeroPersonas();
            }
        }
        return totalPersonas;
    }
    
    public void agregarReserva(Reserva reserva) {
        if (buscarReserva(reserva.getCodigoR()) != null) {
            throw new IllegalArgumentException("Ya existe una reserva con el código " + reserva.getCodigoR());
        }
        reservas.add(reserva);
    }
    
    public void eliminarReserva(int codigo) {
        reservas.removeIf(r -> r.getCodigoR() == codigo);
    }
    
    public void eliminarReserva(Reserva r) {
        reservas.remove(r);
    }
    
    public Reserva buscarReserva(int codigo) {
        for (Reserva r : reservas) {
            if (r.getCodigoR() == codigo) {
                return r;
            }
        }
        return null;
    }

    public void listarReservas() {
        for(Reserva r : reservas) System.out.println(r);
    }
    
    public boolean estaAbierta(LocalTime hora){
        if (horaCierre.isAfter(horaApertura)){
            return !hora.isBefore(horaApertura) && hora.isBefore(horaCierre);
        }
        else{
            return !hora.isBefore(horaApertura) || hora.isBefore(horaCierre);
        }
    }

    @Override
    public String toString() {
        return codigo + " - " + nombre + " (" + descripcion + "), cap: " + cantidadMax 
                + ", reservas: " + reservas.size();
    }
}