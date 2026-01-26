package view;

import controller.MainController;
import test.Statistik_Grafik_AES;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.time.LocalDate;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;


public class MenuFrame extends JFrame {
    private String username;
    private String role;
    private String currentUsername;
    private String currentRole;
    private JLabel lblLoginInfo;
    private JTextField txtTanggal;
    private JTextField lblFilePath;
    private JPasswordField txtPassword;
    private JTextArea txtKeterangan;
    private JTextField txtCari;
    private JComboBox<String> cmbStatus;
    private JTable tableLog;
    private MainController controller;
    private JButton btnHapusLog;
    private JButton btnExportPDF;
    private JButton btnUserManagement;
    private JButton btnGrafikPerforma;
    private JButton btnStatistik;
    private File selectedFile;

    
    private JPanel createStatistikPanel() {
        JPanel panel = new JPanel(new GridLayout(1, 3, 20, 10));
        panel.setBackground(new Color(245, 240, 230));
        panel.setBorder(BorderFactory.createTitledBorder("Statistik Penggunaan Aplikasi"));
        panel.setBackground(Color.WHITE);

        int totalEnkripsi = controller.countLogByStatus("Enkripsi");
        int totalDekripsi = controller.countLogByStatus("Dekripsi");
        int totalFile = totalEnkripsi + totalDekripsi;

        JPanel enkripsiPanel = createStatBox("Enkripsi", totalEnkripsi, new Color(70, 130, 180));
        JPanel dekripsiPanel = createStatBox("Dekripsi", totalDekripsi, new Color(34, 139, 34));
        JPanel totalPanel   = createStatBox("Total File", totalFile, new Color(255, 140, 0));

        panel.add(enkripsiPanel);
        panel.add(dekripsiPanel);
        panel.add(totalPanel);

        return panel;
    }
    
    private void refreshStatistikPanel() {
        JPanel newStatistikPanel = createStatistikPanel();
        getContentPane().remove(getContentPane().getComponentCount() - 1);
        getContentPane().add(newStatistikPanel, BorderLayout.SOUTH); 
        revalidate();
        repaint();
    }

    private JPanel createStatBox(String label, int value, Color color) {
        JPanel box = new JPanel(new BorderLayout());
        box.setBorder(BorderFactory.createLineBorder(color, 2));
        box.setBackground(Color.WHITE);

        JLabel lblTitle = new JLabel(label, SwingConstants.CENTER);
        lblTitle.setFont(new Font("Arial", Font.BOLD, 14));
        lblTitle.setForeground(color);

        JLabel lblValue = new JLabel(String.valueOf(value), SwingConstants.CENTER);
        lblValue.setFont(new Font("Arial", Font.BOLD, 28));
        lblValue.setForeground(color.darker());

        box.add(lblTitle, BorderLayout.NORTH);
        box.add(lblValue, BorderLayout.CENTER);
        return box;
    }

    public MenuFrame(String username, String role) {
        this.currentUsername = username;
        this.currentRole = role;

        setTitle("Aplikasi Kriptografi File - AES 128 Bit | Login sebagai: " + username + " (" + role + ")");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        
        initComponents();
        setLocationRelativeTo(null);
        pack();
        
        addComponentListener(new java.awt.event.ComponentAdapter() {
            @Override
            public void componentResized(java.awt.event.ComponentEvent e) {
                tableLog.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
            }
        });
        txtPassword.requestFocusInWindow();
    }

    private void initComponents() {
        setTitle("Menu Utama - " + currentUsername.toUpperCase() + " [" + currentRole.toUpperCase() + "]");
        setPreferredSize(new Dimension(850, 650));
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new FlowLayout());
        pack(); 
        
        new File("file_encrypt").mkdirs();
        new File("file_decrypt").mkdirs();
        new File("file_upload").mkdirs();
        new File("backup_log").mkdirs();
        
        txtTanggal = new JTextField(LocalDate.now().toString());
        txtTanggal.setEditable(false);
        
        lblFilePath = new JTextField("Belum ada file diupload");
        lblFilePath.setEditable(false);
        lblFilePath.setBorder(null);
        lblFilePath.setBackground(null);
        lblFilePath.setFocusable(false);
        
        txtPassword = new JPasswordField();
        txtPassword.setToolTipText("Masukkan password bebas, tidak harus 16 karakter");
        JCheckBox chkLihatPassword = new JCheckBox("Lihat Password");
        chkLihatPassword.addActionListener(e -> {
            if (chkLihatPassword.isSelected()) {
                txtPassword.setEchoChar((char) 0); 
            } else {
                txtPassword.setEchoChar('\u2022');
            }
        });

        txtKeterangan = new JTextArea(3, 20);
        JScrollPane scrollKeterangan = new JScrollPane(txtKeterangan);
        scrollKeterangan.setPreferredSize(new Dimension(530, 60));

