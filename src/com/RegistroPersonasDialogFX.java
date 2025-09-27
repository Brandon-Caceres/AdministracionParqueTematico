/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com;

/**
 *
 * @author Brandon
 */
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.converter.IntegerStringConverter;

public class RegistroPersonasDialogFX {

    private Stage stage;
    private TableView<Persona> table;
    private ObservableList<Persona> grupoData;
    private Reserva reserva;

    public RegistroPersonasDialogFX(Stage owner, Reserva reserva) {
        this.reserva = reserva;
        stage = new Stage();
        
        // --- Configuración del Stage (Ventana) ---
        stage.initOwner(owner);
        stage.initModality(Modality.APPLICATION_MODAL); // Bloquea la ventana principal
        stage.setTitle("Gestionar Personas de la Reserva #" + reserva.getCodigoR());

        // --- Layout Principal ---
        BorderPane root = new BorderPane();
        StyleManager.applyBackground(root);
        root.setPadding(new Insets(15));
        
        // --- Título Superior ---
        Label titulo = StyleManager.createTitleLabel("Edita los datos del grupo");
        titulo.setPadding(new Insets(0, 0, 15, 0));
        root.setTop(titulo);
        
        // --- Tabla Editable ---
        table = new TableView<>();
        table.setEditable(true); // ¡IMPORTANTE!
        grupoData = FXCollections.observableArrayList(reserva.getGrupo());
        
        // --- Columnas de la Tabla ---
        TableColumn<Persona, String> nombreCol = new TableColumn<>("Nombre");
        nombreCol.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        nombreCol.setCellFactory(TextFieldTableCell.forTableColumn());
        nombreCol.setOnEditCommit(event -> {
            Persona p = event.getRowValue();
            p.setNombre(event.getNewValue());
        });
        nombreCol.setPrefWidth(250); // Dar más espacio al nombre

        TableColumn<Persona, Integer> edadCol = new TableColumn<>("Edad");
        edadCol.setCellValueFactory(new PropertyValueFactory<>("edad"));
        edadCol.setCellFactory(TextFieldTableCell.forTableColumn(new IntegerStringConverter()));
        edadCol.setOnEditCommit(event -> {
            Persona p = event.getRowValue();
            p.setEdad(event.getNewValue());
        });

        TableColumn<Persona, Integer> alturaCol = new TableColumn<>("Altura (cm)");
        alturaCol.setCellValueFactory(new PropertyValueFactory<>("altura"));
        alturaCol.setCellFactory(TextFieldTableCell.forTableColumn(new IntegerStringConverter()));
        alturaCol.setOnEditCommit(event -> {
             Persona p = event.getRowValue();
            p.setAltura(event.getNewValue());
        });

        table.getColumns().addAll(nombreCol, edadCol, alturaCol);
        table.setItems(grupoData);
        StyleManager.applyTableStyles(table);
        
        // --- Botones ---
        Button btnAnadir = StyleManager.createStyledButton("Añadir Acompañante");
        Button btnEliminar = StyleManager.createStyledButton("Eliminar Seleccionado");
        Button btnGuardar = StyleManager.createStyledButton("Guardar Cambios");
        Button btnCancelar = StyleManager.createStyledButton("Cancelar");

        // Panel para botones de acción (izquierda)
        HBox actionButtons = new HBox(10, btnAnadir, btnEliminar);
        actionButtons.setAlignment(Pos.CENTER_LEFT);

        // Panel para botones de confirmación (derecha)
        HBox confirmationButtons = new HBox(10, btnCancelar, btnGuardar);
        confirmationButtons.setAlignment(Pos.CENTER_RIGHT);

        // Panel inferior que contiene ambos grupos de botones
        BorderPane bottomPanel = new BorderPane();
        bottomPanel.setPadding(new Insets(15, 0, 0, 0));
        bottomPanel.setLeft(actionButtons);
        bottomPanel.setRight(confirmationButtons);
        
        // --- Lógica de los Botones ---
        btnCancelar.setOnAction(e -> stage.close());
        
        btnAnadir.setOnAction(e -> {
            // Se añade una nueva persona con datos por defecto a la lista observable
            // La tabla se actualiza automáticamente
            grupoData.add(new Persona("Nuevo Acompañante", 0, 0));
        });

        btnEliminar.setOnAction(e -> {
            Persona seleccionada = table.getSelectionModel().getSelectedItem();
            if (seleccionada != null) {
                grupoData.remove(seleccionada);
            } else {
                mostrarAlerta("Error", "Debes seleccionar una persona para eliminar.");
            }
        });
        
        btnGuardar.setOnAction(e -> {
            // Validación simple antes de guardar
            for (Persona p : grupoData) {
                if (p.getNombre().trim().isEmpty() || p.getEdad() <= 0 || p.getAltura() <= 0) {
                    mostrarAlerta("Datos incompletos", "Todas las personas deben tener nombre, y edad/altura mayor a cero.");
                    return; // Detiene el guardado
                }
            }
            
            // Si todo es válido, actualiza la reserva original y cierra
            reserva.getGrupo().clear();
            reserva.getGrupo().addAll(grupoData);
            stage.close();
        });

        // --- Montaje Final ---
        root.setCenter(table);
        root.setBottom(bottomPanel);
        
        Scene scene = new Scene(root, 650, 450);
        stage.setScene(scene);
    }
    
    /** Muestra la ventana y espera hasta que sea cerrada. */
    public void show() {
        stage.showAndWait();
    }
    
    /** Método de utilidad para mostrar alertas de error. */
    private void mostrarAlerta(String titulo, String mensaje) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }
}