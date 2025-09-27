/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package administracionparquetematico;

/**
 *
 * @author Brandon
 */
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class RegistroPersonasDialog extends JDialog {
    private JTable table;
    private DefaultTableModel tableModel;
    private List<Persona> grupo = null;
    private Reserva reservaAEditar;

    public RegistroPersonasDialog(Frame owner, int cantidadPersonas, String nombreResponsable) {
        super(owner, "Registrar Personas del Grupo", true); // true = modal
        
        // --- Modelo de la Tabla ---
        String[] columnNames = {"Nombre", "Edad", "Altura (cm)"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            // Hacemos que solo las columnas de Edad y Altura sean editables
            @Override
            public boolean isCellEditable(int row, int column) {
                return column > 0;
            }
        };
        table = new JTable(tableModel);
        table.setRowHeight(25);
        table.setFont(new Font("Arial", Font.PLAIN, 14));

        // --- Llenar la tabla con filas vacías ---
        for (int i = 0; i < cantidadPersonas; i++) {
            String nombre = (i == 0) ? nombreResponsable : "Acompañante " + (i + 1);
            tableModel.addRow(new Object[]{nombre, "", ""});
        }

        // --- Botones ---
        JButton btnAceptar = new JButton("Aceptar");
        JButton btnCancelar = new JButton("Cancelar");
        
        btnAceptar.addActionListener(e -> procesarYVerificarDatos());
        btnCancelar.addActionListener(e -> dispose()); // Simplemente cierra la ventana

        // --- Layout y Paneles ---
        JLabel labelInstruccion = new JLabel("Completa los datos para cada persona del grupo:", SwingConstants.CENTER);
        labelInstruccion.setFont(new Font("Arial", Font.BOLD, 16));
        labelInstruccion.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        panelBotones.add(btnCancelar);
        panelBotones.add(btnAceptar);

        setLayout(new BorderLayout());
        add(labelInstruccion, BorderLayout.NORTH);
        add(new JScrollPane(table), BorderLayout.CENTER);
        add(panelBotones, BorderLayout.SOUTH);

        setSize(500, 400);
        setLocationRelativeTo(owner);
    }
    
    public RegistroPersonasDialog(Frame owner, Reserva reserva) {
        super(owner, "Editar Personas de la Reserva #" + reserva.getCodigoR(), true);
        this.reservaAEditar = reserva;
        setupUI();

        // Llenar la tabla con las personas que ya existen en la reserva
        for (Persona p : reserva.getGrupo()) {
            tableModel.addRow(new Object[]{p.getNombre(), p.getEdad(), p.getAltura()});
        }
    }
    
    private void setupUI() {
        // --- Modelo de la Tabla ---
        String[] columnNames = {"Nombre", "Edad", "Altura (cm)"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                if (row == 0 && column == 0) return false; // El nombre del responsable no se edita
                return true;
            }
        };
        table = new JTable(tableModel);
        table.setRowHeight(25);
        table.setFont(new Font("Arial", Font.PLAIN, 14));

        // --- Botones ---
        JButton btnGuardar = new JButton("Guardar Cambios");
        JButton btnCancelar = new JButton("Cancelar");
        JButton btnAnadir = new JButton("Añadir Acompañante");
        JButton btnEliminar = new JButton("Eliminar Seleccionado");
    
        btnGuardar.addActionListener(e -> procesarYVerificarDatos());
        btnCancelar.addActionListener(e -> dispose());
        btnAnadir.addActionListener(e -> anadirFila());
        btnEliminar.addActionListener(e -> eliminarFila());

        // --- Layout y Paneles (AQUÍ ESTÁ LA CORRECCIÓN) ---
        JLabel labelInstruccion = new JLabel("Edita los datos y haz clic en 'Guardar Cambios'.", SwingConstants.CENTER);
        labelInstruccion.setFont(new Font("Arial", Font.BOLD, 16));
        labelInstruccion.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Panel para los botones de la izquierda (Añadir, Eliminar)
        JPanel panelBotonesAccion = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panelBotonesAccion.add(btnAnadir);
        panelBotonesAccion.add(btnEliminar);

        // Panel para los botones de la derecha (Cancelar, Guardar)
        JPanel panelBotonesConfirmacion = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        panelBotonesConfirmacion.add(btnCancelar);
        panelBotonesConfirmacion.add(btnGuardar);

        // Panel inferior principal que organiza los dos paneles anteriores
        JPanel panelInferior = new JPanel(new BorderLayout());
        panelInferior.add(panelBotonesAccion, BorderLayout.WEST); // A la izquierda
        panelInferior.add(panelBotonesConfirmacion, BorderLayout.EAST); // A la derecha

        setLayout(new BorderLayout());
        add(labelInstruccion, BorderLayout.NORTH);
        add(new JScrollPane(table), BorderLayout.CENTER);
        add(panelInferior, BorderLayout.SOUTH);

        setSize(650, 400);
        setLocationRelativeTo(getOwner());
    }
    
    private void anadirFila() {
        if (reservaAEditar != null && tableModel.getRowCount() >= reservaAEditar.getNumeroPersonas()) {
            JOptionPane.showMessageDialog(this, "No se pueden añadir más personas, la reserva ya está completa.", "Límite Alcanzado", JOptionPane.WARNING_MESSAGE);
            return;
        }
        tableModel.addRow(new Object[]{"Nuevo Acompañante", "", ""});
    }

    private void eliminarFila() {
        int filaSeleccionada = table.getSelectedRow();
        if (filaSeleccionada < 0) {
            JOptionPane.showMessageDialog(this, "Por favor, selecciona una fila para eliminar.", "Aviso", JOptionPane.WARNING_MESSAGE);
        } else if (filaSeleccionada == 0) {
            JOptionPane.showMessageDialog(this, "No se puede eliminar a la persona responsable de la reserva.", "Acción no permitida", JOptionPane.ERROR_MESSAGE);
        } else {
            tableModel.removeRow(filaSeleccionada);
        }
    }
    
    private void procesarYVerificarDatos() {
        // Detiene la edición de celdas para asegurar que el último dato ingresado se guarde
        if (table.isEditing()) {
            table.getCellEditor().stopCellEditing();
        }

        List<Persona> grupoTemporal = new ArrayList<>();
        for (int i = 0; i < tableModel.getRowCount(); i++) {
            try {
                String nombre = tableModel.getValueAt(i, 0).toString();
                int edad = Integer.parseInt(tableModel.getValueAt(i, 1).toString());
                int altura = Integer.parseInt(tableModel.getValueAt(i, 2).toString());

                if (edad <= 0 || altura <= 0) {
                    throw new IllegalArgumentException("La edad y altura deben ser números positivos.");
                }
                
                grupoTemporal.add(new Persona(nombre, edad, altura));
                
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Error en la fila " + (i + 1) + ":\nLa edad y la altura deben ser números.", "Error de Formato", JOptionPane.ERROR_MESSAGE);
                return; // Detiene el proceso sin cerrar la ventana
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error en la fila " + (i + 1) + ":\n" + ex.getMessage(), "Error de Datos", JOptionPane.ERROR_MESSAGE);
                return;
            }
        }
        
        // Si todos los datos son válidos, se asigna el resultado y se cierra la ventana
        if (reservaAEditar != null) {
            try {
                reservaAEditar.limpiarGrupo();
                for (Persona p : grupoTemporal) {
                    // Esta es la línea que puede lanzar la excepción
                    reservaAEditar.agregarPersona(p); 
                }
            } catch (ReservaCompletaException ex) {
                // Si la excepción ocurre, se muestra un mensaje de error
                JOptionPane.showMessageDialog(this, ex.getMessage(), "Error de Reserva", JOptionPane.ERROR_MESSAGE);
                return; // Detiene el proceso
            }
        } else {
            // Para el caso de una nueva reserva, el comportamiento no cambia
            this.grupo = grupoTemporal;
        }
        
        dispose();
    }
    
    /**
     * Método público para obtener el resultado después de que la ventana se cierra.
     * @return La lista de personas si se presionó Aceptar, o null si se canceló.
     */
    public List<Persona> getGrupo() {
        return this.grupo;
    }
}