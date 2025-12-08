/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Instagram.GUI;

import Instagram.Modelo.Usuario;
import Instagram.Logica.GestorINSTACompleto;
import Instagram.Logica.GestorUsuariosLocalINSTA;
import Instagram.Logica.GestorMensajes;
import Instagram.Modelo.Conversacion;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.*;
import java.util.ArrayList;

/**
 *
 * @author najma
 */
public class PanelMensajes extends JPanel {
    
    private GestorINSTACompleto gestorINSTA;
    private GestorUsuariosLocalINSTA gestorUsuarios;
    private VentanaINSTA ventanaPrincipal;
    private GestorMensajes gestorMensajes;
    
    private JPanel panelConversaciones;
    private JScrollPane scrollPane;
    
    private static final Color BACKGROUND_COLOR = new Color(255, 240, 245);
    private static final Color CARD_COLOR = Color.WHITE;
    private static final Color BORDER_COLOR = new Color(255, 192, 203);
    private static final Color TEXT_PRIMARY = new Color(38, 38, 38);
    private static final Color TEXT_SECONDARY = new Color(142, 142, 142);
    private static final Color INSTAGRAM_PINK = new Color(242, 80, 129);
    private static final Color HOVER_COLOR = new Color(255, 230, 240);
    
    public PanelMensajes(GestorINSTACompleto gestor, GestorUsuariosLocalINSTA gestorUsuariosLocal, VentanaINSTA ventana) {
        this.gestorINSTA = gestor;
        this.gestorUsuarios = gestorUsuariosLocal;
        this.ventanaPrincipal = ventana;
        this.gestorMensajes = new GestorMensajes();
        
        initComponents();
    }
    
    private void initComponents() {
        setLayout(new BorderLayout());
        setBackground(BACKGROUND_COLOR);
        
        JPanel panelSuperior = crearPanelSuperior();
        add(panelSuperior, BorderLayout.NORTH);
        
        panelConversaciones = new JPanel();
        panelConversaciones.setLayout(new BoxLayout(panelConversaciones, BoxLayout.Y_AXIS));
        panelConversaciones.setBackground(BACKGROUND_COLOR);
        panelConversaciones.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        scrollPane = new JScrollPane(panelConversaciones);
        scrollPane.setBorder(null);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        
        add(scrollPane, BorderLayout.CENTER);
    }
    
    private JPanel crearPanelSuperior() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(CARD_COLOR);
        panel.setBorder(new CompoundBorder(
            new MatteBorder(0, 0, 1, 0, BORDER_COLOR),
            BorderFactory.createEmptyBorder(20, 20, 20, 20)
        ));
        
