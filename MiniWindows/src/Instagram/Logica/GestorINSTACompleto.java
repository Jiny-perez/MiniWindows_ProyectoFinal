/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Instagram.Logica;

import Instagram.Modelo.Usuario;
import Instagram.Modelo.Publicacion;
import java.util.ArrayList;
import java.util.HashMap;

/**
 *
 * @author najma
 */
public class GestorINSTACompleto {
   
    private Usuario usuarioActual;
    private GestorFollowingINSTA gestorFollowing;
    private GestorFollowersINSTA gestorFollowers;
    private GestorINSTA gestorInsta;
    
    // Cache de gestores para no recargar archivos constantemente
    private static HashMap<String, GestorINSTA> cacheInsta = new HashMap<>();
    
    public GestorINSTACompleto(Usuario usuarioActual) {
        this.usuarioActual = usuarioActual;
        
        // Cargar gestores del usuario actual
        this.gestorFollowing = new GestorFollowingINSTA(usuarioActual.getUsername());
        this.gestorFollowers = new GestorFollowersINSTA(usuarioActual.getUsername());
        this.gestorInsta = new GestorINSTA(usuarioActual.getUsername());
        
        cacheInsta.put(usuarioActual.getUsername(), gestorInsta);
    }
    
    // ═══════════════════════════════════════════════════════════════
    // PUBLICACIONES
    // ═══════════════════════════════════════════════════════════════
    
    /**
     * Crear publicación
     * - Guarda en mi insta.ins
     * - Propaga a insta.ins de mis seguidores
     */
    public Publicacion crearPublicacion(String contenido) throws IllegalArgumentException {
        Publicacion publicacion = new Publicacion(usuarioActual.getUsername(), contenido);
        
        // Agregar a mi timeline
        gestorInsta.agregarPublicacion(publicacion);
        
        // Propagar a timeline de mis seguidores
        propagarPublicacionASeguidores(publicacion);
        
        return publicacion;
    }
    
    public Publicacion crearPublicacion(String contenido, String rutaImagen) throws IllegalArgumentException {
        Publicacion publicacion = new Publicacion(usuarioActual.getUsername(), contenido, rutaImagen);
        
        // Agregar a mi timeline
        gestorInsta.agregarPublicacion(publicacion);
        
        // Propagar a timeline de mis seguidores
        propagarPublicacionASeguidores(publicacion);
        
        return publicacion;
    }
    
    /**
     * Propagar publicación a los insta.ins de mis seguidores
     */
    private void propagarPublicacionASeguidores(Publicacion publicacion) {
        ArrayList<String> seguidores = gestorFollowers.obtenerSeguidores();
        
        for (String seguidor : seguidores) {
            GestorINSTA gestorSeguidor = obtenerGestorInsta(seguidor);
            gestorSeguidor.agregarPublicacion(publicacion);
        }
        
        System.out.println("✓ Publicación propagada a " + seguidores.size() + " seguidores");
    }
    
    /**
     * Eliminar publicación
     * - Elimina de mi insta.ins
     * - Elimina de insta.ins de mis seguidores
     */
    public boolean eliminarPublicacion(String publicacionId) {
        // Solo puedo eliminar mis propias publicaciones
        Publicacion pub = gestorInsta.buscarPorId(publicacionId);
        if (pub == null || !pub.getUsername().equals(usuarioActual.getUsername())) {
            return false;
        }
        
        // Eliminar de mi timeline
        gestorInsta.eliminarPublicacion(publicacionId);
        
        // Eliminar de timeline de mis seguidores
        ArrayList<String> seguidores = gestorFollowers.obtenerSeguidores();
        for (String seguidor : seguidores) {
            GestorINSTA gestorSeguidor = obtenerGestorInsta(seguidor);
            gestorSeguidor.eliminarPublicacion(publicacionId);
        }
        
        return true;
    }
    
    /**
     * Obtener timeline (publicaciones de mi insta.ins ordenadas)
     */
    public ArrayList<Publicacion> obtenerTimeline() {
        return gestorInsta.obtenerTimeline();
    }
    
    /**
     * Obtener solo mis publicaciones
     */
    public ArrayList<Publicacion> obtenerMisPublicaciones() {
        return gestorInsta.obtenerPublicacionesPropias();
    }
    
    /**
     * Obtener publicaciones de un usuario específico
     */
    public ArrayList<Publicacion> obtenerPublicacionesDeUsuario(String username) {
        GestorINSTA gestor = obtenerGestorInsta(username);
        return gestor.obtenerPublicacionesPropias();
    }
    
    // ═══════════════════════════════════════════════════════════════
    // SEGUIR / DEJAR DE SEGUIR
    // ═══════════════════════════════════════════════════════════════
    
    /**
     * Seguir a un usuario
     * - Agregar a mi following.ins
     * - Agregar mi username a su followers.ins
     * - Copiar sus publicaciones a mi insta.ins
     */
    public boolean seguir(String username) {
        if (username.equals(usuarioActual.getUsername())) {
            return false;
        }
        
        // Agregar a mi lista de siguiendo
        boolean resultado = gestorFollowing.seguir(username);
        
        if (resultado) {
            // Agregar mi username a su lista de seguidores
            GestorFollowersINSTA gestorSeguido = new GestorFollowersINSTA(username);
            gestorSeguido.agregarSeguidor(usuarioActual.getUsername());
            
            // Copiar sus publicaciones a mi timeline
            copiarPublicacionesDeUsuario(username);
            
            System.out.println("✓ Ahora sigues a @" + username);
        }
        
        return resultado;
    }
    
