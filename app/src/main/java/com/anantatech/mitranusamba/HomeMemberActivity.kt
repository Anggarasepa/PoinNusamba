package com.anantatech.mitranusamba

import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.anantatech.mitranusamba.databinding.ActivityHomeMemberBinding

class HomeMemberActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHomeMemberBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeMemberBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val userName = intent.getStringExtra("userName")
        val tvWelcome = findViewById<TextView>(R.id.tvWelcome)
        tvWelcome.text = "Selamat Datang, \n$userName"

        // Klik ikon Berita
        binding.btnBerita.setOnClickListener {
            startActivity(Intent(this, BeritaActivity::class.java))
        }

        // Klik ikon Poin
        binding.btnPoin.setOnClickListener {
            startActivity(Intent(this, PoinActivity::class.java))
        }

        // Klik tombol Logout
        binding.btnLogout.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
            finish() // agar tidak bisa kembali ke home tanpa login
        }
    }
}
