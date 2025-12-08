package VisorImagenes;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;

/**
 *
 * @author najma
 */
public class GUIVisorImagenes extends JFrame {
   
    private final VisorImagenes visor;

    private JLabel lblImagenPrincipal;
    private JScrollPane scrollPaneImagen;
    private JPanel panelMiniaturas;
    private JScrollPane scrollMiniaturas;
    private JLabel lblNombreArchivo;

    private JButton btnAnterior;
    private JButton btnSiguiente;

    private boolean ajusteAutomatico = true;
    
    private static final Color ROSA_PRINCIPAL = new Color(242, 80, 129);
    private static final Color ROSA_HOVER = new Color(220, 60, 110);
    private static final Color ROSA_CLARO = new Color(255, 240, 245);
    private static final Color FONDO_OSCURO = new Color(40, 40, 40);
    private static final Color FONDO_PANEL = new Color(50, 50, 50);
    private static final Color BORDE = new Color(80, 80, 80);

    public GUIVisorImagenes(File archivoInicial) {
        super("Visor de Imágenes");
        this.visor = new VisorImagenes(archivoInicial);

        inicializarComponentes();
        configurarVentana();

        if (visor.getTotalImagenes() > 0) {
            visor.cargarImagen(visor.getIndiceActual());
            actualizarVista();
            SwingUtilities.invokeLater(this::ajustarPantalla);
        }
    }

    private void inicializarComponentes() {
        setLayout(new BorderLayout());
        getContentPane().setBackground(FONDO_OSCURO);

        JPanel panelSuperior = crearPanelSuperior();
        add(panelSuperior, BorderLayout.NORTH);

        JPanel panelCentral = crearPanelCentral();
        add(panelCentral, BorderLayout.CENTER);

        JPanel panelInferior = crearPanelInferior();
        add(panelInferior, BorderLayout.SOUTH);
    }

    private JPanel crearPanelSuperior() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(FONDO_PANEL);
        panel.setBorder(BorderFactory.createMatteBorder(0, 0, 2, 0, ROSA_PRINCIPAL));
        panel.setPreferredSize(new Dimension(0, 60));

