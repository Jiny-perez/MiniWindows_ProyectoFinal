/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Instagram.Logica;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

/**
 *
 * @author najma
 */
public class GestorArchivosUsuarioINSTA {
     
    private static final String DIR_DATOS = "datos";
    private static final String DIR_USUARIOS = DIR_DATOS + "/usuarios";
    
    public static final String ARCHIVO_USERS = DIR_DATOS + "/users.ins";
    
    private static final String ARCHIVO_FOLLOWING = "following.ins";
    private static final String ARCHIVO_FOLLOWERS = "followers.ins";
    private static final String ARCHIVO_INSTA = "insta.ins";
    private static final String ARCHIVO_MENSAJES = "mensajes.ins";
    private static final String DIR_IMAGENES = "imagenes";

    public static void inicializarEstructura() {
        try {
            Files.createDirectories(Paths.get(DIR_DATOS));
            Files.createDirectories(Paths.get(DIR_USUARIOS));
            System.out.println("✓ Estructura de directorios inicializada");
        } catch (IOException e) {
            System.err.println("Error al crear estructura de directorios: " + e.getMessage());
        }
    }

    public static boolean crearDirectorioUsuario(String username) {
        try {
            Path dirUsuario = Paths.get(getDirUsuario(username));
            Path dirImagenes = Paths.get(getDirImagenesUsuario(username));
            
            Files.createDirectories(dirUsuario);
            Files.createDirectories(dirImagenes);
            
            crearArchivoVacioSiNoExiste(getArchivoFollowing(username));
            crearArchivoVacioSiNoExiste(getArchivoFollowers(username));
            crearArchivoVacioSiNoExiste(getArchivoInsta(username));
            crearArchivoVacioSiNoExiste(getArchivoMensajes(username));
            
            System.out.println("✓ Directorio y archivos .ins creados para: " + username);
            return true;
        } catch (IOException e) {
            System.err.println("Error al crear directorio para " + username + ": " + e.getMessage());
            return false;
        }
    }
    
    private static void crearArchivoVacioSiNoExiste(String rutaArchivo) throws IOException {
        File archivo = new File(rutaArchivo);
        if (!archivo.exists()) {
            archivo.createNewFile();
        }
    }

    public static boolean existeDirectorioUsuario(String username) {
        File dir = new File(getDirUsuario(username));
        return dir.exists() && dir.isDirectory();
    }
    
    public static String getDirUsuario(String username) {
        return DIR_USUARIOS + "/" + username;
    }

    public static String getArchivoFollowing(String username) {
        return getDirUsuario(username) + "/" + ARCHIVO_FOLLOWING;
    }

    public static String getArchivoFollowers(String username) {
        return getDirUsuario(username) + "/" + ARCHIVO_FOLLOWERS;
    }

    public static String getArchivoInsta(String username) {
        return getDirUsuario(username) + "/" + ARCHIVO_INSTA;
    }
    
    public static String getArchivoMensajes(String username) {
        return getDirUsuario(username) + "/" + ARCHIVO_MENSAJES;
    }

    public static String getDirImagenesUsuario(String username) {
        return getDirUsuario(username) + "/" + DIR_IMAGENES;
    }

    public static String copiarImagenUsuario(String username, String rutaOrigen, String nombreArchivo) {
        try {
            Path origen = Paths.get(rutaOrigen);
            
            if (!Files.exists(origen)) {
                System.err.println("Archivo origen no existe: " + rutaOrigen);
                return null;
            }
            
            String dirImagenes = getDirImagenesUsuario(username);
            Files.createDirectories(Paths.get(dirImagenes));
            
            String rutaDestino = dirImagenes + "/" + nombreArchivo;
            Path destino = Paths.get(rutaDestino);
            
            Files.copy(origen, destino, StandardCopyOption.REPLACE_EXISTING);
            
            System.out.println("✓ Imagen copiada: " + nombreArchivo);
            return rutaDestino;
        } catch (IOException e) {
            System.err.println("Error al copiar imagen: " + e.getMessage());
            return null;
        }
    }

    public static String generarNombreImagenPublicacion(String username, String extension) {
        String dirImagenes = getDirImagenesUsuario(username);
        File dir = new File(dirImagenes);
        
        int contador = 1;
        if (dir.exists()) {
            File[] archivos = dir.listFiles();
            if (archivos != null) {
                contador = archivos.length + 1;
            }
        }
        
        return String.format("pub_%03d%s", contador, extension);
    }

    public static String obtenerExtension(String rutaArchivo) {
        if (rutaArchivo == null || rutaArchivo.isEmpty()) {
            return ".jpg";
        }
        
        int lastDot = rutaArchivo.lastIndexOf('.');
        if (lastDot > 0 && lastDot < rutaArchivo.length() - 1) {
            return rutaArchivo.substring(lastDot);
        }
        
        return ".jpg";
    }
    
    public static boolean eliminarImagen(String rutaImagen) {
        try {
            Path path = Paths.get(rutaImagen);
            if (Files.exists(path)) {
                Files.delete(path);
                System.out.println("✓ Imagen eliminada: " + rutaImagen);
                return true;
            }
            return false;
        } catch (IOException e) {
            System.err.println("Error al eliminar imagen: " + e.getMessage());
            return false;
        }
    }

    public static boolean eliminarDirectorioUsuario(String username) {
        try {
            Path dirUsuario = Paths.get(getDirUsuario(username));
            if (Files.exists(dirUsuario)) {
                Files.walk(dirUsuario)
                    .sorted((a, b) -> b.compareTo(a))
                    .forEach(path -> {
                        try {
                            Files.delete(path);
                        } catch (IOException e) {
                            System.err.println("Error al eliminar: " + path);
                        }
                    });
                
                System.out.println("✓ Directorio de usuario eliminado: " + username);
                return true;
            }
            return false;
        } catch (IOException e) {
            System.err.println("Error al eliminar directorio de usuario: " + e.getMessage());
            return false;
        }
    }
}