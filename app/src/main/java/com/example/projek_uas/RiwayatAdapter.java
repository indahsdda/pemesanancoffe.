package com.example.projek_uas;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class RiwayatAdapter extends RecyclerView.Adapter<RiwayatAdapter.ViewHolder> {
    private List<Produk> listRiwayat;
    private OnDeleteClickListener listener; // Listener untuk hapus

    // Interface untuk menghubungkan Java ke Kotlin
    public interface OnDeleteClickListener {
        void onDeleteClick(String orderId);
    }

    // Constructor sekarang menerima 2 argumen agar RiwayatActivity tidak error
    public RiwayatAdapter(List<Produk> listRiwayat, OnDeleteClickListener listener) {
        this.listRiwayat = listRiwayat;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_riwayat, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Produk p = listRiwayat.get(position);
        holder.tvNama.setText(p.getNama());
        holder.tvHarga.setText("Rp " + p.getHarga());
        holder.tvTanggal.setText(p.getTanggal());
        holder.imgProduk.setImageResource(p.getGambarRes());

        // Tombol hapus memanggil listener
        holder.btnHapus.setOnClickListener(v -> {
            if (listener != null) {
                listener.onDeleteClick(p.getId()); // Mengirim ID untuk dihapus di Firebase
            }
        });
    }

    @Override
    public int getItemCount() { return listRiwayat.size(); }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvNama, tvHarga, tvTanggal;
        ImageView imgProduk;
        ImageButton btnHapus;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvNama = itemView.findViewById(R.id.tvNamaProdukRiwayat);
            tvHarga = itemView.findViewById(R.id.tvHargaTotalRiwayat);
            tvTanggal = itemView.findViewById(R.id.tvTanggalRiwayat);
            imgProduk = itemView.findViewById(R.id.imgProdukRiwayat);
            btnHapus = itemView.findViewById(R.id.btnHapusRiwayat);
        }
    }
}