        lblNombreArchivo = new JLabel("Subprograma", SwingConstants.CENTER);
        lblNombreArchivo.setFont(new Font("Segoe UI", Font.BOLD, 18));
        lblNombreArchivo.setForeground(Color.WHITE);
        lblNombreArchivo.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));

        panel.add(lblNombreArchivo, BorderLayout.CENTER);

        return panel;
    }

    private JPanel crearPanelCentral() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(FONDO_OSCURO);
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        lblImagenPrincipal = new JLabel();
        lblImagenPrincipal.setHorizontalAlignment(SwingConstants.CENTER);
        lblImagenPrincipal.setVerticalAlignment(SwingConstants.CENTER);
        lblImagenPrincipal.setBackground(FONDO_OSCURO);
        lblImagenPrincipal.setOpaque(true);

        scrollPaneImagen = new JScrollPane(lblImagenPrincipal);
        scrollPaneImagen.setBackground(FONDO_OSCURO);
        scrollPaneImagen.getViewport().setBackground(FONDO_OSCURO);
        scrollPaneImagen.setBorder(BorderFactory.createLineBorder(BORDE, 1));

        panel.add(scrollPaneImagen, BorderLayout.CENTER);

        return panel;
    }

    private JPanel crearPanelInferior() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(FONDO_PANEL);
        panel.setBorder(BorderFactory.createMatteBorder(2, 0, 0, 0, BORDE));
        panel.setPreferredSize(new Dimension(0, 160));

        JLabel lblEstado = new JLabel("Subprograma iniciado.", SwingConstants.CENTER);
        lblEstado.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lblEstado.setForeground(new Color(180, 180, 180));
        lblEstado.setBorder(BorderFactory.createEmptyBorder(8, 0, 8, 0));
        panel.add(lblEstado, BorderLayout.NORTH);

        JPanel contenedorMiniaturas = new JPanel(new BorderLayout());
        contenedorMiniaturas.setBackground(FONDO_PANEL);

        panelMiniaturas = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        panelMiniaturas.setBackground(FONDO_PANEL);

        scrollMiniaturas = new JScrollPane(panelMiniaturas);
        scrollMiniaturas.setBackground(FONDO_PANEL);
        scrollMiniaturas.getViewport().setBackground(FONDO_PANEL);
        scrollMiniaturas.setBorder(BorderFactory.createEmptyBorder());
        scrollMiniaturas.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scrollMiniaturas.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_NEVER);
        scrollMiniaturas.setPreferredSize(new Dimension(0, 120));

        contenedorMiniaturas.add(scrollMiniaturas, BorderLayout.CENTER);

        JPanel panelNavegacion = new JPanel(new BorderLayout());
        panelNavegacion.setBackground(FONDO_PANEL);
        panelNavegacion.setPreferredSize(new Dimension(100, 0));

        btnAnterior = crearBotonFlecha("◄");
        btnAnterior.addActionListener(e -> imagenAnterior());

        btnSiguiente = crearBotonFlecha("►");
        btnSiguiente.addActionListener(e -> imagenSiguiente());

        JPanel panelIzq = new JPanel(new BorderLayout());
        panelIzq.setBackground(FONDO_PANEL);
        panelIzq.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 0));
        panelIzq.add(btnAnterior, BorderLayout.CENTER);

        JPanel panelDer = new JPanel(new BorderLayout());
        panelDer.setBackground(FONDO_PANEL);
        panelDer.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 10));
        panelDer.add(btnSiguiente, BorderLayout.CENTER);

        panel.add(panelIzq, BorderLayout.WEST);
        panel.add(contenedorMiniaturas, BorderLayout.CENTER);
        panel.add(panelDer, BorderLayout.EAST);

        return panel;
    }

    private JButton crearBotonFlecha(String texto) {
        JButton btn = new JButton(texto);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 24));
        btn.setForeground(Color.WHITE);
        btn.setBackground(ROSA_PRINCIPAL);
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setContentAreaFilled(true);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setPreferredSize(new Dimension(60, 100));

        btn.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                if (btn.isEnabled()) btn.setBackground(ROSA_HOVER);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                btn.setBackground(ROSA_PRINCIPAL);
            }
        });

        return btn;
    }

    private void actualizarMiniaturas() {
        panelMiniaturas.removeAll();

        int total = visor.getTotalImagenes();
        int actual = visor.getIndiceActual();

        for (int i = 0; i < total; i++) {
            final int indice = i;
            
            JPanel tarjetaMiniatura = new JPanel(new BorderLayout());
            tarjetaMiniatura.setPreferredSize(new Dimension(80, 80));
            tarjetaMiniatura.setBackground(FONDO_OSCURO);
            tarjetaMiniatura.setCursor(new Cursor(Cursor.HAND_CURSOR));

            if (i == actual) {
                tarjetaMiniatura.setBorder(BorderFactory.createLineBorder(ROSA_PRINCIPAL, 3));
            } else {
                tarjetaMiniatura.setBorder(BorderFactory.createLineBorder(BORDE, 1));
            }

            JLabel lblMiniatura = new JLabel();
            lblMiniatura.setHorizontalAlignment(SwingConstants.CENTER);
            lblMiniatura.setVerticalAlignment(SwingConstants.CENTER);
            lblMiniatura.setOpaque(true);
            lblMiniatura.setBackground(FONDO_OSCURO);

            SwingUtilities.invokeLater(() -> {
                try {
                    File archivo = obtenerArchivoPorIndice(indice);
                    if (archivo != null && archivo.exists()) {
                        BufferedImage imgOriginal = javax.imageio.ImageIO.read(archivo);
                        if (imgOriginal != null) {
                            Image imgEscalada = imgOriginal.getScaledInstance(74, 74, Image.SCALE_SMOOTH);
                            lblMiniatura.setIcon(new ImageIcon(imgEscalada));
                        }
                    }
                } catch (Exception ex) {
                    lblMiniatura.setText("?");
                    lblMiniatura.setForeground(Color.GRAY);
                }
            });

            tarjetaMiniatura.add(lblMiniatura, BorderLayout.CENTER);

            // Click para cambiar imagen
            tarjetaMiniatura.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    visor.cargarImagen(indice);
                    actualizarVista();
                    if (ajusteAutomatico) {
                        SwingUtilities.invokeLater(() -> ajustarPantalla());
                    }
                }

                @Override
                public void mouseEntered(MouseEvent e) {
                    if (indice != visor.getIndiceActual()) {
                        tarjetaMiniatura.setBorder(BorderFactory.createLineBorder(ROSA_CLARO, 2));
                    }
                }

                @Override
                public void mouseExited(MouseEvent e) {
                    if (indice != visor.getIndiceActual()) {
                        tarjetaMiniatura.setBorder(BorderFactory.createLineBorder(BORDE, 1));
                    }
                }
            });

            panelMiniaturas.add(tarjetaMiniatura);
        }

        panelMiniaturas.revalidate();
        panelMiniaturas.repaint();

        SwingUtilities.invokeLater(() -> {
            if (actual >= 0 && actual < panelMiniaturas.getComponentCount()) {
                Component comp = panelMiniaturas.getComponent(actual);
                if (comp instanceof JComponent) {
                    ((JComponent) comp).scrollRectToVisible(new Rectangle(comp.getPreferredSize()));
                }
            }
        });
    }
    
    private File obtenerArchivoPorIndice(int indice) {
        File archivoActual = visor.getArchivoActual();
        if (archivoActual == null) {
            return null;
        }
        
        File carpeta = archivoActual.getParentFile();
        if (carpeta == null || !carpeta.exists() || !carpeta.isDirectory()) {
            return null;
        }
        
        java.util.ArrayList<File> imagenes = new java.util.ArrayList<>();
        File[] archivos = carpeta.listFiles();
        if (archivos != null) {
            for (File f : archivos) {
                if (f != null && f.isFile() && esImagen(f)) {
                    imagenes.add(f);
                }
            }
        }
        
        if (indice >= 0 && indice < imagenes.size()) {
            return imagenes.get(indice);
        }
        
        return null;
    }
    
    private boolean esImagen(File archivo) {
        if (archivo == null || !archivo.isFile()) {
            return false;
        }
        String nombre = archivo.getName().toLowerCase();
        return nombre.endsWith(".jpg") || nombre.endsWith(".jpeg")
                || nombre.endsWith(".png") || nombre.endsWith(".gif")
                || nombre.endsWith(".bmp") || nombre.endsWith(".webp");
    }

    private void configurarVentana() {
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(900, 700);
        setLocationRelativeTo(null);

        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                if (ajusteAutomatico) {
                    SwingUtilities.invokeLater(() -> ajustarPantalla());
                }
            }
        });

        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                switch (e.getKeyCode()) {
                    case KeyEvent.VK_LEFT:
                        imagenAnterior();
                        break;
                    case KeyEvent.VK_RIGHT:
                        imagenSiguiente();
                        break;
                    case KeyEvent.VK_PLUS:
                    case KeyEvent.VK_ADD:
                        ajusteAutomatico = false;
                        visor.zoomIn();
                        actualizarImagen();
                        break;
                    case KeyEvent.VK_MINUS:
                    case KeyEvent.VK_SUBTRACT:
                        ajusteAutomatico = false;
                        visor.zoomOut();
                        actualizarImagen();
                        break;
                    case KeyEvent.VK_0:
                        ajusteAutomatico = false;
                        visor.tamañoReal();
                        actualizarImagen();
                        break;
                    case KeyEvent.VK_F:
                        ajusteAutomatico = true;
                        ajustarPantalla();
                        break;
                }
            }
        });

        setFocusable(true);
    }

    private void imagenAnterior() {
        if (visor.imagenAnterior()) {
            actualizarVista();
            if (ajusteAutomatico) {
                SwingUtilities.invokeLater(() -> ajustarPantalla());
            }
        }
    }

    private void imagenSiguiente() {
        if (visor.imagenSiguiente()) {
            actualizarVista();
            if (ajusteAutomatico) {
                SwingUtilities.invokeLater(() -> ajustarPantalla());
            }
        }
    }

    private void ajustarPantalla() {
        int anchoVentana = Math.max(1, scrollPaneImagen.getViewport().getWidth() - 20);
        int altoVentana = Math.max(1, scrollPaneImagen.getViewport().getHeight() - 20);

        visor.ajustarPantalla(anchoVentana, altoVentana);
        actualizarImagen();
    }

    private void actualizarVista() {
        actualizarImagen();
        actualizarInformacion();
        actualizarMiniaturas();
        actualizarBotonesNavegacion();
    }

    private void actualizarImagen() {
        if (visor.getImagenTransformada() != null) {
            lblImagenPrincipal.setIcon(new ImageIcon(visor.getImagenTransformada()));
            lblImagenPrincipal.revalidate();
            lblImagenPrincipal.repaint();
        } else {
            lblImagenPrincipal.setIcon(null);
            lblImagenPrincipal.repaint();
        }
    }

    private void actualizarInformacion() {
        File archivo = visor.getArchivoActual();

        if (archivo != null) {
            lblNombreArchivo.setText(archivo.getName());
        } else {
            lblNombreArchivo.setText("Subprograma");
        }
    }

    private void actualizarBotonesNavegacion() {
        btnAnterior.setEnabled(visor.hayAnterior());
        btnSiguiente.setEnabled(visor.haySiguiente());
    }
}
