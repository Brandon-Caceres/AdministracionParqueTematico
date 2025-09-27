/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package administracionparquetematico;

/**
 *
 * @author Brandon
 */
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.StringJoiner;

/**
 * Clase utilitaria para manejar la persistencia de datos del Parque Temático.
 * Se encarga de guardar y cargar el estado de las atracciones, reservas y personas
 * utilizando archivos CSV.
 */
public class PersistenciaParque {

    private static final String SEPARADOR = ",";

    /**
     * Guarda el estado completo del parque en archivos CSV dentro de un directorio específico.
     *
     * @param parque El objeto Parque con el estado actual.
     * @param directorio La carpeta donde se guardarán los archivos CSV.
     */
    public static void guardarDatos(Parque parque, String directorio) {
        File dir = new File(directorio);
        if (!dir.exists()) {
            dir.mkdirs(); // Crea el directorio si no existe
        }
        guardarAtracciones(parque, directorio + "/atracciones.csv");
        guardarReservasYPersonas(parque, directorio + "/reservas.csv", directorio + "/personas.csv");
    }

    private static void guardarAtracciones(Parque parque, String rutaArchivo) {
        try (PrintWriter writer = new PrintWriter(new OutputStreamWriter(new FileOutputStream(rutaArchivo), StandardCharsets.UTF_8))) {
            // Añadir nuevas columnas al encabezado
            writer.println("codigo,nombre,descripcion,cantidadMax,horaApertura,horaCierre,edadMinima,alturaMinimaCm");
            for (Atraccion atr : parque.getAtraccionesCollection()) {
                StringJoiner sj = new StringJoiner(SEPARADOR);
                sj.add(String.valueOf(atr.getCodigo()));
                sj.add(escaparCampo(atr.getNombre()));
                sj.add(escaparCampo(atr.getDescripcion()));
                sj.add(String.valueOf(atr.getCantidadMax()));
                sj.add(atr.getApertura().toString());
                sj.add(atr.getCierre().toString());
                sj.add(String.valueOf(atr.getEdad()));
                sj.add(String.valueOf(atr.getAltura()));
                sj.add(String.valueOf(atr.getDuracion())); 
                writer.println(sj.toString());
            }
        } catch (IOException e) {
            System.err.println("Error al guardar atracciones: " + e.getMessage());
        }
    }

    private static void guardarReservasYPersonas(Parque parque, String rutaReservas, String rutaPersonas) {
        try (PrintWriter writerR = new PrintWriter(new OutputStreamWriter(new FileOutputStream(rutaReservas), StandardCharsets.UTF_8));
             PrintWriter writerP = new PrintWriter(new OutputStreamWriter(new FileOutputStream(rutaPersonas), StandardCharsets.UTF_8))) {

            writerR.println("codigoAtraccion,codigoReserva,fecha,hora,numeroPersonas");
            writerP.println("codigoAtraccion,codigoReserva,nombre,edad,alturaCm");

            for (Atraccion atr : parque.getAtraccionesCollection()) {
                for (Reserva res : atr.getReservas()) {
                    StringJoiner sjR = new StringJoiner(SEPARADOR);
                    sjR.add(String.valueOf(atr.getCodigo()));
                    sjR.add(String.valueOf(res.getCodigoR()));
                    sjR.add(res.getFecha());
                    sjR.add(res.getHora().toString());
                    sjR.add(String.valueOf(res.getNumeroPersonas()));
                    writerR.println(sjR.toString());

                    for (Persona per : res.getGrupo()) {
                        StringJoiner sjP = new StringJoiner(SEPARADOR);
                        sjP.add(String.valueOf(atr.getCodigo()));
                        sjP.add(String.valueOf(res.getCodigoR()));
                        sjP.add(escaparCampo(per.getNombre()));
                        sjP.add(String.valueOf(per.getEdad()));
                        sjP.add(String.valueOf(per.getAltura()));
                        writerP.println(sjP.toString());
                    }
                }
            }
        } catch (IOException e) {
            System.err.println("Error al guardar reservas y personas: " + e.getMessage());
        }
    }

