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
import java.time.format.DateTimeParseException;

public class GestionAtraccionesDialog extends JDialog {
    private Parque parque;
    private JTable table;
    private DefaultTableModel tableModel;

    public GestionAtraccionesDialog(Frame owner, Parque parque) {
        super(owner, "Gestión de Atracciones", true);
        this.parque = parque;
        
        setSize(800, 500);
        setLocationRelativeTo(owner);
        setLayout(new BorderLayout(10, 10));

        //--- Configuración de la Tabla ---
        String[] columnNames = {"Código", "Nombre", "Descripción", "Capacidad Máx.", "Apertura", "Cierre"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Hace que la tabla no sea editable directamente
            }
        };
        table = new JTable(tableModel);
        actualizarTabla();

        //--- Panel de Botones ---
        JPanel buttonPanel = new JPanel();
        JButton btnAgregar = new JButton("Agregar Atracción");
        JButton btnModificar = new JButton("Modificar Seleccionada");
        JButton btnEliminar = new JButton("Eliminar Seleccionada");

        buttonPanel.add(btnAgregar);
        buttonPanel.add(btnModificar);
        buttonPanel.add(btnEliminar);

        //--- Asignación de Acciones ---
        btnAgregar.addActionListener(e -> agregarAtraccion());
        btnModificar.addActionListener(e -> modificarAtraccion());
        btnEliminar.addActionListener(e -> eliminarAtraccion());

