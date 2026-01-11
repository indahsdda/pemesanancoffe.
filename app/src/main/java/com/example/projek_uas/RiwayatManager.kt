package com.example.projek_uas

// Menggunakan class Produk yang sudah ada di folder Anda
object RiwayatManager {
    private val listRiwayat = mutableListOf<Produk>()

    fun tambahKeRiwayat(items: List<Produk>) {
        listRiwayat.addAll(items)
    }

    fun getRiwayat(): List<Produk> {
        return listRiwayat
    }
}