/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Instagram.Logica;

import Modelo.Usuario;
import java.io.*;
import java.util.HashMap;
import java.util.ArrayList;

/**
 *
 * @author najma
 */
public class GestorUsuariosLocal {
    
    private static final String ARCHIVO_USUARIOS = "usuarios_insta.dat";
    private HashMap<String, Usuario> usuarios;
    
    public GestorUsuariosLocal() {
        cargarUsuarios();
    }
    
    private void cargarUsuarios() {
        File archivo = new File(ARCHIVO_USUARIOS);
        
        if (archivo.exists()) {
            try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(archivo))) {
                usuarios = (HashMap<String, Usuario>) ois.readObject();
            } catch (Exception e) {
                System.err.println("Error al cargar usuarios: " + e.getMessage());
                usuarios = new HashMap<>();
            }
        } else {
            usuarios = new HashMap<>();
        }
    }
    
    private void guardarUsuarios() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(ARCHIVO_USUARIOS))) {
            oos.writeObject(usuarios);
        } catch (Exception e) {
            System.err.println("Error al guardar usuarios: " + e.getMessage());
        }
    }
    
    public boolean existeUsuario(String username) {
        return usuarios.containsKey(username);
    }
    
    public Usuario obtenerUsuario(String username) {
        return usuarios.get(username);
    }
    
    public boolean registrarUsuario(String username, String nombreCompleto, char genero, int edad, String password) {
        if (existeUsuario(username)) {
            return false;
        }
        
        Usuario nuevoUsuario = new Usuario(username, nombreCompleto, genero, edad, password, true);
        usuarios.put(username, nuevoUsuario);
        guardarUsuarios();
        return true;
    }
    
    public boolean validarLogin(String username, String password) {
        Usuario usuario = usuarios.get(username);
        return usuario != null && usuario.getPassword().equals(password) && usuario.isActivo();
    }
    
    public int getCantidadUsuarios() {
        return usuarios.size();
    }
    
    public ArrayList<Usuario> buscarUsuarios(String termino) {
        ArrayList<Usuario> resultados = new ArrayList<>();
        
        if (termino == null || termino.trim().isEmpty()) {
            return resultados;
        }
        
        String terminoLower = termino.toLowerCase();
        
        for (Usuario usuario : usuarios.values()) {
            if (usuario.getUsername().toLowerCase().contains(terminoLower) ||
                usuario.getNombreCompleto().toLowerCase().contains(terminoLower)) {
                resultados.add(usuario);
            }
        }
        
        return resultados;
    }

    public ArrayList<Usuario> obtenerTodosLosUsuarios() {
        return new ArrayList<>(usuarios.values());
    }
    
    public ArrayList<Usuario> obtenerUsuariosSugeridos(String usernameActual, int limite) {
        ArrayList<Usuario> sugeridos = new ArrayList<>();
        
        for (Usuario usuario : usuarios.values()) {
            if (!usuario.getUsername().equals(usernameActual)) {
                sugeridos.add(usuario);
                if (sugeridos.size() >= limite) {
                    break;
                }
            }
        }
        
        return sugeridos;
    }
}