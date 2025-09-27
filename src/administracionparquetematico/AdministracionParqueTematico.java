/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package administracionparquetematico;

/**
 *
 * @author Brandon
 */
import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class AdministracionParqueTematico {
    private static Parque parque = new Parque();
    private static final String DIRECTORIO_DATOS = "datos_parque"; // Carpeta para guardar los CSV

    public static void main(String[] args) {
        // --- 1. CARGAR DATOS AL INICIAR LA APLICACIÓN ---
        PersistenciaParque.cargarDatos(parque, DIRECTORIO_DATOS);

        // Intenta aplicar un estilo visual nativo del sistema operativo
        /*try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }*/
        
        //--- Crear la Ventana Principal ---
        JFrame frame = new JFrame("Administración del Parque Temático");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(500, 350);
        frame.setLocationRelativeTo(null); // Centrar la ventana

        // --- 2. GUARDAR DATOS AL CERRAR LA VENTANA ---
        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                PersistenciaParque.guardarDatos(parque, DIRECTORIO_DATOS);
                System.out.println("Datos guardados correctamente. Saliendo del sistema.");
            }
        });
        
        //--- Panel Principal y Botones ---
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        JLabel welcomeLabel = new JLabel("Sistema de Gestión", SwingConstants.CENTER);
        welcomeLabel.setFont(new Font("Arial", Font.BOLD, 24));
        panel.add(welcomeLabel, BorderLayout.NORTH);

        JPanel buttonPanel = new JPanel(new GridLayout(2, 1, 15, 15));
        JButton btnGestionAtracciones = new JButton("Gestión de Atracciones");
        JButton btnGestionReservas = new JButton("Gestión de Reservas");
        
        btnGestionAtracciones.setFont(new Font("Arial", Font.PLAIN, 18));
        btnGestionReservas.setFont(new Font("Arial", Font.PLAIN, 18));
        
        buttonPanel.add(btnGestionAtracciones);
        buttonPanel.add(btnGestionReservas);
        panel.add(buttonPanel, BorderLayout.CENTER);

        //--- Acciones de los Botones ---
        btnGestionAtracciones.addActionListener(e -> {
            GestionAtraccionesDialog dialog = new GestionAtraccionesDialog(frame, parque);
            dialog.setVisible(true);
        });

        btnGestionReservas.addActionListener(e -> {
            GestionReservasDialog dialog = new GestionReservasDialog(frame, parque);
            dialog.setVisible(true);
        });

        frame.add(panel);
        frame.setVisible(true);
    }
}