/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Instagram.GUI;

import Instagram.Modelo.Usuario;
import Instagram.Logica.GestorUsuariosLocalINSTA;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.*;

/**
 *
 * @author najma
 */
public class DialogEditarPerfil extends JDialog {
   
    private Usuario usuario;
    private GestorUsuariosLocalINSTA gestorUsuarios;
    private boolean perfilActualizado = false;
    
    private JTextField txtNombreCompleto;
    private JTextArea txtBiografia;
    private JTextField txtEmail;
    
    private static final Color BACKGROUND_COLOR = new Color(255, 240, 245);
    private static final Color CARD_COLOR = Color.WHITE;
    private static final Color BORDER_COLOR = new Color(255, 192, 203);
    private static final Color TEXT_PRIMARY = new Color(38, 38, 38);
    private static final Color TEXT_SECONDARY = new Color(142, 142, 142);
    private static final Color INSTAGRAM_PINK = new Color(242, 80, 129);
    
    public DialogEditarPerfil(Frame parent, Usuario usuario, GestorUsuariosLocalINSTA gestorUsuarios) {
        super(parent, "Editar Perfil", true);
        this.usuario = usuario;
        this.gestorUsuarios = gestorUsuarios;
        
        initComponents();
        cargarDatos();
        configurarVentana();
    }
    
