/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Instagram.GUI;

import Instagram.Logica.GestorINSTA;
import Instagram.Logica.GestorINSTA.EstadisticasUsuario;
import Instagram.Logica.GestorUsuariosLocal;
import Instagram.Modelo.Publicacion;
import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.awt.image.BufferedImage;
import javax.swing.*;
import javax.swing.border.*;
import java.util.ArrayList;

/**
 *
 * @author najma
 */
public class PanelPerfil extends JPanel {
   
    private GestorINSTA gestorINSTA;
    private GestorUsuariosLocal gestorUsuarios;
    private String usernameVisualizando;
    private VentanaINSTA ventanaPrincipal;
    
    private JPanel panelEstadisticas;
    private JPanel panelPublicaciones;
    private JPanel panelHeader;
    private JPanel panelPrincipal;
    private JButton btnSeguir;
    private JScrollPane scrollPane;
    
    // Theme Rosa de Instagram
    private static final Color BACKGROUND_COLOR = new Color(255, 240, 245); // Rosa pastel
    private static final Color CARD_COLOR = Color.WHITE;
    private static final Color BORDER_COLOR = new Color(255, 192, 203);
    private static final Color TEXT_PRIMARY = new Color(38, 38, 38);
    private static final Color TEXT_SECONDARY = new Color(142, 142, 142);
    private static final Color INSTAGRAM_PINK = new Color(242, 80, 129);
    
    public PanelPerfil(GestorINSTA gestor, GestorUsuariosLocal gestorUsuariosLocal, String username, VentanaINSTA ventana) {
        this.gestorINSTA = gestor;
        this.gestorUsuarios = gestorUsuariosLocal;
        this.usernameVisualizando = username;
        this.ventanaPrincipal = ventana;
        
        initComponents();
    }
    
    private void initComponents() {
        setLayout(new BorderLayout());
        setBackground(BACKGROUND_COLOR);
        
        panelPrincipal = new JPanel();
        panelPrincipal.setLayout(new BoxLayout(panelPrincipal, BoxLayout.Y_AXIS));
        panelPrincipal.setBackground(BACKGROUND_COLOR);
        
        panelHeader = crearHeader();
        panelPrincipal.add(panelHeader);
        panelPrincipal.add(Box.createVerticalStrut(20));
        
        JSeparator separador = new JSeparator();
        separador.setForeground(BORDER_COLOR);
        separador.setMaximumSize(new Dimension(800, 1));
        panelPrincipal.add(separador);
        panelPrincipal.add(Box.createVerticalStrut(20));
        
        panelPublicaciones = new JPanel();
        panelPublicaciones.setLayout(new BoxLayout(panelPublicaciones, BoxLayout.Y_AXIS));
        panelPublicaciones.setBackground(BACKGROUND_COLOR);
        panelPublicaciones.setBorder(BorderFactory.createEmptyBorder(0, 20, 20, 20));
        
        panelPrincipal.add(panelPublicaciones);
        
        scrollPane = new JScrollPane(panelPrincipal);
        scrollPane.setBorder(null);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        
        add(scrollPane, BorderLayout.CENTER);
    }
    
