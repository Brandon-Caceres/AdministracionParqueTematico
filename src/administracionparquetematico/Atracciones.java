/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package administracionparquetematico;

/**
 *
 * @author Brandon
 */

public class Atracciones{
    private String nombre;
    private String descripcion;
    private int codigo;
    private int cantidadMax;
    private int duracion;
    private int espera;
    private Persona restriccion;
    
    public Atracciones(String nombre, String descripcion, int codigo,
                     int cantidadMax, int duracion, int espera, Persona restriccion) {
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.codigo = codigo;
        this.cantidadMax = cantidadMax;
        this.duracion = duracion;
        this.espera = espera;
        this.restriccion = restriccion;
    }
    
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }

    public int getCodigo() { return codigo; }
    public void setCodigo(int codigo) { this.codigo = codigo; }

    public int getCantidadMax() { return cantidadMax; }
    public void setCantidadMax(int cantidadMax) { this.cantidadMax = cantidadMax; }

    public int getDuracion() { return duracion; }
    public void setDuracion(int duracion) { this.duracion = duracion; }

    public int getEspera() { return espera; }
    public void setEspera(int espera) { this.espera = espera; }

    public Persona getRestriccion() { return restriccion; }
    public void setRestriccion(Persona restriccion) { this.restriccion = restriccion; }
}