        tableLog = new JTable(new DefaultTableModel(
            new Object[]{"Nama File Asli", "Nama File Sumber", "Ukuran (KB)", "Status", "Tanggal", "Keterangan"}, 0
        ));
        
        tableLog.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
        tableLog.getColumnModel().getColumn(0).setPreferredWidth(150);
        tableLog.getColumnModel().getColumn(1).setPreferredWidth(300);
        tableLog.getColumnModel().getColumn(2).setPreferredWidth(100);
        tableLog.getColumnModel().getColumn(3).setPreferredWidth(80);
        tableLog.getColumnModel().getColumn(4).setPreferredWidth(150);
        tableLog.getColumnModel().getColumn(5).setPreferredWidth(200);
        
        JScrollPane scrollTable = new JScrollPane(tableLog);
        scrollTable.setPreferredSize(null); 
        scrollTable.setMinimumSize(new Dimension(0, 300));
        
        controller = new MainController(txtTanggal, lblFilePath, txtPassword, txtKeterangan, tableLog);
        controller.loadLog();

        tableLog.addMouseListener(new java.awt.event.MouseAdapter() {    
        public void mouseClicked(java.awt.event.MouseEvent evt) {
            int selectedRow = tableLog.getSelectedRow();
            if (selectedRow >= 0) {
                String namaAsli = tableLog.getValueAt(selectedRow, 0).toString(); 
                String status = tableLog.getValueAt(selectedRow, 3).toString(); 
                String namaSumber = controller.getFileNameFromDBByNamaAsli(namaAsli, status);
                if (namaSumber == null) {
                JOptionPane.showMessageDialog(null, "File log tidak ditemukan di database.");
                return;
            }
            File fileEncrypt = new File("file_encrypt/" + namaSumber);
            File fileDecrypt = new File("file_decrypt/" + namaSumber);
            File selectedFile = fileEncrypt.exists() ? fileEncrypt : (fileDecrypt.exists() ? fileDecrypt : null);

            if (selectedFile != null && selectedFile.exists()) {
                lblFilePath.setText(selectedFile.getAbsolutePath());
                JOptionPane.showMessageDialog(null, "File \"" + namaAsli + "\" berhasil dimuat ke form.");
            } else {
                JOptionPane.showMessageDialog(null, "File tidak ditemukan di folder penyimpanan.");
            }
            }
        }
        });

        JButton btnEncrypt = new JButton("ENKRIPSI FILE");
        btnEncrypt.addActionListener(e -> {
            controller.encryptFile();
            refreshStatistikPanel();
        });
       
        JButton btnDecrypt = new JButton("DEKRIPSI FILE");
        btnDecrypt.addActionListener(e -> {

            controller.showFileDetailsBeforeDecrypt();

            int confirm = JOptionPane.showConfirmDialog(null, "Lanjutkan dekripsi file ini?", "Konfirmasi", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                controller.decryptFile();
                refreshStatistikPanel();
            }
        });

        JButton btnUpload = new JButton("UPLOAD FILE");
        btnUpload.addActionListener(e -> {
            JFileChooser chooser = new JFileChooser();
            int option = chooser.showOpenDialog(this);
            if (option == JFileChooser.APPROVE_OPTION) {
                File selectedFile = chooser.getSelectedFile();
                
                try {
                    File dest = new File("file_upload/" + selectedFile.getName());
                    dest.getParentFile().mkdirs(); // Buat folder jika belum ada
                    Files.copy(selectedFile.toPath(), dest.toPath(), StandardCopyOption.REPLACE_EXISTING);

                    lblFilePath.setText(dest.getAbsolutePath());

                    JOptionPane.showMessageDialog(this, "File berhasil di-upload!");
                } catch (IOException ex) {
                    JOptionPane.showMessageDialog(this, "Gagal mengunggah file: " + ex.getMessage());
                }
            }
        });

