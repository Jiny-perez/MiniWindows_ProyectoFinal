/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Instagram.GUI;

import GestorArchivos.Archivo;
import GestorArchivos.SistemaArchivos;
import GestorArchivos.MiniWindowsClass;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.util.ArrayList;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;

/**
 *
 * @author najma
 */
public class DialogSelectorImagenes extends JDialog {
    
    private File archivoSeleccionado;
    private SistemaArchivos sistemaArchivos;
    private JPanel panelImagenes;
    private JScrollPane scrollPane;
    
    private static final Color BACKGROUND_COLOR = new Color(255, 240, 245);
    private static final Color CARD_COLOR = Color.WHITE;
    private static final Color BORDER_COLOR = new Color(255, 192, 203);
    private static final Color TEXT_PRIMARY = new Color(38, 38, 38);
    private static final Color TEXT_SECONDARY = new Color(142, 142, 142);
    private static final Color INSTAGRAM_PINK = new Color(242, 80, 129);
    private static final Color HOVER_COLOR = new Color(255, 230, 240);
    
    public DialogSelectorImagenes(Frame parent) {
        super(parent, "Seleccionar Imagen - Mis Imágenes", true);
        
        MiniWindowsClass sistema = MiniWindowsClass.getInstance();
        this.sistemaArchivos = sistema.getSistemaArchivos();
        
        initComponents();
        cargarImagenes();
        configurarVentana();
    }
    
