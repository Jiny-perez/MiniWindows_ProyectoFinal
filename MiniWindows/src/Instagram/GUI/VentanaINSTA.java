/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Instagram.GUI;

import Instagram.Modelo.Usuario;
import Instagram.Logica.GestorINSTACompleto;
import Instagram.Logica.GestorUsuariosLocalINSTA;
import Instagram.Logica.GestorNotificaciones;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.*;

/**
 *
 * @author najma
 */
public class VentanaINSTA extends JFrame {
    
    private GestorINSTACompleto gestorINSTA;
    private GestorUsuariosLocalINSTA gestorUsuarios;
    private GestorNotificaciones gestorNotificaciones;
    private Usuario usuarioActual;
    
    private JPanel panelLateral;
    private JPanel panelContenido;
    private CardLayout cardLayout;
    
    private PanelTimeline panelTimeline;
    private PanelExplorar panelExplorar;
    private PanelPerfil panelPerfil;
    private PanelMensajes panelMensajes;
    private PanelNotificaciones panelNotificaciones;
    
    private static final Color INSTAGRAM_PINK = new Color(242, 80, 129);
    private static final Color BACKGROUND_COLOR = new Color(255, 240, 245);
    private static final Color SIDEBAR_COLOR = Color.WHITE;
    private static final Color BORDER_COLOR = new Color(255, 192, 203);
    private static final Color TEXT_PRIMARY = new Color(38, 38, 38);
    private static final Color TEXT_SECONDARY = new Color(142, 142, 142);
    private static final Color HOVER_COLOR = new Color(255, 230, 240);
    
    public VentanaINSTA(Usuario usuario, GestorINSTACompleto gestor, GestorUsuariosLocalINSTA gestorUsuariosLocal) {
        this.usuarioActual = usuario;
        this.gestorINSTA = gestor;
        this.gestorUsuarios = gestorUsuariosLocal;
        this.gestorNotificaciones = new GestorNotificaciones();
        
        initComponents();
        configurarVentana();
    }
    
    private void initComponents() {
        setLayout(new BorderLayout());
        getContentPane().setBackground(BACKGROUND_COLOR);
        
        crearPanelLateral();
        
        cardLayout = new CardLayout();
        panelContenido = new JPanel(cardLayout);
        panelContenido.setBackground(BACKGROUND_COLOR);
        
        panelTimeline = new PanelTimeline(gestorINSTA, this);
        panelExplorar = new PanelExplorar(gestorINSTA, gestorUsuarios, this);
        panelPerfil = new PanelPerfil(gestorINSTA, gestorUsuarios, usuarioActual.getUsername(), this);
        panelMensajes = new PanelMensajes(gestorINSTA, gestorUsuarios, this);
        panelNotificaciones = new PanelNotificaciones(gestorINSTA, gestorNotificaciones, this);
        
        panelContenido.add(panelTimeline, "TIMELINE");
        panelContenido.add(panelExplorar, "EXPLORAR");
        panelContenido.add(panelPerfil, "PERFIL");
        panelContenido.add(panelMensajes, "MENSAJES");
        panelContenido.add(panelNotificaciones, "NOTIFICACIONES");
        
        add(panelLateral, BorderLayout.WEST);
        add(panelContenido, BorderLayout.CENTER);
        
        SwingUtilities.invokeLater(() -> mostrarTimeline());
    }
    
    private void crearPanelLateral() {
        panelLateral = new JPanel();
        panelLateral.setLayout(new BoxLayout(panelLateral, BoxLayout.Y_AXIS));
        panelLateral.setBackground(SIDEBAR_COLOR);
        panelLateral.setBorder(new MatteBorder(0, 0, 0, 1, BORDER_COLOR));
        panelLateral.setPreferredSize(new Dimension(245, 600));
        
        JLabel lblLogo = new JLabel();
        try {
            ImageIcon logoIcon = new ImageIcon(getClass().getResource("/Instagram/icons/icon_insta.png"));
            Image img = logoIcon.getImage().getScaledInstance(50, 50, Image.SCALE_SMOOTH);
            lblLogo.setIcon(new ImageIcon(img));
        } catch (Exception e) {
            lblLogo.setText("Instagram");
            lblLogo.setFont(new Font("Brush Script MT", Font.ITALIC, 32));
            lblLogo.setForeground(INSTAGRAM_PINK);
        }
        lblLogo.setAlignmentX(Component.LEFT_ALIGNMENT);
        lblLogo.setBorder(BorderFactory.createEmptyBorder(30, 20, 30, 20));
        panelLateral.add(lblLogo);
        
        panelLateral.add(crearBotonNavegacion("Inicio", "/Instagram/icons/icon_inicio.png", e -> mostrarTimeline()));
        panelLateral.add(Box.createVerticalStrut(5));
        
        panelLateral.add(crearBotonNavegacion("Explorar", "/Instagram/icons/icon_explorar.png", e -> mostrarExplorar()));
        panelLateral.add(Box.createVerticalStrut(5));
        
        panelLateral.add(crearBotonNavegacion("Mensajes", "/Instagram/icons/icon_mensajeria.png", e -> mostrarMensajes()));
        panelLateral.add(Box.createVerticalStrut(5));
        
        panelLateral.add(crearBotonNavegacion("Notificaciones", "/Instagram/icons/icon_notificacion.png", e -> mostrarNotificaciones()));
        panelLateral.add(Box.createVerticalStrut(5));
        
        panelLateral.add(crearBotonNavegacion("Crear", "/Instagram/icons/icon_crear.png", e -> crearPublicacion()));
        panelLateral.add(Box.createVerticalStrut(5));
        
        panelLateral.add(crearBotonNavegacion("Perfil", "/Instagram/icons/icon_perfil.png", e -> mostrarPerfil()));
        panelLateral.add(Box.createVerticalStrut(5));
        
        panelLateral.add(Box.createVerticalGlue());
        
        panelLateral.add(crearBotonNavegacion("Cerrar Sesión", "/Instagram/icons/icon_cerrar_sesion.png", e -> cerrarSesion()));
        panelLateral.add(Box.createVerticalStrut(20));
    }
    
