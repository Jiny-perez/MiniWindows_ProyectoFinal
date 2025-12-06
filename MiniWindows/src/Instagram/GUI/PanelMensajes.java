/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Instagram.GUI;

import Instagram.Logica.GestorINSTA;
import java.awt.*;
import javax.swing.*;
import javax.swing.border.*;

/**
 *
 * @author najma
 */
public class PanelMensajes extends JPanel {
    
    private GestorINSTA gestorINSTA;
    
    private static final Color BACKGROUND_COLOR = new Color(255, 240, 245);
    private static final Color CARD_COLOR = Color.WHITE;
    private static final Color BORDER_COLOR = new Color(255, 192, 203);
    private static final Color TEXT_PRIMARY = new Color(38, 38, 38);
    private static final Color TEXT_SECONDARY = new Color(142, 142, 142);
    private static final Color INSTAGRAM_PINK = new Color(242, 80, 129);
    
    public PanelMensajes(GestorINSTA gestor) {
        this.gestorINSTA = gestor;
        
        initComponents();
    }
    
    private void initComponents() {
        setLayout(new BorderLayout());
        setBackground(BACKGROUND_COLOR);
        
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(CARD_COLOR);
        header.setBorder(new CompoundBorder(
            new MatteBorder(0, 0, 1, 0, BORDER_COLOR),
            BorderFactory.createEmptyBorder(20, 20, 20, 20)
        ));
        
        JLabel lblTitulo = new JLabel("Mensajes");
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 24));
        lblTitulo.setForeground(INSTAGRAM_PINK);
        
        header.add(lblTitulo, BorderLayout.WEST);
        add(header, BorderLayout.NORTH);
        
        JPanel panelCentro = new JPanel();
        panelCentro.setLayout(new BoxLayout(panelCentro, BoxLayout.Y_AXIS));
        panelCentro.setBackground(BACKGROUND_COLOR);
                
        JLabel lblIcono = new JLabel();
        try {
            ImageIcon icono = new ImageIcon(getClass().getResource("/Instagram/icons/icon_mensajeria.png"));
            Image img = icono.getImage().getScaledInstance(80, 80, Image.SCALE_SMOOTH);
            lblIcono.setIcon(new ImageIcon(img));
        } catch (Exception e) {
            lblIcono.setText("💬");
            lblIcono.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 60));
            lblIcono.setForeground(INSTAGRAM_PINK);
        }
        lblIcono.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        JLabel lblMensaje1 = new JLabel("Mensajería directa");
        lblMensaje1.setFont(new Font("Segoe UI", Font.BOLD, 20));
        lblMensaje1.setForeground(INSTAGRAM_PINK);
        lblMensaje1.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        JLabel lblMensaje2 = new JLabel("Erm... en proceso");
        lblMensaje2.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        lblMensaje2.setForeground(TEXT_SECONDARY);
        lblMensaje2.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        panelCentro.add(Box.createVerticalGlue());
        panelCentro.add(lblIcono);
        panelCentro.add(Box.createVerticalStrut(20));
        panelCentro.add(lblMensaje1);
        panelCentro.add(Box.createVerticalStrut(10));
        panelCentro.add(lblMensaje2);
        panelCentro.add(Box.createVerticalGlue());
        
        add(panelCentro, BorderLayout.CENTER);
    }
}
