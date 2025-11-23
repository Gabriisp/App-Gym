package gym.Vista;

import java.awt.GridLayout;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

public class VistaLogin {
    
    public int mostrarOpcionInicial() {
        Object[] opciones = {"Registrarse", "Iniciar Sesión"};
        return JOptionPane.showOptionDialog(
            null,
            "¡Bienvenido a Olympus!\n¿Qué deseas hacer?",
            "Inicio",
            JOptionPane.DEFAULT_OPTION,
            JOptionPane.QUESTION_MESSAGE,
            null,
            opciones,
            opciones[0]
        );
    }
    
    public String mostrarSeleccionTipo() {
        Object[] opciones = {"Usuario Normal", "Entrenador"};
        int seleccion = JOptionPane.showOptionDialog(
            null,
            "Selecciona tu tipo de cuenta:",
            "Tipo de Usuario",
            JOptionPane.DEFAULT_OPTION,
            JOptionPane.QUESTION_MESSAGE,
            null,
            opciones,
            opciones[0]
        );
        
        if (seleccion == JOptionPane.CLOSED_OPTION || seleccion == -1) {
            return null;
        }
        
        return seleccion == 0 ? "usuario" : "entrenador";
    }
    
    public String[] mostrarRegistro(String tipo) {
        JPanel panel = new JPanel(new GridLayout(4, 2, 10, 10));
        
        JTextField txtNombre = new JTextField();
        JTextField txtEmail = new JTextField();
        JPasswordField txtPassword = new JPasswordField();
        JPasswordField txtConfirmar = new JPasswordField();
        
        panel.add(new JLabel("Nombre:"));
        panel.add(txtNombre);
        panel.add(new JLabel("Email:"));
        panel.add(txtEmail);
        panel.add(new JLabel("Contraseña:"));
        panel.add(txtPassword);
        panel.add(new JLabel("Confirmar:"));
        panel.add(txtConfirmar);
        
        int result = JOptionPane.showConfirmDialog(
            null, panel, "Registro - " + tipo, 
            JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE
        );
        
        if (result == JOptionPane.OK_OPTION) {
            String nombre = txtNombre.getText().trim();
            String email = txtEmail.getText().trim();
            String password = new String(txtPassword.getPassword());
            String confirmar = new String(txtConfirmar.getPassword());
            
            if (nombre.isEmpty() || email.isEmpty() || password.isEmpty()) {
                mostrarError("Todos los campos son obligatorios");
                return null;
            }
            
            if (!password.equals(confirmar)) {
                mostrarError("Las contraseñas no coinciden");
                return null;
            }
            
            return new String[]{nombre, email, password, tipo};
        }
        return null;
    }
    
    public String[] mostrarLogin() {
        JPanel panel = new JPanel(new GridLayout(2, 2, 10, 10));
        
        JTextField txtEmail = new JTextField();
        JPasswordField txtPassword = new JPasswordField();
        
        panel.add(new JLabel("Email:"));
        panel.add(txtEmail);
        panel.add(new JLabel("Contraseña:"));
        panel.add(txtPassword);
        
        int result = JOptionPane.showConfirmDialog(
            null, panel, "Iniciar Sesión", 
            JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE
        );
        
        if (result == JOptionPane.OK_OPTION) {
            String email = txtEmail.getText().trim();
            String password = new String(txtPassword.getPassword());
            
            if (email.isEmpty() || password.isEmpty()) {
                mostrarError("Complete todos los campos");
                return null;
            }
            
            return new String[]{email, password};
        }
        return null;
    }
    
    public void mostrarMensaje(String mensaje) {
        JOptionPane.showMessageDialog(null, mensaje, "Olympus", JOptionPane.INFORMATION_MESSAGE);
    }
    
    public void mostrarError(String mensaje) {
        JOptionPane.showMessageDialog(null, mensaje, "Error", JOptionPane.ERROR_MESSAGE);
    }
}