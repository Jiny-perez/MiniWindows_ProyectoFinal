/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Instagram.Logica;

import Instagram.Modelo.Notificacion;
import Instagram.Modelo.Notificacion.TipoNotificacion;
import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

/**
 *
 * @author najma
 */
public class GestorNotificaciones {
    
    private static final String ARCHIVO_NOTIFICACIONES = "notificaciones_insta.dat";
    private ArrayList<Notificacion> notificaciones;
    
    public GestorNotificaciones() {
        cargarNotificaciones();
    }
    
    private void cargarNotificaciones() {
        File archivo = new File(ARCHIVO_NOTIFICACIONES);
        
        if (archivo.exists()) {
            try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(archivo))) {
                notificaciones = (ArrayList<Notificacion>) ois.readObject();
            } catch (Exception e) {
                System.err.println("Error al cargar notificaciones: " + e.getMessage());
                notificaciones = new ArrayList<>();
            }
        } else {
            notificaciones = new ArrayList<>();
        }
    }
    
    public void guardarNotificaciones() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(ARCHIVO_NOTIFICACIONES))) {
            oos.writeObject(notificaciones);
        } catch (Exception e) {
            System.err.println("Error al guardar notificaciones: " + e.getMessage());
        }
    }

    public void crearNotificacionLike(String usernameOrigen, String usernameDestino, String idPublicacion) {
        if (usernameOrigen.equals(usernameDestino)) {
            return;
        }
        
        for (Notificacion notif : notificaciones) {
            if (notif.getTipo() == TipoNotificacion.LIKE &&
                notif.getUsernameOrigen().equals(usernameOrigen) &&
                notif.getUsernameDestino().equals(usernameDestino) &&
                notif.getIdPublicacion().equals(idPublicacion)) {
                return;
            }
        }
        
        Notificacion notif = new Notificacion(TipoNotificacion.LIKE, usernameOrigen, usernameDestino, idPublicacion, null);
        notificaciones.add(notif);
        guardarNotificaciones();
    }

    public void eliminarNotificacionLike(String usernameOrigen, String usernameDestino, String idPublicacion) {
        notificaciones.removeIf(notif -> 
            notif.getTipo() == TipoNotificacion.LIKE &&
            notif.getUsernameOrigen().equals(usernameOrigen) &&
            notif.getUsernameDestino().equals(usernameDestino) &&
            notif.getIdPublicacion().equals(idPublicacion)
        );
        guardarNotificaciones();
    }

    public void crearNotificacionComentario(String usernameOrigen, String usernameDestino, 
                                           String idPublicacion, String contenidoComentario) {
        if (usernameOrigen.equals(usernameDestino)) {
            return;
        }
        
        Notificacion notif = new Notificacion(TipoNotificacion.COMENTARIO, usernameOrigen, 
                                              usernameDestino, idPublicacion, contenidoComentario);
        notificaciones.add(notif);
        guardarNotificaciones();
    }

    public void crearNotificacionMencion(String usernameOrigen, String usernameDestino, 
                                        String idPublicacion, String contenido) {
        // No crear notificación si el usuario se menciona a sí mismo
        if (usernameOrigen.equals(usernameDestino)) {
            return;
        }
        
        Notificacion notif = new Notificacion(TipoNotificacion.MENCION, usernameOrigen, 
                                              usernameDestino, idPublicacion, contenido);
        notificaciones.add(notif);
        guardarNotificaciones();
    }

    public void crearNotificacionSeguidor(String usernameOrigen, String usernameDestino) {
        for (Notificacion notif : notificaciones) {
            if (notif.getTipo() == TipoNotificacion.SEGUIDOR &&
                notif.getUsernameOrigen().equals(usernameOrigen) &&
                notif.getUsernameDestino().equals(usernameDestino)) {
                return; // Ya existe
            }
        }
        
        Notificacion notif = new Notificacion(TipoNotificacion.SEGUIDOR, usernameOrigen, usernameDestino);
        notificaciones.add(notif);
        guardarNotificaciones();
    }

    public void eliminarNotificacionSeguidor(String usernameOrigen, String usernameDestino) {
        notificaciones.removeIf(notif -> 
            notif.getTipo() == TipoNotificacion.SEGUIDOR &&
            notif.getUsernameOrigen().equals(usernameOrigen) &&
            notif.getUsernameDestino().equals(usernameDestino)
        );
        guardarNotificaciones();
    }

    public ArrayList<Notificacion> obtenerNotificaciones(String username) {
        ArrayList<Notificacion> notificacionesUsuario = new ArrayList<>();
        
        for (Notificacion notif : notificaciones) {
            if (notif.getUsernameDestino().equals(username)) {
                notificacionesUsuario.add(notif);
            }
        }
        
        Collections.sort(notificacionesUsuario, new Comparator<Notificacion>() {
            @Override
            public int compare(Notificacion n1, Notificacion n2) {
                return n2.getFechaCreacion().compareTo(n1.getFechaCreacion());
            }
        });
        
        return notificacionesUsuario;
    }

    public int contarNoLeidas(String username) {
        int count = 0;
        for (Notificacion notif : notificaciones) {
            if (notif.getUsernameDestino().equals(username) && !notif.isLeida()) {
                count++;
            }
        }
        return count;
    }
    
    /**
     * Marca todas las notificaciones de un usuario como leídas
     */
    public void marcarTodasComoLeidas(String username) {
        for (Notificacion notif : notificaciones) {
            if (notif.getUsernameDestino().equals(username)) {
                notif.setLeida(true);
            }
        }
        guardarNotificaciones();
    }

    public void limpiarNotificacionesAntiguas() {
        // En proceso
    }
}