/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Instagram.GUI;

import Instagram.Logica.GestorINSTACompleto;
import Instagram.Modelo.Publicacion;
import Instagram.Modelo.Comentario;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.*;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

/**
 *
 * @author najma
 */
public class TarjetaPublicacion extends JPanel {
   
    private GestorINSTACompleto gestorINSTA;
    private VentanaINSTA ventanaPrincipal;
    private Publicacion publicacion;
    
    private JButton btnLike;
    private JLabel lblLikes;
    private JPanel panelComentarios;
    private JTextField txtComentario;
    
    private static final Color CARD_COLOR = Color.WHITE;
    private static final Color BORDER_COLOR = new Color(255, 192, 203);
    private static final Color TEXT_PRIMARY = new Color(38, 38, 38);
    private static final Color TEXT_SECONDARY = new Color(142, 142, 142);
    private static final Color INSTAGRAM_PINK = new Color(242, 80, 129);
    private static final Color LIKE_COLOR = new Color(237, 73, 86);
    
    private static final DateTimeFormatter FORMATO_FECHA = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
    
    public TarjetaPublicacion(Publicacion publicacion, GestorINSTACompleto gestor, VentanaINSTA ventana) {
        this.publicacion = publicacion;
        this.gestorINSTA = gestor;
        this.ventanaPrincipal = ventana;
        
        initComponents();
    }
    
    private void initComponents() {
        setLayout(new BorderLayout(0, 12));
        setBackground(CARD_COLOR);
        setBorder(new CompoundBorder(
            new LineBorder(BORDER_COLOR, 2),
            BorderFactory.createEmptyBorder(16, 16, 16, 16)
        ));
        setMaximumSize(new Dimension(470, 800));
        setAlignmentX(Component.LEFT_ALIGNMENT);
        
        JPanel panelSuperior = crearPanelSuperior();
        add(panelSuperior, BorderLayout.NORTH);
        
        JPanel panelContenido = crearPanelContenido();
        add(panelContenido, BorderLayout.CENTER);
        
        JPanel panelInferior = crearPanelInferior();
        add(panelInferior, BorderLayout.SOUTH);
    }
    
