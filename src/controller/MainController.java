package controller;

import aes.AESUtils;
import config.DatabaseConnection;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.File;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.Vector;
import java.io.FileOutputStream;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;


public class MainController {
    private JTextField txtTanggal;
    private JTextField lblFilePath;
    private JPasswordField txtPassword;
    private JTextArea txtKeterangan;
    private JTable table;

    public MainController(JTextField txtTanggal, JTextField lblFilePath,
                        JPasswordField txtPassword, JTextArea txtKeterangan,
                        JTable table) {
        this.txtTanggal = txtTanggal;
        this.lblFilePath = lblFilePath;
        this.txtPassword = txtPassword;
        this.txtKeterangan = txtKeterangan;
        this.table = table;
    }

    public void encryptFile() {
    try {

        String password = new String(txtPassword.getPassword());
        if (password.length() < 1) {
            JOptionPane.showMessageDialog(null, "Password tidak boleh kosong!");
            return;
        }

        String filePath = lblFilePath.getText();
        if (filePath == null || filePath.equals("[Belum ada file dipilih]")) {
            JOptionPane.showMessageDialog(null, "Silakan upload file terlebih dahulu!");
            return;
        }

        File source = new File(filePath);
        if (!source.exists()) {
            JOptionPane.showMessageDialog(null, "File tidak ditemukan!");
            return;
        }
        
        if (isDuplicateFile(source.getName(), "Enkripsi")) {
            JOptionPane.showMessageDialog(null, "File dengan nama ini sudah pernah dienkripsi!");
            return;
        }

        String encryptedFileName = System.currentTimeMillis() + "-" + source.getName() + ".aes";
        File dest = new File("file_encrypt/" + encryptedFileName);
        dest.getParentFile().mkdirs(); 


        AESUtils.encrypt(source, dest, password);

        saveLog(source.getName(), dest.getName(), "Enkripsi", txtKeterangan.getText());

        File sampleFolder = new File("hasil_pengujian/sample/");
        if (!sampleFolder.exists()) {
            sampleFolder.mkdirs();
        }
        File targetSample = new File(sampleFolder, source.getName());
        if (!targetSample.exists()) {
            Files.copy(source.toPath(), targetSample.toPath(), StandardCopyOption.REPLACE_EXISTING);
            System.out.println("File disalin ke folder sample/: " + targetSample.getName());
        } else {
            System.out.println("File sudah ada di folder sample/: " + targetSample.getName());
        }
        
        loadLog();
        
        lblFilePath.setText("[Belum ada file dipilih]");
        txtPassword.setText("");
        txtKeterangan.setText("");

        JOptionPane.showMessageDialog(null, "Berkas berhasil dienkripsi dan disimpan!");
    } catch (Exception e) {
        e.printStackTrace();
        JOptionPane.showMessageDialog(null, "Gagal enkripsi: " + e.getMessage());
    }
    }

    public void decryptFile() {
        try {
            String password = txtPassword.getText();
            if (password.length() < 1) {
                JOptionPane.showMessageDialog(null, "Password tidak boleh kosong!");
                return;
            }
            File source = new File(lblFilePath.getText());
            
            if (!source.getName().endsWith(".aes")) {
                JOptionPane.showMessageDialog(null, "File ini bukan file terenkripsi (.aes) yang valid!");
                return;
            }
            
            String namaAsli = getOriginalFileName(source.getName());
            if (namaAsli == null) {
                JOptionPane.showMessageDialog(null, "Nama file asli tidak ditemukan di log!");
                return;
            }
            
            File dest = new File("file_decrypt/" + namaAsli);
            dest.getParentFile().mkdirs(); 
            AESUtils.decrypt(source, dest, password);
            
            saveLog(namaAsli, dest.getName(), "Dekripsi", txtKeterangan.getText());

            File sampleFolder = new File("hasil_pengujian/sample/");
            if (!sampleFolder.exists()) {
                sampleFolder.mkdirs();
            }

            File targetSample = new File(sampleFolder, source.getName());
            Files.copy(source.toPath(), targetSample.toPath(), StandardCopyOption.REPLACE_EXISTING);
            System.out.println("File disalin ke folder sample/: " + targetSample.getName());

            loadLog();

            lblFilePath.setText("[Belum ada file dipilih]");
            txtPassword.setText("");
            txtKeterangan.setText("");
            
            JOptionPane.showMessageDialog(null, "Berkas berhasil didekripsi sebagai: " + namaAsli);
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Gagal dekripsi!");
        }
    }
    
