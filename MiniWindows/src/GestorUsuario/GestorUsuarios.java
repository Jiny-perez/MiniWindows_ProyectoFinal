package GestorUsuario;

import GestorUsuario.Usuario;
import Excepciones.UsuarioNoEncontradoException;
import Excepciones.ArchivoNoValidoException;

import java.io.*;
import java.util.ArrayList;

/**
 *
 * @author najma
 */
public class GestorUsuarios implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    private ArrayList<Usuario> usuarios;
    private Usuario usuarioActual;
    private static final String ARCHIVO_USUARIOS = "usuarios.sop";
    
    public GestorUsuarios() {
        this.usuarios = new ArrayList<>();
        cargarUsuarios();
        
        if (usuarios.isEmpty()) {
            usuarios.add(Usuario.crearAdmin());
            guardarUsuarios();
        }
    }
    
    public Usuario login(String username, String password) throws UsuarioNoEncontradoException {
        for (Usuario usuario : usuarios) {
            if (usuario.getUsername().equalsIgnoreCase(username)) {
                if (!usuario.isActivo()) {
                    throw new UsuarioNoEncontradoException("La cuenta está desactivada");
                }
                
                if (usuario.verificarPassword(password)) {
                    this.usuarioActual = usuario;
                    return usuario;
                } else {
                    throw new UsuarioNoEncontradoException("Contraseña incorrecta");
                }
            }
        }
        throw new UsuarioNoEncontradoException(username);
    }
    
    public void logout() {
        this.usuarioActual = null;
    }

    public Usuario crearUsuario(String nombreCompleto, String username, String password) throws ArchivoNoValidoException {    
        if (nombreCompleto == null || nombreCompleto.trim().isEmpty()) {
            throw new ArchivoNoValidoException("El nombre completo no puede estar vacío");
        }
        
        if (username == null || username.trim().isEmpty()) {
            throw new ArchivoNoValidoException("El nombre de usuario no puede estar vacío");
        }
        
        if (password == null || password.length() < 4) {
            throw new ArchivoNoValidoException("La contraseña debe tener al menos 4 caracteres");
        }
        
        if (existeUsuario(username)) {
            throw new ArchivoNoValidoException("El nombre de usuario ["+username+"] ya existe");
        }
        
        if (username.contains(" ") || username.contains("\\") || username.contains("/")) {
            throw new ArchivoNoValidoException("El nombre de usuario contiene caracteres inválidos");
        }
        
        Usuario nuevoUsuario = new Usuario(nombreCompleto, username, password, false);
        usuarios.add(nuevoUsuario);
        guardarUsuarios();
        
        return nuevoUsuario;
    }
    
    public boolean existeUsuario(String username) {
        for (Usuario usuario : usuarios) {
            if (usuario.getUsername().equalsIgnoreCase(username)) {
                return true;
            }
        }
        return false;
    }
    
    public Usuario obtenerUsuario(String username) throws UsuarioNoEncontradoException {
        for (Usuario usuario : usuarios) {
            if (usuario.getUsername().equalsIgnoreCase(username)) {
                return usuario;
            }
        }
        throw new UsuarioNoEncontradoException(username);
    }
    
    public ArrayList<Usuario> obtenerTodosLosUsuarios() {
        return new ArrayList<>(usuarios);
    }
    
    public ArrayList<Usuario> obtenerUsuariosActivos() {
        ArrayList<Usuario> activos = new ArrayList<>();
        for (Usuario usuario : usuarios) {
            if (usuario.isActivo()) {
                activos.add(usuario);
            }
        }
        return activos;
    }
    
    public void cambiarEstadoUsuario(String username, boolean activo) throws UsuarioNoEncontradoException {
        Usuario usuario = obtenerUsuario(username);
        usuario.setActivo(activo);
        guardarUsuarios();
    }
    
    public void cambiarPassword(String username, String passwordActual, String passwordNueva) 
            throws UsuarioNoEncontradoException, ArchivoNoValidoException {
        
        Usuario usuario = obtenerUsuario(username);
        
        if (!usuario.verificarPassword(passwordActual)) {
            throw new ArchivoNoValidoException("La contraseña actual es incorrecta");
        }
        
        if (passwordNueva == null || passwordNueva.length() < 4) {
            throw new ArchivoNoValidoException("La nueva contraseña debe tener al menos 4 caracteres");
        }
        
        usuario.setPassword(passwordNueva);
        guardarUsuarios();
    }
    
    public boolean eliminarUsuario(String username) throws UsuarioNoEncontradoException, ArchivoNoValidoException {
        if (usuarioActual == null || !usuarioActual.esAdmin()) {
            throw new ArchivoNoValidoException("Solo el administrador puede eliminar usuarios");
        }
        
        if (username.equalsIgnoreCase("admin")) {
            throw new ArchivoNoValidoException("No se puede eliminar al usuario administrador");
        }
        
        Usuario usuario = obtenerUsuario(username);
        usuarios.remove(usuario);
        guardarUsuarios();
        return true;
    }
    
    public Usuario getUsuarioActual() {
        return usuarioActual;
    }
    
    public boolean haySesionActiva() {
        return usuarioActual != null;
    }
    
    public void guardarUsuarios() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(ARCHIVO_USUARIOS))) {
            oos.writeObject(usuarios);
        } catch (IOException e) {
            System.err.println("Error al guardar usuarios: " +e.getMessage());
        }
    }
    
    private void cargarUsuarios() {
        File archivo = new File(ARCHIVO_USUARIOS);
        if (!archivo.exists()) {
            return;
        }
        
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(ARCHIVO_USUARIOS))) {
            this.usuarios = (ArrayList<Usuario>) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Error al cargar usuarios: "+e.getMessage());
            this.usuarios = new ArrayList<>();
        }
    }
    
    public int getCantidadUsuarios() {
        return usuarios.size();
    }
}