/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Instagram.GUI;

import Instagram.Modelo.Usuario;
import Instagram.Modelo.Mensaje;
import Instagram.Logica.GestorMensajes;
import Instagram.Logica.GestorUsuariosLocalINSTA;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.*;
import java.util.ArrayList;
import java.time.format.DateTimeFormatter;

/**
 *
 * @author najma
 */
public class DialogConversacion extends JDialog {
    
    private GestorMensajes gestorMensajes;
    private GestorUsuariosLocalINSTA gestorUsuarios;
    private String usuarioActual;
    private String otroUsuario;
    
    private JPanel panelMensajes;
    private JScrollPane scrollPane;
    private JTextField txtMensaje;
    
    private static final Color BACKGROUND_COLOR = new Color(255, 240, 245);
    private static final Color CARD_COLOR = Color.WHITE;
    private static final Color BORDER_COLOR = new Color(255, 192, 203);
    private static final Color TEXT_PRIMARY = new Color(38, 38, 38);
    private static final Color TEXT_SECONDARY = new Color(142, 142, 142);
    private static final Color INSTAGRAM_PINK = new Color(242, 80, 129);
    private static final Color MENSAJE_PROPIO_BG = new Color(255, 230, 240);
    private static final Color MENSAJE_OTRO_BG = new Color(240, 240, 240);
    
    private static final DateTimeFormatter FORMATO_FECHA = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
    
    public DialogConversacion(Frame parent, GestorMensajes gestor, GestorUsuariosLocalINSTA gestorUsuariosLocal,
                             String usuarioActual, String otroUsuario) {
        super(parent, "Conversación con @" + otroUsuario, true);
        this.gestorMensajes = gestor;
        this.gestorUsuarios = gestorUsuariosLocal;
        this.usuarioActual = usuarioActual;
        this.otroUsuario = otroUsuario;
        
        initComponents();
        cargarMensajes();
        configurarVentana();
    }
    
    private void initComponents() {
        setLayout(new BorderLayout());
        getContentPane().setBackground(BACKGROUND_COLOR);
        
        JPanel panelSuperior = crearPanelSuperior();
        add(panelSuperior, BorderLayout.NORTH);
        
        panelMensajes = new JPanel();
        panelMensajes.setLayout(new BoxLayout(panelMensajes, BoxLayout.Y_AXIS));
        panelMensajes.setBackground(BACKGROUND_COLOR);
        panelMensajes.setBorder(BorderFactory.createEmptyBorder(16, 16, 16, 16));
        
        scrollPane = new JScrollPane(panelMensajes);
        scrollPane.setBorder(null);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        
        add(scrollPane, BorderLayout.CENTER);
        
        JPanel panelInferior = crearPanelInferior();
        add(panelInferior, BorderLayout.SOUTH);
    }
    
