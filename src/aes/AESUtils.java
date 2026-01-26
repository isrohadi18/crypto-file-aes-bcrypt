package aes;

import javax.crypto.*;
import javax.crypto.spec.*;
import java.io.*;
import java.security.*;
import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.nio.file.Files;
import java.security.MessageDigest;
import java.security.SecureRandom;
import java.util.Arrays;

public class AESUtils {

    // 🔐 Versi untuk enkripsi File -> File (pakai password)
    public static void encrypt(File inputFile, File outputFile, String password) throws Exception {
        byte[] fileBytes = Files.readAllBytes(inputFile.toPath());
        byte[] encryptedBytes = encrypt(fileBytes, password);
        Files.write(outputFile.toPath(), encryptedBytes);
    }

    public static void decrypt(File inputFile, File outputFile, String password) throws Exception {
        byte[] fileBytes = Files.readAllBytes(inputFile.toPath());
        byte[] decryptedBytes = decrypt(fileBytes, password);
        Files.write(outputFile.toPath(), decryptedBytes);
    }

    // 🔐 Versi enkripsi byte[] dengan password
    public static byte[] encrypt(byte[] data, String password) throws Exception {
        SecretKeySpec secretKey = getKeyFromPassword(password);
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");

        byte[] iv = new byte[16];
        new SecureRandom().nextBytes(iv);
        IvParameterSpec ivParams = new IvParameterSpec(iv);

        cipher.init(Cipher.ENCRYPT_MODE, secretKey, ivParams);
        byte[] encrypted = cipher.doFinal(data);

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        outputStream.write(iv); // Simpan IV di awal
        outputStream.write(encrypted);
        return outputStream.toByteArray();
    }

    public static byte[] decrypt(byte[] data, String password) throws Exception {
        SecretKeySpec secretKey = getKeyFromPassword(password);
        byte[] iv = Arrays.copyOfRange(data, 0, 16);
        byte[] encryptedData = Arrays.copyOfRange(data, 16, data.length);

        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        IvParameterSpec ivParams = new IvParameterSpec(iv);

        cipher.init(Cipher.DECRYPT_MODE, secretKey, ivParams);
        return cipher.doFinal(encryptedData);
    }

    private static SecretKeySpec getKeyFromPassword(String password) throws Exception {
        MessageDigest sha = MessageDigest.getInstance("SHA-256");
        byte[] key = password.getBytes("UTF-8");
        key = sha.digest(key);
        return new SecretKeySpec(Arrays.copyOf(key, 16), "AES"); // 128-bit AES key
    }

    // ✅ Tambahan untuk uji performa (pakai key dan IV manual)
    public static byte[] encryptAES_CBC(byte[] data, byte[] key, byte[] iv) throws Exception {
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        SecretKeySpec secretKey = new SecretKeySpec(key, "AES");
        IvParameterSpec ivParams = new IvParameterSpec(iv);

        cipher.init(Cipher.ENCRYPT_MODE, secretKey, ivParams);
        return cipher.doFinal(data);
    }

    public static byte[] decryptAES_CBC(byte[] data, byte[] key, byte[] iv) throws Exception {
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        SecretKeySpec secretKey = new SecretKeySpec(key, "AES");
        IvParameterSpec ivParams = new IvParameterSpec(iv);

        cipher.init(Cipher.DECRYPT_MODE, secretKey, ivParams);
        return cipher.doFinal(data);
    }

    // 1. Generate AES Key (128-bit) - Aman dan modular
    public static byte[] generateAESKey() throws NoSuchAlgorithmException {
        KeyGenerator keyGen = KeyGenerator.getInstance("AES");
        SecureRandom secureRandom = SecureRandom.getInstanceStrong(); // Lebih aman dari default PRNG
        keyGen.init(128, secureRandom); // Inisialisasi 128-bit key dengan SecureRandom
        SecretKey secretKey = keyGen.generateKey();
        return secretKey.getEncoded();
    }

    // 2. Generate IV (Initialization Vector) - Aman
    public static byte[] generateIV() {
        byte[] iv = new byte[16]; // 128-bit IV untuk AES
        SecureRandom secureRandom = new SecureRandom();
        secureRandom.nextBytes(iv); // Generate IV secara acak
        return iv;
    }

    // 3. Encrypt File
    public static void encryptForTest(File source, File dest, String password) throws Exception {
        byte[] key = Arrays.copyOf(password.getBytes("UTF-8"), 16); // fix length
        SecretKeySpec secretKey = new SecretKeySpec(key, "AES");
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");

        byte[] iv = generateIV();
        IvParameterSpec ivSpec = new IvParameterSpec(iv);
        cipher.init(Cipher.ENCRYPT_MODE, secretKey, ivSpec);

        try (FileInputStream fis = new FileInputStream(source);
             FileOutputStream fos = new FileOutputStream(dest)) {
            fos.write(iv); // simpan IV di awal file

            CipherOutputStream cos = new CipherOutputStream(fos, cipher);
            byte[] buffer = new byte[4096];
            int read;
            while ((read = fis.read(buffer)) != -1) {
                cos.write(buffer, 0, read);
            }
            cos.close();
        }
    }

    // 4. Decrypt File
    public static void decryptForTest(File source, File dest, String password) throws Exception {
        byte[] key = Arrays.copyOf(password.getBytes("UTF-8"), 16); // fix length
        SecretKeySpec secretKey = new SecretKeySpec(key, "AES");
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");

        try (FileInputStream fis = new FileInputStream(source)) {
            byte[] iv = new byte[16];
            fis.read(iv); // ambil IV dari awal file
            IvParameterSpec ivSpec = new IvParameterSpec(iv);

            cipher.init(Cipher.DECRYPT_MODE, secretKey, ivSpec);
            try (FileOutputStream fos = new FileOutputStream(dest);
                 CipherInputStream cis = new CipherInputStream(fis, cipher)) {
                byte[] buffer = new byte[4096];
                int read;
                while ((read = cis.read(buffer)) != -1) {
                    fos.write(buffer, 0, read);
                }
            }
        }
    }
}
