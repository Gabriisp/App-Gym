package gym.Controlador;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import javax.swing.JOptionPane;
import gym.Modelo.Entidades;
import gym.Modelo.GestorBD;
import gym.Vista.VistaUsuario;

// Controlador para gestionar la lógica relacionada con los usuarios
public class UsuarioControlador {
    private Entidades.Usuario usuario;
    private VistaUsuario vistaUsuario;
    // Constructor
    public UsuarioControlador(Entidades.Usuario usuario) {
        this.usuario = usuario;
        this.vistaUsuario = new VistaUsuario();
    }
    // Iniciar la interfaz principal del usuario
    public void iniciar() {
        boolean salir = false;
        while (!salir) {
            int opcion = vistaUsuario.mostrarMenuUsuario(usuario.getNombre());

            switch (opcion) {
                case 0:
                    verRutinas();
                    break;
                case 1:
                    verEjerciciosRutina();
                    break;
                case 2:
                    crearRutina();
                    break;
                case 3:
                    crearNuevoEjercicioGeneral();
                    break;
                case 4:
                    registrarEntrenamiento();
                    break;
                case 5:
                    verProgresoCompleto();
                    break;
                case 6:
                    eliminarRutinaCompleta();
                    break;
                case 7: // NUEVA OPCIÓN: Solicitar entrenador
                    solicitarEntrenador();
                    break;
                case 8:
                case -1:
                    salir = true;
                    vistaUsuario.mostrarMensaje("¡Hasta pronto " + usuario.getNombre() + "!");
                    break;
                default:
                    vistaUsuario.mostrarError("Opción no válida");
            }
        }
    }

    // Solicitar entrenador
    private void solicitarEntrenador() {
        try {
            List<Entidades.Usuario> entrenadores = GestorBD.obtenerTodosEntrenadores();
            
            if (entrenadores.isEmpty()) {
                vistaUsuario.mostrarMensaje("No hay entrenadores disponibles en el sistema.");
                return;
            }

            // Mostrar selección de entrenador
            int entrenadorSeleccionado = vistaUsuario.mostrarSeleccionEntrenador(entrenadores);
            
            if (entrenadorSeleccionado >= 0 && entrenadorSeleccionado < entrenadores.size()) {
                Entidades.Usuario entrenador = entrenadores.get(entrenadorSeleccionado);
                
                // Verificar si ya existe una solicitud pendiente
                if (GestorBD.existeSolicitudPendiente(usuario.getIdUsuario(), entrenador.getIdUsuario())) {
                    vistaUsuario.mostrarMensaje("Ya tienes una solicitud pendiente con este entrenador.");
                    return;
                }
                
                // Pedir mensaje personalizado
                String mensaje = vistaUsuario.mostrarMensajeSolicitud();
                
                if (mensaje != null) {
                    if (GestorBD.enviarSolicitudEntrenador(usuario.getIdUsuario(), entrenador.getIdUsuario(), mensaje)) {
                        vistaUsuario.mostrarMensaje("¡Solicitud enviada al entrenador " + entrenador.getNombre() + "!");
                    } else {
                        vistaUsuario.mostrarError("Error al enviar la solicitud.");
                    }
                }
            }
        } catch (SQLException e) {
            vistaUsuario.mostrarError("Error: " + e.getMessage());
        }
    }

