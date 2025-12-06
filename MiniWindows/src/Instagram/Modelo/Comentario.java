/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Instagram.Modelo;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 *
 * @author najma
 */
public class Comentario implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    private String id;
    private String username;
    private String contenido;
    private LocalDateTime fechaCreacion;
    private String publicacionId;
    
    public Comentario(String username, String contenido, String publicacionId) {
        this.id = UUID.randomUUID().toString();
        this.username = username;
        this.contenido = contenido;
        this.fechaCreacion = LocalDateTime.now();
        this.publicacionId = publicacionId;
    }
    
    public String getTiempoTranscurrido() {
        LocalDateTime ahora = LocalDateTime.now();
        long minutos = java.time.Duration.between(fechaCreacion, ahora).toMinutes();
        
        if (minutos < 1) {
            return "ahora";
        } else if (minutos < 60) {
            return "hace " + minutos + "m";
        } else if (minutos < 1440) {
            long horas = minutos / 60;
            return "hace " + horas + "h";
        } else {
            long dias = minutos / 1440;
            return "hace " + dias + "d";
        }
    }
    
    public String getId() {
        return id;
    }
    
    public String getUsername() {
        return username;
    }
    
    public String getContenido() {
        return contenido;
    }
    
    public void setContenido(String contenido) {
        this.contenido = contenido;
    }
    
    public LocalDateTime getFechaCreacion() {
        return fechaCreacion;
    }
    
    public String getPublicacionId() {
        return publicacionId;
    }
    
    @Override
    public String toString() {
        return "@" + username + " · " + getTiempoTranscurrido() + 
               "\n" + contenido;
    }
}