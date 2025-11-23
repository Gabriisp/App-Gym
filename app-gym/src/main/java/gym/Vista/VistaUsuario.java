package gym.Vista;

import java.awt.*;
import java.sql.SQLException;
import java.util.List;

import javax.swing.*;

import gym.Modelo.GestorBD;
import gym.Modelo.Entidades;

// Vista para la interfaz de usuario después del login
public class VistaUsuario {

    public int mostrarMenuUsuario(String nombre) {
        JDialog dialog = new JDialog();
        dialog.setTitle("Menú Usuario - Olympus");
        dialog.setModal(true);
        dialog.setLayout(new BorderLayout());
        dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        JLabel titulo = new JLabel("Hola, " + nombre + "!");
        titulo.setFont(new Font("Arial", Font.BOLD, 18));
        titulo.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        JLabel subtitulo = new JLabel("¿Qué deseas hacer?");
        subtitulo.setFont(new Font("Arial", Font.PLAIN, 14));
        subtitulo.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        panel.add(titulo);
        panel.add(Box.createVerticalStrut(10));
        panel.add(subtitulo);
        panel.add(Box.createVerticalStrut(30));
        
        final int[] resultado = {-1};
        
        String[] opciones = {
            "Ver mis rutinas",
            "Ver/Editar ejercicios de rutina",
            "Crear rutina",
            "Crear ejercicio nuevo",
            "Registrar entrenamiento",
            "Ver mi progreso",
            "Eliminar Rutina",
            "Solicitar Entrenador", 
            "Cerrar sesión"
        };
        
        for (int i = 0; i < opciones.length; i++) {
            final int index = i;
            JButton boton = new JButton(opciones[i]);
            boton.setFont(new Font("Arial", Font.PLAIN, 14));
            boton.setAlignmentX(Component.CENTER_ALIGNMENT);
            boton.setMaximumSize(new Dimension(300, 45));
            boton.setPreferredSize(new Dimension(300, 45));
            boton.setFocusPainted(false);
            boton.setBackground(new Color(240, 240, 240));
            
            if (i > 0) {
                panel.add(Box.createVerticalStrut(10));
            }
            
            boton.addActionListener(e -> {
                resultado[0] = index;
                dialog.dispose();
            });
            
            panel.add(boton);
        }
        
        JScrollPane scrollPane = new JScrollPane(panel);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.setPreferredSize(new Dimension(400, 550));
        
        dialog.add(scrollPane, BorderLayout.CENTER);
        dialog.pack();
        dialog.setLocationRelativeTo(null); 
        dialog.setVisible(true);
        
        return resultado[0];
    }

    //  Mostrar selección de entrenador
    public int mostrarSeleccionEntrenador(List<Entidades.Usuario> entrenadores) {
        if (entrenadores.isEmpty()) {
            mostrarMensaje("No hay entrenadores disponibles.");
            return -1;
        }

        Object[] opciones = new Object[entrenadores.size()];
        for (int i = 0; i < entrenadores.size(); i++) {
            opciones[i] = entrenadores.get(i).getNombre() + " (" + entrenadores.get(i).getEmail() + ")";
        }

        return JOptionPane.showOptionDialog(
                null,
                "Selecciona el entrenador al que quieres enviar solicitud:",
                "Solicitar Entrenador",
                JOptionPane.DEFAULT_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                null,
                opciones,
                opciones[0]);
    }

    // Mostrar mensaje de solicitud
    public String mostrarMensajeSolicitud() {
        JTextArea textArea = new JTextArea(5, 30);
        textArea.setLineWrap(true);
        textArea.setWrapStyleWord(true);
        
        JScrollPane scrollPane = new JScrollPane(textArea);
        
        int result = JOptionPane.showConfirmDialog(
                null,
                scrollPane,
                "Mensaje para el entrenador (opcional)",
                JOptionPane.OK_CANCEL_OPTION,
                JOptionPane.PLAIN_MESSAGE);
        
        if (result == JOptionPane.OK_OPTION) {
            return textArea.getText().trim();
        }
        return null;
    }

