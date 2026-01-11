package com.example.projek_uas

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.projek_uas.databinding.ActivityCartBinding
import java.text.NumberFormat
import java.util.*

class CartActivity : AppCompatActivity() {
    private lateinit var binding: ActivityCartBinding
    private lateinit var adapter: CartAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCartBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupRecyclerView()

        updateTotalUI()

        binding.btnBayar.setOnClickListener {
            if (CartManager.getKeranjang().isNotEmpty()) {
                // Alihkan ke CheckoutActivity, bukan langsung ke PaymentActivity
                val intent = Intent(this, CheckoutActivity::class.java)
                startActivity(intent)
            } else {
                Toast.makeText(this, "Keranjang kosong!", Toast.LENGTH_SHORT).show()
            }
        }

        try {
            val idBack = resources.getIdentifier("btnBack", "id", packageName)
            if (idBack != 0) {
                findViewById<android.view.View>(idBack).setOnClickListener { finish() }
            }
        } catch (e: Exception) { }
    }

    private fun updateTotalUI() {
        val localeID = Locale("in", "ID")
        val formatRupiah = NumberFormat.getCurrencyInstance(localeID)
        // Memanggil fungsi hitungTotal() dari CartManager
        binding.tvTotalHarga.text = formatRupiah.format(CartManager.hitungTotal())
    }

    private fun setupRecyclerView() {
        binding.rvCart.layoutManager = LinearLayoutManager(this)

        adapter = CartAdapter {
            updateTotalUI()
        }

        binding.rvCart.adapter = adapter
    }

    override fun onResume() {
        super.onResume()

        adapter.notifyDataSetChanged()
        updateTotalUI()
    }
}