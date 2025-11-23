package gym.Vista;

import javax.swing.*;

public class VistaRutina {
    
    // Muestra la ventana con la descripción y datos generales de la rutina
    public void mostrarDetallesRutina(String detalles) {
        JTextArea area = new JTextArea(detalles);
        area.setEditable(false);
        JScrollPane scroll = new JScrollPane(area);
        scroll.setPreferredSize(new java.awt.Dimension(400, 300));
        
        JOptionPane.showMessageDialog(null, scroll, "Detalles de Rutina", JOptionPane.INFORMATION_MESSAGE);
    }
    
    // Muestra la lista de los ejercicios que componen una rutina específica
    public void mostrarEjerciciosRutina(String ejercicios) {
        JTextArea area = new JTextArea(ejercicios);
        area.setEditable(false);
        JScrollPane scroll = new JScrollPane(area);
        scroll.setPreferredSize(new java.awt.Dimension(500, 400));
        
        JOptionPane.showMessageDialog(null, scroll, "Ejercicios de Rutina", JOptionPane.INFORMATION_MESSAGE);
    }
    
    // Muestra los mensajes informativos al usuario
    public void mostrarMensaje(String mensaje) {
        JOptionPane.showMessageDialog(null, mensaje, "Olympus - Rutina", JOptionPane.INFORMATION_MESSAGE);
    }
    
    // Muestra los mensajes de error cuando algo falla
    public void mostrarError(String mensaje) {
        JOptionPane.showMessageDialog(null, mensaje, "Error", JOptionPane.ERROR_MESSAGE);
    }
}