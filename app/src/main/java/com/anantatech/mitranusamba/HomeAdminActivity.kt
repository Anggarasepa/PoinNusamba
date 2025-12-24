package com.anantatech.mitranusamba

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class HomeAdminActivity : AppCompatActivity() {

    private lateinit var tvWelcomeAdmin: TextView
    private lateinit var btnPoin: Button
    private lateinit var btnBerita: Button
    private lateinit var btnLogout: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home_admin)

        tvWelcomeAdmin = findViewById(R.id.tvWelcomeAdmin)
        btnPoin = findViewById(R.id.btnPoin)
        btnBerita = findViewById(R.id.btnBerita)
        btnLogout = findViewById(R.id.btnLogout)

        val userName = intent.getStringExtra("userName") ?: "Admin"
        tvWelcomeAdmin.text = "Selamat Datang Admin, $userName"

        btnPoin.setOnClickListener {
            val intent = Intent(this, AdminPoinActivity::class.java)
            startActivity(intent)
        }

        btnBerita.setOnClickListener {
            // Sementara diarahkan ke halaman belum tersedia
            // Bisa diganti ke AdminBeritaActivity nanti
            val intent = Intent(this, BeritaActivity::class.java)
            startActivity(intent)
        }

        btnLogout.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
        }
    }
}
