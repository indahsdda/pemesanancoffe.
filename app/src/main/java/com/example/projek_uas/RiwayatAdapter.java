package com.example.projek_uas;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.List;

public class RiwayatAdapter extends RecyclerView.Adapter<RiwayatAdapter.ViewHolder> {
    private List<Produk> listRiwayat;

    public RiwayatAdapter(List<Produk> listRiwayat) {
        this.listRiwayat = listRiwayat;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Menghubungkan ke layout item_riwayat.xml
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_riwayat, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Produk p = listRiwayat.get(position);

        // Menampilkan data teks
        holder.tvNama.setText(p.getNama());
        holder.tvHarga.setText("Rp " + p.getHarga());

        // Menampilkan gambar secara dinamis sesuai pesanan
        holder.imgProduk.setImageResource(p.getGambarRes());

        // LOGIKA HAPUS RIWAYAT PERMANEN
        holder.btnHapus.setOnClickListener(v -> {
            DatabaseReference db = FirebaseDatabase.getInstance().getReference("riwayat");

            // Mengambil data satu kali untuk mencari mana yang mau dihapus
            db.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    // Iterasi folder riwayat -> ID_Acak
                    for (DataSnapshot grupSnapshot : snapshot.getChildren()) {

                        // Iterasi isi di dalam ID_Acak tersebut
                        for (DataSnapshot produkSnapshot : grupSnapshot.getChildren()) {
                            String namaDiFB = produkSnapshot.child("nama").getValue(String.class);

                            // Jika nama di Firebase cocok dengan nama produk yang diklik
                            if (namaDiFB != null && namaDiFB.equals(p.getNama())) {

                                // Hapus seluruh grup pesanan tersebut di Firebase
                                grupSnapshot.getRef().removeValue().addOnSuccessListener(aVoid -> {
                                    int currentPos = holder.getAdapterPosition();
                                    if (currentPos != RecyclerView.NO_POSITION && currentPos < listRiwayat.size()) {
                                        // Hapus dari list di aplikasi agar langsung hilang dari layar
                                        listRiwayat.remove(currentPos);
                                        notifyItemRemoved(currentPos);
                                        Toast.makeText(v.getContext(), "Riwayat terhapus permanen", Toast.LENGTH_SHORT).show();
                                    }
                                });
                                return; // Keluar dari loop setelah data ditemukan dan dihapus
                            }
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Toast.makeText(v.getContext(), "Gagal menghapus: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        });
    }

    @Override
    public int getItemCount() {
        return listRiwayat.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvNama, tvHarga;
        ImageView imgProduk;
        ImageButton btnHapus;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvNama = itemView.findViewById(R.id.tvNamaProdukRiwayat);
            tvHarga = itemView.findViewById(R.id.tvHargaTotalRiwayat);
            imgProduk = itemView.findViewById(R.id.imgProdukRiwayat);
            btnHapus = itemView.findViewById(R.id.btnHapusRiwayat);
        }
    }
}