    public void showFileDetailsBeforeDecrypt() {
        File file = new File(lblFilePath.getText());
        if (!file.exists()) {
            JOptionPane.showMessageDialog(null, "File tidak ditemukan!");
            return;
        }

        try (Connection conn = DatabaseConnection.connect()) {
            String sql = "SELECT * FROM log_file WHERE nama_sumber = ? AND status = 'Enkripsi' ORDER BY tanggal DESC LIMIT 1";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, file.getName());
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                String namaAsli = rs.getString("nama_asli");
                String namaEnkripsi = rs.getString("nama_sumber");
                String status = rs.getString("status");
                String tanggal = rs.getTimestamp("tanggal").toString();
                String keterangan = rs.getString("keterangan");

                long ukuranKB = file.length() / 1024;

                new view.FileDetailDialog(null, namaEnkripsi, namaAsli, ukuranKB, tanggal, keterangan).setVisible(true);
            } else {
                JOptionPane.showMessageDialog(null, "Data log file tidak ditemukan di database!");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void loadLog() {
        DefaultTableModel model = (DefaultTableModel) table.getModel();
        model.setRowCount(0);
        try (Connection conn = DatabaseConnection.connect();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT * FROM log_file ORDER BY tanggal DESC")) {
            while (rs.next()) {
                Vector<Object> row = new Vector<>();
                String namaAsli = rs.getString("nama_asli");
                String namaSumber = rs.getString("nama_sumber");

                File fileEncrypted = new File("file_encrypt/" + namaSumber);
                File fileDecrypted = new File("file_decrypt/" + namaSumber);
                File file = fileEncrypted.exists() ? fileEncrypted : fileDecrypted;
                long sizeKB = file.exists() ? file.length() / 1024 : 0;
                row.add(namaAsli); 
                row.add(namaSumber); 
                row.add(sizeKB + " KB"); 
                row.add(rs.getString("status"));
                row.add(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(rs.getTimestamp("tanggal"))); 
                row.add(rs.getString("keterangan")); 
                model.addRow(row);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    private String getOriginalFileName(String encryptedName) {
    try (Connection conn = DatabaseConnection.connect()) {
        String sql = "SELECT nama_asli FROM log_file WHERE nama_sumber = ? AND status = 'Enkripsi' ORDER BY tanggal DESC LIMIT 1";
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setString(1, encryptedName);
        ResultSet rs = ps.executeQuery();
        if (rs.next()) {
            return rs.getString("nama_asli");
        }
    } catch (SQLException e) {
        e.printStackTrace();
    }
    return null;
}
    
    public String getFileNameFromDBByNamaAsli(String namaAsli, String status) {
    try (Connection conn = DatabaseConnection.connect()) {
        String sql = "SELECT nama_sumber FROM log_file WHERE nama_asli = ? AND status = ? ORDER BY tanggal DESC LIMIT 1";
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setString(1, namaAsli);
        ps.setString(2, status);
        ResultSet rs = ps.executeQuery();
        if (rs.next()) {
            return rs.getString("nama_sumber");
        }
    } catch (SQLException e) {
        e.printStackTrace();
    }
    return null;
}
    
    private boolean isDuplicateFile(String namaFile, String status) {
    try (Connection conn = DatabaseConnection.connect()) {
        String sql = "SELECT COUNT(*) FROM log_file WHERE nama_asli = ? AND status = ?";
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setString(1, namaFile);
        ps.setString(2, status);
        ResultSet rs = ps.executeQuery();
        if (rs.next()) {
            return rs.getInt(1) > 0;
        }
    } catch (SQLException e) {
        e.printStackTrace();
    }
    return false;
}
    
    public void filterLog(String keyword, String status) {
        if (keyword == null) keyword = "";
        if (status == null || status.trim().isEmpty()) status = "Semua";
        
        DefaultTableModel model = (DefaultTableModel) table.getModel();
        model.setRowCount(0); 
           
        try (Connection conn = DatabaseConnection.connect()) {
            String sql = "SELECT * FROM log_file WHERE nama_asli LIKE ?";
            
            if (!status.equalsIgnoreCase("Semua")) {
                sql += " AND status = ?";
            }
        
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, "%" + keyword + "%");
            
            if (!status.equalsIgnoreCase("Semua")) {
                ps.setString(2, status);
            }
        
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                String namaAsli = rs.getString("nama_asli");
                String namaSumber = rs.getString("nama_sumber");
                String statusLog = rs.getString("status");
                Timestamp tanggal = rs.getTimestamp("tanggal");
                String keterangan = rs.getString("keterangan");

                File fileEncrypted = new File("file_encrypt/" + namaSumber);
                File fileDecrypted = new File("file_decrypt/" + namaSumber);
                File file = fileEncrypted.exists() ? fileEncrypted : fileDecrypted;
                long sizeKB = file.exists() ? file.length() / 1024 : 0;

                model.addRow(new Object[]{
                    namaAsli,
                    namaSumber,
                    sizeKB + " KB",
                    statusLog,
                    new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(tanggal),
                    keterangan
                });
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Gagal memfilter log: " + e.getMessage());
        }
    }

    
    public void deleteSelectedLog() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(null, "Silakan pilih data log yang ingin dihapus!");
            return;
        }

        String namaAsli = table.getValueAt(selectedRow, 0).toString();
        String status = table.getValueAt(selectedRow, 3).toString();

        String namaSumber = getFileNameFromDBByNamaAsli(namaAsli, status);

        if (namaSumber == null) {
            JOptionPane.showMessageDialog(null, "Data file tidak ditemukan di database.");
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(null,
                "Apakah Anda yakin ingin menghapus log untuk file:\n" + namaAsli + " [" + status + "] ?",
                "Konfirmasi Hapus",
                JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            try (Connection conn = DatabaseConnection.connect()) {
                String sql = "DELETE FROM log_file WHERE nama_sumber = ? AND status = ?";
                PreparedStatement ps = conn.prepareStatement(sql);
                ps.setString(1, namaSumber);
                ps.setString(2, status);
                ps.executeUpdate();

                String folder = status.equalsIgnoreCase("Enkripsi") ? "file_encrypt/" : "file_decrypt/";
                File file = new File(folder + namaSumber);

                boolean fileDeleted = false;
                if (file.exists()) {
                    fileDeleted = file.delete();
                }

                if (fileDeleted) {
                    JOptionPane.showMessageDialog(null, "Log dan file berhasil dihapus.");
                } else {
                    JOptionPane.showMessageDialog(null, "Log berhasil dihapus, namun file tidak ditemukan atau gagal dihapus.");
                }

                loadLog();
            } catch (SQLException e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(null, "Gagal menghapus log: " + e.getMessage());
            }
        }
    }

    private void saveLog(String namaAsli, String namaSumber, String status, String keterangan) {
        try (Connection conn = DatabaseConnection.connect()) {
            String sql = "INSERT INTO log_file (nama_asli, nama_sumber, status, tanggal, keterangan) VALUES (?, ?, ?, NOW(), ?)";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, namaAsli);
            ps.setString(2, namaSumber);
            ps.setString(3, status);
            ps.setString(4, keterangan);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
  
    public int countLogByStatus(String status) {
        int count = 0;
        String sql = "SELECT COUNT(*) FROM log_file WHERE status = ?";
        try (Connection conn = DatabaseConnection.connect();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, status);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                count = rs.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return count;
    }

    public int countTotalLog() {
        int count = 0;
        String sql = "SELECT COUNT(*) FROM log_file";
        try (Connection conn = DatabaseConnection.connect();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            if (rs.next()) {
                count = rs.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return count;
    }
    
    public void exportLogToPDF() {
        try (Connection conn = DatabaseConnection.connect();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT * FROM log_file ORDER BY tanggal DESC")) {

            File file = new File("backup_log/log_file_backup_" + System.currentTimeMillis() + ".pdf");
            file.getParentFile().mkdirs(); 

            Document document = new Document();
            PdfWriter.getInstance(document, new FileOutputStream(file));
            document.open();

            Paragraph title = new Paragraph("LAPORAN LOG FILE ENKRIPSI & DEKRIPSI",
                FontFactory.getFont(FontFactory.HELVETICA_BOLD, 16));
            title.setAlignment(Element.ALIGN_CENTER);
            document.add(title);

            document.add(new Paragraph("Tanggal: " + java.time.LocalDate.now().toString()));
            document.add(new Paragraph(" ")); 

            PdfPTable table = new PdfPTable(6);
            table.setWidthPercentage(100);
            table.setSpacingBefore(10f);
            table.setSpacingAfter(10f);

            String[] headers = {"ID", "Nama Asli", "Nama Sumber", "Status", "Tanggal", "Keterangan"};
            for (String header : headers) {
                PdfPCell cell = new PdfPCell(new Phrase(header, FontFactory.getFont(FontFactory.HELVETICA_BOLD)));
                cell.setBackgroundColor(BaseColor.LIGHT_GRAY);
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                table.addCell(cell);
            }

            while (rs.next()) {
                table.addCell(rs.getString("id"));
                table.addCell(rs.getString("nama_asli"));
                table.addCell(rs.getString("nama_sumber"));
                table.addCell(rs.getString("status"));
                table.addCell(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(rs.getTimestamp("tanggal")));
                table.addCell(rs.getString("keterangan"));
            }

            document.add(table);
            document.close();

            JOptionPane.showMessageDialog(null, 
                "Log berhasil diekspor ke PDF!\nFile tersimpan di:\n" + file.getAbsolutePath());

        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Gagal ekspor PDF: " + e.getMessage());
        }
    }
}