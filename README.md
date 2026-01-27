<h1 align="center">
  <img src="src/assets/icons/logo.svg" width="300px">
  <br>
  <b>CRYPTO - APP</b>
</h1>

<p align="center">
<!-- JAVA -->
  <img src="src/assets/icons/java.svg" height="20" />
  <img src="https://img.shields.io/badge/Java-Swing%20GUI-orange?style=flat-square" />
  <!-- MYSQL -->
  <img src="src/assets/icons/mysql.svg" height="20" />
  <img src="https://img.shields.io/badge/MySQL-Database-darkblue?style=flat-square" />
    <!-- NETBEANS -->
  <img src="src/assets/icons/netbeans.svg" height="20" />
  <img src="https://img.shields.io/badge/NetBeans-IDE-purple?style=flat-square" />
  <br>
  <!-- DETAIL APP -->
  <img src="https://img.shields.io/github/repo-size/isrohadi18/crypto-file-aes-bcrypt" />&nbsp;
  <img src="https://img.shields.io/github/last-commit/isrohadi18/crypto-file-aes-bcrypt" />&nbsp;
  <img src="https://img.shields.io/badge/platform-Windows%20%7C%20Linux%20%7C%20macOS-brown" />
</p>

Aplikasi **keamanan file berbasis Java Desktop** yang berfungsi untuk melakukan **enkripsi dan dekripsi file menggunakan algoritma AES (Advanced Encryption Standard)** dengan mode **CBC**, serta **pengamanan password menggunakan BCrypt**. Aplikasi ini dilengkapi dengan **manajemen user, logging, dan statistik performa enkripsi**.

---

> ### **Fitur Utama**

- Enkripsi & Dekripsi File (AES-CBC)
- Hashing Password menggunakan **BCrypt**
- Sistem Login & Register User
- Manajemen File (upload, encrypt, decrypt)
- Statistik & Grafik Performa AES
- Logging hasil pengujian enkripsi
- Integrasi Database MySQL
- Dan banyak lagi fitur yang menarik

---

> ### **Library**

  <img src="https://img.shields.io/badge/commons--collections-4--4.4-darkgrey?style=plasic" />
  <img src="https://img.shields.io/badge/commons--compress-1.21-darkgrey?style=plasic" />
  <img src="https://img.shields.io/badge/itextpdf-5.5.13.2-darkgrey?style=plasic" />
  <img src="https://img.shields.io/badge/jbcrypt-0.4-darkgrey?style=plasic" />
  <img src="https://img.shields.io/badge/jcommon-1.0.16-darkgrey?style=plasic" />
  <img src="https://img.shields.io/badge/jfreechart-1.5.0-darkgrey?style=plasic" />
  <img src="https://img.shields.io/badge/log4j--api-2.13.3-darkgrey?style=plasic" />
  <img src="https://img.shields.io/badge/log4j--core-2.13.3-darkgrey?style=plasic" />
  <img src="https://img.shields.io/badge/mysql--connector--j-8.4.0-darkgrey?style=plasic" />
  <img src="https://img.shields.io/badge/ooxml--schemas-1.4-darkgrey?style=plasic" />
  <img src="https://img.shields.io/badge/poi-5.0.0-darkgrey?style=plasic" />
  <img src="https://img.shields.io/badge/poi--ooxml-5.0.0-darkgrey?style=plasic" />
  <img src="https://img.shields.io/badge/poi--ooxml--full-5.2.3-darkgrey?style=plasic" />
  <img src="https://img.shields.io/badge/xmlbeans-5.1.1-darkgrey?style=plasic" />

---

> ### **Tampilan Aplikasi**

| Fitur           | fungsi | Preview                                        |
| --------------- | ------ | ---------------------------------------------- |
| Login           |        | <img src="src/assets/frame/login.svg" />       |
| Register        |        | <img src="src/assets/frame/register.svg" />    |
| Dashboard User  |        | <img src="src/assets/frame/menutama.svg" />    |
| Admin Panel     |        | <img src="src/assets/frame/muser.svg" />       |
| Logo / Branding |        | <img src="src/assets/frame/performasta.svg" /> |
| Logo / Branding |        | <img src="src/assets/frame/grafiksta.svg" />   |

> 📌 Gambar dapat dilihat langsung pada folder `src/assets/`

---

## 📂 Struktur Folder Utama

```
crypto-file-aes-bcrypt-main/
│
├── src/
│   ├── aes/              # Utilitas AES
│   ├── controller/       # Logic aplikasi
│   ├── view/             # Tampilan GUI (Swing)
│   ├── config/           # Koneksi database
│   └── assets/           # Gambar & asset UI
│
├── lib/                  # Library (.jar)
├── sample/               # File uji coba
├── hasil_pengujian/      # Grafik & log hasil tes
├── file_encrypt/         # Output file terenkripsi
├── file_decrypt/         # Output file dekripsi
├── kriptografi_db.sql    # Struktur database
└── README.md
```

---

## ⚙️ Cara Menjalankan Aplikasi (Step-by-Step)

### 1️⃣ Persiapan Environment

- Install **Java JDK 8 atau lebih baru**
- Install **MySQL Server**

---

### 2️⃣ Setup Database

1. Buat database di MySQL
2. Import file berikut:

   ```
   kriptografi_db.sql
   ```

3. Sesuaikan konfigurasi database di:

   ```
   src/config/DatabaseConnection.java
   ```

---

### 3️⃣ Jalankan Aplikasi

#### Opsi A – Menggunakan IDE (Disarankan)

- Buka project menggunakan **IntelliJ IDEA / NetBeans / Eclipse**
- Tambahkan seluruh file `.jar` di folder `lib/` ke **Build Path**
- Jalankan file utama:

  ```
  src/view/LoginRegisterFrame.java
  ```

#### Opsi B – Compile Manual (CLI)

```bash
javac -cp "lib/*" src/**/*.java
java  -cp "lib/*;src" view.LoginRegisterFrame
```

---

## 🧪 Pengujian

- File uji tersedia di folder `sample/`
- Hasil enkripsi/dekripsi otomatis tersimpan di:
  - `file_encrypt/`
  - `file_decrypt/`

- Statistik dan grafik performa AES tersedia di:
  - `hasil_pengujian/`

---

## 📌 Catatan

- Aplikasi ini ditujukan untuk **pembelajaran dan penelitian kriptografi**
- Mendukung berbagai format file: **.txt, .pdf, .xlsx, dll**

---

## 👨‍💻 Author

Dikembangkan untuk kebutuhan **Keamanan Informasi & Kriptografi File**.

---

⭐ Jika project ini membantu, jangan lupa beri **star** di GitHub!
