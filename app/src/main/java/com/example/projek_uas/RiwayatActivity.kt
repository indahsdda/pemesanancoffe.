package com.example.projek_uas

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.projek_uas.databinding.ActivityRiwayatBinding
import com.google.firebase.database.*

class RiwayatActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRiwayatBinding
    private lateinit var dbRef: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRiwayatBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.rvRiwayat.layoutManager = LinearLayoutManager(this)
        dbRef = FirebaseDatabase.getInstance().getReference("RiwayatPesanan")

        loadData()
    }

    private fun loadData() {
        dbRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val listData = mutableListOf<Produk>()
                for (order in snapshot.children) {
                    // Simpan Key Order (ID di Firebase) ke dalam produk agar bisa dihapus nanti
                    val orderKey = order.key ?: ""
                    for (node in order.children) {
                        val p = node.getValue(Produk::class.java)
                        if (p != null) {
                            // Gunakan ID unik dari Firebase agar hapusnya akurat
                            val produkDenganKey = p.copy(id = orderKey)
                            listData.add(produkDenganKey)
                        }
                    }
                }

                // Pasang Adapter dengan Fungsi Hapus
                binding.rvRiwayat.adapter = RiwayatAdapter(listData) { orderId ->
                    hapusDataFirebase(orderId)
                }
            }
            override fun onCancelled(error: DatabaseError) {}
        })
    }

    private fun hapusDataFirebase(id: String) {
        // Hapus data berdasarkan ID di Firebase
        dbRef.child(id).removeValue().addOnSuccessListener {
            Toast.makeText(this, "Riwayat Berhasil Dihapus", Toast.LENGTH_SHORT).show()
        }.addOnFailureListener {
            Toast.makeText(this, "Gagal Menghapus Riwayat", Toast.LENGTH_SHORT).show()
        }
    }
}