    private JButton crearBotonNavegacion(String texto, String rutaIcono, ActionListener action) {
        JButton btn = new JButton(texto);
        
        try {
            ImageIcon icono = new ImageIcon(getClass().getResource(rutaIcono));
            Image img = icono.getImage().getScaledInstance(24, 24, Image.SCALE_SMOOTH);
            btn.setIcon(new ImageIcon(img));
            btn.setHorizontalTextPosition(SwingConstants.RIGHT);
            btn.setIconTextGap(15);
        } catch (Exception e) {
            System.err.println("No se pudo cargar el icono: " + rutaIcono);
        }
        
        btn.setAlignmentX(Component.LEFT_ALIGNMENT);
        btn.setMaximumSize(new Dimension(225, 50));
        btn.setPreferredSize(new Dimension(225, 50));
        btn.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        btn.setForeground(TEXT_PRIMARY);
        btn.setBackground(SIDEBAR_COLOR);
        btn.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 20));
        btn.setHorizontalAlignment(SwingConstants.LEFT);
        btn.setFocusPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        btn.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                btn.setBackground(HOVER_COLOR);
            }
            
            @Override
            public void mouseExited(MouseEvent e) {
                btn.setBackground(SIDEBAR_COLOR);
            }
        });
        
        btn.addActionListener(action);
        
        return btn;
    }
    
    public void mostrarTimeline() {
        cardLayout.show(panelContenido, "TIMELINE");
        panelTimeline.actualizarContenido();
    }
    
    public void mostrarExplorar() {
        cardLayout.show(panelContenido, "EXPLORAR");
        panelExplorar.actualizarContenido();
    }
    
    public void mostrarPerfil() {
        cardLayout.show(panelContenido, "PERFIL");
        panelPerfil.actualizarContenido();
    }
    
    public void mostrarMensajes() {
        cardLayout.show(panelContenido, "MENSAJES");
        panelMensajes.actualizarContenido();
    }
    
    public void mostrarNotificaciones() {
        cardLayout.show(panelContenido, "NOTIFICACIONES");
        panelNotificaciones.actualizarContenido();
    }
    
    private void crearPublicacion() {
        DialogCrearPublicacion dialog = new DialogCrearPublicacion(this, gestorINSTA);
        dialog.setVisible(true);
        
        if (dialog.isPublicacionCreada()) {
            mostrarTimeline();
        }
    }
    
    private void cerrarSesion() {
        int opcion = JOptionPane.showConfirmDialog(
            this,
            "¿Estás seguro de que deseas cerrar sesión?",
            "Cerrar Sesión",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.QUESTION_MESSAGE
        );
        
        if (opcion == JOptionPane.YES_OPTION) {
            gestorINSTA.guardarDatos();
            dispose();
            
            SwingUtilities.invokeLater(() -> {
                VentanaLogin ventanaLogin = new VentanaLogin();
                ventanaLogin.setVisible(true);
            });
        }
    }
    
    private void configurarVentana() {
        setTitle("Instagram - @" + usuarioActual.getUsername());
        setSize(1200, 800);
        setMinimumSize(new Dimension(1000, 600));
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
    }
    
    public void mostrarPerfilDeUsuario(String username) {
        PanelPerfil perfilUsuario = new PanelPerfil(gestorINSTA, gestorUsuarios, username, this);
        panelContenido.add(perfilUsuario, "PERFIL_" + username);
        cardLayout.show(panelContenido, "PERFIL_" + username);
        perfilUsuario.actualizarContenido();
    }
    
    public Usuario getUsuarioActual() {
        return usuarioActual;
    }
    
    public GestorINSTACompleto getGestorINSTA() {
        return gestorINSTA;
    }
    
    public GestorNotificaciones getGestorNotificaciones() {
        return gestorNotificaciones;
    }
}
