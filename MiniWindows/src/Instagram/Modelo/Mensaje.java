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
public class Mensaje implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    private String id;
    private String emisor;
    private String receptor;
    private String contenido;
    private LocalDateTime fechaHora;
    private boolean leido;
    
    public Mensaje(String emisor, String receptor, String contenido) {
        this.id = UUID.randomUUID().toString();
        this.emisor = emisor;
        this.receptor = receptor;
        this.contenido = contenido;
        this.fechaHora = LocalDateTime.now();
        this.leido = false;
    }
    
    public String getId() {
        return id;
    }
    
    public String getEmisor() {
        return emisor;
    }
    
    public void setEmisor(String emisor) {
        this.emisor = emisor;
    }
    
    public String getReceptor() {
        return receptor;
    }
    
    public void setReceptor(String receptor) {
        this.receptor = receptor;
    }
    
    public String getContenido() {
        return contenido;
    }
    
    public void setContenido(String contenido) {
        this.contenido = contenido;
    }
    
    public LocalDateTime getFechaHora() {
        return fechaHora;
    }
    
    public void setFechaHora(LocalDateTime fechaHora) {
        this.fechaHora = fechaHora;
    }
    
    public boolean isLeido() {
        return leido;
    }
    
    public void setLeido(boolean leido) {
        this.leido = leido;
    }
    
    @Override
    public String toString() {
        return "Mensaje{" +
                "id='" + id + '\'' +
                ", emisor='" + emisor + '\'' +
                ", receptor='" + receptor + '\'' +
                ", contenido='" + contenido + '\'' +
                ", fechaHora=" + fechaHora +
                ", leido=" + leido +
                '}';
    }
}