    // Menú para gestionar ejercicios de una rutina específica
    public int mostrarMenuEjerciciosRutina(String nombreRutina) {
        JDialog dialog = new JDialog();
        dialog.setTitle("Gestión de Ejercicios - " + nombreRutina);
        dialog.setModal(true);
        dialog.setLayout(new BorderLayout());
        dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        JLabel titulo = new JLabel("Gestión de Ejercicios");
        titulo.setFont(new Font("Arial", Font.BOLD, 18));
        titulo.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        JLabel subtitulo = new JLabel("Rutina: " + nombreRutina);
        subtitulo.setFont(new Font("Arial", Font.PLAIN, 14));
        subtitulo.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        panel.add(titulo);
        panel.add(Box.createVerticalStrut(10));
        panel.add(subtitulo);
        panel.add(Box.createVerticalStrut(30));
        
        final int[] resultado = {-1};
        
        String[] opciones = {
            "Ver ejercicios de la rutina",
            "Agregar ejercicios a rutina", 
            "Borrar ejercicio de rutina",
            "Volver al menú principal"
        };
        
        for (int i = 0; i < opciones.length; i++) {
            final int index = i;
            JButton boton = new JButton(opciones[i]);
            boton.setFont(new Font("Arial", Font.PLAIN, 14));
            boton.setAlignmentX(Component.CENTER_ALIGNMENT);
            boton.setMaximumSize(new Dimension(300, 45));
            boton.setPreferredSize(new Dimension(300, 45));
            boton.setFocusPainted(false);
            boton.setBackground(new Color(240, 240, 240));
            
            if (i > 0) {
                panel.add(Box.createVerticalStrut(10));
            }
            
            boton.addActionListener(e -> {
                resultado[0] = index;
                dialog.dispose();
            });
            
            panel.add(boton);
        }
        
        JScrollPane scrollPane = new JScrollPane(panel);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.setPreferredSize(new Dimension(400, 350));
        
        dialog.add(scrollPane, BorderLayout.CENTER);
        dialog.pack();
        dialog.setLocationRelativeTo(null); 
        dialog.setVisible(true);
        
        return resultado[0];
    }
    // Vista para seleccionar una rutina para iniciar un entrenamiento
    public int mostrarSeleccionRutinaParaEntrenamiento(List<Entidades.Rutina> rutinas) {
        if (rutinas.isEmpty()) {
            mostrarMensaje("No tienes rutinas creadas.");
            return -1;
        }

        Object[] opciones = new Object[rutinas.size()];
        for (int i = 0; i < rutinas.size(); i++) {
            opciones[i] = "ENTRENAR: " + rutinas.get(i).getNombre() + " - " + rutinas.get(i).getDescripcion();
        }

        return JOptionPane.showOptionDialog(
                null,
                "Selecciona la rutina para tu entrenamiento de hoy:",
                "Seleccionar Rutina para Entrenar",
                JOptionPane.DEFAULT_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                null, opciones, opciones[0]);
    }

