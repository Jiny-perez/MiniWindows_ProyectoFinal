/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Instagram.GUI;

import Instagram.Logica.GestorINSTA;
import Instagram.Logica.GestorMensajes;
import Instagram.Logica.GestorUsuariosLocal;
import Instagram.Modelo.Mensaje;
import Instagram.Modelo.Usuario;
import java.awt.*;
import javax.swing.*;
import javax.swing.border.*;
import java.util.ArrayList;

/**
 *
 * @author najma
 */
public class PanelMensajes extends JPanel {
    private GestorINSTA gestorINSTA;
    private GestorUsuariosLocal gestorUsuarios;
    private VentanaINSTA ventanaPrincipal;
    private GestorMensajes gestorMensajes;
    
    private JPanel panelConversaciones;
    private JTextField txtBuscarUsuario;
    
    private static final Color BACKGROUND_COLOR = new Color(255, 240, 245);
    private static final Color CARD_COLOR = Color.WHITE;
    private static final Color BORDER_COLOR = new Color(255, 192, 203);
    private static final Color TEXT_PRIMARY = new Color(38, 38, 38);
    private static final Color TEXT_SECONDARY = new Color(142, 142, 142);
    private static final Color INSTAGRAM_PINK = new Color(242, 80, 129);
    
    public PanelMensajes(GestorINSTA gestor, GestorUsuariosLocal gestorUsuariosLocal, VentanaINSTA ventana) {
        this.gestorINSTA = gestor;
        this.gestorUsuarios = gestorUsuariosLocal;
        this.ventanaPrincipal = ventana;
        this.gestorMensajes = new GestorMensajes();
        
        initComponents();
    }
    
    public void actualizarContenido() {
        panelConversaciones.removeAll();
        
        String usernameActual = gestorINSTA.getUsernameActual();
        ArrayList<String> conversaciones = gestorMensajes.obtenerConversaciones(usernameActual);
        
        if (conversaciones.isEmpty()) {
            mostrarMensajeVacio();
        } else {
            for (String otroUsuario : conversaciones) {
                JPanel tarjeta = crearTarjetaConversacion(otroUsuario);
                panelConversaciones.add(tarjeta);
                panelConversaciones.add(Box.createVerticalStrut(8));
            }
        }
        
        panelConversaciones.revalidate();
        panelConversaciones.repaint();
    }
    
