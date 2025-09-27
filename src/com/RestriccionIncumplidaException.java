/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com;

/**
 *
 * @author Brandon
 */
public class RestriccionIncumplidaException extends Exception{
    /**
     * Constructor para la excepción de restricción no cumplida.
     * @param message El mensaje de error detallado.
     */
    public RestriccionIncumplidaException(String message) {
        super(message);
    }
}
