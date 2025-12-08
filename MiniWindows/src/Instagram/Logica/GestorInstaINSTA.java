/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Instagram.Logica;

import Instagram.Modelo.Publicacion;
import java.io.*;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.stream.Collectors;

/**
 *
 * @author najma
 */
public class GestorInstaINSTA {
    
    private String username;
    private ArrayList<Publicacion> timeline;
    private String archivoInsta;
    
    public GestorInstaINSTA(String username) {
        this.username = username;
        this.archivoInsta = GestorArchivosUsuarioINSTA.getArchivoInsta(username);
        this.timeline = new ArrayList<>();
        cargarPublicaciones();
    }
    
    public void agregarPublicacion(Publicacion publicacion) {
        if (!contienePublicacion(publicacion.getId())) {
            timeline.add(publicacion);
            guardarPublicaciones();
        }
    }
    
    public boolean eliminarPublicacion(String publicacionId) {
        boolean eliminado = timeline.removeIf(p -> p.getId().equals(publicacionId));
        if (eliminado) {
            guardarPublicaciones();
        }
        return eliminado;
    }
    
    public void eliminarPublicacionesDeUsuario(String username) {
        boolean cambios = timeline.removeIf(p -> p.getUsername().equals(username));
        if (cambios) {
            guardarPublicaciones();
        }
    }
    
    public Publicacion buscarPorId(String publicacionId) {
        for (Publicacion pub : timeline) {
            if (pub.getId().equals(publicacionId)) {
                return pub;
            }
        }
        return null;
    }
    
    private boolean contienePublicacion(String publicacionId) {
        return timeline.stream().anyMatch(p -> p.getId().equals(publicacionId));
    }
    
    public ArrayList<Publicacion> obtenerTimeline() {
        ArrayList<Publicacion> timelineOrdenado = new ArrayList<>(timeline);
        timelineOrdenado.sort(Comparator.comparing(Publicacion::getFechaHora).reversed());
        return timelineOrdenado;
    }
    
    public ArrayList<Publicacion> obtenerPublicacionesPropias() {
        return timeline.stream()
                .filter(p -> p.getUsername().equals(username))
                .sorted(Comparator.comparing(Publicacion::getFechaHora).reversed())
                .collect(Collectors.toCollection(ArrayList::new));
    }
    
    public int contarPublicacionesPropias() {
        return (int) timeline.stream()
                .filter(p -> p.getUsername().equals(username))
                .count();
    }
    
    public ArrayList<Publicacion> buscarPorHashtag(String hashtag) {
        return timeline.stream()
                .filter(p -> p.tieneHashtag(hashtag))
                .sorted(Comparator.comparing(Publicacion::getFechaHora).reversed())
                .collect(Collectors.toCollection(ArrayList::new));
    }

    // ===== NUEVO: actualizar una publicación existente en el timeline =====
    public void actualizarPublicacion(Publicacion publicacionActualizada) {
        if (publicacionActualizada == null) return;

        boolean cambios = false;

        for (int i = 0; i < timeline.size(); i++) {
            Publicacion p = timeline.get(i);
            if (p.getId().equals(publicacionActualizada.getId())) {
                timeline.set(i, publicacionActualizada);
                cambios = true;
                break;
            }
        }

        if (cambios) {
            guardarPublicaciones();
        }
    }
    // =======================================================================
    
    @SuppressWarnings("unchecked")
    private void cargarPublicaciones() {
        File archivo = new File(archivoInsta);
        
        if (archivo.exists()) {
            try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(archivo))) {
                timeline = (ArrayList<Publicacion>) ois.readObject();
                System.out.println("✓ Timeline cargado para @" + username + ": " + timeline.size() + " publicaciones");
            } catch (IOException | ClassNotFoundException e) {
                System.err.println("Error al cargar timeline de " + username + ": " + e.getMessage());
                timeline = new ArrayList<>();
            }
        } else {
            System.out.println("⚠ Archivo insta.ins no existe para @" + username + ", se creará al guardar");
        }
    }
    
    public void guardarPublicaciones() {
        File archivo = new File(archivoInsta);
        archivo.getParentFile().mkdirs();
        
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(archivo))) {
            oos.writeObject(timeline);
            System.out.println("✓ Timeline guardado para @" + username + ": " + timeline.size() + " publicaciones");
        } catch (IOException e) {
            System.err.println("Error al guardar timeline de " + username + ": " + e.getMessage());
        }
    }
    
    public int getTotalPublicaciones() {
        return timeline.size();
    }
    
    public String getUsername() {
        return username;
    }
}