/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Instagram.Logica;

import Instagram.Modelo.Seguidor;
import java.util.ArrayList;
import java.util.stream.Collectors;

/**
 *
 * @author najma
 */
public class GestorSeguidores {
    
    private ArrayList<Seguidor> relaciones;
    
    public GestorSeguidores() {
        this.relaciones = new ArrayList<>();
    }
    
    public boolean seguir(String seguidor, String seguido) {
        if (seguidor.equals(seguido)) {
            return false;
        }
        
        if (estaSiguiendo(seguidor, seguido)) {
            return false;
        }
        
        Seguidor nuevaRelacion = new Seguidor(seguidor, seguido);
        relaciones.add(nuevaRelacion);
        return true;
    }
    
    public boolean dejarDeSeguir(String seguidor, String seguido) {
        return relaciones.removeIf(r -> r.esRelacion(seguidor, seguido));
    }
    
    public boolean toggleSeguir(String seguidor, String seguido) {
        if (estaSiguiendo(seguidor, seguido)) {
            return dejarDeSeguir(seguidor, seguido);
        } else {
            return seguir(seguidor, seguido);
        }
    }
    
    public boolean estaSiguiendo(String seguidor, String seguido) {
        return relaciones.stream()
            .anyMatch(r -> r.esRelacion(seguidor, seguido));
    }
    
    public boolean esSeguimientoMutuo(String user1, String user2) {
        return estaSiguiendo(user1, user2) && estaSiguiendo(user2, user1);
    }
    
    public ArrayList<String> obtenerSiguiendo(String username) {
        return relaciones.stream()
            .filter(r -> r.esSeguidor(username))
            .map(Seguidor::getSeguido)
            .collect(Collectors.toCollection(ArrayList::new));
    }
    
    public ArrayList<String> obtenerSeguidores(String username) {
        return relaciones.stream()
            .filter(r -> r.esSeguido(username))
            .map(Seguidor::getSeguidor)
            .collect(Collectors.toCollection(ArrayList::new));
    }
    
    public ArrayList<String> obtenerAmigos(String username) {
        ArrayList<String> siguiendo = obtenerSiguiendo(username);
        ArrayList<String> seguidores = obtenerSeguidores(username);
        
        siguiendo.retainAll(seguidores);
        return siguiendo;
    }
    
    public int contarSiguiendo(String username) {
        return (int) relaciones.stream()
            .filter(r -> r.esSeguidor(username))
            .count();
    }
    
    public int contarSeguidores(String username) {
        return (int) relaciones.stream()
            .filter(r -> r.esSeguido(username))
            .count();
    }
    
    public int contarAmigos(String username) {
        return obtenerAmigos(username).size();
    }
    
    public ArrayList<String> sugerirUsuarios(String username, ArrayList<String> todosLosUsuarios) {
        ArrayList<String> siguiendo = obtenerSiguiendo(username);
        ArrayList<String> sugerencias = new ArrayList<>();
        
        for (String usuarioSeguido : siguiendo) {
            ArrayList<String> siguenEllos = obtenerSiguiendo(usuarioSeguido);
            
            for (String candidato : siguenEllos) {
                if (!candidato.equals(username) && 
                    !siguiendo.contains(candidato) && 
                    !sugerencias.contains(candidato)) {
                    sugerencias.add(candidato);
                }
            }
        }
        
        if (sugerencias.size() < 5) {
            for (String usuario : todosLosUsuarios) {
                if (!usuario.equals(username) && 
                    !siguiendo.contains(usuario) && 
                    !sugerencias.contains(usuario)) {
                    sugerencias.add(usuario);
                    
                    if (sugerencias.size() >= 5) {
                        break;
                    }
                }
            }
        }
        
        if (sugerencias.size() > 5) {
            return new ArrayList<>(sugerencias.subList(0, 5));
        }
        
        return sugerencias;
    }
    
    public ArrayList<String> obtenerMasPopulares(ArrayList<String> todosLosUsuarios, int cantidad) {
        return todosLosUsuarios.stream()
            .sorted((u1, u2) -> Integer.compare(contarSeguidores(u2), contarSeguidores(u1)))
            .limit(cantidad)
            .collect(Collectors.toCollection(ArrayList::new));
    }
    
    public void eliminarTodasRelacionesDe(String username) {
        relaciones.removeIf(r -> r.involucra(username));
    }
    
    public ArrayList<Seguidor> getRelaciones() {
        return new ArrayList<>(relaciones);
    }
    
    public int getTotalRelaciones() {
        return relaciones.size();
    }
    
    public void setRelaciones(ArrayList<Seguidor> relaciones) {
        this.relaciones = relaciones;
    }
}