    /**
     * Dejar de seguir a un usuario
     * - Eliminar de mi following.ins
     * - Eliminar mi username de su followers.ins
     * - Eliminar sus publicaciones de mi insta.ins
     */
    public boolean dejarDeSeguir(String username) {
        boolean resultado = gestorFollowing.dejarDeSeguir(username);
        
        if (resultado) {
            // Eliminar mi username de su lista de seguidores
            GestorFollowersINSTA gestorSeguido = new GestorFollowersINSTA(username);
            gestorSeguido.eliminarSeguidor(usuarioActual.getUsername());
            
            // Eliminar sus publicaciones de mi timeline
            gestorInsta.eliminarPublicacionesDeUsuario(username);
            
            System.out.println("✓ Dejaste de seguir a @" + username);
        }
        
        return resultado;
    }
    
    public boolean toggleSeguir(String username) {
        if (gestorFollowing.estaSiguiendo(username)) {
            return dejarDeSeguir(username);
        } else {
            return seguir(username);
        }
    }
    
    public boolean estaSiguiendo(String username) {
        return gestorFollowing.estaSiguiendo(username);
    }
    
    /**
     * Copiar publicaciones de un usuario a mi timeline
     * Se usa cuando empiezo a seguir a alguien
     */
    private void copiarPublicacionesDeUsuario(String username) {
        GestorINSTA gestorOtro = obtenerGestorInsta(username);
        ArrayList<Publicacion> publicacionesOtro = gestorOtro.obtenerPublicacionesPropias();
        
        for (Publicacion pub : publicacionesOtro) {
            gestorInsta.agregarPublicacion(pub);
        }
        
        System.out.println("✓ Copiadas " + publicacionesOtro.size() + " publicaciones de @" + username);
    }
    
    // ═══════════════════════════════════════════════════════════════
    // BÚSQUEDA
    // ═══════════════════════════════════════════════════════════════
    
    public ArrayList<Publicacion> buscarPorHashtag(String hashtag) {
        return gestorInsta.buscarPorHashtag(hashtag);
    }
    
    // ═══════════════════════════════════════════════════════════════
    // ESTADÍSTICAS
    // ═══════════════════════════════════════════════════════════════
    
    public EstadisticasUsuario obtenerEstadisticas(String username) {
        EstadisticasUsuario stats = new EstadisticasUsuario();
        stats.username = username;
        
        GestorINSTA gestorUsuario = obtenerGestorInsta(username);
        GestorFollowingINSTA gestorFollowingUsuario = new GestorFollowingINSTA(username);
        GestorFollowersINSTA gestorFollowersUsuario = new GestorFollowersINSTA(username);
        
        stats.cantidadPublicaciones = gestorUsuario.contarPublicacionesPropias();
        stats.cantidadSeguidores = gestorFollowersUsuario.contarSeguidores();
        stats.cantidadSiguiendo = gestorFollowingUsuario.contarSiguiendo();
        
        return stats;
    }
    
    public EstadisticasUsuario obtenerMisEstadisticas() {
        return obtenerEstadisticas(usuarioActual.getUsername());
    }
    
    public ArrayList<String> obtenerSeguidores(String username) {
        GestorFollowersINSTA gestor = new GestorFollowersINSTA(username);
        return gestor.obtenerSeguidores();
    }
    
    public ArrayList<String> obtenerSiguiendo(String username) {
        GestorFollowingINSTA gestor = new GestorFollowingINSTA(username);
        return gestor.obtenerSiguiendo();
    }
    
    // ═══════════════════════════════════════════════════════════════
    // UTILIDADES
    // ═══════════════════════════════════════════════════════════════
    
    private GestorINSTA obtenerGestorInsta(String username) {
        if (!cacheInsta.containsKey(username)) {
            cacheInsta.put(username, new GestorINSTA(username));
        }
        return cacheInsta.get(username);
    }
    
    public String getUsernameActual() {
        return usuarioActual.getUsername();
    }
    
    public Usuario getUsuarioActual() {
        return usuarioActual;
    }
    
    public void guardarDatos() {
        gestorFollowing.guardarFollowing();
        gestorFollowers.guardarFollowers();
        gestorInsta.guardarPublicaciones();
        System.out.println("✓ Datos guardados para @" + usuarioActual.getUsername());
    }
    
    // ═══════════════════════════════════════════════════════════════
    // CLASE INTERNA - ESTADÍSTICAS
    // ═══════════════════════════════════════════════════════════════
    
    public static class EstadisticasUsuario {
        public String username;
        public int cantidadPublicaciones;
        public int cantidadSeguidores;
        public int cantidadSiguiendo;
        public int likesRecibidos;
        
        @Override
        public String toString() {
            return String.format(
                "@%s\n%d posts • %d seguidores • %d siguiendo",
                username, cantidadPublicaciones, cantidadSeguidores, cantidadSiguiendo
            );
        }
    }
}
