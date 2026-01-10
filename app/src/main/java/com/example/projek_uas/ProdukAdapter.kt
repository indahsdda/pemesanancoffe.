package com.example.projek_uas

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.projek_uas.databinding.ItemProdukBinding
import com.google.firebase.database.FirebaseDatabase

class ProdukAdapter(private val list: List<Produk>, private val onAdd: (Produk) -> Unit) :
    RecyclerView.Adapter<ProdukAdapter.ViewHolder>() {

    class ViewHolder(val binding: ItemProdukBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val b = ItemProdukBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(b)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val p = list[position]
        holder.binding.tvNamaProduk.text = p.nama
        holder.binding.tvHargaProduk.text = "Rp ${p.harga}"
        holder.binding.imgProduk.setImageResource(p.gambarRes)

        holder.binding.btnTambah.setOnClickListener {
            // 1. Jalankan dulu fungsi onAdd agar p.jumlah bertambah di aplikasi
            onAdd(p)

            // 2. Baru simpan ke Firebase dengan nilai p.jumlah yang sudah terbaru
            val database = FirebaseDatabase.getInstance().getReference("pesanan")
            val idPesanan = database.push().key

            val dataPesanan = mapOf(
                "id_produk" to p.id,
                "nama" to p.nama,
                "harga" to p.harga,
                "gambarRes" to p.gambarRes,
                "kategori" to p.kategori,
                "jumlah" to p.jumlah // Sekarang jumlahnya sudah benar (misal: 1, 2, dst)
            )

            if (idPesanan != null) {
                database.child(idPesanan).setValue(dataPesanan)
                    .addOnSuccessListener {
                        // Opsional: beri pesan kecil
                        // Toast.makeText(holder.itemView.context, "${p.nama} ditambahkan", Toast.LENGTH_SHORT).show()
                    }
            }
        }
    }

    override fun getItemCount() = list.size
}