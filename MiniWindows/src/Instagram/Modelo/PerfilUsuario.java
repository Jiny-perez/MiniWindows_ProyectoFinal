/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Instagram.Modelo;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 *
 * @author najma
 */
public class PerfilUsuario implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    private String username;
    private String biografia;
    private String rutaAvatar;
    private LocalDateTime fechaRegistro;
    private int cantidadPublicaciones;
    private int cantidadSeguidores;
    private int cantidadSeguidos;
    
    // Configuraciones del perfil
    private boolean perfilPrivado;
    private boolean mostrarEnBusqueda;
    
    public PerfilUsuario(String username) {
        this.username = username;
        this.biografia = "";
        this.rutaAvatar = null;
        this.fechaRegistro = LocalDateTime.now();
        this.cantidadPublicaciones = 0;
        this.cantidadSeguidores = 0;
        this.cantidadSeguidos = 0;
        this.perfilPrivado = false;
        this.mostrarEnBusqueda = true;
    }
    
    public String getUsername() { return username; }
    public String getBiografia() { return biografia; }
    public String getRutaAvatar() { return rutaAvatar; }
    public LocalDateTime getFechaRegistro() { return fechaRegistro; }
    public int getCantidadPublicaciones() { return cantidadPublicaciones; }
    public int getCantidadSeguidores() { return cantidadSeguidores; }
    public int getCantidadSeguidos() { return cantidadSeguidos; }
    public boolean isPerfilPrivado() { return perfilPrivado; }
    public boolean isMostrarEnBusqueda() { return mostrarEnBusqueda; }
    
    public void setBiografia(String biografia) { 
        this.biografia = biografia != null ? biografia : ""; 
    }
    
    public void setRutaAvatar(String rutaAvatar) { 
        this.rutaAvatar = rutaAvatar; 
    }
    
    public void setCantidadPublicaciones(int cantidad) { 
        this.cantidadPublicaciones = Math.max(0, cantidad); 
    }
    
    public void setCantidadSeguidores(int cantidad) { 
        this.cantidadSeguidores = Math.max(0, cantidad); 
    }
    
    public void setCantidadSeguidos(int cantidad) { 
        this.cantidadSeguidos = Math.max(0, cantidad); 
    }
    
    public void setPerfilPrivado(boolean perfilPrivado) { 
        this.perfilPrivado = perfilPrivado; 
    }
    
    public void setMostrarEnBusqueda(boolean mostrarEnBusqueda) { 
        this.mostrarEnBusqueda = mostrarEnBusqueda; 
    }
    
    public void incrementarPublicaciones() { 
        this.cantidadPublicaciones++; 
    }
    
    public void decrementarPublicaciones() { 
        if (this.cantidadPublicaciones > 0) {
            this.cantidadPublicaciones--;
        }
    }
    
    public void incrementarSeguidores() { 
        this.cantidadSeguidores++; 
    }
    
    public void decrementarSeguidores() { 
        if (this.cantidadSeguidores > 0) {
            this.cantidadSeguidores--;
        }
    }
    
    public void incrementarSeguidos() { 
        this.cantidadSeguidos++; 
    }
    
    public void decrementarSeguidos() { 
        if (this.cantidadSeguidos > 0) {
            this.cantidadSeguidos--;
        }
    }

    public boolean tieneAvatarPersonalizado() {
        return rutaAvatar != null && !rutaAvatar.isEmpty();
    }

    public String getTiempoDesdeRegistro() {
        if (fechaRegistro == null) {
            return "Fecha desconocida";
        }
        
        LocalDateTime ahora = LocalDateTime.now();
        long dias = java.time.temporal.ChronoUnit.DAYS.between(fechaRegistro, ahora);
        
        if (dias == 0) {
            return "Hoy";
        } else if (dias == 1) {
            return "Ayer";
        } else if (dias < 30) {
            return "Hace " + dias + " días";
        } else if (dias < 365) {
            long meses = dias / 30;
            return "Hace " + meses + (meses == 1 ? " mes" : " meses");
        } else {
            long años = dias / 365;
            return "Hace " + años + (años == 1 ? " año" : " años");
        }
    }
    
    @Override
    public String toString() {
        return "PerfilUsuario{" +
                "username='" + username + '\'' +
                ", publicaciones=" + cantidadPublicaciones +
                ", seguidores=" + cantidadSeguidores +
                ", seguidos=" + cantidadSeguidos +
                ", biografia='" + biografia + '\'' +
                '}';
    }
}