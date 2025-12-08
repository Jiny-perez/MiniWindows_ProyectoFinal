/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Instagram.Logica;

import Instagram.Modelo.PerfilUsuario;
import java.io.*;
import java.util.HashMap;

/**
 *
 * @author najma
 */
public class GestorPerfiles {

    private HashMap<String, PerfilUsuario> perfiles;
    
    public GestorPerfiles() {
        this.perfiles = new HashMap<>();
    }
    
    public PerfilUsuario obtenerPerfil(String username) {
        if (perfiles.containsKey(username)) {
            return perfiles.get(username);
        }
        
        String rutaArchivo = GestorArchivosUsuarioINSTA.getDirUsuario(username) + "/perfil.ins";
        
        File archivo = new File(rutaArchivo);
        if (archivo.exists()) {
            try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(archivo))) {
                PerfilUsuario perfil = (PerfilUsuario) ois.readObject();
                perfiles.put(username, perfil);
                return perfil;
            } catch (IOException | ClassNotFoundException e) {
                System.err.println("Error al cargar perfil: " + e.getMessage());
            }
        }
        
        return null;
    }
    
    public void crearPerfil(String username, String biografia, String rutaImagen) {
        if (!GestorArchivosUsuarioINSTA.existeDirectorioUsuario(username)) {
            GestorArchivosUsuarioINSTA.crearDirectorioUsuario(username);
        }
        
        String rutaArchivo = GestorArchivosUsuarioINSTA.getDirUsuario(username) + "/perfil.ins";
        
        PerfilUsuario perfil = new PerfilUsuario(username);
        perfil.setBiografia(biografia);
        
        if (rutaImagen != null && !rutaImagen.isEmpty()) {
            String extension = obtenerExtension(rutaImagen);
            String nombreAvatar = "avatar." + extension;
            
            String rutaNueva = GestorArchivosUsuarioINSTA.copiarImagenUsuario(
                username, rutaImagen, nombreAvatar
            );
            
            if (rutaNueva != null) {
                perfil.setRutaImagenPerfil(rutaNueva);
            }
        }
        
        perfiles.put(username, perfil);
        guardarPerfil(username);
    }
    
    private String obtenerExtension(String ruta) {
        int index = ruta.lastIndexOf('.');
        if (index > 0 && index < ruta.length() - 1) {
            return ruta.substring(index + 1).toLowerCase();
        }
        return "jpg";
    }
    
    public void actualizarBiografia(String username, String biografia) {
        PerfilUsuario perfil = obtenerPerfil(username);
        
        if (perfil == null) {
            crearPerfil(username, biografia, null);
            return;
        }
        
        perfil.setBiografia(biografia);
        guardarPerfil(username);
    }
    
    public void actualizarImagenPerfil(String username, String rutaImagen) {
        PerfilUsuario perfil = obtenerPerfil(username);
        
        if (perfil == null) {
            crearPerfil(username, "", rutaImagen);
            return;
        }
        
        if (rutaImagen != null && !rutaImagen.isEmpty()) {
            String extension = obtenerExtension(rutaImagen);
            String nombreAvatar = "avatar." + extension;
            
            String rutaNueva = GestorArchivosUsuarioINSTA.copiarImagenUsuario(
                username, rutaImagen, nombreAvatar
            );
            
            if (rutaNueva != null) {
                perfil.setRutaImagenPerfil(rutaNueva);
                guardarPerfil(username);
            }
        }
    }
    
    public String obtenerBiografia(String username) {
        PerfilUsuario perfil = obtenerPerfil(username);
        return perfil != null ? perfil.getBiografia() : "";
    }
    
    public String obtenerImagenPerfil(String username) {
        PerfilUsuario perfil = obtenerPerfil(username);
        return perfil != null ? perfil.getRutaImagenPerfil() : null;
    }
    
    private void guardarPerfil(String username) {
        PerfilUsuario perfil = perfiles.get(username);
        if (perfil == null) {
            return;
        }
        
        String rutaArchivo = GestorArchivosUsuarioINSTA.getDirUsuario(username) + "/perfil.ins";
        
        File archivo = new File(rutaArchivo);
        archivo.getParentFile().mkdirs();
        
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(archivo))) {
            oos.writeObject(perfil);
        } catch (IOException e) {
            System.err.println("Error al guardar perfil: " + e.getMessage());
        }
    }
    
    public void eliminarPerfil(String username) {
        perfiles.remove(username);
        
        String rutaArchivo = GestorArchivosUsuarioINSTA.getDirUsuario(username) + "/perfil.ins";
        File archivo = new File(rutaArchivo);
        
        if (archivo.exists()) {
            archivo.delete();
        }
    }
}
