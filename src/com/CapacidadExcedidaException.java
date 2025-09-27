/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com;

/**
 *
 * @author Brandon
 */
public class CapacidadExcedidaException extends Exception{
    /**
     * Constructor para la excepción de capacidad excedida.
     * @param message El mensaje de error detallado.
     */
    public CapacidadExcedidaException(String message) {
        super(message);
    }
}
