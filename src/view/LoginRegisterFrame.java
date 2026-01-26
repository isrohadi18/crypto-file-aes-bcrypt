package view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.border.EmptyBorder;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.File;
import java.io.IOException;
import config.DatabaseConnection;
import controller.AuthController;
import org.mindrot.jbcrypt.BCrypt;
import java.sql.*;
import static javax.swing.JFrame.EXIT_ON_CLOSE;

public class LoginRegisterFrame extends JFrame {
    private final AuthController authController = new AuthController();
    private String loggedInUsername;
    private String loggedInRole;
    private JTextField txtUsername;
    private JPasswordField txtPassword;
    private JTextField txtUsernameRegister;
    private JPasswordField txtPasswordRegister;
    private JCheckBox chkRememberMe;
    private JComboBox<String> comboRoleRegister;
    private JCheckBox chkShowPasswordLogin;
    private JCheckBox chkShowPasswordRegister;
    private JButton btnLogin;
    private JButton btnRegister;
    private JLabel lblSwitchToRegister;
    private JLabel lblSwitchToLogin;
    private JPanel panelLogin;
    private JPanel panelRegister;
    private CardLayout cardLayout;
    private JPanel cardPanel;
    private final String REMEMBER_FILE_PATH = "config/remembered_user.txt";

    public LoginRegisterFrame() {
        setTitle("Aplikasi Kriptografi File - AES 128 Bit");
        setSize(900, 350);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        cardLayout = new CardLayout();
        cardPanel = new JPanel(cardLayout);

        createLoginPanel();
        createRegisterPanel();

        cardPanel.add(panelLogin, "login");
        cardPanel.add(panelRegister, "register");
 
        add(cardPanel, BorderLayout.CENTER);
        cardLayout.show(cardPanel, "login");

        setVisible(true);
    }

