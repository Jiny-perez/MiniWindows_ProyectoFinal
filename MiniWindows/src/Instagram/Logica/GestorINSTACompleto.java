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
    private GestorUsuariosLocalINSTA gestorUsuarios;
    private GestorFollowingINSTA gestorFollowing;
    private GestorFollowersINSTA gestorFollowers;
    private GestorInstaINSTA gestorInsta;
    
    private static HashMap<String, GestorInstaINSTA> cacheInsta = new HashMap<>();
    
    public GestorINSTACompleto(GestorUsuariosLocalINSTA gestorUsuarios) {
        this.gestorUsuarios = gestorUsuarios;
        this.usuarioActual = null;
        this.gestorFollowing = null;
        this.gestorFollowers = null;
        this.gestorInsta = null;
    }
    
    public void setUsuarioActual(Usuario usuario) {
        this.usuarioActual = usuario;
        this.gestorFollowing = new GestorFollowingINSTA(usuario.getUsername());
        this.gestorFollowers = new GestorFollowersINSTA(usuario.getUsername());
        this.gestorInsta = new GestorInstaINSTA(usuario.getUsername());
        cacheInsta.put(usuario.getUsername(), gestorInsta);
    }
        
    public Publicacion crearPublicacion(String contenido) throws IllegalArgumentException {
        Publicacion publicacion = new Publicacion(usuarioActual.getUsername(), contenido);
        gestorInsta.agregarPublicacion(publicacion);
        propagarPublicacionASeguidores(publicacion);
        return publicacion;
    }
    
    public Publicacion crearPublicacion(String contenido, String rutaImagen) throws IllegalArgumentException {
        Publicacion publicacion = new Publicacion(usuarioActual.getUsername(), contenido, rutaImagen);
        gestorInsta.agregarPublicacion(publicacion);
        propagarPublicacionASeguidores(publicacion);
        return publicacion;
    }
    
    private void propagarPublicacionASeguidores(Publicacion publicacion) {
        ArrayList<String> seguidores = gestorFollowers.obtenerSeguidores();
        for (String seguidor : seguidores) {
            GestorInstaINSTA gestorSeguidor = obtenerGestorInsta(seguidor);
            gestorSeguidor.agregarPublicacion(publicacion);
        }
    }
    
    public boolean eliminarPublicacion(String publicacionId) {
        Publicacion pub = gestorInsta.buscarPorId(publicacionId);
        if (pub == null || !pub.getUsername().equals(usuarioActual.getUsername())) {
            return false;
        }
        
        gestorInsta.eliminarPublicacion(publicacionId);
        
        ArrayList<String> seguidores = gestorFollowers.obtenerSeguidores();
        for (String seguidor : seguidores) {
            GestorInstaINSTA gestorSeguidor = obtenerGestorInsta(seguidor);
            gestorSeguidor.eliminarPublicacion(publicacionId);
        }
        
        return true;
    }

    public ArrayList<Publicacion> obtenerTimeline() {
        ArrayList<Publicacion> base = gestorInsta.obtenerTimeline();
        ArrayList<Publicacion> filtrado = new ArrayList<>();
        
        for (Publicacion p : base) {
            Usuario autor = gestorUsuarios.obtenerUsuario(p.getUsername());
            // Si el autor no existe o está activo, se muestra.
            if (autor == null || autor.isActivo()) {
                filtrado.add(p);
            }
        }
        return filtrado;
    }
    
    public ArrayList<Publicacion> obtenerMisPublicaciones() {
        return gestorInsta.obtenerPublicacionesPropias();
    }
    
    public ArrayList<Publicacion> obtenerPublicacionesDeUsuario(String username) {
        GestorInstaINSTA gestor = obtenerGestorInsta(username);
        return gestor.obtenerPublicacionesPropias();
    }

    public void actualizarPublicacion(Publicacion publicacionActualizada) {
        if (publicacionActualizada == null) return;

        String autor = publicacionActualizada.getUsername();

        GestorInstaINSTA gestorAutor = obtenerGestorInsta(autor);
        gestorAutor.actualizarPublicacion(publicacionActualizada);

        GestorFollowersINSTA gestorFollowersAutor = new GestorFollowersINSTA(autor);
        ArrayList<String> seguidoresAutor = gestorFollowersAutor.obtenerSeguidores();
        for (String seguidor : seguidoresAutor) {
            GestorInstaINSTA gestorSeguidor = obtenerGestorInsta(seguidor);
            gestorSeguidor.actualizarPublicacion(publicacionActualizada);
        }
    }
        
    public boolean seguir(String username) {
        if (username.equals(usuarioActual.getUsername())) {
            return false;
        }
        
        boolean resultado = gestorFollowing.seguir(username);
        
        if (resultado) {
            GestorFollowersINSTA gestorSeguido = new GestorFollowersINSTA(username);
            gestorSeguido.agregarSeguidor(usuarioActual.getUsername());
            copiarPublicacionesDeUsuario(username);
        }
        
        return resultado;
    }
    
    public boolean dejarDeSeguir(String username) {
        boolean resultado = gestorFollowing.dejarDeSeguir(username);
        
        if (resultado) {
            GestorFollowersINSTA gestorSeguido = new GestorFollowersINSTA(username);
            gestorSeguido.eliminarSeguidor(usuarioActual.getUsername());
            gestorInsta.eliminarPublicacionesDeUsuario(username);
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
    
    private void copiarPublicacionesDeUsuario(String username) {
        GestorInstaINSTA gestorOtro = obtenerGestorInsta(username);
        ArrayList<Publicacion> publicacionesOtro = gestorOtro.obtenerPublicacionesPropias();
        
        for (Publicacion pub : publicacionesOtro) {
            gestorInsta.agregarPublicacion(pub);
        }
    }
        
    public ArrayList<Publicacion> buscarPorHashtag(String hashtag) {
        return gestorInsta.buscarPorHashtag(hashtag);
    }
        
    public EstadisticasUsuario obtenerEstadisticas(String username) {
        EstadisticasUsuario stats = new EstadisticasUsuario();
        stats.username = username;
        
        GestorInstaINSTA gestorUsuario = obtenerGestorInsta(username);
        GestorFollowingINSTA gestorFollowingUsuario = new GestorFollowingINSTA(username);
        GestorFollowersINSTA gestorFollowersUsuario = new GestorFollowersINSTA(username);
        
        stats.publicaciones = gestorUsuario.contarPublicacionesPropias();
        stats.seguidores = gestorFollowersUsuario.contarSeguidores();
        stats.siguiendo = gestorFollowingUsuario.contarSiguiendo();
        
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
        
    private GestorInstaINSTA obtenerGestorInsta(String username) {
        if (!cacheInsta.containsKey(username)) {
            cacheInsta.put(username, new GestorInstaINSTA(username));
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
        if (gestorFollowing != null) gestorFollowing.guardarFollowing();
        if (gestorFollowers != null) gestorFollowers.guardarFollowers();
        if (gestorInsta != null) gestorInsta.guardarPublicaciones();
    }
        
    public static class EstadisticasUsuario {
        public String username;
        public int publicaciones;
        public int seguidores;
        public int siguiendo;
        
        @Override
        public String toString() {
            return String.format(
                "@%s\n%d posts • %d seguidores • %d siguiendo",
                username, publicaciones, seguidores, siguiendo
            );
        }
    }
}