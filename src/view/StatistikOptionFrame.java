package view;

import controller.MainController;
import javax.swing.*;
import java.awt.*;

public class StatistikOptionFrame extends JFrame {
    
    private MainController controller;

    public StatistikOptionFrame(MainController controller) {
        this.controller = controller;
        initComponents();
    }

    public StatistikOptionFrame() {
        this(null);
    }

    private void initComponents() {
        setTitle("Statistik");
        setSize(300, 150);
        setLocationRelativeTo(null);
        setLayout(new GridLayout(2, 1, 10, 10));

        JButton btnStatistikLog = new JButton("Grafik Penyimpanan File");
        JButton btnGrafikAES = new JButton("Data Uji Performance AES");

        btnStatistikLog.addActionListener(e -> {
            new StatistikBarChart(controller).setVisible(true);
        });

        btnGrafikAES.addActionListener(e -> {
            new test.Statistik_Grafik_AES("hasil_pengujian/hasil_pengujian_aes_cbc.csv").setVisible(true);
        });

        add(btnStatistikLog);
        add(btnGrafikAES);
    }
}
