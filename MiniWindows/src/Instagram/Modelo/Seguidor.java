/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Instagram.Modelo;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 *
 * @author najma
 */
public class Seguidor implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    private String seguidor;
    private String seguido;
    private LocalDateTime fechaSeguimiento;
    
    public Seguidor(String seguidor, String seguido) {
        this.seguidor = seguidor;
        this.seguido = seguido;
        this.fechaSeguimiento = LocalDateTime.now();
    }
    
    public boolean involucra(String username) {
        return seguidor.equals(username) || seguido.equals(username);
    }
    
    public boolean esSeguidor(String username) {
        return seguidor.equals(username);
    }
    
    public boolean esSeguido(String username) {
        return seguido.equals(username);
    }
    
    public boolean esRelacion(String user1, String user2) {
        return (seguidor.equals(user1) && seguido.equals(user2));
    }
    
    
    public String getSeguidor() {
        return seguidor;
    }
    
    public String getSeguido() {
        return seguido;
    }
    
    public LocalDateTime getFechaSeguimiento() {
        return fechaSeguimiento;
    }
    
    @Override
    public String toString() {
        return "@" + seguidor + " sigue a @" + seguido;
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        
        Seguidor other = (Seguidor) obj;
        return seguidor.equals(other.seguidor) && seguido.equals(other.seguido);
    }
    
    @Override
    public int hashCode() {
        return (seguidor + seguido).hashCode();
    }
}