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
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

public class GestionAtraccionesDialogFX {

    private Stage stage;
    private TableView<Atraccion> table;
    private ObservableList<Atraccion> atraccionesData;
    private Parque parque;

    public GestionAtraccionesDialogFX(Stage owner, Parque parque) {
        this.parque = parque;
        stage = new Stage();
        stage.initOwner(owner);
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setTitle("Gestión de Atracciones");

        BorderPane root = new BorderPane();
        root.setPadding(new Insets(15));
        StyleManager.applyBackground(root);

        // --- Panel Superior: Búsqueda ---
        HBox topPanel = new HBox(10);
        topPanel.setPadding(new Insets(0, 0, 15, 0));
        topPanel.setAlignment(Pos.CENTER_LEFT);
        Label searchLabel = StyleManager.createNormalLabel("Buscar por código o nombre:");
        TextField searchField = StyleManager.createStyledTextField();
        searchField.setPrefWidth(250);
        topPanel.getChildren().addAll(searchLabel, searchField);

        // --- Centro: Tabla ---
        setupTable();
        
        // --- Lógica de Filtrado y Ordenamiento ---
        FilteredList<Atraccion> filteredData = new FilteredList<>(atraccionesData, p -> true);
        searchField.textProperty().addListener((obs, oldVal, newVal) -> {
            filteredData.setPredicate(atraccion -> {
                if (newVal == null || newVal.isEmpty()) return true;
                String lowerCaseFilter = newVal.toLowerCase();
                if (atraccion.getNombre().toLowerCase().contains(lowerCaseFilter)) return true;
                return String.valueOf(atraccion.getCodigo()).contains(lowerCaseFilter);
            });
        });
        
        SortedList<Atraccion> sortedData = new SortedList<>(filteredData);
        sortedData.comparatorProperty().bind(table.comparatorProperty());
        table.setItems(sortedData);


        // --- Panel Inferior: Botones ---
        HBox bottomPanel = new HBox(10);
        bottomPanel.setPadding(new Insets(15, 0, 0, 0));
        bottomPanel.setAlignment(Pos.CENTER);
        Button btnAgregar = StyleManager.createStyledButton("Agregar Atracción");
        Button btnModificar = StyleManager.createStyledButton("Modificar Seleccionada");
        Button btnEliminar = StyleManager.createStyledButton("Eliminar Seleccionada");
        bottomPanel.getChildren().addAll(btnAgregar, btnModificar, btnEliminar);

        // --- Acciones de Botones ---
        btnAgregar.setOnAction(e -> agregarAtraccion());
        btnModificar.setOnAction(e -> modificarAtraccion());
        btnEliminar.setOnAction(e -> eliminarAtraccion());

        // --- Montaje Final ---
        root.setTop(topPanel);
        root.setCenter(table);
        root.setBottom(bottomPanel);
        
        Scene scene = new Scene(root, 900, 600);
        stage.setScene(scene);
    }
    
    // Configura la estructura de la tabla
    private void setupTable() {
        table = new TableView<>();
        atraccionesData = FXCollections.observableArrayList(parque.getAtraccionesCollection());

        TableColumn<Atraccion, Integer> codigoCol = new TableColumn<>("Código");
        codigoCol.setCellValueFactory(new PropertyValueFactory<>("codigo"));

        TableColumn<Atraccion, String> nombreCol = new TableColumn<>("Nombre");
        nombreCol.setCellValueFactory(new PropertyValueFactory<>("nombre"));

        TableColumn<Atraccion, String> descCol = new TableColumn<>("Descripción");
        descCol.setCellValueFactory(new PropertyValueFactory<>("descripcion"));
        
        TableColumn<Atraccion, Integer> capCol = new TableColumn<>("Cap. Máx.");
        capCol.setCellValueFactory(new PropertyValueFactory<>("cantidadMax"));

        TableColumn<Atraccion, LocalTime> aperturaCol = new TableColumn<>("Apertura");
        aperturaCol.setCellValueFactory(new PropertyValueFactory<>("apertura"));
        
        TableColumn<Atraccion, LocalTime> cierreCol = new TableColumn<>("Cierre");
        cierreCol.setCellValueFactory(new PropertyValueFactory<>("cierre"));

        table.getColumns().addAll(codigoCol, nombreCol, descCol, capCol, aperturaCol, cierreCol);
        StyleManager.applyTableStyles(table);
    }
    
