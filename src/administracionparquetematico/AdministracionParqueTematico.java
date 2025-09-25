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

public class AdministracionParqueTematico {
    // El objeto Parque será la única fuente de datos para toda la aplicación.
    private static Parque parque = new Parque();

    public static void main(String[] args) {
        // Intenta aplicar un look and feel más moderno, nativo del sistema operativo.
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Crear la ventana principal
        JFrame frame = new JFrame("Administración del Parque Temático");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(500, 350);
        frame.setLocationRelativeTo(null); // Centrar en la pantalla

        // Panel de bienvenida y botones
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        JLabel welcomeLabel = new JLabel("Sistema de Gestión", SwingConstants.CENTER);
        welcomeLabel.setFont(new Font("Arial", Font.BOLD, 24));
        panel.add(welcomeLabel, BorderLayout.NORTH);

        // Panel para los botones
        JPanel buttonPanel = new JPanel(new GridLayout(2, 1, 15, 15));
        JButton btnGestionAtracciones = new JButton("Gestión de Atracciones");
        JButton btnGestionReservas = new JButton("Gestión de Reservas");
        
        btnGestionAtracciones.setFont(new Font("Arial", Font.PLAIN, 18));
        btnGestionReservas.setFont(new Font("Arial", Font.PLAIN, 18));
        
        buttonPanel.add(btnGestionAtracciones);
        buttonPanel.add(btnGestionReservas);
        panel.add(buttonPanel, BorderLayout.CENTER);

        // Acción para el botón de atracciones: abre el diálogo de gestión de atracciones.
        btnGestionAtracciones.addActionListener(e -> {
            GestionAtraccionesDialog dialog = new GestionAtraccionesDialog(frame, parque);
            dialog.setVisible(true);
        });

        // Acción para el botón de reservas: abre el diálogo de gestión de reservas.
        btnGestionReservas.addActionListener(e -> {
            GestionReservasDialog dialog = new GestionReservasDialog(frame, parque);
            dialog.setVisible(true);
        });

        frame.add(panel);
        frame.setVisible(true);
    }
}