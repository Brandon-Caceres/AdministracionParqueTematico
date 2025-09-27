/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com;

/**
 *
 * @author Brandon
 */

import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;

public class StyleManager {

    // --- Colores y Fuentes ---
    private static final String BACKGROUND_COLOR = "#15191E"; // Fondo oscuro
    private static final String ACCENT_COLOR = "#6750A4";     // Morado para botones
    private static final String ACCENT_COLOR_HOVER = "#7E69B5"; // Morado más claro
    private static final String TEXT_COLOR_PRIMARY = "white";
    private static final String TEXT_COLOR_SECONDARY = "#CCCCCC";
    private static final String FONT_FAMILY = "'Roboto'"; // Usar comillas simples para nombres con espacios

    // --- Estilos de Botón ---
    private static final String STYLE_BOTON_NORMAL =
        "-fx-font-family: " + FONT_FAMILY + "; -fx-font-size: 14px; -fx-text-fill: " + TEXT_COLOR_PRIMARY + "; " +
        "-fx-background-color: " + ACCENT_COLOR + "; -fx-background-radius: 20; " +
        "-fx-pref-height: 35px;";

    private static final String STYLE_BOTON_HOVER =
        "-fx-font-family: " + FONT_FAMILY + "; -fx-font-size: 14px; -fx-text-fill: " + TEXT_COLOR_PRIMARY + "; " +
        "-fx-background-color: " + ACCENT_COLOR_HOVER + "; -fx-background-radius: 20; " +
        "-fx-pref-height: 35px; -fx-cursor: hand;";
    
    // --- Métodos de Aplicación de Estilos ---

    /** Aplica el fondo oscuro a un panel principal. */
    public static void applyBackground(Pane pane) {
        pane.setStyle("-fx-background-color: " + BACKGROUND_COLOR + ";");
    }
    
    /** Crea un botón ya estilizado. */
    public static Button createStyledButton(String text) {
        Button button = new Button(text);
        button.setStyle(STYLE_BOTON_NORMAL);
        button.setOnMouseEntered(e -> button.setStyle(STYLE_BOTON_HOVER));
        button.setOnMouseExited(e -> button.setStyle(STYLE_BOTON_NORMAL));
        return button;
    }
    
    /** Crea una etiqueta de título principal. */
    public static Label createTitleLabel(String text) {
        Label label = new Label(text);
        label.setStyle(
            "-fx-font-family: " + FONT_FAMILY + "; -fx-font-size: 24px; " +
            "-fx-text-fill: " + TEXT_COLOR_PRIMARY + "; -fx-font-weight: bold;"
        );
        return label;
    }
    
    /** Crea una etiqueta de texto normal. */
    public static Label createNormalLabel(String text) {
        Label label = new Label(text);
        label.setStyle("-fx-font-family: " + FONT_FAMILY + "; -fx-font-size: 14px; -fx-text-fill: " + TEXT_COLOR_SECONDARY + ";");
        return label;
    }
    
    /** Crea un campo de texto estilizado. */
    public static TextField createStyledTextField() {
        TextField textField = new TextField();
        textField.setStyle(
            "-fx-font-family: " + FONT_FAMILY + "; -fx-font-size: 14px; " +
            "-fx-background-color: #333842; -fx-text-fill: " + TEXT_COLOR_PRIMARY + "; " +
            "-fx-background-radius: 10;"
        );
        return textField;
    }
    
    /** Aplica el estilo a una TableView. */
    public static void applyTableStyles(TableView<?> table) {
        // Este es un estilo más complejo, se podría poner en un CSS si se quisiera
        // Por ahora, solo lo básico
        table.setStyle("-fx-background-color: #333842; -fx-font-family: " + FONT_FAMILY + ";");
    }
    
    // --- AÑADE ESTE NUEVO MÉTODO AQUÍ ---
    public static void aplicarEstiloBoton(Button boton) {
        if (boton != null) {
            boton.setStyle(STYLE_BOTON_NORMAL);
            boton.setOnMouseEntered(e -> boton.setStyle(STYLE_BOTON_HOVER));
            boton.setOnMouseExited(e -> boton.setStyle(STYLE_BOTON_NORMAL));
        }
    }
}