        JButton btnPreview = new JButton("PREVIEW FILE");
        btnPreview.addActionListener(e -> {
            try {
                String path = lblFilePath.getText();

                if (path == null || path.trim().isEmpty() || path.equals("[Belum ada file dipilih]")) {
                    JOptionPane.showMessageDialog(null, "Silakan pilih atau upload file terlebih dahulu sebelum preview.");
                    return;
                }
                
                File file = new File(path);
                if (file.exists() && Desktop.isDesktopSupported()) {
                    Desktop.getDesktop().open(file);
                } else {
                    JOptionPane.showMessageDialog(null, "File tidak ditemukan atau tidak dapat dibuka di sistem ini!");
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(null, "Gagal membuka file: " + ex.getMessage());
            }
        });

        JButton btnReset = new JButton("RESET FORM");
        btnReset.addActionListener(e -> {
            lblFilePath.setText("Belum ada file diupload");
            txtPassword.setText("");
            txtKeterangan.setText("");
            txtCari.setText("");        
            cmbStatus.setSelectedIndex(0); 
            txtPassword.requestFocus(); 
        });
        
        btnHapusLog = new JButton("HAPUS FILE");
        btnHapusLog.addActionListener(e -> {
            if (!currentRole.equalsIgnoreCase("admin")) {
                showAccessDenied();
                return;
            }
            controller.deleteSelectedLog();
        });

        
        btnExportPDF = new JButton("EXPORT TABEL FILE (PDF)");
        btnExportPDF.addActionListener(e -> {
            if (!currentRole.equalsIgnoreCase("admin")) {
                showAccessDenied();
                return;
            }
            controller.exportLogToPDF();
        });

        btnStatistik = new JButton("Statistik");
        btnStatistik.addActionListener(e -> {
            if (!currentRole.equalsIgnoreCase("admin")) {
                showAccessDenied();
                return;
            }
            new StatistikOptionFrame(controller).setVisible(true);
        });



        lblFilePath.setPreferredSize(new Dimension(400, 20));

        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(new Color(245, 240, 230)); 
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 10, 5, 10);
        gbc.anchor = GridBagConstraints.WEST;

        String safeRole = (currentRole != null) ? currentRole : "user";

        String iconPath = safeRole.equalsIgnoreCase("admin") ? "/assets/admin.png" : "/assets/user.png";
        String displayRole = safeRole.equalsIgnoreCase("admin") ? "ADMIN" : "USER";

        ImageIcon roleIcon = new ImageIcon(getClass().getResource(iconPath));
        Image img = roleIcon.getImage().getScaledInstance(18, 18, Image.SCALE_SMOOTH);
        roleIcon = new ImageIcon(img);

        JLabel lblUser = new JLabel(" LOGIN SEBAGAI " + displayRole + " - " + currentUsername, roleIcon, JLabel.LEFT);
        lblUser.setFont(new Font("Arial", Font.BOLD, 14));

        if (safeRole.equalsIgnoreCase("admin")) {
            lblUser.setForeground(new Color(235, 51, 36)); 
        } else {
            lblUser.setForeground(new Color(0, 35, 245));
        }
      
        JPanel topHeaderPanel = new JPanel(new BorderLayout());
        topHeaderPanel.setBackground(new Color(245, 240, 230)); 
        topHeaderPanel.setOpaque(false);