    private JPanel crearPanelSuperior() {
        JPanel panel = new JPanel(new BorderLayout(12, 0));
        panel.setBackground(CARD_COLOR);
        panel.setBorder(new CompoundBorder(
            new MatteBorder(0, 0, 1, 0, BORDER_COLOR),
            BorderFactory.createEmptyBorder(16, 16, 16, 16)
        ));
        
        JLabel lblAvatar = new JLabel();
        try {
            ImageIcon avatarIcon = new ImageIcon(getClass().getResource("/Instagram/icons/icon_perfil.png"));
            Image img = avatarIcon.getImage().getScaledInstance(48, 48, Image.SCALE_SMOOTH);
            lblAvatar.setIcon(new ImageIcon(img));
        } catch (Exception e) {
            lblAvatar.setText("👤");
            lblAvatar.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 36));
            lblAvatar.setForeground(INSTAGRAM_PINK);
        }
        lblAvatar.setPreferredSize(new Dimension(48, 48));
        
        JPanel panelInfo = new JPanel();
        panelInfo.setLayout(new BoxLayout(panelInfo, BoxLayout.Y_AXIS));
        panelInfo.setBackground(CARD_COLOR);
        
        JLabel lblUsername = new JLabel("@" + otroUsuario);
        lblUsername.setFont(new Font("Segoe UI", Font.BOLD, 18));
        lblUsername.setForeground(TEXT_PRIMARY);
        lblUsername.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        Usuario usuario = gestorUsuarios.obtenerUsuario(otroUsuario);
        JLabel lblNombre = new JLabel(usuario != null ? usuario.getNombreCompleto() : otroUsuario);
        lblNombre.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        lblNombre.setForeground(TEXT_SECONDARY);
        lblNombre.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        panelInfo.add(lblUsername);
        panelInfo.add(lblNombre);
        
        panel.add(lblAvatar, BorderLayout.WEST);
        panel.add(panelInfo, BorderLayout.CENTER);
        
        return panel;
    }
    
    private JPanel crearPanelInferior() {
        JPanel panel = new JPanel(new BorderLayout(12, 0));
        panel.setBackground(CARD_COLOR);
        panel.setBorder(new CompoundBorder(
            new MatteBorder(1, 0, 0, 0, BORDER_COLOR),
            BorderFactory.createEmptyBorder(16, 16, 16, 16)
        ));
        
        txtMensaje = new JTextField();
        txtMensaje.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        txtMensaje.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(BORDER_COLOR, 2),
            BorderFactory.createEmptyBorder(10, 12, 10, 12)
        ));
        
        txtMensaje.addActionListener(e -> enviarMensaje());
        
        JButton btnEnviar = new JButton("Enviar");
        btnEnviar.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnEnviar.setForeground(Color.WHITE);
        btnEnviar.setBackground(INSTAGRAM_PINK);
        btnEnviar.setBorder(BorderFactory.createEmptyBorder(10, 24, 10, 24));
        btnEnviar.setFocusPainted(false);
        btnEnviar.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnEnviar.addActionListener(e -> enviarMensaje());
        
        panel.add(txtMensaje, BorderLayout.CENTER);
        panel.add(btnEnviar, BorderLayout.EAST);
        
        return panel;
    }
    
    private void cargarMensajes() {
        panelMensajes.removeAll();
        
        ArrayList<Mensaje> mensajes = gestorMensajes.obtenerMensajes(usuarioActual, otroUsuario);
        
        if (mensajes.isEmpty()) {
            JLabel lblVacio = new JLabel("No hay mensajes. ¡Sé el primero en escribir!");
            lblVacio.setFont(new Font("Segoe UI", Font.PLAIN, 14));
            lblVacio.setForeground(TEXT_SECONDARY);
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
        boolean esMio = mensaje.getEmisor().equals(usuarioActual);
        
        JPanel contenedor = new JPanel();
        contenedor.setLayout(new BoxLayout(contenedor, BoxLayout.X_AXIS));
        contenedor.setBackground(BACKGROUND_COLOR);
        contenedor.setAlignmentX(Component.LEFT_ALIGNMENT);
        contenedor.setMaximumSize(new Dimension(Integer.MAX_VALUE, 150));
        
        JPanel burbuja = new JPanel();
        burbuja.setLayout(new BoxLayout(burbuja, BoxLayout.Y_AXIS));
        burbuja.setBackground(esMio ? MENSAJE_PROPIO_BG : MENSAJE_OTRO_BG);
        burbuja.setBorder(new CompoundBorder(
            new LineBorder(BORDER_COLOR, 1, true),
            BorderFactory.createEmptyBorder(10, 14, 10, 14)
        ));
        burbuja.setMaximumSize(new Dimension(400, 150));
        
        if (!esMio) {
            JLabel lblEmisor = new JLabel("@" + mensaje.getEmisor());
            lblEmisor.setFont(new Font("Segoe UI", Font.BOLD, 12));
            lblEmisor.setForeground(INSTAGRAM_PINK);
            lblEmisor.setAlignmentX(Component.LEFT_ALIGNMENT);
            burbuja.add(lblEmisor);
            burbuja.add(Box.createVerticalStrut(4));
        }
        
        JTextArea txtContenido = new JTextArea(mensaje.getContenido());
        txtContenido.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        txtContenido.setForeground(TEXT_PRIMARY);
        txtContenido.setBackground(burbuja.getBackground());
        txtContenido.setEditable(false);
        txtContenido.setLineWrap(true);
        txtContenido.setWrapStyleWord(true);
        txtContenido.setBorder(null);
        txtContenido.setAlignmentX(Component.LEFT_ALIGNMENT);
        burbuja.add(txtContenido);
        
        burbuja.add(Box.createVerticalStrut(6));
        
        JLabel lblFecha = new JLabel(mensaje.getFechaHora().format(FORMATO_FECHA));
        lblFecha.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        lblFecha.setForeground(TEXT_SECONDARY);
        lblFecha.setAlignmentX(esMio ? Component.RIGHT_ALIGNMENT : Component.LEFT_ALIGNMENT);
        burbuja.add(lblFecha);
        
        if (esMio) {
            contenedor.add(Box.createHorizontalGlue());
            contenedor.add(burbuja);
        } else {
            contenedor.add(burbuja);
            contenedor.add(Box.createHorizontalGlue());
        }
        
        return contenedor;
    }
    
    private void enviarMensaje() {
        String contenido = txtMensaje.getText().trim();
        
        if (contenido.isEmpty()) {
            return;
        }
        
        gestorMensajes.enviarMensaje(usuarioActual, otroUsuario, contenido);
        
        txtMensaje.setText("");
        cargarMensajes();
    }
    
    private void configurarVentana() {
        setSize(600, 700);
        setMinimumSize(new Dimension(500, 600));
        setLocationRelativeTo(getParent());
        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
    }
}