    public boolean confirmarAccion(String mensaje) {
        int respuesta = JOptionPane.showConfirmDialog(null, mensaje, "Confirmar", JOptionPane.YES_NO_OPTION);
        return respuesta == JOptionPane.YES_OPTION;
    }
    // Vista para mostrar las rutinas del usuario
    public void mostrarRutinas(List<Entidades.Rutina> rutinas) {
        if (rutinas.isEmpty()) {
            mostrarMensaje("No tienes rutinas creadas.");
            return;
        }

        StringBuilder sb = new StringBuilder("Mis rutinas:\n\n");
        for (Entidades.Rutina r : rutinas) {
            sb.append("• ").append(r.getNombre())
                    .append(" - ").append(r.getDescripcion())
                    .append(" - Creada: ").append(r.getFechaCreacion().toLocalDate())
                    .append("\n");
        }

        JTextArea area = new JTextArea(sb.toString());
        area.setEditable(false);
        JScrollPane scroll = new JScrollPane(area);
        scroll.setPreferredSize(new Dimension(500, 300));

        JOptionPane.showMessageDialog(null, scroll, "Mis Rutinas", JOptionPane.INFORMATION_MESSAGE);
    }
    // Vista para crear una nueva rutina personal
    public String[] mostrarCrearRutina() {
        JPanel panel = new JPanel(new GridLayout(2, 2, 10, 10));

        JTextField txtNombre = new JTextField();
        JTextField txtDescripcion = new JTextField();

        panel.add(new JLabel("Nombre rutina:"));
        panel.add(txtNombre);
        panel.add(new JLabel("Descripción:"));
        panel.add(txtDescripcion);

        int result = JOptionPane.showConfirmDialog(
                null, panel, "Crear Rutina Personal",
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

        if (result == JOptionPane.OK_OPTION) {
            String nombre = txtNombre.getText().trim();
            String descripcion = txtDescripcion.getText().trim();

            if (nombre.isEmpty()) {
                mostrarError("El nombre es obligatorio");
                return null;
            }
            return new String[] { nombre, descripcion };
        }
        return null;
    }

    public int mostrarSeleccionRutinaParaEjercicios(List<Entidades.Rutina> rutinas) {
        if (rutinas.isEmpty()) {
            mostrarMensaje("No tienes rutinas creadas.");
            return -1;
        }

        Object[] opciones = new Object[rutinas.size()];
        for (int i = 0; i < rutinas.size(); i++) {
            opciones[i] = rutinas.get(i).getNombre() + " - " + rutinas.get(i).getDescripcion();
        }

        return JOptionPane.showOptionDialog(
                null,
                "Selecciona la rutina para agregar ejercicios:",
                "Seleccionar Rutina",
                JOptionPane.DEFAULT_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                null, opciones, opciones[0]);
    }
    // Vista para mostrar los ejercicios de una rutina específica
    public void mostrarEjerciciosRutina(List<Entidades.EjercicioRutina> ejercicios) {
        if (ejercicios.isEmpty()) {
            mostrarMensaje("Esta rutina no tiene ejercicios.");
            return;
        }

        StringBuilder sb = new StringBuilder("Ejercicios en la rutina:\n\n");
        for (Entidades.EjercicioRutina er : ejercicios) {
            sb.append("• ").append(er.getEjercicioNombre())
                    .append(" (").append(er.getGrupoMuscular()).append(")\n")
                    .append("   Series: ").append(er.getSeriesPlanificadas())
                    .append(" x ").append(er.getRepeticionesPlanificadas()).append(" reps")
                    .append(" - Peso: ").append(er.getPesoRecomendado()).append(" kg\n")
                    .append("   Descanso: ").append(er.getDescansoSegundos()).append(" segundos\n")
                    .append("   Orden: ").append(er.getOrden()).append("\n\n");
        }

        JTextArea area = new JTextArea(sb.toString());
        area.setEditable(false);
        JScrollPane scroll = new JScrollPane(area);
        scroll.setPreferredSize(new Dimension(600, 400));

        JOptionPane.showMessageDialog(null, scroll, "Ejercicios de Rutina", JOptionPane.INFORMATION_MESSAGE);
    }
    // Vista para agregar un ejercicio a una rutina
    public String[] mostrarAgregarEjercicioRutina(List<Entidades.Ejercicio> ejerciciosDisponibles) {
        JPanel panel = new JPanel(new GridLayout(6, 2, 10, 10));

        JComboBox<String> comboEjercicios = new JComboBox<>();
        for (Entidades.Ejercicio ejercicio : ejerciciosDisponibles) {
            comboEjercicios.addItem(ejercicio.getNombre() + " (" + ejercicio.getGrupoMuscular() + ")");
        }

        JTextField txtSeries = new JTextField("3");
        JTextField txtRepeticiones = new JTextField("10");
        JTextField txtPeso = new JTextField("0.0");
        JTextField txtDescanso = new JTextField("60");
        JTextField txtOrden = new JTextField("1");

        panel.add(new JLabel("Ejercicio:"));
        panel.add(comboEjercicios);
        panel.add(new JLabel("Series:"));
        panel.add(txtSeries);
        panel.add(new JLabel("Repeticiones:"));
        panel.add(txtRepeticiones);
        panel.add(new JLabel("Peso (kg):"));
        panel.add(txtPeso);
        panel.add(new JLabel("Descanso (seg):"));
        panel.add(txtDescanso);
        panel.add(new JLabel("Orden:"));
        panel.add(txtOrden);

        int result = JOptionPane.showConfirmDialog(
                null, panel, "Agregar Ejercicio a Rutina",
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

        if (result == JOptionPane.OK_OPTION) {
            try {
                int series = Integer.parseInt(txtSeries.getText().trim());
                int repeticiones = Integer.parseInt(txtRepeticiones.getText().trim());
                double peso = Double.parseDouble(txtPeso.getText().trim());
                int descanso = Integer.parseInt(txtDescanso.getText().trim());
                int orden = Integer.parseInt(txtOrden.getText().trim());

                if (series <= 0 || repeticiones <= 0 || orden <= 0) {
                    mostrarError("Series, repeticiones y orden deben ser positivos");
                    return null;
                }

                int ejercicioIndex = comboEjercicios.getSelectedIndex();

                return new String[] {
                        String.valueOf(ejercicioIndex),
                        String.valueOf(series),
                        String.valueOf(repeticiones),
                        String.valueOf(peso),
                        String.valueOf(descanso),
                        String.valueOf(orden)
                };

            } catch (NumberFormatException e) {
                mostrarError("Por favor ingresa valores numéricos válidos");
                return null;
            }
        }
        return null;
    }

    public int mostrarSeleccionRutina(List<Entidades.Rutina> rutinas) {
        return mostrarSeleccionRutinaParaEjercicios(rutinas);
    }
    // Vista para registrar una sesión de entrenamiento
    public String[] mostrarRegistroEntrenamiento() {
        JPanel panel = new JPanel(new BorderLayout(5, 5));
        JPanel campos = new JPanel(new GridLayout(2, 2, 5, 5));
        
        JTextField txtDuracion = new JTextField("1.0");
        JTextArea txtNotas = new JTextArea(5, 20); 
        txtNotas.setLineWrap(true);
        JScrollPane scrollNotas = new JScrollPane(txtNotas);
        
        campos.add(new JLabel("Duración (horas):"));
        campos.add(txtDuracion);
        campos.add(new JLabel("Notas / Sensaciones:"));
        
        panel.add(campos, BorderLayout.NORTH);
        panel.add(scrollNotas, BorderLayout.CENTER);
        
        int result = JOptionPane.showConfirmDialog(
            null, panel, "Registrar Sesión de Entrenamiento", 
            JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE
        );
        
        if (result == JOptionPane.OK_OPTION) {
            try {
                double duracion = Double.parseDouble(txtDuracion.getText().trim());
                if (duracion <= 0) {
                    mostrarError("La duración debe ser mayor a 0");
                    return null;
                }
                
                String notas = txtNotas.getText().trim();
                if (notas.isEmpty()) notas = "Sin notas"; 
                
                return new String[]{String.valueOf(duracion), notas};
                
            } catch (NumberFormatException e) {
                mostrarError("Por favor ingresa una duración válida (ej: 1.5)");
                return null;
            }
        }
        return null; 
    }
    // Vista para registrar un ejercicio específico durante el entrenamiento
    public String[] mostrarRegistroEjercicio(Entidades.EjercicioRutina ejercicio) {
        JPanel panel = new JPanel(new GridLayout(5, 2, 10, 10));
        
        JLabel lblEjercicio = new JLabel("Ejercicio: " + ejercicio.getEjercicioNombre());
        JTextField txtSeries = new JTextField(String.valueOf(ejercicio.getSeriesPlanificadas()));
        JTextField txtRepeticiones = new JTextField(String.valueOf(ejercicio.getRepeticionesPlanificadas()));
        JTextField txtPeso = new JTextField(String.valueOf(ejercicio.getPesoRecomendado()));
        JTextField txtRpe = new JTextField("6");
        
        panel.add(lblEjercicio);
        panel.add(new JLabel("")); 
        panel.add(new JLabel("Series realizadas:"));
        panel.add(txtSeries);
        panel.add(new JLabel("Repeticiones:"));
        panel.add(txtRepeticiones);
        panel.add(new JLabel("Peso usado (kg):"));
        panel.add(txtPeso);
        panel.add(new JLabel("RPE (1-10):"));
        panel.add(txtRpe);
        
        int result = JOptionPane.showConfirmDialog(
            null, panel, "Registrar " + ejercicio.getEjercicioNombre(), 
            JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE
        );
        
        if (result == JOptionPane.OK_OPTION) {
            try {
                int series = Integer.parseInt(txtSeries.getText().trim());
                int repeticiones = Integer.parseInt(txtRepeticiones.getText().trim());
                double peso = Double.parseDouble(txtPeso.getText().trim());
                int rpe = Integer.parseInt(txtRpe.getText().trim());
                
                if (series <= 0 || repeticiones <= 0 || rpe < 1 || rpe > 10) {
                    mostrarError("Valores inválidos. RPE debe ser entre 1-10");
                    return null;
                }
                
                return new String[]{
                    String.valueOf(series),
                    String.valueOf(repeticiones), 
                    String.valueOf(peso),
                    String.valueOf(rpe)
                };
                
            } catch (NumberFormatException e) {
                mostrarError("Por favor ingresa valores numéricos válidos");
                return null;
            }
        }
        return null;
    }
    // Vista para seleccionar ejercicio a borrar de una rutina
    public int mostrarSeleccionEjercicioParaBorrar(List<Entidades.EjercicioRutina> ejercicios) {
        if (ejercicios.isEmpty()) return -1;

        Object[] opciones = new Object[ejercicios.size()];
        for (int i = 0; i < ejercicios.size(); i++) {
            opciones[i] = "Orden " + ejercicios.get(i).getOrden() + ": " + ejercicios.get(i).getEjercicioNombre();
        }

        return JOptionPane.showOptionDialog(null,
                "Selecciona el ejercicio que quieres ELIMINAR:",
                "Borrar Ejercicio",
                JOptionPane.DEFAULT_OPTION, JOptionPane.WARNING_MESSAGE,
                null, opciones, opciones[0]);
    }
    // Vista para crear un nuevo ejercicio en la base de datos
    public String[] mostrarCrearEjercicioNuevo() {
        JPanel panel = new JPanel(new GridLayout(4, 2, 10, 10));

        JTextField txtNombre = new JTextField();
        JTextField txtGrupo = new JTextField();
        JTextField txtEquipo = new JTextField("Ninguno");
        JTextArea txtDesc = new JTextArea(3, 20);

        panel.add(new JLabel("Nombre Ejercicio:"));
        panel.add(txtNombre);
        panel.add(new JLabel("Grupo Muscular:"));
        panel.add(txtGrupo);
        panel.add(new JLabel("Equipamiento:"));
        panel.add(txtEquipo);
        panel.add(new JLabel("Descripción:"));
        panel.add(new JScrollPane(txtDesc));

        int result = JOptionPane.showConfirmDialog(null, panel, "Crear Nuevo Ejercicio en Base de Datos",
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

        if (result == JOptionPane.OK_OPTION) {
            if (txtNombre.getText().trim().isEmpty()) {
                mostrarError("El nombre es obligatorio");
                return null;
            }
            return new String[] {
                    txtNombre.getText().trim(),
                    txtGrupo.getText().trim(),
                    txtEquipo.getText().trim(),
                    txtDesc.getText().trim()
            };
        }
        return null;
    }
    // Vista para mostrar el progreso completo del usuario
    public void mostrarProgresoCompleto(List<Entidades.Entrenamiento> entrenamientos, 
                                       List<Entidades.Ejercicio> misEjercicios,
                                       int idUsuario) {
        
        JDialog dialog = new JDialog();
        dialog.setTitle("Mi Progreso Completo - Olympus");
        dialog.setModal(true);
        dialog.setLayout(new BorderLayout());
        dialog.setPreferredSize(new Dimension(900, 700));
        
        JTabbedPane tabbedPane = new JTabbedPane();
        
        JPanel panelEntrenamientos = new JPanel(new BorderLayout());
        StringBuilder sbEntrenamientos = new StringBuilder();
        sbEntrenamientos.append("HISTORIAL COMPLETO DE ENTRENAMIENTOS\n");
        sbEntrenamientos.append("=".repeat(60)).append("\n\n");
        
        int totalEntrenamientos = entrenamientos.size();
        double totalHoras = entrenamientos.stream().mapToDouble(Entidades.Entrenamiento::getDuracionHoras).sum();
        double promedioHoras = totalHoras / totalEntrenamientos;
        
        sbEntrenamientos.append("RESUMEN: ").append(totalEntrenamientos).append(" entrenamientos | ")
                       .append(String.format("%.1f", totalHoras)).append(" horas totales | ")
                       .append(String.format("%.1f", promedioHoras)).append(" horas promedio\n\n");
        
        for (Entidades.Entrenamiento e : entrenamientos) {
            sbEntrenamientos.append(" ").append(e.getFechaEntrenamiento().toLocalDate()).append("\n");
            sbEntrenamientos.append("Duración: ").append(String.format("%.2f", e.getDuracionHoras())).append(" horas\n");
            sbEntrenamientos.append("Notas: ").append(e.getNotas() != null ? e.getNotas() : "Sin notas").append("\n");
            sbEntrenamientos.append("   ").append("-".repeat(40)).append("\n\n");
        }
        
        JTextArea areaEntrenamientos = new JTextArea(sbEntrenamientos.toString());
        areaEntrenamientos.setEditable(false);
        areaEntrenamientos.setFont(new Font("Monospaced", Font.PLAIN, 12));
        JScrollPane scrollEntrenamientos = new JScrollPane(areaEntrenamientos);
        panelEntrenamientos.add(scrollEntrenamientos, BorderLayout.CENTER);
        
        JPanel panelProgreso = new JPanel(new BorderLayout());
        JPanel panelSeleccionEjercicio = new JPanel(new FlowLayout());
        JComboBox<String> comboEjercicios = new JComboBox<>();
        JTextArea areaProgreso = new JTextArea();
        areaProgreso.setEditable(false);
        areaProgreso.setFont(new Font("Monospaced", Font.PLAIN, 12));
        
        for (Entidades.Ejercicio ejercicio : misEjercicios) {
            comboEjercicios.addItem(ejercicio.getNombre());
        }
        
        comboEjercicios.addActionListener(e -> {
            if (comboEjercicios.getSelectedIndex() >= 0) {
                try {
                    Entidades.Ejercicio ejercicioSeleccionado = misEjercicios.get(comboEjercicios.getSelectedIndex());
                    List<Entidades.ProgresoEjercicio> progreso = GestorBD.obtenerProgresoEjercicio(
                        idUsuario, ejercicioSeleccionado.getIdEjercicio());
                    
                    StringBuilder sbProgreso = new StringBuilder();
                    sbProgreso.append("PROGRESO DETALLADO: ").append(ejercicioSeleccionado.getNombre()).append("\n");
                    sbProgreso.append("=".repeat(70)).append("\n\n");
                    sbProgreso.append(String.format("%-12s | %-10s | %-12s | %-8s\n", 
                        "FECHA", "PESO (kg)", "REPETICIONES", "SERIES"));
                    sbProgreso.append("-".repeat(70)).append("\n");
                    
                    for (Entidades.ProgresoEjercicio p : progreso) {
                        sbProgreso.append(String.format("%-12s | %-10.1f | %-12d | %-8d\n",
                                p.getFecha().toLocalDate(),
                                p.getPesoReal(),
                                p.getRepeticionesReales(),
                                p.getSeriesReales()));
                    }
                    
                    if (progreso.isEmpty()) {
                        sbProgreso.append("\n No hay registros para este ejercicio.");
                    } else {
                        sbProgreso.append("\n").append("-".repeat(70)).append("\n");
                        sbProgreso.append(" Total de sesiones: ").append(progreso.size()).append("\n");
                        
                        double avgPeso = progreso.stream().mapToDouble(Entidades.ProgresoEjercicio::getPesoReal).average().orElse(0);
                        double avgReps = progreso.stream().mapToDouble(Entidades.ProgresoEjercicio::getRepeticionesReales).average().orElse(0);
                        double avgSeries = progreso.stream().mapToDouble(Entidades.ProgresoEjercicio::getSeriesReales).average().orElse(0);
                        
                        sbProgreso.append(" Promedios: ").append(String.format("%.1f", avgPeso)).append(" kg | ")
                                 .append(String.format("%.1f", avgReps)).append(" reps | ")
                                 .append(String.format("%.1f", avgSeries)).append(" series\n");
                    }
                    
                    areaProgreso.setText(sbProgreso.toString());
                    
                } catch (SQLException ex) {
                    mostrarError("Error al cargar progreso: " + ex.getMessage());
                }
            }
        });
        
        panelSeleccionEjercicio.add(new JLabel("Selecciona ejercicio:"));
        panelSeleccionEjercicio.add(comboEjercicios);
        panelProgreso.add(panelSeleccionEjercicio, BorderLayout.NORTH);
        panelProgreso.add(new JScrollPane(areaProgreso), BorderLayout.CENTER);
        
        // Pestaña 3: Estadísticas Generales
        JPanel panelEstadisticas = new JPanel(new BorderLayout());
        StringBuilder sbEstadisticas = new StringBuilder();
        sbEstadisticas.append("ESTADÍSTICAS GENERALES DE PROGRESO\n");
        sbEstadisticas.append("=".repeat(50)).append("\n\n");
        
        sbEstadisticas.append("RESUMEN DE ENTRENAMIENTO:\n");
        sbEstadisticas.append("- Total de entrenamientos: ").append(totalEntrenamientos).append("\n");
        sbEstadisticas.append("- Tiempo total entrenado: ").append(String.format("%.2f", totalHoras)).append(" horas\n");
        sbEstadisticas.append("- Promedio por entrenamiento: ").append(String.format("%.2f", promedioHoras)).append(" horas\n");
        sbEstadisticas.append("- Ejercicios diferentes realizados: ").append(misEjercicios.size()).append("\n");
        
        long diasConsecutivos = calcularDiasConsecutivos(entrenamientos);
        sbEstadisticas.append("- Días consecutivos entrenando: ").append(diasConsecutivos).append("\n");
        sbEstadisticas.append("- Último entrenamiento: ").append(entrenamientos.get(0).getFechaEntrenamiento().toLocalDate()).append("\n\n");
        
        sbEstadisticas.append("EJERCICIOS REALIZADOS:\n");
        for (Entidades.Ejercicio ej : misEjercicios) {
            sbEstadisticas.append("- ").append(ej.getNombre()).append(" (").append(ej.getGrupoMuscular()).append(")\n");
        }
        
        sbEstadisticas.append("\n GRUPOS MUSCULARES TRABAJADOS:\n");
        misEjercicios.stream()
            .map(Entidades.Ejercicio::getGrupoMuscular)
            .distinct()
            .forEach(grupo -> sbEstadisticas.append("- ").append(grupo).append("\n"));
        
        JTextArea areaEstadisticas = new JTextArea(sbEstadisticas.toString());
        areaEstadisticas.setEditable(false);
        areaEstadisticas.setFont(new Font("Monospaced", Font.PLAIN, 12));
        panelEstadisticas.add(new JScrollPane(areaEstadisticas), BorderLayout.CENTER);
        
        tabbedPane.addTab("Historial", panelEntrenamientos);
        tabbedPane.addTab("Progreso", panelProgreso);
        tabbedPane.addTab("Estadísticas", panelEstadisticas);
        
        JPanel panelBotones = new JPanel();
        JButton btnDetalles = new JButton("Ver Detalles de Entrenamiento Específico");
        btnDetalles.addActionListener(e -> {
            Object[] opcionesEntrenamientos = new Object[entrenamientos.size()];
            for (int i = 0; i < entrenamientos.size(); i++) {
                opcionesEntrenamientos[i] = entrenamientos.get(i).getFechaEntrenamiento().toLocalDate()
                        + " - " + String.format("%.2f", entrenamientos.get(i).getDuracionHoras()) + " horas";
            }
            
            int seleccion = JOptionPane.showOptionDialog(dialog,
                    "Selecciona un entrenamiento para ver detalles completos:",
                    "Detalles de Entrenamiento",
                    JOptionPane.DEFAULT_OPTION,
                    JOptionPane.QUESTION_MESSAGE,
                    null,
                    opcionesEntrenamientos,
                    opcionesEntrenamientos[0]);
            
            if (seleccion >= 0) {
                try {
                    Entidades.Entrenamiento entrenamiento = entrenamientos.get(seleccion);
                    List<Entidades.Ejecuta> ejecuciones = GestorBD.obtenerEjecucionesDeEntrenamiento(entrenamiento.getIdEntrenamiento());
                    mostrarDetallesEntrenamientoCompleto(entrenamiento, ejecuciones);
                } catch (SQLException ex) {
                    mostrarError("Error al cargar detalles: " + ex.getMessage());
                }
            }
        });
        
        panelBotones.add(btnDetalles);
        
        dialog.add(tabbedPane, BorderLayout.CENTER);
        dialog.add(panelBotones, BorderLayout.SOUTH);
        
        dialog.pack();
        dialog.setLocationRelativeTo(null);
        dialog.setVisible(true);
        
        if (!misEjercicios.isEmpty()) {
            comboEjercicios.setSelectedIndex(0);
        }
    }

    // Método para calcular días consecutivos
    private long calcularDiasConsecutivos(List<Entidades.Entrenamiento> entrenamientos) {
        if (entrenamientos.isEmpty()) return 0;
        
        entrenamientos.sort((e1, e2) -> e2.getFechaEntrenamiento().compareTo(e1.getFechaEntrenamiento()));
        
        long consecutivos = 0;
        java.time.LocalDate fechaActual = java.time.LocalDate.now();
        
        for (Entidades.Entrenamiento e : entrenamientos) {
            java.time.LocalDate fechaEntreno = e.getFechaEntrenamiento().toLocalDate();
            
            if (fechaEntreno.equals(fechaActual.minusDays(consecutivos))) {
                consecutivos++;
            } else {
                break;
            }
        }
        return consecutivos;
    }

    public void mostrarDetallesEntrenamientoCompleto(Entidades.Entrenamiento entrenamiento,
            List<Entidades.Ejecuta> ejecuciones) {
        StringBuilder sb = new StringBuilder();
        sb.append("DETALLES DEL ENTRENAMIENTO (").append(entrenamiento.getFechaEntrenamiento().toLocalDate())
                .append(")\n\n");

        if (ejecuciones.isEmpty()) {
            sb.append("No hay ejercicios registrados en esta sesión.");
        } else {
            for (Entidades.Ejecuta ex : ejecuciones) {
                sb.append(" ").append(ex.getEjercicioNombre()).append("\n")
                        .append("   Realizado: ").append(ex.getSeriesReales()).append(" series x ")
                        .append(ex.getRepeticionesReales()).append(" reps @ ")
                        .append(ex.getPesoReal()).append(" kg\n")
                        .append("   RPE: ").append(ex.getRpe()).append("/10\n\n");
            }
        }

        JTextArea area = new JTextArea(sb.toString());
        area.setEditable(false);
        JScrollPane scroll = new JScrollPane(area);
        scroll.setPreferredSize(new Dimension(400, 400));
        JOptionPane.showMessageDialog(null, scroll, "Detalle Entrenamiento", JOptionPane.INFORMATION_MESSAGE);
    }

    public void mostrarMensaje(String mensaje) {
        JOptionPane.showMessageDialog(null, mensaje, "Olympus - Usuario", JOptionPane.INFORMATION_MESSAGE);
    }

    public void mostrarError(String mensaje) {
        JOptionPane.showMessageDialog(null, mensaje, "Error", JOptionPane.ERROR_MESSAGE);
    }
}