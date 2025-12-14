package gym.Vista;

import javax.swing.*;
import java.awt.Dimension;

public class VistaRutina {
    
    public void mostrarDetallesRutina(String detalles) {
        JTextArea area = new JTextArea(detalles);
        area.setEditable(false);
        JScrollPane scroll = new JScrollPane(area);
        scroll.setPreferredSize(new Dimension(400, 300));
        
        JOptionPane.showMessageDialog(null, scroll, "Detalles de Rutina", JOptionPane.INFORMATION_MESSAGE);
    }
    
    public void mostrarEjerciciosRutina(String ejercicios) {
        JTextArea area = new JTextArea(ejercicios);
        area.setEditable(false);
        JScrollPane scroll = new JScrollPane(area);
        scroll.setPreferredSize(new Dimension(500, 400));
        
        JOptionPane.showMessageDialog(null, scroll, "Ejercicios de Rutina", JOptionPane.INFORMATION_MESSAGE);
    }
    
    public void mostrarMensaje(String mensaje) {
        JOptionPane.showMessageDialog(null, mensaje, "Olympus - Rutina", JOptionPane.INFORMATION_MESSAGE);
    }
    
    public void mostrarError(String mensaje) {
        JOptionPane.showMessageDialog(null, mensaje, "Error", JOptionPane.ERROR_MESSAGE);
    }
}