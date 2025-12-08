/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Instagram.GUI;

import Instagram.Modelo.Usuario;
import Instagram.Logica.GestorINSTA;
import Instagram.Logica.GestorUsuariosLocal;
import Instagram.Logica.GestorArchivosUsuario;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;


/**
 *
 * @author najma
 */
public class VentanaLogin extends JFrame {
   
    private GestorUsuariosLocal gestorUsuarios;
    
    private JPanel panelPrincipal;
    private JPanel panelLogin;
    private JPanel panelRegistro;
    private CardLayout cardLayout;
    
    private JTextField txtUsernameLogin;
    private JPasswordField txtPasswordLogin;
    private JButton btnLogin;
    private JButton btnIrARegistro;
    
    private JTextField txtUsernameRegistro;
    private JTextField txtNombreCompleto;
    private JComboBox<String> cmbGenero;
    private JSpinner spnEdad;
    private JPasswordField txtPasswordRegistro;
    private JPasswordField txtConfirmarPassword;
    private JButton btnRegistrar;
    private JButton btnIrALogin;
    
    private static final Color INSTAGRAM_PINK = new Color(242, 80, 129);
    private static final Color INSTAGRAM_PINK_HOVER = new Color(220, 60, 110);
    private static final Color BACKGROUND_COLOR = new Color(255, 240, 245);
    private static final Color TEXT_PRIMARY = new Color(38, 38, 38);
    private static final Color TEXT_SECONDARY = new Color(142, 142, 142);
    private static final Color BORDER_COLOR = new Color(255, 192, 203);
    
    public VentanaLogin() {
        GestorArchivosUsuario.inicializarEstructura();
        gestorUsuarios = new GestorUsuariosLocal();
        initComponents();
        configurarVentana();
    }
    
    private void initComponents() {
        cardLayout = new CardLayout();
        panelPrincipal = new JPanel(cardLayout);
        panelPrincipal.setBackground(BACKGROUND_COLOR);
        
        panelLogin = crearPanelLogin();
        panelRegistro = crearPanelRegistro();
        
        panelPrincipal.add(panelLogin, "LOGIN");
        panelPrincipal.add(panelRegistro, "REGISTRO");
        
        getContentPane().add(panelPrincipal);
    }
    
