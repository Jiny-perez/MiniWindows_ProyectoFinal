/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Instagram.GUI;

import Instagram.Logica.GestorINSTA;
import Instagram.Modelo.Publicacion;
import Instagram.Modelo.Comentario;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.*;
import java.util.ArrayList;
import java.io.File;
import javax.imageio.ImageIO;

/**
 *
 * @author najma
 */
public class TarjetaPublicacion extends JPanel {
     
    private Publicacion publicacion;
    private GestorINSTA gestorINSTA;
    private VentanaINSTA ventanaPrincipal;
    
    private JLabel lblLikes;
    private JLabel lblComentarios;
    private JButton btnLike;
    private JPanel panelComentarios;
    
    private static final Color BACKGROUND_COLOR = Color.WHITE;
    private static final Color BORDER_COLOR = new Color(255, 192, 203);
    private static final Color TEXT_PRIMARY = new Color(38, 38, 38);
    private static final Color TEXT_SECONDARY = new Color(142, 142, 142);
    private static final Color INSTAGRAM_PINK = new Color(242, 80, 129);
    
    public TarjetaPublicacion(Publicacion publicacion, GestorINSTA gestor, VentanaINSTA ventana) {
        this.publicacion = publicacion;
        this.gestorINSTA = gestor;
        this.ventanaPrincipal = ventana;
        
        initComponents();
    }
    
    private void initComponents() {
        setLayout(new BorderLayout());
        setBackground(BACKGROUND_COLOR);
        setBorder(new CompoundBorder(
            new LineBorder(BORDER_COLOR, 2),
            BorderFactory.createEmptyBorder(0, 0, 0, 0)
        ));
        setMaximumSize(new Dimension(470, 2000));
        setAlignmentX(Component.CENTER_ALIGNMENT);
        
        add(crearHeader(), BorderLayout.NORTH);
        add(crearCentro(), BorderLayout.CENTER);
        add(crearFooter(), BorderLayout.SOUTH);
    }
    
