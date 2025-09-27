/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com;

/**
 *
 * @author Brandon
 */
import javafx.beans.property.SimpleStringProperty;
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
import javafx.util.StringConverter;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class GestionReservasDialogFX {

    private Stage stage;
    private TableView<Reserva> table;
    private ObservableList<Reserva> reservasData;
    private Parque parque;
    private ComboBox<Atraccion> atraccionComboBox;
    private Stage owner;

    public GestionReservasDialogFX(Stage owner, Parque parque) {
        this.parque = parque;
        this.owner = owner;
        stage = new Stage();
        stage.initOwner(owner);
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setTitle("Gestión de Reservas");

        BorderPane root = new BorderPane();
        root.setPadding(new Insets(15));
        StyleManager.applyBackground(root);

        // --- Panel Superior ---
        HBox topPanel = new HBox(10);
        topPanel.setPadding(new Insets(0, 0, 15, 0));
        topPanel.setAlignment(Pos.CENTER_LEFT);
        
        atraccionComboBox = new ComboBox<>();
        atraccionComboBox.setItems(FXCollections.observableArrayList(parque.getAtraccionesCollection()));
        
        // Convertidor para mostrar "Código - Nombre" en el ComboBox
        atraccionComboBox.setConverter(new StringConverter<Atraccion>() {
            @Override
            public String toString(Atraccion atraccion) {
                return atraccion == null ? "Seleccione una atracción" : atraccion.getCodigo() + " - " + atraccion.getNombre();
            }
            @Override
            public Atraccion fromString(String string) { return null; }
        });
        
        Label searchLabel = StyleManager.createNormalLabel("Filtrar por Código o Fecha:");
        TextField searchField = StyleManager.createStyledTextField();
        topPanel.getChildren().addAll(StyleManager.createNormalLabel("Atracción:"), atraccionComboBox, searchLabel, searchField);
        
        setupTable();
        
        FilteredList<Reserva> filteredData = new FilteredList<>(reservasData, p -> true);
        searchField.textProperty().addListener((obs, oldVal, newVal) -> {
            filteredData.setPredicate(reserva -> {
                if (newVal == null || newVal.isEmpty()) return true;
                String lowerCaseFilter = newVal.toLowerCase();
                if (String.valueOf(reserva.getCodigoR()).contains(lowerCaseFilter)) return true;
                return reserva.getFecha().toLowerCase().contains(lowerCaseFilter);
            });
        });

        SortedList<Reserva> sortedData = new SortedList<>(filteredData);
        sortedData.comparatorProperty().bind(table.comparatorProperty());
        table.setItems(sortedData);

        atraccionComboBox.valueProperty().addListener((obs, oldAtr, newAtr) -> actualizarTablaReservas());
        
        // --- Panel Inferior: Botones ---
        HBox bottomPanel = new HBox(10);
        bottomPanel.setAlignment(Pos.CENTER);
        bottomPanel.setPadding(new Insets(15, 0, 0, 0));
        Button btnAgregar = StyleManager.createStyledButton("Nueva Reserva");
        Button btnModificar = StyleManager.createStyledButton("Modificar");
        Button btnEliminar = StyleManager.createStyledButton("Eliminar");
        Button btnPersonas = StyleManager.createStyledButton("Gestionar Personas");
        bottomPanel.getChildren().addAll(btnAgregar, btnModificar, btnEliminar, btnPersonas);
        
        // --- Acciones de Botones ---
        btnAgregar.setOnAction(e -> agregarReserva());
        btnModificar.setOnAction(e -> modificarReserva());
        btnEliminar.setOnAction(e -> eliminarReserva());
        btnPersonas.setOnAction(e -> gestionarPersonas());
        
        root.setTop(topPanel);
        root.setCenter(table);
        root.setBottom(bottomPanel);
        
        if (!atraccionComboBox.getItems().isEmpty()) {
            atraccionComboBox.getSelectionModel().selectFirst();
        }
        
        Scene scene = new Scene(root, 950, 600);
        stage.setScene(scene);
    }

    private void setupTable() {
        table = new TableView<>();
        reservasData = FXCollections.observableArrayList();

        TableColumn<Reserva, Integer> codCol = new TableColumn<>("Cod. Reserva");
        codCol.setCellValueFactory(new PropertyValueFactory<>("codigoR"));
        
        TableColumn<Reserva, String> fechaCol = new TableColumn<>("Fecha");
        fechaCol.setCellValueFactory(new PropertyValueFactory<>("fecha"));

        TableColumn<Reserva, LocalTime> horaCol = new TableColumn<>("Hora");
        horaCol.setCellValueFactory(new PropertyValueFactory<>("hora"));
        
        TableColumn<Reserva, String> personasCol = new TableColumn<>("Personas (Reg/Total)");
        personasCol.setCellValueFactory(cellData -> {
            Reserva r = cellData.getValue();
            return new SimpleStringProperty(r.getGrupo().size() + " / " + r.getNumeroPersonas());
        });

        table.getColumns().addAll(codCol, fechaCol, horaCol, personasCol);
        StyleManager.applyTableStyles(table);
    }

    private void actualizarTablaReservas() {
        Atraccion seleccionada = atraccionComboBox.getValue();
        reservasData.setAll(seleccionada != null ? seleccionada.getReservas() : FXCollections.emptyObservableList());
    }

    private void agregarReserva() {
        Atraccion atr = atraccionComboBox.getValue();
        if (atr == null) {
            mostrarAlerta("Aviso", "Debes seleccionar una atracción primero.");
            return;
        }

        Optional<Reserva> resultadoForm = mostrarFormularioReserva(atr, null);
        resultadoForm.ifPresent(reservaParcial -> {
            try {
                // Prepara una reserva temporal para el diálogo de registro de personas
                Reserva reservaTemporal = new Reserva(-1, "", "", LocalTime.now(), reservaParcial.getNumeroPersonas());
                for (int i = 0; i < reservaParcial.getNumeroPersonas(); i++) {
                    String nombre = (i == 0) ? reservaParcial.getGrupo().get(0).getNombre() : "Acompañante " + (i + 1);
                    reservaTemporal.agregarPersona(new Persona(nombre, 0, 0));
                }

                RegistroPersonasDialogFX dialogoPersonas = new RegistroPersonasDialogFX(stage, reservaTemporal);
                dialogoPersonas.show();
                
                // Si el usuario guardó, la reservaTemporal ahora tiene los datos completos
                List<Persona> grupoCompleto = reservaTemporal.getGrupo();
                
                // Valida las restricciones antes de crear la reserva final
                for (Persona p : grupoCompleto) {
                    if (atr.getEdad() > 0 && p.getEdad() < atr.getEdad()) {
                        throw new RestriccionIncumplidaException("Reserva rechazada: " + p.getNombre() + " no cumple la edad mínima.");
                    }
                    if (atr.getAltura() > 0 && p.getAltura() < atr.getAltura()) {
                        throw new RestriccionIncumplidaException("Reserva rechazada: " + p.getNombre() + " no cumple la altura mínima.");
                    }
                }
                
                // Finalmente, crear la reserva real en el sistema
                atr.crearNuevaReserva(reservaParcial.getFecha(), reservaParcial.getHora(), grupoCompleto);
                actualizarTablaReservas();

            } catch (Exception e) {
                mostrarAlerta("Error", "No se pudo crear la reserva: " + e.getMessage());
            }
        });
    }

    private void modificarReserva() {
        Reserva seleccionada = table.getSelectionModel().getSelectedItem();
        if (seleccionada == null) {
            mostrarAlerta("Aviso", "Selecciona una reserva para modificar.");
            return;
        }
        
        Optional<Reserva> resultadoForm = mostrarFormularioReserva(null, seleccionada);
        resultadoForm.ifPresent(reservaModificada -> {
            try {
                seleccionada.setFecha(reservaModificada.getFecha());
                seleccionada.setHora(reservaModificada.getHora().toString());
                table.refresh(); // Refresca la tabla para mostrar los cambios
            } catch (Exception e) {
                mostrarAlerta("Error", "No se pudo modificar la reserva: " + e.getMessage());
            }
        });
    }

    private void eliminarReserva() {
        Atraccion atr = atraccionComboBox.getValue();
        Reserva seleccionada = table.getSelectionModel().getSelectedItem();
        if (atr == null || seleccionada == null) {
            mostrarAlerta("Aviso", "Selecciona una atracción y una reserva para eliminar.");
            return;
        }

        Alert confirmacion = new Alert(Alert.AlertType.CONFIRMATION);
        confirmacion.setTitle("Confirmar Eliminación");
        confirmacion.setHeaderText("¿Eliminar reserva #" + seleccionada.getCodigoR() + "?");
        
        Optional<ButtonType> resultado = confirmacion.showAndWait();
        if (resultado.isPresent() && resultado.get() == ButtonType.OK) {
            atr.eliminarReserva(seleccionada.getCodigoR());
            actualizarTablaReservas();
        }
    }

    private void gestionarPersonas() {
        Reserva seleccionada = table.getSelectionModel().getSelectedItem();
        if (seleccionada == null) {
            mostrarAlerta("Aviso", "Selecciona una reserva para gestionar las personas.");
            return;
        }
        
        RegistroPersonasDialogFX dialogo = new RegistroPersonasDialogFX(stage, seleccionada);
        dialogo.show();
        table.refresh();
    }
    
    private Optional<Reserva> mostrarFormularioReserva(Atraccion atr, Reserva reservaExistente) {
        Dialog<Reserva> dialog = new Dialog<>();
        dialog.setTitle(reservaExistente == null ? "Nueva Reserva para: " + atr.getNombre() : "Modificar Reserva #" + reservaExistente.getCodigoR());
        dialog.setHeaderText(null);
        StyleManager.applyBackground(dialog.getDialogPane());

        ButtonType okButtonType = new ButtonType(reservaExistente == null ? "Siguiente" : "Guardar", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(okButtonType, ButtonType.CANCEL);
        StyleManager.aplicarEstiloBoton((Button) dialog.getDialogPane().lookupButton(okButtonType));
        StyleManager.aplicarEstiloBoton((Button) dialog.getDialogPane().lookupButton(ButtonType.CANCEL));
        
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));

        DatePicker datePicker = new DatePicker();
        ComboBox<LocalTime> horaComboBox = new ComboBox<>(); 
        TextField cantidadPersonas = StyleManager.createStyledTextField();
        TextField nombreResponsable = StyleManager.createStyledTextField();
    
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        
        final LocalDate fechaMinima = LocalDate.of(2025, 9, 27);
    
        datePicker.setDayCellFactory(picker -> new DateCell() {
            @Override
            public void updateItem(LocalDate date, boolean empty) {
                super.updateItem(date, empty);
                // Deshabilita la celda si la fecha es anterior a la fecha mínima
                setDisable(empty || date.isBefore(fechaMinima));
            }
        });
        
        if (reservaExistente != null) { // Caso: Modificar Reserva
            grid.add(StyleManager.createNormalLabel("Fecha:"), 0, 0); grid.add(datePicker, 1, 0);
            grid.add(StyleManager.createNormalLabel("Hora:"), 0, 1); grid.add(horaComboBox, 1, 1);
        
            datePicker.setValue(LocalDate.parse(reservaExistente.getFecha(), dtf));
        
            // CORRECCIÓN: Renombrar la variable del bucle para evitar el conflicto
            Atraccion atraccionDeLaReserva = null; 
            for (Atraccion a : parque.getAtraccionesCollection()) {
                if (a.getNombre().equals(reservaExistente.getAtraccion())) {
                    atraccionDeLaReserva = a;
                    break;
                }
            }
        
            if (atraccionDeLaReserva != null) {
                horaComboBox.setItems(FXCollections.observableArrayList(generarHorariosDisponibles(atraccionDeLaReserva)));
            }
            horaComboBox.setValue(reservaExistente.getHora());
        
        } else { // Caso: Agregar Nueva Reserva
            grid.add(StyleManager.createNormalLabel("Fecha:"), 0, 0); grid.add(datePicker, 1, 0);
            grid.add(StyleManager.createNormalLabel("Hora:"), 0, 1); grid.add(horaComboBox, 1, 1);
            grid.add(StyleManager.createNormalLabel("Cantidad de Personas:"), 0, 2); grid.add(cantidadPersonas, 1, 2);
            grid.add(StyleManager.createNormalLabel("Nombre del Responsable:"), 0, 3); grid.add(nombreResponsable, 1, 3);
        
            datePicker.setValue(LocalDate.now());
            List<LocalTime> horarios = generarHorariosDisponibles(atr);
            horaComboBox.setItems(FXCollections.observableArrayList(horarios));
            if (!horarios.isEmpty()) horaComboBox.getSelectionModel().selectFirst();
        }

        dialog.getDialogPane().setContent(grid);
        
        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == okButtonType) {
                try {
                    String fecha = datePicker.getValue().format(dtf);
                    LocalTime hora = horaComboBox.getValue();
                    if (hora == null) throw new IllegalArgumentException("Debe seleccionar una hora.");
                
                    int numPersonas = reservaExistente != null ? reservaExistente.getNumeroPersonas() : Integer.parseInt(cantidadPersonas.getText());
                    String nombreResp = reservaExistente != null ? reservaExistente.getGrupo().get(0).getNombre() : nombreResponsable.getText();
                
                    if (nombreResp.trim().isEmpty() || (reservaExistente == null && numPersonas <= 0)) {
                        throw new IllegalArgumentException("Datos inválidos.");
                    }
                
                    Reserva res = new Reserva(-1, atr != null ? atr.getNombre() : "", fecha, hora, numPersonas);
                    res.agregarPersona(new Persona(nombreResp, 0, 0));
                    return res;
                } catch (Exception e) { 
                    mostrarAlerta("Error de Formato", "Asegúrate de que todos los campos sean correctos.");
                    return null; 
                }
            }
            return null;
        });
        return dialog.showAndWait();
    }
    
    private List<LocalTime> generarHorariosDisponibles(Atraccion atr) {
        List<LocalTime> horarios = new ArrayList<>();
        if (atr == null || atr.getDuracion() <= 0) return horarios;
        LocalTime horarioActual = atr.getApertura();
        while (horarioActual.isBefore(atr.getCierre())) {
            horarios.add(horarioActual);
            horarioActual = horarioActual.plusMinutes(atr.getDuracion());
        }
        return horarios;
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