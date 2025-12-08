/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Instagram.Modelo;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;

/**
 *
 * @author najma
 */
public class Conversacion implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    private String usuario1;
    private String usuario2;
    private ArrayList<Mensaje> mensajes;
    private LocalDateTime ultimaActualizacion;
    
    public Conversacion(String usuario1, String usuario2) {
        this.usuario1 = usuario1;
        this.usuario2 = usuario2;
        this.mensajes = new ArrayList<>();
        this.ultimaActualizacion = LocalDateTime.now();
    }
    
    public void agregarMensaje(Mensaje mensaje) {
        mensajes.add(mensaje);
        ultimaActualizacion = LocalDateTime.now();
    }
    
    public String getOtroUsuario(String usuarioActual) {
        if (usuarioActual.equals(usuario1)) {
            return usuario2;
        } else {
            return usuario1;
        }
    }
    
    public boolean involucraUsuarios(String user1, String user2) {
        return (usuario1.equals(user1) && usuario2.equals(user2)) ||
               (usuario1.equals(user2) && usuario2.equals(user1));
    }
    
    public String getUsuario1() {
        return usuario1;
    }
    
    public String getUsuario2() {
        return usuario2;
    }
    
    public ArrayList<Mensaje> getMensajes() {
        return new ArrayList<>(mensajes);
    }
    
    public LocalDateTime getUltimaActualizacion() {
        return ultimaActualizacion;
    }
    
    public Mensaje getUltimoMensaje() {
        if (mensajes.isEmpty()) {
            return null;
        }
        return mensajes.get(mensajes.size() - 1);
    }
    
    @Override
    public String toString() {
        return "Conversacion{" +
                "usuario1='" + usuario1 + '\'' +
                ", usuario2='" + usuario2 + '\'' +
                ", mensajes=" + mensajes.size() +
                ", ultimaActualizacion=" + ultimaActualizacion +
                '}';
    }
}
