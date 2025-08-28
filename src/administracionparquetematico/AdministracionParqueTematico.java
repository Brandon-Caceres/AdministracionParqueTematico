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
        int opcion;
        do {
            System.out.println("=== Menú Parque ===");
            System.out.println("1. Ver atracciones");
            System.out.println("2. Administrtar atracciones");
            System.out.println("3. Estadisticas");
            System.out.println("4. Salir");
            opcion = sc.nextInt();

            switch(opcion) {
                case 1:
                    // lógica para mostrar atracciones(submenu)
                    break;
                case 2:
                    // administrar atracciones(submenu)
                    break;
                case 3:
                    //estadisticas(submenu)
                    break;
                case 4:
                    System.out.println("Saliendo del sistema...");
                    break;
                default:
                    System.out.println("Opcion invalida");
                    break;
            }
        } while(opcion != 4);
    }
}
