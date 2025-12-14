package gym.Controlador;

import java.sql.SQLException;
import javax.swing.JOptionPane;
import gym.Modelo.GestorBD;
import gym.Modelo.Entidades;
import gym.Vista.VistaLogin;

// Controlador para gestionar la lógica relacionada con el inicio de sesión y registro de usuarios
public class LoginControlador {
    
    private VistaLogin vistaLogin;
    
    // Constructor
    public LoginControlador() {
        this.vistaLogin = new VistaLogin();
    }
    
    public void iniciar() {
        try {
            int opcion = vistaLogin.mostrarOpcionInicial();
            
            if (opcion == 0) {
                registrarUsuario();
            } else if (opcion == 1) {    
                loginUsuario();        
            } else if (opcion == -1 || opcion == JOptionPane.CLOSED_OPTION) {
                System.exit(0);
            }
            
        } catch (Exception e) {
            vistaLogin.mostrarError("Error en el sistema: " + e.getMessage());
            iniciar();    
        }
    }
    
    // Registrar un nuevo usuario
    private void registrarUsuario() throws SQLException {
        String tipo = vistaLogin.mostrarSeleccionTipo();
        
        if (tipo == null) {
            iniciar();
            return;
        }

        String[] datos = vistaLogin.mostrarRegistro(tipo);
        
        if (datos == null) {
            iniciar();
            return;
        }
                Entidades.Usuario nuevo = new Entidades.Usuario(
            datos[0].trim(),    
            datos[1].trim().toLowerCase(),    
            datos[2],    
            datos[3]
        );
        
        if (GestorBD.registrarUsuario(nuevo)) {
            vistaLogin.mostrarMensaje("Registro exitoso, ahora inicia sesión");
            loginUsuario();
        } else {
            vistaLogin.mostrarError("Error en el registro - El email ya existe");
            registrarUsuario();
        }
    }
    
    // Iniciar sesión de un usuario existente
    private void loginUsuario() throws SQLException {
        String[] credenciales = vistaLogin.mostrarLogin();
        
        if (credenciales == null) {
            iniciar();
            return;
        }
        
        Entidades.Usuario usuario = GestorBD.login(credenciales[0], credenciales[1]);
        
        if (usuario != null) {
            vistaLogin.mostrarMensaje("¡Bienvenido " + usuario.getNombre() + "!");
            
            if ("entrenador".equals(usuario.getTipo())) {
                new EntrenadorControlador(usuario).mostrarMenuEntrenador();    
            } else {
                new UsuarioControlador(usuario).iniciar();    
            }
        } else {
            vistaLogin.mostrarError("Credenciales incorrectas");
            loginUsuario();
        }
    }
}