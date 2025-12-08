/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Instagram.Logica;

import java.io.*;
import java.util.ArrayList;

/**
 *
 * @author najma
 */
public class GestorFollowersINSTA {
    
    private String username;
    private ArrayList<String> seguidores;
    
    public GestorFollowersINSTA(String username) {
        this.username = username;
        this.seguidores = new ArrayList<>();
        cargarFollowers();
    }
    
    private void cargarFollowers() {
        String rutaArchivo = GestorArchivosUsuarioINSTA.getArchivoFollowers(username);
        File archivo = new File(rutaArchivo);
        
        if (archivo.exists() && archivo.length() > 0) {
            try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(archivo))) {
                seguidores = (ArrayList<String>) ois.readObject();
                System.out.println("✓ Followers cargado para " + username + ": " + seguidores.size());
            } catch (Exception e) {
                System.err.println("Error al cargar followers.ins de " + username + ": " + e.getMessage());
                seguidores = new ArrayList<>();
            }
        } else {
            seguidores = new ArrayList<>();
        }
    }
    
    public void guardarFollowers() {
        String rutaArchivo = GestorArchivosUsuarioINSTA.getArchivoFollowers(username);
        
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(rutaArchivo))) {
            oos.writeObject(seguidores);
            System.out.println("✓ Followers guardado para " + username);
        } catch (Exception e) {
            System.err.println("Error al guardar followers.ins de " + username + ": " + e.getMessage());
        }
    }
    
    public boolean agregarSeguidor(String seguidor) {
        if (seguidor.equals(username)) {
            return false;
        }
        
        if (seguidores.contains(seguidor)) {
            return false;
        }
        
        seguidores.add(seguidor);
        guardarFollowers();
        return true;
    }
    
    public boolean eliminarSeguidor(String seguidor) {
        boolean resultado = seguidores.remove(seguidor);
        if (resultado) {
            guardarFollowers();
        }
        return resultado;
    }
    
    public boolean esSeguidor(String usuario) {
        return seguidores.contains(usuario);
    }
    
    public ArrayList<String> obtenerSeguidores() {
        return new ArrayList<>(seguidores);
    }
    
    public int contarSeguidores() {
        return seguidores.size();
    }
}