        //--- Añadir Componentes al Diálogo ---
        add(new JScrollPane(table), BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);
    }

    private void actualizarTabla() {
        tableModel.setRowCount(0); // Limpia la tabla
        if (parque.getAtraccionesCollection() != null) {
            for (Atraccion a : parque.getAtraccionesCollection()) {
                Object[] row = {
                    a.getCodigo(),
                    a.getNombre(),
                    a.getDescripcion(),
                    a.getCantidadMax(), // <-- Aquí se muestra la capacidad en la tabla
                    a.getApertura(),
                    a.getCierre(),
                    a.getEdad(),
                    a.getAltura()
                };
                tableModel.addRow(row);
            }
        }
    }

    private void agregarAtraccion() {
        JPanel panel = crearPanelFormularioAtraccion(null, "", "", "", "09:00", "21:00", "", "", "");
        
        int result = JOptionPane.showConfirmDialog(this, panel, "Agregar Nueva Atracción",
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

        if (result == JOptionPane.OK_OPTION) {
            try {
                String nombre = ((JTextField) panel.getComponent(1)).getText();
                String descripcion = ((JTextField) panel.getComponent(3)).getText();
                // Se lee la capacidad máxima desde el formulario
                int capacidad = Integer.parseInt(((JTextField) panel.getComponent(5)).getText());
                String apertura = ((JTextField) panel.getComponent(7)).getText();
                String cierre = ((JTextField) panel.getComponent(9)).getText();
                int edadMin = Integer.parseInt(((JTextField) panel.getComponent(11)).getText());
                int alturaMin = Integer.parseInt(((JTextField) panel.getComponent(13)).getText());
                int duracion = Integer.parseInt(((JTextField) panel.getComponent(15)).getText());

                parque.agregarAtraccion(nombre, descripcion, capacidad, apertura, cierre, edadMin, alturaMin, duracion);
                actualizarTabla();
            } catch (Exception ex) {
                mostrarError(ex);
            }
        }
    }
    
    private void modificarAtraccion() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow < 0) {
            JOptionPane.showMessageDialog(this, "Por favor, selecciona una atracción de la tabla para modificar.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }
        int codigo = (int) tableModel.getValueAt(selectedRow, 0);
        Atraccion atraccionAModificar = parque.buscarAtraccion(codigo);

        if (atraccionAModificar == null) {
             JOptionPane.showMessageDialog(this, "Error: No se pudo encontrar la atracción seleccionada.", "Error Interno", JOptionPane.ERROR_MESSAGE);
             return;
        }

        JPanel panel = crearPanelFormularioAtraccion(
            String.valueOf(atraccionAModificar.getCodigo()),
            atraccionAModificar.getNombre(),
            atraccionAModificar.getDescripcion(),
            // Se pasa la capacidad actual al formulario
            String.valueOf(atraccionAModificar.getCantidadMax()),
            atraccionAModificar.getApertura().toString(),
            atraccionAModificar.getCierre().toString(),
            String.valueOf(atraccionAModificar.getEdad()),
            String.valueOf(atraccionAModificar.getAltura()),
            String.valueOf(atraccionAModificar.getDuracion())
        );

        int result = JOptionPane.showConfirmDialog(this, panel, "Modificar Atracción",
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

        if (result == JOptionPane.OK_OPTION) {
            try {
                atraccionAModificar.setNombre(((JTextField) panel.getComponent(3)).getText());
                atraccionAModificar.setDescripcion(((JTextField) panel.getComponent(5)).getText());
                // Se actualiza la capacidad máxima con el valor del formulario
                atraccionAModificar.setCantidadMax(Integer.parseInt(((JTextField) panel.getComponent(7)).getText()));
                atraccionAModificar.setApertura(((JTextField) panel.getComponent(9)).getText());
                atraccionAModificar.setCierre(((JTextField) panel.getComponent(11)).getText());
                atraccionAModificar.setEdad(Integer.parseInt(((JTextField) panel.getComponent(13)).getText()));
            atraccionAModificar.setAltura(Integer.parseInt(((JTextField) panel.getComponent(15)).getText()));
                actualizarTabla();
            } catch (Exception ex) {
                mostrarError(ex);
            }
        }
    }
    
    private void eliminarAtraccion() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow >= 0) {
            int codigo = (int) tableModel.getValueAt(selectedRow, 0);
            int confirm = JOptionPane.showConfirmDialog(this,
                    "¿Estás seguro de que quieres eliminar la atracción con código " + codigo + "?",
                    "Confirmar Eliminación", JOptionPane.YES_NO_OPTION);
            
            if (confirm == JOptionPane.YES_OPTION) {
                parque.eliminarAtraccion(codigo);
                actualizarTabla();
            }
        } else {
            JOptionPane.showMessageDialog(this, "Por favor, selecciona una atracción de la tabla para eliminar.", "Aviso", JOptionPane.WARNING_MESSAGE);
        }
    }
    
    // Este método crea el formulario y ya incluye el campo "Capacidad Máxima"
    private JPanel crearPanelFormularioAtraccion(String codigo, String nombre, 
            String desc, String cap, String ap, String ci, String edadMin, String altMin, String duracion) {
        JPanel panel = new JPanel(new GridLayout(0, 2, 5, 5));
        
        if (codigo != null) {
            JTextField codigoField = new JTextField(codigo);
            codigoField.setEditable(false); 
            panel.add(new JLabel("Código:"));
            panel.add(codigoField);
        }
        
        panel.add(new JLabel("Nombre:"));
        panel.add(new JTextField(nombre));
        panel.add(new JLabel("Descripción:"));
        panel.add(new JTextField(desc));
        
        // --- AQUÍ SE PIDE LA CAPACIDAD MÁXIMA ---
        panel.add(new JLabel("Capacidad Máxima:"));
        panel.add(new JTextField(cap));
        // -----------------------------------------
        
        panel.add(new JLabel("Hora Apertura (HH:MM):"));
        panel.add(new JTextField(ap));
        panel.add(new JLabel("Hora Cierre (HH:MM):"));
        panel.add(new JTextField(ci));
        
        panel.add(new JLabel("Edad Mínima (0 si no aplica):"));
        panel.add(new JTextField(edadMin));
        panel.add(new JLabel("Altura Mínima en cm (0 si no aplica):"));
        panel.add(new JTextField(altMin));
        
        panel.add(new JLabel("Duración en minutos:"));
        panel.add(new JTextField(duracion));
        
        return panel;
    }
    
    private void mostrarError(Exception ex) {
        if (ex instanceof NumberFormatException) {
            JOptionPane.showMessageDialog(this, "La capacidad debe ser un número.", "Error de Formato", JOptionPane.ERROR_MESSAGE);
        } else if (ex instanceof DateTimeParseException) {
            JOptionPane.showMessageDialog(this, "El formato de hora debe ser HH:MM (ej. 14:30).", "Error de Formato", JOptionPane.ERROR_MESSAGE);
        } else if (ex instanceof IllegalArgumentException) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Error al Guardar", JOptionPane.ERROR_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(this, "Ocurrió un error inesperado.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}