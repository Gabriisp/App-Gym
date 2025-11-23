package gym_Main;

import gm_controlador.LoginControlador;
import gm_modelo.Getrom0;
import java.sql.*;
import javax.swing.*;
import java.sql.SQLException;

// Punto de entrada principal de la aplicación
public class Main {
    public static void main(String[] args) {
        try {
            // Establecer look and feel
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            
            // Establecer conexión a BD
            Getrom0.getConnection();
            
            // Iniciar controlador de login
            LoginControlador loginControlador = new LoginControlador();
            loginControlador.iniciar();
        } 
        catch (SQLException e) {
            JOptionPane.showMessageDialog(null,
                "Error de conexión a BD: " + e.getMessage() +
                "\nAsegúrate de que MySQL esté ejecutándose",
                "Error", JOptionPane.ERROR_MESSAGE);
        }
        catch (Exception e) {
            JOptionPane.showMessageDialog(null,
                "Error: " + e.getMessage(),
                "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}