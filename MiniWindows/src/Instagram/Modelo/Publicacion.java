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
    
    private String id;
    private String username;
    private String contenido;
    private String rutaImagen;
    private LocalDateTime fechaCreacion;
    
    private ArrayList<String> likes;
    private ArrayList<Comentario> comentarios;
    private ArrayList<String> hashtags;
    
    public Publicacion(String username, String contenido) {
        this.id = UUID.randomUUID().toString();
        this.username = username;
        this.contenido = contenido;
        this.fechaCreacion = LocalDateTime.now();
        this.rutaImagen = null;
        
        this.likes = new ArrayList<>();
        this.comentarios = new ArrayList<>();
        this.hashtags = new ArrayList<>();
        
        extraerHashtags();
    }
    
    public Publicacion(String username, String contenido, String rutaImagen) {
        this(username, contenido);
        this.rutaImagen = rutaImagen;
    }
    
    public boolean darLike(String username) {
        if (!likes.contains(username)) {
            likes.add(username);
            return true;
        }
        return false;
    }
    
    public boolean quitarLike(String username) {
        return likes.remove(username);
    }
    
    public boolean tieneLikeDe(String username) {
        return likes.contains(username);
    }
    
    public int getCantidadLikes() {
        return likes.size();
    }
    
    public void agregarComentario(Comentario comentario) {
        comentarios.add(comentario);
    }
    
    public boolean eliminarComentario(String comentarioId) {
        return comentarios.removeIf(c -> c.getId().equals(comentarioId));
    }
    
    public int getCantidadComentarios() {
        return comentarios.size();
    }
    
    private void extraerHashtags() {
        hashtags.clear();
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
    }
    
    public boolean tieneHashtag(String hashtag) {
        return hashtags.contains(hashtag.toLowerCase());
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
        extraerHashtags();
    }
    
    public String getRutaImagen() {
        return rutaImagen;
    }
    
    public void setRutaImagen(String rutaImagen) {
        this.rutaImagen = rutaImagen;
    }
    
    public boolean tieneImagen() {
        return rutaImagen != null && !rutaImagen.isEmpty();
    }
    
    public LocalDateTime getFechaCreacion() {
        return fechaCreacion;
    }
    
    public ArrayList<String> getLikes() {
        return new ArrayList<>(likes);
    }
    
    public ArrayList<Comentario> getComentarios() {
        return new ArrayList<>(comentarios);
    }
    
    public ArrayList<String> getHashtags() {
        return new ArrayList<>(hashtags);
    }
    
    @Override
    public String toString() {
        return "@" + username + " · " + getTiempoTranscurrido() + 
               "\n" + contenido +
               "\n❤️ " + getCantidadLikes() + "  💬 " + getCantidadComentarios();
    }
}