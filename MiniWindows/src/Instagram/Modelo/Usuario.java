/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Instagram.Modelo;

import java.io.Serializable;

/**
 *
 * @author najma
 */
public class Usuario implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    private String username;
    private String password;
    private String nombreCompleto;
    private String email;
    private char genero;
    private int edad;
    private String biografia;
    private boolean activo;
    private String rutaImagenPerfil;
    
    public Usuario(String username, String password, char genero, int edad, String nombreCompleto, boolean activo) {
        this.username = username;
        this.password = password;
        this.genero = genero;
        this.edad = edad;
        this.nombreCompleto = nombreCompleto;
        this.activo = activo;
        this.email = "";
        this.biografia = "";
        this.rutaImagenPerfil = null;
    }
    
    public Usuario(String username, String password, String nombreCompleto, String email) {
        this.username = username;
        this.password = password;
        this.nombreCompleto = nombreCompleto;
        this.email = email;
        this.genero = 'O';
        this.edad = 18;
        this.activo = true;
        this.biografia = "";
        this.rutaImagenPerfil = null;
    }
    
    public String getUsername() {
        return username;
    }
    
    public void setUsername(String username) {
        this.username = username;
    }
    
    public String getPassword() {
        return password;
    }
    
    public void setPassword(String password) {
        this.password = password;
    }
    
    public String getNombreCompleto() {
        return nombreCompleto;
    }
    
    public void setNombreCompleto(String nombreCompleto) {
        this.nombreCompleto = nombreCompleto;
    }
    
    public String getEmail() {
        return email;
    }
    
    public void setEmail(String email) {
        this.email = email;
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
    
    public String getBiografia() {
        return biografia;
    }
    
    public void setBiografia(String biografia) {
        this.biografia = biografia;
    }
    
    public boolean isActivo() {
        return activo;
    }
    
    public void setActivo(boolean activo) {
        this.activo = activo;
    }
    
    public String getRutaImagenPerfil() {
        return rutaImagenPerfil;
    }
    
    public void setRutaImagenPerfil(String rutaImagenPerfil) {
        this.rutaImagenPerfil = rutaImagenPerfil;
    }
    
    @Override
    public String toString() {
        return "Usuario{" +
                "username='" + username + '\'' +
                ", nombreCompleto='" + nombreCompleto + '\'' +
                ", email='" + email + '\'' +
                ", activo=" + activo +
                '}';
    }
}