    /**
     * Carga el estado completo del parque desde archivos CSV en un directorio.
     *
     * @param parque El objeto Parque que será poblado con los datos.
     * @param directorio La carpeta desde donde se leerán los archivos.
     */
    public static void cargarDatos(Parque parque, String directorio) {
        try {
            cargarAtracciones(parque, directorio + "/atracciones.csv");
            cargarReservas(parque, directorio + "/reservas.csv");
            cargarPersonas(parque, directorio + "/personas.csv");
            
            for (Atraccion atr : parque.getAtraccionesCollection()) {
                atr.inicializarContadorReservas();
            }
            System.out.println("Datos cargados y contadores inicializados correctamente.");
        } catch (Exception e) {
            System.err.println("No se pudo cargar la información, iniciando con datos vacíos: " + e.getMessage());
            // e.printStackTrace(); // Descomentar para ver el detalle completo del error
        }
    }

    private static void cargarAtracciones(Parque parque, String rutaArchivo) throws IOException {
        File f = new File(rutaArchivo);
        if (!f.exists()) return;

        try (BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(rutaArchivo), StandardCharsets.UTF_8))) {
            br.readLine(); // Saltar encabezado
            String linea;
            while ((linea = br.readLine()) != null) {
                if (linea.trim().isEmpty()) continue;
                String[] campos = parsearLineaCSV(linea);
                // Actualizar el constructor para leer los nuevos campos
                Atraccion atr = new Atraccion(
                        Integer.parseInt(campos[0]),
                        campos[1],
                        campos[2],
                        Integer.parseInt(campos[3]),
                        campos[4],
                        campos[5],
                        Integer.parseInt(campos[6]),
                        Integer.parseInt(campos[7]),
                        Integer.parseInt(campos[8])
                );
                parque.agregarAtraccion(atr);
            }
        }
    }

    private static void cargarReservas(Parque parque, String rutaArchivo) throws IOException {
        File f = new File(rutaArchivo);
        if (!f.exists()) return;
        
        try (BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(rutaArchivo), StandardCharsets.UTF_8))) {
            br.readLine();
            String linea;
            while ((linea = br.readLine()) != null) {
                if (linea.trim().isEmpty()) continue;
                String[] campos = parsearLineaCSV(linea);
                int codAtraccion = Integer.parseInt(campos[0]);
                Atraccion atr = parque.buscarAtraccion(codAtraccion);
                if (atr != null) {
                    LocalTime hora = LocalTime.parse(campos[3]);
                    Reserva res = new Reserva(
                            Integer.parseInt(campos[1]),
                            atr.getNombre(),
                            campos[2],
                            hora,
                            Integer.parseInt(campos[4])
                    );
                    atr.agregarReserva(res);
                }
            }
        }
    }
    
    private static void cargarPersonas(Parque parque, String rutaArchivo) throws IOException {
        File f = new File(rutaArchivo);
        if (!f.exists()) return;

        try (BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(rutaArchivo), StandardCharsets.UTF_8))) {
            br.readLine();
            String linea;
            while ((linea = br.readLine()) != null) {
                if (linea.trim().isEmpty()) continue;
                String[] campos = parsearLineaCSV(linea);
                int codAtraccion = Integer.parseInt(campos[0]);
                int codReserva = Integer.parseInt(campos[1]);
                Atraccion atr = parque.buscarAtraccion(codAtraccion);
                if (atr != null) {
                    Reserva res = atr.buscarReserva(codReserva);
                    if (res != null) {
                        Persona per = new Persona(campos[2], Integer.parseInt(campos[3]), Integer.parseInt(campos[4]));
                        res.agregarPersona(per);
                    }
                }
            }
        }
    }
    
    private static String escaparCampo(String campo) {
        if (campo.contains(SEPARADOR) || campo.contains("\"")) {
            return "\"" + campo.replace("\"", "\"\"") + "\"";
        }
        return campo;
    }

    private static String[] parsearLineaCSV(String linea) {
        List<String> campos = new ArrayList<>();
        StringBuilder campoActual = new StringBuilder();
        boolean enComillas = false;

        for (int i = 0; i < linea.length(); i++) {
            char c = linea.charAt(i);

            if (c == '"') {
                if (enComillas && i + 1 < linea.length() && linea.charAt(i + 1) == '"') {
                    campoActual.append('"');
                    i++; 
                } else {
                    enComillas = !enComillas;
                }
            } else if (c == ',' && !enComillas) {
                campos.add(campoActual.toString().trim());
                campoActual.setLength(0);
            } else {
                campoActual.append(c);
            }
        }
        campos.add(campoActual.toString().trim());

        return campos.toArray(new String[0]);
    }
}