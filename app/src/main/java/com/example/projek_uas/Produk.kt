package com.example.projek_uas

data class Produk(
    val id: String = "",
    val nama: String = "",
    val harga: Int = 0,
    val gambarRes: Int = 0,
    val deskripsi: String = "",
    val kategori: String = "",
    var jumlah: Int = 1,
    var tanggal: String = ""
)