package com.example.projek_uas

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.projek_uas.databinding.ActivityPaymentBinding
import com.google.firebase.database.FirebaseDatabase
import java.text.SimpleDateFormat
import java.util.*

class PaymentActivity : AppCompatActivity() {
    private lateinit var binding: ActivityPaymentBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPaymentBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.tvNamaPenerima.text = intent.getStringExtra("NAMA_USER")
        binding.tvPhonePenerima.text = intent.getStringExtra("PHONE_USER")
        binding.tvAlamatPenerima.text = intent.getStringExtra("ALAMAT_USER")

        val listBank = arrayOf("BCA", "Mandiri", "BNI", "BRI")
        binding.spinnerBank.adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, listBank)
        binding.spinnerBank.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, pos: Int, p3: Long) {
                val bank = listBank[pos]
                binding.tvLabelVA.text = "Nomor Virtual Account $bank"
                binding.tvNomorVA.text = when(bank) {
                    "BCA" -> "6080 7749 99"
                    "Mandiri" -> "8890 1234 55"
                    "BNI" -> "4450 9876 11"
                    else -> "1120 5566 88"
                }
            }
            override fun onNothingSelected(p0: AdapterView<*>?) {}
        }

        binding.rgMetode.setOnCheckedChangeListener { _, id ->
            binding.cardVA.visibility = if (id == R.id.rbCOD) View.GONE else View.VISIBLE
        }

        binding.btnBayar.setOnClickListener { showSuccessDialog() }
    }

    private fun showSuccessDialog() {
        AlertDialog.Builder(this).apply {
            setTitle("Sukses")
            setMessage("Pesanan Berhasil!")
            setCancelable(false)
            setPositiveButton("OK") { _, _ ->
                val database = FirebaseDatabase.getInstance().getReference("RiwayatPesanan")
                val orderId = database.push().key ?: System.currentTimeMillis().toString()

                val sdf = SimpleDateFormat("dd MMM yyyy", Locale.getDefault())
                val tanggalSkrg = sdf.format(Date())

                val listBelanja = CartManager.getKeranjang()
                for (p in listBelanja) { p.tanggal = tanggalSkrg }

                database.child(orderId).setValue(listBelanja).addOnSuccessListener {
                    CartManager.getKeranjang().clear()
                    startActivity(Intent(this@PaymentActivity, RiwayatActivity::class.java))
                    finish()
                }
            }
            show()
        }
    }
}