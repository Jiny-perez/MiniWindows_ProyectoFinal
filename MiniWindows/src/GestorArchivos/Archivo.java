package GestorArchivos;

import java.io.Serializable;
import java.util.Calendar;

/**
 *
 * @author najma
 */
public class Archivo implements Serializable {

    private static final long serialVersionUID = 1L;
    private String nombre;
    private boolean esCarpeta;
    private long tamanio;
    private Calendar fechaModificacion;
    private String rutaRelativa;
    private String rutaAbsoluta;

    public Archivo(String nombre, boolean esCarpeta, long tamanio, Calendar fechaModificacion, String rutaRelativa, String rutaAbsoluta) {
        this.nombre = nombre;
        this.esCarpeta = esCarpeta;
        this.tamanio = tamanio;
        this.fechaModificacion = fechaModificacion;
        this.rutaRelativa = rutaRelativa;
        this.rutaAbsoluta = rutaAbsoluta;
    }

    public String getNombre() {
        return nombre;
    }

    public boolean isEsCarpeta() {
        return esCarpeta;
    }

    public long getTamanio() {
        return tamanio;
    }

    public Calendar getFechaModificacion() {
        return fechaModificacion;
    }

    public Calendar setFechaModificacion(Calendar fechaModificacion) {
        return fechaModificacion=fechaModificacion;
    }

    public String getRutaRelativa() {
        return rutaRelativa;
    }

    public String getRutaAbsoluta() {
        return rutaAbsoluta;
    }
}
