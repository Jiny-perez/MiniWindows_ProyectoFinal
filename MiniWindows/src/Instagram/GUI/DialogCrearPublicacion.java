/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Instagram.GUI;

import Instagram.Logica.GestorINSTACompleto;
import Instagram.Modelo.Publicacion;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.*;
import java.io.File;

/**
 *
 * @author najma
 */
public class DialogCrearPublicacion extends JDialog {
    
    private GestorINSTACompleto gestorINSTA;
    private boolean publicacionCreada = false;
    
    private JTextArea txtContenido;
    private JLabel lblContador;
    private JLabel lblImagenPreview;
    private JButton btnSeleccionarImagen;
    private JButton btnPublicar;
    private File imagenSeleccionada;
    
    private static final Color BACKGROUND_COLOR = new Color(255, 240, 245);
    private static final Color CARD_COLOR = Color.WHITE;
    private static final Color BORDER_COLOR = new Color(255, 192, 203);
    private static final Color TEXT_PRIMARY = new Color(38, 38, 38);
    private static final Color TEXT_SECONDARY = new Color(142, 142, 142);
    private static final Color INSTAGRAM_PINK = new Color(242, 80, 129);
    private static final Color WARNING_COLOR = new Color(220, 53, 69);
    
    public DialogCrearPublicacion(Frame parent, GestorINSTACompleto gestor) {
        super(parent, "Crear Publicación", true);
        this.gestorINSTA = gestor;
        
        initComponents();
        configurarVentana();
    }
    
