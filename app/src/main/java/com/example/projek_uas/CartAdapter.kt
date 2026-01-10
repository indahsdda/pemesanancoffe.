package com.example.projek_uas

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.projek_uas.databinding.ItemCartBinding

class CartAdapter(
    // Kita hapus parameter 'list' di constructor agar adapter langsung ambil dari Manager
    private val onUpdate: () -> Unit
) : RecyclerView.Adapter<CartAdapter.ViewHolder>() {

    // Ambil referensi data langsung dari CartManager agar selalu sinkron
    private val listItems get() = CartManager.getKeranjang()

    class ViewHolder(val binding: ItemCartBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val b = ItemCartBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(b)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        // Gunakan listItems (data dari Manager)
        val p = listItems[holder.adapterPosition]

        holder.binding.apply {
            tvNamaProduk.text = p.nama
            tvHarga.text = "Rp ${p.harga}"
            tvJumlah.text = p.jumlah.toString()

            btnTambah.setOnClickListener {
                val pos = holder.adapterPosition
                if (pos != RecyclerView.NO_POSITION) {
                    listItems[pos].jumlah++
                    notifyItemChanged(pos)
                    onUpdate()
                }
            }

            btnKurang.setOnClickListener {
                val pos = holder.adapterPosition
                if (pos != RecyclerView.NO_POSITION && listItems[pos].jumlah > 1) {
                    listItems[pos].jumlah--
                    notifyItemChanged(pos)
                    onUpdate()
                }
            }

            btnHapus.setOnClickListener {
                val pos = holder.adapterPosition
                if (pos != RecyclerView.NO_POSITION) {
                    // 1. Ambil data spesifik yang akan dihapus
                    val itemDihapus = listItems[pos]

                    // 2. Hapus dari CartManager
                    CartManager.hapusDariKeranjang(itemDihapus)

                    // 3. Beri tahu RecyclerView bahwa 1 item dihapus
                    notifyItemRemoved(pos)

                    // 4. Update index item lainnya agar tidak error
                    notifyItemRangeChanged(pos, listItems.size)

                    // 5. Update total harga di Activity
                    onUpdate()
                }
            }
        }
    }

    override fun getItemCount() = listItems.size
}