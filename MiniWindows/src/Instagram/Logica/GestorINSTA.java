/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Instagram.Logica;

import Instagram.Modelo.Publicacion;
import java.io.*;
import java.util.ArrayList;
import java.util.Comparator;

/**
 *
 * @author najma
 */
public class GestorINSTA {
    
    private String username;
    private ArrayList<Publicacion> publicaciones;
    
    public GestorINSTA(String username) {
        this.username = username;
        this.publicaciones = new ArrayList<>();
        cargarPublicaciones();
    }
    
    private void cargarPublicaciones() {
        String rutaArchivo = GestorArchivosUsuarioINSTA.getArchivoInsta(username);
        File archivo = new File(rutaArchivo);
        
        if (archivo.exists() && archivo.length() > 0) {
            try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(archivo))) {
                publicaciones = (ArrayList<Publicacion>) ois.readObject();
                System.out.println("✓ Publicaciones cargadas para " + username + ": " + publicaciones.size());
            } catch (Exception e) {
                System.err.println("Error al cargar insta.ins de " + username + ": " + e.getMessage());
                publicaciones = new ArrayList<>();
            }
        } else {
            publicaciones = new ArrayList<>();
        }
    }
    
    public void guardarPublicaciones() {
        String rutaArchivo = GestorArchivosUsuarioINSTA.getArchivoInsta(username);
        
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(rutaArchivo))) {
            oos.writeObject(publicaciones);
            System.out.println("✓ Publicaciones guardadas para " + username);
        } catch (Exception e) {
            System.err.println("Error al guardar insta.ins de " + username + ": " + e.getMessage());
        }
    }
    
    /**
     * Agregar publicación al timeline del usuario
     * Se usa cuando:
     * - El usuario crea una publicación propia
     * - Un usuario que sigo crea una publicación
     */
    public boolean agregarPublicacion(Publicacion publicacion) {
        if (publicacion == null) {
            return false;
        }
        
        // Evitar duplicados
        if (publicaciones.stream().anyMatch(p -> p.getId().equals(publicacion.getId()))) {
            return false;
        }
        
        publicaciones.add(publicacion);
        guardarPublicaciones();
        return true;
    }
    
    /**
     * Eliminar publicación del timeline
     */
    public boolean eliminarPublicacion(String publicacionId) {
        boolean resultado = publicaciones.removeIf(p -> p.getId().equals(publicacionId));
        if (resultado) {
            guardarPublicaciones();
        }
        return resultado;
    }
    
    /**
     * Obtener timeline ordenado (más reciente primero)
     */
    public ArrayList<Publicacion> obtenerTimeline() {
        ArrayList<Publicacion> timeline = new ArrayList<>(publicaciones);
        timeline.sort(Comparator.comparing(Publicacion::getFechaPublicacion).reversed());
        return timeline;
    }
    
    /**
     * Obtener solo publicaciones propias
     */
    public ArrayList<Publicacion> obtenerPublicacionesPropias() {
        ArrayList<Publicacion> propias = new ArrayList<>();
        for (Publicacion pub : publicaciones) {
            if (pub.getUsername().equals(username)) {
                propias.add(pub);
            }
        }
        propias.sort(Comparator.comparing(Publicacion::getFechaPublicacion).reversed());
        return propias;
    }
    
    /**
     * Buscar publicaciones que contengan un hashtag
     */
    public ArrayList<Publicacion> buscarPorHashtag(String hashtag) {
        ArrayList<Publicacion> resultados = new ArrayList<>();
        String hashtagLimpio = hashtag.startsWith("#") ? hashtag.substring(1) : hashtag;
        
        for (Publicacion pub : publicaciones) {
            if (pub.tieneHashtag(hashtagLimpio)) {
                resultados.add(pub);
            }
        }
        
        return resultados;
    }
    
    /**
     * Buscar publicación por ID
     */
    public Publicacion buscarPorId(String id) {
        return publicaciones.stream()
            .filter(p -> p.getId().equals(id))
            .findFirst()
            .orElse(null);
    }
    
    /**
     * Contar publicaciones propias
     */
    public int contarPublicacionesPropias() {
        return (int) publicaciones.stream()
            .filter(p -> p.getUsername().equals(username))
            .count();
    }
    
    /**
     * Eliminar todas las publicaciones de un usuario específico
     * Se usa cuando se deja de seguir a alguien
     */
    public void eliminarPublicacionesDeUsuario(String usernameEliminar) {
        publicaciones.removeIf(p -> p.getUsername().equals(usernameEliminar));
        guardarPublicaciones();
    }
}