    private void initComponents() {
        setLayout(new BorderLayout());
        getContentPane().setBackground(BACKGROUND_COLOR);
        
        JPanel panelSuperior = new JPanel(new BorderLayout());
        panelSuperior.setBackground(CARD_COLOR);
        panelSuperior.setBorder(new CompoundBorder(
            new MatteBorder(0, 0, 1, 0, BORDER_COLOR),
            BorderFactory.createEmptyBorder(16, 20, 16, 20)
        ));
        
        JLabel lblTitulo = new JLabel("Selecciona una imagen de 'Mis Imagenes'");
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 20));
        lblTitulo.setForeground(INSTAGRAM_PINK);
        
        JLabel lblDescripcion = new JLabel("Haz clic en una imagen para seleccionarla");
        lblDescripcion.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        lblDescripcion.setForeground(TEXT_SECONDARY);
        
        JPanel panelTextos = new JPanel();
        panelTextos.setLayout(new BoxLayout(panelTextos, BoxLayout.Y_AXIS));
        panelTextos.setBackground(CARD_COLOR);
        
        lblTitulo.setAlignmentX(Component.LEFT_ALIGNMENT);
        lblDescripcion.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        panelTextos.add(lblTitulo);
        panelTextos.add(Box.createVerticalStrut(5));
        panelTextos.add(lblDescripcion);
        
        panelSuperior.add(panelTextos, BorderLayout.CENTER);
        add(panelSuperior, BorderLayout.NORTH);
        
        panelImagenes = new JPanel(new GridLayout(0, 3, 15, 15));
        panelImagenes.setBackground(BACKGROUND_COLOR);
        panelImagenes.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        scrollPane = new JScrollPane(panelImagenes);
        scrollPane.setBorder(null);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        
        add(scrollPane, BorderLayout.CENTER);
        
        JPanel panelInferior = new JPanel(new FlowLayout(FlowLayout.RIGHT, 12, 0));
        panelInferior.setBackground(CARD_COLOR);
        panelInferior.setBorder(new CompoundBorder(
            new MatteBorder(1, 0, 0, 0, BORDER_COLOR),
            BorderFactory.createEmptyBorder(16, 20, 16, 20)
        ));
        
        JButton btnCancelar = new JButton("Cancelar");
        btnCancelar.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        btnCancelar.setForeground(TEXT_PRIMARY);
        btnCancelar.setBackground(CARD_COLOR);
        btnCancelar.setBorder(new LineBorder(BORDER_COLOR, 2));
        btnCancelar.setFocusPainted(false);
        btnCancelar.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnCancelar.setPreferredSize(new Dimension(100, 36));
        btnCancelar.addActionListener(e -> {
            archivoSeleccionado = null;
            dispose();
        });
        
        panelInferior.add(btnCancelar);
        add(panelInferior, BorderLayout.SOUTH);
    }
    
    private void cargarImagenes() {
        panelImagenes.removeAll();
        
        try {
            String rutaOriginal = sistemaArchivos.getRutaActual();
            
            try {
                String usuarioActual = obtenerUsuarioActual();
                String rutaMisImagenes = "Z:\\" + usuarioActual + "\\Mis Imagenes";
                
                sistemaArchivos.navegarARuta(rutaMisImagenes);
            } catch (Exception e) {
                mostrarMensajeVacio();
                return;
            }
            
            ArrayList<Archivo> archivos = sistemaArchivos.listarContenido();
            
            if (archivos == null || archivos.isEmpty()) {
                mostrarMensajeVacio();
                return;
            }
            
            ArrayList<File> imagenes = new ArrayList<>();
            for (Archivo archivo : archivos) {
                if (!archivo.isEsCarpeta() && esImagen(archivo.getNombre())) {
                    File file = new File(archivo.getRutaAbsoluta());
                    if (file.exists()) {
                        imagenes.add(file);
                    }
                }
            }
            
            if (imagenes.isEmpty()) {
                mostrarMensajeVacio();
                return;
            }
            
            for (File imagen : imagenes) {
                JPanel tarjeta = crearTarjetaImagen(imagen);
                panelImagenes.add(tarjeta);
            }
            
            try {
                sistemaArchivos.navegarARuta(rutaOriginal);
            } catch (Exception e) {
            }
            
        } catch (Exception e) {
            e.printStackTrace();
            mostrarMensajeVacio();
        }
        
        panelImagenes.revalidate();
        panelImagenes.repaint();
    }
    
    private JPanel crearTarjetaImagen(File archivo) {
        JPanel tarjeta = new JPanel(new BorderLayout());
        tarjeta.setBackground(CARD_COLOR);
        tarjeta.setBorder(new CompoundBorder(
            new LineBorder(BORDER_COLOR, 2),
            BorderFactory.createEmptyBorder(8, 8, 8, 8)
        ));
        tarjeta.setCursor(new Cursor(Cursor.HAND_CURSOR));
        tarjeta.setPreferredSize(new Dimension(200, 220));
        
        JLabel lblImagen = new JLabel();
        lblImagen.setHorizontalAlignment(SwingConstants.CENTER);
        lblImagen.setVerticalAlignment(SwingConstants.CENTER);
        lblImagen.setPreferredSize(new Dimension(180, 180));
        lblImagen.setBackground(new Color(250, 250, 250));
        lblImagen.setOpaque(true);
        
        try {
            BufferedImage img = ImageIO.read(archivo);
            if (img != null) {
                int anchoMax = 180;
                int altoMax = 180;
                
                double escala = Math.min(
                    (double) anchoMax / img.getWidth(),
                    (double) altoMax / img.getHeight()
                );
                
                int nuevoAncho = (int) (img.getWidth() * escala);
                int nuevoAlto = (int) (img.getHeight() * escala);
                
                Image imgEscalada = img.getScaledInstance(nuevoAncho, nuevoAlto, Image.SCALE_SMOOTH);
                lblImagen.setIcon(new ImageIcon(imgEscalada));
            }
        } catch (Exception e) {
            lblImagen.setText("📷");
            lblImagen.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 40));
            lblImagen.setForeground(TEXT_SECONDARY);
        }
        
        JLabel lblNombre = new JLabel(archivo.getName());
        lblNombre.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        lblNombre.setForeground(TEXT_PRIMARY);
        lblNombre.setHorizontalAlignment(SwingConstants.CENTER);
        
        tarjeta.add(lblImagen, BorderLayout.CENTER);
        tarjeta.add(lblNombre, BorderLayout.SOUTH);
        
        tarjeta.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                tarjeta.setBackground(HOVER_COLOR);
                tarjeta.setBorder(new CompoundBorder(
                    new LineBorder(INSTAGRAM_PINK, 2),
                    BorderFactory.createEmptyBorder(8, 8, 8, 8)
                ));
            }
            
            @Override
            public void mouseExited(MouseEvent e) {
                tarjeta.setBackground(CARD_COLOR);
                tarjeta.setBorder(new CompoundBorder(
                    new LineBorder(BORDER_COLOR, 2),
                    BorderFactory.createEmptyBorder(8, 8, 8, 8)
                ));
            }
            
            @Override
            public void mouseClicked(MouseEvent e) {
                archivoSeleccionado = archivo;
                dispose();
            }
        });
        
        return tarjeta;
    }
    
    private void mostrarMensajeVacio() {
        panelImagenes.setLayout(new BorderLayout());
        panelImagenes.removeAll();
        
        JPanel panelMensaje = new JPanel();
        panelMensaje.setLayout(new BoxLayout(panelMensaje, BoxLayout.Y_AXIS));
        panelMensaje.setBackground(BACKGROUND_COLOR);
        
        JLabel lblIcono = new JLabel("📷");
        lblIcono.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 60));
        lblIcono.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        JLabel lblMensaje = new JLabel("No hay imágenes en 'Mis Imagenes'");
        lblMensaje.setFont(new Font("Segoe UI", Font.BOLD, 16));
        lblMensaje.setForeground(TEXT_PRIMARY);
        lblMensaje.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        JLabel lblDescripcion = new JLabel("Sube imágenes al navegador de archivos primero");
        lblDescripcion.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        lblDescripcion.setForeground(TEXT_SECONDARY);
        lblDescripcion.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        panelMensaje.add(Box.createVerticalGlue());
        panelMensaje.add(lblIcono);
        panelMensaje.add(Box.createVerticalStrut(15));
        panelMensaje.add(lblMensaje);
        panelMensaje.add(Box.createVerticalStrut(5));
        panelMensaje.add(lblDescripcion);
        panelMensaje.add(Box.createVerticalGlue());
        
        panelImagenes.add(panelMensaje, BorderLayout.CENTER);
    }
    
    private boolean esImagen(String nombreArchivo) {
        String nombre = nombreArchivo.toLowerCase();
        return nombre.endsWith(".jpg") || nombre.endsWith(".jpeg") ||
               nombre.endsWith(".png") || nombre.endsWith(".gif") ||
               nombre.endsWith(".bmp") || nombre.endsWith(".webp");
    }
    
    private String obtenerUsuarioActual() {
        try {
            MiniWindowsClass sistema = MiniWindowsClass.getInstance();
            if (sistema.getUsuarioActual() != null) {
                return sistema.getUsuarioActual().getUsername();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "admin";
    }
    
    private void configurarVentana() {
        setSize(700, 600);
        setMinimumSize(new Dimension(600, 500));
        setLocationRelativeTo(getParent());
        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
    }
    
    public File getArchivoSeleccionado() {
        return archivoSeleccionado;
    }
}
