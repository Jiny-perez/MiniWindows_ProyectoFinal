/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Instagram.GUI;

import Instagram.Modelo.Usuario;
import Instagram.Logica.GestorUsuariosLocalINSTA;
import Instagram.Logica.GestorPerfiles;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.*;
import java.io.File;

/**
 *
 * @author najma
 */
public class DialogEditarPerfil extends JDialog {
   
    private Usuario usuario;
    private GestorUsuariosLocalINSTA gestorUsuarios;
    private GestorPerfiles gestorPerfiles;
    
    private JTextField txtNombreCompleto;
    private JTextField txtEmail;
    private JTextArea txtBiografia;
    private JLabel lblImagenPerfil;
    private File imagenSeleccionada;
    
    private static final Color INSTAGRAM_PINK = new Color(242, 80, 129);
    private static final Color BACKGROUND_COLOR = new Color(255, 240, 245);
    private static final Color CARD_COLOR = Color.WHITE;
    private static final Color BORDER_COLOR = new Color(255, 192, 203);
    private static final Color TEXT_PRIMARY = new Color(38, 38, 38);
    private static final Color TEXT_SECONDARY = new Color(142, 142, 142);
    
    public DialogEditarPerfil(Frame parent, Usuario usuario, GestorUsuariosLocalINSTA gestorUsuarios) {
        super(parent, "Editar Perfil", true);
        this.usuario = usuario;
        this.gestorUsuarios = gestorUsuarios;
        this.gestorPerfiles = new GestorPerfiles();
        
        initComponents();
        cargarDatosUsuario();
        configurarDialog();
    }
    
