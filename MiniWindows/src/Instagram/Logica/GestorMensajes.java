/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Instagram.Logica;

import Instagram.Modelo.Conversacion;
import Instagram.Modelo.Mensaje;
import java.io.*;
import java.util.ArrayList;
import java.util.stream.Collectors;

/**
 *
 * @author najma
 */
public class GestorMensajes {
    
    private ArrayList<Conversacion> conversaciones;
    private static final String ARCHIVO_MENSAJES = "datos/mensajes_insta.dat";
    
    public GestorMensajes() {
        this.conversaciones = new ArrayList<>();
        cargarMensajes();
    }
    
    public void enviarMensaje(String emisor, String receptor, String contenido) {
        Mensaje mensaje = new Mensaje(emisor, receptor, contenido);
        
        Conversacion conversacion = buscarConversacion(emisor, receptor);
        
        if (conversacion == null) {
            conversacion = new Conversacion(emisor, receptor);
            conversaciones.add(conversacion);
        }
        
        conversacion.agregarMensaje(mensaje);
        guardarMensajes();
    }
    
    public ArrayList<Mensaje> obtenerMensajes(String usuario1, String usuario2) {
        Conversacion conversacion = buscarConversacion(usuario1, usuario2);
        
        if (conversacion != null) {
            return conversacion.getMensajes();
        }
        
        return new ArrayList<>();
    }
    
    public ArrayList<Conversacion> obtenerConversaciones(String username) {
        return conversaciones.stream()
                .filter(c -> c.getUsuario1().equals(username) || c.getUsuario2().equals(username))
                .sorted((c1, c2) -> c2.getUltimaActualizacion().compareTo(c1.getUltimaActualizacion()))
                .collect(Collectors.toCollection(ArrayList::new));
    }
    
    private Conversacion buscarConversacion(String usuario1, String usuario2) {
        for (Conversacion conversacion : conversaciones) {
            if (conversacion.involucraUsuarios(usuario1, usuario2)) {
                return conversacion;
            }
        }
        return null;
    }
    
    public int contarMensajesNoLeidos(String username) {
        int count = 0;
        for (Conversacion conversacion : conversaciones) {
            if (conversacion.getUsuario1().equals(username) || conversacion.getUsuario2().equals(username)) {
                for (Mensaje mensaje : conversacion.getMensajes()) {
                    if (mensaje.getReceptor().equals(username) && !mensaje.isLeido()) {
                        count++;
                    }
                }
            }
        }
        return count;
    }
    
    public void marcarMensajesComoLeidos(String emisor, String receptor) {
        Conversacion conversacion = buscarConversacion(emisor, receptor);
        if (conversacion != null) {
            for (Mensaje mensaje : conversacion.getMensajes()) {
                if (mensaje.getEmisor().equals(emisor) && mensaje.getReceptor().equals(receptor)) {
                    mensaje.setLeido(true);
                }
            }
            guardarMensajes();
        }
    }
    
    @SuppressWarnings("unchecked")
    private void cargarMensajes() {
        File archivo = new File(ARCHIVO_MENSAJES);
        
        if (archivo.exists()) {
            try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(archivo))) {
                conversaciones = (ArrayList<Conversacion>) ois.readObject();
            } catch (IOException | ClassNotFoundException e) {
                System.err.println("Error al cargar mensajes: " + e.getMessage());
                conversaciones = new ArrayList<>();
            }
        }
    }
    
    private void guardarMensajes() {
        File archivo = new File(ARCHIVO_MENSAJES);
        archivo.getParentFile().mkdirs();
        
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(archivo))) {
            oos.writeObject(conversaciones);
        } catch (IOException e) {
            System.err.println("Error al guardar mensajes: " + e.getMessage());
        }
    }
    
    public void eliminarMensajesDeUsuario(String username) {
        conversaciones.removeIf(c -> 
            c.getUsuario1().equals(username) || c.getUsuario2().equals(username)
        );
        guardarMensajes();
    }
}
