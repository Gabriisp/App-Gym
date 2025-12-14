package gym.Modelo;

import java.time.LocalDateTime;

// Clase que contiene las entidades del sistema
public class Entidades {

    public static class Usuario {
        private int idUsuario;
        private String nombre, email, password, tipo;
        private boolean activo;

        public Usuario() {
        }

        public Usuario(String nombre, String email, String password, String tipo) {
            this.nombre = nombre;
            this.email = email;
            this.password = password;
            this.tipo = tipo;
            this.activo = true;
        }

        public int getIdUsuario() {
            return idUsuario;
        }

        public void setIdUsuario(int id) {
            this.idUsuario = id;
        }

        public String getNombre() {
            return nombre;
        }

        public void setNombre(String n) {
            this.nombre = n;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String e) {
            this.email = e;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String p) {
            this.password = p;
        }

        public String getTipo() {
            return tipo;
        }

        public void setTipo(String t) {
            this.tipo = t;
        }

        public boolean isActivo() {
            return activo;
        }

        public void setActivo(boolean a) {
            this.activo = a;
        }

        @Override
        public String toString() {
            return nombre;
        }
    }

    public static class Rutina {
        private int idRutina, idUsuarioCreador;
        private Integer idEntrenadorAsignador;
        private String nombre, descripcion;
        private LocalDateTime fechaCreacion;
        private boolean activa;

        public Rutina() {
        }

        public Rutina(String nombre, String descripcion, int idUsuarioCreador) {
            this.nombre = nombre;
            this.descripcion = descripcion;
            this.idUsuarioCreador = idUsuarioCreador;
            this.activa = true;
            this.fechaCreacion = LocalDateTime.now();
        }

        public int getIdRutina() {
            return idRutina;
        }

        public void setIdRutina(int id) {
            this.idRutina = id;
        }

        public String getNombre() {
            return nombre;
        }

        public void setNombre(String n) {
            this.nombre = n;
        }

        public String getDescripcion() {
            return descripcion;
        }

        public void setDescripcion(String d) {
            this.descripcion = d;
        }

        public int getIdUsuarioCreador() {
            return idUsuarioCreador;
        }

        public void setIdUsuarioCreador(int id) {
            this.idUsuarioCreador = id;
        }

        public Integer getIdEntrenadorAsignador() {
            return idEntrenadorAsignador;
        }

        public void setIdEntrenadorAsignador(Integer id) {
            this.idEntrenadorAsignador = id;
        }

        public LocalDateTime getFechaCreacion() {
            return fechaCreacion;
        }

        public void setFechaCreacion(LocalDateTime f) {
            this.fechaCreacion = f;
        }

        public boolean isActiva() {
            return activa;
        }

        public void setActiva(boolean a) {
            this.activa = a;
        }

        @Override
        public String toString() {
            return nombre;
        }
    }

    public static class Ejercicio {
        private int idEjercicio;
        private String nombre, descripcion, grupoMuscular, equipamientoNecesario;

        public int getIdEjercicio() {
            return idEjercicio;
        }

        public void setIdEjercicio(int id) {
            this.idEjercicio = id;
        }

        public String getNombre() {
            return nombre;
        }

        public void setNombre(String n) {
            this.nombre = n;
        }

        public String getDescripcion() {
            return descripcion;
        }

        public void setDescripcion(String d) {
            this.descripcion = d;
        }

        public String getGrupoMuscular() {
            return grupoMuscular;
        }

        public void setGrupoMuscular(String g) {
            this.grupoMuscular = g;
        }

        public String getEquipamientoNecesario() {
            return equipamientoNecesario;
        }

        public void setEquipamientoNecesario(String e) {
            this.equipamientoNecesario = e;
        }

        @Override
        public String toString() {
            return nombre;
        }
    }

    public static class EjercicioRutina {
        private int idEjercicioRutina, idRutina, idEjercicio, series, repeticiones, descanso, orden;
        private double peso;
        private String ejercicioNombre, grupoMuscular;

        public int getIdEjercicioRutina() {
            return idEjercicioRutina;
        }

        public void setIdEjercicioRutina(int id) {
            this.idEjercicioRutina = id;
        }

        public int getIdRutina() {
            return idRutina;
        }

        public void setIdRutina(int id) {
            this.idRutina = id;
        }

        public int getIdEjercicio() {
            return idEjercicio;
        }

        public void setIdEjercicio(int id) {
            this.idEjercicio = id;
        }

        public int getSeriesPlanificadas() {
            return series;
        }

        public void setSeriesPlanificadas(int s) {
            this.series = s;
        }

        public int getRepeticionesPlanificadas() {
            return repeticiones;
        }

        public void setRepeticionesPlanificadas(int r) {
            this.repeticiones = r;
        }

        public double getPesoRecomendado() {
            return peso;
        }

        public void setPesoRecomendado(double p) {
            this.peso = p;
        }

        public int getDescansoSegundos() {
            return descanso;
        }

        public void setDescansoSegundos(int d) {
            this.descanso = d;
        }

        public int getOrden() {
            return orden;
        }

        public void setOrden(int o) {
            this.orden = o;
        }

        public String getEjercicioNombre() {
            return ejercicioNombre;
        }

        public void setEjercicioNombre(String n) {
            this.ejercicioNombre = n;
        }

        public String getGrupoMuscular() {
            return grupoMuscular;
        }

        public void setGrupoMuscular(String g) {
            this.grupoMuscular = g;
        }
    }

    public static class Entrenamiento {
        private int idEntrenamiento, idUsuario, idRutina;
        private LocalDateTime fechaEntrenamiento;
        private double duracionHoras;
        private String notas;
        private boolean completado;

