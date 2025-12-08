package Instagram.GUI;

import Instagram.Logica.GestorINSTACompleto;
import Instagram.Modelo.Publicacion;
import java.awt.*;
import javax.swing.*;
import javax.swing.border.*;
import java.util.ArrayList;

/**
 *
 * @author najma
 */
public class PanelTimeline extends JPanel {

    private GestorINSTACompleto gestorINSTA;
    private VentanaINSTA ventanaPrincipal;

    private JPanel panelPublicaciones;
    private JScrollPane scrollPane;

    private static final Color BACKGROUND_COLOR = new Color(255, 240, 245);
    private static final Color CARD_COLOR = Color.WHITE;
    private static final Color BORDER_COLOR = new Color(255, 192, 203);
    private static final Color INSTAGRAM_PINK = new Color(242, 80, 129);

    public PanelTimeline(GestorINSTACompleto gestor, VentanaINSTA ventana) {
        this.gestorINSTA = gestor;
        this.ventanaPrincipal = ventana;

        initComponents();
    }

    private void initComponents() {
        setLayout(new BorderLayout());
        setBackground(BACKGROUND_COLOR);

        panelPublicaciones = new JPanel();
        panelPublicaciones.setLayout(new BoxLayout(panelPublicaciones, BoxLayout.Y_AXIS));
        panelPublicaciones.setBackground(BACKGROUND_COLOR);
        panelPublicaciones.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));

        scrollPane = new JScrollPane(panelPublicaciones);
        scrollPane.setBorder(null);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

        add(scrollPane, BorderLayout.CENTER);
    }

    public void actualizarContenido() {
        panelPublicaciones.removeAll();
        ArrayList<Publicacion> publicaciones = gestorINSTA.obtenerTimeline();
        if (publicaciones == null || publicaciones.isEmpty()) {
            mostrarMensajeVacio();
        } else {
            for (Publicacion publicacion : publicaciones) {
                TarjetaPublicacion tarjeta = new TarjetaPublicacion(publicacion, gestorINSTA, ventanaPrincipal);
                tarjeta.setAlignmentX(Component.CENTER_ALIGNMENT);

                tarjeta.setMaximumSize(new Dimension(470, tarjeta.getPreferredSize().height));

                panelPublicaciones.add(tarjeta);
                panelPublicaciones.add(Box.createVerticalStrut(15));
            }
        }
        panelPublicaciones.revalidate();
        panelPublicaciones.repaint();

        SwingUtilities.invokeLater(() -> {
            JScrollBar bar = scrollPane.getVerticalScrollBar();
            if (bar != null) {
                bar.setValue(0);
            }
        });
    }

    private void mostrarMensajeVacio() {
        JPanel panelVacio = new JPanel();
        panelVacio.setLayout(new BoxLayout(panelVacio, BoxLayout.Y_AXIS));
        panelVacio.setBackground(CARD_COLOR);
        panelVacio.setBorder(new CompoundBorder(
                new LineBorder(BORDER_COLOR, 2),
                BorderFactory.createEmptyBorder(60, 40, 60, 40)
        ));
        panelVacio.setMaximumSize(new Dimension(470, 300));

        JLabel lblIcono = new JLabel();
        try {
            ImageIcon icono = new ImageIcon(getClass().getResource("/Instagram/icons/icon_insta.png"));
            Image img = icono.getImage().getScaledInstance(80, 80, Image.SCALE_SMOOTH);
            lblIcono.setIcon(new ImageIcon(img));
        } catch (Exception e) {
            lblIcono.setText("📷");
            lblIcono.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 60));
        }
        lblIcono.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel lblTitulo = new JLabel("¡Bienvenido a Instagram!");
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 20));
        lblTitulo.setForeground(INSTAGRAM_PINK);
        lblTitulo.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel lblMensaje = new JLabel("Sigue a otros usuarios para ver sus publicaciones aquí");
        lblMensaje.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        lblMensaje.setForeground(new Color(142, 142, 142));
        lblMensaje.setAlignmentX(Component.CENTER_ALIGNMENT);

        panelVacio.add(lblIcono);
        panelVacio.add(Box.createVerticalStrut(20));
        panelVacio.add(lblTitulo);
        panelVacio.add(Box.createVerticalStrut(10));
        panelVacio.add(lblMensaje);

        panelPublicaciones.add(Box.createVerticalGlue());
        panelPublicaciones.add(panelVacio);
        panelPublicaciones.add(Box.createVerticalGlue());
    }
}
