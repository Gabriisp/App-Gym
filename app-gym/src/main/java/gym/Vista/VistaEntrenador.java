package gym.Vista;

import java.awt.*;
import java.sql.SQLException;
import java.util.List;

import javax.swing.*;

import gym.Modelo.GestorBD;
import gym.Modelo.Entidades;

public class VistaEntrenador {

    public int mostrarMenuEntrenador(String nombre) {
        JDialog dialog = new JDialog();
        dialog.setTitle("Menú Entrenador - Olympus");
        dialog.setModal(true);
        dialog.setLayout(new BorderLayout());
        dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel titulo = new JLabel("Hola, Entrenador " + nombre + "!");
        titulo.setFont(new Font("Arial", Font.BOLD, 18));
        titulo.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel subtitulo = new JLabel("¿Qué deseas hacer?");
        subtitulo.setFont(new Font("Arial", Font.PLAIN, 14));
        subtitulo.setAlignmentX(Component.CENTER_ALIGNMENT);

        panel.add(titulo);
        panel.add(Box.createVerticalStrut(10));
        panel.add(subtitulo);
        panel.add(Box.createVerticalStrut(30));

        final int[] resultado = { -1 };

        String[] opciones = {
                "Ver mis usuarios asignados",
                "Crear rutina para cliente",
                "Ver/Editar ejercicios de rutina",
                "Eliminar rutina",
                "Ver progreso clientes",
                "Gestionar Solicitudes",
                "Ver rutinas de cliente específico",
                "Crear ejercicio general",
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
        scrollPane.setPreferredSize(new Dimension(400, 500));

        dialog.add(scrollPane, BorderLayout.CENTER);
        dialog.pack();
        dialog.setLocationRelativeTo(null);
        dialog.setVisible(true);

        return resultado[0];
    }
    // Mostrar selección de solicitudes


    public int mostrarSeleccionRutinaConOpciones(List<Entidades.Rutina> rutinas) {
        if (rutinas.isEmpty()) {
            mostrarMensaje("No hay rutinas disponibles.");
            return -1;
        }

        Object[] opciones = new Object[rutinas.size()];
        for (int i = 0; i < rutinas.size(); i++) {
            opciones[i] = rutinas.get(i).getNombre() + " - " + rutinas.get(i).getDescripcion();
        }

        int seleccion = JOptionPane.showOptionDialog(
                null,
                "Selecciona una rutina para ver sus ejercicios:",
                "Rutinas del Cliente - Ver Ejercicios",
                JOptionPane.DEFAULT_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                null,
                opciones,
                opciones[0]);

        return seleccion;
    }

    // Mostrar diálogo para crear nuevo ejercicio
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

    public int mostrarSeleccionSolicitud(List<Entidades.Asignacion> solicitudes) {
        if (solicitudes.isEmpty()) {
            mostrarMensaje("No hay solicitudes pendientes.");
            return -1;
        }

        Object[] opciones = new Object[solicitudes.size()];
        for (int i = 0; i < solicitudes.size(); i++) {
            Entidades.Asignacion s = solicitudes.get(i);
            String fecha = s.getFechaSolicitud().toLocalDate().toString();
            opciones[i] = s.getNombreUsuario() + " (" + s.getEmailUsuario() + ") - " + fecha;
        }

        return JOptionPane.showOptionDialog(
                null,
                "Selecciona una solicitud para gestionar:",
                "Solicitudes Pendientes",
                JOptionPane.DEFAULT_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                null,
                opciones,
                opciones[0]);
    }

    // Mostrar opciones para una solicitud
    public int mostrarOpcionesSolicitud(Entidades.Asignacion solicitud) {
        Object[] opciones = { "Aceptar Solicitud", "Rechazar Solicitud" };

        JPanel panel = new JPanel(new BorderLayout());

        StringBuilder info = new StringBuilder();
        info.append("Solicitud de: ").append(solicitud.getNombreUsuario()).append("\n");
        info.append("Email: ").append(solicitud.getEmailUsuario()).append("\n");
        info.append("Fecha: ").append(solicitud.getFechaSolicitud().toLocalDate()).append("\n");
        info.append("Mensaje: ")
                .append(solicitud.getMensajeSolicitud() != null ? solicitud.getMensajeSolicitud() : "Sin mensaje");

        JTextArea textArea = new JTextArea(info.toString());
        textArea.setEditable(false);
        textArea.setBackground(panel.getBackground());

        panel.add(new JScrollPane(textArea), BorderLayout.CENTER);

        return JOptionPane.showOptionDialog(
                null,
                panel,
                "Gestionar Solicitud",
                JOptionPane.DEFAULT_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                null,
                opciones,
                opciones[0]);
    }

    // MMostrar menú 
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

        final int[] resultado = { -1 };

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
    // Mostrar selección de ejercicio para borrar
    public int mostrarSeleccionEjercicioParaBorrar(List<Entidades.EjercicioRutina> ejercicios) {
        if (ejercicios.isEmpty())
            return -1;

        Object[] opciones = new Object[ejercicios.size()];
        for (int i = 0; i < ejercicios.size(); i++) {
            opciones[i] = "Orden " + ejercicios.get(i).getOrden() + ": " + ejercicios.get(i).getEjercicioNombre();
        }

        return JOptionPane.showOptionDialog(null,
                "Selecciona el ejercicio a ELIMINAR:",
                "Borrar Ejercicio de Cliente",
                JOptionPane.DEFAULT_OPTION, JOptionPane.WARNING_MESSAGE,
                null, opciones, opciones[0]);
    }
    // Mostrar mensaje de información
    public boolean confirmarEliminacion(String mensaje) {
        int respuesta = JOptionPane.showConfirmDialog(null, mensaje, "Confirmar Eliminación",
                JOptionPane.YES_NO_OPTION);
        return respuesta == JOptionPane.YES_OPTION;
    }
    // Mostrar lista de usuarios asignados
    public void mostrarUsuarios(List<Entidades.Usuario> usuarios) {
        if (usuarios.isEmpty()) {
            mostrarMensaje("No tienes usuarios asignados.");
            return;
        }

        StringBuilder sb = new StringBuilder("Mis usuarios asignados:\n\n");
        for (Entidades.Usuario u : usuarios) {
            sb.append("• ").append(u.getNombre())
                    .append(" (").append(u.getEmail()).append(")")
                    .append(" - ").append(u.getTipo())
                    .append("\n");
        }

        JTextArea area = new JTextArea(sb.toString());
        area.setEditable(false);
        JScrollPane scroll = new JScrollPane(area);
        scroll.setPreferredSize(new Dimension(500, 300));

        JOptionPane.showMessageDialog(null, scroll, "Mis Usuarios Asignados", JOptionPane.INFORMATION_MESSAGE);
    }
    // Mostrar lista de rutinas de clientes
    public void mostrarRutinas(List<Entidades.Rutina> rutinas) {
        if (rutinas.isEmpty()) {
            mostrarMensaje("No hay rutinas creadas.");
            return;
        }

        StringBuilder sb = new StringBuilder("RUTINAS DE TUS CLIENTES:\n\n");
        for (Entidades.Rutina r : rutinas) {
            try {
                String nombreUsuario = GestorBD.obtenerNombreUsuarioPorId(r.getIdUsuarioCreador());

                sb.append("• ").append(r.getNombre())
                        .append("\n   Descripción: ").append(r.getDescripcion())
                        .append("\n   Cliente: ").append(nombreUsuario)
                        .append("\n   Creada: ").append(r.getFechaCreacion().toLocalDate())
                        .append("\n   Activa: ").append(r.isActiva() ? "Sí" : "No")
                        .append("\n\n");
            } catch (SQLException e) {
                sb.append("• ").append(r.getNombre())
                        .append(" - ").append(r.getDescripcion())
                        .append(" (Creada por: Usuario ").append(r.getIdUsuarioCreador()).append(")")
                        .append(" - ").append(r.getFechaCreacion().toLocalDate())
                        .append("\n");
            }
        }

        JTextArea area = new JTextArea(sb.toString());
        area.setEditable(false);
        JScrollPane scroll = new JScrollPane(area);
        scroll.setPreferredSize(new Dimension(500, 400));

        JOptionPane.showMessageDialog(null, scroll, "Rutinas de Clientes", JOptionPane.INFORMATION_MESSAGE);
    }
    // Mostrar selección de usuario
    public int mostrarSeleccionUsuario(List<Entidades.Usuario> usuarios) {
        if (usuarios.isEmpty()) {
            mostrarMensaje("No hay usuarios disponibles.");
            return -1;
        }

        Object[] opciones = new Object[usuarios.size()];
        for (int i = 0; i < usuarios.size(); i++) {
            opciones[i] = usuarios.get(i).getNombre() + " (" + usuarios.get(i).getEmail() + ")";
        }

        return JOptionPane.showOptionDialog(
                null,
                "Selecciona el usuario:",
                "Seleccionar Usuario",
                JOptionPane.DEFAULT_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                null,
                opciones,
                opciones[0]);
    }
    // Mostrar selección de rutina
    public int mostrarSeleccionRutina(List<Entidades.Rutina> rutinas) {
        if (rutinas.isEmpty()) {
            mostrarMensaje("No hay rutinas disponibles.");
            return -1;
        }

        Object[] opciones = new Object[rutinas.size()];
        for (int i = 0; i < rutinas.size(); i++) {
            opciones[i] = rutinas.get(i).getNombre() + " - " + rutinas.get(i).getDescripcion();
        }

        return JOptionPane.showOptionDialog(
                null,
                "Selecciona la rutina:",
                "Seleccionar Rutina",
                JOptionPane.DEFAULT_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                null,
                opciones,
                opciones[0]);
    }
    // Mostrar mensaje de error
    public void mostrarEjerciciosRutina(List<Entidades.EjercicioRutina> ejercicios) {
        if (ejercicios.isEmpty()) {
            mostrarMensaje("Esta rutina no tiene ejercicios.");
            return;
        }

        StringBuilder sb = new StringBuilder("Ejercicios en la rutina:\n\n");
        for (Entidades.EjercicioRutina er : ejercicios) {
            sb.append("* ").append(er.getEjercicioNombre())
                    .append(" (").append(er.getGrupoMuscular()).append(")\n")
                    .append("  Series: ").append(er.getSeriesPlanificadas())
                    .append(" x ").append(er.getRepeticionesPlanificadas()).append(" reps")
                    .append(" - Peso: ").append(er.getPesoRecomendado()).append(" kg\n")
                    .append("  Descanso: ").append(er.getDescansoSegundos()).append(" segundos\n")
                    .append("  Orden: ").append(er.getOrden()).append("\n\n");
        }

        JTextArea area = new JTextArea(sb.toString());
        area.setEditable(false);
        JScrollPane scroll = new JScrollPane(area);
        scroll.setPreferredSize(new Dimension(600, 400));

        JOptionPane.showMessageDialog(null, scroll, "Ejercicios de Rutina", JOptionPane.INFORMATION_MESSAGE);
    }

    public int mostrarSeleccionProgresoClientes() {
        Object[] opciones = {
                "Ver progreso detallado de un cliente específico",
                "Ver resumen general de todos los clientes"
        };

        return JOptionPane.showOptionDialog(
                null,
                "¿Cómo quieres ver el progreso de tus clientes?",
                "Seleccionar Tipo de Progreso",
                JOptionPane.DEFAULT_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                null,
                opciones,
                opciones[0]);
    }

    // Mostrar progreso completo de un cliente
    public void mostrarProgresoCompletoCliente(List<Entidades.Entrenamiento> entrenamientos,
            List<Entidades.Ejercicio> ejercicios,
            Entidades.Usuario cliente) {

        JDialog dialog = new JDialog();
        dialog.setTitle("Progreso Completo - " + cliente.getNombre());
        dialog.setModal(true);
        dialog.setLayout(new BorderLayout());
        dialog.setPreferredSize(new Dimension(900, 700));

        JTabbedPane tabbedPane = new JTabbedPane();

        JPanel panelEntrenamientos = new JPanel(new BorderLayout());
        StringBuilder sbEntrenamientos = new StringBuilder();
        sbEntrenamientos.append("HISTORIAL COMPLETO DE ENTRENAMIENTOS - ").append(cliente.getNombre()).append("\n");
        sbEntrenamientos.append("=".repeat(60)).append("\n\n");

        int totalEntrenamientos = entrenamientos.size();
        double totalHoras = entrenamientos.stream().mapToDouble(Entidades.Entrenamiento::getDuracionHoras).sum();
        double promedioHoras = totalEntrenamientos > 0 ? totalHoras / totalEntrenamientos : 0;

        sbEntrenamientos.append("RESUMEN: ").append(totalEntrenamientos).append(" entrenamientos | ")
                .append(String.format("%.1f", totalHoras)).append(" horas totales | ")
                .append(String.format("%.1f", promedioHoras)).append(" horas promedio\n\n");

        for (Entidades.Entrenamiento e : entrenamientos) {
            sbEntrenamientos.append(" ").append(e.getFechaEntrenamiento().toLocalDate()).append("\n");
            sbEntrenamientos.append("Duración: ").append(String.format("%.2f", e.getDuracionHoras()))
                    .append(" horas\n");
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
        for (Entidades.Ejercicio ejercicio : ejercicios) {
            comboEjercicios.addItem(ejercicio.getNombre());
        }

        comboEjercicios.addActionListener(e -> {
            if (comboEjercicios.getSelectedIndex() >= 0) {
                try {
                    Entidades.Ejercicio ejercicioSeleccionado = ejercicios.get(comboEjercicios.getSelectedIndex());
                    List<Entidades.ProgresoEjercicio> progreso = GestorBD.obtenerProgresoEjercicio(
                            cliente.getIdUsuario(), ejercicioSeleccionado.getIdEjercicio());

                    StringBuilder sbProgreso = new StringBuilder();
                    sbProgreso.append("PROGRESO DETALLADO: ").append(ejercicioSeleccionado.getNombre()).append("\n");
                    sbProgreso.append("Cliente: ").append(cliente.getNombre()).append("\n");
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

                        double avgPeso = progreso.stream().mapToDouble(Entidades.ProgresoEjercicio::getPesoReal)
                                .average().orElse(0);
                        double avgReps = progreso.stream()
                                .mapToDouble(Entidades.ProgresoEjercicio::getRepeticionesReales).average().orElse(0);
                        double avgSeries = progreso.stream().mapToDouble(Entidades.ProgresoEjercicio::getSeriesReales)
                                .average().orElse(0);

                        sbProgreso.append(" Promedios: ").append(String.format("%.1f", avgPeso)).append(" kg | ")
                                .append(String.format("%.1f", avgReps)).append(" reps | ")
                                .append(String.format("%.1f", avgSeries)).append(" series\n");

                        
                        if (progreso.size() > 1) {
                            Entidades.ProgresoEjercicio primera = progreso.get(progreso.size() - 1); 
                            Entidades.ProgresoEjercicio ultima = progreso.get(0);

                            double mejoraPeso = ultima.getPesoReal() - primera.getPesoReal();
                            double mejoraReps = ultima.getRepeticionesReales() - primera.getRepeticionesReales();

                            sbProgreso.append(" Mejora desde el inicio: ");
                            if (mejoraPeso > 0)
                                sbProgreso.append("+").append(String.format("%.1f", mejoraPeso)).append("kg ");
                            if (mejoraReps > 0)
                                sbProgreso.append("+").append(String.format("%.0f", mejoraReps)).append("reps");
                            if (mejoraPeso <= 0 && mejoraReps <= 0)
                                sbProgreso.append("Sin mejora significativa");
                            sbProgreso.append("\n");
                        }
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

        JPanel panelEstadisticas = new JPanel(new BorderLayout());
        StringBuilder sbEstadisticas = new StringBuilder();
        sbEstadisticas.append("ESTADÍSTICAS GENERALES - ").append(cliente.getNombre()).append("\n");
        sbEstadisticas.append("=".repeat(50)).append("\n\n");

        sbEstadisticas.append("RESUMEN DE ENTRENAMIENTO:\n");
        sbEstadisticas.append("- Total de entrenamientos: ").append(totalEntrenamientos).append("\n");
        sbEstadisticas.append("- Tiempo total entrenado: ").append(String.format("%.2f", totalHoras))
                .append(" horas\n");
        sbEstadisticas.append("- Promedio por entrenamiento: ").append(String.format("%.2f", promedioHoras))
                .append(" horas\n");
        sbEstadisticas.append("- Ejercicios diferentes realizados: ").append(ejercicios.size()).append("\n");

        long diasConsecutivos = calcularDiasConsecutivos(entrenamientos);
        sbEstadisticas.append("- Días consecutivos entrenando: ").append(diasConsecutivos).append("\n");
        if (!entrenamientos.isEmpty()) {
            sbEstadisticas.append("- Último entrenamiento: ")
                    .append(entrenamientos.get(0).getFechaEntrenamiento().toLocalDate()).append("\n");
        }
        sbEstadisticas.append("\n");

        sbEstadisticas.append("EJERCICIOS REALIZADOS:\n");
        for (Entidades.Ejercicio ej : ejercicios) {
            sbEstadisticas.append("- ").append(ej.getNombre()).append(" (").append(ej.getGrupoMuscular()).append(")\n");
        }

        sbEstadisticas.append("\n GRUPOS MUSCULARES TRABAJADOS:\n");
        ejercicios.stream()
                .map(Entidades.Ejercicio::getGrupoMuscular)
                .distinct()
                .forEach(grupo -> sbEstadisticas.append("- ").append(grupo).append("\n"));

        JTextArea areaEstadisticas = new JTextArea(sbEstadisticas.toString());
        areaEstadisticas.setEditable(false);
        areaEstadisticas.setFont(new Font("Monospaced", Font.PLAIN, 12));
        panelEstadisticas.add(new JScrollPane(areaEstadisticas), BorderLayout.CENTER);

        tabbedPane.addTab("Historial", panelEntrenamientos);
        tabbedPane.addTab("Progreso por Ejercicio", panelProgreso);
        tabbedPane.addTab("Estadísticas", panelEstadisticas);

        dialog.add(tabbedPane, BorderLayout.CENTER);
        dialog.pack();
        dialog.setLocationRelativeTo(null);
        dialog.setVisible(true);

        if (!ejercicios.isEmpty()) {
            comboEjercicios.setSelectedIndex(0);
        }
    }
    // Mostrar resumen general de todos los clientes
    public void mostrarResumenGeneral(String resumen) {
        JTextArea area = new JTextArea(resumen);
        area.setEditable(false);
        area.setFont(new Font("Monospaced", Font.PLAIN, 12));
        JScrollPane scroll = new JScrollPane(area);
        scroll.setPreferredSize(new Dimension(700, 500));

        JOptionPane.showMessageDialog(null, scroll, "Resumen General de Clientes", JOptionPane.INFORMATION_MESSAGE);
    }

    private long calcularDiasConsecutivos(List<Entidades.Entrenamiento> entrenamientos) {
        if (entrenamientos.isEmpty())
            return 0;

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
    // Mostrar diálogo para agregar ejercicio a rutina
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

                if (series <= 0 || repeticiones <= 0 || descanso <= 0 || orden <= 0) {
                    mostrarError("Todos los valores deben ser positivos");
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

    public String[] mostrarCrearRutinaCliente() {
        JPanel panel = new JPanel(new GridLayout(2, 2, 10, 10));

        JTextField txtNombre = new JTextField();
        JTextField txtDescripcion = new JTextField();

        panel.add(new JLabel("Nombre rutina:"));
        panel.add(txtNombre);
        panel.add(new JLabel("Descripción:"));
        panel.add(txtDescripcion);

        int result = JOptionPane.showConfirmDialog(
                null, panel, "Crear Rutina para Cliente",
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

    public void mostrarMensaje(String mensaje) {
        JOptionPane.showMessageDialog(null, mensaje, "Olympus - Entrenador", JOptionPane.INFORMATION_MESSAGE);
    }

    public void mostrarError(String mensaje) {
        JOptionPane.showMessageDialog(null, mensaje, "Error", JOptionPane.ERROR_MESSAGE);
    }
}