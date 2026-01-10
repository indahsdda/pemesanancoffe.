package com.example.projek_uas
data class Produk(
    val id: Int,
    val nama: String,
    val harga: Int,
    val gambarRes: Int,
    val kategori: String,
    var jumlah: Int = 1
)
