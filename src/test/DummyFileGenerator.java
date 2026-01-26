package test;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Random;

public class DummyFileGenerator {

    private static final String OUTPUT_FOLDER = "hasil_pengujian/sample/";
    private static final int[] FILE_SIZES_KB = {100, 500, 1024, 2048, 3072, 4096, 5120}; // Ukuran: 100KB - 5MB

    public static void main(String[] args) {
        createOutputFolder();

        for (int sizeKB : FILE_SIZES_KB) {
            generateDummyFile(sizeKB);
        }

        System.out.println("✅ Semua file dummy berhasil dibuat di folder: " + OUTPUT_FOLDER);
    }

    private static void createOutputFolder() {
        File dir = new File(OUTPUT_FOLDER);
        if (!dir.exists()) {
            dir.mkdirs();
        }
    }

    private static void generateDummyFile(int sizeKB) {
        String fileName = OUTPUT_FOLDER + "file_dummy_" + sizeKB + "KB.txt";
        File file = new File(fileName);

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            Random rand = new Random();
            int targetSizeBytes = sizeKB * 1024;
            int currentSize = 0;

            while (currentSize < targetSizeBytes) {
                String line = generateRandomLine(rand);
                writer.write(line);
                writer.newLine();
                currentSize += line.getBytes().length + System.lineSeparator().getBytes().length;
            }

            System.out.println("✔ File dummy " + file.getName() + " berhasil dibuat (" + sizeKB + "KB)");
        } catch (IOException e) {
            System.err.println("❌ Gagal membuat file " + fileName + ": " + e.getMessage());
        }
    }

    private static String generateRandomLine(Random rand) {
        int length = rand.nextInt(50) + 30; // panjang karakter acak 30–80
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < length; i++) {
            char c = (char) (rand.nextInt(26) + 97); // a-z
            sb.append(c);
        }
        return sb.toString();
    }
}
