package GestorUsuario;

import javax.swing.*;
import java.awt.*;
import GestorUsuario.GestorUsuarios;
import Excepciones.*;
import GestorArchivos.MiniWindowsClass;

/**
 *
 * @author marye
 */
public class GestorUsuarioGUI extends JDialog {

    private final GestorUsuarios gestor;

    public GestorUsuarioGUI(Window owner, GestorUsuarios gestor) {
        super(owner, "Crear Nueva Cuenta", ModalityType.APPLICATION_MODAL);
        this.gestor = gestor;

        initComponents();

        setSize(400, 350);
        setLocationRelativeTo(owner);
        setResizable(false);
        setVisible(true);
    }

    private void initComponents() {
        setLayout(null);
        getContentPane().setBackground(Color.WHITE);

        JLabel lblNombre = new JLabel("Nombre Completo:");
        lblNombre.setBounds(30, 20, 340, 25);
        lblNombre.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        add(lblNombre);

        JTextField txtNombre = new JTextField();
        txtNombre.setBounds(30, 45, 340, 30);
        txtNombre.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        txtNombre.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(180, 180, 180), 1),
                BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));
        add(txtNombre);

        JLabel lblUsuarioNuevo = new JLabel("Nombre de Usuario:");
        lblUsuarioNuevo.setBounds(30, 85, 340, 25);
        lblUsuarioNuevo.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        add(lblUsuarioNuevo);

        JTextField txtUsuarioNuevo = new JTextField();
        txtUsuarioNuevo.setBounds(30, 110, 340, 30);
        txtUsuarioNuevo.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        txtUsuarioNuevo.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(180, 180, 180), 1),
                BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));
        add(txtUsuarioNuevo);

        JLabel lblPasswordNueva = new JLabel("Contraseña:");
        lblPasswordNueva.setBounds(30, 150, 340, 25);
        lblPasswordNueva.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        add(lblPasswordNueva);

        JPasswordField txtPasswordNueva = new JPasswordField();
        txtPasswordNueva.setBounds(30, 175, 340, 30);
        txtPasswordNueva.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        txtPasswordNueva.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(180, 180, 180), 1),
                BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));
        add(txtPasswordNueva);

        JLabel lblInfo = new JLabel("La contraseña debe tener al menos 4 caracteres");
        lblInfo.setBounds(30, 210, 340, 20);
        lblInfo.setFont(new Font("Segoe UI", Font.ITALIC, 11));
        lblInfo.setForeground(Color.GRAY);
        add(lblInfo);

        JButton btnCrear = new JButton("Crear Cuenta");
        btnCrear.setBounds(30, 245, 165, 40);
        btnCrear.setBackground(new Color(255, 182, 193)); // ROSADO
        btnCrear.setForeground(Color.WHITE);
        btnCrear.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btnCrear.setFocusPainted(false);
        btnCrear.setBorderPainted(false);
        btnCrear.setCursor(new Cursor(Cursor.HAND_CURSOR));
        add(btnCrear);

        JButton btnCancelar = new JButton("Cancelar");
        btnCancelar.setBounds(205, 245, 165, 40);
        btnCancelar.setBackground(new Color(255, 182, 193)); // ROSADO
        btnCancelar.setForeground(Color.WHITE);
        btnCancelar.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btnCancelar.setFocusPainted(false);
        btnCancelar.setBorderPainted(false);
        btnCancelar.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnCancelar.addActionListener(e -> dispose());
        add(btnCancelar);

        btnCrear.addActionListener(e -> {
            String nombre = txtNombre.getText().trim();
            String usuario = txtUsuarioNuevo.getText().trim();
            String pass = new String(txtPasswordNueva.getPassword());

            if (nombre.isEmpty() || usuario.isEmpty() || pass.isEmpty()) {
                JOptionPane.showMessageDialog(GestorUsuarioGUI.this,
                        "Por favor complete todos los campos",
                        "Campos Incompletos",
                        JOptionPane.WARNING_MESSAGE);
                return;
            }
            if (pass.length() < 4) {
                JOptionPane.showMessageDialog(GestorUsuarioGUI.this,
                        "La contraseña debe tener al menos 4 caracteres",
                        "Contraseña débil",
                        JOptionPane.WARNING_MESSAGE);
                return;
            }

            try {
                Usuario nuevo = gestor.crearUsuario(nombre, usuario, pass);

                MiniWindowsClass windows = MiniWindowsClass.getInstance();
                if (windows != null && windows.getSistemaArchivos() != null) {
                    windows.getSistemaArchivos().crearCarpetaUsuario(usuario);
                    windows.guardarSistema();
                }

                JOptionPane.showMessageDialog(GestorUsuarioGUI.this,
                        "Cuenta creada exitosamente!\nYa puede iniciar sesión.",
                        "Éxito", JOptionPane.INFORMATION_MESSAGE);
                dispose();

            } catch (ArchivoNoValidoException ex) {
                JOptionPane.showMessageDialog(GestorUsuarioGUI.this,
                        ex.getMessage(),
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });
    }
}
