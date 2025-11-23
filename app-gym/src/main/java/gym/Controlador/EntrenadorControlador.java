package gym.Controlador;

import java.sql.SQLException;
import java.util.List;
import javax.swing.JOptionPane;
import gym.Modelo.GestorBD;
import gym.Modelo.Entidades;
import gym.Vista.VistaEntrenador;

// Controlador para las funcionalidades del entrenador
public class EntrenadorControlador {

    private Entidades.Usuario entrenador;
    private VistaEntrenador vistaEntrenador;

    // Constructor que recibe el usuario entrenador
    public EntrenadorControlador(Entidades.Usuario entrenador) {
        this.entrenador = entrenador;
        this.vistaEntrenador = new VistaEntrenador();
    }

    public void mostrarMenuEntrenador() {
        boolean salir = false;

        while (!salir) {
            int opcion = vistaEntrenador.mostrarMenuEntrenador(entrenador.getNombre());

            switch (opcion) {
                case 0:
                    verUsuariosAsignados();
                    break;
                case 1:
                    crearRutinaParaCliente();
                    break;
                case 2:
                    verEjerciciosRutina();
                    break;
                case 3:
                    eliminarRutina();
                    break;
                case 4:
                    verProgresoClientes();
                    break;
                case 5:
                    gestionarSolicitudes();
                    break;
                case 6:
                    verRutinasClienteEspecifico();
                    break;
                case 7:
                    crearEjercicioGeneral();
                    break;
                case 8:
                    salir = true;
                    break;
                case -1:
                    salir = true;
                    break;
                default:
                    vistaEntrenador.mostrarError("Opción no válida");
                    break;
            }
        }
    }

    // Gestion de solicitudes de usuarios
    private void gestionarSolicitudes() {
        try {
            List<Entidades.Asignacion> solicitudes = GestorBD.obtenerSolicitudesPendientes(entrenador.getIdUsuario());

            if (solicitudes.isEmpty()) {
                vistaEntrenador.mostrarMensaje("No tienes solicitudes pendientes.");
                return;
            }

            int solicitudSeleccionada = vistaEntrenador.mostrarSeleccionSolicitud(solicitudes);

            if (solicitudSeleccionada >= 0 && solicitudSeleccionada < solicitudes.size()) {
                Entidades.Asignacion solicitud = solicitudes.get(solicitudSeleccionada);

                int accion = vistaEntrenador.mostrarOpcionesSolicitud(solicitud);

                switch (accion) {
                    case 0:
                        if (GestorBD.actualizarEstadoSolicitud(solicitud.getIdUsuario(), entrenador.getIdUsuario(),
                                "activa")) {
                            vistaEntrenador.mostrarMensaje(
                                    "¡Solicitud aceptada! Ahora " + solicitud.getNombreUsuario() + " es tu cliente.");
                        } else {
                            vistaEntrenador.mostrarError("Error al aceptar la solicitud.");
                        }
                        break;

                    case 1:
                        if (GestorBD.actualizarEstadoSolicitud(solicitud.getIdUsuario(), entrenador.getIdUsuario(),
                                "rechazada")) {
                            vistaEntrenador.mostrarMensaje("Solicitud rechazada.");
                        } else {
                            vistaEntrenador.mostrarError("Error al rechazar la solicitud.");
                        }
                        break;

                    default:
                        break;
                }
            }
        } catch (SQLException e) {
            vistaEntrenador.mostrarError("Error: " + e.getMessage());
        }
    }

