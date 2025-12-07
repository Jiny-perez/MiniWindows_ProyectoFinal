/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Instagram.Logica;

import Instagram.Modelo.Usuario;
import Instagram.Modelo.*;
import java.io.*;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author najma
 */
public class GestorINSTA {
    
    private static final String ARCHIVO_DATOS = "insta.sop";
    
    private GestorPublicaciones gestorPublicaciones;
    private GestorSeguidores gestorSeguidores;
    private GestorNotificaciones gestorNotificaciones;
    private Usuario usuarioActual;
    
    public GestorINSTA(Usuario usuarioActual) {
        this.usuarioActual = usuarioActual;
        this.gestorPublicaciones = new GestorPublicaciones();
        this.gestorSeguidores = new GestorSeguidores();
        this.gestorNotificaciones = new GestorNotificaciones();
        
        cargarDatos();
    }
    
    public boolean guardarDatos() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(ARCHIVO_DATOS))) {
            
            DatosINSTA datos = new DatosINSTA();
            datos.publicaciones = gestorPublicaciones.getPublicaciones();
            datos.relaciones = gestorSeguidores.getRelaciones();
            
            oos.writeObject(datos);
            System.out.println("Datos de INSTA guardados correctamente");
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
        
        procesarMenciones(post);
        
        guardarDatos();
        GestorPerfiles.incrementarPublicaciones(usuarioActual.getUsername());
        
        return post;
    }
    
    public Publicacion crearPublicacion(String contenido, String rutaImagen) {
        Publicacion post = gestorPublicaciones.crearPublicacion(usuarioActual.getUsername(), contenido, rutaImagen);
        
        procesarMenciones(post);
        
        guardarDatos();
        GestorPerfiles.incrementarPublicaciones(usuarioActual.getUsername());
        
        return post;
    }
    
    private void procesarMenciones(Publicacion post) {
        ArrayList<String> menciones = extraerMenciones(post.getContenido());
        
        for (String usernameMencionado : menciones) {
            if (!usernameMencionado.equals(usuarioActual.getUsername())) {
                gestorNotificaciones.crearNotificacionMencion(
                    usuarioActual.getUsername(),
                    usernameMencionado,
                    post.getId(),
                    post.getContenido()
                );
            }
        }
    }
    
    private ArrayList<String> extraerMenciones(String texto) {
        ArrayList<String> menciones = new ArrayList<>();
        Pattern pattern = Pattern.compile("@([a-zA-Z0-9_]+)");
        Matcher matcher = pattern.matcher(texto);
        
        while (matcher.find()) {
            String username = matcher.group(1);
            if (!menciones.contains(username)) {
                menciones.add(username);
            }
        }
        
        return menciones;
    }
    
    public boolean eliminarPublicacion(String postId) {
        Publicacion post = gestorPublicaciones.buscarPorId(postId);
        if (post == null) {
            return false;
        }
        
        if (post.tieneImagen()) {
            GestorArchivosUsuario.eliminarImagen(post.getRutaImagen());
        }
        
        boolean resultado = gestorPublicaciones.eliminarPublicacion(postId, usuarioActual.getUsername());
        
        if (resultado) {
            guardarDatos();
            GestorPerfiles.decrementarPublicaciones(usuarioActual.getUsername());
        }
        
        return resultado;
    }
    
    public boolean toggleLike(String postId) {
        Publicacion post = gestorPublicaciones.buscarPorId(postId);
        if (post == null) {
            return false;
        }
        
        boolean yaLeDioLike = post.tieneLikeDe(usuarioActual.getUsername());
        boolean resultado = gestorPublicaciones.toggleLike(postId, usuarioActual.getUsername());
        
        if (resultado) {
            if (!yaLeDioLike) {
                gestorNotificaciones.crearNotificacionLike(
                    usuarioActual.getUsername(),
                    post.getUsername(),
                    postId
                );
            } else {
                gestorNotificaciones.eliminarNotificacionLike(
                    usuarioActual.getUsername(),
                    post.getUsername(),
                    postId
                );
            }
            
            guardarDatos();
        }
        
        return resultado;
    }
    
    public Comentario agregarComentario(String postId, String contenido) {
        Publicacion post = gestorPublicaciones.buscarPorId(postId);
        if (post == null) {
            return null;
        }
        
        Comentario comentario = gestorPublicaciones.agregarComentario(
            postId, 
            usuarioActual.getUsername(), 
            contenido
        );
        
        if (comentario != null) {
            gestorNotificaciones.crearNotificacionComentario(
                usuarioActual.getUsername(),
                post.getUsername(),
                postId,
                contenido
            );
            
            ArrayList<String> menciones = extraerMenciones(contenido);
            for (String usernameMencionado : menciones) {
                if (!usernameMencionado.equals(usuarioActual.getUsername())) {
                    gestorNotificaciones.crearNotificacionMencion(
                        usuarioActual.getUsername(),
                        usernameMencionado,
                        postId,
                        contenido
                    );
                }
            }
            
            guardarDatos();
        }
        
        return comentario;
    }
    
    public boolean eliminarComentario(String postId, String comentarioId) {
        boolean resultado = gestorPublicaciones.eliminarComentario(
            postId, 
            comentarioId, 
            usuarioActual.getUsername()
        );
        
        if (resultado) {
            guardarDatos();
        }
        
        return resultado;
    }
    
    public boolean seguir(String username) {
        boolean resultado = gestorSeguidores.seguir(usuarioActual.getUsername(), username);
        
        if (resultado) {
            gestorNotificaciones.crearNotificacionSeguidor(
                usuarioActual.getUsername(),
                username
            );
            
            GestorPerfiles.incrementarSeguidos(usuarioActual.getUsername());
            GestorPerfiles.incrementarSeguidores(username);
            
            guardarDatos();
        }
        
        return resultado;
    }
    
    public boolean dejarDeSeguir(String username) {
        boolean resultado = gestorSeguidores.dejarDeSeguir(usuarioActual.getUsername(), username);
        
        if (resultado) {
            gestorNotificaciones.eliminarNotificacionSeguidor(
                usuarioActual.getUsername(),
                username
            );
            
            GestorPerfiles.decrementarSeguidos(usuarioActual.getUsername());
            GestorPerfiles.decrementarSeguidores(username);
            
            guardarDatos();
        }
        
        return resultado;
    }
    
    public boolean toggleSeguir(String username) {
        boolean estabaSiguiendo = gestorSeguidores.estaSiguiendo(usuarioActual.getUsername(), username);
        
        if (estabaSiguiendo) {
            return dejarDeSeguir(username);
        } else {
            return seguir(username);
        }
    }
    
    public boolean estaSiguiendo(String username) {
        return gestorSeguidores.estaSiguiendo(usuarioActual.getUsername(), username);
    }
    
    public ArrayList<Publicacion> obtenerTimeline() {
        ArrayList<String> siguiendo = gestorSeguidores.obtenerSiguiendo(usuarioActual.getUsername());
        ArrayList<Publicacion> timeline = gestorPublicaciones.obtenerTimeline(usuarioActual.getUsername(), siguiendo);
        
        ArrayList<Publicacion> timelineFiltrado = new ArrayList<>();
        GestorUsuariosLocal gestorUsuarios = new GestorUsuariosLocal();
        
        for (Publicacion pub : timeline) {
            Usuario usuario = gestorUsuarios.obtenerUsuario(pub.getUsername());
            if (usuario != null && usuario.isActivo()) {
                timelineFiltrado.add(pub);
            }
        }
        
        return timelineFiltrado;
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
    
    public ArrayList<Publicacion> obtenerPublicacionesConMenciones() {
        ArrayList<Publicacion> todasLasPublicaciones = gestorPublicaciones.getPublicaciones();
        ArrayList<Publicacion> conMenciones = new ArrayList<>();
        
        String mencionBuscada = "@" + usuarioActual.getUsername();
        
        for (Publicacion pub : todasLasPublicaciones) {
            if (pub.getContenido().contains(mencionBuscada)) {
                conMenciones.add(pub);
            }
            
            for (Comentario com : pub.getComentarios()) {
                if (com.getContenido().contains(mencionBuscada) && !conMenciones.contains(pub)) {
                    conMenciones.add(pub);
                    break;
                }
            }
        }
        
        return conMenciones;
    }
    
    public ArrayList<Notificacion> obtenerNotificaciones() {
        return gestorNotificaciones.obtenerNotificaciones(usuarioActual.getUsername());
    }
    
    public int contarNotificacionesNoLeidas() {
        return gestorNotificaciones.contarNoLeidas(usuarioActual.getUsername());
    }
    
    public void marcarNotificacionesComoLeidas() {
        gestorNotificaciones.marcarTodasComoLeidas(usuarioActual.getUsername());
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
    
    public GestorNotificaciones getGestorNotificaciones() {
        return gestorNotificaciones;
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
