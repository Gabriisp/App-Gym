package gym.vista;

import java.awt.Dimension;

import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

public class VistaEntrenamiento {
    
    // Se muestra una ventana con los detalles de una sesión específica
    public void mostrarDetallesEntrenamiento(String detalles) {
        JTextArea area = new JTextArea(detalles); 
        area.setEditable(false); 
        JScrollPane scroll = new JScrollPane(area); 
        scroll.setPreferredSize(new Dimension(400, 300)); 
        
        JOptionPane.showMessageDialog(null, scroll, "Detalles de Entrenamiento", JOptionPane.INFORMATION_MESSAGE);
    }
    
    // Muestra las estadísticas acumuladas del usuario
    public void mostrarEstadisticas(String estadisticas) {
        JTextArea area = new JTextArea(estadisticas);
        area.setEditable(false);
        JScrollPane scroll = new JScrollPane(area);
        scroll.setPreferredSize(new Dimension(500, 400));
        
        JOptionPane.showMessageDialog(null, scroll, "Estadísticas de Entrenamiento", JOptionPane.INFORMATION_MESSAGE);
    }
    
    // Muestra un mensaje simple de información (Pop-up)
    public void mostrarMensaje(String mensaje) {
        JOptionPane.showMessageDialog(null, mensaje, "Olympus - Entrenamiento", JOptionPane.INFORMATION_MESSAGE);
    }
    
    // Muestra un mensaje de error crítico
    public void mostrarError(String mensaje) {
        JOptionPane.showMessageDialog(null, mensaje, "Error", JOptionPane.ERROR_MESSAGE);
    }
    
}