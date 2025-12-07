/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Instagram.GUI;

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
public class DialogConversacion extends JDialog {
    
    private GestorMensajes gestorMensajes;
    private GestorUsuariosLocal gestorUsuarios;
    private String usuarioActual;
    private String otroUsuario;
    
    private JPanel panelMensajes;
    private JTextField txtMensaje;
    private JScrollPane scrollPane;
    
    private static final Color BACKGROUND_COLOR = new Color(255, 240, 245);
    private static final Color CARD_COLOR = Color.WHITE;
    private static final Color MENSAJE_ENVIADO = new Color(242, 80, 129);
    private static final Color MENSAJE_RECIBIDO = new Color(230, 230, 230);
    private static final Color TEXT_PRIMARY = new Color(38, 38, 38);
    private static final Color BORDER_COLOR = new Color(255, 192, 203);
    
    public DialogConversacion(Frame parent, GestorMensajes gestor, GestorUsuariosLocal gestorUsuariosLocal,
                             String usuarioActual, String otroUsuario) {
        super(parent, "@" + otroUsuario, false);
        this.gestorMensajes = gestor;
        this.gestorUsuarios = gestorUsuariosLocal;
        this.usuarioActual = usuarioActual;
        this.otroUsuario = otroUsuario;
        
        initComponents();
        cargarMensajes();
        marcarComoLeido();
        configurarDialogo();
    }
    
    private void initComponents() {
        setLayout(new BorderLayout());
        getContentPane().setBackground(BACKGROUND_COLOR);
        
        JPanel header = crearHeader();
        add(header, BorderLayout.NORTH);
        
        panelMensajes = new JPanel();
        panelMensajes.setLayout(new BoxLayout(panelMensajes, BoxLayout.Y_AXIS));
        panelMensajes.setBackground(BACKGROUND_COLOR);
        panelMensajes.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        scrollPane = new JScrollPane(panelMensajes);
        scrollPane.setBorder(null);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        add(scrollPane, BorderLayout.CENTER);
        
        JPanel footer = crearFooter();
        add(footer, BorderLayout.SOUTH);
    }
    
    private JPanel crearHeader() {
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(CARD_COLOR);
        header.setBorder(new CompoundBorder(
            new MatteBorder(0, 0, 1, 0, BORDER_COLOR),
            BorderFactory.createEmptyBorder(12, 16, 12, 16)
        ));
        
        JButton btnVolver = new JButton("←");
        btnVolver.setFont(new Font("Segoe UI", Font.BOLD, 20));
        btnVolver.setForeground(MENSAJE_ENVIADO);
        btnVolver.setBackground(CARD_COLOR);
        btnVolver.setBorderPainted(false);
        btnVolver.setContentAreaFilled(false);
        btnVolver.setFocusPainted(false);
        btnVolver.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnVolver.addActionListener(e -> dispose());
        
        JLabel lblUsuario = new JLabel("@" + otroUsuario);
        lblUsuario.setFont(new Font("Segoe UI", Font.BOLD, 18));
        lblUsuario.setForeground(TEXT_PRIMARY);
        
        header.add(btnVolver, BorderLayout.WEST);
        header.add(lblUsuario, BorderLayout.CENTER);
        
        return header;
    }
    