    private JPanel crearPanelSuperior() {
        JPanel panel = new JPanel(new BorderLayout(10, 0));
        panel.setBackground(CARD_COLOR);
        
        JLabel lblAvatar = new JLabel();
        try {
            ImageIcon avatarIcon = new ImageIcon(getClass().getResource("/Instagram/icons/icon_perfil.png"));
            Image img = avatarIcon.getImage().getScaledInstance(40, 40, Image.SCALE_SMOOTH);
            lblAvatar.setIcon(new ImageIcon(img));
        } catch (Exception e) {
            lblAvatar.setText("👤");
            lblAvatar.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 30));
            lblAvatar.setForeground(INSTAGRAM_PINK);
        }
        lblAvatar.setPreferredSize(new Dimension(40, 40));
        
        JPanel panelInfo = new JPanel();
        panelInfo.setLayout(new BoxLayout(panelInfo, BoxLayout.Y_AXIS));
        panelInfo.setBackground(CARD_COLOR);
        
        JButton btnUsername = new JButton("@" + publicacion.getUsername());
        btnUsername.setFont(new Font("Segoe UI", Font.BOLD, 15));
        btnUsername.setForeground(TEXT_PRIMARY);
        btnUsername.setBackground(CARD_COLOR);
        btnUsername.setBorderPainted(false);
        btnUsername.setContentAreaFilled(false);
        btnUsername.setFocusPainted(false);
        btnUsername.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnUsername.setHorizontalAlignment(SwingConstants.LEFT);
        btnUsername.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        btnUsername.addActionListener(e -> {
            ventanaPrincipal.mostrarPerfilDeUsuario(publicacion.getUsername());
        });
        
        JLabel lblFecha = new JLabel(publicacion.getFechaHora().format(FORMATO_FECHA));
        lblFecha.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lblFecha.setForeground(TEXT_SECONDARY);
        lblFecha.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        panelInfo.add(btnUsername);
        panelInfo.add(lblFecha);
        
        panel.add(lblAvatar, BorderLayout.WEST);
        panel.add(panelInfo, BorderLayout.CENTER);
        
        String usernameActual = gestorINSTA.getUsernameActual();
        if (publicacion.getUsername().equals(usernameActual)) {
            JButton btnEliminar = new JButton();
            try {
                ImageIcon iconEliminar = new ImageIcon(getClass().getResource("/Instagram/icons/icon_eliminar.png"));
                Image img = iconEliminar.getImage().getScaledInstance(20, 20, Image.SCALE_SMOOTH);
                btnEliminar.setIcon(new ImageIcon(img));
            } catch (Exception e) {
                btnEliminar.setText("🗑");
                btnEliminar.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 16));
            }
            
            btnEliminar.setBackground(CARD_COLOR);
            btnEliminar.setBorderPainted(false);
            btnEliminar.setContentAreaFilled(false);
            btnEliminar.setFocusPainted(false);
            btnEliminar.setCursor(new Cursor(Cursor.HAND_CURSOR));
            btnEliminar.setPreferredSize(new Dimension(32, 32));
            
            btnEliminar.addActionListener(e -> eliminarPublicacion());
            
            panel.add(btnEliminar, BorderLayout.EAST);
        }
        
        return panel;
    }
    
    private JPanel crearPanelContenido() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(CARD_COLOR);
        
        if (publicacion.getRutaImagen() != null && !publicacion.getRutaImagen().isEmpty()) {
            JLabel lblImagen = new JLabel();
            lblImagen.setAlignmentX(Component.LEFT_ALIGNMENT);
            lblImagen.setMaximumSize(new Dimension(438, 400));
            
            try {
                ImageIcon iconImagen = new ImageIcon(publicacion.getRutaImagen());
                Image img = iconImagen.getImage();
                
                int anchoOriginal = img.getWidth(null);
                int altoOriginal = img.getHeight(null);
                
                int anchoMax = 438;
                int altoMax = 400;
                
                double escala = Math.min(
                    (double) anchoMax / anchoOriginal,
                    (double) altoMax / altoOriginal
                );
                
                int nuevoAncho = (int) (anchoOriginal * escala);
                int nuevoAlto = (int) (altoOriginal * escala);
                
                Image imgEscalada = img.getScaledInstance(nuevoAncho, nuevoAlto, Image.SCALE_SMOOTH);
                lblImagen.setIcon(new ImageIcon(imgEscalada));
                lblImagen.setBorder(new LineBorder(BORDER_COLOR, 1));
            } catch (Exception e) {
                lblImagen.setText("📷 Imagen no disponible");
                lblImagen.setFont(new Font("Segoe UI", Font.PLAIN, 14));
                lblImagen.setForeground(TEXT_SECONDARY);
                lblImagen.setHorizontalAlignment(SwingConstants.CENTER);
                lblImagen.setPreferredSize(new Dimension(438, 200));
            }
            
            panel.add(lblImagen);
            panel.add(Box.createVerticalStrut(12));
        }
        
        if (publicacion.getContenido() != null && !publicacion.getContenido().trim().isEmpty()) {
            JTextArea txtContenido = new JTextArea(publicacion.getContenido());
            txtContenido.setFont(new Font("Segoe UI", Font.PLAIN, 14));
            txtContenido.setForeground(TEXT_PRIMARY);
            txtContenido.setBackground(CARD_COLOR);
            txtContenido.setEditable(false);
            txtContenido.setLineWrap(true);
            txtContenido.setWrapStyleWord(true);
            txtContenido.setBorder(null);
            txtContenido.setAlignmentX(Component.LEFT_ALIGNMENT);
            txtContenido.setMaximumSize(new Dimension(438, Integer.MAX_VALUE));
            
            panel.add(txtContenido);
        }
        
        return panel;
    }
    
    private JPanel crearPanelInferior() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(CARD_COLOR);
        
        JPanel panelAcciones = new JPanel(new FlowLayout(FlowLayout.LEFT, 12, 0));
        panelAcciones.setBackground(CARD_COLOR);
        panelAcciones.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        btnLike = new JButton();
        String usernameActual = gestorINSTA.getUsernameActual();
        boolean tieneLike = publicacion.tieneLikeDe(usernameActual);
        
        try {
            String iconoPath = tieneLike ? "/Instagram/icons/icon_corazon_lleno.png" : "/Instagram/icons/icon_corazon_vacio.png";
            ImageIcon iconLike = new ImageIcon(getClass().getResource(iconoPath));
            Image img = iconLike.getImage().getScaledInstance(24, 24, Image.SCALE_SMOOTH);
            btnLike.setIcon(new ImageIcon(img));
            btnLike.setText(null);
        } catch (Exception e) {
            btnLike.setText(tieneLike ? "❤" : "🤍");
            btnLike.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 20));
        }
        
        btnLike.setBackground(CARD_COLOR);
        btnLike.setBorderPainted(false);
        btnLike.setContentAreaFilled(false);
        btnLike.setFocusPainted(false);
        btnLike.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnLike.addActionListener(e -> toggleLike());
        
        JButton btnComentar = new JButton();
        try {
            ImageIcon iconComentar = new ImageIcon(getClass().getResource("/Instagram/icons/icon_comentario.png"));
            Image img = iconComentar.getImage().getScaledInstance(24, 24, Image.SCALE_SMOOTH);
            btnComentar.setIcon(new ImageIcon(img));
            btnComentar.setText(null);
        } catch (Exception e) {
            btnComentar.setText("💬");
            btnComentar.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 20));
        }
        
        btnComentar.setBackground(CARD_COLOR);
        btnComentar.setBorderPainted(false);
        btnComentar.setContentAreaFilled(false);
        btnComentar.setFocusPainted(false);
        btnComentar.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnComentar.addActionListener(e -> txtComentario.requestFocus());
        
        panelAcciones.add(btnLike);
        panelAcciones.add(btnComentar);
        
        panel.add(panelAcciones);
        panel.add(Box.createVerticalStrut(8));
        
        lblLikes = new JLabel(obtenerTextoLikes());
        lblLikes.setFont(new Font("Segoe UI", Font.BOLD, 13));
        lblLikes.setForeground(TEXT_PRIMARY);
        lblLikes.setAlignmentX(Component.LEFT_ALIGNMENT);
        panel.add(lblLikes);
        panel.add(Box.createVerticalStrut(12));
        
        panelComentarios = new JPanel();
        panelComentarios.setLayout(new BoxLayout(panelComentarios, BoxLayout.Y_AXIS));
        panelComentarios.setBackground(CARD_COLOR);
        panelComentarios.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        actualizarComentarios();
        
        panel.add(panelComentarios);
        panel.add(Box.createVerticalStrut(8));
        
        JPanel panelNuevoComentario = new JPanel(new BorderLayout(8, 0));
        panelNuevoComentario.setBackground(CARD_COLOR);
        panelNuevoComentario.setAlignmentX(Component.LEFT_ALIGNMENT);
        panelNuevoComentario.setMaximumSize(new Dimension(438, 36));
        
        txtComentario = new JTextField();
        txtComentario.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        txtComentario.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(BORDER_COLOR, 1),
            BorderFactory.createEmptyBorder(6, 10, 6, 10)
        ));
        
        txtComentario.addActionListener(e -> agregarComentario());
        
        JButton btnPublicar = new JButton("Publicar");
        btnPublicar.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btnPublicar.setForeground(Color.WHITE);
        btnPublicar.setBackground(INSTAGRAM_PINK);
        btnPublicar.setBorder(BorderFactory.createEmptyBorder(6, 12, 6, 12));
        btnPublicar.setFocusPainted(false);
        btnPublicar.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnPublicar.addActionListener(e -> agregarComentario());
        
        panelNuevoComentario.add(txtComentario, BorderLayout.CENTER);
        panelNuevoComentario.add(btnPublicar, BorderLayout.EAST);
        
        panel.add(panelNuevoComentario);
        
        return panel;
    }
    
    private void toggleLike() {
        String usernameActual = gestorINSTA.getUsernameActual();
        
        // Trabajar directamente con la publicación
        if (publicacion.tieneLikeDe(usernameActual)) {
            publicacion.quitarLike(usernameActual);
        } else {
            publicacion.darLike(usernameActual);
        }
        
        boolean tieneLikeAhora = publicacion.tieneLikeDe(usernameActual);
        
        // Actualizar icono
        try {
            String iconoPath = tieneLikeAhora ? "/Instagram/icons/icon_corazon_lleno.png" : "/Instagram/icons/icon_corazon_vacio.png";
            ImageIcon iconLike = new ImageIcon(getClass().getResource(iconoPath));
            Image img = iconLike.getImage().getScaledInstance(24, 24, Image.SCALE_SMOOTH);
            btnLike.setIcon(new ImageIcon(img));
            btnLike.setText(null);
        } catch (Exception e) {
            btnLike.setText(tieneLikeAhora ? "❤" : "🤍");
        }
        
        lblLikes.setText(obtenerTextoLikes());
    }
    
    private String obtenerTextoLikes() {
        int cantidad = publicacion.getCantidadLikes();
        if (cantidad == 0) {
            return "Sé el primero en dar like";
        } else if (cantidad == 1) {
            return "1 me gusta";
        } else {
            return cantidad + " me gusta";
        }
    }
    
    private void agregarComentario() {
        String contenido = txtComentario.getText().trim();
        
        if (contenido.isEmpty()) {
            return;
        }
        
        // Crear comentario directamente y agregarlo a la publicación
        Comentario nuevoComentario = new Comentario(
            gestorINSTA.getUsernameActual(), 
            contenido, 
            publicacion.getId()
        );
        publicacion.agregarComentario(nuevoComentario);
        
        txtComentario.setText("");
        actualizarComentarios();
    }
    
    private void actualizarComentarios() {
        panelComentarios.removeAll();
        
        ArrayList<Comentario> comentarios = publicacion.getComentarios();
        
        int maxComentarios = Math.min(3, comentarios.size());
        
        for (int i = 0; i < maxComentarios; i++) {
            Comentario comentario = comentarios.get(i);
            JLabel lblComentario = new JLabel(
                "<html><b>@" + comentario.getUsername() + "</b> " + comentario.getContenido() + "</html>"
            );
            lblComentario.setFont(new Font("Segoe UI", Font.PLAIN, 13));
            lblComentario.setForeground(TEXT_PRIMARY);
            lblComentario.setAlignmentX(Component.LEFT_ALIGNMENT);
            lblComentario.setMaximumSize(new Dimension(438, 50));
            
            panelComentarios.add(lblComentario);
            panelComentarios.add(Box.createVerticalStrut(6));
        }
        
        if (comentarios.size() > 3) {
            JLabel lblMas = new JLabel("Ver los " + comentarios.size() + " comentarios");
            lblMas.setFont(new Font("Segoe UI", Font.PLAIN, 13));
            lblMas.setForeground(TEXT_SECONDARY);
            lblMas.setAlignmentX(Component.LEFT_ALIGNMENT);
            panelComentarios.add(lblMas);
        }
        
        panelComentarios.revalidate();
        panelComentarios.repaint();
    }
    
    private void eliminarPublicacion() {
        int opcion = JOptionPane.showConfirmDialog(
            this,
            "¿Estás seguro de que deseas eliminar esta publicación?",
            "Eliminar Publicación",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.WARNING_MESSAGE
        );
        
        if (opcion == JOptionPane.YES_OPTION) {
            gestorINSTA.eliminarPublicacion(publicacion.getId());
            
            Container parent = getParent();
            parent.remove(this);
            parent.revalidate();
            parent.repaint();
        }
    }
}
