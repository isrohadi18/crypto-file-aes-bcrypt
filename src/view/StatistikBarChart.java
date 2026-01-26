package view;


import controller.MainController;
import org.jfree.chart.title.TextTitle;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.ChartUtils;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.chart.axis.CategoryLabelPositions;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class StatistikBarChart extends JFrame {
    public StatistikBarChart(MainController controller) {
        int totalEnkripsi = controller.countLogByStatus("Enkripsi");
        int totalDekripsi = controller.countLogByStatus("Dekripsi");
        int totalFile = totalEnkripsi + totalDekripsi;

        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        dataset.addValue(totalEnkripsi, "Jumlah", "Enkripsi");
        dataset.addValue(totalDekripsi, "Jumlah", "Dekripsi");
        dataset.addValue(totalFile, "Jumlah", "Total File");

        String waktuSekarang = LocalDateTime.now()
            .format(DateTimeFormatter.ofPattern("dd MMM yyyy HH:mm:ss"));
        JFreeChart barChart = ChartFactory.createBarChart(
                "Grafik Penyimpanan File",
                null,
                null,
                dataset,
                PlotOrientation.VERTICAL,
                true,
                true,
                false
        );

        TextTitle subtitle = new TextTitle("Update: " + waktuSekarang);
        barChart.addSubtitle(subtitle);
 
        CategoryPlot plot = barChart.getCategoryPlot();
        plot.setBackgroundPaint(new Color(245, 245, 245));
        plot.setRangeGridlinePaint(new Color(200, 200, 200));
        plot.setDomainGridlinesVisible(false);

        BarRenderer renderer = (BarRenderer) plot.getRenderer();
        renderer.setSeriesPaint(0, new Color(255, 99, 71)); 
        renderer.setDrawBarOutline(false);
        renderer.setMaximumBarWidth(0.15);

        ChartPanel chartPanel = new ChartPanel(barChart);
        chartPanel.setPreferredSize(new Dimension(580, 340));

        JButton btnExportPNG = new JButton("Export Grafik ke PNG");
        btnExportPNG.addActionListener(e -> {
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setDialogTitle("Simpan Grafik sebagai PNG");
            fileChooser.setSelectedFile(new File("Grafik_penyimpanan_File.png"));

            int userSelection = fileChooser.showSaveDialog(this);
            if (userSelection == JFileChooser.APPROVE_OPTION) {
                File fileToSave = fileChooser.getSelectedFile();
                if (!fileToSave.getName().endsWith(".png")) {
                    fileToSave = new File(fileToSave.getAbsolutePath() + ".png");
                }
                try {
                    ChartUtils.saveChartAsPNG(fileToSave, barChart, 600, 400);
                    JOptionPane.showMessageDialog(this, "Grafik berhasil disimpan: " + fileToSave.getAbsolutePath());
                } catch (IOException ex) {
                    JOptionPane.showMessageDialog(this, "Gagal menyimpan grafik: " + ex.getMessage());
                }
            }
        });

        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        bottomPanel.add(btnExportPNG);

        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.add(chartPanel, BorderLayout.CENTER);
        mainPanel.add(bottomPanel, BorderLayout.SOUTH);

        setContentPane(mainPanel);
        setTitle("Gafik Penyimpanan File");
        setSize(600, 450);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
    }
}