    // Crear un nuevo ejercicio en la base de datos global
    private void crearEjercicioGeneral() {
        try {
            String[] datos = vistaEntrenador.mostrarCrearEjercicioNuevo();
            if (datos != null && !datos[0].trim().isEmpty()) {
                Entidades.Ejercicio nuevo = new Entidades.Ejercicio();
                nuevo.setNombre(datos[0].trim());
                nuevo.setGrupoMuscular(datos[1]);
                nuevo.setEquipamientoNecesario(datos[2]);
                nuevo.setDescripcion(datos[3]);

                if (GestorBD.crearEjercicioGeneral(nuevo)) {
                    vistaEntrenador.mostrarMensaje("¡Ejercicio '" + datos[0] + "' añadido a la base de datos global!");
                } else {
                    vistaEntrenador.mostrarError("Error al guardar el ejercicio.");
                }
            } else {
                vistaEntrenador.mostrarError("El nombre del ejercicio no puede estar vacío");
            }
        } catch (SQLException e) {
            vistaEntrenador.mostrarError("Error BD: " + e.getMessage());
        }
    }

    // Ver rutinas de un cliente específico
    private void verRutinasClienteEspecifico() {
        try {
            List<Entidades.Usuario> usuarios = GestorBD.obtenerUsuariosPorEntrenador(entrenador.getIdUsuario());

            if (usuarios.isEmpty()) {
                vistaEntrenador.mostrarMensaje("No tienes usuarios asignados.");
                return;
            }

            int usuarioSeleccionado = vistaEntrenador.mostrarSeleccionUsuario(usuarios);

            if (usuarioSeleccionado >= 0 && usuarioSeleccionado < usuarios.size()) {
                Entidades.Usuario cliente = usuarios.get(usuarioSeleccionado);

                // Obtener rutinas del cliente seleccionado
                List<Entidades.Rutina> rutinasCliente = GestorBD.obtenerRutinasPorUsuario(cliente.getIdUsuario());

                if (rutinasCliente.isEmpty()) {
                    vistaEntrenador.mostrarMensaje("El cliente " + cliente.getNombre() + " no tiene rutinas creadas.");
                    return;
                }

                // Mostrar las rutinas del cliente
                StringBuilder sb = new StringBuilder();
                sb.append("RUTINAS DEL CLIENTE: ").append(cliente.getNombre()).append("\n\n");

                for (Entidades.Rutina rutina : rutinasCliente) {
                    sb.append("• ").append(rutina.getNombre())
                            .append(" - ").append(rutina.getDescripcion())
                            .append("\n   Creada: ").append(rutina.getFechaCreacion().toLocalDate())
                            .append(" | Activa: ").append(rutina.isActiva() ? "Sí" : "No")
                            .append("\n\n");
                }

                // Mostrar opciones para ver ejercicios de alguna rutina
                int opcionRutina = vistaEntrenador.mostrarSeleccionRutinaConOpciones(rutinasCliente);

                if (opcionRutina >= 0 && opcionRutina < rutinasCliente.size()) {
                    Entidades.Rutina rutinaSeleccionada = rutinasCliente.get(opcionRutina);

                    // Mostrar ejercicios de la rutina seleccionada
                    RutinaControlador rutinaCtrl = new RutinaControlador();
                    rutinaCtrl.mostrarEjerciciosRutina(rutinaSeleccionada.getIdRutina());
                }
            }
        } catch (SQLException e) {
            vistaEntrenador.mostrarError("Error al cargar rutinas del cliente: " + e.getMessage());
        }
    }

    // Ver la lista de usuarios asignados al entrenador
    private void verUsuariosAsignados() {
        try {
            List<Entidades.Usuario> usuarios = GestorBD.obtenerUsuariosPorEntrenador(entrenador.getIdUsuario());
            vistaEntrenador.mostrarUsuarios(usuarios);
        } catch (SQLException e) {
            vistaEntrenador.mostrarError("Error al cargar usuarios: " + e.getMessage());
        }
    }

    // Ver el progreso de los clientes asignados al entrenador
    private void verProgresoClientes() {
        try {
            List<Entidades.Usuario> usuarios = GestorBD.obtenerUsuariosPorEntrenador(entrenador.getIdUsuario());
            if (usuarios.isEmpty()) {
                vistaEntrenador.mostrarMensaje("No tienes usuarios asignados.");
                return;
            }

            int opcion = vistaEntrenador.mostrarSeleccionProgresoClientes();

            switch (opcion) {
                case 0:
                    verProgresoClienteEspecifico(usuarios);
                    break;
                case 1:
                    verResumenGeneralClientes(usuarios);
                    break;
                default:
                    break;
            }

        } catch (SQLException e) {
            vistaEntrenador.mostrarError("Error al cargar el progreso: " + e.getMessage());
        }
    }

