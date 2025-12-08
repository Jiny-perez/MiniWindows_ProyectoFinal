/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Instagram.Logica;

import Instagram.Modelo.PerfilUsuario;
import java.io.*;

/**
 *
 * @author najma
 */
public class GestorPerfiles {

    public static PerfilUsuario cargarPerfil(String username) {
        String rutaArchivo = GestorArchivosUsuarioINSTA.getArchivoPerfil(username);
        File archivo = new File(rutaArchivo);
        
        if (archivo.exists()) {
            try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(archivo))) {
                PerfilUsuario perfil = (PerfilUsuario) ois.readObject();
                System.out.println("✓ Perfil cargado: " + username);
                return perfil;
            } catch (Exception e) {
                System.err.println("Error al cargar perfil de " + username + ": " + e.getMessage());
            }
        }
        
        System.out.println("○ Creando perfil nuevo para: " + username);
        return new PerfilUsuario(username);
    }

    public static boolean guardarPerfil(PerfilUsuario perfil) {
        if (perfil == null) {
            System.err.println("Error: perfil es null");
            return false;
        }
        
        String username = perfil.getUsername();
        
        if (!GestorArchivosUsuario.existeDirectorioUsuario(username)) {
            GestorArchivosUsuario.crearDirectorioUsuario(username);
        }
        
        String rutaArchivo = GestorArchivosUsuario.getArchivoPerfil(username);
        
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(rutaArchivo))) {
            oos.writeObject(perfil);
            System.out.println("✓ Perfil guardado: " + username);
            return true;
        } catch (Exception e) {
            System.err.println("Error al guardar perfil de " + username + ": " + e.getMessage());
            return false;
        }
    }
    
    public static boolean actualizarAvatar(String username, String rutaImagen) {
        PerfilUsuario perfil = cargarPerfil(username);
        
        if (rutaImagen != null && !rutaImagen.isEmpty()) {
            String extension = GestorArchivosUsuario.obtenerExtension(rutaImagen);
            String nombreAvatar = "avatar" + extension;
            
            String rutaNueva = GestorArchivosUsuario.copiarImagenUsuario(username, rutaImagen, nombreAvatar);
            
            if (rutaNueva != null) {
                perfil.setRutaAvatar(rutaNueva);
                return guardarPerfil(perfil);
            }
        } else {
            perfil.setRutaAvatar(null);
            return guardarPerfil(perfil);
        }
        
        return false;
    }

    public static boolean actualizarBiografia(String username, String biografia) {
        PerfilUsuario perfil = cargarPerfil(username);
        perfil.setBiografia(biografia);
        return guardarPerfil(perfil);
    }

    public static void actualizarContadorPublicaciones(String username, int cantidad) {
        PerfilUsuario perfil = cargarPerfil(username);
        perfil.setCantidadPublicaciones(cantidad);
        guardarPerfil(perfil);
    }

    public static void incrementarPublicaciones(String username) {
        PerfilUsuario perfil = cargarPerfil(username);
        perfil.incrementarPublicaciones();
        guardarPerfil(perfil);
    }

    public static void decrementarPublicaciones(String username) {
        PerfilUsuario perfil = cargarPerfil(username);
        perfil.decrementarPublicaciones();
        guardarPerfil(perfil);
    }

    public static void actualizarContadoresSeguidores(String username, int seguidores, int seguidos) {
        PerfilUsuario perfil = cargarPerfil(username);
        perfil.setCantidadSeguidores(seguidores);
        perfil.setCantidadSeguidos(seguidos);
        guardarPerfil(perfil);
    }

    public static void incrementarSeguidores(String username) {
        PerfilUsuario perfil = cargarPerfil(username);
        perfil.incrementarSeguidores();
        guardarPerfil(perfil);
    }

    public static void decrementarSeguidores(String username) {
        PerfilUsuario perfil = cargarPerfil(username);
        perfil.decrementarSeguidores();
        guardarPerfil(perfil);
    }

    public static void incrementarSeguidos(String username) {
        PerfilUsuario perfil = cargarPerfil(username);
        perfil.incrementarSeguidos();
        guardarPerfil(perfil);
    }

    public static void decrementarSeguidos(String username) {
        PerfilUsuario perfil = cargarPerfil(username);
        perfil.decrementarSeguidos();
        guardarPerfil(perfil);
    }

    public static boolean eliminarPerfil(String username) {
        String rutaArchivo = GestorArchivosUsuario.getArchivoPerfil(username);
        File archivo = new File(rutaArchivo);
        
        if (archivo.exists()) {
            boolean eliminado = archivo.delete();
            if (eliminado) {
                System.out.println("✓ Perfil eliminado: " + username);
            }
            return eliminado;
        }
        
        return false;
    }

    public static boolean existePerfil(String username) {
        String rutaArchivo = GestorArchivosUsuario.getArchivoPerfil(username);
        return new File(rutaArchivo).exists();
    }   
}