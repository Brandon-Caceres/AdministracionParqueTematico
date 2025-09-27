/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package com;

/**
 *
 * @author Brandon
 */
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import java.io.File;

public class AdministracionParqueTematicoFX extends Application {
    private static Parque parque = new Parque();
    private static final String DIRECTORIO_DATOS = "datos_parque"; // Carpeta para guardar los CSV

    // El método start es el punto de entrada para la UI de JavaFX
    @Override
    public void start(Stage primaryStage) {
        // --- Configuración de la Ventana Principal (Stage) ---
        primaryStage.setTitle("Administración del Parque Temático");

        // --- Layout Principal y Estilos ---
        VBox root = new VBox(20); // VBox apila elementos verticalmente con un espacio de 20px
        root.setAlignment(Pos.CENTER);
        root.setPadding(new Insets(30));
        StyleManager.applyBackground(root); // Aplicamos el fondo oscuro

        // --- Componentes con Estilo ---
        Label welcomeLabel = StyleManager.createTitleLabel("Sistema de Gestión");
        
        Button btnGestionAtracciones = StyleManager.createStyledButton("Gestión de Atracciones");
        Button btnGestionReservas = StyleManager.createStyledButton("Gestión de Reservas");
        Button btnGenerarReporte = StyleManager.createStyledButton("Generar Reporte TXT");

        // --- Acciones de los Botones ---
        btnGestionAtracciones.setOnAction(e -> {
            GestionAtraccionesDialogFX dialog = new GestionAtraccionesDialogFX(primaryStage, parque);
            dialog.show();
        });

        btnGestionReservas.setOnAction(e -> {
            GestionReservasDialogFX dialog = new GestionReservasDialogFX(primaryStage, parque);
            dialog.show();
        });
        
        btnGenerarReporte.setOnAction(e -> {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Guardar Reporte");
            fileChooser.setInitialFileName("reporte_parque_tematico.txt");
            
            // Añadir filtro para que solo muestre archivos .txt
            FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("Archivos de Texto (*.txt)", "*.txt");
            fileChooser.getExtensionFilters().add(extFilter);

            File archivoParaGuardar = fileChooser.showSaveDialog(primaryStage);

            if (archivoParaGuardar != null) {
                try {
                    PersistenciaParque.generarReporteTXT(parque, archivoParaGuardar);
                    mostrarAlerta(Alert.AlertType.INFORMATION, "Reporte Generado",
                        "Reporte generado exitosamente en:\n" + archivoParaGuardar.getAbsolutePath());
                } catch (Exception ex) {
                    mostrarAlerta(Alert.AlertType.ERROR, "Error",
                        "Ocurrió un error al generar el reporte.");
                }
            }
        });

        // --- Guardar Datos al Cerrar ---
        primaryStage.setOnCloseRequest(event -> {
            System.out.println("Guardando datos antes de salir...");
            PersistenciaParque.guardarDatos(parque, DIRECTORIO_DATOS);
            System.out.println("Datos guardados. Saliendo.");
        });

        // --- Montaje Final ---
        root.getChildren().addAll(welcomeLabel, btnGestionAtracciones, btnGestionReservas, btnGenerarReporte);
        Scene scene = new Scene(root, 500, 350);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    // El método main ahora es muy simple
    public static void main(String[] args) {
        // 1. CARGAR DATOS
        PersistenciaParque.cargarDatos(parque, DIRECTORIO_DATOS);
    
        // 2. ACTUALIZAR EL CONTADOR (ESTA ES LA LÍNEA QUE FALTA)
        parque.actualizarContadorAtracciones();
    
        // 3. LANZAR LA APLICACIÓN JAVAFX
        launch(args);
    }
    
    // Método de utilidad para mostrar alertas
    private void mostrarAlerta(Alert.AlertType tipo, String titulo, String contenido) {
        Alert alert = new Alert(tipo);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(contenido);
        alert.showAndWait();
    }
}