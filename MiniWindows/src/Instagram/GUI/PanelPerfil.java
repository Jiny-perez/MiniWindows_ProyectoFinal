/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Instagram.GUI;

import Instagram.Modelo.Usuario;
import Instagram.Logica.GestorINSTACompleto;
import Instagram.Logica.GestorINSTACompleto.EstadisticasUsuario;
import Instagram.Logica.GestorUsuariosLocalINSTA;
import Instagram.Logica.GestorPerfiles;
import Instagram.Modelo.Publicacion;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.*;
import java.util.ArrayList;
import java.io.File;

/**
 *
 * @author najma
 */
public class PanelPerfil extends JPanel {
   
    private GestorINSTACompleto gestorINSTA;
    private GestorUsuariosLocalINSTA gestorUsuarios;
    private GestorPerfiles gestorPerfiles;
    private VentanaINSTA ventanaPrincipal;
    private String usernameDelPerfil;
    
    private JLabel lblAvatar;
    private JLabel lblUsername;
    private JLabel lblNombreCompleto;
    private JLabel lblBiografia;
    private JLabel lblPublicaciones;
    private JLabel lblSeguidores;
    private JLabel lblSiguiendo;
    private JButton btnAccion;
    private JPanel panelPublicaciones;
    private JScrollPane scrollPane;
    
    private static final Color BACKGROUND_COLOR = new Color(255, 240, 245);
    private static final Color CARD_COLOR = Color.WHITE;
    private static final Color BORDER_COLOR = new Color(255, 192, 203);
    private static final Color TEXT_PRIMARY = new Color(38, 38, 38);
    private static final Color TEXT_SECONDARY = new Color(142, 142, 142);
    private static final Color INSTAGRAM_PINK = new Color(242, 80, 129);
    
    public PanelPerfil(GestorINSTACompleto gestor, GestorUsuariosLocalINSTA gestorUsuariosLocal, String username, VentanaINSTA ventana) {
        this.gestorINSTA = gestor;
        this.gestorUsuarios = gestorUsuariosLocal;
        this.gestorPerfiles = new GestorPerfiles();
        this.usernameDelPerfil = username;
        this.ventanaPrincipal = ventana;
        
        initComponents();
        actualizarContenido();
    }
    