    // Lógica para el botón "Agregar"
    private void agregarAtraccion() {
        // Llamamos al formulario pasándole 'null' para indicar que es una nueva atracción
        Optional<Atraccion> result = mostrarFormularioAtraccion(null);

        // si el usuario presionó "Guardar" y los datos son válidos, el resultado estará presente
        result.ifPresent(nuevaAtraccion -> {
            try {
            // Se usa la lógica de tu negocio para agregar la atracción al parque
                parque.agregarAtraccion(
                    nuevaAtraccion.getNombre(), nuevaAtraccion.getDescripcion(),
                    nuevaAtraccion.getCantidadMax(), nuevaAtraccion.getApertura().toString(),
                    nuevaAtraccion.getCierre().toString(), nuevaAtraccion.getEdad(),
                    nuevaAtraccion.getAltura(), nuevaAtraccion.getDuracion()
                );
                actualizarTabla(); // Refresca la tabla para mostrar el nuevo registro
            } catch (Exception ex) {
                // El método 'mostrarAlerta' es el equivalente a tu 'mostrarError'
                mostrarAlerta("Error", "No se pudo agregar la atracción: " + ex.getMessage());
            }
        });
    }
    

    // Lógica para el botón "Modificar"
    private void modificarAtraccion() {
        Atraccion seleccionada = table.getSelectionModel().getSelectedItem();
        if (seleccionada == null) {
            mostrarAlerta("Aviso", "Por favor, selecciona una atracción de la tabla para modificar.");
            return;
        }
        
        Optional<Atraccion> result = mostrarFormularioAtraccion(seleccionada);
        result.ifPresent(atraccionModificada -> {
            try {
                // Aquí actualizamos el objeto original con los nuevos datos
                seleccionada.setNombre(atraccionModificada.getNombre());
                seleccionada.setDescripcion(atraccionModificada.getDescripcion());
                seleccionada.setCantidadMax(atraccionModificada.getCantidadMax());
                seleccionada.setApertura(atraccionModificada.getApertura().toString());
                seleccionada.setCierre(atraccionModificada.getCierre().toString());
                seleccionada.setEdad(atraccionModificada.getEdad());
                seleccionada.setAltura(atraccionModificada.getAltura());
                // El código no se modifica
                actualizarTabla();
            } catch (Exception e) {
                mostrarAlerta("Error", "No se pudo modificar la atracción: " + e.getMessage());
            }
        });
    }

    // Lógica para el botón "Eliminar"
    private void eliminarAtraccion() {
        Atraccion seleccionada = table.getSelectionModel().getSelectedItem();
        if (seleccionada == null) {
            mostrarAlerta("Aviso", "Por favor, selecciona una atracción de la tabla para eliminar.");
            return;
        }

        Alert confirmacion = new Alert(Alert.AlertType.CONFIRMATION);
        confirmacion.setTitle("Confirmar Eliminación");
        confirmacion.setHeaderText("¿Eliminar atracción '" + seleccionada.getNombre() + "'?");
        confirmacion.setContentText("Esta acción no se puede deshacer.");

        Optional<ButtonType> resultado = confirmacion.showAndWait();
        if (resultado.isPresent() && resultado.get() == ButtonType.OK) {
            parque.eliminarAtraccion(seleccionada.getCodigo());
            actualizarTabla();
        }
    }
    
