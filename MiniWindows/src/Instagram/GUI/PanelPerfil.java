/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Instagram.GUI;

import Instagram.Modelo.Usuario;
import Instagram.Logica.GestorINSTACompleto;
import Instagram.Logica.GestorINSTACompleto.EstadisticasUsuario;
import Instagram.Logica.GestorUsuariosLocalINSTA;
import Instagram.Modelo.Publicacion;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.*;
import java.util.ArrayList;

/**
 *
 * @author najma
 */
public class PanelPerfil extends JPanel {
   
    private GestorINSTACompleto gestorINSTA;
    private GestorUsuariosLocalINSTA gestorUsuarios;
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
        this.usernameDelPerfil = username;
        this.ventanaPrincipal = ventana;
        
        initComponents();
    }
    
   private void initComponents() {
    setLayout(new BorderLayout());
    setBackground(BACKGROUND_COLOR);

    // panel superior (perfil)
    JPanel panelSuperior = crearPanelSuperior();
    add(panelSuperior, BorderLayout.NORTH);

    // Contenedor central con padding
    JPanel panelCentral = new JPanel(new BorderLayout());
    panelCentral.setBackground(BACKGROUND_COLOR);
    panelCentral.setBorder(BorderFactory.createEmptyBorder(16, 16, 16, 16));

    // Título fijo (fuera del scroll)
    JLabel lblTitulo = new JLabel("Publicaciones");
    lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 18));
    lblTitulo.setForeground(INSTAGRAM_PINK);
    lblTitulo.setBorder(BorderFactory.createEmptyBorder(0, 6, 12, 0));
    panelCentral.add(lblTitulo, BorderLayout.NORTH);

    // panelPublicaciones = GRID DIRECTO (0 filas dinámicas, 3 columnas)
    panelPublicaciones = new JPanel();
    panelPublicaciones.setLayout(new GridLayout(0, 3, 10, 10)); // 3 columnas, gaps 10px
    panelPublicaciones.setBackground(BACKGROUND_COLOR);

    // JScrollPane con el grid directo como viewport view
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
        try {
            ImageIcon avatarIcon = new ImageIcon(getClass().getResource("/Instagram/icons/icon_perfil.png"));
            Image img = avatarIcon.getImage().getScaledInstance(120, 120, Image.SCALE_SMOOTH);
            lblAvatar.setIcon(new ImageIcon(img));
        } catch (Exception e) {
            lblAvatar.setText("👤");
            lblAvatar.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 80));
            lblAvatar.setForeground(INSTAGRAM_PINK);
        }
        lblAvatar.setPreferredSize(new Dimension(120, 120));
        lblAvatar.setHorizontalAlignment(SwingConstants.CENTER);
        
        JPanel panelDatos = new JPanel();
        panelDatos.setLayout(new BoxLayout(panelDatos, BoxLayout.Y_AXIS));
        panelDatos.setBackground(CARD_COLOR);
        
        lblUsername = new JLabel();
        lblUsername.setFont(new Font("Segoe UI", Font.BOLD, 28));
        lblUsername.setForeground(TEXT_PRIMARY);
        lblUsername.setAlignmentX(Component.LEFT_ALIGNMENT);
        
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
        
        panelDatos.add(lblUsername);
        panelDatos.add(Box.createVerticalStrut(5));
        panelDatos.add(lblNombreCompleto);
        panelDatos.add(Box.createVerticalStrut(15));
        panelDatos.add(panelEstadisticas);
        panelDatos.add(Box.createVerticalStrut(12));
        panelDatos.add(lblBiografia);
        
        btnAccion = new JButton();
        btnAccion.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnAccion.setFocusPainted(false);
        btnAccion.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnAccion.setAlignmentX(Component.LEFT_ALIGNMENT);
        btnAccion.setMaximumSize(new Dimension(180, 36));
        
        panelDatos.add(Box.createVerticalStrut(15));
        panelDatos.add(btnAccion);
        
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
        
        String biografia = usuario.getBiografia();
        if (biografia != null && !biografia.trim().isEmpty()) {
            lblBiografia.setText("<html>" + biografia.replace("\n", "<br>") + "</html>");
        } else {
            lblBiografia.setText("");
        }
        
        EstadisticasUsuario stats = gestorINSTA.obtenerEstadisticas(usernameDelPerfil);
        
        lblPublicaciones.setText("<html><b style='font-size:16px'>" + stats.publicaciones + "</b> <span style='color:rgb(142,142,142)'>Publicaciones</span></html>");
        lblSeguidores.setText("<html><b style='font-size:16px'>" + stats.seguidores + "</b> <span style='color:rgb(142,142,142)'>Seguidores</span></html>");
        lblSiguiendo.setText("<html><b style='font-size:16px'>" + stats.siguiendo + "</b> <span style='color:rgb(142,142,142)'>Siguiendo</span></html>");
        
        configurarBotonAccion();
        
        cargarPublicaciones();
    }
    
    private void configurarBotonAccion() {
        String usernameActual = gestorINSTA.getUsernameActual();
        
        for (ActionListener al : btnAccion.getActionListeners()) {
            btnAccion.removeActionListener(al);
        }
        
        if (usernameDelPerfil.equals(usernameActual)) {
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
        }
    }
    
  private void cargarPublicaciones() {
    panelPublicaciones.removeAll();

    ArrayList<Publicacion> publicaciones = gestorINSTA.obtenerPublicacionesDeUsuario(usernameDelPerfil);

    if (publicaciones == null || publicaciones.isEmpty()) {
        // si no hay publicaciones, mostramos mensaje centrado ocupando las 3 columnas
        panelPublicaciones.setLayout(new GridLayout(1, 1));
        JLabel lblVacio = new JLabel("No hay publicaciones para mostrar", SwingConstants.CENTER);
        lblVacio.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        lblVacio.setForeground(TEXT_SECONDARY);
        JPanel p = new JPanel(new BorderLayout());
        p.setBackground(BACKGROUND_COLOR);
        p.add(lblVacio, BorderLayout.CENTER);
        panelPublicaciones.add(p);
    } else {
        // aseguramos layout GRID 3 columnas
        panelPublicaciones.setLayout(new GridLayout(0, 3, 10, 10));

        for (Publicacion publicacion : publicaciones) {
            TarjetaPublicacion tarjeta = new TarjetaPublicacion(publicacion, gestorINSTA, ventanaPrincipal);

            // Recomendado: que cada tarjeta tenga un tamaño preferido cuadrado (ajusta 240 a lo que prefieras)
            tarjeta.setPreferredSize(new Dimension(240, 240));
            tarjeta.setMaximumSize(new Dimension(240, 240));
            tarjeta.setMinimumSize(new Dimension(240, 240));

            // Asegurarse que la tarjeta pinta su fondo (si tiene transparencia)
            tarjeta.setOpaque(true);
            panelPublicaciones.add(tarjeta);
        }

        // Si la última fila queda incompleta, añadimos panels invisibles para mantener alineación
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

    // volver al top del scroll
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
