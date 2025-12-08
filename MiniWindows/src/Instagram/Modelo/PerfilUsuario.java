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
public class PerfilUsuario implements Serializable {
   
    private static final long serialVersionUID = 1L;
    
    private String username;
    private String biografia;
    private String rutaImagenPerfil;
    
    public PerfilUsuario(String username) {
        this.username = username;
        this.biografia = "";
        this.rutaImagenPerfil = null;
    }
    
    public String getUsername() {
        return username;
    }
    
    public void setUsername(String username) {
        this.username = username;
    }
    
    public String getBiografia() {
        return biografia;
    }
    
    public void setBiografia(String biografia) {
        this.biografia = biografia;
    }
    
    public String getRutaImagenPerfil() {
        return rutaImagenPerfil;
    }
    
    public void setRutaImagenPerfil(String rutaImagenPerfil) {
        this.rutaImagenPerfil = rutaImagenPerfil;
    }
    
    @Override
    public String toString() {
        return "PerfilUsuario{" +
                "username='" + username + '\'' +
                ", biografia='" + biografia + '\'' +
                ", rutaImagenPerfil='" + rutaImagenPerfil + '\'' +
                '}';
    }
}