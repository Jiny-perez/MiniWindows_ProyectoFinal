/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Instagram.GUI;

import Instagram.Logica.GestorINSTACompleto;
import Instagram.Logica.GestorNotificaciones;
import Instagram.Modelo.Notificacion;
import java.awt.*;
import javax.swing.*;
import javax.swing.border.*;
import java.util.ArrayList;
import java.time.format.DateTimeFormatter;

/**
 *
 * @author najma
 */
public class PanelNotificaciones extends JPanel {
    
    private GestorINSTACompleto gestorINSTA;
    private GestorNotificaciones gestorNotificaciones;
    private VentanaINSTA ventanaPrincipal;
    
    private JPanel panelNotificaciones;
    private JScrollPane scrollPane;
    
    private static final Color BACKGROUND_COLOR = new Color(255, 240, 245);
    private static final Color CARD_COLOR = Color.WHITE;
    private static final Color BORDER_COLOR = new Color(255, 192, 203);
    private static final Color TEXT_PRIMARY = new Color(38, 38, 38);
    private static final Color TEXT_SECONDARY = new Color(142, 142, 142);
    private static final Color INSTAGRAM_PINK = new Color(242, 80, 129);
    private static final Color UNREAD_BG = new Color(255, 245, 250);
    
    private static final DateTimeFormatter FORMATO_FECHA = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
    
    public PanelNotificaciones(GestorINSTACompleto gestor, GestorNotificaciones gestorNotif, VentanaINSTA ventana) {
        this.gestorINSTA = gestor;
        this.gestorNotificaciones = gestorNotif;
        this.ventanaPrincipal = ventana;
        
        initComponents();
    }
    
