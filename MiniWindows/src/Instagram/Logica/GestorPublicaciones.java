/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Instagram.Logica;

import Instagram.Modelo.Comentario;
import Instagram.Modelo.Publicacion;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.stream.Collectors;

/**
 *
 * @author najma
 */
public class GestorPublicaciones {
    
    private ArrayList<Publicacion> publicaciones;
    
    public GestorPublicaciones() {
        this.publicaciones = new ArrayList<>();
    }
    
    public Publicacion crearPublicacion(String username, String contenido) {
        Publicacion post = new Publicacion(username, contenido);
        publicaciones.add(post);
        return post;
    }
    
    public Publicacion crearPublicacion(String username, String contenido, String rutaImagen) {
        Publicacion post = new Publicacion(username, contenido, rutaImagen);
        publicaciones.add(post);
        return post;
    }

    public boolean eliminarPublicacion(String postId, String username) {
        Publicacion post = buscarPorId(postId);
        
        if (post != null && post.getUsername().equals(username)) {
            return publicaciones.remove(post);
        }
        
        return false;
    }
    
    public boolean editarPublicacion(String postId, String username, String nuevoContenido) {
        Publicacion post = buscarPorId(postId);
        
        if (post != null && post.getUsername().equals(username)) {
            post.setContenido(nuevoContenido);
            return true;
        }
        
        return false;
    }
    
    public boolean darLike(String postId, String username) {
        Publicacion post = buscarPorId(postId);
        
        if (post != null) {
            return post.darLike(username);
        }
        
        return false;
    }
    
    public boolean quitarLike(String postId, String username) {
        Publicacion post = buscarPorId(postId);
        
        if (post != null) {
            return post.quitarLike(username);
        }
        
        return false;
    }
    
    public boolean toggleLike(String postId, String username) {
        Publicacion post = buscarPorId(postId);
        
        if (post != null) {
            if (post.tieneLikeDe(username)) {
                return post.quitarLike(username);
            } else {
                return post.darLike(username);
            }
        }
        
        return false;
    }
    
    public Comentario agregarComentario(String postId, String username, String contenido) {
        Publicacion post = buscarPorId(postId);
        
        if (post != null) {
            Comentario comentario = new Comentario(username, contenido, postId);
            post.agregarComentario(comentario);
            return comentario;
        }
        
        return null;
    }
    
    public boolean eliminarComentario(String postId, String comentarioId, String username) {
        Publicacion post = buscarPorId(postId);
        
        if (post != null) {
            
            Comentario comentario = post.getComentarios().stream()
                .filter(c -> c.getId().equals(comentarioId))
                .findFirst()
                .orElse(null);
            
            if (comentario != null) {
                if (comentario.getUsername().equals(username) || post.getUsername().equals(username)) {
                    return post.eliminarComentario(comentarioId);
                }
            }
        }
        
        return false;
    }
    
    public Publicacion buscarPorId(String postId) {
        return publicaciones.stream()
            .filter(p -> p.getId().equals(postId))
            .findFirst()
            .orElse(null);
    }
    
    public ArrayList<Publicacion> obtenerPublicacionesDeUsuario(String username) {
        return publicaciones.stream()
            .filter(p -> p.getUsername().equals(username))
            .sorted(Comparator.comparing(Publicacion::getFechaCreacion).reversed())
            .collect(Collectors.toCollection(ArrayList::new));
    }
    
    public ArrayList<Publicacion> obtenerTimeline(String username, ArrayList<String> siguiendo) {
        
        ArrayList<String> usuariosIncluidos = new ArrayList<>(siguiendo);
        if (!usuariosIncluidos.contains(username)) {
            usuariosIncluidos.add(username);
        }
        
        return publicaciones.stream()
            .filter(p -> usuariosIncluidos.contains(p.getUsername()))
            .sorted(Comparator.comparing(Publicacion::getFechaCreacion).reversed())
            .collect(Collectors.toCollection(ArrayList::new));
    }
    
    public ArrayList<Publicacion> buscarPorHashtag(String hashtag) {
        String hashtagLimpio = hashtag.startsWith("#") ? hashtag.substring(1) : hashtag;
        
        return publicaciones.stream()
            .filter(p -> p.tieneHashtag(hashtagLimpio))
            .sorted(Comparator.comparing(Publicacion::getFechaCreacion).reversed())
            .collect(Collectors.toCollection(ArrayList::new));
    }
    
    public ArrayList<Publicacion> buscarPorContenido(String texto) {
        String textoLower = texto.toLowerCase();
        
        return publicaciones.stream()
            .filter(p -> p.getContenido().toLowerCase().contains(textoLower) ||
                        p.getUsername().toLowerCase().contains(textoLower))
            .sorted(Comparator.comparing(Publicacion::getFechaCreacion).reversed())
            .collect(Collectors.toCollection(ArrayList::new));
    }
    
    public ArrayList<Publicacion> obtenerRecientes(int cantidad) {
        return publicaciones.stream()
            .sorted(Comparator.comparing(Publicacion::getFechaCreacion).reversed())
            .limit(cantidad)
            .collect(Collectors.toCollection(ArrayList::new));
    }
    
    public int contarPublicacionesDeUsuario(String username) {
        return (int) publicaciones.stream()
            .filter(p -> p.getUsername().equals(username))
            .count();
    }
    
    public int contarLikesRecibidos(String username) {
        return publicaciones.stream()
            .filter(p -> p.getUsername().equals(username))
            .mapToInt(Publicacion::getCantidadLikes)
            .sum();
    }
    
    public ArrayList<Publicacion> obtenerMasPopulares(int cantidad) {
        return publicaciones.stream()
            .sorted(Comparator.comparing(Publicacion::getCantidadLikes).reversed())
            .limit(cantidad)
            .collect(Collectors.toCollection(ArrayList::new));
    }
    
    public ArrayList<Publicacion> getPublicaciones() {
        return new ArrayList<>(publicaciones);
    }
    
    public int getTotalPublicaciones() {
        return publicaciones.size();
    }
    
    public void setPublicaciones(ArrayList<Publicacion> publicaciones) {
        this.publicaciones = publicaciones;
    }
}