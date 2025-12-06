/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Instagram.GUI;

import Instagram.Logica.GestorINSTA;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.io.File;

/**
 *
 * @author najma
 */
public class DialogCrearPublicacion extends JDialog {
    
    private GestorINSTA gestorINSTA;
    private boolean publicacionCreada = false;
    
    private JTextArea txtContenido;
    private JLabel lblImagenPreview;
    private JButton btnSeleccionarImagen;
    private JButton btnQuitarImagen;
    private File imagenSeleccionada;
    
    private static final Color BACKGROUND_COLOR = Color.WHITE;
    private static final Color BORDER_COLOR = new Color(255, 192, 203);
    private static final Color TEXT_PRIMARY = new Color(38, 38, 38);
    private static final Color TEXT_SECONDARY = new Color(142, 142, 142);
    private static final Color INSTAGRAM_PINK = new Color(242, 80, 129);
    
    public DialogCrearPublicacion(Frame parent, GestorINSTA gestor) {
        super(parent, true);
        this.gestorINSTA = gestor;
        setUndecorated(true);
        
        initComponents();
        configurarDialogo();
    }
    
    private void initComponents() {
        setLayout(new BorderLayout(0, 0));
        getContentPane().setBackground(BACKGROUND_COLOR);
        
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(BACKGROUND_COLOR);
        header.setBorder(new CompoundBorder(
            new MatteBorder(0, 0, 1, 0, BORDER_COLOR),
            BorderFactory.createEmptyBorder(12, 16, 12, 16)
        ));
        
        JLabel lblTitulo = new JLabel("Crear nueva publicación");
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 16));
        lblTitulo.setForeground(INSTAGRAM_PINK);
        
        JButton btnCerrar = new JButton("×");
        btnCerrar.setFont(new Font("Segoe UI", Font.PLAIN, 28));
        btnCerrar.setForeground(INSTAGRAM_PINK);
        btnCerrar.setBackground(BACKGROUND_COLOR);
        btnCerrar.setBorderPainted(false);
        btnCerrar.setContentAreaFilled(false);
        btnCerrar.setFocusPainted(false);
        btnCerrar.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnCerrar.addActionListener(e -> dispose());
        
        header.add(lblTitulo, BorderLayout.CENTER);
        header.add(btnCerrar, BorderLayout.EAST);
        
        add(header, BorderLayout.NORTH);
        
        JPanel centro = new JPanel();
        centro.setLayout(new BoxLayout(centro, BoxLayout.Y_AXIS));
        centro.setBackground(BACKGROUND_COLOR);
        centro.setBorder(BorderFactory.createEmptyBorder(16, 16, 16, 16));
        
        JLabel lblContenido = new JLabel("Escribe algo...");
        lblContenido.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        lblContenido.setForeground(TEXT_SECONDARY);
        lblContenido.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        txtContenido = new JTextArea(6, 40);
        txtContenido.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        txtContenido.setForeground(TEXT_PRIMARY);
        txtContenido.setLineWrap(true);
        txtContenido.setWrapStyleWord(true);
        txtContenido.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(BORDER_COLOR, 2),
            BorderFactory.createEmptyBorder(8, 8, 8, 8)
        ));
        
        JScrollPane scrollContenido = new JScrollPane(txtContenido);
        scrollContenido.setBorder(null);
        scrollContenido.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        centro.add(lblContenido);
        centro.add(Box.createVerticalStrut(8));
        centro.add(scrollContenido);
        centro.add(Box.createVerticalStrut(16));
        
        JPanel panelImagen = new JPanel();
        panelImagen.setLayout(new BoxLayout(panelImagen, BoxLayout.Y_AXIS));
        panelImagen.setBackground(BACKGROUND_COLOR);
        panelImagen.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        btnSeleccionarImagen = new JButton("Agregar imagen");
        btnSeleccionarImagen.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        btnSeleccionarImagen.setForeground(INSTAGRAM_PINK);
        btnSeleccionarImagen.setBackground(BACKGROUND_COLOR);
        btnSeleccionarImagen.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(INSTAGRAM_PINK, 2),
            BorderFactory.createEmptyBorder(8, 16, 8, 16)
        ));
        btnSeleccionarImagen.setFocusPainted(false);
        btnSeleccionarImagen.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnSeleccionarImagen.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        try {
            ImageIcon iconCamera = new ImageIcon(getClass().getResource("/Instagram/icons/icon_crear.png"));
            Image img = iconCamera.getImage().getScaledInstance(20, 20, Image.SCALE_SMOOTH);
            btnSeleccionarImagen.setIcon(new ImageIcon(img));
            btnSeleccionarImagen.setHorizontalTextPosition(SwingConstants.RIGHT);
            btnSeleccionarImagen.setIconTextGap(8);
        } catch (Exception e) {
        }
        
        btnSeleccionarImagen.addActionListener(e -> seleccionarImagen());
        
        lblImagenPreview = new JLabel();
        lblImagenPreview.setBorder(new LineBorder(BORDER_COLOR, 2));
        lblImagenPreview.setAlignmentX(Component.LEFT_ALIGNMENT);
        lblImagenPreview.setVisible(false);
        
        btnQuitarImagen = new JButton("Quitar imagen");
        btnQuitarImagen.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        btnQuitarImagen.setForeground(INSTAGRAM_PINK);
        btnQuitarImagen.setBackground(BACKGROUND_COLOR);
        btnQuitarImagen.setBorderPainted(false);
        btnQuitarImagen.setContentAreaFilled(false);
        btnQuitarImagen.setFocusPainted(false);
        btnQuitarImagen.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnQuitarImagen.setAlignmentX(Component.LEFT_ALIGNMENT);
        btnQuitarImagen.setVisible(false);
        btnQuitarImagen.addActionListener(e -> quitarImagen());
        
        panelImagen.add(btnSeleccionarImagen);
        panelImagen.add(Box.createVerticalStrut(12));
        panelImagen.add(lblImagenPreview);
        panelImagen.add(Box.createVerticalStrut(8));
        panelImagen.add(btnQuitarImagen);
        
        centro.add(panelImagen);
        
        add(centro, BorderLayout.CENTER);
        
        JPanel footer = new JPanel(new FlowLayout(FlowLayout.RIGHT, 12, 12));
        footer.setBackground(BACKGROUND_COLOR);
        footer.setBorder(new MatteBorder(1, 0, 0, 0, BORDER_COLOR));
        
        JButton btnCancelar = new JButton("Cancelar");
        btnCancelar.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        btnCancelar.setForeground(TEXT_PRIMARY);
        btnCancelar.setBackground(BACKGROUND_COLOR);
        btnCancelar.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(BORDER_COLOR, 2),
            BorderFactory.createEmptyBorder(8, 20, 8, 20)
        ));
        btnCancelar.setFocusPainted(false);
        btnCancelar.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnCancelar.addActionListener(e -> dispose());
        
        JButton btnPublicar = new JButton("Publicar");
        btnPublicar.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnPublicar.setForeground(Color.WHITE);
        btnPublicar.setBackground(INSTAGRAM_PINK);
        btnPublicar.setBorder(BorderFactory.createEmptyBorder(8, 24, 8, 24));
        btnPublicar.setFocusPainted(false);
        btnPublicar.setOpaque(true);
        btnPublicar.setBorderPainted(false);
        btnPublicar.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnPublicar.addActionListener(e -> publicar());
        
        footer.add(btnCancelar);
        footer.add(btnPublicar);
        
        add(footer, BorderLayout.SOUTH);
    }
    
    private void seleccionarImagen() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Seleccionar imagen");
        fileChooser.setFileFilter(new FileNameExtensionFilter(
            "Imágenes (*.png, *.jpg, *.jpeg, *.gif)", "png", "jpg", "jpeg", "gif"
        ));
        
        int resultado = fileChooser.showOpenDialog(this);
        
        if (resultado == JFileChooser.APPROVE_OPTION) {
            imagenSeleccionada = fileChooser.getSelectedFile();
            mostrarPreviewImagen();
        }
    }
    
    private void mostrarPreviewImagen() {
        try {
            ImageIcon iconoOriginal = new ImageIcon(imagenSeleccionada.getAbsolutePath());
            
            int anchoMax = 400;
            int altoMax = 300;
            
            Image imagen = iconoOriginal.getImage();
            int anchoOriginal = imagen.getWidth(null);
            int altoOriginal = imagen.getHeight(null);
            
            double escala = Math.min(
                (double) anchoMax / anchoOriginal,
                (double) altoMax / altoOriginal
            );
            
            int nuevoAncho = (int) (anchoOriginal * escala);
            int nuevoAlto = (int) (altoOriginal * escala);
            
            Image imagenEscalada = imagen.getScaledInstance(nuevoAncho, nuevoAlto, Image.SCALE_SMOOTH);
            
            lblImagenPreview.setIcon(new ImageIcon(imagenEscalada));
            lblImagenPreview.setVisible(true);
            btnQuitarImagen.setVisible(true);
            btnSeleccionarImagen.setText("Cambiar imagen");
            
            revalidate();
            repaint();
            
        } catch (Exception e) {
            JOptionPane.showMessageDialog(
                this,
                "Error al cargar la imagen",
                "Error",
                JOptionPane.ERROR_MESSAGE
            );
        }
    }
    
    private void quitarImagen() {
        imagenSeleccionada = null;
        lblImagenPreview.setIcon(null);
        lblImagenPreview.setVisible(false);
        btnQuitarImagen.setVisible(false);
        btnSeleccionarImagen.setText("Agregar imagen");
        
        revalidate();
        repaint();
    }
    
    private void publicar() {
        String contenido = txtContenido.getText().trim();
        
        if (contenido.isEmpty() && imagenSeleccionada == null) {
            JOptionPane.showMessageDialog(
                this,
                "Debes escribir algo o agregar una imagen",
                "Publicación vacía",
                JOptionPane.WARNING_MESSAGE
            );
            return;
        }
        
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
    }
    
    private void configurarDialogo() {
        setSize(600, 650);
        setResizable(false);
        setLocationRelativeTo(getParent());
    }
    
    public boolean isPublicacionCreada() {
        return publicacionCreada;
    }
}
