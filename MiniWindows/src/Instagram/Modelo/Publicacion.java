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
    private LocalDateTime fechaHora;
    private ArrayList<String> likes;
    private ArrayList<Comentario> comentarios;
    private ArrayList<String> hashtags;
    private ArrayList<String> menciones;
    
    public Publicacion(String username, String contenido) {
        if (contenido != null && contenido.length() > MAX_CARACTERES) {
            throw new IllegalArgumentException("El contenido no puede exceder " + MAX_CARACTERES + " caracteres");
        }
        
        this.id = UUID.randomUUID().toString();
        this.username = username;
        this.contenido = contenido;
        this.rutaImagen = null;
        this.fechaHora = LocalDateTime.now();
        this.likes = new ArrayList<>();
        this.comentarios = new ArrayList<>();
        this.hashtags = extraerHashtags(contenido);
        this.menciones = extraerMenciones(contenido);
    }
    
    public Publicacion(String username, String contenido, String rutaImagen) {
        if (contenido != null && contenido.length() > MAX_CARACTERES) {
            throw new IllegalArgumentException("El contenido no puede exceder " + MAX_CARACTERES + " caracteres");
        }
        
        this.id = UUID.randomUUID().toString();
        this.username = username;
        this.contenido = contenido;
        this.rutaImagen = rutaImagen;
        this.fechaHora = LocalDateTime.now();
        this.likes = new ArrayList<>();
        this.comentarios = new ArrayList<>();
        this.hashtags = extraerHashtags(contenido);
        this.menciones = extraerMenciones(contenido);
    }
    
    private ArrayList<String> extraerHashtags(String texto) {
        ArrayList<String> tags = new ArrayList<>();
        if (texto == null || texto.isEmpty()) {
            return tags;
        }
        
        String[] palabras = texto.split("\\s+");
        for (String palabra : palabras) {
            if (palabra.startsWith("#") && palabra.length() > 1) {
                tags.add(palabra.substring(1).toLowerCase());
            }
        }
        return tags;
    }
    
    private ArrayList<String> extraerMenciones(String texto) {
        ArrayList<String> mentions = new ArrayList<>();
        if (texto == null || texto.isEmpty()) {
            return mentions;
        }
        
        String[] palabras = texto.split("\\s+");
        for (String palabra : palabras) {
            if (palabra.startsWith("@") && palabra.length() > 1) {
                mentions.add(palabra.substring(1).toLowerCase());
            }
        }
        return mentions;
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
    
    public ArrayList<String> getLikes() {
        return new ArrayList<>(likes);
    }
    
    public void agregarComentario(Comentario comentario) {
        comentarios.add(comentario);
    }
    
    public ArrayList<Comentario> getComentarios() {
        return new ArrayList<>(comentarios);
    }
    
    public boolean eliminarComentario(String comentarioId) {
        return comentarios.removeIf(c -> c.getId().equals(comentarioId));
    }
    
    public boolean tieneHashtag(String hashtag) {
        return hashtags.contains(hashtag.toLowerCase());
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
        if (contenido != null && contenido.length() > MAX_CARACTERES) {
            throw new IllegalArgumentException("El contenido no puede exceder " + MAX_CARACTERES + " caracteres");
        }
        this.contenido = contenido;
        this.hashtags = extraerHashtags(contenido);
        this.menciones = extraerMenciones(contenido);
    }
    
    public String getRutaImagen() {
        return rutaImagen;
    }
    
    public void setRutaImagen(String rutaImagen) {
        this.rutaImagen = rutaImagen;
    }
    
    public LocalDateTime getFechaHora() {
        return fechaHora;
    }
    
    public LocalDateTime getFechaCreacion() {
        return fechaHora;
    }
    
    public ArrayList<String> getHashtags() {
        return new ArrayList<>(hashtags);
    }
    
    public ArrayList<String> getMenciones() {
        return new ArrayList<>(menciones);
    }
    
    @Override
    public String toString() {
        return "Publicacion{" +
                "id='" + id + '\'' +
                ", username='" + username + '\'' +
                ", contenido='" + contenido + '\'' +
                ", likes=" + likes.size() +
                ", comentarios=" + comentarios.size() +
                ", fechaHora=" + fechaHora +
                '}';
    }
}
