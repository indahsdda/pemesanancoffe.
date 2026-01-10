package com.example.projek_uas

object CartManager {
    private val keranjangItems = mutableListOf<Produk>()
    private val riwayatItems = mutableListOf<Produk>()

    fun getKeranjang(): MutableList<Produk> = keranjangItems
    fun getRiwayat(): MutableList<Produk> = riwayatItems

    fun tambahKeKeranjang(produk: Produk) {
        val itemAda = keranjangItems.find { it.id == produk.id }
        if (itemAda != null) {
            itemAda.jumlah++
        } else {
            // Gunakan .copy() jika Produk adalah data class agar tidak merujuk ke objek yang sama
            keranjangItems.add(produk.copy(jumlah = 1))
        }
    }

    fun hapusDariKeranjang(produk: Produk) {
        // Menghapus berdasarkan ID unik produk
        keranjangItems.removeAll { it.id == produk.id }
    }

    fun hitungTotal(): Int = keranjangItems.sumOf { it.harga * it.jumlah }

    fun bayarDanPindahKeRiwayat() {
        if (keranjangItems.isNotEmpty()) {
            riwayatItems.addAll(keranjangItems.map { it.copy() })
            keranjangItems.clear()
        }
    }
}