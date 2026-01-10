package com.example.projek_uas

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.projek_uas.databinding.ActivityOtpBinding
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import java.util.concurrent.TimeUnit

class OtpActivity : AppCompatActivity() {
    private lateinit var binding: ActivityOtpBinding
    private lateinit var auth: FirebaseAuth
    private var verificationId: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOtpBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()

        // Menerima verificationId jika dikirim dari LoginActivity
        verificationId = intent.getStringExtra("verificationId")

        // Jika ID sudah ada (dikirim dari Login), langsung tampilkan tombol verifikasi
        if (verificationId != null) {
            binding.btnVerifyOtp.visibility = View.VISIBLE
        }

        // 1. Klik Tombol Kirim Kode (Jika user ingin kirim ulang atau baru input di sini)
        binding.btnSendOtp.setOnClickListener {
            val number = binding.etPhoneNumber.text.toString().trim()
            if (number.isNotEmpty()) {
                // Pastikan format +62
                val formattedNumber = if (number.startsWith("0")) "+62${number.substring(1)}"
                else if (!number.startsWith("+")) "+62$number"
                else number
                sendCode(formattedNumber)
            } else {
                Toast.makeText(this, "Masukkan nomor HP dahulu", Toast.LENGTH_SHORT).show()
            }
        }

        // 2. Klik Tombol Verifikasi (Sesuai gambar 6 digit input)
        binding.btnVerifyOtp.setOnClickListener {
            val code = binding.etOtpCode.text.toString().trim()
            if (code.length == 6 && verificationId != null) {
                val credential = PhoneAuthProvider.getCredential(verificationId!!, code)
                signIn(credential)
            } else {
                Toast.makeText(this, "Masukkan 6 digit kode OTP", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun sendCode(phoneNumber: String) {
        val options = PhoneAuthOptions.newBuilder(auth)
            .setPhoneNumber(phoneNumber)
            .setTimeout(60L, TimeUnit.SECONDS)
            .setActivity(this)
            .setCallbacks(object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                override fun onCodeSent(id: String, token: PhoneAuthProvider.ForceResendingToken) {
                    verificationId = id
                    binding.btnVerifyOtp.visibility = View.VISIBLE
                    Toast.makeText(this@OtpActivity, "Kode OTP Terkirim", Toast.LENGTH_SHORT).show()
                }
                override fun onVerificationCompleted(p0: PhoneAuthCredential) {
                    signIn(p0)
                }
                override fun onVerificationFailed(e: FirebaseException) {
                    Toast.makeText(this@OtpActivity, "Gagal: ${e.message}", Toast.LENGTH_LONG).show()
                }
            }).build()
        PhoneAuthProvider.verifyPhoneNumber(options)
    }

    private fun signIn(credential: PhoneAuthCredential) {
        auth.signInWithCredential(credential).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                Toast.makeText(this, "Login Berhasil!", Toast.LENGTH_SHORT).show()
                val intent = Intent(this, MainActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                startActivity(intent)
                finish()
            } else {
                Toast.makeText(this, "OTP Salah atau Kadaluarsa", Toast.LENGTH_SHORT).show()
            }
        }
    }
}