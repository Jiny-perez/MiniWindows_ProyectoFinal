package Instagram.Logica;

import Instagram.Modelo.Notificacion;
import Instagram.Modelo.Notificacion.TipoNotificacion;
import java.io.*;
import java.util.ArrayList;

/**
 *
 * @author najma
 */
public class GestorNotificaciones {

    private ArrayList<Notificacion> notificaciones;
    private static final String ARCHIVO_NOTIFICACIONES = "datos/notificaciones_insta.dat";

    public GestorNotificaciones() {
        this.notificaciones = new ArrayList<>();
        cargarNotificaciones();
    }

    public void agregarNotificacionLike(String usernameOrigen, String usernameDestino, String idPublicacion) {
        if (usernameOrigen == null || usernameDestino == null) {
            return;
        }
        if (usernameOrigen.equals(usernameDestino)) {
            return;
        }

        for (Notificacion notif : notificaciones) {
            if (notif.getUsuarioOrigen().equals(usernameOrigen)
                    && notif.getUsuarioDestino().equals(usernameDestino)
                    && notif.getIdReferencia() != null
                    && notif.getIdReferencia().equals(idPublicacion)
                    && notif.getTipo() == TipoNotificacion.LIKE) {
                return;
            }
        }

        Notificacion notif = new Notificacion(
                usernameDestino,
                usernameOrigen,
                TipoNotificacion.LIKE,
                "le dio like a tu publicación",
                idPublicacion
        );

        notificaciones.add(notif);
        guardarNotificaciones();
    }

    public void eliminarNotificacionLike(String usernameOrigen, String usernameDestino, String idPublicacion) {
        boolean changed = notificaciones.removeIf(notif
                -> notif.getUsuarioOrigen().equals(usernameOrigen)
                && notif.getUsuarioDestino().equals(usernameDestino)
                && notif.getIdReferencia() != null
                && notif.getIdReferencia().equals(idPublicacion)
                && notif.getTipo() == TipoNotificacion.LIKE
        );
        if (changed) {
            guardarNotificaciones();
        }
    }

    public void agregarNotificacionComentario(String usernameOrigen, String usernameDestino,
            String idPublicacion, String comentario) {
        if (usernameOrigen == null || usernameDestino == null) {
            return;
        }
        Notificacion notif = new Notificacion(
                usernameDestino,
                usernameOrigen,
                TipoNotificacion.COMENTARIO,
                "comentó: \"" + comentario + "\"",
                idPublicacion
        );

        notificaciones.add(notif);
        guardarNotificaciones();
    }

    public void agregarNotificacionMencion(String usernameOrigen, String usernameDestino,
            String idPublicacion, String contenido) {
        if (usernameOrigen == null || usernameDestino == null) {
            return;
        }
        Notificacion notif = new Notificacion(
                usernameDestino,
                usernameOrigen,
                TipoNotificacion.MENCION,
                "te mencionó en una publicación",
                idPublicacion
        );

        notificaciones.add(notif);
        guardarNotificaciones();
    }

    public void agregarNotificacionSeguidor(String usernameOrigen, String usernameDestino) {
        if (usernameOrigen == null || usernameDestino == null) {
            return;
        }
        if (usernameOrigen.equals(usernameDestino)) {
            return;
        }

        for (Notificacion notif : notificaciones) {
            if (notif.getUsuarioOrigen().equals(usernameOrigen)
                    && notif.getUsuarioDestino().equals(usernameDestino)
                    && notif.getTipo() == TipoNotificacion.SEGUIDOR) {
                return;
            }
        }

        Notificacion notif = new Notificacion(
                usernameDestino,
                usernameOrigen,
                TipoNotificacion.SEGUIDOR,
                "comenzó a seguirte"
        );

        notificaciones.add(notif);
        guardarNotificaciones();
    }

    public void eliminarNotificacionSeguidor(String usernameOrigen, String usernameDestino) {
        boolean changed = notificaciones.removeIf(notif
                -> notif.getUsuarioOrigen().equals(usernameOrigen)
                && notif.getUsuarioDestino().equals(usernameDestino)
                && notif.getTipo() == TipoNotificacion.SEGUIDOR
        );
        if (changed) {
            guardarNotificaciones();
        }
    }

    public ArrayList<Notificacion> obtenerNotificaciones(String username) {
        ArrayList<Notificacion> notifUsuario = new ArrayList<>();
        for (Notificacion notif : notificaciones) {
            if (notif.getUsuarioDestino().equals(username)) {
                notifUsuario.add(notif);
            }
        }
        notifUsuario.sort((n1, n2)
                -> n2.getFechaHora().compareTo(n1.getFechaHora())
        );
        return notifUsuario;
    }

    public int contarNoLeidas(String username) {
        int count = 0;
        for (Notificacion notif : notificaciones) {
            if (notif.getUsuarioDestino().equals(username) && !notif.isLeida()) {
                count++;
            }
        }
        return count;
    }

    public void marcarComoLeida(String username, String idNotificacion) {
        for (Notificacion notif : notificaciones) {
            if (notif.getId().equals(idNotificacion)
                    && notif.getUsuarioDestino().equals(username)) {
                notif.setLeida(true);
                guardarNotificaciones();
                return;
            }
        }
    }

    public void marcarTodasComoLeidas(String username) {
        boolean cambios = false;
        for (Notificacion notif : notificaciones) {
            if (notif.getUsuarioDestino().equals(username) && !notif.isLeida()) {
                notif.setLeida(true);
                cambios = true;
            }
        }
        if (cambios) {
            guardarNotificaciones();
        }
    }

    public void eliminarNotificacionesDeUsuario(String username) {
        boolean changed = notificaciones.removeIf(notif
                -> notif.getUsuarioOrigen().equals(username)
                || notif.getUsuarioDestino().equals(username)
        );
        if (changed) {
            guardarNotificaciones();
        }
    }

    public void eliminarNotificacionesDePublicacion(String idPublicacion) {
        boolean changed = notificaciones.removeIf(notif
                -> notif.getIdReferencia() != null
                && notif.getIdReferencia().equals(idPublicacion)
        );
        if (changed) {
            guardarNotificaciones();
        }
    }

    private void cargarNotificaciones() {
        File archivo = new File(ARCHIVO_NOTIFICACIONES);
        if (archivo.exists()) {
            try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(archivo))) {
                notificaciones = (ArrayList<Notificacion>) ois.readObject();
            } catch (IOException | ClassNotFoundException e) {
                notificaciones = new ArrayList<>();
            }
        }
    }

    private void guardarNotificaciones() {
        File archivo = new File(ARCHIVO_NOTIFICACIONES);
        File parent = archivo.getParentFile();
        if (parent != null && !parent.exists()) {
            parent.mkdirs();
        }

        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(archivo))) {
            oos.writeObject(notificaciones);
        } catch (IOException e) {
        }
    }
}