    // Ver el progreso de un cliente específico
    private void verProgresoClienteEspecifico(List<Entidades.Usuario> usuarios) throws SQLException {
        int usuarioSeleccionado = vistaEntrenador.mostrarSeleccionUsuario(usuarios);

        if (usuarioSeleccionado >= 0 && usuarioSeleccionado < usuarios.size()) {
            Entidades.Usuario cliente = usuarios.get(usuarioSeleccionado);

            List<Entidades.Entrenamiento> entrenamientos = GestorBD
                    .obtenerEntrenamientosPorUsuario(cliente.getIdUsuario());
            List<Entidades.Ejercicio> ejerciciosCliente = GestorBD
                    .obtenerEjerciciosDeMisRutinas(cliente.getIdUsuario());

            vistaEntrenador.mostrarProgresoCompletoCliente(entrenamientos, ejerciciosCliente, cliente);
        }
    }

    // Ver un resumen general del progreso de todos los clientes
    private void verResumenGeneralClientes(List<Entidades.Usuario> usuarios) throws SQLException {
        StringBuilder resumen = new StringBuilder();
        resumen.append("RESUMEN GENERAL DE TODOS LOS CLIENTES\n");
        resumen.append("=".repeat(50)).append("\n\n");

        for (Entidades.Usuario cliente : usuarios) {
            if ("usuario".equals(cliente.getTipo())) {
                List<Entidades.Entrenamiento> entrenamientos = GestorBD
                        .obtenerEntrenamientosPorUsuario(cliente.getIdUsuario());
                List<Entidades.Ejercicio> ejercicios = GestorBD.obtenerEjerciciosDeMisRutinas(cliente.getIdUsuario());

                resumen.append("CLIENTE: ").append(cliente.getNombre()).append("\n");
                resumen.append("- Total entrenamientos: ").append(entrenamientos.size()).append("\n");

                if (!entrenamientos.isEmpty()) {
                    double totalHoras = entrenamientos.stream()
                            .mapToDouble(Entidades.Entrenamiento::getDuracionHoras).sum();
                    double promedioHoras = totalHoras / entrenamientos.size();
                    resumen.append("- Horas totales: ").append(String.format("%.1f", totalHoras)).append(" horas\n");
                    resumen.append("- Promedio por sesión: ").append(String.format("%.1f", promedioHoras))
                            .append(" horas\n");
                    resumen.append("- Último entrenamiento: ")
                            .append(entrenamientos.get(0).getFechaEntrenamiento().toLocalDate()).append("\n");
                }

                resumen.append("- Ejercicios diferentes: ").append(ejercicios.size()).append("\n");
                resumen.append("- Grupos musculares trabajados: ");
                ejercicios.stream()
                        .map(Entidades.Ejercicio::getGrupoMuscular)
                        .distinct()
                        .forEach(grupo -> resumen.append(grupo).append(" "));
                resumen.append("\n");
                resumen.append("-".repeat(40)).append("\n\n");
            }
        }

        vistaEntrenador.mostrarResumenGeneral(resumen.toString());
    }

