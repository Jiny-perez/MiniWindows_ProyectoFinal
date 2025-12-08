/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Instagram.Modelo;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.UUID;

/**
 *
 * @author najma
 */
public class Publicacion implements Serializable {
    
    private static final long serialVersionUID = 1L;
    public static final int MAX_CARACTERES = 140;
    
    private String id;
    private String username;
    private String contenido;
    private String rutaImagen;
    private LocalDateTime fechaPublicacion;
    
    public Publicacion(String username, String contenido) throws IllegalArgumentException {
        if (contenido != null && contenido.length() > MAX_CARACTERES) {
            throw new IllegalArgumentException("El contenido no puede exceder " + MAX_CARACTERES + " caracteres");
        }
        
        this.id = UUID.randomUUID().toString();
        this.username = username;
        this.contenido = contenido != null ? contenido : "";
        this.fechaPublicacion = LocalDateTime.now();
        this.rutaImagen = null;
    }
    
    public Publicacion(String username, String contenido, String rutaImagen) throws IllegalArgumentException {
        this(username, contenido);
        this.rutaImagen = rutaImagen;
    }
    
    public String getTiempoTranscurrido() {
        LocalDateTime ahora = LocalDateTime.now();
        long minutos = java.time.Duration.between(fechaPublicacion, ahora).toMinutes();
        
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
    
    public ArrayList<String> extraerHashtags() {
        ArrayList<String> hashtags = new ArrayList<>();
        String[] palabras = contenido.split("\\s+");
        
        for (String palabra : palabras) {
            if (palabra.startsWith("#") && palabra.length() > 1) {
                String hashtag = palabra.substring(1).toLowerCase();
                hashtag = hashtag.replaceAll("[^a-záéíóúñ0-9_]$", "");
                if (!hashtag.isEmpty() && !hashtags.contains(hashtag)) {
                    hashtags.add(hashtag);
                }
            }
        }
        
        return hashtags;
    }
    
    public ArrayList<String> extraerMenciones() {
        ArrayList<String> menciones = new ArrayList<>();
        String[] palabras = contenido.split("\\s+");
        
        for (String palabra : palabras) {
            if (palabra.startsWith("@") && palabra.length() > 1) {
                String mencion = palabra.substring(1);
                mencion = mencion.replaceAll("[^a-zA-Z0-9_]$", "");
                if (!mencion.isEmpty() && !menciones.contains(mencion)) {
                    menciones.add(mencion);
                }
            }
        }
        
        return menciones;
    }
    
    public boolean tieneHashtag(String hashtag) {
        return extraerHashtags().contains(hashtag.toLowerCase());
    }
    
    // Getters
    public String getId() { return id; }
    public String getUsername() { return username; }
    public String getContenido() { return contenido; }
    public String getRutaImagen() { return rutaImagen; }
    public LocalDateTime getFechaPublicacion() { return fechaPublicacion; }
    
    public boolean tieneImagen() {
        return rutaImagen != null && !rutaImagen.isEmpty();
    }
    
    @Override
    public String toString() {
        return "@" + username + " · " + getTiempoTranscurrido() + 
               "\n" + contenido;
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Publicacion other = (Publicacion) obj;
        return id.equals(other.id);
    }
    
    @Override
    public int hashCode() {
        return id.hashCode();
    }
}
