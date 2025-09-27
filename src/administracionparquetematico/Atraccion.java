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
    private int edadMin;
    private int alturaMinCm;
    private int duracion;
    private int contadorCodigosReserva = 1;
    private List<Reserva> reservas;
    
    //Constructor
    public Atraccion(int codigo, String nombre, String descripcion, 
            int cantidadMax, String apertura, String cierre, int edadMin, int alturaMinCm, int duracion) {
        this.codigo = codigo;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.cantidadMax = cantidadMax;
        this.horaApertura = LocalTime.parse(apertura);
        this.horaCierre = LocalTime.parse(cierre);
        this.edadMin = edadMin;
        this.alturaMinCm = alturaMinCm;
        this.duracion = duracion;
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
    
    public int getEdad() { return edadMin; }
    public void setEdad(int edadMin) { this.edadMin = edadMin; }
    
    public int getAltura() { return alturaMinCm; }
    public void setAltura(int alturaMinCm) { this.alturaMinCm = alturaMinCm; }
    
    public int getDuracion() { return duracion; }
    public void setDuracion(int duracion) { this.duracion = duracion; }
    
    public List<Reserva> getReservas() {
        return Collections.unmodifiableList(reservas);
    }
    public void setReservas(List<Reserva> reservas) {
        this.reservas = new ArrayList<>(reservas);
    }
    
    public void inicializarContadorReservas() {
        if (reservas.isEmpty()) {
            this.contadorCodigosReserva = 1;
        } else {
            // Busca el código máximo en la lista de reservas
            int maxCodigo = 0;
            for (Reserva r : reservas) {
                if (r.getCodigoR() > maxCodigo) {
                    maxCodigo = r.getCodigoR();
                }
            }
            this.contadorCodigosReserva = maxCodigo + 1;
        }
    }
    
    public Reserva crearNuevaReserva(String fecha, LocalTime hora, List<Persona> grupoInicial) throws CapacidadExcedidaException {
    
        // VALIDACIÓN 1: Horario válido
        if (duracion > 0 && hora.getMinute() % this.duracion != 0) {
            // Esta puede seguir siendo una IllegalArgumentException, ya que es un error de "parámetro inválido"
            throw new IllegalArgumentException("La hora de la reserva (" + hora + ") no es un horario válido para esta atracción.");
        }
    
        // VALIDACIÓN 2: Capacidad
        int numeroDePersonas = grupoInicial.size();
        int capacidadOcupada = getCapacidadOcupadaEn(hora);
        if ((capacidadOcupada + numeroDePersonas) > this.cantidadMax) {
            // AQUÍ ES DONDE LANZAS TU EXCEPCIÓN PERSONALIZADA
            throw new CapacidadExcedidaException("Capacidad excedida. Solo quedan " + (this.cantidadMax - capacidadOcupada) + " cupos para esa hora.");
        }
    
        // Si todo está bien, se crea la reserva
        int nuevoCodigoReserva = contadorCodigosReserva++;
        Reserva nuevaReserva = new Reserva(nuevoCodigoReserva, this.nombre, fecha, hora, numeroDePersonas);
    
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