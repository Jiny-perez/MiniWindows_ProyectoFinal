package MiniWindows;

import CMD.GUICMD;
import EditorTexto.GUIEditor;
import GestorArchivos.MiniWindowsClass;
import GestorArchivos.Archivo;
import GestorArchivos.SistemaArchivos;
import GestorUsuario.GestorUsuarioGUI;
import GestorUsuario.GestorUsuarios;
import GestorUsuario.Usuario;
import ReproductorMusical.GUIReproductorMusica;
import VisorImagenes.GUIVisorImagenes;
import Instagram.GUI.VentanaLogin;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.util.ArrayList;

/**
 *
 * @author najma
 */
public class VentanaPrincipal extends JFrame {

    private Usuario usuarioActual;
    private MiniWindowsClass sistema;
    private JPanel panelEscritorio;

    public VentanaPrincipal(Usuario usuario, MiniWindowsClass sistema) {
        this.usuarioActual = usuario;
        this.sistema = sistema;
        inicializarComponentes();
    }

    private void inicializarComponentes() {
        setUndecorated(true);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        setLocationRelativeTo(null);
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                salir();
            }
        });

        panelEscritorio = new JPanel() {
            private Image imagenFondo;

            {
                try {
                    imagenFondo = new ImageIcon(getClass().getResource("/Background/BgPrincipal.png")).getImage();
                } catch (Exception e) {
                    imagenFondo = null;
                }
            }

            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                if (imagenFondo != null) {
                    g.drawImage(imagenFondo, 0, 0, getWidth(), getHeight(), this);
                } else {
                    g.setColor(new Color(0, 120, 215));
                    g.fillRect(0, 0, getWidth(), getHeight());
                }
            }
        };
        panelEscritorio.setLayout(new BorderLayout());

        JPanel barraTareas = crearBarraTareas();
        panelEscritorio.add(barraTareas, BorderLayout.SOUTH);

        add(panelEscritorio);
    }

    private JPanel crearBarraTareas() {
        JPanel barra = new JPanel();
        barra.setLayout(new BorderLayout());
        barra.setBackground(Color.WHITE);
        barra.setPreferredSize(new Dimension(0, 60));
        barra.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, new Color(200, 200, 200)));

        JPanel panelIzquierda = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 8));
        panelIzquierda.setOpaque(false);

        JButton btnInicio = crearBotonInicio();
        panelIzquierda.add(btnInicio);

        if (usuarioActual.esAdmin()) {
            panelIzquierda.add(crearSeparador());
            JButton btnGestorUsuarios = crearBotonIcono("/Icons/icon_gestor_de_usuarios.png", "Gestor de Usuarios", false);
            btnGestorUsuarios.addActionListener(e -> gestionarUsuarios());
            panelIzquierda.add(btnGestorUsuarios);
        }

        JPanel panelCentro = new JPanel(new FlowLayout(FlowLayout.CENTER, 12, 8));
        panelCentro.setOpaque(false);

        JButton btnNavegador = crearBotonIcono("/Icons/icon_navegador_de_archivos.png", "Navegador de Archivos", true);
        btnNavegador.addActionListener(e -> abrirNavegador());

        JButton btnEditor = crearBotonIcono("/Icons/icon_editor_de_texto.png", "Editor de Texto", true);
        btnEditor.addActionListener(e -> abrirEditor());

        JButton btnVisor = crearBotonIcono("/Icons/icon_visor_de_imagenes.png", "Visor de Imágenes", true);
        btnVisor.addActionListener(e -> abrirVisorImagenes());

        JButton btnCMD = crearBotonIcono("/Icons/icon_cmd.png", "CMD", true);
        btnCMD.addActionListener(e -> abrirCMD());

        JButton btnReproductor = crearBotonIcono("/Icons/icon_reproductor_de_musica.png", "Reproductor de Música", true);
        btnReproductor.addActionListener(e -> abrirReproductor());

        JButton btnInsta = crearBotonIcono("/Icons/icon_instagram.png", "Instagram", true);
        btnInsta.addActionListener(e -> abrirInstagram());

        panelCentro.add(btnNavegador);
        panelCentro.add(btnEditor);
        panelCentro.add(btnVisor);
        panelCentro.add(btnCMD);
        panelCentro.add(btnReproductor);
        panelCentro.add(btnInsta);

        JPanel panelDerecha = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 8));
        panelDerecha.setOpaque(false);

        panelDerecha.add(crearIconoSistema("/Icons/icon_actualizar.png", "Actualizar"));
        panelDerecha.add(crearIconoSistema("/Icons/icon_espaniol.png", "Idioma: Español"));
        panelDerecha.add(crearIconoSistema("/Icons/icon_wifi.png", "WiFi"));
        panelDerecha.add(crearIconoSistema("/Icons/icon_volumen.png", "Volumen"));
        panelDerecha.add(crearIconoSistema("/Icons/icon_bateria.png", "Batería"));

        JLabel lblFechaHora = crearReloj();
        panelDerecha.add(lblFechaHora);

        barra.add(panelIzquierda, BorderLayout.WEST);
        barra.add(panelCentro, BorderLayout.CENTER);
        barra.add(panelDerecha, BorderLayout.EAST);

        return barra;
    }

    private JButton crearBotonInicio() {
        JButton boton = new JButton();
        try {
            ImageIcon icono = new ImageIcon(getClass().getResource("/Background/BgLogIn.jpg"));
            Image img = icono.getImage().getScaledInstance(32, 32, Image.SCALE_SMOOTH);
            boton.setIcon(new ImageIcon(img));
        } catch (Exception e) {
            boton.setText("≡");
            boton.setFont(new Font("Segoe UI", Font.BOLD, 20));
        }

        boton.setPreferredSize(new Dimension(44, 44));
        boton.setBackground(new Color(0, 120, 215));
        boton.setBorder(BorderFactory.createEmptyBorder(2, 2, 2, 2));
        boton.setFocusPainted(false);
        boton.setToolTipText("Inicio");
        boton.setCursor(new Cursor(Cursor.HAND_CURSOR));

        boton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                boton.setBackground(new Color(0, 100, 180));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                boton.setBackground(new Color(0, 120, 215));
            }
        });

        boton.addActionListener(e -> mostrarMenuInicio(boton));

        return boton;
    }

    private void mostrarMenuInicio(JButton boton) {
        JPopupMenu menu = new JPopupMenu();
        menu.setBackground(Color.WHITE);
        menu.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200), 1));

        JMenuItem itemCerrarSesion = new JMenuItem("Cerrar Sesión");
        itemCerrarSesion.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        itemCerrarSesion.addActionListener(e -> cerrarSesion());

        JMenuItem itemSalir = new JMenuItem("Apagar");
        itemSalir.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        itemSalir.addActionListener(e -> salir());

        menu.add(itemCerrarSesion);
        menu.addSeparator();
        menu.add(itemSalir);

        menu.show(boton, 0, -menu.getPreferredSize().height);
    }

    private JLabel crearReloj() {
        JLabel lblReloj = new JLabel();
        lblReloj.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        lblReloj.setForeground(new Color(50, 50, 50));

        Timer timer = new Timer(1000, e -> {
            java.text.SimpleDateFormat formatoHora = new java.text.SimpleDateFormat("HH:mm:ss");
            java.text.SimpleDateFormat formatoFecha = new java.text.SimpleDateFormat("dd/MM/yyyy");
            java.util.Date ahora = new java.util.Date();
            lblReloj.setText("<html><center>" + formatoHora.format(ahora) + "<br>" + formatoFecha.format(ahora) + "</center></html>");
        });
        timer.start();
        timer.setInitialDelay(0);

        return lblReloj;
    }

    private JButton crearIconoSistema(String rutaIcono, String tooltip) {
        JButton boton = new JButton();
        try {
            ImageIcon icono = new ImageIcon(getClass().getResource(rutaIcono));
            Image img = icono.getImage().getScaledInstance(20, 20, Image.SCALE_SMOOTH);
            boton.setIcon(new ImageIcon(img));
        } catch (Exception e) {
            boton.setText("•");
        }

        boton.setPreferredSize(new Dimension(24, 24));
        boton.setBackground(Color.WHITE);
        boton.setBorder(BorderFactory.createEmptyBorder(2, 2, 2, 2));
        boton.setFocusPainted(false);
        boton.setContentAreaFilled(false);
        boton.setToolTipText(tooltip);
        boton.setCursor(new Cursor(Cursor.HAND_CURSOR));

        boton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                boton.setContentAreaFilled(true);
                boton.setBackground(new Color(230, 230, 230));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                boton.setContentAreaFilled(false);
            }
        });

        return boton;
    }

    private JPanel crearSeparador() {
        JPanel separador = new JPanel();
        separador.setPreferredSize(new Dimension(1, 30));
        separador.setBackground(new Color(220, 220, 220));
        return separador;
    }

    private JButton crearBotonIcono(String rutaIcono, String tooltip, boolean conIndicador) {
        JButton boton = new JButton();

        try {
            ImageIcon icono = new ImageIcon(getClass().getResource(rutaIcono));
            Image img = icono.getImage().getScaledInstance(40, 40, Image.SCALE_SMOOTH);
            boton.setIcon(new ImageIcon(img));
        } catch (Exception e) {
            boton.setText("?");
        }

        boton.setPreferredSize(new Dimension(50, 50));
        boton.setBackground(Color.WHITE);
        boton.setBorder(BorderFactory.createEmptyBorder(4, 4, 4, 4));
        boton.setFocusPainted(false);
        boton.setContentAreaFilled(false);
        boton.setToolTipText(tooltip);
        boton.setCursor(new Cursor(Cursor.HAND_CURSOR));

        boton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                boton.setContentAreaFilled(true);
                boton.setBackground(new Color(240, 240, 240));
                boton.setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(new Color(200, 200, 200), 1),
                        BorderFactory.createEmptyBorder(3, 3, 3, 3)
                ));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                boton.setContentAreaFilled(false);
                boton.setBorder(BorderFactory.createEmptyBorder(4, 4, 4, 4));
            }
        });

        return boton;
    }

    private void abrirEditor() {
        try {
            new GUIEditor(sistema);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                    "Error al abrir el editor de texto: " + e.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private void abrirCMD() {
        try {
            SistemaArchivos sys = sistema.getSistemaArchivos();
            new GUICMD(usuarioActual, sys);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                    "Error al abrir la consola CMD: " + e.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private void abrirReproductor() {
        try {
            new GUIReproductorMusica();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                    "Error al abrir el reproductor de música: " + e.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private void abrirInstagram() {
        try {
            // Abrir ventana de LOGIN de Instagram (el usuario debe hacer login en Instagram)
            SwingUtilities.invokeLater(() -> {
                try {
                    VentanaLogin loginInstagram = new VentanaLogin();
                    loginInstagram.setVisible(true);
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(this,
                            "Error al iniciar Instagram: " + ex.getMessage(),
                            "Error",
                            JOptionPane.ERROR_MESSAGE);
                    ex.printStackTrace();
                }
            });
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                    "Error al abrir Instagram: " + e.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

    private void abrirNavegador() {
        NavegadorArchivos navegador = new NavegadorArchivos(usuarioActual, sistema);
        navegador.setVisible(true);
    }

    private Archivo obtenerCarpetaMisImagenes() {
        SistemaArchivos sa = sistema.getSistemaArchivos();
        if (sa == null) {
            return null;
        }
        Archivo carpeta = sa.obtenerArchivoEnRuta("Mis Imagenes", usuarioActual.getUsername());
        if (carpeta != null && carpeta.isEsCarpeta()) {
            return carpeta;
        }
        try {
            try {
                sa.crearCarpetaUsuario(usuarioActual.getUsername());
            } catch (Exception ignore) {
            }
            try {
                sa.crearCarpetaEnRuta("Mis Imagenes", usuarioActual.getUsername());
            } catch (Exception ignore) {
            }
            carpeta = sa.obtenerArchivoEnRuta("Mis Imagenes", usuarioActual.getUsername());
            if (carpeta != null && carpeta.isEsCarpeta()) {
                return carpeta;
            }
        } catch (Exception e) {
        }
        return null;
    }

    private void abrirVisorImagenes() {
        SistemaArchivos sa = sistema.getSistemaArchivos();
        Archivo carpeta = obtenerCarpetaMisImagenes();
        if (carpeta == null) {
            JOptionPane.showMessageDialog(this, "No se encontró ni se pudo crear 'Mis Imagenes'.", "Aviso", JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        
        ArrayList<Archivo> imgs = sa.listarContenidoEnRuta(carpeta.getRutaRelativa());
        if (imgs == null || imgs.isEmpty()) {
            JOptionPane.showMessageDialog(this, "La carpeta 'Mis Imagenes' está vacía.", "Aviso", JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        
        for (Archivo a : imgs) {
            if (!a.isEsCarpeta()) {
                String n = a.getNombre().toLowerCase();
                if (n.endsWith(".jpg") || n.endsWith(".jpeg") || n.endsWith(".png")
                        || n.endsWith(".gif") || n.endsWith(".bmp") || n.endsWith(".webp")) {
                    File f = new File(a.getRutaAbsoluta());
                    if (f.exists()) {
                        GUIVisorImagenes visor = new GUIVisorImagenes(f);
                        visor.setVisible(true);
                        return;
                    }
                }
            }
        }
        JOptionPane.showMessageDialog(this, "No se encontraron imágenes soportadas en 'Mis Imagenes'.", "Aviso", JOptionPane.INFORMATION_MESSAGE);
    }

    private void gestionarUsuarios() {
        try {
            GestorUsuarios gu = sistema.getGestorUsuarios();
            new GestorUsuarioGUI(this, gu);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "No se pudo abrir gestor de usuarios: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void cerrarSesion() {
        int confirmacion = JOptionPane.showConfirmDialog(this,
                "¿Está seguro que desea cerrar sesión?",
                "Confirmar Cierre de Sesión",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE);
        if (confirmacion == JOptionPane.YES_OPTION) {
            sistema.logout();
            dispose();
            SwingUtilities.invokeLater(() -> {
                PantallaLogin login = new PantallaLogin();
                login.setVisible(true);
            });
        }
    }

    private void salir() {
        int confirmacion = JOptionPane.showConfirmDialog(this,
                "¿Está seguro que desea salir de Mini-Windows?",
                "Confirmar Salida",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE);
        if (confirmacion == JOptionPane.YES_OPTION) {
            sistema.guardarSistema();
            System.exit(0);
        }
    }
}
