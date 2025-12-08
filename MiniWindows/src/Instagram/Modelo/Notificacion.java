package Instagram.Modelo;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 *
 * @author najma
 */
public class Notificacion implements Serializable {
     
    private static final long serialVersionUID = 1L;
    
    public enum TipoNotificacion {
        LIKE,
        COMENTARIO,
        SEGUIDOR,
        MENCION,
        MENSAJE
    }
    
    private String id;
    private String usuarioDestino;
    private String usuarioOrigen;
    private TipoNotificacion tipo;
    private String mensaje;
    private String idReferencia;
    private LocalDateTime fechaHora;
    private boolean leida;
    
    public Notificacion(String usuarioDestino, String usuarioOrigen, TipoNotificacion tipo, String mensaje) {
        this.id = UUID.randomUUID().toString();
        this.usuarioDestino = usuarioDestino;
        this.usuarioOrigen = usuarioOrigen;
        this.tipo = tipo;
        this.mensaje = mensaje;
        this.fechaHora = LocalDateTime.now();
        this.leida = false;
        this.idReferencia = null;
    }
    
    public Notificacion(String usuarioDestino, String usuarioOrigen, TipoNotificacion tipo, String mensaje, String idReferencia) {
        this.id = UUID.randomUUID().toString();
        this.usuarioDestino = usuarioDestino;
        this.usuarioOrigen = usuarioOrigen;
        this.tipo = tipo;
        this.mensaje = mensaje;
        this.idReferencia = idReferencia;
        this.fechaHora = LocalDateTime.now();
        this.leida = false;
    }
    
    public String getId() {
        return id;
    }
    
    public String getUsuarioDestino() {
        return usuarioDestino;
    }
    
    public void setUsuarioDestino(String usuarioDestino) {
        this.usuarioDestino = usuarioDestino;
    }
    
    public String getUsuarioOrigen() {
        return usuarioOrigen;
    }
    
    public void setUsuarioOrigen(String usuarioOrigen) {
        this.usuarioOrigen = usuarioOrigen;
    }
    
    public TipoNotificacion getTipo() {
        return tipo;
    }
    
    public void setTipo(TipoNotificacion tipo) {
        this.tipo = tipo;
    }
    
    public String getMensaje() {
        return mensaje;
    }
    
    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }
    
    public String getIdReferencia() {
        return idReferencia;
    }
    
    public void setIdReferencia(String idReferencia) {
        this.idReferencia = idReferencia;
    }
    
    public LocalDateTime getFechaHora() {
        return fechaHora;
    }
    
    public void setFechaHora(LocalDateTime fechaHora) {
        this.fechaHora = fechaHora;
    }
    
    public boolean isLeida() {
        return leida;
    }
    
    public void setLeida(boolean leida) {
        this.leida = leida;
    }
    
    
    public String toString() {
        return "Notificacion{" +
                "id='" + id + '\'' +
                ", usuarioDestino='" + usuarioDestino + '\'' +
                ", usuarioOrigen='" + usuarioOrigen + '\'' +
                ", tipo=" + tipo +
                ", mensaje='" + mensaje + '\'' +
                ", fechaHora=" + fechaHora +
                ", leida=" + leida +
                '}';
    }
}
