package com.example.projek_uas

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import com.example.projek_uas.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var listProduk: List<Produk>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        listProduk = listOf(
            Produk(1, "Biji Kopi Arabica 1Kg", 120000, R.drawable.biji_kopi_arabica_1kg, "Biji Kopi"),
            Produk(2, "Biji Kopi Blend 1Kg", 110000, R.drawable.biji_kopi_blend_1kg, "Biji Kopi"),
            Produk(3, "Caramel Macchiato", 28000, R.drawable.caramel_machiato, "Minuman"),
            Produk(4, "Coklat Bubuk", 45000, R.drawable.coklat_bubuk, "Bubuk Coklat"),
            Produk(5, "Creamer", 30000, R.drawable.creamer, "Creamer"),
            Produk(6, "Gula Aren Cair", 25000, R.drawable.gula_aren_cair, "Gula")
        )

        setupRecyclerView(listProduk)

        // FILTER KATEGORI
        binding.btnAll.setOnClickListener { setupRecyclerView(listProduk) }
        binding.btnBijiKopi.setOnClickListener { setupRecyclerView(listProduk.filter { it.kategori == "Biji Kopi" }) }
        binding.btnCreamer.setOnClickListener { setupRecyclerView(listProduk.filter { it.kategori == "Creamer" }) }
        binding.btnBubukCoklat.setOnClickListener { setupRecyclerView(listProduk.filter { it.kategori == "Bubuk Coklat" }) }
        binding.btnMinuman.setOnClickListener { setupRecyclerView(listProduk.filter { it.kategori == "Minuman" }) }
        binding.btnGula.setOnClickListener { setupRecyclerView(listProduk.filter { it.kategori == "Gula" }) }

        // BOTTOM NAVIGATION
        binding.bottomNavigation.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.menu_home -> {
                    setupRecyclerView(listProduk)
                    true
                }
                R.id.menu_cart -> {
                    startActivity(Intent(this, CartActivity::class.java))
                    true
                }
                R.id.menu_pesan -> {
                    val nomorWa = "628123456789"
                    val pesan = "Halo Sekian Kopi, saya mau tanya stok produk."
                    val url = "https://api.whatsapp.com/send?phone=$nomorWa&text=${Uri.encode(pesan)}"
                    startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(url)))
                    true
                }
                R.id.menu_assignment -> {
                    // PINDAH KE HALAMAN RIWAYAT (Halaman yang menampilkan data dari Firebase)
                    startActivity(Intent(this, RiwayatActivity::class.java))
                    true
                }
                else -> false
            }
        }
    }

    private fun setupRecyclerView(data: List<Produk>) {
        binding.rvProduk.layoutManager = GridLayoutManager(this, 2)
        binding.rvProduk.adapter = ProdukAdapter(data) { produk ->
            CartManager.tambahKeKeranjang(produk)
            Toast.makeText(this, "${produk.nama} ditambah ke keranjang", Toast.LENGTH_SHORT).show()
        }
    }
}