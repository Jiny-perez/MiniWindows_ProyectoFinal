/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Instagram.Logica;

import Instagram.Modelo.Mensaje;
import java.io.*;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.stream.Collectors;

/**
 *
 * @author najma
 */
public class GestorMensajes {
    
    private static final String ARCHIVO_MENSAJES = "mensajes_insta.dat";
    private ArrayList<Mensaje> mensajes;
    
    public GestorMensajes() {
        cargarMensajes();
    }
    
    private void cargarMensajes() {
        File archivo = new File(ARCHIVO_MENSAJES);
        
        if (archivo.exists()) {
            try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(archivo))) {
                mensajes = (ArrayList<Mensaje>) ois.readObject();
                System.out.println("Mensajes cargados: " + mensajes.size());
            } catch (Exception e) {
                System.err.println("Error al cargar mensajes: " + e.getMessage());
                mensajes = new ArrayList<>();
            }
        } else {
            mensajes = new ArrayList<>();
        }
    }
    
    public void guardarMensajes() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(ARCHIVO_MENSAJES))) {
            oos.writeObject(mensajes);
        } catch (Exception e) {
            System.err.println("Error al guardar mensajes: " + e.getMessage());
        }
    }
    
    public Mensaje enviarMensaje(String remitente, String destinatario, String contenido) {
        if (remitente.equals(destinatario)) {
            return null;
        }
        
        Mensaje mensaje = new Mensaje(remitente, destinatario, contenido);
        mensajes.add(mensaje);
        guardarMensajes();
        
        return mensaje;
    }
    
    public ArrayList<Mensaje> obtenerConversacion(String usuario1, String usuario2) {
        return mensajes.stream()
            .filter(m -> (m.getRemitente().equals(usuario1) && m.getDestinatario().equals(usuario2)) ||
                        (m.getRemitente().equals(usuario2) && m.getDestinatario().equals(usuario1)))
            .sorted(Comparator.comparing(Mensaje::getFechaEnvio))
            .collect(Collectors.toCollection(ArrayList::new));
    }
    
    public ArrayList<String> obtenerConversaciones(String username) {
        HashMap<String, Mensaje> ultimosMensajes = new HashMap<>();
        
        for (Mensaje mensaje : mensajes) {
            if (mensaje.involucra(username)) {
                String otroUsuario = mensaje.getOtroUsuario(username);
                
                if (!ultimosMensajes.containsKey(otroUsuario) ||
                    mensaje.getFechaEnvio().isAfter(ultimosMensajes.get(otroUsuario).getFechaEnvio())) {
                    ultimosMensajes.put(otroUsuario, mensaje);
                }
            }
        }
        
        return ultimosMensajes.entrySet().stream()
            .sorted((e1, e2) -> e2.getValue().getFechaEnvio().compareTo(e1.getValue().getFechaEnvio()))
            .map(e -> e.getKey())
            .collect(Collectors.toCollection(ArrayList::new));
    }
    
    public int contarMensajesNoLeidos(String username) {
        return (int) mensajes.stream()
            .filter(m -> m.getDestinatario().equals(username) && !m.isLeido())
            .count();
    }
    
    public void marcarConversacionComoLeida(String usuario1, String usuario2) {
        for (Mensaje mensaje : mensajes) {
            if (mensaje.getRemitente().equals(usuario2) && 
                mensaje.getDestinatario().equals(usuario1) && 
                !mensaje.isLeido()) {
                mensaje.marcarComoLeido();
            }
        }
        guardarMensajes();
    }
    
    public Mensaje obtenerUltimoMensaje(String usuario1, String usuario2) {
        return mensajes.stream()
            .filter(m -> (m.getRemitente().equals(usuario1) && m.getDestinatario().equals(usuario2)) ||
                        (m.getRemitente().equals(usuario2) && m.getDestinatario().equals(usuario1)))
            .max(Comparator.comparing(Mensaje::getFechaEnvio))
            .orElse(null);
    }
    
    public boolean eliminarMensaje(String mensajeId, String username) {
        Mensaje mensaje = mensajes.stream()
            .filter(m -> m.getId().equals(mensajeId))
            .findFirst()
            .orElse(null);
        
        if (mensaje != null && mensaje.getRemitente().equals(username)) {
            boolean eliminado = mensajes.remove(mensaje);
            if (eliminado) {
                guardarMensajes();
            }
            return eliminado;
        }
        
        return false;
    }
    
    public void eliminarConversacion(String usuario1, String usuario2) {
        mensajes.removeIf(m -> 
            (m.getRemitente().equals(usuario1) && m.getDestinatario().equals(usuario2)) ||
            (m.getRemitente().equals(usuario2) && m.getDestinatario().equals(usuario1))
        );
        guardarMensajes();
    }
    
    public int contarMensajesEnConversacion(String usuario1, String usuario2) {
        return (int) mensajes.stream()
            .filter(m -> (m.getRemitente().equals(usuario1) && m.getDestinatario().equals(usuario2)) ||
                        (m.getRemitente().equals(usuario2) && m.getDestinatario().equals(usuario1)))
            .count();
    }
}