    private void initComponents() {
        setLayout(new BorderLayout());
        setBackground(BACKGROUND_COLOR);
        
        JPanel panelSuperior = crearPanelSuperior();
        add(panelSuperior, BorderLayout.NORTH);
        
        panelNotificaciones = new JPanel();
        panelNotificaciones.setLayout(new BoxLayout(panelNotificaciones, BoxLayout.Y_AXIS));
        panelNotificaciones.setBackground(BACKGROUND_COLOR);
        panelNotificaciones.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        scrollPane = new JScrollPane(panelNotificaciones);
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
        
        JLabel lblTitulo = new JLabel("Notificaciones");
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 24));
        lblTitulo.setForeground(INSTAGRAM_PINK);
        
        JButton btnMarcarLeidas = new JButton("Marcar todas como leídas");
        btnMarcarLeidas.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        btnMarcarLeidas.setForeground(INSTAGRAM_PINK);
        btnMarcarLeidas.setBackground(CARD_COLOR);
        btnMarcarLeidas.setBorder(BorderFactory.createEmptyBorder(6, 12, 6, 12));
        btnMarcarLeidas.setFocusPainted(false);
        btnMarcarLeidas.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnMarcarLeidas.addActionListener(e -> {
            String username = gestorINSTA.getUsernameActual();
            gestorNotificaciones.marcarTodasComoLeidas(username);
            actualizarContenido();
        });
        
        panel.add(lblTitulo, BorderLayout.WEST);
        panel.add(btnMarcarLeidas, BorderLayout.EAST);
        
        return panel;
    }
    
    public void actualizarContenido() {
        panelNotificaciones.removeAll();
        
        String username = gestorINSTA.getUsernameActual();
        ArrayList<Notificacion> notificaciones = gestorNotificaciones.obtenerNotificaciones(username);
        
        if (notificaciones.isEmpty()) {
            mostrarMensajeVacio();
        } else {
            for (Notificacion notificacion : notificaciones) {
                JPanel tarjeta = crearTarjetaNotificacion(notificacion);
                panelNotificaciones.add(tarjeta);
                panelNotificaciones.add(Box.createVerticalStrut(8));
            }
        }
        
        panelNotificaciones.revalidate();
        panelNotificaciones.repaint();
        
        SwingUtilities.invokeLater(() -> scrollPane.getVerticalScrollBar().setValue(0));
    }
    
    private JPanel crearTarjetaNotificacion(Notificacion notificacion) {
        JPanel tarjeta = new JPanel(new BorderLayout(12, 0));
        
        if (!notificacion.isLeida()) {
            tarjeta.setBackground(UNREAD_BG);
        } else {
            tarjeta.setBackground(CARD_COLOR);
        }
        
        tarjeta.setBorder(new CompoundBorder(
            new LineBorder(BORDER_COLOR, notificacion.isLeida() ? 1 : 2),
            BorderFactory.createEmptyBorder(14, 14, 14, 14)
        ));
        tarjeta.setMaximumSize(new Dimension(700, 90));
        tarjeta.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        JLabel lblIcono = new JLabel();
        lblIcono.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 32));
        lblIcono.setForeground(INSTAGRAM_PINK);
        
        switch (notificacion.getTipo()) {
            case LIKE:
                lblIcono.setText("❤️");
                break;
            case COMENTARIO:
                lblIcono.setText("💬");
                break;
            case SEGUIDOR:
                lblIcono.setText("👤");
                break;
            case MENCION:
                lblIcono.setText("@");
                break;
            default:
                lblIcono.setText("🔔");
        }
        
        lblIcono.setPreferredSize(new Dimension(40, 40));
        lblIcono.setHorizontalAlignment(SwingConstants.CENTER);
        
        JPanel panelContenido = new JPanel();
        panelContenido.setLayout(new BoxLayout(panelContenido, BoxLayout.Y_AXIS));
        panelContenido.setBackground(tarjeta.getBackground());
        
        JLabel lblMensaje = new JLabel("<html><b>@" + notificacion.getUsuarioOrigen() + "</b> " + notificacion.getMensaje() + "</html>");
        lblMensaje.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        lblMensaje.setForeground(TEXT_PRIMARY);
        lblMensaje.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        JLabel lblFecha = new JLabel(notificacion.getFechaHora().format(FORMATO_FECHA));
        lblFecha.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lblFecha.setForeground(TEXT_SECONDARY);
        lblFecha.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        panelContenido.add(lblMensaje);
        panelContenido.add(Box.createVerticalStrut(4));
        panelContenido.add(lblFecha);
        
        JPanel panelDerecha = new JPanel();
        panelDerecha.setLayout(new BoxLayout(panelDerecha, BoxLayout.Y_AXIS));
        panelDerecha.setBackground(tarjeta.getBackground());
        
        if (!notificacion.isLeida()) {
            JLabel lblNueva = new JLabel("●");
            lblNueva.setFont(new Font("Segoe UI", Font.BOLD, 20));
            lblNueva.setForeground(INSTAGRAM_PINK);
            lblNueva.setAlignmentX(Component.CENTER_ALIGNMENT);
            panelDerecha.add(lblNueva);
        }
        
        tarjeta.add(lblIcono, BorderLayout.WEST);
        tarjeta.add(panelContenido, BorderLayout.CENTER);
        tarjeta.add(panelDerecha, BorderLayout.EAST);
        
        tarjeta.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                if (!notificacion.isLeida()) {
                    String username = gestorINSTA.getUsernameActual();
                    gestorNotificaciones.marcarComoLeida(username, notificacion.getId());
                    actualizarContenido();
                }
            }
        });
        
        return tarjeta;
    }
    
    private void mostrarMensajeVacio() {
        JPanel panelVacio = new JPanel();
        panelVacio.setLayout(new BoxLayout(panelVacio, BoxLayout.Y_AXIS));
        panelVacio.setBackground(CARD_COLOR);
        panelVacio.setBorder(new CompoundBorder(
            new LineBorder(BORDER_COLOR, 2),
            BorderFactory.createEmptyBorder(60, 40, 60, 40)
        ));
        panelVacio.setMaximumSize(new Dimension(500, 240));
        panelVacio.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        JLabel lblIcono = new JLabel();
        try {
            ImageIcon icono = new ImageIcon(getClass().getResource("/Instagram/icons/icon_notificacion.png"));
            Image img = icono.getImage().getScaledInstance(80, 80, Image.SCALE_SMOOTH);
            lblIcono.setIcon(new ImageIcon(img));
        } catch (Exception e) {
            lblIcono.setText("🔔");
            lblIcono.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 60));
        }
        lblIcono.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        JLabel lblTitulo = new JLabel("Sin notificaciones");
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 20));
        lblTitulo.setForeground(INSTAGRAM_PINK);
        lblTitulo.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        JLabel lblMensaje = new JLabel("Aquí aparecerán tus notificaciones");
        lblMensaje.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        lblMensaje.setForeground(TEXT_SECONDARY);
        lblMensaje.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        panelVacio.add(lblIcono);
        panelVacio.add(Box.createVerticalStrut(20));
        panelVacio.add(lblTitulo);
        panelVacio.add(Box.createVerticalStrut(10));
        panelVacio.add(lblMensaje);
        
        panelNotificaciones.add(Box.createVerticalGlue());
        panelNotificaciones.add(panelVacio);
        panelNotificaciones.add(Box.createVerticalGlue());
    }
}