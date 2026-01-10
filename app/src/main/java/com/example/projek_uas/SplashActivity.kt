package com.example.projek_uas

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity

class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Baris ini akan mencari file activity_splash.xml di folder res/layout
        setContentView(R.layout.activity_splash)

        // Menggunakan Looper agar Handler tidak dianggap jadul (deprecated)
        Handler(Looper.getMainLooper()).postDelayed({
            // Berpindah ke LoginActivity
            val intent = Intent(this@SplashActivity, LoginActivity::class.java)
            startActivity(intent)
            finish() // Agar user tidak bisa kembali ke splash screen
        }, 3000) // Delay 3 detik
    }
}