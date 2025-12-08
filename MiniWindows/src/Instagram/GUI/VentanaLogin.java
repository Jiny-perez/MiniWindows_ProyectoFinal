package Instagram.GUI;

import Instagram.Modelo.Usuario;
import Instagram.Logica.GestorINSTACompleto;
import Instagram.Logica.GestorUsuariosLocalINSTA;
import Instagram.Logica.GestorArchivosUsuarioINSTA;
import java.awt.*;
import javax.swing.*;
import javax.swing.border.*;

/**
 *
 * @author najma
 */
public class VentanaLogin extends JFrame {
   
    private GestorINSTACompleto gestorINSTA;
    private GestorUsuariosLocalINSTA gestorUsuarios;
    
    private JTextField txtUsername;
    private JPasswordField txtPassword;
    private JPanel panelPrincipal;
    private CardLayout cardLayout;
    
    private static final Color INSTAGRAM_PINK = new Color(242, 80, 129);
    private static final Color BACKGROUND_COLOR = new Color(255, 240, 245);
    private static final Color CARD_COLOR = Color.WHITE;
    private static final Color BORDER_COLOR = new Color(255, 192, 203);
    private static final Color TEXT_PRIMARY = new Color(38, 38, 38);
    private static final Color TEXT_SECONDARY = new Color(142, 142, 142);
    
    public VentanaLogin() {
        GestorArchivosUsuarioINSTA.inicializarEstructura();
        gestorUsuarios = new GestorUsuariosLocalINSTA();
        gestorINSTA = new GestorINSTACompleto(gestorUsuarios);
        
        initComponents();
        configurarVentana();
    }
    
    private void initComponents() {
        cardLayout = new CardLayout();
        panelPrincipal = new JPanel(cardLayout);
        panelPrincipal.setBackground(BACKGROUND_COLOR);
        
        JPanel panelLogin = crearPanelLogin();
        JPanel panelRegistro = crearPanelRegistro();
        
        panelPrincipal.add(panelLogin, "LOGIN");
        panelPrincipal.add(panelRegistro, "REGISTRO");
        
        add(panelPrincipal);
    }
    
    private JPanel crearPanelLogin() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(BACKGROUND_COLOR);
        panel.setBorder(BorderFactory.createEmptyBorder(40, 40, 40, 40));
        
        JPanel panelFormulario = new JPanel();
        panelFormulario.setLayout(new BoxLayout(panelFormulario, BoxLayout.Y_AXIS));
        panelFormulario.setBackground(CARD_COLOR);
        panelFormulario.setBorder(new CompoundBorder(
            new LineBorder(BORDER_COLOR, 3, true),
            BorderFactory.createEmptyBorder(40, 40, 40, 40)
        ));
        panelFormulario.setMaximumSize(new Dimension(400, 500));
        
