/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Instagram.Modelo;

import java.io.Serializable;
import java.util.Date;

/**
 *
 * @author najma
 */
public class Usuario implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    private String username;
    private String nombreCompleto;
    private char genero;
    private int edad;
    private String password;
    private Date fechaEntrada;
    private boolean activo;
    private String rutaFotoPerfil;
    
    public Usuario(String username, String nombreCompleto, char genero, int edad, String password, boolean activo) {
        this.username = username;
        this.nombreCompleto = nombreCompleto;
        this.genero = genero;
        this.edad = edad;
        this.password = password;
        this.activo = activo;
        this.fechaEntrada = new Date();
        this.rutaFotoPerfil = null;
    }
    
    public String getUsername() {
        return username;
    }
    
    public void setUsername(String username) {
        this.username = username;
    }
    
    public String getNombreCompleto() {
        return nombreCompleto;
    }
    
    public void setNombreCompleto(String nombreCompleto) {
        this.nombreCompleto = nombreCompleto;
    }
    
    public char getGenero() {
        return genero;
    }
    
    public void setGenero(char genero) {
        this.genero = genero;
    }
    
    public int getEdad() {
        return edad;
    }
    
    public void setEdad(int edad) {
        this.edad = edad;
    }
    
    public String getPassword() {
        return password;
    }
    
    public void setPassword(String password) {
        this.password = password;
    }
    
    public Date getFechaEntrada() {
        return fechaEntrada;
    }
    
    public boolean isActivo() {
        return activo;
    }
    
    public void setActivo(boolean activo) {
        this.activo = activo;
    }
    
    public String getRutaFotoPerfil() {
        return rutaFotoPerfil;
    }
    
    public void setRutaFotoPerfil(String rutaFotoPerfil) {
        this.rutaFotoPerfil = rutaFotoPerfil;
    }
    
    public boolean tieneFotoPersonalizada() {
        return rutaFotoPerfil != null && !rutaFotoPerfil.isEmpty();
    }
    
    @Override
    public String toString() {
        return "Usuario{" +
                "username='" + username + '\'' +
                ", nombreCompleto='" + nombreCompleto + '\'' +
                ", genero=" + genero +
                ", edad=" + edad +
                ", activo=" + activo +
                '}';
    }
}