/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Instagram.Logica;

import Instagram.Modelo.Usuario;
import java.io.*;
import java.util.HashMap;
import java.util.ArrayList;

/**
 *
 * @author najma
 */
public class GestorUsuariosLocalINSTA {
   
    private HashMap<String, Usuario> usuarios;
    
    public GestorUsuariosLocalINSTA() {
        cargarUsuarios();
    }
    
    private void cargarUsuarios() {
        File archivo = new File(GestorArchivosUsuarioINSTA.ARCHIVO_USERS);
        
        if (archivo.exists()) {
            try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(archivo))) {
                usuarios = (HashMap<String, Usuario>) ois.readObject();
                System.out.println("✓ Usuarios cargados desde users.ins: " + usuarios.size());
            } catch (Exception e) {
                System.err.println("Error al cargar users.ins: " + e.getMessage());
                usuarios = new HashMap<>();
            }
        } else {
            usuarios = new HashMap<>();
            System.out.println("○ Iniciando con nuevo archivo users.ins");
        }
    }
    
    private void guardarUsuarios() {
        try (ObjectOutputStream oos = new ObjectOutputStream(
                new FileOutputStream(GestorArchivosUsuarioINSTA.ARCHIVO_USERS))) {
            oos.writeObject(usuarios);
            System.out.println("✓ Usuarios guardados en users.ins");
        } catch (Exception e) {
            System.err.println("Error al guardar users.ins: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    public boolean existeUsuario(String username) {
        return usuarios.containsKey(username);
    }
    
    public Usuario obtenerUsuario(String username) {
        return usuarios.get(username);
    }
    
    public boolean registrarUsuario(String username, String nombreCompleto, char genero, 
                                   int edad, String password) {
        if (existeUsuario(username)) {
            System.out.println("El usuario ya existe: " + username);
            return false;
        }
        
        try {
            Usuario nuevoUsuario = new Usuario(username, nombreCompleto, genero, edad, password, true);
            usuarios.put(username, nuevoUsuario);
            
            // Crear directorio del usuario con archivos .ins
            GestorArchivosUsuarioINSTA.crearDirectorioUsuario(username);
            
            guardarUsuarios();
            
            System.out.println("✓ Usuario registrado exitosamente: " + username);
            return true;
        } catch (Exception e) {
            System.err.println("Error al registrar usuario: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
    
    public boolean validarLogin(String username, String password) {
        Usuario usuario = usuarios.get(username);
        
        if (usuario == null) {
            return false;
        }
        
        if (!usuario.isActivo()) {
            return false;
        }
        
        return usuario.getPassword().equals(password);
    }
    
    public boolean validarCredenciales(String username, String password) {
        Usuario usuario = usuarios.get(username);
        
        if (usuario == null) {
            return false;
        }
        
        return usuario.getPassword().equals(password);
    }
    
    public boolean actualizarUsuario(Usuario usuario) {
        if (usuario == null || !existeUsuario(usuario.getUsername())) {
            return false;
        }
        
        usuarios.put(usuario.getUsername(), usuario);
        guardarUsuarios();
        return true;
    }
    
    public boolean desactivarUsuario(String username) {
        Usuario usuario = obtenerUsuario(username);
        if (usuario != null) {
            usuario.setActivo(false);
            guardarUsuarios();
            return true;
        }
        return false;
    }
    
    public boolean activarUsuario(String username) {
        Usuario usuario = obtenerUsuario(username);
        if (usuario != null) {
            usuario.setActivo(true);
            guardarUsuarios();
            return true;
        }
        return false;
    }
    
    public int getCantidadUsuarios() {
        return usuarios.size();
    }
    
    public int getCantidadUsuariosActivos() {
        return (int) usuarios.values().stream()
            .filter(Usuario::isActivo)
            .count();
    }
    
    public ArrayList<Usuario> buscarUsuarios(String termino) {
        ArrayList<Usuario> resultados = new ArrayList<>();
        
        if (termino == null || termino.trim().isEmpty()) {
            return resultados;
        }
        
        String terminoLower = termino.toLowerCase();
        
        for (Usuario usuario : usuarios.values()) {
            if (!usuario.isActivo()) {
                continue;
            }
            
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
    
    public ArrayList<Usuario> obtenerUsuariosActivos() {
        ArrayList<Usuario> activos = new ArrayList<>();
        for (Usuario usuario : usuarios.values()) {
            if (usuario.isActivo()) {
                activos.add(usuario);
            }
        }
        return activos;
    }
    
    public ArrayList<Usuario> obtenerUsuariosSugeridos(String usernameActual, int limite) {
        ArrayList<Usuario> sugeridos = new ArrayList<>();
        
        for (Usuario usuario : usuarios.values()) {
            if (!usuario.getUsername().equals(usernameActual) && usuario.isActivo()) {
                sugeridos.add(usuario);
                if (sugeridos.size() >= limite) {
                    break;
                }
            }
        }
        
        return sugeridos;
    }
    
    public ArrayList<String> obtenerUsernamesActivos() {
        ArrayList<String> usernames = new ArrayList<>();
        for (Usuario usuario : usuarios.values()) {
            if (usuario.isActivo()) {
                usernames.add(usuario.getUsername());
            }
        }
        return usernames;
    }
}