    // Crear una nueva rutina para un cliente asignado
    private void crearRutinaParaCliente() {
        try {
            List<Entidades.Usuario> usuarios = GestorBD.obtenerUsuariosPorEntrenador(entrenador.getIdUsuario());

            if (usuarios.isEmpty()) {
                vistaEntrenador.mostrarMensaje("No tienes usuarios asignados para crear rutinas");
                return;
            }

            int usuarioSeleccionado = vistaEntrenador.mostrarSeleccionUsuario(usuarios);

            if (usuarioSeleccionado >= 0 && usuarioSeleccionado < usuarios.size()) {
                Entidades.Usuario usuarioCliente = usuarios.get(usuarioSeleccionado);
                String[] datosRutina = vistaEntrenador.mostrarCrearRutinaCliente();

                if (datosRutina != null && !datosRutina[0].trim().isEmpty()) {
                    Entidades.Rutina nuevaRutina = new Entidades.Rutina(
                            datosRutina[0].trim(),
                            datosRutina[1],
                            usuarioCliente.getIdUsuario());
                    nuevaRutina.setIdEntrenadorAsignador(entrenador.getIdUsuario());

                    if (GestorBD.crearRutina(nuevaRutina)) {
                        vistaEntrenador.mostrarMensaje("¡Rutina creada exitosamente!");
                    } else {
                        vistaEntrenador.mostrarError("Error al crear la rutina");
                    }
                } else {
                    vistaEntrenador.mostrarError("El nombre de la rutina no puede estar vacío");
                }
            }
        } catch (SQLException e) {
            vistaEntrenador.mostrarError("Error: " + e.getMessage());
        }
    }

    // Ver y gestionar los ejercicios de una rutina de un cliente
    private void verEjerciciosRutina() {
        try {
            List<Entidades.Usuario> usuarios = GestorBD.obtenerUsuariosPorEntrenador(entrenador.getIdUsuario());
            if (usuarios.isEmpty()) {
                vistaEntrenador.mostrarMensaje("No tienes usuarios asignados");
                return;
            }

            int usuarioSeleccionado = vistaEntrenador.mostrarSeleccionUsuario(usuarios);

            if (usuarioSeleccionado < 0 || usuarioSeleccionado >= usuarios.size()) {
                return;
            }

            Entidades.Usuario usuarioCliente = usuarios.get(usuarioSeleccionado);
            List<Entidades.Rutina> rutinas = GestorBD.obtenerRutinasPorUsuario(usuarioCliente.getIdUsuario());

            if (rutinas.isEmpty()) {
                vistaEntrenador.mostrarMensaje("El usuario no tiene rutinas");
                return;
            }

            int rutinaSeleccionada = vistaEntrenador.mostrarSeleccionRutina(rutinas);

            if (rutinaSeleccionada < 0 || rutinaSeleccionada >= rutinas.size()) {
                return;
            }

            Entidades.Rutina rutina = rutinas.get(rutinaSeleccionada);

            boolean volver = false;
            while (!volver) {
                int opcionEjercicios = vistaEntrenador.mostrarMenuEjerciciosRutina(rutina.getNombre());

                switch (opcionEjercicios) {
                    case 0:
                        mostrarEjerciciosRutina(rutina);
                        break;
                    case 1:
                        agregarEjerciciosARutina(rutina);
                        break;
                    case 2:
                        eliminarEjercicioDeRutina(rutina);
                        break;
                    case 3:
                    case -1:
                        volver = true;
                        break;
                    default:
                        vistaEntrenador.mostrarError("Opción no válida");
                        break;
                }
            }
        } catch (SQLException e) {
            vistaEntrenador.mostrarError("Error: " + e.getMessage());
        }
    }

    private void mostrarEjerciciosRutina(Entidades.Rutina rutina) {
        try {
            RutinaControlador rutinaCtrl = new RutinaControlador();
            rutinaCtrl.mostrarEjerciciosRutina(rutina.getIdRutina());

        } catch (Exception e) {
            vistaEntrenador.mostrarError("Error al cargar ejercicios: " + e.getMessage());
        }
    }

