/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com;

/**
 *
 * @author Brandon
 */
import java.util.*;

public class Parque{
    //Atributo
    private Map<Integer, Atraccion> atracciones;
    private int contadorCodigosAtraccion = 1;
    
    //Constructor
    public Parque() {
        atracciones = new HashMap<>();
    }
    
    //Metodos
    public Map<Integer, Atraccion> getAtracciones() {
        return new HashMap<>(atracciones);
    }
    
    public Collection<Atraccion> getAtraccionesCollection() {
        return Collections.unmodifiableCollection(atracciones.values());
    }
    
    public void setAtracciones(Map<Integer, Atraccion> atracciones) {
        if (atracciones == null) {
            throw new IllegalArgumentException("El Map de atracciones no puede ser nulo.");
        }
        this.atracciones = new HashMap<>(atracciones);
    }
    
    public void agregarAtraccion(String nombre, String descripcion, int capacidad, String apertura, String cierre, int edadMin, int alturaMin, int duracion) {
        int nuevoCodigo = contadorCodigosAtraccion++;
        
        Atraccion nuevaAtraccion = new Atraccion(nuevoCodigo, nombre, descripcion, capacidad, apertura, cierre, edadMin, alturaMin, duracion);
        atracciones.put(nuevoCodigo, nuevaAtraccion);
    }
    
    public void agregarAtraccion(Atraccion nueva){
        atracciones.put(nueva.getCodigo() , nueva);
    }
    
    public Atraccion buscarAtraccion(int codigo) {
        return atracciones.get(codigo);
    }

    public boolean eliminarAtraccion(int codigo) {
        return atracciones.remove(codigo) != null;
    }
    
    public void actualizarContadorAtracciones() {
        if (this.atracciones.isEmpty()) {
            this.contadorCodigosAtraccion = 1; // O el valor inicial que uses
            return;
        }

        // Busca el código más alto entre todas las atracciones existentes
        int maxCodigo = 0;
        for (Atraccion a : this.atracciones.values()) {
            if (a.getCodigo() > maxCodigo) {
                maxCodigo = a.getCodigo();
            }
        }
    
        // Establece el próximo código a ser uno más que el máximo encontrado
        this.contadorCodigosAtraccion = maxCodigo + 1;
        System.out.println("Contador de atracciones actualizado. Próximo código: " + this.contadorCodigosAtraccion);
    }
    
    public String generarEstadisticas() {
        if (atracciones.isEmpty()) {
            return "No hay datos para generar estadísticas.";
        }

        int totalReservas = 0;
        int totalVisitantes = 0;
        Atraccion atraccionMasPopular = null;
        int maxReservas = -1;

        for (Atraccion atr : atracciones.values()) {
            int reservasEnAtraccion = atr.getReservas().size();
            totalReservas += reservasEnAtraccion;

            for (Reserva res : atr.getReservas()) {
                totalVisitantes += res.getGrupo().size();
            }

            if (reservasEnAtraccion > maxReservas) {
                maxReservas = reservasEnAtraccion;
                atraccionMasPopular = atr;
            }
        }

        double promedioVisitantesPorReserva = (totalReservas > 0) ? (double) totalVisitantes / totalReservas : 0;

        StringBuilder sb = new StringBuilder();
        sb.append("Estadísticas Generales del Parque\n");
        sb.append("---------------------------------\n");
        sb.append(String.format("Número Total de Atracciones: %d\n", atracciones.size()));
        sb.append(String.format("Número Total de Reservas: %d\n", totalReservas));
        sb.append(String.format("Número Total de Visitantes Registrados: %d\n", totalVisitantes));
        sb.append(String.format("Promedio de Visitantes por Reserva: %.2f\n", promedioVisitantesPorReserva));
        if (atraccionMasPopular != null) {
            sb.append(String.format("Atracción más Popular: %s (%d reservas)\n", atraccionMasPopular.getNombre(), maxReservas));
        }
    
        return sb.toString();
    }

    public void listarAtracciones() {
        for(Atraccion a : atracciones.values()) {
            System.out.println(a);
        }
    }
}