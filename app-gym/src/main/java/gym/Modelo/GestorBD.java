package gym.Modelo;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import gym.Modelo.Entidades.*;

public class GestorBD {
    private static final String url = "jdbc:mysql://localhost:3306/gym_app";
    private static final String user = "root";
    private static final String pass = "1234";

    public static Connection getConnection() throws SQLException {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            return DriverManager.getConnection(url, user, pass);
        } catch (ClassNotFoundException e) {
            throw new SQLException("Driver no encontrado", e);
        }
    }

    // Usuarios
    public static Usuario login(String email, String password) throws SQLException {
        try (Connection c = getConnection();
                PreparedStatement p = c
                        .prepareStatement("SELECT * FROM usuario WHERE email=? AND password=? AND activo=TRUE")) {
            p.setString(1, email);
            p.setString(2, password);
            ResultSet rs = p.executeQuery();
            return rs.next() ? mapUsuario(rs) : null;
        }
    }

    public static boolean registrarUsuario(Usuario u) throws SQLException {
        try (Connection c = getConnection();
                PreparedStatement p = c.prepareStatement(
                        "INSERT INTO usuario (nombre, email, password, tipo) VALUES (?,?,?,?)",
                        Statement.RETURN_GENERATED_KEYS)) {
            p.setString(1, u.getNombre());
            p.setString(2, u.getEmail());
            p.setString(3, u.getPassword());
            p.setString(4, u.getTipo());
            if (p.executeUpdate() > 0) {
                ResultSet rs = p.getGeneratedKeys();
                if (rs.next())
                    u.setIdUsuario(rs.getInt(1));
                return true;
            }
        } catch (SQLException e) {
            if (e.getMessage().contains("Duplicate"))
                return false;
            throw e;
        }
        return false;
    }

    public static String obtenerNombreUsuarioPorId(int id) throws SQLException {
        try (Connection c = getConnection();
                PreparedStatement p = c.prepareStatement("SELECT nombre FROM usuario WHERE id_usuario=?")) {
            p.setInt(1, id);
            ResultSet rs = p.executeQuery();
            return rs.next() ? rs.getString("nombre") : "Usuario " + id;
        }
    }

    public static List<Usuario> obtenerTodosEntrenadores() throws SQLException {
        List<Usuario> lista = new ArrayList<>();
        try (Connection c = getConnection();
                Statement s = c.createStatement();
                ResultSet rs = s.executeQuery(
                        "SELECT u.* FROM usuario u " +
                                "JOIN entrenador e ON u.id_usuario = e.id_usuario " +
                                "WHERE u.activo=TRUE AND e.activo=TRUE")) {
            while (rs.next())
                lista.add(mapUsuario(rs));
        }
        return lista;
    }

    // Solicitudes de entrenador
    public static boolean enviarSolicitudEntrenador(int idUsuario, int idEntrenadorUsuario, String mensaje)
            throws SQLException {
        try (Connection c = getConnection()) {

            int idEntrenadorReal = -1;
            String sqlGetEntrenador = "SELECT id_entrenador FROM entrenador WHERE id_usuario = ?";
            try (PreparedStatement pGet = c.prepareStatement(sqlGetEntrenador)) {
                pGet.setInt(1, idEntrenadorUsuario);
                ResultSet rs = pGet.executeQuery();
                if (rs.next()) {
                    idEntrenadorReal = rs.getInt("id_entrenador");
                }
            }
            if (idEntrenadorReal == -1) {
                return false; 
            }
            try (PreparedStatement p = c.prepareStatement(
                    "INSERT INTO asigna (id_entrenador, id_usuario, id_rutina, estado, fecha_solicitud, mensaje_solicitud) VALUES (?,?,NULL,?,NOW(),?)")) {
                p.setInt(1, idEntrenadorReal);
                p.setInt(2, idUsuario);
                p.setString(3, "pendiente");
                p.setString(4, mensaje);
                return p.executeUpdate() > 0;
            }
        }
    }
    // Obtener solicitudes pendientes para un entrenador
    public static List<Asignacion> obtenerSolicitudesPendientes(int idEntrenador) throws SQLException {
        List<Asignacion> lista = new ArrayList<>();

        int idEntrenadorReal = -1;
        try (Connection c = getConnection();
                PreparedStatement pGet = c
                        .prepareStatement("SELECT id_entrenador FROM entrenador WHERE id_usuario = ?")) {
            pGet.setInt(1, idEntrenador);
            ResultSet rs = pGet.executeQuery();
            if (rs.next()) {
                idEntrenadorReal = rs.getInt("id_entrenador");
            }
        }

        if (idEntrenadorReal == -1) {
            return lista; 
        }

        String sql = "SELECT a.*, u.nombre as nombre_usuario, u.email as email_usuario " +
                "FROM asigna a " +
                "JOIN usuario u ON a.id_usuario = u.id_usuario " +
                "WHERE a.id_entrenador = ? AND a.estado = 'pendiente' " + 
                "ORDER BY a.fecha_solicitud DESC";

        try (Connection c = getConnection(); PreparedStatement p = c.prepareStatement(sql)) {
            p.setInt(1, idEntrenadorReal); 
            ResultSet rs = p.executeQuery();
            while (rs.next()) {
                lista.add(mapAsignacion(rs));
            }
        }
        return lista;
    }
    // Actualizar el estado de una solicitud
    public static boolean actualizarEstadoSolicitud(int idUsuario, int idEntrenadorUsuario, String estado)
            throws SQLException {
        try (Connection c = getConnection()) {

            int idEntrenadorReal = -1;
            try (PreparedStatement pGet = c
                    .prepareStatement("SELECT id_entrenador FROM entrenador WHERE id_usuario = ?")) {
                pGet.setInt(1, idEntrenadorUsuario);
                ResultSet rs = pGet.executeQuery();
                if (rs.next()) {
                    idEntrenadorReal = rs.getInt("id_entrenador");
                }
            }

            if (idEntrenadorReal == -1) {
                return false;
            }

            try (PreparedStatement p = c.prepareStatement(
                    "UPDATE asigna SET estado = ? WHERE id_usuario = ? AND id_entrenador = ?")) { 
                p.setString(1, estado);
                p.setInt(2, idUsuario);
                p.setInt(3, idEntrenadorReal);
                return p.executeUpdate() > 0;
            }
        }
    }
    // Verificar si existe una solicitud pendiente
    public static boolean existeSolicitudPendiente(int idUsuario, int idEntrenador) throws SQLException {
        try (Connection c = getConnection();
                PreparedStatement p = c.prepareStatement(
                        "SELECT COUNT(*) FROM asigna " +
                                "WHERE id_usuario = ? AND id_entrenador = ? AND estado = 'pendiente'")) {
            p.setInt(1, idUsuario);
            p.setInt(2, idEntrenador);
            ResultSet rs = p.executeQuery();
            return rs.next() && rs.getInt(1) > 0;
        }
    }
    // Obtener usuarios asignados a un entrenador
    public static List<Usuario> obtenerUsuariosPorEntrenador(int idEntrenadorUsuario) throws SQLException {
    List<Usuario> lista = new ArrayList<>();
    
    int idEntrenadorReal = -1;
    try (Connection c = getConnection();
         PreparedStatement pGet = c.prepareStatement("SELECT id_entrenador FROM entrenador WHERE id_usuario = ?")) {
        pGet.setInt(1, idEntrenadorUsuario);
        ResultSet rs = pGet.executeQuery();
        if (rs.next()) {
            idEntrenadorReal = rs.getInt("id_entrenador");
        }
    }
    
    if (idEntrenadorReal == -1) {
        return lista;
    }
    
    String sql = "SELECT u.* FROM usuario u JOIN asigna a ON u.id_usuario = a.id_usuario " +
        "WHERE a.id_entrenador = ? AND a.estado = 'activa' AND u.activo = TRUE";
    
    try (Connection c = getConnection(); PreparedStatement p = c.prepareStatement(sql)) {
        p.setInt(1, idEntrenadorReal); 
        ResultSet rs = p.executeQuery();
        while (rs.next()) {
            lista.add(mapUsuario(rs));
        }
    }
    return lista;
}
    // Obtener todos los usuarios
    public static List<Usuario> obtenerTodosUsuarios() throws SQLException {
        List<Usuario> lista = new ArrayList<>();
        try (Connection c = getConnection();
                Statement s = c.createStatement();
                ResultSet rs = s.executeQuery("SELECT * FROM usuario WHERE activo=TRUE")) {
            while (rs.next())
                lista.add(mapUsuario(rs));
        }
        return lista;
    }

    // Rutinas
    public static boolean crearRutina(Rutina r) throws SQLException {
        try (Connection c = getConnection();
                PreparedStatement p = c.prepareStatement(
                        "INSERT INTO rutina (nombre, descripcion, id_usuario_creador) VALUES (?,?,?)",
                        Statement.RETURN_GENERATED_KEYS)) {
            p.setString(1, r.getNombre());
            p.setString(2, r.getDescripcion());
            p.setInt(3, r.getIdUsuarioCreador());
            if (p.executeUpdate() > 0) {
                ResultSet rs = p.getGeneratedKeys();
                if (rs.next())
                    r.setIdRutina(rs.getInt(1));
                return true;
            }
        }
        return false;
    }
    // Obtener rutinas creadas por un usuario
    public static List<Rutina> obtenerRutinasPorUsuario(int idUsuario) throws SQLException {
        List<Rutina> lista = new ArrayList<>();
        try (Connection c = getConnection();
                PreparedStatement p = c.prepareStatement(
                        "SELECT * FROM rutina WHERE id_usuario_creador=? AND activa=TRUE ORDER BY fecha_creacion DESC")) {
            p.setInt(1, idUsuario);
            ResultSet rs = p.executeQuery();
            while (rs.next())
                lista.add(mapRutina(rs));
        }
        return lista;
    }
    // Obtener rutinas de los clientes asignados a un entrenador
    public static List<Rutina> obtenerRutinasDeMisClientes(int idEntrenador) throws SQLException {
        List<Rutina> lista = new ArrayList<>();
        String sql = "SELECT r.* FROM rutina r JOIN asigna a ON r.id_usuario_creador = a.id_usuario " +
                "WHERE a.id_entrenador = ? AND a.estado = 'activa' AND r.activa = TRUE";
        try (Connection c = getConnection(); PreparedStatement p = c.prepareStatement(sql)) {
            p.setInt(1, idEntrenador);
            ResultSet rs = p.executeQuery();
            while (rs.next())
                lista.add(mapRutina(rs));
        }
        return lista;
    }
    // Obtener rutina por su ID
    public static Rutina obtenerRutinaPorId(int id) throws SQLException {
        try (Connection c = getConnection();
                PreparedStatement p = c.prepareStatement("SELECT * FROM rutina WHERE id_rutina=?")) {
            p.setInt(1, id);
            ResultSet rs = p.executeQuery();
            return rs.next() ? mapRutina(rs) : null;
        }
    }
    // Eliminar una rutina por su ID
    public static boolean eliminarRutina(int id) throws SQLException {
        try (Connection c = getConnection();
                PreparedStatement p = c.prepareStatement("DELETE FROM rutina WHERE id_rutina=?")) {
            p.setInt(1, id);
            return p.executeUpdate() > 0;
        }
    }

    // Ejercicios 
    public static List<Ejercicio> obtenerTodosEjercicios() throws SQLException {
        List<Ejercicio> lista = new ArrayList<>();
        try (Connection c = getConnection();
                Statement s = c.createStatement();
                ResultSet rs = s.executeQuery("SELECT * FROM ejercicio ORDER BY nombre")) {
            while (rs.next())
                lista.add(mapEjercicio(rs));
        }
        return lista;
    }
    // Crear un nuevo ejercicio general
    public static boolean crearEjercicioGeneral(Ejercicio e) throws SQLException {
        try (Connection c = getConnection();
                PreparedStatement p = c.prepareStatement(
                        "INSERT INTO ejercicio (nombre, descripcion, grupo_muscular, equipamiento_necesario) VALUES (?,?,?,?)")) {
            p.setString(1, e.getNombre());
            p.setString(2, e.getDescripcion());
            p.setString(3, e.getGrupoMuscular());
            p.setString(4, e.getEquipamientoNecesario());
            return p.executeUpdate() > 0;
        }
    }

    // Ejercicios en Rutinas
    public static List<EjercicioRutina> obtenerEjerciciosDeRutina(int idRutina) throws SQLException {
        List<EjercicioRutina> lista = new ArrayList<>();
        String sql = "SELECT er.*, e.nombre as ej_nombre, e.grupo_muscular FROM ejercicio_rutina er JOIN ejercicio e ON er.id_ejercicio = e.id_ejercicio WHERE er.id_rutina = ? ORDER BY er.orden";
        try (Connection c = getConnection(); PreparedStatement p = c.prepareStatement(sql)) {
            p.setInt(1, idRutina);
            ResultSet rs = p.executeQuery();
            while (rs.next()) {
                EjercicioRutina er = mapEjercicioRutina(rs);
                er.setEjercicioNombre(rs.getString("ej_nombre"));
                er.setGrupoMuscular(rs.getString("grupo_muscular"));
                lista.add(er);
            }
        }
        return lista;
    }
    // Agregar un ejercicio a una rutina
    public static boolean agregarEjercicioARutina(EjercicioRutina er) throws SQLException {
        String sql = "INSERT INTO ejercicio_rutina (id_rutina, id_ejercicio, series_planificadas, repeticiones_planificadas, peso_recomendado, descanso_segundos, orden) VALUES (?,?,?,?,?,?,?)";
        try (Connection c = getConnection();
                PreparedStatement p = c.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            p.setInt(1, er.getIdRutina());
            p.setInt(2, er.getIdEjercicio());
            p.setInt(3, er.getSeriesPlanificadas());
            p.setInt(4, er.getRepeticionesPlanificadas());
            p.setDouble(5, er.getPesoRecomendado());
            p.setInt(6, er.getDescansoSegundos());
            p.setInt(7, er.getOrden());
            if (p.executeUpdate() > 0) {
                ResultSet rs = p.getGeneratedKeys();
                if (rs.next())
                    er.setIdEjercicioRutina(rs.getInt(1));
                return true;
            }
        }
        return false;
    }
    // Eliminar un ejercicio de una rutina
    public static boolean eliminarEjercicioDeRutina(int id) throws SQLException {
        try (Connection c = getConnection();
                PreparedStatement p = c.prepareStatement("DELETE FROM ejercicio_rutina WHERE id_ejercicio_rutina=?")) {
            p.setInt(1, id);
            return p.executeUpdate() > 0;
        }
    }
    //  Obtener ejercicios de las rutinas creadas por un usuario
    public static List<Ejercicio> obtenerEjerciciosDeMisRutinas(int idUsuario) throws SQLException {
        List<Ejercicio> lista = new ArrayList<>();
        String sql = "SELECT DISTINCT e.* FROM ejercicio e JOIN ejercicio_rutina er ON e.id_ejercicio = er.id_ejercicio JOIN rutina r ON er.id_rutina = r.id_rutina WHERE r.id_usuario_creador = ? ORDER BY e.nombre";
        try (Connection c = getConnection(); PreparedStatement p = c.prepareStatement(sql)) {
            p.setInt(1, idUsuario);
            ResultSet rs = p.executeQuery();
            while (rs.next())
                lista.add(mapEjercicio(rs));
        }
        return lista;
    }
    // Obtener ejercicios de las rutinas de los clientes asignados a un entrenador
    public static List<Ejercicio> obtenerEjerciciosDeClientes(int idEntrenador) throws SQLException {
        List<Ejercicio> lista = new ArrayList<>();
        String sql = "SELECT DISTINCT ej.* FROM ejercicio ej " +
                "JOIN ejercicio_rutina er ON ej.id_ejercicio = er.id_ejercicio " +
                "JOIN rutina r ON er.id_rutina = r.id_rutina " +
                "JOIN usuario u ON r.id_usuario_creador = u.id_usuario " +
                "JOIN asigna a ON u.id_usuario = a.id_usuario " +
                "WHERE a.id_entrenador = ? AND a.estado = 'activa' ORDER BY ej.nombre";
        try (Connection c = getConnection(); PreparedStatement p = c.prepareStatement(sql)) {
            p.setInt(1, idEntrenador);
            ResultSet rs = p.executeQuery();
            while (rs.next())
                lista.add(mapEjercicio(rs));
        }
        return lista;
    }

    //  Entrenamientos y Ejecuciones
    public static boolean registrarEntrenador(int idUsuario) throws SQLException {
        try (Connection c = getConnection();
                PreparedStatement p = c.prepareStatement(
                        "INSERT INTO entrenador (id_usuario, especialidad) VALUES (?, 'Entrenador personal')")) {
            p.setInt(1, idUsuario);
            return p.executeUpdate() > 0;
        }
    }
    // Registrar un nuevo entrenamiento
    public static boolean registrarEntrenamiento(Entrenamiento e) throws SQLException {
        try (Connection c = getConnection();
                PreparedStatement p = c.prepareStatement(
                        "INSERT INTO entrenamiento (id_usuario, id_rutina, duracion_horas, notas_usuario) VALUES (?,?,?,?)",
                        Statement.RETURN_GENERATED_KEYS)) {
            p.setInt(1, e.getIdUsuario());
            p.setInt(2, e.getIdRutina());
            p.setDouble(3, e.getDuracionHoras());
            p.setString(4, e.getNotas());
            if (p.executeUpdate() > 0) {
                ResultSet rs = p.getGeneratedKeys();
                if (rs.next())
                    e.setIdEntrenamiento(rs.getInt(1));
                return true;
            }
        }
        return false;
    }
    // Registrar una ejecución de ejercicio dentro de un entrenamiento
    public static boolean registrarEjecucion(Ejecuta ex) throws SQLException {
        try (Connection c = getConnection();
                PreparedStatement p = c.prepareStatement(
                        "INSERT INTO ejecuta (id_entrenamiento, id_ejercicio_rutina, series_reales, repeticiones_reales, peso_real, rpe, notas) VALUES (?,?,?,?,?,?,?)")) {
            p.setInt(1, ex.getIdEntrenamiento());
            p.setInt(2, ex.getIdEjercicioRutina());
            p.setInt(3, ex.getSeriesReales());
            p.setInt(4, ex.getRepeticionesReales());
            p.setDouble(5, ex.getPesoReal());
            p.setInt(6, ex.getRpe());
            p.setString(7, ex.getNotas());
            return p.executeUpdate() > 0;
        }
    }
    // Obtener entrenamientos de un usuario
    public static List<Entrenamiento> obtenerEntrenamientosPorUsuario(int idUsuario) throws SQLException {
        List<Entrenamiento> lista = new ArrayList<>();
        try (Connection c = getConnection();
                PreparedStatement p = c.prepareStatement(
                        "SELECT * FROM entrenamiento WHERE id_usuario=? ORDER BY fecha_entrenamiento DESC")) {
            p.setInt(1, idUsuario);
            ResultSet rs = p.executeQuery();
            while (rs.next())
                lista.add(mapEntrenamiento(rs));
        }
        return lista;
    }
    // Obtener entrenamientos de los clientes asignados a un entrenador
    public static List<Entrenamiento> obtenerEntrenamientosDeMisClientes(int idEntrenador) throws SQLException {
        List<Entrenamiento> lista = new ArrayList<>();
        String sql = "SELECT e.* FROM entrenamiento e " +
                "JOIN usuario u ON e.id_usuario = u.id_usuario " +
                "JOIN asigna a ON u.id_usuario = a.id_usuario " +
                "WHERE a.id_entrenador = ? AND a.estado = 'activa' ORDER BY e.fecha_entrenamiento DESC";
        try (Connection c = getConnection(); PreparedStatement p = c.prepareStatement(sql)) {
            p.setInt(1, idEntrenador);
            ResultSet rs = p.executeQuery();
            while (rs.next())
                lista.add(mapEntrenamiento(rs));
        }
        return lista;
    }
    // Obtener ejecuciones de un entrenamiento
    public static List<Ejecuta> obtenerEjecucionesDeEntrenamiento(int idEntrenamiento) throws SQLException {
        List<Ejecuta> lista = new ArrayList<>();
        String sql = "SELECT ex.*, e.nombre as ejercicio_nombre FROM ejecuta ex " +
                "JOIN ejercicio_rutina er ON ex.id_ejercicio_rutina = er.id_ejercicio_rutina " +
                "JOIN ejercicio e ON er.id_ejercicio = e.id_ejercicio " +
                "WHERE ex.id_entrenamiento = ?";

        try (Connection c = getConnection(); PreparedStatement p = c.prepareStatement(sql)) {
            p.setInt(1, idEntrenamiento);
            ResultSet rs = p.executeQuery();
            while (rs.next()) {
                Ejecuta ex = new Ejecuta();
                ex.setIdEntrenamiento(rs.getInt("id_entrenamiento"));
                ex.setIdEjercicioRutina(rs.getInt("id_ejercicio_rutina"));
                ex.setSeriesReales(rs.getInt("series_reales"));
                ex.setRepeticionesReales(rs.getInt("repeticiones_reales"));
                ex.setPesoReal(rs.getDouble("peso_real"));
                ex.setRpe(rs.getInt("rpe"));
                ex.setEjercicioNombre(rs.getString("ejercicio_nombre"));
                lista.add(ex);
            }
        }
        return lista;
    }
    // Obtener progreso de un ejercicio específico para un usuario
    public static List<ProgresoEjercicio> obtenerProgresoEjercicio(int idUsuario, int idEjercicio) throws SQLException {
        List<ProgresoEjercicio> lista = new ArrayList<>();
        String sql = "SELECT ej.fecha_entrenamiento, ex.series_reales, ex.repeticiones_reales, ex.peso_real FROM ejecuta ex JOIN entrenamiento ej ON ex.id_entrenamiento = ej.id_entrenamiento JOIN ejercicio_rutina er ON ex.id_ejercicio_rutina = er.id_ejercicio_rutina WHERE ej.id_usuario = ? AND er.id_ejercicio = ? ORDER BY ej.fecha_entrenamiento DESC";
        try (Connection c = getConnection(); PreparedStatement p = c.prepareStatement(sql)) {
            p.setInt(1, idUsuario);
            p.setInt(2, idEjercicio);
            ResultSet rs = p.executeQuery();
            while (rs.next()) {
                ProgresoEjercicio pg = new ProgresoEjercicio();
                pg.setFecha(rs.getTimestamp("fecha_entrenamiento").toLocalDateTime());
                pg.setSeriesReales(rs.getInt("series_reales"));
                pg.setRepeticionesReales(rs.getInt("repeticiones_reales"));
                pg.setPesoReal(rs.getDouble("peso_real"));
                lista.add(pg);
            }
        }
        return lista;
    }

    // Mapeos de las Entidades
    private static Usuario mapUsuario(ResultSet rs) throws SQLException {
        Usuario u = new Usuario();
        u.setIdUsuario(rs.getInt("id_usuario"));
        u.setNombre(rs.getString("nombre"));
        u.setEmail(rs.getString("email"));
        u.setPassword(rs.getString("password"));
        u.setTipo(rs.getString("tipo"));
        u.setActivo(rs.getBoolean("activo"));
        return u;
    }

    private static Rutina mapRutina(ResultSet rs) throws SQLException {
        Rutina r = new Rutina();
        r.setIdRutina(rs.getInt("id_rutina"));
        r.setNombre(rs.getString("nombre"));
        r.setDescripcion(rs.getString("descripcion"));
        r.setIdUsuarioCreador(rs.getInt("id_usuario_creador"));
        r.setFechaCreacion(rs.getTimestamp("fecha_creacion").toLocalDateTime());
        r.setActiva(rs.getBoolean("activa"));
        return r;
    }

    private static Ejercicio mapEjercicio(ResultSet rs) throws SQLException {
        Ejercicio e = new Ejercicio();
        e.setIdEjercicio(rs.getInt("id_ejercicio"));
        e.setNombre(rs.getString("nombre"));
        e.setDescripcion(rs.getString("descripcion"));
        e.setGrupoMuscular(rs.getString("grupo_muscular"));
        e.setEquipamientoNecesario(rs.getString("equipamiento_necesario"));
        return e;
    }

    private static EjercicioRutina mapEjercicioRutina(ResultSet rs) throws SQLException {
        EjercicioRutina er = new EjercicioRutina();
        er.setIdEjercicioRutina(rs.getInt("id_ejercicio_rutina"));
        er.setIdRutina(rs.getInt("id_rutina"));
        er.setIdEjercicio(rs.getInt("id_ejercicio"));
        er.setSeriesPlanificadas(rs.getInt("series_planificadas"));
        er.setRepeticionesPlanificadas(rs.getInt("repeticiones_planificadas"));
        er.setPesoRecomendado(rs.getDouble("peso_recomendado"));
        er.setDescansoSegundos(rs.getInt("descanso_segundos"));
        er.setOrden(rs.getInt("orden"));
        return er;
    }

    private static Entrenamiento mapEntrenamiento(ResultSet rs) throws SQLException {
        Entrenamiento e = new Entrenamiento();
        e.setIdEntrenamiento(rs.getInt("id_entrenamiento"));
        e.setIdUsuario(rs.getInt("id_usuario"));
        e.setIdRutina(rs.getInt("id_rutina"));
        e.setFechaEntrenamiento(rs.getTimestamp("fecha_entrenamiento").toLocalDateTime());
        e.setDuracionHoras(rs.getDouble("duracion_horas"));
        e.setNotas(rs.getString("notas_usuario"));
        return e;
    }

    private static Asignacion mapAsignacion(ResultSet rs) throws SQLException {
    Asignacion a = new Asignacion();
    a.setIdAsignacion(rs.getInt("id_asignacion"));
    a.setIdUsuario(rs.getInt("id_usuario"));
    a.setIdEntrenador(rs.getInt("id_entrenador"));
    a.setIdRutina(rs.getInt("id_rutina"));
    a.setEstado(rs.getString("estado"));
    a.setFechaSolicitud(
            rs.getTimestamp("fecha_solicitud") != null ? rs.getTimestamp("fecha_solicitud").toLocalDateTime()
                    : null);
    a.setFechaAsignacion(
            rs.getTimestamp("fecha_asignacion") != null ? rs.getTimestamp("fecha_asignacion").toLocalDateTime()
                    : null);
    a.setMensajeSolicitud(rs.getString("mensaje_solicitud"));
    a.setInstruccionesEspeciales(rs.getString("instrucciones_especiales"));
    a.setNombreUsuario(rs.getString("nombre_usuario"));
    a.setEmailUsuario(rs.getString("email_usuario"));
    return a;
}
}