package test;

import aes.AESUtils;

// 📊 JFreeChart
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.renderer.category.LineAndShapeRenderer;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.title.TextTitle;



// 📄 PDF - iText
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Image;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.Image;

// 🖼️ Grafik dan Image
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;


// 🧩 Java Swing dan AWT
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumnModel;
import java.awt.*;
import java.awt.event.*;
import javax.swing.table.DefaultTableCellRenderer;

// 📁 File dan IO
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.ArrayList;
import java.io.ByteArrayOutputStream;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Statistik_Grafik_AES extends JFrame {
    private JTable table;
    private DefaultTableModel tableModel;
    private ChartPanel chartPanel;
    private JButton btnexportToPDF;
    private JLabel lblDateTime;
    private Timer timer;


    private static final String PASSWORD = "passwordku123";
    private static final String SAMPLE_FOLDER = "hasil_pengujian/sample/";
    private static final String ENCRYPT_FOLDER = "file_encrypt/";
    private static final String DECRYPT_FOLDER = "file_decrypt/";
    private static final String CSV_FILE = "hasil_pengujian/hasil_pengujian_aes_cbc.csv";

    public Statistik_Grafik_AES() {
        this(CSV_FILE);
    }

    public Statistik_Grafik_AES(String filePath) {
        super("Statistik & Grafik AES");
        setSize(1000, 700);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        JButton btnRefresh = new JButton("Refresh Data");
        btnRefresh.addActionListener(e -> refreshData());

        JPanel mainPanel = new JPanel();
        mainPanel.setBackground(new Color(245, 240, 230));
        mainPanel.setBackground(Color.WHITE);
        mainPanel.setLayout(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(0, 15, 15, 15));

        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        topPanel.setBackground(new Color(245, 240, 230)); 
        topPanel.setBackground(Color.WHITE);
        topPanel.setBorder(BorderFactory.createEmptyBorder(30, 0, 10, 20));
        btnexportToPDF = createSavePDFButton();
        topPanel.add(btnexportToPDF);
        topPanel.add(btnRefresh);

        chartPanel = new ChartPanel(null);
        mainPanel.add(chartPanel, BorderLayout.CENTER);

        tableModel = new DefaultTableModel();
        table = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setPreferredSize(new Dimension(800, 150));
        mainPanel.add(scrollPane, BorderLayout.SOUTH);

        add(topPanel, BorderLayout.NORTH);
        add(mainPanel, BorderLayout.CENTER);

        setVisible(true);
        SwingUtilities.invokeLater(this::refreshData);
    }

    private void refreshData() {
        SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() {
            @Override
            protected Void doInBackground() throws Exception {
                jalankanPengujianAES();
                return null;
            }
            @Override
            protected void done() {
                loadCSVtoTable(CSV_FILE, table);
                updateChartFromCSV(new File(CSV_FILE));
            }
        };
        worker.execute();
    }

    private void loadCSVtoTable(String filePath, JTable table) {
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            DefaultTableModel model = new DefaultTableModel();
            String line;
            boolean header = true;

            while ((line = br.readLine()) != null) {
                String[] data = line.split(",");
                if (header) {
                    model.setColumnIdentifiers(data);
                    header = false;
                } else {
                    model.addRow(data);
                }
            }

            table.setModel(model);
            TableColumnModel columnModel = table.getColumnModel();
            columnModel.getColumn(0).setPreferredWidth(30); 
            columnModel.getColumn(1).setPreferredWidth(200); 
            columnModel.getColumn(2).setPreferredWidth(100); 
            columnModel.getColumn(3).setPreferredWidth(120); 
            columnModel.getColumn(4).setPreferredWidth(120); 
            columnModel.getColumn(5).setPreferredWidth(80); 
            DefaultTableCellRenderer center = new DefaultTableCellRenderer();
            center.setHorizontalAlignment(JLabel.CENTER);
            for (int i = 0; i < model.getColumnCount(); i++) {
                if (!model.getColumnName(i).equalsIgnoreCase("Nama File")) {
                    table.getColumnModel().getColumn(i).setCellRenderer(center);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void updateChartFromCSV(File file) {
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();

        try (BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(file)))) {
            String line = br.readLine();
            int no = 1; 
            
            while ((line = br.readLine()) != null) {
                String[] values = line.split(",");
                if (values.length >= 6) {
                    String ukuranKB = values[2].trim();
                    double waktuEnkripsi = Double.parseDouble(values[3].trim());
                    double waktuDekripsi = Double.parseDouble(values[4].trim());

                    String label = String.valueOf(no++);
                    
                    dataset.addValue(waktuEnkripsi, "Enkripsi", label);
                    dataset.addValue(waktuDekripsi, "Dekripsi", label);
                    
                }
            }

            JFreeChart chart = ChartFactory.createBarChart(
                "Perbandingan Waktu Enkripsi & Dekripsi",
                "NOMER FILE",
                "Waktu Proses AES (ms)",
                dataset,
                PlotOrientation.VERTICAL,
                true, true, false
            );

            chartPanel.setChart(chart);
            String timestamp = new java.text.SimpleDateFormat("dd MMMM yyyy - HH:mm 'WIB'").format(new java.util.Date());
            TextTitle subtitle = new TextTitle("Terakhir diperbarui: " + timestamp);
            chart.addSubtitle(subtitle);

        } catch (IOException | NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Gagal memuat grafik: " + e.getMessage());
        }
    }

    public static void jalankanPengujianAES() {
        try {
            Files.createDirectories(Paths.get(ENCRYPT_FOLDER));
            Files.createDirectories(Paths.get(DECRYPT_FOLDER));
            Files.createDirectories(Paths.get("hasil_pengujian/"));

            File sampleDir = new File(SAMPLE_FOLDER);
            File[] files = sampleDir.listFiles(File::isFile);

            if (files == null || files.length == 0) return;

            List<String[]> results = new ArrayList<>();
            results.add(new String[] {"No", "Nama File", "Ukuran File(KB)", "Waktu Enkripsi (ms)", "Waktu Dekripsi (ms)", "Status"});

            int no = 1;
            for (File file : files) {
                String fileName = file.getName();
                long sizeKB = file.length() / 1024;

                File encryptedFile = new File(ENCRYPT_FOLDER + fileName + ".enc");
                File decryptedFile = new File(DECRYPT_FOLDER + fileName + ".dec");

                long startEncrypt = System.nanoTime();
                AESUtils.encryptForTest(file, encryptedFile, PASSWORD);
                long endEncrypt = System.nanoTime();

                long startDecrypt = System.nanoTime();
                AESUtils.decryptForTest(encryptedFile, decryptedFile, PASSWORD);
                long endDecrypt = System.nanoTime();

                long timeEncrypt = (endEncrypt - startEncrypt) / 1_000_000;
                long timeDecrypt = (endDecrypt - startDecrypt) / 1_000_000;

                results.add(new String[] {
                    String.valueOf(no++),
                    fileName,
                    String.valueOf(sizeKB),
                    String.valueOf(timeEncrypt),
                    String.valueOf(timeDecrypt),
                    "Berhasil"
                });
            }

            try (PrintWriter writer = new PrintWriter(new FileWriter(CSV_FILE))) {
                for (String[] row : results) {
                    writer.println(String.join(",", row));
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private JButton createSavePDFButton() {
        JButton btnSavePDF = new JButton("Simpan ke PDF");
        btnSavePDF.addActionListener(e -> {
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setDialogTitle("Simpan Grafik dan Tabel ke PDF");
            int userSelection = fileChooser.showSaveDialog(this);
            if (userSelection == JFileChooser.APPROVE_OPTION) {
                File fileToSave = fileChooser.getSelectedFile();
                if (!fileToSave.getName().toLowerCase().endsWith(".pdf")) {
                    fileToSave = new File(fileToSave.getAbsolutePath() + ".pdf");
                }
                exportToPDF(table, chartPanel.getChart(), fileToSave);
            }
        });
        return btnSavePDF;
    }


    private void saveChartAsPDF(File file) {
        try {
            int width = chartPanel.getWidth();
            int height = chartPanel.getHeight();

            Document document = new Document(new Rectangle(width, height));
            PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(file));
            document.open();

            PdfContentByte cb = writer.getDirectContent();
            Graphics2D g2 = cb.createGraphics(width, height);
            chartPanel.getChart().draw(g2, new Rectangle2D.Double(0, 0, width, height));
            g2.dispose();

            document.close();
            JOptionPane.showMessageDialog(this, "Grafik berhasil disimpan ke PDF.");
            if (Desktop.isDesktopSupported()) {
    Desktop.getDesktop().open(file); 
}

        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Gagal menyimpan grafik ke PDF: " + ex.getMessage());
        }
    }
    
    public static void exportToPDF(JTable table, JFreeChart chart, File filePDF) {
        try {
            Document document = new Document();
            PdfWriter.getInstance(document, new FileOutputStream(filePDF));
            document.open();

            Font titleFont = new Font(Font.FontFamily.HELVETICA, 18, Font.BOLD);
            Paragraph title = new Paragraph("Hasil Pengujian Kriptografi AES", titleFont);
            title.setAlignment(Element.ALIGN_CENTER);
            document.add(title);
            document.add(Chunk.NEWLINE);

            BufferedImage chartImage = chart.createBufferedImage(500, 300);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ImageIO.write(chartImage, "png", baos);
            Image chartImg = Image.getInstance(baos.toByteArray());
            chartImg.scaleToFit(500, 300);
            chartImg.setAlignment(Element.ALIGN_CENTER);
            document.add(chartImg);
            document.add(Chunk.NEWLINE);

            PdfPTable pdfTable = new PdfPTable(table.getColumnCount());
            pdfTable.setWidthPercentage(100);
            pdfTable.setSpacingBefore(10f);
            pdfTable.setSpacingAfter(10f);

            for (int i = 0; i < table.getColumnCount(); i++) {
                pdfTable.addCell(new PdfPCell(new Phrase(table.getColumnName(i))));
            }

            for (int row = 0; row < table.getRowCount(); row++) {
                for (int col = 0; col < table.getColumnCount(); col++) {
                    Object value = table.getValueAt(row, col);
                    pdfTable.addCell(value != null ? value.toString() : "");
                }
            }

            document.add(pdfTable);
            document.close();

            JOptionPane.showMessageDialog(null, "Berhasil diekspor ke PDF:\n" + filePDF.getAbsolutePath());

            if (Desktop.isDesktopSupported()) {
        Desktop.getDesktop().open(filePDF); 
        }} catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Gagal ekspor PDF: " + e.getMessage());
        }
    }
}