    private void initComponents() {
        setLayout(new BorderLayout());
        getContentPane().setBackground(BACKGROUND_COLOR);
        
        JPanel panelPrincipal = new JPanel();
        panelPrincipal.setLayout(new BoxLayout(panelPrincipal, BoxLayout.Y_AXIS));
        panelPrincipal.setBackground(CARD_COLOR);
        panelPrincipal.setBorder(BorderFactory.createEmptyBorder(24, 24, 24, 24));
        
        JLabel lblTitulo = new JLabel("Crear nueva publicación");
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 22));
        lblTitulo.setForeground(INSTAGRAM_PINK);
        lblTitulo.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        JLabel lblDescripcion = new JLabel("Comparte algo con tus seguidores");
        lblDescripcion.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        lblDescripcion.setForeground(TEXT_SECONDARY);
        lblDescripcion.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        panelPrincipal.add(lblTitulo);
        panelPrincipal.add(Box.createVerticalStrut(4));
        panelPrincipal.add(lblDescripcion);
        panelPrincipal.add(Box.createVerticalStrut(24));
        
        JLabel lblContenidoLabel = new JLabel("¿Qué estás pensando?");
        lblContenidoLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lblContenidoLabel.setForeground(TEXT_PRIMARY);
        lblContenidoLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        panelPrincipal.add(lblContenidoLabel);
        panelPrincipal.add(Box.createVerticalStrut(8));
        
        txtContenido = new JTextArea(5, 40);
        txtContenido.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        txtContenido.setLineWrap(true);
        txtContenido.setWrapStyleWord(true);
        txtContenido.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(BORDER_COLOR, 2),
            BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));
        
        txtContenido.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                actualizarContador();
            }
        });
        
        JScrollPane scrollContenido = new JScrollPane(txtContenido);
        scrollContenido.setBorder(null);
        scrollContenido.setAlignmentX(Component.LEFT_ALIGNMENT);
        scrollContenido.setMaximumSize(new Dimension(500, 120));
        
        panelPrincipal.add(scrollContenido);
        panelPrincipal.add(Box.createVerticalStrut(8));
        
        lblContador = new JLabel("0 / " + Publicacion.MAX_CARACTERES + " caracteres");
        lblContador.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lblContador.setForeground(TEXT_SECONDARY);
        lblContador.setAlignmentX(Component.LEFT_ALIGNMENT);
        panelPrincipal.add(lblContador);
        panelPrincipal.add(Box.createVerticalStrut(20));
        
        JLabel lblImagenLabel = new JLabel("Imagen (opcional)");
        lblImagenLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lblImagenLabel.setForeground(TEXT_PRIMARY);
        lblImagenLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        panelPrincipal.add(lblImagenLabel);
        panelPrincipal.add(Box.createVerticalStrut(8));
        
        JPanel panelImagen = new JPanel(new BorderLayout(12, 0));
        panelImagen.setBackground(CARD_COLOR);
        panelImagen.setAlignmentX(Component.LEFT_ALIGNMENT);
        panelImagen.setMaximumSize(new Dimension(500, 200));
        
        lblImagenPreview = new JLabel("No hay imagen seleccionada");
        lblImagenPreview.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        lblImagenPreview.setForeground(TEXT_SECONDARY);
        lblImagenPreview.setHorizontalAlignment(SwingConstants.CENTER);
        lblImagenPreview.setBorder(new LineBorder(BORDER_COLOR, 2, true));
        lblImagenPreview.setPreferredSize(new Dimension(200, 180));
        lblImagenPreview.setBackground(new Color(250, 250, 250));
        lblImagenPreview.setOpaque(true);
        
        JPanel panelBotonesImagen = new JPanel();
        panelBotonesImagen.setLayout(new BoxLayout(panelBotonesImagen, BoxLayout.Y_AXIS));
        panelBotonesImagen.setBackground(CARD_COLOR);
        
        btnSeleccionarImagen = new JButton("Seleccionar Imagen");
        btnSeleccionarImagen.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnSeleccionarImagen.setForeground(Color.WHITE);
        btnSeleccionarImagen.setBackground(INSTAGRAM_PINK);
        btnSeleccionarImagen.setOpaque(true);
        btnSeleccionarImagen.setBorderPainted(false);
        btnSeleccionarImagen.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        btnSeleccionarImagen.setFocusPainted(false);
        btnSeleccionarImagen.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnSeleccionarImagen.setAlignmentX(Component.LEFT_ALIGNMENT);
        btnSeleccionarImagen.addActionListener(e -> seleccionarImagen());
        
        JButton btnQuitarImagen = new JButton("Quitar Imagen");
        btnQuitarImagen.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        btnQuitarImagen.setForeground(TEXT_PRIMARY);
        btnQuitarImagen.setBackground(CARD_COLOR);
        btnQuitarImagen.setBorder(new LineBorder(BORDER_COLOR, 2));
        btnQuitarImagen.setFocusPainted(false);
        btnQuitarImagen.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnQuitarImagen.setAlignmentX(Component.LEFT_ALIGNMENT);
        btnQuitarImagen.setMaximumSize(new Dimension(200, 36));
        btnQuitarImagen.addActionListener(e -> quitarImagen());
        
        panelBotonesImagen.add(btnSeleccionarImagen);
        panelBotonesImagen.add(Box.createVerticalStrut(10));
        panelBotonesImagen.add(btnQuitarImagen);
        panelBotonesImagen.add(Box.createVerticalGlue());
        
        panelImagen.add(lblImagenPreview, BorderLayout.WEST);
        panelImagen.add(panelBotonesImagen, BorderLayout.CENTER);
        
        panelPrincipal.add(panelImagen);
        panelPrincipal.add(Box.createVerticalStrut(24));
        
        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.RIGHT, 12, 0));
        panelBotones.setBackground(CARD_COLOR);
        panelBotones.setAlignmentX(Component.LEFT_ALIGNMENT);
        panelBotones.setMaximumSize(new Dimension(500, 40));
        
        JButton btnCancelar = new JButton("Cancelar");
        btnCancelar.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        btnCancelar.setForeground(TEXT_PRIMARY);
        btnCancelar.setBackground(CARD_COLOR);
        btnCancelar.setBorder(new LineBorder(BORDER_COLOR, 2));
        btnCancelar.setFocusPainted(false);
        btnCancelar.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnCancelar.setPreferredSize(new Dimension(100, 36));
        btnCancelar.addActionListener(e -> dispose());
        
        btnPublicar = new JButton("Publicar");
        btnPublicar.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnPublicar.setForeground(Color.WHITE);
        btnPublicar.setBackground(INSTAGRAM_PINK);
        btnPublicar.setBorder(null);
        btnPublicar.setFocusPainted(false);
        btnPublicar.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnPublicar.setPreferredSize(new Dimension(100, 36));
        btnPublicar.addActionListener(e -> publicar());
        
        panelBotones.add(btnCancelar);
        panelBotones.add(btnPublicar);
        
        panelPrincipal.add(panelBotones);
        
        add(panelPrincipal, BorderLayout.CENTER);
    }
    
    private void actualizarContador() {
        int longitud = txtContenido.getText().length();
        lblContador.setText(longitud + " / " + Publicacion.MAX_CARACTERES + " caracteres");
        
        if (longitud > Publicacion.MAX_CARACTERES) {
            lblContador.setForeground(WARNING_COLOR);
            btnPublicar.setEnabled(false);
        } else {
            lblContador.setForeground(TEXT_SECONDARY);
            btnPublicar.setEnabled(true);
        }
    }
    
    private void seleccionarImagen() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Seleccionar Imagen");
        fileChooser.setFileFilter(new javax.swing.filechooser.FileNameExtensionFilter(
            "Imágenes (*.jpg, *.jpeg, *.png, *.gif)", "jpg", "jpeg", "png", "gif"
        ));
        
        int resultado = fileChooser.showOpenDialog(this);
        
        if (resultado == JFileChooser.APPROVE_OPTION) {
            imagenSeleccionada = fileChooser.getSelectedFile();
            mostrarPreviewImagen();
        }
    }
    
    private void mostrarPreviewImagen() {
        if (imagenSeleccionada != null) {
            try {
                ImageIcon iconImagen = new ImageIcon(imagenSeleccionada.getAbsolutePath());
                Image img = iconImagen.getImage();
                
                int anchoMax = 200;
                int altoMax = 180;
                
                double escala = Math.min(
                    (double) anchoMax / img.getWidth(null),
                    (double) altoMax / img.getHeight(null)
                );
                
                int nuevoAncho = (int) (img.getWidth(null) * escala);
                int nuevoAlto = (int) (img.getHeight(null) * escala);
                
                Image imgEscalada = img.getScaledInstance(nuevoAncho, nuevoAlto, Image.SCALE_SMOOTH);
                lblImagenPreview.setIcon(new ImageIcon(imgEscalada));
                lblImagenPreview.setText(null);
            } catch (Exception e) {
                lblImagenPreview.setIcon(null);
                lblImagenPreview.setText("Error al cargar imagen");
            }
        }
    }
    
    private void quitarImagen() {
        imagenSeleccionada = null;
        lblImagenPreview.setIcon(null);
        lblImagenPreview.setText("No hay imagen seleccionada");
    }
    
    private void publicar() {
        String contenido = txtContenido.getText().trim();
        
        // VALIDACIÓN CRÍTICA: 140 CARACTERES
        if (contenido.length() > Publicacion.MAX_CARACTERES) {
            JOptionPane.showMessageDialog(
                this,
                "El contenido no puede exceder " + Publicacion.MAX_CARACTERES + " caracteres.\n" +
                "Caracteres actuales: " + contenido.length(),
                "Contenido demasiado largo",
                JOptionPane.WARNING_MESSAGE
            );
            return;
        }
        
        if (contenido.isEmpty() && imagenSeleccionada == null) {
            JOptionPane.showMessageDialog(
                this,
                "Debes escribir algo o agregar una imagen",
                "Publicación vacía",
                JOptionPane.WARNING_MESSAGE
            );
            return;
        }
        
        try {
            if (imagenSeleccionada != null) {
                gestorINSTA.crearPublicacion(contenido, imagenSeleccionada.getAbsolutePath());
            } else {
                gestorINSTA.crearPublicacion(contenido);
            }
            
            publicacionCreada = true;
            
            JOptionPane.showMessageDialog(
                this,
                "¡Publicación creada exitosamente!",
                "Éxito",
                JOptionPane.INFORMATION_MESSAGE
            );
            
            dispose();
        } catch (IllegalArgumentException e) {
            JOptionPane.showMessageDialog(
                this,
                e.getMessage(),
                "Error",
                JOptionPane.ERROR_MESSAGE
            );
        } catch (Exception e) {
            JOptionPane.showMessageDialog(
                this,
                "Error al crear la publicación: " + e.getMessage(),
                "Error",
                JOptionPane.ERROR_MESSAGE
            );
        }
    }
    
    private void configurarVentana() {
        setSize(580, 650);
        setMinimumSize(new Dimension(580, 650));
        setLocationRelativeTo(getParent());
        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        setResizable(false);
    }
    
    public boolean isPublicacionCreada() {
        return publicacionCreada;
    }
}