    private JPanel crearHeader() {
        JPanel header = new JPanel();
        header.setLayout(new BoxLayout(header, BoxLayout.Y_AXIS));
        header.setBackground(CARD_COLOR);
        header.setBorder(BorderFactory.createEmptyBorder(40, 40, 40, 40));
        header.setMaximumSize(new Dimension(800, 300));
        header.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        JPanel panelSuperior = new JPanel(new FlowLayout(FlowLayout.LEFT, 30, 0));
        panelSuperior.setBackground(CARD_COLOR);
        panelSuperior.setMaximumSize(new Dimension(800, 160));
        
        // Foto de perfil con icono según género
        // Foto de perfil con icono según género o foto personalizada
        JLabel fotoPerfil = new JLabel();
        try {
            // Obtener el usuario para saber su género y foto
            Instagram.Modelo.Usuario usuario = gestorUsuarios.obtenerUsuario(usernameVisualizando);
            
            if (usuario != null && usuario.tieneFotoPersonalizada()) {
                // Mostrar foto personalizada CIRCULAR
                ImageIcon avatarIcon = new ImageIcon(usuario.getRutaFotoPerfil());
                Image img = avatarIcon.getImage().getScaledInstance(150, 150, Image.SCALE_SMOOTH);
                fotoPerfil.setIcon(crearImagenCircular(img, 150));
            } else if (usuario != null) {
                // Mostrar icono según género CIRCULAR
                String iconoPath = "/Instagram.icons/icon_perfil.png"; // Default
                
                if (usuario.getGenero() == 'F') {
                    iconoPath = "/Instagram.icons/icon_usuario_mujer.png";
                } else if (usuario.getGenero() == 'M') {
                    iconoPath = "/Instagram.icons/icon_usuario_hombre.png";
                }
                
                ImageIcon avatarIcon = new ImageIcon(getClass().getResource(iconoPath));
                Image img = avatarIcon.getImage().getScaledInstance(150, 150, Image.SCALE_SMOOTH);
                fotoPerfil.setIcon(crearImagenCircular(img, 150));
            }
        } catch (Exception e) {
            // Fallback: usar icono genérico CIRCULAR
            try {
                ImageIcon avatarIcon = new ImageIcon(getClass().getResource("/Instagram.icons/icon_perfil.png"));
                Image img = avatarIcon.getImage().getScaledInstance(150, 150, Image.SCALE_SMOOTH);
                fotoPerfil.setIcon(crearImagenCircular(img, 150));
            } catch (Exception e2) {
                fotoPerfil.setText("👤");
                fotoPerfil.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 100));
                fotoPerfil.setForeground(INSTAGRAM_PINK);
            }
        }
        fotoPerfil.setPreferredSize(new Dimension(150, 150));
        
        JPanel panelInfo = new JPanel();
        panelInfo.setLayout(new BoxLayout(panelInfo, BoxLayout.Y_AXIS));
        panelInfo.setBackground(CARD_COLOR);
        
        JLabel lblUsername = new JLabel("@" + usernameVisualizando);
        lblUsername.setFont(new Font("Segoe UI", Font.BOLD, 28));
        lblUsername.setForeground(INSTAGRAM_PINK);
        lblUsername.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        panelInfo.add(lblUsername);
        panelInfo.add(Box.createVerticalStrut(10));
        
        // Obtener y mostrar nombre completo y biografía
        Instagram.Modelo.Usuario usuarioInfo = gestorUsuarios.obtenerUsuario(usernameVisualizando);
        Instagram.Modelo.PerfilUsuario perfilInfo = Instagram.Logica.GestorPerfiles.cargarPerfil(usernameVisualizando);
        
        if (usuarioInfo != null) {
            JLabel lblNombre = new JLabel(usuarioInfo.getNombreCompleto());
            lblNombre.setFont(new Font("Segoe UI", Font.PLAIN, 16));
            lblNombre.setForeground(TEXT_SECONDARY);
            lblNombre.setAlignmentX(Component.LEFT_ALIGNMENT);
            panelInfo.add(lblNombre);
        }
        
        // Mostrar biografía si existe
        if (perfilInfo != null && perfilInfo.getBiografia() != null && !perfilInfo.getBiografia().trim().isEmpty()) {
            panelInfo.add(Box.createVerticalStrut(8));
            
            JTextArea txtBiografia = new JTextArea(perfilInfo.getBiografia());
            txtBiografia.setFont(new Font("Segoe UI", Font.PLAIN, 14));
            txtBiografia.setForeground(TEXT_PRIMARY);
            txtBiografia.setBackground(CARD_COLOR);
            txtBiografia.setLineWrap(true);
            txtBiografia.setWrapStyleWord(true);
            txtBiografia.setEditable(false);
            
            // Establecer ancho primero para calcular líneas correctamente
            txtBiografia.setSize(new Dimension(400, Short.MAX_VALUE));
            
            // Calcular altura preferida según el contenido
            int alturaPreferida = txtBiografia.getPreferredSize().height;
            int alturaNecesaria = Math.min(alturaPreferida + 10, 300); // Max 300px
            
            txtBiografia.setPreferredSize(new Dimension(400, alturaNecesaria));
            txtBiografia.setMaximumSize(new Dimension(400, alturaNecesaria));
            txtBiografia.setAlignmentX(Component.LEFT_ALIGNMENT);
            panelInfo.add(txtBiografia);
        }
        
        panelInfo.add(Box.createVerticalStrut(20));
        
        // Botón editar perfil solo para el usuario actual
        if (usernameVisualizando.equals(gestorINSTA.getUsernameActual())) {
            JButton btnEditarPerfil = new JButton("Editar perfil");
            btnEditarPerfil.setFont(new Font("Segoe UI", Font.BOLD, 14));
            btnEditarPerfil.setAlignmentX(Component.LEFT_ALIGNMENT);
            btnEditarPerfil.setForeground(TEXT_PRIMARY);
            btnEditarPerfil.setBackground(CARD_COLOR);
            btnEditarPerfil.setBorder(new CompoundBorder(
                new LineBorder(BORDER_COLOR, 2),
                BorderFactory.createEmptyBorder(8, 24, 8, 24)
            ));
            btnEditarPerfil.setFocusPainted(false);
            btnEditarPerfil.setCursor(new Cursor(Cursor.HAND_CURSOR));
            btnEditarPerfil.addActionListener(e -> {
                Instagram.Modelo.Usuario usuarioActual = gestorUsuarios.obtenerUsuario(usernameVisualizando);
                DialogEditarPerfil dialog = new DialogEditarPerfil(
                    (Frame) SwingUtilities.getWindowAncestor(this),
                    usuarioActual,
                    gestorUsuarios
                );
                dialog.setVisible(true);
                
                // Si se guardaron cambios, actualizar la vista
                if (dialog.isCambiosGuardados()) {
                    actualizarContenido();
                    
                    // Si desactivó la cuenta, cerrar sesión
                    if (!usuarioActual.isActivo()) {
                        JOptionPane.showMessageDialog(
                            this,
                            "Tu cuenta ha sido desactivada. Se cerrará la sesión.",
                            "Cuenta Desactivada",
                            JOptionPane.INFORMATION_MESSAGE
                        );
                        ventanaPrincipal.dispose();
                        
                        SwingUtilities.invokeLater(() -> {
                            VentanaLogin ventanaLogin = new VentanaLogin();
                            ventanaLogin.setVisible(true);
                        });
                    }
                }
            });
            
            panelInfo.add(btnEditarPerfil);
        }
        
        // Botón seguir solo si no es el usuario actual
        else {
            boolean estaSiguiendo = gestorINSTA.estaSiguiendo(usernameVisualizando);
            
            btnSeguir = new JButton(estaSiguiendo ? "Siguiendo" : "Seguir");
            btnSeguir.setFont(new Font("Segoe UI", Font.BOLD, 14));
            btnSeguir.setAlignmentX(Component.LEFT_ALIGNMENT);
            
            if (estaSiguiendo) {
                btnSeguir.setForeground(TEXT_PRIMARY);
                btnSeguir.setBackground(CARD_COLOR);
                btnSeguir.setBorder(new CompoundBorder(
                    new LineBorder(BORDER_COLOR, 2),
                    BorderFactory.createEmptyBorder(8, 24, 8, 24)
                ));
            } else {
                btnSeguir.setForeground(Color.WHITE);
                btnSeguir.setBackground(INSTAGRAM_PINK);
                btnSeguir.setBorder(BorderFactory.createEmptyBorder(8, 24, 8, 24));
            }
            
            btnSeguir.setFocusPainted(false);
            btnSeguir.setCursor(new Cursor(Cursor.HAND_CURSOR));
            btnSeguir.addActionListener(e -> toggleSeguir());
            
            panelInfo.add(btnSeguir);
        }
        
        panelSuperior.add(fotoPerfil);
        panelSuperior.add(panelInfo);
        
        header.add(panelSuperior);
        header.add(Box.createVerticalStrut(30));
        header.add(crearPanelEstadisticas());
        
        return header;
    }
    
    private JPanel crearPanelEstadisticas() {
        panelEstadisticas = new JPanel(new FlowLayout(FlowLayout.CENTER, 40, 0));
        panelEstadisticas.setBackground(CARD_COLOR);
        panelEstadisticas.setMaximumSize(new Dimension(600, 50));
        
        EstadisticasUsuario stats = gestorINSTA.obtenerEstadisticas(usernameVisualizando);
        
        panelEstadisticas.add(crearEstadistica(String.valueOf(stats.cantidadPublicaciones), "publicaciones"));
        panelEstadisticas.add(crearEstadistica(String.valueOf(stats.cantidadSeguidores), "seguidores"));
        panelEstadisticas.add(crearEstadistica(String.valueOf(stats.cantidadSiguiendo), "siguiendo"));
        
        return panelEstadisticas;
    }
    
    private JPanel crearEstadistica(String valor, String etiqueta) {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(CARD_COLOR);
        
        JLabel lblValor = new JLabel(valor);
        lblValor.setFont(new Font("Segoe UI", Font.BOLD, 18));
        lblValor.setForeground(INSTAGRAM_PINK);
        lblValor.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        JLabel lblEtiqueta = new JLabel(etiqueta);
        lblEtiqueta.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        lblEtiqueta.setForeground(TEXT_SECONDARY);
        lblEtiqueta.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        panel.add(lblValor);
        panel.add(lblEtiqueta);
        
        return panel;
    }
    
    private void toggleSeguir() {
        gestorINSTA.toggleSeguir(usernameVisualizando);
        
        boolean ahoraSiguiendo = gestorINSTA.estaSiguiendo(usernameVisualizando);
        btnSeguir.setText(ahoraSiguiendo ? "Siguiendo" : "Seguir");
        
        if (ahoraSiguiendo) {
            btnSeguir.setForeground(TEXT_PRIMARY);
            btnSeguir.setBackground(CARD_COLOR);
            btnSeguir.setBorder(new CompoundBorder(
                new LineBorder(BORDER_COLOR, 2),
                BorderFactory.createEmptyBorder(8, 24, 8, 24)
            ));
        } else {
            btnSeguir.setForeground(Color.WHITE);
            btnSeguir.setBackground(INSTAGRAM_PINK);
            btnSeguir.setBorder(BorderFactory.createEmptyBorder(8, 24, 8, 24));
        }
        
        actualizarEstadisticas();
    }
    
    private void actualizarEstadisticas() {
        panelEstadisticas.removeAll();
        
        EstadisticasUsuario stats = gestorINSTA.obtenerEstadisticas(usernameVisualizando);
        
        panelEstadisticas.add(crearEstadistica(String.valueOf(stats.cantidadPublicaciones), "publicaciones"));
        panelEstadisticas.add(crearEstadistica(String.valueOf(stats.cantidadSeguidores), "seguidores"));
        panelEstadisticas.add(crearEstadistica(String.valueOf(stats.cantidadSiguiendo), "siguiendo"));
        
        panelEstadisticas.revalidate();
        panelEstadisticas.repaint();
    }
    
    public void actualizarContenido() {
        // Actualizar header (foto de perfil y biografía)
        int headerIndex = 0; // El header es el primer componente
        panelPrincipal.remove(headerIndex);
        panelHeader = crearHeader();
        panelPrincipal.add(panelHeader, headerIndex);
        
        // Actualizar publicaciones
        panelPublicaciones.removeAll();
        
        ArrayList<Publicacion> publicaciones = gestorINSTA.obtenerPublicacionesDeUsuario(usernameVisualizando);
        
        if (publicaciones.isEmpty()) {
            mostrarMensajeVacio();
        } else {
            JLabel lblTitulo = new JLabel("Publicaciones");
            lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 16));
            lblTitulo.setForeground(INSTAGRAM_PINK);
            lblTitulo.setAlignmentX(Component.CENTER_ALIGNMENT);
            lblTitulo.setBorder(BorderFactory.createEmptyBorder(0, 0, 16, 0));
            
            panelPublicaciones.add(lblTitulo);
            
            for (Publicacion publicacion : publicaciones) {
                TarjetaPublicacion tarjeta = new TarjetaPublicacion(publicacion, gestorINSTA, ventanaPrincipal);
                panelPublicaciones.add(tarjeta);
                panelPublicaciones.add(Box.createVerticalStrut(15));
            }
        }
        
        actualizarEstadisticas();
        
        // Refrescar ambos paneles
        panelPrincipal.revalidate();
        panelPrincipal.repaint();
        panelPublicaciones.revalidate();
        panelPublicaciones.repaint();
        
        SwingUtilities.invokeLater(() -> scrollPane.getVerticalScrollBar().setValue(0));
    }
    
    private void mostrarMensajeVacio() {
        JPanel panelVacio = new JPanel();
        panelVacio.setLayout(new BoxLayout(panelVacio, BoxLayout.Y_AXIS));
        panelVacio.setBackground(BACKGROUND_COLOR);
        panelVacio.setBorder(BorderFactory.createEmptyBorder(60, 0, 60, 0));
        
        // Icono de fotos
        JLabel lblIconoFotos = new JLabel();
        try {
            ImageIcon iconoFotos = new ImageIcon(getClass().getResource("/Instagram.icons/icon_fotos.png"));
            Image img = iconoFotos.getImage().getScaledInstance(120, 120, Image.SCALE_SMOOTH);
            lblIconoFotos.setIcon(new ImageIcon(img));
        } catch (Exception e) {
            lblIconoFotos.setText("📷");
            lblIconoFotos.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 80));
            lblIconoFotos.setForeground(TEXT_SECONDARY);
        }
        lblIconoFotos.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        JLabel lblTitulo = new JLabel("Share Photos");
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 24));
        lblTitulo.setForeground(TEXT_PRIMARY);
        lblTitulo.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        JLabel lblMensaje = new JLabel("When you share photos, they will appear on your profile.");
        lblMensaje.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        lblMensaje.setForeground(TEXT_SECONDARY);
        lblMensaje.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        panelVacio.add(lblIconoFotos);
        panelVacio.add(Box.createVerticalStrut(20));
        panelVacio.add(lblTitulo);
        panelVacio.add(Box.createVerticalStrut(10));
        panelVacio.add(lblMensaje);
        
        panelPublicaciones.add(panelVacio);
    }
    
    private ImageIcon crearImagenCircular(Image img, int diametro) {
        BufferedImage buffered = new BufferedImage(diametro, diametro, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = buffered.createGraphics();
        
        // Activar antialiasing para bordes suaves
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        
        // Crear clip circular
        Ellipse2D.Double circle = new Ellipse2D.Double(0, 0, diametro, diametro);
        g2.setClip(circle);
        
        // Dibujar imagen dentro del círculo
        g2.drawImage(img, 0, 0, diametro, diametro, null);
        g2.dispose();
        
        return new ImageIcon(buffered);
    }
}
 