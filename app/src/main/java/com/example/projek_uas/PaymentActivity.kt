package com.example.projek_uas

import android.app.AlertDialog
import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.projek_uas.databinding.ActivityPaymentBinding
import com.google.firebase.database.FirebaseDatabase

class PaymentActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPaymentBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPaymentBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Logika menyembunyikan kartu VA jika pilih COD
        binding.rgMetode.setOnCheckedChangeListener { _, checkedId ->
            if (checkedId == R.id.rbCOD) {
                binding.cardVA.visibility = View.GONE
            } else {
                binding.cardVA.visibility = View.VISIBLE
            }
        }

        binding.btnBayar.setOnClickListener {
            if (binding.rbTransfer.isChecked) {
                prosesMbanking()
            } else {
                prosesCOD()
            }
        }
    }

    private fun prosesMbanking() {
        val pd = ProgressDialog(this)
        pd.setMessage("Memverifikasi Transfer M-Banking...")
        pd.setCancelable(false)
        pd.show()

        // Simulasi loading 2 detik
        Handler(Looper.getMainLooper()).postDelayed({
            pd.dismiss()
            tampilkanDialogSukses("Pembayaran M-Banking Berhasil!")
        }, 2000)
    }

    private fun prosesCOD() {
        val pd = ProgressDialog(this)
        pd.setMessage("Memproses Pesanan COD...")
        pd.setCancelable(false)
        pd.show()

        // Simulasi loading 1.5 detik
        Handler(Looper.getMainLooper()).postDelayed({
            pd.dismiss()
            tampilkanDialogSukses("Pesanan COD Berhasil Dibuat!")
        }, 1500)
    }

    private fun tampilkanDialogSukses(pesan: String) {
        AlertDialog.Builder(this)
            .setTitle("Sukses")
            .setMessage("$pesan\nKlik OK untuk melihat riwayat pesanan.")
            .setPositiveButton("OK") { _, _ ->
                // INI KUNCINYA: Memanggil fungsi pindah data setelah klik OK
                pindahkanKeRiwayat()
            }
            .setCancelable(false)
            .show()
    }

    private fun pindahkanKeRiwayat() {
        val dbPesanan = FirebaseDatabase.getInstance().getReference("pesanan")
        val dbRiwayat = FirebaseDatabase.getInstance().getReference("riwayat")

        dbPesanan.get().addOnSuccessListener { snapshot ->
            if (snapshot.exists()) {
                val key = dbRiwayat.push().key
                if (key != null) {
                    // 1. Simpan data ke Riwayat
                    dbRiwayat.child(key).setValue(snapshot.value).addOnSuccessListener {

                        // 2. HAPUS data dari folder Pesanan (agar keranjang kosong)
                        dbPesanan.removeValue().addOnSuccessListener {
                            Toast.makeText(this, "Berhasil masuk ke riwayat!", Toast.LENGTH_SHORT).show()

                            // 3. Pindah ke halaman RiwayatActivity
                            val intent = Intent(this, RiwayatActivity::class.java)
                            startActivity(intent)
                            finish()
                        }
                    }
                }
            } else {
                Toast.makeText(this, "Keranjang kosong, tidak ada data dipindah.", Toast.LENGTH_SHORT).show()
            }
        }.addOnFailureListener {
            Toast.makeText(this, "Gagal terhubung ke Firebase", Toast.LENGTH_SHORT).show()
        }
    }
}