    private void initComponents() {
        setLayout(new BorderLayout());
        setBackground(BACKGROUND_COLOR);
        
        JPanel header = new JPanel();
        header.setLayout(new BoxLayout(header, BoxLayout.Y_AXIS));
        header.setBackground(CARD_COLOR);
        header.setBorder(new CompoundBorder(
            new MatteBorder(0, 0, 1, 0, BORDER_COLOR),
            BorderFactory.createEmptyBorder(20, 20, 20, 20)
        ));
        
        JLabel lblTitulo = new JLabel("Mensajes");
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 24));
        lblTitulo.setForeground(INSTAGRAM_PINK);
        lblTitulo.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        JPanel panelBuscar = new JPanel(new BorderLayout(8, 0));
        panelBuscar.setBackground(CARD_COLOR);
        panelBuscar.setMaximumSize(new Dimension(800, 45));
        panelBuscar.setPreferredSize(new Dimension(800, 45));
        panelBuscar.setAlignmentX(Component.LEFT_ALIGNMENT);
        panelBuscar.setBorder(BorderFactory.createEmptyBorder(12, 0, 0, 0));
        
        txtBuscarUsuario = new JTextField();
        txtBuscarUsuario.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        txtBuscarUsuario.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(BORDER_COLOR, 2),
            BorderFactory.createEmptyBorder(8, 12, 8, 12)
        ));
        txtBuscarUsuario.addActionListener(e -> buscarUsuarioParaChat());
        
        JButton btnNuevoChat = new JButton("Nuevo Chat");
        btnNuevoChat.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnNuevoChat.setForeground(Color.WHITE);
        btnNuevoChat.setBackground(INSTAGRAM_PINK);
        btnNuevoChat.setBorder(BorderFactory.createEmptyBorder(8, 16, 8, 16));
        btnNuevoChat.setFocusPainted(false);
        btnNuevoChat.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnNuevoChat.addActionListener(e -> buscarUsuarioParaChat());
        
        panelBuscar.add(txtBuscarUsuario, BorderLayout.CENTER);
        panelBuscar.add(btnNuevoChat, BorderLayout.EAST);
        
        header.add(lblTitulo);
        header.add(panelBuscar);
        
        add(header, BorderLayout.NORTH);
        
        panelConversaciones = new JPanel();
        panelConversaciones.setLayout(new BoxLayout(panelConversaciones, BoxLayout.Y_AXIS));
        panelConversaciones.setBackground(BACKGROUND_COLOR);
        panelConversaciones.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        JScrollPane scrollPane = new JScrollPane(panelConversaciones);
        scrollPane.setBorder(null);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        
        add(scrollPane, BorderLayout.CENTER);
    }
    
    private JPanel crearTarjetaConversacion(String otroUsuario) {
        JPanel tarjeta = new JPanel(new BorderLayout(12, 0));
        tarjeta.setBackground(CARD_COLOR);
        tarjeta.setBorder(new CompoundBorder(
            new LineBorder(BORDER_COLOR, 2),
            BorderFactory.createEmptyBorder(12, 12, 12, 12)
        ));
        tarjeta.setMaximumSize(new Dimension(700, 80));
        tarjeta.setAlignmentX(Component.LEFT_ALIGNMENT);
        tarjeta.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        JLabel lblAvatar = new JLabel();
        try {
            Usuario usuario = gestorUsuarios.obtenerUsuario(otroUsuario);
            
            if (usuario != null && usuario.tieneFotoPersonalizada()) {
                ImageIcon avatarIcon = new ImageIcon(usuario.getRutaFotoPerfil());
                Image img = avatarIcon.getImage().getScaledInstance(48, 48, Image.SCALE_SMOOTH);
                lblAvatar.setIcon(new ImageIcon(img));
            } else if (usuario != null) {
                String iconoPath = "/Instagram/icons/icon_perfil.png";
                if (usuario.getGenero() == 'F') {
                    iconoPath = "/Instagram/icons/icon_usuario_mujer.png";
                } else if (usuario.getGenero() == 'M') {
                    iconoPath = "/Instagram/icons/icon_usuario_hombre.png";
                }
                ImageIcon avatarIcon = new ImageIcon(getClass().getResource(iconoPath));
                Image img = avatarIcon.getImage().getScaledInstance(48, 48, Image.SCALE_SMOOTH);
                lblAvatar.setIcon(new ImageIcon(img));
            } else {
                lblAvatar.setText("👤");
                lblAvatar.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 36));
            }
        } catch (Exception e) {
            lblAvatar.setText("👤");
            lblAvatar.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 36));
        }
        lblAvatar.setPreferredSize(new Dimension(48, 48));
        
        JPanel panelInfo = new JPanel();
        panelInfo.setLayout(new BoxLayout(panelInfo, BoxLayout.Y_AXIS));
        panelInfo.setBackground(CARD_COLOR);
        
        JLabel lblUsername = new JLabel("@" + otroUsuario);
        lblUsername.setFont(new Font("Segoe UI", Font.BOLD, 16));
        lblUsername.setForeground(TEXT_PRIMARY);
        lblUsername.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        String usernameActual = gestorINSTA.getUsernameActual();
        Mensaje ultimoMensaje = gestorMensajes.obtenerUltimoMensaje(usernameActual, otroUsuario);
        
        String textoPreview = "Inicia una conversación";
        String tiempo = "";
        
        if (ultimoMensaje != null) {
            String contenido = ultimoMensaje.getContenido();
            if (contenido.length() > 40) {
                contenido = contenido.substring(0, 37) + "...";
            }
            
            boolean esMio = ultimoMensaje.getRemitente().equals(usernameActual);
            textoPreview = (esMio ? "Tú: " : "") + contenido;
            tiempo = ultimoMensaje.getTiempoTranscurrido();
        }
        
        JLabel lblPreview = new JLabel(textoPreview);
        lblPreview.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        lblPreview.setForeground(TEXT_SECONDARY);
        lblPreview.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        panelInfo.add(lblUsername);
        panelInfo.add(Box.createVerticalStrut(4));
        panelInfo.add(lblPreview);
        
        JPanel panelDerecha = new JPanel();
        panelDerecha.setLayout(new BoxLayout(panelDerecha, BoxLayout.Y_AXIS));
        panelDerecha.setBackground(CARD_COLOR);
        
        JLabel lblTiempo = new JLabel(tiempo);
        lblTiempo.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lblTiempo.setForeground(TEXT_SECONDARY);
        lblTiempo.setAlignmentX(Component.RIGHT_ALIGNMENT);
        
        panelDerecha.add(lblTiempo);
        
        tarjeta.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                abrirConversacion(otroUsuario);
            }
            
            @Override
            public void mouseEntered(java.awt.event.MouseEvent e) {
                tarjeta.setBackground(new Color(255, 230, 240));
                panelInfo.setBackground(new Color(255, 230, 240));
                panelDerecha.setBackground(new Color(255, 230, 240));
            }
            
            @Override
            public void mouseExited(java.awt.event.MouseEvent e) {
                tarjeta.setBackground(CARD_COLOR);
                panelInfo.setBackground(CARD_COLOR);
                panelDerecha.setBackground(CARD_COLOR);
            }
        });
        
        tarjeta.add(lblAvatar, BorderLayout.WEST);
        tarjeta.add(panelInfo, BorderLayout.CENTER);
        tarjeta.add(panelDerecha, BorderLayout.EAST);
        
        return tarjeta;
    }
    
    private void abrirConversacion(String otroUsuario) {
        DialogConversacion dialog = new DialogConversacion(
            (Frame) SwingUtilities.getWindowAncestor(this),
            gestorMensajes,
            gestorUsuarios,
            gestorINSTA.getUsernameActual(),
            otroUsuario
        );
        dialog.setVisible(true);
        
        actualizarContenido();
    }
    
    private void buscarUsuarioParaChat() {
        String username = txtBuscarUsuario.getText().trim();
        
        if (username.isEmpty()) {
            JOptionPane.showMessageDialog(
                this,
                "Escribe el username del usuario con quien quieres chatear",
                "Buscar Usuario",
                JOptionPane.INFORMATION_MESSAGE
            );
            return;
        }
        
        if (username.startsWith("@")) {
            username = username.substring(1);
        }
        
        Usuario usuario = gestorUsuarios.obtenerUsuario(username);
        
        if (usuario == null) {
            JOptionPane.showMessageDialog(
                this,
                "Usuario no encontrado: @" + username,
                "Error",
                JOptionPane.ERROR_MESSAGE
            );
            return;
        }
        
        if (username.equals(gestorINSTA.getUsernameActual())) {
            JOptionPane.showMessageDialog(
                this,
                "No puedes enviarte mensajes a ti mismo",
                "Error",
                JOptionPane.ERROR_MESSAGE
            );
            return;
        }
        
        txtBuscarUsuario.setText("");
        abrirConversacion(username);
    }
    
    private void mostrarMensajeVacio() {
        JPanel panelVacio = new JPanel();
        panelVacio.setLayout(new BoxLayout(panelVacio, BoxLayout.Y_AXIS));
        panelVacio.setBackground(BACKGROUND_COLOR);
                
        JLabel lblIcono = new JLabel();
        try {
            ImageIcon icono = new ImageIcon(getClass().getResource("/Instagram/icons/icon_mensajeria.png"));
            Image img = icono.getImage().getScaledInstance(80, 80, Image.SCALE_SMOOTH);
            lblIcono.setIcon(new ImageIcon(img));
        } catch (Exception e) {
            lblIcono.setText("💬");
            lblIcono.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 60));
            lblIcono.setForeground(INSTAGRAM_PINK);
        }
        lblIcono.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        JLabel lblMensaje1 = new JLabel("Mensajería directa");
        lblMensaje1.setFont(new Font("Segoe UI", Font.BOLD, 20));
        lblMensaje1.setForeground(INSTAGRAM_PINK);
        lblMensaje1.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        JLabel lblMensaje2 = new JLabel("Busca un usuario para iniciar una conversación");
        lblMensaje2.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        lblMensaje2.setForeground(TEXT_SECONDARY);
        lblMensaje2.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        panelVacio.add(Box.createVerticalGlue());
        panelVacio.add(lblIcono);
        panelVacio.add(Box.createVerticalStrut(20));
        panelVacio.add(lblMensaje1);
        panelVacio.add(Box.createVerticalStrut(10));
        panelVacio.add(lblMensaje2);
        panelVacio.add(Box.createVerticalGlue());
        
        panelConversaciones.add(panelVacio);
    }
}
