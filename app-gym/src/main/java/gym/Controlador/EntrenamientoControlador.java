package gym.Controlador;

import gym.Modelo.GestorBD;
import gym.Modelo.Entidades;
import gym.Vista.VistaEntrenamiento;
import java.sql.SQLException;
import java.util.List;
 // Controlador para gestionar la lógica relacionada con los entrenamientos
public class EntrenamientoControlador {

    private VistaEntrenamiento vistaEntrenamiento;

    public EntrenamientoControlador() {
        this.vistaEntrenamiento = new VistaEntrenamiento();
    }
    // Mostrar detalles de un entrenamiento específico
    public void mostrarDetallesEntrenamiento(int idEntrenamiento, int idUsuarioActual) {
        try {
            List<Entidades.Entrenamiento> entrenamientos = GestorBD.obtenerEntrenamientosPorUsuario(idUsuarioActual);
            
            for (Entidades.Entrenamiento entrenamiento : entrenamientos) {
                if (entrenamiento.getIdEntrenamiento() == idEntrenamiento) {
                    String detalles = "Entrenamiento #" + entrenamiento.getIdEntrenamiento() + "\n" +
                                      "Fecha: " + entrenamiento.getFechaEntrenamiento().toLocalDate() + "\n" +
                                      "Duración: " + String.format("%.2f", entrenamiento.getDuracionHoras()) + " horas\n" +
                                      "Notas: " + (entrenamiento.getNotas() != null ? entrenamiento.getNotas() : "Ninguna") + "\n" +
                                      "Completado: " + (entrenamiento.isCompletado() ? "Sí" : "No");
                    
                    vistaEntrenamiento.mostrarDetallesEntrenamiento(detalles);
                    return;
                }
            }
            vistaEntrenamiento.mostrarMensaje("Entrenamiento no encontrado");
            
        } catch (SQLException e) {
            vistaEntrenamiento.mostrarError("Error: " + e.getMessage());
        }
    }
}