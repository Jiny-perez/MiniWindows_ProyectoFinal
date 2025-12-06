/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Instagram.Logica;

import Modelo.Usuario;
import Instagram.Modelo.*;
import java.io.*;
import java.util.ArrayList;

/**
 *
 * @author najma
 */
public class GestorINSTA {
    
    private static final String ARCHIVO_DATOS = "insta.sop";
    
    private GestorPublicaciones gestorPublicaciones;
    private GestorSeguidores gestorSeguidores;
    private Usuario usuarioActual;
    
    public GestorINSTA(Usuario usuarioActual) {
        this.usuarioActual = usuarioActual;
        this.gestorPublicaciones = new GestorPublicaciones();
        this.gestorSeguidores = new GestorSeguidores();
        
        cargarDatos();
    }
    
    public boolean guardarDatos() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(ARCHIVO_DATOS))) {
            
            DatosINSTA datos = new DatosINSTA();
            datos.publicaciones = gestorPublicaciones.getPublicaciones();
            datos.relaciones = gestorSeguidores.getRelaciones();
            
            oos.writeObject(datos);
            return true;
            
        } catch (IOException e) {
            System.err.println("Error al guardar datos de INSTA: " + e.getMessage());
            return false;
        }
    }
    
    private void cargarDatos() {
        File archivo = new File(ARCHIVO_DATOS);
        
        if (!archivo.exists()) {
            System.out.println("Archivo de INSTA no existe. Iniciando con datos vacíos.");
            return;
        }
        
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(ARCHIVO_DATOS))) {
            
            DatosINSTA datos = (DatosINSTA) ois.readObject();
            
            gestorPublicaciones.setPublicaciones(datos.publicaciones);
            gestorSeguidores.setRelaciones(datos.relaciones);
            
            System.out.println("Datos de INSTA cargados correctamente.");
            System.out.println("- Publicaciones: " + datos.publicaciones.size());
            System.out.println("- Relaciones: " + datos.relaciones.size());
            
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Error al cargar datos de INSTA: " + e.getMessage());
        }
    }
    
    public Publicacion crearPublicacion(String contenido) {
        Publicacion post = gestorPublicaciones.crearPublicacion(usuarioActual.getUsername(), contenido);
        guardarDatos();
        return post;
    }
    
    public Publicacion crearPublicacion(String contenido, String rutaImagen) {
        Publicacion post = gestorPublicaciones.crearPublicacion(usuarioActual.getUsername(), contenido, rutaImagen);
        guardarDatos();
        return post;
    }
    
    public boolean eliminarPublicacion(String postId) {
        boolean resultado = gestorPublicaciones.eliminarPublicacion(postId, usuarioActual.getUsername());
        if (resultado) {
            guardarDatos();
        }
        return resultado;
    }
    
    public boolean toggleLike(String postId) {
        boolean resultado = gestorPublicaciones.toggleLike(postId, usuarioActual.getUsername());
        if (resultado) {
            guardarDatos();
        }
        return resultado;
    }
    
    public Comentario agregarComentario(String postId, String contenido) {
        Comentario comentario = gestorPublicaciones.agregarComentario(postId, usuarioActual.getUsername(), contenido);
        if (comentario != null) {
            guardarDatos();
        }
        return comentario;
    }
    
    public boolean eliminarComentario(String postId, String comentarioId) {
        boolean resultado = gestorPublicaciones.eliminarComentario(postId, comentarioId, usuarioActual.getUsername());
        if (resultado) {
            guardarDatos();
        }
        return resultado;
    }
    
    public boolean seguir(String username) {
        boolean resultado = gestorSeguidores.seguir(usuarioActual.getUsername(), username);
        if (resultado) {
            guardarDatos();
        }
        return resultado;
    }
    
    public boolean dejarDeSeguir(String username) {
        boolean resultado = gestorSeguidores.dejarDeSeguir(usuarioActual.getUsername(), username);
        if (resultado) {
            guardarDatos();
        }
        return resultado;
    }
    
    public boolean toggleSeguir(String username) {
        boolean resultado = gestorSeguidores.toggleSeguir(usuarioActual.getUsername(), username);
        guardarDatos();
        return resultado;
    }
    
    public boolean estaSiguiendo(String username) {
        return gestorSeguidores.estaSiguiendo(usuarioActual.getUsername(), username);
    }
    
    public ArrayList<Publicacion> obtenerTimeline() {
        ArrayList<String> siguiendo = gestorSeguidores.obtenerSiguiendo(usuarioActual.getUsername());
        return gestorPublicaciones.obtenerTimeline(usuarioActual.getUsername(), siguiendo);
    }
    
    public ArrayList<Publicacion> obtenerPublicacionesDeUsuario(String username) {
        return gestorPublicaciones.obtenerPublicacionesDeUsuario(username);
    }
    
    public ArrayList<Publicacion> obtenerMisPublicaciones() {
        return gestorPublicaciones.obtenerPublicacionesDeUsuario(usuarioActual.getUsername());
    }
    
    public ArrayList<Publicacion> buscarPublicaciones(String texto) {
        return gestorPublicaciones.buscarPorContenido(texto);
    }
    
    public ArrayList<Publicacion> buscarPorHashtag(String hashtag) {
        return gestorPublicaciones.buscarPorHashtag(hashtag);
    }
    
    public EstadisticasUsuario obtenerEstadisticas(String username) {
        EstadisticasUsuario stats = new EstadisticasUsuario();
        stats.username = username;
        stats.cantidadPublicaciones = gestorPublicaciones.contarPublicacionesDeUsuario(username);
        stats.cantidadSeguidores = gestorSeguidores.contarSeguidores(username);
        stats.cantidadSiguiendo = gestorSeguidores.contarSiguiendo(username);
        stats.likesRecibidos = gestorPublicaciones.contarLikesRecibidos(username);
        return stats;
    }
    
    public EstadisticasUsuario obtenerMisEstadisticas() {
        return obtenerEstadisticas(usuarioActual.getUsername());
    }
    
    public ArrayList<String> obtenerSeguidores(String username) {
        return gestorSeguidores.obtenerSeguidores(username);
    }
    
    public ArrayList<String> obtenerSiguiendo(String username) {
        return gestorSeguidores.obtenerSiguiendo(username);
    }
    
    public ArrayList<String> sugerirUsuarios(ArrayList<String> todosLosUsuarios) {
        return gestorSeguidores.sugerirUsuarios(usuarioActual.getUsername(), todosLosUsuarios);
    }
    
    public GestorPublicaciones getGestorPublicaciones() {
        return gestorPublicaciones;
    }
    
    public GestorSeguidores getGestorSeguidores() {
        return gestorSeguidores;
    }
    
    public Usuario getUsuarioActual() {
        return usuarioActual;
    }
    
    public String getUsernameActual() {
        return usuarioActual.getUsername();
    }
    
    private static class DatosINSTA implements Serializable {
        private static final long serialVersionUID = 1L;
        
        ArrayList<Publicacion> publicaciones;
        ArrayList<Seguidor> relaciones;
    }
    
    public static class EstadisticasUsuario {
        public String username;
        public int cantidadPublicaciones;
        public int cantidadSeguidores;
        public int cantidadSiguiendo;
        public int likesRecibidos;
        
        @Override
        public String toString() {
            return String.format(
                "@%s\n%d posts • %d seguidores • %d siguiendo\n%d likes totales",
                username, cantidadPublicaciones, cantidadSeguidores, 
                cantidadSiguiendo, likesRecibidos
            );
        }
    }
}