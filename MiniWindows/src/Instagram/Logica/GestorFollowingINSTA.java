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
public class GestorFollowingINSTA {
    
    private String username;
    private ArrayList<String> siguiendo;
    
    public GestorFollowingINSTA(String username) {
        this.username = username;
        this.siguiendo = new ArrayList<>();
        cargarFollowing();
    }
    
    private void cargarFollowing() {
        String rutaArchivo = GestorArchivosUsuarioINSTA.getArchivoFollowing(username);
        File archivo = new File(rutaArchivo);
        
        if (archivo.exists() && archivo.length() > 0) {
            try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(archivo))) {
                siguiendo = (ArrayList<String>) ois.readObject();
                System.out.println("✓ Following cargado para " + username + ": " + siguiendo.size());
            } catch (Exception e) {
                System.err.println("Error al cargar following.ins de " + username + ": " + e.getMessage());
                siguiendo = new ArrayList<>();
            }
        } else {
            siguiendo = new ArrayList<>();
        }
    }
    
    public void guardarFollowing() {
        String rutaArchivo = GestorArchivosUsuarioINSTA.getArchivoFollowing(username);
        
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(rutaArchivo))) {
            oos.writeObject(siguiendo);
            System.out.println("✓ Following guardado para " + username);
        } catch (Exception e) {
            System.err.println("Error al guardar following.ins de " + username + ": " + e.getMessage());
        }
    }
    
    public boolean seguir(String usuarioSeguido) {
        if (usuarioSeguido.equals(username)) {
            return false; // No puedo seguirme a mí mismo
        }
        
        if (siguiendo.contains(usuarioSeguido)) {
            return false; // Ya lo sigo
        }
        
        siguiendo.add(usuarioSeguido);
        guardarFollowing();
        return true;
    }
    
    public boolean dejarDeSeguir(String usuarioSeguido) {
        boolean resultado = siguiendo.remove(usuarioSeguido);
        if (resultado) {
            guardarFollowing();
        }
        return resultado;
    }
    
    public boolean estaSiguiendo(String usuarioSeguido) {
        return siguiendo.contains(usuarioSeguido);
    }
    
    public ArrayList<String> obtenerSiguiendo() {
        return new ArrayList<>(siguiendo);
    }
    
    public int contarSiguiendo() {
        return siguiendo.size();
    }
}
