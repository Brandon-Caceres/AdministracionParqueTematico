/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package administracionparquetematico;

/**
 *
 * @author Brandon
 */
import java.util.*;

public class AdministracionParqueTematico {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        Parque parque = new Parque();
        
        parque.cargarDatosIniciales();
        
        int opcion;
        do {
            System.out.println("=== Menú Parque ===");
            System.out.println("1. Gestión de atracciones");
            System.out.println("2. Gestión de reservas");
            System.out.println("3. Salir");
            opcion = sc.nextInt();
            sc.nextLine();

            switch(opcion) {
                case 1:
                    menuGestionAtracciones(parque, sc);
                    break;
                case 2:
                    menuGestionReservas(parque, sc);
                    break;
                case 3:
                    System.out.println("Saliendo del sistema...");
                    break;
                default:
                    System.out.println("Opcion invalida");
                    break;
            }
        } while(opcion != 3);
    }
    
    private static void menuGestionAtracciones(Parque parque, Scanner sc){
        int opcion;
        do {
            System.out.println("\n=== Gestión de Atracciones ===");
            System.out.println("1. Ver atracciones");
            System.out.println("2. Agregar atracción");
            System.out.println("3. Buscar atracción por código");
            System.out.println("4. Eliminar atracción");
            System.out.println("5. Volver");
            System.out.print("Elige una opción: ");
            opcion = sc.nextInt();
            sc.nextLine();

            switch (opcion) {
                case 1:{
                    parque.listarAtracciones();
                    break;
                }
                case 2: {
                    System.out.print("Código: ");
                    int codigo = sc.nextInt();
                    sc.nextLine();
                    System.out.print("Nombre: ");
                    String nombre = sc.nextLine();
                    System.out.print("Descripción: ");
                    String descripcion = sc.nextLine();
                    System.out.print("Capacidad máxima: ");
                    int capacidad = sc.nextInt();
                    sc.nextLine();
                    parque.agregarAtraccion(codigo, nombre, descripcion, capacidad);
                    break;
                }
                case 3: {
                    System.out.print("Código de atracción: ");
                    int codigo = sc.nextInt();
                    sc.nextLine();
                    Atraccion atraccion = parque.buscarAtraccion(codigo);
                    if (atraccion != null) {
                        System.out.println("Atracción encontrada: " + atraccion);
                    } 
                    else {
                        System.out.println("No se encontró la atracción.");
                    }
                    break;
                }
                case 4: {
                    System.out.print("Código de atracción a eliminar: ");
                    int codigo = sc.nextInt();
                    sc.nextLine();
                    parque.eliminarAtraccion(codigo);
                    break;
                }
                case 5: 
                    System.out.println("Volviendo al menú principal...");
                    break;
                default:
                    System.out.println("Opción no válida.");
                    break;

            }
        } while (opcion != 5);
    }
    
    private static void menuGestionReservas(Parque parque, Scanner sc) {
    int opcion;
        do {
            System.out.println("\n=== Gestión de Reservas ===");
            System.out.println("1. Insertar reserva en una atracción");
            System.out.println("2. Listar reservas de una atracción");
            System.out.println("3. Eliminar reserva de una atracción");
            System.out.println("4. Agregar personas a una reserva");
            System.out.println("5. Volver");
            System.out.print("Elige una opción: ");
            opcion = sc.nextInt();
            sc.nextLine();

            switch (opcion) {
                case 1: {
                    System.out.print("Código de atracción: ");
                    int codigoAtr = sc.nextInt();
                    sc.nextLine();
                    Atraccion atr = parque.buscarAtraccion(codigoAtr);
                    if (atr != null) {
                        System.out.print("Código de reserva: ");
                        int codReserva = sc.nextInt();
                        sc.nextLine();
                        System.out.print("Fecha de reserva: ");
                        String fecha = sc.nextLine();
                        System.out.print("Nombre de la persona responsable: ");
                        String nombre = sc.nextLine();
                        System.out.print("Edad de la persona: ");
                        int edad = sc.nextInt();
                        sc.nextLine();
                        Reserva r = new Reserva(codReserva, atr.getNombre(), fecha); // si manejas fecha, agregarla
                        r.agregarPersona(new Persona(nombre, edad));
                        atr.agregarReserva(r);
                    } 
                    else {
                        System.out.println("Atracción no encontrada.");
                    }
                    break;
                }
                case 2: {
                    System.out.print("Código de atracción: ");
                    int codigoAtr = sc.nextInt();
                    sc.nextLine();
                    Atraccion atr = parque.buscarAtraccion(codigoAtr);
                    if (atr != null) {
                        atr.listarReservas();
                    } 
                    else {
                        System.out.println("Atracción no encontrada.");
                    }
                    break;
                }
                case 3: {
                    System.out.print("Código de atracción: ");
                    int codigoAtr = sc.nextInt();
                    sc.nextLine();
                    Atraccion atr = parque.buscarAtraccion(codigoAtr);
                    if (atr != null) {
                        System.out.print("Código de reserva a eliminar: ");
                        int codReserva = sc.nextInt();
                        sc.nextLine();
                        atr.eliminarReserva(codReserva);
                    } 
                    else {
                        System.out.println("Atracción no encontrada.");
                    }
                    break;
                }
                case 4: {
                    System.out.print("Código de atracción: ");
                    int codigoAtr = sc.nextInt();
                    sc.nextLine();
                    Atraccion atr = parque.buscarAtraccion(codigoAtr);
                    if (atr != null) {
                        System.out.print("Código de reserva: ");
                        int codReserva = sc.nextInt();
                        sc.nextLine();
                        for (Reserva r : atr.getReservas()) {
                            if (r.getCodigoR() == codReserva) {
                                System.out.print("Nombre de la persona: ");
                                String nombre = sc.nextLine();
                                System.out.print("Edad de la persona: ");
                                int edad = sc.nextInt();
                                sc.nextLine();
                                r.agregarPersona(new Persona(nombre, edad));
                            }
                        }
                    } 
                    else {
                        System.out.println("Atracción no encontrada.");
                    }
                    break;
                }
                case 5:
                    System.out.println("Volviendo al menú principal...");
                    break;
                default:
                    System.out.println("Opción no válida.");
            }
        } while (opcion != 5);
    }
}