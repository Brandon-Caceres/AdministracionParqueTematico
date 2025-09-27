/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package administracionparquetematico;

/**
 *
 * @author Brandon
 */
import com.toedter.calendar.JDateChooser;
import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.time.DateTimeException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeParseException;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class GestionReservasDialog extends JDialog {
    private Parque parque;
    private JComboBox<AtraccionWrapper> atraccionesComboBox;
    private JTable table;
    private DefaultTableModel tableModel;
    private JTextField campoBusquedaReserva;
    private TableRowSorter<DefaultTableModel> sorterReservas;

    public GestionReservasDialog(Frame owner, Parque parque) {
        super(owner, "Gestión de Reservas", true);
        this.parque = parque;
        setSize(800, 600);
        setLocationRelativeTo(owner);
        setLayout(new BorderLayout(10, 10));

        //--- Panel Superior con Selector de Atracción ---
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        topPanel.add(new JLabel("Seleccionar Atracción:"));
        atraccionesComboBox = new JComboBox<>();
        actualizarComboBox();
        topPanel.add(atraccionesComboBox);
        atraccionesComboBox.addActionListener(e -> actualizarTablaReservas());
        
        // --- AÑADIR BARRA DE BÚSQUEDA AL PANEL SUPERIOR ---
        topPanel.add(new JLabel("   Filtrar por Código o Fecha:"));
        campoBusquedaReserva = new JTextField(15);
        topPanel.add(campoBusquedaReserva);

        //--- Tabla de Reservas ---
        String[] columnNames = {"Cod. Reserva", "Fecha", "Hora", "Personas (Registradas / Total)"};
        tableModel = new DefaultTableModel(columnNames, 0) {
             @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        table = new JTable(tableModel);
        
        // --- Instalar el filtro en la tabla ---
        sorterReservas = new TableRowSorter<>(tableModel);
        table.setRowSorter(sorterReservas);

        //--- Lógica de los Componentes ---
        atraccionesComboBox.addActionListener(e -> {
            // Al cambiar de atracción, se limpia la búsqueda y se actualiza la tabla
            campoBusquedaReserva.setText(""); 
            actualizarTablaReservas();
        });

        campoBusquedaReserva.getDocument().addDocumentListener(new DocumentListener() {
            public void insertUpdate(DocumentEvent e) { filtrarTablaReservas(); }
            public void removeUpdate(DocumentEvent e) { filtrarTablaReservas(); }
            public void changedUpdate(DocumentEvent e) { filtrarTablaReservas(); }
        });

        
        //--- Panel de Botones ---
        JPanel buttonPanel = new JPanel();

        JButton btnAgregarReserva = new JButton("Nueva Reserva");
        btnAgregarReserva.addActionListener(e -> agregarReserva());
        buttonPanel.add(btnAgregarReserva);

        JButton btnModificarReserva = new JButton("Modificar Reserva");
        btnModificarReserva.addActionListener(e -> modificarReserva());
        buttonPanel.add(btnModificarReserva);

        JButton btnEliminarReserva = new JButton("Eliminar Reserva");
        btnEliminarReserva.addActionListener(e -> eliminarReserva());
        buttonPanel.add(btnEliminarReserva);

        JButton btnGestionarPersonas = new JButton("Gestionar Personas");
        btnGestionarPersonas.addActionListener(e -> gestionarPersonas());
        buttonPanel.add(btnGestionarPersonas);
        
        //--- Añadir Componentes al Diálogo ---
        add(topPanel, BorderLayout.NORTH);
        add(new JScrollPane(table), BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);

        if (atraccionesComboBox.getItemCount() > 0) {
            actualizarTablaReservas();
        }
    }

    private void filtrarTablaReservas() {
        String texto = campoBusquedaReserva.getText();
        if (texto.trim().length() == 0) {
            sorterReservas.setRowFilter(null);
        } else {
            // Busca en la columna 0 (Código) y 1 (Fecha)
            sorterReservas.setRowFilter(RowFilter.regexFilter("(?i)" + texto, 0, 1));
        }
    }
    
    private void agregarReserva() {
    Atraccion atr = getAtraccionSeleccionada();
    if (atr == null) {
        JOptionPane.showMessageDialog(this, "Debes seleccionar una atracción primero.", "Aviso", JOptionPane.WARNING_MESSAGE);
        return;
    }
    
    JPanel panelPrincipal = crearPanelFormularioPrincipal(atr);
    panelPrincipal.setPreferredSize(new Dimension(480, 200)); 
    int resultPrincipal = JOptionPane.showConfirmDialog(this, panelPrincipal, "Nueva Reserva para: " + atr.getNombre(), JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
    
    if (resultPrincipal == JOptionPane.OK_OPTION) {
        try {
            // 1. OBTENER FECHA (Forma correcta, desde JDateChooser)
            JDateChooser dateChooser = (JDateChooser) panelPrincipal.getComponent(1);
            Date fechaSeleccionada = dateChooser.getDate();
            if (fechaSeleccionada == null) {
                throw new IllegalArgumentException("Debe seleccionar una fecha.");
            }
            LocalDate localDate = fechaSeleccionada.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
            String fecha = String.format("%02d/%02d/%d", localDate.getDayOfMonth(), localDate.getMonthValue(), localDate.getYear());
            
            // 2. OBTENER HORA, CANTIDAD Y NOMBRE (con índices corregidos)
            LocalTime horaSeleccionada = (LocalTime) ((JComboBox<?>) panelPrincipal.getComponent(3)).getSelectedItem(); // <-- ÍNDICE CORREGIDO (era 7)
            if (horaSeleccionada == null) {
                throw new IllegalArgumentException("Debe seleccionar un horario válido.");
            }

            int cantidadPersonas = Integer.parseInt(((JTextField) panelPrincipal.getComponent(5)).getText()); // <-- ÍNDICE CORREGIDO (era 9)
            if (cantidadPersonas <= 0) {
                throw new IllegalArgumentException("La cantidad de personas debe ser mayor a cero.");
            }

            // Chequeo de capacidad anticipado
            int capacidadOcupada = atr.getCapacidadOcupadaEn(horaSeleccionada);
            if ((capacidadOcupada + cantidadPersonas) > atr.getCantidadMax()) {
                throw new IllegalArgumentException("Capacidad excedida. Solo quedan " + (atr.getCantidadMax() - capacidadOcupada) + " cupos para esa hora.");
            }
            
            String nombreResponsable = ((JTextField) panelPrincipal.getComponent(7)).getText(); // <-- ÍNDICE CORREGIDO (era 11)
            if (nombreResponsable.trim().isEmpty()) {
                throw new IllegalArgumentException("El nombre del responsable no puede estar vacío.");
            }

            // El bloque que leía la fecha de los Spinners ha sido ELIMINADO por ser redundante e incorrecto.

            // 3. Pedir detalles de las personas
            List<Persona> grupo = registrarPersonas(cantidadPersonas, nombreResponsable);
            if (grupo == null) return; // El usuario canceló

            // 4. Validar restricciones (con nombres de métodos corregidos)
            for (Persona p : grupo) {
                if (atr.getEdad() > 0 && p.getEdad() < atr.getEdad()) { // <-- NOMBRES CORREGIDOS
                    throw new IllegalArgumentException("Reserva rechazada: " + p.getNombre() + " no cumple la edad mínima de " + atr.getEdad() + " años.");
                }
                if (atr.getAltura() > 0 && p.getAltura() < atr.getAltura()) { // <-- NOMBRES CORREGIDOS
                    throw new IllegalArgumentException("Reserva rechazada: " + p.getNombre() + " no cumple la altura mínima de " + atr.getAltura() + " cm.");
                }
            }

            // 5. Crear la reserva
            atr.crearNuevaReserva(fecha, horaSeleccionada, grupo);
            actualizarTablaReservas();

        } catch (Exception ex) {
            mostrarError(ex);
        }
    }
}
    
    private void modificarReserva() {
        Atraccion atr = getAtraccionSeleccionada();
        int selectedRow = table.getSelectedRow();
        if (atr == null || selectedRow < 0) {
            JOptionPane.showMessageDialog(this, "Selecciona una atracción y una reserva de la tabla para modificar.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }
        int codigoReserva = (int) tableModel.getValueAt(selectedRow, 0);
        Reserva reservaAModificar = atr.buscarReserva(codigoReserva);
        
        JPanel panel = new JPanel(new GridLayout(0, 2, 5, 5));
        JTextField codigoField = new JTextField(String.valueOf(reservaAModificar.getCodigoR()));
        codigoField.setEditable(false);
        panel.add(new JLabel("Código Reserva:"));
        panel.add(codigoField);
        panel.add(new JLabel("Fecha:"));
        panel.add(new JTextField(reservaAModificar.getFecha()));
        panel.add(new JLabel("Hora (HH:MM):"));
        panel.add(new JTextField(reservaAModificar.getHora().toString()));
        
        int result = JOptionPane.showConfirmDialog(this, panel, "Modificar Reserva", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

        if (result == JOptionPane.OK_OPTION) {
            try {
                String fecha = ((JTextField) panel.getComponent(3)).getText();
                String horaStr = ((JTextField) panel.getComponent(5)).getText();
                if (!atr.estaAbierta(LocalTime.parse(horaStr))) {
                    JOptionPane.showMessageDialog(this, "La atracción está cerrada a esa hora.", "Error de Horario", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                reservaAModificar.setFecha(fecha);
                reservaAModificar.setHora(horaStr);
                actualizarTablaReservas();
            } catch (Exception ex) {
                mostrarError(ex);
            }
        }
    }

    private void eliminarReserva() {
        Atraccion atr = getAtraccionSeleccionada();
        int selectedRow = table.getSelectedRow();
        if (atr == null || selectedRow < 0) {
            JOptionPane.showMessageDialog(this, "Selecciona una atracción y una reserva de la tabla.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }
        int codigoReserva = (int) tableModel.getValueAt(selectedRow, 0);
        int confirm = JOptionPane.showConfirmDialog(this,
                "¿Seguro que quieres eliminar la reserva con código " + codigoReserva + "?",
                "Confirmar Eliminación", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            atr.eliminarReserva(codigoReserva);
            actualizarTablaReservas();
        }
    }
    
    private void gestionarPersonas() {
        Atraccion atr = getAtraccionSeleccionada();
        int selectedRow = table.getSelectedRow();
        if (atr == null || selectedRow < 0) {
            JOptionPane.showMessageDialog(this, "Selecciona una atracción y una reserva de la tabla.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }
        int codigoReserva = (int) tableModel.getValueAt(selectedRow, 0);
        Reserva reservaSeleccionada = atr.buscarReserva(codigoReserva);
        
        if (reservaSeleccionada != null) {
            try {
                if (reservaSeleccionada.getGrupo().size() >= reservaSeleccionada.getNumeroPersonas()) {
                    throw new IllegalStateException("Ya se han registrado todas las personas para esta reserva.");
                }

                JTextField nombreField = new JTextField();
                JTextField edadField = new JTextField();
                JTextField alturaField = new JTextField();
                JPanel panel = new JPanel(new GridLayout(0, 2, 5, 5));
                panel.add(new JLabel("Nombre Acompañante:"));
                panel.add(nombreField);
                panel.add(new JLabel("Edad Acompañante:"));
                panel.add(edadField);
                panel.add(new JLabel("Altura Acompañante (cm):"));
                panel.add(alturaField);
                
                int result = JOptionPane.showConfirmDialog(this, panel, "Agregar Persona a Reserva " + codigoReserva, JOptionPane.OK_CANCEL_OPTION);
                if (result == JOptionPane.OK_OPTION) {
                    String nombre = nombreField.getText();
                    if (nombre.trim().isEmpty()) { throw new IllegalArgumentException("El nombre no puede estar vacío."); }
                    int edad = Integer.parseInt(edadField.getText());
                    int altura = Integer.parseInt(alturaField.getText());
                    reservaSeleccionada.agregarPersona(new Persona(nombre, edad, altura));
                    actualizarTablaReservas();
                }
            } catch (Exception ex) {
                mostrarError(ex);
            }
        }
    }

    private JPanel crearPanelFormularioPrincipal(Atraccion atr) {
        JPanel panel = new JPanel(new GridLayout(0, 2, 5, 5));
        
        panel.add(new JLabel("Fecha de la Reserva:"));
        JDateChooser dateChooser = new JDateChooser();
        dateChooser.setDate(new Date());

        Calendar cal = Calendar.getInstance();
        cal.set(2025, Calendar.SEPTEMBER, 27);
        dateChooser.setMinSelectableDate(cal.getTime());
        
        panel.add(dateChooser);
        
        // JComboBox para la hora
        panel.add(new JLabel("Horarios Disponibles:"));
        JComboBox<LocalTime> horaComboBox = new JComboBox<>();
        List<LocalTime> horarios = generarHorariosDisponibles(atr);
        for (LocalTime horario : horarios) {
            horaComboBox.addItem(horario);
        }
        panel.add(horaComboBox);
        
        panel.add(new JLabel("Cantidad de Personas:"));
        panel.add(new JTextField());
        panel.add(new JLabel("Nombre del Responsable:"));
        panel.add(new JTextField());

        return panel;
    }
    
    private List<LocalTime> generarHorariosDisponibles(Atraccion atr) {
        List<LocalTime> horarios = new ArrayList<>();
        if (atr == null || atr.getDuracion() <= 0) {
            return horarios;
        }

        LocalTime horarioActual = atr.getApertura();
        while (horarioActual.isBefore(atr.getCierre())) {
            horarios.add(horarioActual);
            horarioActual = horarioActual.plusMinutes(atr.getDuracion());
        }
        return horarios;
    }

    private List<Persona> registrarPersonas(int cantidadPersonas, String nombreResponsable) throws NumberFormatException {
        // Usamos un JScrollPane para que el formulario no sea demasiado grande si hay muchas personas
        JPanel panelPersonas = new JPanel(new GridLayout(0, 4, 5, 5)); // 4 columnas: Label, Txt, Label, Txt
        List<JTextField> camposEdad = new ArrayList<>();
        List<JTextField> camposAltura = new ArrayList<>();

        for (int i = 1; i <= cantidadPersonas; i++) {
            String etiquetaNombre = (i == 1) ? nombreResponsable + " (Responsable):" : "Acompañante " + i + ":";
            
            panelPersonas.add(new JLabel(etiquetaNombre));
            panelPersonas.add(new JLabel("")); // Espacio vacío para alinear
            
            JTextField campoEdad = new JTextField();
            camposEdad.add(campoEdad);
            panelPersonas.add(new JLabel("Edad:"));
            panelPersonas.add(campoEdad);

            JTextField campoAltura = new JTextField();
            camposAltura.add(campoAltura);
            panelPersonas.add(new JLabel("Altura (cm):"));
            panelPersonas.add(campoAltura);
        }

        JScrollPane scrollPane = new JScrollPane(panelPersonas);
        scrollPane.setPreferredSize(new Dimension(450, 300));

        int result = JOptionPane.showConfirmDialog(this, scrollPane, "Registrar Personas del Grupo", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

        if (result == JOptionPane.OK_OPTION) {
            List<Persona> grupo = new ArrayList<>();
            for (int i = 0; i < camposEdad.size(); i++) {
                int edad = Integer.parseInt(camposEdad.get(i).getText());
                int altura = Integer.parseInt(camposAltura.get(i).getText());
                
                if (edad <= 0 || altura <= 0) throw new IllegalArgumentException("La edad y la altura deben ser números positivos.");
                
                String nombre = (i == 0) ? nombreResponsable : "Acompañante " + (i + 1);
                grupo.add(new Persona(nombre, edad, altura));
            }
            return grupo;
        }
        return null; // El usuario presionó Cancelar
    }

    private void actualizarComboBox() {
        atraccionesComboBox.removeAllItems();
        if (parque.getAtraccionesCollection() != null) {
            for (Atraccion a : parque.getAtraccionesCollection()) {
                atraccionesComboBox.addItem(new AtraccionWrapper(a));
            }
        }
    }
    
    // ESTE ES EL MÉTODO QUE TE FALTABA
    private void actualizarTablaReservas() {
        tableModel.setRowCount(0); // Limpia la tabla
        Atraccion atr = getAtraccionSeleccionada();
        if (atr != null) {
            for (Reserva r : atr.getReservas()) {
                Object[] row = {
                    r.getCodigoR(),
                    r.getFecha(),
                    r.getHora(),
                    // Muestra "Personas registradas / Total de la reserva"
                    r.getGrupo().size() + " / " + r.getNumeroPersonas() 
                };
                tableModel.addRow(row);
            }
        }
    }

    private Atraccion getAtraccionSeleccionada() {
        AtraccionWrapper wrapper = (AtraccionWrapper) atraccionesComboBox.getSelectedItem();
        return (wrapper != null) ? wrapper.getAtraccion() : null;
    }

    private void mostrarError(Exception ex) {
        String message;
        if (ex instanceof NumberFormatException) {
            message = "La cantidad y la edad deben ser números válidos.";
        } else if (ex instanceof DateTimeParseException) {
            message = "El formato de hora debe ser HH:MM (ej. 14:30).";
        } else if (ex instanceof IllegalArgumentException || ex instanceof IllegalStateException) {
            message = ex.getMessage();
        } else {
            message = "Ocurrió un error inesperado.";
        }
        JOptionPane.showMessageDialog(this, message, "Error", JOptionPane.ERROR_MESSAGE);
    }

    // Clase interna para mostrar el objeto Atraccion correctamente en el JComboBox
    private static class AtraccionWrapper {
        private Atraccion atraccion;
        public AtraccionWrapper(Atraccion atraccion) { this.atraccion = atraccion; }
        public Atraccion getAtraccion() { return atraccion; }
        @Override
        public String toString() { return atraccion.getCodigo() + " - " + atraccion.getNombre(); }
    }
}