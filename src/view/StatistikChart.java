package view;

import controller.MainController;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.data.general.DefaultPieDataset;

import javax.swing.*;

public class StatistikChart extends JFrame {
    public StatistikChart(MainController controller) {
        setTitle("Grafik Statistik Enkripsi vs Dekripsi");
        setSize(500, 400);
        setLocationRelativeTo(null);

        int totalEnkripsi = controller.countLogByStatus("Enkripsi");
        int totalDekripsi = controller.countLogByStatus("Dekripsi");

        DefaultPieDataset dataset = new DefaultPieDataset();
        dataset.setValue("Enkripsi", totalEnkripsi);
        dataset.setValue("Dekripsi", totalDekripsi);

        JFreeChart chart = ChartFactory.createPieChart(
                "Statistik Penggunaan Aplikasi",
                dataset,
                true, true, false);

        ChartPanel panel = new ChartPanel(chart);
        setContentPane(panel);
    }
}