    private void initComponents() {
        setLayout(new BorderLayout());
        getContentPane().setBackground(BACKGROUND_COLOR);
        
        JPanel panelPrincipal = new JPanel();
        panelPrincipal.setLayout(new BoxLayout(panelPrincipal, BoxLayout.Y_AXIS));
        panelPrincipal.setBackground(CARD_COLOR);
        panelPrincipal.setBorder(BorderFactory.createEmptyBorder(24, 24, 24, 24));
        
        JLabel lblTitulo = new JLabel("Editar tu perfil");
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 22));
        lblTitulo.setForeground(INSTAGRAM_PINK);
        lblTitulo.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        JLabel lblDescripcion = new JLabel("@" + usuario.getUsername());
        lblDescripcion.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        lblDescripcion.setForeground(TEXT_SECONDARY);
        lblDescripcion.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        panelPrincipal.add(lblTitulo);
        panelPrincipal.add(Box.createVerticalStrut(4));
        panelPrincipal.add(lblDescripcion);
        panelPrincipal.add(Box.createVerticalStrut(24));
        
        JLabel lblNombreLabel = new JLabel("Nombre completo");
        lblNombreLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lblNombreLabel.setForeground(TEXT_PRIMARY);
        lblNombreLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        panelPrincipal.add(lblNombreLabel);
        panelPrincipal.add(Box.createVerticalStrut(6));
        
        txtNombreCompleto = new JTextField();
        txtNombreCompleto.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        txtNombreCompleto.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(BORDER_COLOR, 2),
            BorderFactory.createEmptyBorder(8, 10, 8, 10)
        ));
        txtNombreCompleto.setAlignmentX(Component.LEFT_ALIGNMENT);
        txtNombreCompleto.setMaximumSize(new Dimension(400, 40));
        panelPrincipal.add(txtNombreCompleto);
        panelPrincipal.add(Box.createVerticalStrut(16));
        
        JLabel lblEmailLabel = new JLabel("Correo electrónico");
        lblEmailLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lblEmailLabel.setForeground(TEXT_PRIMARY);
        lblEmailLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        panelPrincipal.add(lblEmailLabel);
        panelPrincipal.add(Box.createVerticalStrut(6));
        
        txtEmail = new JTextField();
        txtEmail.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        txtEmail.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(BORDER_COLOR, 2),
            BorderFactory.createEmptyBorder(8, 10, 8, 10)
        ));
        txtEmail.setAlignmentX(Component.LEFT_ALIGNMENT);
        txtEmail.setMaximumSize(new Dimension(400, 40));
        panelPrincipal.add(txtEmail);
        panelPrincipal.add(Box.createVerticalStrut(16));
        
        JLabel lblBiografiaLabel = new JLabel("Biografía");
        lblBiografiaLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lblBiografiaLabel.setForeground(TEXT_PRIMARY);
        lblBiografiaLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        panelPrincipal.add(lblBiografiaLabel);
        panelPrincipal.add(Box.createVerticalStrut(6));
        
        txtBiografia = new JTextArea(4, 30);
        txtBiografia.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        txtBiografia.setLineWrap(true);
        txtBiografia.setWrapStyleWord(true);
        txtBiografia.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(BORDER_COLOR, 2),
            BorderFactory.createEmptyBorder(8, 10, 8, 10)
        ));
        
        JScrollPane scrollBiografia = new JScrollPane(txtBiografia);
        scrollBiografia.setBorder(null);
        scrollBiografia.setAlignmentX(Component.LEFT_ALIGNMENT);
        scrollBiografia.setMaximumSize(new Dimension(400, 100));
        panelPrincipal.add(scrollBiografia);
        panelPrincipal.add(Box.createVerticalStrut(24));
        
        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.RIGHT, 12, 0));
        panelBotones.setBackground(CARD_COLOR);
        panelBotones.setAlignmentX(Component.LEFT_ALIGNMENT);
        panelBotones.setMaximumSize(new Dimension(400, 40));
        
        JButton btnCancelar = new JButton("Cancelar");
        btnCancelar.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        btnCancelar.setForeground(TEXT_PRIMARY);
        btnCancelar.setBackground(CARD_COLOR);
        btnCancelar.setBorder(new LineBorder(BORDER_COLOR, 2));
        btnCancelar.setFocusPainted(false);
        btnCancelar.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnCancelar.setPreferredSize(new Dimension(100, 36));
        btnCancelar.addActionListener(e -> dispose());
        
        JButton btnGuardar = new JButton("Guardar");
        btnGuardar.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnGuardar.setForeground(Color.WHITE);
        btnGuardar.setBackground(INSTAGRAM_PINK);
        btnGuardar.setBorder(null);
        btnGuardar.setFocusPainted(false);
        btnGuardar.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnGuardar.setPreferredSize(new Dimension(100, 36));
        btnGuardar.addActionListener(e -> guardarCambios());
        
        panelBotones.add(btnCancelar);
        panelBotones.add(btnGuardar);
        
        panelPrincipal.add(panelBotones);
        
        add(panelPrincipal, BorderLayout.CENTER);
    }
    
    private void cargarDatos() {
        txtNombreCompleto.setText(usuario.getNombreCompleto());
        txtEmail.setText(usuario.getEmail());
        
        String biografia = usuario.getBiografia();
        if (biografia != null && !biografia.isEmpty()) {
            txtBiografia.setText(biografia);
        }
    }
    
    private void guardarCambios() {
        String nuevoNombre = txtNombreCompleto.getText().trim();
        String nuevoEmail = txtEmail.getText().trim();
        String nuevaBiografia = txtBiografia.getText().trim();
        
        if (nuevoNombre.isEmpty()) {
            JOptionPane.showMessageDialog(
                this,
                "El nombre completo no puede estar vacío",
                "Error",
                JOptionPane.ERROR_MESSAGE
            );
            return;
        }
        
        if (nuevoEmail.isEmpty()) {
            JOptionPane.showMessageDialog(
                this,
                "El correo electrónico no puede estar vacío",
                "Error",
                JOptionPane.ERROR_MESSAGE
            );
            return;
        }
        
        if (!nuevoEmail.contains("@") || !nuevoEmail.contains(".")) {
            JOptionPane.showMessageDialog(
                this,
                "El correo electrónico no es válido",
                "Error",
                JOptionPane.ERROR_MESSAGE
            );
            return;
        }
        
        usuario.setNombreCompleto(nuevoNombre);
        usuario.setEmail(nuevoEmail);
        usuario.setBiografia(nuevaBiografia);
        
        gestorUsuarios.actualizarUsuario(usuario);
        
        perfilActualizado = true;
        
        JOptionPane.showMessageDialog(
            this,
            "Perfil actualizado exitosamente",
            "Éxito",
            JOptionPane.INFORMATION_MESSAGE
        );
        
        dispose();
    }
    
    private void configurarVentana() {
        setSize(480, 520);
        setMinimumSize(new Dimension(480, 520));
        setLocationRelativeTo(getParent());
        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        setResizable(false);
    }
    
    public boolean isPerfilActualizado() {
        return perfilActualizado;
    }
}