    private JPanel crearHeader() {
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(BACKGROUND_COLOR);
        header.setBorder(BorderFactory.createEmptyBorder(12, 12, 12, 12));
        
        JPanel panelUsuario = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        panelUsuario.setBackground(BACKGROUND_COLOR);
        
        JLabel lblAvatar = new JLabel();
        try {

            ImageIcon avatarIcon = new ImageIcon(getClass().getResource("/Instagram/icons/icon_perfil.png"));
            Image img = avatarIcon.getImage().getScaledInstance(32, 32, Image.SCALE_SMOOTH);
            lblAvatar.setIcon(new ImageIcon(img));
        } catch (Exception e) {
            lblAvatar.setText("👤");
            lblAvatar.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 24));
        }
        lblAvatar.setPreferredSize(new Dimension(32, 32));
        panelUsuario.add(lblAvatar);
        
        JButton btnUsuario = new JButton("@" + publicacion.getUsername());
        btnUsuario.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnUsuario.setForeground(TEXT_PRIMARY);
        btnUsuario.setBackground(BACKGROUND_COLOR);
        btnUsuario.setBorderPainted(false);
        btnUsuario.setContentAreaFilled(false);
        btnUsuario.setFocusPainted(false);
        btnUsuario.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnUsuario.setHorizontalAlignment(SwingConstants.LEFT);
        
        btnUsuario.addActionListener(e -> {
            ventanaPrincipal.mostrarPerfilDeUsuario(publicacion.getUsername());
        });
        
        panelUsuario.add(btnUsuario);
        header.add(panelUsuario, BorderLayout.WEST);
        
        if (publicacion.getUsername().equals(gestorINSTA.getUsernameActual())) {
            JButton btnOpciones = new JButton("⋮");
            btnOpciones.setFont(new Font("Arial Unicode MS", Font.BOLD, 24));
            btnOpciones.setForeground(INSTAGRAM_PINK);
            btnOpciones.setBackground(BACKGROUND_COLOR);
            btnOpciones.setBorder(null);
            btnOpciones.setContentAreaFilled(false);
            btnOpciones.setFocusPainted(false);
            btnOpciones.setCursor(new Cursor(Cursor.HAND_CURSOR));
            btnOpciones.setMargin(new Insets(0, 0, 0, 0));
            
            btnOpciones.addActionListener(e -> mostrarMenuOpciones());
            
            header.add(btnOpciones, BorderLayout.EAST);
        }
        
        return header;
    }
    
    private JPanel crearCentro() {
        JPanel centro = new JPanel(new BorderLayout());
        centro.setBackground(BACKGROUND_COLOR);
        
        // Imagen si existe
        if (publicacion.tieneImagen()) {
            JLabel lblImagen = cargarImagen(publicacion.getRutaImagen());
            if (lblImagen != null) {
                centro.add(lblImagen, BorderLayout.NORTH);
            }
        }
        
        if (publicacion.getContenido() != null && !publicacion.getContenido().trim().isEmpty()) {
            JTextArea txtContenido = new JTextArea(publicacion.getContenido());
            txtContenido.setFont(new Font("Segoe UI", Font.PLAIN, 14));
            txtContenido.setForeground(TEXT_PRIMARY);
            txtContenido.setBackground(BACKGROUND_COLOR);
            txtContenido.setLineWrap(true);
            txtContenido.setWrapStyleWord(true);
            txtContenido.setEditable(false);
            txtContenido.setBorder(BorderFactory.createEmptyBorder(12, 12, 8, 12));
            
            centro.add(txtContenido, BorderLayout.CENTER);
        }
        
        return centro;
    }
    
    private JLabel cargarImagen(String rutaImagen) {
        try {
            File archivoImagen = new File(rutaImagen);
            if (archivoImagen.exists()) {
                Image imagen = ImageIO.read(archivoImagen);
                
                int anchoDeseado = 470;
                int altoOriginal = imagen.getHeight(null);
                int anchoOriginal = imagen.getWidth(null);
                int altoDeseado = (altoOriginal * anchoDeseado) / anchoOriginal;
                
                if (altoDeseado > 600) {
                    altoDeseado = 600;
                }
                
                Image imagenEscalada = imagen.getScaledInstance(anchoDeseado, altoDeseado, Image.SCALE_SMOOTH);
                
                JLabel lblImagen = new JLabel(new ImageIcon(imagenEscalada));
                lblImagen.setPreferredSize(new Dimension(anchoDeseado, altoDeseado));
                lblImagen.setBorder(new MatteBorder(1, 0, 1, 0, BORDER_COLOR));
                
                return lblImagen;
            }
        } catch (Exception e) {
            System.err.println("Error al cargar imagen: " + e.getMessage());
        }
        
        return null;
    }
    
    private JPanel crearFooter() {
        JPanel footer = new JPanel();
        footer.setLayout(new BoxLayout(footer, BoxLayout.Y_AXIS));
        footer.setBackground(BACKGROUND_COLOR);
        footer.setBorder(BorderFactory.createEmptyBorder(4, 12, 12, 12));
        
        // Panel de acciones (Like y Comentar)
        JPanel panelAcciones = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        panelAcciones.setBackground(BACKGROUND_COLOR);
        
        boolean tienelike = publicacion.tieneLikeDe(gestorINSTA.getUsernameActual());
        btnLike = new JButton();
        try {
            String iconoPath = tienelike ? "/Instagram/icons/icon_corazon_lleno.png" : "/Instagram/icons/icon_corazon_vacio.png";
            ImageIcon iconLike = new ImageIcon(getClass().getResource(iconoPath));
            Image img = iconLike.getImage().getScaledInstance(24, 24, Image.SCALE_SMOOTH);
            btnLike.setIcon(new ImageIcon(img));
        } catch (Exception e) {
            btnLike.setText(tienelike ? "❤" : "🤍");
            btnLike.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 20));
        }
        btnLike.setBackground(BACKGROUND_COLOR);
        btnLike.setBorderPainted(false);
        btnLike.setContentAreaFilled(false);
        btnLike.setFocusPainted(false);
        btnLike.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnLike.addActionListener(e -> toggleLike());
        
        JButton btnComentar = new JButton();
        try {
            ImageIcon iconComment = new ImageIcon(getClass().getResource("/Instagram/icons/icon_comentario.png"));
            Image img = iconComment.getImage().getScaledInstance(24, 24, Image.SCALE_SMOOTH);
            btnComentar.setIcon(new ImageIcon(img));
        } catch (Exception e) {
            btnComentar.setText("💬");
            btnComentar.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 20));
        }
        btnComentar.setBackground(BACKGROUND_COLOR);
        btnComentar.setBorderPainted(false);
        btnComentar.setContentAreaFilled(false);
        btnComentar.setFocusPainted(false);
        btnComentar.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnComentar.addActionListener(e -> mostrarComentarios());
        
        panelAcciones.add(btnLike);
        panelAcciones.add(btnComentar);
        
        footer.add(panelAcciones);
        footer.add(Box.createVerticalStrut(8));
        
        lblLikes = new JLabel(obtenerTextoLikes());
        lblLikes.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lblLikes.setForeground(TEXT_PRIMARY);
        lblLikes.setAlignmentX(Component.LEFT_ALIGNMENT);
        footer.add(lblLikes);
        
        footer.add(Box.createVerticalStrut(4));
        
        JLabel lblTiempo = new JLabel(publicacion.getTiempoTranscurrido());
        lblTiempo.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lblTiempo.setForeground(TEXT_SECONDARY);
        lblTiempo.setAlignmentX(Component.LEFT_ALIGNMENT);
        footer.add(lblTiempo);
        
        panelComentarios = new JPanel();
        panelComentarios.setLayout(new BoxLayout(panelComentarios, BoxLayout.Y_AXIS));
        panelComentarios.setBackground(BACKGROUND_COLOR);
        panelComentarios.setVisible(false);
        footer.add(panelComentarios);
        
        return footer;
    }
    
    private void toggleLike() {
        String usernameActual = gestorINSTA.getUsernameActual();
        boolean tenialikeAntes = publicacion.tieneLikeDe(usernameActual);
        
        gestorINSTA.toggleLike(publicacion.getId());
        
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
            return "1 Me gusta";
        } else {
            return cantidad + " Me gusta";
        }
    }
    
    private void mostrarComentarios() {
        if (panelComentarios.isVisible()) {
            panelComentarios.setVisible(false);
        } else {
            actualizarComentarios();
            panelComentarios.setVisible(true);
        }
        
        revalidate();
        repaint();
    }
    
    private void actualizarComentarios() {
        panelComentarios.removeAll();
        
        panelComentarios.add(Box.createVerticalStrut(10));
        
        ArrayList<Comentario> comentarios = publicacion.getComentarios();
        
        for (Comentario comentario : comentarios) {
            JPanel panelComentario = new JPanel(new BorderLayout());
            panelComentario.setBackground(BACKGROUND_COLOR);
            panelComentario.setBorder(BorderFactory.createEmptyBorder(4, 0, 4, 0));
            panelComentario.setMaximumSize(new Dimension(450, 100));
            
            JTextArea txtComentario = new JTextArea();
            txtComentario.setText("@" + comentario.getUsername() + " " + comentario.getContenido());
            txtComentario.setFont(new Font("Segoe UI", Font.PLAIN, 14));
            txtComentario.setForeground(TEXT_PRIMARY);
            txtComentario.setBackground(BACKGROUND_COLOR);
            txtComentario.setLineWrap(true);
            txtComentario.setWrapStyleWord(true);
            txtComentario.setEditable(false);
            
            panelComentario.add(txtComentario, BorderLayout.CENTER);
            panelComentarios.add(panelComentario);
        }
        
        JPanel panelNuevoComentario = new JPanel(new BorderLayout(5, 0));
        panelNuevoComentario.setBackground(BACKGROUND_COLOR);
        panelNuevoComentario.setBorder(BorderFactory.createEmptyBorder(8, 0, 0, 0));
        
        JTextField txtNuevoComentario = new JTextField();
        txtNuevoComentario.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        txtNuevoComentario.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(BORDER_COLOR, 1),
            BorderFactory.createEmptyBorder(6, 8, 6, 8)
        ));
        
        JButton btnPublicar = new JButton("Publicar");
        btnPublicar.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnPublicar.setForeground(Color.WHITE);
        btnPublicar.setBackground(INSTAGRAM_PINK);
        btnPublicar.setBorderPainted(false);
        btnPublicar.setFocusPainted(false);
        btnPublicar.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        btnPublicar.addActionListener(e -> {
            String contenido = txtNuevoComentario.getText().trim();
            if (!contenido.isEmpty()) {
                gestorINSTA.agregarComentario(publicacion.getId(), contenido);
                txtNuevoComentario.setText("");
                actualizarComentarios();
                revalidate();
                repaint();
            }
        });
        
        txtNuevoComentario.addActionListener(e -> btnPublicar.doClick());
        
        panelNuevoComentario.add(txtNuevoComentario, BorderLayout.CENTER);
        panelNuevoComentario.add(btnPublicar, BorderLayout.EAST);
        
        panelComentarios.add(panelNuevoComentario);
    }
    
    private void mostrarMenuOpciones() {
        JPopupMenu menu = new JPopupMenu();
        
        JMenuItem itemEliminar = new JMenuItem("Eliminar publicación");
        itemEliminar.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        itemEliminar.setForeground(INSTAGRAM_PINK);
        itemEliminar.addActionListener(e -> eliminarPublicacion());
        
        menu.add(itemEliminar);
        menu.show(this, getWidth() / 2, 50);
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
            ventanaPrincipal.mostrarTimeline();
        }
    }
}