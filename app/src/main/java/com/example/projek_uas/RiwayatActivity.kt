package com.example.projek_uas

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.projek_uas.databinding.ActivityRiwayatBinding
import com.google.firebase.database.*

class RiwayatActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRiwayatBinding

    // List untuk menampung data Produk dari Firebase
    private val listRiwayat = mutableListOf<Produk>()
    private lateinit var adapter: RiwayatAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRiwayatBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Inisialisasi Adapter dan RecyclerView
        adapter = RiwayatAdapter(listRiwayat)
        binding.rvRiwayat.layoutManager = LinearLayoutManager(this)
        binding.rvRiwayat.adapter = adapter

        ambilDataDariFirebase()
    }

    private fun ambilDataDariFirebase() {
        // Mengambil referensi dari folder 'riwayat'
        val database = FirebaseDatabase.getInstance().getReference("riwayat")

        database.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                listRiwayat.clear()

                // Looping setiap grup pesanan yang ada di folder riwayat
                for (riwayatSnapshot in snapshot.children) {

                    // Looping isi produk di dalam setiap riwayat
                    for (produkSnapshot in riwayatSnapshot.children) {
                        val id = produkSnapshot.child("id_produk").getValue(Int::class.java) ?: 0
                        val nama = produkSnapshot.child("nama").getValue(String::class.java) ?: ""
                        val harga = produkSnapshot.child("harga").getValue(Int::class.java) ?: 0
                        val kategori = produkSnapshot.child("kategori").getValue(String::class.java) ?: ""

                        // AMBIL ID GAMBAR DARI FIREBASE (DINAMIS)
                        // Jika data di Firebase tidak ada, baru akan menggunakan angka 0 agar tidak error
                        val gambarRes = produkSnapshot.child("gambarRes").getValue(Int::class.java) ?: 0

                        // Masukkan ke list sesuai data asli yang dibeli
                        listRiwayat.add(Produk(id, nama, harga, gambarRes, kategori))
                    }
                }

                // Memberitahu adapter bahwa data sudah berubah untuk ditampilkan ke layar
                adapter.notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@RiwayatActivity, "Gagal ambil data: ${error.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }
}