    // Agregar ejercicios a una rutina existente
    private void agregarEjerciciosARutina(Entidades.Rutina rutina) {
        try {
            RutinaControlador rutinaCtrl = new RutinaControlador();
            rutinaCtrl.mostrarEjerciciosRutina(rutina.getIdRutina());

            List<Entidades.Ejercicio> ejerciciosDisponibles = GestorBD.obtenerTodosEjercicios();
            boolean continuar = true;

            while (continuar) {
                String[] datosEjercicio = vistaEntrenador.mostrarAgregarEjercicioRutina(ejerciciosDisponibles);

                if (datosEjercicio != null) {
                    int ejercicioIndex = Integer.parseInt(datosEjercicio[0]);
                    if (ejercicioIndex >= 0 && ejercicioIndex < ejerciciosDisponibles.size()) {
                        Entidades.Ejercicio ejercicioSeleccionado = ejerciciosDisponibles.get(ejercicioIndex);

                        Entidades.EjercicioRutina ejercicioRutina = new Entidades.EjercicioRutina();
                        ejercicioRutina.setIdRutina(rutina.getIdRutina());
                        ejercicioRutina.setIdEjercicio(ejercicioSeleccionado.getIdEjercicio());
                        ejercicioRutina.setSeriesPlanificadas(Integer.parseInt(datosEjercicio[1]));
                        ejercicioRutina.setRepeticionesPlanificadas(Integer.parseInt(datosEjercicio[2]));
                        ejercicioRutina.setPesoRecomendado(Double.parseDouble(datosEjercicio[3]));
                        ejercicioRutina.setDescansoSegundos(Integer.parseInt(datosEjercicio[4]));
                        ejercicioRutina.setOrden(Integer.parseInt(datosEjercicio[5]));

                        if (GestorBD.agregarEjercicioARutina(ejercicioRutina)) {
                            vistaEntrenador.mostrarMensaje("¡Ejercicio agregado!");
                        } else {
                            vistaEntrenador.mostrarError("Error al agregar");
                        }
                    }
                    int respuesta = JOptionPane.showConfirmDialog(null, "¿Agregar otro?", "Continuar",
                            JOptionPane.YES_NO_OPTION);
                    continuar = (respuesta == JOptionPane.YES_OPTION);
                } else {
                    continuar = false;
                }
            }
        } catch (Exception e) {
            vistaEntrenador.mostrarError("Error: " + e.getMessage());
        }
    }

    // Eliminar un ejercicio de una rutina
    private void eliminarEjercicioDeRutina(Entidades.Rutina rutina) {
        try {
            List<Entidades.EjercicioRutina> ejercicios = GestorBD.obtenerEjerciciosDeRutina(rutina.getIdRutina());

            if (ejercicios.isEmpty()) {
                vistaEntrenador.mostrarMensaje("Esta rutina ya está vacía");
                return;
            }

            int ejercicioIndex = vistaEntrenador.mostrarSeleccionEjercicioParaBorrar(ejercicios);
            if (ejercicioIndex >= 0) {
                Entidades.EjercicioRutina aBorrar = ejercicios.get(ejercicioIndex);

                if (GestorBD.eliminarEjercicioDeRutina(aBorrar.getIdEjercicioRutina())) {
                    vistaEntrenador.mostrarMensaje("Ejercicio eliminado.");
                } else {
                    vistaEntrenador.mostrarError("Error al eliminar ejercicio.");
                }
            }
        } catch (SQLException e) {
            vistaEntrenador.mostrarError("Error: " + e.getMessage());
        }
    }

    // Eliminar una rutina asignada a un cliente
    private void eliminarRutina() {
        try {
            List<Entidades.Rutina> rutinas = GestorBD.obtenerRutinasDeMisClientes(entrenador.getIdUsuario());

            if (rutinas.isEmpty()) {
                vistaEntrenador.mostrarMensaje("No hay rutinas para eliminar.");
                return;
            }

            int index = vistaEntrenador.mostrarSeleccionRutina(rutinas);
            if (index >= 0) {
                Entidades.Rutina rutina = rutinas.get(index);

                if (vistaEntrenador.confirmarEliminacion("¿Eliminar rutina '" + rutina.getNombre() + "'?")) {
                    if (GestorBD.eliminarRutina(rutina.getIdRutina())) {
                        vistaEntrenador.mostrarMensaje("Rutina eliminada correctamente");
                    } else {
                        vistaEntrenador.mostrarError("No se pudo eliminar la rutina");
                    }
                }
            }
        } catch (SQLException e) {
            vistaEntrenador.mostrarError("Error: " + e.getMessage());
        }
    }
}