    private void initComponents() {
        setLayout(new BorderLayout());
        getContentPane().setBackground(BACKGROUND_COLOR);
        
        JPanel panelPrincipal = new JPanel();
        panelPrincipal.setLayout(new BoxLayout(panelPrincipal, BoxLayout.Y_AXIS));
        panelPrincipal.setBackground(CARD_COLOR);
        panelPrincipal.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createEmptyBorder(20, 20, 20, 20),
            new LineBorder(BORDER_COLOR, 2, true)
        ));
        
        JLabel lblTitulo = new JLabel("Editar Perfil");
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 24));
        lblTitulo.setForeground(TEXT_PRIMARY);
        lblTitulo.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        panelPrincipal.add(lblTitulo);
        panelPrincipal.add(Box.createVerticalStrut(30));
        
        JPanel panelAvatar = new JPanel();
        panelAvatar.setLayout(new BoxLayout(panelAvatar, BoxLayout.Y_AXIS));
        panelAvatar.setBackground(CARD_COLOR);
        panelAvatar.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        lblImagenPerfil = new JLabel();
        lblImagenPerfil.setPreferredSize(new Dimension(100, 100));
        lblImagenPerfil.setAlignmentX(Component.CENTER_ALIGNMENT);
        try {
            ImageIcon avatarIcon = new ImageIcon(getClass().getResource("/Instagram/icons/icon_perfil.png"));
            Image img = avatarIcon.getImage().getScaledInstance(100, 100, Image.SCALE_SMOOTH);
            lblImagenPerfil.setIcon(new ImageIcon(img));
        } catch (Exception e) {
            lblImagenPerfil.setText("👤");
            lblImagenPerfil.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 80));
            lblImagenPerfil.setForeground(INSTAGRAM_PINK);
        }
        
        JButton btnCambiarFoto = new JButton("Cambiar foto de perfil");
        btnCambiarFoto.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btnCambiarFoto.setForeground(INSTAGRAM_PINK);
        btnCambiarFoto.setBackground(CARD_COLOR);
        btnCambiarFoto.setBorderPainted(false);
        btnCambiarFoto.setContentAreaFilled(false);
        btnCambiarFoto.setFocusPainted(false);
        btnCambiarFoto.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnCambiarFoto.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnCambiarFoto.addActionListener(e -> cambiarFotoPerfil());
        
        panelAvatar.add(lblImagenPerfil);
        panelAvatar.add(Box.createVerticalStrut(10));
        panelAvatar.add(btnCambiarFoto);
        
        panelPrincipal.add(panelAvatar);
        panelPrincipal.add(Box.createVerticalStrut(30));
        
        JPanel panelForm = new JPanel();
        panelForm.setLayout(new BoxLayout(panelForm, BoxLayout.Y_AXIS));
        panelForm.setBackground(CARD_COLOR);
        panelForm.setAlignmentX(Component.CENTER_ALIGNMENT);
        panelForm.setMaximumSize(new Dimension(400, 500));
        
        txtNombreCompleto = crearCampoTexto("Nombre completo", panelForm);
        txtEmail = crearCampoTexto("Correo electrónico", panelForm);
        
        JLabel lblBiografia = new JLabel("Biografía");
        lblBiografia.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lblBiografia.setForeground(TEXT_PRIMARY);
        lblBiografia.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        txtBiografia = new JTextArea(4, 20);
        txtBiografia.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        txtBiografia.setLineWrap(true);
        txtBiografia.setWrapStyleWord(true);
        txtBiografia.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(BORDER_COLOR, 2),
            BorderFactory.createEmptyBorder(8, 10, 8, 10)
        ));
        
        JScrollPane scrollBio = new JScrollPane(txtBiografia);
        scrollBio.setBorder(null);
        scrollBio.setMaximumSize(new Dimension(400, 100));
        scrollBio.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        panelForm.add(lblBiografia);
        panelForm.add(Box.createVerticalStrut(6));
        panelForm.add(scrollBio);
        panelForm.add(Box.createVerticalStrut(30));
        
        panelPrincipal.add(panelForm);
        
        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
        panelBotones.setBackground(CARD_COLOR);
        panelBotones.setAlignmentX(Component.CENTER_ALIGNMENT);
        panelBotones.setMaximumSize(new Dimension(400, 50));
        
        JButton btnGuardar = new JButton("Guardar");
        btnGuardar.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnGuardar.setForeground(Color.WHITE);
        btnGuardar.setBackground(INSTAGRAM_PINK);
        btnGuardar.setOpaque(true);
        btnGuardar.setBorderPainted(false);
        btnGuardar.setBorder(BorderFactory.createEmptyBorder(10, 30, 10, 30));
        btnGuardar.setFocusPainted(false);
        btnGuardar.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnGuardar.addActionListener(e -> guardarCambios());
        
        JButton btnCancelar = new JButton("Cancelar");
        btnCancelar.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnCancelar.setForeground(TEXT_PRIMARY);
        btnCancelar.setBackground(Color.WHITE);
        btnCancelar.setBorder(new CompoundBorder(
            new LineBorder(BORDER_COLOR, 2),
            BorderFactory.createEmptyBorder(10, 30, 10, 30)
        ));
        btnCancelar.setFocusPainted(false);
        btnCancelar.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnCancelar.addActionListener(e -> dispose());
        
        panelBotones.add(btnGuardar);
        panelBotones.add(btnCancelar);
        
        panelPrincipal.add(panelBotones);
        panelPrincipal.add(Box.createVerticalStrut(20));
        
        add(panelPrincipal, BorderLayout.CENTER);
    }
    
    private JTextField crearCampoTexto(String etiqueta, JPanel panel) {
        JLabel lbl = new JLabel(etiqueta);
        lbl.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lbl.setForeground(TEXT_PRIMARY);
        lbl.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        JTextField campo = new JTextField();
        campo.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        campo.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(BORDER_COLOR, 2),
            BorderFactory.createEmptyBorder(8, 10, 8, 10)
        ));
        campo.setMaximumSize(new Dimension(400, 40));
        campo.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        panel.add(lbl);
        panel.add(Box.createVerticalStrut(6));
        panel.add(campo);
        panel.add(Box.createVerticalStrut(16));
        
        return campo;
    }
    
    private void cargarDatosUsuario() {
        txtNombreCompleto.setText(usuario.getNombreCompleto());
        txtEmail.setText(usuario.getEmail());
        
        String biografia = usuario.getBiografia();
        if (biografia != null && !biografia.isEmpty()) {
            txtBiografia.setText(biografia);
        }
    }
    
    private void cambiarFotoPerfil() {
        // Usar el selector de imágenes del navegador en lugar de JFileChooser
        DialogSelectorImagenes selector = new DialogSelectorImagenes(
            (Frame) SwingUtilities.getWindowAncestor(this)
        );
        selector.setVisible(true);
        
        File archivoSeleccionado = selector.getArchivoSeleccionado();
        if (archivoSeleccionado != null) {
            imagenSeleccionada = archivoSeleccionado;
            
            try {
                ImageIcon icon = new ImageIcon(imagenSeleccionada.getAbsolutePath());
                Image img = icon.getImage().getScaledInstance(100, 100, Image.SCALE_SMOOTH);
                lblImagenPerfil.setIcon(new ImageIcon(img));
                lblImagenPerfil.setText("");
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, 
                    "Error al cargar la imagen", 
                    "Error", 
                    JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    private void guardarCambios() {
        String nuevoNombre = txtNombreCompleto.getText().trim();
        String nuevoEmail = txtEmail.getText().trim();
        String nuevaBiografia = txtBiografia.getText().trim();
        
        if (nuevoNombre.isEmpty()) {
            JOptionPane.showMessageDialog(this, 
                "El nombre completo es obligatorio", 
                "Error", 
                JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        // ACTUALIZAR USUARIO
        usuario.setNombreCompleto(nuevoNombre);
        usuario.setEmail(nuevoEmail);
        usuario.setBiografia(nuevaBiografia);
        
        // GUARDAR EN GESTOR DE USUARIOS
        gestorUsuarios.actualizarUsuario(usuario);
        
        // ACTUALIZAR IMAGEN DE PERFIL
        if (imagenSeleccionada != null) {
            gestorPerfiles.actualizarImagenPerfil(
                usuario.getUsername(), 
                imagenSeleccionada.getAbsolutePath()
            );
        }
        
        // ACTUALIZAR BIOGRAFÍA EN PERFIL
        if (!nuevaBiografia.isEmpty()) {
            gestorPerfiles.actualizarBiografia(usuario.getUsername(), nuevaBiografia);
        }
        
        JOptionPane.showMessageDialog(this, 
            "Perfil actualizado correctamente", 
            "Éxito", 
            JOptionPane.INFORMATION_MESSAGE);
        
        // Notificar que se actualizó
        setVisible(false);
        dispose();
    }
    
    private void configurarDialog() {
        setSize(500, 780);
        setMinimumSize(new Dimension(450, 700));
        setLocationRelativeTo(getParent());
        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
    }
    
    // Método para que la ventana padre actualice la vista
    public boolean fueActualizado() {
        return !isVisible();
    }
}
