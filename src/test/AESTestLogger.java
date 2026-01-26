package test;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;

public class AESTestLogger {
    private static final String FILE_PATH = "hasil_pengujian/hasil_pengujian_aes.csv";
    private static final String HEADER = "No.,Nama File,Ukuran File (KB),Waktu Enkripsi (ms),Waktu Dekripsi (ms),Status";

    // Tambah log baru ke CSV
    public static void log(int no, String namaFile, long ukuranKB, long waktuEnkripsi, long waktuDekripsi, String status) {
        try {
            File file = new File(FILE_PATH);
            boolean fileBaru = file.createNewFile();

            try (BufferedWriter bw = new BufferedWriter(new FileWriter(file, true))) {
                if (fileBaru || file.length() == 0) {
                    bw.write(HEADER);
                    bw.newLine();
                }
                bw.write(no + "," + namaFile + "," + ukuranKB + "," + waktuEnkripsi + "," + waktuDekripsi + "," + status);
                bw.newLine();
            }
        } catch (IOException e) {
            System.err.println("Gagal menulis log ke CSV: " + e.getMessage());
        }
    }

    // Hitung jumlah entri (baris) di CSV
    public static int getNextRowNumber() {
        int lines = 0;
        try (BufferedReader br = new BufferedReader(new FileReader(FILE_PATH))) {
            while (br.readLine() != null) {
                lines++;
            }
        } catch (IOException ignored) {}
        return Math.max(lines - 1, 0) + 1; // header tidak dihitung
    }
}
