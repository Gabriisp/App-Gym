package gym.Controlador;

import gym.Modelo.GestorBD;
import gym.Modelo.Entidades;
import gym.Vista.VistaRutina;
import java.sql.SQLException;
import java.util.List;

// Controlador para gestionar la lógica relacionada con las rutinas
public class RutinaControlador {
    private VistaRutina vistaRutina;
    // Constructor
    public RutinaControlador() {
        this.vistaRutina = new VistaRutina();
    }
    // Mostrar detalles de una rutina específica
    public void mostrarDetallesRutina(int idRutina) {
        try {
            Entidades.Rutina rutina = GestorBD.obtenerRutinaPorId(idRutina);
            if (rutina != null) {
                String nombreCreador = obtenerNombreUsuario(rutina.getIdUsuarioCreador());
                
                String detalles = "Rutina: " + rutina.getNombre() + "\n" +
                        "Descripción: " + rutina.getDescripcion() + "\n" +
                        "Creada por: " + nombreCreador + "\n" +
                        "Creada: " + rutina.getFechaCreacion().toLocalDate() + "\n" +
                        "Activa: " + (rutina.isActiva() ? "Sí" : "No");
                vistaRutina.mostrarDetallesRutina(detalles);
            } else {
                vistaRutina.mostrarMensaje("Rutina no encontrada");
            }
        } catch (SQLException e) {
            vistaRutina.mostrarMensaje("Error: " + e.getMessage());
        }
    }
    // Mostrar los ejercicios asociados a una rutina
    public void mostrarEjerciciosRutina(int idRutina) {
        try {
            List<Entidades.EjercicioRutina> listaEjercicios = GestorBD.obtenerEjerciciosDeRutina(idRutina);

            if (listaEjercicios.isEmpty()) {
                vistaRutina.mostrarEjerciciosRutina("Esta rutina aún no tiene ejercicios asignados.");
                return;
            }

            StringBuilder sb = new StringBuilder();
            sb.append("Ejercicios de la rutina:\n\n");

            for (Entidades.EjercicioRutina ej : listaEjercicios) {
                sb.append("- ").append(ej.getOrden()).append(". ").append(ej.getEjercicioNombre())
                        .append(" (").append(ej.getGrupoMuscular()).append(")\n")
                        .append("   Plan: ").append(ej.getSeriesPlanificadas()).append(" series x ")
                        .append(ej.getRepeticionesPlanificadas()).append(" reps")
                        .append(" @ ").append(ej.getPesoRecomendado()).append("kg\n")
                        .append("   Descanso: ").append(ej.getDescansoSegundos()).append("s\n\n");
            }

            vistaRutina.mostrarEjerciciosRutina(sb.toString());

        } catch (SQLException e) {
            vistaRutina.mostrarMensaje("Error al cargar ejercicios: " + e.getMessage());
        }
    }
    // Obtener el nombre del usuario por su ID
    private String obtenerNombreUsuario(int idUsuario) {
        try {
            List<Entidades.Usuario> usuarios = GestorBD.obtenerTodosUsuarios();
            for (Entidades.Usuario usuario : usuarios) {
                if (usuario.getIdUsuario() == idUsuario) {
                    return usuario.getNombre();
                }
            }
            return "Usuario " + idUsuario;
        } catch (SQLException e) {
            return "Usuario " + idUsuario;
        }
    }
}