    // Método centralizado que crea y muestra el formulario de Agregar/Modificar
    private Optional<Atraccion> mostrarFormularioAtraccion(Atraccion atraccionExistente) {
        Dialog<Atraccion> dialog = new Dialog<>();
        dialog.setTitle(atraccionExistente == null ? "Agregar Nueva Atracción" : "Modificar Atracción");
        dialog.setHeaderText(null); // Para un look más limpio

        // --- 1. APLICAR ESTILO AL PANEL PRINCIPAL DEL DIÁLOGO ---
        DialogPane dialogPane = dialog.getDialogPane();
        StyleManager.applyBackground(dialogPane);

        // --- 2. ESTILIZAR LOS BOTONES PREDETERMINADOS DEL DIÁLOGO ---
        ButtonType okButtonType = new ButtonType("Guardar", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(okButtonType, ButtonType.CANCEL);

        // Se necesita buscar los nodos de los botones para poder aplicarles el estilo
        Button okButton = (Button) dialog.getDialogPane().lookupButton(okButtonType);
        Button cancelButton = (Button) dialog.getDialogPane().lookupButton(ButtonType.CANCEL);
        StyleManager.aplicarEstiloBoton(okButton);     // Reutilizamos el método del StyleManager
        StyleManager.aplicarEstiloBoton(cancelButton);

        // --- 3. CREAR EL FORMULARIO CON COMPONENTES ESTILIZADOS ---
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));

        // Se usan los métodos de StyleManager en lugar de 'new TextField()' o 'new Label()'
        TextField nombre = StyleManager.createStyledTextField();
        TextField descripcion = StyleManager.createStyledTextField();
        TextField capacidad = StyleManager.createStyledTextField();
        TextField apertura = StyleManager.createStyledTextField();
        TextField cierre = StyleManager.createStyledTextField();
        TextField edadMin = StyleManager.createStyledTextField();
        TextField alturaMin = StyleManager.createStyledTextField();
        TextField duracion = StyleManager.createStyledTextField();

        if (atraccionExistente != null) {
            // Llenar los campos si estamos modificando
            nombre.setText(atraccionExistente.getNombre());
            descripcion.setText(atraccionExistente.getDescripcion());
            capacidad.setText(String.valueOf(atraccionExistente.getCantidadMax()));
            apertura.setText(atraccionExistente.getApertura().toString());
            cierre.setText(atraccionExistente.getCierre().toString());
            edadMin.setText(String.valueOf(atraccionExistente.getEdad()));
            alturaMin.setText(String.valueOf(atraccionExistente.getAltura()));
            duracion.setText(String.valueOf(atraccionExistente.getDuracion()));
        } else {
            // Establecer valores por defecto para una nueva atracción
            apertura.setText("09:00");
            cierre.setText("21:00");
        }

        grid.add(StyleManager.createNormalLabel("Nombre:"), 0, 0); grid.add(nombre, 1, 0);
        grid.add(StyleManager.createNormalLabel("Descripción:"), 0, 1); grid.add(descripcion, 1, 1);
        grid.add(StyleManager.createNormalLabel("Capacidad Máxima:"), 0, 2); grid.add(capacidad, 1, 2);
        grid.add(StyleManager.createNormalLabel("Apertura (HH:MM):"), 0, 3); grid.add(apertura, 1, 3);
        grid.add(StyleManager.createNormalLabel("Cierre (HH:MM):"), 0, 4); grid.add(cierre, 1, 4);
        grid.add(StyleManager.createNormalLabel("Edad Mínima:"), 0, 5); grid.add(edadMin, 1, 5);
        grid.add(StyleManager.createNormalLabel("Altura Mínima (cm):"), 0, 6); grid.add(alturaMin, 1, 6);
        grid.add(StyleManager.createNormalLabel("Duración (minutos):"), 0, 7); grid.add(duracion, 1, 7);
    
        dialog.getDialogPane().setContent(grid);
    
        // --- Lógica para devolver el resultado (sin cambios) ---
        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == okButtonType) {
                try {
                    Atraccion nueva = new Atraccion(-1, nombre.getText(), descripcion.getText(),
                        Integer.parseInt(capacidad.getText()), apertura.getText(), cierre.getText(),
                        Integer.parseInt(edadMin.getText()), Integer.parseInt(alturaMin.getText()),
                        Integer.parseInt(duracion.getText()));
                    return nueva;
                } catch (Exception e) {
                    mostrarAlerta("Error de Formato", "Asegúrate de que los campos numéricos y de hora sean correctos.");
                    return null;
                }
            }
            return null;
        });

        return dialog.showAndWait();
    }
    
    private void actualizarTabla() {
        atraccionesData.setAll(parque.getAtraccionesCollection());
    }

    private void mostrarAlerta(String titulo, String contenido) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(contenido);
        alert.showAndWait();
    }

    public void show() {
        stage.showAndWait();
    }
}