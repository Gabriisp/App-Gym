package gym.Main;

import gym.Controlador.LoginControlador;
import gym.Modelo.GestorBD;
import javax.swing.*;
import java.sql.SQLException;

// Punto de entrada principal de la aplicación
public class Main {
    public static void main(String[] args) throws UnsupportedLookAndFeelException {
        try {
            UIManager.setLookAndFeel(UIManager.getLookAndFeel());
            
            GestorBD.getConnection();
            
            LoginControlador loginControlador = new LoginControlador();
            loginControlador.iniciar();
            
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, 
                "Error de conexión a BD: " + e.getMessage() + 
                "\nAsegúrate de que MySQL esté ejecutándose", 
                "Error", JOptionPane.ERROR_MESSAGE);
        } 
    }
}