        public int getIdEntrenamiento() {
            return idEntrenamiento;
        }

        public void setIdEntrenamiento(int id) {
            this.idEntrenamiento = id;
        }

        public int getIdUsuario() {
            return idUsuario;
        }

        public void setIdUsuario(int id) {
            this.idUsuario = id;
        }

        public int getIdRutina() {
            return idRutina;
        }

        public void setIdRutina(int id) {
            this.idRutina = id;
        }

        public LocalDateTime getFechaEntrenamiento() {
            return fechaEntrenamiento;
        }

        public void setFechaEntrenamiento(LocalDateTime f) {
            this.fechaEntrenamiento = f;
        }

        public double getDuracionHoras() {
            return duracionHoras;
        }

        public void setDuracionHoras(double d) {
            this.duracionHoras = d;
        }

        public String getNotas() {
            return notas;
        }

        public void setNotas(String n) {
            this.notas = n;
        }

        public boolean isCompletado() {
            return completado;
        }

        public void setCompletado(boolean c) {
            this.completado = c;
        }
    }

    public static class Ejecuta {
        private int idEntrenamiento, idEjercicioRutina, series, repeticiones, rpe;
        private double peso;
        private String notas;
        private String ejercicioNombre;

        public void setIdEntrenamiento(int id) {
            this.idEntrenamiento = id;
        }

        public int getIdEntrenamiento() {
            return idEntrenamiento;
        }

        public void setIdEjercicioRutina(int id) {
            this.idEjercicioRutina = id;
        }

        public int getIdEjercicioRutina() {
            return idEjercicioRutina;
        }

        public void setSeriesReales(int s) {
            this.series = s;
        }

        public int getSeriesReales() {
            return series;
        }

        public void setRepeticionesReales(int r) {
            this.repeticiones = r;
        }

        public int getRepeticionesReales() {
            return repeticiones;
        }

        public void setPesoReal(double p) {
            this.peso = p;
        }

        public double getPesoReal() {
            return peso;
        }

        public void setRpe(int r) {
            this.rpe = r;
        }

        public int getRpe() {
            return rpe;
        }

        public void setNotas(String n) {
            this.notas = n;
        }

        public String getNotas() {
            return notas;
        }

        public void setEjercicioNombre(String n) {
            this.ejercicioNombre = n;
        }

        public String getEjercicioNombre() {
            return ejercicioNombre;
        }
    }

    public static class ProgresoEjercicio {
        private LocalDateTime fecha;
        private int seriesReales, repeticionesReales;
        private double pesoReal;

        public LocalDateTime getFecha() {
            return fecha;
        }

        public void setFecha(LocalDateTime f) {
            this.fecha = f;
        }

        public int getSeriesReales() {
            return seriesReales;
        }

        public void setSeriesReales(int s) {
            this.seriesReales = s;
        }

        public int getRepeticionesReales() {
            return repeticionesReales;
        }

        public void setRepeticionesReales(int r) {
            this.repeticionesReales = r;
        }

        public double getPesoReal() {
            return pesoReal;
        }

        public void setPesoReal(double p) {
            this.pesoReal = p;
        }
    }

    public static class Asignacion {
        private int idAsignacion;
        private int idUsuario;
        private int idEntrenador; // ID de la tabla 'entrenador'
        private int idEntrenadorUsuario; // ID de la tabla 'usuario' del entrenador
        private int idRutina;
        private String estado;
        private LocalDateTime fechaSolicitud;
        private LocalDateTime fechaAsignacion;
        private String mensajeSolicitud;
        private String instruccionesEspeciales;
        private boolean activa;
        private String nombreUsuario;
        private String emailUsuario;

        // Getters y Setters
        public int getIdAsignacion() { return idAsignacion; }
        public void setIdAsignacion(int id) { this.idAsignacion = id; }

        public int getIdUsuario() { return idUsuario; }
        public void setIdUsuario(int id) { this.idUsuario = id; }

        public int getIdEntrenador() { return idEntrenador; }
        public void setIdEntrenador(int id) { this.idEntrenador = id; }

        public int getIdEntrenadorUsuario() { return idEntrenadorUsuario; }
        public void setIdEntrenadorUsuario(int id) { this.idEntrenadorUsuario = id; }
        
        public int getIdRutina() { return idRutina; }
        public void setIdRutina(int id) { this.idRutina = id; }

        public String getEstado() { return estado; }
        public void setEstado(String estado) { this.estado = estado; }

        public LocalDateTime getFechaSolicitud() { return fechaSolicitud; }
        public void setFechaSolicitud(LocalDateTime fecha) { this.fechaSolicitud = fecha; }

        public LocalDateTime getFechaAsignacion() { return fechaAsignacion; }
        public void setFechaAsignacion(LocalDateTime fecha) { this.fechaAsignacion = fecha; }

        public String getMensajeSolicitud() { return mensajeSolicitud; }
        public void setMensajeSolicitud(String mensaje) { this.mensajeSolicitud = mensaje; }

        public String getInstruccionesEspeciales() { return instruccionesEspeciales; }
        public void setInstruccionesEspeciales(String instrucciones) { this.instruccionesEspeciales = instrucciones; }

        public boolean isActiva() { return activa; }
        public void setActiva(boolean activa) { this.activa = activa; }

        public String getNombreUsuario() { return nombreUsuario; }
        public void setNombreUsuario(String nombre) { this.nombreUsuario = nombre; }

        public String getEmailUsuario() { return emailUsuario; }
        public void setEmailUsuario(String email) { this.emailUsuario = email; }
    }
}