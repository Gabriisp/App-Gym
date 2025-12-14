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
            // Lógica de acceso a BD
            Entidades.Rutina rutina = GestorBD.obtenerRutinaPorId(idRutina);
            if (rutina != null) {
                // Lógica de negocio (obtener nombre del creador para la presentación)
                String nombreCreador = GestorBD.obtenerNombreUsuarioPorId(rutina.getIdUsuarioCreador());
                
                // Lógica de formateo
                String detalles = "Rutina: " + rutina.getNombre() + "\n" +
                                 "Descripción: " + rutina.getDescripcion() + "\n" +
                                 "Creada por: " + nombreCreador + "\n" +
                                 "Creada: " + rutina.getFechaCreacion().toLocalDate() + "\n" +
                                 "Activa: " + (rutina.isActiva() ? "Sí" : "No");
                // La vista solo muestra
                vistaRutina.mostrarDetallesRutina(detalles);
            } else {
                vistaRutina.mostrarMensaje("Rutina no encontrada");
            }
        } catch (SQLException e) {
            vistaRutina.mostrarMensaje("Error al cargar detalles de la rutina: " + e.getMessage());
        }
    }
    
    // Mostrar los ejercicios asociados a una rutina
    public void mostrarEjerciciosRutina(int idRutina) {
        try {
            // Lógica de acceso a BD
            List<Entidades.EjercicioRutina> listaEjercicios = GestorBD.obtenerEjerciciosDeRutina(idRutina);

            if (listaEjercicios.isEmpty()) {
                vistaRutina.mostrarEjerciciosRutina("Esta rutina aún no tiene ejercicios asignados.");
                return;
            }

            // Lógica de formateo en el Controlador
            StringBuilder sb = new StringBuilder();
            sb.append("Ejercicios de la rutina:\n\n");

            for (Entidades.EjercicioRutina ej : listaEjercicios) {
                sb.append("- ").append(ej.getOrden()).append(". ").append(ej.getEjercicioNombre())
                        .append(" (").append(ej.getGrupoMuscular()).append(")\n")
                        .append("    Plan: ").append(ej.getSeriesPlanificadas()).append(" series x ")
                        .append(ej.getRepeticionesPlanificadas()).append(" reps")
                        .append(" @ ").append(ej.getPesoRecomendado()).append("kg\n")
                        .append("    Descanso: ").append(ej.getDescansoSegundos()).append("s\n\n");
            }

            // La vista solo muestra
            vistaRutina.mostrarEjerciciosRutina(sb.toString());

        } catch (SQLException e) {
            vistaRutina.mostrarMensaje("Error al cargar ejercicios: " + e.getMessage());
        }
    }
}