        JLabel lblLogo = new JLabel();
        try {
            ImageIcon logoIcon = new ImageIcon(getClass().getResource("/Instagram/icons/icon_insta.png"));
            Image img = logoIcon.getImage().getScaledInstance(80, 80, Image.SCALE_SMOOTH);
            lblLogo.setIcon(new ImageIcon(img));
        } catch (Exception e) {
            lblLogo.setText("Instagram");
            lblLogo.setFont(new Font("Brush Script MT", Font.ITALIC, 42));
            lblLogo.setForeground(INSTAGRAM_PINK);
        }
        lblLogo.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        JLabel lblTitulo = new JLabel("Iniciar Sesión");
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 24));
        lblTitulo.setForeground(TEXT_PRIMARY);
        lblTitulo.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        panelFormulario.add(lblLogo);
        panelFormulario.add(Box.createVerticalStrut(20));
        panelFormulario.add(lblTitulo);
        panelFormulario.add(Box.createVerticalStrut(30));
        
        JPanel panelCampos = new JPanel();
        panelCampos.setLayout(new BoxLayout(panelCampos, BoxLayout.Y_AXIS));
        panelCampos.setBackground(CARD_COLOR);
        panelCampos.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        txtUsername = crearCampo("Usuario", panelCampos);
        txtPassword = crearCampoPassword("Contraseña", panelCampos);
        txtPassword.addActionListener(e -> iniciarSesion());
        
        panelFormulario.add(panelCampos);
        panelFormulario.add(Box.createVerticalStrut(24));
        
        JButton btnLogin = new JButton("Iniciar Sesión");
        btnLogin.setFont(new Font("Segoe UI", Font.BOLD, 15));
        btnLogin.setForeground(Color.WHITE);
        btnLogin.setBackground(INSTAGRAM_PINK);
        btnLogin.setOpaque(true);
        btnLogin.setBorderPainted(false);
        btnLogin.setBorder(BorderFactory.createEmptyBorder(12, 0, 12, 0));
        btnLogin.setFocusPainted(false);
        btnLogin.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnLogin.setMaximumSize(new Dimension(320, 44));
        btnLogin.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnLogin.addActionListener(e -> iniciarSesion());
        
        panelFormulario.add(btnLogin);
        panelFormulario.add(Box.createVerticalStrut(20));
        
        JPanel panelRegistro = new JPanel(new FlowLayout(FlowLayout.CENTER, 4, 0));
        panelRegistro.setBackground(CARD_COLOR);
        panelRegistro.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        JLabel lblNoTienesCuenta = new JLabel("¿No tienes cuenta?");
        lblNoTienesCuenta.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        lblNoTienesCuenta.setForeground(TEXT_SECONDARY);
        
        JButton btnIrARegistro = new JButton("Regístrate");
        btnIrARegistro.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btnIrARegistro.setForeground(INSTAGRAM_PINK);
        btnIrARegistro.setBackground(CARD_COLOR);
        btnIrARegistro.setBorderPainted(false);
        btnIrARegistro.setContentAreaFilled(false);
        btnIrARegistro.setFocusPainted(false);
        btnIrARegistro.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnIrARegistro.addActionListener(e -> cardLayout.show(panelPrincipal, "REGISTRO"));
        
        panelRegistro.add(lblNoTienesCuenta);
        panelRegistro.add(btnIrARegistro);
        
        panelFormulario.add(panelRegistro);
        
        panel.add(Box.createVerticalGlue());
        panel.add(panelFormulario);
        panel.add(Box.createVerticalGlue());
        
        return panel;
    }
    
    private JPanel crearPanelRegistro() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(BACKGROUND_COLOR);
        panel.setBorder(BorderFactory.createEmptyBorder(40, 40, 40, 40));
        
        JPanel panelFormulario = new JPanel();
        panelFormulario.setLayout(new BoxLayout(panelFormulario, BoxLayout.Y_AXIS));
        panelFormulario.setBackground(CARD_COLOR);
        panelFormulario.setBorder(new CompoundBorder(
            new LineBorder(BORDER_COLOR, 3, true),
            BorderFactory.createEmptyBorder(40, 40, 40, 40)
        ));
        panelFormulario.setMaximumSize(new Dimension(400, 700));
        
        JLabel lblLogo = new JLabel();
        try {
            ImageIcon logoIcon = new ImageIcon(getClass().getResource("/Instagram/icons/icon_insta.png"));
            Image img = logoIcon.getImage().getScaledInstance(70, 70, Image.SCALE_SMOOTH);
            lblLogo.setIcon(new ImageIcon(img));
        } catch (Exception e) {
            lblLogo.setText("Instagram");
            lblLogo.setFont(new Font("Brush Script MT", Font.ITALIC, 38));
            lblLogo.setForeground(INSTAGRAM_PINK);
        }
        lblLogo.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        JLabel lblTitulo = new JLabel("Crear Cuenta");
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 24));
        lblTitulo.setForeground(TEXT_PRIMARY);
        lblTitulo.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        panelFormulario.add(lblLogo);
        panelFormulario.add(Box.createVerticalStrut(16));
        panelFormulario.add(lblTitulo);
        panelFormulario.add(Box.createVerticalStrut(24));
        
        JPanel panelCampos = new JPanel();
        panelCampos.setLayout(new BoxLayout(panelCampos, BoxLayout.Y_AXIS));
        panelCampos.setBackground(CARD_COLOR);
        panelCampos.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        JTextField txtNombreCompleto = crearCampo("Nombre completo", panelCampos);
        JTextField txtUsernameReg = crearCampo("Usuario", panelCampos);
        JPasswordField txtPasswordReg = crearCampoPassword("Contraseña", panelCampos);
        JPasswordField txtConfirmarPassword = crearCampoPassword("Confirmar contraseña", panelCampos);
        
        JLabel lblGenero = new JLabel("Género");
        lblGenero.setFont(new Font("Segoe UI", Font.BOLD, 13));
        lblGenero.setForeground(TEXT_PRIMARY);
        lblGenero.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        JComboBox<String> cmbGenero = new JComboBox<>(new String[]{"Masculino", "Femenino", "Otro"});
        cmbGenero.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        cmbGenero.setMaximumSize(new Dimension(320, 40));
        cmbGenero.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        panelCampos.add(lblGenero);
        panelCampos.add(Box.createVerticalStrut(6));
        panelCampos.add(cmbGenero);
        panelCampos.add(Box.createVerticalStrut(14));
        
        JLabel lblEdad = new JLabel("Edad");
        lblEdad.setFont(new Font("Segoe UI", Font.BOLD, 13));
        lblEdad.setForeground(TEXT_PRIMARY);
        lblEdad.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        JSpinner spnEdad = new JSpinner(new SpinnerNumberModel(18, 13, 100, 1));
        spnEdad.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        spnEdad.setMaximumSize(new Dimension(320, 40));
        spnEdad.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        panelCampos.add(lblEdad);
        panelCampos.add(Box.createVerticalStrut(6));
        panelCampos.add(spnEdad);
        
        panelFormulario.add(panelCampos);
        panelFormulario.add(Box.createVerticalStrut(20));
        
        JButton btnRegistro = new JButton("Registrarse");
        btnRegistro.setFont(new Font("Segoe UI", Font.BOLD, 15));
        btnRegistro.setForeground(Color.WHITE);
        btnRegistro.setBackground(INSTAGRAM_PINK);
        btnRegistro.setOpaque(true);
        btnRegistro.setBorderPainted(false);
        btnRegistro.setBorder(BorderFactory.createEmptyBorder(12, 0, 12, 0));
        btnRegistro.setFocusPainted(false);
        btnRegistro.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnRegistro.setMaximumSize(new Dimension(320, 44));
        btnRegistro.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        btnRegistro.addActionListener(e -> {
            String nombreCompleto = txtNombreCompleto.getText().trim();
            String username = txtUsernameReg.getText().trim();
            String password = new String(txtPasswordReg.getPassword());
            String confirmarPassword = new String(txtConfirmarPassword.getPassword());
            String generoStr = (String) cmbGenero.getSelectedItem();
            int edad = (Integer) spnEdad.getValue();
            
            char genero = generoStr.equals("Masculino") ? 'M' : (generoStr.equals("Femenino") ? 'F' : 'O');
            
            if (nombreCompleto.isEmpty() || username.isEmpty() || 
                password.isEmpty() || confirmarPassword.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Todos los campos son obligatorios", 
                    "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            if (!password.equals(confirmarPassword)) {
                JOptionPane.showMessageDialog(this, "Las contraseñas no coinciden", 
                    "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            if (gestorUsuarios.existeUsuario(username)) {
                JOptionPane.showMessageDialog(this, "El usuario ya existe", 
                    "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            if (gestorUsuarios.registrarUsuario(username, password, genero, edad, nombreCompleto)) {
                JOptionPane.showMessageDialog(this, "Cuenta creada exitosamente", 
                    "Éxito", JOptionPane.INFORMATION_MESSAGE);
                
                txtNombreCompleto.setText("");
                txtUsernameReg.setText("");
                txtPasswordReg.setText("");
                txtConfirmarPassword.setText("");
                
                cardLayout.show(panelPrincipal, "LOGIN");
            }
        });
        
        panelFormulario.add(btnRegistro);
        panelFormulario.add(Box.createVerticalStrut(16));
        
        JPanel panelLogin = new JPanel(new FlowLayout(FlowLayout.CENTER, 4, 0));
        panelLogin.setBackground(CARD_COLOR);
        panelLogin.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        JLabel lblYaTienesCuenta = new JLabel("¿Ya tienes cuenta?");
        lblYaTienesCuenta.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        lblYaTienesCuenta.setForeground(TEXT_SECONDARY);
        
        JButton btnIrALogin = new JButton("Inicia sesión");
        btnIrALogin.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btnIrALogin.setForeground(INSTAGRAM_PINK);
        btnIrALogin.setBackground(CARD_COLOR);
        btnIrALogin.setBorderPainted(false);
        btnIrALogin.setContentAreaFilled(false);
        btnIrALogin.setFocusPainted(false);
        btnIrALogin.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnIrALogin.addActionListener(e -> cardLayout.show(panelPrincipal, "LOGIN"));
        
        panelLogin.add(lblYaTienesCuenta);
        panelLogin.add(btnIrALogin);
        
        panelFormulario.add(panelLogin);
        
        panel.add(Box.createVerticalGlue());
        panel.add(panelFormulario);
        panel.add(Box.createVerticalGlue());
        
        return panel;
    }
    
    private JTextField crearCampo(String etiqueta, JPanel panel) {
        JLabel lbl = new JLabel(etiqueta);
        lbl.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lbl.setForeground(TEXT_PRIMARY);
        lbl.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        JTextField campo = new JTextField();
        campo.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        campo.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(BORDER_COLOR, 2),
            BorderFactory.createEmptyBorder(8, 10, 8, 10)
        ));
        campo.setMaximumSize(new Dimension(320, 40));
        campo.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        panel.add(lbl);
        panel.add(Box.createVerticalStrut(6));
        panel.add(campo);
        panel.add(Box.createVerticalStrut(16));
        
        return campo;
    }
    
    private JPasswordField crearCampoPassword(String etiqueta, JPanel panel) {
        JLabel lbl = new JLabel(etiqueta);
        lbl.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lbl.setForeground(TEXT_PRIMARY);
        lbl.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        JPasswordField campo = new JPasswordField();
        campo.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        campo.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(BORDER_COLOR, 2),
            BorderFactory.createEmptyBorder(8, 10, 8, 10)
        ));
        campo.setMaximumSize(new Dimension(320, 40));
        campo.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        panel.add(lbl);
        panel.add(Box.createVerticalStrut(6));
        panel.add(campo);
        panel.add(Box.createVerticalStrut(16));
        
        return campo;
    }
    
    private void iniciarSesion() {
        String username = txtUsername.getText().trim();
        String password = new String(txtPassword.getPassword());
        
        if (username.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Ingresa usuario y contraseña", 
                "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        Usuario usuario = gestorUsuarios.validarLogin(username, password);
        
        if (usuario != null) {
            gestorINSTA.setUsuarioActual(usuario);
            abrirInstagram(usuario);
        } else {
            JOptionPane.showMessageDialog(this, "Usuario o contraseña incorrectos", 
                "Error", JOptionPane.ERROR_MESSAGE);
            txtPassword.setText("");
        }
    }
    
    private void abrirInstagram(Usuario usuario) {
        dispose();
        
        SwingUtilities.invokeLater(() -> {
            VentanaINSTA ventana = new VentanaINSTA(usuario, gestorINSTA, gestorUsuarios);
            ventana.setVisible(true);
        });
    }
    
    private void configurarVentana() {
        setTitle("Instagram - Login");
        setSize(550, 820);
        setMinimumSize(new Dimension(500, 750));
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
    }
    
    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        SwingUtilities.invokeLater(() -> {
            VentanaLogin ventana = new VentanaLogin();
            ventana.setVisible(true);
        });
    }
}