    private void createLoginPanel() {
        panelLogin = new JPanel(new BorderLayout());
        panelLogin.setBackground(new Color(245, 240, 230));

        JPanel leftPanel = new JPanel();
        leftPanel.setBackground(new Color(121, 85, 72));
        leftPanel.setPreferredSize(new Dimension(300, 500));
        leftPanel.setLayout(new BoxLayout(leftPanel, BoxLayout.Y_AXIS));
        leftPanel.setBorder(new EmptyBorder(40, 10, 10, 10));

        ImageIcon icon = new ImageIcon(getClass().getResource("/assets/ALS.png"));
        JLabel lblLogo = new JLabel(icon, JLabel.CENTER);
        lblLogo.setAlignmentX(Component.CENTER_ALIGNMENT);
        lblLogo.setBorder(new EmptyBorder(40, 0, 15, 0));

        JLabel lbltittle = new JLabel("SELAMAT DATANG", JLabel.CENTER);
        lbltittle.setForeground(Color.WHITE);
        lbltittle.setFont(new Font("Arial", Font.BOLD, 20));
        lbltittle.setAlignmentX(Component.CENTER_ALIGNMENT);
        lbltittle.setBorder(new EmptyBorder(0, 0, 0, 0));

        JLabel lblSubtittle = new JLabel("APLIKASI KRIPTOGRAFI AES 128-bit", JLabel.CENTER);
        lblSubtittle.setForeground(Color.WHITE);
        lblSubtittle.setFont(new Font("Arial", Font.BOLD, 12));
        lblSubtittle.setAlignmentX(Component.CENTER_ALIGNMENT);

        leftPanel.add(lblLogo);
        leftPanel.add(lbltittle);
        leftPanel.add(lblSubtittle);

        JPanel formPanel = new JPanel();
        formPanel.setBackground(new Color(245, 240, 230));
        formPanel.setLayout(new GridBagLayout());

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        ImageIcon originallogin = new ImageIcon(getClass().getResource("/assets/login.png"));
        Image scaledImage = originallogin.getImage().getScaledInstance(24, 24, Image.SCALE_SMOOTH);
        ImageIcon scaledIcon = new ImageIcon(scaledImage);

        JLabel lblLoginTitle = new JLabel("LOGIN AKUN", scaledIcon, JLabel.LEFT);
        lblLoginTitle.setIconTextGap(10); 

        lblLoginTitle.setIconTextGap(10); 
        lblLoginTitle.setFont(new Font("Segoe UI", Font.BOLD, 20));
        lblLoginTitle.setForeground(new Color(121, 85, 72));
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
        formPanel.add(lblLoginTitle, gbc);

        gbc.gridwidth = 1;
        gbc.gridx = 0; gbc.gridy = 1; 
        formPanel.add(new JLabel("Username"), gbc);
        gbc.gridx = 1; 
        txtUsername = new JTextField(20);
        formPanel.add(txtUsername, gbc);

        gbc.gridx = 0; gbc.gridy=2;
        formPanel.add(new JLabel("Password"), gbc);
        gbc.gridx = 1;

        JPanel panelPassword = new JPanel(new BorderLayout());
        txtPassword = new JPasswordField(20);
        JCheckBox chkShowPassword = new JCheckBox("Lihat Password");
        chkShowPassword.setBackground(new Color(245, 240, 230));

        chkShowPassword.addActionListener(e -> {
            if (chkShowPassword.isSelected()) {
                txtPassword.setEchoChar((char) 0);
            } else {
                txtPassword.setEchoChar('\u2022');
            }
        });

        panelPassword.setBackground(new Color(245, 240, 230));
        panelPassword.add(txtPassword, BorderLayout.CENTER);
        panelPassword.add(chkShowPassword, BorderLayout.EAST);

        formPanel.add(panelPassword, gbc);

        gbc.gridx = 1; gbc.gridy++;
        chkRememberMe = new JCheckBox("Remember Me");
        chkRememberMe.setBackground(new Color(245, 240, 230));
        formPanel.add(chkRememberMe, gbc);

        gbc.gridx = 1; gbc.gridy++;
        btnLogin = new JButton("LOGIN");
        btnLogin.setBackground(new Color(121, 85, 72));
        btnLogin.setForeground(Color.WHITE);
        formPanel.add(btnLogin, gbc);
        
        btnLogin.addActionListener(e -> {
            String username = txtUsername.getText();
            String password = new String(txtPassword.getPassword());

            String role = checkLogin(username, password);
            if (role != null) {
                if (chkRememberMe.isSelected()) {
                    saveRememberedUser(username, role);
                } else {
                    clearRememberedUser();
                }

                JOptionPane.showMessageDialog(this, "Login berhasil sebagai " + role + "!");
                dispose(); 
                
                MenuFrame menu = new MenuFrame(username, role); 
                menu.setLocationRelativeTo(null); 
                menu.setVisible(true);
            } else {
                JOptionPane.showMessageDialog(this, "Username atau Password salah!");
            }
        });

        gbc.gridx = 1; gbc.gridy++;
        lblSwitchToRegister = new JLabel("<html><span style='color:#5A4A42;'>Belum Punya Akun ? </span><a href='#' style='text-decoration:none;color:#1976D2;'>  DAFTAR DI SINI </a></html>");
        lblSwitchToRegister.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        lblSwitchToRegister.setFont(new Font("Segoe UI", Font.BOLD, 12));
        lblSwitchToRegister.setForeground(new Color(90, 70, 60));
        formPanel.add(lblSwitchToRegister, gbc);

        lblSwitchToRegister.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                lblSwitchToRegister.setText("<html><span style='color:#5A4A42;'>Silahkan Daftar ? </span><a href='#' style='text-decoration:underline;color:#1976D2;'>  DAFTAR DI SINI </a></html>");
            }

            @Override
            public void mouseExited(MouseEvent e) {
                lblSwitchToRegister.setText("<html><span style='color:#5A4A42;'>Belum Punya Akun ? </span><a href='#' style='text-decoration:none;color:#1976D2;'> DAFTAR DI SINI </a></html>");
            }

