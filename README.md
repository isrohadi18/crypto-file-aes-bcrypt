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
  <img src="https://img.shields.io/github/last-commit/isrohadi18/crypto-file-aes-bcrypt" />&nbsp;
  <img src="https://img.shields.io/github/repo-size/isrohadi18/crypto-file-aes-bcrypt" />&nbsp;
  <img src="https://img.shields.io/github/downloads/isrohadi18/crypto-file-aes-bcrypt/releases/download/download/CyrptoFileAES.exe/total" />&nbsp;
  <a href="https://github.com/isrohadi18/crypto-file-aes-bcrypt/releases/latest">
  <img src="https://img.shields.io/badge/Download-App-blue?style=for-the-badge&logo=github">
</a>
  <img src="https://img.shields.io/github/v/release/isrohadi18/crypto-file-aes-bcrypt" />
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

<p>
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
</p>

---

> ### **Tampilan Aplikasi**

| Fitur                   | Preview                                        |
| ----------------------- | ---------------------------------------------- |
| Login                   | <img src="src/assets/frame/login.svg" />       |
| Register                | <img src="src/assets/frame/register.svg" />    |
| Menu Utama              | <img src="src/assets/frame/menutama.svg" />    |
| Manajement User         | <img src="src/assets/frame/muser.svg" />       |
| Statistik performa File | <img src="src/assets/frame/performasta.svg" /> |
| Grafik File Aes         | <img src="src/assets/frame/grafiksta.svg" />   |

---

> ### **Alur Menjalankan Aplikasi**

#### 1️⃣ **Persiapan Software**

- Install _Java JDK 8_ atau lebih baru
- Install Apache _NetBeans IDE_
- Install _MySQL_ Server

#### 2️⃣ **Setup Database**

1. Buat database di MySQL
2. Import file berikut:

   ```
   kriptografi_db.sql
   ```

3. Sesuaikan konfigurasi database di:

   ```
   src/config/DatabaseConnection.java
   ```

#### 3️⃣ **Jalankan Aplikasi**

- Buka project menggunakan _IntelliJ IDEA / NetBeans / Eclipse_
- Tambahkan seluruh file `.jar` di folder `lib/` ke _Build Path_
- Jalankan file utama:

  ```
  src/view/LoginRegisterFrame.java
  ```

#### &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;**_Pengujian_**

- File uji tersedia di folder `sample/`
- Hasil enkripsi/dekripsi otomatis tersimpan di:
  - `file_encrypt/`
  - `file_decrypt/`

- Statistik dan grafik performa AES tersedia di:
  - `hasil_pengujian/`

---

> ### **License**

Aplikasi ini ditujukan untuk **_pembelajaran keamanan informasi & kriptografi file._** Mendukung berbagai format file **`.txt, .pdf, .xlsx, dll.`** Sourcecode **_Gratis ..,_** Digunakan dan dimodifikasi dalam **_tujuan pembelajaran._** Jika ingin Sourcecode full **silahkan Hubungi Author** Thanks ...

Jika Anda merasa repositori ini bermanfaat, **_silakan beri bintang ⭐ di GitHub_**