    private JPanel crearFooter() {
        JPanel footer = new JPanel(new BorderLayout(8, 0));
        footer.setBackground(CARD_COLOR);
        footer.setBorder(new CompoundBorder(
            new MatteBorder(1, 0, 0, 0, BORDER_COLOR),
            BorderFactory.createEmptyBorder(12, 16, 12, 16)
        ));
        
        txtMensaje = new JTextField();
        txtMensaje.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        txtMensaje.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(BORDER_COLOR, 2),
            BorderFactory.createEmptyBorder(8, 12, 8, 12)
        ));
        txtMensaje.addActionListener(e -> enviarMensaje());
        
        JButton btnEnviar = new JButton("Enviar");
        btnEnviar.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnEnviar.setForeground(Color.WHITE);
        btnEnviar.setBackground(MENSAJE_ENVIADO);
        btnEnviar.setBorder(BorderFactory.createEmptyBorder(8, 20, 8, 20));
        btnEnviar.setFocusPainted(false);
        btnEnviar.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnEnviar.addActionListener(e -> enviarMensaje());
        
        footer.add(txtMensaje, BorderLayout.CENTER);
        footer.add(btnEnviar, BorderLayout.EAST);
        
        return footer;
    }
    
    private void cargarMensajes() {
        panelMensajes.removeAll();
        
        ArrayList<Mensaje> mensajes = gestorMensajes.obtenerConversacion(
            usuarioActual, otroUsuario
        );
        
        if (mensajes.isEmpty()) {
            JLabel lblVacio = new JLabel("Inicia una conversación con @" + otroUsuario);
            lblVacio.setFont(new Font("Segoe UI", Font.PLAIN, 14));
            lblVacio.setForeground(Color.GRAY);
            lblVacio.setAlignmentX(Component.CENTER_ALIGNMENT);
            
            panelMensajes.add(Box.createVerticalGlue());
            panelMensajes.add(lblVacio);
            panelMensajes.add(Box.createVerticalGlue());
        } else {
            for (Mensaje mensaje : mensajes) {
                JPanel burbuja = crearBurbujaMensaje(mensaje);
                panelMensajes.add(burbuja);
                panelMensajes.add(Box.createVerticalStrut(8));
            }
        }
        
        panelMensajes.revalidate();
        panelMensajes.repaint();
        
        SwingUtilities.invokeLater(() -> {
            JScrollBar vertical = scrollPane.getVerticalScrollBar();
            vertical.setValue(vertical.getMaximum());
        });
    }
    
    private JPanel crearBurbujaMensaje(Mensaje mensaje) {
        boolean esMio = mensaje.getRemitente().equals(usuarioActual);
        
        JPanel contenedor = new JPanel(new FlowLayout(
            esMio ? FlowLayout.RIGHT : FlowLayout.LEFT, 10, 0
        ));
        contenedor.setBackground(BACKGROUND_COLOR);
        contenedor.setMaximumSize(new Dimension(500, 200));
        contenedor.setAlignmentX(esMio ? Component.RIGHT_ALIGNMENT : Component.LEFT_ALIGNMENT);
        
        if (!esMio) {
            JLabel lblAvatar = crearAvatar(otroUsuario);
            contenedor.add(lblAvatar);
        }
        
        JPanel burbuja = new JPanel();
        burbuja.setLayout(new BoxLayout(burbuja, BoxLayout.Y_AXIS));
        burbuja.setBackground(esMio ? MENSAJE_ENVIADO : MENSAJE_RECIBIDO);
        burbuja.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(esMio ? MENSAJE_ENVIADO : MENSAJE_RECIBIDO, 2, true),
            BorderFactory.createEmptyBorder(8, 12, 8, 12)
        ));
        
        JTextArea txtContenido = new JTextArea(mensaje.getContenido());
        txtContenido.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        txtContenido.setForeground(esMio ? Color.WHITE : TEXT_PRIMARY);
        txtContenido.setBackground(esMio ? MENSAJE_ENVIADO : MENSAJE_RECIBIDO);
        txtContenido.setLineWrap(true);
        txtContenido.setWrapStyleWord(true);
        txtContenido.setEditable(false);
        txtContenido.setMaximumSize(new Dimension(250, 150));
        
        JLabel lblTiempo = new JLabel(mensaje.getTiempoTranscurrido());
        lblTiempo.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        lblTiempo.setForeground(esMio ? new Color(255, 255, 255, 180) : Color.GRAY);
        lblTiempo.setAlignmentX(esMio ? Component.RIGHT_ALIGNMENT : Component.LEFT_ALIGNMENT);
        
        burbuja.add(txtContenido);
        burbuja.add(Box.createVerticalStrut(4));
        burbuja.add(lblTiempo);
        
        contenedor.add(burbuja);
        
        if (esMio) {
            JLabel lblAvatar = crearAvatar(usuarioActual);
            contenedor.add(lblAvatar);
        }
        
        return contenedor;
    }
    
    private JLabel crearAvatar(String username) {
        JLabel lblAvatar = new JLabel();
        lblAvatar.setPreferredSize(new Dimension(32, 32));
        lblAvatar.setMinimumSize(new Dimension(32, 32));
        lblAvatar.setMaximumSize(new Dimension(32, 32));
        
        try {
            Usuario usuario = gestorUsuarios.obtenerUsuario(username);
            
            if (usuario != null && usuario.tieneFotoPersonalizada()) {
                ImageIcon avatarIcon = new ImageIcon(usuario.getRutaFotoPerfil());
                Image img = avatarIcon.getImage().getScaledInstance(32, 32, Image.SCALE_SMOOTH);
                lblAvatar.setIcon(new ImageIcon(img));
            } else if (usuario != null) {
                String iconoPath = "/Instagram/icons/icon_perfil.png";
                if (usuario.getGenero() == 'F') {
                    iconoPath = "/Instagram/icons/icon_usuario_mujer.png";
                } else if (usuario.getGenero() == 'M') {
                    iconoPath = "/Instagram/icons/icon_usuario_hombre.png";
                }
                ImageIcon avatarIcon = new ImageIcon(getClass().getResource(iconoPath));
                Image img = avatarIcon.getImage().getScaledInstance(32, 32, Image.SCALE_SMOOTH);
                lblAvatar.setIcon(new ImageIcon(img));
            } else {
                lblAvatar.setText("👤");
                lblAvatar.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 24));
            }
        } catch (Exception e) {
            lblAvatar.setText("👤");
            lblAvatar.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 24));
        }
        
        return lblAvatar;
    }
    
    private void enviarMensaje() {
        String contenido = txtMensaje.getText().trim();
        
        if (contenido.isEmpty()) {
            return;
        }
        
        Mensaje nuevo = gestorMensajes.enviarMensaje(
            usuarioActual, 
            otroUsuario, 
            contenido
        );
        
        if (nuevo != null) {
            txtMensaje.setText("");
            cargarMensajes();
        }
    }
    
    private void marcarComoLeido() {
        gestorMensajes.marcarConversacionComoLeida(usuarioActual, otroUsuario);
    }
    
    private void configurarDialogo() {
        setSize(500, 650);
        setLocationRelativeTo(getParent());
    }
}
