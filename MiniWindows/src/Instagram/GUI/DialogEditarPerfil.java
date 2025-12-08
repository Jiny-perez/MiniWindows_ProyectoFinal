/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Instagram.GUI;

import Instagram.Modelo.Usuario;
import Instagram.Logica.GestorUsuariosLocal;
import Instagram.Logica.GestorPerfiles;
import Instagram.Modelo.PerfilUsuario;
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
public class DialogEditarPerfil extends JDialog {
   
    private Usuario usuario;
    private GestorUsuariosLocal gestorUsuarios;
    private boolean cambiosGuardados = false;
    
    private JTextArea txtBiografia;
    private JLabel lblFotoPreview;
    private JButton btnCambiarFoto;
    private JButton btnEliminarFoto;
    private File fotoSeleccionada;
    private String rutaFotoActual;
    private JCheckBox chkDesactivarCuenta;
    
    private static final Color BACKGROUND_COLOR = Color.WHITE;
    private static final Color BORDER_COLOR = new Color(255, 192, 203);
    private static final Color TEXT_PRIMARY = new Color(38, 38, 38);
    private static final Color TEXT_SECONDARY = new Color(142, 142, 142);
    private static final Color INSTAGRAM_PINK = new Color(242, 80, 129);
    
    public DialogEditarPerfil(Frame parent, Usuario usuario, GestorUsuariosLocal gestorUsuarios) {
        super(parent, "Editar Perfil", true);
        this.usuario = usuario;
        this.gestorUsuarios = gestorUsuarios;
        
        initComponents();
        cargarDatosActuales();
        configurarDialogo();
    }
    