        topHeaderPanel.add(lblUser, BorderLayout.WEST);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 0));
        buttonPanel.setBackground(new Color(245, 240, 230)); 
        buttonPanel.setOpaque(false);
        
        JButton btnUserManagement = new JButton("Manajemen User");
        btnUserManagement.setBackground(new Color(90, 70, 60));
        btnUserManagement.setForeground(Color.WHITE);
        btnUserManagement.setFont(new Font("Arial", Font.BOLD, 12));
        btnUserManagement.addActionListener(e -> {
            if (!currentRole.equalsIgnoreCase("admin")) {
                showAccessDenied();
                return;
            }
            new UserManagementFrame().setVisible(true);
        });

        JButton btnLogout = new JButton("LOGOUT");
        btnLogout.setBackground(new Color(255, 0, 0));
        btnLogout.setForeground(Color.WHITE);
        btnUserManagement.setFont(new Font("Arial", Font.BOLD, 12));
        btnLogout.addActionListener(e -> {
            int confirm = JOptionPane.showConfirmDialog(this,
                "Apakah Anda yakin ingin logout?", "Konfirmasi Logout",
                JOptionPane.YES_NO_OPTION);

            if (confirm == JOptionPane.YES_OPTION) {
                dispose();
                new LoginRegisterFrame().setVisible(true);
            }
        });
        
        buttonPanel.add(btnUserManagement);
        buttonPanel.add(Box.createHorizontalStrut(15)); 
        buttonPanel.add(btnLogout);

        topHeaderPanel.add(buttonPanel, BorderLayout.EAST);

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 3;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        formPanel.add(topHeaderPanel, gbc);
        
        gbc.gridy = 1; 
        gbc.gridwidth = 1;
        gbc.gridx = 0;
        formPanel.add(new JLabel("Tanggal:"), gbc);
        
        gbc.gridx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        formPanel.add(txtTanggal, gbc);
        txtPassword.setEchoChar('\u2022'); 

        gbc.gridx = 0;
        gbc.gridy = 2;
        formPanel.add(new JLabel("File Path:"), gbc);
        gbc.gridx = 1;
        formPanel.add(lblFilePath, gbc);
        gbc.gridx = 2;
        formPanel.add(btnUpload, gbc);

        gbc.gridx = 0; gbc.gridy++;
        formPanel.add(new JLabel("Password (bebas jumlah karakter):"), gbc);
        gbc.gridx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        formPanel.add(txtPassword, gbc);
        gbc.gridx = 2;
        gbc.fill = GridBagConstraints.NONE;
        formPanel.add(chkLihatPassword, gbc);
        chkLihatPassword.setBackground(new Color(245, 240, 230));

        gbc.gridx = 0; gbc.gridy++;
        gbc.anchor = GridBagConstraints.NORTHWEST;
        formPanel.add(new JLabel("Keterangan:"), gbc);
        gbc.gridx = 1;
        gbc.gridwidth = 2;
        formPanel.add(scrollKeterangan, gbc);
        formPanel.setBackground(new Color(245, 240, 230));

        JPanel actionPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        actionPanel.setBackground(new Color(245, 240, 230)); 
        actionPanel.add(btnPreview);
        actionPanel.add(btnEncrypt);
        actionPanel.add(btnDecrypt);
        actionPanel.add(btnReset);

        JPanel searchPanel = new JPanel(new BorderLayout());
        searchPanel.setBackground(new Color(245, 240, 230));
        JPanel searchForm = new JPanel(new FlowLayout(FlowLayout.LEFT));
        searchForm.setBackground(new Color(245, 240, 230)); 
        txtCari = new JTextField(20);
        cmbStatus = new JComboBox<>(new String[]{"Semua", "Enkripsi", "Dekripsi"});
        
        searchForm.add(new JLabel("Cari Nama File"));
        searchForm.add(txtCari);
        searchForm.add(cmbStatus);
        searchForm.add(btnHapusLog);
        searchForm.add(btnExportPDF);
        searchForm.add(btnStatistik);
                
        searchPanel.add(searchForm, BorderLayout.WEST);
        formPanel.setBackground(new Color(245, 240, 230));

        addSearchKeyListener();

        JPanel atasPanel = new JPanel();
        atasPanel.setBackground(new Color(245, 240, 230));
        atasPanel.setLayout(new BoxLayout(atasPanel, BoxLayout.Y_AXIS));
        atasPanel.add(formPanel);
        atasPanel.add(actionPanel);

        JPanel middlePanel = new JPanel(new BorderLayout());
        middlePanel.setBackground(new Color(245, 240, 230));
        middlePanel.add(scrollTable, BorderLayout.CENTER);

        JPanel tabelSection = new JPanel();
        tabelSection.setBackground(new Color(245, 240, 230));
        tabelSection.setLayout(new BoxLayout(tabelSection, BoxLayout.Y_AXIS));

        JLabel lblJudulTabel = new JLabel("TABEL HASIL FILE ENKRIPSI DAN DEKRIPSI", SwingConstants.CENTER);
        lblJudulTabel.setAlignmentX(Component.CENTER_ALIGNMENT); 
        lblJudulTabel.setFont(new Font("arial", Font.BOLD, 14));

        tabelSection.add(Box.createVerticalStrut(10));
        tabelSection.add(lblJudulTabel);
        tabelSection.add(Box.createVerticalStrut(2));
        tabelSection.add(searchPanel); 
        tabelSection.add(scrollTable);
        middlePanel.add(tabelSection, BorderLayout.CENTER);
        middlePanel.setBackground(new Color(245, 240, 230));

        JPanel mainPanel = new JPanel(new BorderLayout(0, 0));
        JPanel middleWrapper = new JPanel(new BorderLayout());
        middleWrapper.setBackground(new Color(245, 240, 230)); 
        middleWrapper.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0)); 
        middleWrapper.add(middlePanel, BorderLayout.CENTER);

        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10)); 
        mainPanel.add(atasPanel, BorderLayout.NORTH);        
        mainPanel.add(middleWrapper, BorderLayout.CENTER);   
        JPanel statistikPanel = createStatistikPanel();
        mainPanel.add(statistikPanel, BorderLayout.SOUTH);

        setContentPane(mainPanel);
        mainPanel.setBackground(new Color(245, 240, 230)); 
    }
    
    private void showPerformanceChart() {
        try {
            Statistik_Grafik_AES chartViewer = new Statistik_Grafik_AES("hasil_pengujian/hasil_pengujian_aes_cbc.csv");
            chartViewer.setVisible(true);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Gagal menampilkan grafik performa: " + ex.getMessage());
            ex.printStackTrace();
        }
    }
    
   private void showAccessDenied() {
        JOptionPane.showMessageDialog(this,
            "Akses ditolak! Fitur ini hanya tersedia untuk admin.",
            "Akses Ditolak", JOptionPane.WARNING_MESSAGE);
    }

    private void addSearchKeyListener() {
        txtCari.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                controller.filterLog(txtCari.getText(), (String) cmbStatus.getSelectedItem());
            }
        });

        cmbStatus.addActionListener(e -> {
            controller.filterLog(txtCari.getText(), (String) cmbStatus.getSelectedItem());
        });
    }     
}