    private void initComponents() {
        setLayout(new BorderLayout());
        setBackground(BACKGROUND_COLOR);

        JPanel panelSuperior = crearPanelSuperior();
        add(panelSuperior, BorderLayout.NORTH);

        JPanel panelCentral = new JPanel(new BorderLayout());
        panelCentral.setBackground(BACKGROUND_COLOR);
        panelCentral.setBorder(BorderFactory.createEmptyBorder(16, 16, 16, 16));

        JLabel lblTitulo = new JLabel("Publicaciones");
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 18));
        lblTitulo.setForeground(INSTAGRAM_PINK);
        lblTitulo.setBorder(BorderFactory.createEmptyBorder(0, 6, 12, 0));
        panelCentral.add(lblTitulo, BorderLayout.NORTH);

        panelPublicaciones = new JPanel();
        panelPublicaciones.setLayout(new GridLayout(0, 3, 10, 10));
        panelPublicaciones.setBackground(BACKGROUND_COLOR);

        scrollPane = new JScrollPane(panelPublicaciones,
                JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
                JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.setBorder(null);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        scrollPane.getViewport().setBackground(BACKGROUND_COLOR);
        scrollPane.setBackground(BACKGROUND_COLOR);

        panelCentral.add(scrollPane, BorderLayout.CENTER);
        add(panelCentral, BorderLayout.CENTER);
    }
    
    private JPanel crearPanelSuperior() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(CARD_COLOR);
        panel.setBorder(new CompoundBorder(
            new MatteBorder(0, 0, 2, 0, BORDER_COLOR),
            BorderFactory.createEmptyBorder(30, 40, 30, 40)
        ));
        
        JPanel panelInfo = new JPanel(new BorderLayout(20, 0));
        panelInfo.setBackground(CARD_COLOR);
        panelInfo.setMaximumSize(new Dimension(800, 150));
        
        lblAvatar = new JLabel();
        lblAvatar.setPreferredSize(new Dimension(120, 120));
        lblAvatar.setHorizontalAlignment(SwingConstants.CENTER);
        
        JPanel panelDatos = new JPanel();
        panelDatos.setLayout(new BoxLayout(panelDatos, BoxLayout.Y_AXIS));
        panelDatos.setBackground(CARD_COLOR);
        
        // ---------- FILA: USERNAME + BOTÓN A LA DERECHA ----------
        lblUsername = new JLabel();
        lblUsername.setFont(new Font("Segoe UI", Font.BOLD, 28));
        lblUsername.setForeground(TEXT_PRIMARY);
        
        btnAccion = new JButton();
        btnAccion.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnAccion.setFocusPainted(false);
        btnAccion.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnAccion.setPreferredSize(new Dimension(140, 32));
        btnAccion.setMaximumSize(new Dimension(160, 32));
        btnAccion.setMinimumSize(new Dimension(120, 32));
        
        JPanel panelUsuario = new JPanel();
        panelUsuario.setLayout(new BoxLayout(panelUsuario, BoxLayout.X_AXIS));
        panelUsuario.setBackground(CARD_COLOR);
        panelUsuario.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        panelUsuario.add(lblUsername);
        panelUsuario.add(Box.createHorizontalStrut(15));
        panelUsuario.add(Box.createHorizontalGlue());
        panelUsuario.add(btnAccion);
        // ---------------------------------------------------------
        
        lblNombreCompleto = new JLabel();
        lblNombreCompleto.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        lblNombreCompleto.setForeground(TEXT_SECONDARY);
        lblNombreCompleto.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        JPanel panelEstadisticas = new JPanel(new FlowLayout(FlowLayout.LEFT, 40, 0));
        panelEstadisticas.setBackground(CARD_COLOR);
        panelEstadisticas.setAlignmentX(Component.LEFT_ALIGNMENT);
        panelEstadisticas.setMaximumSize(new Dimension(600, 40));
        
        lblPublicaciones = crearLabelEstadistica("0", "Publicaciones");
        lblSeguidores = crearLabelEstadistica("0", "Seguidores");
        lblSiguiendo = crearLabelEstadistica("0", "Siguiendo");
        
        panelEstadisticas.add(lblPublicaciones);
        panelEstadisticas.add(lblSeguidores);
        panelEstadisticas.add(lblSiguiendo);
        
        lblBiografia = new JLabel();
        lblBiografia.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        lblBiografia.setForeground(TEXT_PRIMARY);
        lblBiografia.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        panelDatos.add(panelUsuario);               // fila con nombre + botón
        panelDatos.add(Box.createVerticalStrut(5));
        panelDatos.add(lblNombreCompleto);
        panelDatos.add(Box.createVerticalStrut(15));
        panelDatos.add(panelEstadisticas);
        panelDatos.add(Box.createVerticalStrut(12));
        panelDatos.add(lblBiografia);
        
        panelInfo.add(lblAvatar, BorderLayout.WEST);
        panelInfo.add(panelDatos, BorderLayout.CENTER);
        
        panel.add(panelInfo);
        
        return panel;
    }
    
    private JLabel crearLabelEstadistica(String valor, String texto) {
        JLabel label = new JLabel("<html><b style='font-size:16px'>" + valor + "</b> <span style='color:rgb(142,142,142)'>" + texto + "</span></html>");
        label.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        label.setForeground(TEXT_PRIMARY);
        return label;
    }
    
    public void actualizarContenido() {
        Usuario usuario = gestorUsuarios.obtenerUsuario(usernameDelPerfil);
        
        if (usuario == null) {
            mostrarPerfilNoEncontrado();
            return;
        }
        
        lblUsername.setText("@" + usuario.getUsername());
        lblNombreCompleto.setText(usuario.getNombreCompleto());
        
        // Cargar biografía desde GestorPerfiles (más actualizada)
        String biografia = gestorPerfiles.obtenerBiografia(usernameDelPerfil);
        if (biografia == null || biografia.trim().isEmpty()) {
            biografia = usuario.getBiografia();
        }
        
        if (biografia != null && !biografia.trim().isEmpty()) {
            lblBiografia.setText("<html>" + biografia.replace("\n", "<br>") + "</html>");
        } else {
            lblBiografia.setText("");
        }
        
        // Cargar imagen de perfil desde GestorPerfiles
        cargarImagenPerfil();
        
        EstadisticasUsuario stats = gestorINSTA.obtenerEstadisticas(usernameDelPerfil);
        
        lblPublicaciones.setText("<html><b style='font-size:16px'>" + stats.publicaciones + "</b> <span style='color:rgb(142,142,142)'>Publicaciones</span></html>");
        lblSeguidores.setText("<html><b style='font-size:16px'>" + stats.seguidores + "</b> <span style='color:rgb(142,142,142)'>Seguidores</span></html>");
        lblSiguiendo.setText("<html><b style='font-size:16px'>" + stats.siguiendo + "</b> <span style='color:rgb(142,142,142)'>Siguiendo</span></html>");
        
        configurarBotonAccion();
        cargarPublicaciones();
    }
    
    private void cargarImagenPerfil() {
        String rutaImagen = gestorPerfiles.obtenerImagenPerfil(usernameDelPerfil);
        
        if (rutaImagen != null && !rutaImagen.isEmpty()) {
            File archivoImagen = new File(rutaImagen);
            if (archivoImagen.exists()) {
                try {
                    ImageIcon icon = new ImageIcon(rutaImagen);
                    Image img = icon.getImage().getScaledInstance(120, 120, Image.SCALE_SMOOTH);
                    lblAvatar.setIcon(new ImageIcon(img));
                    lblAvatar.setText(null);
                    return;
                } catch (Exception e) {
                    System.err.println("Error al cargar imagen de perfil: " + e.getMessage());
                }
            }
        }
        
        try {
            ImageIcon avatarIcon = new ImageIcon(getClass().getResource("/Instagram/icons/icon_perfil.png"));
            Image img = avatarIcon.getImage().getScaledInstance(120, 120, Image.SCALE_SMOOTH);
            lblAvatar.setIcon(new ImageIcon(img));
            lblAvatar.setText(null);
        } catch (Exception e) {
            lblAvatar.setIcon(null);
            lblAvatar.setText("👤");
            lblAvatar.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 80));
            lblAvatar.setForeground(INSTAGRAM_PINK);
        }
    }
    
    private void configurarBotonAccion() {
        String usernameActual = gestorINSTA.getUsernameActual();
        
        btnAccion.setVisible(true);
        
        for (ActionListener al : btnAccion.getActionListeners()) {
            btnAccion.removeActionListener(al);
        }
        
        if (usernameActual != null && usernameDelPerfil.equals(usernameActual)) {
            btnAccion.setText("Editar Perfil");
            btnAccion.setForeground(TEXT_PRIMARY);
            btnAccion.setBackground(CARD_COLOR);
            btnAccion.setBorder(new LineBorder(BORDER_COLOR, 2));
            
            btnAccion.addActionListener(e -> editarPerfil());
        } else {
            boolean estaSiguiendo = gestorINSTA.estaSiguiendo(usernameDelPerfil);
            btnAccion.setText(estaSiguiendo ? "Siguiendo" : "Seguir");
            
            if (estaSiguiendo) {
                btnAccion.setForeground(TEXT_PRIMARY);
                btnAccion.setBackground(CARD_COLOR);
                btnAccion.setBorder(new LineBorder(BORDER_COLOR, 2));
            } else {
                btnAccion.setForeground(Color.WHITE);
                btnAccion.setBackground(INSTAGRAM_PINK);
                btnAccion.setBorder(null);
            }
            
            btnAccion.addActionListener(e -> {
                gestorINSTA.toggleSeguir(usernameDelPerfil);
                actualizarContenido();
            });
        }
    }
    
    private void editarPerfil() {
        Usuario usuario = gestorUsuarios.obtenerUsuario(usernameDelPerfil);
        if (usuario != null) {
            DialogEditarPerfil dialog = new DialogEditarPerfil(
                (Frame) SwingUtilities.getWindowAncestor(this),
                usuario,
                gestorUsuarios
            );
            dialog.setVisible(true);

            if (dialog.fueActualizado()) {
                actualizarContenido();
            }
        }
    }
    
    private void cargarPublicaciones() {
        panelPublicaciones.removeAll();

        ArrayList<Publicacion> publicaciones = gestorINSTA.obtenerPublicacionesDeUsuario(usernameDelPerfil);

        if (publicaciones == null || publicaciones.isEmpty()) {
            panelPublicaciones.setLayout(new GridLayout(1, 1));
            JLabel lblVacio = new JLabel("No hay publicaciones para mostrar", SwingConstants.CENTER);
            lblVacio.setFont(new Font("Segoe UI", Font.PLAIN, 16));
            lblVacio.setForeground(TEXT_SECONDARY);
            JPanel p = new JPanel(new BorderLayout());
            p.setBackground(BACKGROUND_COLOR);
            p.add(lblVacio, BorderLayout.CENTER);
            panelPublicaciones.add(p);
        } else {
            panelPublicaciones.setLayout(new GridLayout(0, 3, 10, 10));

            for (Publicacion publicacion : publicaciones) {
                TarjetaPublicacion tarjeta = new TarjetaPublicacion(publicacion, gestorINSTA, ventanaPrincipal);

                tarjeta.setPreferredSize(new Dimension(240, 240));
                tarjeta.setMaximumSize(new Dimension(240, 240));
                tarjeta.setMinimumSize(new Dimension(240, 240));
                tarjeta.setOpaque(true);
                panelPublicaciones.add(tarjeta);
            }

            int resto = publicaciones.size() % 3;
            if (resto != 0) {
                int faltan = 3 - resto;
                for (int i = 0; i < faltan; i++) {
                    JPanel filler = new JPanel();
                    filler.setBackground(BACKGROUND_COLOR);
                    panelPublicaciones.add(filler);
                }
            }
        }

        panelPublicaciones.revalidate();
        panelPublicaciones.repaint();

        SwingUtilities.invokeLater(() -> scrollPane.getVerticalScrollBar().setValue(0));
    }
    
    private void mostrarPerfilNoEncontrado() {
        lblUsername.setText("Usuario no encontrado");
        lblNombreCompleto.setText("");
        lblBiografia.setText("");
        lblPublicaciones.setText("<html><b>0</b> Publicaciones</html>");
        lblSeguidores.setText("<html><b>0</b> Seguidores</html>");
        lblSiguiendo.setText("<html><b>0</b> Siguiendo</html>");
        btnAccion.setVisible(false);
        
        panelPublicaciones.removeAll();
        panelPublicaciones.revalidate();
        panelPublicaciones.repaint();
    }
}