-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Waktu pembuatan: 06 Agu 2025 pada 11.43
-- Versi server: 10.4.32-MariaDB
-- Versi PHP: 8.2.12

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `kriptografi_db`
--

-- --------------------------------------------------------

--
-- Struktur dari tabel `log_file`
--

CREATE TABLE `log_file` (
  `id` int(11) NOT NULL,
  `nama_asli` varchar(255) DEFAULT NULL,
  `nama_sumber` varchar(255) DEFAULT NULL,
  `status` varchar(50) DEFAULT NULL,
  `tanggal` timestamp NOT NULL DEFAULT current_timestamp(),
  `keterangan` text DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data untuk tabel `log_file`
--

INSERT INTO `log_file` (`id`, `nama_asli`, `nama_sumber`, `status`, `tanggal`, `keterangan`) VALUES
(100, 'Price_List_Produk_ALS.xlsx', '1753000752954-Price_List_Produk_ALS.xlsx.aes', 'Enkripsi', '2025-07-20 08:39:12', 'ALS02'),
(101, 'Price_List_Produk_ALS.xlsx', 'Price_List_Produk_ALS.xlsx', 'Dekripsi', '2025-07-20 08:39:32', 'ALS02'),
(102, 'Sample_Data_Daftar_Karyawan.xlsx', '1753000791860-Sample_Data_Daftar_Karyawan.xlsx.aes', 'Enkripsi', '2025-07-20 08:39:51', 'ALS03'),
(103, 'Sample_Data_Daftar_Karyawan.xlsx', 'Sample_Data_Daftar_Karyawan.xlsx', 'Dekripsi', '2025-07-20 08:40:09', 'ALS03'),
(104, 'Sample_Kontrak_kerjasama_vendor.docx', '1753000830653-Sample_Kontrak_kerjasama_vendor.docx.aes', 'Enkripsi', '2025-07-20 08:40:31', 'ALS04'),
(105, 'Sample_Kontrak_kerjasama_vendor.docx', 'Sample_Kontrak_kerjasama_vendor.docx', 'Dekripsi', '2025-07-20 08:40:43', 'ALS04'),
(106, 'Sample_Kontrak_kerjasama_vendor.pdf', '1753000861685-Sample_Kontrak_kerjasama_vendor.pdf.aes', 'Enkripsi', '2025-07-20 08:41:01', 'ALS05'),
(107, 'Sample_Kontrak_kerjasama_vendor.pdf', 'Sample_Kontrak_kerjasama_vendor.pdf', 'Dekripsi', '2025-07-20 08:41:14', 'ALS05'),
(108, 'Sample_Laporan_Keuangan_Q1_2024.docx', '1753000890539-Sample_Laporan_Keuangan_Q1_2024.docx.aes', 'Enkripsi', '2025-07-20 08:41:30', 'ALS06'),
(109, 'Sample_Laporan_Keuangan_Q1_2024.docx', 'Sample_Laporan_Keuangan_Q1_2024.docx', 'Dekripsi', '2025-07-20 08:41:42', 'ALS06'),
(110, 'Sample_Laporan_Keuangan_Q1_2024.pdf', '1753000930179-Sample_Laporan_Keuangan_Q1_2024.pdf.aes', 'Enkripsi', '2025-07-20 08:42:10', 'ALS07'),
(111, 'Sample_Laporan_Keuangan_Q1_2024.pdf', 'Sample_Laporan_Keuangan_Q1_2024.pdf', 'Dekripsi', '2025-07-20 08:42:21', 'ALS07'),
(112, 'Sample_Persentasi_Perusahaan.pptx', '1753000960907-Sample_Persentasi_Perusahaan.pptx.aes', 'Enkripsi', '2025-07-20 08:42:41', 'ALS08'),
(113, 'Sample_Persentasi_Perusahaan.pptx', 'Sample_Persentasi_Perusahaan.pptx', 'Dekripsi', '2025-07-20 08:42:51', 'ALS08'),
(114, 'Pedoman 2024-2025 final.pdf', '1753054697501-Pedoman 2024-2025 final.pdf.aes', 'Enkripsi', '2025-07-20 23:38:19', 'ALS01'),
(116, 'Pedoman 2024-2025 final.pdf', 'Pedoman 2024-2025 final.pdf', 'Dekripsi', '2025-07-20 23:44:40', 'ALS01'),
(118, 'Cetak-kartu_ujian-202143501395 (1).pdf', 'Cetak-kartu_ujian-202143501395 (1).pdf', 'Dekripsi', '2025-08-02 06:42:01', ''),
(119, 'Financial_Report_9M2010_Indonesia_(unaudit).pdf', '1754382296222-Financial_Report_9M2010_Indonesia_(unaudit).pdf.aes', 'Enkripsi', '2025-08-05 08:24:57', 'FSGFD'),
(120, 'Financial_Report_9M2010_Indonesia_(unaudit).pdf', 'Financial_Report_9M2010_Indonesia_(unaudit).pdf', 'Dekripsi', '2025-08-05 08:26:15', '');

-- --------------------------------------------------------

--
-- Struktur dari tabel `users`
--

CREATE TABLE `users` (
  `id` int(11) NOT NULL,
  `username` varchar(50) DEFAULT NULL,
  `password` varchar(255) DEFAULT NULL,
  `role` enum('admin','user') DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data untuk tabel `users`
--

INSERT INTO `users` (`id`, `username`, `password`, `role`) VALUES
(12, 'ROHADI', '$2a$10$ZqXocXfUHhJVbJoXjaSM3.0fhIJRqf59w1.kxvQz5VayDhoo/PwdC', 'admin'),
(18, 'HADI', '$2a$10$SjYh7hgrvEAKoZ6erWLn1OKoKZ1CXkGXdHBmkfyBsbkZLkYNfxj16', 'user'),
(19, 'AGUS', '$2a$10$Q.lkabA41lD7ZtkQQer07umtW5AqOQcYySD7MIG8w0UQ/3ivbT7vy', 'admin');

--
-- Indexes for dumped tables
--

--
-- Indeks untuk tabel `log_file`
--
ALTER TABLE `log_file`
  ADD PRIMARY KEY (`id`);

--
-- Indeks untuk tabel `users`
--
ALTER TABLE `users`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `username` (`username`);

--
-- AUTO_INCREMENT untuk tabel yang dibuang
--

--
-- AUTO_INCREMENT untuk tabel `log_file`
--
ALTER TABLE `log_file`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=121;

--
-- AUTO_INCREMENT untuk tabel `users`
--
ALTER TABLE `users`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=20;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