        JLabel lblTitulo = new JLabel("Mensajes");
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 24));
        lblTitulo.setForeground(INSTAGRAM_PINK);
        
        JButton btnNuevoMensaje = new JButton("Nuevo Mensaje");
        btnNuevoMensaje.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnNuevoMensaje.setForeground(Color.WHITE);
        btnNuevoMensaje.setBackground(INSTAGRAM_PINK);
        btnNuevoMensaje.setBorder(BorderFactory.createEmptyBorder(8, 16, 8, 16));
        btnNuevoMensaje.setFocusPainted(false);
        btnNuevoMensaje.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnNuevoMensaje.addActionListener(e -> iniciarNuevoMensaje());
        
        panel.add(lblTitulo, BorderLayout.WEST);
        panel.add(btnNuevoMensaje, BorderLayout.EAST);
        
        return panel;
    }
    
    public void actualizarContenido() {
        panelConversaciones.removeAll();
        
        String usernameActual = gestorINSTA.getUsernameActual();
        ArrayList<Conversacion> conversaciones = gestorMensajes.obtenerConversaciones(usernameActual);
        
        if (conversaciones.isEmpty()) {
            mostrarMensajeVacio();
        } else {
            for (Conversacion conversacion : conversaciones) {
                String otroUsuario = conversacion.getOtroUsuario(usernameActual);
                JPanel tarjeta = crearTarjetaConversacion(otroUsuario, conversacion);
                panelConversaciones.add(tarjeta);
                panelConversaciones.add(Box.createVerticalStrut(10));
            }
        }
        
        panelConversaciones.revalidate();
        panelConversaciones.repaint();
    }
    
    private JPanel crearTarjetaConversacion(String otroUsuario, Conversacion conversacion) {
        JPanel tarjeta = new JPanel(new BorderLayout(12, 0));
        tarjeta.setBackground(CARD_COLOR);
        tarjeta.setBorder(new CompoundBorder(
            new LineBorder(BORDER_COLOR, 2),
            BorderFactory.createEmptyBorder(16, 16, 16, 16)
        ));
        tarjeta.setMaximumSize(new Dimension(700, 80));
        tarjeta.setAlignmentX(Component.LEFT_ALIGNMENT);
        tarjeta.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
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
        
        JLabel lblUsuario = new JLabel("@" + otroUsuario);
        lblUsuario.setFont(new Font("Segoe UI", Font.BOLD, 16));
        lblUsuario.setForeground(TEXT_PRIMARY);
        lblUsuario.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        Usuario usuario = gestorUsuarios.obtenerUsuario(otroUsuario);
        JLabel lblNombre = new JLabel(usuario != null ? usuario.getNombreCompleto() : otroUsuario);
        lblNombre.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        lblNombre.setForeground(TEXT_SECONDARY);
        lblNombre.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        panelInfo.add(lblUsuario);
        panelInfo.add(Box.createVerticalStrut(4));
        panelInfo.add(lblNombre);
        
        tarjeta.add(lblAvatar, BorderLayout.WEST);
        tarjeta.add(panelInfo, BorderLayout.CENTER);
        
        tarjeta.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                tarjeta.setBackground(HOVER_COLOR);
                panelInfo.setBackground(HOVER_COLOR);
            }
            
            @Override
            public void mouseExited(MouseEvent e) {
                tarjeta.setBackground(CARD_COLOR);
                panelInfo.setBackground(CARD_COLOR);
            }
            
            @Override
            public void mouseClicked(MouseEvent e) {
                abrirConversacion(otroUsuario);
            }
        });
        
        return tarjeta;
    }
    
    private void iniciarNuevoMensaje() {
        String usernameActual = gestorINSTA.getUsernameActual();
        ArrayList<String> siguiendo = gestorINSTA.obtenerSiguiendo(usernameActual);
        
        if (siguiendo.isEmpty()) {
            JOptionPane.showMessageDialog(
                this,
                "No sigues a ningún usuario. Sigue a alguien primero para enviar mensajes.",
                "Sin contactos",
                JOptionPane.INFORMATION_MESSAGE
            );
            return;
        }
        
        String[] opciones = siguiendo.toArray(new String[0]);
        String seleccionado = (String) JOptionPane.showInputDialog(
            this,
            "Selecciona un usuario para enviar un mensaje:",
            "Nuevo Mensaje",
            JOptionPane.QUESTION_MESSAGE,
            null,
            opciones,
            opciones[0]
        );
        
        if (seleccionado != null) {
            abrirConversacion(seleccionado);
        }
    }
    
    private void abrirConversacion(String otroUsuario) {
        String usernameActual = gestorINSTA.getUsernameActual();
        
        DialogConversacion dialog = new DialogConversacion(
            (Frame) SwingUtilities.getWindowAncestor(this),
            gestorMensajes,
            gestorUsuarios,
            usernameActual,
            otroUsuario
        );
        dialog.setVisible(true);
        
        actualizarContenido();
    }
    
    private void mostrarMensajeVacio() {
        JPanel panelVacio = new JPanel();
        panelVacio.setLayout(new BoxLayout(panelVacio, BoxLayout.Y_AXIS));
        panelVacio.setBackground(CARD_COLOR);
        panelVacio.setBorder(new CompoundBorder(
            new LineBorder(BORDER_COLOR, 2),
            BorderFactory.createEmptyBorder(60, 40, 60, 40)
        ));
        panelVacio.setMaximumSize(new Dimension(500, 280));
        panelVacio.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        JLabel lblIcono = new JLabel();
        try {
            ImageIcon icono = new ImageIcon(getClass().getResource("/Instagram/icons/icon_mensajeria.png"));
            Image img = icono.getImage().getScaledInstance(80, 80, Image.SCALE_SMOOTH);
            lblIcono.setIcon(new ImageIcon(img));
        } catch (Exception e) {
            lblIcono.setText("💬");
            lblIcono.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 60));
        }
        lblIcono.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        JLabel lblTitulo = new JLabel("Tus mensajes");
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 20));
        lblTitulo.setForeground(INSTAGRAM_PINK);
        lblTitulo.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        JLabel lblMensaje = new JLabel("Envía mensajes privados a tus amigos");
        lblMensaje.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        lblMensaje.setForeground(TEXT_SECONDARY);
        lblMensaje.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        JButton btnEnviarMensaje = new JButton("Enviar mensaje");
        btnEnviarMensaje.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnEnviarMensaje.setForeground(Color.WHITE);
        btnEnviarMensaje.setBackground(INSTAGRAM_PINK);
        btnEnviarMensaje.setBorder(BorderFactory.createEmptyBorder(10, 24, 10, 24));
        btnEnviarMensaje.setFocusPainted(false);
        btnEnviarMensaje.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnEnviarMensaje.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnEnviarMensaje.addActionListener(e -> iniciarNuevoMensaje());
        
        panelVacio.add(lblIcono);
        panelVacio.add(Box.createVerticalStrut(20));
        panelVacio.add(lblTitulo);
        panelVacio.add(Box.createVerticalStrut(10));
        panelVacio.add(lblMensaje);
        panelVacio.add(Box.createVerticalStrut(20));
        panelVacio.add(btnEnviarMensaje);
        
        panelConversaciones.add(Box.createVerticalGlue());
        panelConversaciones.add(panelVacio);
        panelConversaciones.add(Box.createVerticalGlue());
    }
}
