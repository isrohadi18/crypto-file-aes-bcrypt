package view;

import config.DatabaseConnection;
import org.mindrot.jbcrypt.BCrypt;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.sql.*;

public class UserManagementFrame extends JFrame {
    private JTable table;
    private DefaultTableModel model;
    private JLabel lblTotalUser;
    private JTextField tfCari;
    private JComboBox<String> cbRoleFilter;


    public UserManagementFrame() {
       setTitle("Manajemen User");
       setSize(700, 400);
       setLocationRelativeTo(null);
       setDefaultCloseOperation(DISPOSE_ON_CLOSE);
       setLayout(new BorderLayout());

       model = new DefaultTableModel(new Object[]{"ID", "Username", "Role"}, 0);
       table = new JTable(model);

       JScrollPane scrollPane = new JScrollPane(table);

       JPanel mainPanel = new JPanel(new BorderLayout());
       mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 35, 20, 35));

       JPanel centerPanel = new JPanel(new BorderLayout());
       centerPanel.add(new JScrollPane(table), BorderLayout.CENTER);

       JPanel topPanel = new JPanel(new BorderLayout());

       JPanel filterPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
       tfCari = new JTextField(15);
       filterPanel.add(new JLabel("Cari Username: "));
       filterPanel.add(tfCari);

       cbRoleFilter = new JComboBox<>(new String[]{"Semua", "admin", "user"});
       filterPanel.add(new JLabel("Role: "));
       filterPanel.add(cbRoleFilter);
       cbRoleFilter.addActionListener(e -> loadUsers());

       topPanel.add(filterPanel, BorderLayout.WEST);

       JPanel totalPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
       lblTotalUser = new JLabel("TOTAL USER : 0");
       lblTotalUser.setFont(new Font("Segoe UI", Font.BOLD, 24));
       lblTotalUser.setForeground(new Color(0, 102, 204));
       totalPanel.add(lblTotalUser);
       topPanel.add(totalPanel, BorderLayout.EAST);
                        
       JPanel buttonPanel = new JPanel();
       
       JButton btnTambah = new JButton("Tambah");
        buttonPanel.add(btnTambah);
        btnTambah.addActionListener(e -> {
            JTextField tfUsername = new JTextField();
            JPasswordField tfPassword = new JPasswordField();
            JComboBox<String> cbRole = new JComboBox<>(new String[]{"admin", "user"});

            JPanel inputPanel = new JPanel(new GridLayout(0, 1));
            inputPanel.add(new JLabel("Username:"));
            inputPanel.add(tfUsername);
            inputPanel.add(new JLabel("Password:"));
            inputPanel.add(tfPassword);
            inputPanel.add(new JLabel("Role:"));
            inputPanel.add(cbRole);

            int result = JOptionPane.showConfirmDialog(this, inputPanel, "Tambah User", JOptionPane.OK_CANCEL_OPTION);
            if (result == JOptionPane.OK_OPTION) {
                String username = tfUsername.getText().trim();
                String password = new String(tfPassword.getPassword()).trim();
                String role = cbRole.getSelectedItem().toString();

                if (username.isEmpty() || password.isEmpty()) {
                    JOptionPane.showMessageDialog(this, "Username dan Password tidak boleh kosong.");
                    return;
                }

                try (Connection conn = DatabaseConnection.connect()) {
                    PreparedStatement checkStmt = conn.prepareStatement("SELECT COUNT(*) FROM users WHERE username = ?");
                    checkStmt.setString(1, username);
                    ResultSet rs = checkStmt.executeQuery();
                    if (rs.next() && rs.getInt(1) > 0) {
                        JOptionPane.showMessageDialog(this, "Username sudah digunakan. Gunakan yang lain.");
                        return;
                    }

                    String hashedPassword = BCrypt.hashpw(password, BCrypt.gensalt());
                    String sql = "INSERT INTO users (username, password, role) VALUES (?, ?, ?)";
                    PreparedStatement ps = conn.prepareStatement(sql);
                    ps.setString(1, username);
                    ps.setString(2, hashedPassword);
                    ps.setString(3, role);
                    ps.executeUpdate();
                    JOptionPane.showMessageDialog(this, "User berhasil ditambahkan!");
                    loadUsers();
                } catch (SQLException ex) {
                    JOptionPane.showMessageDialog(this, "Gagal tambah user: " + ex.getMessage());
                }
            }
        });

        JButton btnEdit = new JButton("Edit");
        buttonPanel.add(btnEdit);
        btnEdit.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row == -1) {
                JOptionPane.showMessageDialog(this, "Pilih user terlebih dahulu.");
                return;
            }

            String id = table.getValueAt(row, 0).toString();
            String currentUsername = table.getValueAt(row, 1).toString();
            String currentRole = table.getValueAt(row, 2).toString();

            JTextField tfUsername = new JTextField(currentUsername);
            JComboBox<String> cbRole = new JComboBox<>(new String[]{"admin", "user"});
            cbRole.setSelectedItem(currentRole);

            JCheckBox chkUbahPassword = new JCheckBox("Ubah Password");
            JPasswordField tfPasswordBaru = new JPasswordField();
            tfPasswordBaru.setEnabled(false);

            chkUbahPassword.addActionListener(evt -> tfPasswordBaru.setEnabled(chkUbahPassword.isSelected()));

            JPanel inputPanel = new JPanel(new GridLayout(0, 1));
            inputPanel.add(new JLabel("Username:"));
            inputPanel.add(tfUsername);
            inputPanel.add(new JLabel("Role:"));
            inputPanel.add(cbRole);
            inputPanel.add(chkUbahPassword);
            inputPanel.add(new JLabel("Password Baru (jika diubah):"));
            inputPanel.add(tfPasswordBaru);

            int result = JOptionPane.showConfirmDialog(this, inputPanel, "Edit User", JOptionPane.OK_CANCEL_OPTION);
            if (result == JOptionPane.OK_OPTION) {
                String usernameBaru = tfUsername.getText().trim();
                String roleBaru = cbRole.getSelectedItem().toString();

                if (usernameBaru.isEmpty()) {
                    JOptionPane.showMessageDialog(this, "Username tidak boleh kosong.");
                    return;
                }

                try (Connection conn = DatabaseConnection.connect()) {
                    PreparedStatement checkStmt = conn.prepareStatement("SELECT COUNT(*) FROM users WHERE username = ? AND id != ?");
                    System.out.println("ID user yang diedit: " + id);
                    System.out.println("Username baru: " + usernameBaru);
                    checkStmt.setString(1, usernameBaru);
                    checkStmt.setString(2, id);
                    ResultSet rs = checkStmt.executeQuery();
                    if (rs.next() && rs.getInt(1) > 0) {
                        JOptionPane.showMessageDialog(this, "Username sudah digunakan oleh user lain.");
                        return;
                    }

                    if (chkUbahPassword.isSelected()) {
                        String passBaru = new String(tfPasswordBaru.getPassword()).trim();
                        if (passBaru.isEmpty()) {
                            JOptionPane.showMessageDialog(this, "Password baru tidak boleh kosong.");
                            return;
                        }
                        String hashedBaru = BCrypt.hashpw(passBaru, BCrypt.gensalt());
                        String sql = "UPDATE users SET username=?, role=?, password=? WHERE id=?";
                        PreparedStatement ps = conn.prepareStatement(sql);
                        ps.setString(1, usernameBaru);
                        ps.setString(2, roleBaru);
                        ps.setString(3, hashedBaru);
                        ps.setString(4, id);
                        ps.executeUpdate();
                    } else {
                        String sql = "UPDATE users SET username=?, role=? WHERE id=?";
                        PreparedStatement ps = conn.prepareStatement(sql);
                        ps.setString(1, usernameBaru);
                        ps.setString(2, roleBaru);
                        ps.setString(3, id);
                        ps.executeUpdate();
                    }

                    JOptionPane.showMessageDialog(this, "User berhasil diupdate!");
                    loadUsers();
                    table.repaint();

                } catch (SQLException ex) {
                    JOptionPane.showMessageDialog(this, "Gagal edit user: " + ex.getMessage());
                    ex.printStackTrace();
                }
            }
        });

        JButton btnHapus = new JButton("Hapus");
        buttonPanel.add(btnHapus);
        btnHapus.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row == -1) {
                JOptionPane.showMessageDialog(this, "Pilih user terlebih dahulu.");
                return;
            }

            String id = table.getValueAt(row, 0).toString();
            String username = table.getValueAt(row, 1).toString();

            if (username.equalsIgnoreCase("admin")) {
                JOptionPane.showMessageDialog(this, "Akun admin utama tidak boleh dihapus.");
                return;
            }

            int confirm = JOptionPane.showConfirmDialog(this, "Hapus user \"" + username + "\"?", "Konfirmasi", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                try (Connection conn = DatabaseConnection.connect()) {
                    String sql = "DELETE FROM users WHERE id=?";
                    PreparedStatement ps = conn.prepareStatement(sql);
                    ps.setString(1, id);
                    ps.executeUpdate();
                    JOptionPane.showMessageDialog(this, "User berhasil dihapus!");
                    loadUsers(); 
                } catch (SQLException ex) {
                    JOptionPane.showMessageDialog(this, "Gagal hapus user: " + ex.getMessage());
                }
            }
        });

        JButton btnResetPassword = new JButton("Reset Password");
        buttonPanel.add(btnResetPassword);
        btnResetPassword.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row == -1) {
                JOptionPane.showMessageDialog(this, "Pilih user terlebih dahulu.");
                return;
            }

            String id = table.getValueAt(row, 0).toString();
            String username = table.getValueAt(row, 1).toString();

            JPasswordField tfNewPassword = new JPasswordField();

            JPanel inputPanel = new JPanel(new GridLayout(0, 1));
            inputPanel.add(new JLabel("Reset password untuk: " + username));
            inputPanel.add(new JLabel("Password Baru:"));
            inputPanel.add(tfNewPassword);

            int result = JOptionPane.showConfirmDialog(this, inputPanel, "Reset Password", JOptionPane.OK_CANCEL_OPTION);
            if (result == JOptionPane.OK_OPTION) {
                String newPassword = new String(tfNewPassword.getPassword());

                if (newPassword.trim().isEmpty()) {
                    JOptionPane.showMessageDialog(this, "Password tidak boleh kosong!");
                    return;
                }

                String hashedPassword = org.mindrot.jbcrypt.BCrypt.hashpw(newPassword, org.mindrot.jbcrypt.BCrypt.gensalt());

                try (Connection conn = DatabaseConnection.connect()) {
                    String sql = "UPDATE users SET password=? WHERE id=?";
                    PreparedStatement ps = conn.prepareStatement(sql);
                    ps.setString(1, hashedPassword);
                    ps.setString(2, id);
                    ps.executeUpdate();
                    JOptionPane.showMessageDialog(this, "Password berhasil di-reset!");
                } catch (SQLException ex) {
                    JOptionPane.showMessageDialog(this, "Gagal reset password: " + ex.getMessage());
                }
            }
        });
        
        mainPanel.add(topPanel, BorderLayout.NORTH);
        mainPanel.add(centerPanel, BorderLayout.CENTER);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);
        add(mainPanel);
        
        tfCari.addKeyListener(new KeyAdapter() {
            public void keyReleased(KeyEvent e) {
                loadUsers();
            }
        });
        loadUsers();
    }
        
    private void loadUsers() {
        model.setRowCount(0);
        int total = 0;
        try (Connection conn = DatabaseConnection.connect()) {
            String keyword = tfCari.getText().trim();
            String selectedRole = cbRoleFilter.getSelectedItem().toString();

            String sql = "SELECT id, username, role FROM users WHERE username LIKE ?";
            if (!selectedRole.equals("Semua")) {
                sql += " AND role = ?";
            }
            sql += " ORDER BY id";

            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, "%" + keyword + "%");
            if (!selectedRole.equals("Semua")) {
                ps.setString(2, selectedRole);
            }

            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                model.addRow(new Object[]{
                    rs.getInt("id"),
                    rs.getString("username"),
                    rs.getString("role")
                });
                total++;
            }
            lblTotalUser.setText("TOTAL USER : " + total);
            model.fireTableDataChanged();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Gagal memuat data user: " + e.getMessage());
        }
    }
}