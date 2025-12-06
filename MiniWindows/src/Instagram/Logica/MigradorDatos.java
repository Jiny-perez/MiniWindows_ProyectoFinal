/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Instagram.Logica;

import Instagram.Modelo.Publicacion;
import Instagram.Modelo.PerfilUsuario;
import Modelo.Usuario;
import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;

/**
 *
 * @author najma
 */
public class MigradorDatos {
    
    private static final String ARCHIVO_PUBLICACIONES_ANTIGUO = "publicaciones_insta.dat";
    private static final String ARCHIVO_USUARIOS_ANTIGUO = "usuarios_insta.dat";

    public static boolean migrarDatos() {
        System.out.println("=== INICIANDO MIGRACIÓN DE DATOS ===");
        
        GestorArchivosUsuario.inicializarEstructura();
        
        HashMap<String, Usuario> usuarios = cargarUsuariosAntiguos();
        if (usuarios != null && !usuarios.isEmpty()) {
            migrarUsuarios(usuarios);
        } else {
            System.out.println("○ No hay usuarios antiguos para migrar");
        }
        
        ArrayList<Publicacion> publicacionesAntiguas = cargarPublicacionesAntiguas();
        if (publicacionesAntiguas != null && !publicacionesAntiguas.isEmpty()) {
            migrarPublicaciones(publicacionesAntiguas);
        } else {
            System.out.println("○ No hay publicaciones antiguas para migrar");
        }
        
        System.out.println("=== MIGRACIÓN COMPLETADA ===");
        return true;
    }

    private static HashMap<String, Usuario> cargarUsuariosAntiguos() {
        File archivo = new File(ARCHIVO_USUARIOS_ANTIGUO);
        
        if (!archivo.exists()) {
            System.out.println("○ Archivo de usuarios antiguo no existe");
            return null;
        }
        
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(archivo))) {
            HashMap<String, Usuario> usuarios = (HashMap<String, Usuario>) ois.readObject();
            System.out.println("✓ Cargados " + usuarios.size() + " usuarios antiguos");
            return usuarios;
        } catch (Exception e) {
            System.err.println("Error al cargar usuarios antiguos: " + e.getMessage());
            return null;
        }
    }

    private static void migrarUsuarios(HashMap<String, Usuario> usuarios) {
        System.out.println("\n--- Migrando usuarios ---");
        
        for (Usuario usuario : usuarios.values()) {
            String username = usuario.getUsername();
            
            if (GestorArchivosUsuario.crearDirectorioUsuario(username)) {
                PerfilUsuario perfil = new PerfilUsuario(username);
                GestorPerfiles.guardarPerfil(perfil);
                
                System.out.println("✓ Migrado usuario: " + username);
            } else {
                System.err.println("✗ Error al migrar usuario: " + username);
            }
        }
    }

    private static ArrayList<Publicacion> cargarPublicacionesAntiguas() {
        File archivo = new File(ARCHIVO_PUBLICACIONES_ANTIGUO);
        
        if (!archivo.exists()) {
            System.out.println("○ Archivo de publicaciones antiguo no existe");
            return null;
        }
        
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(archivo))) {
            ArrayList<Publicacion> publicaciones = (ArrayList<Publicacion>) ois.readObject();
            System.out.println("✓ Cargadas " + publicaciones.size() + " publicaciones antiguas");
            return publicaciones;
        } catch (Exception e) {
            System.err.println("Error al cargar publicaciones antiguas: " + e.getMessage());
            return null;
        }
    }

    private static void migrarPublicaciones(ArrayList<Publicacion> publicacionesAntiguas) {
        System.out.println("\n--- Migrando publicaciones ---");
        
        HashMap<String, ArrayList<Publicacion>> publicacionesPorUsuario = new HashMap<>();
        
        for (Publicacion pub : publicacionesAntiguas) {
            String username = pub.getUsername();
            
            if (!publicacionesPorUsuario.containsKey(username)) {
                publicacionesPorUsuario.put(username, new ArrayList<>());
            }
            
            publicacionesPorUsuario.get(username).add(pub);
        }
        
        for (String username : publicacionesPorUsuario.keySet()) {
            ArrayList<Publicacion> pubs = publicacionesPorUsuario.get(username);
            
            if (!GestorArchivosUsuario.existeDirectorioUsuario(username)) {
                GestorArchivosUsuario.crearDirectorioUsuario(username);
            }
            
            String rutaArchivo = GestorArchivosUsuario.getArchivoPublicaciones(username);
            
            try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(rutaArchivo))) {
                oos.writeObject(pubs);
                
                GestorPerfiles.actualizarContadorPublicaciones(username, pubs.size());
                
                System.out.println("✓ Migradas " + pubs.size() + " publicaciones de " + username);
            } catch (Exception e) {
                System.err.println("✗ Error al migrar publicaciones de " + username + ": " + e.getMessage());
            }
        }
    }

    public static boolean crearBackup() {
        System.out.println("=== CREANDO BACKUP ===");
        
        boolean backupUsuarios = crearBackupArchivo(ARCHIVO_USUARIOS_ANTIGUO);
        boolean backupPublicaciones = crearBackupArchivo(ARCHIVO_PUBLICACIONES_ANTIGUO);
        
        if (backupUsuarios || backupPublicaciones) {
            System.out.println("✓ Backup creado");
            return true;
        } else {
            System.out.println("○ No hay archivos para hacer backup");
            return false;
        }
    }

    private static boolean crearBackupArchivo(String nombreArchivo) {
        File archivo = new File(nombreArchivo);
        
        if (!archivo.exists()) {
            return false;
        }
        
        String nombreBackup = nombreArchivo + ".backup";
        File archivoBackup = new File(nombreBackup);
        
        try {
            java.nio.file.Files.copy(
                archivo.toPath(), 
                archivoBackup.toPath(), 
                java.nio.file.StandardCopyOption.REPLACE_EXISTING
            );
            System.out.println("✓ Backup creado: " + nombreBackup);
            return true;
        } catch (Exception e) {
            System.err.println("Error al crear backup de " + nombreArchivo + ": " + e.getMessage());
            return false;
        }
    }
    
    public static boolean necesitaMigracion() {
        File archivoPubs = new File(ARCHIVO_PUBLICACIONES_ANTIGUO);
        File archivoUsers = new File(ARCHIVO_USUARIOS_ANTIGUO);
        
        return archivoPubs.exists() || archivoUsers.exists();
    }

    public static boolean migrarConBackup() {
        if (!necesitaMigracion()) {
            System.out.println("○ No es necesario migrar datos");
            return false;
        }
        
        System.out.println("=== MIGRACIÓN CON BACKUP ===");
        
        crearBackup();
        
        boolean exito = migrarDatos();
        
        if (exito) {
            System.out.println("\n✓ Migración completada exitosamente");
            System.out.println("  Los archivos antiguos se mantienen como backup (.backup)");
        } else {
            System.err.println("\n✗ Error en la migración");
        }
        
        return exito;
    }
    
    public static void main(String[] args) {
        System.out.println("========================================");
        System.out.println("  MIGRADOR DE DATOS - INSTAGRAM MINI");
        System.out.println("========================================\n");
        
        if (necesitaMigracion()) {
            System.out.println("Se detectaron datos antiguos que necesitan migración.");
            System.out.println("¿Desea continuar? (Se creará un backup automático)");
            System.out.println();
            
            migrarConBackup();
        } else {
            System.out.println("✓ No hay datos antiguos para migrar.");
            System.out.println("  El sistema ya está usando la nueva estructura.");
        }
        
        System.out.println("\n========================================");
    }
}