    // Ver las rutinas del usuario
    private void verRutinas() {
        try {
            List<Entidades.Rutina> rutinas = GestorBD.obtenerRutinasPorUsuario(usuario.getIdUsuario());
            vistaUsuario.mostrarRutinas(rutinas);
        } catch (SQLException e) {
            vistaUsuario.mostrarError("Error al cargar rutinas: " + e.getMessage());
        }
    }
    // Crear una nueva rutina
    private void crearRutina() {
        try {
            String[] datos = vistaUsuario.mostrarCrearRutina();
            if (datos != null && !datos[0].trim().isEmpty()) {
                Entidades.Rutina nuevaRutina = new Entidades.Rutina(datos[0].trim(), datos[1],
                        usuario.getIdUsuario());
                if (GestorBD.crearRutina(nuevaRutina)) {
                    vistaUsuario.mostrarMensaje("¡Rutina '" + datos[0] + "' creada exitosamente!");
                } else {
                    vistaUsuario.mostrarError("Error al crear la rutina");
                }
            } else {
                vistaUsuario.mostrarError("El nombre de la rutina no puede estar vacío");
            }
        } catch (SQLException e) {
            vistaUsuario.mostrarError("Error: " + e.getMessage());
        }
    }
    // Ver y gestionar los ejercicios de una rutina
    private void verEjerciciosRutina() {
        try {
            List<Entidades.Rutina> rutinas = GestorBD.obtenerRutinasPorUsuario(usuario.getIdUsuario());
            if (rutinas.isEmpty()) {
                vistaUsuario.mostrarMensaje("No tienes rutinas creadas.");
                return;
            }

            int rutinaSeleccionada = vistaUsuario.mostrarSeleccionRutina(rutinas);

            if (rutinaSeleccionada >= 0 && rutinaSeleccionada < rutinas.size()) {
                Entidades.Rutina rutina = rutinas.get(rutinaSeleccionada);

                boolean volver = false;
                while (!volver) {
                    int opcionEjercicios = vistaUsuario.mostrarMenuEjerciciosRutina(rutina.getNombre());

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
                            volver = true;
                            break;
                        case -1:
                            volver = true;
                            break;
                        default:
                            vistaUsuario.mostrarError("Opción no válida");
                            break;
                    }
                }
            }
        } catch (SQLException e) {
            vistaUsuario.mostrarError("Error: " + e.getMessage());
        }
    }
    // Mostrar los ejercicios de una rutina específica
    private void mostrarEjerciciosRutina(Entidades.Rutina rutina) {
        try {
            RutinaControlador rutinaCtrl = new RutinaControlador();
            rutinaCtrl.mostrarEjerciciosRutina(rutina.getIdRutina());

        } catch (Exception e) {
            vistaUsuario.mostrarError("Error: " + e.getMessage());
        }
    }
    // Agregar ejercicios a una rutina
    private void agregarEjerciciosARutina(Entidades.Rutina rutina) {
        try {
            List<Entidades.EjercicioRutina> ejerciciosActuales = GestorBD
                    .obtenerEjerciciosDeRutina(rutina.getIdRutina());
            vistaUsuario.mostrarEjerciciosRutina(ejerciciosActuales);

            List<Entidades.Ejercicio> ejerciciosDisponibles = GestorBD.obtenerTodosEjercicios();
            boolean continuar = true;

            while (continuar) {
                String[] datosEjercicio = vistaUsuario.mostrarAgregarEjercicioRutina(ejerciciosDisponibles);
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
                            vistaUsuario.mostrarMensaje(
                                    "¡Ejercicio '" + ejercicioSeleccionado.getNombre() + "' agregado a la rutina!");
                        } else {
                            vistaUsuario.mostrarError("Error al agregar el ejercicio a la rutina");
                        }
                    }

                    int respuesta = JOptionPane.showConfirmDialog(null,
                            "¿Quieres agregar otro ejercicio a esta rutina?", "Agregar más ejercicios",
                            JOptionPane.YES_NO_OPTION);
                    continuar = (respuesta == JOptionPane.YES_OPTION);
                } else {
                    continuar = false;
                }
            }
        } catch (SQLException e) {
            vistaUsuario.mostrarError("Error: " + e.getMessage());
        } catch (NumberFormatException e) {
            vistaUsuario.mostrarError("Error en el formato de los datos");
        }
    }
    // Eliminar un ejercicio de una rutina
    private void eliminarEjercicioDeRutina(Entidades.Rutina rutina) {
        try {
            List<Entidades.EjercicioRutina> ejercicios = GestorBD.obtenerEjerciciosDeRutina(rutina.getIdRutina());

            if (ejercicios.isEmpty()) {
                vistaUsuario.mostrarMensaje("Esta rutina ya está vacía.");
                return;
            }

            int ejercicioIndex = vistaUsuario.mostrarSeleccionEjercicioParaBorrar(ejercicios);
            if (ejercicioIndex >= 0) {
                Entidades.EjercicioRutina aBorrar = ejercicios.get(ejercicioIndex);
                if (GestorBD.eliminarEjercicioDeRutina(aBorrar.getIdEjercicioRutina())) {
                    vistaUsuario.mostrarMensaje("Ejercicio eliminado correctamente.");
                } else {
                    vistaUsuario.mostrarError("No se pudo eliminar.");
                }
            }
        } catch (SQLException e) {
            vistaUsuario.mostrarError("Error: " + e.getMessage());
        }
    }
    // Crear un nuevo ejercicio en la base de datos global
    private void crearNuevoEjercicioGeneral() {
        try {
            String[] datos = vistaUsuario.mostrarCrearEjercicioNuevo();
            if (datos != null && !datos[0].trim().isEmpty()) {
                Entidades.Ejercicio nuevo = new Entidades.Ejercicio();
                nuevo.setNombre(datos[0].trim());
                nuevo.setGrupoMuscular(datos[1]);
                nuevo.setEquipamientoNecesario(datos[2]);
                nuevo.setDescripcion(datos[3]);

                if (GestorBD.crearEjercicioGeneral(nuevo)) {
                    vistaUsuario.mostrarMensaje("¡Ejercicio '" + datos[0] + "' añadido a la base de datos global!");
                } else {
                    vistaUsuario.mostrarError("Error al guardar el ejercicio.");
                }
            } else {
                vistaUsuario.mostrarError("El nombre del ejercicio no puede estar vacío");
            }
        } catch (SQLException e) {
            vistaUsuario.mostrarError("Error BD: " + e.getMessage());
        }
    }
    // Eliminar una rutina completa
    private void eliminarRutinaCompleta() {
        try {
            List<Entidades.Rutina> rutinas = GestorBD.obtenerRutinasPorUsuario(usuario.getIdUsuario());
            if (rutinas.isEmpty()) {
                vistaUsuario.mostrarMensaje("No tienes rutinas para eliminar.");
                return;
            }

            int index = vistaUsuario.mostrarSeleccionRutina(rutinas);
            if (index >= 0) {
                Entidades.Rutina rutina = rutinas.get(index);
                if (vistaUsuario.confirmarAccion(
                        "¿Seguro que quieres eliminar la rutina '" + rutina.getNombre()
                                + "'?\nSe perderán todos los ejercicios configurados en ella.")) {

                    if (GestorBD.eliminarRutina(rutina.getIdRutina())) {
                        vistaUsuario.mostrarMensaje("Rutina eliminada correctamente.");
                    } else {
                        vistaUsuario.mostrarError("No se pudo eliminar la rutina.");
                    }
                }
            }
        } catch (SQLException e) {
            vistaUsuario.mostrarError("Error al eliminar: " + e.getMessage());
        }
    }
    // Registrar un nuevo entrenamiento
    private void registrarEntrenamiento() {
        try {
            List<Entidades.Rutina> rutinas = GestorBD.obtenerRutinasPorUsuario(usuario.getIdUsuario());

            int rutinaSeleccionada = vistaUsuario.mostrarSeleccionRutinaParaEntrenamiento(rutinas);

            if (rutinaSeleccionada >= 0 && rutinaSeleccionada < rutinas.size()) {
                Entidades.Rutina rutina = rutinas.get(rutinaSeleccionada);
                List<Entidades.EjercicioRutina> ejercicios = GestorBD.obtenerEjerciciosDeRutina(rutina.getIdRutina());

                if (ejercicios.isEmpty()) {
                    vistaUsuario.mostrarMensaje("Esta rutina no tiene ejercicios. Agrega ejercicios primero.");
                    return;
                }

                vistaUsuario.mostrarMensaje("Vas a entrenar: " + rutina.getNombre() + "\n\nEjercicios:\n" +
                        obtenerListaEjercicios(ejercicios));

                String[] datosEntrenamiento = vistaUsuario.mostrarRegistroEntrenamiento();

                if (datosEntrenamiento != null) {
                    Entidades.Entrenamiento entrenamiento = new Entidades.Entrenamiento();
                    entrenamiento.setIdUsuario(usuario.getIdUsuario());
                    entrenamiento.setIdRutina(rutina.getIdRutina());
                    entrenamiento.setDuracionHoras(Double.parseDouble(datosEntrenamiento[0]));
                    entrenamiento.setNotas(datosEntrenamiento[1]);

                    if (GestorBD.registrarEntrenamiento(entrenamiento)) {
                        vistaUsuario.mostrarMensaje("¡Entrenamiento registrado exitosamente!\n\n" +
                                "Rutina: " + rutina.getNombre() + "\n" +
                                "Duración: " + datosEntrenamiento[0] + " horas\n" +
                                "Ejercicios: " + ejercicios.size());

                        boolean registrarEjercicios = vistaUsuario.confirmarAccion(
                                "¿Quieres registrar los detalles de cada ejercicio realizado?");

                        if (registrarEjercicios) {
                            registrarEjecucionEjercicios(entrenamiento.getIdEntrenamiento(), ejercicios);
                        }
                    } else {
                        vistaUsuario.mostrarError("Error al registrar el entrenamiento");
                    }
                }
            }
        } catch (SQLException e) {
            vistaUsuario.mostrarError("Error: " + e.getMessage());
        } catch (NumberFormatException e) {
            vistaUsuario.mostrarError("Error en el formato de los datos");
        }
    }
    // Registrar la ejecución de los ejercicios durante un entrenamiento
    private void registrarEjecucionEjercicios(int idEntrenamiento, List<Entidades.EjercicioRutina> ejercicios) {
        int ejerciciosRegistrados = 0;

        for (Entidades.EjercicioRutina ejercicio : ejercicios) {
            String[] datosEjercicio = vistaUsuario.mostrarRegistroEjercicio(ejercicio);
            if (datosEjercicio != null) {
                try {
                    Entidades.Ejecuta ejecuta = new Entidades.Ejecuta();
                    ejecuta.setIdEntrenamiento(idEntrenamiento);
                    ejecuta.setIdEjercicioRutina(ejercicio.getIdEjercicioRutina());
                    ejecuta.setSeriesReales(Integer.parseInt(datosEjercicio[0]));
                    ejecuta.setRepeticionesReales(Integer.parseInt(datosEjercicio[1]));
                    ejecuta.setPesoReal(Double.parseDouble(datosEjercicio[2]));
                    ejecuta.setRpe(Integer.parseInt(datosEjercicio[3]));
                    ejecuta.setNotas("Ejecutado en entrenamiento del " + LocalDate.now());

                    if (GestorBD.registrarEjecucion(ejecuta)) {
                        ejerciciosRegistrados++;
                    }
                } catch (Exception e) {
                    vistaUsuario.mostrarError("Error al registrar ejercicio: " + ejercicio.getEjercicioNombre());
                }
            }
        }

        if (ejerciciosRegistrados > 0) {
            vistaUsuario.mostrarMensaje("Se registraron " + ejerciciosRegistrados + " ejercicios correctamente.");
        }
    }
    // Obtener una lista formateada de ejercicios
    private String obtenerListaEjercicios(List<Entidades.EjercicioRutina> ejercicios) {
        StringBuilder sb = new StringBuilder();
        for (Entidades.EjercicioRutina ejercicio : ejercicios) {
            sb.append("• ").append(ejercicio.getEjercicioNombre())
                    .append(" - ").append(ejercicio.getSeriesPlanificadas())
                    .append("x").append(ejercicio.getRepeticionesPlanificadas())
                    .append(" @ ").append(ejercicio.getPesoRecomendado()).append("kg\n");
        }
        return sb.toString();
    }
    // Ver el progreso completo del usuario
    private void verProgresoCompleto() {
        try {
            List<Entidades.Entrenamiento> entrenamientos = GestorBD
                    .obtenerEntrenamientosPorUsuario(usuario.getIdUsuario());

            if (entrenamientos.isEmpty()) {
                vistaUsuario.mostrarMensaje("No hay entrenamientos registrados.");
                return;
            }

            List<Entidades.Ejercicio> misEjercicios = GestorBD.obtenerEjerciciosDeMisRutinas(usuario.getIdUsuario());

            vistaUsuario.mostrarProgresoCompleto(entrenamientos, misEjercicios, usuario.getIdUsuario());

        } catch (SQLException e) {
            vistaUsuario.mostrarError("Error al cargar el progreso: " + e.getMessage());
        }
    }
}