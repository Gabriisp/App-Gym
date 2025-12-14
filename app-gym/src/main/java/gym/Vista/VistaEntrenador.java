package gym.Vista;

import java.awt.*;
import java.util.List;
import javax.swing.*;
import gym.Modelo.Entidades;

public class VistaEntrenador {
    // CORRECCIÓN: Eliminada la dependencia directa a GestorBD.
    
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
                "Eliminar rutina de cliente",
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
    
    // El controlador ahora pasa el nombre del creador (ya no se llama a GestorBD aquí)
    public void mostrarRutinas(List<Entidades.Rutina> rutinas, List<String> nombresClientes) {
        if (rutinas.isEmpty()) {
            mostrarMensaje("No hay rutinas creadas.");
            return;
        }

        StringBuilder sb = new StringBuilder("RUTINAS DE TUS CLIENTES:\n\n");
        for (int i = 0; i < rutinas.size(); i++) {
            Entidades.Rutina r = rutinas.get(i);
            String nombreCliente = (i < nombresClientes.size()) ? nombresClientes.get(i) : "Cliente Desconocido";

            sb.append("• ").append(r.getNombre())
                    .append("\n  Descripción: ").append(r.getDescripcion())
                    .append("\n  Cliente: ").append(nombreCliente)
                    .append("\n  Creada: ").append(r.getFechaCreacion().toLocalDate())
                    .append("\n  Activa: ").append(r.isActiva() ? "Sí" : "No")
                    .append("\n\n");
        }

        JTextArea area = new JTextArea(sb.toString());
        area.setEditable(false);
        JScrollPane scroll = new JScrollPane(area);
        scroll.setPreferredSize(new Dimension(500, 400));

        JOptionPane.showMessageDialog(null, scroll, "Rutinas de Clientes", JOptionPane.INFORMATION_MESSAGE);
    }
    
    public int mostrarSeleccionRutinaConOpciones(List<Entidades.Rutina> rutinas) {
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
                "Selecciona una rutina para ver sus ejercicios:",
                "Rutinas del Cliente - Ver Ejercicios",
                JOptionPane.DEFAULT_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                null,
                opciones,
                opciones[0]);
    }

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
    
    public boolean confirmarEliminacion(String mensaje) {
        int respuesta = JOptionPane.showConfirmDialog(null, mensaje, "Confirmar Eliminación",
                JOptionPane.YES_NO_OPTION);
        return respuesta == JOptionPane.YES_OPTION;
    }
    
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

    // El controlador ahora pasa los datos ya procesados para mostrarlos
    public void mostrarProgresoCompletoCliente(String resumenGeneral, String progresoEjercicio) {
        JDialog dialog = new JDialog();
        dialog.setTitle("Progreso Completo de Cliente");
        dialog.setModal(true);
        dialog.setLayout(new BorderLayout());
        dialog.setPreferredSize(new Dimension(900, 700));

        JTabbedPane tabbedPane = new JTabbedPane();
        
        JTextArea areaResumen = new JTextArea(resumenGeneral);
        areaResumen.setEditable(false);
        areaResumen.setFont(new Font("Monospaced", Font.PLAIN, 12));
        tabbedPane.addTab("Historial/Estadísticas", new JScrollPane(areaResumen));

        JTextArea areaProgreso = new JTextArea(progresoEjercicio);
        areaProgreso.setEditable(false);
        areaProgreso.setFont(new Font("Monospaced", Font.PLAIN, 12));
        tabbedPane.addTab("Progreso por Ejercicio", new JScrollPane(areaProgreso));
        
        dialog.add(tabbedPane, BorderLayout.CENTER);
        dialog.pack();
        dialog.setLocationRelativeTo(null);
        dialog.setVisible(true);
    }
    
    public void mostrarResumenGeneral(String resumen) {
        JTextArea area = new JTextArea(resumen);
        area.setEditable(false);
        area.setFont(new Font("Monospaced", Font.PLAIN, 12));
        JScrollPane scroll = new JScrollPane(area);
        scroll.setPreferredSize(new Dimension(700, 500));

        JOptionPane.showMessageDialog(null, scroll, "Resumen General de Clientes", JOptionPane.INFORMATION_MESSAGE);
    }
    
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

                if (series <= 0 || repeticiones <= 0 || descanso < 0 || orden <= 0) {
                    mostrarError("Series, repeticiones y orden deben ser positivos. El descanso debe ser >= 0.");
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