            @Override
            public void mouseClicked(MouseEvent e) {
                cardLayout.show(cardPanel, "register"); 
            }
        });
        
        panelLogin.add(leftPanel, BorderLayout.WEST);
        panelLogin.add(formPanel, BorderLayout.CENTER);
    }

    private void createRegisterPanel() {
        panelRegister = new JPanel(new BorderLayout());
        panelRegister.setBackground(new Color(245, 240, 230));

        JPanel leftPanel = new JPanel();
        leftPanel.setBackground(new Color(121, 85, 72));
        leftPanel.setPreferredSize(new Dimension(300, 500));
        leftPanel.setLayout(new BoxLayout(leftPanel, BoxLayout.Y_AXIS));
        leftPanel.setBorder(new EmptyBorder(40, 10, 10, 10));

        ImageIcon icon = new ImageIcon(getClass().getResource("/assets/ALS.png"));
        JLabel lblLogo = new JLabel(icon, JLabel.CENTER);
        lblLogo.setAlignmentX(Component.CENTER_ALIGNMENT);
        lblLogo.setBorder(new EmptyBorder(40, 0, 15, 0));

        JLabel lbltittle = new JLabel("DAFTAR AKUN BARU", JLabel.CENTER);
        lbltittle.setForeground(Color.WHITE);
        lbltittle.setFont(new Font("Arial", Font.BOLD, 20));
        lbltittle.setAlignmentX(Component.CENTER_ALIGNMENT);
        lbltittle.setBorder(new EmptyBorder(0, 0, 0, 0));

        JLabel lblSubtittle = new JLabel("APLIKASI KRIPTOGRAFI AES 128-bit", JLabel.CENTER);
        lblSubtittle.setForeground(Color.WHITE);
        lblSubtittle.setFont(new Font("Arial", Font.BOLD, 12));
        lblSubtittle.setAlignmentX(Component.CENTER_ALIGNMENT);

        leftPanel.add(lblLogo);
        leftPanel.add(lbltittle);
        leftPanel.add(lblSubtittle);

        JPanel formPanel = new JPanel();
        formPanel.setBackground(new Color(245, 240, 230));
        formPanel.setLayout(new GridBagLayout());

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
        ImageIcon originalregister = new ImageIcon(getClass().getResource("/assets/register.png"));
        Image scaledImage = originalregister.getImage().getScaledInstance(24, 24, Image.SCALE_SMOOTH);
        ImageIcon scaledIcon = new ImageIcon(scaledImage);

        JLabel lblTitle = new JLabel("DAFTAR AKUN", scaledIcon, JLabel.LEFT);
        lblTitle.setIconTextGap(10); 
        
        lblTitle.setIconTextGap(10);
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 20));
        lblTitle.setForeground(new Color(121, 85, 72));
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
        formPanel.add(lblTitle, gbc);

        gbc.gridwidth = 1;
        gbc.gridx = 0; gbc.gridy++;
        formPanel.add(new JLabel("Username"), gbc);
        gbc.gridx = 1;
        txtUsernameRegister = new JTextField(20);
        formPanel.add(txtUsernameRegister, gbc);

        gbc.gridx = 0; gbc.gridy=2;
        formPanel.add(new JLabel("Password"), gbc);
        gbc.gridx = 1;

        JPanel panelPassword = new JPanel(new BorderLayout());
        txtPasswordRegister = new JPasswordField(20);
        JCheckBox chkShowPassword = new JCheckBox("Lihat Password");
        chkShowPassword.setBackground(new Color(245, 240, 230));

        chkShowPassword.addActionListener(e -> {
            if (chkShowPassword.isSelected()) {
                txtPasswordRegister.setEchoChar((char) 0);
            } else {
                txtPasswordRegister.setEchoChar('\u2022');
            }
        });

        panelPassword.setBackground(new Color(245, 240, 230));
        panelPassword.add(txtPasswordRegister, BorderLayout.CENTER);
        panelPassword.add(chkShowPassword, BorderLayout.EAST);

        formPanel.add(panelPassword, gbc);

        gbc.gridx = 0; gbc.gridy++;
        formPanel.add(new JLabel("Role"), gbc);

        gbc.gridx = 1;
        String[] roles = {"user", "admin"};
        comboRoleRegister = new JComboBox<>(roles);
        comboRoleRegister.setSelectedItem("user");
        formPanel.add(comboRoleRegister, gbc);

        comboRoleRegister.addItemListener(e -> {
            if (e.getStateChange() == ItemEvent.SELECTED) {
                String selected = (String) comboRoleRegister.getSelectedItem();
                if ("admin".equalsIgnoreCase(selected)) {
                    JOptionPane.showMessageDialog(this, "Role 'admin' hanya bisa dibuat oleh Admin melalui sistem.");
                    comboRoleRegister.setSelectedItem("user");
                }
            }
        });
            
        gbc.gridx = 1; gbc.gridy++;
        btnRegister = new JButton("REGISTER");
        btnRegister.setBackground(new Color(121, 85, 72));
        btnRegister.setForeground(Color.WHITE);
        formPanel.add(btnRegister, gbc);
        
        btnRegister.addActionListener(e -> {
            String username = txtUsernameRegister.getText();
            String password = new String(txtPasswordRegister.getPassword());
            String role = comboRoleRegister.getSelectedItem().toString();

            if (authController.registerUser(username, password, role)) {
                JOptionPane.showMessageDialog(this, "Pendaftaran berhasil! Silakan login.");
                cardLayout.show(cardPanel, "login");
            } else {
                JOptionPane.showMessageDialog(this, "Username sudah digunakan!");
            }
        });

        gbc.gridx = 1; gbc.gridy++;
        lblSwitchToLogin = new JLabel("<html><span style='color:#5A4A42;'>Sudah Punya Akun ? </span><a href='#' style='text-decoration:none;color:#1976D2;'>  LOGIN DI SINI </a></html>");
        lblSwitchToLogin.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        lblSwitchToLogin.setFont(new Font("Segoe UI", Font.BOLD, 12));
        lblSwitchToLogin.setForeground(new Color(90, 70, 60));
        formPanel.add(lblSwitchToLogin, gbc);

        lblSwitchToLogin.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                lblSwitchToLogin.setText("<html><span style='color:#5A4A42;'>Silahkan Login ? </span><a href='#' style='text-decoration:underline;color:#1976D2;'>  LOGIN DI SINI </a></html>");
            }

            @Override
            public void mouseExited(MouseEvent e) {
                lblSwitchToLogin.setText("<html><span style='color:#5A4A42;'>Sudah Punya Akun ? </span><a href='#' style='text-decoration:none;color:#1976D2;'> LOGIN DI SINI </a></html>");
            }

            @Override
            public void mouseClicked(MouseEvent e) {
                cardLayout.show(cardPanel, "login");
            }
        });

        panelRegister.add(leftPanel, BorderLayout.WEST);
        panelRegister.add(formPanel, BorderLayout.CENTER);
    }
    
    private String checkLogin(String username, String password) {
        try (Connection conn = DatabaseConnection.connect()) {
            String sql = "SELECT password, role FROM users WHERE username=?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, username);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                String hashedPassword = rs.getString("password");
                if (BCrypt.checkpw(password, hashedPassword)) {
                    loggedInUsername = username;
                    loggedInRole = rs.getString("role");
                    return loggedInRole;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public boolean isUserStillValid(String username, String role) {
        try (Connection conn = DatabaseConnection.connect()) {
            String sql = "SELECT role FROM users WHERE username = ?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, username);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                String dbRole = rs.getString("role");
                return dbRole.equalsIgnoreCase(role);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    private void saveRememberedUser(String username, String role) {
        try {
            File configFolder = new File("config");
            if (!configFolder.exists()) {
                configFolder.mkdirs();
            }

            long currentTime = System.currentTimeMillis(); // waktu sekarang
            try (FileWriter writer = new FileWriter(REMEMBER_FILE_PATH)) {
                writer.write(username + ":" + role + ":" + currentTime);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void clearRememberedUser() {
        File file = new File("config/remembered_user.txt");
        if (file.exists()) file.delete();
    }

    public void printRememberedUser() {
        String[] remembered = readRememberedUser();
        if (remembered != null) {
            System.out.println("Remembered Username: " + remembered[0]);
            System.out.println("Role: " + remembered[1]);
        } else {
            System.out.println("No remembered user.");
        }
    }

    public String[] readRememberedUser() {
        try (BufferedReader reader = new BufferedReader(new FileReader(REMEMBER_FILE_PATH))) {
            String line = reader.readLine();
            if (line != null && line.split(":").length == 3) {
                String[] parts = line.split(":");
                long savedTime = Long.parseLong(parts[2]);
                long currentTime = System.currentTimeMillis();

                if ((currentTime - savedTime) < 86400000) {
                    return new String[]{parts[0], parts[1]};
                }
            }
        } catch (IOException e) {
        }
        return null;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            LoginRegisterFrame loginFrame = new LoginRegisterFrame();

            String[] remembered = loginFrame.readRememberedUser();
            if (remembered != null && remembered.length == 2) {
                String username = remembered[0];
                String role = remembered[1];

            if (loginFrame.isUserStillValid(username, role)) {
                MenuFrame mainFrame = new MenuFrame(username, role);
                mainFrame.setLocationRelativeTo(null);
                mainFrame.setVisible(true);
            } else {
                loginFrame.clearRememberedUser();
                loginFrame.setLocationRelativeTo(null);
                loginFrame.setVisible(true);
            }
            } else {
                loginFrame.setLocationRelativeTo(null); 
                loginFrame.setVisible(true);
            }
        });
    }
}