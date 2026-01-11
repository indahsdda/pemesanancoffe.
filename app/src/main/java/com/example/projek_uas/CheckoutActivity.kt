package com.example.projek_uas

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.projek_uas.databinding.ActivityCheckoutBinding

class CheckoutActivity : AppCompatActivity() {
    private lateinit var binding: ActivityCheckoutBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCheckoutBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnLanjutPembayaran.setOnClickListener {
            val nama = binding.etNama.text.toString()
            val phone = binding.etPhone.text.toString()
            val alamat = binding.etAlamat.text.toString()

            if (nama.isEmpty() || phone.isEmpty() || alamat.isEmpty()) {
                Toast.makeText(this, "Harap isi semua data!", Toast.LENGTH_SHORT).show()
            } else {
                val intent = Intent(this, PaymentActivity::class.java)
                // Mengirim data input ke PaymentActivity
                intent.putExtra("NAMA_USER", nama)
                intent.putExtra("PHONE_USER", phone)
                intent.putExtra("ALAMAT_USER", alamat)
                startActivity(intent)
            }
        }
    }
}