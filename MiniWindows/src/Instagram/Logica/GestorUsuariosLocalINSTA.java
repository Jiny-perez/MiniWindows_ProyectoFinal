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
    private static final String ARCHIVO_USUARIOS = "datos/users.ins";
    
    public GestorUsuariosLocalINSTA() {
        this.usuarios = new HashMap<>();
        cargarUsuarios();
    }
    
    public boolean registrarUsuario(String username, String password, char genero, int edad, String nombreCompleto) {
        if (usuarios.containsKey(username)) {
            return false;
        }
        
        Usuario nuevoUsuario = new Usuario(username, password, genero, edad, nombreCompleto, true);
        usuarios.put(username, nuevoUsuario);
        
        GestorArchivosUsuarioINSTA.crearDirectorioUsuario(username);
        
        guardarUsuarios();
        return true;
    }
    
    public Usuario validarLogin(String username, String password) {
        Usuario usuario = usuarios.get(username);
        
        if (usuario != null && usuario.getPassword().equals(password) && usuario.isActivo()) {
            return usuario;
        }
        
        return null;
    }

    public boolean esUsuarioInactivoConPasswordCorrecta(String username, String password) {
        Usuario usuario = usuarios.get(username);
        return usuario != null
                && usuario.getPassword().equals(password)
                && !usuario.isActivo();
    }

    public boolean reactivarUsuario(String username) {
        Usuario usuario = usuarios.get(username);
        if (usuario != null && !usuario.isActivo()) {
            usuario.setActivo(true);
            guardarUsuarios();
            return true;
        }
        return false;
    }
    
    public Usuario obtenerUsuario(String username) {
        return usuarios.get(username);
    }
    
    public boolean existeUsuario(String username) {
        return usuarios.containsKey(username);
    }
    
    public void actualizarUsuario(Usuario usuario) {
        if (usuarios.containsKey(usuario.getUsername())) {
            usuarios.put(usuario.getUsername(), usuario);
            guardarUsuarios();
        }
    }
    
    public void desactivarUsuario(String username) {
        Usuario usuario = usuarios.get(username);
        if (usuario != null) {
            usuario.setActivo(false);
            guardarUsuarios();
        }
    }
    
    public ArrayList<Usuario> buscarUsuarios(String termino) {
        ArrayList<Usuario> resultados = new ArrayList<>();
        
        for (Usuario usuario : usuarios.values()) {
            if (usuario.isActivo() && 
                (usuario.getUsername().toLowerCase().contains(termino.toLowerCase()) ||
                 usuario.getNombreCompleto().toLowerCase().contains(termino.toLowerCase()))) {
                resultados.add(usuario);
            }
        }
        
        return resultados;
    }
    
    public ArrayList<Usuario> obtenerUsuariosSugeridos(String usernameActual, int cantidad) {
        ArrayList<Usuario> sugeridos = new ArrayList<>();
        
        for (Usuario usuario : usuarios.values()) {
            if (usuario.isActivo() && 
                !usuario.getUsername().equals(usernameActual) && 
                sugeridos.size() < cantidad) {
                sugeridos.add(usuario);
            }
        }
        
        return sugeridos;
    }
    
    @SuppressWarnings("unchecked")
    private void cargarUsuarios() {
        File archivo = new File(ARCHIVO_USUARIOS);
        
        if (archivo.exists()) {
            try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(archivo))) {
                usuarios = (HashMap<String, Usuario>) ois.readObject();
                System.out.println("✓ Usuarios cargados: " + usuarios.size());
            } catch (IOException | ClassNotFoundException e) {
                System.err.println("Error al cargar usuarios: " + e.getMessage());
                usuarios = new HashMap<>();
            }
        } else {
            System.out.println("⚠ Archivo users.ins no existe, se creará al registrar usuarios");
        }
    }
    
    private void guardarUsuarios() {
        File archivo = new File(ARCHIVO_USUARIOS);
        archivo.getParentFile().mkdirs();
        
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(archivo))) {
            oos.writeObject(usuarios);
            System.out.println("✓ Usuarios guardados: " + usuarios.size());
        } catch (IOException e) {
            System.err.println("Error al guardar usuarios: " + e.getMessage());
        }
    }
    
    public int contarUsuarios() {
        return usuarios.size();
    }
    
    public ArrayList<String> obtenerTodosLosUsernames() {
        return new ArrayList<>(usuarios.keySet());
    }
}