    private void initComponents() {
        setLayout(new BorderLayout());
        getContentPane().setBackground(BACKGROUND_COLOR);
        
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(BACKGROUND_COLOR);
        header.setBorder(new CompoundBorder(
            new MatteBorder(0, 0, 1, 0, BORDER_COLOR),
            BorderFactory.createEmptyBorder(16, 20, 16, 20)
        ));
        
        JLabel lblTitulo = new JLabel("Editar Perfil");
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 20));
        lblTitulo.setForeground(INSTAGRAM_PINK);
        
        header.add(lblTitulo, BorderLayout.WEST);
        add(header, BorderLayout.NORTH);
        
        JPanel centro = new JPanel();
        centro.setLayout(new BoxLayout(centro, BoxLayout.Y_AXIS));
        centro.setBackground(BACKGROUND_COLOR);
        centro.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));
        
        JPanel panelFoto = crearPanelFoto();
        panelFoto.setAlignmentX(Component.LEFT_ALIGNMENT);
        centro.add(panelFoto);
        centro.add(Box.createVerticalStrut(20));
        
        JLabel lblBiografiaTitulo = new JLabel("Biografía");
        lblBiografiaTitulo.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lblBiografiaTitulo.setForeground(TEXT_PRIMARY);
        lblBiografiaTitulo.setAlignmentX(Component.LEFT_ALIGNMENT);
        centro.add(lblBiografiaTitulo);
        centro.add(Box.createVerticalStrut(8));
        
        txtBiografia = new JTextArea(4, 40);
        txtBiografia.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        txtBiografia.setForeground(TEXT_PRIMARY);
        txtBiografia.setLineWrap(true);
        txtBiografia.setWrapStyleWord(true);
        txtBiografia.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(BORDER_COLOR, 2),
            BorderFactory.createEmptyBorder(8, 8, 8, 8)
        ));
        
        JScrollPane scrollBiografia = new JScrollPane(txtBiografia);
        scrollBiografia.setBorder(null);
        scrollBiografia.setAlignmentX(Component.LEFT_ALIGNMENT);
        centro.add(scrollBiografia);
        
        JLabel lblCaracteres = new JLabel("Máximo 150 caracteres");
        lblCaracteres.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lblCaracteres.setForeground(TEXT_SECONDARY);
        lblCaracteres.setAlignmentX(Component.LEFT_ALIGNMENT);
        centro.add(lblCaracteres);
        centro.add(Box.createVerticalStrut(20));
        
        JSeparator sep1 = new JSeparator();
        sep1.setMaximumSize(new Dimension(500, 1));
        sep1.setAlignmentX(Component.LEFT_ALIGNMENT);
        centro.add(sep1);
        centro.add(Box.createVerticalStrut(20));
        
        JLabel lblDesactivarTitulo = new JLabel("Configuración de Cuenta");
        lblDesactivarTitulo.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lblDesactivarTitulo.setForeground(TEXT_PRIMARY);
        lblDesactivarTitulo.setAlignmentX(Component.LEFT_ALIGNMENT);
        centro.add(lblDesactivarTitulo);
        centro.add(Box.createVerticalStrut(8));
        
        chkDesactivarCuenta = new JCheckBox("Desactivar mi cuenta temporalmente");
        chkDesactivarCuenta.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        chkDesactivarCuenta.setForeground(TEXT_PRIMARY);
        chkDesactivarCuenta.setBackground(BACKGROUND_COLOR);
        chkDesactivarCuenta.setAlignmentX(Component.LEFT_ALIGNMENT);
        centro.add(chkDesactivarCuenta);
        
        JLabel lblDesactivarInfo = new JLabel("<html>Al desactivar tu cuenta, tu perfil y publicaciones no serán visibles<br>" +
                                             "para otros usuarios hasta que la reactives.</html>");
        lblDesactivarInfo.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lblDesactivarInfo.setForeground(TEXT_SECONDARY);
        lblDesactivarInfo.setAlignmentX(Component.LEFT_ALIGNMENT);
        centro.add(lblDesactivarInfo);
        
        JScrollPane scrollCentro = new JScrollPane(centro);
        scrollCentro.setBorder(null);
        scrollCentro.getVerticalScrollBar().setUnitIncrement(16);
        add(scrollCentro, BorderLayout.CENTER);
        
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
        
        JButton btnGuardar = new JButton("Guardar Cambios");
        btnGuardar.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnGuardar.setForeground(Color.WHITE);
        btnGuardar.setBackground(INSTAGRAM_PINK);
        btnGuardar.setBorder(BorderFactory.createEmptyBorder(8, 24, 8, 24));
        btnGuardar.setFocusPainted(false);
        btnGuardar.setOpaque(true);
        btnGuardar.setBorderPainted(false);
        btnGuardar.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnGuardar.addActionListener(e -> guardarCambios());
        
        footer.add(btnCancelar);
        footer.add(btnGuardar);
        
        add(footer, BorderLayout.SOUTH);
    }
    
    private JPanel crearPanelFoto() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT, 20, 0));
        panel.setBackground(BACKGROUND_COLOR);
        panel.setMaximumSize(new Dimension(600, 180));
        
        lblFotoPreview = new JLabel();
        lblFotoPreview.setPreferredSize(new Dimension(150, 150));
        lblFotoPreview.setBorder(new LineBorder(BORDER_COLOR, 2));
        lblFotoPreview.setHorizontalAlignment(SwingConstants.CENTER);
        
        JPanel panelBotones = new JPanel();
        panelBotones.setLayout(new BoxLayout(panelBotones, BoxLayout.Y_AXIS));
        panelBotones.setBackground(BACKGROUND_COLOR);
        
        JLabel lblFotoTitulo = new JLabel("Foto de Perfil");
        lblFotoTitulo.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lblFotoTitulo.setForeground(TEXT_PRIMARY);
        lblFotoTitulo.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        btnCambiarFoto = new JButton("Cambiar Foto");
        btnCambiarFoto.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        btnCambiarFoto.setForeground(INSTAGRAM_PINK);
        btnCambiarFoto.setBackground(BACKGROUND_COLOR);
        btnCambiarFoto.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(INSTAGRAM_PINK, 2),
            BorderFactory.createEmptyBorder(8, 16, 8, 16)
        ));
        btnCambiarFoto.setFocusPainted(false);
        btnCambiarFoto.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnCambiarFoto.setAlignmentX(Component.LEFT_ALIGNMENT);
        btnCambiarFoto.addActionListener(e -> seleccionarFoto());
        
        btnEliminarFoto = new JButton("Eliminar Foto");
        btnEliminarFoto.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        btnEliminarFoto.setForeground(INSTAGRAM_PINK);
        btnEliminarFoto.setBackground(BACKGROUND_COLOR);
        btnEliminarFoto.setBorderPainted(false);
        btnEliminarFoto.setContentAreaFilled(false);
        btnEliminarFoto.setFocusPainted(false);
        btnEliminarFoto.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnEliminarFoto.setAlignmentX(Component.LEFT_ALIGNMENT);
        btnEliminarFoto.addActionListener(e -> eliminarFoto());
        
        panelBotones.add(lblFotoTitulo);
        panelBotones.add(Box.createVerticalStrut(12));
        panelBotones.add(btnCambiarFoto);
        panelBotones.add(Box.createVerticalStrut(8));
        panelBotones.add(btnEliminarFoto);
        
        panel.add(lblFotoPreview);
        panel.add(panelBotones);
        
        return panel;
    }
    
    private void cargarDatosActuales() {
        PerfilUsuario perfil = GestorPerfiles.cargarPerfil(usuario.getUsername());
        
        if (perfil.getBiografia() != null && !perfil.getBiografia().isEmpty()) {
            txtBiografia.setText(perfil.getBiografia());
        }
        
        rutaFotoActual = usuario.getRutaFotoPerfil();
        mostrarFotoActual();
        
        chkDesactivarCuenta.setSelected(!usuario.isActivo());
    }
    
    private void mostrarFotoActual() {
        try {
            if (rutaFotoActual != null && !rutaFotoActual.isEmpty()) {
                ImageIcon iconoOriginal = new ImageIcon(rutaFotoActual);
                Image img = iconoOriginal.getImage().getScaledInstance(150, 150, Image.SCALE_SMOOTH);
                lblFotoPreview.setIcon(new ImageIcon(img));
                lblFotoPreview.setText(null);
            } else {
                // Mostrar icono según género
                String iconoPath = usuario.getGenero() == 'F' ? 
                    "/Instagram/icons/icon_usuario_mujer.png" : 
                    "/Instagram/icons/icon_usuario_hombre.png";
                
                ImageIcon avatarIcon = new ImageIcon(getClass().getResource(iconoPath));
                Image img = avatarIcon.getImage().getScaledInstance(150, 150, Image.SCALE_SMOOTH);
                lblFotoPreview.setIcon(new ImageIcon(img));
                lblFotoPreview.setText(null);
            }
        } catch (Exception e) {
            lblFotoPreview.setText("👤");
            lblFotoPreview.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 80));
            lblFotoPreview.setForeground(INSTAGRAM_PINK);
        }
    }
    
    private void seleccionarFoto() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Seleccionar foto de perfil");
        fileChooser.setFileFilter(new FileNameExtensionFilter(
            "Imágenes (*.png, *.jpg, *.jpeg)", "png", "jpg", "jpeg"
        ));
        
        int resultado = fileChooser.showOpenDialog(this);
        
        if (resultado == JFileChooser.APPROVE_OPTION) {
            fotoSeleccionada = fileChooser.getSelectedFile();
            mostrarPreviewFoto();
        }
    }
    
    private void mostrarPreviewFoto() {
        try {
            ImageIcon iconoOriginal = new ImageIcon(fotoSeleccionada.getAbsolutePath());
            Image img = iconoOriginal.getImage().getScaledInstance(150, 150, Image.SCALE_SMOOTH);
            lblFotoPreview.setIcon(new ImageIcon(img));
            lblFotoPreview.setText(null);
            btnCambiarFoto.setText("Cambiar Foto");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(
                this,
                "Error al cargar la imagen",
                "Error",
                JOptionPane.ERROR_MESSAGE
            );
        }
    }
    
    private void eliminarFoto() {
        fotoSeleccionada = null;
        rutaFotoActual = null;
        mostrarFotoActual();
        btnCambiarFoto.setText("Agregar Foto");
    }
    
    private void guardarCambios() {
        String biografia = txtBiografia.getText().trim();
        
        // Validar longitud de biografía
        if (biografia.length() > 150) {
            JOptionPane.showMessageDialog(
                this,
                "La biografía no puede tener más de 150 caracteres",
                "Error",
                JOptionPane.WARNING_MESSAGE
            );
            return;
        }
        
        GestorPerfiles.actualizarBiografia(usuario.getUsername(), biografia);
        
        if (fotoSeleccionada != null) {
            boolean resultado = GestorPerfiles.actualizarAvatar(
                usuario.getUsername(), 
                fotoSeleccionada.getAbsolutePath()
            );
            
            if (resultado) {
                PerfilUsuario perfil = GestorPerfiles.cargarPerfil(usuario.getUsername());
                usuario.setRutaFotoPerfil(perfil.getRutaAvatar());
            }
        } else if (rutaFotoActual == null) {
            usuario.setRutaFotoPerfil(null);
            GestorPerfiles.actualizarAvatar(usuario.getUsername(), null);
        }
        
        gestorUsuarios.actualizarUsuario(usuario);
        
        boolean estadoAnterior = usuario.isActivo();
        boolean estadoNuevo = !chkDesactivarCuenta.isSelected();
        
        boolean desactivando = estadoAnterior && !estadoNuevo;
        
        if (estadoAnterior != estadoNuevo) {
            usuario.setActivo(estadoNuevo);
            
            if (estadoNuevo) {
                gestorUsuarios.activarUsuario(usuario.getUsername());
            } else {
                gestorUsuarios.desactivarUsuario(usuario.getUsername());
            }
            
            String mensaje = estadoNuevo ? 
                "Tu cuenta ha sido reactivada correctamente." :
                "Tu cuenta ha sido desactivada. Puedes reactivarla cuando quieras.";
            
            JOptionPane.showMessageDialog(
                this,
                mensaje,
                "Estado de Cuenta",
                JOptionPane.INFORMATION_MESSAGE
            );
        }
        
        cambiosGuardados = true;
        
        if (!desactivando) {
            JOptionPane.showMessageDialog(
                this,
                "¡Perfil actualizado exitosamente!",
                "Éxito",
                JOptionPane.INFORMATION_MESSAGE
            );
        }
        
        dispose();
    }
    
    private void configurarDialogo() {
        setSize(600, 650);
        setResizable(false);
        setLocationRelativeTo(getParent());
    }
    
    public boolean isCambiosGuardados() {
        return cambiosGuardados;
    }
}
