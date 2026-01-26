package view;

import javax.swing.*;
import java.awt.*;

public class FileDetailDialog extends JDialog {
    public FileDetailDialog(Frame parent, String namaEnkripsi, String namaAsli, long sizeKB, String tanggal, String keterangan) {
        super(parent, "Detail File Sebelum Dekripsi", true);
        setLocationRelativeTo(parent);

        JPanel panel = new JPanel(new GridLayout(0, 1));
        panel.setBorder(BorderFactory.createEmptyBorder(0, 10, 10, 10)); 
        panel.add(new JLabel("<html>&nbsp;&nbsp;&nbsp;Nama File Enkripsi: " + namaEnkripsi + "</html>"));
        panel.add(new JLabel("<html>&nbsp;&nbsp;&nbsp;Nama File Asli    : " + namaAsli + "</html>"));
        panel.add(new JLabel("<html>&nbsp;&nbsp;&nbsp;Ukuran File (KB)  : " + sizeKB + "</html>"));
        panel.add(new JLabel("<html>&nbsp;&nbsp;&nbsp;Tanggal Enkripsi  : " + tanggal + "</html>"));
        panel.add(new JLabel("<html>&nbsp;&nbsp;&nbsp;Keterangan        : " + keterangan + "</html>"));
        
        JButton btnOK = new JButton("Lanjutkan Dekripsi");
        btnOK.addActionListener(e -> dispose());
        panel.add(btnOK);

        setLayout(new BorderLayout());
        setLocationRelativeTo(null);
        add(panel, BorderLayout.CENTER);
        pack();
    }
}