    private JPanel crearPanelLogin() {
        JPanel panel = new JPanel();
        panel.setLayout(new GridBagLayout());
        panel.setBackground(BACKGROUND_COLOR);
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
        JPanel container = new JPanel();
        container.setLayout(new BoxLayout(container, BoxLayout.Y_AXIS));
        container.setBackground(Color.WHITE);
        container.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(BORDER_COLOR, 2),
            BorderFactory.createEmptyBorder(40, 40, 40, 40)
        ));
        container.setMaximumSize(new Dimension(350, 500));
        
        JLabel lblLogo = new JLabel();
        try {
            ImageIcon logoIcon = new ImageIcon(getClass().getResource("/Instagram.icons/icon_insta.png"));
            Image img = logoIcon.getImage().getScaledInstance(80, 80, Image.SCALE_SMOOTH);
            lblLogo.setIcon(new ImageIcon(img));
        } catch (Exception e) {
            lblLogo.setText("Instagram");
            lblLogo.setFont(new Font("Brush Script MT", Font.ITALIC, 48));
            lblLogo.setForeground(INSTAGRAM_PINK);
        }
        lblLogo.setAlignmentX(Component.CENTER_ALIGNMENT);
        lblLogo.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
        container.add(lblLogo);
        container.add(Box.createVerticalStrut(30));
        
        txtUsernameLogin = crearCampoTexto("Username");
        txtUsernameLogin.setMaximumSize(new Dimension(270, 40));
        txtUsernameLogin.setAlignmentX(Component.CENTER_ALIGNMENT);
        container.add(txtUsernameLogin);
        container.add(Box.createVerticalStrut(10));
        
        txtPasswordLogin = crearCampoPassword("Contraseña");
        txtPasswordLogin.setMaximumSize(new Dimension(270, 40));
        txtPasswordLogin.setAlignmentX(Component.CENTER_ALIGNMENT);
        container.add(txtPasswordLogin);
        container.add(Box.createVerticalStrut(20));
        
        btnLogin = crearBotonPrincipal("Iniciar Sesión");
        btnLogin.addActionListener(e -> intentarLogin());
        btnLogin.setMaximumSize(new Dimension(270, 40));
        btnLogin.setAlignmentX(Component.CENTER_ALIGNMENT);
        container.add(btnLogin);
        container.add(Box.createVerticalStrut(30));
        
        JSeparator separador = new JSeparator();
        separador.setMaximumSize(new Dimension(270, 1));
        separador.setForeground(BORDER_COLOR);
        separador.setAlignmentX(Component.CENTER_ALIGNMENT);
        container.add(separador);
        container.add(Box.createVerticalStrut(20));
        
        JLabel lblNoTienesCuenta = new JLabel("¿No tienes una cuenta?");
        lblNoTienesCuenta.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        lblNoTienesCuenta.setForeground(TEXT_SECONDARY);
        lblNoTienesCuenta.setAlignmentX(Component.CENTER_ALIGNMENT);
        container.add(lblNoTienesCuenta);
        container.add(Box.createVerticalStrut(10));
        
        btnIrARegistro = crearBotonSecundario("Regístrate");
        btnIrARegistro.addActionListener(e -> mostrarRegistro());
        btnIrARegistro.setMaximumSize(new Dimension(270, 40));
        btnIrARegistro.setAlignmentX(Component.CENTER_ALIGNMENT);
        container.add(btnIrARegistro);
        
        gbc.gridx = 0;
        gbc.gridy = 0;
        panel.add(container, gbc);
        
        txtPasswordLogin.addActionListener(e -> btnLogin.doClick());
        
        return panel;
    }
    
    private JPanel crearPanelRegistro() {
        JPanel panel = new JPanel();
        panel.setLayout(new GridBagLayout());
        panel.setBackground(BACKGROUND_COLOR);
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
        JPanel container = new JPanel();
        container.setLayout(new BoxLayout(container, BoxLayout.Y_AXIS));
        container.setBackground(Color.WHITE);
        container.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(BORDER_COLOR, 2),
            BorderFactory.createEmptyBorder(40, 40, 40, 40)
        ));
        container.setMaximumSize(new Dimension(350, 700));
        
        JLabel lblLogo = new JLabel();
        try {
            ImageIcon logoIcon = new ImageIcon(getClass().getResource("/Instagram/icons/icon_insta.png"));
            Image img = logoIcon.getImage().getScaledInstance(70, 70, Image.SCALE_SMOOTH);
            lblLogo.setIcon(new ImageIcon(img));
        } catch (Exception e) {
            lblLogo.setText("Instagram");
            lblLogo.setFont(new Font("Brush Script MT", Font.ITALIC, 42));
            lblLogo.setForeground(INSTAGRAM_PINK);
        }
        lblLogo.setAlignmentX(Component.CENTER_ALIGNMENT);
        lblLogo.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
        container.add(lblLogo);
        container.add(Box.createVerticalStrut(10));
        
        JLabel lblTitulo = new JLabel("Regístrate en Instagram");
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 16));
        lblTitulo.setForeground(TEXT_SECONDARY);
        lblTitulo.setAlignmentX(Component.CENTER_ALIGNMENT);
        container.add(lblTitulo);
        container.add(Box.createVerticalStrut(20));
        
        txtUsernameRegistro = crearCampoTexto("Username");
        txtUsernameRegistro.setMaximumSize(new Dimension(270, 40));
        txtUsernameRegistro.setAlignmentX(Component.CENTER_ALIGNMENT);
        container.add(txtUsernameRegistro);
        container.add(Box.createVerticalStrut(10));
        
        txtNombreCompleto = crearCampoTexto("Nombre completo");
        txtNombreCompleto.setMaximumSize(new Dimension(270, 40));
        txtNombreCompleto.setAlignmentX(Component.CENTER_ALIGNMENT);
        container.add(txtNombreCompleto);
        container.add(Box.createVerticalStrut(10));
        
        cmbGenero = new JComboBox<>(new String[]{"Género", "Masculino", "Femenino"});
        cmbGenero.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        cmbGenero.setMaximumSize(new Dimension(270, 40));
        cmbGenero.setAlignmentX(Component.CENTER_ALIGNMENT);
        cmbGenero.setBackground(Color.WHITE);
        cmbGenero.setForeground(TEXT_SECONDARY);
        cmbGenero.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(BORDER_COLOR, 1),
            BorderFactory.createEmptyBorder(8, 8, 8, 8)
        ));
        container.add(cmbGenero);
        container.add(Box.createVerticalStrut(10));
        
        JPanel panelEdad = new JPanel(new BorderLayout(10, 0));
        panelEdad.setMaximumSize(new Dimension(270, 40));
        panelEdad.setAlignmentX(Component.CENTER_ALIGNMENT);
        panelEdad.setBackground(Color.WHITE);
        panelEdad.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(BORDER_COLOR, 1),
            BorderFactory.createEmptyBorder(8, 12, 8, 12)
        ));
        
        JLabel lblEdad = new JLabel("Edad:");
        lblEdad.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        lblEdad.setForeground(TEXT_SECONDARY);
        
        SpinnerNumberModel modeloEdad = new SpinnerNumberModel(18, 13, 120, 1);
        spnEdad = new JSpinner(modeloEdad);
        spnEdad.setFont(new Font("Segoe UI", Font.BOLD, 15));
        
        panelEdad.add(lblEdad, BorderLayout.WEST);
        panelEdad.add(spnEdad, BorderLayout.EAST);
        
        container.add(panelEdad);
        container.add(Box.createVerticalStrut(10));
        
        txtPasswordRegistro = crearCampoPassword("Contraseña");
        txtPasswordRegistro.setMaximumSize(new Dimension(270, 40));
        txtPasswordRegistro.setAlignmentX(Component.CENTER_ALIGNMENT);
        container.add(txtPasswordRegistro);
        container.add(Box.createVerticalStrut(10));
        
        txtConfirmarPassword = crearCampoPassword("Confirmar contraseña");
        txtConfirmarPassword.setMaximumSize(new Dimension(270, 40));
        txtConfirmarPassword.setAlignmentX(Component.CENTER_ALIGNMENT);
        container.add(txtConfirmarPassword);
        container.add(Box.createVerticalStrut(20));
        
        btnRegistrar = crearBotonPrincipal("Registrarse");
        btnRegistrar.addActionListener(e -> intentarRegistro());
        btnRegistrar.setMaximumSize(new Dimension(270, 40));
        btnRegistrar.setAlignmentX(Component.CENTER_ALIGNMENT);
        container.add(btnRegistrar);
        container.add(Box.createVerticalStrut(20));
        
        JSeparator separador = new JSeparator();
        separador.setMaximumSize(new Dimension(270, 1));
        separador.setForeground(BORDER_COLOR);
        separador.setAlignmentX(Component.CENTER_ALIGNMENT);
        container.add(separador);
        container.add(Box.createVerticalStrut(15));
        
        JLabel lblYaTienesCuenta = new JLabel("¿Ya tienes una cuenta?");
        lblYaTienesCuenta.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        lblYaTienesCuenta.setForeground(TEXT_SECONDARY);
        lblYaTienesCuenta.setAlignmentX(Component.CENTER_ALIGNMENT);
        container.add(lblYaTienesCuenta);
        container.add(Box.createVerticalStrut(10));
        
        btnIrALogin = crearBotonSecundario("Inicia sesión");
        btnIrALogin.addActionListener(e -> mostrarLogin());
        btnIrALogin.setMaximumSize(new Dimension(270, 40));
        btnIrALogin.setAlignmentX(Component.CENTER_ALIGNMENT);
        container.add(btnIrALogin);
        
        gbc.gridx = 0;
        gbc.gridy = 0;
        panel.add(container, gbc);
        
        return panel;
    }
    
    private JTextField crearCampoTexto(String placeholder) {
        JTextField campo = new JTextField();
        campo.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        campo.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(BORDER_COLOR, 1),
            BorderFactory.createEmptyBorder(8, 12, 8, 12)
        ));
        
        campo.setText(placeholder);
        campo.setForeground(TEXT_SECONDARY);
        
        campo.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                if (campo.getText().equals(placeholder)) {
                    campo.setText("");
                    campo.setForeground(TEXT_PRIMARY);
                }
                campo.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(INSTAGRAM_PINK, 2),
                    BorderFactory.createEmptyBorder(8, 12, 8, 12)
                ));
            }
            
            @Override
            public void focusLost(FocusEvent e) {
                if (campo.getText().isEmpty()) {
                    campo.setText(placeholder);
                    campo.setForeground(TEXT_SECONDARY);
                }
                campo.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(BORDER_COLOR, 1),
                    BorderFactory.createEmptyBorder(8, 12, 8, 12)
                ));
            }
        });
        
        return campo;
    }
    
    private JPasswordField crearCampoPassword(String placeholder) {
        JPasswordField campo = new JPasswordField();
        campo.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        campo.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(BORDER_COLOR, 1),
            BorderFactory.createEmptyBorder(8, 12, 8, 12)
        ));
        campo.setEchoChar((char) 0);
        campo.setText(placeholder);
        campo.setForeground(TEXT_SECONDARY);
        
        campo.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                if (String.valueOf(campo.getPassword()).equals(placeholder)) {
                    campo.setText("");
                    campo.setEchoChar('•');
                    campo.setForeground(TEXT_PRIMARY);
                }
                campo.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(INSTAGRAM_PINK, 2),
                    BorderFactory.createEmptyBorder(8, 12, 8, 12)
                ));
            }
            
            @Override
            public void focusLost(FocusEvent e) {
                if (String.valueOf(campo.getPassword()).isEmpty()) {
                    campo.setEchoChar((char) 0);
                    campo.setText(placeholder);
                    campo.setForeground(TEXT_SECONDARY);
                }
                campo.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(BORDER_COLOR, 1),
                    BorderFactory.createEmptyBorder(8, 12, 8, 12)
                ));
            }
        });
        
        return campo;
    }
    
    private JButton crearBotonPrincipal(String texto) {
        JButton boton = new JButton(texto);
        boton.setFont(new Font("Segoe UI", Font.BOLD, 14));
        boton.setForeground(Color.WHITE);
        boton.setBackground(INSTAGRAM_PINK);
        boton.setBorderPainted(false);
        boton.setFocusPainted(false);
        boton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        boton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                boton.setBackground(INSTAGRAM_PINK_HOVER);
            }
            
            @Override
            public void mouseExited(MouseEvent e) {
                boton.setBackground(INSTAGRAM_PINK);
            }
        });
        
        return boton;
    }
    
    private JButton crearBotonSecundario(String texto) {
        JButton boton = new JButton(texto);
        boton.setFont(new Font("Segoe UI", Font.BOLD, 14));
        boton.setForeground(INSTAGRAM_PINK);
        boton.setBackground(Color.WHITE);
        boton.setBorder(BorderFactory.createLineBorder(INSTAGRAM_PINK, 2));
        boton.setFocusPainted(false);
        boton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        boton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                boton.setBackground(new Color(255, 230, 240));
            }
            
            @Override
            public void mouseExited(MouseEvent e) {
                boton.setBackground(Color.WHITE);
            }
        });
        
        return boton;
    }
    
    private void mostrarLogin() {
        cardLayout.show(panelPrincipal, "LOGIN");
    }
    
    private void mostrarRegistro() {
        cardLayout.show(panelPrincipal, "REGISTRO");
    }
    
    private void intentarLogin() {
        String username = txtUsernameLogin.getText().trim();
        String password = String.valueOf(txtPasswordLogin.getPassword());
        
        if (username.isEmpty() || username.equals("Username")) {
            mostrarError("Por favor ingresa tu username");
            return;
        }
        
        if (password.isEmpty() || password.equals("Contraseña")) {
            mostrarError("Por favor ingresa tu contraseña");
            return;
        }
        
        if (!gestorUsuarios.validarCredenciales(username, password)) {
            mostrarError("Username o contraseña incorrectos");
            return;
        }
        
        Usuario usuario = gestorUsuarios.obtenerUsuario(username);
        
        if (!usuario.isActivo()) {
            int opcion = JOptionPane.showConfirmDialog(
                this,
                "Tu cuenta está desactivada.\n¿Deseas reactivarla?",
                "Reactivar cuenta",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE
            );
            
            if (opcion == JOptionPane.YES_OPTION) {
                gestorUsuarios.activarUsuario(username);
                usuario = gestorUsuarios.obtenerUsuario(username);
                JOptionPane.showMessageDialog(
                    this,
                    "¡Tu cuenta ha sido reactivada exitosamente!",
                    "Cuenta reactivada",
                    JOptionPane.INFORMATION_MESSAGE
                );
                abrirInstagram(usuario);
            }
        } else {
            // Usuario activo, abrir Instagram normalmente
            abrirInstagram(usuario);
        }
    }
    
    private void intentarRegistro() {
        String username = txtUsernameRegistro.getText().trim();
        String nombreCompleto = txtNombreCompleto.getText().trim();
        String password = String.valueOf(txtPasswordRegistro.getPassword());
        String confirmarPassword = String.valueOf(txtConfirmarPassword.getPassword());
        
        if (username.isEmpty() || username.equals("Username")) {
            mostrarError("Por favor ingresa un username");
            return;
        }
        
        if (nombreCompleto.isEmpty() || nombreCompleto.equals("Nombre completo")) {
            mostrarError("Por favor ingresa tu nombre completo");
            return;
        }
        
        if (cmbGenero.getSelectedIndex() == 0) {
            mostrarError("Por favor selecciona tu género");
            return;
        }
        
        int edad = (int) spnEdad.getValue();
        
        if (password.isEmpty() || password.equals("Contraseña")) {
            mostrarError("Por favor ingresa una contraseña");
            return;
        }
        
        if (password.length() < 6) {
            mostrarError("La contraseña debe tener al menos 6 caracteres");
            return;
        }
        
        if (!password.equals(confirmarPassword)) {
            mostrarError("Las contraseñas no coinciden");
            return;
        }
        
        char genero = cmbGenero.getSelectedIndex() == 1 ? 'M' : 'F';
        
        if (gestorUsuarios.registrarUsuario(username, nombreCompleto, genero, edad, password)) {
            Usuario usuario = gestorUsuarios.obtenerUsuario(username);
            
            this.setVisible(false);
            
            JOptionPane.showMessageDialog(
                null, // Sin parent para que no dependa de la ventana cerrada
                "¡Cuenta creada exitosamente! Bienvenido/a a Instagram, " + username + "!",
                "Registro Exitoso",
                JOptionPane.INFORMATION_MESSAGE
            );
            
            abrirInstagram(usuario);
        } else {
            mostrarError("Error al crear la cuenta. El username puede estar en uso.");
        }
    }
    
    private void abrirInstagram(Usuario usuario) {
        SwingUtilities.invokeLater(() -> {
            try {
                System.out.println("═══════════════════════════════════════");
                System.out.println("Abriendo Instagram para: " + usuario.getUsername());
                
                // Crear gestor de INSTA
                GestorINSTA gestorINSTA = new GestorINSTA(usuario);
                System.out.println("✓ GestorINSTA inicializado");
                
                // Crear ventana principal
                VentanaINSTA ventanaINSTA = new VentanaINSTA(usuario, gestorINSTA, gestorUsuarios);
                System.out.println("✓ VentanaINSTA creada");
                
                VentanaLogin.this.dispose();
                System.out.println("✓ VentanaLogin cerrada");
                
                ventanaINSTA.setVisible(true);
                System.out.println("✓ VentanaINSTA visible");
                
                System.out.println("═══════════════════════════════════════");
                System.out.println("¡Instagram abierto exitosamente!");
                System.out.println("Usuario: @" + usuario.getUsername());
                System.out.println("═══════════════════════════════════════");
                
            } catch (Exception e) {
                System.err.println("═══════════════════════════════════════");
                System.err.println("ERROR al abrir Instagram:");
                System.err.println("═══════════════════════════════════════");
                e.printStackTrace();
                
                JOptionPane.showMessageDialog(
                    null,
                    "Error al abrir la aplicación:\n" + e.getMessage() + 
                    "\n\nRevisa la consola para más detalles.",
                    "Error",
                    JOptionPane.ERROR_MESSAGE
                );
                
                VentanaLogin nuevaVentana = new VentanaLogin();
                nuevaVentana.setVisible(true);
            }
        });
    }
    
    private void mostrarError(String mensaje) {
        JOptionPane.showMessageDialog(
            this,
            mensaje,
            "Error",
            JOptionPane.ERROR_MESSAGE
        );
    }
    
    private void mostrarExito(String mensaje) {
        JOptionPane.showMessageDialog(
            this,
            mensaje,
            "Éxito",
            JOptionPane.INFORMATION_MESSAGE
        );
    }
    
    private void configurarVentana() {
        setTitle("Instagram - Iniciar Sesión");
        setSize(450, 650);
        setMinimumSize(new Dimension(450, 650));
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);
    }
    
}
