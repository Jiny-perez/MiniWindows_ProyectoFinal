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
public class Mensaje {
    
    private static final long serialVersionUID = 1L;
    
    private String id;
    private String remitente;
    private String destinatario;
    private String contenido;
    private LocalDateTime fechaEnvio;
    private boolean leido;
    
    public Mensaje(String remitente, String destinatario, String contenido) {
        this.id = UUID.randomUUID().toString();
        this.remitente = remitente;
        this.destinatario = destinatario;
        this.contenido = contenido;
        this.fechaEnvio = LocalDateTime.now();
        this.leido = false;
    }
    
    public String getId() {
        return id;
    }
    
    public String getRemitente() {
        return remitente;
    }
    
    public String getDestinatario() {
        return destinatario;
    }
    
    public String getContenido() {
        return contenido;
    }
    
    public LocalDateTime getFechaEnvio() {
        return fechaEnvio;
    }
    
    public boolean isLeido() {
        return leido;
    }
    
    public void marcarComoLeido() {
        this.leido = true;
    }
    
    public String getTiempoTranscurrido() {
        LocalDateTime ahora = LocalDateTime.now();
        long minutos = java.time.Duration.between(fechaEnvio, ahora).toMinutes();
        
        if (minutos < 1) {
            return "ahora";
        } else if (minutos < 60) {
            return minutos + "m";
        } else if (minutos < 1440) {
            return (minutos / 60) + "h";
        } else {
            return (minutos / 1440) + "d";
        }
    }
    
    public boolean involucra(String username) {
        return remitente.equals(username) || destinatario.equals(username);
    }
    
    public String getOtroUsuario(String username) {
        return remitente.equals(username) ? destinatario : remitente;
    }
    
    @Override
    public String toString() {
        return remitente + " → " + destinatario + ": " + contenido;
    }
}